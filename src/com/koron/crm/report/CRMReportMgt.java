package com.koron.crm.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;


/**
 * 
 * 
 * <p>Title:CRM报表Mgt</p> 
 * <p>Description: </p>
 *
 * @Date:2013-3-5
 * @Copyright: 科荣软件
 * @Author 徐洁俊
 */
public class CRMReportMgt extends AIODBManager{
	
	/**
	 * 统计报表查询
	 * @param clientId
	 * @param type
	 * @return
	 */
	public Result queryFieldCount(final String querySql,final String fieldName,final String secondFieldName,final String sumFieldName) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							System.out.println("crmReport query sql :" + querySql);
							PreparedStatement ps = connection.prepareStatement(querySql);
							ResultSet rss = ps.executeQuery();
							if(secondFieldName == null || "".equals(secondFieldName)){
								ArrayList<String[]> dataList = new ArrayList<String[]>() ;
								double count = 0;
								while(rss.next()){
									if("".equals(rss.getString(fieldName)) || (rss.getString(fieldName)!=null && rss.getString("zh_CN") ==null)){
										count += rss.getDouble("counts");
									}else{
										String[] str=new String[3];
										str[0]=rss.getString(fieldName);
										str[1]=rss.getString("zh_CN");
										str[2]=rss.getString("counts");
										dataList.add(str) ;
									}
								}
								if(count>0){
									String[] empty = {"","",count+""};
									dataList.add(empty);
								}
								rst.setRetVal(dataList) ;
							}else{
								LinkedHashMap<String, List<String[]>> dataMap = new LinkedHashMap<String, List<String[]>>();//存放数据的map,key是secondFieldId,value是类型总部
								HashMap<String,String> countMap = new HashMap<String, String>();
								String key = "";
								long tempCount = 0;
								while(rss.next()){
									String[] str=new String[3];
									if("employeeId".equals(secondFieldName)||"createBy".equals(secondFieldName)){
										str[0]=GlobalsTool.getEmpFullNameByUserId(rss.getString(secondFieldName));
									}else{
										str[0]=rss.getString(secondFieldName);
									}
									
									str[1]=rss.getString(fieldName);
									str[2]=rss.getString("counts");
									
									if((str[1]!=null && rss.getString("zh_CN") == null) || "".equals(str[1])){
										if(countMap.get(str[0])==null){
											countMap.put(str[0], str[2]);
										}else{
											countMap.put(str[0], Double.parseDouble(str[2])+Double.parseDouble(countMap.get(str[0]))+"");
										}
									}else{
										//封装数据
										if(dataMap.get(str[0]) == null){
											List<String[]> tempList = new ArrayList<String[]>();
											tempList.add(str);
											dataMap.put(str[0], tempList);
										}else{
											List<String[]> tempList = dataMap.get(str[0]);
											tempList.add(str);
											dataMap.put(str[0], tempList);
										}
									}
								}
								
								Set keys = countMap.keySet( );
								if(keys != null) {
								Iterator iterator = keys.iterator( );
									while(iterator.hasNext( )) {
										Object mapKey = iterator.next();
										if(countMap.get(mapKey) !=null && !"0".equals(countMap.get(mapKey))){
											String[] tempStr = {mapKey+"","",countMap.get(mapKey)};
											if(dataMap.get(mapKey) !=null){
												dataMap.get(mapKey).add(tempStr);
											}else{
												List<String[]> list = new ArrayList<String[]>();
												list.add(tempStr);
												dataMap.put(mapKey+"", list);
											}
										}
									}
								}
								
								rst.setRetVal(dataMap) ;
							}
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 查询同比环比数据
	 * @param querySql 查询语句
	 * @param timeScope 所有的时间段
	 * @param fieldName 字段名
	 * @param sumFieldName sum(fieldName),
	 * @param unit 单位
	 * @param isRound 是否环比查询 
	 * @return
	 */
	public Result queryCompareInfo(final String querySql,final String timeScope,final String fieldName,final String secondFieldName,final String sumFieldName,final String phase,final String unit,final boolean isRound) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							System.out.println("compare query sql :" + querySql);
							HashMap<String, String> rsMap = new HashMap<String, String>();
							HashMap<String, String> timeScopeMap = new HashMap<String, String>();
							HashMap<String, HashMap<String,String>> mulFieldsMap = new HashMap<String, HashMap<String,String>>();//存放有secondFieldName的Map
							HashMap<String,String> countMap = new HashMap<String, String>();
							String tempVal = "";
							PreparedStatement ps = connection.prepareStatement(querySql);
							ResultSet rss = ps.executeQuery();
							while(rss.next()){
								String fieldVal = rss.getString(fieldName);
								if(isRound && "quarter".equals(unit)){
									int i =(Math.abs(Integer.parseInt(fieldVal)%4))+1;
									fieldVal = rss.getString("year")+"年Q"+i;
								}else if("month".equals(phase) && "day".equals(unit)){
									fieldVal = Integer.parseInt(fieldVal.substring(fieldVal.indexOf("-")+1))+"";
								}	
								
								if(secondFieldName !=null && !"".equals(secondFieldName)){
									if((rss.getString(secondFieldName)!=null && rss.getString("zh_CN") == null) || "".equals(rss.getString(secondFieldName))){
										if(countMap.get(fieldVal)==null){
											countMap.put(fieldVal, rss.getString("counts"));
										}else{
											countMap.put(fieldVal, Double.parseDouble(rss.getString("counts"))+Double.parseDouble(countMap.get(fieldVal))+"");
										}
									}else{
										if(mulFieldsMap.get(fieldVal) == null){
											//不存在new一个Map
											HashMap<String,String> tempMap = new HashMap<String,String>();
											tempMap.put(rss.getString(secondFieldName), rss.getString("counts"));//存放key:枚举的值,val:counts
											mulFieldsMap.put(fieldVal, tempMap);//key:时间
										}else{
											//存在放入已有的Map中
											mulFieldsMap.get(fieldVal).put(rss.getString(secondFieldName), rss.getString("counts"));
										}
									}
									
									Set keys = countMap.keySet( );
									if(keys != null) {
									Iterator iterator = keys.iterator( );
										while(iterator.hasNext( )) {
											Object mapKey = iterator.next();
											if(countMap.get(mapKey) !=null && !"0".equals(countMap.get(mapKey))){
												
												if(mulFieldsMap.get(mapKey) !=null){
													mulFieldsMap.get(mapKey).put("", countMap.get(mapKey));
												}else{
													HashMap<String,String> tempMap = new HashMap<String,String>();
													tempMap.put("", countMap.get(mapKey));//存放key:枚举的值,val:counts
													mulFieldsMap.put(mapKey+"",tempMap);
												}
											}
										}
									}
									
								}else{
									rsMap.put(fieldVal, rss.getString("counts"));
								}
							}
							
