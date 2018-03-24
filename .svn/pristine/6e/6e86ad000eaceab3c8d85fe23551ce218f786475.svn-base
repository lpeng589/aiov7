package com.koron.hr.grade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Alignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.hr.exam.ExamMgt;
import com.menyi.web.util.BaseSearchForm;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class GradeAction extends MgtBaseAction {
	
	private GradeMgt mgt = new GradeMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
			case OperationConst.OP_DETAIL:
				forward = detail(mapping, form, request, response);
				break;
			case OperationConst.OP_DELETE:
				forward = delete(mapping, form, request, response);
				break;
			case OperationConst.OP_QUERY:
				forward = query(mapping, form, request, response);
				break;
			default:
				forward = query(mapping, form, request, response);
		}
		return forward;
	}
	
	//查看考卷分数
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String testStartTime = request.getParameter("testStartTime")==null?"":request.getParameter("testStartTime");
		String score = request.getParameter("score")==null?"":request.getParameter("score");
		String titleType = request.getParameter("titleType")==null?"":request.getParameter("titleType");
		String takePartPerson = request.getParameter("takePartPerson")==null?"":request.getParameter("takePartPerson");
		String[] conditions = new String[]{testStartTime, score, titleType, takePartPerson,getLoginBean(request).getId()};
		BaseSearchForm baseForm = form==null?new BaseSearchForm():(BaseSearchForm)form;
		Result rs = mgt.queryOldExam(conditions, baseForm.getPageNo(), baseForm.getPageSize());
		List<String[]> listOldExam = (List<String[]>)rs.getRetVal();
		/*导出席卷成绩*/
		if("export".equals(getParameter("queryType", request))){
			File file = new File("../../AIOBillExport") ;
			if(!file.exists()){
				file.mkdirs() ;
			}
			exportGrade(listOldExam, file.getPath()+"/考试成绩.xls");
			EchoMessage.success().add("导出成功,<a href='/ReadFile?tempFile=export&fileName="+GlobalsTool.encode("考试成绩")+".xls' target='_blank'>下载考试成绩</a>")
                    .setBackUrl("/GradeAction.do?operation="+OperationConst.OP_QUERY).setNotAutoBack().
                    setAlertRequest(request);
     	   	return getForward(request, mapping, "message");
		}
		request.setAttribute("listOldExam", listOldExam);
		request.setAttribute("pageBar", pageBar(rs, request));
		request.setAttribute("searchValues", conditions);
		request.setAttribute("questionType", new ExamMgt().queryQuestionType().retVal);
		return getForward(request, mapping, "query");
	}
	
	public void exportGrade(List<String[]> listExam,String fileName){
		try {
			WritableCellFormat wf = new WritableCellFormat();
			FileOutputStream fos = new FileOutputStream(fileName);
			wf.setBorder(Border.ALL, BorderLineStyle.THIN);
			wf.setAlignment(Alignment.CENTRE);
			WritableWorkbook wbook = Workbook.createWorkbook(fos);
			WritableSheet ws =  wbook.createSheet("考试成绩", 0);
			
			Label cell = new Label(0, 0, "题目类型", wf);
			ws.addCell(cell);
			Label cell1 = new Label(1, 0, "参与人", wf);
			ws.addCell(cell1);
			Label cell2 = new Label(2, 0, "得分", wf);
			ws.addCell(cell2);
			Label cell3 = new Label(3, 0, "答题数", wf);
			ws.addCell(cell3);
			Label cell4 = new Label(4, 0, "答卷开始时间", wf);
			ws.addCell(cell4);
			Label cell5 = new Label(5, 0, "使用时间", wf);
			ws.addCell(cell5);
			ws.setColumnView(0, 30);
			ws.setColumnView(4, 20);
			for(int i=0;i<listExam.size();i++){
				String[] obj = listExam.get(i);
				Label rcell = new Label(0, i+1, obj[2], wf);
				ws.addCell(rcell);
				Label rcell1 = new Label(1, i+1, obj[1], wf);
				ws.addCell(rcell1);
				Label rcell2 = new Label(2, i+1, obj[3], wf);
				ws.addCell(rcell2);
				Label rcell3 = new Label(3, i+1, obj[4], wf);
				ws.addCell(rcell3);
				Label rcell4 = new Label(4, i+1, obj[5], wf);
				ws.addCell(rcell4);
				Label rcell5 = new Label(5, i+1, obj[6], wf);
				ws.addCell(rcell5);
			}
			wbook.write();
			wbook.close();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//答卷明细
	protected ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String examId = request.getParameter("examId");
		String employeeId = getParameter("employeeId", request);
		String title = request.getParameter("title");
		if(title==null){
			title="";
		}
		BaseSearchForm baseForm = form==null?new BaseSearchForm():(BaseSearchForm)form;
		Result rs = mgt.queryHistoryTestDetail(examId,employeeId, title, baseForm.getPageNo(), baseForm.getPageSize());
		List listDetail = (List)rs.getRetVal();
		request.setAttribute("listDetail", listDetail);
		request.setAttribute("examId", examId);
		request.setAttribute("pageBar", pageBar(rs, request));
		request.setAttribute("title", title);
		return getForward(request, mapping, "detail");
	}
	
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] keyIds = request.getParameterValues("keyId");
        for (String keyId : keyIds) {
        	Result rs = mgt.deleteExamDet(keyId);
            if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
         	   EchoMessage.success().add(getMessage(
                        request, "common.msg.delError"))
                        .setBackUrl("/GradeAction.do?operation="+OperationConst.OP_QUERY).
                        setAlertRequest(request);
         	   break;
            }
            rs = mgt.deleteHistory(keyId);
            if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
         	   EchoMessage.success().add(getMessage(
                        request, "common.msg.delError"))
                        .setBackUrl("/GradeAction.do?operation="+OperationConst.OP_QUERY).
                        setAlertRequest(request);
         	   break;
            }
        } 
        EchoMessage.success().add(getMessage(
                    request, "common.msg.delSuccess"))
                    .setBackUrl("/GradeAction.do?operation="+OperationConst.OP_QUERY).
                    setAlertRequest(request);
        
        return getForward(request, mapping, "message");
	}

}
