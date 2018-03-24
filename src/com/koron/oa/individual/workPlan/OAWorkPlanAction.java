package com.koron.oa.individual.workPlan;

import java.io.File;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bean.OADayWorkPlanBean;
import com.koron.oa.bean.OAPlanAssociateBean;
import com.koron.oa.individual.workPlan.OADateWorkPlanMgt.CheckDay;
import com.koron.oa.individual.workPlan.OADateWorkPlanMgt.WeekDay;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.PublicMgt;

/**
 * 
 * <p>
 * Title:�ҵĹ����ƻ�����
 * </p>
 * <p>
 * Description: ��Ҫ�����գ��£������깤���ƻ��������ܽ����ɾ�鿴���Լ������ƻ����ܣ��£�������ķ�ʽ��ʾ��
 * </p>
 * 
 * @Date:2010-5-7
 * @Copyright: �������
 * @Author ��СǮ    
 */
public class OAWorkPlanAction extends MgtBaseAction {

	private OADateWorkPlanMgt dmgt = new OADateWorkPlanMgt();
	private PublicMgt pubMgt = new PublicMgt();

	/**
	 * exe ��������ں���
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
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String noback=request.getParameter("noback");//���ܷ���
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD:
			String opType = getParameter("opType", request) ;
			if("delAssItem".equals(opType)){
				forward = delAssItem(mapping, form, request, response);
			}else if("addAssItem".equals(opType)){
				forward = addAssItem(mapping, form, request, response);
			}else if("commit".equals(opType)){
				forward = commit(mapping, form, request, response);
			}else if("summaryPrepare".equals(opType)){
				forward = summaryPrepare(mapping, form, request, response) ;
			}else if("summary".equals(opType)){
				forward = summary(mapping, form, request, response) ;
			}else{
				forward = add(mapping, form, request, response) ;
			}
			break;
		// ����ǰ��׼��
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response) ;
			break;
		// �޸�ǰ��׼��
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response) ;
			break;
		// �޸�
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response) ;
			break;
		// ����������ѯ
		case OperationConst.OP_QUERY:
			opType = getParameter("opType", request) ;
			if("leftMenu".equals(opType)){ 
				forward = frameLeft(mapping, form, request, response);
			}else if("eventWorkList".equals(opType)){
				forward = eventWorkList(mapping, form, request, response);
			}else if("param".equals(opType)){
				forward = param(mapping, form, request, response);
			}else if("check".equals(opType)){
				forward = check(mapping, form, request, response);
			}else if("calendar".equals(opType)){
				forward = calendar(mapping, form, request, response);
			}else if("selPerson".equals(opType)){
				forward = selPerson(mapping, form, request, response);
			}else{
				forward = query(mapping, form, request, response);
			}
			break;
		// ɾ��
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		// תҳ��
		case OperationConst.frameLeft:
			forward = frameLeft(mapping, form, request, response);
			break;
		default:
			forward = goMain(mapping, form, request, response); // �ҵĹ����ƻ���ҳ
		}
		return forward;
	}

	/**
	 * ɾ�������ƻ������ܽ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String[] keyId = request.getParameterValues("keyId");
		
		String strUserId = getParameter("userId", request); // �û�Id
		String strDate = getParameter("strDate", request); // ����
		String planType = getParameter("planType", request); // �ƻ�����
		String delType = request.getParameter("delType");
		String planId = request.getParameter("planId");
		String adUserId = request.getParameter("adUserId");
		String score = request.getParameter("score");
		String planStatus = request.getParameter("planStatus");
		
		Result rs = new Result();
		if("commit".equals(delType)){
			String commId = request.getParameter("commId");
			rs = dmgt.delCommit(commId,adUserId,planId);
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				new AdviceMgt().deleteByRelationId(planId, adUserId);
			}
		}else{
			rs = dmgt.delDayWorkPlan(keyId);
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				String ids = "";
				for (String id : keyId) {
					ids += id + ",";
				}
				new AdviceMgt().deleteByRelationId(ids, "");
			}
		}
		
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			if("commit".equals(delType)){
				
				EchoMessage.success().add(getMessage(request,"common.msg.delSuccess")).setBackUrl(
						"/OAWorkPlanAction.do?operation=4&planType="+planType+"&userId="+strUserId+"&strDate="+strDate+"&keyId="+keyId[0]+"&score="+score+"&planStatus="+planStatus)
					.setAlertRequest(request);
			}else{
				if("event".equals(planType)){
					//�����б�
					EchoMessage.success().add(getMessage(request,"common.msg.delSuccess")).setBackUrl(
							"/OAWorkPlanQueryAction.do?operation=4&opType=eventWorkList")
							.setAlertRequest(request);
				}else{
					EchoMessage.success().add(getMessage(request,"common.msg.delSuccess")).setBackUrl(
							"/OAWorkPlanAction.do?operation=4&planType="+planType+"&userId="+strUserId+"&strDate="+strDate+"&score="+score+"&planStatus="+planStatus+"&del=DELETE")
							.setAlertRequest(request);					
				}
				
			}
			
		} else {
			EchoMessage.error().add(getMessage(request,"common.msg.delFailture")).setAlertRequest(request);
		}
		return getForward(request,mapping,"alert");
	}

	
	/**
	 * �鿴�չ����ƻ������ܽ��������
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward param(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {


		if("true".equals(request.getParameter("save"))){			
			HashMap map = new HashMap();
			map.put("weekplanday",request.getParameter("weekplanday"));
			map.put("weekplanhour",request.getParameter("weekplanhour"));
			map.put("weekplanmin",request.getParameter("weekplanmin"));
			map.put("weeksumnext",request.getParameter("weeksumnext"));
			map.put("weeksumday",request.getParameter("weeksumday"));
			map.put("weeksumhour",request.getParameter("weeksumhour"));
			map.put("weeksummin",request.getParameter("weeksummin"));
			map.put("dayplanhour",request.getParameter("dayplanhour"));
			map.put("dayplanmin",request.getParameter("dayplanmin"));
			map.put("daysumnext",request.getParameter("daysumnext"));
			map.put("daysumall",request.getParameter("daysumall"));
			map.put("daysumday",request.getParameter("daysumday"));
			map.put("daysumhour",request.getParameter("daysumhour"));
			map.put("weekplanday",request.getParameter("weekplanday"));
			map.put("daysummin",request.getParameter("daysummin"));			
			Result rs = dmgt.updateParam(map);
			
			if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
					EchoMessage.success().add(getMessage(request,"common.msg.updateSuccess")).setBackUrl(
							"/OAWorkPlanAction.do?operation=4&opType=param")
							.setAlertRequest(request);
			} else {
				EchoMessage.error().add(getMessage(request,"common.msg.updateFailture")).setAlertRequest(request);
			}
			return getForward(request, mapping, "alert");
		}else{
			Result rs = dmgt.getParam();
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
				List<Object[]> list = (List<Object[]>)rs.retVal;
				HashMap map = new HashMap();
				for(Object[] os :list){
					map.put(os[0], os[1]);
				}
				request.setAttribute("result", map);
			}
			
			return getForward(request, mapping, "planParam");
			
		}		
	}
	
	private ActionForward calendar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
    	String userId = request.getParameter("userId");    	
    	String strDate = request.getParameter("strDate"); 
    	String planStatus=request.getParameter("planStatus");
    	if(strDate == null){
			strDate = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
		}
		String year  = strDate.substring(0,4);
		String month = strDate.substring(5,7);
		
		request.setAttribute("userId", userId);
		request.setAttribute("userName", OnlineUserInfo.getUser(userId).getName());
		request.setAttribute("strDate", strDate);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("planStatus", planStatus);
		long beginTimeout=System.currentTimeMillis();
		System.out.println(System.currentTimeMillis());
		if("true".equals(request.getParameter("getDate"))){
			//ȡ����			
			//String []seDate = getSEDate("month",strDate);	
			String start = request.getParameter("start");
			String end = request.getParameter("end");
			if(start != null && !"".equals(start)){
				start = start + " 00:00:00";
			}
			if(end != null && !"".equals(end)){
				end = end + " 23:59:59";
			}
			if(planStatus!=null && "0".equals(planStatus)){
				planStatus=null;
			}
			LoginBean loginBean=this.getLoginBean(request);
			Result rs = dmgt.getWorkPlan("day",userId, start, end ,planStatus,null,null,null,null,null,false,loginBean.getId());	
			ArrayList<OADayWorkPlanBean> list = (ArrayList<OADayWorkPlanBean>)rs.retVal;
			HashMap<OAPlanAssociateBean,ArrayList<OADayWorkPlanBean>> assMap = new HashMap();
			//��ѯ���еĹ�����
			rs = dmgt.getPlanAssociate();
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
				ArrayList<OAPlanAssociateBean> paList = (ArrayList<OAPlanAssociateBean>)rs.retVal;
				for(OAPlanAssociateBean pabean:paList){
					if(pabean.getIsEmployee().equals("1")){
						rs = dmgt.getAssociateWorkPlan("day",userId, start, end, pabean.getId());
						if(rs.retVal != null){
							assMap.put(pabean, (ArrayList<OADayWorkPlanBean>)rs.retVal);
						}
					}
				}
			}
			String mycalendar_str = "";
			String daysold = request.getParameter("daysold");
			int day = Integer.parseInt(daysold);
			//��װ�ҵĹ����ƻ�
			for(int i = 0;i<list.size(); i++){
				OADayWorkPlanBean bean = (OADayWorkPlanBean)list.get(i);
				String dayClendar = "";
				if (bean != null) {
					dayClendar = "{\"id\": \""+bean.getId()+"\",\"title\": \"" +GlobalsTool.encodeHTMLLine(bean.getTitle())+"\",\"start\": \""+bean.getBeginDate()+"\",\"end\": \""+bean.getEndDate()+"\",\"className\": \""+"\",\"summaryTime\": \""+(bean.getSummaryTime()==null?"":bean.getSummaryTime())+"\"}@koron@";
				}
				mycalendar_str += dayClendar;
			}
			
			//��װЭͬ��֪���ƻ�wyy
			/*for (OAPlanAssociateBean pabean : assMap.keySet()) {
				pabean.setName(pabean.getName().replace("��",""));
				for (OADayWorkPlanBean bean : assMap.get(pabean)) {
					String dayClendar = "";
					if(bean != null){
						dayClendar = "{\"id\": \""+bean.getId()+"\",\"title\": \"" + "["+OnlineUserInfo.getUser(bean.getCreateBy()).getName()+ pabean.getName()+"]"+GlobalsTool.encodeHTMLLine(bean.getTitle())+"\",\"start\": \""+bean.getBeginDate()+"\",\"end\": \""+bean.getEndDate()+"\",\"className\": \""+"\",\"summaryTime\": \""+(bean.getSummaryTime()==null?"":bean.getSummaryTime())+"\"}@koron@";
					}
					mycalendar_str += dayClendar;
				}
			}
			*/
			if(mycalendar_str!=null && mycalendar_str.length()>0){
				if(mycalendar_str.endsWith("@koron@")){
					mycalendar_str = mycalendar_str.substring(0,mycalendar_str.length()-7);
				}
			}
			request.setAttribute("msg", mycalendar_str);
			
