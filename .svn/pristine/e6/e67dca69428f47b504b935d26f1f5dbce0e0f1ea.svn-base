package com.menyi.aio.web.advice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.AdviceBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.msgif.CancelMsgReq;
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
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Aug 24, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class AdviceAction extends BaseAction {

	AdviceMgt mgt = new AdviceMgt();
	UserMgt userMgt = new UserMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		String noback = request.getParameter("noback");// 不能返回
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		/*是否添加body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_UPDATE:
			forward = readOver(mapping, form, request, response);
			break;
		case OperationConst.OP_DETAIL:
			forward = detail(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			forward = query(mapping,form,request,response);
			break;
		default:
			String module = request.getParameter("module");
			String opentype=getParameter("actiontype",request);
			if(module != null && !"".equals(module)) {
				if ("dedailAdviceInfo".equals(module)) {
					addDedailAdviceInfoMessage(request, response);
				}else if("invitePre".equals(module)){
					//邀请阅读前
					forward = invitePre(mapping, form, request, response);
				}else if("inviteAdvice".equals(module)){
					//邀请
					forward = addInviteAdvice(mapping, form, request, response);
				}
			}else if(null != opentype && !opentype.equals("")){
				forward = toTree(mapping, form, request, response);
			}else{
				forward = frame(mapping, form, request, response);
			}
	    }
		return forward;
	}
	
	/**
	    * 加载框架
	    * @param mapping
	    * @param form
	    * @param request
	    * @param response
	    * @return
	    * @throws Exception
	    */
	 protected ActionForward frame(ActionMapping mapping,ActionForm form,
              HttpServletRequest request, HttpServletResponse response) throws Exception {
		 String deskType=this.getParameter("type", request);
		 String src=this.getParameter("src", request);
		 if("menu".equals(src)){ 
			 deskType="noRead";
		 }
		 request.setAttribute("deskType", deskType);
		 return getForward(request, mapping, "frame");
	 }

	 /**
		 * 加载左侧页面
		 * @param mapping
		 * @param form
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
	    protected ActionForward toTree(ActionMapping mapping,
	                                   ActionForm form,
	                                   HttpServletRequest request,
	                                   HttpServletResponse response) throws
	            Exception {
	    	String deskType=this.getParameter("deskType", request);
	    	request.setAttribute("selectType", deskType);
	        return getForward(request, mapping, "tree");
	    }
	/**
	 * 已阅读
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward readOver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] keyIds = request.getParameterValues("keyId");
		String ids = "";
		for (String id : keyIds) {
			ids += id + ",";
		}
		boolean rst = mgt.readOverById(ids);
		if (rst) {
			return this.query(mapping, form, request, response);

		} else {
			EchoMessage.error().add(
					getMessage(request, "AccPeriodInfo.update.error"))
					.setBackUrl("/AdviceAction.do?src=menu").setAlertRequest(
							request);
			return getForward(request, mapping, "message");
		}
	}

	/**
	 * 删除
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = null;
		String ids = getParameter("keyId", request);

		if (ids != null && ids.length() > 0) {
			if (mgt.deleteById(ids)) {
				request.setAttribute("result", ErrorCanst.DEFAULT_SUCCESS);
			} else {
				// 删除失败
				EchoMessage.error()
						.add(getMessage(request, "common.msg.delError"))
						.setRequest(request);
				forward = getForward(request, mapping, "message");
			}
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * 查询
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		AdviceForm adviceform = (AdviceForm) form;
		String goType=this.getParameter("selectType", request);
	    if("keword".equals(goType)){ //关键字
			String kw = this.getParameter("keywordVal", request);
			kw = GlobalsTool.toChinseChar(kw);
			request.getSession().setAttribute("Adviceform", null);
			adviceform.setTitle(kw);		
		}	
		if("daiShen".equals(goType)&& !"approve".equals(adviceform.getType())){//待审消息
			adviceform = new AdviceForm();
			request.getSession().setAttribute("AdviceForm", null);
			adviceform.setType("notApprove");
		}		
		if("hasShen".equals(goType) && !"approve".equals(adviceform.getType())){ //已审消息
			adviceform = new AdviceForm();
			request.getSession().setAttribute("AdviceForm", null);
			adviceform.setType("approve");
		}
		if("email".equals(goType) && !"email".equals(adviceform.getType())){  //邮件消息
			adviceform = new AdviceForm();
			request.getSession().setAttribute("AdviceForm", null);
			adviceform.setType("email");
		}
		if("other".equals(goType) && (!"other".equals(adviceform.getType()) || !"Other".equals(adviceform.getType()))){  //其他消息
			adviceform = new AdviceForm();
			request.getSession().setAttribute("AdviceForm", null);
			adviceform.setType("other");
		}
		
		if("noRead".equals(goType) && !"noRead".equals(adviceform.getStatus())){  //未读消息
			adviceform = new AdviceForm();
			request.getSession().setAttribute("AdviceForm", null);
			adviceform.setStatus("noRead");
		}		
		if("hasRead".equals(goType) && !"read".equals(adviceform.getStatus())){ //已读消息
			adviceform = new AdviceForm();
			request.getSession().setAttribute("AdviceForm", null);
			adviceform.setStatus("read");
		} 
		if("".equals(goType) || "all".equals(goType)){
			adviceform = new AdviceForm();
			request.getSession().setAttribute("AdviceForm", null);
		}
		
		Result rs = mgt.query(this.getLoginBean(request).getId(), adviceform
				.getContent(), adviceform.getType(), adviceform.getStartDate(),
				adviceform.getEndDate(), adviceform.getStatus(), adviceform
						.getPageNo(), adviceform.getPageSize(), adviceform
						.getTitle());
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 查询成功
			// 对查询结果资源文件替换
			for (int i = 0; rs.retVal != null
					&& i < ((ArrayList<AdviceBean>) rs.retVal).size(); i++) {
				AdviceBean bean = ((ArrayList<AdviceBean>) rs.retVal).get(i);
				bean.setTitle(replace(request, bean.getTitle()));
				bean.setContent(replace(request, bean.getContent()));
				if (bean.getStatus().equals("noRead")) {
					
					if (bean.getContent().toLowerCase().indexOf("<a ") > -1) {
						bean.setContent(bean.getContent().substring(0,
								bean.getContent().toLowerCase().indexOf("<a ")+3)
								+ "onclick=\" read('"
								+ bean.getId()
								+ "'); \""
								+ bean.getContent().substring(
										bean.getContent().toLowerCase().indexOf("<a ")+3));
					}
					
//					if (bean.getContent().indexOf("mdiwin") > 0) {
//						bean.setContent(bean.getContent().substring(0,
//								bean.getContent().indexOf("mdiwin"))
//								+ "read('"
//								+ bean.getId()
//								+ "');"
//								+ bean.getContent().substring(
//										bean.getContent().indexOf("mdiwin")));
//					}
				}
			}
			request.setAttribute("AdviceForm", adviceform);
			request.setAttribute("result", rs.retVal);
			request.setAttribute("loginId", this.getLoginBean(request).getId());
			request.setAttribute("pageBar", pageBar(rs, request));
		} else {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "adviceList");
	}

	private String replace(HttpServletRequest request, String str) {
		while (str.indexOf("RES<") > -1) {
			int pos = str.indexOf("RES<");
			String temp = str.substring(pos + 4, str.indexOf(">", pos));
			temp = this.getMessage(request, temp);
			str = str.substring(0, pos) + temp
					+ str.substring(str.indexOf(">", pos) + 1);
		}
		return str;
	}

	/**
	 * 明细
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String desktop = getParameter("desktop", request) == null ? ""
				: getParameter("desktop", request);
		request.setAttribute("desktop", desktop);
		String nstr = request.getParameter("keyId");
		if (nstr != null && nstr.length() != 0) {
			// 执行加载明细
			Result rs = mgt.detail(nstr);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				// 加载成功
				AdviceBean msgBean = (AdviceBean) rs.retVal;
				msgBean.setTitle(replace(request, msgBean.getTitle()));
				msgBean.setContent(replace(request, msgBean.getContent()));

				request.setAttribute("result", rs.retVal);

				if (this.getLoginBean(request).getId().equals(
						msgBean.getReceive())
						&& "noRead".equals(msgBean.getStatus())) {
					if (mgt.readOverById(nstr)) {
						EchoMessage.error().add(
								getMessage(request, "common.msg.error"))
								.setRequest(request);
						return getForward(request, mapping, "message");
					}
				}
			} else if (rs.retCode == ErrorCanst.ER_NO_DATA) {
				// 记录不存在错误
				EchoMessage.error()
						.add(getMessage(request, "com.msg.not.find"))
						.setBackUrl(
								"/AdviceAction.do?winCurIndex="
										+ getParameter("winCurIndex", request))
						.setRequest(request);
				return getForward(request, mapping, "message");
			} else {
				// 加载失败
				EchoMessage.error()
						.add(getMessage(request, "common.msg.error"))
						.setRequest(request);
				return getForward(request, mapping, "message");
			}
		}
		request.setAttribute("loginId", this.getLoginBean(request).getId());
		return getForward(request, mapping, "adviceDetail");
	}

	/**
	 * 邀请阅读的方法
	 * 
	 * @param request
	 * @param response
	 */
	private void addDedailAdviceInfoMessage(HttpServletRequest request, HttpServletResponse response) {
//    	AdviceForm oaMessage=new AdviceForm();
    	String favoriteURL=request.getParameter("favoriteURL");
    	String favoriteName=request.getParameter("favoriteName");  
    	favoriteName = GlobalsTool.toChinseChar(favoriteName); 
    	String moduleName = "";
    	String type = "Other";
    	if(favoriteURL.contains("OABBSForumAction.do")){
    		moduleName = getMessage(request, "oa.bbs.innerBBS");
    	}else if(favoriteURL.contains("OAKnowCenter.do")){
    		moduleName = getMessage(request, "oa.common.knowledgeCenter");
    	}else if(favoriteURL.contains("OAnewOrdain.do")){
    		moduleName = getMessage(request, "oa.common.bylaw");
    	}else if(favoriteURL.contains("OANews.do")){
    		moduleName = getMessage(request, "oa.common.newList");
    	}else if(favoriteURL.contains("OAnewAdvice.do")){
    		moduleName = getMessage(request, "oa.common.adviceList");
    	}else if(favoriteURL.contains("AlbumQueryAction.do")||favoriteURL.contains("PhotoAction.do")){
    		moduleName = getMessage(request, "enterprise.album");
    	}else if (favoriteURL.contains("EMailAction.do")){
    		type = "email";
    		moduleName = getMessage(request, "oa.mail.myMail");
    	} else{
    		moduleName = favoriteName;
    	}
    	
    	String content="<a href=javascript:mdiwin('"+favoriteURL+"','"+moduleName+"')>"+favoriteName+getMessage(request, "com.click.see")+"</a>";// oa.oamessage.clickin
    	String send=this.getLoginBean(request).getId();
    	String id=IDGenerater.getId();
    	 
//    	String sendName=OnlineUserInfo.getUser(send).getName();
    	String receive=request.getParameter("receive");
//    	String receiveName=OnlineUserInfo.getUser(receive).getName();
//    	oaMessage.setContent(content);
//    	oaMessage.setId(id);
//    	oaMessage.setCreateTime(BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss));
//    	oaMessage.setReceive(receive);
//    	oaMessage.setReceiveName(receiveName);
//    	oaMessage.setSend(send);
//    	oaMessage.setSendName(sendName);
//    	oaMessage.setTitle(favoriteName);
//    	oaMessage.setRelatMsgId("");
//    	oaMessage.setType(type);
    	// mj
    	String relationId = request.getParameter("relationId");
//    	oaMessage.setRelationId(relationId);
//    	mgt.add(oaMessage);
    	mgt.add(send, favoriteName, content, receive, relationId, type);
	}
	
	
	/**
	 * 邀请阅读人前
	 * @param mapping
	 * @param from
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward invitePre(ActionMapping mapping, ActionForm from,
			HttpServletRequest request, HttpServletResponse response){
			
		
		String favoriteURL = request.getParameter("favoriteURL");
		if(favoriteURL != null && !"".equals(favoriteURL)){
			favoriteURL = GlobalsTool.toChinseChar(favoriteURL);
		}
		String title = request.getParameter("title");
		if(title != null && !"".equals(title)){
			title = GlobalsTool.toChinseChar(title);
		}
		String favoriteName = request.getParameter("favoriteName");
		if(favoriteName != null && !"".equals(favoriteName)){
			favoriteName = GlobalsTool.toChinseChar(favoriteName);
		}
		String userinvite = request.getParameter("userinvite");
		if(userinvite != null && !"".equals(userinvite)){
			userinvite = GlobalsTool.toChinseChar(userinvite);
		}
		request.setAttribute("favoriteURL", favoriteURL);
		request.setAttribute("title", title);
		request.setAttribute("userinvite", userinvite);
		request.setAttribute("favoriteName", favoriteName);
		request.setAttribute("id", request.getParameter("id"));
		return getForward(request, mapping, "inviteAdvice");
	}
	
	/**
	 * 邀请人
	 * @param request
	 * @param response
	 */
	protected ActionForward addInviteAdvice(ActionMapping mapping, ActionForm from,
			HttpServletRequest request, HttpServletResponse response){
		
		LoginBean loginBean = this.getLoginBean(request);
		String popedomDeptIds = request.getParameter("popedomDeptIds");   		//部门
		String popedomUserIds = request.getParameter("popedomUserIds");			//职员
		String empGroupId = request.getParameter("empGroupId");					//分组
		String favoriteURL = request.getParameter("favoriteURL");				//连接地址
		String id = request.getParameter("id");									//文件的id
		String description = request.getParameter("description");				//内容
		String favoriteName = request.getParameter("favoriteName");
		try {
		
			String moduleName = "";
	    	if(favoriteURL.contains("OABBSForumAction.do")){
	    		moduleName = getMessage(request, "oa.bbs.innerBBS");
	    	}else if(favoriteURL.contains("OAKnowCenter.do")){
	    		moduleName = getMessage(request, "oa.common.knowledgeCenter");
	    	}else if(favoriteURL.contains("OAnewOrdain.do")){
	    		moduleName = getMessage(request, "oa.common.bylaw");
	    	}else if(favoriteURL.contains("OANews.do")){
	    		moduleName = getMessage(request, "oa.common.newList");
	    	}else if(favoriteURL.contains("OAnewAdvice.do")){
	    		moduleName = getMessage(request, "oa.common.adviceList");
	    	}else if(favoriteURL.contains("AlbumQueryAction.do")||favoriteURL.contains("PhotoAction.do")){
	    		moduleName = getMessage(request, "personal.album");
	    	}else if (favoriteURL.contains("EMailAction.do")){
	    		moduleName = getMessage(request, "oa.mail.myMail");
	    	} else{
	    		moduleName = favoriteName;
	    	}
			String userIds = "";   //通知对象
			//个人
			if (popedomUserIds != null && popedomUserIds.length() > 0) {
				userIds += popedomUserIds;
			}
			//部门
			if (null != popedomDeptIds && !"".equals(popedomDeptIds)) {
				String[] deptIds = popedomDeptIds.split(","); 
				for (String departId : deptIds) {
					ArrayList<OnlineUser> users=OnlineUserInfo.getDeptUser(departId) ;
					for (OnlineUser user : users) {
						if (!userIds.contains(","+user.getId()+",")) {
							userIds += user.getId()+ ",";
						}
					}
				}
			}
			//职员分组
			if (null != empGroupId && !"".equals(empGroupId)) {
				String[] empGroupIds = empGroupId.split(","); // 根据分组查找分组人员
				for (String empGroup : empGroupIds) {
					List list = userMgt.queryAllEmployeeByGroup(empGroup);
					for (int i = 0; i < list.size(); i++) {
						if (!userIds.contains(list.get(i).toString())) {// 判断是否已经单独授权
							userIds += list.get(i).toString() + ",";
						}
					}
				}
			}
			
			String personnels="";
			//判断通知对象是否包含当前用户，不需要向自己发送通知消息
			if(userIds!=null && userIds.length()>0){
				String [] popedomUser=userIds.split(",");
				for(String pope:popedomUser){
					if(!pope.equals(loginBean.getId()))
						personnels +=pope+",";
				}
			}
			
			String content ="";
			String adviceType = "other";
			if(favoriteURL!=null && favoriteURL.contains("/OABBSForumAction.do")){
				adviceType = "bbs";
			}else if(favoriteURL!=null && favoriteURL.contains("/OAnewAdvice.do")){
				adviceType = "advice";
			}
			if(description ==null || "".equals(description)){
				content="<a href=javascript:mdiwin('"+favoriteURL+"','"+moduleName+"')>"+favoriteName+getMessage(request, "com.click.see")+"</a>";
				new NotifyFashion(loginBean.getId(), favoriteName, content, personnels, 4,"yes",id,"","",adviceType).start() ;
			}else{
				content = "<a href=javascript:mdiwin('"+favoriteURL+"','"+moduleName+"')>"+description+"</a>";
				//向用户添加提醒方式
		    	new NotifyFashion(loginBean.getId(), description, content, personnels, 4,"yes",id,"","",adviceType).start() ;
			}
			request.setAttribute("msg", "success");
		} catch (Exception e) {
			// TODO: handle exception
			request.setAttribute("msg", "failure");
		}
		
		return getForward(request, mapping, "blank");
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
}
