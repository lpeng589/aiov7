package com.koron.oa.controlPanel;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.menyi.web.util.BaseEnv;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class FileUpload extends HttpServlet {

	public FileUpload() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String flag = request.getParameter("flag");//用于判断是大图片进入还是小图片 1表示大图 2表示小图
		String picShape = "b"; //用于命名图片,b表示大 s表示小
		if(flag.equals("2")){
			picShape = "s";
		}
		
		response.setContentType("text/html");   
		response.setCharacterEncoding("UTF-8");
		String realDir = request.getSession().getServletContext().getRealPath("");
		String contextpath = request.getContextPath();
		String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ contextpath + "/";

		try {
		String filePath = "temporarys/";//保存临时图片文件
		String realPath = realDir+"\\"+filePath;
		//判断路径是否存在，不存在则创建
		File dir = new File(realPath);
		if(!dir.isDirectory())
		    dir.mkdir();

		if(ServletFileUpload.isMultipartContent(request)){

		    DiskFileItemFactory dff = new DiskFileItemFactory();
		    dff.setRepository(dir);
		    dff.setSizeThreshold(1024000);
		    ServletFileUpload sfu = new ServletFileUpload(dff);
		    FileItemIterator fii = null;
		    fii = sfu.getItemIterator(request);
		    String title = "";   //图片标题
		    String url = "";    //图片地址
		    String fileName = "";
			String state="SUCCESS";
			String realFileName="";
			String picSuffix = "";
		    while(fii.hasNext()){
		        FileItemStream fis = fii.next();
		        try{
		            if(!fis.isFormField() && fis.getName().length()>0){
		                fileName = fis.getName();
						Pattern reg=Pattern.compile("[.]jpg|png|jpeg|gif$");
						Matcher matcher=reg.matcher(fileName);
						if(!matcher.find()) {
							state = "文件类型不允许！";
							break;
						}
						picSuffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()); 
						realFileName =  picShape + new Date().getTime()+ "." + picSuffix;
		                url = realPath + realFileName;
		                BufferedInputStream in = new BufferedInputStream(fis.openStream());//获得文件输入流
		                FileOutputStream a = new FileOutputStream(new File(url));
		                BufferedOutputStream output = new BufferedOutputStream(a);
		                Streams.copy(in, output, true);//开始把文件写到你指定的上传文件夹
		            }else{
		                String fname = fis.getFieldName();

		                if(fname.indexOf("pictitle")!=-1){
		                    BufferedInputStream in = new BufferedInputStream(fis.openStream());
		                    byte c [] = new byte[10];
		                    int n = 0;
		                    while((n=in.read(c))!=-1){
		                        title = new String(c,0,n);
		                        break;
		                    }
		                }
		            }

		        }catch(Exception e){
		            e.printStackTrace();
		        }
		    }
		    File file = new File(url);
		    
		    if(file.exists()){
		    	//获得图片大小
		    	java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(new File(url));
		    	int width = img.getWidth();
		    	int height = img.getHeight();
		    	this.doCompress(url,picSuffix);
		    }
		    response.getWriter().print(realFileName);//返回图片名称
		}
		}catch(Exception ee) {
			ee.printStackTrace();
		}
		
	}
	public void init() throws ServletException {
		// Put your code here
	}

	
	 /**
	 * 压缩图片方法
	 * 
	 * @param oldFile 将要压缩的图片
	 * @param width 压缩宽
	 * @param height 压缩高
	 * @param quality 压缩清晰度 <b>建议为1.0</b>
	 * @param smallIcon 压缩图片后,添加的扩展名（在图片后缀名前添加）
	 * @param percentage 是否等比压缩 若true宽高比率将将自动调整
	 * @author zhengsunlei
	 * @return 如果处理正确返回压缩后的文件名 null则参数可能有误
	 */
	private String doCompress(String oldFile, String smallIcon) {
		if (oldFile != null) {
			Image srcFile = null;
			String newImage = null;
			File file = new File(oldFile);
			if(!file.exists()) return null ;
			try {
				/* 读取图片信息 */
				srcFile = ImageIO.read(file);
				int new_w = 500;
				int new_h = 400;
				// 为等比缩放计算输出的图片宽度及高度
				double rate1 = ((double) srcFile.getWidth(null))
							/ (double) 500 + 0.1;
				double rate2 = ((double) srcFile.getHeight(null))
							/ (double) 400 + 0.1;
				double rate = rate1 > rate2 ? rate1 : rate2;
				//new_w = (int) (((double) srcFile.getWidth(null)) / rate);
				//new_h = (int) (((double) srcFile.getHeight(null)) / rate);
				
				/* 宽高设定 */
				BufferedImage tag = new BufferedImage(new_w, new_h,
						BufferedImage.TYPE_INT_RGB);
				tag.getGraphics().drawImage(srcFile, 0, 0, new_w, new_h, null);

				/* 压缩后的文件名 */
				String filePrex = oldFile.substring(0, oldFile.lastIndexOf('.'));
				newImage = filePrex
						+ oldFile.substring(filePrex.length());

				/* 压缩之后临时存放位置 */
				FileOutputStream out = new FileOutputStream(newImage);

				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);

				/* 压缩质量 */
				jep.setQuality(1, true);
				encoder.encode(tag, jep);

				out.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				srcFile.flush();
			}
			return newImage;
		} else {
			return null;
		}
	}
    
}

