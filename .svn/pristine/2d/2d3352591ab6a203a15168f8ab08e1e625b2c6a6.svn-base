package com.koron.oa.accredit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.omg.CORBA.Request;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * 
 * <p>Title:授权选择Action</p> 
 * <p>Description: </p>
 *
 * @Date:2012-8-27
 * @Copyright: 科荣软件
 * @Author 方家俊
 */
public class AccreditAction extends MgtBaseAction{
	
	AccreditMgt mgt = new AccreditMgt();
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		//跟据不同操作类型分配给不同函数处理
		String operation = request.getParameter("operation");
		ActionForward forward = null;
		
		if("accreditInfo".equals(operation)){
			//右边的详情
			forward = accreditInfo(mapping, form, request, response);
		}else{
			//首页
			forward = query(mapping, form, request, response);
		}
		return forward;
	}
	
	/**
	 * 右边的详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	
	protected ActionForward accreditInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		AccreditSearchForm accForm = (AccreditSearchForm)form;
		
		Result result = null;
		if(!"".equals(accForm.getValue()) && accForm.getValue() != null){
			accForm.setValue(GlobalsTool.toChinseChar(accForm.getValue()));
		}
		if("deptGroup".equals(accForm.getPopname())){
			//部门数据
			result = mgt.queryDeptData(accForm);
		}else if("empGroup".equals(accForm.getPopname())){
			//分组
			result = mgt.queryEmpGroupData(accForm);			
		}else if("communicationGroup".equals(accForm.getPopname())){
			//通讯录(由于通讯录改造成数据全部去职员表的)
			String local = this.getLocale(request).toString();
			result = mgt.queryUserData(accForm,local);
//			result = mgt.queryCommunication(accForm, userId);
		}else if("clientGroup".equals(accForm.getPopname())){
			//客户联系人
			result = mgt.queryClientData(accForm, this.getLoginBean(request));
		}else if("CrmClickGroup".equals(accForm.getPopname())){
			//客户弹出框
			result = mgt.queryCRMClientData(accForm, request, this.getLoginBean(request));
		}else if("defineClass".equals(accForm.getPopname())){
			//自定义数据
			String local = this.getLocale(request).toString();
			
			ArrayList allTables = (ArrayList) request.getSession()
			.getServletContext().getAttribute("scopeCata");
			Object[] cataTab=null;
			for (int i = 0; i < allTables.size(); i++) {
				cataTab = (Object[]) allTables.get(i);
				if (cataTab[0].equals(accForm.getCondition())) {
					break;
				}
			}			
			result = mgt.queryDefineClassChildData(accForm,(String)cataTab[4],(String)cataTab[5],(String)cataTab[6]);
		}else if("jobGroup".equals(accForm.getPopname()) || "staffGroup".equals(accForm.getPopname())
				|| "onLineGroup".equals(accForm.getPopname()) || "leaveGroup".equals(accForm.getPopname()) || "userGroup".equals(accForm.getPopname())){
			result = this.getEmployee(accForm, this.getLoginBean(request));			
		}
		if(result !=null){
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("list", result.retVal);
				request.setAttribute("pageBar", this.pageBar(result, request));
			}
		}
		
		return getForward(request, mapping, "accreditInfo");
	}

	/**
	 * 首页
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		AccreditSearchForm accForm = (AccreditSearchForm)form;
		if(accForm.getInputType() == null || "".equals(accForm.getInputType())){
			//默认是单选框
			accForm.setInputType("radio");
		}
		String data = "";
		String userId = this.getLoginBean(request).getId();
		accForm.setParameterCode(accForm.getParameterCode()==null?"":accForm.getParameterCode());//传入部门过滤条件
		if(accForm.getPopname() != null && "deptGroup".equals(accForm.getPopname())){
			//查询部门的数据
			accForm.setValue(accForm.getValue()== null?"":GlobalsTool.toChinseChar(accForm.getValue()));
			data = queryDeptData(accForm,1);
		}else if(accForm.getPopname() != null && "userGroup".equals(accForm.getPopname())){
			//对职员进行分组
			accForm.setUserCode(accForm.getUserCode()==null?"":accForm.getUserCode());					//传入人员过滤条件
			//查询部门组
			data = queryDeptData(accForm,2);			
		}else if(accForm.getPopname() != null && "empGroup".equals(accForm.getPopname())){
			//分组
			accForm.setValue(accForm.getValue()== null?"":GlobalsTool.toChinseChar(accForm.getValue()));
			//查询职员分组
			data = queryEmpData(accForm);
		}else if(accForm.getPopname() != null && "communicationGroup".equals(accForm.getPopname())){
			//通讯录人员弹出框(取部门)
			//data = queryCommunicationData(accForm,userId);
			data = queryDeptData(accForm,2);
		}else if(accForm.getPopname() != null && "clientGroup".equals(accForm.getPopname())){
			//客户联系人
			data = queryClientGroup(accForm, userId);
		}else if(accForm.getPopname() != null && "CrmClickGroup".equals(accForm.getPopname())){
			//客户弹出框
			data = queryCRMClientGroup(accForm, userId);
		}else if(accForm.getPopname() != null && "staffGroup".equals(accForm.getPopname())){
			//wyy职员分组
			LoginBean login = getLoginBean(request);
			String id = login.getId();
			accForm.setValue(accForm.getValue()== null?"":GlobalsTool.toChinseChar(accForm.getValue()));
			//查询职员分组
			String str=mgt.queryGroupData(id);
			request.setAttribute("msg", str);
			return getForward(request, mapping, "blank");
		}else if(accForm.getPopname() != null && "defineClass".equals(accForm.getPopname())){
			//自定义分类资料，如仓库，商品等待
			accForm.setValue(accForm.getValue()== null?"":GlobalsTool.toChinseChar(accForm.getValue()));
			
			data = queryDefineClass(accForm,request);
		}else if("jobGroup".equals(accForm.getPopname()) || "onLineGroup".equals(accForm.getPopname()) || "leaveGroup".equals(accForm.getPopname())){
			accForm.setValue(accForm.getValue()== null?"":GlobalsTool.toChinseChar(accForm.getValue()));
			String str = this.queryLeft(accForm.getPopname(), request);		
			request.setAttribute("msg",str);
			return getForward(request, mapping, "blank");
			
		}
		request.setAttribute("chooseData", request.getParameter("chooseData")); //已选数据的编号
		request.setAttribute("data", data);
		return getForward(request, mapping, "accreditIndex");
	}
	
	
	/**
	 * 查询部门分组
	 * @return
	 */
	
	protected String queryDeptData(AccreditSearchForm accForm, Integer types){
		Result rs = mgt.queryData(accForm, types);
		String folderTree = "[";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)rs.retVal;
			int num =0;
			for(int i=0;i<list.size();i++){
				Object[] o = (Object[])list.get(i);
				if(((String)o[3]).length() == 5){
					folderTree += "{ id:\""+o[3]+"\", pId:0, name:\""+o[2]+"\"";
				}else{
					String classC = ((String)o[3]).substring(0, ((String)o[3]).length()-5);
					folderTree += "{ id:\""+o[3]+"\", pId:\""+classC+"\", name:\""+o[2]+"\"";
				}
				if(Integer.parseInt(o[4].toString()) == 1 && !accForm.isChooseParent()){
					//当是父级并且设置了不可以选择父级时，进行处理
					folderTree += ",nocheck:true";
				}
				folderTree += "}";
				if(num != list.size()-1){
					folderTree+=",";
				}
				num ++;
			}
		}
		folderTree += "]";
		return folderTree;
	}
	
	/**
	 * 分组
	 * @param accForm
	 * @return
	 */
	protected String queryEmpData(AccreditSearchForm accForm){
		Result rs=mgt.queryEmpData(accForm);
		String folderTree = "[";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)rs.retVal;
			for(int i=0;i<list.size();i++){
				Object[] o = (Object[])list.get(i);
				folderTree += "{id:\""+o[0]+"\", name:\""+o[1]+"\"}";
				if(i !=list.size()-1){
					folderTree += ",";
				}
			}
		}
		folderTree += "]";
		return folderTree;
	}
	/**
	 * 通讯录组
	 * @param accForm
	 * @return
	 */
