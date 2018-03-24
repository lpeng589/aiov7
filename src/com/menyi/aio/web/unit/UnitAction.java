package com.menyi.aio.web.unit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.*;
import org.apache.struts.action.*;

/**
 * <p>Title: 单位管理控制类</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class UnitAction extends MgtBaseAction {

    UnitMgt mgt = new UnitMgt();

    public UnitAction() {
    }

    /**
     * exe 控制器入口函数
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.huawei.sms.web.util.BaseAction method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        //跟据不同操作类型分配给不同函数处理
        int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_ADD_PREPARE:
            forward = addPrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_ADD:
            forward = add(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE_PREPARE:
            forward = updatePrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_UPDATE:
            forward = update(mapping, form, request, response);
            break;
        case OperationConst.OP_DELETE:
            forward = delete(mapping, form, request, response);
            break;
        case OperationConst.OP_QUERY:
            forward = query(mapping, form, request, response);
            break;
        case OperationConst.OP_DETAIL:
            forward = detail(mapping, form, request, response);
            break;
        default:
            forward = query(mapping, form, request, response);
        }
        return forward;
    }

    /**
     * 添加前的准备
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
        Exception {
        request.removeAttribute("operation");
        return getForward(request, mapping, "unitAdminAdd");
    }

    /**
     * 添加
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward add(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

          UnitForm myForm = (UnitForm)form;
        //执行添加操作

        Result rs = mgt.add(myForm.getId(),myForm.getUnitName(),myForm.getRemark(),myForm.getCreateBy());

        ActionForward forward = getForward(request, mapping, "alert");
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //添加成功
            EchoMessage.success().add(getMessage(
                request, "common.msg.addSuccess"))
                .setBackUrl("/UnitQueryAction.do").
                setAlertRequest(request);
        }else {
            //添加失败
            EchoMessage.error().add(getMessage(
                request, "common.msg.addFailture")).
                setAlertRequest(request);
        }
        return forward;
    }

    /**
     * 修改前的准备
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward updatePrepare(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws
        Exception {
        ActionForward forward = null;
        String nstr = request.getParameter("keyId");
        if (nstr != null && nstr.length() != 0) {
            long id = 0L;
            try {
                id = Integer.parseInt(nstr);
            } catch (NumberFormatException ex) {
                log.error(
                    "----------UnitAction.updatePrepare()------Parse unitId Error");
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
                forward = getForward(request, mapping, "message");
            }
            //执行查询前的加载
            Result rs = mgt.detail(id);
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //加载成功
                request.setAttribute("result", rs.retVal);
                forward = getForward(request, mapping, "unitAdminUpdate");
            }  else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
                //记录不存在错误
                EchoMessage.error().add(getMessage(
                    request, "common.error.nodata")).setRequest(request);
            } else {
                //加载失败
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
                forward = getForward(request, mapping, "message");
            }
        }
        return forward;
    }

    /**
     * 修改
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward update(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {


        UnitForm myForm = (UnitForm) form;
        ActionForward forward = null;
        //执行修改
        Result rs = mgt.update(myForm.getId(),
                               myForm.getUnitName(),
                               myForm.getRemark(),
                               myForm.getLstUpdateBy());

        forward = getForward(request, mapping, "alert");
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //修改成功
            EchoMessage.success().add(getMessage(
                request, "common.msg.updateSuccess")).
                setAlertRequest(request);
        }  else {
            //修改失败
            EchoMessage.error().add(getMessage(
                request, "common.msg.updateErro")).setAlertRequest(
                    request);
        }
        return forward;
    }

    /**
     * 删除
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward delete(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {
        ActionForward forward = null;
        String nstr[] = request.getParameterValues("keyId");
        if (nstr != null && nstr.length != 0) {
            long[] lg = new long[nstr.length];
            for (int i = 0; i < nstr.length; i++) {
                lg[i] = Long.parseLong(nstr[i]);
            }
            Result rs = mgt.delete(lg);

            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //删除成功
                request.setAttribute("result", rs.retVal);
                forward = query(mapping, form, request, response);
            } else {
                //删除失败
                EchoMessage.error().add(getMessage(request, "common.msg.delError")).
                    setRequest(request);
                forward = getForward(request, mapping, "message");
            }
        }
        return forward;

    }

    /**
     * 查询
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward query(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
        Exception {
        UnitSearchForm searchForm = (UnitSearchForm) form;
        if (searchForm != null) {
            //执行查询

            Result rs = mgt.query(searchForm.getName(),
                                        searchForm.getPageNo(),
                                        searchForm.getPageSize());

            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //查询成功
                request.setAttribute("result", rs.retVal);
                request.setAttribute("pageBar", pageBar(rs, request));
            } else {
                //查询失败
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
                return getForward(request, mapping, "message");
            }
        }
        return getForward(request, mapping, "unitAdminList");
    }

    /**
     * 明细
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward detail(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
        Exception {

        String nstr = request.getParameter("keyId");
        if (nstr != null && nstr.length() != 0) {
            long unitId = Integer.parseInt(nstr);
            //执行加载明细
            Result rs = mgt.detail(unitId);
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //加载成功
                request.setAttribute("result", rs.retVal);
            }else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
                //记录不存在错误
                EchoMessage.error().add(getMessage(
                    request, "common.error.nodata")).setRequest(request);
                return getForward(request, mapping, "message");
            } else {
                //加载失败
                EchoMessage.error().add(getMessage(request, "common.msg.error")).
                    setRequest(request);
                return getForward(request, mapping, "message");
            }
        }
        return getForward(request, mapping, "unitAdminDetail");
    }
}
