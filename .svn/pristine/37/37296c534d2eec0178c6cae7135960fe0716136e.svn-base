package com.koron.oa.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;

/**
 * 附件预览
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Jun 27, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class PreviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException  {
		
        String view = request.getParameter("view");
    	String images = ".jpg,.jpeg,.bmp,.gif,.png";
    	String extOffice = ".xls,.xlsx,.doc,.docx,.ppt,.pptx,";
    	String texts = ".txt";
    	
    	if(view!=null && view.equals("data")){
    		String path = request.getParameter("path");
            path = GlobalsTool.toChinseChar(path);
			try {
				FileHandler.readFile(path,response);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	String tableName = request.getParameter("tableName");
    	String type = request.getParameter("type");
    	String fileName = request.getParameter("fileName");
    	fileName = GlobalsTool.toChinseChar(fileName);
    	
    	String path = FileHandler.getPathReal(tableName,"PIC".equals(type) ?FileHandler.TYPE_PIC :FileHandler.TYPE_AFFIX,fileName);
    	String ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
    	
    	if(images.contains(ext)){
    		ext = "images";
    	}else if(extOffice.contains(ext)){
    		ext = "office";
    	}else if(texts.contains(ext)){
    		ext = "txt";
    	}else if("pdf".equals(ext)){
    		ext = "pdf";
    	}
    	request.setAttribute("path", GlobalsTool.encode(path));
    	request.setAttribute("ext", ext);
    	request.getRequestDispatcher("/common/previewFile.jsp").forward(request, response);
	}

	public PreviewServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}

	public void init() throws ServletException {
		
	}

}
