package com.menyi.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.math.RandomUtils;

import com.menyi.aio.bean.FusionBean;

/**
 * 图形报表
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Mar 23, 2011
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class FusionCharts {

	public static String getChart(FusionBean fusion,ArrayList<ReportField> queryFields){
		String str = "" ;
		if("Bar2D".equals(fusion.getType()) 
				|| "Column2D".equals(fusion.getType()) 
				|| "Column3D".equals(fusion.getType()) 
				|| "Doughnut2D".equals(fusion.getType())){
			str = getBarChart(fusion.getCaption(), fusion.getXAxis(), fusion.getYAxis(), fusion.getValues()) ;
		}else if("Line".equals(fusion.getType())){
			str = getLine2DChart(fusion.getCaption(), fusion.getXAxis(), fusion.getYAxis(), fusion.getValues()) ;
		}else if("Pie3D".equals(fusion.getType()) 
				|| "Pie2D".equals(fusion.getType())){
			str = getPie2DChart(fusion.getCaption(),  fusion.getValues()) ;
		}else if("MSColumn2D".equals(fusion.getType()) 
				|| "MSColumn3D".equals(fusion.getType()) 
				|| "MSBar2D".equals(fusion.getType()) 
				|| "StackedColumn2D".equals(fusion.getType())
				|| "StackedColumn3D".equals(fusion.getType())
				|| "StackedBar2D".equals(fusion.getType())){
			str = getMultiBarChart(fusion.getCaption(), fusion.getXAxis(), fusion.getYAxis(),fusion.getCategorys(), fusion.getValues(),queryFields) ;
		}else if("StackedArea2D".equals(fusion.getType())){
			
		}else if("Area2D".equals(fusion.getType())){
			str = getArea2DChart(fusion.getCaption(), fusion.getXAxis(), fusion.getYAxis(), fusion.getValues()) ;
		}else if("MSArea2D".equals(fusion.getType()) 
				|| "MSLine".equals(fusion.getType())){
			str = getMultiArea2DChart(fusion.getCaption(), fusion.getXAxis(), fusion.getYAxis(),fusion.getCategorys(), fusion.getValues(),queryFields) ;
		}else if("Funnel".equals(fusion.getType())){
			str = getFunnelChart(fusion.getCaption(), fusion.getValues()) ;
		}else if("MSColumn3DLineDY".equals(fusion.getType())
				|| "MSColumn2DLineDY".equals(fusion.getType())){
			str = getMultiCol3DLineDYChart(fusion.getCaption(), fusion.getXAxis(), fusion.getYAxis(),fusion.getCategorys(), fusion.getValues(),queryFields) ;
		}
		return str ;
	}
	
	/**
	 * 2D3D 柱形图
	 * @param caption 顶部标题
	 * @param xAxisName x轴标题
	 * @param yAxisName y轴标题
	 * @return
	 */
	public static String getBarChart(String caption,String xAxisName,String yAxisName,HashMap values){
		StringBuilder strChart = new StringBuilder() ;
		strChart.append("<graph caption='"+caption+"' baseFontsize='13' xAxisName='"+xAxisName+"' yAxisName='"+yAxisName+"' decimalPrecision='0' formatNumberScale='0'>") ;
		Iterator iterator = values.keySet().iterator() ;
		int i = 0 ;
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next() ;
			strChart.append("<set name='"+strKey+"' value='"+values.get(strKey)+"' color='"+getColor(i++)+"' />") ;
		}
		strChart.append("</graph>") ;
		return strChart.toString() ;
	}
	
	/**
	 * 漏斗图
	 * @param caption 顶部标题
	 * @param xAxisName x轴标题
	 * @param yAxisName y轴标题
	 * @return
	 */
	public static String getFunnelChart(String caption,HashMap values){
		StringBuilder strChart = new StringBuilder() ;
		strChart.append("<graph caption='"+caption+"' baseFontsize='13' isSliced='1' slicingDistance='4' decimalPrecision='0'>") ;
		Iterator iterator = values.keySet().iterator() ;
		int i = 0 ;
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next() ;
			strChart.append("<set name='"+strKey+"' value='"+values.get(strKey)+"' color='"+getColor(i++)+"' alpha='85'/>") ;
		}
		strChart.append("</graph>") ;
		return strChart.toString() ;
	}
	
	/**
	 * 2D3D 多列柱形图
	 * @param caption 顶部标题
	 * @param xAxisName x轴标题
	 * @param yAxisName y轴标题
	 * @return
	 */
	public static String getMultiBarChart(String caption,String xAxisName,String yAxisName,
			String[] categories ,HashMap values,ArrayList<ReportField> queryFields){
		StringBuilder strChart = new StringBuilder() ;
		strChart.append("<graph xaxisname='"+xAxisName+"' baseFontsize='13' yaxisname='"+yAxisName+"' hovercapbg='DEDEBE' hovercapborder='889E6D' rotateNames='0' " +
				"yAxisMaxValue='' numdivlines='9' divLineColor='CCCCCC' divLineAlpha='80' decimalPrecision='0' showAlternateHGridColor='1' " +
				"AlternateHGridAlpha='30' AlternateHGridColor='CCCCCC' caption='"+caption+"' subcaption='' >") ;
		
		strChart.append("<categories font='Arial' fontSize='11' fontColor='000000'>") ;
		for(String strName : categories){
			strChart.append("<category name='"+strName+"' />") ;
		}
		strChart.append("</categories>") ;
		
		Iterator iterator = values.keySet().iterator() ;
		int i = 0 ;
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next() ;
			strChart.append("<dataset seriesname='"+getFieldDisplay(strKey, queryFields)+"' color='"+getColor(i++)+"'>") ;
			ArrayList<String> arrStr = (ArrayList<String>) values.get(strKey) ;
			for(String strValue : arrStr){
				strChart.append("<set value='"+strValue+"'/>") ;
			}
			strChart.append("</dataset>") ;
		}
		strChart.append("</graph>") ;
		return strChart.toString() ;
	}
	
	/**
	 * 2D3D饼图 环图
	 * @param caption 顶部标题
	 * @param values  
	 * @return
	 */
	public static String getPie2DChart(String caption,HashMap values){
		StringBuilder strChart = new StringBuilder() ;
		strChart.append("<graph caption='"+caption+"' baseFontsize='13' showNames='1'  decimalPrecision='0'  >") ;
		Iterator iterator = values.keySet().iterator() ;
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next() ;
			strChart.append("<set name='"+strKey+"' value='"+values.get(strKey)+"'/>") ;
		}
		strChart.append("</graph>") ;
		return strChart.toString() ;
	}
	
	/**
	 * 曲线2D图
	 * @param caption 顶部标题
	 * @param xAxisName x轴标题
	 * @param yAxisName y轴标题
	 * @param values
	 * @return
	 */
	public static String getLine2DChart(String caption,String xAxisName,String yAxisName,HashMap values){
		StringBuilder strChart = new StringBuilder() ;
		strChart.append("<graph caption='"+caption+"' baseFontsize='13' xAxisName='"+xAxisName+"' yAxisName='"+yAxisName+"' decimalPrecision='0' " +
				"formatNumberScale='0' numberPrefix='' showNames='1' showValues='0'  showAlternateHGridColor='1' AlternateHGridColor='ff5904' " +
				"divLineColor='ff5904' divLineAlpha='20' alternateHGridAlpha='5' >") ;
		Iterator iterator = values.keySet().iterator() ;
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next() ;
			strChart.append("<set name='"+strKey+"' value='"+values.get(strKey)+"' />") ;
		}
		strChart.append("</graph>") ;
		return strChart.toString() ;
	}
	
	/**
	 * 多条 曲线2D图
	 * @param caption 顶部标题
	 * @param xAxisName x轴标题
	 * @param yAxisName y轴标题
	 * @param values
	 * @return
	 */
	public static String getMultiLine2DChart(String caption,String xAxisName,String yAxisName,
			String[] categories,HashMap values,ArrayList<ReportField> queryFields){
		StringBuilder strChart = new StringBuilder() ;
		strChart.append("<graph caption='"+caption+"' baseFontsize='13' subcaption='' xAxisName='"+xAxisName+"' yAxisName='"+yAxisName+"' hovercapbg='FFECAA' " +
				"hovercapborder='F47E00' formatNumberScale='0' decimalPrecision='0' showvalues='0' numdivlines='3' numVdivlines='0' " +
				"yaxisminvalue='' yaxismaxvalue=''  rotateNames='1'>") ;
		strChart.append("<categories>") ;
		for(String strName : categories){
			strChart.append("<category name='"+strName+"' />") ;
		}
		strChart.append("</categories>") ;
		Iterator iterator = values.keySet().iterator() ;
		int i = 0 ;
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next() ;
			strChart.append("<dataset seriesName='"+getFieldDisplay(strKey, queryFields)+"' color='"+getColor(i++)+"' anchorBorderColor='"+getColor(i++)+"' anchorBgColor='"+getColor(i++)+"'>") ;
			ArrayList<String> arrStr = (ArrayList<String>) values.get(strKey) ;
			for(String strValue : arrStr){
				strChart.append("<set value='"+strValue+"'/>") ;
			}
			strChart.append("</dataset>") ;
		}
		strChart.append("</graph>") ;
		return strChart.toString() ;
	}
	
	/**
	 * 区域2D图
	 * @param caption 顶部标题
	 * @param xAxisName x轴标题
	 * @param yAxisName y轴标题
	 * @param values
	 * @return
	 */
	public static String getArea2DChart(String caption,String xAxisName,String yAxisName,HashMap values){
		StringBuilder strChart = new StringBuilder() ;
		String maxValue = "" ; 
		String minValue = "" ;
		strChart.append("<graph caption='"+caption+"' baseFontsize='13' xAxisName='"+xAxisName+"' yAxisMinValue='"+minValue+"'  yAxisMaxValue='"+maxValue+"' " +
				"yAxisName='"+yAxisName+"' decimalPrecision='0' " +
				"formatNumberScale='0' numberPrefix='' >") ;
		Iterator iterator = values.keySet().iterator() ;
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next() ;
			strChart.append("<set name='"+strKey+"' value='"+values.get(strKey)+"' />") ;
		}
		strChart.append("</graph>") ;
		return strChart.toString() ;
	}
	
	/**
	 * 多列 区域2D图
	 * @param caption 顶部标题
	 * @param xAxisName x轴标题
	 * @param yAxisName y轴标题
	 * @param values
	 * @return
	 */
	public static String getMultiArea2DChart(String caption,String xAxisName,String yAxisName,
			String[] categories,HashMap values,ArrayList<ReportField> queryFields){
		StringBuilder strChart = new StringBuilder() ;
		String maxValue = "" ; 
		String minValue = "" ;
		strChart.append("<graph caption='"+caption+"' baseFontsize='13' subcaption='' xAxisName='"+xAxisName+"' yAxisName='"+yAxisName+"' " +
				"divlinecolor='F47E00' numdivlines='4' showAreaBorder='1' " +
				"areaBorderColor='000000' numberPrefix='' showNames='1' numVDivLines='29' vDivLineAlpha='30' " +
				"formatNumberScale='1' rotateNames='1'  decimalPrecision='0'>") ;
		strChart.append("<categories>") ;
		for(String strName : categories){
			strChart.append("<category name='"+strName+"' />") ;
		}
		strChart.append("</categories>") ;
		Iterator iterator = values.keySet().iterator() ;
		int i = 0 ;
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next() ;
			String color = getColor(i++) ;
			strChart.append("<dataset seriesname='"+getFieldDisplay(strKey, queryFields)+"' color='"+color+"' showValues='0' areaAlpha='50' showAreaBorder='1'" +
							" areaBorderThickness='2' areaBorderColor='"+color+"'>") ;
			ArrayList<String> arrStr = (ArrayList<String>) values.get(strKey) ;
			for(String strValue : arrStr){
				strChart.append("<set value='"+strValue+"'/>") ;
			}
			strChart.append("</dataset>") ;
		}
		strChart.append("</graph>") ;
		return strChart.toString() ;
	}
	
	/**
	 * 2D3D 联合图
	 * @param caption 顶部标题
	 * @param xAxisName x轴标题
	 * @param yAxisName y轴标题
	 * @param values
	 * @return
	 */
	public static String getMultiCol3DLineDYChart(String caption,String xAxisName,String yAxisName,
			String[] categories,HashMap values,ArrayList<ReportField> queryFields){
		StringBuilder strChart = new StringBuilder() ;
		strChart.append("<graph caption='"+caption+"' baseFontsize='13' PYAxisName='"+xAxisName+"' SYAxisName='"+yAxisName+"' " +
				"numberPrefix='' showvalues='0'  numDivLines='4' formatNumberScale='0' decimalPrecision='0' " +
				"anchorSides='10' anchorRadius='3' anchorBorderColor='009900'>") ;
		strChart.append("<categories>") ;
		for(String strName : categories){
			strChart.append("<category name='"+strName+"' />") ;
		}
		strChart.append("</categories>") ;
		Iterator iterator = values.keySet().iterator() ;
		int i = 0 ;
		while (iterator.hasNext()) {
			String strKey = (String) iterator.next() ;
			strChart.append("<dataset seriesName='"+getFieldDisplay(strKey, queryFields)+"' color='"+getColor(i++)+"' showValues='0' ") ;
			if(!iterator.hasNext()){
				strChart.append(" parentYAxis='S'>") ;
			}else{
				strChart.append(" >") ;
			}
			ArrayList<String> arrStr = (ArrayList<String>) values.get(strKey) ;
			for(String strValue : arrStr){
				strChart.append("<set value='"+strValue+"'/>") ;
			}
			strChart.append("</dataset>") ;
		}
		strChart.append("</graph>") ;
		return strChart.toString() ;
	}
	
	/**
	 * 颜色
	 * @param number
	 * @return
	 */
	public static String getColor(int number){
		String[] arrColor = {"AFD8F8","F6BD0F","8BBA00","FF8E46","008E8E","D64646","8E468E","588526","B3AA00","008ED6","9D080D","A186BE"} ;
		if(number>=arrColor.length){
			number = RandomUtils.nextInt(12) ;
		}
		return arrColor[number] ;
	}
	
	/**
	 * 获取字段的显示名称
	 * @param fieldName
	 * @param queryFields
	 * @return
	 */
	public static String getFieldDisplay(String fieldName,ArrayList<ReportField> queryFields){
		if(fieldName==null || "".equals(fieldName.trim())) return "" ;
		if(queryFields==null || queryFields.size()==0) return fieldName ;
		String dispayName = "" ;
		for(ReportField field : queryFields){
			if(fieldName.equals(field.getAsFieldName())){
				dispayName = field.getDisplay() ;
				break ;
			}
		}
		return dispayName ;
	}
}
