package com.menyi.aio.web.billNumber;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.dbfactory.Result;
import com.menyi.aio.bean.TblBillNoBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.ErrorCanst;


public class BillNoManager {
	private static TreeMap<String, BillNo> map = new TreeMap<String, BillNo>();
	private void register(BillNo bn)
	{
		map.put(bn.getKey(), bn);
	}
	
	public static void unRegister(String key)
	{
		map.remove(key);
	}
	
	public static void clear()
	{
		map.clear();
	}
	
	//Map中取数据，如果没有就查询数据库再put()
	public synchronized static BillNo find(String key){
		return find(key,null);
	}
	
	//Map中取数据，如果没有就查询数据库再put()
	public synchronized static BillNo find(String key,Connection conn){
		if(key != null && !"".equals(key)){  
			BillNo b = map.get(key);
			if(b!=null){
				return b;
			}else{
				//TODO 从tblbillno表中生成BillNo并注册,如找不着则返回空
				BillNoMgt mgt = new BillNoMgt();
				Result result = new Result();
				if(conn != null){
					result = mgt.onLoad(key,conn);
				}else{
					result = mgt.onLoad(key);
				}
				if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
					TblBillNoBean billNoBean = (TblBillNoBean)result.getRetVal();
					if(billNoBean!=null){
						//存在记录
						BillNo bn = new BillNo(billNoBean.getKey(),billNoBean.getReset(),billNoBean.getLaststamp(),billNoBean.getBillNO());
						bn.setPattern(billNoBean.getPattern());
						bn.setBillNO(billNoBean.getBillNO());
						bn.setFillBack(billNoBean.getIsfillback());
						bn.setStartValue(billNoBean.getStart());
						bn.setStepValue(billNoBean.getStep());
						bn.setAddbeform(billNoBean.getIsAddbeform());
						new BillNoManager().register(bn);
						return bn;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 清空单据编号 fjj
	 * @param fieldLists
	 * @param values
	 */
	public static void billNORemoves1(DBTableInfoBean tableInfo,HashMap values){
		billNORemoves1(tableInfo, values, null);
	}
	
	/**
	 * 清空单据编号 fjj
	 * @param fieldLists
	 * @param values
	 */
	public static void billNORemoves1(DBTableInfoBean tableInfo,HashMap values,Connection conn){
		List fieldLists = tableInfo.getFieldInfos();
		for (int i = 0; i < fieldLists.size(); i++) {
			DBFieldInfoBean fieldInfo = (DBFieldInfoBean) fieldLists.get(i);
	        if(fieldInfo.getFieldIdentityStr()!=null&&DBFieldInfoBean.FIELD_IDENTIFIER.equals(fieldInfo.getFieldIdentityStr())){
	        	String key = "";
	        	String defaultValue = fieldInfo.getDefaultValue();
	        	if(defaultValue!=null && !"".equals(defaultValue)){
	        		//存在默认值
	        		key = defaultValue;
	        	}else{
	        		key = tableInfo.getTableName()+"_"+fieldInfo.getFieldName();
	        	}
	        	BillNo billno = BillNoManager.find(key,conn);
	        	if(billno != null){
		        	if(billno.isFillBack()){
		        		billno.remove(String.valueOf(values.get(fieldInfo.getFieldName())), conn);
		        	}
	        	}
	        }
    	}
	}
	
	public static String find(String key,Object login){
		return find(key, new HashMap<String, String>(), login);
	}
	
	public static String find(String key,HashMap<String, String> input,Object login){
		return find(key, input, login, null);
	}
	
	public static String find(String key,Object login, Connection conn){
		return find(key, new HashMap<String, String>(), login, conn);
	}
	
	public static String find(String key,HashMap<String, String> input,Object login, Connection conn){
		BillNo billNo = find(key,conn);
		String billCode = "" ;
		if(billNo!=null){
			billCode = billNo.getNumber(input, login, conn).getValStr();
		}
		return billCode;
	}
	
}