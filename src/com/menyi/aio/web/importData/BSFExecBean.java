package com.menyi.aio.web.importData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.GlobalsTool;

public class BSFExecBean {
	private HashMap values;
	private Hashtable allTable;
	private DBTableInfoBean tableInfo;
	private HashMap<String,ExcelFieldInfoBean> impFields;
	private Locale locale;
	
	public BSFExecBean(HashMap values ,Hashtable allTable,DBTableInfoBean tableInfo,HashMap<String,ExcelFieldInfoBean> impFields,Locale locale){
		this.values = values;
		this.allTable = allTable;
		this.tableInfo = tableInfo;
		this.impFields = impFields;
		this.locale = locale;
	}
	/**
	 * 主表推算明细表的时候暂不支持
	 * @param field
	 * @param value
	 * @param line
	 */
	public void setValue(String oldField,String field,String value,int line){
		try{
		if(value ==null|| value.length() ==0|| "Infinity".equals(value)||"NaN".equals(value)){
			return;
		}
		if(value.indexOf(".")> -1){ //部分整数运算后变为小数，这里取整
			value = value.substring(0,value.indexOf("."))+value.substring(value.indexOf(".")).replaceFirst("[\\.0]*$", "");
		}
		//BaseEnv.log.debug("重设字段："+field+"; 触 发字段："+oldField+";值:"+value+";行:"+line);
		String tableName="";
		String fieldName="";
		//暂时不执行主表触发明细表的公式
		if(field.indexOf("_")>0&& line != -1){
			tableName = field.substring(0,field.indexOf("_"));
			fieldName = field.substring(field.indexOf("_")+1);
			
			ArrayList cl = (ArrayList)values.get("TABLENAME_"+tableName);
			if(cl != null){
				
				HashMap map = (HashMap)cl.get(line);
				if(map!=null){
					//导入模板中不存在，可给值
					if(impFields.get(field)==null){
						System.out.println("SetValue "+line+":"+field+"="+value);
						DBFieldInfoBean fb= GlobalsTool.getFieldBean(tableName, fieldName);
						int dig =GlobalsTool.getDigitsOrSysSetting(tableName, fieldName);
						if(fb !=null && fb.getFieldType()==DBFieldInfoBean.FIELD_INT){
							dig = 0;				
						}
						if(dig==0){
							map.put(fieldName, Math.round(new BigDecimal(value).doubleValue())+"");
						}else{
							map.put(fieldName, GlobalsTool.round(new BigDecimal(value).doubleValue(), dig)+"");
						}
					}else{
						DBFieldInfoBean fb= GlobalsTool.getFieldBean(tableName, fieldName);
						//如果来源字段在,本字段的公式中出现，说明是有反推功能。要校验公式计算结果对不对.
						if(oldField.startsWith(tableInfo.getTableName()+"_")){
							//来源字段是主表的
							oldField = oldField.substring(oldField.indexOf("_")+1);
						}
						if(fb.getCalculate().indexOf(oldField)>-1){
							//校验值是否正确，如果不正确报错
							
							int dig =GlobalsTool.getDigitsOrSysSetting(tableName, fieldName);
							if(fb !=null && fb.getFieldType()==DBFieldInfoBean.FIELD_INT){
								dig = 0;				
							}
							if(dig==0){
								if(!(Math.round(new BigDecimal(value).doubleValue())+"").equals(map.get(fieldName)==null?null:map.get(fieldName).toString())){
									throw new Exception (fb.getDisplay().get(locale.toString())+"("+(map.get(fieldName)==null?"":map.get(fieldName))+")与公式推算结果("+Math.round(new BigDecimal(value).doubleValue())+")不一致");
								}
							}else{
								if((GlobalsTool.round(new BigDecimal(value).doubleValue(), dig))!=Double.parseDouble(map.get(fieldName)==null?"0":map.get(fieldName).toString())){
									throw new Exception (fb.getDisplay().get(locale.toString())+"("+(map.get(fieldName)==null?"":map.get(fieldName))+")与公式推算结果("+GlobalsTool.round(new BigDecimal(value).doubleValue(), dig)+")不一致");
								}
							}
						}
					}
				}
				
			}	
		}else{
			//主表
			tableName = tableInfo.getTableName();
			fieldName = field;
			
			Object o = values.get(fieldName);
			if(impFields.get(tableName+"_"+field)==null){
				//System.out.println("SetValue +"+field+"="+value);
				int dig =GlobalsTool.getDigitsOrSysSetting(tableName, fieldName);
				DBFieldInfoBean fb = GlobalsTool.getFieldBean(tableName, fieldName);
				if(fb !=null && fb.getFieldType()==DBFieldInfoBean.FIELD_INT){
					dig = 0;				
				}
				if(dig==0){
					values.put(fieldName, Math.round(new BigDecimal(value).doubleValue())+"");
				}else{
					String rv = GlobalsTool.round(new BigDecimal(value).doubleValue(), dig)+"";
					if(rv.indexOf(".")> -1){ //部分整数运算后变为小数，这里取整
						rv = rv.substring(0,rv.indexOf("."))+rv.substring(rv.indexOf(".")).replaceFirst("[\\.0]*$", "");
					}
					values.put(fieldName, rv);
				}
			}else{
				//校验值是否正确，如果不正确报错
				DBFieldInfoBean fb= GlobalsTool.getFieldBean(tableName, fieldName);
				//如果来源字段在,本字段的公式中出现，说明是有反推功能。要校验公式计算结果对不对.
				if(oldField.startsWith(tableInfo.getTableName()+"_")){
					//来源字段是主表的
					oldField = oldField.substring(oldField.indexOf("_")+1);
				}
				if(fb.getCalculate().indexOf(oldField)>-1){					
				
					int dig =GlobalsTool.getDigitsOrSysSetting(tableName, fieldName);
					if(fb !=null && fb.getFieldType()==DBFieldInfoBean.FIELD_INT){
						dig = 0;				
					}
					if(dig==0){
						if(!(Math.round(new BigDecimal(value).doubleValue())+"").equals(values.get(fieldName)==null?null:values.get(fieldName).toString())){
							throw new Exception (fb.getDisplay().get(locale.toString())+"("+(values.get(fieldName)==null?"":values.get(fieldName))+")与公式推算结果("+Math.round(new BigDecimal(value).doubleValue())+")不一致");
						}
					}else{
						if(GlobalsTool.round(new BigDecimal(value).doubleValue(), dig)!=Double.parseDouble(values.get(fieldName)==null?"0":values.get(fieldName).toString())){
							throw new Exception (fb.getDisplay().get(locale.toString())+"("+(values.get(fieldName)==null?"":values.get(fieldName))+")与公式推算结果("+GlobalsTool.round(new BigDecimal(value).doubleValue(), dig)+")不一致");
						}
					}
				}
			}
		}
		}catch(Exception e){
			BaseEnv.log.error("公式运算设置 "+field+" set error:"+value);
			throw new RuntimeException(e);
		}
	}
	public Double getValue(String field,int line){
		String tableName="";
		String fieldName="";
		if(field.indexOf("_")>0){
			tableName = field.substring(0,field.indexOf("_"));
			fieldName = field.substring(field.indexOf("_")+1);
			
			ArrayList cl = (ArrayList)values.get("TABLENAME_"+tableName);
			if(cl != null){
				HashMap map = (HashMap)cl.get(line);
				if(map!=null&&map.get(fieldName)!=null){
					//System.out.println("getValue "+line+":"+field+"="+map.get(fieldName));
					return Double.parseDouble(map.get(fieldName).toString());
				}
			}
//			return "NaN"; //当某个值为空时，如果整个公式不运算会造成最终结果不正确
			return 0d;
//			DBFieldInfoBean bean = DDLOperation.getFieldInfo(allTable, tableName,
//					fieldName);
//			if(bean.getFieldType() == 0 || bean.getFieldType() == 1){
//				return ""; //数值型为空也要给空
//			}else{
//				return "";
//			}			
		}else{
			//主表
			tableName = tableInfo.getTableName();
			fieldName = field;
			
			Object o = values.get(fieldName);
			if(o==null){		
//				return "NaN"; //当某个值为空时，如果整个公式不运算会造成最终结果不正确
				return 0d;
//				DBFieldInfoBean bean = DDLOperation.getFieldInfo(allTable, tableName,
//					fieldName);
//				if(bean.getFieldType() == 0 || bean.getFieldType() == 1){
//					return ""; //数值型为空也要给空
//				}else{
//					return "";
//				}
			}else {
				return Double.parseDouble(o.toString());
			}
		}
	}
	