			long over=System.currentTimeMillis()-beginTimeout;
			System.out.println("չʾ���� ��ʱ��"+over);
			return getForward(request, mapping, "blank");
		}else{		
			//չʾ����
			return getForward(request, mapping, "calendar");
		}
	}
		
	private ActionForward selPerson(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
	
			return getForward(request, mapping, "selPersonBox");
	}

	/**
	 * ���ܣ��£��������ѯ�ҵĹ����ƻ��͹����ܽ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String strUserId = getParameter("userId", request); // �û�Id
		String strDate = getParameter("strDate", request); // ����
		String planType = getParameter("planType", request); // �ƻ�����
		String associateId = getParameter("associateId", request); // ��������
		//String flagNews = getParameter("flagNews", request);//�жϴ�֪ͨ��Ϣ����
		
		//�رյ�ǰ����
		String del = getParameter("del", request);
		request.setAttribute("del", del);
		
		String score = getParameter("score",request) ;
		String keyId = getParameter("keyId",request);
		String planStatus = getParameter("planStatus",request);
		String planId = getParameter("planId", request) ;
		String opType = getParameter("opType",request);
		String windowOpen=getParameter("windowOpen",request);
		//��λ����
		HttpSession session = request.getSession();
		Object flagId = session.getAttribute("flagId");
		if(flagId !=null){
			request.setAttribute("flagId", flagId.toString());
			session.removeAttribute("flagId");
			planId = planId==null?flagId.toString():planId;
		}
		
		//�ж��Ƿ�ͨ��֪ͨͨ�����
		String flagAdvice=getParameter("flagAdvice",request);
		//�ж��Ƿ�ͨ������̨����
		String dsflag = getParameter("dsFlag", request);
		request.setAttribute("dsFlag", dsflag);
			
		if("overThing".equals(opType) && "0".equals(planStatus)){
			planStatus=null;
		}
		if(strUserId == null || strUserId.length() == 0){
			strUserId = this.getLoginBean(request).getId();
		}
		if(strDate == null || strDate.length() == 0){
			strDate = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
			opType = "";
		}
		if(planType == null || planType.length() == 0){
			planType = "day";
		}
		
		String planType2 = planType ;
		request.setAttribute("planType", planType);
		request.setAttribute("strDate", strDate);
		request.setAttribute("strUserId", strUserId);
		request.setAttribute("strUserName", OnlineUserInfo.getUser(strUserId).getName());
		request.setAttribute("associateId", associateId);
		request.setAttribute("score", score);
		request.setAttribute("keyId", keyId);
		request.setAttribute("planStatus", planStatus);	
		
		request.setAttribute("windowOpen", windowOpen);
		request.setAttribute("flagAdvice", flagAdvice);
		
		String[] seDate = getSEDate(planType,strDate);		
		if("week".equals(planType)){
			seDate = getSEDate("week",strDate);
			planType = "day" ;
		}else if("month".equals(planType)){
			seDate = getSEDate("month",strDate);
			planType = "day" ;
		}
		
		LoginBean loginBean=this.getLoginBean(request);
		List<String> weekPlanList=new ArrayList<String>();
		Result rs = null;
		System.out.println("-------"+getParameter("queryType", request));
		if("advance".equals(getParameter("queryType", request))){
			/*�߼���ѯ*/
			String keyWord = getParameter("keyWord",request);
			String searchPlanType = getParameter("searchPlanType",request);
			String beginTime = getParameter("beginTime",request);
			String endTime = getParameter("endTime",request);
			String planTitle = getParameter("planTitle", request);
			if(keyWord!=null && keyWord.trim().length()>0){
				keyWord = GlobalsTool.toChinseChar(keyWord) ;
				request.setAttribute("keyWord",keyWord);
			}
			if(planTitle!=null && planTitle.trim().length()>0){
				keyWord = planTitle;
				request.setAttribute("planTitle",planTitle);
			}
			
			request.setAttribute("searchPlanType",searchPlanType);
			request.setAttribute("beginTime",beginTime);
			request.setAttribute("endTime",endTime);
			request.setAttribute("queryType", "advance");
			rs = dmgt.queryWorkPlan(searchPlanType, getLoginBean(request).getId(), beginTime, endTime, keyWord) ;
			//request.setAttribute("weekPlan", rs.retVal) ;
		}else{
			//���ص�ǰ�û��������ܼƻ����¼ƻ�
			if("week".equals(planType2) || "month".equals(planType2)){
				//�����ܼƻ� �� �¼ƻ�
				boolean flag=false;
				if(associateId!=null){
					flag=true;
				}
				 if("search".equals(opType)){
					 rs = dmgt.getWorkPlan(planType,strUserId, seDate[0], seDate[1],null,"",planId,"search","","",flag,loginBean.getId());
				 }else{
					 rs = dmgt.getWorkPlan(planType2,strUserId, seDate[0], seDate[1],planStatus,null,null,null,"","",flag,loginBean.getId());
				 }
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
					if(!"advice".equals(flagAdvice)){
						request.setAttribute("weekPlan", rs.retVal) ;
					}					
					List<OADayWorkPlanBean> planList = (List<OADayWorkPlanBean>)rs.retVal ;
					if(planList!=null && planList.size()>0){
						for(int i=0;i<planList.size();i++){
						//	weekPlanId = planList.get(0).getId() ;
							weekPlanList.add(planList.get(i).getId());
						}
					
					}
				}
			}
			if(keyId !=null && keyId.length() >0){
				//ͨ������ID��ƻ���һ�������¼��ƻ�
				rs = dmgt.loadWorkPlan(keyId);
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS || rs.retVal == null){
					EchoMessage.error().add(this.getMessage(request, "common.msg.RET_ID_NO_VALUE_ERROR")).setClose().
	                setRequest(request);
					return getForward(request, mapping, "message");
				}
				ArrayList lb = new ArrayList<OADayWorkPlanBean>();
				lb.add(rs.retVal);
				rs.realTotal = 1;
				rs.retVal = lb;
			}else if(associateId==null || associateId.length() == 0){
				//�����ܼƻ��е��ռƻ�,�¼ƻ��в���Ҫ�����ռƻ�
				if("search".equals(opType)){
					rs = dmgt.getWorkPlan(planType,strUserId, seDate[0], seDate[1],planStatus,"",planId,"search","","",false,loginBean.getId());
				}else{
					rs = dmgt.getWorkPlan(planType,strUserId, seDate[0], seDate[1],planStatus,"",planId,null,"","",false,loginBean.getId());
				}
			}else{
				//��ȡЭͬ��֪�������ƻ�
				rs = dmgt.getAssociateWorkPlan(planType2,strUserId, seDate[0], seDate[1],associateId);
				request.setAttribute("assName", GlobalsTool.toChinseChar(getParameter("assName", request))) ;
			}
		}
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
			request.setAttribute("result", rs.retVal);
			List <OADayWorkPlanBean> list = (List<OADayWorkPlanBean>)rs.retVal;
			String ids[] = new String[list.size()+weekPlanList.size()];
			TreeMap<String, List <OADayWorkPlanBean>> dayMap = new TreeMap<String, List<OADayWorkPlanBean>>() ;
			HashMap<String, String> timeMap = new HashMap<String, String>() ;
			String empIds[] = new String[list.size()+1];			
			for(int i=0;i<list.size();i++){
				OADayWorkPlanBean plan = list.get(i) ;
				ids[i] = plan.getId();
				empIds[i] = list.get(i).getEmployeeID();
				if("week".equals(planType2) || "month".equals(planType2)){
					String beginDate = plan.getBeginDate().substring(0,10) ;
					if(dayMap.get(beginDate)==null){
						List <OADayWorkPlanBean> planList = new ArrayList <OADayWorkPlanBean>() ;
						planList.add(plan) ;
						dayMap.put(beginDate, planList) ;
					}else{
						List <OADayWorkPlanBean> planList = dayMap.get(beginDate) ;
						planList.add(plan) ;
					}
					
					if(timeMap.get(beginDate)==null){
						timeMap.put(beginDate, plan.getTime()) ;
					}else{
						if(plan.getTime()!=null && plan.getTime().trim().length()>0){
							double time = Double.parseDouble(timeMap.get(beginDate))+Double.parseDouble(plan.getTime()) ;
							timeMap.put(beginDate, String.valueOf(time)) ;
						}
					}
				}
			}
			if(!"advice".equals(flagAdvice)){
				for(int i=0;i<weekPlanList.size();i++){
					String newId=weekPlanList.get(i).toString();
					
					ids[ids.length+i-weekPlanList.size()]=newId;
				}
			}			
			request.setAttribute("dayMap", dayMap) ;
			request.setAttribute("timeMap", timeMap) ;
			//����strUserId�Լ�
			empIds[empIds.length-1] = strUserId;
			
			rs = dmgt.getPlanAssItem(ids,planType2);
			// ����Эͬ/�˵ļƻ���֪���˵ļƻ� ÿ��������
			Result PlanAssItemCount=dmgt.getPlanAssItemCount(planType2,strUserId, seDate[0], seDate[1],associateId);;
			request.setAttribute("AssItemCount", (ArrayList)PlanAssItemCount.retVal);
			if(rs.retVal!=null){
				if(!"event".equals(planType) && ((HashMap) rs.retVal).size()>0){
					HashMap<String, ArrayList<String[]>> mapItem2 = (HashMap<String, ArrayList<String[]>>) rs.retVal ;
					request.setAttribute("assItem", mapItem2);
					rs = dmgt.getPlanAssociate(mapItem2.values());
				}else{
					request.setAttribute("assItem", rs.retVal);
					rs = dmgt.getPlanAssociate();
				}
				request.setAttribute("associate", rs.retVal);
			}
			//��ѯ���Ƿ�Эͬ�ƻ���֪���ƻ�
			rs = dmgt.getPlanAssociate();
			request.setAttribute("existAssociate", rs.retVal);
			//��ѯ����
			rs = dmgt.getPlanCommit(ids);
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("commits", rs.retVal);
				//��ѯ�ظ�
				rs = dmgt.getPlanCommit2(ids) ;
				request.setAttribute("commitMap", rs.retVal) ;
			}
			
			//ȡ�����ƻ���ְԱ��Ϣ
			rs = dmgt.getEmployee(empIds);
			request.setAttribute("employeeMap", rs.retVal);
		}
	
		if("event".equals(planType)){
			return getForward(request, mapping, "workList");
		}else if("week".equals(planType2) || "month".equals(planType2)){
			return getForward(request, mapping, "weekList");
		}else{
			return getForward(request, mapping, "planList");
		}
	}
	
	private ActionForward eventWorkList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		OADayWorkPlanSearchForm myForm = (OADayWorkPlanSearchForm)form;
		MOperation mop = (MOperation)getLoginBean(request).getOperationMap().get("/OAWorkPlanQueryAction.do?operation=4&opType=eventWorkList");
		String scopeSQL = "" ;
		if(mop!=null){
			ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
			if (scopeRight != null && scopeRight.size()>0) {
				scopeSQL = " or " ;
				for (Object o : scopeRight) {
					LoginScopeBean lsb = (LoginScopeBean) o;
					if(lsb!=null && "1".equals(lsb.getFlag())){
						String empSQL = "" ;
						for(String strId : lsb.getScopeValue().split(";")){
							empSQL += "'"+strId+"'," ;
						}
						empSQL = empSQL.substring(0, empSQL.length()-1) ;
						scopeSQL += " a.employeeId in ("+empSQL+") or " ;
					}
					if(lsb!=null && "5".equals(lsb.getFlag())){
						String deptSQL = "" ;
						for(String strId : lsb.getScopeValue().split(";")){
							deptSQL += " a.departmentCode like '"+strId+"%' or " ;
						}
						if(deptSQL.endsWith("or ")){
							deptSQL = deptSQL.substring(0,deptSQL.length()-3) ;
						}
						scopeSQL += deptSQL +" or ";
					}
				}
				if(scopeSQL.endsWith("or ")){
					scopeSQL = scopeSQL.substring(0,scopeSQL.length()-3) ;
				}
			}
		}
		Result rs = dmgt.getEventWorkPlan(myForm.getDepartment(), myForm.getEmployee(), myForm.getTitle(), 
				myForm.getStatusId(), myForm.getBeginDate(), myForm.getEndDate(),getLoginBean(request).getId(),scopeSQL,myForm.getTypeId());
		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //��ѯ�ɹ�
            request.setAttribute("result", rs.retVal);
            request.setAttribute("pageBar", pageBar(rs, request));
            
            rs = dmgt.getPlanAssociate();
			request.setAttribute("associate", rs.retVal);
        } else {
            //��ѯʧ��
            EchoMessage.error().add(getMessage(request, "common.msg.error")).
                setRequest(request);
            return getForward(request, mapping, "message");
        }
		request.setAttribute("MOID", mop.moduleId);	
		request.setAttribute("OADayWorkPlanSearchForm", myForm);
		return getForward(request, mapping, "EventWorkList");
	}
	
	private ActionForward check(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		OADayWorkPlanCheckSearchForm myForm = (OADayWorkPlanCheckSearchForm)form;
		//��ѯ���õ�Ȩ����Ա
		String loginId = getLoginBean(request).getId();
		String bySeeId =  dmgt.queryPower(loginId);		
		
		String flag = getParameter("flag", request);
		if(flag == null || "".equals(flag)){
			if(myForm.getDepartmentName() !=null && !"".equals(myForm.getDepartmentName()) && myForm.getEmployee() !=null && !"".equals(myForm.getEmployee())){
				String rs = dmgt.isexsit(myForm.getEmployee(),myForm.getDepartment());
				if(rs.equals("flase")){
					  EchoMessage.error().add(new DynDBManager().getDefSQLMsg(getLocale(request).toString(), "ְԱ���ڴ˲�����")).setBackUrl("/OAWorkPlanCheckAction.do?operation=4&opType=check&flag=flag").
		                setAlertRequest(request);	   
					  return getForward(request, mapping, "message");	
				}			
			}
		}	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		List yearList = new ArrayList();
		
		//���㵱ǰ���ǰ5��
		int curYear = Integer.parseInt(sdf.format(new Date()));		
		int lYear = curYear;
		for(int i=0;i<=5;i++){
			yearList.add(lYear);
			lYear--;
		}
		request.setAttribute("yList", yearList);
		
		//���㵱ǰ��
		sdf = new SimpleDateFormat("M");
		int curM = Integer.parseInt(sdf.format(new Date()));
		request.setAttribute("curM", curM);
//		int curS = 1;
//		//��ǰ����
//		if(curM<4){
//			//һ����
//			curS = 1;
//		}else if(curM<7){
//			//������
//			curS = 2;
//		}else if(curM<10){
//			//������
//			curS = 3;
//		}else{
//			//�ļ���
//			curS = 4;
//		}		
//		request.setAttribute("curS", curS);
		
		String year = myForm.getMyear();
		int month = myForm.getMonth();
		if(null == year || year.length() ==0){
			year = curYear+"";
			month = curM;
		}
		//���ø��µ�����ͷ
		ArrayList<CheckDay> monthHead = dmgt.getMonthHead(year, month+"");
		request.setAttribute("monthHead", monthHead);
		//ȡ�ܵĶ�����
		HashMap weekLang = new HashMap();
		weekLang.put("0", this.getMessage(request, "data.lb.w0"));
		weekLang.put("1", this.getMessage(request, "data.lb.w1"));
		weekLang.put("2", this.getMessage(request, "data.lb.w2"));
		weekLang.put("3", this.getMessage(request, "data.lb.w3"));
		weekLang.put("4", this.getMessage(request, "data.lb.w4"));
		weekLang.put("5", this.getMessage(request, "data.lb.w5"));
		weekLang.put("6", this.getMessage(request, "data.lb.w6"));
		request.setAttribute("weekLang", weekLang);
		MOperation mop = (MOperation)getLoginBean(request).getOperationMap().get("/OAWorkPlanCheckAction.do?operation=4&opType=check");
		request.setAttribute("MOID", mop.moduleId);	
		String empSQL = "" ;
		String deptSQL = "" ;
		String scopeSQL = "" ;
		if(mop!=null){
			ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
			if (scopeRight != null && scopeRight.size()>0) {
				scopeSQL = " or " ;
				for (Object o : scopeRight) {
					LoginScopeBean lsb = (LoginScopeBean) o;
					if(lsb!=null && "1".equals(lsb.getFlag())){
						for(String strId : lsb.getScopeValue().split(";")){
							empSQL += "'"+strId+"'," ;
						}
						empSQL = empSQL.substring(0, empSQL.length()-1) ;
						scopeSQL += " a.employeeId in ("+empSQL+") or " ;
					}
					if(lsb!=null && "5".equals(lsb.getFlag())){
						for(String strId : lsb.getScopeValue().split(";")){
							deptSQL += " c.classcode like '"+strId+"%' or " ;
						}
						if(deptSQL.endsWith("or ")){
							deptSQL = deptSQL.substring(0,deptSQL.length()-3) ;
						}
						scopeSQL += deptSQL + " or ";
					}
				}
				if(scopeSQL.endsWith("or ")){
					scopeSQL = scopeSQL.substring(0,scopeSQL.length()-3) ;
				}
			}
		}
		Result rs = new Result();
		if(myForm != null && myForm.getMyear() != null){
			if(myForm.getPlanType().equals("day")){
				rs = dmgt.planDayCheck(myForm,monthHead,scopeSQL,loginId,bySeeId);
			}else if(myForm.getPlanType().equals("week")){
				ArrayList<WeekDay> weekHead = new ArrayList<WeekDay>();
				rs = dmgt.planWeekCheck(myForm,monthHead,weekHead,scopeSQL,loginId,bySeeId);
				request.setAttribute("weekHead", weekHead);
			}
		}else{
			return getForward(request, mapping, "planCheck");
		}
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            //��ѯ�ɹ�						
            request.setAttribute("result", rs.retVal);
            //request.setAttribute("pageBar", pageBar(rs, request));
        } else {
            //��ѯʧ��
            EchoMessage.error().add(getMessage(request, "common.msg.error")).
                setRequest(request);
            return getForward(request, mapping, "message");
        }
		return getForward(request, mapping, "planCheck");
	}
	
	private String[] getSEDate(String planType,String strDate) {
		String startDate = "";
		String endDate = "";
		//week season year
		
		//����깤���ƻ�
		if("year".equals(planType)){
			startDate = strDate.substring(0,4)+"-01-01 00:00:00";
			endDate = strDate.substring(0,4)+"-12-31 23:59:59";
		}else if("month".equals(planType)){
			Calendar lastDate = Calendar.getInstance();   
			try{
			lastDate.setTime(BaseDateFormat.parse(strDate,BaseDateFormat.yyyyMMdd)); 
			}catch(Exception e){e.printStackTrace();}
			lastDate.set(Calendar.DATE, 1);// ��Ϊ��ǰ�µ�1 ��  
			startDate = BaseDateFormat.format(lastDate.getTime(), BaseDateFormat.yyyyMMdd)+" 00:00:00";
			lastDate.add(Calendar.MONTH, 1);// ��һ���£���Ϊ���µ�1 ��   
			lastDate.add(Calendar.DATE, -1);// ��ȥһ�죬��Ϊ�������һ��  
			endDate = BaseDateFormat.format(lastDate.getTime(), BaseDateFormat.yyyyMMdd)+" 23:59:59";
		}else if("season".equals(planType)){
			/* ���������ڵڼ����ȡ�
			��һ���ȣ�1��1�ţ�3��31�� 
			�ڶ����ȣ�4��1�ţ�6��30��
			�������ȣ�7��1�ţ�9��30�� 
			���ļ��ȣ�10��1�ţ�12��31�� */

			String m = strDate.substring(5,7);
			if(m.startsWith("0")) m = m.substring(1);
			int i = Integer.parseInt(m);
			if(i<4){
				//һ����
				startDate = strDate.substring(0,4)+"-01-01 00:00:00";
				endDate = strDate.substring(0,4)+"-03-31 23:59:59";
			}else if(i<7){
				//������
				startDate = strDate.substring(0,4)+"-04-01 00:00:00";
				endDate = strDate.substring(0,4)+"-06-30 23:59:59";
			}else if(i<10){
				//������
				startDate = strDate.substring(0,4)+"-07-01 00:00:00";
				endDate = strDate.substring(0,4)+"-09-30 23:59:59";
			}else{
				//�ļ���
				startDate = strDate.substring(0,4)+"-10-01 00:00:00";
				endDate = strDate.substring(0,4)+"-12-31 23:59:59";
			}
		}else if("week".equals(planType)){
			/* ������������ */

			Calendar cal = Calendar.getInstance();
			try{
			cal.setTime(BaseDateFormat.parse(strDate, BaseDateFormat.yyyyMMdd));
			}catch(Exception e){e.printStackTrace();}
			
			int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
			cal.add(Calendar.DATE, -day_of_week);
			startDate = BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd)+" 00:00:00";
			cal.add(Calendar.DATE, 6);
			endDate = BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd)+" 23:59:59";			

		}else if("day".equals(planType)){
			startDate = strDate+" 00:00:00";
			endDate = strDate+" 23:59:59";
		}
		return new String[]{startDate,endDate};
	}


	/**
	 * �����ҵĹ����ƻ��͹����ܽ���ҳ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward goMain(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("planId", getParameter("planId", request)) ;
		String planId=getParameter("planId", request);
		return getForward(request, mapping, "planIndex");
	}

	/**
	 * ��������JS��ʽ d = new dTree('d'); d.add(0,-1,''); d.add(1,0,'','');
	 * d.add(2,0,'',''); d.add(3,1,'',''); d.add(4,0,'',''); d.add(5,3,'','');
	 * d.add(6,5,'',''); d.add(7,0,'',''); d.add(8,1,'',''); d.add(9,0,'','');
	 * d.add(10,9,'',''); d.add(11,9,'',''); d.add(12,0,'','');
	 */
	public String queryEmployeeTree(String strSQL) {
		Result rss = dmgt.queryEmployeeByDept(strSQL);
		StringBuffer strTree = new StringBuffer();
		if (rss.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			HashMap<String, List<String[]>> deptMap = (HashMap<String, List<String[]>>) rss.retVal ;
			int i = 1 ;
			int j = 1 ;
			for(String key : deptMap.keySet()){
				List<String[]> empList = deptMap.get(key) ;
				i = j++ ;
				if(empList==null || empList.size()==0) continue ;
				strTree.append("d.add("+i+",0,'"+key+"');") ;
				for(String[] emp : empList){
					strTree.append("d.add("+(j++)+","+i+",'"+emp[2]+"','javascript:clickTree(\\'"+ emp[3] + "\\')');") ;
				}
			}
		}
		return strTree.toString();
	}


	
	/**
	 * �޸��ҵ��չ����ƻ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		OADayWorkPlanForm planForm = (OADayWorkPlanForm) form;
		
		Result rs = dmgt.loadWorkPlan(planForm.getId());
		OADayWorkPlanBean planBean=null;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			planBean =(OADayWorkPlanBean)rs.retVal;
		}else{
			EchoMessage.error().add(getMessage(request,"common.msg.updateFailture")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		planBean.setBeginDate(planForm.getBeginDate());
		planBean.setEndDate(planForm.getEndDate());
		planBean.setTitle(planForm.getTitle());
		planBean.setContent(planForm.getContent());
		planBean.setGrade(planForm.getGrade());
		planBean.setLastUpdateBy(this.getLoginBean(request).getId());
		planBean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		String[] assoicates = request.getParameterValues("assoicate");
		if(assoicates !=null && assoicates.length>0){
			for (int i = 0; i < assoicates.length; i++) {
				assoicates[i] = assoicates[i].replace("/��ϵ��", "");
			}
		}
		
		String score = request.getParameter("score");
		String planStatus = request.getParameter("planStatus");
		
		// ����
		String mailAttaches = request.getParameter("attachFiles");
		mailAttaches = mailAttaches== null?"":mailAttaches;
		//��ɾ���ĸ���
		String delFiles = request.getParameter("delFiles");
		//��ɾ�������У�����ڸ�����Ҳ���ڣ������û�ɾ������������ӵģ����Բ�������ɾ�� 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){				
				File aFile = new File(BaseEnv.FILESERVERPATH + "/bbs/" + del);
				aFile.delete();
			}
		}			
		planBean.setAttach(mailAttaches) ;
		System.out.println(getParameter("queryType", request));
		String adt = null;
		if(planBean.getPlanType().equals("event")){
			adt= this.getMessage(request, "workplan.msg.note",OnlineUserInfo.getUser(planBean.getEmployeeID()).getName(),this.getMessage(request, "crm.event.plan")+
					"["+planBean.getTitle()+"]");
		}else{
			adt= this.getMessage(request, "workplan.msg.note",OnlineUserInfo.getUser(planBean.getEmployeeID()).getName(),this.getMessage(request, "oa.mydesk.workPlan")+
					"["+planBean.getTitle()+"]");
		}
		rs = dmgt.updateDayWorkPlan(planBean,assoicates,null,adt,getLoginBean(request));
				
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			AdviceMgt amgt = new AdviceMgt();
			List<String[]> listAdvice = (List<String[]>) rs.getRetVal();
			for (String[] val : listAdvice) {
				if (val.length == 2) {
					amgt.deleteByRelationId(val[0], val[1]);
				} else {
					amgt.add(val[0], val[1], val[2], val[3], val[4], val[5]);
				}
			}
			
			if(planBean.getPlanType().equals("event")){
				EchoMessage.success().add(getMessage(request,"common.msg.updateSuccess")).setBackUrl(
						"/OAWorkPlanAction.do?operation=4&planType="+planBean.getPlanType()+"&keyId="+planBean.getId())
						.setAlertRequest(request);
			}else{
				request.setAttribute("dealAsyn", "true");				
				HttpSession session = request.getSession();
				session.setAttribute("flagId", planForm.getId());
				EchoMessage.success().add(getMessage(request,"common.msg.updateSuccess")).setBackUrl(
						"/OAWorkPlanAction.do?operation=4&planType="+planBean.getPlanType()+"&userId="+planBean.getCreateBy()+"&strDate="+request.getParameter("strDate")+"&keyId="+request.getParameter("keyId")+"&score="+score+"&planStatus="+planStatus)
						.setAlertRequest(request);
				
			}
		} else {
			EchoMessage.error().add(getMessage(request,"common.msg.updateFailture")).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}


	 public boolean getDletedFile(String[] str_array, String str) {
		boolean talg = false;
		if (null != str_array) {
			for (String s : str_array) {
				if (s.equals(str)) {
					talg = true;
				}
			}
		}
		return talg;
	}
	
	/**
	 * �޸��ҵ��չ����ƻ����չ����ܽ�֮ǰ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String employeeID = getParameter("userId", request); // �û�Id
		String departmentCode = getParameter("departmentCode", request); // �û�Id
		String strDate = getParameter("strDate", request); // ����
		String planType = getParameter("planType", request); // �ƻ�����
		
		String score = request.getParameter("score");
		String planStatus = request.getParameter("planStatus");
		
		if(strDate == null || strDate.length() == 0){
			strDate = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
		}
		if(planType == null || planType.length() == 0){
			planType = "day";
		}
		
		if(employeeID == null || employeeID.length() == 0){
			employeeID = this.getLoginBean(request).getId();			
		}
		if(departmentCode == null || departmentCode.length() == 0){
			departmentCode = this.getLoginBean(request).getDepartCode();
		}
		if(strDate == null || strDate.length() == 0){
			strDate = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
		}
		
		request.setAttribute("planType", planType);
		request.setAttribute("strDate", strDate);
		request.setAttribute("score", score);
		request.setAttribute("planStatus", planStatus);
		
		String keyId = getParameter("keyId", request); // �ƻ�����
		String planId = getParameter("planId", request); // �ƻ�����
		request.setAttribute("keyId", keyId);
				
		Result rs = dmgt.loadWorkPlan(planId);
		//��ԃ�Ƿ����ģ��
		OADayWorkPlanBean bean = (OADayWorkPlanBean)rs.retVal;
		request.setAttribute("result", bean);
		
		rs = dmgt.getPlanAssociate();
		request.setAttribute("associate", rs.retVal);
		if(bean !=null){
			rs = dmgt.getPlanAssItem(new String[]{bean.getId()},planType);
			request.setAttribute("assItem", rs.retVal);				
		}
		if("event".equals(planType)){
			return getForward(request, mapping, "addDayWork");
		}else{
			return getForward(request, mapping, "planAdd");
		}
	}
	
	private ActionForward summaryPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String employeeID = getParameter("userId", request); // �û�Id
		String departmentCode = getParameter("departmentCode", request); // �û�Id
		String strDate = getParameter("strDate", request); // ����
		String planType = getParameter("planType", request); // �ƻ�����
		String planId = getParameter("planId", request);
		String keyId = request.getParameter("keyId");
		String score = request.getParameter("score");
		String planStatus = request.getParameter("planStatus");
		
		if(strDate == null || strDate.length() == 0){
			strDate = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
		}
		if(planType == null || planType.length() == 0){
			planType = "day";
		}
		
		if(employeeID == null || employeeID.length() == 0){
			employeeID = this.getLoginBean(request).getId();			
		}
		if(departmentCode == null || departmentCode.length() == 0){
			departmentCode = this.getLoginBean(request).getDepartCode();
		}
		if(strDate == null || strDate.length() == 0){
			strDate = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
		}
		
		request.setAttribute("planType", planType);
		request.setAttribute("planId", planId);
		request.setAttribute("strDate", strDate);
		request.setAttribute("keyId", keyId);
		request.setAttribute("score", score);
		request.setAttribute("planStatus", planStatus);
		
		Result rs = dmgt.loadWorkPlan(planId);
		//��ԃ�Ƿ����ģ��
		OADayWorkPlanBean bean = (OADayWorkPlanBean)rs.retVal;
		
		if(bean.getSummaryTime() == null || bean.getSummaryTime().length()==0){
			bean.setSummary(dmgt.getTemplate(planType, employeeID, departmentCode, "1"));
		}		
		request.setAttribute("result", bean);
		
		rs = dmgt.getPlanAssociate();
		request.setAttribute("associate", rs.retVal);
		rs = dmgt.getPlanAssItem(new String[]{bean.getId()},planType);
		request.setAttribute("assItem", rs.retVal);
		rs = dmgt.getGoalTypeItem(this.getLocale(request).toString(), bean.getId());
		request.setAttribute("goalTypeItem", rs.retVal);
		rs = dmgt.getPlanCommit(request.getParameterValues("planId"));
		request.setAttribute("commits", rs.retVal);
		
		return getForward(request, mapping, "sumaryAdd");
	}



	/**
	 * ����ҵ��չ����ƻ����չ����ܽ�֮ǰ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String employeeID = getParameter("userId", request); // �û�Id
		String departmentCode = getParameter("departmentCode", request); // �û�Id
		String strDate = getParameter("strDate", request); // ����
		String planType = getParameter("planType", request); // �ƻ�����
		String keyId = getParameter("keyId", request); // �ƻ�����
		String score = request.getParameter("score");
		String planStatus = request.getParameter("planStatus");
		
		String enterFrom = request.getParameter("enterFrom");//enterFrom=CRMBrother��ʾ��CRM�ھӱ�����Ӽƻ�
		
		if(strDate == null || strDate.length() == 0){
			strDate = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
		}
		if(planType == null || planType.length() == 0){
			planType = "day";
		}
		
		if(employeeID == null || employeeID.length() == 0){
			employeeID = this.getLoginBean(request).getId();			
		}
		if(departmentCode == null || departmentCode.length() == 0){
			departmentCode = this.getLoginBean(request).getDepartCode();
		}
		if(strDate == null || strDate.length() == 0){
			strDate = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
		}
		
		ActionForward forward = getForward(request, mapping, "addDayWork");
		if(!employeeID.equals(this.getLoginBean(request).getId())){
			EchoMessage.error().add(this.getMessage(request, "workplan.msg.addself")).setNotAutoBack().setAlertRequest(request);
//					setBackUrl(
//					"/OAWorkPlanAction.do")
//			
			return getForward(request, mapping, "alert");
		}
		
		//����CRM�ھӱ����ƻ���������ȡ�ͻ���Ϣ
		if(enterFrom!=null && "CRMBrother".equals(enterFrom)){
			String clientId = getParameter("clientId",request);
			Result result = pubMgt.findClient(clientId);
			List<Object> list = (List<Object>)result.retVal;
			String clientName = GlobalsTool.get(list.get(0),0).toString();
			String clientInfo = clientId+";"+clientName+";";
			request.setAttribute("clientInfo", clientInfo);
		}
		
		
		request.setAttribute("planType", planType);
		request.setAttribute("strDate", strDate);
		request.setAttribute("calendar", request.getParameter("calendar"));
		request.setAttribute("score", score);
		request.setAttribute("planStatus", planStatus);
		request.setAttribute("enterFrom", enterFrom);
		
		String[] seDate = getSEDate(planType,strDate);
		
		
		String title="";
		if("year".equals(planType)){
			title = strDate.substring(0,4)+this.getMessage(request, "oa.type.year")+this.getMessage(request, "oa.mydesk.workPlan");
		}else if("season".equals(planType)){
			int j = 1;
			String m = strDate.substring(4,7);
			if(m.compareTo("04")<0){
				j = 1;
			}else if(m.compareTo("07")<0){
				j = 2;
			}else if(m.compareTo("10")<0){
				j = 3;
			}else {
				j = 4;
			}
			title = strDate.substring(0,4)+this.getMessage(request, "oa.type.year")+this.getMessage(request, "common.theNo")+j+this.getMessage(request, "oa.type.season")+this.getMessage(request, "oa.mydesk.workPlan");
		}else if("month".equals(planType)){
			title = strDate.substring(0,7)+this.getMessage(request, "oa.type.month")+this.getMessage(request, "oa.mydesk.workPlan");
		}else if("week".equals(planType)){
			Calendar cal=Calendar.getInstance(); 
			try{
			cal.setTime(BaseDateFormat.parse(strDate, BaseDateFormat.yyyyMMdd));
			}catch(Exception e){}
			int i = cal.get(Calendar.WEEK_OF_YEAR);			
			if("12".equals(strDate.substring(5, 7)) && i==1){
				
				int year = Integer.parseInt(strDate.substring(0,4))+1;
				title = String.valueOf(year)+this.getMessage(request, "oa.type.year")+this.getMessage(request, "common.theNo")+i+this.getMessage(request, "oa.type.week")+this.getMessage(request, "oa.mydesk.workPlan");
			}else{
				title = strDate.substring(0,4)+this.getMessage(request, "oa.type.year")+this.getMessage(request, "common.theNo")+i+this.getMessage(request, "oa.type.week")+this.getMessage(request, "oa.mydesk.workPlan");
			}			
		}else  if("day".equals(planType)){
			seDate[0] = strDate+" 09:00:00";
			seDate[1] = strDate+" 18:00:00";
		}
		OADayWorkPlanBean bean = new OADayWorkPlanBean();
		bean.setTitle(title);
		bean.setGrade("3");
		bean.setBeginDate(seDate[0]);
		bean.setEndDate(seDate[1]);
		bean.setEmployeeID(employeeID);
		bean.setDepartmentCode(departmentCode);
		bean.setPlanType(planType);
		bean.setContent(dmgt.getTemplate(planType, employeeID, departmentCode, "0"));
		LoginBean loginBean=this.getLoginBean(request);
		//��ԃ�Ƿ����ģ��
		String planId = request.getParameter("planId");
		if(planId != null && planId.length() > 0){
			if(!planType.equals("day")&&!planType.equals("event")){
				//��ѯ�����Ƿ�δ��ӹ������ƻ�
				Result rs = dmgt.getWorkPlan(planType,employeeID, seDate[0], seDate[1],null,null,null,null,null,null,false,loginBean.getId());
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && ((List)rs.retVal).size()>0){
					EchoMessage.error().add(this.getMessage(request, "workplan.msg.dbadd")).setClose()
					.setAlertRequest(request);
					return getForward(request, mapping, "alert");
				}
			}
			Result rs = dmgt.loadWorkPlan(planId);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				OADayWorkPlanBean b = (OADayWorkPlanBean)rs.retVal;
				if(bean.getPlanType().equals("day") || bean.getPlanType().equals("event")){
					bean.setTitle(b.getTitle());
				}
				bean.setGrade(b.getGrade());
				bean.setContent(b.getContent());
			}
			//���ø��Ʋ���
			request.setAttribute("copy", "true");
			request.setAttribute("oldPlanType", request.getParameter("oldPlanType"));
			request.setAttribute("oldStrDate", request.getParameter("oldStrDate"));
			request.setAttribute("oldUserId", request.getParameter("oldUserId"));
			request.setAttribute("keyId", keyId);
		}		
		request.setAttribute("result", bean);
		
		Result rs = dmgt.getPlanAssociate();
		request.setAttribute("associate", rs.retVal);
		if("event".equals(planType)){
			return getForward(request, mapping, "addDayWork");
		}else{
			return getForward(request, mapping, "planAdd");
		}

	}
	
	private ActionForward summary(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		OADayWorkPlanForm planForm = (OADayWorkPlanForm) form;
		Result rs = dmgt.loadWorkPlan(planForm.getId());
		OADayWorkPlanBean planBean=null;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			planBean =(OADayWorkPlanBean)rs.retVal;
		}else{
			EchoMessage.error().add(getMessage(request,"common.msg.updateFailture")).setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}

		planBean.setLastUpdateBy(this.getLoginBean(request).getId());
		planBean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		planBean.setTime(planForm.getTime());
		planBean.setSummary(planForm.getSummary());
		planBean.setStatusId(planForm.getStatusId());
		if(planBean.getSummaryTime() == null || planBean.getSummaryTime().length() == 0){
			planBean.setSummaryTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		}
		String score = request.getParameter("score");
		String planStatus = request.getParameter("planStatus");
		String flagId = request.getParameter("flagId");
		// ����
		String mailAttaches = request.getParameter("attachFiles");
		mailAttaches = mailAttaches== null?"":mailAttaches;
		//��ɾ���ĸ���
		String delFiles = request.getParameter("delFiles");
		//��ɾ�������У�����ڸ�����Ҳ���ڣ������û�ɾ������������ӵģ����Բ�������ɾ�� 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){				
				File aFile = new File(BaseEnv.FILESERVERPATH + "/bbs/" + del);
				aFile.delete();
			}
		}			
		planBean.setAttach(mailAttaches) ;
		
		String[] assoicates = request.getParameterValues("assoicate");
		
		//�ƻ�Ŀ��
		String gItems[] = request.getParameterValues("goalTypeItem");
		ArrayList<String> gLst = new ArrayList();
		if(gItems != null && gItems.length > 0){
			for(String gi :gItems){
				String o = request.getParameter("gi_"+gi);
				try{ double scored = Double.parseDouble(o); gLst.add(gi+":"+scored); }catch(Exception e){}
			}
		}
		String adt = null;
		if(planBean.getPlanType().equals("event")){
			adt= this.getMessage(request, "workplan.msg.note",OnlineUserInfo.getUser(planBean.getEmployeeID()).getName(),this.getMessage(request, "crm.event.plan")+
					"["+planBean.getTitle()+"]");
		}else{
			adt= this.getMessage(request, "workplan.msg.note",OnlineUserInfo.getUser(planBean.getEmployeeID()).getName(),this.getMessage(request, "oa.mydesk.workPlan")+
					"["+planBean.getTitle()+"]");
		}
		
		rs = dmgt.updateDayWorkPlan(planBean,assoicates,gLst.toArray(new String[]{}),adt,getLoginBean(request));
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			AdviceMgt amgt = new AdviceMgt();
			List<String[]> listAdvice = (List<String[]>) rs.getRetVal();
			for (String[] val : listAdvice) {
				if (val.length == 2) {
					amgt.deleteByRelationId(val[0], val[1]);
				} else {
					amgt.add(val[0], val[1], val[2], val[3], val[4], val[5]);
				}
			}
			//�����¼����ܣ���
			if(!"event".equals(planBean.getPlanType())){
				request.setAttribute("dealAsyn", "true");
			}		
			HttpSession session = request.getSession();
			session.setAttribute("flagId",flagId);
			EchoMessage.success().add(getMessage(request,"common.msg.addSuccess")).setBackUrl(
					"/OAWorkPlanAction.do?operation=4&planType="+planBean.getPlanType()+"&strType=strDay&userId="+planBean.getCreateBy()+"&strDate="+request.getParameter("strDate")+
					"&keyId="+request.getParameter("keyId")+"&score="+score+"&planStatus="+planStatus)
					.setAlertRequest(request);
			
		} else {
			EchoMessage.error().add(getMessage(request,"common.msg.updateFailture")).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * ����ҵ��չ����ƻ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		OADayWorkPlanForm planForm = (OADayWorkPlanForm) form;
		OADayWorkPlanBean planBean = new OADayWorkPlanBean();		
		this.read(planForm, planBean);		
		planBean.setCreateBy(this.getLoginBean(request).getId());
		planBean.setLastUpdateBy(this.getLoginBean(request).getId());
		planBean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		planBean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		planBean.setEmployeeID(this.getLoginBean(request).getId());
		planBean.setDepartmentCode(this.getLoginBean(request).getDepartCode());
		planBean.setId(IDGenerater.getId());
		
		// ����
		String mailAttaches = request.getParameter("attachFiles");
		mailAttaches = mailAttaches== null?"":mailAttaches;
		//��ɾ���ĸ���
		String delFiles = request.getParameter("delFiles");
		//��ɾ�������У�����ڸ�����Ҳ���ڣ������û�ɾ������������ӵģ����Բ�������ɾ�� 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){				
				File aFile = new File(BaseEnv.FILESERVERPATH + "/bbs/" + del);
				aFile.delete();
			}
		}			
		planBean.setAttach(mailAttaches) ;
		
		String score = request.getParameter("score");
		String planStatus = request.getParameter("planStatus");
		
				
		String[] assoicates = request.getParameterValues("assoicate");
		if(assoicates !=null){
			if(assoicates.length>0){
				for (int i = 0; i < assoicates.length; i++) {
					assoicates[i] = assoicates[i].replace("/��ϵ��", "");
				}
			}
		}	
		String adt = null;
		if(planBean.getPlanType().equals("event")){
			adt= this.getMessage(request, "workplan.msg.note",OnlineUserInfo.getUser(planBean.getEmployeeID()).getName(),this.getMessage(request, "crm.event.plan")+
					"["+planBean.getTitle()+"]");
		}else{
			adt= this.getMessage(request, "workplan.msg.note",OnlineUserInfo.getUser(planBean.getEmployeeID()).getName(),this.getMessage(request, "oa.mydesk.workPlan")+
					"["+planBean.getTitle()+"]");
		}
		Result rs = dmgt.addDayWorkPlan(planBean,assoicates,adt,getLoginBean(request));
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			AdviceMgt amgt = new AdviceMgt();
			List<String[]> listAdvice = (List<String[]>) rs.getRetVal();
			for (String[] val : listAdvice) {
				if (val.length == 2) {
					amgt.deleteByRelationId(val[0], val[1]);
				} else {
					amgt.add(val[0], val[1], val[2], val[3], val[4], val[5]);
				}
			}
			
			if("true".equals(request.getParameter("copy"))){
				EchoMessage.success().add(this.getMessage(request, "com.auto.succeed")).setRequest(request);
				
				request.getSession().setAttribute("noback", true);
				request.getSession().setAttribute("BUTTON", "CLOSE");
				request.getSession().setAttribute("BACK_URL", "window.close();");
			}else if(planBean.getPlanType().equals("event")){
				EchoMessage.success().add(getMessage(request,"common.msg.addSuccess")).setBackUrl(
						"/OAWorkPlanQueryAction.do?operation=4&opType=eventWorkList&winCurIndex="+request.getParameter("winCurIndex"))
						.setAlertRequest(request);
			}else{
				String opType="&operation=4";
				if("addNew".equals(getParameter("submitType", request))){
					opType = "&operation=6" ;
				}else{
					request.setAttribute("dealAsyn", "true");
				}
				if("true".equals(request.getParameter("calendar"))){
					opType="&opType=calendar";
				}
				EchoMessage.success().add(getMessage(request,"common.msg.addSuccess")).setBackUrl(
						"/OAWorkPlanAction.do?planType="+planBean.getPlanType()+"&strType=strDay&userId="+planBean.getCreateBy()+"&strDate="+request.getParameter("strDate")+opType+"&score="+score+"&planStatus="+planStatus)
						.setAlertRequest(request);
			}
		} else {
			EchoMessage.error().add(getMessage(request,"common.msg.addFailture")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	private ActionForward commit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String planId = request.getParameter("planId");
		String commitId = request.getParameter("commitId");
		String keyId = request.getParameter("keyId");
		String commitType = request.getParameter("commitType"); // 0
		String commit = request.getParameter("commit");	//����
		String planType = request.getParameter("planType");  //���� ���� day.
		String strDate = request.getParameter("strDate");
		String strUserId = request.getParameter("strUserId");//�����ƻ�����û�ID
		String planEmployeeId = request.getParameter("planEmployeeId"); //�����ƻ�Ա��ID
		String planTitle = request.getParameter("planTitle");
		String score = request.getParameter("score");
		String planStatus = request.getParameter("planStatus");
		String associateId=request.getParameter("associateId"); //��ʶ �ӹ����ƻ� ����Эͬ�ƻ���������ġ�
		String flagAdvice=request.getParameter("flagAdvice");
		String adviceTitle="";
		List<String> userIds=null;
		Result rs=null;
		 Result rss=dmgt.selComPerson(commitId);
		 List<String> empIds=(List<String>)rss.getRetVal();
			 if(rss.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
				 if(empIds!=null && empIds.size()>0){
					  userIds=empIds;
					  adviceTitle = new DynDBManager().getDefSQLMsg(getLocale(request).toString(), this.getMessage(request, "oa.mydesk.workPlan")+"["+planTitle+"]"+"���µĵ���");
					  //adviceTitle = this.getMessage(request, "workplan.msg.newcommit", this.getMessage(request, "oa.mydesk.workPlan")+"["+planTitle+"]");
					  if(planType.equals("event")){
						  adviceTitle = this.getMessage(request, "workplan.msg.newcommit", this.getMessage(request, "crm.event.plan")+"["+planTitle+"]");
					  }
					  rs = dmgt.commit(planId, commitType, commit, this.getLoginBean(request).getId(), BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss),
								adviceTitle,planEmployeeId,OnlineUserInfo.getUser(planEmployeeId).name,planType,commitId,userIds,planTitle);
				 }else{
					 adviceTitle = new DynDBManager().getDefSQLMsg(getLocale(request).toString(), this.getMessage(request, "oa.mydesk.workPlan")+"["+planTitle+"]"+"���µĵ���");
					 //adviceTitle = this.getMessage(request, "workplan.msg.newcommit", this.getMessage(request, "oa.mydesk.workPlan")+"["+planTitle+"]");
					 if(planType.equals("event")){
						 adviceTitle = this.getMessage(request, "workplan.msg.newcommit", this.getMessage(request, "crm.event.plan")+"["+planTitle+"]");
					 }
					 rs = dmgt.commit(planId, commitType, commit, this.getLoginBean(request).getId(), BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss),
								adviceTitle,planEmployeeId,OnlineUserInfo.getUser(planEmployeeId).name,planType,commitId,null,planTitle);
				 }
			 }
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			if("event".equals(planType)){
				EchoMessage.success().add(getMessage(request,"oa.bbs.operationOk")).setBackUrl(
						"/OAWorkPlanAction.do?operation=4&planType="+planType+"&flagAdvice="+flagAdvice+"&associateId="+associateId+"&keyId="+planId)
						.setAlertRequest(request);
			}else{
				if(associateId!=null && associateId.length()>0){
				 EchoMessage.success().add(getMessage(request,"oa.bbs.operationOk")).setBackUrl(
							"/OAWorkPlanAction.do?operation=4&planType="+planType+"&flagAdvice="+flagAdvice+"&userId="+strUserId+"&strDate="+strDate+"&keyId="+keyId+"&associateId="+associateId)
							.setAlertRequest(request);
				}else{
					///OAWorkPlanAction.do?operation=4&userId=7237978a_0912251628128400201&planType=day&strDate=2012-04-11
					EchoMessage.success().add(getMessage(request,"oa.bbs.operationOk")).setBackUrl(
						"/OAWorkPlanAction.do?operation=4&planType="+planType+"&flagAdvice="+flagAdvice+"&userId="+strUserId+"&strDate="+strDate+"&keyId="+keyId+"&score="+score+"&planStatus="+planStatus)
						.setAlertRequest(request);
				}
			}
		} else {
			EchoMessage.error().add(getMessage(request,"common.alert.updateFailure")).setBackUrl(
					"/OAWorkPlanAction.do?operation=4&planType="+planType+"&flagAdvice="+flagAdvice+"&userId="+strUserId+"&strDate="+strDate+"&keyId="+keyId+"&score="+score+"&planStatus="+planStatus).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	

	
	private ActionForward delAssItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String planId = request.getParameter("planId");
		String assId = request.getParameter("assId");
		String keyId = request.getParameter("keyId");
		Result rs = dmgt.delAssItem(planId, assId, keyId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			new AdviceMgt().deleteByRelationId(planId, keyId);
			request.setAttribute("msg", this.getMessage(request, "com.auto.succeed"));
		}else{
			request.setAttribute("msg", this.getMessage(request, "com.auto.succeed"));
		}
		return getForward(request, mapping, "blank");
	}
	private ActionForward addAssItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String planId = request.getParameter("planId");
		String planType = request.getParameter("planType");
		String assId = request.getParameter("assId");
		String keyId = request.getParameter("keyId");
		String keyName = request.getParameter("keyName");
		String isemployee = request.getParameter("isemployee");
		String assName = request.getParameter("assName");
		try{
			keyName = new String(keyName.getBytes("ISO8859-1"),"UTF-8");
			assName = new String(assName.getBytes("ISO8859-1"),"UTF-8");
		}catch(Exception e){}
		Result rs = dmgt.loadWorkPlan(planId);
		OADayWorkPlanBean bean =(OADayWorkPlanBean)rs.retVal;
		String etTitle = bean==null?"":bean.getTitle();
		
		String adt = null;
		if(bean.getPlanType().equals("event")){
			adt= this.getMessage(request, "workplan.msg.note",OnlineUserInfo.getUser(bean.getEmployeeID()).getName(),this.getMessage(request, "crm.event.plan")+
					"["+bean.getTitle()+"]");
		}else{
			adt= this.getMessage(request, "workplan.msg.note",OnlineUserInfo.getUser(bean.getEmployeeID()).getName(),this.getMessage(request, "oa.mydesk.workPlan")+
					"["+bean.getTitle()+"]");
		}
		
		
		//assId,final String keyId,final String keyName,final String isemployee,final String assName,final String adviceTitle
		rs = dmgt.addAssItem(planId,planType, assId, keyId,keyName,isemployee,assName,adt);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){

			if("1".equals(isemployee)){
				String tres= "oa.mydesk.workPlan";
				if("event".equals(planType)){
					tres = "crm.event.plan";
				}
    	        new AdviceMgt().add("1", adt+assName, 
    	        		"<a href=\"javascript:mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&planType="+planType+"&keyId="+planId+"','RES<"+tres+">')\">"+adt+assName+"</a>", 
    	        		keyId, planId, "plan");
			}
			request.setAttribute("msg", this.getMessage(request, "com.auto.succeed"));
		}else{
			request.setAttribute("msg", this.getMessage(request, "com.auto.succeed"));
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * ��ѯ��ǰ�û��Ƿ��з���ĳ�������ƻ�
	 */
	public boolean isVisitWorkPlan(ArrayList scopeRight, String deptCode,
			String userId) {
		if (scopeRight != null) {
			boolean hasDept = false;
			boolean hasEmp = false;
			for (Object o : scopeRight) {
				LoginScopeBean lsb = (LoginScopeBean) o;
				if ("tblDepartment".equals(lsb.getTableName())) {
					if (lsb.getScopeValue().contains(deptCode)) {
						hasDept = true;
					}
				} else if ("tblEmployee".equals(lsb.getTableName())) {
					if (lsb.getScopeValue().contains(userId)) {
						hasEmp = true;
					}
				}

			}
			if (hasDept || hasEmp) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private ActionForward frameLeft(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		MOperation mop = (MOperation)getLoginBean(request).getOperationMap().get("/OAWorkPlanAction.do");
		String empSQL = "" ;
		String deptSQL = "" ;
		String strSQL = "" ;
		if(mop!=null){
			ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
			if (scopeRight != null && scopeRight.size()>0) {
				strSQL = " and (" ;
				for (Object o : scopeRight) {
					LoginScopeBean lsb = (LoginScopeBean) o;
					if(lsb!=null && "1".equals(lsb.getFlag())){
						empSQL += " b.id in (" ;
						for(String strId : lsb.getScopeValue().split(";")){
							empSQL += "'"+strId+"'," ;
						}
						empSQL = empSQL.substring(0, empSQL.length()-1) ;
						empSQL += ") or " ;
						strSQL += empSQL ;
					}
					if(lsb!=null && "5".equals(lsb.getFlag())){
						for(String strId : lsb.getScopeValue().split(";")){
							deptSQL += " a.classCode like '"+strId+"%' or " ;
						}
						//deptSQL = deptSQL.substring(0, deptSQL.length()-3) ;
						strSQL += deptSQL ;
					}
				}
				if(strSQL.endsWith("or ")){
					strSQL = strSQL.substring(0,strSQL.length()-3) ;
				}
				strSQL += ")" ;
			}
		}
		// ��ѯְԱ��
		String employeeTree = "" ;
		if((strSQL!=null && strSQL.trim().length()>0) || "1".equals(getLoginBean(request).getId())){
			employeeTree = queryEmployeeTree(strSQL);
		}
		request.setAttribute("treeEployee", employeeTree);
		return getForward(request, mapping, "frameleft");
	}

}
