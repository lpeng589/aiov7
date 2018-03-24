package com.koron.oa.publicMsg.newordain;

import java.io.File;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;

import com.koron.oa.bean.OAOrdainBean;
import com.koron.oa.bean.OAOrdainGroupBean;
import com.koron.oa.publicMsg.newordain.OAOrdainMgt;
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
import com.menyi.web.util.PublicMgt;
/**
 * <p>
 * Title:规章制度
 * <p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2012-06-26
 * @CopyRight:科荣软件
 * @Author:李文祥
 */
public class OAOrdainAction extends MgtBaseAction {

	String username;
	OAOrdainMgt mgt = new OAOrdainMgt();
	OANewsMgt newsmgt=new OANewsMgt();
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
		ActionForward forward = null;
		switch (operation) {
		// 新增操作
		case OperationConst.OP_ADD:
			String thetype=this.getParameter("thetype", request);
			if("getShouquan".equals(thetype))
				forward=getShouquan(mapping,request,response);
			else
				forward = add(mapping, form, request, response);
			break;
		// 新增规章制度前的准备
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		// 修改前的准备
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response);
			break;
		// 修改
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		// 根据条件查询
		case OperationConst.OP_QUERY:
			String type=request.getParameter("dealType");
			String flag = request.getParameter("flag");//用于识别目录管理设置的上下级 1.表示本级,不需要筛选 2表示下级,需要筛选
			if(flag ==null || "".equals(flag)){
				
				flag ="1";
			}
			request.setAttribute("flag", flag);
			
			if("ordainGroup".equals(type)){
				forward= queryGroup(mapping,form,request,response);
			}else if("addGroup".equals(type)){
				forward = addPrepareGroup(mapping, form, request, response);
			}else if("add".equals(type)){
				forward = addGroup(mapping, form, request, response);
			}else if("updateGroup".equals(type)){
				forward = updatePrepareGroup(mapping, form, request,response);
			}else if("update".equals(type)){
				forward = updateGroup(mapping, form, request, response);
			}else if("del".equals(type)){
				String check=this.getParameter("check", request);
				if("true".equals(check))
					forward=checkGroup(mapping,request,response);
				else
					forward = del(mapping, form, request, response);
			}else{		
				forward = query(mapping, form, request, response);
			}
			break;
		// 规章制度详细
		case OperationConst.OP_DETAIL:
			forward = detail(mapping, form, request, response);
			break;
		// 删除操作
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		//默认
		default:
			String opentype=getParameter("type",request);
			if(null != opentype && !opentype.equals("") )
				forward = toTree(mapping, form, request, response);
			else
				forward = frame(mapping, form, request, response); // 规章制度首页
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
    	
        LoginBean loginBean=this.getLoginBean(request);
		//获得登陆用户所在部门	
		String depts=loginBean.getDepartCode();
		
		//获取登陆用户ID
		String userId=loginBean.getId();
    	
