package com.menyi.aio.web.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.advance.AdvanceAction;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.ServerConnection;
import com.menyi.web.util.SystemState;
public class ServiceAction extends BaseAction {

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		// TODO Auto-generated method stub
		String opType = req.getParameter("opType");
		if("login".equals(opType)){
			return null;
		}else if("valator".equals(opType)){
			Object  o= req.getSession().getAttribute("ServicerBean");
			if(o == null){
				return getForward(req, mapping, "indexPage");
			}
		}else if("auth".equals(opType)){ //����ϵͳ����Ա������Ȩ����
			LoginBean  o= this.getLoginBean(req);
			if(o == null || !o.getId().equals("1")){
				return getForward(req, mapping, "indexPage");
			}
		}else{
			Object  o= req.getSession().getAttribute("ServicerBean");
			if(o == null){
				return getForward(req, mapping, "indexPage");
			}
			ServicerBean bean = (ServicerBean)o;
			if(!bean.isVal ){
				return getForward(req, mapping, "indexPage");
			}
		}
		return null;
	}

	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String opType = request.getParameter("opType");
		ActionForward forward = null;
		if("login".equals(opType)){
			return this.login(mapping, form, request, response);
		}else if("valator".equals(opType)){			
			return this.valator(mapping, form, request, response);
		}else if("auth".equals(opType)){
			return this.auth(mapping, form, request, response);
		}else if("database".equals(opType)){
			return this.database(mapping, form, request, response);
		}else if("file".equals(opType)){
			return this.file(mapping, form, request, response);
		}else if("fileUpload".equals(opType)){
			return this.fileUpload(mapping, form, request, response);
		}else if("logList".equals(opType)){
			return this.logList(mapping, form, request, response);
		}else if("recoveFile".equals(opType)){
			return this.recoveFile(mapping, form, request, response);
		}else if("deleteFile".equals(opType)){
			return this.deleteFile(mapping, form, request, response);
		}else if("addFile".equals(opType)){
			return this.addFile(mapping, form, request, response);
		}else if("addPath".equals(opType)){
			return this.addPath(mapping, form, request, response);
		}else if("refreshMem".equals(opType)){
			return this.refreshMem(mapping, form, request, response);
		}else if("logout".equals(opType)){
			return this.logout(mapping, form, request, response);
		}
		return forward;
	}
	
