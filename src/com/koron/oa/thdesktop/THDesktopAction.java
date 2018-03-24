package com.koron.oa.thdesktop;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.MyDeskBean;
import com.koron.oa.oaCollection.OACollectionMgt;
import com.koron.oa.util.AttentionMgt;
import com.koron.oa.workflow.OAMyWorkFlowForm;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.TextBoxUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * <p>Title:OA工作台</p> 
 * <p>Description: </p>
 *
 * @Date:Jul 6, 2012
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class THDesktopAction extends MgtBaseAction {
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();	
	
	THDesktopMgt mgt = new THDesktopMgt() ;
    /**
     * Action执行函数
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.menyi.web.util.BaseAction method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
              HttpServletRequest request,HttpServletResponse response) throws Exception {
        
    	int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
	        //增加前执行方法
	        case OperationConst.OP_QUERY:
	        	String type = getParameter("type", request);
	        	if("order".equals(type)){
	        		forward = queryOrder(mapping, form, request, response);
	        	}else if("mail".equals(type)){
	        		//查询没读邮件
	        		forward = queryNoread(mapping, form, request, response);
	        	}else{
	        		forward = queryAll(mapping, form, request, response);
	        	}             
	            break;
	        //修改配置
	        case OperationConst.OP_DRAFT:
	            forward = updateOrder(mapping, form, request, response);
	            break;
	        case OperationConst.OP_OA_VIEW_SINLE:
	            forward = querySingle(mapping, form, request, response);
	            break;	       
	        default:
	        	forward = queryAll(mapping, form, request, response);
        }
        return forward;
    }
    
    private ActionForward queryNoread(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
    	Result result = mgt.selectOutByUser(this.getLoginBean(request).getId());
    	ArrayList results = (ArrayList)result.retVal;
    	int msg=0; 
    	String stat="";
    	if(results !=null && results.size()>0){   	
    		for (Object obj : results) {
				stat += GlobalsTool.get(obj,0).toString()+":";
			}
    	}
    	Result rs = mgt.NoreadMail(stat,this.getLoginBean(request).getId());
    	ArrayList rss = (ArrayList)rs.retVal;
    	if(rss !=null && rss.size()>0){
    		for (Object obj : rss) {
				msg += Integer.parseInt(GlobalsTool.get(obj,2).toString());
			}
    	}
    	request.setAttribute("msg", msg);
		return getForward(request, mapping, "blank");
	}

	private ActionForward queryOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
    	String pot = getParameter("pot", request);
    	String userId = getLoginBean(request).getId();    	
 		String msg = mgt.queryOrder(userId,pot);   	
    	request.setAttribute("msg", msg);
    	return getForward(request, mapping, "blank");
	}

	private ActionForward updateOrder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String setting = getParameter("setting", request);
		String msg = "3";
		if(setting != null && setting.length()>0){
			/*setting 中 包含 位置，排序，和id*/
			Result rs = mgt.updateOrder(setting,getLoginBean(request).getId());	
			if(rs.retCode == ErrorCanst.DEFAULT_FAILURE){
				//失败
				msg = "0";
			}
		}
    	request.setAttribute("msg", msg);
		return getForward(request, mapping, "blank");
	}
    /**
     * 单个模块内容显示
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward querySingle(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) {
        
    	String type = getParameter("type", request) ;
        String deskId = getParameter("deskId", request) ;
        Integer rowCount = getParameterInt("rowCount", request) ;
        if(rowCount==null || rowCount==0){
        	rowCount = 7 ;
        }
    	LoginBean login = getLoginBean(request) ;
        if(type == null){
        	return getForward(request, mapping, "blank") ;
        }       
        if("workflow".equals(type)){
        	OAMyWorkFlowMgt flowMgt = new OAMyWorkFlowMgt();
    		OAMyWorkFlowForm flowForm = new OAMyWorkFlowForm();
    		flowForm.setPageSize(rowCount);
    		flowForm.setPageNo(1);
    		flowForm.setFlowBelong("other"); 
    		Result rs = flowMgt.query(flowForm, login.getId(),"transing",this.getLocale(request).toString(),true);
    		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
    			request.setAttribute("workFlowList", rs.getRetVal());
    		}
    		return getForward(request, mapping, "workflow") ;
        }else if("common".equals(type)){
        	Result result = mgt.queryDesk(deskId, rowCount, login.getId()) ;
        	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){     		
        		request.setAttribute("msgList", result.retVal) ;
        	}
        	request.setAttribute("deskId", deskId);
        	return getForward(request, mapping, "common") ;
        }       
        return getForward(request, mapping, "blank") ;
    }
    
    /**
     * 跳转到工作台
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward queryAll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) {
    	return getForward(request, mapping, "body_new") ;
    }

	/**
	 * 不做权限特殊控制
	 */
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
       
		return null ;
	}

    
}
