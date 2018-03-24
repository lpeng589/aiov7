package com.menyi.sms.note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.NoteSetBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.sms.setting.SMSsetMgt;
import com.menyi.web.util.AIOTelecomCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.SystemState;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: 付湘鄂
 * </p>
 * 
 * @author 付湘鄂
 * @version 1.0
 */
public class NoteAction extends MgtBaseAction {
	public NoteAction() {
	}

	AIOTelecomCenter sms = new AIOTelecomCenter();
    NoteMgt noteMgt=new NoteMgt();
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setAttribute("winCurIndex",request.getParameter("winCurIndex"));
		ActionForward forward = null;
		// 跟据不同操作类型分配给不同函数处理
		String noback = request.getParameter("noback");// 不能返回
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		String operator = request.getParameter("operator");
		if(operator==null){
			operator=request.getParameter("operation");
		}
		
		if("querySendSMS".equals(operator)){
		   forward = querySendSMS(mapping, form, request, response);//已发送短信查询
		} else if("queryReceivedSMS".equals(operator)){
		   forward = queryReceivedSMS(mapping, form, request, response);//已发送短信查询
		} else if("sendSMS".equals(operator)){
			//重复发送与发送使用同一个权限
			if("true".equals(request.getParameter("repeatSend"))){
				forward = repeatSend(mapping,form,request,response) ;
			}else{
				forward=sendSMS(mapping, form, request, response);//发送短信
			}
		} else if("delSMS".equals(operator)){
			forward = delSMS(mapping,form,request,response) ;
		} else if("repeatSend".equals(operator)){
			forward = repeatSend(mapping,form,request,response) ;
		}
		