//	protected String queryCommunicationData(AccreditSearchForm accForm,String userId){
//		Result rs = mgt.queryCommunicationGroup(accForm,userId);
//		String folderTree = "[";
//		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
//			ArrayList list = (ArrayList)rs.retVal;
//			int num =0;
//			for(int i=0;i<list.size();i++){
//				Object[] o = (Object[])list.get(i);
//				if(((String)o[1]).length() == 5){
//					folderTree += "{ id:\""+o[1]+"\", pId:0, name:\""+o[2]+"\"}";
//				}else{
//					String classC = ((String)o[1]).substring(0, ((String)o[1]).length()-5);
//					folderTree += "{ id:\""+o[1]+"\", pId:\""+classC+"\", name:\""+o[2]+"\"}";
//				}
//				
//				if(num != list.size()-1){
//					folderTree+=",";
//				}
//				num ++;
//			}
//		}
//		folderTree += "]";
//		return folderTree;
//	}
	
	/**
	 * 客户联系人组
	 * @param accForm
	 * @param userId
	 * @return
	 */
	protected String queryClientGroup(AccreditSearchForm accForm,String userId){
		
		//Result rs = mgt.queryClientGroup(accForm, userId);
		String folderTree = "[";
		//if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//ArrayList list = (ArrayList)rs.retVal;
			//for(int i=0;i<list.size();i++){
			//	Object[] o = (Object[])list.get(i);
				folderTree += "{id:\"3421\", name:\"客户联系人\"}";
			//	if(i !=list.size()-1){
			//		folderTree += ",";
			//	}
			//}
		//}
		folderTree += "]";
		return folderTree;
	}
	
	/**
	 * 客户弹出框
	 * @param accForm
	 * @param userId
	 * @return
	 */
	protected String queryCRMClientGroup(AccreditSearchForm accForm,String userId){
		List<KeyPair> list=GlobalsTool.getEnumerationItems("ClientStatus", "zh_CN");
		String folderTree ="[";
		for(int i=0;i<list.size();i++){
			KeyPair kp=list.get(i);
			folderTree=folderTree+"{id:\""+kp.getValue()+"\", name:\""+kp.getName()+"\"},";
		}
		folderTree =folderTree.substring(0,folderTree.length()-1)+"]";
		return folderTree;
	}
	
	
	/**
	 * 
	 * 查询自定义分类资料
	 * @return
	 */
	protected String queryDefineClass(AccreditSearchForm accForm, HttpServletRequest request){
		
		ArrayList allTables = (ArrayList) request.getSession()
		.getServletContext().getAttribute("scopeCata");
		Object[] cataTab=null;
		for (int i = 0; i < allTables.size(); i++) {
			cataTab = (Object[]) allTables.get(i);
			if (cataTab[0].equals(accForm.getCondition())) {
				break;
			}
		}
		 
		
		Result rs = mgt.queryDefineClassData(accForm,(String)cataTab[4],(String)cataTab[5],(String)cataTab[6]);
		String folderTree = "[";
		if("tblCompany".equals(accForm.getCondition())){
			//往来单位，先加上类别
			folderTree += "{ id:\"1\", pId:0, name:\"供应商\"},";
			folderTree += "{ id:\"2\", pId:0, name:\"客户\"},";
			folderTree += "{ id:\"3\", pId:0, name:\"客户供应商\"},";
		}
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)rs.retVal;
			for(int i=0;i<list.size();i++){
				Object[] o = (Object[])list.get(i);
				if(((String)o[3]).length() == 5){
					if("tblCompany".equals(accForm.getCondition())){
						folderTree += "{ id:\""+o[3]+"\", pId:\""+o[5]+"\", name:\""+o[2]+"\"}";
					}else{
						folderTree += "{ id:\""+o[3]+"\", pId:0, name:\""+o[2]+"\"}";
					}
				}else{
					String classC = ((String)o[3]).substring(0, ((String)o[3]).length()-5);
					folderTree += "{ id:\""+o[3]+"\", pId:\""+classC+"\", name:\""+o[2]+"\"}";
				}	
				
				folderTree+=",";			
			}
		}
		folderTree += "]";
		return folderTree;
	}
	
	/*wyy**/
	/**
	 * 
	 * 加载左边
	 * @return
	 */
	public String queryLeft(String type,HttpServletRequest request){
		//List<String[]> list = new ArrayList<String[]>();
		String[] obj = new String[2];
		String str = "";
		if("jobGroup".equals(type)){
			List<String[]> lt = getEnumerationItems("duty", request);			
			for (int i = 0; i < lt.size(); i++) {
				Object line = (Object)lt.get(i);
				KeyPair kp = (KeyPair)line;
				String[] ob = new String[2];
				ob[0] = kp.getValue();
				ob[1] = kp.getName();
				str += ob[0]+","+ob[1]+"|";
				//list.add(ob);
			}
		}else if("onLineGroup".equals(type)){			
			obj[0] = "1_0";
			obj[1] = "在线人员";
			str += obj[0]+","+obj[1]+"|";
			//list.add(obj);
		}else if("leaveGroup".equals(type)){		
			obj[0] = "1_1";
			obj[1] = "离职人员";	
			str += obj[0]+","+obj[1]+"|";
			//list.add(obj);
		}
		//return list;
		return str;
	}
	
	/*获取所有职员*/
	public Result getEmployee(AccreditSearchForm accForm, LoginBean lg){
		Result result = new Result();						
		ArrayList<OnlineUser> userList = new ArrayList<OnlineUser>();
		OnlineUser[] users = OnlineUserInfo.getAllUserList();
		HashMap<String, String> groupMap = new HashMap<String, String>();
		/*判断出此职位有谁*/
		if("staffGroup".equals(accForm.getPopname()) && accForm.getValue() !=null && "group".equals(accForm.getKeyType())){
			Result rs = mgt.queryStaff(accForm.getValue());
			ArrayList rsRs = (ArrayList)rs.retVal;
			for (int i = 0; i < rsRs.size(); i++) {
				if(((Object[])rsRs.get(i))[1] !=null && ((Object[])rsRs.get(i))[0] !=null){
					groupMap.put(((Object[])rsRs.get(i))[1].toString(), ((Object[])rsRs.get(i))[0].toString());
				}			
			} 
		}
		for(OnlineUser user : users){
			//只取系统用户
			if(user.isPublic ==1){
				continue;
			}
			if( (user.getSysName() ==null || user.getSysName().length() <=0 )&&!"userGroup".equals(accForm.getPopname()) ){
				continue;			
			}
			if("noAdmin".equals(accForm.getCondition())){
				//判断是否过滤admin用户
				if("1".equals(user.getId())){
					continue;
				}
			}else if("openFlag".equals(accForm.getCondition())){
				//只显示可以进入系统的用户
				if(user.getSysName() == null || "".equals(user.getSysName())){
					continue;
				}
			}else if("openFlagNoAdmin".equals(accForm.getCondition())){
				//显示系统用户并且不显示admin
				if(user.getSysName() == null || "".equals(user.getSysName()) 
						|| "1".equals(user.getId())){
					continue;
				}
			}
			
			if("keyWord".equals(accForm.getKeyType())){			
				if("keyWord".equals(accForm.getKeyType()) && user.pingying.toUpperCase().indexOf(accForm.getValue().toUpperCase())>=0 
						|| user.getName().indexOf(accForm.getValue())>=0 || user.getEmpNumber().toUpperCase().indexOf(accForm.getValue().toUpperCase())>=0){
						if(!"-1".equals(user.statusId)){
							userList.add(user);
						}				
					}
			}else if("letter".equals(accForm.getKeyType())){//字母搜索
				String fstLter = user.pingying.split(",")[1].toUpperCase();
				if("letter".equals(accForm.getKeyType()) && fstLter.indexOf("0".equals(accForm.getValue())?"":accForm.getValue())==0){
					if(!"-1".equals(user.statusId)){
						userList.add(user);
					}	
				}				
			}else if("group".equals(accForm.getKeyType())){
				if("userGroup".equals(accForm.getPopname()) && accForm.getValue() !=null){
					if(user.getDeptId().indexOf(accForm.getValue())==0 && !"-1".equals(user.statusId)){
						userList.add(user);
					}
				}else if("jobGroup".equals(accForm.getPopname()) && accForm.getValue() !=null){
					if(user.getTitleID().equals((accForm.getValue())) && !"-1".equals(user.statusId)){
						userList.add(user);
					}
				}else if("staffGroup".equals(accForm.getPopname()) && accForm.getValue() !=null){
					if(groupMap.get(user.getId())!=null && !"-1".equals(user.statusId)){
						userList.add(user);
					}
				}else if("onLineGroup".equals(accForm.getPopname()) && accForm.getValue() !=null){				
					if(user.isOnline() && !"-1".equals(user.statusId)){
						userList.add(user);
					}
				}else if("leaveGroup".equals(accForm.getPopname())){
					if("-1".equals(user.statusId)){
						userList.add(user);
					}
				}
			}else if("onLineGroup".equals(accForm.getPopname()) && accForm.getValue() !=null){				
				if(user.isOnline() && !"-1".equals(user.statusId)){
					userList.add(user);
				}
			}else if("leaveGroup".equals(accForm.getPopname())){
				if("-1".equals(user.statusId)){
					userList.add(user);
				}
			}else{				
				if(user.getDeptId().indexOf(accForm.getValue()==null?"":accForm.getValue())==0 && !"-1".equals(user.statusId)){
					userList.add(user);
				}			
			}	
		}
		//f分页处理		
        int pageNo = accForm.getPageNo();
		int pageSize = accForm.getPageSize();   
		int totalSize = userList.size();
		int totalPage = totalSize%pageSize>0?totalSize/pageSize+1:totalSize/pageSize;
      	
		ArrayList<OnlineUser> userList2 = new ArrayList<OnlineUser>();
		for(int i=1;i<=userList.size();i++){
			if(i>((pageNo-1)*pageSize) && i<(pageNo)*pageSize){
				userList2.add(userList.get(i-1));
			}
		}
		
		result.setRealTotal(userList.size());
		result.setPageSize(pageSize);
		result.setPageNo(pageNo);
		result.setTotalPage(totalPage);
		result.setRetVal(userList2);
		return result;
	}
	
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		
		int operation = getParameterInt("operation", req) ;
		if(operation == 0){
			return null ;
		}
		return super.doAuth(req, mapping);
	}
}
