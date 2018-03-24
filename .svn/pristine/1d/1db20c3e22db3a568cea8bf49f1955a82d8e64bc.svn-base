package com.koron.oa.bbs.forum;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.dbfactory.Result;
import com.koron.oa.bbs.vote.OABBSVoteMgt;
import com.koron.oa.bean.OABBSForumBean;
import com.koron.oa.bean.OABBSReplayForumBean;
import com.koron.oa.bean.OABBSTopicBean;
import com.koron.oa.bean.OABBSUserBean;
import com.koron.oa.bean.OABBSVoteAnswerBean;
import com.koron.oa.bean.OABBSVoteBean;
import com.koron.oa.bean.OABBSVoteUserBean;
import com.koron.oa.mydesktop.MyDesktopMgt;

import com.koron.oa.util.AttentionMgt;
import com.menyi.aio.bean.AdviceBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
/**
 * 
 * <p>Title:论坛 帖子管理（添加，修改，删除，置顶，加精华，回帖）</p>
 * <p>Description:</p>
 * 
 * @Date:2011-4-12
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class OABBSForumAction extends BaseAction {

	OABBSForumMgt mgt = new OABBSForumMgt() ;
	String searchSel;
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
        int operation = getOperation(request) ;
        String type = getParameter("type", request) ;
        
        String noback=request.getParameter("noback");//不能返回
        searchSel= getParameter("searchSel",request);
		request.setAttribute("searchSel",searchSel); //查询类型
		/*是否添加body2head.js*/
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		
		if(noback!=null&&noback.equals("true")){
			request.setAttribute("noback", true);
		}else{
			request.setAttribute("noback", false);
		}
        ActionForward forward = null;
        switch (operation) { 
        	case OperationConst.OP_ADD_PREPARE:
        		/*发帖 之前*/
        		forward = addPrepare(mapping,form,request,response) ;
        		break ;
        	case OperationConst.OP_ADD:
        		/*发帖*/
        		forward = add(mapping,form,request,response) ;
        		break ;
	        case OperationConst.OP_UPDATE_PREPARE:
	        	/*修改帖子 之前*/
	        	if("user".equals(type)){
	        		forward = updateUserPrepare(mapping, form, request, response) ;
	        	}else{
	        		forward = updatePrepare(mapping, form, request, response);
	        	}
	            break ;
	        case OperationConst.OP_UPDATE:
	        	/*修改发帖*/
	        	if("move".equals(type)){	/*帖子搬移*/
	        		forward = moveForum(mapping, form, request, response) ;
	        	}else if("user".equals(type)){	/*修改 用户签名*/
	        		forward = updateUser(mapping, form, request, response) ;
	        	}else{
	        		forward = update(mapping, form, request, response);
	        	}
	            break ;
	        case OperationConst.OP_SEND_PREPARE:
	        	/*回帖 之前*/
	        	forward = sendPrepare(mapping, form, request, response);
	        	break ;
	        case OperationConst.OP_SEND:
	        	/*回帖*/
	        	forward = send(mapping, form, request, response);
	        	break ;
	        case OperationConst.OP_DELETE:
	        	/*删除帖子*/
	        	forward = delete(mapping,form,request,response) ;
	        	break ;
	        case OperationConst.OP_QUERY:
	        	
	        	/*查询 模块，帖子*/
	        	forward = query(mapping, form, request, response);
	        	
	        	break ;
	        case OperationConst.OP_DOWNLOAD:
	        	forward = downFile(mapping, form, request, response) ;
	        	break ;
	        default:
	        	/*查询 模块，帖子*/
	            forward = query(mapping, form, request, response);
        }
        return forward;
	}

	/**
	 * 查询 论坛模块，帖子
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward query(ActionMapping mapping, ActionForm form,
              HttpServletRequest request,HttpServletResponse response) throws Exception{
		String topicId = getParameter("topicId", request) ;			/*版块Id*/
		String keyword = getParameter("keyword", request) ;
		String hightype= getParameter("type",request);
		String queryType = getParameter("queryType", request) ;
	    searchSel =getParameter("searchSel",request);
		String displayList = getParameter("displayList",request);
		request.setAttribute("displayList", displayList);
		/*登录信息*/
		LoginBean loginBean = getLoginBean(request) ;
		String firstPage = GlobalsTool.getMessage(request, "oa.bbs.firstPage");
		
		OABBSUserBean userBean = (OABBSUserBean)request.getSession().getAttribute("BBSUser") ;
		Result result = null;
		if(((topicId == null || topicId.length() == 0) &&(searchSel==null || !searchSel.trim().equals("yes"))
				&&(keyword ==null || keyword.trim().length()==0))||(searchSel!=null && searchSel.trim().equals("dayForum"))){
			/*查询版块*/
			
			/*返回版块清楚查询框内容*/
			request.getSession().setAttribute("forumSearchForm", null);
			request.getSession().setAttribute("searchSel","");
			result = mgt.queryTopic(userBean.getId(),getLoginBean(request).getId(),getLoginBean(request).getDepartCode()) ;
			ArrayList<String[]> topicList = (ArrayList<String[]>) result.retVal ;
			if(topicList.size()==0){
				EchoMessage.info().add("<a href='/UserFunctionQueryAction.do?tableName=OABBSTopic&operation=6&winCurIndex=18'>"+getMessage(request,"oa.forum.setTopic")+"</a>")
					.setNoBackButton().setAlertRequest(request);
				return this.getForward(request, mapping, "message");
			}
			request.getSession().setAttribute("topicList", topicList) ;
			/*查询版块的最后一条帖子*/
			String forumIds = "" ;
			String topicIds = "" ;
			for(String[] array : topicList){
				forumIds += "'"+array[4]+"'," ;
				topicIds += "'"+array[5]+"'," ;
			}
			if(forumIds.length()>0){
				forumIds = forumIds.substring(0, forumIds.length()-1) ;
				topicIds = topicIds.substring(0, topicIds.length()-1) ;
				//时间过滤
				
				if("dayForum".equals(searchSel) || "dayForum".equals(queryType)){
					//时间分为今日，三天，本周，本月，指定时间
					String timeTlag = request.getParameter("timeTlag");
					String nameType = "今日新帖";
					if("month".equals(timeTlag)){
						nameType = "本月内贴";
					}else if("zhou".equals(timeTlag)){
						nameType = "本周内贴";
					}else if("three".equals(timeTlag)){
						nameType = "三天内贴";
					}
					request.setAttribute("queryType", queryType) ;
					/*如果是今日新帖就不显示查询*/
					request.setAttribute("searchSel","dayForum");			
					request.setAttribute("dayForumNone", "none");
					result = mgt.queryDayForums(topicIds,timeTlag) ;
					request.setAttribute("forumList", result.retVal) ;
					request.setAttribute("parentName", "<a href=\"/OABBSForumQueryAction.do?addTHhead="+getParameter("addTHhead", request)+"\">"+firstPage+"</a> >> "+nameType) ;
					return getForward(request, mapping, "displayForum") ;
										
				}else{
					result = mgt.queryForums(forumIds) ;
					request.setAttribute("mapForum", result.retVal) ;
					result = mgt.queryTodayForumCount();
					request.setAttribute("todayForumCount", result.retVal);//此版块中今日新帖数
				}
			}
			return getForward(request, mapping, "displayTopic") ;
		}else if(searchSel!=null && searchSel.trim().equals("yes")){		
			request.setAttribute("dayForumNone","");
			OABBSForumSearchForm forumForm = (OABBSForumSearchForm) form ;
			forumForm.setKeyword("");
			if(keyword!=null ){
				if(keyword.trim().equals("标题搜索")){
					keyword="";
				}
				forumForm.setForumTitle(keyword) ;
				forumForm.setKeyword(keyword);
			}
			
			/*模糊查询 查看帖子 返回的时候 返回当前的模糊查询*/
			if("yes".equals(searchSel)){
				request.setAttribute("searchSel",getParameter("searchSel",request));
			}
			
			/*论坛首页 模糊查询 根据权限可以查看的帖子*/
			result = mgt.queryTopicForum(userBean.getId(), getLoginBean(request).getId(), getLoginBean(request).getDepartCode());
			List<String> topicIdList=(List<String>)result.getRetVal();
			
			/*查询 所有 的帖子*/
			result =mgt.queryForum(forumForm.getTopic(), forumForm, true, loginBean.getId(),hightype);
			List<OABBSForumBean> beanList=(List<OABBSForumBean>)result.getRetVal();
			
			/*由于获取有权限的版块是用sql作的，而查询贴子 是用hql 中的 这个是筛选有权限的帖子*/
			for(int index=0;index<beanList.size();index++){
				if(!topicIdList.contains(beanList.get(index).getTopic().getId())){
					beanList.remove(index);
					index=-1;
				}
			}
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("forumList", result.retVal) ;
				 
				request.setAttribute("pageBar", pageBar(result, request)) ;
			}
			request.setAttribute("queryType", queryType);
			request.setAttribute("parentName", "<a href=\"/OABBSForumQueryAction.do?addTHhead="+getParameter("addTHhead", request)+"\">"+firstPage+"</a> >> 搜索结果") ;
			return getForward(request, mapping, "displayForum") ;
		}else{
			request.setAttribute("dayForumNone","");
			request.getSession().setAttribute("searchSel","");
			OABBSForumSearchForm forumForm = (OABBSForumSearchForm) form ;
			forumForm.setKeyword(keyword);
			if(keyword!=null){
				if(keyword.trim().equals("标题搜索")){
					keyword="";
				}
				forumForm.setForumTitle(keyword);
				forumForm.setKeyword(keyword);
			}
			
			if ("menu".equals(getParameter("src", request))) {
				forumForm = new OABBSForumSearchForm();
				request.getSession().setAttribute("forumSearchForm", null);
			}
			/*查询当前版块信息*/
			OABBSTopicBean topicBean = null ;
			if(topicId!=null && topicId.length()>0 ){
				topicBean = (OABBSTopicBean) mgt.loadTopic(topicId).retVal ;
			}			
			/*查询当前版块信息*/
			if(topicBean!=null){
				request.setAttribute("BBSTopic", topicBean) ;
				/*查询论坛 当前位置*/
				String parentName = queryParentName(topicBean.getSortName(),"",firstPage,getParameter("addTHhead", request)) ;
				request.setAttribute("parentName", parentName) ;
				request.setAttribute("moudelNames", topicBean.getSortName()) ;
			}
			
			/*查询是有发贴的权限*/
			result = mgt.queryTopicScope(topicId, userBean.getId(), getLoginBean(request).getId(), 
											getLoginBean(request).getDepartCode(), "2,") ;
			request.setAttribute("addScope", result.retVal) ;
			
			/*查询是有查看他人帖子的权限*/
			result = mgt.queryTopicScope(topicId, userBean.getId(), getLoginBean(request).getId(), 
											getLoginBean(request).getDepartCode(), "6,") ;
			boolean viewOther = (Boolean)result.retVal;
			
			
			/*查询当前版块的帖子*/ 
			result = mgt.queryForum(topicId, forumForm,viewOther,loginBean.getId(),hightype) ;
			
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("forumList", result.retVal) ;
				 
				request.setAttribute("pageBar", pageBar(result, request)) ;
			}
			 
			/*查询是否关注此版块*/
			result = new AttentionMgt().isAttention(loginBean.getId(), topicId, "OABBSTopic");
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("attention", "attention");
			}
			//查询版块分分类
			Result rs = mgt.queryTopicType(topicId); 
			request.setAttribute("topicType", rs.retVal) ;
			request.setAttribute("topicId", topicId) ;  
			request.setAttribute("searchSelShow", forumForm);
			return getForward(request, mapping, "displayForum") ;
		}
	}
	
	/**
	 * 发布 帖子之前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addPrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		String topicId = getParameter("topicId", request) ; /*版块Id*/
		
		
		if(!"1".equals(getLoginBean(request).getId())){
			/*查询是有上传附件的权限*/
			OABBSUserBean userBean = (OABBSUserBean) request.getSession().getAttribute("BBSUser") ;
			Result result = mgt.queryTopicScope(topicId, userBean.getId(), getLoginBean(request).getId(), 
											getLoginBean(request).getDepartCode(), "5,") ;
			request.setAttribute("uploadScope", result.retVal) ;
		}
		
		/*查询当前版块信息*/
		OABBSTopicBean topicBean = null ;
		if(topicId!=null && topicId.length()>0){
			topicBean = (OABBSTopicBean) mgt.loadTopic(topicId).retVal ;
		}			
		/*查询当前版块信息*/
		if(topicBean!=null){
			/*查询论坛 当前位置*/
			String firstPage = GlobalsTool.getMessage(request, "oa.bbs.firstPage");
			String parentName = queryParentName(topicBean.getSortName(),topicId,firstPage,getParameter("addTHhead", request)) ;
			request.setAttribute("parentName", parentName) ;
			request.setAttribute("moudelNames", topicBean.getSortName()) ;
		}
		//查询版块分分类
		Result result = mgt.queryTopicType(topicId); 
		request.setAttribute("topicType", result.retVal) ;
		
		request.setAttribute("topicId", topicId) ;
		return getForward(request, mapping, "addForum") ;
	}
	
	/**
	 * 发布 帖子
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward add(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		OABBSForumForm forumForm = (OABBSForumForm) form ;
		OABBSForumBean forumBean = new OABBSForumBean() ;
		read(forumForm, forumBean) ;
		
		//上传文件
		String mailAttaches = request.getParameter("attachFiles");
		mailAttaches = mailAttaches== null?"":mailAttaches;

		//需删除的附件
		String delFiles = request.getParameter("delFiles");
		//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){
				File aFile = new File(BaseEnv.FILESERVERPATH + "/bbs/" + del);
				aFile.delete();
			}
		}		
		OABBSTopicBean topic = (OABBSTopicBean) mgt.loadTopic(forumForm.getTopicId()).retVal ;
		forumBean.setAttachment(mailAttaches) ;
		forumBean.setId(IDGenerater.getId()) ;
		forumBean.setTopic(topic) ;
		forumBean.setForumType("topic") ;
		/*加载用户*/
		OABBSUserBean bbsUser = (OABBSUserBean) request.getSession().getAttribute("BBSUser") ;
		forumBean.setBbsUser(bbsUser) ;
		forumBean.setCreateBy(getLoginBean(request).getId()) ;
		forumBean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
		
		forumBean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
		Result result = mgt.addForum(forumBean) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<String[]> list =(ArrayList<String[]>)new AttentionMgt().queryAttention(forumForm.getTopicId(),"OABBSTopic").getRetVal();			
			String refreshIds= "";
			for(int i = 0;i<list.size();i++){
															//OABBSForumQueryAction.do?operation="+OperationConst.OP_QUERY+"&topicId="+forumForm.getTopicId()+
				String content="<a href=\"javascript:mdiwin('/OABBSForumAction.do?operation="+OperationConst.OP_SEND_PREPARE+"&forumId="+forumBean.getId()+"&topicId="+forumForm.getTopicId()+
				"','"+getMessage(request,"oa.bbs.innerBBS")+"')\">"+getMessage(request,"oa.bbs.Attention",list.get(i)[2])+"</a>";
				NotifyFashion notifyFashion = new NotifyFashion(list.get(i)[0], getMessage(request,"oa.bbs.innerBBS"), content, list.get(i)[0], 4, "OA", "bbs");
				notifyFashion.sendAdvice(list.get(i)[0], getMessage(request,"oa.bbs.Attention",list.get(i)[2]), content,list.get(i)[0], forumBean.getId(), "bbs");
				refreshIds += "'"+list.get(i)[1]+"',";
			}
			
			if(refreshIds.length() > 0){
				refreshIds = refreshIds.substring(0,refreshIds.length() -1);
				new AttentionMgt().refreshAttention(refreshIds);
			}
			 EchoMessage.success().add(getMessage(request, "com.deliver.succeed"))
                    .setBackUrl("/OABBSForumAction.do?operation=70&abc=kk&invite=true&forumId="+forumBean.getId()+"&topicId="+forumForm.getTopicId()+"&searchSel="+searchSel+"&addTHhead="+getParameter("addTHhead", request))
                    .setAlertRequest(request);
		}else{
			EchoMessage.success().add(getMessage(request, "com.deliver.failed"))
           		.setBackUrl("/OABBSForumQueryAction.do?topicId="+forumForm.getTopicId()+"&addTHhead="+getParameter("addTHhead", request))
           		.setAlertRequest(request);
		}
		return getForward(request, mapping, "message") ;
	}
	
	/**
	 * 帖子搬移
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward moveForum(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String[] forumIds = getParameters("forumId", request) ;		/*帖子ID*/
		String newTopicId = getParameter("newTopicId", request) ;	/*要搬移到哪个版块下*/
		String   topicId  = getParameter("topicId", request) ;		/*版块ID*/
		
		if(newTopicId != null && !"".equals(newTopicId)){
			Result result = mgt.moveForum(forumIds, newTopicId) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add(getMessage(request, "com.data.move.success"))
	                    .setBackUrl("/OABBSForumQueryAction.do?topicId="+topicId+"&addTHhead="+getParameter("addTHhead", request))
	                    .setAlertRequest(request);
			}else{
				EchoMessage.success().add(getMessage(request, "com.data.move.failure"))
	           		.setBackUrl("/OABBSForumQueryAction.do?topicId="+topicId+"&addTHhead="+getParameter("addTHhead", request))
	           		.setAlertRequest(request);
			}			
		}
		return getForward(request, mapping, "message") ;
	}
	
	/**
	 * 修改 用户之前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateUserPrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		OABBSUserBean userBean = (OABBSUserBean) request.getSession().getAttribute("BBSUser") ;
		request.setAttribute("userBean", userBean) ;
		return getForward(request, mapping, "updateUser") ;
	}
	
	/**
	 * 修改 帖子之前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String forumId = getParameter("forumId", request) ;		/*帖子ID*/
		String topicId = getParameter("topicId",request);
		/*加载当前帖子*/
		Result result = mgt.loadForum(forumId) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			OABBSForumBean forumBean = (OABBSForumBean) result.retVal ;
			request.setAttribute("forum", forumBean) ;
			
			//查询版块分分类
			Result rs = mgt.queryTopicType(topicId); 
			request.setAttribute("topicType", rs.retVal) ;
			if(!"1".equals(getLoginBean(request).getId())){
				/*查询是有上传附件的权限*/
				OABBSUserBean userBean = (OABBSUserBean) request.getSession().getAttribute("BBSUser") ;
				result = mgt.queryTopicScope(forumBean.getTopic().getId(), userBean.getId(), getLoginBean(request).getId(), 
												getLoginBean(request).getDepartCode(), "5,") ;
				request.setAttribute("uploadScope", result.retVal) ;
			}
		}
		return getForward(request, mapping, "updateForum") ;
	}
	

	
	/**
	 * 修改 用户签名
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateUser(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String signature = getParameter("signature", request) ; 	/*用户签名*/
		String nickName = getParameter("nickName", request) ;
		request.setAttribute("noback", true);
		Result result = new Result() ;
		OABBSUserBean userBean = (OABBSUserBean) request.getSession().getAttribute("BBSUser") ;
		if(userBean!=null){
			userBean.setNickName(nickName);
			userBean.setSignature(signature) ;
			result = mgt.updateUser(userBean) ;
		}
		
		OABBSUserForm imageForm = (OABBSUserForm) form ;
		FormFile imageFile = imageForm.getImageFile();
		
		if(imageFile != null && imageFile.getFileName() != null&& imageFile.getFileName().length() >0){
			String ext = "gif,jpg,jpeg,png,bmp" ;
			if(imageFile.getFileName().contains(".")){
				String fileExt = imageFile.getFileName().substring(imageFile.getFileName().lastIndexOf(".") + 1).toLowerCase() ;
				if(!Arrays.asList(ext.split(",")).contains(fileExt)){
					EchoMessage.error().add(getMessage(request, "com.bbs.user.image"))
   									.setAlertRequest(request);
					return getForward(request, mapping, "alert") ;
				}
			}else{
				EchoMessage.error().add(getMessage(request, "com.bbs.user.image"))
								.setAlertRequest(request);
				return getForward(request, mapping, "alert") ;
			}
            FileOutputStream fos;
			try {
				String oldfileName = imageFile.getFileName();
				String fileName = imageFile.getFileName();
				File f = new File(BaseEnv.FILESERVERPATH+"/pic/tblEmployee/"+fileName);
				for(int i=1;f.exists()&& i<1000;i++){ 
		        	//文件名存在，改名
		        	fileName = oldfileName.substring(0,oldfileName.lastIndexOf("."))+"("+i+")"+oldfileName.substring(oldfileName.lastIndexOf("."));
		        	f = new File(BaseEnv.FILESERVERPATH+"/pic/tblEmployee/"+fileName); 
		        }
				String imageName = BaseEnv.FILESERVERPATH+"/pic/tblEmployee/"+fileName;
				
				File file = new File(imageName) ;
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs() ;
				}
				fos = new FileOutputStream(file);
				fos.write(imageFile.getFileData());
	            fos.close();

	            result = new MyDesktopMgt().updateEmployeeImage(getLoginBean(request).getId(), fileName) ;
	            if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
	            	String oldImage = BaseEnv.FILESERVERPATH+"/pic/tblEmployee/" + getLoginBean(request).getPhoto() ;
	            	File oldFile = new File(oldImage) ;
	            	if(oldFile.exists()){
	            		oldFile.delete() ;
	            	}
	            	LoginBean loginBean = getLoginBean(request) ;
	            	loginBean.setPhoto(fileName) ;
	            	request.getSession().setAttribute("LoginBean", loginBean);
        			MSGConnectCenter.refreshEmpInfo(loginBean.getId());	// 同步更新
	            }
	            request.setAttribute("fileName", fileName) ;
			} catch (Exception e) {
				e.printStackTrace();
				BaseEnv.log.error("OABBSFormAction.do uploadImage :",e) ;
			}
		}
		
		if(userBean !=null && result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("noAlert", "true");
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess"))
            			.setAlertRequest(request);
		}else{
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro"))
       					.setAlertRequest(request);
		}
		return getForward(request, mapping, "message") ;
	}
	
	/**
	 * 修改 论坛 帖子
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		OABBSForumForm forumForm = (OABBSForumForm) form ;
		/*加载帖子*/
		String forumId = getParameter("forumId", request) ;
		Result result = mgt.loadForum(forumId) ;
		OABBSForumBean forumBean = (OABBSForumBean) result.retVal ;
		
		String updateType = getParameter("type", request) ;
		String paramValue = getParameter("paramValue", request) ;
		if(updateType!=null && updateType.length()>0){ 
			if("setTop".equals(updateType)){ /*置顶*/
				forumBean.setIsSetTop(Integer.parseInt(paramValue)) ;
			}else if("elite".equals(updateType)){	/*加精华*/
				forumBean.setIsElite(Integer.parseInt(paramValue)) ;
			}
			result = mgt.updateForum(forumBean, updateType,paramValue) ;
		}else{
			read(forumForm, forumBean) ;

			//上传文件

			// 附件
			String mailAttaches = request.getParameter("attachFiles");
			mailAttaches = mailAttaches== null?"":mailAttaches;

			//需删除的附件
			String delFiles = request.getParameter("delFiles");
			//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
			String[] dels = delFiles==null?new String[0]:delFiles.split(";");
			for(String del:dels){
				if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){
					if(del.indexOf(":")>0){ //兼容老板本的删除
						del = del.substring(del.indexOf(":")+1);
						String path = request.getSession().getServletContext().getRealPath("/upload");
						File aFile = new File(path, del);
						aFile.delete();
					}else{
						File aFile = new File(BaseEnv.FILESERVERPATH + "/bbs/" + del);
						aFile.delete();
					}
				}
			}	
			if(mailAttaches.indexOf("|") > 0){
				//将老板本转为新版本
				String newatt="";
				for(String str:mailAttaches.split("\\||;")){
					if(str.indexOf(":") > 0 ){
						newatt +=str.substring(0,str.indexOf(":"));
						String path = request.getSession().getServletContext().getRealPath("/upload");
						try{
							File oldFile = new File(path,str.substring(str.indexOf(":")+1));
							FileInputStream fis = new FileInputStream(oldFile);
							FileOutputStream fos = new FileOutputStream(new File(BaseEnv.FILESERVERPATH + "/bbs/",newatt));
							byte[] bs = new byte[81920];
							int count=0;
							while((count= fis.read(bs)) > -1){
								fos.write(bs, 0, count);
							}
							fis.close();
							fos.close();
							oldFile.delete();
						}catch(Exception e){
							e.printStackTrace();
						}
						newatt +=";";
					}else{
						newatt +=str+";";
					}
				}
				mailAttaches = newatt;
			}
			forumBean.setAttachment(mailAttaches) ;
			result = mgt.updateForum(forumBean) ;
		}
		forumForm.setTopicId(getParameter("topicId", request)) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess"))
            			.setBackUrl("/OABBSForumAction.do?operation=70&forumId="+forumId+"&topicId="+forumBean.getTopic().getId()+"&searchSel="+searchSel+"&addTHhead="+getParameter("addTHhead", request))
            			.setAlertRequest(request);
			
		}else{
			EchoMessage.error().add(getMessage(request, "common.msg.updateErro"))
       					.setBackUrl("/OABBSForumAction.do?operation=70&forumId="+forumId+"&topicId="+forumBean.getTopic().getId()+"&searchSel="+searchSel+"&addTHhead="+getParameter("addTHhead", request))
       					.setAlertRequest(request);
		}
		return getForward(request, mapping, "message") ;
	}
	
	/**
	 * 回帖 之前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward sendPrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{	
		String forumId   = getParameter("forumId", request) ; /*帖子Id*/
		String topicId   = getParameter("topicId", request) ; /*版块ID*/	
			
		request.setAttribute("invite", getParameter("invite", request)) ;

		/*查询当前版块信息*/
		OABBSTopicBean topicBean = null ;
		if(topicId!=null && topicId.length()>0){
			topicBean = (OABBSTopicBean) mgt.loadTopic(topicId).retVal ;
			if(topicBean!=null){
				request.setAttribute("BBSTopic", topicBean) ;
			}
		}
		
		/*查询当前版块信息*/
		if(topicBean!=null){
			/*查询论坛 当前位置*/
			String firstPage = GlobalsTool.getMessage(request, "oa.bbs.firstPage");
			String parentName = queryParentName(topicBean.getSortName(),topicId,firstPage,getParameter("addTHhead", request)) ;
			request.setAttribute("parentName", parentName) ;
			request.setAttribute("moudelNames", topicBean.getSortName()) ;
		}
		/*查询 积分等级*/
		Result result = mgt.queryBBSGrade() ;
		
		UserMgt usermgt=new UserMgt();
		Result re=usermgt.queryAllEmployee();
		request.setAttribute("userlist", re.retVal);
		
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("gradeList", result.retVal) ;
		}
		OABBSUserBean userBean = (OABBSUserBean)request.getSession().getAttribute("BBSUser") ;
		/*加载当前帖子*/
		result = mgt.loadForum(forumId) ;		
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){			
			OABBSForumBean forumBean = (OABBSForumBean) result.retVal ;
			
			String userId = forumBean.getBbsUser().getUserID();			
			String myPhoto = GlobalsTool.checkEmployeePhoto("140",userId);
			request.setAttribute("myPhoto", myPhoto) ;
			request.setAttribute("BBSForum", result.retVal) ;
			
			OABBSForumSearchForm forumSearchForm =(OABBSForumSearchForm)request.getSession().getAttribute("forumSearchForm");
			/*查询是有查看他人帖子的权限*/
			
			Result voresult = mgt.queryTopicScope(topicId, userBean.getId(), getLoginBean(request).getId(), 
											getLoginBean(request).getDepartCode(), "6,") ;
			boolean viewOther = (Boolean)voresult.retVal;
			//查上一帖，和一帖
			ArrayList fl = (ArrayList)mgt.queryPreNextForum(true,forumSearchForm,forumBean.getTopic().getId(),forumBean
					,forumBean.getId(),viewOther,this.getLoginBean(request).getId()).retVal;
			if(fl != null && fl.size()>0){
				request.setAttribute("preforumId", ((Object[])fl.get(0))[0]);
			}
			fl = (ArrayList)mgt.queryPreNextForum(false,forumSearchForm,forumBean.getTopic().getId(),forumBean
					,forumBean.getId(),viewOther,this.getLoginBean(request).getId()).retVal;
			if(fl != null && fl.size()>0){
				request.setAttribute("nextforumId", ((Object[])fl.get(0))[0]);
			}
			
			//阅读痕迹
			request.setAttribute("uri", java.net.URLEncoder.encode("/OABBSForumAction.do?operation=70&forumId="+forumId+"&topicId="+topicId+"&searchSel="+searchSel));
			int pageNo = getParameterInt("pageNo", request) ;
			/*累加点击数*/
			if(pageNo==0){
				forumBean.setPointCount(forumBean.getPointCount()+1) ;
				result = mgt.updateForum(forumBean) ;
			}
			/*如果当前帖子是投票*/
			if("vote".equals(forumBean.getForumType())){
				result = new OABBSVoteMgt().loadVote(forumBean.getVoteId()) ;
				OABBSVoteBean voteBean = (OABBSVoteBean) result.retVal ;
				int voteCount = 0 ;
				boolean isVote = false ;
				for(OABBSVoteAnswerBean answerBean : voteBean.getVoteAnswer()){
					voteCount += answerBean.getVoteCount() ;					
				}
				//查询是否投票用户
				result = new OABBSVoteMgt().loadVoteUser(forumBean.getVoteId()) ;
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
					List ulist = (List)result.retVal;
					
					//看是否在投票期限内
					Date bd = BaseDateFormat.parse(voteBean.getBeginTime()+" 00:00:00", BaseDateFormat.yyyyMMddHHmmss);
					Date ed = BaseDateFormat.parse(voteBean.getEndTime()+" 23:59:59", BaseDateFormat.yyyyMMddHHmmss);
					if(new Date().before(ed)&& new Date().after(bd)){
						for(Object uBean:ulist){
							if(((OABBSVoteUserBean)uBean).getBbsUser().getUserID().equals(this.getLoginBean(request).getId())){
								isVote = true;
								break;
							}
						}
					}else{
						isVote = true;
						if(new Date().after(ed)){
							request.setAttribute("voteDate", "after") ;
						}else if(new Date().before(bd)){
							request.setAttribute("voteDate", "before") ;
						}
					}
					request.setAttribute("voteUser", ulist) ;
				}				
				request.setAttribute("isVote", isVote) ;
				request.setAttribute("voteCount", voteCount) ;
				request.setAttribute("voteBean", voteBean) ;
			}
			/*查询回复帖子*/
			int pageSize = getParameterInt("pageSize", request) ;
			if(pageSize==0){
				pageSize = GlobalsTool.getPageSize() ;
			}
			/*查看是否收藏过帖子*/
			LoginBean loginBean = getLoginBean(request) ;
			OABBSForumForm forumForm = (OABBSForumForm) form ;
			result = new AttentionMgt().isAttention(loginBean.getId(), forumForm.getForumId(), "OABBSSends");
			if(result.retCode != ErrorCanst.MULTI_VALUE_ERROR){
				request.setAttribute("attentionCard", "attentionCard");
			}
			
			 String auto2=request.getParameter("auto2");
			 String auto=request.getParameter("auth");
			 String auto3="";
			 String orderBey=request.getParameter("order");
			 String publi=request.getParameter("publi");
			 if("1".equals(publi)){
				 if(!"222".equals(auto2)){
					 auto3=auto;
					 request.setAttribute("order", orderBey);
					 if("asc".equals(orderBey)){
						 orderBey="desc";
					 }else{
						 orderBey="asc";
					 }
					 request.setAttribute("auto2", "222");
				 }else{
					 request.setAttribute("auto2", "");
					 auto3="";
					 request.setAttribute("order", orderBey);
					 if("asc".equals(orderBey)){
						 orderBey="desc";
					 }else{
						 orderBey="asc";
					 }
				 }
			 }else if("2".equals(publi)){
				 if("222".equals(auto2)){
					 auto3=auto;
				 }
				if(orderBey == null || orderBey.trim().length()==0){
						orderBey="desc";
						request.setAttribute("order", "asc");
						request.setAttribute("auto2", auto2);
				}else{			
					if("desc".equals(orderBey)){
						request.setAttribute("order", "asc");
						request.setAttribute("auto2", auto2);
					}
					else{
						request.setAttribute("order", "desc");
						request.setAttribute("auto2", auto2);
					}
				}
			}
			result = mgt.queryReplayForum(forumId, pageSize, pageNo,auto3,orderBey) ;
			Result userPhonos = mgt.queryAllPhoto(forumId);
			request.setAttribute("userPhonos", userPhonos.retVal) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal!=null){
				request.setAttribute("replayList", result.retVal) ;
				if(result.realTotal >0){
					request.setAttribute("pageBar", pageBar(result, request)) ;
				}
				//String userPhoto = getLoginBean(request).getPhoto();
				//String userPhoto = getLoginBean(request).getId();
				String userPhoto = GlobalsTool.checkEmployeePhoto("140",getLoginBean(request).getId());
				request.setAttribute("userPhoto", userPhoto) ;
				if(!"1".equals(getLoginBean(request).getId())){
					/*发帖权限*/
					result = mgt.queryTopicScope(topicId, userBean.getId(), getLoginBean(request).getId(),
												getLoginBean(request).getDepartCode(), "2,") ;
					request.setAttribute("addScope", result.retVal) ;
					/*回帖权限*/
					result = mgt.queryTopicScope(topicId, userBean.getId(), getLoginBean(request).getId(),
												getLoginBean(request).getDepartCode(), "3,") ;
					request.setAttribute("replayScope", result.retVal) ;
					/*下载附件*/
					result = mgt.queryTopicScope(topicId, userBean.getId(), getLoginBean(request).getId(),
							getLoginBean(request).getDepartCode(), "4,") ;
					request.setAttribute("downScope", result.retVal) ;
					/*上传附件*/
					result = mgt.queryTopicScope(topicId, userBean.getId(), getLoginBean(request).getId(),
							getLoginBean(request).getDepartCode(), "5,") ;
					request.setAttribute("uploadScope", result.retVal) ;
				}
			}
		}else{
			EchoMessage.error().add(getMessage(request, "bbs.forum.not.find"))
					.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "detailForum") ;
	}
	
	/**
	 * 回帖
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward send(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		OABBSForumForm forumForm = (OABBSForumForm) form ;
		//上传文件

		// 附件
 		String mailAttaches = request.getParameter("attachFiles");
		mailAttaches = mailAttaches== null?"":mailAttaches;

		//需删除的附件
		String delFiles = request.getParameter("delFiles");
		//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && mailAttaches.indexOf(del) == -1){
				File aFile = new File(BaseEnv.FILESERVERPATH + "/bbs/" + del);
				aFile.delete();
			}
		}		
		OABBSReplayForumBean replayBean = new OABBSReplayForumBean() ;
		replayBean.setId(IDGenerater.getId()) ;
		replayBean.setAttachment(mailAttaches) ;
		replayBean.setContent(forumForm.getTopicContent()) ;
		/*加载用户*/
		OABBSUserBean bbsUser = (OABBSUserBean) request.getSession().getAttribute("BBSUser") ;
		String replayNote=request.getParameter("replayNote");
		OABBSUserBean bUser= new OABBSUserBean();
		bUser.setId(bbsUser.getId());
		replayBean.setBbsUser(bUser) ;
		replayBean.setSendId(forumForm.getForumId()) ;
		replayBean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
		replayBean.setCreateBy(getLoginBean(request).getId()) ;
		String[] message = new String[2] ;
		message[0] = getMessage(request, "bbs.msg.newreplay") ;
		message[1] = getMessage(request, "bbs.msg.newreplay2") ;
		Result result = mgt.replayForum(replayBean,forumForm.getTopicId(),message,replayNote) ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<String[]> list =(ArrayList<String[]>)new AttentionMgt().queryAttention(forumForm.getForumId(),"OABBSSends").getRetVal();			
			String refreshIds= "";
			 
			for(int i = 0;i<list.size();i++){
				String content="<a href=\"javascript:mdiwin('/OABBSForumAction.do?operation="+OperationConst.OP_SEND_PREPARE+"&noback=true"+"&forumId="+forumForm.getForumId()+"&topicId="+forumForm.getTopicId()+"&searchSel="+searchSel+
				"','"+getMessage(request,"oa.bbs.innerBBS")+"')\">"+getMessage(request,"oa.bbs.AttentionCard",list.get(i)[2])+"</a>";
				String title=getMessage(request,"oa.bbs.AttentionCard",list.get(i)[2]);
				NotifyFashion notifyFashion = new NotifyFashion(list.get(i)[0], title, content, list.get(i)[0], 4, "OA", "bbs");
				notifyFashion.sendAdvice(list.get(i)[0], title, content,list.get(i)[0], forumForm.getForumId(), "bbs");
				refreshIds += "'"+list.get(i)[1]+"',";
			 
			}
			 
			ArrayList<String[]>	list2 =(ArrayList<String[]>)new AttentionMgt().queryAttention(forumForm.getTopicId(),"OABBSTopic").getRetVal();
				for(int i = 0;i<list2.size();i++){
					int flag=0;
					for(int j=0;j<list.size();j++){
						if(list.get(j)[0].equals(list2.get(i)[0])){
							flag=1;
						}
					}
					if(flag==0){
							String content="<a href=\"javascript:mdiwin('/OABBSForumAction.do?operation="+OperationConst.OP_SEND_PREPARE+"&noback=true"+"&forumId="+forumForm.getForumId()+"&topicId="+forumForm.getTopicId()+"&searchSel="+searchSel+
							"','"+getMessage(request,"oa.bbs.innerBBS")+"')\">"+getMessage(request,"oa.bbs.Attention",list2.get(i)[2])+"</a>";	
							String title=getMessage(request,"oa.bbs.Attention",list2.get(i)[2]);
							NotifyFashion notifyFashion = new NotifyFashion(list2.get(i)[0], title, content, list2.get(i)[0], 4, "OA", "attention");
							notifyFashion.sendAdvice(list2.get(i)[0], title, content,list2.get(i)[0], forumForm.getForumId(), "bbs");
							refreshIds += "'"+list2.get(i)[1]+"',";
					}
				}
				
			if(refreshIds.length() > 0){
				refreshIds = refreshIds.substring(0,refreshIds.length() -1);
				new AttentionMgt().refreshAttention(refreshIds);
			}
			
			EchoMessage.success().add(getMessage(request, "com.revert.to.succeed"))
                     .setBackUrl("/OABBSForumAction.do?operation=70&topicId="+forumForm.getTopicId()+"&forumId="+forumForm.getForumId()+"&searchSel="+searchSel+"&addTHhead="+getParameter("addTHhead", request))
                     .setAlertRequest(request);
		}else{
			EchoMessage.success().add(getMessage(request, "com.revert.to.failure"))
            		.setBackUrl("/OABBSForumAction.do?&searchSel="+searchSel+"&addTHhead="+getParameter("addTHhead", request)).setAlertRequest(request);
		}
		return getForward(request, mapping, "message") ;
	}
	
	/**
	 * 删除 帖子
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String topicId = getParameter("topicId", request) ;		/*版块Id*/
		String type = getParameter("type", request) ;
		Result result = null ;
		if("replay".equals(type)){
			String replayId = getParameter("replayId", request) ;	/*回帖ID*/
			String forumId  = getParameter("forumId", request) ;	/*帖子ID*/
			
			//删除附件
			String att= "";
			result  =mgt.loadPeplay(replayId);
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				OABBSReplayForumBean db = (OABBSReplayForumBean)result.retVal;
				att = db.getAttachment();				
			}
			
			result = mgt.deleteReplay(replayId) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				if(att != null && att.length() > 0 ){
					if(att.indexOf("|") > 0){
						//兼容老格式
						for(String str: att.split("|")){
							if(str.length() ==0) continue;
							String path = request.getSession().getServletContext().getRealPath("/upload");
							File f = new File(path,str.substring(0,str.indexOf(":")));
							f.delete();
						}
					}else{
						for(String str: att.split(";")){
							if(str.length() ==0) continue;
							String path = BaseEnv.FILESERVERPATH + "/bbs/"  ;
							File f = new File(path,str);
							f.delete();
						}
					}
				}
				
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess"))
	                     .setBackUrl("/OABBSForumAction.do?operation=70&topicId="+topicId+"&forumId="+forumId+"&searchSel="+searchSel+"&addTHhead="+getParameter("addTHhead", request))
	                     .setAlertRequest(request);
			}else{
				EchoMessage.success().add(getMessage(request, "common.msg.delError"))
						.setBackUrl("/OABBSForumAction.do?operation=70&topicId="+topicId+"&forumId="+forumId+"&searchSel="+searchSel+"&addTHhead="+getParameter("addTHhead", request))
	            		.setAlertRequest(request);
			}
		}else{
			String[] forumIds = getParameters("forumId", request) ;
			result = mgt.deleteForum(forumIds,topicId) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//删除成功
				//删除与之相关的通知
				String ids = "";
				for(String delId : forumIds){
					ids += delId + ",";
				}
				new AdviceMgt().deleteByRelationId(ids, "");
				
				ArrayList<String> attachList = (ArrayList<String>)result.retVal;
				for(String att :attachList ){
					if(att.indexOf("|") > 0){
						//兼容老格式
						for(String str: att.split("|")){
							if(str.length() ==0) continue;
							String path = request.getSession().getServletContext().getRealPath("/upload");
							File f = new File(path,str.substring(0,str.indexOf(":")));
							f.delete();
						}
					}else{
						for(String str: att.split(";")){
							if(str.length() ==0) continue;
							String path = BaseEnv.FILESERVERPATH + "/bbs/"  ;
							File f = new File(path,str);
							f.delete();
						}
					}
				}
				
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess"))
						.setBackUrl("/OABBSForumQueryAction.do?topicId="+topicId+"&searchSel="+searchSel+"&addTHhead="+getParameter("addTHhead", request)).setAlertRequest(request);
				return getForward(request, mapping, "message");
			}else{
				//添加失败
				EchoMessage.error().add(getMessage(request, "common.msg.delError"))
						.setBackUrl("/OABBSForumAction.do?topicId="+topicId+"&searchSel="+searchSel+"&addTHhead="+getParameter("addTHhead", request)).setAlertRequest(request);
			}
		}
		return getForward(request, mapping, "message") ;
	}
	
	/**
	 * 下载 文件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward downFile(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {

	    	ActionForward forward = null ;
			String strFileName = request.getParameter("filename");
			String strFileName2 = getParameter("fileName2", request) ;	
			String sendId = request.getParameter("sendId") ;
			String topicId = request.getParameter("topicId") ;
			String path = request.getRealPath("/upload");
			File file = new File(path + "\\" + strFileName);

			System.out.println("file==========" + file);
			if (file.exists()) {
				try {
					BufferedInputStream bis = new BufferedInputStream(
							new FileInputStream(file));
					byte[] buffer = new byte[1024];
					strFileName2 = new String(GlobalsTool.toChinseChar(strFileName2)); // 处理中文文件名的问题
					strFileName2 = java.net.URLEncoder.encode(strFileName2, "UTF-8"); // 处理中文文件名的问题
					response.reset();
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/x-rar-compressed"); // 不同类型的文件对应不同的MIME类型
					response.setHeader("Content-Disposition",
							"attachment; filename=" + strFileName2);
					OutputStream os = response.getOutputStream();
					while (bis.read(buffer) > 0) {
						os.write(buffer);
					}
					bis.close();
					os.close();
				} catch (Exception e) {
					System.out.println("下载文件时出错!");
				}
			} else {

				forward = getForward(request, mapping, "message");
	            EchoMessage.error().add(getMessage(request,
	                    "common.msg.noDownFile")).setBackUrl(
	                            "/OABBSSend.do?operation="+OperationConst.OP_QUERY
	                            +"&type_query=to_showReplays&sendId="+sendId+"&topicId="+topicId+"&addTHhead="+getParameter("addTHhead", request)).
	                    setAlertRequest(request);
			}
			return forward ;

	}
	
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            BaseEnv.log.debug("OABBSForumAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
        //进行唯一用户验证，如果有生复登陆的，则后进入用户踢出前进入用户
        if (!OnlineUserInfo.checkUser(req)) {
            //需踢出
            EchoMessage.error().setAlertRequest(req);
            return getForward(req, mapping, "doubleOnline");
        }        
        OABBSUserBean userBean = (OABBSUserBean)req.getSession().getAttribute("BBSUser") ;
        if(userBean == null || !loginBean.getId().equals(userBean.getUserID())){
        	//没有用户信息，或者用户信息和登陆用户不一致。
			/*加载论坛 用户信息,每次更新用户信息，需要重新进入论坛*/
			Result result = mgt.loadBBSUser(loginBean.getId()) ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.totalPage>0){
				userBean = ((List<OABBSUserBean>)result.retVal).get(0) ;
				req.getSession().setAttribute("BBSUser", userBean) ;
			}
			/*如果用户还注册 先注册*/
			if(userBean==null){
				userBean = new OABBSUserBean() ;
				userBean.setId(IDGenerater.getId()) ;
				userBean.setNickName(loginBean.getEmpFullName());
				userBean.setFullName(loginBean.getEmpFullName());
				userBean.setUserID(loginBean.getId()) ;
				userBean.setCreateBy(loginBean.getId()) ;
				result = mgt.addUser(userBean) ;
				req.getSession().setAttribute("BBSUser", userBean) ;
			}
        }
        
        //进行权限验证
        String topicId = req.getParameter("topicId");
        if(!loginBean.getId().equals("1") && topicId !=null && topicId.length() > 0){
        	//topicId不为空时，需要权限校验，topicId为空时，表时是进入论坛首页，不需校验
        	HashMap map = (HashMap)req.getSession().getAttribute("BBSUserRight");
        	if(map==null){
        		map = new HashMap();
        		req.getSession().setAttribute("BBSUserRight", map) ;
        	}
        	HashMap rightMap = (HashMap)map.get(topicId);
        	if(rightMap == null){
        		rightMap = new HashMap();
        		Result result = mgt.queryTopicScope(topicId, userBean.getId(), getLoginBean(req).getId(),
						getLoginBean(req).getDepartCode(), "1,") ;
        		rightMap.put("1", result.retVal);        		
        	}
        	
        	Boolean bl = (Boolean)rightMap.get("1");
        	if(!bl){
        		//没有权限
        		ActionForward forward = getForward(req, mapping, "alert");
        		req.setAttribute("noback", true);
                EchoMessage.error().add(getMessage(req,
                                                   "common.msg.RET_NO_RIGHT_ERROR")).
                    setAlertRequest(req);
                return forward;
        	}
        }
		return null;
	}
	
	/**
	 * 查询 论坛 当前位置
	 * @param parentList
	 * @return
	 */
	public String queryParentName(String formName,String topicId,String firstPage,String addTHhead){
		String parentUrl = "";
		
		parentUrl += "<a href=\"/OABBSForumQueryAction.do?addTHhead="+addTHhead+"\">"+firstPage+"</a> >> ";
		if(topicId != null && topicId.length() >0){
			parentUrl += "<a href=\"/OABBSForumQueryAction.do?topicId="+topicId+"&addTHhead="+addTHhead+"\">"+formName+"</a>";
		}else{
			parentUrl += formName;
		}
			
		return parentUrl ;
	}
	
	/**
	 * 查询 论坛 当前位置
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
}
