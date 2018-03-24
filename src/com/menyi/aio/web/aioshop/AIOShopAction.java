/**
 * 
 */
package com.menyi.aio.web.aioshop;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import com.dbfactory.Result;
import com.koron.aioshop.bean.SpAlert;
import com.koron.aioshop.services.IAIOShopServices;
import com.menyi.aio.bean.AIOShopBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * <p>Title:AIOShop电子商务网站设置</p> 
 * <p>Description: </p>
 *
 * @Date:Jun 2, 2011
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class AIOShopAction extends MgtBaseAction{

	private AIOShopMgt shopMgt = new AIOShopMgt() ;
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_ADD_PREPARE:
            forward = addPrepare(mapping, form, request, response); //添加映射准备
            break ;
        case OperationConst.OP_ADD:
            forward = add(mapping, form, request, response); //添加映射准备
            break ;
        case OperationConst.OP_UPDATE_PREPARE:
        	forward = updatePrepare(mapping, form, request, response) ;
        	break ;
        case OperationConst.OP_UPDATE:
        	forward = update(mapping, form, request, response) ;
        	break ;
        default:
            forward = addPrepare(mapping, form, request, response);
        }
        return forward;
	}

	/**
	 * AIOSHOP设置前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
             HttpServletRequest request,HttpServletResponse response) throws Exception {
		 
		 Result result = shopMgt.queryAIOShop() ;
		 if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			 List<AIOShopBean> listShop = (List<AIOShopBean>) result.retVal ;
			 if(listShop.size()>0){
				 AIOShopBean shopBean = listShop.get(0) ;
				 request.setAttribute("linkAddr", shopBean.getLinkAddr()) ;
			 }
		 }
		 return getForward(request, mapping, "shopset") ;
	 }
	 
	/**
	 * AIOSHOP设置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	 protected ActionForward add(ActionMapping mapping, ActionForm form,
             HttpServletRequest request,HttpServletResponse response) throws Exception {
		 
		String linkAddr = getParameter("linkAddr", request);
		String aioUrl = linkAddr + "/services/AIOShopServices";
		HttpURLConnection conn = (HttpURLConnection) new URL(aioUrl).openConnection();
		int resCode = 0;
		try {
			resCode = conn.getResponseCode();
		} catch (Exception e) {
			resCode = -1;
		}
		String message = "启用成功！" ;
		if(200!=resCode){
			message = "启用失败，AIOSHOP地址链接错误！" ;
		}else{
			AIOShopBean shopBean = new AIOShopBean() ;
			shopBean.setLinkAddr(linkAddr) ;
			shopBean.setStatusId(0) ;
			Result result = shopMgt.addAIOShop(shopBean) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				BaseEnv.AIO_SHOP = shopBean ;
			}
		}
		
		EchoMessage.error().add(message).setBackUrl("/AIOShopAction.do?operation=6")
		   				   .setAlertRequest(request);
		return getForward(request, mapping, "alert") ;
	 }
	 
	 /**
	 * AIOSHOP提醒设置前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Result result = shopMgt.queryShopAlert() ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			HashMap alluser = OnlineUserInfo.cloneMap();
			String[] alert = (String[]) result.retVal ;
			if(alert[1]!=null && alert[1].length()>0){
				String orderMsg = "" ;
				String[] arrayOrder = alert[1].split(",") ;
				for(String order : arrayOrder){
					OnlineUser user = (OnlineUser) alluser.get(order);
					if(user!=null){
						orderMsg += user.getName()+"," ;
					}
				}
				request.setAttribute("orderMsg", orderMsg) ;
			}
			if(alert[4]!=null && alert[4].length()>0){
				String memberMsg = "" ;
				String[] arrayMember = alert[4].split(",") ;
				for(String member : arrayMember){
					OnlineUser user = (OnlineUser) alluser.get(member);
					if(user!=null){
						memberMsg += user.getName()+"," ;
					}
				}
				request.setAttribute("memberMsg", memberMsg) ;
			}
			if(alert[7]!=null && alert[7].length()>0){
				String consultMsg = "" ;
				String[] arrayConsult = alert[7].split(",") ;
				for(String consult : arrayConsult){
					OnlineUser user = (OnlineUser) alluser.get(consult);
					if(user!=null){
						consultMsg += user.getName()+"," ;
					}
				}
				request.setAttribute("consultMsg", consultMsg) ;
			}
			request.setAttribute("result", result.retVal) ;
		}
		return getForward(request, mapping, "alertset"); 
	}
			 
	 /**
	 * AIOSHOP提醒设置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String orderMobile = getParameter("orderMobile", request) ;
		String orderMsgId  = getParameter("orderMsgId", request) ;
		String orderEmail  = getParameter("orderEmail", request) ;
		
		String memberMobile = getParameter("memberMobile", request) ;
		String memberMsgId  = getParameter("memberMsgId", request) ;
		String memberEmail  = getParameter("memberEmail", request) ;
		
		String consultMobile = getParameter("consultMobile", request) ;
		String consultMsgId  = getParameter("consultMsgId", request) ;
		String consultEmail  = getParameter("consultEmail", request) ;
		
		AIOShopBean shopBean = BaseEnv.AIO_SHOP ;
		HttpURLConnection conn = (HttpURLConnection) new URL(shopBean.getLinkAddr()+"/services/AIOShopServices").openConnection();
		int resCode = 0;
		try {
			resCode = conn.getResponseCode();
		} catch (Exception e) {
			resCode = -1;
		}
		if(200!=resCode){
			EchoMessage.error().add("设置失败，请确认AIO-SHOP电子商务网站运行正常。").setBackUrl("/AIOShopAction.do?operation=7")
			   					.setAlertRequest(request);
		}else{
			Result result = shopMgt.setShopAlert(orderMobile, orderMsgId, orderEmail, memberMobile, memberMsgId, 
					memberEmail, consultMobile, consultMsgId, consultEmail);
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				Service srvcModel = new ObjectServiceFactory().create(IAIOShopServices.class);
				XFireProxyFactory factory = new XFireProxyFactory(XFireFactory.newInstance().getXFire());
				String aioShopURL = shopBean.getLinkAddr()+"/services/AIOShopServices";
				IAIOShopServices srvc = (IAIOShopServices) factory.create(srvcModel,aioShopURL);
				SpAlert alert = new SpAlert() ;
				alert.setId("aioshopAlert101") ;
				alert.setOrderMobile(orderMobile) ;
				alert.setOrderEmail(orderEmail) ;
				alert.setMemberMobile(memberMobile) ;
				alert.setMemberEmail(memberEmail) ;
				alert.setConsultMobile(consultMobile) ;
				alert.setConsultEmail(consultEmail) ;
				srvc.setShopAlert(alert) ;
				EchoMessage.error().add("提醒方式设置成功").setBackUrl("/AIOShopAction.do?operation=7")
			   				   .setAlertRequest(request);
			}else{
				EchoMessage.error().add("提醒方式设置失败").setBackUrl("/AIOShopAction.do?operation=7")
				   .setAlertRequest(request);
			}
		}
		
		return getForward(request, mapping, "alert") ;
	}		
}
