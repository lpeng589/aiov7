package com.koron.oa.toDo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.OACalendar.OACalendaMgt;
import com.koron.oa.OACalendar.OACalendarBean;
import com.koron.oa.oaItems.OAItemsMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.TextBoxUtil;

/**
 * 
 * <p>Title:�ҵĴ���</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/5
 * @Copyright: �����п���������޹�˾
 * @Author wyy
 */
public class ToDoAction extends BaseAction{
	ToDoMgt mgt = new ToDoMgt();
	AdviceMgt admgt = new AdviceMgt();
	OACalendaMgt omgt = new OACalendaMgt();
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();	
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);		
		ActionForward forward = null;
		
		//��ȡ����ְԱ��Ϣ,����textBox�ؼ�
		ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
		request.setAttribute("textBoxValues",gson.toJson(list));
		
		/*�Ƿ����body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		
		switch (operation) {				
		case OperationConst.OP_ADD:
			forward = addToDo(mapping, form, request, response);
			break;
		/*�첽 ��ӷ���*/	
		case OperationConst.OP_ADD_PREPARE:
			forward = addType(mapping, form, request, response);
			break;
		case OperationConst.OP_QUOTE:
			forward = updateColor(mapping, form, request, response);
			break; 
			
		case OperationConst.OP_UPDATE_PREPARE:			
			forward = updatePrepare(mapping, form, request, response);									
			break;
		case OperationConst.OP_DETAIL:			
			forward = detail(mapping, form, request, response);//�ճ̴�����ϸ									
			break;
		case OperationConst.OP_UPDATE:		
			String typeFlag = request.getParameter("typeFlag");
			if("type".equals(typeFlag)){
				forward = updateType(mapping, form, request, response);	
			}else{
				forward = updateToDo(mapping, form, request, response);	
			}					
			break;
		
		case OperationConst.OP_QUERY:
				String outFlag = request.getParameter("Flag");
				if("outIn".equals(outFlag)){
					forward = outQuery(mapping, form, request, response);
				}else{
					forward = queryToDo(mapping, form, request, response);	
				}					           	           															
			break;
		
		case OperationConst.OP_DELETE:	
			String delFlag = request.getParameter("delFlag");
			if("TYPE".equals(delFlag)){
				forward = delType(mapping, form, request, response);
			}else if("affax".equals(delFlag)){
				forward = delAffax(mapping, form, request, response);
			}else{
				forward = delToDo(mapping, form, request, response);
			}
			break;
		
		default:	
				forward = queryToDo(mapping, form, request, response); 
		}
		return forward;
	}
	
	private ActionForward delAffax(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String afax = request.getParameter("afax");
		OAToDoBean bean = (OAToDoBean)mgt.loadToDo(id).retVal;
		bean.setUploadFile(bean.getUploadFile().replace(afax+";", ""));
		Result rs = mgt.updateToDo(bean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", 3);
		}
		return getForward(request, mapping, "blank");
	}
	private ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String eventId = request.getParameter("eventId");//�ճ�Id
		Result rs = mgt.detailByEvent(eventId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("toDoList", rs.retVal);
		}
		return getForward(request, mapping, "detail");
	}
	/**
	 * ��������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String type = GlobalsTool.toChinseChar(request.getParameter("type"));
		OAToDoBean bean = (OAToDoBean)mgt.loadToDo(id).retVal;
		bean.setType(type);
		Result rs = mgt.updateType(bean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", 3);
		}else{
			request.setAttribute("msg", 0);
		}
		return getForward(request, mapping, "blank");
	}
	/**
	 * ���Ӳ�ѯ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward outQuery(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String keyId = request.getParameter("keyId");
		try{
			if(keyId !=null && !"".equals(keyId)){				
				Result rs = mgt.getByOutId(keyId,getLoginBean(request).getId());
				request.setAttribute("List", rs.retVal);
				
				//request.setAttribute("tab", "1".equals(bean.getStatus())?"over":"");
				//��ѯ����
				Result res = mgt.selectType(getLoginBean(request).getId()); 
				request.setAttribute("type", res.retVal);
			}					
		}catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(request, mapping, "queryToDo");			
	}

	/**
	 * �޸���ɫ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateColor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String type = GlobalsTool.toChinseChar(request.getParameter("type"));
		String color=request.getParameter("color");
		Result rs = mgt.changeColor(type,color,getLoginBean(request).getId());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", 3);
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * ����첽����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String type = GlobalsTool.toChinseChar(request.getParameter("type"));			
		String color = request.getParameter("color");
		//�ж�Ψһ
		Result uqion = mgt.getTypeColor(type,getLoginBean(request).getId());
		ArrayList uqArr = (ArrayList)uqion.retVal;
		if(uqArr!=null && uqArr.size()>0){					
			request.setAttribute("msg", "");									
		}else{			
			if(!"".equals(type)){
				String id = IDGenerater.getId();
				Result rs = mgt.addType(id,getLoginBean(request).getId(), type,color);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					request.setAttribute("msg", id+";"+color);
				}	
			}
		}					
		return getForward(request, mapping, "blank");
	}
	private ActionForward delType(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String type = GlobalsTool.toChinseChar(request.getParameter("type"));					
		Result rs = mgt.delType(type);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", 3);
			
		}					
		return getForward(request, mapping, "blank");
	}

	/**
	 * ��Ӵ���
	 * @param bean
	 * @return
	 */
	public static String addTodo(OAToDoBean bean) {

		if (null == bean.getTitle() || "".equals(bean.getId())) {
			return "���ⲻ��Ϊ��";
		}
		if (null == bean.getCreateBy() || "".equals(bean.getCreateBy())) {
			return "�����˲���Ϊ��";
		}

		if (null == bean.getId() || "".equals(bean.getId())) {
			bean.setId(IDGenerater.getId());
		}
		if (null == bean.getType() || "".equals(bean.getType())) {
			bean.setType("δ����");
		}
		if (null == bean.getAlertTime()) {
			bean.setAlertTime("");
		}
		if (null == bean.getRef_taskId()) {
			bean.setRef_taskId("");
		}
		bean.setCreateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		bean.setStatus("0");// δ���
		Result rs = new ToDoMgt().addToDo(bean);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (!"".equals(bean.getAlertTime())) {
				new OACalendaMgt().outAddCalendar(bean.getCreateBy(),
						bean.getTitle(), "�ҵĴ���", bean.getAlertTime(), bean.getAlertTime(), bean.getId(),
						"",null,"0","");
			} else {
				new OACalendaMgt().delByRelationId(bean.getId());
			}
			return "";
		}
		return "�������ݿ�ʧ��";
	}

	/**
	 *  ��� ��ʾ���ٺ;���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addToDo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{		
		String title = request.getParameter("context");
		String selType = request.getParameter("selType");
		String clientId = request.getParameter("clientId");
		String alertTime = request.getParameter("alertTime");	
		String taskId = request.getParameter("taskId");
		String affix = getParameter("affix",request);//����
		String userId = getLoginBean(request).getId();
		String id = IDGenerater.getId();
		OAToDoBean bean = new OAToDoBean();
		bean.setId(id);
		bean.setCreateBy(userId);
		bean.setTitle(title);
		bean.setType(selType);
		bean.setRelationId(clientId);
		bean.setAlertTime(alertTime);
		bean.setRef_taskId(taskId);
		bean.setUploadFile(affix);
		String err = addTodo(bean);
		if ("".equals(err)) {
			//������
			if(affix!=null && !"".equals(affix)){
				for(String str :affix.split(";")){
					FileHandler.copy("OAToDo", FileHandler.TYPE_AFFIX, str, str);
					FileHandler.deleteTemp(str);
				}
			}
			// ��ȡ����
			String num = this.twoSum(userId, selType);
			String numAll = this.twoSum(userId, null);
			request.setAttribute("msg", id + ";" + num+";"+numAll);
		}else {					
			request.setAttribute("msg", "err:"+err);
		}
		
		return getForward(request, mapping, "blank");
	}

	/**
	 * ɾ������
	 * @param id
	 * @return
	 */
	public static boolean delToDo(String id) {
		Result rs = new ToDoMgt().delToDo(id);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//ɾ��֪ͨ
			/*if(bean.getRelationId() !=null && !"".equals(bean.getRelationId())){
				admgt.deleteByRelationId(bean.getRelationId(), id);
			}*/
			//ɾ���ճ�����
			new OACalendaMgt().delByRelationId(id);	
			return true;
		}
		return false;
	}

	private ActionForward delToDo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
			try {
				String id = request.getParameter("id");
				String selType = request.getParameter("selType");
				if(delToDo(id)){
					//��ȡ����
					//String num = this.twoSum(getLoginBean(request).getId(),selType);				
					request.setAttribute("msg", 3);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}		
		return getForward(request, mapping, "blank");
	}
	/**
	 * �޸Ĵ���
	 * @param bean
	 * @return
	 */
	public static String updateToDo(OAToDoBean bean) {

		if (null == bean.getTitle() || "".equals(bean.getId())) {
			return "���ⲻ��Ϊ��";
		}
		if (null == bean.getCreateBy() || "".equals(bean.getCreateBy())) {
			return "�����˲���Ϊ��";
		}
		
		if (null == bean.getType() || "".equals(bean.getType())) {
			bean.setType("δ����");
		}
		if (null == bean.getStatus()) {
			bean.setStatus("0"); // �������ڣ�ֻ�Ǹ���ֵ
		}
		if (null == bean.getAlertTime()) {
			bean.setAlertTime("");
		}
		if (null == bean.getRef_taskId()) {
			bean.setRef_taskId("");
		}

		OAToDoBean beanOld = (OAToDoBean) new ToDoMgt().loadToDo(bean.getId()).retVal;
		if (beanOld == null) {
			return "�Ҳ���ԭ���ļ�¼";
		}
		if(beanOld.getAlertTime() == null){
			beanOld.setAlertTime("");
		}
		if (!beanOld.getAlertTime().equals(bean.getAlertTime())
				&& "1".equals(bean.getStatus())) {
			return "���״̬�²������޸�����ʱ��";
		}

		Result rs = new ToDoMgt().updateToDo(bean);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			String now = BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss);

			if (!beanOld.getStatus().equals(bean.getStatus())) { // �޸�״̬
				if ("1".equals(bean.getStatus())) { // δ��ɱ����ɣ�����ճ�
					new OACalendaMgt()
							.outAddCalendar(bean.getCreateBy(),
									bean.getTitle(), "�ҵĴ���", now, now,
									bean.getId(), "",null,"1","");
				} else if ("0".equals(bean.getStatus())) { // ��ɱ�δ��ɣ�ɾ�ճ�
					new OACalendaMgt().delByRelationId(bean.getId());
				}
			} else if (!beanOld.getAlertTime().equals(bean.getAlertTime())) { // ״̬������ʱ�䲻��ͬʱ�޸�
				if (!"".equals(bean.getAlertTime())) { // ������ʱ�������ճ�
					new OACalendaMgt().outAddCalendar(bean.getCreateBy(),
							bean.getTitle(), "�ҵĴ���", bean.getAlertTime(),
							bean.getAlertTime(), bean.getId(), "",null,"0","");
				} else { // û����ʱ�䣬��ɾ�ճ�
					new OACalendaMgt().delByRelationId(bean.getId());
				}
			}
			return ""; // ���ؿ��ַ�����ʾ�ɹ�
		} else {
			return "�������ݿ�ʧ��";
		}
	}
	
	private ActionForward updateToDo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String updateFg = request.getParameter("updateFlag");
		String id = request.getParameter("id");
		String title = request.getParameter("context");
		String selType = request.getParameter("selType");
		String clientId = request.getParameter("clientId");
		String alertTime = request.getParameter("alertTime");
		String taskId = request.getParameter("taskId");
		String affix = getParameter("affix",request);//����
		OAToDoBean bean = (OAToDoBean)mgt.loadToDo(id).retVal;
		//���
		if("over".equals(updateFg)){
			String overOrNo = "YES".equals(request.getParameter("overOrNo")) ? "1" : "0";
			
			bean.setStatus(overOrNo);
			String err = updateToDo(bean);
			if("".equals(err)){
				//��ȡ����
				//String num = this.twoSum(getLoginBean(request).getId(),selType);							
				request.setAttribute("msg", 3);
			} else {					
				request.setAttribute("msg", "err:"+err);
			}
		}
		// ���ݸ���
		if("update".equals(updateFg)){
				
			bean.setTitle(title);
			bean.setRelationId(clientId);
			bean.setType("".equals(selType)?"δ����":selType);
			bean.setRef_taskId(taskId);
			bean.setUploadFile(affix);
//			bean.setAlertTime(alertTime);
			String err = updateToDo(bean);
			if("".equals(err)){
				//������
				if(affix!=null && !"".equals(affix)){
					for(String str :affix.split(";")){
						FileHandler.copy("OAToDo", FileHandler.TYPE_AFFIX, str, str);
						FileHandler.deleteTemp(str);
					}
				}
				//����֪ͨ
				request.setAttribute("msg", id);		
			} else {					
				request.setAttribute("msg", "err:"+err);
			}
		}
		// �������� 
		if("time".equals(updateFg)){
			bean.setAlertTime(alertTime);
			String err = updateToDo(bean);
			if("".equals(err)){
				request.setAttribute("msg", 3);	
			} else {					
				request.setAttribute("msg", "err:"+err);
			}
		}
		//
		return getForward(request, mapping, "blank");
	}


	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
			try {
				String id = request.getParameter("id");									
							
				//��������
				Result rs = mgt.queryPre(id);				
				ArrayList rsrs = (ArrayList)rs.retVal;
				String tilte =  GlobalsTool.get(rsrs.get(0),0).toString();
				String type =  GlobalsTool.get(rsrs.get(0),1).toString();
				String afax = GlobalsTool.get(rsrs.get(0),8).toString();//����
				String taskId=GlobalsTool.get(rsrs.get(0),7).toString();								
				String relationId = GlobalsTool.get(rsrs.get(0),3).toString();	
				String das = "";
				
				if(!"".equals(relationId)){
					Result clientN = mgt.clientName(relationId);
					ArrayList clN = (ArrayList)clientN.retVal;
					for (int i = 0; i < clN.size(); i++) {
						das += ((Object[])clN.get(i))[0].toString()+";";
					}
				}
				
				String msg =id+"|"+tilte+"|"+type+"|"+relationId+"|"+das+"|"+taskId+"|"+afax;
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){				
					request.setAttribute("msg", msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return getForward(request, mapping, "blank");
	}

	/**
	 * ��ѯ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward queryToDo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		try {
			ToDoForm lvForm = (ToDoForm)form;
			if((lvForm.getPageNo()) == 0){
				lvForm.setPageNo(1);
			}
			lvForm.setId(getParameter("id", request));//����̨������id
			String flagAll = request.getParameter("flagAll");
			String tab = request.getParameter("tab");
			String selType = request.getParameter("selType");
			String selTypeId = request.getParameter("selTypeId");
			String findcontxt = request.getParameter("fdContxt");
			Result rs = mgt.queryToDo(lvForm,tab,getLoginBean(request).getId(),findcontxt,selType);
			request.setAttribute("List", rs.retVal);
			request.setAttribute("tab", tab);
			request.setAttribute("selType", selType);
			if(!"all".equals(flagAll)){
				request.setAttribute("selTypeId", selTypeId);				
			}
			request.setAttribute("pageBar", pageBar2(rs, request));
			request.setAttribute("fdContxt", findcontxt);
			
			
			//��ѯ����
			Result res = mgt.selectType(getLoginBean(request).getId()); 
			request.setAttribute("type", res.retVal);
			HashMap<String, String> map = new HashMap<String, String>();
			//��װmap
			ArrayList mapres = (ArrayList)res.retVal;
			if(mapres!=null && mapres.size()>0){
				for (int i = 0; i < mapres.size(); i++) {
					String key = ((Object[])mapres.get(i))[1].toString();
					String value = ((Object[])mapres.get(i))[3].toString();
					map.put(key, value);
				}
			}
			request.setAttribute("mapList", map);
			
						
			//
			Calendar c = Calendar.getInstance();                 				
			c.add(Calendar.DAY_OF_MONTH, -1);
			request.setAttribute("yestoday",BaseDateFormat.format(c.getTime(), BaseDateFormat.yyyyMMdd));
			c.add(Calendar.DAY_OF_MONTH, -1);
			request.setAttribute("oldthree",BaseDateFormat.format(c.getTime(), BaseDateFormat.yyyyMMdd));
			
			if(lvForm.getId() == null){
			//��ȡ����
				String num = this.twoSum(getLoginBean(request).getId(),selType);
				String[] ssum = num.split(";");
				request.setAttribute("unOver", ssum[0]);
				request.setAttribute("oversd", ssum[1]);
			}
			
			
			//��ѯ��ǰ�û���ѡ�����Ŀ
			ArrayList<Object> itemList = new OAItemsMgt().queryItemsByUserId(getLoginBean(request).getId(),true);
			request.setAttribute("itemList",itemList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(request, mapping, "queryToDo");
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean login = getLoginBean(req);
		if(login == null){
			BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
		}
		
		return null;
	}
	
	public String twoSum(String loginId,String type){
		String num = "0;0";
		Result sum = mgt.getSum(loginId,type);
		ArrayList sumrs = (ArrayList)sum.retVal;
		String unOver = ((Object[])sumrs.get(0))[0]==null?"0":((Object[])sumrs.get(0))[0].toString();
		String allS = ((Object[])sumrs.get(0))[1]==null?"0":((Object[])sumrs.get(0))[1].toString();
		String oversd = String.valueOf(Integer.parseInt(allS) - Integer.parseInt(unOver));	
		num = unOver+";"+oversd;
		return num;
	}
}
