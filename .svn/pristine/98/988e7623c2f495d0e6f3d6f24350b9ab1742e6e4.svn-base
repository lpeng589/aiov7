package com.menyi.aio.web.finance.popupSelect;

import java.sql.*;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.web.util.*;

/**
 * 财务弹出框 数据库操作类
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2013-05-22
 * @Copyright: 科荣软件
 * @Author fjj
 */
public class PopupMgt extends AIODBManager{

	
	/**
	 * 查询会计科目
	 * @param selectType
	 * @param selectValue
	 * @param isCease
	 * @param allScopeRightList   控制分类权限
	 * @return
	 */
	public Result queryDataAcc(final PopupSearchForm searchForm, final String scopeRightSql){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement st = conn.createStatement();
						ResultSet rs = null;
						
						/* 查询禁用的科目 */
						StringBuffer sql = new StringBuffer("select classCode from tblAccTypeInfo where statusId!=0");
						rs = st.executeQuery(sql.toString());
						String conSql = "";
						while(rs.next()){
							String classCode = rs.getString("classCode");
							conSql = conSql+" classCode not like '"+classCode+"%' AND ";
						}
						
						sql = new StringBuffer("select tblAccTypeInfo.AccNumber,l.zh_CN as AccName,language.zh_CN as AccFullName,PyCode,tblAccTypeInfo.classCode,");
						sql.append("case tblAccTypeInfo.jdFlag when '1' then '借' when '2' then '贷' end as isjdFlag,row_number() over(order by tblAccTypeInfo.classCode) as row_id,");
						if(searchForm.getChooseType()!=null && "chooseChild".equals(searchForm.getChooseType())){
							sql.append("(SELECT COUNT(0) FROM tblAccTypeInfo acc LEFT JOIN tblLanguage la ON la.id=acc.AccName WHERE acc.statusId!='-1' ");
							if(searchForm.getIsCheckItem()!=null && !"".equals(searchForm.getIsCheckItem())){
								sql.append(" and isnull(isCalculate,'')!=1 ");
							}
							sql.append(" and acc.classCode like tblAccTypeInfo.classCode+'_____') ");
						}else{
							sql.append(" isCatalog ");
						}
						sql.append(" AS countnumber from tblAccTypeInfo LEFT JOIN tblLanguage l ON l.id=tblAccTypeInfo.AccName ");
						sql.append("left JOIN tblLanguage language ON language.id=tblAccTypeInfo.AccFullName where 1=1 ");
						if(searchForm.getIsCheckItem()!=null && !"".equals(searchForm.getIsCheckItem())){
							sql.append(" and isnull(isCalculate,'')!=1 ");
						}
						/* 会计科目控制分类权限控制*/
						sql.append(scopeRightSql);
						String selectType = searchForm.getSelectType();
						String selectValue = searchForm.getSelectValue();
						String isCease = searchForm.getIsCease();
						String chooseType = searchForm.getChooseType();
						if(searchForm.getSelectType() != null && !"".equals(selectType)){
							if("keyword".equals(selectType)){
								/* 关键字查询*/
								sql.append(" and (AccNumber like '%"+selectValue+"%' or l.zh_CN like '%"+selectValue+"%' or language.zh_CN like '%"+selectValue+"%')");
							}else if("group".equals(selectType)){
								/* 查询下级 */
								if(selectValue != null && !"".equals(selectValue)){
									sql.append(" and (classCode like '"+selectValue+"_____' or classCode='"+selectValue+"') ");
								}
							}else if("choose".equals(selectType)){
								/* 查询已选的数据 */
								if(selectValue != null && !"".equals(selectValue)){
									sql.append(" and AccNumber in ("+selectValue+")");
								}else{
									sql.append(" and 1=2 ");
								}
							}
						}
						
						if(chooseType!=null && "choosePerent".equals(chooseType)){
							sql.append(" and isCatalog=1 ");
						}
						
						//只显示启用的科目
						if(isCease==null || "".equals(isCease) || "false".equals(isCease)){
							sql.append(" and statusId=0 ");
							if(conSql.length()>0){
								sql.append(" and "+conSql+" 1=1 ");
							}
						}
                        int total=0;
                        int pageNo2 = searchForm.getPageNo();
                        int pageSize = searchForm.getPageSize();
                        String sqlStr = "";
                        try {  
                        	st = conn.createStatement();
	                        if(pageSize>0){//查询总行数
	                        	sqlStr="select count(0) from ("+sql.toString()+") as acc";
	                            rs=st.executeQuery(sqlStr);                            	
	                            if(rs.next()){
	                            	int totalSize=rs.getInt(1);
	                            	total = totalSize;
	                              	int totalPage = totalSize%pageSize>0?totalSize/pageSize+1:totalSize/pageSize;
	                              	if(pageNo2>totalPage){
	                              		pageNo2 = totalPage;
	                              	}
	                              	rst.setTotalPage(totalPage);
	                            }
	                            rs.close();
	                            sqlStr="select *,ROW_NUMBER() OVER( order by row_id asc) as counts from ("+sql.toString()+") as acc";
	                            sqlStr+=" where row_id between "+(pageSize*(pageNo2-1)+1)+" and "+pageSize*pageNo2;    
	                            sqlStr += " ORDER BY classCode,AccName";
	                        }else{
	                            sqlStr=sql.toString();
	                        }
	                        
                            if (pageNo2 < 1){
                            	pageNo2 = 1;
                            }
                            List list = new ArrayList();
                            rs=st.executeQuery(sqlStr);    
                            while (rs.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rs.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	list.add(map);
                            }
                            rst.setRetVal(list);
                            rst.setPageNo(pageNo2);
                            rst.setPageSize(pageSize);
                            rst.setRealTotal(total);
                        } catch (Exception ex) {
                        	BaseEnv.log.error("PopupMgt queryDataAcc:",ex) ;
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
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
	 * ajax调用数据
	 * @param searchForm
	 * @param allScopeRightList
	 * @return
	 */
	public Result ajaxPopAccType(final PopupSearchForm searchForm, final ArrayList allScopeRightList){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement st = conn.createStatement();
						StringBuffer sql = new StringBuffer("SELECT a.AccNumber,l.zh_CN as AccFullName,a.classCode,isCatalog");
						sql.append(" FROM tblAccTypeInfo a LEFT JOIN tblLanguage l ON l.id=a.AccFullName ");
						sql.append(" WHERE 1=1 ");
						/* 会计科目控制分类权限控制*/
						String consql = "";
						if(allScopeRightList != null && allScopeRightList.size()>0){
							for(int i=0;i<allScopeRightList.size();i++){
								LoginScopeBean loginScope = (LoginScopeBean)allScopeRightList.get(i);
								if(loginScope!=null && "tblAccTypeInfo".equals(loginScope.getTableName())){
									//存在设置可以管理的科目
									String scopes = loginScope.getScopeValue();
									String[] value = scopes.split(";");
									for(String strvalue : value){
										if(!"".equals(strvalue)){
											consql += " classCode like '"+strvalue + "%' or ";
										}
									}
								}
							}
						}
						if(consql.length()>0){
							consql = " AND ("+consql.substring(0,consql.length()-3)+")";
						}
						sql.append(consql);
						String selectValue = searchForm.getSelectValue();
						String isCease = searchForm.getIsCease();
						String chooseType = searchForm.getChooseType();
						sql.append(" AND (AccNumber like '%"+selectValue+"%' or l.zh_CN like '%"+selectValue+"%')");
						
						if(chooseType!=null && "choosePerent".equals(chooseType)){
							//只取父级的科目
							sql.append(" AND isCatalog=1 ");
						}else if(chooseType!=null && "chooseChild".equals(chooseType)){
							//只取最下级的科目
							sql.append(" AND isCatalog=0 ");
						}else if(chooseType!=null && "chooseChildItem".equals(chooseType)){
							//选择最下级，当最下级的为核算项时，取上一级
							sql.append(" AND (isCatalog=0 OR IsDept=1 OR IsPersonal=1 OR IsClient=1 OR IsProvider=1 OR isStock=1) AND ISNULL(isCalculate,'')!='1'");
						}
						
						//只显示启用的科目
						if(isCease==null || "".equals(isCease) || "false".equals(isCease)){
							sql.append(" AND statusId=0 ");
						}
						sql.append(" ORDER BY a.AccNumber");
                        try {  
                            List list = new ArrayList();
                            ResultSet rs=st.executeQuery(sql.toString());    
                            while (rs.next()) {
                            	HashMap map=new HashMap();
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rs.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	list.add(map);
                            }
                            rst.setRetVal(list);
                        } catch (Exception ex) {
                        	BaseEnv.log.error("PopupMgt ajaxPopAccType:",ex) ;
                            rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
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
	 * 根据科目代码查询科目的数据
	 * @param accnumber
	 * @return
	 */
	public Result queryAccCode(final String accNumber){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement st = conn.createStatement();
						StringBuffer sql = new StringBuffer("SELECT * FROM tblAccTypeInfo where AccNumber='"+accNumber+"'");
						ResultSet rs=st.executeQuery(sql.toString());    
						HashMap map=new HashMap();
						if(rs.next()) {
                        	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                        		Object obj=rs.getObject(i);
                        		if(obj==null){
                        			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                        				map.put(rs.getMetaData().getColumnName(i), 0);
                        			}else{
                        				map.put(rs.getMetaData().getColumnName(i), "");
                        			}
                        		}else{
                        			map.put(rs.getMetaData().getColumnName(i), obj);
                        		}
                        	}
                        }
						rst.setRetVal(map);
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst;
	}
}
