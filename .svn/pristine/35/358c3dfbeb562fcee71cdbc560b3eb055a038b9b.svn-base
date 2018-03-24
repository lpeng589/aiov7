package com.koron.crm.tab;

import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.hibernate.collection.PersistentBag;

import com.koron.crm.client.CRMClientMgt;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.dbfactory.Result;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.menyi.aio.bean.ColConfigBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.tab.TabMgt;
/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-6
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class CrmTabAction extends BaseAction {
    TabMgt tabMgt = new TabMgt();

    public ActionForward exe(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
    	ActionForward forward = null ;
        String type = request.getParameter("tabType") ;
    	if("tab".equals(type)){
    		forward = queryTab(mapping, form, request, response) ;
    	}else if("state".equals(type)){
    		forward = queryState(mapping, form, request, response) ;
    	}else if("detail".equals(type)){
    		forward = queryDetail(mapping, form, request, response) ;
    	}else{
    		forward = queryAll(mapping, form, request, response) ;
    	}
    	return forward ;
    }
    
    
	 /**
	 * 客户详情页面 显示客户最新动态
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward queryState(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String clientId = getParameter("clientId", request) ;/*客户Id*/
		
		return mapping.findForward("crmState");
	}
	
	/**
	 * 框架
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward queryAll(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String keyId = getParameter("keyId", request) ;
		String showClose = getParameter("showClose", request) ;/*是否关闭按钮*/
		String tableName = request.getParameter("tableName") ;
        if(tableName==null || tableName.length()==0){
        	tableName = "CRMClientInfo" ;
        }
        request.setAttribute("tableName", tableName) ;
		request.setAttribute("public", getParameter("public", request)) ;
		request.setAttribute("designId", getParameter("designId", request)) ;
		request.setAttribute("keyId", keyId) ;
		request.setAttribute("showClose", showClose) ;
		return mapping.findForward("crmAll");
	}
	
	/**
	 * 客户详情页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward queryDetail(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String keyId = getParameter("keyId", request) ;
		LoginBean lg = getLoginBean(request) ;
		String tableName = getParameter("tableName", request);
		Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
		DBTableInfoBean tableInfo = (DBTableInfoBean) map.get(tableName);
		boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		ArrayList<DBFieldInfoBean> fieldList = new ArrayList<DBFieldInfoBean>() ;
		
		Result rs = new DynDBManager().detail(tableName, map, keyId, lg.getSunCmpClassCode(),props,lg.getId(),isLastSunCompany,"");
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	Hashtable<String,ArrayList<ColConfigBean>> userColConfig = (Hashtable<String,ArrayList<ColConfigBean>>)
													 request.getSession().getServletContext().getAttribute("userSettingColConfig") ;
        	ArrayList<ColConfigBean> configList = new ArrayList<ColConfigBean>() ;//主表的列配置
        	if(userColConfig!=null && userColConfig.size()>0){
            	configList = userColConfig.get("detail"+tableName+"bill") ;
                 ArrayList<DBFieldInfoBean> dbList = new ArrayList<DBFieldInfoBean>();
                 if(PersistentBag.class.getName().equals(tableInfo.getFieldInfos().getClass().getName())){
                 	PersistentBag bag = (PersistentBag) tableInfo.getFieldInfos();
                 	for (Object object : bag) {
        					dbList.add((DBFieldInfoBean) object);
        				}
                 }else{
                 	dbList = (ArrayList<DBFieldInfoBean>) tableInfo.getFieldInfos() ;
                 }
            	//加载主表中自定列配置
                if(configList!=null && configList.size()>0){
                	for(ColConfigBean configBean : configList){
                		for(DBFieldInfoBean fieldBean : dbList){
                			if(configBean.getColName().equals("@TABLENAME."+fieldBean.getFieldName())){
                    			fieldList.add(fieldBean) ;
                    			break ;
                			}else if(configBean.getColName().equals(tableName+"."+fieldBean.getFieldName())){
                    			fieldList.add(fieldBean) ;
                    			break ;
                			}
                			if(configBean.getColName().equals(fieldBean.getFieldName())){
                				if(fieldBean.getInputType()==DBFieldInfoBean.INPUT_HIDDEN_INPUT){
                					DBFieldInfoBean field = cloneObject(fieldBean) ;
            	        			field.setInputType(fieldBean.getInputTypeOld()) ;
            	        			fieldList.add(field) ;
                				}else{
                					fieldList.add(fieldBean) ;
                				}
                				break ;
                			}
                		}
                	}
                }
        	}
        }
        if(fieldList.size()==0){
        	fieldList = (ArrayList<DBFieldInfoBean>) tableInfo.getFieldInfos() ; ;
        }
        HashMap values = (HashMap) rs.retVal ;
        ArrayList childList = (ArrayList) values.get("TABLENAME_CRMClientInfoDet");
        request.setAttribute("childList", childList) ;
        request.setAttribute("tableName", tableName) ;
        request.setAttribute("fieldInfos", fieldList) ;
        request.setAttribute("values", rs.retVal) ;
        request.setAttribute("fieldInfos2", tableInfo.getFieldInfos()) ;
		return mapping.findForward("crmDetail");
	}
	
	/**
	   * 克隆一个DBFieldInfoBean对象
	   * @param fieldBean
	   * @return
	   */
	  public DBFieldInfoBean cloneObject(DBFieldInfoBean fieldBean) {
		DBFieldInfoBean field = new DBFieldInfoBean();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(fieldBean);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			field = (DBFieldInfoBean) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return field ;
	}
	  
	/**
	 * 查询兄弟表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward queryTab(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	String userId=getLoginBean(request).getId();
        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        ArrayList childTabList = new ArrayList();
        String tableName = request.getParameter("tableName") ;
        String keyId = request.getParameter("keyId") ;
        if(keyId!=null && keyId.length()>0){
        	request.setAttribute("firstId", keyId);
        }
        if(tableName==null || tableName.length()==0){
        	tableName = "CRMClientInfo" ;
        }
        String tabIndex = request.getParameter("tabIndex")==null?"":request.getParameter("tabIndex") ;
        if(tabIndex.length()>0){
        	request.setAttribute("tabIndex", tabIndex) ;
        }
        
//      得到用户有没有操作邻居表设置的权限
        MOperation mopNeighbour = (MOperation) this.getLoginBean(request).getOperationMap().get("/UserFunctionQueryAction.do?tableName=tblNeighbourMain");
        if(mopNeighbour!=null){
        	request.setAttribute("updatestate", mopNeighbour.update);
        }
        
        Result re=new TabMgt().selectHavingBrothertable(map, tableName);
        ArrayList<DBTableInfoBean> childHavingTabList = (ArrayList<DBTableInfoBean>) re.retVal ;
        
        childTabList = DDLOperation.getBrotherTables(tableName, map);
        /*查询用户是否设置邻居了*/
        Result result = new TabMgt().selectBrotherTable(map,tableName,userId) ;
        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	ArrayList<DBTableInfoBean> listBrother = (ArrayList<DBTableInfoBean>) result.retVal ;
        	if(listBrother.size()>0){
        		childTabList = listBrother ;
        	}
        }
        ArrayList moduleList = BaseEnv.allModule;
        for(int i=0;i<childTabList.size();i++){
        	//判断模块是否启用
        	DBTableInfoBean tableIfno = (DBTableInfoBean) childTabList.get(i) ;
			Result result2 = new Result() ;
			GlobalsTool.moduleIsUsed(moduleList, tableIfno.getTableName(),result2) ;
			MOperation mop = (MOperation) getLoginBean(request).getOperationMap()
						.get("/UserFunctionQueryAction.do?parentTableName="+tableName+"&tableName="+tableIfno.getTableName()) ;
			if(result2.retCode != ErrorCanst.DATA_ALREADY_USED || mop==null){
				childTabList.remove(i) ;
				i-- ;
			}
        }
        
        request.getSession().setAttribute("childTabHavingList", childHavingTabList);
       
        request.setAttribute("public", getParameter("public", request)) ;
        request.setAttribute("childTabList", childTabList);
        request.setAttribute("tableName", tableName);
        request.setAttribute("showClose", getParameter("showClose", request)) ;
        request.setAttribute("designId", getParameter("designId", request)) ;
        return mapping.findForward("crmTab");
    }

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		// TODO Auto-generated method stub
		return null;
	}
}
