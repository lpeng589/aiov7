package com.koron.oa.converter.swf;

import java.io.File;
import java.io.IOException;

import com.koron.oa.converter.FileUtils;
import com.menyi.web.util.BaseEnv;

/**
 * 
 * <p>Title:把pdf转换成swf文件</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 5, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class SWFToolsSWFConverter implements SWFConverter{

	private static String PDF2SWF_PATH = BaseEnv.FILESERVERPATH+"\\..\\SWFTools\\pdf2swf.exe";
	
	public void convert2SWF(String inputFile, String swfFile) {
		File pdfFile = new File(inputFile);
		File outFile = new File(swfFile);
		if(!inputFile.endsWith(".pdf")){
			System.out.println("文件格式非PDF！");
			return ;
		}
		if(!pdfFile.exists()){
			System.out.println("PDF文件不存在！");
			return ;
		}
		if(outFile.exists()){
			//System.out.println("SWF文件已存在！");
			return ;
		}
		String command = PDF2SWF_PATH +" \""+inputFile+"\" -o "+swfFile+" -T 9 -f";
		try {
			//System.out.println("开始转换文档: "+inputFile);
			Runtime.getRuntime().exec(command);
			//System.out.println("成功转换为SWF文件！");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("转换文档为swf文件失败！");
		}
		
	}

	public void convert2SWF(String inputFile) {
		String swfFile = FileUtils.getFilePrefix(inputFile)+".swf";
		convert2SWF(inputFile,swfFile);
	}

}