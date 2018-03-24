package com.menyi.web.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.struts.util.MessageResources;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import sun.misc.BASE64Encoder;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.Employee;
import com.koron.oa.bean.FieldBean;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.util.EmployeeMgt;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.koron.oa.workflow.ReadXML;
import com.koron.oa.workflow.template.OAWorkFlowTempMgt;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.KeyPair;
import com.menyi.aio.bean.ReatailBean;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.GlobalMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.service.ServiceMgt;
import com.menyi.aio.web.sysAcc.SysAccMgt;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * <p>
 * Title: ���ӿ�Ϊ���������ר�ýӿڣ��������벻���������д
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: ������
 * </p>
 * 
 * @author ������
 * @version 1.0
 */
public class ReportServlet extends HttpServlet {

	private static final long serialVersionUID = -1283066781275506739L;

	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	
	private static DES des=new DES();

	
	private void writeError(String str,HttpServletResponse resp){
		try{
			BaseEnv.log.error("------"+str);
			resp.setContentType("text/HTML;   charset=UTF-8");
			resp.setHeader("Cache-Control", "no-cache");
			PrintWriter out = resp.getWriter();
			out.println(str);
			out.close();
		}catch(Exception e){
			BaseEnv.log.error("ReportServlet.writeError Error:"+e);
		}
	}
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			
			BaseEnv.log.debug("ReportServlet.service �����ַ��"+req.getRequestURI()+(req.getQueryString()==null?"":"?"+req.getQueryString()));
			String operation = req.getParameter("operation");
			
			if ("getPicFile".equals(operation)) { //����session��Ȩ
					getPicFile(req, resp);
					return;
			}
			
			//���ӿ�Ϊ���������ר�ýӿڣ��������벻���������д
			//������봫�����SESSION,��ɳɷ�ΪDES(userId:JSessionID:timesmap)
			String session = req.getParameter("SESSION");
			if (session == null || session.length() == 0 ) {
				writeError("�ӿ�������SESSION����,���ܱ����ؼ��汾���������һ��",resp);
				return;
			}
			
			session = des.Decode(strDefaultKey,session).toString().trim();
			String[] ses = session.split(":");
			if (ses.length != 3) {
				writeError("�ӿ�����SESSION�������ܺ���ȷ",resp);
				return;
			}
			OnlineUser user =OnlineUserInfo.getUser(ses[0]);
			if(user==null){
				writeError("�ӿ�������ܺ�userId������",resp);
				return;
			}
			//������ʱ����ʱ���JsessionID������
			try {
				Date date = BaseDateFormat.parse(ses[2], BaseDateFormat.yyyyMMddHHmmss2);
				//�����䳬��12Сʱ����ʱ
				long ts = new Date().getTime() - date.getTime();
				if(ts <0){
					ts = -ts;
				}
				if(ts > 12 * 60 * 60000){
					//ʱ�����12��Сʱ��
					writeError("�ӿ���������ʧЧ,����ԭ�򱾵�ʱ���������ʱ�䲻ͬ��",resp);
					return;
				}
			} catch (Exception e) {
				writeError("�ӿ�����SESSION�������ܺ�ʱ���ʽ����ȷ",resp);
				return;
			}
			
