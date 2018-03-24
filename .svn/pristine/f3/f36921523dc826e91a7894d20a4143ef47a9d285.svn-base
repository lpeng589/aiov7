package com.menyi.aio.web.bol88;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

/**
 *
 * <p>Title:登陆 </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class AIOBOL88Action extends MgtBaseAction {
    AIOBOL88Mgt aioBol88Mgt = new AIOBOL88Mgt();
    public AIOBOL88Action() {

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
            
        default: //默认返回首页
            forward = bol88(mapping, form, request, response);
        }
        return forward;
    }

    protected ActionForward add(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
            Exception {
        //查找tblbol88set表，如果没有数据，提示注册
        String aioClose = getParameter("aioClose",request);
        if(aioClose != null){
            String flag = getParameter("flag",request);
            if(flag == null || flag.length()==0){
                flag = "1";
            }
            aioBol88Mgt.closeBol88(flag);
            String msg = getMessage(request, "common.msg.openSucceed");
            if(flag.equals("1")){
                msg = getMessage(request, "common.msg.closeSucceed");
            }
            EchoMessage.success().add(msg).setBackUrl("/AIOBOL88Action.do?winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);

        }else{
            String userName = getParameter("userName", request);
            String password = getParameter("password", request);
            password = toMD5(password);
            aioBol88Mgt.updateBol88(userName, password);

            EchoMessage.success().add(getMessage(request, "common.msg.bindSucceed")).setBackUrl("/AIOBOL88Action.do?winCurIndex="+request.getParameter("winCurIndex")).
                    setAlertRequest(request);
        }
        return getForward(request,mapping,"alert");
    }

    public static String toMD5(String arg) {
        try {
            StringBuffer sb = new StringBuffer();
            MessageDigest code;

            code = MessageDigest.getInstance("MD5");

            code.update(arg.getBytes());
            byte[] bs = code.digest();
            for (int i = 0; i < bs.length; i++) {
                int v = bs[i] & 0xff;
                if (v < 16) {
                    sb.append(0);
                } else {
                    sb.append(Integer.toHexString(v));
                }
            }
            arg = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return arg;
    }


    protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
            Exception {
        //查找tblbol88set表，如果没有数据，提示注册
        Result rs = aioBol88Mgt.queryBol88();
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            //无数据，显示，注册
            request.setAttribute("result",rs.getRetVal());
        } else {
            //有数据，显示，bol88页面
            request.setAttribute("result",rs.getRetVal());
        }
        return getForward(request,mapping,"regInfo");
    }
    
 


    /**
     * 登陆系统
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward bol88(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
        Exception {
        //查找tblbol88set表，如果没有数据，提示注册
        Result rs = aioBol88Mgt.queryBol88();
        if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
            //无数据，显示，注册
            return getForward(request,mapping,"regPage");
        }else{
            //有数据，显示，bol88页面
            request.setAttribute("result",rs.getRetVal());
            return getForward(request,mapping,"bol88");
        }
    }
}
