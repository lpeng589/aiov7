package com.menyi.web.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;

/**
 * 
 * <p>
 * Title: ������action
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: ������
 * </p>
 * 
 * @author ������
 * @version 1.0
 * @preserve all
 */

public abstract class BaseAction extends Action {

	private static final int OPERATION_ADD = 0;

	private static final int OPERATION_UPDATE = 1;

	private static final int OPERATION_DELETE = 2;

	private static String process = Thread.currentThread().getName();

	private static String hostName = null;

	private long timePoint = 0;

	private long access = 0;

	protected Logger log = BaseEnv.log;

	/**
	 * ��ȡ��������,��ҳ��form�б�����һ��operation�����Ա�ʾ����������
	 * 
	 * @param form
	 *            ActionForm
	 * @return int
	 */
	protected int getOperation(HttpServletRequest req) {
		String operation = req.getParameter("operation");
		Object attribute_operation = req.getAttribute("operation");

		if (attribute_operation != null) {
			operation = attribute_operation.toString();
		}

		try {
			return Integer.parseInt(operation);
		} catch (Exception ex) {
			return 0;
		}
	}



	/**
	 * ִ��Action,����Ա��д��action�಻Ӧ���ش��ࡣ��Ӧ��ʵ��exec��������˷���ʵ���˺ܶ����ͨ�ù���
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
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String ip=request.getHeader("Referer");
        if(ip!=null && ip.length() > 0){ //��δ��뱾���Ƿ���login�еĵ���KK�򿪵���Ϣû��Referer���Է�������ÿ�ζ�����IP����ȫһ��
            String port="";
            ip=ip.substring(ip.indexOf("http://")+"http://".length());
            if(ip.indexOf(":")>0 && ip.indexOf(":") < ip.indexOf("/")){    
            	port=ip.substring(ip.indexOf(":")+1,ip.indexOf("/", ip.indexOf(":")));
            	ip=ip.substring(0,ip.indexOf(":"));
            }else if(ip.indexOf("/")>0){
            	ip=ip.substring(0,ip.indexOf("/"));
            }
            if(port!=null && port.length()==0){
            	port = "80" ;
            }
            request.getSession().setAttribute("IP", ip);
            request.getSession().setAttribute("port",port);
        }
		
		//��¼�����������־ getParameterMap() getQueryString() getRequestURI()
		if(BaseEnv.log.isDebugEnabled()){
			if(!request.getRequestURI().contains("/LogManageAction") && (request.getQueryString() == null || !request.getQueryString().contains("tradeAjax")) ){
				String uName = "";
				if(this.getLoginBean(request) != null){
					uName = this.getLoginBean(request).getEmpFullName();
				}
				BaseEnv.log.debug("BaseAction.execute �����ַ��"+request.getRequestURI()+(request.getQueryString()==null?"":"?"+request.getQueryString()));
				String rd ="";
				for(Object key:request.getParameterMap().keySet()){
					Object value=request.getParameterMap().get(key);
					String values="";
					for(String v:(String[])value){
						values +=v+",";
					}
					rd +=key+"=["+values+"];";
				}
			
				BaseEnv.log.debug("BaseAction.execute ������:"+uName+";"+"�������ݣ�"+rd);
			}
			
		}
		ActionForward forward;

		// ��ϵͳ����ʱ
		if (SystemState.instance.dogState == SystemState.DOG_RESTART) {
			return getForward(request, mapping, "indexPage");
		}

		// �ж�ϵͳ�Ƿ���ס
		if (OnlineUserInfo.lockState()) {
			return getForward(request, mapping, "lockMessage");
		}

		// ���ܹ�����Ҫת����ҳ��������У���������ҳ
		if (!(request.getRequestURI().endsWith("/UpgradeAction.do") && "true"
				.equals(request.getParameter("exig")))
				&& !request.getRequestURI().endsWith("/RegisterAction.do")) {
			if (SystemState.instance.lastErrorCode != SystemState.NORMAL
					|| (SystemState.instance.dogState != SystemState.DOG_FORMAL && SystemState.instance.dogState != SystemState.DOG_EVALUATE)) {
				return getForward(request, mapping, "fatalAlert");
			}
		}
		// �Ƿ��Ǳ�������
		String LinkType = getParameter("LinkType", request);
		if ("@URL:".equals(LinkType)) {
			request.setAttribute("LinkType", LinkType);
			request.setAttribute("isLinkType", "true");
		}
		/**
		 * ������в���NOTTOKEN,ָ����ϣ�������ظ��ύ���� �������ύǰ�����е���������Ҫ�����ظ��ύ�����������ύ���޷�����
		 */
		/*
		 * if((request.getParameter("NOTTOKEN")) == null){ if
		 * ((request.getParameter("org.apache.struts.taglib.html.TOKEN")) !=
		 * null) { //�ظ��ύ�ж� if (!isTokenValid(request, true)) {
		 * BaseEnv.log.debug("---------�ظ��ύ-" + request.getRequestURI() +
		 * "?operation=" + request.getParameter("operation"));
		 * saveToken(request); forward = getDefaultAction(request, mapping);
		 * BaseEnv.log.debug("-------forward=" + forward); return forward; } }
		 * if (getOperation(request) != OperationConst.OP_POPUP_SELECT) {
		 * saveToken(request); } }
		 */
		// ����ǲ˵��������session�е�form��Ϣȥ��
		// ÿ�����˵���Ҫ�Ӳ���src=menu����������Ϊ���û������˵����л�ʱ�����ϵͳSession������
		// ��������˲������������session
		String src = request.getParameter("src");
		if ("menu".equals(src)
				&& !"22".equals(request.getParameter("operation"))) {
			String curReqUrl = request.getRequestURI() + "?"
					+ request.getQueryString();
			curReqUrl = curReqUrl == null ? "" : java.net.URLDecoder.decode(
					curReqUrl, "UTF-8");
			if (curReqUrl.indexOf("src=menu") > 0) {
				curReqUrl = curReqUrl.substring(0, curReqUrl
						.indexOf("src=menu") - 1);
			}
			String winCurIndex = request.getParameter("winCurIndex");
			if (winCurIndex != null && !winCurIndex.equals("-1")) {
				HashMap winIndex = (HashMap) request.getSession().getAttribute(
						BaseEnv.WIN_MAP);
				if (winIndex == null) {
					winIndex = new HashMap();
					request.getSession()
							.setAttribute(BaseEnv.WIN_MAP, winIndex);
				}
				winIndex.put(winCurIndex, curReqUrl);
				BaseEnv.log.debug("---------->SET Page " + winCurIndex
						+ " DEFAULT URL = " + curReqUrl);
			} else {
				request.getSession().setAttribute(BaseEnv.CUR_INDEX_URL,
						curReqUrl);
				BaseEnv.log.debug("---------->SET DEFAULT URL = " + curReqUrl);
			}

			// ���form�� ����ʹ�öര�ڣ����ﲻ����������лỰ��Ϣ��ֻ�����ǰ��form����
			try {
				if (form != null) {
					form = (ActionForm) form.getClass().newInstance();
				}
			} catch (Exception ex) {
			}
			Enumeration enu = request.getSession().getAttributeNames();
			while (enu.hasMoreElements()) {
				String name = (String) enu.nextElement();
				Object obj = request.getSession().getAttribute(name);
				if (obj instanceof ActionForm) {
					request.getSession().removeAttribute(name);
					request.removeAttribute(name);
				}
			}
		}
		// �ര��
		String winIndex = getParameter("winCurIndex", request);
		request.setAttribute("winCurIndex", winIndex);

