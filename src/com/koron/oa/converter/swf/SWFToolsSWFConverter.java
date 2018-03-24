package com.koron.oa.converter.swf;

import java.io.File;
import java.io.IOException;

import com.koron.oa.converter.FileUtils;
import com.menyi.web.util.BaseEnv;

/**
 * 
 * <p>Title:��pdfת����swf�ļ�</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 5, 2013
 * @Copyright: �����п���������޹�˾
 * @Author ǮС��
 */
public class SWFToolsSWFConverter implements SWFConverter{

	private static String PDF2SWF_PATH = BaseEnv.FILESERVERPATH+"\\..\\SWFTools\\pdf2swf.exe";
	
	public void convert2SWF(String inputFile, String swfFile) {
		File pdfFile = new File(inputFile);
		File outFile = new File(swfFile);
		if(!inputFile.endsWith(".pdf")){
			System.out.println("�ļ���ʽ��PDF��");
			return ;
		}
		if(!pdfFile.exists()){
			System.out.println("PDF�ļ������ڣ�");
			return ;
		}
		if(outFile.exists()){
			//System.out.println("SWF�ļ��Ѵ��ڣ�");
			return ;
		}
		String command = PDF2SWF_PATH +" \""+inputFile+"\" -o "+swfFile+" -T 9 -f";
		try {
			//System.out.println("��ʼת���ĵ�: "+inputFile);
			Runtime.getRuntime().exec(command);
			//System.out.println("�ɹ�ת��ΪSWF�ļ���");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ת���ĵ�Ϊswf�ļ�ʧ�ܣ�");
		}
		
	}

	public void convert2SWF(String inputFile) {
		String swfFile = FileUtils.getFilePrefix(inputFile)+".swf";
		convert2SWF(inputFile,swfFile);
	}

}