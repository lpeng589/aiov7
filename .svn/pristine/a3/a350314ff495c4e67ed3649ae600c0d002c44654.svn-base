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
 * <p>Title:我的待办</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/5
 * @Copyright: 深圳市科荣软件有限公司
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
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);		
		ActionForward forward = null;
		
		//获取所有职员信息,用于textBox控件
		ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
		request.setAttribute("textBoxValues",gson.toJson(list));
		
		/*是否添加body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		
		switch (operation) {				
		case OperationConst.OP_ADD:
			forward = addToDo(mapping, form, request, response);
			break;
		/*异步 添加分类*/	
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
			forward = detail(mapping, form, request, response);//日程待办详细									
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
		String eventId = request.getParameter("eventId");//日程Id
		Result rs = mgt.detailByEvent(eventId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("toDoList", rs.retVal);
		}
		return getForward(request, mapping, "detail");
	}
	/**
	 * 更新类型
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
	 * 链接查询
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
				//查询分类
				Result res = mgt.selectType(getLoginBean(request).getId()); 
				request.setAttribute("type", res.retVal);
			}					
		}catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(request, mapping, "queryToDo");			
	}

	/**
	 * 修改颜色
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
	 * 添加异步分类
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
		//判断唯一
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
	 * 添加待办
	 * @param bean
	 * @return
	 */
	public static String addTodo(OAToDoBean bean) {

		if (null == bean.getTitle() || "".equals(bean.getId())) {
			return "标题不能为空";
		}
		if (null == bean.getCreateBy() || "".equals(bean.getCreateBy())) {
			return "创建人不能为空";
		}

		if (null == bean.getId() || "".equals(bean.getId())) {
			bean.setId(IDGenerater.getId());
		}
		if (null == bean.getType() || "".equals(bean.getType())) {
			bean.setType("未分类");
		}
		if (null == bean.getAlertTime()) {
			bean.setAlertTime("");
		}
		if (null == bean.getRef_taskId()) {
			bean.setRef_taskId("");
		}
		bean.setCreateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		bean.setStatus("0");// 未完成
		Result rs = new ToDoMgt().addToDo(bean);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (!"".equals(bean.getAlertTime())) {
				new OACalendaMgt().outAddCalendar(bean.getCreateBy(),
						bean.getTitle(), "我的待办", bean.getAlertTime(), bean.getAlertTime(), bean.getId(),
						"",null,"0","");
			} else {
				new OACalendaMgt().delByRelationId(bean.getId());
			}
			return "";
		}
		return "插入数据库失败";
	}

	/**
	 *  添加 标示快速和具体
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
		String affix = getParameter("affix",request);//附件
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
			//处理附件
			if(affix!=null && !"".equals(affix)){
				for(String str :affix.split(";")){
					FileHandler.copy("OAToDo", FileHandler.TYPE_AFFIX, str, str);
					FileHandler.deleteTemp(str);
				}
			}
			// 获取数量
			String num = this.twoSum(userId, selType);
			String numAll = this.twoSum(userId, null);
			request.setAttribute("msg", id + ";" + num+";"+numAll);
		}else {					
			request.setAttribute("msg", "err:"+err);
		}
		
		return getForward(request, mapping, "blank");
	}

	/**
	 * 删除待办
	 * @param id
	 * @return
	 */
	public static boolean delToDo(String id) {
		Result rs = new ToDoMgt().delToDo(id);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//删除通知
			/*if(bean.getRelationId() !=null && !"".equals(bean.getRelationId())){
				admgt.deleteByRelationId(bean.getRelationId(), id);
			}*/
			//删除日程数据
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
					//获取数量
					//String num = this.twoSum(getLoginBean(request).getId(),selType);				
					request.setAttribute("msg", 3);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}		
		return getForward(request, mapping, "blank");
	}
	/**
	 * 修改待办
	 * @param bean
	 * @return
	 */
	public static String updateToDo(OAToDoBean bean) {

		if (null == bean.getTitle() || "".equals(bean.getId())) {
			return "标题不能为空";
		}
		if (null == bean.getCreateBy() || "".equals(bean.getCreateBy())) {
			return "创建人不能为空";
		}
		
		if (null == bean.getType() || "".equals(bean.getType())) {
			bean.setType("未分类");
		}
		if (null == bean.getStatus()) {
			bean.setStatus("0"); // 按理不存在，只是给个值
		}
		if (null == bean.getAlertTime()) {
			bean.setAlertTime("");
		}
		if (null == bean.getRef_taskId()) {
			bean.setRef_taskId("");
		}

		OAToDoBean beanOld = (OAToDoBean) new ToDoMgt().loadToDo(bean.getId()).retVal;
		if (beanOld == null) {
			return "找不到原来的记录";
		}
		if(beanOld.getAlertTime() == null){
			beanOld.setAlertTime("");
		}
		if (!beanOld.getAlertTime().equals(bean.getAlertTime())
				&& "1".equals(bean.getStatus())) {
			return "完成状态下不允许修改提醒时间";
		}

		Result rs = new ToDoMgt().updateToDo(bean);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			String now = BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss);

			if (!beanOld.getStatus().equals(bean.getStatus())) { // 修改状态
				if ("1".equals(bean.getStatus())) { // 未完成变成完成，添加日程
					new OACalendaMgt()
							.outAddCalendar(bean.getCreateBy(),
									bean.getTitle(), "我的待办", now, now,
									bean.getId(), "",null,"1","");
				} else if ("0".equals(bean.getStatus())) { // 完成变未完成，删日程
					new OACalendaMgt().delByRelationId(bean.getId());
				}
			} else if (!beanOld.getAlertTime().equals(bean.getAlertTime())) { // 状态和提醒时间不能同时修改
				if (!"".equals(bean.getAlertTime())) { // 有提醒时间就添加日程
					new OACalendaMgt().outAddCalendar(bean.getCreateBy(),
							bean.getTitle(), "我的待办", bean.getAlertTime(),
							bean.getAlertTime(), bean.getId(), "",null,"0","");
				} else { // 没提醒时间，就删日程
					new OACalendaMgt().delByRelationId(bean.getId());
				}
			}
			return ""; // 返回空字符串表示成功
		} else {
			return "更新数据库失败";
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
		String affix = getParameter("affix",request);//附件
		OAToDoBean bean = (OAToDoBean)mgt.loadToDo(id).retVal;
		//完成
		if("over".equals(updateFg)){
			String overOrNo = "YES".equals(request.getParameter("overOrNo")) ? "1" : "0";
			
			bean.setStatus(overOrNo);
			String err = updateToDo(bean);
			if("".equals(err)){
				//获取数量
				//String num = this.twoSum(getLoginBean(request).getId(),selType);							
				request.setAttribute("msg", 3);
			} else {					
				request.setAttribute("msg", "err:"+err);
			}
		}
		// 内容跟新
		if("update".equals(updateFg)){
				
			bean.setTitle(title);
			bean.setRelationId(clientId);
			bean.setType("".equals(selType)?"未分类":selType);
			bean.setRef_taskId(taskId);
			bean.setUploadFile(affix);
//			bean.setAlertTime(alertTime);
			String err = updateToDo(bean);
			if("".equals(err)){
				//处理附件
				if(affix!=null && !"".equals(affix)){
					for(String str :affix.split(";")){
						FileHandler.copy("OAToDo", FileHandler.TYPE_AFFIX, str, str);
						FileHandler.deleteTemp(str);
					}
				}
				//更改通知
				request.setAttribute("msg", id);		
			} else {					
				request.setAttribute("msg", "err:"+err);
			}
		}
		// 提醒设置 
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
							
				//加载数据
				Result rs = mgt.queryPre(id);				
				ArrayList rsrs = (ArrayList)rs.retVal;
				String tilte =  GlobalsTool.get(rsrs.get(0),0).toString();
				String type =  GlobalsTool.get(rsrs.get(0),1).toString();
				String afax = GlobalsTool.get(rsrs.get(0),8).toString();//附件
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
	 * 查询
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
			lvForm.setId(getParameter("id", request));//工作台进来的id
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
			
			
			//查询分类
			Result res = mgt.selectType(getLoginBean(request).getId()); 
			request.setAttribute("type", res.retVal);
			HashMap<String, String> map = new HashMap<String, String>();
			//封装map
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
			//获取数量
				String num = this.twoSum(getLoginBean(request).getId(),selType);
				String[] ssum = num.split(";");
				request.setAttribute("unOver", ssum[0]);
				request.setAttribute("oversd", ssum[1]);
			}
			
			
			//查询当前用户能选择的项目
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