		String moduleType = getParameter("moduleType", request);
		request.setAttribute("moduleType", moduleType);
		
		/*�ж��Ƿ��ǹȸ��ƻ�������*/
		String userAgent = request.getHeader("user-agent");
		if(!(userAgent.toLowerCase().indexOf("ipad")!=-1 
					|| userAgent.toLowerCase().indexOf("iphone")!=-1)
					|| userAgent.toLowerCase().indexOf("Chrome")!=-1
					|| userAgent.toLowerCase().indexOf("Safari")!=-1){
			//forward = getForward(request, mapping, "browser");
			//return forward;
		}
		// Ȩ�޿���,�û���ʵ��doAuth������������Ȩ�޿��ƣ�doAuth
		forward = doAuth(request, mapping);
		if (forward != null) {
			return forward;
		}
		
		// ִ����Ӧ���߼�
		try {
			forward = exe(mapping, form, request, response);
		} catch (BusinessException ex) {
			String msg = ex.key;
			if (ex.key == null || ex.key.length() == 0) {
				msg = "common.msg.error";
			}
			msg = this.getMessage(request, msg);
			if (msg == null || msg.length() == 0) {
				msg = ex.key;
			}
			EchoMessage.error().add(msg).setBackUrl(ex.backUrl)
					.setAlertRequest(request);
			forward = getForward(request, mapping, "alert");
		} catch (Exception ex) {
			BaseEnv.log.error("BaseAction---execute", ex);
			EchoMessage.error().setAlertRequest(request);
			forward = getForward(request, mapping, "alert");
		}

