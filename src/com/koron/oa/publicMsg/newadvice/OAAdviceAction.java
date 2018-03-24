package com.koron.oa.publicMsg.newadvice;

import java.io.File;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bean.OAAdviceBean;
import com.koron.oa.bean.OANewsInfoReplyBean;
import com.koron.oa.publicMsg.newadvice.OAAdviceMgt;
import com.koron.oa.publicMsg.newsInfo.OANewsMgt;
import com.koron.oa.util.AttentionMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;

import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.OperationConst;
/**
 * <p>
 * Title:通知通告
 * <p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2012-06-19
 * @CopyRight:科荣软件
 * @Author:李文祥
 */
public class OAAdviceAction extends MgtBaseAction {

	OAAdviceMgt mgt = new OAAdviceMgt();
	OANewsMgt newsmgt=new OANewsMgt();
	OAAdviceMgt adviceMgt = new OAAdviceMgt();
	AdviceMgt amgt=new AdviceMgt();


	/**
	 * exe 控制器入口函数
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
	 * @todo Implement this com.huawei.sms.web.util.BaseAction method
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		/*是否添加body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		System.out.println(getParameter("addTHhead", request));
		ActionForward forward = null;
		switch (operation) {
		// 新增操作
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		// 新增通知通告前的准备
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		// 修改前的准备
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response);
			break;
		// 修改
		case OperationConst.OP_UPDATE:
			if("fb".equals(request.getParameter("updateType"))){
				forward= upSave(mapping, form, request, response);
			}else{
				forward = update(mapping, form, request, response);
			}
			break;
		// 根据条件查询
		case OperationConst.OP_QUERY:
			String type = getParameter("type", request);
			//添加评论
			if("addreply".equals(type) && null != type && !type.equals("")) 
				forward = addReply(mapping, form, request, response);
			else		
				forward = query(mapping, form, request, response);		
			break;
		// 通知通告详细信息
		case OperationConst.OP_DETAIL:
			String addtype=this.getParameter("type", request);
			if("addreplyPrepare".equals(addtype))
				forward=addreplyPrepare(mapping,form,request,response);
			else
				forward = detail(mapping, form, request, response);
			break;
		// 删除操作
		case OperationConst.OP_DELETE:
			String deleteType = getParameter("type",request);
			if(!"deletereply".equals(deleteType)) /*删除通知通告*/
				forward = delete(mapping, form, request, response);
			else  /*删除评论*/
				forward = deleteReply(mapping, form, request, response);
			break;
		//默认
		default:
			String opentype=getParameter("type",request);
			if(null != opentype && !opentype.equals("") )
				forward = toTree(mapping, form, request, response);
			else
				forward = frame(mapping, form, request, response); // 通知通告首页
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
    protected ActionForward frame(ActionMapping mapping,
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws
            Exception {
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
        return getForward(request, mapping, "tree");
    }
    /**
	 * 加载评论页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    protected ActionForward addreplyPrepare(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
    	String adviceId=this.getParameter("adviceId", request);
    	int pagesize=this.getParameterInt("pageSize", request);
		int pageno=this.getParameterInt("pageNo", request);
		if (pageno ==0)
				pageno=1;
		if(pagesize==0)
				pagesize=15;
    	Result replyResult = mgt.queryReplys(adviceId,pageno,pagesize);
    	/*获取所有评论者的图片*/
		Result allPhotos =newsmgt.queryAllPhoto(adviceId);
		request.setAttribute("userPhotos", allPhotos.retVal) ;
    	request.setAttribute("replyList", replyResult.getRetVal());
    	request.setAttribute("adviceId", adviceId);
    	request.setAttribute("pageBar", pageBar(replyResult, request));
        return getForward(request, mapping, "to_addreply");
    }
	/**
	 * 通知通告中心首页
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		OAAdviceSearchForm adviceSearchForm = (OAAdviceSearchForm) form;
		
		String type=this.getParameter("selectType", request);
		
		//判断是否是关键字和条件查询进入，如果是，给selectType赋值
		if("keyword".equals(type) || "gaoji".equals(type))
			adviceSearchForm.setSelectType(type);
		
		//如果是关键字搜索，获取关键字内容
		if("keyword".equals(type)){
			String kw = this.getParameter("keywordVal", request);
			kw = GlobalsTool.toChinseChar(kw);
			adviceSearchForm.setKeyWord(kw);
			};
		
			
		//判断是否是新增后返回列表页面
		if("addreturn".equals(type)){
			
			adviceSearchForm.setSelectType("type");
			adviceSearchForm.setSelectId(this.getParameter("selectId", request));	
		}
			
		//判断是否是首次选择查询
		if(null == adviceSearchForm.getHavingType()){
			adviceSearchForm.setHavingType(adviceSearchForm.getSelectType());
			adviceSearchForm.setHavingId(adviceSearchForm.getSelectId());
		}
		/*begin  获取本次和上次选择的查询类型（时间关键字、通知通告类型）*/
		String x=adviceSearchForm.getSelectType();
		String y=adviceSearchForm.getHavingType();
		String a=adviceSearchForm.getSelectId();
		String b=adviceSearchForm.getHavingId();
		
