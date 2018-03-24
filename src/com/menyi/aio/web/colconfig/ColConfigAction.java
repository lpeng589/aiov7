package com.menyi.aio.web.colconfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dbfactory.Result;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GenJS;
import com.menyi.web.util.InitMenData;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.PopSelectConfig;

public class ColConfigAction extends BaseAction{

	private ColConfigMgt mgt = new ColConfigMgt() ;
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD:
			forward = add(mapping, form, request, response);
			break;
		default:
			forward = defaults(mapping, form, request, response);
		}
		return forward;
	}
	
	/**
	 * 默认设置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward defaults(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		String colType = getParameter("colType", request) ;
		String tableName = getParameter("tableName", request) ;
		String allTableName = getParameter("allTableName", request) ;
		String reportNumber = getParameter("reportNumber", request) ;
		String popupName = getParameter("popupName", request) ;
		String strAction = getParameter("strAction", request) ;
		strAction = strAction.replace("&LinkType=@URL:", "") ;
		if("popup".equals(colType)){//弹出窗口
			mgt.delByTableName(popupName, colType,"1") ;
		}else if("report".equals(colType)){//报表中心
			mgt.delByTableName(reportNumber, "list",this.getLoginBean(request).getId()) ;
		}else if("list".equals(colType)){//单据列表
			mgt.delByTableName(tableName, colType,this.getLoginBean(request).getId()) ;
		}else{//单据增加
			mgt.delByTableNameAndColType(allTableName, colType) ;
		}
		new InitMenData().initUserColConfig(request.getSession().getServletContext()) ;
		PopSelectConfig.parseConfig() ;
		/*清除js*/
		//String jsKey = "/js/gen/define.vjs"+tableName+"_"+getLocale(request)+".js" ;
		//BaseEnv.jsMap.clear() ;
		GenJS.clearJS();
		return new ActionForward(strAction) ;
	}

	/**
	 * 添加
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
		
		String userId = getLoginBean(request).getId() ;//用户ID
		String tableName = getParameter("tableName", request) ; //表名
		String allTableName = getParameter("allTableName", request) ;
		String reportNumber = getParameter("reportNumber", request) ;
		String colType = getParameter("colType", request) ;//类型
		String popupName = getParameter("popupName", request) ;
		String action = getParameter("strAction", request) ;//连接
		String colNames = getParameter("colSelect", request) ; //列名
		action = action.replace("&LinkType=@URL:", "") ;
		if("bill".equals(colType)){//单据形式
			if(colNames!=null){
				Result rs_del = mgt.delByTableNameAndColType(allTableName, colType) ;
				if(rs_del.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					String[] colSelect = colNames.split(",") ;
					for(int i=0;i<colSelect.length;i++){
						String[] str = colSelect[i].split(":") ;
						mgt.add(str[1],str[2],str[3], str[0],"",Integer.parseInt(str[4]), colType, i+1,userId) ;
					}
					new InitMenData().initUserColConfig(request.getSession().getServletContext()) ;
				}
			}
		}else if("list".equals(colType)){//单据报表形式
			if(colNames!=null){
				Result rs_del = mgt.delByTableName(tableName, colType,this.getLoginBean(request).getId()) ;
				if(rs_del.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					String[] colSelect = colNames.split(",") ;
					for(int i=0;i<colSelect.length;i++){
						mgt.add(colSelect[i],colSelect[i],colSelect[i], tableName,"",0, colType, i+1,userId) ;
					}
					new InitMenData().initUserColConfig(request.getSession().getServletContext()) ;
				}
			}
		}else if("report".equals(colType)){//报表中心
			if(colNames!=null){
				Result rs_del = mgt.delByTableName(reportNumber, "list",this.getLoginBean(request).getId()) ;
				if(rs_del.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					String[] colSelect = colNames.split(",") ;
					for(int i=0;i<colSelect.length;i++){
						mgt.add(colSelect[i], colSelect[i],colSelect[i],reportNumber,"",0, "list", i+1,userId) ;
					}
					new InitMenData().initUserColConfig(request.getSession().getServletContext()) ;
				}
			}
		}else{//弹出窗口形式
			if(colNames!=null){
				Result rs_del = mgt.delByTableName(popupName, colType,"1") ;
				if(rs_del.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
					String[] colSelect = colNames.split("],"); 
					for(int i=0;i<colSelect.length;i++){
						mgt.add(colSelect[i],colSelect[i],colSelect[i], tableName, popupName,0, colType, i+1, userId) ;
					}
					new InitMenData().initUserColConfig(request.getSession().getServletContext()) ;
				}
			}
			PopSelectConfig.parseConfig() ;
		}
		/*清除js*/
		//String jsKey = "/js/gen/define.vjs"+tableName+"_"+getLocale(request)+".js" ;
		//BaseEnv.jsMap.clear() ;
		GenJS.clearJS();
		return new ActionForward(action) ;
	}

	/**
	 * 列配置不做权限控制
	 */
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		return null;
	}
}
