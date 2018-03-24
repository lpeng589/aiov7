package com.koron.oa.converter;

import com.koron.oa.converter.pdf.OpenOfficePDFConverter;
import com.koron.oa.converter.pdf.PDFConverter;
import com.koron.oa.converter.swf.SWFConverter;
import com.koron.oa.converter.swf.SWFToolsSWFConverter;

/**
 * 
 * <p>Title:把doc转换成pdf</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 5, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class DocConverter {

	private PDFConverter pdfConverter;
	private SWFConverter swfConverter;
	
	
	public DocConverter(PDFConverter pdfConverter, SWFConverter swfConverter) {
		super();
		this.pdfConverter = pdfConverter;
		this.swfConverter = swfConverter;
	}


	public  void convert(String inputFile,String swfFile){
		this.pdfConverter.convert2PDF(inputFile);
		String pdfFile = FileUtils.getFilePrefix(inputFile)+".pdf";
		this.swfConverter.convert2SWF(pdfFile, swfFile);
	}
	
	public void convert(String inputFile){
		this.pdfConverter.convert2PDF(inputFile);
		String pdfFile = FileUtils.getFilePrefix(inputFile)+".pdf";
		this.swfConverter.convert2SWF(pdfFile);
		
	}
	
	public void toConvert(String docFile,String swfFile){
		PDFConverter pdfConverter = new OpenOfficePDFConverter();
		//PDFConverter pdfConverter = new JacobPDFConverter();
		SWFConverter swfConverter = new SWFToolsSWFConverter();
		DocConverter converter = new DocConverter(pdfConverter,swfConverter);
		converter.convert(docFile, swfFile);
	}
	
}
