package com.koron.oa.office.meeting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;

public class OABoardroomAction extends BaseAction{
	
	private OABoardroomMgt bmgt=new OABoardroomMgt();

	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int operation = getOperation(request);
		
		ActionForward forward = null;
		switch(operation){
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		// 新增操作
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		//添加会议之前准备
		case OperationConst.OP_ADD_PREPARE:			
			forward =addPrepare(mapping, form, request, response);
			break;
			//修改会议之前准备
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;	
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;	
		case OperationConst.OP_DELETE_PREPARE:
			forward = deletePrepare(mapping, form, request, response);
			break;
			default:
				forward = query(mapping, form, request, response);
				break;
		}
		
		return forward;
	}
	
	/**
	 *  //会议室管理
		case OP_BOARDROOM:
			allBoardroom(request);
			forward = boardroom(mapping, form, request, response);
			break;
	 */
	
	/**
	 * 会议室管理准备
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		String flagOut = request.getParameter("flagOut");
		request.setAttribute("flagOut", flagOut);
		return getForward(request, mapping, "to_addboardroom");
		
	}
	
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OABoardroomForm boardroomForm=(OABoardroomForm)form;
				Result result=bmgt.loadBoardroom(boardroomForm.getBoardroomId());
				 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						OABoardroomBean bean =(OABoardroomBean)result.getRetVal();	
						request.setAttribute("boardroom",bean);	
				 }
		return getForward(request, mapping, "to_addboardroom");
	}
	
	
	protected ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//OABoardroomForm boardroomForm=(OABoardroomForm)form;
		//String flagOut = request.getParameter("flagOut");
		
		LoginBean lg = this.getLoginBean(request);
		String msg = "0";
		String describe = request.getParameter("describe");
		String boardroomName = request.getParameter("boardroomName");
		String address = request.getParameter("address");		
		String peopleNumber = request.getParameter("peopleNumber");
		OABoardroomBean bean=new OABoardroomBean();
		//read(boardroomForm,bean);
		bean.setBoardroomId(IDGenerater.getId());
		bean.setAddress(address);
		bean.setBoardroomName(boardroomName);
		bean.setDescribe(describe);
		bean.setPeopleNumber(Integer.parseInt(peopleNumber==null||"".equals(peopleNumber)?"0":peopleNumber));		
		Result result=bmgt.addBoardroom(bean);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			msg = bean.getBoardroomId();	
			new DynDBManager().addLog(0, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"添加会议室管理："+bean.getBoardroomName(),"OABoardroom", "会议室管理","");
		}
		request.setAttribute("msg", msg);
		/*if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {		
			//添加成功
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess"))
					.setBackUrl("/OABoardroom.do?operation=4").setAlertRequest(request);
		}else{
			//添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture"))
					.setBackUrl("/OABoardroom.do?operation=4").setAlertRequest(request);
		}*/		
		return getForward(request, mapping, "blank");
	}
	
	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean lg = this.getLoginBean(request);
		OABoardroomForm boardroomForm=(OABoardroomForm)form;
		String boardroomId = request.getParameter("boardroomId");
		Result result=bmgt.loadBoardroom(boardroomId);
		 if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			 OABoardroomBean bean=(OABoardroomBean)result.getRetVal();
		      read(boardroomForm,bean);
		       result=bmgt.updateBoardroom(bean);
		 }
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {		
			//修改成功
			new DynDBManager().addLog(1, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"修改会议室管理："+boardroomForm.getBoardroomName(),"OABoardroom", "会议室管理","");
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess"))
					.setBackUrl("/OABoardroom.do?operation=4").setAlertRequest(request);
		}else{
			//修改失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture"))
					.setBackUrl("/OABoardroom.do?operation=4").setAlertRequest(request);
		}		
		return getForward(request, mapping, "message");
	}
	
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, OABoardroomBean> boardroomMap=bmgt.getBoardroomMap();
		LoginBean lg = this.getLoginBean(request);
		String[] logsIds = getParameters("keyId", request);
		if(logsIds!=null){
			Result result = bmgt.deleteBoardroom(logsIds);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				//删除成功
				String[] keyNames = getParameters("keyName", request);
				for(String s : keyNames){
					new DynDBManager().addLog(2, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"删除会议室管理："+s,"OABoardroom", "会议室管理","");
				}
				advice(getBoardroomByMeeting(logsIds),boardroomMap,request);
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess"))
						.setBackUrl("/OABoardroom.do").setAlertRequest(request);
				return getForward(request, mapping, "message");
			} else {
				//添加失败
				EchoMessage.error().add(getMessage(request, "common.msg.delError")).setBackUrl("/OABoardroom.do")
						.setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
		}else{
			String isOk="no";
			OABoardroomForm boardroomForm=(OABoardroomForm)form;
			Result result=bmgt.deleteBoardroom(boardroomForm.getBoardroomId());
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				new DynDBManager().addLog(2, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"删除会议室管理："+GlobalsTool.toChinseChar(boardroomForm.getBoardroomName()),"OABoardroom", "会议室管理","");
				logsIds=new String[1];
				logsIds[0]=boardroomForm.getBoardroomId();
				advice(getBoardroomByMeeting(logsIds),boardroomMap,request);
				isOk="yes";
			}
			request.setAttribute("msg", isOk);
			return getForward(request, mapping, "blank");
		}
		
	}
	
	
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Result result=bmgt.queryAll();
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {		
			request.setAttribute("list",result.getRetVal());
		}
		return getForward(request, mapping, "to_allboardroom");
		
	}
	
	/**
	 * 删除之前对会议室相关的会议提醒
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward deletePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int shu=0;
		String[] logsIds = getParameters("keyId", request);
		if(logsIds!=null){
			shu=getBoardroomByMeeting(logsIds).size();
		}
		request.setAttribute("msg", shu);
		return getForward(request, mapping, "blank");
		
	}
	
	private List<String[]> getBoardroomByMeeting(String[] boardroomIds){
		List<String[]> meetingids=new ArrayList();
		Result result=bmgt.getBoardroomByMeeting(boardroomIds);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {		
			List<Object[]> temp=(List<Object[]>)result.getRetVal();		
			for(Object[] meeting : temp){
				String[] cols=new String[3];
				cols[0]=(String)meeting[0];
				cols[1]=(String)meeting[1];
				cols[2]=(String)meeting[2];
				meetingids.add(cols);
			}
		}
		return meetingids;
	}

	/**
	 * 通知相关人员
	 * 
	 * @return
	 * @throws Exception
	 */
	public void advice(List<String[]> meetings,Map<String, OABoardroomBean> boardroomMap,HttpServletRequest request){
		LoginBean lg = this.getLoginBean(request);
		for(String[] i:meetings){
		BaseEnv.log.error("用户ID:"+i[1]);
        String url = "/Meeting.do";
        String title="通知："+lg.getEmpFullName()+"删除了"+boardroomMap.get(i[2]).getBoardroomName()+",请须知";
        String favoriteURL = url + "?noback=true&operation=4&requestType=NOTE&meetingId=" + i[0] + "&isEspecial=1";
        String content = "<a href=javascript:mdiwin('" + favoriteURL + "','"
					  + GlobalsTool.getMessage(request, "oa.common.adviceList")
					  + "')>" + title
					  + "</a>"; // 内容
		//向用户添加提醒方式
				new Thread(new NotifyFashion(i[1], title, content,
						i[1], 4, "yes",
						i[2],"","","advice")).start();
		}
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
