package com.koron.hr.workRule;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.hr.workRule.DutyPeriodsMgt;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.SquadInfoBean;
import com.menyi.aio.bean.SquadSect;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class DutyPeriodsAction extends MgtBaseAction {

	DutyPeriodsMgt mgt = new DutyPeriodsMgt();
	WorkRuleReportMgt workMgt = new WorkRuleReportMgt();
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int opration = 0 == getOperation(request)?4:getOperation(request);
		int type = Integer.parseInt(null == request.getParameter("type")?"0":
			request.getParameter("type"));

		ActionForward forward = null;
		switch (opration) {
		case OperationConst.OP_ADD:
			if(type == 1){
				forward = addSquad(mapping, form, request, response);
			}else{
				forward = addPeriods(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_QUERY:
			if(type == 1){
				forward = searchSquad(mapping, form, request, response);
			}else{
				forward = searchPeriods(mapping, form, request, response);
			}
			break;
		}
		return forward;
	}

	protected ActionForward addPeriods(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List<KeyPair> periods = new ArrayList<KeyPair>();
		List empList = new ArrayList();
		List decList = new ArrayList();
		for(int i = 0;;i++){
			if(null == request.getParameter("myDate"+i)){
				break;
			}
			periods.add(new KeyPair(request.getParameter("myDate"+i),
					request.getParameter("squadEnactmentNo"+i)));
		}
		
		for(int i = 0;;i++){
			if(null == request.getParameter("empNumber"+i)){
				break;
			}
			empList.add(request.getParameter("empNumber"+i));
		}
		
		for (int i = 0;; i++) {
			if(null == request.getParameter("decNumber"+i)){
				break;
			}
			decList.add(request.getParameter("decNumber"+i));
		}
		String periodsName = request.getParameter("periodsName");
		String id = this.getLoginBean(request).getId();
		Result result = mgt.addPeriods(empList,decList,periods, id,periodsName);

		if (ErrorCanst.DEFAULT_SUCCESS == result.getRetCode()) {
			EchoMessage.success().add(
					getMessage(request, "common.msg.addSuccess")).setBackUrl(
					"/DutyPeriodsAction.do?type=2").setAlertRequest(request);

			return getForward(request, mapping, "message");
		}
		EchoMessage.error().add(getMessage(request, "common.msg.addFailture"))
				.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

	protected ActionForward searchPeriods(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		DutyPeriodsForm dutyForm = (DutyPeriodsForm) form;
		int pageNo = "".equals(dutyForm.getPageNo()) || null == dutyForm.getPageNo()?
				1:Integer.parseInt(dutyForm.getPageNo());
		int pageSize = "".equals(dutyForm.getPageSize()) || null == dutyForm.getPageSize()?
				25:Integer.parseInt(dutyForm.getPageSize());
		int startNo = (pageNo-1)*pageSize + 1;
		int endNo = pageNo*pageSize;
		
		if("".equals(dutyForm.getStartDutyDate()) || null == dutyForm.getStartDutyDate()){
			dutyForm.setStartDutyDate(mgt.getDateByNull());
		}
		if("".equals(dutyForm.getEndDutyDate()) || null == dutyForm.getEndDutyDate()){
			dutyForm.setEndDutyDate(mgt.getDateByNull());
		}
		
		Result result = mgt.findAll(dutyForm,startNo,endNo);

		if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.error().add(getMessage(request, "search.inaccuracy"))
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		List<DutyPeriodsForm> periods = (ArrayList<DutyPeriodsForm>) result.getRetVal();
	
		request.setAttribute("pageSumList", workMgt.getSumPageList(result.getTotalPage(), pageSize));
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("pageNo", pageNo);
		request.setAttribute("dutyPeriodses", periods);
		request.setAttribute("DutyPeriodsForm", dutyForm);
		if(result.getTotalPage() > pageSize){
			request.setAttribute("enable", "true");
		}
		return getForward(request, mapping, "searchPeriods");
	}
	
	/**
	 * 鏌ヨ鐝锟�

	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward searchSquad(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String squadEnactmentNo = request.getParameter("squadEnactmentNo");
		String squadEnactmentName = request.getParameter("squadEnactmentName");
		int pageNo = "".equals(request.getParameter("pageNo")) || null == request.getParameter("pageNo")?
				1:Integer.parseInt(request.getParameter("pageNo"));
		int pageSize = "".equals(request.getParameter("pageSize")) || null == request.getParameter("pageSize")?
				25:Integer.parseInt(request.getParameter("pageSize"));
		int startNo = (pageNo-1)*pageSize + 1;
		int endNo = pageNo*pageSize;
		Result result = mgt.findSquadEnactment(squadEnactmentNo, squadEnactmentName,startNo,endNo);
		if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
			EchoMessage.error().add(getMessage(request, "search.inaccuracy"))
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		List<SquadInfoBean> squads = (List<SquadInfoBean>)result.getRetVal();
		
		request.setAttribute("pageSumList", workMgt.getSumPageList(result.getTotalPage(), pageSize));
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("pageNo", pageNo);
		request.setAttribute("squads", squads);
		if(!"".equals(squadEnactmentNo) && null != squadEnactmentNo){
			request.setAttribute("squadEnactmentNo", squadEnactmentNo);
		}
		if(!"".equals(squadEnactmentName) && null != squadEnactmentName){
			request.setAttribute("squadEnactmentName", squadEnactmentName);
		}
		if(result.getTotalPage() > pageSize){
			request.setAttribute("enable", "true");
		}
		return getForward(request, mapping, "searchSquad");
	}
	
	/**
	 * 娣诲姞鐝锟�

	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addSquad(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String squadEnactmentName = null == request.getParameter("squadEnactmentName")?"":
			request.getParameter("squadEnactmentName");
		List<SquadSect> squadSects = new ArrayList<SquadSect>();
		for(int i = 0;;i++){
			if(null == request.getParameter("onDutyTime"+i)){
				break;
			}
			if("".equals(request.getParameter("onDutyTime"+i))){
				break;
			}
			squadSects.add(new SquadSect(
					request.getParameter("onDutyTime"+i),
					request.getParameter("offDutyTime"+i),
					Integer.parseInt(null == request.getParameter("onDutyAvailabilityTime"+i) ||
							"".equals(request.getParameter("onDutyAvailabilityTime"+i))?"0":request.getParameter("onDutyAvailabilityTime"+i)),
					Integer.parseInt(null == request.getParameter("offDutyAvailabilityTime"+i) ||
							"".equals(request.getParameter("offDutyAvailabilityTime"+i))?"0":request.getParameter("onDutyAvailabilityTime"+i)),
					request.getParameter("squadSectType"+i)
					));
		}
		String createBy = this.getLoginBean(request).getId();
		Result result = mgt.addSquadEnactment(squadEnactmentName, squadSects, createBy);
		if(ErrorCanst.DEFAULT_SUCCESS == result.getRetCode()){
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).
				setBackUrl("/DutyPeriodsAction.do?type=1").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		EchoMessage.error().add(getMessage(request, "common.msg.addFailture"))
		.setAlertRequest(request);
		return getForward(request, mapping, "alert");
	}

}