		return forward;
	}

	

	

	protected ActionForward querySendSMS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Result rs=null;
        Object type=request.getParameter("type");
        if(type==null||type.toString().equals("query"))
        { 
        	String content = request.getParameter("content");
        	String receiveMobile=request.getParameter("receiveMobile");
        	String beginTime=request.getParameter("beginTime");
        	String endTime=request.getParameter("endTime");
        	String state=request.getParameter("state");
        	String sender = request.getParameter("sender") ;
        	content = content==null?"":content;
        	receiveMobile = receiveMobile==null?"":receiveMobile;
        	beginTime = beginTime==null?"":beginTime;
        	endTime = endTime==null?"":endTime;
        	sender = sender==null?"":sender;
        	state = state==null?"":state;
        	//BaseSearchForm baseForm = form==null?new BaseSearchForm():(BaseSearchForm)form;
        	int pageNo = request.getParameter("pageNo")==null?1:Integer.parseInt(request.getParameter("pageNo"));
        	int pageSize = request.getParameter("pageSize")==null?100:Integer.parseInt(request.getParameter("pageSize")) ;
        	
        	rs=noteMgt.querySendSMS(receiveMobile, beginTime, endTime, state,sender,content,request,pageNo,pageSize);
        	if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS&&((ArrayList)rs.getRetVal()).size()>0)
        	{
                request.setAttribute("result", (ArrayList)rs.getRetVal());
        	}
        	request.setAttribute("receiveMobile", receiveMobile);
        	request.setAttribute("beginTime", beginTime);
        	request.setAttribute("endTime", endTime);
        	request.setAttribute("state", state);
        	request.setAttribute("sender", sender);
        	request.setAttribute("content", content);
        	request.setAttribute("pageBar", pageBar(rs, request));
        	return getForward(request, mapping, "sendSMSList");
        }else{
        	String keyId=request.getParameter("keyId");
        	rs=noteMgt.querySendSMSById(keyId);
        	if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS&&((ArrayList)rs.getRetVal()).size()>0)
        	{
        		 //查询成功
                request.setAttribute("sendnote",((ArrayList)rs.getRetVal()).get(0));
        	}
        	return getForward(request, mapping, "sendSMSDetail");
        	
        }
       
	}
	protected ActionForward queryReceivedSMS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Result rs=null;
        Object type=request.getParameter("type");
        if(type==null||type.toString().equals("query"))
        { 
        	String sendMobile=request.getParameter("sendMobile");
        	String beginTime=request.getParameter("beginTime");
        	String endTime=request.getParameter("endTime");
        	rs=noteMgt.queryReceivedSMS(sendMobile, beginTime, endTime);
        	if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS&&((ArrayList)rs.getRetVal()).size()>0)
        	{
                request.setAttribute("result", (ArrayList)rs.getRetVal());
        	}
        	request.setAttribute("sendMobile", sendMobile);
        	request.setAttribute("beginTime", beginTime);
        	request.setAttribute("endTime", endTime);
        	return getForward(request, mapping, "receivedSMSList");
        }else
        {
        	String keyId=request.getParameter("keyId");
        	rs=noteMgt.queryReceivedSMSById(keyId);
        	if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS&&((ArrayList)rs.getRetVal()).size()>0)
        	{
        		 //查询成功
                request.setAttribute("receivednote",((ArrayList)rs.getRetVal()).get(0));
        	}
        	return getForward(request, mapping, "receivedSMSDetail");
        	
        }
       
	}
	
	protected ActionForward sendSMS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		LoginBean lg  = (LoginBean) request.getSession().getAttribute("LoginBean");
		String defSys = request.getParameter("defSys");
		if (defSys != null && !defSys.equals("")) {
			request.setAttribute("defSys", defSys);
		} else {
			request.setAttribute("defSys", lg.getDefSys());
		}
				
		Object type = request.getParameter("type");
		if (type == null || type.toString().equals("query")) {
			request.setAttribute("smsSign", AIOTelecomCenter.smsSign);
			request.setAttribute("smsSignLen", AIOTelecomCenter.smsSign==null?0:(AIOTelecomCenter.smsSign.length()+2));
			return getForward(request, mapping, "sendSMS");
		} else {				
			String receiver = request.getParameter("receiver");
			String receiverMobile = request.getParameter("receiverMobile");
			String smsType = request.getParameter("smsType");
			String[] recs = receiver.split(";|,|\\s");
			ArrayList<String[]> list = new ArrayList<String[]>();
			for(String rec:recs){
				rec = rec.trim();
				if(rec!=null && rec.length() > 0 ){
					if(receiverMobile.indexOf(rec)> -1){
						String mobile = receiverMobile.substring(receiverMobile.indexOf(rec)+rec.length()+1,receiverMobile.indexOf(";",receiverMobile.indexOf(rec)+rec.length()+1));
						if(isMobile(mobile)){
							rec = rec.substring(0,rec.lastIndexOf("("));
							list.add(new String[]{rec,mobile});
						}
					}else{
						if(isMobile(rec)){
							list.add(new String[]{"",rec});
						}
					}
				}
			}
			String content = request.getParameter("content");
			int smsLen = (content.length() + 69)/70;
			ArrayList<String> msList = new ArrayList<String>();
			String remark="";
			for (int i = 0; i < list.size(); i++) {	
				msList.add(list.get(i)[1]);
				remark += list.get(i)[0]==null || list.get(i)[0].length() ==0?"":list.get(i)[0]+",";
			}
			BaseEnv.telecomCenter.send(content,msList.toArray(new String[0]),lg.getId());				
			EchoMessage.success().add(
					getMessage(request, "oa.msg.submitSuccess"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		
		}
	}

	/**
	 * 删除短信
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward delSMS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String[] keyIds = getParameters("keyId", request)  ;
		Result result = noteMgt.deleteSMS(keyIds) ;
		if(result.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(request, "common.msg.delSuccess"))
								 .setAlertRequest(request);
		}
		return querySendSMS(mapping, form, request, response) ;
	}
	
	private boolean isMobile(String str){
		return str.matches("1[\\d]{10}");
	}
	
	/**
	 * 重发短信
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward repeatSend(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String[] keyIds = getParameters("keyId", request)  ;
		Result result = noteMgt.querySMS(keyIds) ;
		if(result.retCode!=ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(request, "com.note.anewFailure")).setAlertRequest(request);
		}
		List noteList = (List) result.getRetVal() ;
		for(int i=0;noteList!=null && i<noteList.size();i++){
			Object[] obj = (Object[]) noteList.get(i) ;
			if(obj!=null && !"".equals(obj[0])){
				BaseEnv.telecomCenter.send(obj[1].toString(),obj[0].toString().split(","),getLoginBean(request).getId());
				Thread.sleep(1000L);
			}
		}
		EchoMessage.success().add(getMessage(request, "com.note.anewSucceed")).setAlertRequest(request);
		
		return getForward(request, mapping, "message");
	}
}
