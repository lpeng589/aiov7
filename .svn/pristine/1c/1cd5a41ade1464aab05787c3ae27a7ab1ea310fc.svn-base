package com.koron.hr.workRule;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.menyi.aio.bean.BrushCardAnnalBean;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
public class ParseExcel {
	
	public static List getExcelCells(InputStream stream){
		List columns = null;
		try{
			Workbook workbook = Workbook.getWorkbook(stream);
			Sheet firstSheet = workbook.getSheet(0);
			Cell[] cells = firstSheet.getRow(0);
			columns = new ArrayList();
			for (int i = 1;i <= cells.length;i++) {
				columns.add(i);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}
	
	public static List<BrushCardAnnalBean> getExcelContent(InputStream stream,int employeeNo,int cardAnnal){
		List<BrushCardAnnalBean> contents = null;
		try {
			
			Workbook workbook = Workbook.getWorkbook(stream);
			Sheet firstSheet = workbook.getSheet(0);
			contents = new ArrayList<BrushCardAnnalBean>();
			for (int i = 0;i < firstSheet.getRows();i++) {
				Cell[] cells = firstSheet.getRow(i);
				if(cells.length < 1 || (employeeNo-1) >= cells.length || (cardAnnal-1) >= cells.length){
					break;
				}
				BrushCardAnnalBean annalBean = new BrushCardAnnalBean();
				annalBean.setEmployeeNo(cells[employeeNo-1].getContents());
				annalBean.setCardAnnalTime(cells[cardAnnal-1].getContents());
				contents.add(annalBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return contents;
	}

}
