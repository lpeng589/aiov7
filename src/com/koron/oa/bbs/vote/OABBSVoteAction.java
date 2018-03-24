package com.koron.oa.bbs.vote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bbs.forum.OABBSForumForm;
import com.koron.oa.bbs.forum.OABBSForumMgt;
import com.koron.oa.bean.OABBSForumBean;
import com.koron.oa.bean.OABBSTopicBean;
import com.koron.oa.bean.OABBSUserBean;
import com.koron.oa.bean.OABBSVoteAnswerBean;
import com.koron.oa.bean.OABBSVoteBean;
import com.koron.oa.bean.OABBSVoteUserBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;

/**
 * 
 * <p>Title:ͶƱ���� </p>
 * <p>Description:</p>
 * 
 * @Date:2011-4-18
 * @Copyright: �������
 * @Author ��СǮ
 */
public class OABBSVoteAction extends BaseAction {

	OABBSVoteMgt mgt = new OABBSVoteMgt() ;
	String  searchSel ;
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		int operation = getOperation(request) ;
        String type=getParameter("type",request);
        searchSel= getParameter("searchSel",request);
        ActionForward forward = null;
        /*�Ƿ����body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
        switch (operation) {
        	case OperationConst.OP_ADD_PREPARE:
        		/*ͶƱ ֮ǰ*/
        		forward = addPrepare(mapping,form,request,response) ;
        		break ;
        	case OperationConst.OP_ADD:
        		/*������ ͶƱ*/
        		forward = add(mapping,form,request,response) ;
        		break ;
        	case OperationConst.OP_UPDATE_PREPARE:
            		//ͶƱ�޸�֮ǰ
    	        	forward =updateVotePrepare(mapping,form ,request,response);
        		break;
        	case OperationConst.OP_UPDATE:
        		/*ͶƱ*/
        		 if("upvote".equals(type)){
        			 forward= upVote(mapping, form, request, response);
        		 }else{
        			 forward = update(mapping,form,request,response) ;
        		 }
        		break ;
	        default:
	            forward = add(mapping, form, request, response);
        }
        return forward;
	}

	/**
	 * ���� ͶƱ֮ǰ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addPrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		String topicId = getParameter("topicId", request) ; 		/*���Id*/
		
		/*��ѯ��ǰ�����Ϣ*/
		OABBSTopicBean topicBean = null ;
		if(topicId!=null && topicId.length()>0){
			topicBean = (OABBSTopicBean)  new OABBSForumMgt().loadTopic(topicId).retVal ;
		}			
		/*��ѯ��ǰ�����Ϣ*/
		if(topicBean!=null){
			/*��ѯ��̳ ��ǰλ��*/
			String firstPage = GlobalsTool.getMessage(request, "oa.bbs.firstPage");
			String parentName = queryParentName(topicBean.getSortName(),topicId,firstPage) ;
			request.setAttribute("parentName", parentName) ;
		}
		//��ѯ���ַ���
		Result result = mgt.queryTopicType(topicId); 
		request.setAttribute("topicType", result.retVal) ;
		request.setAttribute("topicId", topicId) ;
		return getForward(request, mapping, "addVote") ;
	}
	
	/**
	 * ��ѯ ��̳ ��ǰλ��
	 * @param parentList
	 * @return
	 */
	public String queryParentName(String formName,String topicId,String firstPage){
		String parentUrl = "";
		
		parentUrl += "<a href=\"/OABBSForumQueryAction.do"+ "\">"+firstPage+"</a> >> ";
		if(topicId != null && topicId.length() >0){
			parentUrl += "<a href=\"/OABBSForumQueryAction.do?topicId="+topicId+ "\">"+formName+"</a>";
		}else{
			parentUrl += formName;
		}
			
		return parentUrl ;
	}
	
	/**
	 * �޸� ͶƱ֮ǰ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateVotePrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String forumId = getParameter("forumId", request) ;		/*����ID*/
		String topicId = getParameter("topicId",request); 
		request.setAttribute("topicId", topicId);
		/*���ص�ǰͶƱ����*/
		Result forumInfo=mgt.getVoteId(forumId);
		ArrayList forumList=(ArrayList)forumInfo.retVal;
		OABBSForumBean forumBean=(OABBSForumBean)forumList.get(0);
		Result result = mgt.loadVote(forumBean.getVoteId());
		OABBSVoteBean voteBean=(OABBSVoteBean)result.retVal;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			 
			request.setAttribute("forum", forumBean) ;
			request.setAttribute("voteBean",voteBean);
			
			//��ѯ���ַ���
			Result rs = mgt.queryTopicType(topicId); 
			request.setAttribute("topicType", rs.retVal) ;
		
		}
		return getForward(request, mapping, "updateVote") ;
	}
	
	/**
	 * �޸�ͶƱ
	 */
	
	public ActionForward upVote(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		String topicId = getParameter("topicId", request) ; 		/*���Id*/
		String forumId = getParameter("forumId", request) ;			/*����Id*/
		OABBSVoteForm voteForm = (OABBSVoteForm) form ;
		/*����*/
		OABBSForumBean forumBean = (OABBSForumBean) mgt.loadForum(forumId).retVal;
		forumBean.setId(forumId);
		forumBean.setTopicName(voteForm.getVoteTopic()) ;
		forumBean.setTopicContent(voteForm.getVoteRemark()) ;
		forumBean.setCreateBy(getLoginBean(request).getId()) ;
		forumBean.setTopicTyle(voteForm.getTopicTyle()) ;
		forumBean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
		forumBean.setReplayNote(voteForm.getReplayNote());
		
		OABBSVoteBean voteBean = (OABBSVoteBean) mgt.loadVote(forumBean.getVoteId()).retVal ;
		read(voteForm, voteBean) ;
		//voteBean.setId(voteForm.getTopicId()) ;
		//voteBean.setCreateBy(getLoginBean(request).getId()) ;
		/*���ѷ�ʽ*/
		String[] wakeUpModes = voteForm.getWakeUpModes() ;
		if(wakeUpModes!=null && wakeUpModes.length>0){
			String strWakes = "" ;
			for(String wakeUp : wakeUpModes){
				strWakes += wakeUp+"," ;
			}
			voteBean.setWakeUpMode(strWakes) ;
		}
		
		/*ͶƱѡ��*/
		String[] voteAnswer = voteForm.getVoteAnswers() ;
		if(voteAnswer!=null && voteAnswer.length>0){
			
			ArrayList<OABBSVoteAnswerBean> listAnswer = new ArrayList<OABBSVoteAnswerBean>() ;
			for(String answer : voteAnswer){
				OABBSVoteAnswerBean answerBean = new OABBSVoteAnswerBean() ;
				answerBean.setId(IDGenerater.getId()) ;
				answerBean.setVoteBean(voteBean) ;
				answerBean.setVoteAnswer(answer) ;
				listAnswer.add(answerBean) ;
			}
			voteBean.setVoteAnswer(listAnswer) ;
		}
		/*���� ͶƱ*/
		Result result = mgt.updateVote(voteBean, forumBean) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			 EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess"))
                   .setBackUrl("/OABBSForumAction.do?operation=70&forumId="+forumBean.getId()+"&topicId="+topicId+"&addTHhead="+getParameter("addTHhead", request))
                   .setAlertRequest(request);
		}else{
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro"))
          		.setBackUrl("/OABBSForumAction.do?operation=70&forumId="+forumBean.getId()+"&topicId="+topicId+"&addTHhead="+getParameter("addTHhead", request))
          		.setAlertRequest(request);
		}
		return getForward(request, mapping, "message") ;
	}
	/**
	 * ͶƱ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String topicId = getParameter("topicId", request) ; 		/*���Id*/
		String forumId = getParameter("forumId", request) ;			/*����Id*/
		String[] answerIds = getParameters("voteAnswer", request);	/*ѡ��*/
		Result result = new Result() ;
		String answerStr = "";
		String voteId = "";
		for(String answerId : answerIds){
			OABBSVoteAnswerBean answerBean = (OABBSVoteAnswerBean) mgt.loadVoteAnswer(answerId).retVal ;
			answerBean.setVoteCount(answerBean.getVoteCount()+1) ;
			voteId = answerBean.getVoteBean().getId();
			result = mgt.updateVoteAnswer(answerBean) ;
			answerStr +=answerId+",";
		}
		OABBSUserBean userBean = (OABBSUserBean)request.getSession().getAttribute("BBSUser");
		OABBSVoteUserBean uBean = new OABBSVoteUserBean();
		uBean.setId(IDGenerater.getId());
		uBean.setBbsUser(userBean);
		uBean.setVoteId(voteId);
		uBean.setAnswerId(answerStr);
		result = mgt.addVoteUser(uBean) ;
		
		
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(request, "oa.bbs.vote.succeed"))
                     .setBackUrl("/OABBSForumAction.do?operation=70&topicId="+topicId+"&forumId="+forumId+"&addTHhead="+getParameter("addTHhead", request))
                     .setAlertRequest(request);
		}else{
			EchoMessage.success().add(getMessage(request, "oa.bbs.vote.failure"))
            		.setBackUrl("/OABBSForumAction.do?addTHhead="+getParameter("addTHhead", request)).setAlertRequest(request);
		}
		return getForward(request, mapping, "message") ;
	}
	
	/**
	 * ���� ͶƱ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward add(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		OABBSVoteForm voteForm = (OABBSVoteForm) form ;
		
		OABBSVoteBean voteBean = new OABBSVoteBean() ;
		read(voteForm, voteBean) ;
		voteBean.setId(IDGenerater.getId()) ;
		voteBean.setCreateBy(getLoginBean(request).getId()) ;
		
		/*����*/
		OABBSForumBean forumBean = new OABBSForumBean() ;
		forumBean.setId(IDGenerater.getId()) ;
		OABBSTopicBean topic = (OABBSTopicBean) new OABBSForumMgt().loadTopic(voteForm.getTopicId()).retVal ;
		forumBean.setTopic(topic) ;
		forumBean.setTopicName(voteForm.getVoteTopic()) ;
		forumBean.setTopicContent(voteForm.getVoteRemark()) ;
		OABBSUserBean bbsUser = (OABBSUserBean) request.getSession().getAttribute("BBSUser") ;
		forumBean.setBbsUser(bbsUser) ;
		forumBean.setCreateBy(getLoginBean(request).getId()) ;
		forumBean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
		forumBean.setTopicTyle(voteForm.getTopicTyle()) ;
		forumBean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
		forumBean.setForumType("vote") ;
		forumBean.setVoteId(voteBean.getId()) ;
		forumBean.setReplayNote(voteForm.getReplayNote());
		
		
		/*���ѷ�ʽ*/
		String[] wakeUpModes = voteForm.getWakeUpModes() ;
		if(wakeUpModes!=null && wakeUpModes.length>0){
			String strWakes = "" ;
			for(String wakeUp : wakeUpModes){
				strWakes += wakeUp+"," ;
			}
			voteBean.setWakeUpMode(strWakes) ;
		}
		
		/*ͶƱѡ��*/
		String[] voteAnswer = voteForm.getVoteAnswers() ;
		if(voteAnswer!=null && voteAnswer.length>0){
			ArrayList<OABBSVoteAnswerBean> listAnswer = new ArrayList<OABBSVoteAnswerBean>() ;
			for(String answer : voteAnswer){
				OABBSVoteAnswerBean answerBean = new OABBSVoteAnswerBean() ;
				answerBean.setId(IDGenerater.getId()) ;
				answerBean.setVoteBean(voteBean) ;
				answerBean.setVoteAnswer(answer) ;
				listAnswer.add(answerBean) ;
			}
			voteBean.setVoteAnswer(listAnswer) ;
		}
		/*���� ͶƱ*/
		Result result = mgt.addVote(voteBean, forumBean) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			 EchoMessage.success().add(getMessage(request, "com.deliver.succeed"))
                   .setBackUrl("/OABBSForumAction.do?operation=70&invite=true&forumId="+forumBean.getId()+"&topicId="+voteForm.getTopicId()+"&addTHhead="+getParameter("addTHhead", request))
                   .setAlertRequest(request);
		}else{
			EchoMessage.success().add(getMessage(request, "com.deliver.failed"))
          		.setBackUrl("/OABBSForumQueryAction.do?topicId="+voteForm.getTopicId()+"&addTHhead="+getParameter("addTHhead", request))
          		.setAlertRequest(request);
		}
		return getForward(request, mapping, "message") ;
	}
	
	/**
	 * ��ѯ ��̳ ��ǰλ��
	 * @param parentList
	 * @return
	 */
	public String queryParentName2(ArrayList<String[]> parentList,String firstPage){
		String parentUrl = "";
		if(parentList.size()==0){
			parentUrl +=  firstPage;
		}else{
			parentUrl += "<a href=\"/OABBSForumQueryAction.do"+ "\">"+firstPage+"</a> >> ";
			for (int i = 0; i < parentList.size(); i++) {
				String[] nameClass = (String[]) parentList.get(i);
				parentUrl += "<a href=\"/OABBSForumQueryAction.do?topicId="
						  + nameClass[2]+"\">" 
						  + nameClass[0]+ "</a> >> ";
			}
		}
		return parentUrl ;
	}
	
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            BaseEnv.log.debug("OABBSForumAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
        //����Ψһ�û���֤�������������½�ģ��������û��߳�ǰ�����û�
        if (!OnlineUserInfo.checkUser(req)) {
            //���߳�
            EchoMessage.error().setAlertRequest(req);
            return getForward(req, mapping, "doubleOnline");
        }
		return null;
	}

}
