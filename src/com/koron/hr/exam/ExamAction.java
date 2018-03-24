package com.koron.hr.exam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.hr.bean.Answer;
import com.koron.hr.bean.ExamDet;
import com.koron.hr.bean.Problem;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.BaseSearchForm;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class ExamAction extends MgtBaseAction {
	
	private ExamMgt mgt = new ExamMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}
	static Map mapExams = new HashMap<String, ExamDet>();
	//出卷使用随机出题,选项顺序随机
	protected ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String examId = null;
		String examDetId = null;
		String titleType = null;
		int quantity = 0;
		String loginId = getLoginBean(request).getId() ;
		ExamDet examDet = null;
		if(request.getParameter("examId")!=null && request.getParameter("examDetId")!=null && request.getParameter("titleType")!=null){
			examId = request.getParameter("examId");
			examDetId = request.getParameter("examDetId");
			titleType = request.getParameter("titleType");
			if(titleType!=null){
				titleType = GlobalsTool.toChinseChar(titleType);
			}
			quantity = Integer.parseInt(request.getParameter("quantity"));
			//生成一张考卷
			mapExams.remove(loginId);
			examDet = new ExamDet();
			Result rsExam = mgt.queryExamById(examId);
			String[] strExam = (String[])rsExam.getRetVal();
			examDet.setId(examDetId);
			examDet.setF_ref(examId);
			examDet.setCreateBy(strExam[5]);
			examDet.setCreateTime(strExam[6]);
			examDet.setStartTime(strExam[3]);
			examDet.setEndTime(strExam[4]);
			examDet.setLimitTime(Integer.parseInt(strExam[7]));
			Result rsProblems = mgt.queryProblemsManage(titleType,quantity);//从符合的类型中选出对应的数目
			TreeMap<Integer, Problem> problemMap = (TreeMap<Integer, Problem>) rsProblems.getRetVal();
			quantity = problemMap.size();
			for (Problem problem : problemMap.values()) {
				Result rsAnswers = mgt.queryAPreSelectAnswer(problem.getId());
				List answers = (List)rsAnswers.getRetVal();
				problem.setAnswers(answers);
			}
			examDet.setProblemMap(problemMap);
			examDet.setTotalProblem(quantity);
			mgt.updateExamDetOfTotalPros(examDetId,quantity);
			mapExams.put(loginId, examDet);
		}else if(mapExams.containsKey(loginId)){
			examDet = (ExamDet)mapExams.get(loginId);
		}else{
			EchoMessage.success().add(getMessage(
                    request, "common.msg.takePartError"))
                    .setBackUrl(
                    "/ExamAction.do?operation="+OperationConst.OP_QUERY).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}
		
		String currProblemId = request.getParameter("id");
		String lastProblemId = null;
		String nextProblemId = null;
		Problem currProblem = new Problem();
		//如果题目id为空，从第一题开始
		if(currProblemId==null||currProblemId.equals("")){
			currProblem = examDet.getProblemMap().get(1);
			currProblemId = currProblem.getId();
			lastProblemId = examDet.getProblemMap().get(examDet.getProblemMap().size()).getId();
			nextProblemId = examDet.getProblemMap().get(2).getId();
		}else{
			TreeMap<Integer, Problem> problemMap = examDet.getProblemMap();
			for (Problem problem : problemMap.values()) {
				if(currProblemId.equals(problem.getId())){
					currProblem = problem;
					if(problem.getSerialNum() == 1){
						lastProblemId = problemMap.get(problemMap.size()).getId();
						nextProblemId = problemMap.get(problem.getSerialNum()+1).getId();
					}else if(problem.getSerialNum() == problemMap.size()){
						lastProblemId = problemMap.get(problem.getSerialNum()-1).getId();
						nextProblemId = problemMap.get(1).getId();
					}else{
						lastProblemId = problemMap.get(problem.getSerialNum()-1).getId();
						nextProblemId = problemMap.get(problem.getSerialNum()+1).getId();
					}
					break;
				}
			}
		}
		//用户的回答
		String[] chkAnswer = request.getParameterValues("chkAnswer");
		if(chkAnswer!=null){
			String currId = request.getParameter("currId");//当前要保存的题目id
			String answer = "";
			String problemScore = "0";
			//将用户回答清空
			for (Problem updProblem : examDet.getProblemMap().values()) {
				if (currId.equals(updProblem.getId())) {
					for (int k = 0; k < updProblem.getAnswers().size(); k++) {
						Answer proAnswer = (Answer)updProblem.getAnswers().get(k);
						proAnswer.setUserAnswer(false);
					}
					break;
				}
			}
			//填充用户回答
			for (int i = 0; i < chkAnswer.length; i++) {
				for (Problem updProblem : examDet.getProblemMap().values()) {
					if (currId.equals(updProblem.getId())) {
						for (int k = 0; k < updProblem.getAnswers().size(); k++) {
							Answer proAnswer = (Answer)updProblem.getAnswers().get(k);
							if (chkAnswer[i].equals(proAnswer.getId())) {
								proAnswer.setUserAnswer(true);
								answer += proAnswer.getAnswerMarke();
							}
						}
					}
				}
			}
			for (Problem updProblem : examDet.getProblemMap().values()) {
				if (currId.equals(updProblem.getId())) {
					if (answer!="") {
						char[] cs = answer.toCharArray();
						for (int k = cs.length-1; k > 0; k--) {
							for (int h = 0; h < k; h++) {
								if (cs[h]>cs[h+1]) {
									char temp = 0;
									temp = cs[h];
									cs[h]= cs[h+1];
									cs[h+1] = temp;
								}
							}
						}
						answer = String.valueOf(cs);
					}
					updProblem.setUserOption(answer);
					problemScore = String.valueOf(updProblem.getScore());
					break;
				}
			}
			//保存用户答题至历史答卷
			String correctAnswer = mgt.queryCorrectAnswer(currId).getRetVal().toString();
			String[] strs = new String[]{loginId, examDet.getF_ref(), currId, answer, correctAnswer,problemScore};
			Result rsHistory = mgt.queryHistoryTest(strs);
			if (rsHistory.getRetVal()!=null) {
				mgt.updateHistoryTest(strs);
			} else {
				mgt.addHistoryTest(strs);
			}
		}
		
		//String[] arr = (String[])mgt.queryLeavingTime(examDet.getId()).getRetVal(); 
		//int leavingTime = Integer.parseInt(arr[0]) ;
		String startTime = String.valueOf(request.getSession().getAttribute("startTime"+examDet.getId()));
		if("null".equals(startTime) || startTime.length()==0){
			startTime = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss) ;
			request.getSession().setAttribute("startTime"+examDet.getId(), startTime);
			
			Date end = BaseDateFormat.parse(examDet.getEndTime(), BaseDateFormat.yyyyMMddHHmmss);
			if(System.currentTimeMillis()>end.getTime()){
				EchoMessage.success().add("考试时间已过，下次准时点！")
	                   .setBackUrl("/ExamAction.do?operation="+OperationConst.OP_QUERY).setAlertRequest(request);
	            return getForward(request, mapping, "message");
			}
		}
		
		request.setAttribute("currProblem", currProblem);
		request.setAttribute("nextProblemId", nextProblemId);
		request.setAttribute("lastProblemId", lastProblemId);
		request.setAttribute("leavingTime", examDet.getLimitTime());
		request.setAttribute("startTime", startTime);
		request.setAttribute("createBy", examDet.getCreateBy());
		request.setAttribute("createTime", examDet.getCreateTime());
		return getForward(request, mapping, "main");
	}
	
	//可以参加的考试
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String loginId = getLoginBean(request).getId() ;
		String startTime = request.getParameter("startTime")==null?"":request.getParameter("startTime");
		String titleType = request.getParameter("titleType")==null?"":request.getParameter("titleType");
		String empName = request.getParameter("empName")==null?"":request.getParameter("empName");
		String[] conditions = new String[]{loginId, startTime, titleType, empName};
		BaseSearchForm baseForm = form==null?new BaseSearchForm():(BaseSearchForm)form;
		Result rsTest = mgt.queryExamByConditions(conditions,baseForm.getPageNo(),baseForm.getPageSize());
		List listTest = (List)rsTest.getRetVal();
		if(listTest.size()>0){
			request.setAttribute("listTest", listTest);
		}
		request.setAttribute("pageBar", pageBar(rsTest, request));
		request.setAttribute("questionType", mgt.queryQuestionType().retVal);
		request.setAttribute("searchValues", conditions);
		return mapping.findForward("takepart");
	}
	
	//提交答卷更新考试管理明细
	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String loginId = getLoginBean(request).getId() ;
		ExamDet examDet = null;
		if(mapExams.containsKey(loginId)){
			examDet = (ExamDet)mapExams.get(loginId);
		}else{
			EchoMessage.success().add(getMessage(
                    request, "common.msg.takePartError"))
                    .setBackUrl(
                    "/ExamAction.do?operation="+OperationConst.OP_QUERY).
                    setAlertRequest(request);
            return getForward(request, mapping, "message");
		}
		
		String[] strs = new String[]{loginId,examDet.getF_ref()};
		Result rsHistory = mgt.queryHistoryTestAll(strs);
		List listHistory = (List)rsHistory.getRetVal();
		Integer sumRight = 0; //答对的题数
		Integer totalScore = 0 ;
		for (int i = 0; i < listHistory.size(); i++) {
			String[] objs = new String[3];
			objs = (String[])listHistory.get(i);
			if (objs[1]==null) {
				continue;
			}else if(objs[1].equals(objs[2])){
				sumRight++;
				totalScore += Integer.parseInt(objs[3]);
			}
		}
		//double score = (sumRight*100)/examDet.getTotalProblem();      //以100分计算。
		mgt.updateExamManageDet(examDet.getId(),totalScore);     //插入用户所得分数
		mapExams.remove(loginId);
		if("auto".equals(getParameter("submitType", request))){
			EchoMessage.success().add("考试时间已到，系统自动提交考试,本次考试得分为：<font color='red'>"+totalScore+"</font>")
				.setBackUrl("/ExamAction.do?operation="+OperationConst.OP_QUERY)
				.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}else{
			EchoMessage.success().add("考试顺利完成，本次考试得分为：<font color='red'>"+totalScore+"</font>")
				.setBackUrl("/ExamAction.do?operation="+OperationConst.OP_QUERY)
				.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
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
                        .setBackUrl("/ExamAction.do?operation="+OperationConst.OP_QUERY).
                        setAlertRequest(request);
         	   break;
            }
        } 
        EchoMessage.success().add(getMessage(
                    request, "common.msg.delSuccess"))
                    .setBackUrl("/ExamAction.do?operation="+OperationConst.OP_QUERY).
                    setAlertRequest(request);
        
        return getForward(request, mapping, "message");
	}
}
