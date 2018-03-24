package com.koron.oa.publicMsg.knowledgeCenter;

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
import com.koron.oa.bean.Department;
import com.koron.oa.bean.Employee;
import com.koron.oa.bean.OAKnowFileBean;
import com.koron.oa.bean.OAKnowFolderBean;
import com.koron.oa.bean.OAOrdainGroupBean;
import com.koron.oa.publicMsg.advice.OAAdviceMgt;
import com.koron.oa.util.AttentionMgt;
import com.koron.oa.util.EmployeeMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * 
 * <p>Title:知识中心Action</p> 
 * <p>Description: </p>
 *
 * @Date:2012-6-13
 * @Copyright: 科荣软件
 * @Author 方家俊
 */
public class OAKnowCenterAction extends MgtBaseAction{
	
	
	OAKnowCenterMgt mgt = new OAKnowCenterMgt();
	EmployeeMgt empMgt = new EmployeeMgt() ;
    OAAdviceMgt adviceMgt = new OAAdviceMgt() ;
    AdviceMgt amgt=new AdviceMgt();
    
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		//跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		String type = request.getParameter("type");
		ActionForward forward = null;
		String flag = request.getParameter("flag");//用于识别目录管理设置的上下级 1.表示本级,不需要筛选 2表示下级,需要筛选
		if(flag ==null || "".equals(flag)){
			
			flag ="1";
		}
		/*是否添加body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		
		request.setAttribute("flag", flag);
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepareFile(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			String thetype=this.getParameter("thetype", request);
			if("getShouquan".equals(thetype)){
				forward=getShouquan(mapping,request,response);
			}else{
				forward = addFile(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepareFile(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			forward = updateFile(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = deleteFile(mapping, form, request, response);
			break;
		case OperationConst.OP_DETAIL:
			forward = detailFile(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			String dealType = request.getParameter("dealType");
		
			if("addGroup".equals(dealType)){
				forward = addPrepareGroup(mapping, form, request, response);
			}else if("add".equals(dealType)){
				forward = addGroup(mapping, form, request, response);
			}else if("updateGroup".equals(dealType)){
				forward = updatePrepareGroup(mapping, form, request,response);
			}else if("update".equals(dealType)){
				forward = updateGroup(mapping, form, request, response);
			}else if("del".equals(dealType)){
				
				String check=this.getParameter("check", request);
				
				if("true".equals(check) && check != null){
					//删除组前，看是否存在下级
					forward=checkGroup(mapping,request,response);
				}else{
					forward = del(mapping, form, request, response);
				}
			}else if("deleteFile".equals(dealType)){
				forward = deleteFiles(mapping, form, request, response);
			}else{
				//查询组
				forward = queryGroup(mapping, form, request, response); 
			}
			break;
		default:
			if(type !=null && !type.equals("")){
				if("toTree".equals(type)){
					//左边的目录
					forward = toTree(mapping, form, request,response);
				}else if("oaKnowList".equals(type)){
					//知识中心列表
					forward = KnowCenterList(mapping, form, request, response);
				}
			}else{
				//加载主页面
				forward = toFrame(mapping, form, request, response);
			}
			break;
		}
		return forward;
	}
	
	/**
	 * 主页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward toFrame(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		return getForward(request, mapping, "toFarme");
	}
	
	/**
	 * 左边的目录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward toTree(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		LoginBean loginBean = this.getLoginBean(request);
		long start = System.currentTimeMillis();
		Result result = mgt.queryFolderUser(loginBean);
		long start2 = System.currentTimeMillis();
		System.out.println("time:"+String.valueOf(start2-start));
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)result.retVal;
			String folderTree="";
			String folderCode="";
			for(int i=0;i<list.size();i++){
				OAKnowFolderBean bean = (OAKnowFolderBean)list.get(i);
				folderCode += "@acute;"+bean.getClassCode()+"@acute;";
				if(i != list.size()-1){
					folderCode += ",";
				}
				if(bean.getClassCode().length() == 5){
					folderTree += this.folderTree(list, bean.getFolderName(), bean.getClassCode(), bean.getIsCatalog(), bean.getId());
				}
			}
			request.setAttribute("fCode", folderCode);
			request.setAttribute("result", folderTree);
		}
		long start3 = System.currentTimeMillis();
		System.out.println("time2:"+String.valueOf(start3-start2));
		return getForward(request, mapping, "toTree");
	}
	
	
	private String folderTree(ArrayList list,String folderName,String classCode,Integer isCatalog,String folderId){
    	String folderTree="";
    	if(isCatalog!=null && isCatalog == 1){
    		folderTree="<li><span><a style=\"color:#000000\" href=\"javascript:goFarme('/OAKnowCenter.do?type=oaKnowList&queryType=number&folderCode="+classCode+"')\" onclick=\"insertType('"+classCode+"');\" ><font style=\"color: black\" id=\"type_"+classCode+"\">"+folderName+"</font></a><font  id=\"_1\"></font></span><ul>";
    		for(int i=0;i<list.size();i++){
    			OAKnowFolderBean bean = (OAKnowFolderBean)list.get(i);    
    			if(bean.getClassCode().substring(0,bean.getClassCode().length()-5).equals(classCode)){
    				folderTree+=this.folderTree(list,bean.getFolderName(), bean.getClassCode(), bean.getIsCatalog(), bean.getId());
    			}
    		}
    		folderTree+="</ul></li>";
    	}else{
    		folderTree="<li><span><a style=\"color:#000000\" href=\"javascript:goFarme('/OAKnowCenter.do?type=oaKnowList&queryType=number&folderCode="+classCode+"')\" onclick=\"insertType('"+classCode+"');\" ><font style=\"color: black\" id=\"type_"+classCode+"\">"+folderName+"</font></a><font  id=\"_1\"></font></span></li>";
    	}
    	
    	return folderTree;
    }
	
	/**
	 * 知识中心列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward KnowCenterList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		OAKnowSearchForm oaForm = (OAKnowSearchForm)form;
		LoginBean loginBean = this.getLoginBean(request);
		if(oaForm.getFolderCodeOld() == null){
			oaForm.setFolderCodeOld(oaForm.getFolderCode());
		}
		String news = oaForm.getFolderCode();
		String old = oaForm.getFolderCodeOld();
		if(news != null && !news.equals(old)){
			oaForm.setPageNo(1);
			oaForm.setFolderCodeOld(oaForm.getFolderCode());
		}
		String keyWord = oaForm.getKeyWord();
		keyWord = keyWord == null? "" : GlobalsTool.toChinseChar(keyWord);
		if(oaForm.getQueryType() != null && !"keyword".equals(oaForm.getQueryType())){
			oaForm.setBeginTimeSearch("");
			oaForm.setCreateBySearch("");
			oaForm.setDescriptSearch("");
			oaForm.setEndTimeSearch("");
			oaForm.setFileNameSearch("");
			oaForm.setFileTitleSearch("");
			oaForm.setGroupIdSearch("");
			oaForm.setGroupNameSearch("");
			oaForm.setProUserName("");
		}
		Result result = mgt.queryKnowFile(oaForm.getFolderCode(), loginBean.getId(), loginBean.getDepartCode(),
				keyWord, oaForm.getQueryType(), oaForm.getTerm(),oaForm.getFileTitleSearch(), 
				oaForm.getCreateBySearch(), oaForm.getProUserName(), oaForm.getBeginTimeSearch(), oaForm.getEndTimeSearch(),
				oaForm.getGroupIdSearch(), oaForm.getGroupNameSearch(), oaForm.getPageNo(), oaForm.getPageSize(), null, null);
		if(null!=this.getParameter("selectKnow", request)){	
			request.setAttribute("selectKnow", this.getParameter("selectKnow", request));
		}
		if(null != request.getParameter("selectKnow")){
			request.setAttribute("selectKnow", request.getParameter("selectKnow"));
		}
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("knowMap", result.retVal);
			request.setAttribute("pageBar", this.pageBar(result, request));
		}
		request.setAttribute("insertPlace", request.getParameter("insertPlace"));
		System.out.println("0000:"+request.getParameter("insertPlace"));
		request.setAttribute("status", request.getParameter("status"));
		return getForward(request, mapping, "knowCenterList");
	}
	/**
	 * 添加前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward addPrepareFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		String folderCode = request.getParameter("folderCode");
		Result rst = mgt.quertFolderCode(folderCode);
		if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("folder", rst.retVal);
		}
		request.setAttribute("folderCode", folderCode);
		return getForward(request, mapping, "addPrepareFile");
	}
	
	/**
	 * 添加
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward addFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		OAKnowForm oaForm = (OAKnowForm)form;
		
		LoginBean loginBean = this.getLoginBean(request);
		OAKnowFileBean bean = new OAKnowFileBean();
		//提醒方式
		String[] wakeType = request.getParameterValues("wakeUpMode");
		String wakeupType = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeupType += str + ",";
			}
		}
		//附件
		String accessories = request.getParameter("attachFiles");
		accessories = accessories== null?"":accessories;

		//需删除的附件
		String delFiles = request.getParameter("delFiles");
		//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && accessories.indexOf(del) == -1){
				File aFile = new File(BaseEnv.FILESERVERPATH + "/OAKnowCenterFile/" + del);
				aFile.delete();
			}
		}
		
		oaForm.setFilePath(accessories);
		read(oaForm, bean);
		String id = IDGenerater.getId();
		bean.setId(id);
		bean.setCreateBy(loginBean.getId());
		bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		bean.setLastUpdateBy(loginBean.getId());
		bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		
		bean.setWakeUpType(wakeupType);
		Result result = mgt.addFile(bean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//提醒
			String popedomUserIds = "";   //通知对象
			String popedomUserId = oaForm.getPopedomUserIds();
			if(oaForm.getIsAlonePopedom() == 0){   //不单独授权
				List listEmp = (List) adviceMgt.sel_allEmployee().getRetVal();
				for (int i = 0; i < listEmp.size(); i++) { // 循环发送
					popedomUserIds += String.valueOf(listEmp.get(i)) + ",";
				}
			}else{   //单独授权
				//个人
				if (oaForm.getPopedomUserIds() != null && oaForm.getPopedomUserIds().length() > 0) {
					popedomUserIds += oaForm.getPopedomUserIds();
				}
				//部门
				if (null != oaForm.getPopedomDeptIds() && !"".equals(oaForm.getPopedomDeptIds())) {
					String[] deptIds = oaForm.getPopedomDeptIds().split(","); 
					for (String departId : deptIds) {
						ArrayList<OnlineUser> users=OnlineUserInfo.getDeptUser(departId) ;
						for (OnlineUser user : users) {
							if (!popedomUserIds.contains(","+user.getId()+",")) {
								popedomUserIds += user.getId()+ ",";
							}
						}
					}
				}
				//职员分组
				if (null != oaForm.getPopedomEmpGroupIds() && !"".equals(oaForm.getPopedomEmpGroupIds())) {
					String[] empGroupIds = oaForm.getPopedomEmpGroupIds().split(","); // 根据分组查找分组人员
					for (String empGroup : empGroupIds) {
						List list = adviceMgt.queryAllEmployeeByGroup(empGroup);
						for (int i = 0; i < list.size(); i++) {
							if (!popedomUserIds.contains(list.get(i).toString())) {// 判断是否已经单独授权
								popedomUserIds += list.get(i).toString() + ",";
							}
						}
					}
				}
			}
//			判断通知对象是否包含当前用户，不需要向自己发送通知消息
			String personnels="";
			if(popedomUserIds!=null && popedomUserIds.length()>0){
				String [] popedomUser=popedomUserIds.split(",");
				for(String pope:popedomUser){
					if(!pope.equals(loginBean.getId())){
						personnels +=pope+",";
					}
				}
			}
			String title =GlobalsTool.getMessage(request,"oa.mail.knowlegeCenter"); //标题
        	title=title+"("+oaForm.getFileTitle()+")"+getMessage(request, "com.click.see");
        	String url = request.getRequestURI() ;
        	String favoriteURL = "/OAKnowCenter.do?noback=true&operation=5&folderCode="+oaForm.getFolderId()+"&fileId="+id+"&isEspecial=1";
			String content = "<a href=javascript:mdiwin('"+favoriteURL+"','"+GlobalsTool.getMessage(request, "oa.common.knowledgeCenter")+"')>"+title+"</a>";
			String[] wakeUpMode=request.getParameterValues("wakeUpMode");
        	//向用户添加提醒方式
        	if(wakeUpMode!=null&&!"".equals(wakeUpMode)){
				for (String type : wakeUpMode) {
					new Thread(new NotifyFashion(loginBean.getId(), title,
							content, personnels, Integer.parseInt(type), "yes",
							id, null, null, "knowledge")).start();
				}
        	}
			
			EchoMessage.success().add(getMessage(
                    request, "common.msg.addSuccess")).
                    setBackUrl("/OAKnowCenter.do?type=oaKnowList&folderCode="+oaForm.getFolderId())
                    .setAlertRequest(request);
		}else{
			EchoMessage.error().add(getMessage(
                    request, "common.msg.addFailture")).
                    setBackUrl("/OAKnowCenter.do?type=oaKnowList").
                    setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	
	/**
	 * 修改前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward updatePrepareFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
	
		String fileId = request.getParameter("fileId");
		Result result = mgt.loadFile(fileId);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			OAKnowFileBean oaknowBean = (OAKnowFileBean) result.retVal;

			String[] wakeUpType = null;// 提醒方式
			if (oaknowBean.getWakeUpType() != null && !"".equals(oaknowBean.getWakeUpType())) {
				wakeUpType = oaknowBean.getWakeUpType().split(",");
			}

			// 根据用户ID循环查询用户信息
			List<Employee> targetUsers = new ArrayList<Employee>();
			if (oaknowBean.getPopedomUserIds() != null && !"".equals(oaknowBean.getPopedomUserIds())) {
				for (String targetUser : oaknowBean.getPopedomUserIds().split(",")) {
					Employee employee = empMgt.getEmployeeById(targetUser);
					if (null != employee) {
						targetUsers.add(employee);
					}
				}
			}
			// 查询通知部门
			List<Department> targetDept = new ArrayList<Department>();
			if (oaknowBean.getPopedomDeptIds() != null
					&& !"".equals(oaknowBean.getPopedomDeptIds())) {
				String[] popedomDeptIds = oaknowBean.getPopedomDeptIds().split(",");
				for (String classCode : popedomDeptIds) {
					Department dept = adviceMgt.getDepartmentByClassCode(classCode);
					if (null != dept) {
						targetDept.add(dept);
					}
				}
			}

			// 查找职员分组
			List<String[]> listEmpGroup = new ArrayList<String[]>();
			if (oaknowBean.getPopedomEmpGroupIds() != null
					&& !"".equals(oaknowBean.getPopedomEmpGroupIds())) {
				String[] popedomEmpGroupIds = oaknowBean.getPopedomEmpGroupIds()
						.split(",");
				for (String empGroup : popedomEmpGroupIds) {
					Result r = empMgt.selectEmpGroupById(empGroup);
					if (r.getRetVal() != null) {
						listEmpGroup.add((String[]) r.getRetVal());
					}
				}
			}
			Result rst = mgt.quertFolderCode(oaknowBean.getFolderId());
			if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("folder", rst.retVal);
			}
			String selectKnow = "";
			if(null != request.getParameter("preId")){
				selectKnow = request.getParameter("preId");
				request.setAttribute("preId", selectKnow);
			}
			request.setAttribute("file", oaknowBean);
			request.setAttribute("targetUsers", targetUsers);
			request.setAttribute("targetDept", targetDept);
			request.setAttribute("targetEmpGroup", listEmpGroup);
			request.setAttribute("wakeUpType", wakeUpType); // 提示方式
			//判断是否是从详细页面进行的修改操作
			request.setAttribute("position", this.getParameter("position", request));
		}
		return getForward(request, mapping, "updateFile");
	}
	
	/**
	 * 修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward updateFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		LoginBean loginBean = this.getLoginBean(request);
		OAKnowForm oaForm = (OAKnowForm)form;
		String accessories = request.getParameter("attachFiles");
		accessories = accessories== null?"":accessories;

		//需删除的附件
		String delFiles = request.getParameter("delFiles");
		//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && accessories.indexOf(del) == -1){
				File aFile = new File(BaseEnv.FILESERVERPATH + "/affix/OAKnowCenterFile/" + del);
				aFile.delete();
			}
		}
		//提醒方式
		String[] wakeType = request.getParameterValues("wakeUpMode");
		String wakeupType = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeupType += str + ",";
			}
		}
		oaForm.setFilePath(accessories);
		
		//判断是否是从详细页面进行的修改操作
		String position=this.getParameter("position", request);
		OAKnowFileBean fileBean = new OAKnowFileBean();
		read(oaForm, fileBean);
		fileBean.setWakeUpType(wakeupType);
		fileBean.setLastUpdateBy(loginBean.getId());
		fileBean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		Result result = mgt.updateFile(fileBean);
		String selectKnow = "";
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//获取通知
			String popedomUserIds = "";
			if (oaForm.getIsAlonePopedom()==0) {// 不单独授权
				List listEmp = (List) adviceMgt.sel_allEmployee().getRetVal();
				for (int i = 0; i < listEmp.size(); i++) { // 循环发送
					popedomUserIds += String.valueOf(listEmp.get(i)) + ",";
				}
				
			} else {//单独授权
				popedomUserIds=",";
				//用户
				if(oaForm.getPopedomUserIds() != null && oaForm.getPopedomUserIds().length()>0){
					popedomUserIds+=oaForm.getPopedomUserIds()+",";
				}
				
				if(!popedomUserIds.contains(",1,")){
					popedomUserIds+="1,";
				}
				//部门
				if (null != oaForm.getPopedomDeptIds() && !"".equals(oaForm.getPopedomDeptIds())) {
					String[] deptIds = oaForm.getPopedomDeptIds().split(","); 
					for (String departId : deptIds) {
						ArrayList<OnlineUser> users=OnlineUserInfo.getDeptUser(departId) ;
						for (OnlineUser user : users) {
							if (!popedomUserIds.contains(","+user.getId()+",")) {
								popedomUserIds += user.getId()+ ",";
							}
						}
					}
				}
				//职员分组
				if(null!=oaForm.getPopedomEmpGroupIds() && !"".equals(oaForm.getPopedomEmpGroupIds())){
					String[] empGroupIds = oaForm.getPopedomEmpGroupIds().split(",")  ; //根据分组查找分组人员
					for(String empGroup : empGroupIds){
						List  list =new OAAdviceMgt().queryAllEmployeeByGroup(empGroup) ;
						for(int i=0;i<list.size();i++){
							if(!popedomUserIds.contains(","+list.get(i).toString()+",")){//判断是否已经单独授权
								popedomUserIds += list.get(i).toString()+"," ;
							}
						}
					}
				}
			}
			
			//判断通知对象是否包含当前用户，不需要向自己发送通知消息
			String personnels="";
			if(popedomUserIds!=null && popedomUserIds.length()>0){
				String [] popedomUser=popedomUserIds.split(",");
				for(String pope:popedomUser){
					if(!pope.equals(loginBean.getId())){
						personnels +=pope+",";
					}
				}
			}
			String id=","+fileBean.getId()+",";
			amgt.deleteByRelationId(id, "");
        	String title =GlobalsTool.getMessage(request,"oa.mail.knowlegeCenter.add"); //标题
        	title=OnlineUserInfo.getUser(loginBean.getId()).getName()+title+":"+oaForm.getFileTitle()+""+getMessage(request, "com.click.see");
        	String favoriteURL = "/OAKnowCenter.do?noback=true&operation=5&folderCode="+oaForm.getFolderId()+"&fileId="+oaForm.getId()+"&isEspecial=1";
			String content = "<a href=javascript:mdiwin('"+favoriteURL+"','"+GlobalsTool.getMessage(request, "oa.common.knowledgeCenter")+"')>"+title+"</a>";
			String[] wakeUpMode=request.getParameterValues("wakeUpMode");
        	//向用户添加提醒方式
        	if(wakeUpMode!=null&&!"".equals(wakeUpMode)){
        		for(String type : wakeUpMode){
        			new Thread(new NotifyFashion(loginBean.getId(), title, content, personnels, Integer.parseInt(type),"yes",oaForm.getId())).start() ;
        		}
        	}
        	
        	if(null != request.getParameter("preId")){
        		selectKnow = request.getParameter("preId");
        	}
        	
        	// 修改成功
			if("detailpage".equals(position))  //判断是否是从详细页面进行的修改操作
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess")).setBackUrl(
						"/OAKnowCenter.do?operation=5&pageNo=1&fileId="+fileBean.getId()).setAlertRequest(request);
			else
				EchoMessage.success().add(getMessage(
                     request, "common.msg.updateSuccess"))
                     .setBackUrl("/OAKnowCenter.do?type=oaKnowList&pageNo=1").
                     setAlertRequest(request);

	    } else {
	         EchoMessage.success().add(getMessage(
	                     request, "common.msg.updateErro"))
	                     .setBackUrl("/OAKnowCenter.do?type=oaKnowList&selectKnow="+selectKnow).
	                     setAlertRequest(request);
	    }
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward deleteFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		String[] keyIds = request.getParameterValues("keyId");
		String selectKnow = "";
		if(null != request.getParameter("preId")){
			selectKnow = request.getParameter("preId");
		}
		Result rs = mgt.deleteFile(keyIds);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			
			//删除和删除记录相关的通知消息
			String delIds=","+getParameter("keyId", request)+",";
			amgt.deleteByRelationId(delIds, "");
			
			EchoMessage.success().add(getMessage(
                  request, "common.msg.delSuccess"))
                  .setBackUrl("/OAKnowCenter.do?type=oaKnowList&selectKnow="+selectKnow).
                  setAlertRequest(request);
		}else {
			EchoMessage.success().add(getMessage(
                  request, "common.msg.delError"))
                  .setBackUrl("/OAKnowCenter.do?type=oaKnowList&selectKnow="+selectKnow).
                  setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward detailFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		try{
			LoginBean loginBean = this.getLoginBean(request);
			String fileId = request.getParameter("fileId");
			//判断是否已经加入收藏
			Result isexit = new AttentionMgt().isAttention(loginBean.getId(), fileId, "OAKnowCenter");		
			if(isexit.retCode == ErrorCanst.MULTI_VALUE_ERROR){
				request.setAttribute("attention", "OK");
			}
			
			//根据ID查询详情
			Result result = mgt.detailFile(fileId);
			OAKnowSearchForm oaForm = (OAKnowSearchForm)form;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				OAKnowFileBean filebean = (OAKnowFileBean)result.retVal;
				String keyWord = oaForm.getKeyWord();
				keyWord = keyWord == null? "" : GlobalsTool.toChinseChar(keyWord);
				if(oaForm.getQueryType() != null && !"keyword".equals(oaForm.getQueryType())){
					oaForm.setBeginTimeSearch("");
					oaForm.setCreateBySearch("");
					oaForm.setDescriptSearch("");
					oaForm.setEndTimeSearch("");
					oaForm.setFileNameSearch("");
					oaForm.setFileTitleSearch("");
					oaForm.setGroupIdSearch("");
					oaForm.setGroupNameSearch("");
					oaForm.setProUserName("");
				}
				String isEspecial = request.getParameter("isEspecial");
				if(isEspecial == null){
					Result preResult = mgt.queryKnowFile(oaForm.getFolderCode(), loginBean.getId(), 
							loginBean.getDepartCode(), keyWord, oaForm.getQueryType(), 
							oaForm.getTerm(),oaForm.getFileTitleSearch(), oaForm.getCreateBySearch(), oaForm.getProUserName(), 
							oaForm.getBeginTimeSearch(), oaForm.getEndTimeSearch(), oaForm.getGroupIdSearch(),oaForm.getGroupNameSearch(), 
							oaForm.getPageNo(), oaForm.getPageSize(), "detailPre",filebean.getLastUpdateTime());
					//上一条
					if (preResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						List<OAKnowFileBean> preNews = (List<OAKnowFileBean>) preResult.getRetVal();
						if (preNews != null && preNews.size() > 0) {
							request.setAttribute("lastKnow", preNews.get(preNews.size()-1));
						    
						}
					}
					//下一条
					Result nextResult = mgt.queryKnowFile(oaForm.getFolderCode(), loginBean.getId(), 
							loginBean.getDepartCode(), keyWord, oaForm.getQueryType(), 
							oaForm.getTerm(),oaForm.getFileTitleSearch(), oaForm.getCreateBySearch(), oaForm.getProUserName(),
							oaForm.getBeginTimeSearch(), oaForm.getEndTimeSearch(), oaForm.getGroupIdSearch(), oaForm.getGroupNameSearch(),
							oaForm.getPageNo(), oaForm.getPageSize(), "detailNext",filebean.getLastUpdateTime());
					if (nextResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						List<OAKnowFileBean> nextNews = (List<OAKnowFileBean>) nextResult.getRetVal();
						if (nextNews != null && nextNews.size() > 0) {
							request.setAttribute("nextKnow", nextNews.get(0));
						}
					}
				}
				
		        //查询通知个人
				String popemdomUserIds = filebean.getPopedomUserIds();
				List<Employee> popemdomUsers = new ArrayList<Employee>();
				if(popemdomUserIds!=null&&!"".equals(popemdomUserIds)){
					String[] userids = popemdomUserIds.split(",");
					for (String userId : userids) {
						Employee e = empMgt.getEmployeeById(userId);
						if (null != e) {
							popemdomUsers.add(e);
						}
					}
				}
				//查询通知部门
				String popedomDeptIds = filebean.getPopedomDeptIds() ;
				List<Department> targetDept = new ArrayList<Department>() ;
				if(popedomDeptIds!=null && !"".equals(popedomDeptIds)){
					String[] popedomDept = popedomDeptIds.split(",") ;
					for(String classCode : popedomDept){
						Department dept = adviceMgt.getDepartmentByClassCode(classCode) ;
						if(null!=dept){
							targetDept.add(dept) ;
						}
					}
				}
				//查找职员分组
				String empUsers = "";
				List<String[]> listEmpGroup=new ArrayList<String[]>();
				String popedomEmpGroups = filebean.getPopedomEmpGroupIds() ;
				if(popedomEmpGroups!=null && !"".equals(popedomEmpGroups)){
					String[] popedomEmpGroupIds = popedomEmpGroups.split(",") ;
					for(String empGroup : popedomEmpGroupIds){
						empUsers += empMgt.selectEmpByGroupId(empGroup);
						Result r = empMgt.selectEmpGroupById(empGroup);
						if(r.getRetVal()!=null){
							listEmpGroup.add((String[])r.getRetVal());
						}
					}
				}
				//判断是否是从收藏夹进入 ，如果是就隐藏一些图标
				request.setAttribute("isEspecial", isEspecial);
				//获取是我的收藏点击进入的
				String myCollection=this.getParameter("isMyCollection", request);
				String url = request.getRequestURI();
				String favoriteURL = java.net.URLEncoder.encode(url + "?operation=5&fileId="+filebean.getId()+"&isEspecial=1&noback=true&folderCode="+filebean.getFolderId());
				String myCollectionURL = URLEncoder.encode("&isMyCollection=1", "utf-8");//用于详情页面连接到url之后,以便于添加到我的收藏路径中
				request.setAttribute("isMyCollection", myCollection);
				request.setAttribute("myCollectionURL", myCollectionURL);
				request.setAttribute("favoriteURL", favoriteURL);
				request.setAttribute("file", result.retVal);
				request.setAttribute("targetUsers", popemdomUsers);
				request.setAttribute("targetDept", targetDept) ;
				request.setAttribute("targetEmpGroup", listEmpGroup) ;
				OAKnowFileBean bean = (OAKnowFileBean)result.retVal;
				
				boolean onDown = false;
				result = mgt.quertFolderCode(bean.getFolderId());
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
					List folderList = (ArrayList)result.retVal;
					if(folderList != null && folderList.size()>0){
						OAKnowFolderBean folderBean = (OAKnowFolderBean)folderList.get(0);

						if(folderBean.getDownDeptIds()==null )
							folderBean.setDownDeptIds("");
						if(folderBean.getDownUserIds()==null)
							folderBean.setDownUserIds("");
						String downDeptIds=folderBean.getDownDeptIds();
						String downUserIds=folderBean.getDownUserIds();
						
						//获得登陆用户所在部门	
						String depts=loginBean.getDepartCode();
						String userId=loginBean.getId();
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
						request.setAttribute("folder", result.retVal);
					}
				}
				
				if("1".equals(loginBean.getId())){
					onDown = true;
				}
				
				
				request.setAttribute("onDown", onDown);
				String selectKnow = "";
				if(null != request.getParameter("preId")){
					selectKnow = request.getParameter("preId");
					request.setAttribute("preId", selectKnow);
				}
				if(null != request.getParameter("pageNo")){
					request.setAttribute("pageNo", request.getParameter("pageNo"));
				}
				
			}else{
				EchoMessage.error().add(getMessage(request, "news.not.find"))
		 		   .setNotAutoBack()
		 		   .setAlertRequest(request);
		    		return getForward(request, mapping, "message");
			}
		}catch(Exception e){
    		e.printStackTrace();
    		EchoMessage.error().add(getMessage(request, "news.not.find"))
 		   .setNotAutoBack()
 		   .setAlertRequest(request);
    		return getForward(request, mapping, "message");
    	}
		return getForward(request, mapping, "detail");
	}
	
	
	/**
	 * 查询组
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward queryGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		OAKnowFolderSearchForm oaForm = (OAKnowFolderSearchForm)form;
		String first = request.getParameter("first");
		if("1".equals(first) && first != null){
			oaForm.setFolderId("");
			oaForm.setPageNo(1);
		}
		Result result = mgt.quertFolder(oaForm.getFolderId(),first);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("groupList", result.retVal);
		}
		return getForward(request, mapping, "quertGroup");
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
			Result fuResult=mgt.quertFolderCode(classCode);
			List<OAKnowFolderBean> fugroup=(List<OAKnowFolderBean>)fuResult.getRetVal();
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
		OAKnowFolderForm oaForm = (OAKnowFolderForm)form;
		OAKnowFolderBean bean = new OAKnowFolderBean();
		read(oaForm, bean);
		String strClassCode = "";
		if(bean.getClassCode() == null || "".equals(bean.getClassCode())){
			strClassCode = new PublicMgt().getLevelCode("OAKnowledgeCenterFolder", "");
			bean.setClassCode(strClassCode);
		}else{
			strClassCode = new PublicMgt().getLevelCode("OAKnowledgeCenterFolder", bean.getClassCode());
			bean.setClassCode(strClassCode);
		}
		bean.setId(IDGenerater.getId());
		bean.setCreateBy(loginBean.getId());
		bean.setIsCatalog(0);
		bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		Result result = mgt.addGroup(bean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			result = mgt.updateIsCatalog(oaForm.getClassCode(),1);
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("dealAsyn", "true");
				EchoMessage.success().add(getMessage(
	                    request, "common.msg.addSuccess")).setBackUrl("/OAKnowFolder.do?operation=4&first=null&folderId="+bean.getClassCode()).
	                    setAlertRequest(request);
			}
		}else {
			EchoMessage.success().add(getMessage(
                    request, "common.msg.addFailture")).setClose().setRefresh(true).
                    setAlertRequest(request);
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
		
		String id = request.getParameter("folderId");
		String insertPlace = request.getParameter("insertPlace");
		OAKnowFolderBean oaknowBean = null;
		Result result = null;
		if(insertPlace != null && "list".equals(insertPlace)){
			//从文件信息列表进入组修改
			result = mgt.quertFolderCode(id);
			oaknowBean = ((List<OAKnowFolderBean>)result.getRetVal()).get(0);
		}else{
			result = mgt.loadGroup(id);
			oaknowBean = (OAKnowFolderBean)result.getRetVal();
		}
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			
			//获取此目录父级目录的权限
			if(oaknowBean.getClassCode().length()>5){
				Result fuResult=mgt.quertFolderCode(oaknowBean.getClassCode().substring(0,oaknowBean.getClassCode().length()-5));
				List<OAKnowFolderBean> fugroup=(List<OAKnowFolderBean>)fuResult.getRetVal();
				
				String str=mgt.getShouquan(fugroup.get(0).getPopedomDeptIds(),fugroup.get(0).getPopedomUserIds());
				String[] getStr=str.split("%%");				
			    
			    request.setAttribute("fuDept", fugroup.get(0).getPopedomDeptIds()); //父级的授权部门
				request.setAttribute("UserIds", fugroup.get(0).getPopedomUserIds()); //父级的授权个人
			    request.setAttribute("fuUser", getStr[1]);	 //整理后的父级的授权个人
			    request.setAttribute("deptNames", getStr[2]);  //父级授权部门的名称
	
			}
			
			//根据用户ID循环查询用户信息
			List<Employee> targetUsers = new ArrayList<Employee>();
			if (oaknowBean.getPopedomUserIds() != null && !"".equals(oaknowBean.getPopedomUserIds())) {
				for (String targetUser : oaknowBean.getPopedomUserIds().split(",")) {
					Employee employee = empMgt.getEmployeeById(targetUser);
					if (null != employee) {
						targetUsers.add(employee);
					}
				}
			}
			
			//下载选择的用户
			List<Employee> downUsers = new ArrayList<Employee>();
			if(oaknowBean.getDownUserIds() != null && !"".equals(oaknowBean.getDownUserIds())){
				for(String downUser : oaknowBean.getDownUserIds().split(",")){
					Employee employee = empMgt.getEmployeeById(downUser);
					if(null != employee){
						downUsers.add(employee);
					}
				}
			}
			// 查询通知部门
			List<Department> targetDept = new ArrayList<Department>();
			if (oaknowBean.getPopedomDeptIds() != null && !"".equals(oaknowBean.getPopedomDeptIds())) {
				String[] popedomDeptIds = oaknowBean.getPopedomDeptIds().split(",");
				for (String classCode : popedomDeptIds) {
					Department dept = adviceMgt.getDepartmentByClassCode(classCode);
					if (null != dept) {
						targetDept.add(dept);
					}
				}
			}
			//下载选择的部门
			List<Department> downDept = new ArrayList<Department>();
			if(oaknowBean.getDownDeptIds() != null && !"".equals(oaknowBean.getDownDeptIds())){
				for(String deptCode : oaknowBean.getDownDeptIds().split(",")){
					Department dept = adviceMgt.getDepartmentByClassCode(deptCode);
					if(null != dept){
						downDept.add(dept);
					}
				}
			}
			request.setAttribute("targetUsers", targetUsers);
			request.setAttribute("targetDept", targetDept);
			request.setAttribute("downUsers", downUsers);
			request.setAttribute("downDept", downDept);
			String depts = oaknowBean.getPopedomDeptIds();
				if(depts != null && !"".equals(depts)){
					if(!",".equals(depts.substring(depts.length()-1))){
						depts +=",";
					}
				}
				
			oaknowBean.setPopedomDeptIds(depts);
			request.setAttribute("group", oaknowBean);
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
		OAKnowFolderForm oaForm = (OAKnowFolderForm)form;
		
		OAKnowFolderBean bean = new OAKnowFolderBean();
		read(oaForm, bean);
		bean.setLastUpdateBy(loginBean.getId());
		bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		Result result = mgt.updateGroup(bean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(
                    request, "common.msg.updateSuccess")).setAlertRequest(request);
	    } else {
	         EchoMessage.success().add(getMessage(
	                     request, "common.msg.updateErro")).
	                     setAlertRequest(request);
	    }
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward del(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		String[] keyIds = getParameter("keyIds", request).split(",");
		String insertPlace=request.getParameter("insertPlace");
		
		Result rs1 = mgt.quertFolderCode(keyIds[0]);
		OAKnowFolderBean knowFolder = null;
		if(rs1.retCode == ErrorCanst.DEFAULT_SUCCESS){
			knowFolder=(OAKnowFolderBean)((ArrayList) rs1.retVal).get(0);
		}
		if(knowFolder.getClassCode().length()-5 != 0){
			//存在父级
			String strCode = knowFolder.getClassCode().substring(0,knowFolder.getClassCode().length()-5);
			Result res = mgt.quertFolder(strCode, "");
			System.out.println(keyIds.length);
			if(res.realTotal == keyIds.length){
				//父级中不存在数据
				res = mgt.updateIsCatalog(strCode, 2);
			}
		}
		/* 删除组 */
		Result rs = mgt.delGroup(keyIds);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			if(insertPlace != null && "knowList".equals(insertPlace)){
				request.getSession().setAttribute("OAKnowSearchForm", null);
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess"))
				.setBackUrl("/OAKnowCenter.do?type=oaKnowList&insertPlace="+insertPlace).setAlertRequest(request);
			}else{
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess"))
				.setBackUrl("/OAKnowFolder.do?operation=4").setAlertRequest(request);
			}
		}else {
			if(insertPlace != null && "knowList".equals(insertPlace)){
				request.getSession().setAttribute("OAKnowSearchForm", null);
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess"))
				.setBackUrl("/OAKnowCenter.do?type=oaKnowList&insertPlace="+insertPlace).setAlertRequest(request);
			}else{
				EchoMessage.success().add(getMessage(request, "common.msg.delError")).setBackUrl("/OAKnowFolder.do?operation=4").setAlertRequest(request);
			}
		}
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 按目录清空文件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward deleteFiles(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		String folderId = request.getParameter("folderId");
		String[] keyids =new String[]{folderId};
		Result result = mgt.deleteFiles(keyids);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
                    request, "oa.know.liquidate.success"))
                    .setBackUrl("/OAKnowFolder.do?operation=4").
                    setAlertRequest(request);
		}else {
			EchoMessage.success().add(getMessage(
                    request, "oa.know.liquidate.error"))
                    .setBackUrl("/OAKnowFolder.do?operation=4").
                    setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * 执行删除操作时，判断该目录下是否存在数据（子目录、知识中心信息）
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward checkGroup(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response){
		String[] keyIds = getParameter("keyIds", request).split(",");
		int sumnum=0;         //获取数据总数
		String theGroup="";   //获取含有子目录
		for(int i=0;i<keyIds.length;i++){
			int num=0;
			Result orbean=mgt.quertFolderCode(keyIds[i]); //获取组
			OAKnowFolderBean obean = null;
			if(orbean.retCode == ErrorCanst.DEFAULT_SUCCESS){
				obean=(OAKnowFolderBean)((ArrayList) orbean.retVal).get(0);
			}
			
			Result allGroup=mgt.quertFolder(obean.getClassCode(),"delete"); //获取与该目录相关的子级目录
			if(allGroup.realTotal>1){
				num +=allGroup.realTotal-1;
			}
			
			Result allOrdain=mgt.queryKnowBygroup(obean.getClassCode()); //获取与该目录相关的知识中心信息
			if(allOrdain.realTotal >0){
				num+=allOrdain.realTotal;
			}
			
			if(num !=0){    //判断数据是否为0，如果不是，获取该目录的目录名 
				theGroup +=obean.getFolderName()+",";
			}
			sumnum+=num;
		}	
		BaseEnv.log.error("此时值:"+sumnum+".."+theGroup);
		request.setAttribute("msg", sumnum+";"+theGroup);

		return getForward(request, mapping, "blank");
		
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
			Result fuResult=mgt.quertFolderCode(classCode);
			List<OAKnowFolderBean> fugroup=(List<OAKnowFolderBean>)fuResult.getRetVal();
			String[] getStr=mgt.getShouquan(fugroup.get(0).getPopedomDeptIds(),fugroup.get(0).getPopedomUserIds()).split("%%");
			String deIds="";
			String userIds="";
			if("".equals(fugroup.get(0).getPopedomDeptIds()))	
				deIds="noDept";
			else{
				deIds=fugroup.get(0).getPopedomDeptIds();
			}
			if("".equals(fugroup.get(0).getPopedomUserIds())){
				userIds="noUser";
			}else{
				userIds=fugroup.get(0).getPopedomUserIds();
			}
		    request.setAttribute("msg", getStr[1]+";"+getStr[2]+";"+deIds+";"+userIds); 
		}
		return  getForward(request, mapping, "blank");
	}
}
