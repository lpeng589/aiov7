package com.menyi.web.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class PDF2PNG2 {
	
   public static String readTotalPage(String fileName) throws Exception{
    	Document document = new Document();
		try {
			document.setFile(fileName);  
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		float scale = 2f;
		float rotation = 0f;
		return document.getNumberOfPages()+"";
    }
	
    public static void readPDF(String fileName,int pageNo,HttpServletResponse resp) throws Exception{
    	Document document = new Document();
		try {
			document.setFile(fileName);  
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		float scale = 2f;
		float rotation = 0f;

		if(pageNo>=document.getNumberOfPages()){
			pageNo = document.getNumberOfPages() -1;
		}

		BufferedImage tag = (BufferedImage)
		document.getPageImage(pageNo, GraphicsRenderingHints.PRINT,
				Page.BOUNDARY_CROPBOX, rotation, scale);
		/*
		 * JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		 * encoder.encode(tag); // JPEG编码 out.close();
		 */

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(resp.getOutputStream());
		JPEGEncodeParam param2 = encoder.getDefaultJPEGEncodeParam(tag);
		param2.setQuality(0.9f, false);// 1f是提高生成的图片质量
		encoder.setJPEGEncodeParam(param2);
		encoder.encode(tag); // JPEG编码
		document.dispose();
    }
	
	public static void main(String[] args) throws ImageFormatException, IOException {
		final String PATH="f:\\2.pdf";
		String OUTPUTPATH="f:\\a\\t";

		String filePath = PATH;

		Document document = new Document();

		try {

			document.setFile(filePath);

		} catch (Exception ex) {

		}

		// save page caputres to file.

		float scale = 2f;

		float rotation = 0f;

		// Paint each pages content to an image and write the image to file

		for (int i = 0; i < document.getNumberOfPages(); i++) {

			BufferedImage tag = (BufferedImage)
			document.getPageImage(i, GraphicsRenderingHints.PRINT,
					Page.BOUNDARY_CROPBOX, rotation, scale);

			FileOutputStream out = new FileOutputStream(OUTPUTPATH 
					+ i + ".jpg");
			System.out.println("成功保存图片到:" + OUTPUTPATH  + i + ".jpg");

			/*
			 * JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			 * encoder.encode(tag); // JPEG编码 out.close();
			 */

			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param2 = encoder.getDefaultJPEGEncodeParam(tag);
			param2.setQuality(0.9f, false);// 1f是提高生成的图片质量
			encoder.setJPEGEncodeParam(param2);
			encoder.encode(tag); // JPEG编码
			out.close();

		}

		// clean up resources

		document.dispose();

	}
}
