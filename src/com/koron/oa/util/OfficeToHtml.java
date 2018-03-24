package com.koron.oa.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 
 * <p>Title:把offfice转换成html</p> 
 * <p>Description: </p>
 *
 * @Date:Jun 27, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class OfficeToHtml {

	/**
	 * word转换成html
	 * @param pptPath
	 * @param htmlPath
	 */
	public static void wordToHtml(String paths, String htmlPath) {
		ActiveXComponent app = new ActiveXComponent("Word.Application");// 启动word
		try {
			app.setProperty("Visible", new Variant(false));
			Dispatch docs = app.getProperty("Documents").toDispatch();
			Dispatch doc = Dispatch.invoke(docs,"Open",Dispatch.Method,new Object[] { paths, new Variant(false),new Variant(true) }, new int[1]).toDispatch();
			// 打开word文件
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {htmlPath, new Variant(8) }, new int[1]);
			// 作为html格式保存到临时文件
			Variant f = new Variant(false);
			Dispatch.call(doc, "Close", f);
			if(paths.endsWith(".doc")){
				replaceHtml(htmlPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			app.invoke("Quit", new Variant[] {});
		}
	} 
	
	public static void replaceHtml(String htmlPath){
		try {
			File file = new File(htmlPath);
			BufferedReader br = new BufferedReader(new FileReader(htmlPath));
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getParent()+"temp.html"));
			String readLine = br.readLine();
			while(readLine!=null){
				if(readLine.contains("x-cp20936")){
					readLine = readLine.replaceAll("x-cp20936", "gb2312");
					bw.write(readLine);
				}else{
					bw.write(readLine);
					bw.newLine();
				}
				readLine = br.readLine();
			}
			br.close();
			bw.close();
			file.delete();
			File newFile = new File(file.getParent()+"temp.html");
			newFile.renameTo(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * excel转换成html
	 * @param xlsfile
	 * @param htmlfile
	 */
	public static void excelToHtml(String xlsfile, String htmlfile) {
		ActiveXComponent app = new ActiveXComponent("Excel.Application"); // 启动word
		try {
			app.setProperty("Visible", new Variant(false));
			Dispatch excels = app.getProperty("Workbooks").toDispatch();
			Dispatch excel = Dispatch.invoke(excels,"Open",Dispatch.Method,
					new Object[] { xlsfile, new Variant(false),new Variant(true) }, new int[1]).toDispatch();
			Dispatch.invoke(excel, "SaveAs", Dispatch.Method, new Object[] {htmlfile, new Variant(44) }, new int[1]);
			Variant f = new Variant(false);
			Dispatch.call(excel, "Close", f);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			app.invoke("Quit", new Variant[] {});
		}
	}  
	
	public static String toHTML(String file,String temp,String ext){
		String strFile = temp+"/" + file;
		deleteTemp(temp);
		String fileName = String.valueOf(System.currentTimeMillis());
		String tempPath = temp + "/" + fileName+".html";
		try{
			if(".doc,.docx".contains(ext)){
				wordToHtml(strFile, tempPath);
			}else if(".xls,.xlsx".contains(ext)){
				excelToHtml(strFile, tempPath);
			}else{
				return "";
			}
		}catch (Exception e) {
			return "";
		}
		return fileName;
	}
	
	/**
	 * 删除临时目录
	 */
	public static void deleteTemp(String temp){
		File folder = new File(temp);
		if(folder.listFiles().length>0){
			for(File cFile : folder.listFiles()){
				if(cFile.isDirectory()){
					for(File dFile : cFile.listFiles()){
						dFile.delete();
					}
					cFile.delete();
				}else{
					cFile.delete();
				}
			}
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String paths = new String("D:\\cccc.doc");
		String htmlPath = new String ("D:\\test4.html"); 
		wordToHtml(paths, htmlPath);
	}

}
