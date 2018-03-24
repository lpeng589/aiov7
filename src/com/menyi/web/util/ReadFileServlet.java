package com.menyi.web.util;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.util.*;

import com.koron.oa.util.OfficeToHtml;
import com.menyi.aio.web.customize.CustomizeMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.service.ServicerBean;
import com.menyi.email.util.AIOEMail;

import java.net.URLEncoder;


/**
 *
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
public class ReadFileServlet extends HttpServlet {
	
	

    public void service(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
    	String tempFile = req.getParameter("tempFile"); //是否是临时文件
    	if("serviceDown".equals(tempFile)){ //客服下载文件
    		Object  o= req.getSession().getAttribute("ServicerBean");
			if(o == null){
				return;
			}
			ServicerBean bean = (ServicerBean)o;
			if(!bean.isVal ){
				return;
			}
    	}
    	
        if(req.getSession().getAttribute("LoginBean") == null && null == req.getParameter("mobile")){
            //未登陆
            //这里要加退出
            return ;
        }; 
        
        String view = req.getParameter("view");
        if(view!=null && view.equals("data")){
    		String path = req.getParameter("path");
            path = GlobalsTool.toChinseChar(path);
			try {
				FileHandler.readFile(path,resp);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
        
        String fileName = req.getParameter("fileName");
        String realName = req.getParameter("realName") ;
        fileName = fileName==null?"":new String(fileName.getBytes("ISO-8859-1"),"UTF-8") ;
        realName = realName==null?"":new String(realName.getBytes("ISO-8859-1"),"UTF-8") ;
        
        
        String tableName = req.getParameter("tableName"); //删除正式文件时要提供表名
        String type = req.getParameter("type"); //删除正式文件时要提供类型
        resp.reset();   
        
        if(realName.length()==0){
        	realName = fileName ;
        }
        String fn = realName.indexOf("/")> -1?realName.substring(realName.lastIndexOf("/")+1):realName;
		fn = realName.indexOf("\\")> -1?realName.substring(realName.lastIndexOf("\\")+1):realName;
        
        String userAgent = req.getHeader("user-agent");
        if("PIC".equals(type)){
            resp.setContentType("image/"+fileName.substring(fileName.lastIndexOf(".")+1));
            //resp.setHeader("Content-Disposition", "form-data; name=\"image\";  filename=" + URLEncoder.encode(fn, "UTF-8"));
        }else{
        	String mime = req.getSession().getServletContext().getMimeType(fileName.substring(fileName.lastIndexOf(".")==-1?0:fileName.lastIndexOf(".")).toLowerCase());
        	if(mime == null || mime.length() == 0){
        		mime = "application/octet-stream; CHARSET=utf8";
        		resp.setContentType(mime);
	            //attachment
	            resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, "UTF-8"));
        	}else{
        		resp.setContentType(mime);
        		//resp.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(fn, "UTF-8"));
        		
        		resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, "UTF-8"));
        	}
        }
        try{
        	if("serviceDown".equals(tempFile)){
        		String path = "../../"+fileName;
	        	FileHandler.readFile(path,resp);
	        	
        	}else if ("workflowtemplete".equals(tempFile)) {
	        	String path = BaseEnv.FILESERVERPATH+"/workflowtemplete/"+fileName;
	        	FileHandler.readFile(path,resp);
	        }else if ("workflowfile".equals(tempFile)) {
	        	String path = BaseEnv.FILESERVERPATH+"/workflowFile/"+fileName;
	        	FileHandler.readFile(path,resp);
	        } else if ("email".equals(tempFile)) {
	        	String emlFile = req.getParameter("emlfile");
	        	String attPath = req.getParameter("attPath");
	        	if(emlFile == null || emlFile.length() == 0 || emlFile.equals("null")){
	        		String path = BaseEnv.FILESERVERPATH+"/email/"+(attPath==null?"":attPath+"/")+fileName;
	        		if("previewFile".equals(req.getParameter("type"))){
	    	        	previewFile(req, tableName, resp, fileName, type,path);
	        		}else{
	        			FileHandler.readFile(path,resp);
	        		}
	        	}else{
	        		emlFile = GlobalsTool.toChinseChar(emlFile);
	        		//从emlFile中取数据
	        		InputStream is = AIOEMail.readFileFormEml(BaseEnv.FILESERVERPATH+"/email/"+attPath+"/eml/"+emlFile,fileName, req.getParameter("charset"));		  
	        		byte[] bs = new byte[8192];
	        		int count = 0;
	        		while((count =is.read(bs)) > -1){
	        			resp.getOutputStream().write(bs,0,count);
	        		}
	        		is.close();	        		
	        	}
	        } else if ("bbs".equals(tempFile)) {
	        	String path = BaseEnv.FILESERVERPATH+"/bbs/"+fileName;
	        	FileHandler.readFile(path,resp);
	        } else if ("true".equals(tempFile)) {
	        	String path = FileHandler.getPathTempRead(fileName);
	        	FileHandler.readFile(path,resp);
	        } else if("example".equals(tempFile)){
	        	String path = BaseEnv.FILESERVERPATH+"/example/"+fileName;
	        	FileHandler.readFile(path,resp);
	        } else if("export".equals(tempFile)){
	        	String path = "../../AIOBillExport/"+fileName;
	        	FileHandler.readFile(path,resp);
	        }else if("logo".equals(tempFile)){
	        	String path = req.getSession().getServletContext().getRealPath("logo") ;
	        	fileName = path+"/"+fileName ;
	        	FileHandler.readFile(fileName,resp);
	        } else if("print".equals(tempFile)){
	        	fileName = BaseEnv.FILESERVERPATH;
	    		if (!fileName.trim().endsWith("/")) {
	    			fileName = fileName.trim() + "/";
	    		}
	    		fileName = fileName + "pic/logo.bmp" ;
	    		FileHandler.readFile(fileName,resp);
	        } else if("tableSql".equals(tempFile)){ //生成sql脚本
	        	String ret =new CustomizeMgt().getTableSql(tableName);
	        	OutputStream out = resp.getOutputStream();
	            byte[] b = new byte[20 * 1024];
	            int i = 0;
	            out.write(ret.getBytes("gbk"));
	        } else if ("path".equals(tempFile)) {
	        	//目前能够处理pdf文件，所以凡是pdf的文件都导入到新的/common/readPDF.jsp页面
	        	String albumType= req.getParameter("albumType");
	        	String pdfDownload = GlobalsTool.getSysSetting("pdfDownload");
	        	if(pdfDownload != null && pdfDownload.equals("true") && fileName.toLowerCase().endsWith(".pdf")&&!"true".equals(req.getParameter("down")) 
	        			&& !"tree".equals(albumType) && !userAgent.contains("iPad") && !userAgent.contains("iPhone")){
	        		req.setAttribute("path", req.getParameter("path"));	        		
	        		req.setAttribute("fileName", GlobalsTool.toChinseChar(req.getParameter("fileName")));
	        		req.setAttribute("onDown", req.getParameter("onDown"));
	        		resp.setContentType("text/html;charset=UTF-8");
	            	resp.setHeader("Content-Disposition", ""); 
	        		req.getRequestDispatcher("/common/readPDF.jsp").forward(req, resp); 
	        	}else{		        	
		        	 albumType= req.getParameter("albumType");
		        	String path = BaseEnv.FILESERVERPATH+req.getParameter("path")+fileName;
		        	if ("tree".equals(albumType)) {
		        		path = req.getParameter("path");
		        		path = GlobalsTool.toChinseChar(path);
		        		path = path + "\\"+fileName;
		        		//path = path.replaceAll("\\\\\\\\", "\\\\");
		        	}
		        	FileHandler.readFile(path,resp);
	        	}
	        }else {
	        	//处理自定义的附件下载
	        	String path = FileHandler.getPathReal(tableName,
	                    "PIC".equals(type) ?FileHandler.TYPE_PIC :FileHandler.TYPE_AFFIX,fileName);
	        	if("true".equals(req.getParameter("YS"))){
	        		FileHandler.compressPic(path, resp);
	        	}else{
	        		String pdfDownload = GlobalsTool.getSysSetting("pdfDownload");
		        	if(pdfDownload != null && pdfDownload.equals("true") && fileName.toLowerCase().endsWith(".pdf") 
		        			&& !"true".equals(req.getParameter("down"))
		        			&& !userAgent.contains("iPad") && !userAgent.contains("iPhone")){
		        		req.setAttribute("path", path.substring(0,path.lastIndexOf("/")+1));	        		
		        		req.setAttribute("fileName", GlobalsTool.toChinseChar(req.getParameter("fileName")));
		        		resp.setContentType("text/html;charset=UTF-8");
		            	resp.setHeader("Content-Disposition", ""); 
		            	req.getRequestDispatcher("/common/readPDF.jsp").forward(req, resp); 
		        	}else{
			        	String albumType= req.getParameter("albumType");
			        	if ("tree".equals(albumType)) {
			        		path = req.getParameter("path");
			        		path = GlobalsTool.toChinseChar(path);
			        	}
			        	FileHandler.readFile(path,resp);
		        	}
	        	}
	        }
        }catch(Exception e){
        	BaseEnv.log.error("ReadFileServlet.service Error ",e);
        	resp.setContentType("text/html;charset=UTF-8");
        	resp.setHeader("Content-Disposition", null);
        	String alertMsg = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><script language='javascript'>alert('"+getMessageResources(req,"oa.common.fileDownLoadError")+"');window.close();</script></head><body></body></html>" ;
    		resp.getOutputStream().write(alertMsg.getBytes("UTF-8")) ;
        }
    }
    
    public void previewFile(HttpServletRequest req,String tableName,HttpServletResponse resp,
    		String fileName,String type,String path) throws Exception{
    	if(path==null || path.length()==0){
    		path = FileHandler.getPathReal(tableName,"PIC".equals(type) ?FileHandler.TYPE_PIC :FileHandler.TYPE_AFFIX,fileName);
    	}
    	String ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
    	String strImages = ".jpg,.jpeg,.bmp,.gif,.png";
    	String extOffice = ".xls,.xlsx,.doc,.docx";//,.ppt,.pptx,
    	String strTexts  = ".txt,.sql,.css";
    	String strExt = "";
    	File file = new File(path);
    	if(!file.exists()){
    		resp.setContentType("text/html;charset=UTF-8");
        	resp.setHeader("Content-Disposition", null);
        	String alertMsg = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><script language='javascript'>alert('"+getMessageResources(req,"oa.common.fileDownLoadError")+"');window.close();</script></head><body></body></html>" ;
    		resp.getOutputStream().write(alertMsg.getBytes("UTF-8")) ;
    		return;
    	}
    	if(strImages.contains(ext)){
    		strExt = "images";
    	}else if(extOffice.contains(ext)){
    		strExt = "office";
    		File tempPath = new File( req.getSession().getServletContext().getRealPath("/")+"temp");
    		if(!tempPath.exists()){
    			tempPath.mkdirs();
    		}
    		String filePath = OfficeToHtml.toHTML(path, tempPath.getPath(), ext);
    		if(filePath==null || filePath.length()==0){
            	resp.setContentType("text/html;charset=UTF-8");
            	resp.setHeader("Content-Disposition", null);
            	String alertMsg = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><script language='javascript'>alert('"+getMessageResources(req,"oa.common.fileDownLoadError")+"');window.close();</script></head><body></body></html>" ;
        		resp.getOutputStream().write(alertMsg.getBytes("UTF-8")) ;
        		return;
    		}
    		req.setAttribute("p",filePath); 
    	}else if(strTexts.contains(ext)){
    		strExt = "txt";
    		req.setAttribute("html", FileHandler.readFile(path));
    	}else if("pdf".equals(ext)){
    		strExt = "pdf";
    	}
    	req.setAttribute("path", GlobalsTool.encode(path));
    	req.setAttribute("ext", strExt);
    	resp.setHeader("Content-Disposition", ""); 
    	req.getRequestDispatcher("/common/previewFile.jsp").forward(req, resp);
    }
    /**
     * 获取资源文件
     * @param request
     * @param key
     * @return
     */
    public String getMessageResources(HttpServletRequest request, String key) {
		String value = "";
		Object o = request.getSession().getServletContext().getAttribute(
				org.apache.struts.Globals.MESSAGES_KEY);
		if (o instanceof MessageResources) {
			MessageResources resources = (MessageResources) o;
			value = resources.getMessage(GlobalsTool.getLocale(request), key);
		}

		return value;
	}
}
