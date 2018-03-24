package com.koron.oa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.util.MessageResources;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.Employee;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.koron.oa.workflow.ReadXML;
import com.koron.wechat.common.util.WXSetting;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.systemset.SystemSetMgt;
import com.menyi.aio.web.upgrade.UpgradeMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfigRefresh;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.QRCode;
import com.menyi.web.util.ServerConnection;
import com.menyi.web.util.SystemState;

/**
 * 
 * <p>Title:由于UtilServet中方法太多， 分担UtilServlet压力</p> 
 * <p>Description: </p>
 *
 * @Date:2012-5-7
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class CommonServlet extends HttpServlet {

	private static final long serialVersionUID = 5275304996910246787L;

	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-DD hh:mm:ss").create();

	BASE64Decoder decoder = new BASE64Decoder();

	BASE64Encoder encoder = new BASE64Encoder();

	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		if(BaseEnv.log.isDebugEnabled()){
			String uName = "";
			if(this.getLoginBean(req) != null){
				uName = this.getLoginBean(req).getEmpFullName();
			}
			BaseEnv.log.debug("CommonServlet.service 请求地址："+req.getRequestURI()+(req.getQueryString()==null?"":"?"+req.getQueryString()));
			String rd ="";
			for(Object key:req.getParameterMap().keySet()){
				Object value=req.getParameterMap().get(key);
				String values="";
				for(String v:(String[])value){
					values +=v+",";
				}
				rd +=key+"=["+values+"];";
			}
			
			BaseEnv.log.debug("CommonServlet.service 操作人:"+uName+";"+"请求数据："+rd);
		}
		
		String operation = req.getParameter("operation");
		if ("qrcodeDownload".equals(operation)) {
			qrcodeDownload(req, resp);
		} else if ("getResourceFileUpload".equals(operation)) {
			getResourceFileUpload(req, resp); //工作流flash客户端取资源文件
		} else if ("getResourceFileFlow".equals(operation)) {
			getResourceFileFlow(req, resp); //工作流flash客户端取资源文件
		} else if ("getEnumeration".equals(operation)) {
			getEnumeration(req, resp); //工作流flash客户端取枚举
		} else if ("getEmpGroup".equals(operation)) {
			getEmpGroup(req, resp); //工作流flash客户端取职员分组
		} else if ("getDeptEmp".equals(operation)) {
			getDeptEmp(req, resp); //工作流flash客户端取部门
		} else if ("getCondFieldInfo".equals(operation)) {
			getCondFieldInfo(req, resp);//工作流flash取条件字段
		} else if ("getFieldInfo".equals(operation)) {
			getFieldInfo(req, resp);
		} else if ("getRelatFieldInfo".equals(operation)) {
			getRelatFieldInfo(req, resp);
		} else if ("saveWorkFlowFile".equals(operation)) {
			saveWorkFlow(req, resp);
		} else if ("getWorkFlowFile".equals(operation)) {
			getWorkFlow(req, resp);
		} else if ("setUpgrade".equals(operation)) {
			setUpgrade(req, resp);
		}else if ("setRestart".equals(operation)) {
			setRestart(req, resp);
		} else if ("checkVersion".equals(operation)) {
			checkVersion(req, resp);
		} else if ("setRemoteAssis".equals(operation)) { //设置远程助手
			setRemoteAssis(req, resp);
		} else if ("refreshRemote".equals(operation)) { //设置远程助手
			refreshRemote(req, resp);
		}else if ("emailValid".equals(operation)) { //注册邮箱校验
			emailValid(req, resp);
		}else if ("firstShow".equals(operation)) { //用户第一次显示的快捷操作
			firstShow(req, resp);
		}else if ("changeWebNote".equals(operation)) { 
			changeWebNote(req, resp);
		}else if ("updateWxSet".equals(operation)) { //企业号设置更改
			updateWxSet(req, resp);
		}
	}
	
	private void changeWebNote(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {
		resp.setContentType("text/html;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		
		String type = req.getParameter("type");
		LoginBean lb = this.getLoginBean(req);
		int iType ="true".equals(type)?0:-1;
		new UserMgt().changeWebNote(lb.getId(), iType);
		lb.setShowWebNote(iType);
		out.println("OK");    	
		out.flush();
		out.close();
	}
	

	/**
	 * 刷新远程助手
	 * @param req
	 * @param resp
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void refreshRemote(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {
		resp.setContentType("text/html;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		
		ConfigRefresh.getLastInfo(req.getSession().getServletContext().getAttribute("SERVERPORT")+"");		
		out.println("OK");    	
		out.flush();
		out.close();
	}
	
	private void emailValid(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {
		resp.setContentType("text/html;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		String email = req.getParameter("email");
		
		ServerConnection conn = new ServerConnection(
        		BaseEnv.bol88URL+"/ClientConnection", new byte[] {0x21, 0x12,
                0xF, 0x58,
                (byte) 0x88, 0x10, 0x40, 0x38
                , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2});
		String posStr = "<operation>EmailValid</operation>" +
    		"<email>" + email + "</email>";
        String ret = conn.send(posStr);
        out.println("true".equals(getValue(ret, "result"))?"OK":"ERROR");  
		out.flush();
		out.close();
	}
	
	/**
	 * 检查最新版本号
	 * @param req
	 * @param resp
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void setRemoteAssis(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {
		resp.setContentType("text/html;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		String name = req.getParameter("name");
		
		ServerConnection conn = new ServerConnection(
        		BaseEnv.bol88URL+"/ClientConnection", new byte[] {0x21, 0x12,
                0xF, 0x58,
                (byte) 0x88, 0x10, 0x40, 0x38
                , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2});
		String posStr = "<operation>setRemote</operation>" +
    		"<dogId>" + SystemState.instance.dogId + "</dogId>"+
    		"<name>" + name + "</name>"+
    		"<port>" + req.getSession().getServletContext().getAttribute("SERVERPORT") + "</port>";
        String ret = conn.send(posStr);
        if (ret != null && ret.length() >0 && "OK".equals(getValue(ret, "result"))) {
        	SystemSetMgt smgt = new SystemSetMgt();
        	Result rs = smgt.startRemote(SystemState.instance.dogId);
        	SystemState.instance.remoteName = name;
        	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		out.println("OK"); 
        	}else{
        		out.println("存入数据库出错，请重试"); 
        	}
        }else if (ret != null && ret.length() >0 ) {
        	out.println(URLDecoder.decode(getValue(ret, "error"),"UTF-8"));    	
        }else{
        	out.println("启动错误,请确保您的服务器能够连入Internet");    	
        }
		out.flush();
		out.close();
	}
    public String getValue(String xml, String name) {
        try {
            return xml.substring(xml.indexOf("<" + name + ">") +
                                 ("<" + name + ">").length(),
                                 xml.indexOf("</" + name + ">"));
        } catch (Exception ex) {
            return null;
        }
    }
	
	/**
	 * 检查最新版本号
	 * @param req
	 * @param resp
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void checkVersion(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {
		resp.setContentType("text/html;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		String szFile[] = new String[4];
		szFile[0]= "http://dl.krrj.cn:7123/dl.xml";
		szFile[1]= "http://dl2.krrj.cn/dl.xml";
		szFile[2]= "http://dl3.koronsoft.com:7234/dl.xml";
		szFile[3]= "http://dl4.koronsoft.com/dl.xml";
		
		for (String urlS :szFile){
			try{
				URL url = new URL(urlS);
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setDoOutput(true);
		        conn.connect();
		        //得到返回的信息
		        InputStream is = conn.getInputStream();
		        byte[] inbuf = new byte[1024];
		        byte[] bs = new byte[0];
		        int rn;
		        while ((rn = is.read(inbuf)) > 0) {
		            byte[] temp = bs;
		            bs = new byte[rn + bs.length];
		            System.arraycopy(temp, 0, bs, 0, temp.length);
		            System.arraycopy(inbuf, 0, bs, temp.length, rn);
		        }
		        String ret = new String(bs,"utf-8");
		        String version = "";
		        if(ret.indexOf("<version>")>0){
		        	version = ret.substring(ret.indexOf("<version>")+"<version>".length(),ret.indexOf("</version>"));
		        	System.out.println(urlS+":"+version);
		        	out.print(version);
		        	break;
		        }	      
			}catch(Exception e){				
				
			}
		}

		out.flush();
		out.close();
	}
	
	private void firstShow(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {
		resp.setContentType("text/html;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		LoginBean lb = getLoginBean(req);
		if(lb == null){
			out.print("true");
		}else{
			String moduleNo = req.getParameter("moduleNo");
			String isSave = req.getParameter("isSave");
			if("true".equals(isSave)){
				Result rs = new UserMgt().firstShowSave(lb.getId(), moduleNo);
				lb.setFirstShow(lb.getFirstShow()==null?";"+moduleNo+";":lb.getFirstShow()+moduleNo+";");
				if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
					out.print("保存成功");
				}else{
					out.print("保存失败");
				}
			}else{
				if(lb.getFirstShow() != null && lb.getFirstShow().contains(";"+moduleNo+";")){
					out.print("true");
				}else{
					out.print("false");
				}
			}
			
		}
		out.flush();
		out.close();
	}
	
	private void setUpgrade(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {
		resp.setContentType("text/html;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		LoginBean lb = getLoginBean(req);
		if(lb == null || !lb.getId().equals("1")){
			out.print("请用admin帐号执行此操作");
		}else{
			Result rs = new UpgradeMgt().setUpgrade();
			if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
				out.print("升级开始，请耐心等待！");
			}else{
				out.print("升级请求失败，请稍后再试，或联系客服");
			}
		}
		out.flush();
		out.close();
	}
	
	private void setRestart(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {
		resp.setContentType("text/html;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		LoginBean lb = getLoginBean(req);
		if(lb != null ){
			Result rs = new UpgradeMgt().setRestart();
			if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
				out.print("重启指令已执行，请等待系统重启！");
			}else{
				out.print("重启请求失败，请稍后再试，或联系客服");
			}
		}
		out.flush();
		out.close();
	}

	/**
	 * 从服务器读取流程设置配置文件
	 */
	private void getWorkFlow(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {

		String fileName = req.getParameter("fileName");
		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		String importFile = req.getParameter("importFile");
		String export = req.getParameter("export");

		String designId = "";
		if (fileName != null) {
			if (fileName.contains(".xml")) {
				designId = fileName.substring(0, fileName.indexOf("."));
			} else {
				designId = fileName;
			}
		}
		OAWorkFlowTemplate template = BaseEnv.workFlowInfo.get(designId);
		if (template != null && template.getLines() != null && template.getLines().length() > 0) {
			PrintWriter out = resp.getWriter();
			if ("true".equals(export)) {
				WorkFlowDesignBean design = BaseEnv.workFlowDesignBeans.get(designId);
				String flowXml = template.getLines().substring(template.getLines().indexOf("<FlowNodes>") + 11, template.getLines().length());
				out.print("<FlowNodes><FlowJson><json>" + gson.toJson(design.getFlowNodeMap()) + "</json></FlowJson>" + flowXml);
			} else {
				out.print(template.getLines());
			}
			out.flush();
			out.close();
		} else {
			ServletOutputStream out = resp.getOutputStream();
			File file;
			if ("true".equals(importFile)) {
				file = new File(this.getUploadWokeFlowPath(fileName));
			} else if (SystemState.instance.dogState == SystemState.DOG_FORMAL && "0".equals(SystemState.instance.dogId)) {
				file = new File(this.getWokeFlowPath(fileName));
			} else {
				file = new File(this.getUserWokeFlowPath(fileName));
			}

			FileInputStream in = new FileInputStream(file);
			byte[] b = new byte[1024];
			int count = 0;
			while ((count = in.read(b)) > -1) {
				out.write(b, 0, count);
			}
			in.close();
			if ("true".equals(importFile)) {
				file.delete();
			}
			out.close();
		}
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

	/**
	 * 保存流程设置配置文件 到服务器
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param resp
	 *            HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	public void saveWorkFlow(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getParameter("fileName");
		if (fileName == null || fileName.length() <= 0) {
			return;
		}
		File file = null;
		File file_old = null;
		if (SystemState.instance.dogState == SystemState.DOG_FORMAL && "0".equals(SystemState.instance.dogId)) {
			file = new File(this.getWokeFlowPath(fileName));
			file_old = new File(this.getWokeFlowPath(fileName + "_old"));
			file.renameTo(file_old);
		} else {
			file = new File(this.getUserWokeFlowPath(fileName));
			file_old = new File(this.getUserWokeFlowPath(fileName + "_old"));
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
			req.getSession().setAttribute("newCreate", "false");

			//将文件加载到系统内存中的工作流对象
			WorkFlowDesignBean bean = ReadXML.parse(fileName);
			if (bean == null) {
				file.delete();
				file_old.renameTo(file);
			} else {
				// 删除原来的文件
				if (file_old.exists()) {
					file_old.delete();
				}
				BaseEnv.workFlowDesignBeans.put(fileName.substring(0, fileName.indexOf(".")), bean);
				String fileFinish = bean == null ? "0" : "1";
				// 保存成功修改是否已经上传记录为old
				PublicMgt workMgt = new PublicMgt();
				workMgt.updateFlowName(fileName, fileFinish);
			}
		} catch (IOException ex) {
			file_old.renameTo(file);
			throw ex;
		}
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

	/**
	 * 获取此表所有关联表的字段信息
	 * 用于工作流读取字段信息，隐藏和不输入的字段在工作流设计界面不显示，同时主表和子表的字段信息都要显示
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getRelatFieldInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Hashtable map = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
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
		ArrayList childTables = DDLOperation.getChildTables(tableName, map);
		ArrayList brotherTables = DDLOperation.getBrotherTables(tableName, map);
		childTables.addAll(brotherTables);
		for (int i = 0; i < brotherTables.size(); i++) {
			ArrayList brochildTables = DDLOperation.getChildTables(((DBTableInfoBean) brotherTables.get(i)).getTableName(), map);
			childTables.addAll(brochildTables);
		}
		for (Object field : tableInfo.getFieldInfos()) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) field;
			if (fieldInfo.getInputType() == fieldInfo.INPUT_HIDDEN || fieldInfo.getInputType() == fieldInfo.INPUT_NO || fieldInfo.getFieldName().equals("id")) {
				continue;
			}

			out.println("<field id=\"" + fieldInfo.getId() + "\" fieldName=\"" + fieldInfo.getFieldName() + "\" listOrder=\"" + fieldInfo.getListOrder() + "\" isNull=\"" + fieldInfo.getIsNull()
					+ "\" isUnique=\"" + fieldInfo.getIsUnique() + "\" isStat=\"" + fieldInfo.getIsStat() + "\" defaultValue=\"" + fieldInfo.getDefaultValue() + "\" fieldType=\""
					+ fieldInfo.getFieldType() + "\" maxLength=\"" + fieldInfo.getMaxLength() + "\" width=\"" + fieldInfo.getWidth() + "\" inputType=\"" + fieldInfo.getInputType()
					+ "\" refEnumerationName=\"" + fieldInfo.getRefEnumerationName() + "\" inputValue=\"" + fieldInfo.getInputValue() + "\" fieldSysType=\"" + fieldInfo.getFieldSysType()
					+ "\" udType=\"" + fieldInfo.getUdType() + "\">");
			out.println("<Displays>");
			if (!tableInfo.getTableName().startsWith("Flow_")) { //判断是否是个性化表单设计生成的表
				if (fieldInfo.getDisplay() != null) {
					for (Object display : fieldInfo.getDisplay().map.keySet()) {
						String key = (String) display;
						String str = "<Display id=\"" + fieldInfo.getDisplay().getId() + "\" localStr=\"" + key + "\" display=\""
								+ (fieldInfo.getDisplay().map.get(key) == null ? "" : fieldInfo.getDisplay().map.get(key).toString().replace("&", "&amp;")) + "\"/>";
						out.println(str);
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

		for (int i = 0; i < childTables.size(); i++) {
			DBTableInfoBean tabInfo = (DBTableInfoBean) childTables.get(i);
			for (Object field : tabInfo.getFieldInfos()) {
				DBFieldInfoBean fieldInfo = (DBFieldInfoBean) field;
				if (fieldInfo.getInputType() == fieldInfo.INPUT_HIDDEN || fieldInfo.getInputType() == fieldInfo.INPUT_NO || fieldInfo.getFieldName().equals("id")
						|| fieldInfo.getFieldName().equals("f_ref") || fieldInfo.getFieldName().equals("f_brother")) {
					continue;
				}
				out.println("<field id=\"" + fieldInfo.getId() + "\" fieldName=\"" + tabInfo.getTableName() + "_" + fieldInfo.getFieldName() + "\" listOrder=\"" + fieldInfo.getListOrder()
						+ "\" isNull=\"" + fieldInfo.getIsNull() + "\" isUnique=\"" + fieldInfo.getIsUnique() + "\" isStat=\"" + fieldInfo.getIsStat() + "\" defaultValue=\""
						+ fieldInfo.getDefaultValue() + "\" fieldType=\"" + fieldInfo.getFieldType() + "\" maxLength=\"" + fieldInfo.getMaxLength() + "\" width=\"" + fieldInfo.getWidth()
						+ "\" inputType=\"" + fieldInfo.getInputType() + "\" refEnumerationName=\"" + fieldInfo.getRefEnumerationName() + "\" inputValue=\"" + fieldInfo.getInputValue()
						+ "\" fieldSysType=\"" + fieldInfo.getFieldSysType() + "\" udType=\"" + fieldInfo.getUdType() + "\">");
				out.println("<Displays>");
				if (!tabInfo.getTableName().startsWith("Flow_")) { //判断是否是个性化表单设计生成的表
					if (fieldInfo.getDisplay() != null) {
						for (Object display : fieldInfo.getDisplay().map.keySet()) {
							String key = (String) display;
							out.println(("<Display id=\"" + fieldInfo.getDisplay().getId() + "\" localStr=\"" + key + "\" display=\""
									+ (tabInfo.getDisplay().get(key) == null ? "" : tabInfo.getDisplay().get(key).toString().replace("&", "&amp;")) + "_"
									+ (fieldInfo.getDisplay().map.get(key) == null ? "" : fieldInfo.getDisplay().map.get(key).toString().replace("&", "&amp;")) + "\"/>"));
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
		}
		out.println("</fieldInfo>");
	}

	/**
	 * 获取所有表结构字段信息
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getFieldInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Hashtable map = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
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
			out.println("<field id=\"" + fieldInfo.getId() + "\" fieldName=\"" + fieldInfo.getFieldName() + "\" listOrder=\"" + fieldInfo.getListOrder() + "\" isNull=\"" + fieldInfo.getIsNull()
					+ "\" isUnique=\"" + fieldInfo.getIsUnique() + "\" isStat=\"" + fieldInfo.getIsStat() + "\" defaultValue=\"" + fieldInfo.getDefaultValue() + "\" fieldType=\""
					+ fieldInfo.getFieldType() + "\" maxLength=\"" + fieldInfo.getMaxLength() + "\" width=\"" + fieldInfo.getWidth() + "\" inputType=\"" + fieldInfo.getInputType()
					+ "\" refEnumerationName=\"" + fieldInfo.getRefEnumerationName() + "\" inputValue=\"" + fieldInfo.getInputValue() + "\" fieldSysType=\"" + fieldInfo.getFieldSysType()
					+ "\" udType=\"" + fieldInfo.getUdType() + "\">");
			out.println("<Displays>");
			if (fieldInfo.getDisplay() != null) {
				for (Object display : fieldInfo.getDisplay().map.keySet()) {
					String key = (String) display;
					if (tableName.startsWith("Flow_")) {
						out.println(("<Display id=\"" + fieldInfo.getDisplay().getId() + "\" localStr=\"" + key + "\" display=\""
								+ (fieldInfo.getLanguageId() == null ? fieldInfo.getFieldName() : fieldInfo.getLanguageId().replace("&", "&amp;")) + "\"/>"));
					} else {
						out.println(("<Display id=\"" + fieldInfo.getDisplay().getId() + "\" localStr=\"" + key + "\" display=\""
								+ (fieldInfo.getDisplay().map.get(key) == null ? key : fieldInfo.getDisplay().map.get(key).toString().replace("&", "&amp;")) + "\"/>"));
					}
				}
			}
			out.println("</Displays>");
			out.println("</field>");
		}
		out.println("</fieldInfo>");
	}

	public void getDeptEmp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		PrintWriter out = resp.getWriter();
		EmployeeMgt empMgt = new EmployeeMgt();
		Result rs = empMgt.getAllDetps();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			String deptTree = "";
			ArrayList depts = (ArrayList) rs.getRetVal();
			List<Employee> empList = empMgt.queryAllEmployeeByClassCode(null);
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			for (int i = 0; i < depts.size(); i++) {
				String[] str = (String[]) depts.get(i);
				if (str[0].length() == 5) {
					deptTree += this.deptTree(depts, empList, str[1], str[0], str[2]);
				}
			}
			out.println("<emp>" + deptTree + "</emp>");
			out.close();
		}
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

	/**
	 * 获取此表所有关联表的字段信息
	 * 用于工作流读取字段信息，隐藏和不输入的字段在工作流设计界面不显示，同时主表和子表的字段信息都要显示
	 * 用于条件设置
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getCondFieldInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Hashtable map = (Hashtable) req.getSession().getServletContext().getAttribute(BaseEnv.TABLE_INFO);
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
		ArrayList childTables = DDLOperation.getChildTables(tableName, map);
		for (Object field : tableInfo.getFieldInfos()) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) field;

			out.println("<field id=\"" + fieldInfo.getId() + "\" fieldName=\"" + fieldInfo.getFieldName() + "\" listOrder=\"" + fieldInfo.getListOrder() + "\" isNull=\"" + fieldInfo.getIsNull()
					+ "\" isUnique=\"" + fieldInfo.getIsUnique() + "\" isStat=\"" + fieldInfo.getIsStat() + "\" defaultValue=\"" + fieldInfo.getDefaultValue() + "\" fieldType=\""
					+ fieldInfo.getFieldType() + "\" maxLength=\"" + fieldInfo.getMaxLength() + "\" width=\"" + fieldInfo.getWidth() + "\" inputType=\"" + fieldInfo.getInputType()
					+ "\" refEnumerationName=\"" + fieldInfo.getRefEnumerationName() + "\" inputValue=\"" + fieldInfo.getInputValue() + "\" fieldSysType=\"" + fieldInfo.getFieldSysType()
					+ "\" udType=\"" + fieldInfo.getUdType() + "\">");
			out.println("<Displays>");
			if (!tableInfo.getTableName().startsWith("Flow_")) { //判断是否是个性化表单设计生成的表
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

		for (int i = 0; i < childTables.size(); i++) {
			DBTableInfoBean tabInfo = (DBTableInfoBean) childTables.get(i);
			for (Object field : tabInfo.getFieldInfos()) {
				DBFieldInfoBean fieldInfo = (DBFieldInfoBean) field;

				out.println("<field id=\"" + fieldInfo.getId() + "\" fieldName=\"" + tabInfo.getTableName() + "_" + fieldInfo.getFieldName() + "\" listOrder=\"" + fieldInfo.getListOrder()
						+ "\" isNull=\"" + fieldInfo.getIsNull() + "\" isUnique=\"" + fieldInfo.getIsUnique() + "\" isStat=\"" + fieldInfo.getIsStat() + "\" defaultValue=\""
						+ fieldInfo.getDefaultValue() + "\" fieldType=\"" + fieldInfo.getFieldType() + "\" maxLength=\"" + fieldInfo.getMaxLength() + "\" width=\"" + fieldInfo.getWidth()
						+ "\" inputType=\"" + fieldInfo.getInputType() + "\" refEnumerationName=\"" + fieldInfo.getRefEnumerationName() + "\" inputValue=\"" + fieldInfo.getInputValue()
						+ "\" fieldSysType=\"" + fieldInfo.getFieldSysType() + "\" udType=\"" + fieldInfo.getUdType() + "\">");
				out.println("<Displays>");
				if (!tabInfo.getTableName().startsWith("Flow_")) { //判断是否是个性化表单设计生成的表
					if (fieldInfo.getDisplay() != null) {
						for (Object display : fieldInfo.getDisplay().map.keySet()) {
							String key = (String) display;
							out.println(("<Display id=\"" + fieldInfo.getDisplay().getId() + "\" localStr=\"" + key + "\" display=\""
									+ (tabInfo.getDisplay().get(key) == null ? "" : tabInfo.getDisplay().get(key).toString().replace("&", "&amp;")) + "_"
									+ (fieldInfo.getDisplay().map.get(key) == null ? "" : fieldInfo.getDisplay().map.get(key).toString().replace("&", "&amp;")) + "\"/>"));
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
		}
		out.println("</fieldInfo>");
	}

	public void getEmpGroup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		Result rs = new EmployeeMgt().getEmpGroup();
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<group>");
			ArrayList list = (ArrayList) rs.retVal;
			for (int i = 0; i < list.size(); i++) {
				String[] str = (String[]) list.get(i);
				out.println("<groupItem id=\"" + str[0] + "\" name=\"" + str[1] + "\"></groupItem>");
			}
			out.println("</group>");
			out.close();
		}
	}

	public void getEnumeration(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		String enumName = req.getParameter("enumName");
		String locale = req.getParameter("locale");
		EnumerateBean bean = (EnumerateBean) BaseEnv.enumerationMap.get(enumName);
		String enumItems = "<enum>";
		for (int i = 0; i < bean.getEnumItem().size(); i++) {
			EnumerateItemBean eib = (EnumerateItemBean) bean.getEnumItem().get(i);
			if (locale != null && locale.length() > 0) {
				enumItems += "<enumItem value=\"" + eib.getEnumValue() + "\" display=\"" + eib.getDisplay().get(locale) + "\" />";
			} else {
				enumItems += "<enumItem value=\"" + eib.getEnumValue() + "\">";
				enumItems += "<displays>";
				KRLanguage kr = eib.getDisplay();
				HashMap map = kr.map;
				Iterator it = map.keySet().iterator();
				while (it.hasNext()) {
					String local = it.next().toString();
					enumItems += "<display  localStr=\"" + local + "\" display=\"" + map.get(local) + "\"/>";
				}

				enumItems += "</displays>";
				enumItems += "</enumItem>";
			}
		}

		enumItems += "</enum>";
		out.println(enumItems);
		out.close();

	}

	/**
	 * 从服务器读取流程设置配置文件
	 */
	private void getResourceFileFlow(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {

		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<resource locale=\"" + GlobalsTool.getLocale(req) + "\" startNodeTip=\"" + getMessageResources(req, "oa.bbss.begin") + "\" endNodeTip=\""
				+ getMessageResources(req, "flow.button.stop") + "\" del=\"" + getMessageResources(req, "oa.common.del") + "\" endNode=\"" + getMessageResources(req, "reportServlet.lb.endcode")
				+ "\" choiceNode=\"" + getMessageResources(req, "common.lb.selectNode") + "\" " + "lineNode=\"" + getMessageResources(req, "reportServlet.lb.link") + "\" flowNode=\""
				+ getMessageResources(req, "reportServlet.lb.flowNode") + "\" startNode=\"" + getMessageResources(req, "reportServlet.lb.startNode") + "\" exportSuccess=\""
				+ getMessageResources(req, "excel.export.success") + "\" importSuccess=\"" + getMessageResources(req, "import.succees.number") + "\" copyt=\""
				+ getMessageResources(req, "common.lb.copy") + "\" exportt=\"" + getMessageResources(req, "com.bill.export") + "\" importt=\"" + getMessageResources(req, "excel.lb.upload")
				+ "\" choice=\"" + getMessageResources(req, "common.lb.select") + "\" validator=\"" + getMessageResources(req, "common.lb.verify") + "\" save=\""
				+ getMessageResources(req, "common.lb.save") + "\" saveWait=\"" + getMessageResources(req, "common.msg.saving") + "\" " + "flowsaveFail=\""
				+ getMessageResources(req, "reportServlet.lb.workFlowUploadFalse") + "\"  errorTitle=\"" + getMessageResources(req, "tableinfo.update.calerror5") + "\" flowDownFail=\""
				+ getMessageResources(req, "reportServlet.lb.fileloadFalse") + "\" downWait=\"" + getMessageResources(req, "reportServlet.lb.downloadingdata") + "\" " + "youEnterNum=\""
				+ getMessageResources(req, "reportServlet.lb.inputNo") + "\"  double=\"" + getMessageResources(req, "billexport.mainfieldname.more") + "\" endNotLine=\""
				+ getMessageResources(req, "reportServlet.msg.endNode") + "\" flowNotLine=\"" + getMessageResources(req, "reportServlet.msg.flowNodeJustoneline") + "\" " + "startNotLine=\""
				+ getMessageResources(req, "reportServlet.msg.startNodeJustoneline") + "\"  onlyOneStartNode=\"" + getMessageResources(req, "reportServlet.msg.everyFlowwithOnestartnode")
				+ "\"  noLine=\"" + getMessageResources(req, "reportServlet.lb.noLine") + "\"  to=\"" + getMessageResources(req, "common.msg.until") + "\"  noCondition=\""
				+ getMessageResources(req, "reportServlet.lb.nosetif") + "\" " + "noCode=\"" + getMessageResources(req, "reportServlet.lb.noCode") + "\"  validPass=\""
				+ getMessageResources(req, "reportServlet.lb.validPass") + "\"  nopath=\"" + getMessageResources(req, "reportServlet.lb.nopath") + "\"  nostartNode=\""
				+ getMessageResources(req, "reportServlet.lb.nostartNode") + "\" " + "date=\"" + getMessageResources(req, "oa.calendar.day") + "\"  hour=\"" + getMessageResources(req, "oa.work.hour")
				+ "\"  minute=\"" + getMessageResources(req, "common.msg.minute") + "\" ok=\"" + getMessageResources(req, "common.lb.ok") + "\" cancel=\"" + getMessageResources(req, "mrp.lb.cancel")
				+ "\" confirmDel=\"" + getMessageResources(req, "reportServlet.lb.deleteensure") + "\" oktitle=\"" + getMessageResources(req, "common.lb.ok") + "\" largeZero=\""
				+ getMessageResources(req, "reportServlet.lb.NumMustOverzero") + "\" errorInt=\"" + getMessageResources(req, "reportServlet.lb.wringwrongmustint") + "\" " + "mustInt=\""
				+ getMessageResources(req, "reportServlet.lb.mustint") + "\"  baseProp=\"" + getMessageResources(req, "reportServlet.lb.baseField") + "\" nodeNo=\""
				+ getMessageResources(req, "reportServlet.lb.stepNum") + "\"  nodeName=\"" + getMessageResources(req, "reportServlet.lb.stepName") + "\" timeLimit=\""
				+ getMessageResources(req, "reportServlet.lb.dealMaxtime") + "\" noteTime=\"" + getMessageResources(req, "reportServlet.lb.advanceremind") + "\" noteRate=\""
				+ getMessageResources(req, "reportServlet.lb.noteRate") + "\" " + "allowBack=\"" + getMessageResources(req, "reportServlet.lb.allowBack") + "\"  allowCancel=\""
				+ getMessageResources(req, "reportServlet.lb.allowCancel") + "\" allowJump=\"" + getMessageResources(req, "reportServlet.lb.allowJump") + "\" allowStop=\""
				+ getMessageResources(req, "reportServlet.lb.allowStop") + "\" useAllApprove=\"" + getMessageResources(req, "reportServlet.lb.useAllApprove") + "\" fieldSet=\""
				+ getMessageResources(req, "reportServlet.lb.fieldSet") + "\" field=\"" + getMessageResources(req, "import.list.field") + "\" " + "readOnly=\""
				+ getMessageResources(req, "reportServlet.lb.readOnly") + "\" hidden=\"" + getMessageResources(req, "reportServlet.lb.hidden") + "\" peopleSet=\""
				+ getMessageResources(req, "call.lb.transactor") + "\" selectPeople=\"" + getMessageResources(req, "reportServlet.lb.selectPeople") + "\" fixPeople=\""
				+ getMessageResources(req, "reportServlet.lb.fixPeople") + "\" typle=\"" + getMessageResources(req, "customTable.lb.tableType") + "\" userName=\""
				+ getMessageResources(req, "reportServlet.lb.userName") + "\" addDuty=\"" + getMessageResources(req, "reportServlet.lb.lbaddDuty") + "\" addGroup=\""
				+ getMessageResources(req, "reportServlet.lb.lbaddGroup") + "\" addEmployee=\"" + getMessageResources(req, "reportServlet.lb.addEmployee") + "\" advanceProp=\""
				+ getMessageResources(req, "reportServlet.lb.advanceProp") + "\" define=\"" + getMessageResources(req, "reportServlet.lb.define") + "\" passExec=\""
				+ getMessageResources(req, "reportServlet.lb.passExec") + "\" backExec=\"" + getMessageResources(req, "reportServlet.lb.backExec") + "\" stopExec=\""
				+ getMessageResources(req, "reportServlet.lb.stopExec") + "\"   " + "advanceRem=\"" + getMessageResources(req, "reportServlet.msg.advanceRem") + "\" conditionSet=\""
				+ getMessageResources(req, "reportServlet.lb.conditionSet") + "\" condName=\"" + getMessageResources(req, "reportServlet.lb.condName") + "\" relation=\""
				+ getMessageResources(req, "reportServlet.lb.relation") + "\" value=\"" + getMessageResources(req, "reportServlet.lb.value") + "\"  " + "andOr=\""
				+ getMessageResources(req, "reportServlet.lb.andOr") + "\" update=\"" + getMessageResources(req, "common.lb.update") + "\" add=\"" + getMessageResources(req, "mrp.lb.add")
				+ "\"  equal=\"" + getMessageResources(req, "reportServlet.lb.equal") + "\" noequal=\"" + getMessageResources(req, "reportServlet.lb.noequal") + "\" large=\""
				+ getMessageResources(req, "reportServlet.lb.large") + "\" largeequal=\"" + getMessageResources(req, "reportServlet.lb.largeequal") + "\" letter=\""
				+ getMessageResources(req, "reportServlet.lb.letter") + "\" letterequal=\"" + getMessageResources(req, "reportServlet.lb.letterequal") + "\" " + "contain=\""
				+ getMessageResources(req, "reportServlet.lb.contain") + "\" nocontain=\"" + getMessageResources(req, "reportServlet.lb.nocontain") + "\" and=\""
				+ getMessageResources(req, "calc.lb.and") + "\" or=\"" + getMessageResources(req, "calc.lb.or") + "\" close=\"" + getMessageResources(req, "com.lb.close") + "\" dutySelect=\""
				+ getMessageResources(req, "reportServlet.lb.dutySelect") + "\" groupSelect=\"" + getMessageResources(req, "reportServlet.lb.groupSelect") + "\" dutyName=\""
				+ getMessageResources(req, "oa.common.job.name") + "\" groupName=\"" + getMessageResources(req, "reportServlet.lb.lbaddGroup") + "\"  empSelect=\""
				+ getMessageResources(req, "reportServlet.lb.empSelect") + "\" empName=\"" + getMessageResources(req, "oa.common.employee") + "\" doTime=\""
				+ getMessageResources(req, "oa.bbs.finish") + "\" standardTime=\"" + getMessageResources(req, "common.lb.plan") + "\" detail=\"" + getMessageResources(req, "common.lb.detail")
				+ "\" saveSuccss=\"" + getMessageResources(req, "crm.help.saveSucess") + "\" finishDisign=\"" + getMessageResources(req, "common.lb.closeDesign") + "\" filter=\""
				+ getMessageResources(req, "common.lb.filter2") + "\" pinyin=\"" + getMessageResources(req, "common.lb.PinYin") + "\" userP=\"" + getMessageResources(req, "userManager.lb.userName")
				+ "\" department=\"" + getMessageResources(req, "oa.common.department") + "\" searchCondition=\"" + getMessageResources(req, "tableInfo.lb.searchCondition") + "\" forwardTime=\""
				+ getMessageResources(req, "workflow.lb.forwardTime") + "\" standaloneNote=\"" + getMessageResources(req, "workflow.lb.standaloneNote") + "\" deptName=\""
				+ getMessageResources(req, "workflow.lb.deptName") + "\" nextNote=\"" + getMessageResources(req, "workflow.lb.nextNote") + "\" startNote=\""
				+ getMessageResources(req, "workflow.lb.startNote") + "\" allNote=\"" + getMessageResources(req, "workflow.lb.allNote") + "\" setNote=\""
				+ getMessageResources(req, "workflow.lb.setNote") + "\" standaloneNoteSet=\"" + getMessageResources(req, "workflow.lb.standaloneNoteSet") + "\" filterSet0=\""
				+ getMessageResources(req, "workflow.lb.filterSet0") + "\" filterSet1=\"" + getMessageResources(req, "workflow.lb.filterSet1") + "\" filterSet2=\""
				+ getMessageResources(req, "workflow.lb.filterSet2") + "\" filterSet3=\"" + getMessageResources(req, "workflow.lb.filterSet3") + "\" filterSet4=\""
				+ getMessageResources(req, "workflow.lb.filterSet4") + "\" filterSetLabel=\"" + getMessageResources(req, "workflow.lb.filterSetLabel") + "\" filterSetTitle=\""
				+ getMessageResources(req, "workflow.lb.filterSetTitle") + "\" autoSelectPeopleLabel=\"" + getMessageResources(req, "workflow.lb.autoSelectPeopleLabel")
				+ "\" autoSelectPeopleTitle=\"" + getMessageResources(req, "workflow.lb.autoSelectPeopleTitle") + "\" autoSelectPeople0=\"" + getMessageResources(req, "workflow.lb.autoSelectPeople0")
				+ "\" autoSelectPeople1=\"" + getMessageResources(req, "workflow.lb.autoSelectPeople1") + "\" notnull=\"" + getMessageResources(req, "common.lb.notnull") + "\" hiddenFieldSet=\""
				+ getMessageResources(req, "workflow.lb.hiddenFieldSet") + "\" hiddenFiledPublic=\"" + getMessageResources(req, "workflow.lb.hiddenFiledPublic") + "\" ideaRequired=\""
				+ getMessageResources(req, "workflow.lb.ideaRequired") + "\" importRes=\"" + getMessageResources(req, "excel.lb.upload") + "\" cleart=\"" + getMessageResources(req, "com.db.clear")
				+ "\" exportRes=\"" + getMessageResources(req, "common.lb.export") + "\" />");
	}

	/**
	 * 从服务器读取流程设置配置文件
	 */
	private void getResourceFileUpload(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {

		resp.setContentType("text/XML;   charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		String str = "<resource close=\"" + this.getMessageResources(req, "common.lb.close") + "\" totalFileNum=\"" + this.getMessageResources(req, "ReportServlet.lb.totalFileMount")
				+ "\" totalSize=\"" + this.getMessageResources(req, "ReportServlet.lb.totalSize") + "\"  upFail=\"" + this.getMessageResources(req, "common.msg.uploadFailure") + "\" fileUp=\""
				+ this.getMessageResources(req, "ReportServlet.lb.fileUpload") + "\" fileName=\"" + this.getMessageResources(req, "oa.mydata.fileName") + "\" " + " fileSize=\""
				+ this.getMessageResources(req, "ReportServlet.lb.fileSize") + "\" fileType=\"" + this.getMessageResources(req, "common.lb.fileType") + "\" state=\""
				+ this.getMessageResources(req, "alertCenter.lb.status") + "\" cancel=\"" + this.getMessageResources(req, "common.lb.cancelUpload") + "\" canceled=\""
				+ this.getMessageResources(req, "common.lb.canceled") + "\" cannotCancel=\"" + this.getMessageResources(req, "common.msg.NoCancel") + "\" " + " delfromList=\""
				+ this.getMessageResources(req, "common.msg.fromListDel") + "\" cannotDel=\"" + this.getMessageResources(req, "common.msg.NoCancel") + "\" query=\""
				+ this.getMessageResources(req, "common.msg.browse") + "\" maxSize=\"" + this.getMessageResources(req, "common.msg.maxSize", BaseEnv.systemSet.get("defaultAttachSize").getSetting())
				+ "\" upload=\"" + this.getMessageResources(req, "upload.lb.upload") + "\" />";
		out.print(str);
		out.close();

	}

	/**
	 * 获取资源文件
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

	public MessageResources getMessageResources(HttpServletRequest req) {
		Object o = req.getSession().getServletContext().getAttribute("res.DynamicResource");
		if (o instanceof MessageResources) {
			return (MessageResources) o;
		} else {
			return null;
		}
	}

	/**
	 * wxq 浏览器验证二维码登录 每隔1000验证一次
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException 
	 */
	public void pc_check(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String uuid = request.getParameter("uuid");
		//解密字符串
		uuid = new String(QRCode.decryptMode(decoder.decodeBuffer(uuid)));
		String str1 = QRCode.getValue(uuid, "str1");

		PrintWriter out = response.getWriter();
		if (str1 != null && str1.length() > 0 && BaseEnv.barcodeMap.get(str1) != null) {
			String[] userInfo = BaseEnv.barcodeMap.get(str1);
			if ("wait".equals(userInfo[1])) {
				out.print("wait");
			} else if (userInfo[1] != null && userInfo[1].length() > 0) {
				out.print("confirm");
			}
		} else {
			out.print("timeout");
		}
		clearBarcode();
	}

	/**
	 * wxq 手机端验证二维码登录 手机确认二维码登录
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException 
	 */
	public void m_check(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String uuid = request.getParameter("uuid");
		if (uuid == null || uuid.length() == 0)
			return;
		//解密字符串
		uuid = new String(QRCode.decryptMode(decoder.decodeBuffer(uuid)));
		String str1 = QRCode.getValue(uuid, "str1");
		String str2 = QRCode.getValue(uuid, "str2");
		//str1 = "21d39013-457b-4603-80a0-7cdda5f0ae82";
		//str2 = "1";
		PrintWriter out = response.getWriter();
		if (str1 == null || str1.length() == 0) {
			out.print("no");
			return;
		}
		if (BaseEnv.barcodeMap.get(str1) != null && str2 == null) {
			BaseEnv.barcodeMap.put(str1, new String[] { String.valueOf(new Date().getTime()), "wait" });
			out.print("ok");
		} else if (str2 != null && str2.length() > 0) {
			BaseEnv.barcodeMap.put(str1, new String[] { String.valueOf(new Date().getTime()), str2 });
			out.print("ok");
		} else {
			out.print("no");
		}
	}

	/**
	 * wxq 登录界面生成二维码
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException 
	 */
	public void qrcode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		try {
			String uuid = request.getParameter("uuid");
			if (uuid == null) {
				String strUUID = UUID.randomUUID().toString();
				uuid = "<str1>" + strUUID + "</str1>";
				uuid = encoder.encode(QRCode.encryptMode(uuid.getBytes()));
				System.out.println(uuid);
				BaseEnv.barcodeMap.put(strUUID, new String[] { String.valueOf(new Date().getTime()), "" });
				PrintWriter out = response.getWriter();
				out.print(uuid);
				return;
			} else {
				response.setContentType("image/png");
				QRCode.encoderQRCode(uuid, response.getOutputStream());
			}
			clearBarcode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清除过期的二维码
	 */
	public void clearBarcode() {
		HashMap<String, String[]> codeMap = BaseEnv.barcodeMap;
		Iterator iter = codeMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String[] userInfo = (String[]) entry.getValue();
			if (new Date().getTime() - 1 * 60000 > Long.valueOf(userInfo[0])) {
				iter.remove();
			}
		}
	}

	/**
	 * pwy 生成移动客户端下载二维码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void qrcodeDownload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String url = request.getParameter("url");

		String strHttp = "";
		if (url == null || url.length() == 0) {
			strHttp = "http://" + request.getHeader("host") + "/d.html";
		} else {
			strHttp = "http://" + request.getHeader("host") + url;
		}
		response.setContentType("image/png");
		QRCode.encoderQRCode(strHttp, response.getOutputStream());
	}

	/**
	 * 获取资源文件
	 * @param request
	 * @param key
	 * @return
	 */
	public String getMessageResources(HttpServletRequest request, String key) {
		String value = "";
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
		return value;
	}

	/**
	 * 获取用户登录信息
	 * @param request
	 * @return
	 */
	private LoginBean getLoginBean(HttpServletRequest request) {
		Object o = request.getSession().getAttribute("LoginBean");
		if (o != null) {
			return (LoginBean) o;
		}
		return null;
	}

	/**
	 * @param 把java对象 转换成json格式
	 * @param response
	 * @throws IOException 
	 */
	public void writeJson(Object obj, HttpServletResponse response) {
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String json = gson.toJson(obj);
		out.println(json);
		out.flush();
		out.close();
	}

	public void init() throws ServletException {
	}

	public CommonServlet() {
		super();
	}

	public void destroy() {
		super.destroy();

	}
	/**
	 * 微信设置更新
	 * @param req
	 * @param resp
	 */
	private void updateWxSet(HttpServletRequest req, HttpServletResponse resp){
		String KeyName=req.getParameter("KeyName");
		String Flag=req.getParameter("Flag");
		String CorpID=req.getParameter("CorpID");
		String Secret=req.getParameter("Secret");
		String AgentId_check=req.getParameter("AgentId_check");
		String RemoteUrl=req.getParameter("RemoteUrl");
		new SystemSetMgt().updateWxSet(Flag, CorpID, Secret, AgentId_check, RemoteUrl, KeyName);
		WXSetting.getInstance().register(KeyName, CorpID, Secret, Integer.parseInt(AgentId_check), RemoteUrl, Boolean.parseBoolean(Flag));
		if (KeyName.equals(WXSetting.AGENTKEYNAME_WXWORK)) {
			String ContactSecret = req.getParameter("ContactSecret");
			String ContactAgentId_check = req.getParameter("ContactAgentId_check");
			new SystemSetMgt().updateWxSet(Flag, CorpID, ContactSecret, ContactAgentId_check, "", WXSetting.AGENTKEYNAME_WXWORKCONTACT);
			WXSetting.getInstance().register(WXSetting.AGENTKEYNAME_WXWORKCONTACT, CorpID, ContactSecret, Integer.parseInt(ContactAgentId_check), RemoteUrl, Boolean.parseBoolean(Flag));
		}
	}
}
