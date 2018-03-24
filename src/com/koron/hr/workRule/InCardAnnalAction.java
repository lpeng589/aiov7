package com.koron.hr.workRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.dbfactory.Result;
import com.menyi.aio.bean.BrushCardAnnalBean;
import com.menyi.aio.web.upload.UploadForm;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;

public class InCardAnnalAction extends MgtBaseAction{

	
	WorkRuleReportMgt workMgt = new WorkRuleReportMgt();
	
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String type = request.getParameter("type");
		if("excelmapped".equals(type)){
			getExcelCell(mapping, form, request,response);
		}
		else if("excelData".equals(type)){
			getExeclData(mapping, form, request, response);
		}
		else if("saveDate".equals(type)){
			return saveAnnals(mapping, form, request, response);
		}
		else{
			request.setAttribute("enableDiv", "false");
			request.setAttribute("enable", "false");
		}
		return getForward(request, mapping, "excelUpload");
	}
	
	protected void getExcelCell(ActionMapping mapping, ActionForm form, HttpServletRequest 
			request, HttpServletResponse response) throws Exception {
	    UploadForm uploadForm = (UploadForm)form;
		FormFile file = uploadForm.getFileName();
		request.setAttribute("enable", "false");
		request.setAttribute("enableDiv", "false");
		request.getSession().setAttribute("file", file);
		if(null != file){
			List cells = ParseExcel.getExcelCells(file.getInputStream());
			List<BrushCardAnnalBean> annals = null == request.getSession().getAttribute("annals")?
				new ArrayList<BrushCardAnnalBean>():(List<BrushCardAnnalBean>)request.getSession().getAttribute("annals");
			if(annals.size() > 0){
				annals.clear();
			}
			request.setAttribute("enable", "true");
			request.setAttribute("cells", cells);
		}
		
	}
	
	protected void getExeclData(ActionMapping mapping, ActionForm form, HttpServletRequest 
			request, HttpServletResponse response) throws Exception {
		int pageNo = "".equals(request.getParameter("pageNo")) || null == request.getParameter("pageNo")?
				1:Integer.parseInt(request.getParameter("pageNo"));
		int pageSize = "".equals(request.getParameter("pageSize")) || null == request.getParameter("pageSize")?
				25:Integer.parseInt(request.getParameter("pageSize"));
		int startNo = (pageNo-1)*pageSize;
		int endNo = pageNo*pageSize;
		int employeeNo = "".equals(request.getParameter("employeeNo")) || 
				null == request.getParameter("employeeNo")?0:Integer.parseInt(request.getParameter("employeeNo"));
		int cardAnnal = "".equals(request.getParameter("cardAnnal")) ||
				null == request.getParameter("cardAnnal")?0:Integer.parseInt(request.getParameter("cardAnnal"));
		
		request.setAttribute("enableDiv", "false");
		List<BrushCardAnnalBean> annalsTemp = 
			(List<BrushCardAnnalBean>) request.getSession().getAttribute("annals");
		FormFile file = (FormFile) request.getSession().getAttribute("file");
		if(null != file){
			if(null == annalsTemp || annalsTemp.size() < 1){
				List<BrushCardAnnalBean> annals = ParseExcel.
					getExcelContent(file.getInputStream(),employeeNo, cardAnnal);
				request.getSession().setAttribute("annals", annals);
				annalsTemp = annals;
			}
			request.setAttribute("enable", "false");
			request.setAttribute("enableDiv", "true");
			request.setAttribute("brushCardAnnals", getListByNo(annalsTemp, startNo, endNo));
			request.setAttribute("pageSumList", workMgt.getSumPageList(annalsTemp.size(), pageSize));
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("pageNo", pageNo);
			if(annalsTemp.size() > pageSize){
				request.setAttribute("enablePage", "true");
			}
		}

	}
	
	protected ActionForward saveAnnals (ActionMapping mapping, ActionForm form, HttpServletRequest 
			request, HttpServletResponse response) throws Exception {
		List<BrushCardAnnalBean> annals = 
			(List<BrushCardAnnalBean>) request.getSession().getAttribute("annals");
		Result result = workMgt.validateEmployeeNo(annals);
		Set<String> employeeNos = (Set<String>)result.getRetVal();
		if(0 >= employeeNos.size()){
			String id = this.getLoginBean(request).getId();
			Result rs = workMgt.insertCardAnnals(annals, id);
			if(ErrorCanst.DEFAULT_SUCCESS != rs.getRetCode()){
				EchoMessage.error().add(getMessage(request, "import.card.annals.faliure"))
					.setAlertRequest(request);
				return getForward(request, mapping, "alert");
			}
			EchoMessage.success().add(getMessage(request, "import.card.annals.success")).
				setBackUrl("/InCardAnnal.do").setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		request.setAttribute("employeeNos", employeeNos);
		return getForward(request, mapping, "err");
	}
	
	protected List<BrushCardAnnalBean> getListByNo(List<BrushCardAnnalBean> annals,int startNo,int endNo){
		List<BrushCardAnnalBean> annalsTemp = new ArrayList<BrushCardAnnalBean>();
		if(endNo > annals.size()){
			endNo = annals.size();
		}
		for (int i = startNo; i < endNo; i++) {
			annalsTemp.add(annals.get(i));
		}
		return annalsTemp;
	}

}
