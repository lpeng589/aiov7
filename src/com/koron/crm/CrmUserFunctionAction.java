package com.koron.crm;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.config.ForwardConfig;

import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.dbfactory.Result;
import java.util.ArrayList;
import java.util.Hashtable;

import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.tab.TabMgt;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class CrmUserFunctionAction extends BaseAction {
    TabMgt tabMgt = new TabMgt();
    public CrmUserFunctionAction() {
    }

    public ActionForward exe(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
    	ActionForward forward = null;
    	String type=request.getParameter("type");
    	if("neighbor".equals(type)){
    		forward = showNeighbor(mapping, form, request, response);
    	}else if("addneighbor".equals(type)){
    		forward = addNeighbour(mapping, form, request, response);
    	}else{
    		forward = query(mapping, form, request, response);
    	}
        return forward;
    }
    
    /**
	 * 添加邻居表的信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addNeighbour(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoginBean loginbean=(LoginBean)request.getSession().getAttribute("LoginBean");
		String[] keyids=request.getParameterValues("keyhaving");
		String tableName=request.getParameter("tableName");
		TabMgt tabmgt=new TabMgt();
		Integer[] orders=new Integer[keyids.length];
		for(int i=0;i<keyids.length;i++){
			String order=request.getParameter(keyids[i]+"orderby");
			if(order==null || "".equals(order)){
				order="0";
			}
			orders[i]=new Integer(Integer.parseInt(order));
		}
		Integer tabStatus = getParameterInt("tab_bottom", request);
		Result result=tabmgt.insertNeighbourDetail(loginbean.getId(),tableName,keyids, orders,tabStatus);
		if(result.retCode==ErrorCanst.DEFAULT_SUCCESS){
			
		}		
		return query(mapping, form, request, response);
	}
    
    
    private ActionForward query(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response){
    	String tableName = request.getParameter("tableName");
        Hashtable map = (Hashtable) request.getSession().getServletContext().
                        getAttribute(BaseEnv.TABLE_INFO);
        ArrayList childTabList = new ArrayList();
        String f_brother = request.getParameter("f_brother") ;
        if(f_brother!=null && f_brother.length()>0){
        	request.setAttribute("firstId", f_brother);
        }
        request.setAttribute("popWinName", request.getParameter("popWinName"));
        
        String tabIndex = request.getParameter("tabIndex")==null?"":request.getParameter("tabIndex") ;
        if(tabIndex.length()>0){
        	request.setAttribute("tabIndex", tabIndex) ;
        }
        childTabList = DDLOperation.getBrotherTables(tableName, map);
        

        	
        
        LoginBean login = (LoginBean)request.getSession().getAttribute("LoginBean");
        
        /*查询用户是否设置邻居了*/
        LoginBean loginbean=(LoginBean)request.getSession().getAttribute("LoginBean");
        Result result = new TabMgt().selectBrotherTable(map,tableName,loginbean.getId()) ;
        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	ArrayList<DBTableInfoBean> listBrother = (ArrayList<DBTableInfoBean>) result.retVal ;
        	if(listBrother.size()>0){
        		childTabList = listBrother ;
        	}
        }
        //查询是否有自定义邻居表选项
        Result rs = new TabMgt().getBrotherDefine(tableName, f_brother);
        if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal instanceof ArrayList){
        	childTabList.clear();
        	for(String tn :(ArrayList<String>)rs.retVal){
        		childTabList.add(BaseEnv.tableInfos.get(tn));
        	}
        }
        /*
        ArrayList moduleList = (ArrayList) BaseEnv.allModule;
        for(int i=0;i<childTabList.size();i++){
        	//判断模块是否启用并且至少有一个操作项的权限
        	DBTableInfoBean tableInfo = (DBTableInfoBean) childTabList.get(i) ;
			Result result2 = new Result() ;
			GlobalsTool.moduleIsUsed(moduleList, tableInfo.getTableName(),result2) ;
			MOperation mop = (MOperation) login.getOperationMap()
					.get("/UserFunctionQueryAction.do?tableName="+tableName) ;
			if(result2.retCode != ErrorCanst.DATA_ALREADY_USED || mop==null){
				childTabList.remove(i) ;
				i-- ;
			}
        }
        */
        request.setAttribute("tabStatus", new TabMgt().selectNeTable2(tableName, loginbean.getId()).getRetVal()) ;
        request.setAttribute("childTabList", childTabList);
        request.setAttribute("tableName", tableName);
        return mapping.findForward("button");
    }
    
    
    
    private ActionForward showNeighbor(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
//    	获取所有邻居表的信息
        ArrayList childHavingTabList = new ArrayList();
        Hashtable map = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
        String tableName=request.getParameter("tableName");
        Result re=new TabMgt().selectHavingBrothertable(map, tableName);
        if(re.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	ArrayList<DBTableInfoBean> childList = (ArrayList<DBTableInfoBean>) re.retVal ;
        	if(childList.size()>0){
        		childHavingTabList = childList ;
        	}
        }
        
        /*查询用户是否设置邻居了*/
        LoginBean loginbean=(LoginBean)request.getSession().getAttribute("LoginBean");
        ArrayList childTabList = new ArrayList();
        Result results = new TabMgt().selectNeTable(map,tableName,loginbean.getId()) ;
        if(results.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	ArrayList<String[]> listBrother = (ArrayList<String[]>) results.retVal ;
        	if(listBrother.size()>0){
        		childTabList = listBrother ;
        	}
        }
        ArrayList moduleList = BaseEnv.allModule;
        for(int i=0;i<childHavingTabList.size();i++){
        	//判断模块是否启用
        	DBTableInfoBean tableIfno = (DBTableInfoBean) childHavingTabList.get(i) ;
			Result result2 = new Result() ;
			GlobalsTool.moduleIsUsed(moduleList, tableIfno.getTableName(),result2) ;
			MOperation mop = (MOperation) loginbean.getOperationMap()
						.get("/UserFunctionQueryAction.do?parentTableName="+tableName+"&tableName="+tableIfno.getTableName()) ;
			if(result2.retCode != ErrorCanst.DATA_ALREADY_USED || mop==null){
				childHavingTabList.remove(i) ;
				i-- ;
			}
        }
        request.setAttribute("tabStatus", new TabMgt().selectNeTable2(tableName, loginbean.getId()).getRetVal()) ;
        request.setAttribute("tableName", tableName);
        request.setAttribute("childTabList", childTabList);
        request.setAttribute("childTabHavingList", childHavingTabList);
    	return mapping.findForward("neighbor");
    }

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		// TODO Auto-generated method stub
		return null;
	}
}