		/*end*/
		
		//记录翻页后，选择其他查询类型，默认将页数改为第一页
		if(x!=null && y!=null && a!=null && b!=null && (!y.equals(x) || !a.equals(b)) && adviceSearchForm.getPageNo()!=1){
			
			adviceSearchForm.setPageNo(1);
			adviceSearchForm.setHavingType(x);
			adviceSearchForm.setHavingId(a);
		}
		
		//判断是否是从系统模块进入，如果是，清空以前的Form数据
		if ("menu".equals(getParameter("src", request))) {
			adviceSearchForm = new OAAdviceSearchForm();
			request.getSession().setAttribute("NewsSearchForm", null);
		}
		LoginBean loginBean = this.getLoginBean(request);
		

		// 获取当前用户对此模块的权限
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/OAnewAdviceAction.do");
		request.setAttribute("add", mop.add()); // 增加权限
		request.setAttribute("delete", mop.delete()); // 删除权限
		request.setAttribute("update", mop.update()); // 修改权限
		request.setAttribute("query", mop.query()); // 修改权限
		
       //获得登陆用户所在分组
		String empGroup=loginBean.getGroupId();
		
		//获得登陆用户所在部门	
		String depts=loginBean.getDepartCode();
		
		//获取登陆用户ID
		String userId=loginBean.getId();
		
		if("indexType".equals(this.getParameter("opentype", request)) || "returnindex".equals(this.getParameter("opentype", request)))
			adviceSearchForm.setSelectType(this.getParameter("opentype", request));
		
		Result result=mgt.queryAdvice(adviceSearchForm, null,userId,empGroup,depts,null);
		
		if(null!=this.getParameter("selectAdvice", request))	
			request.setAttribute("selectAdvice", this.getParameter("selectAdvice", request));
		
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
		
