package com.menyi.aio.web.coldisplay;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.ColDisplayBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GenJS;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
/**
 * 
 * <p>Title:列名显示设置</p> 
 * <p>Description: 该功能主要用于用户可以对表字段进行重命名设置,数据表列表，报表列表的宽度设置</p>
 *
 * @Date:2009-12-17
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class ColDisplayAction extends BaseAction{

	private ColDisplayMgt mgt = new ColDisplayMgt() ;
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		request.setAttribute("popWinName", request.getParameter("popWinName")); //记录用于打开自己的弹出窗的名字，用于关闭自己
		
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD:
			forward = updateColWidth(mapping,form,request,response) ; //列宽设置
			break ;
		case OperationConst.OP_ADD_PREPARE:
			forward = defaultSetting(mapping,form,request,response) ; //缺省设置
			break ;
		case OperationConst.OP_UPDATE_PREPARE:
			forward  = updatePrepare(mapping,form,request,response) ;
			break ;
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * 查询
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
		
		ArrayList list = new ArrayList();

		Hashtable map = (Hashtable) request.getSession().getServletContext()
				.getAttribute(BaseEnv.TABLE_INFO);
		String search = request.getParameter("search") == null ? "" : request
				.getParameter("search");
		if (request.getParameter("LinkType") != null
				&& request.getParameter("LinkType").equals("@URL:")) {
			search = new String(search.getBytes("iso-8859-1"), "UTF-8");
		}

		request.setAttribute("search", search);

		if (map != null) {
			Iterator it = map.values().iterator();
			while (it.hasNext()) {
				DBTableInfoBean tableInfo = (DBTableInfoBean) it.next();
				String parentDisplay = "";
				if (tableInfo.getTableType() == DBTableInfoBean.CHILD_TABLE
						|| tableInfo.getTableType() == DBTableInfoBean.BROTHER_TABLE) {
					String[] parentTableNames = tableInfo.getPerantTableName().split(";");
					for (int i = 0; i < parentTableNames.length; i++) {
						DBTableInfoBean ptb = DDLOperation.getTableInfo(map,parentTableNames[i]);
						if(ptb!=null){
							parentDisplay += ptb.getDisplay() == null ? "" : ptb.getDisplay().get(getLocale(request).toString())+ ";";
						}
					}
				}
				Object[] os = new Object[] {
						tableInfo.getTableName(),
						tableInfo.getDisplay() == null ? "" : tableInfo.getDisplay().get(getLocale(request).toString()),
						tableInfo.getTableType(), tableInfo.getUdType(),
						tableInfo.getUpdateAble(),
						tableInfo.getPerantTableName(), parentDisplay };
				if ("".equals(search)|| tableInfo.getTableName().toUpperCase().contains(search.toUpperCase())
							|| (tableInfo.getDisplay() != null&& tableInfo.getDisplay().get(
										getLocale(request).toString()) != null && tableInfo.getDisplay()
										.get(getLocale(request).toString()).toString()
										.toUpperCase().contains(search.toUpperCase()))) {
					list.add(os);
				}
			}
			request.setAttribute("result",list);
		} else {
			// 查询失败
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");
		}
		GenJS.clearJS();
		return getForward(request, mapping, "tableList");
	}
	
	/**
	 * 修改之前
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

		String search = request.getParameter("search") == null ? "" : request.getParameter("search");
		search = new String(search.getBytes("iso-8859-1"), "UTF-8");
		request.setAttribute("search", search);
		
		String fromConfig = getParameter("fromConfig", request) ;
		request.setAttribute("fromConfig", fromConfig) ;
		ActionForward forward = null;

		String nstr = request.getParameter("keyId");
		if (nstr != null && nstr.length() != 0) {
			Object o = request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			Hashtable tab = (Hashtable) o;
			DBTableInfoBean tib = DDLOperation.getTableInfo(tab, nstr);
			request.setAttribute("result", tib);
			if("config".equals(fromConfig)){
				ArrayList childTableList = DDLOperation.getChildTables(nstr, tab);
				request.setAttribute("childList", childTableList) ;
			}
			
			forward = getForward(request, mapping, "colUpdate");
		}
		request.setAttribute("operation", this.getOperation(request));
		GenJS.clearJS();
		return forward;
	}
	
	/**
	 * 修改之前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward defaultSetting(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String reportNumber = request.getParameter("reportNumber") ;
		String tableName = request.getParameter("tableName") ;
		String colType = request.getParameter("colType") ;
		String allTableName = request.getParameter("allTableName") ;
		String strAction = getParameter("strAction", request) ;
		strAction = strAction.replace("&LinkType=@URL:", "") ;
		
		Result rs = new Result() ;
		if("report".equals(colType)){
			rs = mgt.defaultColWidth(reportNumber,colType,this.getLoginBean(request).getId()) ;
		}else if("bill".equals(colType)){
			rs = mgt.defaultColWidth2(allTableName, colType) ;
		}else if("list".equals(colType)){
			rs = mgt.defaultColWidth(tableName,colType,this.getLoginBean(request).getId()) ;
		}else{
			rs = mgt.defaultColWidth(tableName,colType,"1") ;
		}
		
		if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
			InitMenData init = new InitMenData() ;
			init.initUserColWidth(request.getSession().getServletContext()) ;
		}
		GenJS.clearJS();
		return new ActionForward(strAction) ;
	}
	
	/**
	 * 修改
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
		//String tableName = getParameter("tableName", request) ;
		String userLanguage = getParameter("userLanguage", request) ;
		
		Result rs = mgt.updateTableName(userLanguage, this.getLocale(request).toString());
		if(rs.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add(getMessage(request, "common.msg.updateFailture"))
			.setBackUrl("/ColDisplayAction.do"+"?winCurIndex="+request.getParameter("winCurIndex"))
			.setAlertRequest(request);
		}else{		
			String fromConfig = getParameter("fromConfig", request) ;
			if("config".equals(fromConfig)){
				EchoMessage.error().add(getMessage(
		                request, "common.msg.updateSuccess")).
		                setAlertRequest(request);
			}else{
				EchoMessage.success().add(getMessage(
		                request, "common.msg.updateSuccess"))
		                .setBackUrl("/ColDisplayAction.do"+"?winCurIndex="+request.getParameter("winCurIndex")).
		                setAlertRequest(request);
			}
			GenJS.clearJS();			
		}
		return getForward(request, mapping, "alert"); 
	}
	
	/**
	 * 修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updateColWidth(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String reportNumber = request.getParameter("reportNumber") ;
		String action = request.getParameter("strAction") ;
		String colSelect = request.getParameter("colSelect") ;
		String colType = request.getParameter("colType") ;
		action = action.replace("&LinkType=@URL:", "") ;
		
		if(colSelect!=null && colSelect.length()>0){
			//获取用户自定义列名信息
	        Hashtable<String,ColDisplayBean> userSetWidth = 
	        			(Hashtable<String, ColDisplayBean>) request.getSession().getServletContext().getAttribute("userSettingWidth") ;
			String[] str = colSelect.split(";") ;
			if(str!=null && str.length>0){
				for(String strName : str){
					String[] fields = strName.split(":") ;
					ColDisplayBean display = new ColDisplayBean() ;
					display.setTableName(fields[0]) ;
					display.setColName(fields[1]) ;
					display.setColWidth(fields[2]) ;
					display.setColType(colType) ;
					Result rs = mgt.addUserSetColWidth(display) ;
					if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
						 if("bill".equals(colType)){
							if(fields[1].contains("_")){
								if(userSetWidth!=null){
		                       		ColDisplayBean colBean = userSetWidth.get(fields[0]+fields[1]) ;
		                       		if(colBean!=null){
		                       			colBean.setColWidth(display.getColWidth());
		                       		}else{
		                       			userSetWidth.put(fields[0]+fields[1], display) ;
		                       		}
		                       }
							}else{
								Object o = request.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
								Hashtable tab = (Hashtable) o;
								DBTableInfoBean tib = DDLOperation.getTableInfo(tab, fields[0]); 
								if(tib!=null){
									ArrayList<DBFieldInfoBean> fieldInfo = (ArrayList<DBFieldInfoBean>) tib.getFieldInfos() ;
									for(int j=0;j<fieldInfo.size();j++){
										DBFieldInfoBean field = fieldInfo.get(j) ;
										if(field.getFieldName().equals(fields[1])){
											field.setWidth(Integer.parseInt(fields[2])) ;
										}
									}
								}
							}
						}else{
							if(userSetWidth!=null){
	                       		ColDisplayBean colBean = userSetWidth.get(fields[0]+fields[1]) ;
	                       		if(colBean!=null){
	                       			colBean.setColWidth(display.getColWidth());
	                       		}else{
	                       			userSetWidth.put(fields[0]+fields[1], display) ;
	                       		}
	                       }
						}
					}
				}
			}
		}
		GenJS.clearJS();
		return new ActionForward(action) ;
	}

	/**
	 * 不做权限控制
	 */
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		return null;
	}
	
}