			if ("getTableInfo".equals(operation)) {
				doGetTableInfo(req, resp);
			} else if ("getSysType".equals(operation)) {
				doGetSysType(req, resp);
			} else if ("saveSQLFile".equals(operation)) {
				doSaveSQLFile(req, resp, operation,user);
			} else if ("saveFormatFile".equals(operation)) {
				doSaveFormatFile(req, resp, operation,user);
			} else if ("getSQLFile".equals(operation)) {
				doGetReportFile(req, resp);
			} else if ("getStyleSQLFile".equals(operation)) {
				doGetStyleReportFile(req, resp,user);
			} else if ("getFormatFile".equals(operation)) {
				doGetFormatFile(req, resp);
			} else if ("getPrintData".equals(operation)) {
				doGetPrintData(req, resp,user);
			} else if ("getBillData".equals(operation)) {
				doGetPrintData(req, resp,user);
			} else if ("getEmployeeInfo".equals(operation)) {
				getEmployeeInfo(req, resp);
			} else if ("getDefineXmlFile".equals(operation)) {
				getDefineXmlFile(req, resp);
			} else if ("printCount".equals(operation)) {
				setPrintCount(req, resp);
			} else if ("getPrintCount".equals(operation)) {
				getPrintCount(req, resp);
			} else if ("getNewFormatName".equals(operation)) {
				getNewFormatName(req, resp);
			} else if ("deleteFormatName".equals(operation)) {
				deleteFormatName(req, resp);
			} else if ("getLinkReport".equals(operation)) {
				getLinkReport(req, resp); //ȡ����
			} else if ("getFileList".equals(operation)) {
				getFileList(req, resp); //��ѯͼƬ�ļ����� ĳ��Ŀ¼
			} else if ("getTablList".equals(operation)) {
				getTablList(req, resp); //����ȡ����Ϣ
			} else if ("getTableData".equals(operation)) {
				getTableData(req, resp); //����ȡ����Ϣ
			} else if ("upLoadRetail".equals(operation)) {
				upLoadRetail(req, resp); //�����ϴ�
			} else if ("getFieldInfo".equals(operation)) {
				getFieldInfo(req, resp);
			} else if ("getDefineFile".equals(operation)) {
				getDefineFile(req, resp);
			} else if ("saveDefineFile".equals(operation)) {
				saveDefineFile(req, resp,user);
			} else if ("sql".equals(operation)) {
				defineSQL(req, resp,user);
			} else if ("proc".equals(operation)) {
				defineProc(req, resp,user);
			}
		} catch (Exception e) {
			BaseEnv.log.error("------ReportServlet.service-------",e);
			e.printStackTrace();
			System.out.print("----------------------------------ddddddddddddddd-----------"+e);
		}

	}
	/**
	 * ��ȡ���б�ṹ�ֶ���Ϣ
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getFieldInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Hashtable map = (Hashtable) req.getSession().getServletContext()
				.getAttribute(BaseEnv.TABLE_INFO);
		if (map == null)
			return;

		String tableName = req.getParameter("tableName");
		if (tableName == null || tableName.trim().length() == 0) {
			return;
		}
		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		PrintWriter out = resp.getWriter();

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<fieldInfo>");
		DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
		for (Object field : tableInfo.getFieldInfos()) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) field;
			out.println("<field id=\"" + fieldInfo.getId() + "\" fieldName=\""
					+ fieldInfo.getFieldName() + "\" listOrder=\""
					+ fieldInfo.getListOrder() + "\" isNull=\""
					+ fieldInfo.getIsNull() + "\" isUnique=\""
					+ fieldInfo.getIsUnique() + "\" isStat=\""
					+ fieldInfo.getIsStat() + "\" defaultValue=\""
					+ fieldInfo.getDefaultValue() + "\" fieldType=\""
					+ fieldInfo.getFieldType() + "\" maxLength=\""
					+ fieldInfo.getMaxLength() + "\" width=\""
					+ fieldInfo.getWidth() + "\" inputType=\""
					+ fieldInfo.getInputType() + "\" refEnumerationName=\""
					+ fieldInfo.getRefEnumerationName() + "\" inputValue=\""
					+ fieldInfo.getInputValue() + "\" fieldSysType=\""
					+ fieldInfo.getFieldSysType() + "\" udType=\""
					+ fieldInfo.getUdType() + "\">");
			out.println("<Displays>");
			if (fieldInfo.getDisplay() != null) {
				for (Object display : fieldInfo.getDisplay().map.keySet()) {
					String key = (String) display;
					if(tableName.startsWith("Flow_")){
						out.println(("<Display id=\""
								+ fieldInfo.getDisplay().getId() + "\" localStr=\""
								+ key + "\" display=\"" + (fieldInfo.getLanguageId()==null?fieldInfo.getFieldName():fieldInfo.getLanguageId().replace("&", "&amp;")) + "\"/>"));
					}else{
						out.println(("<Display id=\""
							+ fieldInfo.getDisplay().getId() + "\" localStr=\""
							+ key + "\" display=\""
							+ (fieldInfo.getDisplay().map.get(key)==null?key:fieldInfo.getDisplay().map.get(key).toString().replace("&", "&amp;")) + "\"/>"));
					}
				}
			}
			out.println("</Displays>");
			out.println("</field>");
		}
		out.println("</fieldInfo>");
	}




	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/** 
	 * Convert hex string to byte[] 
	 * @param hexString the hex string 
	 * @return byte[] 
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public void init() throws ServletException {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
	}

	private String deptTree(ArrayList list, List<Employee> emps, String deptName, String classCode, String isCatalog) {
		String deptTree = "";
		deptTree = "<emp type=\"dept\" code=\"" + classCode + "\" name=\"" + deptName + "\">";
		for (int i = 0; i < emps.size(); i++) {
			Employee emp = emps.get(i);
			if (emp.getDepartmentCode().equals(classCode)) {
				deptTree += "<emp type=\"employee\" code=\"" + emp.getid() + "\" name=\"" + emp.getEmpFullName() + "\" py=\"" + emp.getEmpfullnamePym() + "\"/>";
			}
		}

		if (isCatalog != null && isCatalog.equals("1")) {
			for (int i = 0; i < list.size(); i++) {
				String[] obj = (String[]) list.get(i);
				if (obj[0].substring(0, obj[0].length() - 5).equals(classCode)) {
					deptTree += this.deptTree(list, emps, obj[1], obj[0], obj[2]);
				}
			}
		}

		deptTree += "</emp>";
		return deptTree;
	}

	public void getPicFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getParameter("fileName");// ���磺tblGoods/1258191436484.bmp
		fileName = GlobalsTool.toChinseChar_GBK(fileName);
		String path = BaseEnv.FILESERVERPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		if(fileName != null && fileName.indexOf("/DISK") > -1){
			fileName = fileName.substring(fileName.indexOf("/DISK") +1);
		}else if(fileName != null && fileName.indexOf("/PICPATH") > -1){
			fileName = fileName.substring(fileName.indexOf("/PICPATH") +1);
		}
		if(fileName != null && fileName.startsWith("DISK")){
    		String disk = fileName.substring(4,5);
    		fileName = fileName.substring(5);
    		fileName = disk+":"+fileName;
    		//�����ļ��������ļ�   	
    		BaseEnv.log.debug("����ȡ�����ļ�:"+fileName);
    		path = fileName;
    	}else if(fileName != null && fileName.startsWith("PICPATH")){
    		fileName = fileName.substring(8);
    		String picPath = GlobalsTool.getSysSetting("picPath");
        	if(picPath.endsWith("\\")|| picPath.endsWith("/")){
        		picPath = picPath.substring(0,picPath.length()  -1);
        	}
    		fileName = picPath+"\\"+fileName;
    		//�����ļ��������ļ�   	
    		BaseEnv.log.debug("����ȡ�����ļ�:"+fileName);
    		path = fileName;
    		
    	}else{
    		path = path + "pic/" + fileName;
    	}
		File file = new File(path);
		FileInputStream in = new FileInputStream(file);
		ServletOutputStream out = resp.getOutputStream();

		byte[] b = new byte[1024];
		int count = 0;

		while ((count = in.read(b)) > -1) {
			out.write(b, 0, count);
		}
		out.close();
		in.close();
	}

	/**
	 * ��ѯͼƬ�ļ����� ĳ��Ŀ¼
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getFileList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String strPath = req.getParameter("path");
		String picPath = BaseEnv.FILESERVERPATH + "/pic" + strPath;
		File file = new File(picPath);

		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<fileList>");
		if (file != null) {
			String[] files = file.list();
			for (String str : files) {
				File strFile = new File(picPath + "/" + str);
				if (strFile.isDirectory()) {
					out.println("<item fileName='" + strFile.getName() + "' type='1'/>");
				} else {
					out.println("<item fileName='" + strFile.getName() + "' type='0'/>");
				}
			}
		}
		out.println("</fileList>");

	}

	/**
	 * ��ӡ����ʱ���ڵ�ǰ���ݴ�ӡ�����������ۼ�
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void getPrintCount(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String billTable = req.getParameter("billTable");
		String billID = req.getParameter("billId");
		String type = req.getParameter("reportType");

		if (billID != null && billID.length() > 0) {
			SystemSettingBean set = BaseEnv.systemSet.get("printCount");
			PrintWriter out = resp.getWriter();
			int printCount;
			if (set == null || (set != null && set.getSetting().equals("0"))) {
				printCount = 0;
			} else {
				printCount = GlobalMgt.getPrintCount(billTable, billID);
			}

			out.print(printCount);
			out.close();
		}
	}

	/**
	 * ��ӡ����ʱ���ڵ�ǰ���ݴ�ӡ�����������ۼ�
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void setPrintCount(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String billTable = req.getParameter("billTable");
		String billID = req.getParameter("billId");
		String type = req.getParameter("reportType");

		if (billID != null && billID.length() > 0) {
			GlobalMgt.setPrintCount(billTable, billID);
			
			//��¼��ӡ��־
			DynDBManager dbmgt = new DynDBManager();
			LoginBean lg=(LoginBean) req.getSession().getAttribute("LoginBean");
			Hashtable map = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			DBTableInfoBean dbTableInfo = (DBTableInfoBean)((Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO)).get(billTable);
			Hashtable props = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
			Result result = dbmgt.detail(billTable, map, billID, lg.getSunCmpClassCode(),props,lg.getId(),true,"");
			HashMap values = (HashMap) result.getRetVal() ;
			String locale=GlobalsTool.getLocale(req).toString();
			String buttonTypeName=GlobalsTool.getTableInfoBean(billTable).getDisplay().get(locale) ;
			int operation=14;
			dbmgt.addLog(operation, values, null, dbTableInfo, locale.toString(), lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"��ӡ����",buttonTypeName);

		}
	}

	/**
	 * getDefineXmlFile
	 */
	private void getDefineXmlFile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<Define>");
		Map map = BaseEnv.defineSqlMap;
		Iterator iterator = map.keySet().iterator();
		out.println("<defines>");
		while (iterator.hasNext()) {
			out.println("<define Name = \"" + iterator.next().toString() + "\" caption = \"\"/>");
		}
		out.println("</defines>");
		out.println("</Define>");
		out.close();
	}

	/**
	 * getEmployeeInfo
	 * ��������ƾ�������ְԱ��Ϣ
	 * @param req
	 *            HttpServletRequest
	 * @param resp
	 *            HttpServletResponse
	 */
	private void getEmployeeInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<Employee>");
		out.println("<employees>");
		for (OnlineUser e : OnlineUserInfo.getAllUserList()) {
			out.println("<employee name =\"" + e.getName() + "\"  id = \"" + e.getId() + "\" departmentName = \"" + e.getDepartmentName() + "\"/>");
		}
		out.println("</employees>");
		out.println("</Employee>");
		out.close();
	}

	/**
	 * �õ�Ҫ��ӡ�ĵ��ݵ�����
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param resp
	 *            HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGetBillData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String billTable = req.getParameter("BillTable");
		String billID = req.getParameter("BillID");
		String locale = GlobalsTool.getLocale(req).toString();
		// �õ������ӱ�����
		DynDBManager mgt = new DynDBManager();
		Hashtable map = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		// �ж��Ƿ����÷�֧����
		LoginBean lg = new LoginBean();
		lg = (LoginBean) req.getSession().getAttribute("LoginBean");
		String sunClassCode = lg.getSunCmpClassCode();
		Hashtable props = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		Hashtable sessionSet = (Hashtable) BaseEnv.sessionSet.get(lg.getId());
		boolean isLastSunCompany = Boolean.parseBoolean(sessionSet.get("IsLastSCompany").toString());
		Result rs = mgt.detail(billTable, map, billID, sunClassCode, props, lg.getId(), isLastSunCompany,"");

		HashMap values = (HashMap) rs.getRetVal();

		Set set = values.keySet();
		Iterator iter = set.iterator();
		String fieldFullName = "";
		String fieldValue = "";

		String mainRow = "";
		String detRow = "";

		HashMap childMap;
		Iterator childIter;
		Object obj;

		while (iter.hasNext()) {
			fieldFullName = iter.next().toString();
			obj = values.get(fieldFullName);
			String fieldName = fieldFullName.substring(fieldFullName.indexOf(".") + 1);
			if (fieldName.indexOf("TABLENAME_") < 0) {
				if (obj != null) {
					fieldValue = this.parseMainData(map, billTable, fieldName, values, obj.toString(), locale);
					if (fieldValue.length() == 0) {
						fieldValue = obj.toString();
					}
					fieldValue = parseBillDouble(map, billTable, fieldName, fieldValue);
				} else {
					fieldValue = "";
				}
				mainRow = mainRow + fieldName + "=\"" + fieldValue + "\" ";
			} else {
				String tableName = fieldName.substring(fieldName.indexOf("TABLENAME_") + "TABLENAME_".length());
				ArrayList childList = (ArrayList) obj;
				for (int i = 0; i < childList.size(); i++) {
					childMap = (HashMap) childList.get(i);
					childIter = childMap.keySet().iterator();
					detRow = detRow + "<Row ";
					while (childIter.hasNext()) {
						fieldName = childIter.next().toString();

						obj = childMap.get(fieldName);
						if (obj != null) {
							fieldValue = this.parseMainData(map, tableName, fieldName, childMap, obj.toString(), locale);
							if (fieldValue.length() == 0) {
								fieldValue = obj.toString();
							}
							fieldValue = parseBillDouble(map, tableName, fieldName, fieldValue);
						} else {
							fieldValue = "";
						}
						detRow = detRow + fieldName + "=\"" + fieldValue + "\" ";
					}
					detRow = detRow + "/>\n";
				}
			}

		}
		// ����ѯ�Ľ��д����
		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		PrintWriter out = resp.getWriter();

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<ReportData>");
		out.println("<RowMain " + mainRow + "/>\n");

		out.println("<Rows>");
		out.println(detRow);
		out.println("</Rows>");

		out.println("</ReportData>");
		out.close();
	}

	/**
	 * ���ݱ������ֶ������жϴ��ֶ��Ƿ�ΪDOUBLE,��������ֵת����ϵͳ�涨�ĸ�ʽ
	 * 
	 * @param map
	 *            Hashtable
	 * @param tableName
	 *            String
	 * @param fieldName
	 *            String
	 * @param value
	 *            String
	 * @return String
	 */
	public String parseBillDouble(Hashtable map, String tableName, String fieldName, String value) {
		DBTableInfoBean tableBean = (DBTableInfoBean) map.get(tableName);
		List list = tableBean.getFieldInfos();
		for (int i = 0; i < list.size(); i++) {
			DBFieldInfoBean fieldBean = (DBFieldInfoBean) list.get(i);
			if (fieldBean.getFieldName().equals(fieldName)) {
				if (fieldBean.getFieldType() == fieldBean.FIELD_DOUBLE) {
					int digits = Integer.parseInt(BaseEnv.systemSet.get("digits").getSetting());
					value = GlobalsTool.formatNumber(Double.valueOf(value), false, true, "true".equals(BaseEnv.systemSet.get("intswitch").getSetting()), tableName, fieldName, true);
				}
				break;
			}
		}
		return value;
	}

	/**
	 * ��ʾ���ݱ���ʱ���˷������ڽ����������ֶε�ֵ��ҳ���ϵ���ʾֵ�滻
	 * 
	 * @param map
	 *            Hashtable ���еı�ṹ
	 * @param tableName
	 *            String ����
	 * @param fieldName
	 *            String �ֶ���
	 * @param values
	 *            HashMap ���е������ݵ�ֵ
	 * @return String
	 */
	public String parseMainData(Hashtable map, String tableName, String fieldName, HashMap values, String value, String locale) {
		DBFieldInfoBean fieldBean = null;
		DBTableInfoBean tableBean = (DBTableInfoBean) map.get(tableName);
		List list = tableBean.getFieldInfos();
		for (int i = 0; i < list.size(); i++) {
			fieldBean = (DBFieldInfoBean) list.get(i);
			if (fieldBean.getFieldName().equals(fieldName)) {
				break;
			}
		}

		if (fieldBean.getInputType() == fieldBean.INPUT_ENUMERATE) {
			value = GlobalsTool.getEnumerationItemsDisplay(fieldBean.getRefEnumerationName(), value, locale);
		} else if (fieldBean.getSelectBean() != null) {
			value = "";
			PopupSelectBean selectBean = fieldBean.getSelectBean();
			ArrayList dis = selectBean.getDisplayField();
			for (int i = 0; i < dis.size(); i++) {
				PopField popField = (PopField) dis.get(i);
				if (popField.getDisplay() != null && popField.getDisplay().length() > 0) {
					Set set = values.keySet();
					Iterator iter = set.iterator();

					while (iter.hasNext()) {
						Object obj = iter.next();
						if (obj.toString().equals(popField.fieldName)) {
							value = values.get(obj).toString();
							break;
						}
					}
				}
			}
		}

		return value;
	}

	/**
	 * ��Ƶ��ݱ���ʱ�õ������ӱ�Ľṹ
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param resp
	 *            HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGetBillTableInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String billTable = req.getParameter("BillTable");// ������������
		Hashtable map = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		if (map == null)
			return;

		DBTableInfoBean tableInfo = (DBTableInfoBean) DDLOperation.getTableInfo(map, billTable);
		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		PrintWriter out = resp.getWriter();

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		// д����
		out.println("<tableInfo>");
		printTableInfo(tableInfo, out, "");
		// д��ϸ��ṹ
		ArrayList childTableList = DDLOperation.getChildTables(billTable, map);
		for (int i = 0; i < childTableList.size(); i++) {
			DBTableInfoBean child = (DBTableInfoBean) childTableList.get(i);
			printTableInfo(child, out, "");
		}
		out.println("</tableInfo>");

		out.close();
	}

	public void doSaveSQLFile(HttpServletRequest req, HttpServletResponse resp, String operation,OnlineUser user) throws ServletException, IOException {
		String fileName = req.getParameter("fileName");
		if (fileName == null || fileName.length() <= 0) {
			return;
		}

		File file = null;
		File file_old = null;
		
		/**
		 * 1�����user_reportĿ¼
		 * 2�����reportĿ¼
		 * 3����������ʱ�������������������û���������
		 */
		file = new File(this.getUserReportPath(fileName));
		if(file.exists()){
			file = new File(this.getUserReportPath(fileName));
			if(!file.canWrite()){
				//writeError("�ļ�"+fileName+"ֻ���������޸�",resp);
				//return;
			}
			file_old = new File(this.getUserReportPath(fileName + "_old"));
			file.renameTo(file_old);
		}else {			
			if (SystemState.instance.dogState == SystemState.DOG_FORMAL && "0".equals(SystemState.instance.dogId)) {
				file = new File(this.getReportPath(fileName));
				if(file.exists()){
					if(!file.canWrite()){
						//writeError("�ļ�"+fileName+"ֻ���������޸�",resp);
						//return;
					}
				}
				file_old = new File(this.getReportPath(fileName + "_old"));
				file.renameTo(file_old);
			} else {
				file = new File(this.getUserReportPath(fileName));
				file_old = new File(this.getUserReportPath(fileName + "_old"));
				file.renameTo(file_old);
			}
		}		

		try {
			OutputStream out = new FileOutputStream(file);
			ServletInputStream in = req.getInputStream();
			byte[] bs = new byte[1024];
			int count = 0;
			while ((count = in.read(bs)) > -1) {
				out.write(bs, 0, count);
			}

			out.close();
			in.close();

			// ����ɹ��޸��Ƿ��Ѿ��ϴ���¼Ϊold
			ReportSetMgt mgt = new ReportSetMgt();
			mgt.updateNewFlag(operation, fileName);

			// ɾ��ԭ�����ļ�
			if (file_old.exists()) {
				file_old.delete();
			}
			Result rs = mgt.getReportNameByFile(operation, fileName);
					
			String msg = "�޸ı����ļ�"+fileName;
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				msg = "�޸ı����ļ�"+rs.retVal;
			}
			new DynDBManager().addLog(1, user.getId(), user.getName(), "00001",
					 msg, "tblReports", "�����ļ�","");
			
		} catch (IOException ex) {
			file_old.renameTo(file);
			throw ex;
		}
	}

	public void doSaveFormatFile(HttpServletRequest req, HttpServletResponse resp, String operation,OnlineUser user) throws ServletException, IOException {

		String fileName = req.getParameter("fileName");
		if (fileName == null || fileName.length() <= 0) {
			return;
		}
		String language = req.getParameter("language");
		File file = null;
		File file_old = null;
		if (language != null && language.length() > 0 && !"zh_CN".equals(language)) {
			fileName = language + "/" + fileName;
		}
		if (SystemState.instance.dogState == SystemState.DOG_FORMAL && "0".equals(SystemState.instance.dogId)&& ! new File(this.getUserPath(fileName)).exists()) {
			file = new File(this.getPath(fileName));
			file_old = new File(this.getPath(fileName + "_old"));
			file.renameTo(file_old);
		} else {
			file = new File(this.getUserPath(fileName));
			file_old = new File(this.getUserPath(fileName + "_old"));
			file.renameTo(file_old);
		}

		try {
			OutputStream out = new FileOutputStream(file);
			ServletInputStream in = req.getInputStream();
			byte[] bs = new byte[1024];
			int count = 0;
			while ((count = in.read(bs)) > -1) {
				out.write(bs, 0, count);
			}

			out.close();
			in.close();

			// ����ɹ��޸��Ƿ��Ѿ��ϴ���¼Ϊold
			ReportSetMgt mgt = new ReportSetMgt();
			mgt.updateNewFlag(operation, fileName);

			// ɾ��ԭ�����ļ�
			if (file_old.exists()) {
				file_old.delete();
			}

			Result rs = mgt.getReportNameByFile(operation, fileName);
			String msg = "�޸Ĵ�ӡ��ʽ"+fileName;
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				msg = "�޸Ĵ�ӡ��ʽ"+rs.retVal;
			}
			new DynDBManager().addLog(1, user.getId(), user.getName(), "00001",
					 msg, "tblReportsDet", "��ӡ��ʽ","");
		} catch (IOException ex) {
			file_old.renameTo(file);
			throw ex;
		}
	}

	private LoginBean getLoginBean(HttpServletRequest request) {
		Object o = request.getSession().getAttribute("LoginBean");
		if (o != null) {
			return (LoginBean) o;
		}
		return null;
	}

	/**
	 * ������ʽ
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getNewFormatName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();

		String reportNumber = req.getParameter("ReportNumber");
		String styleName = req.getParameter("StyleName");
		String languageType = req.getParameter("Language");
		styleName = new String(styleName.getBytes("ISO-8859-1"));
		if (reportNumber != null) {
			String[] reportNumbers = reportNumber.split(";");
			if (reportNumbers.length > 1) {
				reportNumber = reportNumbers[0];
			}
		}
		ReportSetMgt setMgt = new ReportSetMgt();
		Result rs = setMgt.getNowFormatName(reportNumber, styleName);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			out.print("01");
			out.close();
		} else {
			String strStyleName = reportNumber + "Format_" + IDGenerater.getId() + ".xml";
			setMgt.insertNowFormatName(reportNumber, strStyleName, styleName, languageType);
			out.print("00");
			out.println();
			out.print(strStyleName);
			out.close();
		}
	}

	/**
	 * ɾ����ʽ
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void deleteFormatName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();

		String reportNumber = req.getParameter("ReportNumber");
		String styleName = req.getParameter("StyleName");
		styleName = new String(styleName.getBytes("ISO-8859-1"));
		String language = req.getParameter("Language");
		if (reportNumber != null) {
			String[] reportNumbers = reportNumber.split(";");
			if (reportNumbers.length > 1) {
				reportNumber = reportNumbers[0];
			}
		}
		ReportSetMgt setMgt = new ReportSetMgt();
		Result rs = setMgt.deleteFormatName(reportNumber, styleName, language);
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			out.print("00");
			out.close();
		} else {
			out.print("01");
			out.close();
		}
	}

	public void doGetPrintData(HttpServletRequest req, HttpServletResponse resp,OnlineUser user) throws ServletException, IOException {

		String reportNumber = req.getParameter("ReportNumber");
		String billTable = req.getParameter("BillTable");
		String billID = req.getParameter("BillID");
		if(billID !=null && billID.indexOf(";hasChild;") >0){
			billID = billID.substring(0,billID.indexOf(";hasChild;"));
		}
		String zip = req.getParameter("zip");
		String parentName = "";

		if (reportNumber != null) {
			String[] reportNumbers = reportNumber.split(";");
			if (reportNumbers.length > 1) {
				reportNumber = reportNumbers[0];
				parentName = reportNumbers[1];
			}
		}

		if (billID != null && billID.length() > 0) {
			ReportSetMgt reportMgt = new ReportSetMgt();
			String[] billTables = billTable.split(";");
			String moduleType = "";
			if (billTables.length > 2) {
				billTable = billTables[0];
				parentName = billTables[1];
				moduleType = billTables[2];
			} else if (billTables.length > 1) {
				billTable = billTables[0];
				parentName = billTables[1];
			}
			if(reportNumber==null||reportNumber.length() ==0){
				ReportsBean bean = (ReportsBean) ((List) reportMgt.getBillTable(billTable, moduleType).getRetVal()).get(0);
				reportNumber = bean.getReportNumber();
			}
		}

		ReportSetMgt setMgt = new ReportSetMgt();
		String locale = GlobalsTool.getLocale(req).toString();
		Result rs = setMgt.getReportSetInfo(reportNumber, locale);
		if (rs.retVal == null) {
			writeError("������Ҳ�������"+reportNumber,resp);
			return;
		}
		ReportsBean setBean = (ReportsBean) rs.getRetVal();

		Object obj = req.getSession().getAttribute("LoginBean");
		LoginBean loginBean = (LoginBean) obj;
		String SQL = "";
		String[] paramListStr;
		ArrayList paramList = new ArrayList();
		ArrayList disFields = new ArrayList();
		ArrayList condField = new ArrayList();//������������
		DefineReportBean defBean = null;
		try {
			defBean = DefineReportBean.parse(reportNumber + "SQL.xml", GlobalsTool.getLocale(req).toString(),user.getId());
			disFields = defBean.getQueryFields();
			condField = defBean.getConFields();
		} catch (Exception ex) {
			writeError("�Ҳ�������SQL�ļ�"+reportNumber+"SQL.xml",resp);
			return;
		}
		ArrayList rows = null;
		ArrayList scopeRight = null;
		if (reportNumber.indexOf("FinanceReport") != -1) {
			//���񱨱����⴦��

			//��ȡ����List
			ArrayList reportData = (ArrayList) req.getSession().getAttribute("accBalanceData");
			rows = reportData;

		} else {
			if ((billID == null || billID.length() == 0)) {
				// �õ�SQL����
				ServletInputStream in = req.getInputStream();
				DataInputStream dis = new DataInputStream(in);
				StringBuffer tempSql = new StringBuffer("");
				byte[] bt = new byte[1024];
				byte[] btAll = new byte[0];
				int count = 0;
				while ((count = dis.read(bt)) > 0) {
					byte[] temp = btAll;
					btAll = new byte[temp.length + count];
					System.arraycopy(temp, 0, btAll, 0, temp.length);
					System.arraycopy(bt, 0, btAll, temp.length, count);
				}
				String temp = new String(btAll);

				temp = java.net.URLDecoder.decode(temp);
				tempSql.append(temp.replaceAll("@CentSign:", "%").replaceAll("@AddSign:", "+"));

				String sql = tempSql.toString().substring(0, tempSql.indexOf("@end:"));
				// ����SQL������
				int SQLIndexS = sql.indexOf("@SQL:");
				int ParamIndexS = sql.indexOf("@ParamList:");

				SQL = sql.substring(SQLIndexS + "@SQL:".length(), ParamIndexS);

				paramListStr = (" " + sql.substring(ParamIndexS + "@ParamList:".length()) + " ").split("@Param:");
				for (int i = 0; i < paramListStr.length; i++) {
					paramListStr[i] = paramListStr[i].trim();
				}
			} else {
				SQL = defBean.getSql();
				if (SQL.indexOf("' ' as Printer") >= 0) {
					SQL = SQL.replace("' ' as Printer", "'" + loginBean.getEmpFullName() + "' as Printer");
				}
				paramListStr = new String[1];
				paramListStr[0] = billID;
			}

			for (int i = 0; i < paramListStr.length; i++) {
				String param = paramListStr[i];
				if (param.length() > 0 || setBean.getReportType().equals("PROCLIST")) {
					paramList.add(param);
				}
			}

			String url = "";
			if (setBean.getReportType().equals("TABLELIST")) {
				if (parentName != null && parentName.length() > 0) {// �и�����
					url = "/UserFunctionQueryAction.do?tableName=" + parentName;
				} else {
					url = "/UserFunctionQueryAction.do?tableName=" + reportNumber;
				}
			} else if (setBean.getReportType().equals("BILL")) {
				if (parentName != null && parentName.length() > 0) {// �и�����
					url = "/UserFunctionQueryAction.do?tableName=" + parentName;
				} else {
					url = "/UserFunctionQueryAction.do?tableName=" + billTable;
				}
				// ��ѯ�Ƿ����ӱ�������ӱ����Ҵ�����а������ӱ����ӱ��������Ҫ����
				int fromS = 0;
				for (int i = 0; i < defBean.getQueryFields().size(); i++) {
					ReportField rf = (ReportField) defBean.getQueryFields().get(i);
					if (rf.getFieldName().contains(" from ")) {
						int indexS = SQL.indexOf(rf.getFieldName());
						if (indexS > fromS) {
							fromS = indexS + rf.getFieldName().length();
						}
					}
				}
				ArrayList list = DDLOperation.getChildTables(billTable, BaseEnv.tableInfos);
				if (list != null && list.size() > 0) {
					DBTableInfoBean chlidBean = (DBTableInfoBean) list.get(0);
					if (SQL.indexOf("from" + chlidBean.getTableName(), fromS) > 0 && SQL.indexOf("order by ") < 0) {
						SQL += " order by " + chlidBean.getTableName() + ".detOrderNo ";
					}
				}
			} else {

				url = "/ReportDataAction.do?reportNumber=" + reportNumber;
			}
			MOperation mop = GlobalsTool.getMOperationMap(req);

			/*��ǩ��ӡ ������Ȩ����֤*/
			if (reportNumber != null && !reportNumber.startsWith("PrintSeq")) {
				if (setBean.getReportType().equals("BILL")) {
					scopeRight = mop.getScope(MOperation.M_ADD);
				} else {
					scopeRight = mop.getScope(MOperation.M_QUERY);
				}
			}
			ReportDataMgt mgt = new ReportDataMgt();
			// ����SQL��估ֵ�б���ʾ���� ��ѯ�����
			String type = "";
			if (setBean.getReportType().equals("BILL")) {
				type = "BILL";
			} else if (setBean.getReportType().equals("PROCLIST")) {
				type = "PROCLIST";
			}
			if (SQL.indexOf("where row_id between") > 0) {
				if (SQL.indexOf("select distinct") > -1) {
					SQL = SQL.substring(0, SQL.indexOf("where row_id between"));
				} else {
					SQL = SQL.substring(0, SQL.indexOf("where row_id between")) + " order by row_id ";
				}
			}
			SQL = SQL.replaceAll("@condition:id" , "'" + billID + "'");
			ArrayList sysParam = new ArrayList();
			ArrayList tabParam = new ArrayList();
			ArrayList sessParam = new ArrayList();
			ArrayList codeParam = new ArrayList();
			ArrayList queryParam = new ArrayList();
			HashMap tabMap = new HashMap();
			try {
				ConfigParse.parseSentenceGetParam(SQL, tabParam, sysParam, sessParam, codeParam, queryParam, null);
				HashMap sysMap = ConfigParse.getSystemParam(sysParam, loginBean.getSunCmpClassCode());
				HashMap sessMap = ConfigParse.getSessParam(loginBean.getId(), sessParam);
				
				Connection conn = null;
				HashMap codeMap = ConfigParse.getCodeParam(codeParam, conn);
				SQL = ConfigParse.parseSentencePutParam(SQL, null, sysMap, null, sessMap, codeMap, null, null, null);
			} catch (Exception e) {
				BaseEnv.log.error("ReportServlet.doGetPrintData Error,",e);
			}
			
			rs = mgt.getPrintData(defBean, SQL, paramList, GlobalsTool.getLocale(req).toString(), type, setBean.getShowTotalStat(), loginBean);
			if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				writeError("����SQL���ִ�д���",resp);
				return;
			}
			rows = (ArrayList) rs.getRetVal();
		}
		parentName = parentName.length() == 0 ? billTable : parentName;
		WorkFlowDesignBean workFlowBean = new OAWorkFlowTempMgt().getWorkFlowDesing(parentName);
		ParseReportBean bean = new ParseReportBean();
		// ����ѯ�Ľ��д����
		StringBuffer result = new StringBuffer();
		result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		result.append("<ReportData>");
		result.append("<FieldType");
		for (int i = 0; i < disFields.size(); i++) {
			ReportField field = (ReportField) disFields.get(i);
			result.append(" " + field.getAsFieldName() + "=\"" + field.getFieldType() + "\"");
		}
		//Ϊ��ʹ��ͷ�ж�����Ӧ���ֶΣ����԰�ÿ���ֶζ�=2
		if (workFlowBean != null) {
			bean.setPrintFieldType(result, workFlowBean.getFlowNodeMap());
		}
		HashMap conditionSessionMap = (HashMap) req.getSession().getAttribute("conditinSessionMap");
		String moduleNumber = "";
		String moduleType = req.getParameter("moduleType");
		if (moduleType != null && moduleType.trim().length() > 0) {
			moduleNumber = "-" + moduleType;
		}
		HashMap m = conditionSessionMap == null ? null : (HashMap) conditionSessionMap.get(reportNumber + moduleNumber);

		if (m != null)
			for (Object tmp : condField) {
				ReportField field = (ReportField) tmp;
				String value = (String) m.get(field.getAsFieldName());
				if (value == null) {
					value = "";
				} else {
					value = value.replaceAll("&nbsp;", " ");
					if (value.indexOf("&") >= 0) {
						value = value.replaceAll("&", "&amp;");
					}
					if (value.indexOf("\"") >= 0) {
						value = value.replaceAll("\"", "&quot;");
					}
					if (value.indexOf("<") >= 0) {
						value = value.replaceAll("<", "&lt;");
					}
					if (value.indexOf(">") >= 0) {
						value = value.replaceAll(">", "&gt;");
					}
					value.replaceAll("\n", "");
					value.replaceAll("\r", "");
				}
				result.append(" " + field.getAsFieldName() + "Value=\"" + value + "\"");
			}
		result.append("/>");
		result.append("<Rows>");

		/****************************�鿴��ǰ�������Щ�ֶ���Ҫ����*********************************************/
		String userLastNode = "";
		String currNodeId = "";
		String endAllowField = "";
		FlowNodeBean flowNodeBean = null;
		if (workFlowBean != null) {
			//�õ������ǰ���������������ô��ѯ���������ǰ�û�������ڵ�
			OAMyWorkFlowMgt oaMgt = new OAMyWorkFlowMgt();
			Result rst = oaMgt.getUserLastNode(billID, loginBean.getId(), billTable);

			if (rst.retCode == ErrorCanst.DEFAULT_SUCCESS && rst.retVal != null) {
				userLastNode = rst.retVal.toString();
			}

			rst = oaMgt.getCurrNodeId(billID, billTable);

			if (rst.retCode == ErrorCanst.DEFAULT_SUCCESS && rst.retVal != null) {
				currNodeId = rst.retVal.toString();
			}
			if (userLastNode == null || userLastNode.length() == 0) {
				userLastNode = currNodeId;
			}
			//������̽�������ǰ��¼���Ƿ���Բ鿴ĳЩ�ֶ�
			flowNodeBean = workFlowBean.getFlowNodeMap().get(userLastNode);
			if (flowNodeBean != null) {
				ArrayList hidFields = flowNodeBean.getHiddenFields();
				if (currNodeId.equals("-1") && hidFields != null && hidFields.size() > 0) {
					for (int i = 0; i < hidFields.size(); i++) {
						endAllowField += hidFields.get(i) + ",";
					}
				}
			}
		}
		/****************************�鿴��ǰ�������Щ�ֶ���Ҫ����*********************************************/

		for (int i = 0; i < rows.size(); i++) {
			HashMap map = (HashMap) rows.get(i);
			result.append("<Row ");
			for (int j = 0; j < disFields.size(); j++) {
				ReportField field = (ReportField) disFields.get(j);
				String fieldIdentity = field.getFieldIdentity();
				ArrayList<String[]> list = ConfigParse.getTableFieldByReportField(field.getFieldName());
				boolean flag = true;
				for (int k = 0; k < list.size(); k++) {
					Object[] obj2 = (Object[]) list.get(k);
					flag = DynDBManager.getViewRight(obj2[0].toString(), obj2[1].toString(), scopeRight, fieldIdentity, loginBean);
					if (!flag) {
						break;
					}
				}

				if (flag && userLastNode.length() > 0 && field.getFieldName().indexOf(".") > 0) {//������ֶ��в鿴Ȩ�ޣ���ôҪ��ѯ���û���������ڵ��Ƿ��ܲ鿴���ֶ�	
					String tableName = field.getFieldName().substring(0, field.getFieldName().indexOf("."));
					String fieldName = field.getFieldName().substring(field.getFieldName().indexOf(".") + 1);
					FieldBean fieldBean;
					String fieldStr = "";
					if (tableName.equals(parentName)) {
						fieldStr = fieldName;
					} else {
						fieldStr = field.getFieldName().replace(".", "_");
					}
					if (flowNodeBean != null && flowNodeBean.getFields() != null) {
						fieldBean = GlobalsTool.getFlowField(flowNodeBean.getFields(), fieldStr);
						if (fieldBean != null && fieldBean.getInputType() == DBFieldInfoBean.INPUT_HIDDEN && !endAllowField.contains(fieldStr)) {
							flag = false;
						}
					}
				}

				String value = map.get(field.getAsFieldName()) == null ? "" : map.get(field.getAsFieldName()).toString();
				value = value.replaceAll("\\|=", "");
				value = value.replaceAll("&nbsp;", " ");
				value = GlobalsTool.revertTextCode2(value);
				
				if (value.indexOf("&") >= 0) {
					value = value.replaceAll("&", "&amp;");
				}
				if (value.indexOf("'") >= 0) {
					value = value.replaceAll("'", "&apos;");
				}
				if (value.indexOf("\"") >= 0) {
					value = value.replaceAll("\"", "&quot;");
				}
				if (value.indexOf("<") >= 0) {
					value = value.replaceAll("<", "&lt;");
				}
				if (value.indexOf(">") >= 0) {
					value = value.replaceAll(">", "&gt;");
				}
				if (field.getFieldType() == DBFieldInfoBean.FIELD_PIC && field.getFieldName().indexOf("'") < 0) {
					
					String picPath = GlobalsTool.getSysSetting("picPath") ;
					if(picPath!=null && picPath.length() > 0){
						result.append(field.getAsFieldName() + "=\"" +  value + "\" ");
					}else{
						if (field.getFieldName().indexOf(".") > 0) {
							reportNumber = field.getFieldName().substring(0, field.getFieldName().indexOf("."));
						}
						result.append(field.getAsFieldName() + "=\"" + reportNumber + "/" + value.split(":")[0] + "\" ");
						//��ǰ���ϴ�����ͼ������:����������ʵ���ƺ���ʾ���ģ�
					}
					
				} else {
					if (!flag) {
						result.append(field.getAsFieldName() + "=\"\" ");
					} else {
						result.append(field.getAsFieldName() + "=\"" + value + "\" ");
					}
				}

			}
			if (workFlowBean != null) {
				setPrintFieldValue(result, workFlowBean.getFlowNodeMap(), billID);
			}
			result.append("/>");
		}
		result.append("</Rows>");
		result.append("</ReportData>");

		if ("1".equals(zip)) {
			ByteArrayInputStream byteInput = new ByteArrayInputStream(result.toString().getBytes("UTF-8"));
			resp.setContentType("application/x-zip-compressed");
			resp.setHeader("Content-Disposition", "inline; filename=" + reportNumber + ".zip");
			ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(resp.getOutputStream()));
			zipOut.putNextEntry(new ZipEntry(reportNumber + ".xml"));
			int readLen = 0;
			byte[] buffer = new byte[1024 * 8];
			while ((readLen = byteInput.read(buffer, 0, 1024 * 8)) != -1) {
				zipOut.write(buffer, 0, readLen);
			}
			byteInput.close();
			zipOut.flush();
			zipOut.close();
		} else {
			resp.setContentType("text/XML;charset=UTF-8");
			resp.setHeader("Cache-Control", "no-cache");
			PrintWriter out = resp.getWriter();
			out.print(result.toString());
			out.close();
		}
	}

	/**
	 * ���ڵõ���ӡ����ʱ�����������ڵ��ֵ
	 * @param result
	 * @param flowNodeBean
	 * @throws UnsupportedEncodingException 
	 */
	public void setPrintFieldValue(StringBuffer result, HashMap<String, FlowNodeBean> flowNodeBeans, String billID) throws UnsupportedEncodingException {
		Iterator it = flowNodeBeans.keySet().iterator();
		OAMyWorkFlowMgt mgt = new OAMyWorkFlowMgt();
		Result rs = mgt.getOAWFCheckInfo(billID);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			HashMap map = (HashMap) rs.retVal;
			while (it.hasNext()) {
				String id = it.next().toString();
				if (id.equals("0") || id.equals("-1")) {
					continue;
				}
				if (map.get(id) != null) {
					String pers = "";
					String ct = "";
					String de = "";
					String pic = "";
					ArrayList infos = (ArrayList) map.get(id);

					for (int i = 0; i < infos.size(); i++) {
						String[] info = (String[]) infos.get(i);
						pers += OnlineUserInfo.getUser(info[1]).getName() + ",";
						ct += info[2] + ",";
						de += info[3] + ",";
						pic = info[4];
					}
					FlowNodeBean flowBean = flowNodeBeans.get(id);
					result.append(" checkPerson" + flowBean.getKeyId() + "=\"" + pers.substring(0, pers.length() - 1) + "\"");
					result.append(" checkTime" + flowBean.getKeyId() + "=\"" + ct.substring(0, ct.length() - 1) + "\"");
					result.append(" deliverance" + flowBean.getKeyId() + "=\"" + de.substring(0, de.length() - 1) + "\"");
					pic = java.net.URLEncoder.encode(pic, "UTF-8");
					result.append(" sign" + flowBean.getKeyId() + "=\"tblAutograph/" + pic + "\"");
				} else {
					FlowNodeBean flowBean = flowNodeBeans.get(id);
					result.append(" checkPerson" + flowBean.getKeyId() + "=\"\"");
					result.append(" checkTime" + flowBean.getKeyId() + "=\"\"");
					result.append(" deliverance" + flowBean.getKeyId() + "=\"\"");
					result.append(" sign" + flowBean.getKeyId() + "=\"\"");
				}
			}
		}
	}

	public void doGetStyleReportFile(HttpServletRequest req, HttpServletResponse resp,OnlineUser onlineuser) throws ServletException, IOException {
		String BillTable = req.getParameter("BillTable");
		if (BillTable != null && BillTable.length() > 0) {
			doGetBillTableInfo(req, resp);
			return;
		}
		String fileName = req.getParameter("fileName");
		if (fileName == null || fileName.length() <= 0) {
			return;
		}

		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		boolean user = true;
		File file = new File(this.getUserReportPath(fileName));
		if (file == null || file.isFile() == false) {
			file = new File(this.getReportPath(fileName));
			user = false;
		}
		//���������г����ֶ�
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		byte[] bs = new byte[dis.available()];
		dis.read(bs);
		String fileStr = new String(bs);
		if (fileStr.indexOf("]") > 0 && fileStr.indexOf("[") > 0) {
			try {
				Locale locale = (Locale) req.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
				DefineReportBean bean = DefineReportBean.parse(fileName, locale == null ? "zh_CN" : locale.toString(),onlineuser.getId());
				String path = BaseEnv.REPORTPATH;
				if (!path.trim().endsWith("/")) {
					path = path.trim() + "/";
				}
				file = new File(path + "temp/" + fileName);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				String rStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> " + " <ReportDataSource>" + "  <Report reporteId=\"\" reportName=\"�������ϸ����\" /> " + " <fieldInfo>";
				for (ReportField rf : (ArrayList<ReportField>) (bean.getFieldInfo())) {
					String fs = "<field fieldname=\"" + toXMl(rf.getFieldName()) + "\" AsfieldName=\"" + toXMl(rf.getAsFieldName()) + "\" groupByFlag=\"" + rf.getGroupByFlag() + "\" displayFlag=\""
							+ rf.getDisplayFlag() + "\" condition=\"" + toXMl(rf.getCondition()) + "\" conditionJoin=\"" + rf.getConditionJoin() + "\" orderbyFlag=\"" + rf.getOrderByFlag()
							+ "\" display=\"" + toXMl(rf.getDisplay()) + "\" fieldType=\"" + rf.getFieldType() + "\" inputType=\"" + rf.getInputType() + "\" popUpName=\"" + toXMl(rf.getPopUpName())
							+ "\" width=\"" + rf.getWidth() + "\" classCode=\"" + rf.getClassCode() + "\" linkAdd=\"" + toXMl(rf.getLinkAdd()) + "\" order=\"" + rf.getOrder() + "\" isStat=\""
							+ rf.getIsStat() + "\" defaultValue=\"" + toXMl(rf.getDefaultValue()) + "\" fieldSysType=\"" + rf.getFieldSysType() + "\" fixColName=\"" + toXMl(rf.getFixColName())
							+ "\" isNull=\"" + rf.getIsNull() + "\" planarField=\"" + toXMl(rf.getPalnarField()) + "\" subSQL=\"" + toXMl(rf.getSubSQL()) + "\" repeatNotShow=\""
							+ rf.getRepeatNotShow() + "\" zeroDisplay=\"" + rf.getZeroDisplay() + "\" seriesCol=\"\" seriesNums=\"\" crossField=\"" + rf.getCrossField() + "\" groupName=\""
							+ toXMl(rf.getGroupName()) + "\" fieldIdentity=\"" + rf.getFieldIdentity() + "\" analysis=\"" + rf.getAnalysis() + "\">";
					fs += "<Displays>";
					fs += "<Display localStr=\"" + GlobalsTool.getLocale(req).toString() + "\" display=\"" + toXMl(rf.getDisplay()) + "\" />";
					fs += "</Displays>";
					if (rf.getGroupName() != null && rf.getGroupName().length() > 0) {
						fs += "<groupNames>";
						fs += "<Display localStr=\"" + GlobalsTool.getLocale(req).toString() + "\" display=\"" + toXMl(rf.getGroupName()) + "\" />";
						fs += "</groupNames>";
					} else {
						fs += "<groupNames />";
					}
					fs += "</field>";
					rStr += fs;
				}
				rStr += "</fieldInfo>  <SQL text=\"" + toXMl(bean.getSql()) + "\" />   <charts />   </ReportDataSource>";
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(rStr.getBytes("UTF-8"));
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		File file_update = null;

		//�ؼ����ĺ󣬽��˷�������ΪdoGetReportFile --2011-08-26	
		try {
			//����ǹ��������õ���ǰ����������˽ڵ㣬���ݽڵ�����ÿ���ڵ������ˣ����ʱ�䣬����������ǩ����ֶ�
			String reportNumber = fileName.replace("SQL.xml", "");
			Result rs = new ReportSetMgt().getReportSetInfo(reportNumber);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				ReportsBean bean = (ReportsBean) ((ArrayList) rs.retVal).get(0);
				WorkFlowDesignBean workFlowBean = new OAWorkFlowTempMgt().getWorkFlowDesing(bean.getBillTable());
				if (workFlowBean != null) {
					if (user) {
						file_update = new File(this.getUserReportPath(reportNumber + "_update.xml"));
					} else {
						file_update = new File(this.getReportPath(reportNumber + "_update.xml"));
					}

					FileInputStream in = new FileInputStream(file);
					FileOutputStream out = new FileOutputStream(file_update);
					byte[] b = new byte[1024];
					int count = 0;

					while ((count = in.read(b)) > -1) {
						out.write(b, 0, count);
					}
					out.close();
					in.close();

					ParseReportBean.parse(file_update, GlobalsTool.getLocale(req).toString(), workFlowBean);
					file = file_update;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//�ؼ����ĺ󣬽��˷�������ΪdoGetReportFile --2011-08-26

		FileInputStream in = new FileInputStream(file);
		ServletOutputStream out = resp.getOutputStream();

		byte[] b = new byte[1024];
		int count = 0;

		while ((count = in.read(b)) > -1) {
			out.write(b, 0, count);
		}
		out.close();
		in.close();

		if (file_update != null && file_update.exists()) {
			file_update.delete();
		}
	}

	private String toXMl(String str) {
		if (str == null) {
			return str;
		}
		return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
	}

	public void doGetReportFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String BillTable = req.getParameter("BillTable");
		if (BillTable != null && BillTable.length() > 0) {
			doGetBillTableInfo(req, resp);
			return;
		}
		String fileName = req.getParameter("fileName");
		if (fileName == null || fileName.length() <= 0) {
			return;
		}

		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		File file = new File(this.getUserReportPath(fileName));
		if (file == null || file.isFile() == false) {
			file = new File(this.getReportPath(fileName));
		}

		FileInputStream in = new FileInputStream(file);
		ServletOutputStream out = resp.getOutputStream();

		byte[] b = new byte[1024];
		int count = 0;

		while ((count = in.read(b)) > -1) {
			out.write(b, 0, count);
		}
		out.close();
		in.close();
	}

	public void doGetFormatFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String BillTable = req.getParameter("BillTable");
		if (BillTable != null && BillTable.length() > 0) {
			doGetBillTableInfo(req, resp);
			return;
		}
		String fileName = req.getParameter("fileName");
		String reportNumber = req.getParameter("reportNumber");
		if ((fileName == null || fileName.length() <= 0) && (reportNumber == null || reportNumber.length() <= 0)) {
			return;
		}
		ReportSetMgt mgt = new ReportSetMgt();
		String language = req.getParameter("Language");
		if (reportNumber != null && reportNumber.length() > 0) {
			Result rss = mgt.getFormatNameByReportNumber(reportNumber, language);
			if (rss.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
				fileName = (String) rss.getRetVal();
			} else {
				return;
			}
		}

		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		if (language != null && language.length() > 0 && !"zh_CN".equals(language)) {
			fileName = language + "/" + fileName;
		}
		File file = new File(this.getUserPath(fileName));
		if (file == null || file.isFile() == false || !file.exists()) {
			file = new File(this.getPath(fileName));
		}
		FileInputStream in = new FileInputStream(file);
		ServletOutputStream out = resp.getOutputStream();

		byte[] b = new byte[1024];
		int count = 0;

		while ((count = in.read(b)) > -1) {
			out.write(b, 0, count);
		}
		out.close();
		in.close();

	}
	
	public void getDefineFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		String fileName = req.getParameter("fileName");
		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		
		//���user_config����û��ͬ���ļ�
		String fn = fileName;
		fileName = fileName.replace('\\', '/');
		if(fileName.indexOf("/")> 0){
			fn = fileName.substring(fileName.lastIndexOf("/")+1);
		}
		File file = new File("../../user_config/"+fn);
		if(!file.exists()){
			file = new File("../../"+fileName);
		}
		BaseEnv.log.debug("ReportServlet ��define�ļ�:"+file.getAbsolutePath());
		ServletOutputStream out = resp.getOutputStream();
		
		if(file.exists()){
			FileInputStream in = new FileInputStream(file);
			byte[] b = new byte[1024];
			int count = 0;
			while ((count = in.read(b)) > -1) {
				out.write(b, 0, count);
			}
			in.close();
		}else{
			out.write("<?xml version=\"1.0\" encoding=\"gb2312\"?>\r\n<defineSqls>\r\n\r\n</defineSqls>".getBytes("GB2312"));
		}
		out.close();
		

	}
	public void saveDefineFile(HttpServletRequest req, HttpServletResponse resp,OnlineUser user) throws ServletException, IOException {
		String fileName = req.getParameter("fileName");
		if (fileName == null || fileName.length() <= 0) {
			writeError("�ϴ��ļ�fileName����Ϊ��",resp);
			return;
		}
		fileName = fileName.replace('\\', '/');
		String fn = fileName.substring(fileName.lastIndexOf("/")+1);
		String bakFileName = "../temp/"+(fn.substring(0,fn.indexOf(".")))+"_bak.xml";//�����ϴ����ļ������ڼ���ļ���ȷ��
		File bakfile = new File(bakFileName);	
		
		File file = null;	
		if ("0".equals(SystemState.instance.dogId)) {
			//�����������fileName����·��
			if(fileName.indexOf("/") > 0){
				file = new File("../../"+fileName);
			}else{
				file = new File(BaseEnv.DEFINEPATH+"/define/"+fileName);
			}
		} else {
			//�û�����fileName����·��
			if(fileName.indexOf("/") > 0){				
				file = new File("../../user_config/"+fn);
			}else{			
				file = new File("../../user_config/"+fileName);
			}
		}
		if(file.exists() && !file.canWrite()){
			//���file�ļ��Ƿ��д
			writeError("�ļ�ֻ�������޸ģ�������������ִ��checkout",resp);
			return;
		}
		if(!bakfile.getParentFile().exists()){
			bakfile.getParentFile().mkdirs();
		}
		

		try {
			OutputStream out = new FileOutputStream(bakFileName);
			ServletInputStream in = req.getInputStream();
			byte[] bs = new byte[1024];
			byte[] b = new byte[0];
			int count = 0;
			while ((count = in.read(bs)) > -1) {
				byte[] temp = b;
				b = new byte[temp.length + count];
				System.arraycopy(temp, 0, b, 0, temp.length);
				System.arraycopy(bs, 0, b, temp.length, count);
			}
			String ret  = new String(b,"utf-8").trim();
			//����ļ�ͷ����<?xml version="1.0" encoding="gb2312"?>,���Ϊ���
			if(ret.startsWith("<?xml")){
				ret = "<?xml version=\"1.0\" encoding=\"gb2312\"?>"+ret.substring(ret.indexOf("?>")+2);
			}
			
			out.write(ret.getBytes("GB2312"));
			out.close();
			in.close();
			
			//����ϴ��ļ���ȷ��
			HashMap sqlMap = new HashMap();
	        HashMap pathMap = new HashMap();
	        boolean r = SqlConfig.parse(sqlMap,pathMap, bakFileName);
	        if (!r){	        	
				writeError("�ϴ��ļ����ݴ��������ļ���ʽ������",resp);
				return;
	        }
	        if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
	        
			//�ѱ����ļ�������ʽ�ļ�
	        out = new FileOutputStream(file);
	        FileInputStream inf = new FileInputStream(bakFileName);
			bs = new byte[1024];
			count = 0;
			while ((count = inf.read(bs)) > -1) {
				out.write(bs, 0, count);
			}
			out.close();
			in.close();		
			// ɾ��ԭ�����ļ�
			bakfile.delete();
			
			BaseEnv.defineSqlFiles.add(file.getAbsolutePath());
			SqlConfig.parse(BaseEnv.defineSqlMap ,pathMap, file.getAbsolutePath());

			String msg = "�޸��Զ����ļ�"+fileName;
			new DynDBManager().addLog(1, user.getId(), user.getName(), "00001",
					 msg, "", "�Զ����ļ�","");
			
			writeError("����ɹ�",resp);
			
		} catch (IOException ex) {
			if (bakfile.exists()) {
				bakfile.delete();
			}
			writeError("����ʧ��"+ex.getMessage(),resp);
			throw ex;
		}

	}

	public void doGetSysType(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Hashtable map = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		if (map == null)
			return;

		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		PrintWriter out = resp.getWriter();

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<SysType>");
		//�ֶ�ϵͳ����
		out.println("<FieldSysType>");
		List list = GlobalsTool.getEnumerationItems("FieldSysType", GlobalsTool.getLocale(req).toString());
		for (int i = 0; i < list.size(); i++) {
			KeyPair kp = (KeyPair) list.get(i);
			out.println("<Item name=\"" + kp.getValue() + "\" display=\"" + kp.getName() + "\"/>");
		}
		out.println("</FieldSysType>");
		//��������
		out.println("<inputType>");
		list = GlobalsTool.getDS("inputType", GlobalsTool.getLocale(req).toString(), this.getMessageResources(req));
		for (int i = 0; i < list.size(); i++) {
			KeyPair kp = (KeyPair) list.get(i);
			out.println("<Item name=\"" + kp.getValue() + "\" display=\"" + kp.getName() + "\"/>");
		}
		out.println("</inputType>");

		//�ֶ�����	
		out.println("<fieldType>");
		list = GlobalsTool.getDS("fieldType", GlobalsTool.getLocale(req).toString(), this.getMessageResources(req));
		for (int i = 0; i < list.size(); i++) {
			KeyPair kp = (KeyPair) list.get(i);
			out.println("<Item name=\"" + kp.getValue() + "\" display=\"" + kp.getName() + "\"/>");
		}
		out.println("</fieldType>");

		//�ֶα�ʾ
		out.println("<fieldIdentityStr>");
		list = GlobalsTool.getEnumerationItems("fieldIdentityStr", GlobalsTool.getLocale(req).toString());
		for (int i = 0; i < list.size(); i++) {
			KeyPair kp = (KeyPair) list.get(i);
			out.println("<Item name=\"" + kp.getValue() + "\" display=\"" + kp.getName() + "\"/>");
		}
		out.println("</fieldIdentityStr>");
		out.println("</SysType>");
		out.close();
	}

	public MessageResources getMessageResources(HttpServletRequest req) {
		Object o = req.getSession().getServletContext().getAttribute("res.DynamicResource");
		if (o instanceof MessageResources) {
			return (MessageResources) o;
		} else {
			return null;
		}
	}

	public void doGetTableInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Hashtable map = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		if (map == null)
			return;

		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		PrintWriter out = resp.getWriter();

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<tableInfo>");

		for (Object o : map.values()) {
			DBTableInfoBean tableInfo = (DBTableInfoBean) o;
			printTableInfo(tableInfo, out, "");// �����ṹ
		}
		out.println("</tableInfo>"); 
		out.close();
	}

	/**
	 * ��ṹ�ͱ��������� tableName ���� sendData (0=��ṹ,1=������) fieldList �����ֶ�(�ֶ�֮ǰ�ö��ŷֿ�)
	 * condition ��ѯ����
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getTableData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//����ӿ�����ʱȡ���б���������ݣ��е�Σ�գ�������ʱû�ã������Ρ�
		if (true) {
			return;
		}
		Hashtable map = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		if (map == null)
			return;

		String tableName = req.getParameter("tableName");
		String sendData = req.getParameter("sendData");
		String fieldList = req.getParameter("fieldList");
		String condition = req.getParameter("condition");

		if ("0".equals(sendData)) { /* ֻ���ر�ṹ */
			PrintWriter out = resp.getWriter();
			resp.setContentType("text/XML;   charset=UTF-8");
			resp.setHeader("Cache-Control", "no-cache");
			out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.print("<tableData>");
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
			if (tableInfo != null) {
				printTableInfo(tableInfo, out, fieldList);// �����ṹ
			}
			out.print("</tableData>");
			out.close();
		} else { /* ���ر�ṹ�ͱ����� */
			StringBuffer tableData = new StringBuffer();
			tableData.append(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
			tableData.append(("<tableData>"));
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map, tableName);
			if (tableInfo != null) {
				String strTable = getTableInfoLength(tableInfo, fieldList);
				tableData.append(strTable);
				tableData.append(("<data>"));
				Result result = new ReportDataMgt().getTableData(tableInfo, fieldList, condition);
				if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					tableData.append(result.getRetVal());
				}
				tableData.append(("</data>"));
			}
			tableData.append(("</tableData>"));
			ByteArrayInputStream byteInput = new ByteArrayInputStream(tableData.toString().getBytes("UTF-8"));
			resp.setContentType("application/x-zip-compressed");
			resp.setHeader("Content-Disposition", "inline; filename=" + tableName + ".zip");
			ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(resp.getOutputStream()));
			zipOut.putNextEntry(new ZipEntry(tableName + ".xml"));
			int readLen = 0;
			byte[] buffer = new byte[1024 * 8];
			while ((readLen = byteInput.read(buffer, 0, 1024 * 8)) != -1) {
				zipOut.write(buffer, 0, readLen);
			}
			byteInput.close();
			zipOut.flush();
			zipOut.close();
		}
	}
	
	/**
	 * ����ӿ��ɿͻ������ɱ�дsql,�����ؽ��
	 * @param req
	 * @param resp
	 * @param user
	 * @throws ServletException
	 * @throws IOException
	 */
	public void defineProc(HttpServletRequest req, HttpServletResponse resp,OnlineUser user) throws ServletException, IOException {
		

		
		ServletInputStream in = req.getInputStream();
		byte[] bs =new  byte[0];
		byte b[] =  new byte[1024];
		int count = 0;
		while((count = in.read(b)) > -1){
			byte[] temp = bs;
			bs = new byte[temp.length + count];
			System.arraycopy(temp, 0, bs, 0, temp.length);
			System.arraycopy(b, 0,bs, temp.length, count);
		}
		
		String sql  = new String(bs,"UTF-8");
		sql = sql.trim();
		sql = des.Decode(strDefaultKey, sql);
		sql = sql.trim();
		
		BaseEnv.log.debug("ReportServlet.defineProc �Զ���ִ�д洢���̣�"+sql);
		PublicMgt mgt = new PublicMgt();
		Result rs = mgt.execProc(sql);
		String retStr= "";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			if(rs.retVal instanceof Object[]){
				Object[] os = (Object[])rs.retVal;
				HashMap outputMap = (HashMap)os[0];
				ArrayList result = (ArrayList)os[0];
				for(Object o :result){
					HashMap rowMap = (HashMap)o;
					String rowStr = "";
					for(Object rowkey :rowMap.keySet()){
						String value = String.valueOf(rowMap.get(rowkey));
						value = value.replaceAll("<", "&lt;");
						value = value.replaceAll(">", "&gt;");
						value = value.replaceAll("\"", "&quot;") ;
						value = value.replaceAll("'", "&apos;") ;
						value = value.replaceAll("&", "&amp;") ;						
						rowStr +=" "+rowkey+"=\""+value+"\"";
					}
					retStr += "<row "+rowStr+"/>"; 
				}
				
				String outStr = "";
				for(Object rowkey :outputMap.keySet()){
					String value = String.valueOf(outputMap.get(rowkey));
					value = value.replaceAll("<", "&lt;");
					value = value.replaceAll(">", "&gt;");
					value = value.replaceAll("\"", "&quot;") ;
					value = value.replaceAll("'", "&apos;") ;
					value = value.replaceAll("&", "&amp;") ;						
					outStr +=" "+rowkey+"=\""+value+"\"";
				}
				
				retStr = "<data result='ok' "+outStr+">"+retStr+"</data>";
			}
		}else{
			String msg = rs.retVal+"";
			if(msg ==null || msg.length() ==0){
				msg = "ִ�д���";
			}
			retStr = "<data result='error'>"+msg+"</data>";
		}
		
		retStr  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + retStr;		
		
		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");  
		PrintWriter out = resp.getWriter();
		out.print(retStr);
		out.close();
	}
	/**
	 * ����ӿ��ɿͻ������ɱ�дsql,�����ؽ��
	 * @param req
	 * @param resp
	 * @param user
	 * @throws ServletException
	 * @throws IOException
	 */
	public void defineSQL(HttpServletRequest req, HttpServletResponse resp,OnlineUser user) throws ServletException, IOException {
		
		String nosec=req.getParameter("nosec");
		
		ServletInputStream in = req.getInputStream();
		byte[] bs =new  byte[0];
		byte b[] =  new byte[1024];
		int count = 0;
		while((count = in.read(b)) > -1){
			byte[] temp = bs;
			bs = new byte[temp.length + count];
			System.arraycopy(temp, 0, bs, 0, temp.length);
			System.arraycopy(b, 0,bs, temp.length, count);
		}
		
		String sql  = new String(bs,"UTF-8");
		sql = sql.trim();
		if(!"true".equals(nosec)){
			sql = des.Decode(strDefaultKey,sql);
			sql = sql.trim();
		}
		
		BaseEnv.log.debug("ReportServlet.defineSQL �Զ�����䣺"+sql);
		PublicMgt mgt = new PublicMgt();
		Result rs = mgt.exec(sql);
		String retStr= "";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			if(rs.retVal instanceof List){
				ArrayList result = (ArrayList)rs.retVal;
				for(Object o :result){
					HashMap rowMap = (HashMap)o;
					String rowStr = "";
					for(Object rowkey :rowMap.keySet()){
						String value = String.valueOf(rowMap.get(rowkey));
						value = value.replaceAll("<", "&lt;");
						value = value.replaceAll(">", "&gt;");
						value = value.replaceAll("\"", "&quot;") ;
						value = value.replaceAll("'", "&apos;") ;
						value = value.replaceAll("&", "&amp;") ;						
						rowStr +=" "+rowkey+"=\""+value+"\"";
					}
					retStr += "<row "+rowStr+"/>"; 
				}
				retStr = "<data result='ok'>"+retStr+"</data>";
			}else{
				retStr = "<data result='ok'>"+rs.retVal+"</data>";
			}
		}else{
			String msg = rs.retVal+"";
			if(msg ==null || msg.length() ==0){
				msg = "ִ�д���";
			}
			retStr = "<data result='error'>"+msg+"</data>";
		}
		
		retStr  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + retStr;		
		
		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");  
		PrintWriter out = resp.getWriter();
		out.print(retStr);
		out.close();
	}

	/**
	 * �ϴ����۵�
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void upLoadRetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.print("<reatail>");
		try {
			//File file = new File("C:\\demo.xml");
			ServletInputStream in = req.getInputStream();
			/*
			 * FileOutputStream fo = new FileOutputStream(file) ; byte[] bs =
			 * new byte[1024]; int count = 0; while ((count = in.read(bs)) > -1) {
			 * fo.write(bs, 0, count); }
			 */
			/* �������۵�xml�ļ� */
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			List<ReatailBean> listBean = ParseReatail.parse(document);
			Hashtable allTables = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
			UserFunctionMgt mgt = new UserFunctionMgt();
			DBTableInfoBean tableInfo = DDLOperation.getTableInfo(allTables, "tblSalesOutStock");
			Locale locale = GlobalsTool.getLocale(req);
			String addMessage = this.getMessageResources(req, "common.lb.add");
			Object ob = req.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
			MessageResources resources = null;
			if (ob instanceof MessageResources) {
				resources = (MessageResources) ob;
			}
			Hashtable props = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
			MOperation mop = GlobalsTool.getMOperationMap(req);

			ReportDataMgt dataMgt = new ReportDataMgt();
			for (ReatailBean bean : listBean) {
				LoginBean login = queryLoginBeanByUserId(bean.getEmployeeId(), GlobalsTool.getLocale(req));
				if (login == null) {
					continue;
				}
				/* �ŵ���Ϣ */
				//String[] shop = (String[]) dataMgt.queryReatailShopByID(
				//		bean.getShopId()).getRetVal();
				//if (shop == null || shop[0] == null) {
				//	out.println("<item id=\"" + bean.getId()
				//			+ "\" retCode=\"1\" result=\"\"/>");
				//	continue;
				//}
				String[] shop = null;
				/* �ж����۵��Ƿ��Ѿ��ϴ� */
				Result result = dataMgt.queryExistReatailByID(bean.getId(), bean.getShopId());
				if (result.retCode == ErrorCanst.DEFAULT_FAILURE) {
					out.println("<item id=\"" + bean.getId() + "\" retCode=\"2\" result=\"\"/>");
					continue;
				}
				/* ת�������۳�����Ҫ������ */
				HashMap values = ParseReatail.convertSalesOutStockData(bean, allTables, shop, login);
				// ����Ĭ��ֵ
				for (DBFieldInfoBean field : tableInfo.getFieldInfos()) {
					if (values.get(field.getFieldName()) == null) {
						if (field.getFieldType() == 5) {
							field.setDefaultValue(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
						} else if (field.getFieldType() == 6) {
							field.setDefaultValue(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
						}
						values.put(field.getFieldName(), field.getDefValue());
					}
				}
				values.put("CardNO", bean.getCardNO());
				/* ������ӵ��ݽӿ� */
				result = mgt.add(tableInfo, values, login, "", allTables, "", "", locale, addMessage, resources, props, mop, "");
				if (result.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					out.println("<item id=\"" + bean.getId() + "\" retCode=\"1\" result=\"" + result.getRetCode() + "\"/>");
					continue;
				}
				/* ���»�Ա������ */
				//result = dataMgt.updateCardConsumeIntegral(bean.getCardNO(),Double.parseDouble(bean.getDiscountMoney()));
				out.println("<item id=\"" + bean.getId() + "\" retCode=\"0\" result=\"\"/>");
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("</reatail>");
	}

	/**
	 * ��ȡ�û���Ϣ
	 * 
	 * @param userId
	 * @return
	 */
	public LoginBean queryLoginBeanByUserId(String userId, Locale local) {
		Result result = new UserMgt().detail(userId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			EmployeeBean bean = (EmployeeBean) result.getRetVal();
			LoginBean login = new LoginBean(bean.getId(), bean.getSysName());
			login.setDepartCode(bean.getDepartmentCode());
			login.setSunCmpClassCode(bean.getSCompanyID());
			login.setEmpFullName(bean.getEmpFullName());
			login.setDefStyle(bean.getDefSys());

			Hashtable sessionSet = BaseEnv.sessionSet;
			if (sessionSet.get(login.getId()) == null) {
				Hashtable sess = new Hashtable();
				sess.put("SCompanyID", login.getSunCmpClassCode());
				//��ǰ��¼��֧�����Ļ���ڼ�
				String nowPeriod;
				int nowYear = -1;
				int nowMonth = -1;
				AccPeriodBean accBean = null;
				Result rs = new SysAccMgt().getCurrPeriod(login.getSunCmpClassCode());
				if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
					nowPeriod = "-1";
				} else {
					accBean = (AccPeriodBean) rs.getRetVal();
					nowPeriod = String.valueOf(accBean.getAccPeriod());
					nowYear = accBean.getAccYear();
					nowMonth = accBean.getAccMonth();
				}
				int nowDay = Calendar.getInstance().get(Calendar.DATE);

				sess.put("AccPeriod", accBean);
				sess.put("NowPeriod", nowPeriod);
				sess.put("NowYear", nowYear);
				sess.put("NowMonth", nowMonth);
				sess.put("NowDay", nowDay);
				sess.put("UserId", login.getId());
				sess.put("UserName", login.getEmpFullName());
				sess.put("Local", local);
				sess.put("BillOper", "");
				sessionSet.put(login.getId(), sess);
			}
			return login;
		}
		return null;
	}

	/**
	 * ��ȡ��Դ�ļ�
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public String getMessageResources(HttpServletRequest request, String key) {
		String value = "";
		try {
			Object o = request.getSession().getServletContext().getAttribute("userResource");
			if (o instanceof MessageResources) {
				MessageResources resources = (MessageResources) o;
				value = resources.getMessage(GlobalsTool.getLocale(request), key);
			}
			if (value == null || value == "") {
				o = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
				if (o instanceof MessageResources) {
					MessageResources resources = (MessageResources) o;
					value = resources.getMessage(GlobalsTool.getLocale(request), key);
				}
			}
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * ��ȡ��Դ�ļ�
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public String getMessageResources(HttpServletRequest request, String key, String param1) {
		String value = "";
		try {
			Object o = request.getSession().getServletContext().getAttribute("userResource");
			if (o instanceof MessageResources) {
				MessageResources resources = (MessageResources) o;
				value = resources.getMessage(GlobalsTool.getLocale(request), key, param1);
			}
			if (value == null || value == "") {
				o = request.getSession().getServletContext().getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
				if (o instanceof MessageResources) {
					MessageResources resources = (MessageResources) o;
					value = resources.getMessage(GlobalsTool.getLocale(request), key, param1);
				}
			}
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * ��ȡ���б���Ϣ
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getTablList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Hashtable map = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
		if (map == null)
			return;

		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		PrintWriter out = resp.getWriter();

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<tablList>");

		for (Object o : map.values()) {
			DBTableInfoBean tableInfo = (DBTableInfoBean) o;
			out.println("<table id=\"" + tableInfo.getId() + "\" tableName=\"" + tableInfo.getTableName() + "\">");
			out.println("<Displays>");
			if (tableInfo.getDisplay() != null) {
				for (Object display : tableInfo.getDisplay().map.keySet()) {
					String key = (String) display;
					out.println("<Display id=\"" + tableInfo.getDisplay().getId() + "\" localStr=\"" + key + "\" display=\""
							+ (tableInfo.getDisplay().get(key) == null ? "" : tableInfo.getDisplay().get(key).toString().replace("&", "&amp;")) + "\"/> ");
				}
			}
			out.println("</Displays>");
			out.println("</table>");
		}
		out.println("</tablList>");
		out.close();
	}

	private void printTableInfo(DBTableInfoBean tableInfo, PrintWriter out, String fieldList) {
		out.println("<table id=\"" + tableInfo.getId() + "\" tableName=\"" + tableInfo.getTableName() + "\" tableType=\"" + tableInfo.getTableType() + "\" perantTableName=\""
				+ tableInfo.getPerantTableName() + "\" udType=\"" + tableInfo.getUdType() + "\" updateAble=\"" + tableInfo.getUpdateAble() + "\" createBy=\"" + tableInfo.getCreateBy()
				+ "\" createTime=\"" + tableInfo.getCreateTime() + "\" lastUpdateBy=\"" + tableInfo.getLastUpdateBy() + "\" lastUpdateTime=\"" + tableInfo.getLastUpdateTime() + "\" approveFlow=\""
				+ tableInfo.getApproveFlow() + "\" approveField=\"" + tableInfo.getApproveField() + "\" classFlag=\"" + tableInfo.getClassFlag() + "\" draftFlag=\"" + tableInfo.getDraftFlag()
				+ "\" >");
		out.println("<Displays>");
		if (tableInfo.getDisplay() == null) {
			out.println(("<Display id=\"" + tableInfo.getTableName() + "\" localStr=\"zh_CN" + "\" display=\"" + (tableInfo.getTableName().replace("&", "&amp;")) + "\"/>"));
		} else {
			for (Object display : tableInfo.getDisplay().map.keySet()) {
				String key = (String) display;
				out.println(("<Display id=\"" + tableInfo.getDisplay().getId() + "\" localStr=\"" + key + "\" display=\""
						+ (tableInfo.getDisplay().map.get(key) == null ? "" : tableInfo.getDisplay().map.get(key).toString().replace("&", "&amp;")) + "\"/>"));
			}
		}
		out.println("</Displays>");
		out.println("<fieldInfo>");
		for (Object field : tableInfo.getFieldInfos()) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) field;
			if (fieldList != null && !"".equals(fieldList.trim())) {
				if (!fieldList.contains(fieldInfo.getFieldName() + ",")) {
					continue;
				}
			}
			out.println("<field id=\"" + fieldInfo.getId() + "\" fieldName=\"" + fieldInfo.getFieldName() + "\" listOrder=\"" + fieldInfo.getListOrder() + "\" isNull=\"" + fieldInfo.getIsNull()
					+ "\" isUnique=\"" + fieldInfo.getIsUnique() + "\" isStat=\"" + fieldInfo.getIsStat() + "\" defaultValue=\"" + fieldInfo.getDefaultValue() + "\" fieldType=\""
					+ fieldInfo.getFieldType() + "\" maxLength=\"" + fieldInfo.getMaxLength() + "\" width=\"" + fieldInfo.getWidth() + "\" inputType=\"" + fieldInfo.getInputType()
					+ "\" refEnumerationName=\"" + fieldInfo.getRefEnumerationName() + "\" inputValue=\"" + fieldInfo.getInputValue() + "\" fieldSysType=\"" + fieldInfo.getFieldSysType()
					+ "\" udType=\"" + fieldInfo.getUdType() + "\">");
			out.println("<Displays>");
			if (!tableInfo.getTableName().startsWith("Flow_")) { //�ж��Ƿ��Ǹ��Ի���������ɵı�
				if (fieldInfo.getDisplay() != null) {
					for (Object display : fieldInfo.getDisplay().map.keySet()) {
						String key = (String) display;
						out.println("<Display id=\"" + fieldInfo.getDisplay().getId() + "\" localStr=\"" + key + "\" display=\""
								+ (fieldInfo.getDisplay().map.get(key) == null ? "" : fieldInfo.getDisplay().map.get(key).toString().replace("&", "&amp;")) + "\"/>");
					}
				}
			} else {
				String[] localList = new String[] { "zh_CN", "zh_TW", "en" };
				for (String local : localList) {
					String str = "<Display id=\"" + fieldInfo.getId() + "\" localStr=\"" + local + "\" display=\"" + fieldInfo.getLanguageId() + "\"/>";
					out.println(str);
				}
			}
			out.println("</Displays>");

			out.println("</field>");
		}
		out.println("</fieldInfo>");
		out.println("</table>");

	}

	private String getTableInfoLength(DBTableInfoBean tableInfo, String fieldList) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table id=\"" + tableInfo.getId() + "\" tableName=\"" + tableInfo.getTableName().toUpperCase() + "\" tableType=\"" + tableInfo.getTableType() + "\" perantTableName=\""
				+ tableInfo.getPerantTableName() + "\" udType=\"" + tableInfo.getUdType() + "\" updateAble=\"" + tableInfo.getUpdateAble() + "\" createBy=\"" + tableInfo.getCreateBy()
				+ "\" createTime=\"" + tableInfo.getCreateTime() + "\" lastUpdateBy=\"" + tableInfo.getLastUpdateBy() + "\" lastUpdateTime=\"" + tableInfo.getLastUpdateTime() + "\" approveFlow=\""
				+ tableInfo.getApproveFlow() + "\" approveField=\"" + tableInfo.getApproveField() + "\" classFlag=\"" + tableInfo.getClassFlag() + "\" draftFlag=\"" + tableInfo.getDraftFlag()
				+ "\" >\n");
		sb.append("<Displays>\n");
		if (tableInfo.getDisplay() == null) {
			sb
					.append(("<Display id=\"" + tableInfo.getTableName() + "\" localStr=\"zh_CN" + "\" display=\""
							+ (tableInfo.getTableName() == null ? "" : tableInfo.getTableName().replace("&", "&amp;")) + "\"/>\n"));
		} else {
			for (Object display : tableInfo.getDisplay().map.keySet()) {
				String key = (String) display;
				sb.append(("<Display id=\"" + tableInfo.getDisplay().getId() + "\" localStr=\"" + key + "\" display=\""
						+ (tableInfo.getDisplay().map.get(key) == null ? "" : tableInfo.getDisplay().map.get(key).toString().replace("&", "&amp;")) + "\"/>\n"));
			}
		}
		sb.append("</Displays>\n");
		sb.append("<fieldInfo>\n");
		for (Object field : tableInfo.getFieldInfos()) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) field;
			if (fieldList != null && !"".equals(fieldList.trim())) {
				if (!fieldList.contains(fieldInfo.getFieldName() + ",")) {
					continue;
				}
			}
			sb.append("<field id=\"" + fieldInfo.getId() + "\" fieldName=\"" + fieldInfo.getFieldName().toUpperCase() + "\" listOrder=\"" + fieldInfo.getListOrder() + "\" isNull=\""
					+ fieldInfo.getIsNull() + "\" isUnique=\"" + fieldInfo.getIsUnique() + "\" isStat=\"" + fieldInfo.getIsStat() + "\" defaultValue=\"" + fieldInfo.getDefaultValue()
					+ "\" fieldType=\"" + fieldInfo.getFieldType() + "\" maxLength=\"" + fieldInfo.getMaxLength() + "\" width=\"" + fieldInfo.getWidth() + "\" inputType=\"" + fieldInfo.getInputType()
					+ "\" refEnumerationName=\"" + fieldInfo.getRefEnumerationName() + "\" inputValue=\"" + fieldInfo.getInputValue() + "\" fieldSysType=\"" + fieldInfo.getFieldSysType()
					+ "\" udType=\"" + fieldInfo.getUdType() + "\">\n");
			sb.append("<Displays>\n");
			if (!tableInfo.getTableName().startsWith("Flow_")) { //�ж��Ƿ��Ǹ��Ի���������ɵı�
				if (fieldInfo.getDisplay() != null) {
					for (Object display : fieldInfo.getDisplay().map.keySet()) {
						String key = (String) display;
						sb.append("<Display id=\"" + fieldInfo.getDisplay().getId() + "\" localStr=\"" + key + "\" display=\""
								+ (fieldInfo.getDisplay().map.get(key) == null ? "" : fieldInfo.getDisplay().map.get(key).toString().replace("&", "&amp;")) + "\"/>\n");
					}
				}
			} else {

				String[] localList = new String[] { "zh_CN", "zh_TW", "en" };
				for (String local : localList) {

					sb.append("<Display id=\"" + fieldInfo.getId() + "\" localStr=\"" + local + "\"  display=\"" + fieldInfo.getLanguageId() + "\"/>");
				}
			}
			sb.append("</Displays>\n");

			sb.append("</field>\n");
		}
		sb.append("</fieldInfo>\n");
		sb.append("</table>\n");
		return sb.toString();
	}

	private static String getPath(String fileName) {
		String path = BaseEnv.FILESERVERPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		path += "report/" + fileName;
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (Exception ex) {
			}
		}
		return path;
	}

	private static String getUserPath(String fileName) {
		String path = BaseEnv.FILESERVERPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		path += "userReport/" + fileName;
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (Exception ex) {
			}
		}
		// path=dir + fileName;
		return path;
	}

	private static String getReportPath(String fileName) {
		String path = BaseEnv.REPORTPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		String dir = path;
		File file = new File(dir);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception ex) {
			}
		}
		path = dir + fileName;
		return path;
	}

	private static String getUserReportPath(String fileName) {
		String path = BaseEnv.USERREPORTPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		String dir = path;
		File file = new File(dir);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception ex) {
			}
		}
		path = dir + fileName;
		return path;
	}

	private static String getUploadWokeFlowPath(String fileName) {
		String path = BaseEnv.FILESERVERPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		String dir = path += "wokeflow/upload/";
		File file = new File(dir);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception ex) {
			}
		}
		path = dir + fileName;
		return path;
	}

	private static String getWokeFlowPath(String fileName) {
		String path = BaseEnv.FILESERVERPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		String dir = path += "wokeflow/";
		File file = new File(dir);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception ex) {
			}
		}
		path = dir + fileName;
		return path;
	}

	private static String getUserWokeFlowPath(String fileName) {
		String path = BaseEnv.FILESERVERPATH;
		if (!path.trim().endsWith("/")) {
			path = path.trim() + "/";
		}
		String dir = path += "userWokeflow/";
		File file = new File(dir);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception ex) {
			}
		}
		path = dir + fileName;
		return path;
	}

	public void getLinkReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/XML;   charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();

		String reportNumber = request.getParameter("reportNumber");
		String filedName = request.getParameter("fieldName");
		filedName = new String(filedName.getBytes("ISO-8859-1"));
		String[] filedNames = null;
		if (filedName != null) {
			filedNames = filedName.split("string;");
		}
		ReportSetMgt setMgt = new ReportSetMgt();
		String locale = GlobalsTool.getLocale(request).toString();
		Result rs = setMgt.getReportSetInfo(reportNumber, locale);
		if (rs.retVal == null) {
			writeError("��������Ҳ�����¼"+reportNumber,response);
			return;
		}
		ReportsBean setBean = (ReportsBean) rs.getRetVal();

		Object obj = request.getSession().getAttribute("LoginBean");
		LoginBean loginBean = (LoginBean) obj;
		String SQL = "";
		ArrayList paramList = new ArrayList();
		ArrayList disFields = new ArrayList();
		ArrayList condField = new ArrayList();//�� ���б�

		DefineReportBean defBean = null;
		try {
			defBean = DefineReportBean.parse(reportNumber + "SQL.xml", GlobalsTool.getLocale(request).toString(),getLoginBean(request).getId());
			disFields = defBean.getDisFields();
			condField = defBean.getConFields();
		} catch (Exception ex) {
			writeError("�Ҳ�������SQL�ļ�"+reportNumber+"SQL.xml",response);
			return;
		}
		SQL = defBean.getSql();
		if (SQL.indexOf("where 1=1") != -1) {
			SQL = SQL.substring(0, SQL.indexOf("where 1=1") + 9) + " and ";
		}

		if (filedNames != null) {
			for (String str : filedNames) {
				String strName = "";
				String[] fields = str.split("string:");
				for (int i = 0; i < disFields.size(); i++) {
					ReportField field = (ReportField) disFields.get(i);
					if (field.getAsFieldName().equals(fields[0])) {
						strName = field.getFieldName();
						break;
					}
				}
				SQL = SQL + strName + "=? and ";
				paramList.add(fields[1]);
			}

			if (SQL.endsWith(" and ")) {
				SQL = SQL.substring(0, SQL.lastIndexOf(" and"));
			}
		}

		String url = "";
		if (setBean.getReportType().equals("TABLELIST")) {
			url = "/UserFunctionQueryAction.do?tableName=" + reportNumber;
		} else if (setBean.getReportType().equals("BILL")) {
			url = "/UserFunctionQueryAction.do?tableName=" + reportNumber;
		} else {
			url = "/ReportDataAction.do?reportNumber=" + reportNumber;
		}
		// MOperation mop =(MOperation)loginBean.getOperationMap().get(url);
		// ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
		// ����SQL��估ֵ�б���ʾ���� ��ѯ�����
		String type = "";
		if (setBean.getReportType().equals("BILL")) {
			type = "BILL";
		}
		ReportDataMgt mgt = new ReportDataMgt();
		rs = mgt.getPrintData(defBean, SQL, paramList, GlobalsTool.getLocale(request).toString(), type, setBean.getShowTotalStat(), loginBean);
		if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
			writeError("����SQL����쳣����",response);
			return;
		}
		ArrayList rows = (ArrayList) rs.getRetVal();
		if (defBean.isHaveStat()) {
			rows.remove(rows.size() - 1);
		}
		// ����ѯ�Ľ��д����

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<ReportData>");
		out.println("<FieldType");
		for (int i = 0; i < disFields.size(); i++) {
			ReportField field = (ReportField) disFields.get(i);
			out.println(" " + field.getAsFieldName() + "=\"" + field.getFieldType() + "\"");
		}

		HashMap conditionSessionMap = (HashMap) request.getSession().getAttribute("conditinSessionMap");
		String moduleNumber = "";
		String moduleType = request.getParameter("moduleType");
		if (moduleType != null && moduleType.trim().length() > 0) {
			moduleNumber = "-" + moduleType;
		}
		HashMap m = (HashMap) conditionSessionMap.get(reportNumber + moduleNumber);

		if (m != null)
			for (Object tmp : condField) {
				ReportField field = (ReportField) tmp;
				String value = (String) m.get(field.getAsFieldName());
				if (value == null) {
					value = "";
				} else {
					value = value.replaceAll("&nbsp;", " ");
					if (value.indexOf("&") >= 0) {
						value = value.replaceAll("&", "&amp;");
					}
					if (value.indexOf("\"") >= 0) {
						value = value.replaceAll("\"", "&quot;");
					}
					if (value.indexOf("<") >= 0) {
						value = value.replaceAll("<", "&lt;");
					}
					if (value.indexOf(">") >= 0) {
						value = value.replaceAll(">", "&gt;");
					}
					value.replaceAll("\n", "");
					value.replaceAll("\r", "");
				}
				out.println(" " + field.getAsFieldName() + "Value=\"" + value + "\"");
			}

		out.println("/>");

		out.println("<Rows>");

		for (int i = 0; i < rows.size(); i++) {
			HashMap map = (HashMap) rows.get(i);
			out.println("<Row ");
			for (int j = 0; j < disFields.size(); j++) {
				ReportField field = (ReportField) disFields.get(j);
				// if(field.getFieldName().indexOf(".")>=0 &&
				// !DynDBManager.getViewRight(scopeRight,field.getFieldName().substring(0,field.getFieldName().indexOf(".")),
				// field.getFieldName().substring(field.getFieldName().indexOf(".")+1))){
				// break;
				// }
				String value = map.get(field.getAsFieldName()) == null ? "" : map.get(field.getAsFieldName()).toString();
				value = value.replaceAll("&nbsp;", " ");
				if (value.indexOf("&") >= 0) {
					value = value.replaceAll("&", "&amp;");
				}
				if (value.indexOf("\"") >= 0) {
					value = value.replaceAll("\"", "&quot;");
				}
				if (value.indexOf("<") >= 0) {
					value = value.replaceAll("<", "&lt;");
				}
				if (value.indexOf(">") >= 0) {
					value = value.replaceAll(">", "&gt;");
				}

				if (field.getFieldType() == DBFieldInfoBean.FIELD_PIC && field.getFieldName().indexOf("'") < 0) {
					if (field.getFieldName().indexOf(".") > 0) {
						reportNumber = field.getFieldName().substring(0, field.getFieldName().indexOf("."));
					}
					out.println(field.getAsFieldName() + "=\"" + reportNumber + "/" + value + "\" ");
				} else {
					out.println(field.getAsFieldName() + "=\"" + value + "\" ");
				}

			}
			out.println("/>");
		}
		out.println("</Rows>");
		out.println("</ReportData>");
		out.close();
	}
	
	private static String strDefaultKey = "laowuzxy";

}
