package com.koron.oa.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.GlobalsTool;

import java.util.ArrayList;

/**
 * <p>Title: 用于上传附件</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company:科荣软件</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class UploadServlet extends HttpServlet {
	
    public void service(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
    	//System.out.println("==="+request.getHeader("Cookies"),request.getHeaderNames()); 
    	
    	//System.out.println("request.getSession().getId():"+request.getSession().getId());
    	
    	if (request.getSession().getAttribute("LoginBean") == null) {
			PrintWriter out = response.getWriter();
			BaseEnv.log.debug("UploadServlet no login 请求地址："+request.getRequestURI()+(request.getQueryString()==null?"":"?"+request.getQueryString()));
			out.print("close");
			return;
		}
    	
    	
        request.setCharacterEncoding("UTF-8");
        
        String path = request.getParameter("path");        
        
        String tempPath = System.getProperty("user.dir")+"\\..\\temp";
        File temp = new File(tempPath);
        if(!temp.exists()){
        	temp.mkdirs();
        }
        path = new String(path.getBytes("ISO8859-1"),"UTF-8");
        int ind =  path.lastIndexOf(",uploadTree.jsp");
        if(!path.equals(GlobalsTool.getSysSetting("picPath").replaceAll("\\\\","/"))){   
	        if(ind !=-1){
	           path = path.substring(0,ind);
	        } else {
	        	path = BaseEnv.FILESERVERPATH+path;
	        }
        }
        
        String fieldName= request.getParameter("fieldName");
      
        
     
        File pfile = new File(path);
        if(!pfile.exists()){
        	pfile.mkdirs();
        }        
        
        String filePrex = request.getParameter("filePrex");
        if(filePrex != null){
        	filePrex = new String(filePrex.getBytes("ISO8859-1"),"UTF-8");
        }
        
        String fileType = request.getParameter("fileType");
        
        
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(0); 
        FileUpload fileupload=new FileUpload(factory); 
        
        ServletFileUpload df = new ServletFileUpload(factory);  
        df.setHeaderEncoding("UTF-8"); 
        
        try {
            List<FileItem> list = df.parseRequest(request);
            for (FileItem item : list) {
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    String value = item.getString("UTF-8");
                } else {
                    String name = item.getFieldName();
                    String value = item.getName();
                    if(null!=value && !"".equals(value)){
                    	long maxsize= Long.parseLong(BaseEnv.systemSet.get("defaultAttachSize").getSetting());
                    	if(maxsize > 0 && item.getSize() > maxsize*1024*1024){
                    		response.getOutputStream().write(("ERROR:您不能上传超过"+maxsize+"M的附件").getBytes("UTF-8"));
                    		continue;
                    	}
                    		
                        String oldfileName = value;  
                        if(filePrex !=null && filePrex.length() > 0 ){
                        	oldfileName = filePrex+oldfileName;                        	
                        }
                        if(fieldName != null && fieldName.length() > 0){ //如果存在字段名，则加上字段名作后缀，用来区分不同字段，避免重复覆盖
                        	if(oldfileName.indexOf(".") > 0){
                        		oldfileName = oldfileName.substring(0,oldfileName.lastIndexOf(".")) +"_"+fieldName+oldfileName.substring(oldfileName.lastIndexOf("."));
                        	}else{
                        		oldfileName = oldfileName +"_"+fieldName;
                        	}
                        }
                        String fileName = oldfileName;
                        File f = new File(path, fileName);                        
                        for(int i=1;f.exists()&& i<1000;i++){
                        	//文件名存在，改名
                        	fileName = oldfileName.substring(0,oldfileName.lastIndexOf("."))+"("+i+")"+oldfileName.substring(oldfileName.lastIndexOf("."));
                        	f = new File(path, fileName); 
                        }
                     
                        try {
                        	OutputStream os = new FileOutputStream(f);
	                        InputStream is = item.getInputStream();
	                        byte[] buffer = new byte[1*1024*1024];
	                        int length = 0;
	                        while ((length = is.read(buffer)) > 0) {
	                            os.write(buffer, 0, length);
	                        }
	                        os.close();
	                        is.close();
	                        //mj 如果是相册图片上传 进行压缩
 	                        if (StringUtils.contains(path, "/album/img")) {
//	 	                       	String smallPicPath = path+"/small";
//	 	                        File fSmall = new File(smallPicPath, fileName); 
// 	                            //生成新的压缩后的图片
// 	                            String suffix = oldfileName.substring(oldfileName.lastIndexOf(".")+1);
// 	                            //创建压缩
// 		                        ImgManagerUtil.createImageByImageIo(f,fSmall , suffix);
	 	                       ImgManagerUtil.toSmallerpic(path,f, "_small", fileName, 130, 110,
	 	              				(float) 0.7);
	                        }
	                        
                        } catch (IOException exio) {
                        	exio.getStackTrace();
                        	response.getOutputStream().write(("ERROR:"+exio.getLocalizedMessage()).getBytes("UTF-8"));
                    		continue;
                        }
                        
                        response.getOutputStream().write(fileName.getBytes("UTF-8"));
                        //response.getOutputStream().write("ERROR:您所在网段不允许上传附件".getBytes("UTF-8"));
                        
                        
                        if (fileName != null && fileType != null && fileType.length() > 0) {
                            //上传成功
                            ArrayList slist ;
                            if("PIC".equals(fileType)){
                                Object o = request.getSession().getAttribute("PIC_UPLOAD");
                                if(o == null){
                                	slist = new ArrayList();
                                    request.getSession().setAttribute("PIC_UPLOAD",slist);
                                }else{
                                	slist = (ArrayList)o;
                                }
                            }else{
                                Object o = request.getSession().getAttribute("AFFIX_UPLOAD");
                                if(o == null){
                                	slist = new ArrayList();
                                    request.getSession().setAttribute("AFFIX_UPLOAD",slist);
                                }else{
                                	slist = (ArrayList)o;
                                }
                            }
                            slist.add(fileName);
                        }
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
}
