package com.koron.oa.oaCollection;

import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;

/**
 * 
 * <p>Title:我的日程</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/14
 * @Copyright: 深圳市科荣软件有限公司
 * @Author wyy
 */
public class OACollectionAction extends BaseAction{
	OACollectionMgt mgt = new OACollectionMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		int operation = getOperation(request);		
		ActionForward forward = null;
		 /*是否添加body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		
		switch (operation) {
		case OperationConst.OP_UPDATE:
			String  attType = getParameter("attType", request);
			if("add".equals(attType)){
				forward = addCollection(mapping, form, request, response);
			}else{
				forward = outDelCollection(mapping, form, request, response);
			}					           	           															
			break;
		case OperationConst.OP_QUERY:			
				forward = queryCollection(mapping, form, request, response);		           	           															
			break;
		
		case OperationConst.OP_DELETE:				
				forward = delCollection(mapping, form, request, response);			
			break;
		
		default:	
				forward = queryCollection(mapping, form, request, response); 
		}
		return forward;
	}


	private ActionForward outDelCollection(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String keyId = getParameter("keyId", request);				
		String typeName = getParameter("typeName", request);
		String message = "OK" ;
		Result result = mgt.outDelCollection(keyId, getLoginBean(request).getId(),typeName);
		if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			message = "NO" ;
		}
		request.setAttribute("msg", message) ;
		return getForward(request, mapping, "blank");		
	}


	private ActionForward addCollection(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String keyId = getParameter("keyId", request);			
		String titleName = getParameter("titleName", request);
		String typeName = getParameter("typeName", request);
		String urls = getParameter("urlparam", request);		
		String message = "OK" ;	
		//Result result = mgt.addAttention(getLoginBean(request).getId(), keyId, "CRMClientInfo",moduleId);		
		Result result = mgt.addCollection(titleName, typeName, 
				urls,getLoginBean(request).getId(),keyId);
		if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			message = "NO" ;
		}							
		request.setAttribute("msg", message) ;
		return getForward(request, mapping, "blank");		
	}


	private ActionForward queryCollection(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		OACollectionForm lvForm = (OACollectionForm)form;
		String backColor = request.getParameter("backColor");
		request.setAttribute("backColor", backColor);
		if("0".equals(lvForm.getPageNo())){
			lvForm.setPageNo(1);
		}
		Result rs = mgt.queryCollection(getLoginBean(request).getId(), lvForm);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("ctList", rs.retVal);
			request.setAttribute("pageBar", pageBar2(rs, request));
		}
		
		//填充颜色
		HashMap<String, String> map = new HashMap<String, String>();
		//封装map
		String[] color = {"df7ba6","47a91c","47a91c","9133DB","b54143","3796bf","e5acae","efc0f6","cf69e2","abe7d9","47a81c"};
		String[] types = {"OAnewAdvice","OABBSTopic","OABBSSends","CRMClientInfo","OAItems","OATask","OAWorkLog","OANews","OAKnowCenter","OAMail","OAOrdain"};
		
		for (int i = 0; i < types.length; i++) {						
			map.put(types[i], color[i]);
		}
		
		request.setAttribute("mapList", map);
		
		//时间判断
		Calendar c = Calendar.getInstance();                 				
		c.add(Calendar.DAY_OF_MONTH, -1);
		request.setAttribute("yestoday",BaseDateFormat.format(c.getTime(), BaseDateFormat.yyyyMMdd));
		c.add(Calendar.DAY_OF_MONTH, -1);
		request.setAttribute("oldthree",BaseDateFormat.format(c.getTime(), BaseDateFormat.yyyyMMdd));
		
		request.setAttribute("tabFlag", lvForm.getTabFlag());
		request.setAttribute("context", lvForm.getContext());
		return getForward(request, mapping, "query");
	}
	
	/**
	 * 取消收藏
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delCollection(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		OACollectionForm lvForm = (OACollectionForm)form; 		
		String id = request.getParameter("id");
		//OACollectionBean bean = (OACollectionBean)mgt.loadCollection(id).retVal;
		if(id !=null && !"".equals(id)){
			//bean.setFinishTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			Result rs = mgt.delCollection(id);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				int Count = mgt.queryCount(getLoginBean(request).getId(), lvForm);				
				request.setAttribute("msg", Count);	
				
			}
		}
		return getForward(request, mapping, "blank");
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean login = getLoginBean(req);
		if(login == null){
			BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
		}
		//进行唯一用户验证，如果有生复登陆的，则后进入用户踢出前进入用户
        if (!OnlineUserInfo.checkUser(req)) {
            //需踢出
            EchoMessage.error().setAlertRequest(req);
            return getForward(req, mapping, "doubleOnline");
        }
		return null;
	}
}