protected ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "alert");
		ServicerBean bean = (ServicerBean)request.getSession().getAttribute("ServicerBean");
		request.getSession().removeAttribute("ServicerBean");
    	
    	//��֤�ɹ��������ҳ
    	return getForward(request, mapping, "indexPage"); 
        
	}
	
	protected ActionForward database(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "alert");
		ServicerBean bean = (ServicerBean)request.getSession().getAttribute("ServicerBean");
		
    	
		if("true".equals(request.getParameter("exec"))){
			String sql = request.getParameter("sql");
			request.setAttribute("sql", sql);
			
			BaseEnv.log.info("ServiceAction "+bean.getWorkNo()+bean.getName()+ bean.getCompany()+"ִ�пͷ�sql:"+sql);
			ServiceMgt mgt = new ServiceMgt();
			Result rs = mgt.exec(sql);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				if(rs.retVal instanceof List){
					ArrayList result = (ArrayList)rs.retVal;
					request.setAttribute("resultTitle", result.get(0));
					result.remove(0);
					request.setAttribute("result", result);					
				}else{
					if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
						//��¼��־
						mgt.addLog(bean.getWorkNo(), bean.getName(), bean.getCompany(), 0, sql,"",BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
					}
					request.setAttribute("msg", rs.retVal);		
				}
			}else{
				String msg = rs.retVal+"";
				if(msg ==null || msg.length() ==0){
					msg = "ִ�д���";
				}
				request.setAttribute("msg", msg);		
			}
		}
    	
    	//��֤�ɹ��������ҳ
    	return getForward(request, mapping, "database"); 
        
	}
	

	
	protected ActionForward file(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "alert");   
		String sortName = request.getParameter("sortName");
		sortName = sortName==null || sortName.length()==0?"name":sortName;
		boolean sortDir = "false".equals(request.getParameter("sortDir"))?false:true;
		request.setAttribute("sortName", sortName);		
		request.setAttribute("sortDir", sortDir);
    	    	
    	String path=request.getParameter("path");
    	path = path == null? "":new String(path.getBytes("ISO8859-1"),"UTF-8");
    	request.setAttribute("path", path);
    	if(path == null || path.equals("")){
    		path = "../../";
    	}else{
    		path = "../../"+path;
    	}
    	
    	File fpath = new File(path);
    	
    	File[] listFiles = fpath.listFiles();
    	ArrayList<File> dirFiles = new ArrayList<File>();//����Ŀ¼
    	ArrayList<File> fileFiles = new ArrayList<File>();//�����ļ�
    	ArrayList<File> sortdirFiles = new ArrayList<File>();//����Ŀ¼ �����
    	ArrayList<File> sortfileFiles = new ArrayList<File>();//�����ļ� �����
    	if(listFiles !=null){
	    	for(File f:listFiles){
	    		if(!f.getName().startsWith(".")){
	    			if((path.equals("../../") || path.equals("../../\\")) && f.getName().equals("bakFile")){
	    				continue;
	    			}
		    		if(f.isFile()){
		    			fileFiles.add(f);
		    		}else{
		    			dirFiles.add(f);
		    		}
	    		}
	    	}
    	}
    	if("name".equals(sortName)){
    		for(File f:dirFiles){
    			int i=0;
    			for(i=0;i<sortdirFiles.size();i++){
    				File sf = sortdirFiles.get(i);
    				if(sortDir){//����
        				if(f.getName().compareTo(sf.getName())<=0){
        					break;
        				}
        			}else{
        				if(f.getName().compareTo(sf.getName())>=0){
        					break;
        				}
        			}
				} 
    			sortdirFiles.add(i,f);
    		}
    		for(File f:fileFiles){
    			int i=0;
    			for(i=0;i<sortfileFiles.size();i++){
    				File sf = sortfileFiles.get(i);
    				if(sortDir){//����
        				if(f.getName().compareTo(sf.getName())<=0){
        					break;
        				}
        			}else{
        				if(f.getName().compareTo(sf.getName())>=0){
        					break;
        				}
        			}
				} 
    			sortfileFiles.add(i,f);
    		}
    	}else if("update".equals(sortName)){ //������ʱ������

    		for(File f:dirFiles){
    			int i=0;
    			for(i=0;i<sortdirFiles.size();i++){
    				File sf = sortdirFiles.get(i);
    				if(sortDir){//����
        				if(f.lastModified()-sf.lastModified()<=0){
        					break;
        				}
        			}else{
        				if(f.lastModified()-sf.lastModified()>=0){
        					break;
        				}
        			}
				} 
    			sortdirFiles.add(i,f);
    		}
    		for(File f:fileFiles){
    			int i=0;
    			for(i=0;i<sortfileFiles.size();i++){
    				File sf = sortfileFiles.get(i);
    				if(sortDir){//����
        				if(f.lastModified()-sf.lastModified()<=0){
        					break;
        				}
        			}else{
        				if(f.lastModified()-sf.lastModified()>=0){
        					break;
        				}
        			}
				} 
    			sortfileFiles.add(i,f);
    		}    	
    	}else if("type".equals(sortName)){ //��������
    		sortdirFiles.addAll(dirFiles);    		
    		for(File f:fileFiles){
    			int i=0;
    			for(i=0;i<sortfileFiles.size();i++){
    				File sf = sortfileFiles.get(i);
    				String ft = f.getName();
    				ft = (ft.indexOf(".")>0?ft.substring(ft.lastIndexOf(".")+1):"").toLowerCase();
    				String sft = sf.getName();
    				sft = (sft.indexOf(".")>0?sft.substring(sft.lastIndexOf(".")+1):"").toLowerCase();
    				if(sortDir){//����
        				if(ft.compareTo(sft)<=0){
        					break;
        				}
        			}else{
        				if(ft.compareTo(sft)>=0){
        					break;
        				}
        			}
				} 
    			sortfileFiles.add(i,f);
    		}    	
    	}else if("size".equals(sortName)){ //��С����
    		sortdirFiles.addAll(dirFiles);    		
    		for(File f:fileFiles){
    			int i=0;
    			for(i=0;i<sortfileFiles.size();i++){
    				File sf = sortfileFiles.get(i);
    				if(sortDir){//����
        				if(f.length()-sf.length()<=0){
        					break;
        				}
        			}else{
        				if(f.length()-sf.length()>=0){
        					break;
        				}
        			}
				} 
    			sortfileFiles.add(i,f);
    		}    	
    	}
    	
    	ArrayList fList = new ArrayList();
    	if(sortDir){
    		for(File f:sortdirFiles){
        		String[] fs = new String[6];
        		fs[0] = f.getName();
        		fs[1] = BaseDateFormat.format(new Date(f.lastModified()),BaseDateFormat.yyyyMMddHHmmss);
        		fs[2] ="Ŀ¼";
        		fs[3] = "";
        		fs[4] = "PATH";
        		String fPath = f.getAbsolutePath();
        		fPath = fPath.substring(fPath.indexOf("..\\..")+5);
        		fs[5] = fPath;
        		fList.add(fs);
        	}
        	for(File f:sortfileFiles){
        		String[] fs = new String[6];
        		fs[0] = f.getName();
        		fs[1] = BaseDateFormat.format(new Date(f.lastModified()),BaseDateFormat.yyyyMMddHHmmss);
        		fs[2] = "�ļ�";
        		long len = f.length();
        		if(len> 10 * 1024){
        			len = (len*100/1024) /100;
        			if(len> 10 * 1024){
        				len = (len*100/1024) /100;
            			if(len> 10 * 1024){
            				len = (len*100/1024) /100;            			
                			fs[3] = len+"G";
            			}else{
            				fs[3] = len+"M";
            			}
        			}else{
        				fs[3] = len+"K";
        			}
        		}else{
        			fs[3] = len+"B";
        		}    	
        		fs[4] = "FILE";
        		String fPath = f.getAbsolutePath();
        		fPath = fPath.substring(fPath.indexOf("..\\..")+5);
        		fs[5] = fPath;
        		fList.add(fs);
        	}
    	}else{    		
        	for(File f:sortfileFiles){
        		String[] fs = new String[6];
        		fs[0] = f.getName();
        		fs[1] = BaseDateFormat.format(new Date(f.lastModified()),BaseDateFormat.yyyyMMddHHmmss);
        		fs[2] = "�ļ�";
        		long len = f.length();
        		if(len> 10 * 1024){
        			len = (len*100/1024) /100;
        			if(len> 10 * 1024){
        				len = (len*100/1024) /100;
            			if(len> 10 * 1024){
            				len = (len*100/1024) /100;            			
                			fs[3] = len+"G";
            			}else{
            				fs[3] = len+"M";
            			}
        			}else{
        				fs[3] = len+"K";
        			}
        		}else{
        			fs[3] = len+"B";
        		}    	
        		fs[4] = "FILE";
        		String fPath = f.getAbsolutePath();
        		fPath = fPath.substring(fPath.indexOf("..\\..")+5);
        		fs[5] = fPath;
        		fList.add(fs);
        	}
        	for(File f:sortdirFiles){
        		String[] fs = new String[6];
        		fs[0] = f.getName();
        		fs[1] = BaseDateFormat.format(new Date(f.lastModified()),BaseDateFormat.yyyyMMddHHmmss);
        		fs[2] ="Ŀ¼";
        		fs[3] = "";
        		fs[4] = "PATH";
        		String fPath = f.getAbsolutePath();
        		fPath = fPath.substring(fPath.indexOf("..\\..")+5);
        		fs[5] = fPath;
        		fList.add(fs);
        	}
    	}
    	
    	
    	request.setAttribute("fileList", fList);
    	String curPath = fpath.getAbsolutePath();
    	curPath = curPath.substring(curPath.indexOf("..\\..")+5);
    	if(curPath.length() ==0){
    		curPath = "\\";
    	}
    	String linkPath = "";
    	if(curPath.equals("\\")){
    		linkPath = "��Ŀ¼\\";
    	}else{
    		boolean first = true;
	    	while(curPath.length() > 0){
	    		String p = curPath.substring(curPath.lastIndexOf("\\")+1);    	
	    		if(first){
	    			linkPath =p+"\\";
	    		}else{
	    			linkPath ="<a href=\"javascript:toDir('"+curPath.replace("\\", "\\\\")+"')\"/>"+p+"</a>\\"+linkPath;
	    		}
	    		curPath = curPath.substring(0,curPath.lastIndexOf("\\"));
	    		first = false;
	    	}
	    	linkPath = "<a href=\"javascript:toDir('\\\\')\"/>��Ŀ¼</a>\\"+ linkPath;
    	}
    	
    	request.setAttribute("curPath", linkPath);
    	
    	//��֤�ɹ��������ҳ
    	return getForward(request, mapping, "file"); 
        
	}
	
	protected ActionForward logList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "alert");   
		String logfileName = request.getParameter("logfileName");
		logfileName = logfileName==null?"":new String(logfileName.getBytes("ISO8859-1"),"UTF-8");
		request.setAttribute("logfileName", logfileName);
		
		String workNo= request.getParameter("workNo");
		request.setAttribute("workNo", workNo);
		String name= request.getParameter("name");
		request.setAttribute("name", name);
		String company= request.getParameter("company");
		request.setAttribute("company", company);
		String content= request.getParameter("content");
		request.setAttribute("content", content);
		String startTime= request.getParameter("startTime");
		request.setAttribute("startTime", startTime);
		String endTime= request.getParameter("endTime");
		request.setAttribute("endTime", endTime);
		String type= request.getParameter("type");
		request.setAttribute("type", type);
		int typei = type==null?-1:Integer.parseInt(type);
		
		
    	ServiceMgt mgt = new ServiceMgt();
    	Result rs = mgt.logList(workNo, name, company, typei, content,logfileName, startTime, endTime);
    	request.setAttribute("result", rs.retVal);
    	//��֤�ɹ��������ҳ
    	return getForward(request, mapping, "logList"); 
        
	}
	protected ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "alert");   
		String fileName = request.getParameter("fileName");
		fileName = fileName==null?"":new String(fileName.getBytes("ISO8859-1"),"UTF-8");
		request.setAttribute("fileName", fileName);
		
		String fn = "../.."+fileName;
		File f = new File(fn);
		String msg = "";
		if(f.isDirectory() && f.listFiles() != null && f.listFiles().length > 0){
			msg = "Ŀ¼�ǿգ�����ɾ��";
		}else{
	    	String time = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
	    	String time2 = BaseDateFormat.format(BaseDateFormat.parse(time, BaseDateFormat.yyyyMMddHHmmss), BaseDateFormat.yyyyMMddHHmmss2);
	    	//����
	    	if(f.isFile()){
		    	File bakF = new File("../../bakFile/"+time2+"/"+f.getName());  
		    	bakF.getParentFile().mkdirs();
		    	FileInputStream fis = new FileInputStream(f);
		    	
		    	FileOutputStream bakOs = new FileOutputStream(bakF);
		    	byte[] buffer = new byte[1*1024*1024];
		        int length = 0;
		        while ((length = fis.read(buffer)) > 0) {
		        	bakOs.write(buffer, 0, length);
		        }
		        bakOs.close();
		        fis.close();
	    	}
			boolean ret =f.delete();
			msg  = ret?"ɾ���ɹ�":"ɾ��ʧ��";
			
			if(ret){
		        //��¼��־
		        ServiceMgt mgt = new ServiceMgt();
		        ServicerBean bean = (ServicerBean)request.getSession().getAttribute("ServicerBean");
		        mgt.addLog(bean.getWorkNo(), bean.getName(), bean.getCompany(), 3,fileName,fileName, time);
			}
		}
		request.setAttribute("msg", msg);
    		
    	//��֤�ɹ��������ҳ
    	return this.file(mapping, form, request, response);
        
	}
	protected ActionForward addFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "alert");   
		String fileName = request.getParameter("fileName");
		fileName = fileName==null?"":new String(fileName.getBytes("ISO8859-1"),"UTF-8");
		request.setAttribute("fileName", fileName);
		
    	if("true".equals(request.getParameter("exec"))){
    		
    		DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(0); 
            FileUpload fileupload=new FileUpload(factory); 
            
            ServletFileUpload df = new ServletFileUpload(factory);  
            df.setHeaderEncoding("UTF-8"); 
            
    	}    	
    	//��֤�ɹ��������ҳ
    	return getForward(request, mapping, "logList"); 
        
	}
	protected ActionForward addPath(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "alert");   
		String path = request.getParameter("path");
		path = path==null?"":new String(path.getBytes("ISO8859-1"),"UTF-8");
		path ="../.."+path;
		String newDir = request.getParameter("newDir");
		newDir = newDir==null?"":new String(newDir.getBytes("ISO8859-1"),"UTF-8");
		path +="/"+newDir;
		File f = new File(path);
		if(f.mkdirs()){
			request.setAttribute("msg", "�½��ļ��гɹ�");
		}else{
			request.setAttribute("msg", "�½��ļ���ʧ��");
		}
		String time = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
        //��¼��־
        ServiceMgt mgt = new ServiceMgt();
        ServicerBean bean = (ServicerBean)request.getSession().getAttribute("ServicerBean");
        mgt.addLog(bean.getWorkNo(), bean.getName(), bean.getCompany(), 4,newDir,newDir, time);
    	//��֤�ɹ��������ҳ
    	return getForward(request, mapping, "blank"); 
        
	}
	protected ActionForward recoveFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "alert");   
		String fileName = request.getParameter("fileName");
		fileName = fileName==null?"":new String(fileName.getBytes("ISO8859-1"),"UTF-8");
		request.setAttribute("fileName", fileName);
		
		String logfileName = request.getParameter("logfileName");
		logfileName = logfileName==null?"":new String(logfileName.getBytes("ISO8859-1"),"UTF-8");
		request.setAttribute("logfileName", logfileName);
		
		String createTime = request.getParameter("createTime");
		File f = new File("../.."+fileName); 
		//�ҵ������ļ�
		String path = "../../bakFile/"+BaseDateFormat.format(BaseDateFormat.parse(createTime, BaseDateFormat.yyyyMMddHHmmss), BaseDateFormat.yyyyMMddHHmmss2);
		path+="/"+f.getName();
		File bakFile = new File(path);
		if(!bakFile.exists()){
			EchoMessage.error().add("�����ļ��Ѿ���ʧ���ָ�ʧ��").setBackUrl("/ServiceAction.do?opType=logList&logfileName="+URLEncoder.encode(logfileName,"UTF-8")).setAlertRequest(request);
	    	return forward; 
		}
		try{
			 
	    	String time = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
	    	String time2 = BaseDateFormat.format(BaseDateFormat.parse(time, BaseDateFormat.yyyyMMddHHmmss), BaseDateFormat.yyyyMMddHHmmss2);
	    	//����
	    	if(f.exists()){ //���ɾ���ָ��ǲ���ģ����Բ��豸��
		    	File bakF = new File("../../bakFile/"+time2+"/"+f.getName());  
		    	bakF.getParentFile().mkdirs();
		    	FileInputStream fis = new FileInputStream(f);
		    	
		    	FileOutputStream bakOs = new FileOutputStream(bakF);
		    	byte[] buffer = new byte[1*1024*1024];
		        int length = 0;
		        while ((length = fis.read(buffer)) > 0) {
		        	bakOs.write(buffer, 0, length);
		        }
		        bakOs.close();
		        fis.close();
	    	}
	        
			//�ָ��ļ�
	    	FileInputStream fis = new FileInputStream(bakFile);
	    	
            //��jar�ļ�ֱ�Ӹ���,jar�ļ��ȱ��ݵ�ָ��Ŀ¼,��������ʱ����ʦ�ķ��񿽱���ȥ
        	if(fileName.startsWith("\\website\\WEB-INF\\") && f.getName().toLowerCase().endsWith(".jar")){
        		f= new File("../../bakFile/jar/"+f.getName());     
        		f.getParentFile().mkdirs();
        	}
			
	    	FileOutputStream fos = new FileOutputStream(f);
	    	byte[] buffer = new byte[1*1024*1024];
	        int length = 0;
	        while ((length = fis.read(buffer)) > 0) {
	        	fos.write(buffer, 0, length);
	        }
	        fos.close();
	        fis.close();
	        //��¼��־
	        ServiceMgt mgt = new ServiceMgt();
	        ServicerBean bean = (ServicerBean)request.getSession().getAttribute("ServicerBean");
	        mgt.addLog(bean.getWorkNo(), bean.getName(), bean.getCompany(), 2,"�ָ�"+createTime+"����",fileName, time);
	        
	        EchoMessage.success().add("�ָ��ɹ�,�粻��������Ч������������").setBackUrl("/ServiceAction.do?opType=logList&logfileName="+URLEncoder.encode(logfileName,"UTF-8")).setAlertRequest(request);
	    	return forward; 
		}catch(Exception ex){
			EchoMessage.error().add("�ָ�ʧ��"+ex.getMessage()).setBackUrl("/ServiceAction.do?opType=logList&logfileName="+URLEncoder.encode(logfileName,"UTF-8")).setAlertRequest(request);
	    	return forward; 
			
		}
		
		
        
	}
	
	protected ActionForward fileUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "alert");   
		String fileName = request.getParameter("fileName");
		fileName = fileName==null?"":new String(fileName.getBytes("ISO8859-1"),"UTF-8");
		request.setAttribute("fileName", fileName);
		
		String path = request.getParameter("path");
		path = path==null?"":new String(path.getBytes("ISO8859-1"),"UTF-8");
		request.setAttribute("path", path);
		
    	if("true".equals(request.getParameter("exec"))){
    		
    		DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(0); 
            FileUpload fileupload=new FileUpload(factory); 
            
            ServletFileUpload df = new ServletFileUpload(factory);  
            df.setHeaderEncoding("UTF-8"); 
            
            try {
            	ServicerBean bean = (ServicerBean)request.getSession().getAttribute("ServicerBean");
                List<FileItem> list = df.parseRequest(request);
                for (FileItem item : list) {
                    if (item.isFormField()) {
                        String name = item.getFieldName();
                        if("fileName".equals(name)){
                        	fileName =item.getString("UTF-8");
                        	request.setAttribute("fileName", fileName);
                        }
                        if("path".equals(name)){
                        	path =item.getString("UTF-8");
                        	request.setAttribute("fileName", path);
                        }
                    } else {	
                    	String time = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
                    	String time2 = BaseDateFormat.format(BaseDateFormat.parse(time, BaseDateFormat.yyyyMMddHHmmss), BaseDateFormat.yyyyMMddHHmmss2);
                    	String name = item.getName();
                    	String ext = name.indexOf(".")>0?name.substring(name.indexOf(".")):"";
                    	File f = null;
                    	if(path != null && path.length() > 0){ //���ϴ��ļ�
                    		f = new File("../.."+path+"/"+name);  
                    	}else{
	                    	if(!fileName.toLowerCase().endsWith(ext.toLowerCase())){
	                    		request.setAttribute("msg", "alert('�ļ���չ����һ�£����������');");
	                        	return getForward(request, mapping, "fileUpload"); 
	                    	}
	                    	f = new File("../.."+fileName);  
                    	
	                    	//����
	                    	File bakF = new File("../../bakFile/"+time2+"/"+f.getName());  
	                    	bakF.getParentFile().mkdirs();
	                    	FileInputStream fis = new FileInputStream(f);
	                    	
	                    	FileOutputStream bakOs = new FileOutputStream(bakF);
	                    	byte[] buffer = new byte[1*1024*1024];
	                        int length = 0;
	                        while ((length = fis.read(buffer)) > 0) {
	                        	bakOs.write(buffer, 0, length);
	                        }
	                        bakOs.close();
	                        fis.close();
	                        
	                        //��jar�ļ�ֱ�Ӹ���,jar�ļ��ȱ��ݵ�ָ��Ŀ¼,��������ʱ����ʦ�ķ��񿽱���ȥ
	                    	if(fileName.startsWith("\\website\\WEB-INF\\") && ext.toLowerCase().equals(".jar")){
	                    		f= new File("../../bakFile/jar/"+f.getName());     
	                    		f.getParentFile().mkdirs();
	                    	} 
                    	}
                    	OutputStream os = new FileOutputStream(f);
                        InputStream is = item.getInputStream();
                        byte[] buffer = new byte[1*1024*1024];
                        int length = 0;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer, 0, length);
                        }
                        os.close();
                        is.close();
	                        
	                    
                    	
                        
                        //��¼��ʷ
                        ServiceMgt mgt = new ServiceMgt();
                        if(path != null && path.length() > 0){ //���ϴ��ļ�
                        	mgt.addLog(bean.getWorkNo(), bean.getName(), bean.getCompany(), 4,path+"\\"+name,path+"\\"+name, time);
                        }else{
                        	mgt.addLog(bean.getWorkNo(), bean.getName(), bean.getCompany(), 1,fileName,fileName, time);
                        }
                        
                    }
                }
            } catch (Exception ex) {
            	BaseEnv.log.error("ServiceAction.fileUpload Error:",ex);
            	request.setAttribute("msg", "alert('�ϴ�ʧ��');");
            	return getForward(request, mapping, "fileUpload"); 
            }
            request.setAttribute("msg", "alert('�ϴ��ɹ�,�粻��������Ч��������������');parent.jQuery.close('ServiceUpload'); parent.location.href=parent.location.href;");
            
    	}
    	
    	//��֤�ɹ��������ҳ
    	return getForward(request, mapping, "fileUpload"); 
        
	}
	
	protected ActionForward refreshMem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "alert");
		new AdvanceAction().refreshMem(request);
    	
    	//��֤�ɹ��������ҳ
    	return getForward(request, mapping, "service"); 
        
	}
	
	protected ActionForward valator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionForward forward = getForward(request, mapping, "alert");
		String workNo = request.getParameter("sworkNo");
		String valNo = request.getParameter("valNo");   	
    	
    	String serverValNo = (String)request.getSession().getServletContext().getAttribute("ValNo");
    	if(serverValNo==null||serverValNo.split("&").length <2){
    		String msg = "У��ʧ��,�Է���δ��Ȩ";
    		request.setAttribute("msg", msg);
    		return getForward(request, mapping, "loginSuccess");
    	}
    	String[] ss = serverValNo.split("&");
    	if(!valNo.equals(ss[0])){
    		String msg = "��֤�벻��ȷ";
    		request.setAttribute("msg", msg);
    		return getForward(request, mapping, "loginSuccess");
    	}
    	long servTime = BaseDateFormat.parse(ss[1], BaseDateFormat.yyyyMMddHHmmss).getTime();
    	if((System.currentTimeMillis()-servTime) > 2* 60000){
    		String msg = "��֤�볬��2���ӣ��Ѿ�ʧЧ";
    		request.setAttribute("msg", msg);
    		return getForward(request, mapping, "loginSuccess");
    	}
    	Object  o= request.getSession().getAttribute("ServicerBean");
		if(o == null){
			return getForward(request, mapping, "indexPage");
		}
		ServicerBean bean = (ServicerBean)o;
		bean.setVal(true);
    	
    	//��֤�ɹ��������ҳ
    	return getForward(request, mapping, "service"); 
        
	}
	
	protected ActionForward auth(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String password = request.getParameter("spass");
		
		ActionForward forward = getForward(request, mapping, "alert");
		if("true".equals(request.getParameter("exec"))){
			String workNo = request.getParameter("sworkNo");
        	String name = request.getParameter("name");   	
        	String company = request.getParameter("company");
        	request.setAttribute("workNo", workNo);
        	request.setAttribute("name", name);
        	request.setAttribute("company", company);    
        	
        	//���������
        	Random rd = new Random() ;
        	rd.setSeed(System.currentTimeMillis());
        	String istr =""+rd.nextInt(999999999);
        	//���С��5λ����1���������5λ��ȡ���5λ
        	if(istr.length() > 5){
        		istr = istr.substring(istr.length()-5);        		
        	}else if(istr.length() < 5){
        		for(int i=0;i<5-istr.length();i++){
        			istr = "1"+istr;
        		}
        	}
        	request.setAttribute("valNo", istr);
        	istr +="&"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
        	//��valNo�����ڴ�
        	request.getSession().getServletContext().setAttribute("ValNo", istr);
        	
        	/*���ϵͳ��־*/
			new DynDBManager().addLog(14, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), 
					"����"+company+"�ͷ�["+name+"]�ͻ�����Ȩ��", "", "��Ȩ","��Ȩ�ͷ�");
        	
        	return getForward(request, mapping, "authPass"); 
		}else{
			String workNo = request.getParameter("sworkNo");
        	String name = request.getParameter("name");   	
        	String company = request.getParameter("company");
        	name = new String(name.getBytes("ISO8859-1"),"UTF-8");
        	company = new String(company.getBytes("ISO8859-1"),"UTF-8");
        	
//        	name = URLDecoder.decode(name,"UTF-8");
//        	company =URLDecoder.decode(company,"UTF-8");

        	request.setAttribute("workNo", workNo);
        	request.setAttribute("name", name);
        	request.setAttribute("company", company);        	
        	return getForward(request, mapping, "auth");      	
		}
        
	}
	
	protected ActionForward login(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String workNo = request.getParameter("sworkNo");
		String password = request.getParameter("spass");
		
		ActionForward forward = getForward(request, mapping, "alert");

		ServerConnection conn = new ServerConnection(
        		BaseEnv.bol88URL+"/ClientConnection", new byte[] {0x21, 0x12,
                0xF, 0x58,
                (byte) 0x88, 0x10, 0x40, 0x38
                , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2});
		String posStr = "<operation>validServicer</operation>" +
    		"<dogId>" + SystemState.instance.dogId + "</dogId>"+
    		"<workNo>" + workNo + "</workNo>"+
    		"<pass>" + password + "</pass>";
        String ret = conn.send(posStr);
        if (ret != null && "OK".equals(getValue(ret, "result"))) {
        	ServicerBean srb = new ServicerBean();
        	srb.setWorkNo(  workNo);
        	String name = getValue(ret, "name");        	
        	String company = getValue(ret, "company");
        	
        	srb.setName(URLDecoder.decode(name,"UTF-8"));
        	srb.setCompany(URLDecoder.decode(company,"UTF-8"));
        	
        	//������Ϣ��admin
        	String popedomUserIds = "1,";   //֪ͨ����					
			String content = "<a href=\"javascript:mdiwin('/ServiceAction.do?opType=auth&workNo="+workNo+"&name="+name+"&company="+company+"','�ͷ���Ȩ')\">�ͷ�"+srb.getName()+"������Ȩ�� </a>";
			String title = "";
			
			new Thread(new NotifyFashion("1", title,
					content, popedomUserIds, 4, "no",
					"", null, null, "system")).start();
        	
        	request.getSession().setAttribute("ServicerBean", srb);
        	
        	return getForward(request, mapping, "loginSuccess");      	
        }else{
        	String error = getValue(ret, "error");
        	if(error==null|| error.equals("")){
        		error = "�������,��ȷ�����ķ������ܹ�����Internet";
        	}
        	error = URLDecoder.decode(error,"UTF-8");
        	EchoMessage.error().add(error).setBackUrl("/service").setAlertRequest(request);
        	return forward;        
        }
        
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

}
