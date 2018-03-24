package com.menyi.aio.web.upload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import java.util.ArrayList;

/**
 * <p>Title: 文件上传</p>
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
public class UploadAction extends BaseAction {

    protected ActionForward doAuth(HttpServletRequest req,
                                   ActionMapping mapping) {
        return null;
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
        case OperationConst.OP_DELETE:
            forward = delete(mapping, form, request, response);
            break;
        case OperationConst.OP_DELETE_PREPARE:
            forward = deletePrepare(mapping, form, request, response);
            break;

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
        String type = request.getParameter("type");
        request.setAttribute("type", type);
        return getForward(request, mapping, "upload");
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

        UploadForm myForm = (UploadForm) form;
        //执行添加操作

        FormFile formFile = myForm.getFileName();
        String fileName = null;
        if(formFile != null){
            fileName = FileHandler.writeTemp(formFile.getFileName(),
                    formFile.getFileData());
        }
        ActionForward forward = getForward(request, mapping, "alert");
        if (fileName != null) {
            //上传成功
            ArrayList list ;
            if("PIC".equals(myForm.getType())){
                Object o = request.getSession().getAttribute("PIC_UPLOAD");
                if(o == null){
                    list = new ArrayList();
                    request.getSession().setAttribute("PIC_UPLOAD",list);
                }else{
                    list = (ArrayList)o;
                }
            }else{
                Object o = request.getSession().getAttribute("AFFIX_UPLOAD");
                if(o == null){
                    list = new ArrayList();
                    request.getSession().setAttribute("AFFIX_UPLOAD",list);
                }else{
                    list = (ArrayList)o;
                }
            }
            list.add(fileName);
			
            request.setAttribute("fileName",fileName+":"+formFile.getFileName());

            return getForward(request, mapping, "uploadSuccess");
        } else {
            //上传失败
            EchoMessage.error().add(getMessage(
                    request, "common.msg.uploadFailure")).
                    setAlertRequest(request);
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
    protected ActionForward deletePrepare(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
            Exception {
        request.removeAttribute("operation");
        String fileName = request.getParameter("fileName");
        String tempFile = request.getParameter("tempFile"); //是否是临时文件
        String tableName = request.getParameter("tableName"); //删除正式文件时要提供表名
        String type = request.getParameter("type"); //删除正式文件时要提供类型
        
        request.setAttribute("fileName",fileName);
        request.setAttribute("tempFile",tempFile);
        request.setAttribute("tableName",tableName);
        request.setAttribute("type",type);

        return getForward(request, mapping, "delete");
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
        String fileName = request.getParameter("fileName");
        String tempFile = request.getParameter("tempFile"); //是否是临时文件
        String tableName = request.getParameter("tableName"); //删除正式文件时要提供表名
        String type = request.getParameter("type"); //删除正式文件时要提供类型
        fileName = GlobalsTool.toChinseChar(fileName);
        
        if (fileName != null && fileName.length() != 0) {
            boolean rs = false;
            if("true".equals(tempFile)){
                //删除临时文件
                rs = FileHandler.deleteTemp(fileName);
            }else{
                rs = true;
                //这里不能真实删除正式文件，而在在提交时判断文件被删，才能正式删除
                //否则如果真实删除了，而用户未提交请况下，导致数据不一致
//                rs = FileHandler.delete(tableName,
//                                        "PIC".equals(type) ? FileHandler.TYPE_PIC :
//                                        FileHandler.TYPE_AFFIX, fileName);
            }
            if (rs) {
                //删除成功
                //临时文件要删除内存数据
                if("true".equals(tempFile)){
                    ArrayList list;
                    if ("PIC".equals(type)) {
                        Object o = request.getSession().getAttribute(
                                "PIC_UPLOAD");
                        if (o == null) {
                            list = new ArrayList();
                            request.getSession().setAttribute("PIC_UPLOAD",
                                    list);
                        } else {
                            list = (ArrayList) o;
                        }
                    } else {
                        Object o = request.getSession().getAttribute(
                                "AFFIX_UPLOAD");
                        if (o == null) {
                            list = new ArrayList();
                            request.getSession().setAttribute("AFFIX_UPLOAD",
                                    list);
                        } else {
                            list = (ArrayList) o;
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if(fileName.equals(list.get(i).toString())){
                            list.remove(i);
                            break;
                        }
                    }
                }
//                request.setAttribute("fileName",fileName);
//                return getForward(request, mapping, "deleteSuccess");

            } else {
                //删除失败
//                EchoMessage.error().add(getMessage(request,
//                        "common.msg.delError")).
//                        setRequest(request);
//                forward = getForward(request, mapping, "message");
            }
            request.setAttribute("fileName",fileName);
                return getForward(request, mapping, "deleteSuccess");
        }
        return forward;

    }
}
