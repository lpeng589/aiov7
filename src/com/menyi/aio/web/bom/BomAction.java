package com.menyi.aio.web.bom;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.BomBean;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.billNumber.BillNo;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.billNumber.BillNo.BillNoUnit;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.role.RoleForm;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

/**
 * 
 * <p>Title:BOM单</p> 
 * <p>Description: </p>
 *
 * @Date:2012-5-14
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class BomAction extends MgtBaseAction{

	private BomMgt bomMgt = new BomMgt() ;
	private String moduleType="";
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	protected ActionForward doAuth(HttpServletRequest req,ActionMapping mapping) {
		LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
        return null;
	}
	
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		moduleType=request.getParameter("moduleType");
		request.setAttribute("moduleType", moduleType);
		//跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD: 
			break;
		//新增前的准备
		case OperationConst.OP_ADD_PREPARE: 
			break;
		//修改前的准备
		case OperationConst.OP_UPDATE_PREPARE: 
			break;
		//修改
		case OperationConst.OP_UPDATE:		 
			break;  
		//默认查询
		default:
			String type = request.getParameter("type");
			if(type != null && type.equals("getChild")){
				forward = getChild(mapping, form, request, response);
			}else if(type != null && type.equals("getReplace")){
				forward = getReplace(mapping, form, request, response);
			}
		} 
		return forward;
	}
	

	private ActionForward getReplace(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String bomDetId = request.getParameter("bomDetId");
		Result rs=bomMgt.getReplace(bomDetId) ;
		HashMap map = new HashMap();
		map.put("Code", "Error");
		String msgStr = gson.toJson(map);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)rs.retVal).size()> 0 ){
			 msgStr = gson.toJson(((ArrayList)rs.retVal).get(0));
		}
		request.setAttribute("msg", msgStr);
		return getForward(request, mapping, "blank");
	}
	private ActionForward getChild(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String good = request.getParameter("goods");
		String goods[] = good.split(",");
		HashMap map=bomMgt.getChild(goods) ;
		String msgStr = gson.toJson(map);
		request.setAttribute("msg", msgStr);
		return getForward(request, mapping, "blank");
	}

}
