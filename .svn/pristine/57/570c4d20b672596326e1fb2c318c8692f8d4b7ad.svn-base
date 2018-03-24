package com.koron.oa.communicationNote;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import com.dbfactory.Result;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:我的通讯录Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-11-14 下午 16：28
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class CommunicationNoteAction extends MgtBaseAction{

	CommunicationNoteMgt mgt = new CommunicationNoteMgt();
	
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int operation = getOperation(request);
		
		String opType = request.getParameter("opType");
		
		ActionForward forward = null;
		
		switch (operation) {
		case OperationConst.OP_QUERY:
			if(opType != null && "queryContactInfo".equals(opType)){
				//验证是否输入邮箱和手机号码
				forward = queryContactInfo(mapping, form, request, response);
			}else if(opType != null && "sendPrepare".equals(opType)){
				//发送短信前
				forward = sendPrepare(mapping, form, request, response);
			}else if(opType != null && "sendMessage".equals(opType)){
				//发送短信
				forward = sendMessage(mapping, form, request, response);
			}
			break;
		default:
			//首页查询
			forward = toTree(mapping, form, request, response);
			break;
		}
		return forward;
	}
	
	/**
	 * 首页查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward toTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String locale = this.getLocale(request).toString();
		
		//查询所有部门
		Result rs = mgt.toTreeDeptList();
		String folderTree = "[";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			List stockList = (ArrayList)rs.retVal;
			int num =0;
			//对数据进行处理把数据组合成节点形式
			folderTree += "{ id:\"0\", pId:-1, name:\"";
			if("".equals(GlobalsTool.getCompanyName(locale))){
				folderTree += getMessage(request, "com.framework.from");
			}else{
				folderTree += GlobalsTool.getCompanyName(locale);
			}
			folderTree += "\"}";
			if(stockList.size()>0){
				folderTree += ",";
			}
			for(int i=0;i<stockList.size();i++){
				HashMap o = (HashMap)stockList.get(i);
				if(String.valueOf(o.get("classCode")).length() == 5){
					//父节点
					folderTree += "{ id:\""+o.get("classCode")+"\", pId:0, name:\""+o.get("DeptFullName")+"\"}";
				}else{
					String classC = ((String)o.get("classCode")).substring(0, ((String)o.get("classCode")).length()-5);
					folderTree += "{ id:\""+o.get("classCode")+"\", pId:\""+classC+"\", name:\""+o.get("DeptFullName")+"\"}";
				}
				if(num != stockList.size()-1){
					folderTree+=",";
				}
				num ++;
			}
		}
		folderTree += "]";
		request.setAttribute("deptData", folderTree);
		
		CommunicationNoteSearchForm searchForm = (CommunicationNoteSearchForm)form;
		
		/**
		 * 查询职员数据
		 */
		rs = mgt.queryList(searchForm, locale);
		if(rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("pageBar", this.pageBar2(rs, request));
			request.setAttribute("empList", rs.getRetVal());
		}
		return getForward(request, mapping, "toTree");
	}
	
	/**
	 * 发送邮件前，查看是否有联系人邮箱地址
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward queryContactInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		String fieldName = request.getParameter("fieldName");
		Result rs = mgt.findContactByClientId(id,fieldName);
		List list = (List)rs.retVal;
		if(list ==null || list.size() ==0){
			request.setAttribute("msg", "no");
		}else{
			request.setAttribute("msg", "yes");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 发送短信前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward sendPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String keyId =getParameter("keyId", request); /*客户Id*/
		String msgType = getParameter("msgType", request) ; /*消息类型 sms:短信,email:邮件*/
		Result result=null;
		if(!msgType.contains("detail")){ //列表页面发送信息
			result = mgt.selectClientDetById(keyId.split(","),msgType) ;
			request.setAttribute("clientId", keyId) ;
			request.setAttribute("msgType", msgType) ;
		}
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("clientDetList", result.retVal) ;
			request.setAttribute("smsSign", AIOTelecomCenter.smsSign);
			request.setAttribute("smsSignLen", AIOTelecomCenter.smsSign==null?0:(AIOTelecomCenter.smsSign.length()+2));
		}
		return mapping.findForward("sendPrepare");
	}
	
	
	/**
	 * 发送短信
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward sendMessage(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String mobile = getParameter("newCreateBy", request) ; /*手机号码*/ 
		String sendMessage = getParameter("handContent", request) ; /*短信内容*/
		String smsType = getParameter("handSmsType", request) ; /*短信方式*/
		int smsTypei = 0;
		try{
			smsTypei =Integer.parseInt(smsType);
			if (BaseEnv.telecomCenter == null){
				request.setAttribute("msg", getMessage(request, "sms.note.stop"));
			} else{
				BaseEnv.telecomCenter.send(sendMessage,mobile.split(","),getLoginBean(request).getId());
				request.setAttribute("msg", "yes");
			}
		}catch(Exception e){request.setAttribute("msg", "no");}
		return getForward(request, mapping, "blank");
	}

}
