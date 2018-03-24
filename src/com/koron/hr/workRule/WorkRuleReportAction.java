package com.koron.hr.workRule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.StatWorkRukeListReportBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class WorkRuleReportAction extends MgtBaseAction {

	DutyPeriodsMgt dutyMgt = new DutyPeriodsMgt();
	WorkRuleReportMgt mgt = new WorkRuleReportMgt();
	
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		int opration = 0 == getOperation(request)?4:getOperation(request);
		int type = Integer.parseInt(null == request.getParameter("type")?"0":request.getParameter("type"));
		switch (opration) {
		case OperationConst.OP_ADD:
			if(type == 1){
				forward = getListReport(mapping, form, request, response);
			}else{
				forward = getAssembleReport(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_QUERY:
			if(type == 1){
				forward = searchListReport(mapping, form, request, response);
			}else{
				forward = getForward(request, mapping, "workRuleStatReport");
			}
			break;
		}
		return forward;
	}


	protected ActionForward getListReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String id = this.getLoginBean(request).getId();
		Result result = mgt.findEmployeeAll(id);
		
		if(ErrorCanst.DEFAULT_SUCCESS != result.getRetCode()){
			EchoMessage.error().add(getMessage(request, "work.rule.list.report.err")).
			setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		if(ErrorCanst.DEFAULT_SUCCESS == result.getRealTotal()){
			EchoMessage.success().add(
					getMessage(request, "stat.list.empty")).setBackUrl(
					"/WorkRuleReportAction.do?type=1").setAlertRequest(request);

			return getForward(request, mapping, "message");
		}
		else{
			return searchListReport(mapping, form, request, response);
		}
	}

	protected ActionForward searchListReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		WorkRuleListReportForm reportForm = (WorkRuleListReportForm) form;
		int pageNo = "".equals(request.getParameter("pageNo")) || null == request.getParameter("pageNo")?
				1:Integer.parseInt(request.getParameter("pageNo"));
		int pageSize = "".equals(request.getParameter("pageSize")) || null == request.getParameter("pageSize")?
				25:Integer.parseInt(request.getParameter("pageSize"));
		
		int startNo = (pageNo-1)*pageSize + 1;
		int endNo = pageNo*pageSize;
		
		if("".equals(reportForm.getStartDate()) || null == reportForm.getStartDate()){
			reportForm.setStartDate(dutyMgt.getDateByNull());
		}
		if("".equals(reportForm.getEndDare()) || null == reportForm.getEndDare()){
			reportForm.setEndDare(dutyMgt.getDateByNull());
		}
		
		Result result = mgt.searchListReport(reportForm,startNo,endNo);
		if(ErrorCanst.DEFAULT_SUCCESS != result.getRetCode()){
			EchoMessage.error().add(getMessage(request, "work.rule.list.report.err")).
			setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		List<WorkRuleListReportForm> listReports = (List<WorkRuleListReportForm>)result.getRetVal();

		request.setAttribute("WorkRuleListReportForms",listReports );
		request.setAttribute("pageSumList", mgt.getSumPageList(result.getTotalPage(), pageSize));
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("pageNo", pageNo);
		request.setAttribute("WorkRuleListReportForm", reportForm);
		
		if(result.getTotalPage() > pageSize){
			request.setAttribute("enable", "true");
		}
		return getForward(request, mapping, "workRuleListReport");
	}
	
	
	protected ActionForward getAssembleReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String tblEmployee_empFullName = request.getParameter("tblEmployee_empFullName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String employeeNo = request.getParameter("employeeNo");
		employeeNo = null == employeeNo?"":employeeNo;
		int pageNo = "".equals(request.getParameter("pageNo")) || null == request.getParameter("pageNo")?
				1:Integer.parseInt(request.getParameter("pageNo"));
		int pageSize = "".equals(request.getParameter("pageSize")) || null == request.getParameter("pageSize")?
				25:Integer.parseInt(request.getParameter("pageSize"));
		int startNo = (pageNo-1)*pageSize + 1;
		int endNo = pageNo*pageSize;
		
		Result result = mgt.statDutyAnnalByDate(startDate, endDate,employeeNo,startNo,endNo);
		if(ErrorCanst.DEFAULT_SUCCESS != result.getRetCode()){
			EchoMessage.error().add(getMessage(request, "stat.sum.report.err")).
			setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		List<StatWorkRukeListReportBean> statReports = (List<StatWorkRukeListReportBean>)result.getRetVal();
		
		request.setAttribute("pageSumList", mgt.getSumPageList(result.getTotalPage(), pageSize));
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("pageNo", pageNo);
		request.setAttribute("stats",statReports);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		if(!"".equals(employeeNo) && null != employeeNo){
			request.setAttribute("employeeNo", employeeNo);
		}
		if(!"".equals(tblEmployee_empFullName) && null != tblEmployee_empFullName){
			request.setAttribute("tblEmployee_empFullName", tblEmployee_empFullName);
		}
		if(result.getTotalPage() > pageSize){
			request.setAttribute("enable", "true");
		}
		return getForward(request, mapping, "workRuleStatReport");
	}
}
