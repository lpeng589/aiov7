package com.menyi.aio.web.mobile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.koron.oa.bean.OAMyWorkFlowDetBean;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.koron.wechat.common.oauth2.Oauth2;
import com.koron.wechat.common.oauth2.Oauth2ResultBean;
import com.koron.wechat.common.util.WXSetting;
import com.koron.wechat.common.util.WXSettingBase;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.web.util.AIOConnect;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.OperationConst;

/**
 * 
 * <p>
 * Title:工作流转交
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:Nov 5, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class MobileAction extends BaseAction {

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
	 */
	OAMyWorkFlowMgt mgt = new OAMyWorkFlowMgt();

	DynDBManager dyn = new DynDBManager();
	UserMgt userMgt = new UserMgt();
	AIOConnect mobileAjax =new AIOConnect();

	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int operation = getOperation(request);
		ActionForward forward = null;
		forward = auth(request, response,mapping);
		if (forward != null) {
			return forward;
		}
		switch (operation) {
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_AUDITING_PREPARE:
			forward = prepare(mapping, form, request, response);
			break;
		case OperationConst.OP_TABLE_VIEW:
			forward = getForward(request, mapping, "mBody"); //进入mBody界面，所有ajax请求无登陆时都跳转到这个界面，因为进入MobileAction会执行微信自动登陆，如果不是微信则这里会跳至登陆界面
			break;
		default:
			forward = query(mapping, form, request, response);
			break;
		}
		return forward;
	}
	
	protected ActionForward prepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String type = request.getParameter("type");
		if("data".equals(type)){
			return deliverToPrepareData(mapping,form ,request,response);
		} else{
			return deliverToPrepare(mapping,form ,request,response);
		}
		
	}
	
	protected ActionForward deliverToPrepareData(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response){
		String keyId = request.getParameter("keyId");
		String tableName = request.getParameter("tableName");
		
		Map ret = new HashMap();
		ret.put("mainTableName",tableName);
		ret.put("keyId",keyId);
		DBTableInfoBean tableInfo = BaseEnv.tableInfos.get(tableName);
		ret.put("mainTableInfo", tableInfo);
		LoginBean loginBean = this.getLoginBean(request);
		DetailBean bean = mobileAjax.detail( GlobalsTool.getLocale(request).toString(), loginBean,keyId, tableName);
		
		List<OAMyWorkFlowDetBean> flows = bean.getFlowDepict();
		if (bean.getCode() == ErrorCanst.DEFAULT_SUCCESS) {
			ret.put("datavalues", bean.getValues());
			ret.put("childTableList", bean.getChildTableList());
			ret.put("delivers", new Gson().toJson(flows));
			ret.put("fieldInfos", bean.getFieldList());
			//*****获取字段对应名称******//
			HashMap values = bean.getValues();
			Map kv = new HashMap();
			GlobalsTool tool = new GlobalsTool();
			
			for(DBFieldInfoBean item :(ArrayList<DBFieldInfoBean>) bean.getFieldList()){
				kv.put(item.getFieldName(), item.getDisplay().get("zh_CN"));				
			}
			ret.put("fieldNames", kv);
			//**********end********//
			ret.put("childShowField", bean.getChildShowField());
			ret.put("checkRight", bean.isCheckRight());
			ret.put("retCheckRight", bean.isRetCheckRight());
			ret.put("hurryTrans", bean.isHurryTrans());
			ret.put("hasCancel", bean.isHasCancel());
			ret.put("moduleName", tableInfo.getDisplay().get(GlobalsTool.getLocale(request).toString()));
		} else {
			ret.put("ErrorMsg", bean.getMsg());			
		}
		request.setAttribute("data", ret);
		return getForward(request, mapping, "mWorkFlowData");
	}
	
	protected ActionForward deliverToPrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String keyId = request.getParameter("keyId");
		String tableName = request.getParameter("tableName");
		
		//检查自定义文件是否存在
		String file = request.getRealPath("/mobile/define/"+tableName+".jsp");
		if(new File(file).exists()){
			request.setAttribute("defineKeyId", keyId);
			response.sendRedirect("/mobile/define/"+tableName+".jsp?keyId="+keyId+"&tableName="+tableName);
			return getForward(request, mapping, "mWorkFlow");
		}
		
		request.setAttribute("mainTableName", tableName);
		request.setAttribute("keyId", keyId);
		DBTableInfoBean tableInfo = BaseEnv.tableInfos.get(tableName);
		request.setAttribute("mainTableInfo", tableInfo);
		LoginBean loginBean = this.getLoginBean(request);

		boolean isDetail = true;
		

		DetailBean bean = mobileAjax.detail( GlobalsTool.getLocale(request).toString(), loginBean,keyId, tableName);
		if (bean.getCode() == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("datavalues", bean.getValues());
			request.setAttribute("childTableList", bean.getChildTableList());
			request.setAttribute("delivers", bean.getFlowDepict());
			request.setAttribute("fieldInfos", bean.getFieldList());
			request.setAttribute("childShowField", bean.getChildShowField());
			request.setAttribute("checkRight", bean.isCheckRight());
			request.setAttribute("retCheckRight", bean.isRetCheckRight());
			request.setAttribute("hurryTrans", bean.isHurryTrans());
			request.setAttribute("hasCancel", bean.isHasCancel());
			request.setAttribute("moduleName", tableInfo.getDisplay().get(GlobalsTool.getLocale(request).toString()));
		} else {
			request.setAttribute("ErrorMsg", bean.getMsg());
			return getForward(request, mapping, "mWorkFlow");
		}
		
		return getForward(request, mapping, "mWorkFlow");
	} 



	/**
	 * DBTableInfoBean 按tbleName排序
	 */
	public class SortDBTable implements Comparator {
			
		public int compare(Object o1, Object o2) {
			DBTableInfoBean table1 = (DBTableInfoBean) o1;
			DBTableInfoBean table2 = (DBTableInfoBean) o2;

			if (table1 == null || table2 == null) {
				return 0;
			}

			String tableName1 = table1.getTableName();
			String tableName2 = table2.getTableName();

			return tableName1.compareToIgnoreCase(tableName2);
		}
	}

	/**
	 * 我的工作流首页
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
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Result rs;
		ActionForward forwrd;		
		forwrd = getForward(request, mapping, "mWorkFlowList");

		return forwrd;
	}

	protected ActionForward doAuth(HttpServletRequest request,
			ActionMapping mapping) {
		return null;
	}

	public static DBFieldInfoBean cloneObject(DBFieldInfoBean fieldBean) {
		DBFieldInfoBean field = new DBFieldInfoBean();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(fieldBean);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(
					byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			field = (DBFieldInfoBean) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return field;
	}
	/**
	 * 判断是否登录
	 * @param request
	 * @param response
	 * @param mapping
	 * @return
	 */
	private ActionForward auth(HttpServletRequest request,HttpServletResponse response, ActionMapping mapping) {
		// 判断请求是否来自微信 并且启用额微信企业号
		WXSettingBase base = WXSetting.getInstance().getSettingBase(WXSetting.AGENTKEYNAME_WXQY);
		BaseEnv.log.debug("MobileAction.auth flag="+base.getFlag() +":user-agent="+ request.getHeader("user-agent")
				+":state="+request.getParameterValues("state")+":code="+request.getParameterValues("code"));
		if (base.getFlag()&& request.getHeader("user-agent").toLowerCase().indexOf("micromessenger") >= 0) {
			// 获取授权回调后的用户信息
			
			if (request.getParameterValues("state") != null) {
				String[] codes = request.getParameterValues("code");
				if (codes != null && codes.length > 0) {
					String code = codes[codes.length - 1];
					if (code != null && code.length() > 0) {
						BaseEnv.log.debug("微信企业号回调成功");
						Oauth2ResultBean result = new Oauth2(WXSetting.AGENTKEYNAME_WXQY).getUserIdByCode(code);
						BaseEnv.log.debug("微信企业号回调获得userid"+result.getUserId());
						if (result != null && result.getUserId() != null) {
							// 执行自动登录操作
							Result rs = userMgt.login(result.getUserId());
							Message msg = new AIOApi().loginAuth(request, response,rs,result.getUserId()); 
							if(!msg.getCode().equals("OK")){
								//oauth2失败返回到登录页面
								return getForward(request, mapping, "mlogin");
							}
							BaseEnv.log.debug("微信企业号自动登录成功");
						} else {
							// oauth2失败返回到登录页面
							return getForward(request, mapping, "mlogin");
						}

					}
				}
			}
		}
		if (this.getLoginBean(request) == null) {			
			
			// 判断请求是否来自微信 并且启用额微信企业号
			if (base.getFlag()&& (request.getHeader("user-agent").toLowerCase().indexOf("micromessenger") >= 0 || request.getHeader("user-agent").toLowerCase().indexOf("wxwork")>=0) ) {
				String url = request.getRequestURL().toString() + "?"+ request.getQueryString();
				try {
					String rurl = new Oauth2(WXSetting.AGENTKEYNAME_WXQY).getCodeUrl(url,"123");
					BaseEnv.log.debug("微信企业号转鉴权链接+"+rurl);
					response.sendRedirect(rurl);
					return getForward(request, mapping, "blank");
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				return getForward(request, mapping, "mlogin");
			}
		}
		return null;
	}
}
