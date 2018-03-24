package com.koron.oa.framework;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bean.Department;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
/**
 * 
 * <p>Title:组织架构</p> 
 * <p>Description: </p>
 *
 * @Date:Oct 19, 2010
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class FrameworkAction extends MgtBaseAction{

	FrameworkMgt mgt = new FrameworkMgt() ;
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int operation = getOperation(request);
		String type = request.getParameter("type");
		request.setAttribute("winCurIndex", request.getParameter("winCurIndex"));
		
		ActionForward forward = null;
		switch (operation) {
	        case OperationConst.OP_QUERY:
	        	query(mapping, form, request, response) ;
	        	break ;
	        default:
	        	 if ("tree".equals(type)) {
	        		 forward = toTree(mapping, form, request, response) ;
	        	 } else if ("list".equals(type)){
	        		 forward = list(mapping,form,request,response) ;
	        	 } else if ("dept".equals(type)){
	        		 forward = departInfo(mapping,form,request,response) ;
	        	 } else if ("work".equals(type)){
	        		 forward = workInfo(mapping,form,request,response) ;
	        	 } else if("login".equals(type)){
	        		 forward = loginReatail(mapping, form, request, response) ;
	        	 }else if("orgchart".equals(type)){
	        		 forward = toOrgChart(mapping, form, request, response) ;
	        	 }else{
	        	// forward = query(mapping, form, request, response) ;
	        		 forward = toOrgChart(mapping, form, request, response) ;	
	        	 }
	        	 break ;
		}
		return forward;
	}

	protected ActionForward query(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws  Exception {
		return getForward(request, mapping, "toFramework");
	}
	
	protected ActionForward list(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws  Exception {
		
		String folderName = getParameter("folderName", request) ;
		String classcode = request.getParameter("classcode");
		
		List deptNums = new ArrayList();
		List<Department> list = (List<Department>) mgt.queryAllDetp().retVal ;
		
		if(folderName!=null && folderName.length()>0){
			folderName = GlobalsTool.toChinseChar(folderName) ;
			int num = -1 ;
			for(int i=0;i<list.size();i++){
				String strName = list.get(i).getDeptFullName() ;
				if(folderName.equals(strName)){
					num = i ;
					break ;
				}
			}
			if(num!=-1){
				Department[] dept = {list.get(num)} ;
				request.setAttribute("deparList", dept);
			}
		}else if(classcode!=null && classcode.length()>0){
			Result result = mgt.queryDetpByClassCode(classcode);
			request.setAttribute("deparList", result.retVal);
		}else{
			Result result = mgt.queryAllDetp() ;
			request.setAttribute("deparList", result.retVal);
		}
//		所有部门对应人数
		for (int i = 0; i < list.size(); i++) {
			String num = mgt.getDeptEmpNumber(list.get(i).getclassCode()).getRetVal().toString();
			KeyPair pair =  new KeyPair();
			pair.setName(list.get(i).getid());
			pair.setValue(num);
			deptNums.add(pair);
		}
		request.setAttribute("deptNums", deptNums);
		/*查询所有的职员*/
		Result result = mgt.queryAllEmployee() ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("allEmp", result.retVal) ;
		}
		return getForward(request, mapping, "list");
	}
	
	/**
	 * 查询个人详细信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward workInfo(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws  Exception {
		String userId = getParameter("userId", request) ;
		Result result = mgt.loadEmployeeBean(userId) ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			String[] emp =  (String[]) result.retVal ;
			ArrayList<String> deptList = (ArrayList<String>) mgt.getDeptClassCodeByManager(userId).retVal ;
			if(deptList!=null && deptList.size()>0){
				ArrayList empList = new ArrayList() ;
				for(String str : deptList){
					result = mgt.selectDeptNameByID(str) ;
					empList.addAll((ArrayList)result.retVal) ;
				}
				request.setAttribute("empList", empList) ;
			}
			EmployeeBean bean = (EmployeeBean) mgt.detail(emp[4]).getRetVal() ;
			if(bean!=null && !bean.getId().equals(userId)){
				request.setAttribute("managerName", bean.getEmpFullName()) ;
			}
			request.setAttribute("emp", emp) ;
		}
		Calendar calendar = Calendar.getInstance() ;
		result = mgt.queryEmpInfo(getLocale(request).toString(), userId, 
					String.valueOf(calendar.get(Calendar.YEAR)), String.valueOf(calendar.get(Calendar.MONTH)+1)) ;
		request.setAttribute("infoList", result.retVal) ;
		return getForward(request, mapping, "workdept");
	}
	
	protected ActionForward departInfo(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws  Exception {
		
		String deptId = getParameter("deptId", request) ;
		/*查询部门详细信息*/
		Result result = mgt.getDepartMentById(deptId) ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			String[] deptInfo = (String[]) result.getRetVal() ;
			request.setAttribute("deptInfo", deptInfo) ;
			/*查询部门人数*/
			result = mgt.getDeptEmpNumber(deptInfo[3]) ;
			request.setAttribute("deptNumber", result.retVal) ;
			
			/*查询部门统计信息*/
			Calendar calendar = Calendar.getInstance() ;
			result = mgt.queryDeptInfo(getLocale(request).toString(), deptInfo[3], 
						String.valueOf(calendar.get(Calendar.YEAR)), String.valueOf(calendar.get(Calendar.MONTH)+1)) ;
			request.setAttribute("infoList", result.retVal) ;
		}
		
		return getForward(request, mapping, "deptInfo");
	}
	
	 //到树页面
    protected ActionForward toTree(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
        Result rs=mgt.queryFolder();
        if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
        	ArrayList list=(ArrayList)rs.retVal;
        	String folderTree="";
        	for(int i=0;i<list.size();i++){
        		String []obj=(String[])list.get(i);        		
        		if(obj[1].length()==5){
        			folderTree+=this.folderTree(list,obj[2], obj[1],obj[4],obj[0]);
        		} 
        	}
        	request.setAttribute("result",folderTree); 
        }
        return getForward(request, mapping, "toTree");
    }
    
    private String folderTree(ArrayList list,String folderName,String classCode,String isCatalog,String folderId) throws Exception{
    	String folderTree="";
    	if(isCatalog!=null&&isCatalog.equals("1")){
    		folderTree="<li><span><a title='"+folderName+"' href=\"javascript:goFarmeList('"+classCode+"')\">"+folderName+"</a><font style=\"color:#ff0000\" id=\"_1\"></font></span><ul>";
    		for(int i=0;i<list.size();i++){
    			String []obj=(String[])list.get(i);    
    			if(obj[1].substring(0,obj[1].length()-5).equals(classCode)){
    				folderTree+=this.folderTree(list,obj[2], obj[1],obj[4],obj[0]);
    			}
    		}
    		folderTree+="</ul></li>";
    	}else{
    		folderTree="<li><span><a  title='"+folderName+"' href=\"javascript:goFarmeList('"+classCode+"')\">"+folderName+"</a><font style=\"color:#ff0000\" id=\"_1\"></font></span></li>";
    	}
    	
    	return folderTree;
    }
    
    /**
     * AIO零售单登录界面
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward loginReatail(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
		LoginBean login = getLoginBean(request) ;
		ReportDataMgt mgt = new ReportDataMgt() ;
		Result result = mgt.queryShopIdByUserId(login.getId()) ;
		if(result.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<String[]> listShop = (ArrayList<String[]>) result.getRetVal() ;
			if(listShop!=null && listShop.size()>0){
				request.setAttribute("listShop", listShop) ;
			}else{
				MOperation mop = (MOperation) getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?tableName=tblShop") ;
				String message = "未找到与你相关的门店，请确认门店设置是否正确？请联系管理员。" ;
				if(mop!=null){///UserFunctionQueryAction.do?tableName=tblShop&src=menu
					message = "未找到与你相关的门店，请确认<a href=\"javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblShop&src=menu','门店设置')\">门店设置</a>是否正确？" ;
				}
				EchoMessage.info().add(message)
				 			.setNoBackButton().setClose().setRequest(request);
				return getForward(request, mapping, "alert") ;
			}
		}
		return getForward(request, mapping, "reatailSingle");
    }
    
    
    //orgchart
    protected ActionForward toOrgChart(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
    	
        Result rs=mgt.queryFolder();
        if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
        	
        	
        	ArrayList list=(ArrayList)rs.retVal;
        	String folderTree="";
        	for(int i=0;i<list.size();i++){
        		String []obj=(String[])list.get(i);        		
        		if(obj[1].length()==5){
        			folderTree+=this.folderTree(list,obj[2], obj[1],obj[4],obj[0]);
        		} 
        	}
        	request.setAttribute("result",folderTree); 
        }
        
        /*查询所有的职员*/
		Result result = mgt.queryAllEmployee() ;
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("allEmp", result.retVal) ;
		}
        return getForward(request, mapping, "tochart");
    }
}