							if(secondFieldName !=null && !"".equals(secondFieldName)){
								rst.setRetVal(mulFieldsMap) ;
							}else{
								//根据日期范围封装数据,若get()==null,默认赋值0
								if(rsMap!=null && rsMap.size()>0){
									for(String key : timeScope.split(",")){
										tempVal = rsMap.get(key) == null?"0":rsMap.get(key);
										timeScopeMap.put(key, tempVal);
									}
								}
								rst.setRetVal(timeScopeMap) ;
							}
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	/**
	 * 周月统计报表查询
	 * @param sql
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Result queryWeekMonth(String sql) {
		System.out.println("weekMonth query sql:"+sql);
		return sqlList(sql, new ArrayList()) ;
	}
	
	/**
	 * 查询客户信息
	 * @param id
	 * @param param 字段值
	 * @param fields 字段名
	 * @return
	 */
	public Result queryDetails(String sql,int pageNo, int pageSize) {
		System.out.println("query detail sql:"+sql);
		return sqlListMaps(sql, new ArrayList(), pageNo, pageSize) ;
	}
	
	/**
     * 获取详情查询为空的条件
     * @param defineId
     * @return
     */
	public Result queryDetailEmpty(final String tableName,final String fieldName,final String dbTableName) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String str = tableName+"."+fieldName;
							String sql = "select distinct isnull("+str+",'') as "+fieldName+",tbllanguage.zh_CN as zh_CN from "+tableName+" left join tblDBEnumerationItem  on "+str+"=tblDBEnumerationItem.enumValue and tblDBEnumerationItem.enumId =(select id from tblDBEnumeration where enumName=(select refEnumerationName from tblDBfieldInfo where tableid=(select id from tbldbtableInfo where tablename='"+dbTableName+"') and fieldName='"+fieldName+"')) left join tbllanguage  on tblDBEnumerationItem.languageId=tbllanguage.id where "+str+" is null or zh_CN is null";
							PreparedStatement ps = connection.prepareStatement(sql);
							ResultSet rss = ps.executeQuery();
							List<String> list = new ArrayList<String>();
							while(rss.next()){
								list.add(rss.getString(fieldName));
							}
							rst.setRetVal(list) ;
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
}