    	   Result rs=mgt.queryFolderByUserId(userId,depts);
           if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
           	ArrayList list=(ArrayList)rs.retVal;
           	String folderTree="";
           	String folderCode="";
           	for(int i=0;i<list.size();i++){
           		
           		String []obj=(String[])list.get(i);     
           		folderCode += "@acute;"+obj[1]+"@acute;";
				if(i != list.size()-1){
					folderCode += ",";
				}
           		if(obj[1].length()==5){
           			folderTree+=this.folderTree(list,obj[2], obj[1],obj[4],obj[0]);
           		} 
           	}
           	request.setAttribute("fCode", folderCode);
           	request.setAttribute("result",folderTree); 
           }
        return getForward(request, mapping, "tree");
    }
    /**
     * 加载组所在的树
     * @param list
     * @param folderName
     * @param classCode
     * @param isCatalog
     * @param folderId
     * @return
     */
    private String folderTree(ArrayList list,String folderName,String classCode,String isCatalog,String folderId){
       	String folderTree="";
       	if(isCatalog!=null&&isCatalog.equals("1")){
       		folderTree="<li><span><a style=\"color:#000000\" href=\"javascript:goFarme('/OAnewOrdain.do?operation="+OperationConst.OP_QUERY+"&selectType=type&groupCode="+classCode+"')\" onclick=\"clearContent();insertType('"+classCode+"');\" ><font id=\"type_"+classCode+"\" style=\"color: black\">"+folderName+"</font></a><font  id=\"_1\"></font></span><ul>";
       		for(int i=0;i<list.size();i++){
       			String []obj=(String[])list.get(i);    
       			if(obj[1].substring(0,obj[1].length()-5).equals(classCode)){
       				folderTree+=this.folderTree(list,obj[2], obj[1],obj[4],obj[0]);
       			}
       		}
       		folderTree+="</ul></li>";
       	}else{
       		folderTree="<li><span><a style=\"color:#000000\" href=\"javascript:goFarme('/OAnewOrdain.do?operation="+OperationConst.OP_QUERY+"&selectType=type&groupCode="+classCode+"')\" onclick=\"clearContent();insertType('"+classCode+"');\" ><font id=\"type_"+classCode+"\" style=\"color: black\">"+folderName+"</font></a><font  id=\"_1\"></font></span></li>";
       	}
       	
       	return folderTree;
    }
	/**
	 * 规章制度中心首页
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
		
        String selectId=this.getParameter("groupCode", request);   
		OAOrdainSearchForm ordainSearchForm = (OAOrdainSearchForm) form;
		String type=this.getParameter("selectType", request);
		
		String insertPla=this.getParameter("insertPlace", request);
		if(!"".equals(insertPla) && null!=insertPla){
			request.setAttribute("insertPlace", insertPla);
		}
		
		
		//判断是否是关键字和条件查询进入，如果是，给selectType赋值
		if("keyword".equals(type) || "gaoji".equals(type))
			ordainSearchForm.setSelectType(type);
		
		//保存多条件搜索选择的组
		String gName= this.getParameter("groupName", request);
		request.setAttribute("groupName", gName);
		
		//如果是关键字搜索，获取关键字内容
		
		if("keyword".equals(type) && null!=this.getParameter("keywordVal", request)){
			String kw = this.getParameter("keywordVal", request);
			kw = GlobalsTool.toChinseChar(kw);
			ordainSearchForm.setKeyWord(kw);
			}
			
		
		//判断是否是新增后返回列表页面
		if("addreturn".equals(type)){
			ordainSearchForm.setSelectType("type");
			ordainSearchForm.setSelectId(this.getParameter("selectId", request));
		}
		
		//如果是规章制度所属组查询，将相关参数存入SearchFrom	
		if("type".equals(type)){
			ordainSearchForm.setSelectType(type);
			ordainSearchForm.setSelectId(selectId);
		}
		
		/*判断是否是首次选择查询*/
		if(null == ordainSearchForm.getHavingType() && !"gaoji".equals(type)){
			ordainSearchForm.setHavingType(ordainSearchForm.getSelectType());
			ordainSearchForm.setHavingId(ordainSearchForm.getSelectId());
		}
		
		/*begin   获取本次和上次选择的查询类型（时间关键字、规章制度类型）*/
		String x=ordainSearchForm.getSelectType();
		String y=ordainSearchForm.getHavingType();
		String a=ordainSearchForm.getSelectId();
		String b=ordainSearchForm.getHavingId();
		
		/*end*/
		
		//记录翻页后，选择其他查询类型，默认将页数改为第一页
		if(x!=null && y!=null && a!=null && b!=null && (!y.equals(x) || !a.equals(b)) && ordainSearchForm.getPageNo()!=1){
			
			ordainSearchForm.setPageNo(1);
			ordainSearchForm.setHavingType(x);
			ordainSearchForm.setHavingId(a);
		}
		
        //判断是否是从系统模块进入，如果是，清空以前的Form数据
		if ("menu".equals(getParameter("src", request))) {
			ordainSearchForm = new OAOrdainSearchForm();
			request.getSession().setAttribute("OrdainSearchForm", null);
		}
		LoginBean loginBean = this.getLoginBean(request);

		// 获取当前用户对此模块的权限
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/OAnewOrdainAction.do");
		request.setAttribute("add", mop.add()); // 增加权限
		request.setAttribute("delete", mop.delete()); // 删除权限
		request.setAttribute("update", mop.update()); // 修改权限
		request.setAttribute("query", mop.query());   //查看权限
		
		 //获得登陆用户所在分组
		String empGroup=loginBean.getGroupId();
		
		//获得登陆用户所在部门	
		String depts=loginBean.getDepartCode();
		
		//获取登陆用户ID
		String userId=loginBean.getId();
		
		if("indexType".equals(this.getParameter("opentype", request)) || "returnindex".equals(this.getParameter("opentype", request)))
			ordainSearchForm.setSelectType(this.getParameter("opentype", request));
		
		Result result=mgt.queryOrdain(ordainSearchForm,null, userId,depts,null);
		
		if(null!=this.getParameter("selectOrdain", request))	
			request.setAttribute("selectOrdain", this.getParameter("selectOrdain", request));
		
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("ordainList", result.retVal);
			request.setAttribute("pageBar", pageBar(result, request));
			request.setAttribute("thetype", "queryindex");
		}
		return getForward(request, mapping, "ordainIndex");

	}
	/**
	 * 新增规章制度中心
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

		OAOrdainForm ordainForm = (OAOrdainForm) form;
		/* 获得登陆者ID */
		String userId = this.getLoginBean(request).getId();
		String loginname =this.getLoginBean(request).getEmpFullName();
	
		/* 创建时间 */
		String createTime = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		OAOrdainBean oaOrdain= new OAOrdainBean();
		read(ordainForm, oaOrdain);
		
		oaOrdain.setAccessories(this.getParameter("attachFiles", request));
		oaOrdain.setId(IDGenerater.getId());
		oaOrdain.setCreateBy(userId);
		oaOrdain.setCreateTime(createTime);
		oaOrdain.setLastupDateBy(userId);
		oaOrdain.setLastupDateTime(createTime);
		//获取提醒方式
		String[] wakeType = request.getParameterValues("wakeUpMode");

		// 需删除的附件
		String delFiles = getParameter("delPicFiles",request);
		// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0
					&& ordainForm.getAccessories().indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/news/" + del);
				aFile.delete();
			}
		}
		// 提醒方式
		String wakeupType = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeupType += str + ",";
			}
		}
		oaOrdain.setWakeupType(wakeupType);
		Result result = new Result();	
		result = mgt.addOrdain(oaOrdain);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
		
				//获取通知对象
				String popedomUserIds=mgt.getPopedomUser(oaOrdain,userId);
				// 添加提醒方式
				String title = loginname+GlobalsTool.getMessage(request, "oa.common.addbylaw")
						+ oaOrdain.getOrdainTitle();
				String url = "/OAnewOrdain.do";
				String favoriteURL = url + "?noback=true&operation=5&ordainId="
						+ oaOrdain.getId() + "&isEspecial=1";
				String content = "<a href=javascript:mdiwin('" + favoriteURL
						+ "','"
						+ GlobalsTool.getMessage(request, "oa.common.bylaw")
						+ "')>" + title + getMessage(request, "com.click.see")
						+ "</a>"; // 内容
				//向用户添加提醒方式
				if (wakeType != null && wakeType.length > 0) {
					for (String type : wakeType) {
						new Thread(new NotifyFashion(userId, title, content,
								popedomUserIds, Integer.parseInt(type), "yes",
								oaOrdain.getId(), null, null, "ordain")).start();
					}
				}
	
				// 全部成功后	
				EchoMessage.success().add(
						getMessage(request, "common.msg.addSuccess")).setBackUrl("/OAnewOrdain.do?operation=4&selectType=addreturn&selectId="+oaOrdain.getGroupId()).setAlertRequest(request);
				
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
	 * 新增规章制度前的准备
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		if("type".equals(this.getParameter("selectType", request))){
			Result  group=mgt.queryFolderByCode(this.getParameter("selectId", request));		
			List<OAOrdainGroupBean> obean=(List<OAOrdainGroupBean>) group.retVal;		
			request.setAttribute("selectType", obean.get(0).getGroupName());
			request.setAttribute("selectId",obean.get(0).getClassCode());
		}
		
	    
		return getForward(request, mapping, "to_addOrdain");
	}

	/**
	 * 删除规章制度中心
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

		String[] ordainIds = getParameter("ordainId", request).split(",");
		OAOrdainBean oabean=(OAOrdainBean)mgt.loadOrdain(ordainIds[0]).getRetVal();
		OAOrdainSearchForm ordainSearchForm = (OAOrdainSearchForm) form;
		LoginBean loginBean=this.getLoginBean(request);
		//获得登陆用户所在分组
		String empGroup=loginBean.getGroupId();
		
		//获得登陆用户所在部门	
		String depts=loginBean.getDepartCode();
		
		//获取登陆用户ID
		String userId=loginBean.getId();
		
        //上一条
		String selectOrdain="";
		Result preResult = mgt.queryOrdain(ordainSearchForm,oabean.getLastupDateTime(),userId,depts,"detailPre");
		if (preResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<OAOrdainBean> preOrdain = (List<OAOrdainBean>) preResult.getRetVal();
			if (preOrdain != null && preOrdain.size() > 0) {
				selectOrdain= preOrdain.get(preOrdain.size()-1).getId();				
			}
		}
		
		Result result = mgt.deleteOrdain(ordainIds);
		
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			
			//删除和删除记录相关的通知消息
			String delIds=","+getParameter("ordainId", request)+",";
			amgt.deleteByRelationId(delIds, "");
			
			// 删除成功
			EchoMessage.success().add(
					getMessage(request, "common.msg.delSuccess")).setBackUrl(
					"/OAnewOrdain.do?operation=4&selectOrdain="+selectOrdain).setAlertRequest(request);
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
	 * 
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
		// 获得规章制度ID
		String ordainId = getParameter("ordainId", request);
		// 根据规章制度id查询规章制度
		Result ordain = mgt.loadOrdain(ordainId);
		OAOrdainBean oaordainBean = (OAOrdainBean) ordain.getRetVal();
		BaseEnv.log.error("classCode的值:"+oaordainBean.getGroupId());
		Result groupResult =mgt.queryFolderByCode(oaordainBean.getGroupId());
		
		String[] wakeUpType = null;// 提醒方式
		if (oaordainBean.getWakeupType() != null
				&& !"".equals(oaordainBean.getWakeupType())) {
			wakeUpType = oaordainBean.getWakeupType().split(",");
		}
		/*获取通知对象*/
		List<EmployeeBean> targetUsers =newsmgt.getEmployee(oaordainBean.getPopedomUserIds());
		List<Object> targetDept = newsmgt.getDepartment(oaordainBean.getPopedomDeptIds());
		List<Object> listEmpGroup = newsmgt.getEmpGroup(oaordainBean.getPopedomEmpGroupIds());

		request.setAttribute("ownOrdain", oaordainBean);
		request.setAttribute("targetUsers", targetUsers);
		request.setAttribute("targetDept", targetDept);
		request.setAttribute("targetEmpGroup", listEmpGroup);
		request.setAttribute("wakeUpType", wakeUpType); // 提示方式
		request.setAttribute("group",groupResult.retVal);//所属组
		//判断是否是从详细页面进行的修改操作
		request.setAttribute("position", this.getParameter("position", request));
		return getForward(request, mapping, "to_updateOrdain");
	}

	/**
	 * 修改规章制度
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

		OAOrdainForm ordainForm = (OAOrdainForm) form;
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
					&& ordainForm.getAccessories().indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/news/" + del);
				aFile.delete();
			}
		}
		//判断是否是从详细页面进行的修改操作
		String position=this.getParameter("position", request);
		// 加载规章制度
		Result result = mgt.loadOrdain(ordainForm.getOrdainId());
		
		//判断此记录是否还存在
		if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add(
					getMessage(request, "this.record.not exist"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		OAOrdainBean oaOrdainBean = (OAOrdainBean) result.getRetVal();
		
		// 提醒方式
		String wakeupType = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeupType += str + ",";
			}
		}

		/* form到Bean中 */
		read(ordainForm, oaOrdainBean);
		oaOrdainBean.setAccessories(this.getParameter("attachFiles", request));
		oaOrdainBean.setLastupDateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		oaOrdainBean.setLastupDateBy(getLoginBean(request).getId());
		oaOrdainBean.setWakeupType(wakeupType);
				
		String id=","+oaOrdainBean.getId()+",";
		amgt.deleteByRelationId(id, "");
		
		result = mgt.updateOrdain(oaOrdainBean);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			
				//获取通知对象
				String popedomUserIds=mgt.getPopedomUser(oaOrdainBean,userId);
				
				// 添加提醒方式
				String title = loginname+GlobalsTool.getMessage(request, "oa.common.updateBylaw.add")
						+ oaOrdainBean.getOrdainTitle();
				String url = "/OAnewOrdain.do";
				String favoriteURL = url + "?noback=true&operation=5&ordainId="
						+ oaOrdainBean.getId() + "&isEspecial=1";
				String content = "<a href=javascript:mdiwin('" + favoriteURL
						+ "','"
						+ GlobalsTool.getMessage(request, "oa.common.bylaw")
						+ "')>" + title + getMessage(request, "com.click.see")
						+ "</a>"; // 内容
				if (wakeType != null && wakeType.length > 0) {
					for (String type : wakeType) {
						new Thread(new NotifyFashion(userId, title, content,
								popedomUserIds, Integer.parseInt(type), "yes",
								oaOrdainBean.getId())).start();
					}
				}
		
			/*// 修改成功
			if("detailpage".equals(position))  //判断是否是从详细页面进行的修改操作
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess")).setBackUrl(
						"/OAnewOrdain.do?operation=5&ordainId="+oaOrdainBean.getId()).setAlertRequest(request);
			else
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess")).setBackUrl(
						"/OAnewOrdain.do?operation=4").setAlertRequest(request);
		    return getForward(request, mapping, "message");*/
			EchoMessage.success().add(
				getMessage(request, "common.msg.updateSuccess")).setBackUrl(
					"/OAnewOrdain.do?operation=5&ordainId="+oaOrdainBean.getId()).setAlertRequest(request);	
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
	 * 规章制度详细信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "deprecation","unchecked" })
	protected ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OAOrdainBean oaOrdainBean=new OAOrdainBean();
		Result oaOrdain=null;
		try {
			//获取进入详情页面的路径
			String Especial=this.getParameter("isEspecial", request);
			
			//获取是我的收藏点击进入的
			String myCollection=this.getParameter("isMyCollection", request);
			
			String innoback=this.getParameter("noback", request);
		
			String oaordainId = getParameter("ordainId",request);
			OAOrdainSearchForm ordainSearchForm = (OAOrdainSearchForm) form;
			LoginBean loginBean = this.getLoginBean(request);
			//获得登陆用户所在分组
			String empGroup=loginBean.getGroupId();
			
			//获得登陆用户所在部门	
			String depts=loginBean.getDepartCode();
			
			//获取登陆用户ID
			String userId=loginBean.getId();
			
			//根据ID获得规章制度信息	 
			oaOrdain = mgt.loadOrdain(oaordainId);
			oaOrdainBean=(OAOrdainBean)oaOrdain.getRetVal();
			
			if("1".equals(Especial) || "true".equals(innoback) ){	//判断进入方式，是否是从通知消息进入的详细页面
				
				/*非列表页面链接到详细页面判断是否有权限*/
				/*//获取信息所属的组
				Result fuGroup=mgt.queryFolderByCode(oaOrdainBean.getGroupId().substring(0,5));
				List<OAOrdainGroupBean> fugroup=(List<OAOrdainGroupBean>)fuGroup.retVal;
				//获取有权限查看该信息的个人
				String[] getStr=mgt.getShouquan(fugroup.get(0).getPopedomDeptIds(),fugroup.get(0).getPopedomUserIds()).split("%%");

				if(getStr[1].indexOf(userId)<0 && !"1".equals(userId)){
					EchoMessage.error().add(getMessage(request, "common.msg.RET_NO_RIGHT_ERROR"))
					.setNotAutoBack().setAlertRequest(request);
					return getForward(request, mapping, "message");			
				}*/
				/*非列表页面链接到详细页面判断是否有权限*/
				
			}else{					
				//上一条
				Result preResult = mgt.queryOrdain(ordainSearchForm,oaOrdainBean.getLastupDateTime(),userId,depts,"detailPre");
				if (preResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					List<OAOrdainBean> preNews = (List<OAOrdainBean>) preResult.getRetVal();
					if (preNews != null && preNews.size() > 0) {
						request.setAttribute("preId", preNews.get(preNews.size()-1).getId());
						request.setAttribute("preTitle",preNews.get(preNews.size()-1).getOrdainTitle());
					}
				}
				// 下一条
				Result nextResult = mgt.queryOrdain(ordainSearchForm,oaOrdainBean.getLastupDateTime(),userId,depts,"detailNext");
				if (nextResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					List<OAOrdainBean> nextNews = (List<OAOrdainBean>) nextResult.getRetVal();
					if (nextNews != null && nextNews.size() > 0) {
						request.setAttribute("nextId", nextNews.get(0).getId());
						request.setAttribute("nextTitle",nextNews.get(0).getOrdainTitle());
					}
				}
			}
			
			/*获取通知对象*/
			List<EmployeeBean> targetUsers =newsmgt.getEmployee(oaOrdainBean.getPopedomUserIds());
			List<Object> targetDept = newsmgt.getDepartment(oaOrdainBean.getPopedomDeptIds());
			
			Result groupResult=mgt.queryFolderByCode(oaOrdainBean.getGroupId());
			
			/*判断开始:判断当前登录用户是否有下载附件的权限:onDown为True则有权限*/
			boolean onDown = false;
			
			List<OAOrdainGroupBean> gblist=(List<OAOrdainGroupBean>)groupResult.getRetVal();
			//获取该记录所在的组
			OAOrdainGroupBean gb=gblist.get(0);
			if(gb.getDownDeptIds()==null )
				gb.setDownDeptIds("");
			if(gb.getDownUserIds()==null)
				gb.setDownUserIds("");
			String downDeptIds=gb.getDownDeptIds();
			String downUserIds=gb.getDownUserIds();
			
			/*如果没有选中下载授权的部门和个人，则默认可以看到记录信息的用户都拥有下载附件的权限*/
			if("".equals(downDeptIds) && "".equals(downUserIds))
				onDown=true;
			else{
				String[] downDepts = downDeptIds.split(",");
				String[] downUsers = downUserIds.split(",");
				for(String dept : downDepts){
					if(depts.startsWith(dept) && !"".equals(dept)){
						onDown =true;
						break;
					}
				}
				if(onDown!=true){
					for(String user : downUsers){
						if(userId.equals(user) && !"".equals(user)){
							onDown =true;
							break;
						}
					}
				}
			}
			if("1".equals(userId))  //如果是管理员，则有权限
				onDown=true;
			
			/*判断结束*/
			request.setAttribute("onDown", onDown);
			request.setAttribute("group", groupResult.retVal);
			
			request.setAttribute("oaOrdain", oaOrdainBean);
			String url = request.getRequestURI();
			String favoriteURL = URLEncoder.encode(url + "?operation=5&ordainId="
					+ oaordainId + "&isEspecial=1", "utf-8");
			String myCollectionURL = URLEncoder.encode("&isMyCollection=1", "utf-8");
			request.setAttribute("favoriteURL", favoriteURL);
			request.setAttribute("myCollectionURL", myCollectionURL);

			String uri = java.net.URLEncoder.encode(url+ "?operation=5&ordainId=" + oaordainId);
			request.setAttribute("uri", uri);
			request.setAttribute("targetUsers", targetUsers);
			request.setAttribute("targetDept", targetDept);
		
			request.setAttribute("backtype", innoback);
			request.setAttribute("IsEspecial", Especial);
			request.setAttribute("isMyCollection", myCollection);
			
			//判断是否已经加入收藏
			Result isexit = new AttentionMgt().isAttention(loginBean.getId(), oaordainId, "OAOrdain");		
			if(isexit.retCode == ErrorCanst.MULTI_VALUE_ERROR){
				request.setAttribute("attention", "OK");
			}
			
			request.setAttribute("messageTitle", this.getLoginBean(request)
					.getEmpFullName()
					+ getMessage(request, "request.read.ordain")
					+ oaOrdainBean.getOrdainTitle());	
			
					
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
			.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");			  
		}
		return getForward(request, mapping, "to_detailOrdain");

	}
	/**
	 * 查询组
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
	
		String groupLen=this.getParameter("groupId", request);
		OAOrdainGroupSearchForm oaForm = (OAOrdainGroupSearchForm)form;
		String first = request.getParameter("first");
		if("1".equals(first) && first != null){
			oaForm.setGroupId("");
			oaForm.setPageNo(1);
		}
		//获取当前用户对此模块的权限
		LoginBean loginBean = this.getLoginBean(request);
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/UserFunctionQueryAction.do?tableName=OAOrdainGroup");
		request.setAttribute("addGroup", mop.add()); // 增加权限
		request.setAttribute("deleteGroup", mop.delete()); // 删除权限
		request.setAttribute("updateGroup", mop.update()); // 修改权限
		
		//判断是否单击的返回上级
		if(groupLen !=null && groupLen.length()>=5 && "last".equals(this.getParameter("type", request))){	
			oaForm.setGroupId(oaForm.getGroupId().substring(0, groupLen.length()-5));
			
		}
			
		Result result = mgt.queryFolder(oaForm.getGroupId(),first,"enter");
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("groupList", result.retVal);
			request.setAttribute("groupLen", oaForm.getGroupId());
		}
		return getForward(request, mapping, "queryGroup");
	}
	/**
	 * 添加组前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward addPrepareGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		String classCode=this.getParameter("folderId", request);
     	request.setAttribute("folderId", classCode);
     	
     	if(classCode.length()>=5){  //判断是否添加下级，如果是，获取父级权限
			Result fuResult=mgt.queryFolderByCode(classCode);
			List<OAOrdainGroupBean> fugroup=(List<OAOrdainGroupBean>)fuResult.getRetVal();
			
			String[] getStr=mgt.getShouquan(fugroup.get(0).getPopedomDeptIds(),fugroup.get(0).getPopedomUserIds()).split("%%");
			
			request.setAttribute("fuDept", fugroup.get(0).getPopedomDeptIds()); //父级的授权部门
			request.setAttribute("UserIds", fugroup.get(0).getPopedomUserIds()); //父级的授权个人
		    request.setAttribute("fuUser", getStr[1]);	 //整理后的父级的授权个人
		    request.setAttribute("deptNames", getStr[2]);  //父级授权部门的名称
	    }
     
		request.setAttribute("dealType", "add");
		request.setAttribute("time", new Date());
		
		return getForward(request, mapping, "dealGroup");
	}
	
	/**
	 * 执行删除操作时，判断该目录下是否存在数据（子目录、规章制度信息）
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward checkGroup(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response){
		String thePlace=this.getParameter("insertPlace", request);
		String[] ordainIds = getParameter("keyIds", request).split(",");
		int sumnum=0;         //获取数据总数
		String theGroup="";   //获取含有子目录或规章制度的目录名
		OAOrdainGroupBean obean=new OAOrdainGroupBean();
		for(int i=0;i<ordainIds.length;i++){
			int num=0;
			Result orbean=null;
			
			if("listpage".equals(thePlace)){ //判断是否从规章制度页面进入的删除目录操作
				orbean=mgt.queryFolderByCode(ordainIds[i]);
				obean=(OAOrdainGroupBean)((List<OAOrdainGroupBean>)orbean.retVal).get(0);
			}else{
				orbean=mgt.loadGroup(ordainIds[i]); //获取组
			    obean=(OAOrdainGroupBean) orbean.retVal;
			}
			Result allGroup=mgt.queryFolder(obean.getClassCode(), null, "delete"); //获取与该目录相关的子级目录
			if(allGroup.realTotal>1){
				num +=allGroup.realTotal-1;
			}
			
			Result allOrdain=mgt.queryOrdainBygroup(obean.getClassCode()); //获取与该目录相关的规章制度信息
			if(allOrdain.realTotal >0){
				num+=allOrdain.realTotal;
			}
			
			if(num !=0){    //判断数据是否为0，如果不是，获取该目录的目录名 
				theGroup +=obean.getGroupName()+",";
			}
			sumnum+=num;
		}	
		
		request.setAttribute("msg", sumnum+";"+theGroup+";"+obean.getId());

		return  getForward(request, mapping, "blank");
		
	}
	/**
	 * 执行添加或编辑操作时，根据选择的组，找出可以授权的部门和个人
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward getShouquan(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response){
		String classCode=this.getParameter("groupCode", request);
		if(!"".equals(classCode)){
			Result fuResult=mgt.queryFolderByCode(classCode);
			List<OAOrdainGroupBean> fugroup=(List<OAOrdainGroupBean>)fuResult.getRetVal();
			String[] getStr=mgt.getShouquan(fugroup.get(0).getPopedomDeptIds(),fugroup.get(0).getPopedomUserIds()).split("%%");
			String deIds="";
			String userIds="";
			if("".equals(fugroup.get(0).getPopedomDeptIds()))	
				deIds="noDept";
			else
				deIds=fugroup.get(0).getPopedomDeptIds();
			if("".equals(fugroup.get(0).getPopedomUserIds()))
				userIds="noUser";
			else
				userIds=fugroup.get(0).getPopedomUserIds();
		    request.setAttribute("msg", getStr[1]+";"+getStr[2]+";"+deIds+";"+userIds);  
		}
		return  getForward(request, mapping, "blank");
		
	}
	/**
	 * 添加组
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward addGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
	
		LoginBean loginBean = this.getLoginBean(request);
		OAOrdainGroupForm oaForm =(OAOrdainGroupForm) form;
		
		OAOrdainGroupBean bean = new OAOrdainGroupBean();
		read(oaForm, bean);
		
		if(bean.getClassCode() == null || "".equals(bean.getClassCode())){
			String strClassCode = new PublicMgt().getLevelCode("OAOrdainGroup", "");
			bean.setClassCode(strClassCode);
		}else{
			String strClassCode = new PublicMgt().getLevelCode("OAOrdainGroup", bean.getClassCode());
			bean.setClassCode(strClassCode);
		}
		
		bean.setId(IDGenerater.getId());
		bean.setCreateBy(loginBean.getId());
		bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		
		String theChildGroup=bean.getClassCode().substring(0,bean.getClassCode().length()-5);
			
		Result result = mgt.addGroup(bean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){	
			String classCode=bean.getClassCode();			
			if(classCode.length()>5){	
				Result checkRe=mgt.queryFolderByCode(classCode.substring(0,classCode.length()-5));  //判断上级目录的iscatalog是否为1
				List<OAOrdainGroupBean> oalist =(List<OAOrdainGroupBean>)checkRe.retVal;
				OAOrdainGroupBean oagroupBean=oalist.get(0);
				if(oagroupBean.getIsCatalog()!=1){  //如果不为1，更新父级目录的isCatalog
					Result re = mgt.updateIsCatalog(classCode.substring(0,classCode.length()-5),"add");		
					if(re.retCode == ErrorCanst.DEFAULT_SUCCESS){
						
					}
				}
			}
			request.getSession().setAttribute("ordainSearchForm", null);
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setAlertRequest(request);
			return getForward(request, mapping, "alert");	
		}else {
			EchoMessage.success().add(getMessage(
                    request, "common.msg.addFailture")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 修改组前
	 * @param mapping
	 * @param from
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward updatePrepareGroup(ActionMapping mapping,
			ActionForm from, HttpServletRequest request, HttpServletResponse response){
		String thePlace=this.getParameter("insertPlace", request);
		String id = request.getParameter("folderId");
		OAOrdainGroupBean oagroupBean=null;
		Result result=null;
		if("listpage".equals(thePlace)){ //判断是否从规章制度页面进入的删除目录操作
			result=mgt.queryFolderByCode(id);
			oagroupBean=(OAOrdainGroupBean)((List<OAOrdainGroupBean>)result.retVal).get(0);
		}else{
			result = mgt.loadGroup(id);
			oagroupBean = (OAOrdainGroupBean)result.retVal;
		}
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//获取此目录父级目录的权限
			if(oagroupBean.getClassCode().length()>5){
				Result fuResult=mgt.queryFolderByCode(oagroupBean.getClassCode().substring(0,oagroupBean.getClassCode().length()-5));
				List<OAOrdainGroupBean> fugroup=(List<OAOrdainGroupBean>)fuResult.getRetVal();
				
				String str=mgt.getShouquan(fugroup.get(0).getPopedomDeptIds(),fugroup.get(0).getPopedomUserIds());
				String[] getStr=str.split("%%");				
			    
				request.setAttribute("fuDept", fugroup.get(0).getPopedomDeptIds()); //父级的授权部门
				request.setAttribute("UserIds", fugroup.get(0).getPopedomUserIds()); //父级的授权个人
			    request.setAttribute("fuUser", getStr[1]);	 //整理后的父级的授权个人
			    request.setAttribute("deptNames", getStr[2]);  //父级授权部门的名称
			}
			
			//下载选择的用户
			List<EmployeeBean> downUsers =newsmgt.getEmployee(oagroupBean.getDownUserIds());
			request.setAttribute("downUsers", downUsers);

			//下载选择的部门
			List<Object> downDept = newsmgt.getDepartment(oagroupBean.getDownDeptIds());
		    request.setAttribute("downDept", downDept);
			
			/*获取通知对象*/
			
			List<EmployeeBean> targetUsers =newsmgt.getEmployee(oagroupBean.getPopedomUserIds());
			request.setAttribute("targetUsers", targetUsers);
			
			List<Object> targetDept = newsmgt.getDepartment(oagroupBean.getPopedomDeptIds());
		    request.setAttribute("targetDept", targetDept);
					
			request.setAttribute("group", oagroupBean);
		}
		request.setAttribute("dealType", "update");
		return getForward(request, mapping, "dealGroup");
	}
	
	/**
	 * 修改组
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward updateGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		LoginBean loginBean = this.getLoginBean(request);
		OAOrdainGroupForm oaForm =(OAOrdainGroupForm) form;
		Result re=mgt.loadGroup(oaForm.getGroupid());
		
		//判断此记录是否还存在
		if(re.retCode != ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add(
					getMessage(request, "this.record.not exist"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		OAOrdainGroupBean bean = (OAOrdainGroupBean)re.getRetVal();
		
		read(oaForm, bean);
		
		bean.setLastUpdateBy(loginBean.getId());
		bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		
		Result result=null;
		
		result = mgt.updateGroup(bean);
		if(result.retCode== ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("dealAsyn", "true");
			request.getSession().setAttribute("ordainSearchForm", null);
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
		}
		else{
			  EchoMessage.success().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 删除组
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward del(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		String[] ordainIds = getParameter("keyIds", request).split(",");
		
		//判断是否是从规章制度列表页面进入的删除操作
		String thePlace=this.getParameter("insertPlace", request);
		
		//获取第一个选中的组
		OAOrdainGroupBean obean= (OAOrdainGroupBean) mgt.loadGroup(ordainIds[0]).getRetVal();    
		
		//根据ClassCode的长度判断是否存在上级，
		if(obean.getClassCode().length()-5 !=0){
			
			//查询和当前记录存在相似的classCode的记录
			Result res3=mgt.queryFolder(obean.getClassCode().substring(0,obean.getClassCode().length()-5),null,"delete");  
			
			 //判断选中记录是否是同一父级目录下的所有子级，如果是，此时更新父级目录的Catalog
			if(res3.realTotal ==ordainIds.length +1){ 
				Result res4=mgt.updateIsCatalog(obean.getClassCode().substring(0,obean.getClassCode().length()-5),"delete");
				if(res4.retCode != ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "common.msg.delError")).setBackUrl("/OAOrdainGroup.do?operation=4&dealType=ordainGroup").
	                        setAlertRequest(request);
					 return getForward(request, mapping, "alert"); 	
				}
			}		
		}
	    
				
		Result result = mgt.delGroup(ordainIds); //删除当前选中的记录	
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if("listpage".equals(thePlace)){
				request.getSession().setAttribute("ordainSearchForm", null);
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl("/OAnewOrdain.do?insertPlace=del&operation=4").
	            setAlertRequest(request);
			}else{		
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl("/OAOrdainGroup.do?operation=4&dealType=ordainGroup").
	            setAlertRequest(request);
			}
			return getForward(request, mapping, "alert"); 	
		}else{	 
			if("listpage".equals(thePlace))
				EchoMessage.success().add(getMessage(request, "common.msg.delError")).setBackUrl("/OAnewOrdain.do?operation=4").
	            setAlertRequest(request);
			else
				EchoMessage.success().add(getMessage( request, "common.msg.delError")).setBackUrl("/OAOrdainGroup.do?operation=4&dealType=ordainGroup").
			        setAlertRequest(request);
			return getForward(request, mapping, "alert"); 	
		}
	}
}