	public String getStrVal(String field,int line){
		String tableName="";
		String fieldName="";
	
		if(field.indexOf("_")>0){
			tableName = field.substring(0,field.indexOf("_"));
			fieldName = field.substring(field.indexOf("_")+1);
			
			ArrayList cl = (ArrayList)values.get("TABLENAME_"+tableName);
			if(cl != null){
				HashMap map = (HashMap)cl.get(line);
				if(map!=null&&map.get(fieldName)!=null){
				
					return map.get(fieldName).toString();
				}
			}

			return "";
			
		}else{
			//主表
			tableName = tableInfo.getTableName();
			fieldName = field;
			
			Object o = values.get(fieldName);
			if(o==null){		
				return "";

			}else {
				return o.toString();
			}
		}
	}
	
	public Double getSum(String field,int line){
		String tableName = field.substring(0,field.indexOf("_"));
		String fieldName = field.substring(field.indexOf("_")+1);
		
		ArrayList cl = (ArrayList)values.get("TABLENAME_"+tableName);
		double rs =0;
		if(cl==null || cl.size()==0){
			return 0d;
		}
		if(cl != null){
			for(int i=0;i<cl.size();i++){
				HashMap map = (HashMap)cl.get(i);
				if(map!=null&&map.get(fieldName)!=null&& !map.get(fieldName).toString().equals("")){
					rs+=Double.parseDouble(map.get(fieldName).toString());
				}
			}
		}
		
		//System.out.println("sum----------- +"+field+"="+rs);
		
		return rs;
	}
	
}
