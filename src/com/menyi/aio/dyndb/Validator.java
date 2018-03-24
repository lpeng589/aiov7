package com.menyi.aio.dyndb;

import com.dbfactory.Result;

import java.util.List;

import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

import org.apache.struts.util.MessageResources;

import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;

import com.menyi.web.util.GlobalsTool;

/**
 * 
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: 周新宇
 * </p>
 * 
 * @author 周新宇
 * @version 1.0
 */
public class Validator {
	public static Result validator(final boolean isDraft,final String tableName, final Map allTables, final HashMap values, final MessageResources resources, final Locale locale) {
		Result rs = new Result();
		String minValue = "0";
		String ret = null;
		// 校验主表信息
		DBTableInfoBean tableInfoBean = (DBTableInfoBean) allTables.get(tableName);
		List<DBFieldInfoBean> fieldList = tableInfoBean.getFieldInfos();
		for (DBFieldInfoBean $row : fieldList) {
			if ($row.getInputType() != 100 && !$row.getFieldName().equals("id") && !$row.getFieldName().equals("createBy") && !$row.getFieldName().equals("createTime")
					&& !$row.getFieldName().equals("lastUpdateBy") && !$row.getFieldName().equals("lastUpdateTime")&& !"BillNo".equals($row.getFieldIdentityStr())) {
				if ($row.getInputType() == 0 || $row.getInputType() == 7 || $row.getInputType() == 4 || $row.getInputType() == 8) { 
					if ($row.getInputType() == 4) {
						ret = putValidateItem(isDraft,locale,values,resources,fieldList,$row.getFieldName(), $row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), false);
					} else if ($row.getFieldType() == 16) {
						ret =putValidateItem(isDraft,locale,values,resources,fieldList,$row.getFieldName(), $row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), false);
					} else if ($row.getFieldType() == 13) {
						//ret =putValidateItem(isDraft,locale,values,resources,fieldList,"uploadpic", $row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), false);
					} else if ($row.getFieldType() == 14) {
						//ret =putValidateItem(isDraft,locale,values,resources,fieldList,"uploadaffix", $row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), false);
					} else {
						ret =putValidateItem(isDraft,locale,values,resources,fieldList,$row.getFieldName(), $row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), true);
					}
				} else if ($row.getInputType() == 2 || $row.getInputType() == 1 || $row.getInputType() == 16) {
					ret =putValidateItem(isDraft,locale,values,resources,fieldList,$row.getFieldName(), $row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), true);
				} else if ($row.getInputType() == 5 || $row.getInputType() == 10) {
					ret =putValidateItem(isDraft,locale,values,resources,fieldList,$row.getFieldName(), $row.getDisplay().get(locale.toString()), "checkBox", $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), true);
				}
			}
			if(ret != null && ret.length() > 0 ){
				rs.retCode = ErrorCanst.RET_FIELD_VALIDATOR_ERROR;
				rs.retVal = ret;
				BaseEnv.log.debug("Validator.validator 字段效验"+$row.getTableBean().getTableName()+"."+ $row.getFieldName()+":"+ret);
				return rs;
			}
		}
		ArrayList<DBTableInfoBean> childTableList = DDLOperation.getChildTables(tableName, allTables);
		for(DBTableInfoBean ctableInfoBean :childTableList){
			if(GlobalsTool.getSysSetting(ctableInfoBean.getTableSysType()).equals("true") && ctableInfoBean.getIsUsed()==1 && ctableInfoBean.getIsView()==0){
				ArrayList<HashMap> cvalueList = (ArrayList<HashMap>)values.get("TABLENAME_"+ctableInfoBean.getTableName());
				if((cvalueList==null || cvalueList.size()==0 )&&ctableInfoBean.getIsNull()==1 && !isDraft){
					ret =(ctableInfoBean.getDisplay().get(locale.toString())+getResouce(resources,locale,"common.noData.error"));
					rs.retCode = ErrorCanst.RET_FIELD_VALIDATOR_ERROR;
					rs.retVal = ret;
					BaseEnv.log.debug("Validator.validator 字段效验"+ctableInfoBean.getTableName()+":"+ret);
					return rs;
				}
				if(cvalueList==null){ 
					continue;
				} 				
				for(HashMap cvalues:cvalueList){
					for (DBFieldInfoBean $row : ctableInfoBean.getFieldInfos()) {
						if ($row.getInputType() != 100 && !$row.getFieldName().equals("id") && !$row.getFieldName().equals("createBy") && !$row.getFieldName().equals("createTime")
								&& !$row.getFieldName().equals("lastUpdateBy") && !$row.getFieldName().equals("lastUpdateTime")) {
							if ($row.getInputType() == 0 || $row.getInputType() == 7 || $row.getInputType() == 4 || $row.getInputType() == 8) {
								if ($row.getInputType() == 4) {
									ret = putValidateItem(isDraft,locale,cvalues,resources,ctableInfoBean.getFieldInfos(),$row.getFieldName(),ctableInfoBean.getDisplay().get(locale.toString())+" "+ $row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), false);
								} else if ($row.getFieldType() == 16) {
									ret =putValidateItem(isDraft,locale,cvalues,resources,ctableInfoBean.getFieldInfos(),$row.getFieldName(), ctableInfoBean.getDisplay().get(locale.toString())+" "+$row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), false);
								} else if ($row.getFieldType() == 13) {
									//ret =putValidateItem(isDraft,locale,cvalues,resources,ctableInfoBean.getFieldInfos(),"uploadpic", ctableInfoBean.getDisplay().get(locale.toString())+" "+$row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), false);
								} else if ($row.getFieldType() == 14) {
									//ret =putValidateItem(isDraft,locale,cvalues,resources,ctableInfoBean.getFieldInfos(),"uploadaffix", ctableInfoBean.getDisplay().get(locale.toString())+" "+$row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), false);
								} else {
									ret =putValidateItem(isDraft,locale,cvalues,resources,ctableInfoBean.getFieldInfos(),$row.getFieldName(), ctableInfoBean.getDisplay().get(locale.toString())+" "+$row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), true);
								}
							} else if ($row.getInputType() == 2 || $row.getInputType() == 1 || $row.getInputType() == 16) {
								ret =putValidateItem(isDraft,locale,cvalues,resources,ctableInfoBean.getFieldInfos(),$row.getFieldName(), ctableInfoBean.getDisplay().get(locale.toString())+" "+$row.getDisplay().get(locale.toString()), $row.getStringType(), $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), true);
							} else if ($row.getInputType() == 5 || $row.getInputType() == 10) {
								ret =putValidateItem(isDraft,locale,cvalues,resources,ctableInfoBean.getFieldInfos(),$row.getFieldName(), ctableInfoBean.getDisplay().get(locale.toString())+" "+$row.getDisplay().get(locale.toString()), "checkBox", $row.getFieldSysType(), $row.getSringNull(), minValue, $row.getStringLength(), true);
							}
						}
						if(ret != null && ret.length() > 0 ){
							rs.retCode = ErrorCanst.RET_FIELD_VALIDATOR_ERROR;
							rs.retVal = ret;
							BaseEnv.log.debug("Validator.validator 字段效验"+$row.getTableBean().getTableName()+"."+ $row.getFieldName()+":"+ret);
							return rs;
						}
					}					
					
				}
				
			}
		}			
		
		return rs;
	}

	public static String putValidateItem(boolean isDraft,Locale locale,final HashMap values,final MessageResources resources,List<DBFieldInfoBean> fieldList,String name, String title, String type, String fieldSysType, String nullablestr, String minLimitStr, String maxLimitStr, boolean isSC) {
		
		
		int minLimit = minLimitStr==null ||minLimitStr.length() ==0?0:Integer.parseInt(minLimitStr); 
		int maxLimit = maxLimitStr==null ||maxLimitStr.length() ==0|| maxLimitStr.equals("maxValue")?0:Integer.parseInt(maxLimitStr); 
		boolean nullable = nullablestr.equals("true")?true:false;
		
		String items = values.get( name)==null?"":values.get( name).toString();
		
		if(! nullable && ( items==null || items.length()==0) && !isDraft){
			return ( title+getResouce(resources,locale,"common.validate.error.null"));
		}
		if( nullable && ( items==null || items.length()==0)){ //可为空
			return null;
		}
		if(! nullable && ( items==null || items.length()==0) && isDraft){ //非空字段，草稿可保存
			return null;
		}
		
//		if( isSC){
//			if(!containSC(items)){
//				return ( title+getResouce(resources,locale,"con.validate.contain.sc")) ;
//			}
//		}
		if( type.equals("int")){
			if(!isInt(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.integer"));
			}
		}
		else if( type.equals("numberChar")){
			if(!isInt(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.integer"));
			}
		}
		else if( type.equals("float")){
			if(!isFloat(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.number"));
			}		
		}
		else if( type.equals("double")){
			if(!isFloat(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.number"));
			}
		}else if( type.equals("date")){
			if(!isDate(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.date"));
			}
		}else if( type.equals("time")){
			if(!isTime(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.date"));
				}
			
		}else if( type.equals("datetime")){
			if(!isDatetime(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.datetime"));
			}
		}else if( type.equals("ip")){
			if(!isIp(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.ip"));
			}
		}
		else if( type.equals("en")){
			if(!isEn(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.en"));
			}
		}
		else if( type.equals("title")){
			if(!isTitle(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.any"));
			}
		}
		else if( type.equals("password")){
			if(!isPassword(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.any"));
			}
		}
		else if( type.equals("html")){
			if(!isAny(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.any"));
			}
		}
		else if( type.equals("any")){
			if(!isAny(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.any"));
			}
		}
		else if( type.equals("mail")){
			if(!isMail(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.invalid"));
			}
		}
		else if( type.equals("tel")){
			if(!isTel(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.invalid")+"\\n"+getResouce(resources,locale,"common.example.phone"));
			}
		}
		else if( type.equals("mobile")){
			if(!isMobile(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.invalid")+"\\n"+getResouce(resources,locale,"common.example.mobile"));
			}
		}
		else if( type.equals("phone")){
			if(!(isMobile(items)||isTel(items))){
				return ( title+getResouce(resources,locale,"common.validate.error.invalid")+"\\n"+getResouce(resources,locale,"common.example.mobile")+"\\n"+getResouce(resources,locale,"common.example.phone"));
			}
		}
		else if( type.equals("zip")){
			if(!isZip(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.invalid"));
			}
		}
		else if( type.equals("url")){
			if(!isUrl(items)){
				return ( title+getResouce(resources,locale,"common.validate.error.invalid"));
			}
		}
		
		if(!maxLimitStr.equals("maxValue")){
			if( type.equals("int") ||  type.equals("float")||  type.equals("double")){
				if(Double.parseDouble(items)> maxLimit)
				{
					return ( title+getResouce(resources,locale,"common.validate.error.larger")+ maxLimit+getResouce(resources,locale,"common.excl"));
				}
				if(Double.parseDouble(items)< minLimit)
				{
					return ( title+getResouce(resources,locale,"common.validate.error.less")+ minLimit+getResouce(resources,locale,"common.excl"));
				}
			}else if( type.equals("password")){
				if (getStringLength(items)> maxLimit){
					return ( title+getResouce(resources,locale,"common.validate.error.longer")+ maxLimit+getResouce(resources,locale,"common.excl"));
				}
				if (getStringLength(items)< minLimit){
					return ( title+getResouce(resources,locale,"common.validate.error.shorter")+ minLimit+getResouce(resources,locale,"common.excl"));
				}
			}else if(items.indexOf("zh_CN:") < 0 &&  !type.equals("html") && !type.equals("date")&&!type.equals("time")&&
					!type.equals("datetime")&&!type.equals("ip")&&!type.equals("mail")&&!type.equals("mobile")&&
					!type.equals("tel")&&!type.equals("phone")&&!type.equals("url")&&!type.equals("zip")&&!type.equals("pic")&&
					!type.equals("affix")){
				if (getStringLength(items)> maxLimit)
				{
					return ( title+getResouce(resources,locale,"common.validate.error.longer")+ maxLimit+getResouce(resources,locale,"common.excl")+"\\n" +getResouce(resources,locale,"common.charDesc"));
				}
				if (getStringLength(items)< minLimit)
				{
					return ( title+ getResouce(resources,locale,"common.validate.error.shorter")+ minLimit+getResouce(resources,locale,"common.excl")+"\\n"+ getResouce(resources,locale,"common.charDesc"));
				}
			}
		}
		
		
		//如果当前字段是序列号并且序列号字段有值，查询序列号数量是否与其匹配
		if (!isDraft && "Seq".equalsIgnoreCase(name) && (items != null && items.toString().trim().length() > 0)) {
			for (int j = 0; j < fieldList.size(); j++) {
				DBFieldInfoBean qtyField = (DBFieldInfoBean) fieldList.get(j);
				if ("SeqQty".equals(qtyField.getFieldSysType())) {
					int seqQty = items.toString().trim().split("~").length;
					double qty = Double.parseDouble(values.get(qtyField.getFieldName()).toString());
					if (seqQty != qty) {
						return ( title+"序列号数量与个数不匹配");
					}
				}
			}
		}
		
		return null;
	}
	
	/*判断是否只含有数字和英文字符*/
	public static boolean isEn(String str){
		return str.matches("^[0-9A-Za-z]*$");
	}
	
	/*判断是否正确的title输入*/
	public static boolean isTitle(String str){
		//return str.matches("/[\\\\<>&\\/|'\"]+/");
		return true;
	}
	
	/*判断是否是密码*/
	public static boolean isPassword(String str){
		return str.matches("^[0-9A-Za-z]+$");
	}
	
	/*判断是否是邮件*/
	public static boolean   isMail(String str){
		return str.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
	}

	/*判断是否是电话号码
	  匹配形式如:0511-4405222 或者021-87888822 或者 021-44055520-555
	*/
	public static boolean   isTel(String str){
		return str.matches("^([0-9]{3,4}-)?[0-9]{7,8}(-[0-9]{1,3})*$");
	}

	/*判断是否是手机号码*/
	public static boolean   isMobile(String str){

		return str.matches("^[0]{0,1}1[0-9]{10}$");
	}

	/*判断是否是邮编*/
	public static boolean   isZip(String str){
		return str.matches("^[0-9]{6}$");
	}

	/*判断是否是网址*/
	public static boolean   isUrl(String str){
		return str.matches("^[a-zA-Z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$");
	}
	
	/*判断是否整数*/
	public static boolean  isDatetime(String str){
		return str.matches("^[0-9]{4}-[0-1][0-9]-[0-3][0-9] [0-2][0-9]:[0-6][0-9]:[0-6][0-9]");
	}
	/*判断是否整数*/
	public static boolean  isIp(String str){
		return str.matches("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
	}
	/*判断是否含有非法字符*/
	public static boolean  isAny(String str){
		if(str != null &&str.length() > 0){
			if(str.indexOf("</textarea>")>=0)return false;
			if(str.indexOf("<textarea>")>=0)return false;
			if(str.indexOf("<select>")>=0)return false;
			if(str.indexOf("</select>")>=0)return false;
			if(str.indexOf("<input>")>=0)return false;
			if(str.indexOf("</input>")>=0)return false;
			if(str.indexOf("<button>")>=0)return false;
			if(str.indexOf("</button>")>=0)return false;
		}
		return true;
	}
	
	/*判断是否整数*/
	public static boolean  isDate(String str){
		return str.matches("^[0-9]{4}-[0-1][0-9]-[0-3][0-9]");		
	}
	/*判断是否是00:00:00类型*/
	public static boolean  isTime(String str){
		return str.matches("[0-2][0-9]:[0-6][0-9]:[0-6][0-9]");
	}
	
	/*判断是否数字（包括小数和整数）*/
	public static boolean  isFloat(String str){
		if(str.equals("Infinity")||str.equals("-Infinity")) 
			return false;
		try{
			Double.parseDouble(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	/*判断是否整数*/
	public static boolean  isInt(String str){
		return str.matches("^[-]{0,1}[0-9]*$");
	}
	
	
	public static String getResouce(MessageResources resources,Locale locale,String key){
		return resources.getMessage(locale, key);
	}
	
	
	
	public static int getStringLength(String str){
		int len = 0;
		if (str== null || str.length() <= 0){
			len = 0;
		}else{
			for (int i=0;i<str.length();i++){
				if (str.charAt(i) < 256){
					len = len + 1;
				}else{
					len = len + 2;
				}
			}
		}
		return len;
	}
	
	public static boolean containSC(String str){
		if(str.indexOf("'")>-1)return false ;
		if(str.indexOf("\"")>-1)return false ;
		if(str.indexOf("|")>-1)return false ;
		if(str.indexOf(";")>-1)return false ;
		if(str.indexOf("\\")>-1)return false ;
		return true ;
	}

}