			request.setAttribute("adviceList", result.retVal);
			request.setAttribute("pageBar", pageBar(result, request));
			request.setAttribute("thetype", "queryindex");
		}
		return getForward(request, mapping, "adviceIndex");

	}
	/**
	 * 新增通知通告中心
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OAAdviceForm adviceForm = (OAAdviceForm) form;
		/* 获得登陆者ID */
		String userId = this.getLoginBean(request).getId();
		String loginname =this.getLoginBean(request).getEmpFullName();
		String statu=this.getParameter("statu", request);
		/* 创建时间 */
		String createTime = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		String releaseTime = BaseDateFormat.format(new java.
                util.Date(), BaseDateFormat.yyyyMMddHHmmss);
		OAAdviceBean oaAdvice = new OAAdviceBean();
		read(adviceForm, oaAdvice);
		
		//如果暂时保存成草稿,则将状态设置为1,发布人和发布时间默认
		if("zc".equals(statu)){
			oaAdvice.setStatusId(1);			
		}
		else{ //设置发布人和发布时间为当前时间和当前登陆用户
			oaAdvice.setStatusId(0);
			oaAdvice.setPulisher(userId);
			oaAdvice.setPulishDate(releaseTime);
		}
	
		oaAdvice.setFilePath(this.getParameter("attachFiles", request));
		oaAdvice.setId(IDGenerater.getId());
		oaAdvice.setCreateBy(userId);
		oaAdvice.setCreateTime(createTime);
		oaAdvice.setLastupDateBy(userId);
		oaAdvice.setLastupDateTime(createTime);
		//获取提醒方式
		String[] wakeType = request.getParameterValues("wakeUpMode");

		// 需删除的附件
		String delFiles = getParameter("delPicFiles",request);
		// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0
					&& adviceForm.getFileName().indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/news/" + del);
				aFile.delete();
			}
		}
		// 提醒方式
		String wakeUpMode = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeUpMode += str + ",";
			}
		}
		oaAdvice.setWakeUpMode(wakeUpMode);
		Result result = new Result();
	
		result = mgt.addAdvice(oaAdvice);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//如果状态为1，证明已发布，并向通知对象发送通知消息
			if(oaAdvice.getStatusId()!=1){
				String popedomUserIds=mgt.getPopedomUser(oaAdvice,userId);
				BaseEnv.log.error("用户ID:"+popedomUserIds);
				// 添加提醒
				String title = loginname+GlobalsTool.getMessage(request, "oa.advice.new")
						+ oaAdvice.getAdviceTitle();
				String url = "/OAnewAdvice.do";
				String favoriteURL = url + "?noback=true&operation=5&adviceId="
						+ oaAdvice.getId() + "&isEspecial=1";
				String content = "<a href=javascript:mdiwin('" + favoriteURL
						+ "','"
						+ GlobalsTool.getMessage(request, "oa.common.adviceList")
						+ "')>" + title + getMessage(request, "com.click.see")
						+ "</a>"; // 内容
				//向用户添加提醒方式
				if (wakeType != null && wakeType.length > 0) {
					for (String type : wakeType) {
						new Thread(new NotifyFashion(userId, title, content,
								popedomUserIds, Integer.parseInt(type), "yes",
								oaAdvice.getId(),"","","advice")).start();
					}
				}
	
				// 全部成功后	
				EchoMessage.success().add(
						getMessage(request, "common.msg.addSuccess")).setBackUrl("/OAnewAdvice.do?operation=4&selectType=addreturn&selectId="+oaAdvice.getAdviceType()).setAlertRequest(request);
			}
			else{//状态不为1 ,证明是草稿，只需要保存成功
				EchoMessage.success().add(
						getMessage(request, "oa.mail.saveDraftSuccess")).setBackUrl("/OAnewAdvice.do?operation=4&selectType=addreturn&selectId="+oaAdvice.getAdviceType()).setAlertRequest(request);
			}
				
			return getForward(request, mapping, "message");
		} else {
			// 添加失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.addFailture"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}

	/**
	 * 新增通知通告前的准备
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return getForward(request, mapping, "to_addAdvice");
	}

	/**
	 * 删除通知通告中心
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String[] adviceIds = getParameter("adviceId", request).split(",");
		OAAdviceBean oabean=(OAAdviceBean)mgt.loadAdvice(adviceIds[0]).getRetVal();
		LoginBean loginBean=this.getLoginBean(request);
		//获得登陆用户所在分组
		String empGroup=loginBean.getGroupId();
		
		//获得登陆用户所在部门	
		String depts=loginBean.getDepartCode();
		
		//获取登陆用户ID
		String userId=loginBean.getId();
		OAAdviceSearchForm adviceSearchForm = (OAAdviceSearchForm) form;
		
		
		//获得要删除的记录的评论信息
		String  allreplyId= "";
		for(int j=0;j<adviceIds.length;j++){
			Result reply=mgt.queryReplys(adviceIds[j], 1, 1000);
			List<OANewsInfoReplyBean> allreply=(List<OANewsInfoReplyBean>)reply.getRetVal();
			for(int i=0;i<allreply.size();i++){
				allreplyId +=allreply.get(i).getId()+",";
			}
		}
		String[] allreplyIds=allreplyId.split(",");
		
		
		
        //上一条
		String selectAdvice="";
		Result preResult = mgt.queryAdvice(adviceSearchForm,oabean.getLastupDateTime(),userId,depts,empGroup,"detailPre");
		if (preResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<OAAdviceBean> preAdvice = (List<OAAdviceBean>) preResult.getRetVal();
			if (preAdvice != null && preAdvice.size() > 0) {
				selectAdvice= preAdvice.get(preAdvice.size()-1).getId();				
			}
		}
		Result result = mgt.deleteAdvice(adviceIds);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {		
			
			//删除和删除记录相关的通知消息
			String delIds=","+getParameter("adviceId", request)+",";
			amgt.deleteByRelationId(delIds, "");
			
			//删除此记录的评论信息
			if(allreplyIds.length>0){
				Result del=newsmgt.deleteReplys(allreplyIds);
			
			}
			// 删除成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.delSuccess")).setBackUrl(
					"/OAnewAdvice.do?operation=4&selectAdvice="+selectAdvice).setAlertRequest(request);
			return getForward(request, mapping, "message");
	
		} else {
			// 删除失败
			EchoMessage.error().add(getMessage(request, "common.msg.delError"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}
	
	/**
	 * 修改前的准备
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updatePrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 获得通知通告ID
		String adviceId = getParameter("adviceId", request);
		// 根据通知通告id查询通知通告信息
		Result oAdvice = mgt.loadAdvice(adviceId);
		OAAdviceBean oaadviceBean = (OAAdviceBean) oAdvice.getRetVal();

		String[] wakeUpType = null;// 提醒方式
		if (oaadviceBean.getWakeUpMode() != null
				&& !"".equals(oaadviceBean.getWakeUpMode())) {
			wakeUpType = oaadviceBean.getWakeUpMode().split(",");
		}
		/*获取通知对象*/
		List<EmployeeBean> targetUsers =newsmgt.getEmployee(oaadviceBean.getAccepter());
		List<Object> targetDept = newsmgt.getDepartment(oaadviceBean.getPopedomDeptIds());
		List<Object> listEmpGroup = newsmgt.getEmpGroup(oaadviceBean.getPopedomEmpGroupIds());

		request.setAttribute("ownAdvice", oaadviceBean);
		request.setAttribute("targetUsers", targetUsers);
		request.setAttribute("targetDept", targetDept);
		request.setAttribute("targetEmpGroup", listEmpGroup);
		request.setAttribute("wakeUpType", wakeUpType); // 提示方式
		//判断是否是从详细页面进行的修改操作
		request.setAttribute("position", this.getParameter("position", request));
		return getForward(request, mapping, "to_updateAdvice");
	}

	/**
	 * 修改通知通告中心
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		OAAdviceForm adviceForm = (OAAdviceForm) form;
		/* 获得登陆者ID */
		String userId = this.getLoginBean(request).getId();
		String loginname =this.getLoginBean(request).getEmpFullName();
		String[] wakeType = request.getParameterValues("wakeUpMode");
		// 需删除的附件
		String delFiles = getParameter("delPicFiles",request);
		// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0
					&& adviceForm.getFileName().indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/news/" + del);
				aFile.delete();
			}
		}
		//判断是否是从详细页面进行的修改操作
		String position=this.getParameter("position", request);
		//加载通知通告
		Result result = mgt.loadAdvice(adviceForm.getAdviceId());
		//判断此记录是否还存在
		if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add(
					getMessage(request, "this.record.not exist"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		OAAdviceBean oaAdviceBean = (OAAdviceBean) result.getRetVal();
		
		// 提醒方式
		String wakeupType = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeupType += str + ",";
			}
		}

		/* form到Bean中 */
		read(adviceForm, oaAdviceBean);
		oaAdviceBean.setFilePath(this.getParameter("attachFiles", request));
		oaAdviceBean.setLastupDateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		oaAdviceBean.setLastupDateBy(getLoginBean(request).getId());
		oaAdviceBean.setWakeUpMode(wakeupType);
		//判断通知是否发布
		String statu=this.getParameter("statu", request);
	

		//如果是未发布信息，执行操作
		if(oaAdviceBean.getStatusId()==1){ 
			if(!"zc".equals(statu)){ //判断是否点击的是保存按钮，如果是，发布信息。并且修改发布人和发布时间
				oaAdviceBean.setStatusId(0);
				oaAdviceBean.setPulishDate( BaseDateFormat.format(new java.util.Date(), BaseDateFormat.yyyyMMddHHmmss));
				oaAdviceBean.setPulisher(userId);		
			}	
		}
		else{ //如果是已发布信息
			String id=","+oaAdviceBean.getId()+",";
			amgt.deleteByRelationId(id, "");
		}
		
		
		result = mgt.updateAdvice(oaAdviceBean);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//如果状态为1，证明发布通知通告，并向通知对象发送通知消息
			if(oaAdviceBean.getStatusId()!=1){
				//获取通知对象
		   		String popedomUserIds=mgt.getPopedomUser(oaAdviceBean,userId);
				
				// 添加提醒方式
				String title = loginname+GlobalsTool.getMessage(request, "oa.advice.new.update")
						+ oaAdviceBean.getAdviceTitle();
				String url = "/OAnewAdvice.do";
				String favoriteURL = url + "?noback=true&operation=5&adviceId="
						+ oaAdviceBean.getId() + "&isEspecial=1";
				String content = "<a href=javascript:mdiwin('" + favoriteURL
						+ "','"
						+ GlobalsTool.getMessage(request, "oa.common.adviceList")
						+ "')>" + title + getMessage(request, "com.click.see")
						+ "</a>"; // 内容
				if (wakeType != null && wakeType.length > 0) {
					for (String type : wakeType) {
						new Thread(new NotifyFashion(userId, title, content,
								popedomUserIds, Integer.parseInt(type), "yes",
								oaAdviceBean.getId(),"","","advice")).start();
					}
				}
			}
		/*	// 修改成功(修改草稿或修改已发布的新闻)
			if("detailpage".equals(position))  //判断是否是从详细页面进行的修改操作
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess")).setBackUrl(
						"/OAnewAdvice.do?operation=5&adviceId="+oaAdviceBean.getId()).setAlertRequest(request);
			else
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess")).setBackUrl(
						"/OAnewAdvice.do?operation=4").setAlertRequest(request);
			return getForward(request, mapping, "message");*/
			EchoMessage.success().add(
					getMessage(request, "common.msg.updateSuccess")).setBackUrl(
					"/OAnewAdvice.do?operation=5&adviceId="+oaAdviceBean.getId()).setAlertRequest(request);
			
			return getForward(request, mapping, "message");
		} else {
			// 修改失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateErro"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}

	/**
	 * 通知通告详细信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	protected ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
			OAAdviceSearchForm adviceSearchForm = (OAAdviceSearchForm) form;
			//获取进入详情页面的路径
			String Especial=this.getParameter("isEspecial", request);
			
//			获取是我的收藏点击进入的
			String myCollection=this.getParameter("isMyCollection", request);
			
			String innoback=this.getParameter("noback", request);
	
			int pagesize=this.getParameterInt("pageSize", request);
			int pageno=this.getParameterInt("pageNo", request);
			if (pageno ==0)
				pageno=1;		
			if(pagesize==0)
					pagesize=15;
			String adviceId = getParameter("adviceId",request);
			//判断是否已经加入收藏
			Result isexit = new AttentionMgt().isAttention(getLoginBean(request).getId(), adviceId, "OAnewAdvice");		
			if(isexit.retCode == ErrorCanst.MULTI_VALUE_ERROR){
				request.setAttribute("attention", "OK");
			}
			
			// 根据ID获得通知通告信息
			Result oaAdvice = mgt.loadAdvice(adviceId);
			OAAdviceBean oaAdviceBean = (OAAdviceBean) oaAdvice.getRetVal();
			request.setAttribute("oaAdvice", oaAdviceBean);

			/*获取通知对象（用户、部门、组）*/
			List<EmployeeBean> targetUsers =newsmgt.getEmployee(oaAdviceBean.getAccepter());
			List<Object> targetDept = newsmgt.getDepartment(oaAdviceBean.getPopedomDeptIds());
			List<Object> listEmpGroup = newsmgt.getEmpGroup(oaAdviceBean.getPopedomEmpGroupIds());
			
			
			LoginBean loginBean = this.getLoginBean(request);
			//获得登陆用户所在分组
			String empGroup=loginBean.getGroupId();
			
			//获得登陆用户所在部门	
			String depts=loginBean.getDepartCode();
			
			//获取登陆用户ID
			String userId=loginBean.getId();
			
			if(!"1".equals(Especial) && !"true".equals(innoback) ){			
				//上一条
				Result preResult = mgt.queryAdvice(adviceSearchForm,oaAdviceBean.getLastupDateTime(),userId,depts,empGroup,"detailPre");
				if (preResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					List<OAAdviceBean> preNews = (List<OAAdviceBean>) preResult.getRetVal();
					if (preNews != null && preNews.size() > 0) {
						request.setAttribute("preId", preNews.get(preNews.size()-1).getId());
						request.setAttribute("preTitle",preNews.get(preNews.size()-1).getAdviceTitle());
					}
				}
				// 下一条
				Result nextResult = mgt.queryAdvice(adviceSearchForm,oaAdviceBean.getLastupDateTime(),userId,depts,empGroup,"detailNext");
				if (nextResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					List<OAAdviceBean> nextNews = (List<OAAdviceBean>) nextResult.getRetVal();
					if (nextNews != null && nextNews.size() > 0) {
						request.setAttribute("nextId", nextNews.get(0).getId());
						request.setAttribute("nextTitle",nextNews.get(0).getAdviceTitle());
					}
				}
				
			}
			String url = request.getRequestURI();
			String favoriteURL = URLEncoder.encode(url + "?operation=5&adviceId="
					+ adviceId + "&isEspecial=1", "utf-8");
			String myCollectionURL = URLEncoder.encode("&isMyCollection=1", "utf-8");
			request.setAttribute("myCollectionURL", myCollectionURL);
			request.setAttribute("favoriteURL", favoriteURL);

			String uri = java.net.URLEncoder.encode(url + "?operation=5&adviceId=" + adviceId);
			request.setAttribute("uri", uri);
			request.setAttribute("targetUsers", targetUsers);
			request.setAttribute("targetDept", targetDept);
			request.setAttribute("targetEmpGroup", listEmpGroup);
			request.setAttribute("backtype", innoback);
			request.setAttribute("IsEspecial", Especial);
			request.setAttribute("isMyCollection", myCollection);
			request.setAttribute("messageTitle", this.getLoginBean(request)
					.getEmpFullName()
					+ getMessage(request, "com.invite.Notification")
					+ oaAdviceBean.getAdviceTitle());
		
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
					.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "to_detailAdvice");

	}

	/**
	 * 添加通知通告评论
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addReply(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {				
		/* 获取通知通告ID */
		String adviceid = getParameter("adviceid", request) ;
		String content= getParameter("content", request);

		/* 创建时间 */
		String createTime = BaseDateFormat.format(new java.util.Date(),	BaseDateFormat.yyyyMMddHHmmss);
		OANewsInfoReplyBean oaNewsReply = new OANewsInfoReplyBean();
		oaNewsReply.setId(IDGenerater.getId());
		oaNewsReply.setCreateBy(getLoginBean(request).getId());
		oaNewsReply.setFullName(getLoginBean(request).getEmpFullName());
		oaNewsReply.setCreateTime(createTime);
		oaNewsReply.setNewsId(adviceid);
		oaNewsReply.setContent(content);
		Result result = mgt.addreply(oaNewsReply);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 添加成功
			EchoMessage.success().add(
					getMessage(request, "oa.addreply.success")).setBackUrl(
					"/OAnewAdvice.do?operation=5&type=addreplyPrepare&adviceId=" + adviceid)
					.setAlertRequest(request);
			
			return getForward(request, mapping, "message");
		} else {
			// 添加失败
			EchoMessage.error().add(
					getMessage(request, "oa.addreply.failure"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}

	/**
	 * 删除通知通告评论
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward deleteReply(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String replyid = getParameter("replyId",request);
		String adviceId = getParameter("adviceId",request);
		
		Result result = mgt.deleteReply(replyid);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 删除成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.delSuccess")).setBackUrl(
					"/OAnewAdvice.do?operation=5&type=addreplyPrepare&adviceId=" + adviceId)
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		} else {
			// 删除失败
			EchoMessage.error().add(getMessage(request, "common.msg.delError"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}
	/**
	 * 草稿详情页面点击发布
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward upSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String adviceId = getParameter("adviceId",request);
		String userId = this.getLoginBean(request).getId();
		String loginname =this.getLoginBean(request).getEmpFullName();
		/* 发布时间*/
		String releaseTime = BaseDateFormat.format(new java.
                   util.Date(), BaseDateFormat.yyyyMMddHHmmss);
		Result result = mgt.detailSave(adviceId,releaseTime);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 删除成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.addSuccess")).setBackUrl(
					"/OAnewAdvice.do?operation=4")
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		} else {
			// 删除失败
			EchoMessage.error().add(getMessage(request, "common.msg.addFailture")).setBackUrl("" +
					"/OAnewAdvice.do?operation=4")
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}
}