		// ���������ת��Ĭ��ҳ��
		if (forward == null) {
			// ���ش���ҳ
			EchoMessage.error().setAlertRequest(request);
			forward = getForward(request, mapping, "alert");
		}
		return forward;

	}

	/**
	 * Form����
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param request
	 *            HttpServletRequest
	 */
	public void resetForm(ActionMapping mapping, HttpServletRequest request) {
		String formName = mapping.getName();
		request.getSession().removeAttribute(formName);
	}

	/**
	 * ��������
	 * 
	 * @return boolean
	 */
	private boolean fluxControl() {
		if (timePoint == 0) {
			timePoint = System.currentTimeMillis();
			access = 0;
		}
		access++;
		if (access > 100) {
			return false;
		}
		long curTime = System.currentTimeMillis();
		if ((curTime - timePoint) > 1000) {
			timePoint = curTime;
			access = 0;
		}
		return true;
	}

	/**
	 * ��ȡActionForward,���û�Ҫ����ת·�������⴦��ɵ��ô˷���
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param mapping
	 *            ActionMapping
	 * @param key
	 *            String
	 * @return ActionForward
	 */
	protected ActionForward getForward(HttpServletRequest req,
			ActionMapping mapping, String key) {
		return mapping.findForward(key);
	}

	/**
	 * ��ȡ��Դ�ļ�
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param key
	 *            String
	 * @return String
	 */
	public String getMessage(HttpServletRequest req, String key) {
		try {
			MessageResources mr = this.getResources(req, "userResource");
			if (mr != null) {
				// ����ȡ�û��Զ�����Դ�ļ�
				String str = mr.getMessage(getLocale(req), key);
				if (str != null) {
					return str;
				}
			}
		} catch (Exception e) {
		}

		MessageResources mr = this.getResources(req);
		return mr.getMessage(getLocale(req), key);
	}

	/**
	 * struts��ȡ����
	 * 
	 * @param req
	 * @param key
	 * @return
	 */
	public String getMessage2(HttpServletRequest req, String key) {
		Object o = req.getSession().getServletContext().getAttribute(
				org.apache.struts.Globals.MESSAGES_KEY);
		if (o instanceof MessageResources) {
			MessageResources mr = (MessageResources) o;
			return mr.getMessage(getLocale(req), key);
		}
		return "";
	}

	/**
	 * ��ȡ��Դ�ļ�
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param key
	 *            String
	 * @param param1
	 *            String
	 * @return String
	 */
	public String getMessage(HttpServletRequest req, String key, String param1)

	{
		try {
			MessageResources mr = this.getResources(req, "userResource");
			if (mr != null) {
				// ����ȡ�û��Զ�����Դ�ļ�
				String str = mr.getMessage(getLocale(req), key, param1);
				if (str != null) {
					return str;
				}
			}
		} catch (Exception e) {
		}
		MessageResources mr = this.getResources(req);
		return mr.getMessage(getLocale(req), key, param1);
	}

	/**
	 * ��ȡ��Դ�ļ�
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param key
	 *            String
	 * @param param1
	 *            String
	 * @return String
	 */
	public String getMessage(HttpServletRequest req, String key, String param1,
			String param2)

	{
		try {
			MessageResources mr = this.getResources(req, "userResource");
			if (mr != null) {
				// ����ȡ�û��Զ�����Դ�ļ�
				String str = mr.getMessage(getLocale(req), key, param1, param2);
				if (str != null) {
					return str;
				}
			}
		} catch (Exception e) {
		}
		MessageResources mr = this.getResources(req);
		return mr.getMessage(getLocale(req), key, param1, param2);
	}

	/**
	 * ��ȡ��Դ�ļ�
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param key
	 *            String
	 * @param param1
	 *            String
	 * @return String
	 */
	public String getMessage(HttpServletRequest req, String key, String param1,
			String param2, String param3)

	{
		try {
			MessageResources mr = this.getResources(req, "userResource");
			if (mr != null) {
				// ����ȡ�û��Զ�����Դ�ļ�
				String str = mr.getMessage(getLocale(req), key, param1, param2,
						param3);
				if (str != null) {
					return str;
				}
			}
		} catch (Exception e) {
		}
		MessageResources mr = this.getResources(req);
		return mr.getMessage(getLocale(req), key, param1, param2, param3);
	}

	/**
	 * ��form�����ݶ���bean��
	 * 
	 * @param form
	 *            ActionForm
	 * @param bean
	 *            Object
	 */
	protected Object read(ActionForm form, Class beanClass) {
		try {
			Object o = beanClass.newInstance();
			org.apache.commons.beanutils.PropertyUtils.copyProperties(o, form);
			return o;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * ��form�����ݶ���bean��
	 * 
	 * @param form
	 *            ActionForm
	 * @param bean
	 *            Object
	 */
	protected void read(ActionForm form, Object bean) {
		try {
			org.apache.commons.beanutils.PropertyUtils.copyProperties(bean,
					form);
		} catch (Exception ex) {
		}
	}

	/**
	 * ��ִ��ɾ������д��־ʱ����ӡID[]
	 * 
	 * @param bean
	 *            Object
	 * @param form
	 *            ActionForm
	 */
	protected String parseIdToList(String[] id) {
		String result = "";
		ArrayList al = new ArrayList();
		for (int i = 0; i < id.length; i++) {
			al.add(id[i]);
		}
		result = al.toString();
		result = result.replaceAll(",", ";");
		return result;
	}

	/**
	 * ��ִ�в�ѯ����д��־ʱ����ӡParams[]
	 * 
	 * @param bean
	 *            Object
	 * @param form
	 *            ActionForm
	 */
	protected String parseParamsToList(Object[] params) {
		String result = "";
		ArrayList al = new ArrayList();
		for (int i = 0; i < params.length; i++) {
			al.add(params[i]);
		}
		result = al.toString();
		result = result.replaceAll(",", ";");
		return result;

	}

	/**
	 * ��bean������д��form��
	 * 
	 * @param bean
	 *            Object
	 * @param form
	 *            ActionForm
	 */
	protected void write(Object bean, ActionForm form) {
		try {
			org.apache.commons.beanutils.PropertyUtils.copyProperties(form,
					bean);
		} catch (Exception ex) {
		}
	}

	protected LoginBean getLoginBean(HttpServletRequest req) {
		Object o = req.getSession().getAttribute("LoginBean");
		if (o != null) {
			return (LoginBean) o;
		}
		return null;
	}

	protected Locale getDefaultLocale(HttpServletRequest req) {
		return (Locale) req.getSession().getServletContext().getAttribute(
				"DefaultLocale");
	}

	public List getEnumerationItems(String enumeration, HttpServletRequest req) {
		List list = new ArrayList();
		Map map = BaseEnv.enumerationMap;
		EnumerateBean beans[] = (EnumerateBean[]) map.values().toArray(
				new EnumerateBean[0]);
		for (int i = 0; i < beans.length; i++) {
			if (beans[i].getEnumName().equals(enumeration)) {
				for (int j = 0; j < beans[i].getEnumItem().size(); j++) {
					EnumerateItemBean eib = (EnumerateItemBean) beans[i]
							.getEnumItem().get(j);
					if ("MainModule".equals(enumeration)) {
						// Ϊģ��ö��ʱ�Ӽ��ܹ���ȡ
						if (!eib.getEnumValue().equals("0")
								&& !GlobalsTool.hasMainModule(eib
										.getEnumValue())) {
							continue;
						}
					}

					KeyPair kp = new KeyPair();
					kp.setName(((KRLanguage) (eib.getDisplay())).get(getLocale(
							req).toString()));
					kp.setValue(eib.getEnumValue());
					list.add(kp);

				}
				break;
			}
		}
		return list;
	}

	/**
	 * ��ȡ��ǰģ���һ��ҳ��ĵ�ַ��Ϣ ÿ��ģ��ĵ�һ��ҳ����Ĭ�ϵĵ�ַ�������Ϣ�����棬��ϵͳ������Ҫ���ص�һ��ҳ��ʱʹ��
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @return ActionForward
	 */
	protected ActionForward getDefaultAction(HttpServletRequest req,
			ActionMapping mapping) {
		ActionForward forward = null;
		String url = (String) req.getSession().getAttribute(
				BaseEnv.CUR_INDEX_URL);
		if (url != null && url.length() != 0) {
			// ���Session�е�form����Session�� form

			Enumeration enu = req.getSession().getAttributeNames();
			while (enu.hasMoreElements()) {
				String name = (String) enu.nextElement();
				Object obj = req.getSession().getAttribute(name);
				if (obj instanceof ActionForm) {
					req.getSession().removeAttribute(name);
				}
			}
			forward = new ActionForward(url);
		}
		// ����ָ����ԭʼurl
		else {
			BaseEnv.log
					.debug("BaseAction.getDefaultAction() indexPage---------- ");
			forward = getForward(req, mapping, "indexPage");
		}
		return forward;
	} 

	public void readMainTable(HashMap values, DBTableInfoBean tableInfo,
			HttpServletRequest req) {
		List list = tableInfo.getFieldInfos();
		for (int i = 0; i < list.size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) list.get(i);
			String str = "";
			if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX || 
					(fieldInfo.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_CHECKBOX)) {
				String[] checkbox = req.getParameterValues(fieldInfo.getFieldName());
				if (checkbox != null) {
					for (String string : checkbox) {
						if(string.length()>0)
							str += string + ",";
					}
				}
				str = str.replaceAll(",,", ",");
			} else if(fieldInfo.getFieldType() == DBFieldInfoBean.FIELD_AFFIX
					|| fieldInfo.getFieldType() == DBFieldInfoBean.FIELD_PIC){
				String[] affix = req.getParameterValues(fieldInfo.getFieldName());
				if (affix != null) {
					for (String string : affix) {
						str += string + ";";
					}
				}
			} else {
				str = req.getParameter(fieldInfo.getFieldName());
			}
			if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE|| 
					(fieldInfo.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
				if (str != null && str.length() > 0) {
					values.put(fieldInfo.getFieldName(),
						KRLanguageQuery.create((Hashtable) req.getSession().getServletContext().getAttribute("LocaleTable"),
						(Locale) req.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY),str));
				} else {
					values.put(fieldInfo.getFieldName(), null);
				}
			} else {
				values.put(fieldInfo.getFieldName(), str);
			}
		}
	}

	public void readChildTable(HashMap values, DBTableInfoBean tableInfo,
			HttpServletRequest req, String sunCompany) {
//		Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(this.getLoginBean(req)) ;
		
		ArrayList childList = new ArrayList();
		values.put("TABLENAME_" + tableInfo.getTableName(), childList);
		List list = tableInfo.getFieldInfos();
		// �ҳ���¼����

		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) list.get(i);
			if ((fieldInfo.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE || fieldInfo
					.getFieldType() == DBFieldInfoBean.FIELD_INT)
					&& (fieldInfo.getFieldSysType() == null || (fieldInfo
							.getFieldSysType() != null && !fieldInfo
							.getFieldSysType().equals("RowMarker")))) {
				continue;
			}else if (fieldInfo.getDefaultValue() != null
					&& fieldInfo.getDefaultValue().trim().length() > 0) {
				//��ϸ��Ĭ��ֵ��������ǽ���ֶΣ�������ж�ûʲô����
				String defv = fieldInfo.getDefaultValue();
//        		if(defv.contains("@Sess:")){
//        			String name = defv.substring(defv.indexOf("@Sess:") + "@Sess:".length());
//        			if(name.indexOf(";") > 0){
//        				name = name.substring(0,name.indexOf(";"));
//        			}
//        		}else if(defv.contains("@Date:")){
//        			
//        		}
				//��������Σ�ȡ���һ�в�����Ĭ��ֵ��������Ϊ��������
				if (fieldInfo.getFieldType() == DBFieldInfoBean.FIELD_DOUBLE || fieldInfo
						.getFieldType() == DBFieldInfoBean.FIELD_INT){
					String[] str = req.getParameterValues(tableInfo.getTableName()
							+ "_" + fieldInfo.getFieldName());
					int c = 0;
					if(str != null){
						for (int j = str.length -1; str != null && j >=0 ; j--) {
							if (str[j] != null && str[j].trim().length() > 0
									&& !str[j].equals(defv)) {
								c = j + 1;
								break;
							}
						}
					}
					if (c > count)
						count = c;					
				}
				
				
				// ��Ĭ��ֵ�Ĳ��������
				continue;
			} else if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX|| 
					(fieldInfo.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_CHECKBOX)) {
				continue;
			}  
			String[] str = req.getParameterValues(tableInfo.getTableName()
					+ "_" + fieldInfo.getFieldName());
			int c = 0;
			if(str != null){
				for (int j = str.length -1; str != null && j >=0 ; j--) {
					if (str[j] != null && str[j].trim().length() > 0
							&& !"0".equals(str[j])
							&& !str[j].trim().equals("-100000")) {
						c = j + 1;
						break;
					}
				}
			}
			if (c > count)
				count = c;
		}

		for (int i = 0; i < count; i++) {
			childList.add(new HashMap());
		}

		for (int i = 0; i < list.size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) list.get(i);
			String[] str;
			if (fieldInfo.getFieldName().equals("SCompanyID")) {
				str = new String[count];
				for (int j = 0; j < count; j++) {
					str[j] = sunCompany;
				}
			} else {
				str = req.getParameterValues(tableInfo.getTableName() + "_"
						+ fieldInfo.getFieldName());
			}
			if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_CHECKBOX
					|| (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fieldInfo
							.getInputTypeOld() == DBFieldInfoBean.INPUT_CHECKBOX)) {
				for (int j = 0; j < count; j++) {
					String[] checkValue = req.getParameterValues(tableInfo
							.getTableName()
							+ "_" + fieldInfo.getFieldName() + (j + 1));
					String check = "";
					if (checkValue != null) {
						for (String strs : checkValue) {
							check += strs + ",";
						}
					}
					((HashMap) (childList.get(j))).put(
							fieldInfo.getFieldName(), check);
				}
			} else {
				for (int j = 0; j < count; j++) {
					if(str != null && str.length<count){
						System.out.println("");
					}
					if (str != null && str[j] != null) {
						if (fieldInfo.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE|| 
								(fieldInfo.getInputType() == DBFieldInfoBean.INPUT_HIDDEN_INPUT && fieldInfo.getInputTypeOld() == DBFieldInfoBean.INPUT_LANGUAGE)) {
							// ������
							((HashMap) (childList.get(j)))
									.put(
											fieldInfo.getFieldName(),
											KRLanguageQuery.create((Hashtable) req.getSession().getServletContext().getAttribute("LocaleTable"),
															(Locale) req.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY),
															str[j]));
						} else {
							if (str[j].length() == 0
									&& (fieldInfo.getFieldType() == 1 || fieldInfo
											.getFieldType() == 0)) {
								if (fieldInfo.getDefaultValue() != null
										&& fieldInfo.getDefaultValue().length() > 0) {
									((HashMap) (childList.get(j))).put(
											fieldInfo.getFieldName(), fieldInfo
													.getDefaultValue());
								} else {
									((HashMap) (childList.get(j))).put(
											fieldInfo.getFieldName(), 0);
								}
							} else {
								((HashMap) (childList.get(j))).put(fieldInfo
										.getFieldName(), str[j]);
							}
						}
					} else {
						if (fieldInfo.getFieldType() == 1
								|| fieldInfo.getFieldType() == 0) {
							if (fieldInfo.getDefaultValue() != null
									&& fieldInfo.getDefaultValue().length() > 0) {
								((HashMap) (childList.get(j))).put(fieldInfo
										.getFieldName(), fieldInfo
										.getDefaultValue());
							} else {
								((HashMap) (childList.get(j))).put(fieldInfo
										.getFieldName(), 0);
							}
						}
					}
				}
			}
		}
	}
	
	protected String pageBar(Result rs, HttpServletRequest req) {
		return pageBar(rs,req,"");
	}

	/**
	 * ������ҳ������ҳ����ֱ��ʹ�÷�ҳ����ǩ����ֱ�Ӳ�����ҳHTML���롣���ٹ���
	 * 
	 * @param rs
	 *            Result ���ݿ�ӿڷ��ؽ��
	 * @param req
	 *            HttpServletRequest
	 * @return String
	 */
	protected String pageBar(Result rs, HttpServletRequest req,String listType) {
		String str = "";

		if (rs.realTotal == 0) {
			str = "<div>" + getMessage(req, "common.theNo") + "" + (rs.pageNo)
					+ "" + getMessage(req, "common.page")
					+ "&nbsp;&nbsp;</div>";
		} else {
			str = "<div>" + getMessage(req, "common.theNo") + ""
					+ (rs.totalPage > 0 ? rs.pageNo : 0) + "/" + rs.totalPage
					+ "" + getMessage(req, "common.page") + "&nbsp;("
					+ rs.getRealTotal() + ")&nbsp;&nbsp;</div>";
		}
		req.setAttribute("totalRow", rs.getRealTotal());
		String operation = getParameter("operation", req);
		if ("3".equals(operation) || "2".equals(operation)) {
			operation = OperationConst.OP_QUERY + "";
		}

		int pageSize = rs.pageSize;
		String pageSizeStr = "";
		if (pageSize != 15 && pageSize != 30 && pageSize != 50
				&& pageSize != 100 && pageSize != 500 && pageSize != 1000
				&& pageSize != 5000) {
			pageSizeStr = "<option value=" + pageSize + " selected " + ">"
					+ pageSize + "</option>";
		}

		str += "<select id=\"pageSize\" name=\"pageSize\" onChange=\"document.forms[0].operation.value='"
				+ operation
				+ "';document.forms[0].target = '';document.forms[0].submit();\">"
				+ pageSizeStr
				+ "<option value=15 "
				+ (rs.pageSize == 15 ? "selected" : "")
				+ ">15</option>"
				+ "<option value=30 "
				+ (rs.pageSize == 30 ? "selected" : "")
				+ ">30</option>"
				+ "<option value=50 "
				+ (rs.pageSize == 50 ? "selected" : "")
				+ ">50</option>"
				+ "<option value=100 "
				+ (rs.pageSize == 100 ? "selected" : "")
				+ ">100</option>";
		if(!"popupSelect".equals(listType)){
			str +=		
				 "<option value=500 "
				+ (rs.pageSize == 500 ? "selected" : "")
				+ ">500</option>"
				+ "<option value=1000 "
				+ (rs.pageSize == 1000 ? "selected" : "")
				+ ">1000</option>" ;
		}
		
		str +=	"</select>";
		// ��ҳ
		if (rs.pageNo > 1) {
			str += ""
					+ "<a class=page_a href=\"javascript:document.getElementById('pageNo').value=1;"
					+ "document.forms[0].operation.value='"
					+ operation
					+ "';document.forms[0].target = '';document.forms[0].submit();\">"
					+ "<span class='p_first'></span></a>";

		} else {
			str += "<span class='p_first2'></span>";
		}

		// ��һҳ
		if (rs.pageNo > 1) {

			str += "<a class=page_a href=\"javascript:document.getElementById('pageNo').value="
					+ (rs.pageNo - 1)
					+ ";document.forms[0].operation.value='"
					+ operation
					+ "';document.forms[0].target = '';"
					+ "document.forms[0].submit();\">"
					+ "<span class='p_prep'></span></a>";
		} else {
			str += "<span class='p_prep2'></span>";
		}

		if (rs.pageNo < rs.totalPage) {
			str += "<a class=page_a href=\"javascript:document.getElementById('pageNo').value="
					+ (rs.pageNo + 1)
					+ ";document.forms[0].operation.value='"
					+ operation
					+ "';document.forms[0].target = '';document.forms[0].submit();\">"
					+ "<span class='p_next'></span></a>";
		} else {
			str += "<span class='p_next2'></span>";
		}

		if (rs.totalPage > 0) {
			// βҳ
			if (rs.totalPage > 1 && rs.pageNo < rs.totalPage
					&& rs.realTotal > 0) {
				str += "<a class=page_a href=\"javascript:document.getElementById('pageNo').value="
						+ rs.totalPage
						+ ";document.forms[0].operation.value='"
						+ operation
						+ "';"
						+ "document.forms[0].target = '';document.forms[0].submit();\">"
						+ "<span class='p_last'></span></a>";
			} else {
				str += "<span class='p_last2'></span>";
			}
		}

		str += "<input type=\"text\"   id=\"pageNo\" name=\"pageNo\" value="
				+ rs.pageNo + " >&nbsp;";
		if (rs.realTotal > 0) {
			str += ""
					+ "<button type=\"button\" name=\"ppbutton\"  style=\"line-height: 5px;\" onclick=\"if(parseInt(document.all('pageNo').value)>"
					+ rs.totalPage
					+ ")document.all('pageNo').value="
					+ rs.totalPage
					+ ";document.forms[0].operation.value='"
					+ operation
					+ "';document.forms[0].target = '';document.forms[0].submit();\">Go</button>&nbsp;"
					+ "";
		} else {
			str += ""
					+ "<button type=\"button\" name=\"ppbutton\" style=\"line-height: 5px;\"  onclick=\"if(parseInt(document.all('pageNo').value)<0)document.all('pageNo').value="
					+ rs.pageNo
					+ ";document.forms[0].operation.value='"
					+ operation
					+ "';document.forms[0].target = '';document.forms[0].submit();\">Go</button>&nbsp;"
					+ "";
		}

		return str;
	}

	/**
	 * ������ҳ������ҳ����ֱ��ʹ�÷�ҳ����ǩ����ֱ�Ӳ�����ҳHTML���롣���ٹ��� ���100
	 * 
	 * @param rs
	 *            Result ���ݿ�ӿڷ��ؽ��
	 * @param req
	 *            HttpServletRequest
	 * @return String
	 */
	protected String pageBars(Result rs, HttpServletRequest req) {
		StringBuilder str = new StringBuilder("");

		if (rs.realTotal == 0) {
			str.append("<div>" + getMessage(req, "common.theNo")).append(
					rs.pageNo).append(getMessage(req, "common.page")).append(
					"&nbsp;&nbsp;</div>");

		} else {
			str.append("<div>" + getMessage(req, "common.theNo")).append(
					(rs.totalPage > 0 ? rs.pageNo : 0) + "/" + rs.totalPage)
					.append(getMessage(req, "common.page")).append("&nbsp;(")
					.append(rs.getRealTotal()).append(")&nbsp;&nbsp;</div>");

		}
		req.setAttribute("totalRow", rs.getRealTotal());
		String operation = getParameter("operation", req);
		if ("3".equals(operation) || "2".equals(operation)) {
			operation = OperationConst.OP_QUERY + "";
		}

		int pageSize = rs.pageSize;
		String pageSizeStr = "";

		if (pageSize != 20 && pageSize != 40 && pageSize != 60
				&& pageSize != 80 && pageSize != 100 && pageSize != 200) {
			pageSizeStr = "<option value=" + pageSize + " selected " + ">"
					+ pageSize + "</option>";
		}

		str
				.append(
						"<select id=\"pageSize\" name=\"pageSize\" onChange=\"document.forms[0].operation.value='"
								+ operation
								+ "';document.forms[0].target = '';document.forms[0].submit();\">")
				.append(pageSizeStr).append("<option value=20 ").append(
						(rs.pageSize == 20 ? "selected" : "")).append(
						">20</option>").append("<option value=40 ").append(
						(rs.pageSize == 40 ? "selected" : "")).append(
						">40</option>").append("<option value=60 ").append(
						(rs.pageSize == 60 ? "selected" : "")).append(
						">60</option>").append("<option value=80 ").append(
						(rs.pageSize == 80 ? "selected" : "")).append(
						">80</option>").append("<option value=100 ").append(
						(rs.pageSize == 100 ? "selected" : "")).append(
						">100</option>").append("<option value=200 ").append(
						(rs.pageSize == 200 ? "selected" : "")).append(
						">200</option>").append("</select>");

		// ��ҳ
		if (rs.pageNo > 1) {
			str
					.append(
							"<a class=page_a href=\"javascript:document.getElementById('pageNo').value=1;")
					.append("document.forms[0].operation.value='")
					.append(operation)
					.append(
							"';document.forms[0].target = '';document.forms[0].submit();\">")
					.append("<span class='p_first'></span></a>");

		} else {
			str.append("<span class='p_first2'></span>");
		}

		// ��һҳ
		if (rs.pageNo > 1) {

			str
					.append(
							"<a class=page_a href=\"javascript:document.getElementById('pageNo').value=")
					.append((rs.pageNo - 1)).append(
							";document.forms[0].operation.value='").append(
							operation).append(
							"';document.forms[0].target = '';")
					.append("document.forms[0].submit();\">").append(
							"<span class='p_prep'></span></a>");
		} else {
			str.append("<span class='p_prep2'></span>");

		}

		if (rs.pageNo < rs.totalPage) {
			str
					.append(
							"<a class=page_a href=\"javascript:document.getElementById('pageNo').value=")
					.append((rs.pageNo + 1))
					.append(";document.forms[0].operation.value='")
					.append(operation)
					.append(
							"';document.forms[0].target = '';document.forms[0].submit();\">")
					.append("<span class='p_next'></span></a>");
		} else {
			str.append("<span class='p_next2'></span>");
		}

		if (rs.totalPage > 0) {
			// βҳ
			if (rs.totalPage > 1 && rs.pageNo < rs.totalPage
					&& rs.realTotal > 0) {
				str
						.append(
								"<a class=page_a href=\"javascript:document.getElementById('pageNo').value=")
						.append(rs.totalPage)
						.append(";document.forms[0].operation.value='")
						.append(operation)
						.append("';")
						.append(
								"document.forms[0].target = '';document.forms[0].submit();\">")
						.append("<span class='p_last'></span></a>");
			} else {
				str.append("<span class='p_last2'></span>");
			}
		}

		str.append(
				"<input type=\"text\"   id=\"pageNo\" name=\"pageNo\" value=")
				.append(rs.pageNo).append(" >&nbsp;");
		if (rs.realTotal > 0) {
			str
					.append(
							"<button type=\"button\" name=\"ppbutton\"  onclick=\"if(parseInt(document.all('pageNo').value)>")
					.append(rs.totalPage + ")document.all('pageNo').value=")
					.append(rs.totalPage)
					.append(";document.forms[0].operation.value='")
					.append(operation)
					.append(
							"';document.forms[0].target = '';document.forms[0].submit();\">go</button>&nbsp;")
					.append("");

		} else {
			str
					.append(
							"<button type=\"button\" name=\"ppbutton\"  onclick=\"if(parseInt(document.all('pageNo').value)<0)document.all('pageNo').value=")
					.append(rs.pageNo)
					.append(";document.forms[0].operation.value='")
					.append(operation)
					.append(
							"';document.forms[0].target = '';document.forms[0].submit();\">go</button>&nbsp;");

		}
		return str.toString();
	}

	/**
	 * �������·�ҳ������ҳ����ֱ��ʹ�÷�ҳ����ǩ����ֱ�Ӳ�����ҳHTML���롣���ٹ���
	 * 
	 * @param rs
	 *            Result ���ݿ�ӿڷ��ؽ��
	 * @param req
	 *            HttpServletRequest
	 * @return String
	 */
	protected String pageBar1(Result rs, HttpServletRequest req) {
		String str = "";
		// ��һҳ
		if (rs.pageNo > 1) {

			str += "<a href=\"javascript:document.getElementById('pageNo').value="
					+ (rs.pageNo - 1)
					+ ";document.forms[0].submit();\">"
					+ getMessage2(req, "common.prePage") + "</a>&nbsp;&nbsp;";
		} else {
			str += getMessage2(req, "common.prePage") + "&nbsp;&nbsp;";
		}

		// ��һҳ
		if (rs.pageNo < rs.totalPage) {
			str += "<a id=\"nextPageSize\" href=\"javascript:document.getElementById('pageNo').value="
					+ (rs.pageNo + 1)
					+ ";document.forms[0].submit();\">"
					+ getMessage2(req, "common.nextPage") + "</a>&nbsp;&nbsp;";
		} else {
			str += getMessage2(req, "common.nextPage") + "&nbsp;&nbsp;";
		}

		str += "<input type=\"hidden\"   id=\"pageNo\" name=\"pageNo\" value="
				+ rs.pageNo + " >&nbsp;";

		return str;
	}
	
	/**
	 * �������·�ҳ������ҳ����ֱ��ʹ�÷�ҳ����ǩ����ֱ�Ӳ�����ҳHTML���롣���ٹ���
	 * 
	 */
	protected String pageBar2(Result rs, HttpServletRequest req) {
		StringBuilder pagebar = new StringBuilder() ;
		pagebar.append("<div class='scott'><span class='page-number'>("+rs.realTotal+")</span>") ;
		/*��һ��*/
		if (rs.pageNo > 1) {
			pagebar.append("<a href='javascript:pageSubmit("+(rs.pageNo-1)+");'> < </a>") ;
		}else{
			pagebar.append("<span class='disabled'> < </span>") ;
		}
		/*��һҳ*/
		if(rs.pageNo>=7){
			pagebar.append("<a href='javascript:pageSubmit(1);'>1</a>") ;
		}
		
		if(rs.pageNo<7){
			/*��ʾǰ��7ҳ*/
			for(int i=1;i<8 && i<=rs.totalPage;i++){
				if(rs.pageNo == i){
					pagebar.append("<span class='current'>"+rs.pageNo+"</span>") ;
				}else{
					pagebar.append("<a href='javascript:pageSubmit("+i+");'>"+i+"</a>") ;
				}
			}
			if(rs.totalPage>7){
				pagebar.append("...") ;
			}
		}else if(rs.totalPage-rs.pageNo<6){
			pagebar.append("...") ;
			/*��ʾ����7ҳ*/
			for(int i=rs.totalPage-6;i<=rs.totalPage;i++){
				if(rs.pageNo == i){
					pagebar.append("<span class='current'>"+rs.pageNo+"</span>") ;
				}else{
					pagebar.append("<a href='javascript:pageSubmit("+i+");'>"+i+"</a>") ;
				}
			}
		}else{
			/*�м�ҳ����ʾ����ǰҳ��ǰ3ҳ�Ӻ�3ҳ*/
			for(int i=1;i<rs.totalPage;i++){
				if((rs.pageNo-i >= 0 && rs.pageNo-i<=3) || (i-rs.pageNo >= 0 && i-rs.pageNo<=3)){
					if(rs.pageNo == i){
						pagebar.append("<span class='current'>"+rs.pageNo+"</span>") ;
					}else{
						pagebar.append("<a href='javascript:pageSubmit("+i+");'>"+i+"</a>") ;
					}
				}
			}
			pagebar.append("...") ;
		}
		/*���һҳ*/
		if((rs.totalPage-rs.pageNo>=6 || rs.pageNo<7) && rs.totalPage>7){
			pagebar.append("<a href='javascript:pageSubmit("+rs.totalPage+");'>"+rs.totalPage+"</a>") ;
		}
		/*��һҳ*/
		if (rs.pageNo < rs.totalPage) {
			pagebar.append("<a href='javascript:pageSubmit("+(rs.pageNo+1)+");'> > </a>") ;
		}else{
			pagebar.append("<span class='disabled'> > </span>") ;
		}
		pagebar.append("<span class='page-number'>��<input type='text' id='pageNo' name='pageNo' value='"+rs.pageNo+"' onKeyDown='if(event.keyCode==13) submitQuery();'/>ҳ<a href='javascript:submitQuery();'>GO</a></span>") ;
		
		pagebar.append("<select id=\"pageSize\" name=\"pageSize\" onChange=\"submitQuery();\">");
		if (rs.pageSize != 15 && rs.pageSize != 30 && rs.pageSize != 50
				&& rs.pageSize != 100 && rs.pageSize != 500 && rs.pageSize != 1000) {
			pagebar.append("<option value=" + rs.pageSize + " selected " + ">" + rs.pageSize + "</option>");
		}
		pagebar.append("<option value=15 " + (rs.pageSize == 15 ? "selected" : "") + ">15</option>");
		pagebar.append("<option value=30 " + (rs.pageSize == 30 ? "selected" : "") + ">30</option>");
		pagebar.append("<option value=50 " + (rs.pageSize == 50 ? "selected" : "") + ">50</option>");
		pagebar.append("<option value=100 " + (rs.pageSize == 100 ? "selected" : "") + ">100</option>");
		pagebar.append("<option value=500 " + (rs.pageSize == 500 ? "selected" : "") + ">500</option>");
		pagebar.append("<option value=1000 " + (rs.pageSize == 1000 ? "selected" : "") + ">1000</option>");
		pagebar.append("</select></div>");;
		return pagebar.toString() ;
	}
	
	/**
	 * CRM�ֵܱ��ҳ
	 * @param rs
	 * @param req
	 * @return
	 */
	protected String pageBarForCRM(Result rs, HttpServletRequest req) {
		String type = getParameter("type",req);
		String str = "";
		if (rs.realTotal == 0) {
			str = "<div>" + getMessage(req, "common.theNo") + "" + (rs.pageNo)
					+ "" + getMessage(req, "common.page")
					+ "&nbsp;&nbsp;</div>";
		} else {
			str = "<div>" + getMessage(req, "common.theNo") + ""
					+ (rs.totalPage > 0 ? rs.pageNo : 0) + "/" + rs.totalPage
					+ "" + getMessage(req, "common.page") + "&nbsp;("
					+ rs.getRealTotal() + ")&nbsp;&nbsp;</div>";
		}
		req.setAttribute("totalRow", rs.getRealTotal());
		String operation = getParameter("operation", req);
		if ("3".equals(operation) || "2".equals(operation)) {
			operation = OperationConst.OP_QUERY + "";
		}

		//������ھӱ�����չʾ �ھӱ��ֲ�Ҫselect������
		if(!"ajaxBrotherList".equals(type)){
			int pageSize = rs.pageSize;
			String pageSizeStr = "";
			if (pageSize != 15 && pageSize != 30 && pageSize != 50
					&& pageSize != 100 && pageSize != 500 && pageSize != 1000
					&& pageSize != 5000) {
				pageSizeStr = "<option value=" + pageSize + " selected " + ">"
				+ pageSize + "</option>";
			}
			
			str += "<select id=\"pageSize\" name=\"pageSize\" onChange=\"pageSelect(this);\">"
				+ pageSizeStr
				+ "<option value=15 "
				+ (rs.pageSize == 15 ? "selected" : "")
				+ ">15</option>"
				+ "<option value=30 "
				+ (rs.pageSize == 30 ? "selected" : "")
				+ ">30</option>"
				+ "<option value=50 "
				+ (rs.pageSize == 50 ? "selected" : "")
				+ ">50</option>"
				+ "<option value=100 "
				+ (rs.pageSize == 100 ? "selected" : "")
				+ ">100</option>"
				+ "<option value=500 "
				+ (rs.pageSize == 500 ? "selected" : "")
				+ ">500</option>"
				+ "<option value=1000 "
				+ (rs.pageSize == 1000 ? "selected" : "")
				+ ">1000</option>"
				+ "</select>";
		}
		// ��ҳ
		if (rs.pageNo > 1) {
			str += ""
					+ "<a class='page_a' pageNo='1'>"
					+ "<span class='p_first'></span></a>";

		} else {
			str += "<span class='p_first2'></span>";
		}

		// ��һҳ
		if (rs.pageNo > 1) {

			str += "<a class='page_a' pageNo='"+(rs.pageNo - 1)+"'>"
					+ "<span class='p_prep'></span></a>";
		} else {
			str += "<span class='p_prep2'></span>";
		}

		if (rs.pageNo < rs.totalPage) {
			str += "<a class=page_a pageNo='"+(rs.pageNo + 1)+"'>"
					+ "<span class='p_next'></span></a>";
		} else {
			str += "<span class='p_next2'></span>";
		}

		if (rs.totalPage > 0) {
			// βҳ
			if (rs.totalPage > 1 && rs.pageNo < rs.totalPage
					&& rs.realTotal > 0) {
				str += "<a class='page_a' pageNo='"+rs.totalPage+"' >"
						+ "<span class='p_last'></span></a>";
			} else {
				str += "<span class='p_last2'></span>";
			}
		}

		str += "<input type=\"text\"   id=\"pageNo\" name=\"pageNo\" value="
				+ rs.pageNo + " >&nbsp;";
		if (rs.realTotal > 0) {
			str += "<a class='go' totalPage='"+rs.totalPage+"'>GO</a>";
		} else {
			str += ""
					+ "<button type=\"button\" name=\"ppbutton\" style=\"line-height: 5px;\"  onclick=\"if(parseInt(document.all('pageNo').value)<0)document.all('pageNo').value="
					+ rs.pageNo
					+ ";document.forms[0].operation.value='"
					+ operation
					+ "';document.forms[0].target = '';document.forms[0].submit();\">Go</button>&nbsp;"
					+ "";
		}

		return str;
	}

	
	
	
	protected String getParameter(String param, HttpServletRequest req) {
		String str = req.getParameter(param);
		if (str != null) {
			str = str.trim();
		}
		return str;
	}

	protected int getParameterInt(String param, HttpServletRequest req) {
		try {
			return Integer.parseInt(req.getParameter(param));
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	protected double getParameterDouble(String param, HttpServletRequest req) {
		String doubleStr = req.getParameter(param);
		if (doubleStr == null || doubleStr.length() == 0) {
			return 0;
		}
		try {
			return Double.parseDouble(doubleStr);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	protected Float getParameterFloat(String param, HttpServletRequest req) {
		String floatStr = req.getParameter(param);
		if (floatStr == null || floatStr.length() == 0) {
			return 0.0f;
		}
		try {
			return Float.parseFloat(floatStr);
		} catch (NumberFormatException ex) {
			return 0.0f;
		}
	}

	protected String[] getParameters(String param, HttpServletRequest req) {
		return req.getParameterValues(param);
	}

	protected String getModuleNameByLinkAddr(HttpServletRequest req,
			ActionMapping mapping) {

		Result rs = new Result();
		String index = req.getParameter("winCurIndex") == null ? "" : req
				.getParameter("winCurIndex");
		HashMap winIndex = (HashMap) req.getSession().getAttribute(
				BaseEnv.WIN_MAP);
		String strURL = null;
		if (winIndex != null && index.length() > 0) {
			strURL = (String) winIndex.get(index);
		}
		String forward = req.getParameter("forward");
		String isMenu = req.getParameter("src");
		if (strURL == null && "add".equals(forward)) {
			strURL = req.getQueryString();
			if (strURL != null && strURL.indexOf("&operation=") != -1) {
				strURL = "/UserFunctionQueryAction.do?"
						+ strURL.substring(0, strURL.indexOf("&operation="));
				LoginBean o = (LoginBean) req.getSession().getAttribute(
						"LoginBean");
				ArrayList menu = (ArrayList) BaseEnv.moduleMap.get(o
						.getDefSys());
				GlobalsTool.getModuleNameN(menu, strURL, rs, getLocale(req)
						.toString(), 1);
			}
		} else if ("menu".equals(isMenu)) {
			String queryStr = req.getQueryString();
			strURL = "/UserFunctionQueryAction.do?"
					+ queryStr.substring(0, queryStr.indexOf("src=menu") - 1);
			LoginBean o = (LoginBean) req.getSession()
					.getAttribute("LoginBean");
			ArrayList menu = (ArrayList) BaseEnv.moduleMap.get(o.getDefSys());
			GlobalsTool.getModuleNameN(BaseEnv.allModule, strURL, rs, getLocale(req)
					.toString(), 1);
		} else if (strURL == null) {
			String parameter = mapping.getParameter();
			if (parameter.indexOf("$") > 0) {
				int pos1 = parameter.indexOf("$");
				int pos2 = 0;
				if (parameter.indexOf("&", pos1) > 0) {
					pos2 = parameter.indexOf("&", pos1);
				}

				String beforeStr = parameter.substring(0, pos1);
				String midStr = "";
				String lastStr = "";
				if (pos2 == 0) {
					midStr = getParameter(parameter.substring(pos1 + 1), req);
				} else {
					midStr = getParameter(parameter.substring(pos1 + 1, pos2),
							req);
					lastStr = parameter.substring(pos2);
				}

				// ����������Լ������Ҫ�жϱ��Ƿ�Ϊ�ھӱ�������ھӱ���Ҫ������������Ȩ��
				if (beforeStr
						.startsWith("/UserFunctionQueryAction.do?tableName=")) {
					String tableName = midStr;
					DBTableInfoBean tableInfoBean = DDLOperation.getTableInfo(
							(Hashtable) req.getSession().getServletContext()
									.getAttribute(BaseEnv.TABLE_INFO),
							tableName);
					if (tableInfoBean.getTableType() == DBTableInfoBean.BROTHER_TABLE) {
						parameter = "&tabIndex=" + tableName;
						LoginBean o = (LoginBean) req.getSession()
								.getAttribute("LoginBean");
						ArrayList menu = (ArrayList) BaseEnv.moduleMap.get(o
								.getDefSys());
						GlobalsTool.getModuleNameN(menu, parameter, rs,
								getLocale(req).toString(), 3);
						return rs.getRetVal() == null ? "" : rs.getRetVal()
								.toString();
					}
				}
				parameter = beforeStr + midStr + lastStr;
				LoginBean o = (LoginBean) req.getSession().getAttribute(
						"LoginBean");
				ArrayList menu = (ArrayList) BaseEnv.moduleMap.get(o
						.getDefSys());
				GlobalsTool.getModuleNameN(menu, parameter, rs, getLocale(req)
						.toString(), 1);
			}
		} else {
			LoginBean o = (LoginBean) req.getSession()
					.getAttribute("LoginBean");
			String linkAddr = strURL;
			if (linkAddr.indexOf("&tabIndex") > 0) {
				linkAddr = linkAddr.substring(0,
						linkAddr.indexOf("tabIndex") - 1);
			}
			if (strURL.indexOf("&operation=") != -1) {
				linkAddr = strURL.substring(0, linkAddr.indexOf("&operation="));
			}
			ArrayList menu = (ArrayList) BaseEnv.moduleMap.get(o.getDefSys());
			GlobalsTool.getModuleNameN(BaseEnv.allModule, linkAddr, rs, getLocale(req)
					.toString(), 1);
		}
		return rs.getRetVal() == null ? "" : rs.getRetVal().toString();

	}
	
	public String getModuleId(String url,ArrayList<ModuleBean> list){
		for(ModuleBean mb :list){
			if(mb.getChildList().size() > 0){
				String kid= getModuleId(url,mb.getChildList());
				if(kid != null && kid.length() > 0){
					return kid;
				}
			}else if(mb.getLinkAddress().equals(url)){
				return mb.getId();
			}
		}
		return "";
	}

	protected abstract ActionForward doAuth(HttpServletRequest req,
			ActionMapping mapping);

	/**
	 * Actionִ�к���
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
	protected abstract ActionForward exe(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

}
