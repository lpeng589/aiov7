package com.menyi.aio.web.iniSet;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.jd.open.api.sdk.internal.util.StringUtil;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.CurrencyBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.GlobalMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.web.util.*;

/**
 * 期初 数据库操作类
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2013-10-22
 * @Copyright: 科荣软件
 * @Author fjj
 */
public class IniAccMgt extends AIODBManager{
	
	
	/**
	 * 查询期初数据
	 * @param searchForm
	 * @param accCode
	 * @param sunCompany
	 * @param map
	 * @param simpleAccFlag
	 * @param type
	 * @param department
	 * @param locale
	 * @return
	 */
	public Result queryIni(final IniAccSearchForm searchForm, String accCode, final String sunCompany,
			final Hashtable map, final String simpleAccFlag,final String type, final String department,
			final String locale, final MOperation mop, final LoginBean loginBean){
		
		final String defAccCode=accCode;								//科目classCode
		final String accNumber = searchForm.getAccNumber();				//科目代码
		final String accName = searchForm.getAccName();					//科目名称
		final String keyword = searchForm.getKeyword();					//模糊查询
		final byte inputType=GlobalsTool.getFieldBean("tblAccTypeInfo", "AccName").getInputType();
		
        StringBuffer sql=new StringBuffer("select accBanlance.id, b.classCode,b.AccNumber,");
        sql.append("case isnull(l."+locale+",'') when '' then b.AccName else isnull(l."+locale+",'') end as AccName,");
        sql.append("accBanlance.CurrYIniBase,accBanlance.CurrYIniDebitSumBase,accBanlance.CurrYIniCreditSumBase,accBanlance.CurrYIniBalaBase,");
        sql.append("accBanlance.CurrYIniBala,accBanlance.curRate,b.isCatalog as count,b.JdFlag,");
        sql.append("case when IsDept=1 then 1 when IsPersonal=1 then 1 when IsClient=1 then 1 when IsProvider=1 then 1 ");
        sql.append("when isStock=1 then 1 when IsProject=1 then 1 when IsForCur=1 then 1 else 0 end as ischeck,isnull(b.isCalculate,'') as isCalculate,");
        sql.append("row_number() over(order by accBanlance.subCode) as row_id");
        sql.append(" from tblAccBalance accBanlance left join tblAccTypeInfo b ");
        sql.append("on accBanlance.SubCode=b.AccNumber left join tblLanguage l on l.id=b.AccName");
        sql.append(" where accBanlance.Period=-1 and accBanlance.SCompanyID='"+sunCompany+"' and b.SCompanyID='"+sunCompany+"'");
        
        //部门核算
        if(department!=null&&department.length()>0){
        	sql.append(" and accBanlance.departmentCode='"+department+"'");
        }else{
        	sql.append(" and isnull(accBanlance.departmentCode,'')=''");
        }
        
        if("1".equals(simpleAccFlag)){
            sql.append(" and (accBanlance.SubCode like '1001%' or accBanlance.SubCode like '1002%' )");
            if (accCode.equals("")) {
            	accCode = "_____";
            }
        }
        
//        if("true".equals(BaseEnv.systemSet.get("prerecvpaymarktoneedrecvpay").getSetting())){
//        	sql.append(" and b.AccNumber not in ('1123','2203') ");
//        }
        
        //查询
        if(type==null || "query".equals(type)){
        	if (accNumber != null && !"".equals(accNumber)) {
        		sql.append(" and b.AccNumber like '" + accNumber + "%'");
        	}
            if (accName != null && !"".equals(accName)) {
            	if(inputType!=4){
            		sql.append(" and b.accName like '%" + accName + "%' ");
            	}else{
            		sql.append(" and b.accName=c.id and c."+locale+" like '%"+accName+"%' ");
            	}
            }
            //模糊查询
            String keywordSql = "";
            if (keyword != null && !"".equals(keyword)){
            	keywordSql = "or b.AccNumber like '%"+keyword+"%' or accBanlance.curryinibase like '%" +
            	keyword + "%'"+" or accBanlance.curryinibalabase like '%" +keyword + "%' "+" or accBanlance.curryinidebitsumbase like '%" +
            	keyword + "%' "+ " or accBanlance.curryinicreditsumbase like '%" +
            	keyword + "%'or  b.accname in " +
                "(select accname from (select id as accname, zh_cn,zh_tw,zh_hk,en from tbllanguage d  ) " +
                " e where e.zh_CN like '%"+keyword+"%')";   
            	 
            }	
            if (keywordSql.length() > 0) {
                sql.append(keywordSql.replaceFirst("or", " and (") + ")");
            }
        }
        
        /* 权限控制 */
        ArrayList scopeRight = new ArrayList();
        scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
        scopeRight.addAll(loginBean.getAllScopeRight());
        String scopeRightSql = DynDBManager.scopeRightHandler("tblAccTypeInfo", "TABLELIST", "", loginBean.getId(), scopeRight, "select * from tblAccTypeInfo where 1=1 ", "endClass","");
        scopeRightSql = scopeRightSql.substring(scopeRightSql.indexOf("where 1=1")+"where 1=1".length()).replaceAll("TBLACCTYPEINFO", "b");
        if(accCode.length()==0){
        	if("".equals(scopeRightSql) || scopeRightSql.trim().length()==0){
        		ReportDataMgt reportMgt = new ReportDataMgt();
        		String hqlMin="select min(len(b.classCode)) "+sql.toString().substring(sql.toString().lastIndexOf("from tblAccBalance"));
        		//查询classCode最小的长度
        		Result rs2=reportMgt.getMinClassLen(hqlMin, new ArrayList());
        		if(rs2.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
        			return rs2;
        		}else{
        			sql.append(" and len(b.classCode)="+rs2.getRetVal() );
        		}
        	}else{
        		sql.append(" and isnull(b.isCalculate,'')!=1 and b.isCatalog!=1");
        	}
        }else{
             sql.append(" and b.classCode like '"+accCode+"_____'");
        }
        sql.append(scopeRightSql);
        
		
        final String sqls = sql.toString();
        BaseEnv.log.debug("IniAccMgt sql = "+sqls);
        final Result result=new Result();
        int retCode = DBUtil.execute(new IfDB() {
          public int exec(Session session) {
              session.doWork(new Work() {
                  public void execute(Connection connection) throws
                          SQLException {
                      Connection conn = connection;
                      try {
                    	  Statement st = conn.createStatement();
                    	  ResultSet rs = null;
                    	  String sqlStr = "";
                    	  int total=0;
                          int pageNo2 = searchForm.getPageNo();
                          int pageSize = searchForm.getPageSize();
                          //查询期初数据库（分页）
                          if(pageSize>0){
                        	  sqlStr="select count(0) from ("+sqls+") as a";
                        	  rs=st.executeQuery(sqlStr);                            	
                        	  if(rs.next()){
                        		  int totalSize=rs.getInt(1);
                        		  total = totalSize;
		                          int totalPage = totalSize%pageSize>0?totalSize/pageSize+1:totalSize/pageSize;
		                          if(pageNo2>totalPage){
		                        	  pageNo2 = totalPage;
		                          }
		                          result.setTotalPage(totalPage);
                        	  }
	                          rs.close();
	                          sqlStr="select *,ROW_NUMBER() OVER( order by a.row_id asc) as counts from ("+sqls+") as a";
	                          sqlStr+=" where row_id between "+(pageSize*(pageNo2-1)+1)+" and "+pageSize*pageNo2;    
                          }else{
                        	  sqlStr=sqls.toString();
                          }
                          rs = st.executeQuery(sqlStr);
                          List values=new ArrayList();
                          while(rs.next()){
                        	  Object[] value=new Object[14];
                        	  value[0] = rs.getString(1);
                        	  value[1] = rs.getString(2);
                        	  value[2] = rs.getString(3);
                        	  value[3] = rs.getString(4);
                        	  for(int i = 4;i <= 9 ;i++){
                        		  Object obj = rs.getObject(i+1);
                        		  if(obj == null){
                        			  obj = "0";
                        		  }
                        		  value[i] = GlobalsTool.formatNumberS(obj, false, false,"Amount","");
                        	  }
                        	  value[10] = rs.getInt(11);
                        	  value[11] = rs.getInt(12);
                        	  value[12] = rs.getInt(13);
                        	  value[13] = rs.getString(14);
                        	  values.add(value);
                          }
                          sqlStr = "";
                          if(defAccCode != null && !"".equals(defAccCode)){
                        	  sqlStr="select JdFlag from tblAccTypeInfo where classCode='"+defAccCode+"' ";
                          }else if(accName != null && !"".equals(accName)){
                        	   if(inputType==4){
                        		   sqlStr="select JdFlag from tblAccTypeInfo a left join tbllanguage b on a.accName=b.id where b."+locale+"='"+accName+"'";
                        	   }else{
                        		   sqlStr="select JdFlag from tblAccTypeInfo where accName='"+accName+"' ";
                        	   }
                          }else if(accNumber != null && !"".equals(accNumber)){
                        	   sqlStr="select JdFlag from tblAccTypeInfo where accNumber='"+accNumber+"' ";
                          }
                          if(sqlStr.length()>0){
	                           rs=st.executeQuery(sqlStr);
	                           if(rs.next()){
	                        	   values.add(rs.getString(1));
	                           }
                          }else{
                        	   values.add("");
                          }
                          result.retVal = values;
                          result.setPageNo(pageNo2);
                          result.setPageSize(pageSize);
                          result.setRealTotal(total);
                      } catch (SQLException ex) {
                          ex.printStackTrace();
                          result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                          result.setRetVal(ex.getMessage());
                          return ;
                      }
                  }
              });
              return result.getRetCode();
          }
      });
	    result.setRetCode(retCode);
	    return result;
	}
	
	
	/**
	 * 根据部门classCode查询部门名称
	 * @param deptCode
	 * @return
	 */
	public Result getDeptName(final String deptCode) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						Statement stmt = conn.createStatement();
						try {
                            String sql = "select DeptFullName from tblDepartment where classCode='"+deptCode+"'";
                            ResultSet rss=stmt.executeQuery(sql);
                            String deptName="";
                            if(rss.next()){
                            	deptName=rss.getString(1);
                            }
                            rs.setRetVal(deptName);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 取科目的核算项信息
	 * @param accNumber
	 * @param sunCompany
	 * @return
	 */
	public Result getAccTypeCal(final String accNumber){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						
						try {							
							/**
							 * 查询会计科目数据（是否启用核算项）
							 */
							StringBuffer sql = new StringBuffer(
									"select '部门;DepartmentCode;'+convert(varchar,IsDept)+';SelectIniDepartment;' as dept," +
									"'职员;EmployeeID;'+convert(varchar,IsPersonal)+';SelectIniEmployee;' as personal," +
									"case when IsClient=1 then '客户;CompanyCode;'+convert(varchar,IsClient)+';SelectIniClient;Customer' when " +
									"IsProvider=1 then '供应商;CompanyCode;'+convert(varchar,IsProvider)+';SelectIniProvider;Supplier' else '往来单位;CompanyCode;'+convert(varchar,2)+';SelectIniExpensed;CompanyCodeIdentifier' end as company," +
									"'仓库;StockCode;'+convert(varchar,isStock)+';SelectIniStocks;' as stock," +
									"'项目;ProjectCode;'+convert(varchar,IsProject)+';SelectIniProjectInfo;' as project, " +
									"(convert(varchar,IsForCur)+';'+isnull(convert(varchar,currency),'')) as currencys ," +
									"zh_cn as AccName ");
							sql.append("from tblAccTypeInfo left join tblLanguage on tblAccTypeInfo.AccName=tblLanguage.id where AccNumber = ? ");
							PreparedStatement st = conn.prepareStatement(sql.toString());
							st.setString(1, accNumber);
							ResultSet rs = st.executeQuery();
							List isItem = new ArrayList();								//核算项							
							String currencys = "";										//外币数据（是否启用外币）
							boolean isCalculates = false;
							String accName = "";
							if(rs.next()){
								for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
	                        		String obj=rs.getString(i);
	                        		String[] str = obj.split(";");
	                        		if("currencys".equals(rs.getMetaData().getColumnName(i))){
	                        			//外币进行特殊处理
	                        			currencys= obj;
	                        			if("1".equals(str[0])){
	                        				isCalculates = true;
	                        			}
	                        			continue;
	                        		}else if("AccName".equals(rs.getMetaData().getColumnName(i))){
	                        			accName = obj;
	                        			continue;
	                        		}
	                        		if("1".equals(str[2])){
	                        			//核算
	                        			isItem.add(obj);
	                        			isCalculates = true;
	                        		}
	                        	}
							}
							
							/**
							 * 保存数据（map 期初数据，iniList 期初明细数据，accTypeInfoMap 科目数据，currencys 外币）
							 */
							Object[] obj = new Object[]{isItem, currencys, isCalculates, null,accName}; 
							result.setRetVal(obj);
							
						} catch (Exception ex) {
							ex.printStackTrace();
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							result.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		try {
			//外币进行处理 
			Object[] obj = (Object[])result.getRetVal();
			String str = String.valueOf(obj[1]);
			String[] s = str.split(";");
			GlobalMgt mgt = new GlobalMgt();
			List currList = new ArrayList();
			if("1".equals(s[0])){
				//启用外币核算
				if(s.length == 1 || "".equals(s[1])){
					//未选择任何外币(查询所有的外币)
					
					currList.add(new Object[]{"","本位币"});
					Result rs2 = mgt.getCurrency();
					if(rs2.retCode!=ErrorCanst.DEFAULT_SUCCESS){
		            	result.setRetCode(rs2.getRetCode());
		            }else{
		            	List rsList = (ArrayList)rs2.getRetVal();
		            	for(Object o : rsList){
		            		currList.add(o);
		            	}
		            }
				}else{					
					//指定外币
		            Result rs2=this.loadBean(s[1],CurrencyBean.class);
		            if(rs2.retCode!=ErrorCanst.DEFAULT_SUCCESS){
		            	result.setRetCode(rs2.getRetCode());
		            }else{
		                CurrencyBean cur=(CurrencyBean)rs2.getRetVal();
		                currList.add(new Object[]{s[1],cur.getCurrencyName()});
		            }
				}
			}
			obj[3] = currList;
	        result.setRetVal(obj);
		} catch (Exception ex) {
          	result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
          	result.setRetVal(ex.getMessage());
          	BaseEnv.log.error("IniAccMgt.getAccTypeCal ",ex);
        }
		return result;
	}
	
	public Result getAccItem(final String keyId,final String local){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						
						try {				
							
							/**
							 * 查往期初明细表tblIniAccDet中数据
							 */
							StringBuffer sql = new StringBuffer("");
							sql.append("select tblIniAccDet.*,tblCurrency.CurrencyName,tblCompany.comFullName as CompanyCodeName,tblEmployee.empFullName as EmployeeIDName,tblDepartment.deptFullName as DepartmentCodeName,");
							sql.append("tblStock.stockFullName as StockCodeName,l."+local+" as ProjectCodeName ");
							sql.append("from tblIniAccDet left join tblCurrency on  tblCurrency.id=tblIniAccDet.Currency ");
							sql.append("left join tblCompany on tblCompany.classCode=tblIniAccDet.CompanyCode ");
							sql.append("left join tblDepartment on tblDepartment.classCode=tblIniAccDet.DepartmentCode ");
							sql.append("left join tblEmployee on tblEmployee.id=tblIniAccDet.EmployeeID ");
							sql.append("left join tblProject on tblProject.id=tblIniAccDet.ProjectCode ");
							sql.append("left join tblLanguage l on l.id=tblProject.ProjectName ");
							sql.append("left join tblStock on tblStock.classCode=tblIniAccDet.StockCode ");
							sql.append(" where tblIniAccDet.id=?");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, keyId);
							ResultSet rset = ps.executeQuery();
							HashMap map=new HashMap();
							if(rset.next()){
			                	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
			                		Object obj=rset.getObject(i);
			                		String name = rset.getMetaData().getColumnName(i);
			                		if(obj==null){
			                			map.put(name, "");
			                		}else{
			                			if(rset.getMetaData().getColumnType(i) == Types.NUMERIC){
			            					String strvalue = String.valueOf(obj);	
			            					if (strvalue.indexOf(".")>0){
	                    						String disName = "DigitsAmount";
	                    						if("CurrencyRate".equals(name)){
	                    							disName = "DigitsPrice";
	                    						}
	                    						strvalue = ""+GlobalsTool.round(new BigDecimal(strvalue).doubleValue(),Integer.valueOf(BaseEnv.systemSet.get(disName).getSetting()));
	                    					}
			            					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
			                					strvalue = "0";
			            					}			            					
			                				map.put(name, strvalue);
			                			}else{
			                				if("0E-8".equals(obj)){
			                					obj = "";
			                				}
			                				map.put(name, obj);
			                			}
			                		}
			                	}
							}
							result.setRetVal(map);
						} catch (Exception ex) {
							BaseEnv.log.error("IniAccMgt.getAccItem ",ex);
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							result.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);		
		return result;
	}
	
	public Result getImportHistory(final String accNumber){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						
						try {				
							
							/**
							 * 查往期初明细表tblIniAccDet中数据
							 */
							StringBuffer sql = new StringBuffer("");
							sql.append("select * from tblIniAccImport where accNumber=? ");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, accNumber);
							ResultSet rset = ps.executeQuery();
							ArrayList list = new ArrayList();
							while(rset.next()){
								HashMap map=new HashMap();
			                	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
			                		Object obj=rset.getObject(i);
			                		String name = rset.getMetaData().getColumnName(i);
			                		if(obj==null){
			                			map.put(name, "");
			                		}else{
			                			map.put(name, obj);
			                		}
			                	}
			                	list.add(map);
							}
							result.setRetVal(list);
						} catch (Exception ex) {
							BaseEnv.log.error("IniAccMgt.getImportHistory ",ex);
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							result.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);		
		return result;
	}
	
	
	/**
	 * 根据accCode查询期初金额
	 * @param accCode
	 * @return
	 */
	public Result getIniAccCode(final String accCode,final String accNumber,
			final String local,final String sunCompany,final String pageSize,final String pageNo,final String conDepartmentCode,
			final String conEmployeeID,final String conProjectCode,final String conStockCode,final String conCompanyCode,final String conCurrency) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						Statement st = conn.createStatement();
						try {
							
							/**
							 * 查询tblAccBalance中的期初金额
							 */
							StringBuffer sql=new StringBuffer("select accBanlance.id, b.classCode,b.AccNumber,isnull(b.isCalculate,'') as isCalculate,");
					        sql.append("case isnull(l."+local+",'') when '' then b.AccName else isnull(l."+local+",'') end as AccName,");
					        sql.append("accBanlance.CurrYIniBase,accBanlance.CurrYIniDebitSumBase,accBanlance.CurrYIniCreditSumBase,accBanlance.CurrYIniBalaBase,");
					        sql.append("accBanlance.CurrYIni,accBanlance.CurrYIniDebitSum,accBanlance.CurrYIniCreditSum,accBanlance.CurrYIniBala,");
					        sql.append("isnull(accBanlance.curRate,0) as curRate,b.JdFlag,isnull(b.Currency,'') as Currency,isnull(c.CurrencyName,'') as CurrencyName ");
					        sql.append("from tblAccBalance accBanlance left join tblAccTypeInfo b ");
					        sql.append("on accBanlance.SubCode=b.AccNumber left join tblCurrency c on b.Currency=c.id ");
					        sql.append("left join tblLanguage l on l.id=b.AccName ");
					        sql.append("where accBanlance.SubCode='"+accNumber+"' and accBanlance.period=-1 ");
							ResultSet rs = st.executeQuery(sql.toString());
							HashMap map=new HashMap();
							if(rs.next()){
	                        	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
	                        		Object obj=rs.getObject(i);
	                        		if(obj==null){
	                        			map.put(rs.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			if(rs.getMetaData().getColumnType(i) == Types.NUMERIC){
	                    					String strvalue = String.valueOf(obj);
	                    					if (strvalue.indexOf(".")>0){
	                    						String disName = "DigitsAmount";
	                    						if("curRate".equals(rs.getMetaData().getColumnName(i))){
	                    							disName = "DigitsPrice";
	                    						}
	                    						//strvalue = ""+GlobalsTool.round(new BigDecimal(strvalue).doubleValue(),Integer.valueOf(BaseEnv.systemSet.get(disName).getSetting()));
	                    						java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
	                    						nf.setGroupingUsed(false);
	                    						nf.setMaximumFractionDigits(Integer.valueOf(BaseEnv.systemSet.get(disName).getSetting()));
	                    						nf.setMinimumFractionDigits(0);
	                    						strvalue = nf.format(new BigDecimal(strvalue).doubleValue());
	                    					}
	                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
	                        					strvalue = "0";
	                    					}
	                        				map.put(rs.getMetaData().getColumnName(i), strvalue);
	                        			}else{
	                        				if("0E-8".equals(obj)){
	                        					obj = "";
	                        				}
	                        				map.put(rs.getMetaData().getColumnName(i), obj);
	                        			}
	                        		}
	                        	}
							}
							
							/**
							 * 查询期初的分录数据（tblIniAccDet）
							 */
//							sql = new StringBuffer("select * from tblIniAccDet where f_ref='"+keyid+"'");
							sql = new StringBuffer("");
							sql.append("select tblIniAccDet.*,tblCurrency.CurrencyName,tblCompany.comFullName as CompanyCodeName,tblEmployee.empFullName as EmployeeIDName,tblDepartment.deptFullName as DepartmentCodeName,");
							sql.append("tblStock.stockFullName as StockCodeName,l."+local+" as ProjectCodeName, ");
							sql.append("ROW_NUMBER() over(order by  tblCurrency.CurrencyName,tblCompany.comFullName,tblEmployee.empFullName,tblDepartment.deptFullName,tblStock.stockFullName,l."+local+") as row_id ");
							sql.append("from tblIniAccDet left join tblCurrency on  tblCurrency.id=tblIniAccDet.Currency ");
							sql.append("left join tblCompany on tblCompany.classCode=tblIniAccDet.CompanyCode ");
							sql.append("left join tblDepartment on tblDepartment.classCode=tblIniAccDet.DepartmentCode ");
							sql.append("left join tblEmployee on tblEmployee.id=tblIniAccDet.EmployeeID ");
							sql.append("left join tblProject on tblProject.id=tblIniAccDet.ProjectCode ");
							sql.append("left join tblLanguage l on l.id=tblProject.ProjectName ");
							sql.append("left join tblStock on tblStock.classCode=tblIniAccDet.StockCode ");
							sql.append(" where tblIniAccDet.accCode like '"+accNumber+"%'");
//							String conDepartmentCode,
//							String conEmployeeID,String conProjectCode,String conStockCode,String conCompanyCode
							if(conDepartmentCode != null && conDepartmentCode.length() >0){
								sql.append(" and tblIniAccDet.DepartmentCode like '"+conDepartmentCode+"%'");
							}
							if(conEmployeeID != null && conEmployeeID.length() >0){
								sql.append(" and tblIniAccDet.EmployeeID = '"+conEmployeeID+"'");
							}
							if(conProjectCode != null && conProjectCode.length() >0){
								sql.append(" and tblIniAccDet.ProjectCode like '"+conProjectCode+"%'");
							}
							if(conStockCode != null && conStockCode.length() >0){
								sql.append(" and tblIniAccDet.StockCode like '"+conStockCode+"%'");
							}
							if(conCompanyCode != null && conCompanyCode.length() >0){
								sql.append(" and tblIniAccDet.CompanyCode like '"+conCompanyCode+"%'");
							}
							if(conCurrency != null && !conCurrency.equals("all") ){
								sql.append(" and tblIniAccDet.Currency = '"+conCurrency+"'");
							}
							int pageSizeI = pageSize==null?100:Integer.parseInt(pageSize);
							result.pageSize = pageSizeI;
							
							//计算总行数
							rs = st.executeQuery(" select count(0) from ( "+sql.toString() + " ) a ");
							if(rs.next()){
								result.realTotal = rs.getInt(1);
							}						
							result.totalPage = (result.realTotal+(result.pageSize -1)) / result.pageSize;
							int pageNoI = pageNo==null?1:Integer.parseInt(pageNo);
							
							if(pageNoI <1){
								pageNoI = 1;
							}
							if(pageNoI >result.totalPage){
								pageNoI = result.totalPage;
							}							
							result.pageNo = pageNoI;
							
							
							
							int star = (pageNoI-1)*pageSizeI+1;
							int endr = (pageNoI)*pageSizeI;
							
							System.out.println(sql.toString());
							
							rs = st.executeQuery(" select * from ( "+sql.toString() + " ) a  where row_id between "+star+" and "+endr+" order by row_id ");
							List iniList = new ArrayList();
							while(rs.next()){
								HashMap iniMap = new HashMap();
								for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
	                        		Object obj=rs.getObject(i);
	                        		if(obj==null){
	                        			iniMap.put(rs.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			if(rs.getMetaData().getColumnType(i) == Types.NUMERIC){
	                    					String strvalue = String.valueOf(obj);
	                    					if (strvalue.indexOf(".")>0){
	                    						String disName = "DigitsAmount";
	                    						if("CurrencyRate".equals(rs.getMetaData().getColumnName(i))){
	                    							disName = "DigitsPrice";
	                    						}
	                    						//strvalue = ""+GlobalsTool.round(new BigDecimal(strvalue).doubleValue(),Integer.valueOf(BaseEnv.systemSet.get(disName).getSetting()));
	                    						java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
	                    						nf.setGroupingUsed(false);
	                    						nf.setMaximumFractionDigits(Integer.valueOf(BaseEnv.systemSet.get(disName).getSetting()));
	                    						nf.setMinimumFractionDigits(0);
	                    						strvalue = nf.format(new BigDecimal(strvalue).doubleValue());
	                    					}
	                    					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
	                        					strvalue = "0";
	                    					}
	                    					if("CurrencyRate".equals(rs.getMetaData().getColumnName(i))){
	                    						strvalue = String.valueOf(Float.parseFloat(strvalue));
                    						}
	                    					iniMap.put(rs.getMetaData().getColumnName(i), strvalue);
	                        			}else{
	                        				if("0E-8".equals(obj)){
	                        					obj = "";
	                        				}
	                        				iniMap.put(rs.getMetaData().getColumnName(i), obj);
	                        			}
	                        		}
	                        	}
								iniList.add(iniMap);
							}
							
							/**
							 * 查询会计科目数据（是否启用核算项）
							 */
							sql = new StringBuffer(
									"select '部门;DepartmentCode;'+convert(varchar,IsDept)+';SelectIniDepartment;' as dept," +
									"'职员;EmployeeID;'+convert(varchar,IsPersonal)+';SelectIniEmployee;' as personal," +
									"case when IsClient=1 then '客户;CompanyCode;'+convert(varchar,IsClient)+';SelectIniClient;Customer' when " +
									"IsProvider=1 then '供应商;CompanyCode;'+convert(varchar,IsProvider)+';SelectIniProvider;Supplier' else '往来单位;CompanyCode;'+convert(varchar,2)+';SelectIniExpensed;CompanyCodeIdentifier' end as company," +
									"'仓库;StockCode;'+convert(varchar,isStock)+';SelectIniStocks;' as stock," +
									"'项目;ProjectCode;'+convert(varchar,IsProject)+';SelectIniProjectInfo;' as project, " +
									"(convert(varchar,IsForCur)+';'+isnull(convert(varchar,currency),'')) as currencys ");
							sql.append("from tblAccTypeInfo where classCode = '"+accCode+"'");
							rs = st.executeQuery(sql.toString());
							List isItem = new ArrayList();								//核算项
							List isNoItem = new ArrayList();							//未启用的核算项
							String currencys = "";										//外币数据（是否启用外币）
							boolean isCalculates = false;
							if(rs.next()){
								for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
	                        		String obj=rs.getString(i);
	                        		String[] str = obj.split(";");
	                        		if("currencys".equals(rs.getMetaData().getColumnName(i))){
	                        			//外币进行特殊处理
	                        			currencys= obj;
	                        			if("1".equals(str[0])){
	                        				isCalculates = true;
	                        			}
	                        			continue;
	                        		}
	                        		if("1".equals(str[2])){
	                        			//核算
	                        			isItem.add(obj);
	                        			isCalculates = true;
	                        		}else{
	                        			isNoItem.add(obj);
	                        		}
	                        	}
							}
							
							/**
							 * 保存数据（map 期初数据，iniList 期初明细数据，accTypeInfoMap 科目数据，currencys 外币）
							 */
							Object[] obj = new Object[]{map, iniList, isItem, isNoItem, currencys, isCalculates, null}; 
							result.setRetVal(obj);
							
						} catch (Exception ex) {
							ex.printStackTrace();
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							result.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		try {
			//外币进行处理
			Object[] obj = (Object[])result.getRetVal();
			String str = String.valueOf(obj[4]);
			String[] s = str.split(";");
			HashMap map = (HashMap)obj[0];
			GlobalMgt mgt = new GlobalMgt();
			List currList = new ArrayList();
			if("1".equals(s[0])){
				//启用外币核算
				if(s.length == 1 || "".equals(s[1])){
					//未选择任何外币(查询所有的外币)
					
					currList.add(new Object[]{"","本位币"});
					Result rs2 = mgt.getCurrency();
					if(rs2.retCode!=ErrorCanst.DEFAULT_SUCCESS){
		            	result.setRetCode(rs2.getRetCode());
		            }else{
		            	List rsList = (ArrayList)rs2.getRetVal();
		            	for(Object o : rsList){
		            		currList.add(o);
		            	}
		            }
				}else{
					if(map.get("curRate") != null  && !"".equals(map.get("curRate")) 
		            		&& Double.parseDouble(map.get("curRate").toString()) == 0 ){
		            	map.put("curRate", mgt.getCurrencyRate(map.get("Currency").toString(),sunCompany));
		            }
					//指定外币
		            Result rs2=this.loadBean(map.get("Currency").toString(),CurrencyBean.class);
		            if(rs2.retCode!=ErrorCanst.DEFAULT_SUCCESS){
		            	result.setRetCode(rs2.getRetCode());
		            }else{
		                CurrencyBean cur=(CurrencyBean)rs2.getRetVal();
		                map.put("CurrencyName",cur.getCurrencyName());
		                currList.add(new Object[]{map.get("Currency").toString(),cur.getCurrencyName()});
		            }
				}
			}
			obj[0] = map;
			obj[6] = currList;
	        result.setRetVal(obj);
		} catch (Exception ex) {
          	result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
          	result.setRetVal(ex.getMessage());
        }
		return result;
	}
	/**
	 * 增加核算期初项
	 * @param map 期初项
	 * @param keyid
	 * @param classCode
	 * @param sunClassCode
	 * @param accNumber
	 * @param openFCurrency
	 * @return
	 */
	public Result updateIniAccItem(final HashMap map, final String accNumber,final LoginBean lg,final Locale locale,
			final MessageResources resources) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String[] keyId = new String[]{(String)map.get("id")};
						Result res = doDelIniAccItem(keyId,  accNumber,   lg, locale, resources, conn);
						if(res.retCode != ErrorCanst.DEFAULT_SUCCESS){
							rs.retCode = res.retCode;
							rs.retVal = res.retVal;
							return;
						}
						res = doAddIniAccItem(map,  accNumber,   lg, locale, resources, conn);
						rs.retCode = res.retCode;
						rs.retVal = res.retVal;
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	/**
	 * 增加核算期初项
	 * @param map 期初项
	 * @param keyid
	 * @param classCode
	 * @param sunClassCode
	 * @param accNumber
	 * @param openFCurrency
	 * @return
	 */
	public Result addIniAccItem(final HashMap map, final String accNumber,final LoginBean lg,final Locale locale,
			final MessageResources resources) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						Result res = doAddIniAccItem(map,  accNumber,  lg, locale, resources, conn);
						rs.retCode = res.retCode;
						rs.retVal = res.retVal;
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 增加核算期初项
	 * @param map 期初项
	 * @param keyid
	 * @param classCode
	 * @param sunClassCode
	 * @param accNumber
	 * @param openFCurrency
	 * @return
	 */
	public Result doAddIniAccItem(final HashMap map, final String accNumber,final LoginBean lg,final Locale locale,
			final MessageResources resources,Connection conn) {
		final Result rs = new Result();
		try {
			int digits = Integer.parseInt(GlobalsTool.getSysSetting("DigitsAmount"));
			int digitsPrice = Integer.parseInt(GlobalsTool.getSysSetting("DigitsPrice"));
			
			Result result;
			map.put("CompanyCode", (map.get("CompanyCode") == null ? "" : map.get("CompanyCode")));
			map.put("StockCode", (map.get("StockCode") == null ? "" : map.get("StockCode")));
			map.put("DepartmentCode", (map.get("DepartmentCode") == null ? "" : map.get("DepartmentCode")));
			map.put("EmployeeID", (map.get("EmployeeID") == null ? "" : map.get("EmployeeID")));
			map.put("ProjectCode", (map.get("ProjectCode") == null ? "" : map.get("ProjectCode")));
			map.put("Currency", (map.get("Currency") == null ? "" : map.get("Currency")));
			
			if(map.get("CurrencyRate") != null){ //计算本币金额
				BigDecimal rate = new BigDecimal((String)map.get("CurrencyRate"));
				BigDecimal cur = new BigDecimal(StringUtil.isEmpty((String)map.get("CurBeginAmount"))?"0": (String)map.get("CurBeginAmount"));
				map.put("BeginAmount", GlobalsTool.round(cur.multiply(rate ).doubleValue(),digits));
				cur = new BigDecimal(StringUtil.isEmpty((String)map.get("CurTotalDebit"))?"0": (String)map.get("CurTotalDebit"));
				map.put("TotalDebit", GlobalsTool.round(cur.multiply(rate ).doubleValue(),digits));
				cur = new BigDecimal(StringUtil.isEmpty((String)map.get("CurTotalLend"))?"0": (String)map.get("CurTotalLend"));
				map.put("TotalLend", GlobalsTool.round(cur.multiply(rate ).doubleValue(),digits));
				cur = new BigDecimal(StringUtil.isEmpty((String)map.get("CurTotalRemain"))?"0": (String)map.get("CurTotalRemain"));
				map.put("TotalRemain", GlobalsTool.round(cur.multiply(rate ).doubleValue(),digits));			
			}

			String jdFlag = "1";
			String classSql = "select JdFlag from tblAccTypeInfo where accNumber='" + accNumber + "'";
			Statement classst = conn.createStatement();
			ResultSet classSet = classst.executeQuery(classSql);
			if (classSet.next()) {
				jdFlag = classSet.getString("JdFlag");
			}

			/* 计算余额 余额=年期初+本年累计借-本年累计贷 */
			String[] str = new String[] { "", "Cur" };
			for (String s : str) {
				String moneys = "0";
				Object o = map.get(s + "BeginAmount");
				if (o != null && !"".equals(o)) {
					moneys = o.toString();
				}
				if (map.get(s + "TotalDebit") != null && !"".equals(map.get(s + "TotalDebit"))) {
					if ("1".equals(jdFlag)) {
						moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s + "TotalDebit").toString())).toString();
					} else {
						moneys = new BigDecimal(moneys).subtract(new BigDecimal(map.get(s + "TotalDebit").toString())).toString();
					}
				}
				if (map.get(s + "TotalLend") != null && !"".equals(map.get(s + "TotalLend"))) {
					if ("1".equals(jdFlag)) {
						moneys = new BigDecimal(moneys).subtract(new BigDecimal(map.get(s + "TotalLend").toString())).toString();
					} else {
						moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s + "TotalLend").toString())).toString();
					}
				}
				if (isValue(map.get(s + "BeginAmount")) && isValue(map.get(s + "TotalDebit")) && isValue(map.get(s + "TotalLend"))) {
					map.put(s + "BeginAmount", map.get(s + "TotalRemain"));
				} else {
					map.put(s + "TotalRemain", moneys);
				}
			}
			/**
			 * 调用存储过程getCAccNumber处理会计科目
			 */
			StringBuffer procstr = new StringBuffer("{call getCAccNumer(@accCode=?,@companyCode=?,");
			procstr.append("@stockCode=?,@departmentCode=?,@employeeID=?,@projectCode=?,@CurrencyID=?,");
			procstr.append("@sunCompany=?,@retCode=?,@retVal=?)}");
			CallableStatement cs = conn.prepareCall(procstr.toString());

			String newAccNumber = accNumber;
			cs.setString(1, newAccNumber);
			cs.setString(2, String.valueOf(map.get("CompanyCode") == null ? "" : map.get("CompanyCode")));
			cs.setString(3, String.valueOf(map.get("StockCode") == null ? "" : map.get("StockCode")));
			cs.setString(4, String.valueOf(map.get("DepartmentCode") == null ? "" : map.get("DepartmentCode")));
			cs.setString(5, String.valueOf(map.get("EmployeeID") == null ? "" : map.get("EmployeeID")));
			cs.setString(6, String.valueOf(map.get("ProjectCode") == null ? "" : map.get("ProjectCode")));
			cs.setString(7, String.valueOf(map.get("Currency") == null ? "" : map.get("Currency")));
			cs.setString(8, lg.getSunCmpClassCode());
			cs.registerOutParameter(9, Types.INTEGER);
			cs.registerOutParameter(10, Types.VARCHAR);
			cs.execute();
			rs.setRetCode(cs.getInt(9));
			rs.setRetVal(cs.getString(10));
			if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != 2601) {
				BaseEnv.log.error("IniAccMgt updateIniAcc Info: " + cs.getString(10));
				rs.setRetCode(cs.getInt(9));
				rs.setRetVal(cs.getString(10));
				return rs;
			}

			String detId = IDGenerater.getId();
			map.put("accNumber", cs.getString(10));
			map.put("id", detId);

			StringBuffer sql = new StringBuffer();
			/**
			 * 查询期初是否有重复项
			 */
			sql = new StringBuffer("select count(0) from tblIniAccDet where accCode = ? and CompanyCode=? and DepartmentCode=? and EmployeeID=? and ProjectCode=? and StockCode=? ");
			PreparedStatement st = conn.prepareStatement(sql.toString());
			st.setString(1, (String) map.get("accNumber"));
			st.setString(2, (String) map.get("CompanyCode"));
			st.setString(3, (String) map.get("DepartmentCode"));
			st.setString(4, (String) map.get("EmployeeID"));
			st.setString(5, (String) map.get("ProjectCode"));
			st.setString(6, (String) map.get("StockCode"));
			ResultSet rsSame = st.executeQuery();
			if (rsSame.next()) {
				int count = rsSame.getInt(1);
				if (count > 0) {
					// 核算项重复
					rs.retCode = ErrorCanst.MULTI_VALUE_ERROR;
					rs.retVal = "同一核算项目对应记录只能一条";
					return rs;
				}
			}

			/**
			 * 往期初明细表tblIniAccDet中添加数据
			 */
			sql = new StringBuffer("insert into tblIniAccDet(id,accCode,SCompanyID,createTime,");
			String fields = "Currency,CurrencyRate,BeginAmount,TotalDebit,TotalLend,";
			fields += "TotalRemain,CompanyCode,DepartmentCode,EmployeeID,ProjectCode,StockCode,Remark,impId";
			
			fields += ",CurBeginAmount,CurTotalDebit,CurTotalLend,CurTotalRemain";
			sql.append(fields + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
			sql.append(",?,?,?,?");
			sql.append(")");
			
			PreparedStatement ps = conn.prepareStatement(sql.toString());

			/* 添加期初明细记录 */
			ps.setString(1, detId);
			ps.setString(2, cs.getString(10));
			ps.setString(3, lg.getSunCmpClassCode());
			ps.setString(4, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			String[] fieldStr = fields.split(",");
			for (int j = 0; j < fieldStr.length; j++) {
				Object value = map.get(fieldStr[j]);
				if ("CurrencyRate".equals(fieldStr[j]) || "BeginAmount".equals(fieldStr[j]) || "TotalDebit".equals(fieldStr[j]) || "TotalLend".equals(fieldStr[j]) || "TotalRemain".equals(fieldStr[j])
						|| "CurBeginAmount".equals(fieldStr[j]) || "CurTotalDebit".equals(fieldStr[j]) || "CurTotalLend".equals(fieldStr[j]) || "CurTotalRemain".equals(fieldStr[j])) {
					if (value == null || "".equals(value)) {
						value = 0;
					}else{
						java.text.NumberFormat nf =java.text.NumberFormat.getNumberInstance();
						if("CurrencyRate".equals(fieldStr[j])){
							//value=GlobalsTool.round(Double.parseDouble(value.toString()),digitsPrice);
							nf.setMaximumFractionDigits(digitsPrice);
						}else{
							//value=GlobalsTool.round(Double.parseDouble(value.toString()),digits);
							nf.setMaximumFractionDigits(digits);
						}
						nf.setMinimumFractionDigits(0); 
						nf.setGroupingUsed(false);
						value =  nf.format(Double.parseDouble(value.toString()));
					}
					map.put(fieldStr[j], value);
				}
				ps.setObject(j + 5, value == null ? "" : value);
			}
			ps.execute();

			/**
			 * 修改科目余额表金额
			 */
			result = updateAccBalanceIniItem(map, lg.getSunCmpClassCode(), conn);
			if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && result.getRetCode() != 2601) {
				rs.setRetCode(result.getRetCode());
				rs.setRetVal(result.getRetVal());
				return rs;
			}
			rs.setRetVal(result.getRetVal());
			// 调用define文件进行数据处理
			DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("IniAcc_Update");
			// 添加数据到map中为
			map.put("subCode", map.get("AccCode") == null ? "" : map.get("AccCode").toString());
			result = defineSqlBean.execute(conn, map, lg.getId(), resources, locale, "");
			if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && result.getRetCode() != 2601) {
				rs.setRetCode(result.getRetCode());
				rs.setRetVal(result.getRetVal());
				return rs;
			}

		} catch (Exception ex) {
			BaseEnv.log.error("IniAccMgt.doAddIniAccItem ",ex);
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			rs.setRetVal(ex.getMessage());
			return rs;
		}
		return rs;
	}
	
	/**
	 * 册除核算期初项
	 * 
	 * @param map
	 *            期初项
	 * @param keyid
	 * @param classCode
	 * @param sunClassCode
	 * @param accNumber
	 * @param openFCurrency
	 * @return
	 */
	public Result delImport(final String accNumber,final String impId,final LoginBean lg,final Locale locale,
			final MessageResources resources) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn)
							throws SQLException {
						try {
							String sql = "select id from tbliniaccdet where impId=? ";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1,impId);
							ResultSet res = ps.executeQuery();
							ArrayList<String> ids= new ArrayList<String>();
							while(res.next()){
								ids.add(res.getString(1));
							}
							rs.retVal = ids;
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			                rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}
		ArrayList<String> ids=(ArrayList<String>)rs.retVal;
		final String[] keyId = ids.toArray(new String[]{});
		
		retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
							Result res = doDelIniAccItem(keyId,  accNumber,  lg, locale, resources, connection);
							rs.retCode = res.retCode;
							rs.retVal = res.retVal;
							if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
								return ;
							}
							try {
								String sql = "delete  from tblIniAccImport where impId=? ";
								PreparedStatement ps = connection.prepareStatement(sql);
								ps.setString(1,impId);
								ps.execute();								
							} catch (Exception ex) {
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
				                rs.setRetVal(ex.getMessage());
								return;
							}
							
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 册除核算期初项
	 * 
	 * @param map
	 *            期初项
	 * @param keyid
	 * @param classCode
	 * @param sunClassCode
	 * @param accNumber
	 * @param openFCurrency
	 * @return
	 */
	public Result delIniAccItem(final String[] list, final String accNumber,final LoginBean lg,final Locale locale,
			final MessageResources resources) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
							Result res = doDelIniAccItem(list,  accNumber,  lg, locale, resources, connection);
							rs.retCode = res.retCode;
							rs.retVal = res.retVal;
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}	
	/**
	 * 册除核算期初项
	 * @param map 期初项
	 * @param keyid
	 * @param classCode
	 * @param sunClassCode
	 * @param accNumber
	 * @param openFCurrency
	 * @return
	 */
	public Result doDelIniAccItem(final String[] list, final String accNumber,final LoginBean lg,final Locale locale,
			final MessageResources resources,Connection conn) {
		final Result rs = new Result();
		try { 
			for(String keyId:list){
				Result result;
				
				/**
				 * 查往期初明细表tblIniAccDet中数据
				 */
				StringBuffer sql = new StringBuffer("select  id,AccCode,SCompanyID,createTime,Currency,CurrencyRate," +
						"BeginAmount,TotalDebit,TotalLend, " +
						"TotalRemain,CompanyCode,DepartmentCode,EmployeeID,ProjectCode,StockCode,Remark," +
						"CurBeginAmount,CurTotalDebit,CurTotalLend,CurTotalRemain from tblIniAccDet where id=?");
				PreparedStatement ps = conn.prepareStatement(sql.toString());
				ps.setString(1, keyId);
				ResultSet rset = ps.executeQuery();
				HashMap map=new HashMap();
				if(rset.next()){
                	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
                		Object obj=rset.getObject(i);
                		String name = rset.getMetaData().getColumnName(i);
                		if(obj==null){
                			map.put(name, "");
                		}else{
                			if(rset.getMetaData().getColumnType(i) == Types.NUMERIC){
            					String strvalue = String.valueOf(obj);
            					
            					if(Double.valueOf(strvalue)==0 || "0E-8".equals(strvalue)){
                					strvalue = "0";
            					}
            					if(name.equals("BeginAmount")||name.equals("TotalDebit")||name.equals("TotalLend")||name.equals("TotalRemain")
            							||name.equals("CurBeginAmount")||name.equals("CurTotalDebit")||name.equals("CurTotalLend")||name.equals("CurTotalRemain")){
            						if(strvalue.startsWith("-")){ //原来是负数的变正数
            							strvalue = strvalue.substring(1);
            						}else{
            							strvalue = "-"+strvalue;
            						}
            					}
                				map.put(name, strvalue);
                			}else{
                				if("0E-8".equals(obj)){
                					obj = "";
                				}
                				map.put(name, obj);
                			}
                		}
                	}
				}
				map.put("accNumber", map.get("AccCode"));
				String paraccCode = (String)map.get("AccCode");
				paraccCode = paraccCode.substring(0,paraccCode.length() -5);
				/**
				 * 修改科目余额表金额
				 */
				result = updateAccBalanceIniItem(map, lg.getSunCmpClassCode(), conn);
				if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && result.getRetCode() != 2601) {  
	                rs.setRetCode(result.getRetCode());
                    rs.setRetVal(result.getRetVal());
	                return rs;
	            }
				rs.setRetVal(result.getRetVal());
				//删除记录
				sql = new StringBuffer("delete tblIniAccDet where AccCode=?");
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, (String)map.get("AccCode"));
				ps.executeUpdate();
//				sql = new StringBuffer("delete tblAccBalance from tblAccBalance a join tblAccTypeInfo b on a.SubCode=b.AccName  " +
//						"where a.SubCode=? and a.Period=-1  and a.Nyear=-1 and a.SCompanyID=?  and isnull(a.CurType,'')=? and (case b.isForCur when 1 then isnull(b.Currency,'') else '' end) = '' ");
//				ps = conn.prepareStatement(sql.toString());
//				ps.setString(1, (String)map.get("AccCode"));
//				ps.setString(2, sunClassCode);
//				ps.setString(3, (String)map.get("Currency"));
//				ps.executeUpdate();
				
				//调用define文件进行数据处理
				DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("IniAcc_Update");							
				//添加数据到map中为							
				map.put("subCode", paraccCode);
				result = defineSqlBean.execute(conn, map, lg.getId(),resources,locale,"");
				if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && result.getRetCode() != 2601) {  
	                rs.setRetCode(result.getRetCode());
                    rs.setRetVal(result.getRetVal());
	                return rs;
	            }				
			}
			
		} catch (Exception ex) {
			BaseEnv.log.error("IniAccMgt.doDelIniAccIem",ex);
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            rs.setRetVal(ex.getMessage());
			return rs;
		}
		return rs;
	}
	
	/**
	 * 修改期初值(废止)
	 * @param list
	 * @param keyid
	 * @param classCode
	 * @param sunClassCode
	 * @param accNumber
	 * @param openFCurrency
	 * @return
	 */
	public Result updateIniAcc(final List list, final String keyid, final String classCode,final String accNumber,
			final String sunClassCode,final String openFCurrency,final LoginBean lg,final Locale locale,
			final MessageResources resources,final String type) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							boolean opencurr = false;
							if("true".equals(openFCurrency)){
								//启用外币
								opencurr = true;
							}
							Result result;
							Statement st= conn.createStatement();
							StringBuffer sql = new StringBuffer();
							
							if(type != null && "update".equals(type)){
								/**
								 * 删除期初明细表旧的数据
								 */
								sql = new StringBuffer("delete tblIniAccDet where accCode like '"+accNumber+"%'");
								st.executeUpdate(sql.toString());
								
								/**
								 * 删除科目余额表旧的数据
								 */
								sql = new StringBuffer("delete tblAccBalance where SubCode like '"+accNumber+"%' and SubCode != '"+accNumber+"'");
								st.executeUpdate(sql.toString());
								
								/**
								 *  查询科目的子项，如果当前科目开启了核算修改时把下级所有科目金额清0处理
								 */
								sql = new StringBuffer("update tblAccBalance set CurrYIniBase=0,CurrYIniDebitSumBase=0,CurrYIniCreditSumBase=0,");
								sql.append("CurrYIniBalaBase=0,CurrYIni=0,CurrYIniDebitSum=0,CurrYIniCreditSum=0,CurrYIniBala=0 where SubCode='"+accNumber+"'");
								sql.append(" and Period='-1'  and Nyear='-1' ");
								st.executeUpdate(sql.toString());
							}
							
							/**
							 * 往期初明细表tblIniAccDet中添加数据
							 */
							sql = new StringBuffer("insert into tblIniAccDet(id,accCode,SCompanyID,createTime,");
							String fields = "Currency,CurrencyRate,BeginAmount,TotalDebit,TotalLend,";
							fields += "TotalRemain,CompanyCode,DepartmentCode,EmployeeID,ProjectCode,StockCode,Remark";
							if(opencurr){
								//启用外币
								fields += ",CurBeginAmount,CurTotalDebit,CurTotalLend,CurTotalRemain";
							}
							sql.append(fields + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
							if(opencurr){
								sql.append(",?,?,?,?");
							}
							sql.append(")");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							
							/**
							 * 调用存储过程getCAccNumber处理会计科目
							 */
							StringBuffer procstr = new StringBuffer("{call getCAccNumer(@accCode=?,@companyCode=?,");
							procstr.append("@stockCode=?,@departmentCode=?,@employeeID=?,@projectCode=?,@CurrencyID=?,");
							procstr.append("@sunCompany=?,@retCode=?,@retVal=?)}");
							CallableStatement cs = conn.prepareCall(procstr.toString()) ;
							
							for(int i=0;i<list.size();i++){
								HashMap map = (HashMap)list.get(i);
								String newAccNumber = accNumber;
								if(type != null && "import".equals(type)){
									Object accCode = map.get("AccCode");
									if(accCode == null || "".equals(accCode)){
										continue;
									}
									newAccNumber = accCode.toString();
								}
								cs.setString(1, newAccNumber);
								
								String jdFlag = "1";
								String classSql = "select JdFlag from tblAccTypeInfo where accNumber='"+newAccNumber+"'";
								Statement classst = conn.createStatement();
								ResultSet classSet = classst.executeQuery(classSql);
								if(classSet.next()){
									jdFlag = classSet.getString("JdFlag");
								}
								
								/* 计算余额  余额=年期初+本年累计借-本年累计贷 */
								String[] str = new String[]{"","Cur"};
								for(String s : str){
									String moneys = "0";
									Object o = map.get(s+"BeginAmount");
									if(o!=null && !"".equals(o)){
										moneys = o.toString();
									}
									if(map.get(s+"TotalDebit")!=null && !"".equals(map.get(s+"TotalDebit"))){
										if("1".equals(jdFlag)){
											moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s+"TotalDebit").toString())).toString();												
										}else{
											moneys = new BigDecimal(moneys).subtract(new BigDecimal(map.get(s+"TotalDebit").toString())).toString();
										}
									}
									if(map.get(s+"TotalLend")!=null && !"".equals(map.get(s+"TotalLend"))){
										if("1".equals(jdFlag)){
											moneys = new BigDecimal(moneys).subtract(new BigDecimal(map.get(s+"TotalLend").toString())).toString();
										}else{
											moneys = new BigDecimal(moneys).add(new BigDecimal(map.get(s+"TotalLend").toString())).toString();
										}
									}
									if(isValue(map.get(s+"BeginAmount")) && isValue(map.get(s+"TotalDebit")) && isValue(map.get(s+"TotalLend"))){
										map.put(s+"BeginAmount", map.get(s+"TotalRemain"));
									}else{
										map.put(s+"TotalRemain", moneys);
									}
								}
								
								cs.setString(2, String.valueOf(map.get("CompanyCode")==null ? "" : map.get("CompanyCode")));
								cs.setString(3, String.valueOf(map.get("StockCode")==null ? "" : map.get("StockCode")));
								cs.setString(4, String.valueOf(map.get("DepartmentCode")==null ? "" : map.get("DepartmentCode")));
								cs.setString(5, String.valueOf(map.get("EmployeeID")==null ? "" : map.get("EmployeeID")));
								cs.setString(6, String.valueOf(map.get("ProjectCode")==null ? "" : map.get("ProjectCode")));
								cs.setString(7, String.valueOf(map.get("Currency")==null ? "" : map.get("Currency")));
								cs.setString(8, sunClassCode);
								cs.registerOutParameter(9, Types.INTEGER);
								cs.registerOutParameter(10, Types.VARCHAR);
								cs.execute();
								rs.setRetCode(cs.getInt(9));
								rs.setRetVal(cs.getString(10));
								if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != 2601) {  
									BaseEnv.log.debug("IniAccMgt updateIniAcc Info: " + cs.getString(10));
			                        rs.setRetCode(cs.getInt(9));
			                        rs.setRetVal(cs.getString(10));
			                        return;
			                    }
								
								String detId = IDGenerater.getId();
								map.put("accNumber", cs.getString(10));
								map.put("id", detId);
								/* 添加期初明细记录 */
								ps.setString(1, detId);
								ps.setString(2, cs.getString(10));
								ps.setString(3, sunClassCode);
								ps.setString(4, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
								String[] fieldStr = fields.split(",");
								for(int j=0;j<fieldStr.length;j++){
									Object value = map.get(fieldStr[j]);
									if("CurrencyRate".equals(fieldStr[j]) || "BeginAmount".equals(fieldStr[j]) || "TotalDebit".equals(fieldStr[j]) ||
											"TotalLend".equals(fieldStr[j]) || "TotalRemain".equals(fieldStr[j]) || "CurBeginAmount".equals(fieldStr[j]) ||
											"CurTotalDebit".equals(fieldStr[j]) || "CurTotalLend".equals(fieldStr[j]) || "CurTotalRemain".equals(fieldStr[j])){
										if(value == null || "".equals(value)){
											value = 0;
										}
									}
									ps.setObject(j+5, value==null?"":value);
								}
								ps.addBatch();
							}
							ps.executeBatch();
							
							/**
							 * 修改科目余额表金额
							 */
							result = updateAccBalanceIni(list, sunClassCode, conn);
							if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && result.getRetCode() != 2601) {  
				                rs.setRetCode(result.getRetCode());
		                        rs.setRetVal(result.getRetVal());
				                return;
				            }
							
							//调用define文件进行数据处理
							DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("IniAcc_Update");
							if(type != null && "import".equals(type)){
								for(int i=0;i<list.size();i++){
									HashMap map = (HashMap)list.get(i);
									//添加数据到map中为
									HashMap<String,String> hashmap = new HashMap<String,String>();
									hashmap.put("subCode", map.get("AccCode")==null?"":map.get("AccCode").toString());
									result = defineSqlBean.execute(conn, hashmap, lg.getId(),resources,locale,"");
									if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && result.getRetCode() != 2601) {  
						                rs.setRetCode(result.getRetCode());
				                        rs.setRetVal(result.getRetVal());
						                return;
						            }
								}
								rs.setRetVal(result.getRetVal());
							}else{
								HashMap<String,String> hashmap = new HashMap<String,String>();
								hashmap.put("subCode", accNumber);
								result = defineSqlBean.execute(conn, hashmap, lg.getId(),resources,locale,"");
								if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && result.getRetCode() != 2601) {  
					                rs.setRetCode(result.getRetCode());
					                rs.setRetVal(result.getRetVal());
					                return;
					            }
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			                rs.setRetVal(ex.getMessage());
			                BaseEnv.log.error("IniAccMgt.updateIniAcc Error:",ex);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	public boolean isValue(Object o){
		boolean isYes = false;
		if(o == null || "".equals(o) || Float.valueOf(o.toString())==0){
			isYes = true;
		}
		return isYes;
	}
	
	/**
	 * 调用存储过程proc_updateAccBalanceIni进行处理期初金额 (无核算的期初修改在调这个方法)
	 * @param list
	 * @param conn
	 * @return
	 */
	public Result updateAccBalanceIni(List list,String sunClassCode,Connection conn) throws Exception {
		Result rs = new Result();
		
		String[] moneyStr = new String[]{"CurrencyRate","BeginAmount","TotalDebit","TotalLend","TotalRemain","CurBeginAmount","CurTotalDebit","CurTotalLend","CurTotalRemain"};
		/* 执行存储过程 */
		StringBuffer procstr = new StringBuffer("{call proc_updateAccBalanceIni(@accNumber=?,@period=?,@Nyear=?,");
		procstr.append("@Nmonth=?,@currency=?,@curRate=?,@CurrYIniBase=?,@CurrYIniDebitSumBase=?,");
		procstr.append("@CurrYIniCreditSumBase=?,@CurrYIniBalaBase=?,@CurrYIni=?,@CurrYIniDebitSum=?,");
		procstr.append("@CurrYIniCreditSum=?,@CurrYIniBala=?,@DepartmentCode=?,@sunCompany=?,@retCode=?,@retVal=?)}");
		CallableStatement cs = conn.prepareCall(procstr.toString()) ;
		for(int i=0;i<list.size();i++){
			HashMap map = (HashMap)list.get(i);
			cs.setString(1, String.valueOf(map.get("accNumber")));
			cs.setInt(2, -1);
			cs.setInt(3, -1);
			cs.setInt(4, -1);
			cs.setString(5, String.valueOf(map.get("Currency")==null ? "" : map.get("Currency")));
			for(int j=0;j<moneyStr.length;j++){
				String value = "";
				if(map.get(moneyStr[j]) == null || "".equals(map.get(moneyStr[j]))){
					value = "0";
				}else{
					value = String.valueOf(map.get(moneyStr[j]));
				}
				cs.setString(6+j, value);
			}
			int lastNum = moneyStr.length+6;
			cs.setString(lastNum, "");
			cs.setString(lastNum+1, sunClassCode);
			cs.registerOutParameter(lastNum+2, Types.INTEGER);
			cs.registerOutParameter(lastNum+3, Types.VARCHAR);
			cs.execute();
			rs.setRetCode(cs.getInt(lastNum+2));
			rs.setRetVal(cs.getString(lastNum+3));
			if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != 2601) {  
				BaseEnv.log.debug("IniAccMgt updateAccBalanceIni Info: " + cs.getString(lastNum+3));
                rs.setRetCode(cs.getInt(lastNum+2));
                rs.setRetVal(cs.getString(lastNum+3));
                return rs;
            }
		}
		return rs;
	}
	/**
	 * 记录导入历史记录
	 * @param impId
	 * @param lb
	 * @return
	 */
	public Result addImportRecord(final String impId,final String accNumber,final LoginBean lb){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn)
							throws SQLException {
						try {
							String sql = "insert into tblIniAccImport(impId,userId,userName,createTime,accNumber) values(?,?,?,?,?)";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1,impId);
							ps.setString(2,lb.getId());
							ps.setString(3,lb.getEmpFullName());
							ps.setString(4,BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
							ps.setString(5,accNumber);
							ps.execute();
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			                rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 调用存储过程proc_updateAccBalanceIni进行处理期初金额
	 * @param list
	 * @param conn
	 * @return
	 */
	public Result updateAccBalanceIniItem(Map map,String sunClassCode,Connection conn) throws Exception {
		Result rs = new Result();
		
		String[] moneyStr = new String[]{"CurrencyRate","BeginAmount","TotalDebit","TotalLend","TotalRemain","CurBeginAmount","CurTotalDebit","CurTotalLend","CurTotalRemain"};
		/* 执行存储过程 */
		StringBuffer procstr = new StringBuffer("{call proc_updateAccBalanceIni(@accNumber=?,@period=?,@Nyear=?,");
		procstr.append("@Nmonth=?,@currency=?,@curRate=?,@CurrYIniBase=?,@CurrYIniDebitSumBase=?,");
		procstr.append("@CurrYIniCreditSumBase=?,@CurrYIniBalaBase=?,@CurrYIni=?,@CurrYIniDebitSum=?,");
		procstr.append("@CurrYIniCreditSum=?,@CurrYIniBala=?,@DepartmentCode=?,@sunCompany=?,@retCode=?,@retVal=?)}");
		CallableStatement cs = conn.prepareCall(procstr.toString()) ;
		cs.setString(1, String.valueOf(map.get("accNumber")));
		cs.setInt(2, -1);
		cs.setInt(3, -1);
		cs.setInt(4, -1);
		
		BaseEnv.log.debug("IniAccMgt updateAccBalanceIni : " + procstr.toString());
		BaseEnv.log.debug("IniAccMgt updateAccBalanceIni : 参数：" + String.valueOf(map.get("accNumber")));
		BaseEnv.log.debug("IniAccMgt updateAccBalanceIni : 参数：" + "-1");
		BaseEnv.log.debug("IniAccMgt updateAccBalanceIni : 参数：" + "-1");
		BaseEnv.log.debug("IniAccMgt updateAccBalanceIni : 参数：" + "-1");		
		cs.setString(5, String.valueOf(map.get("Currency")==null ? "" : map.get("Currency")));
		BaseEnv.log.debug("IniAccMgt updateAccBalanceIni : 参数：" + String.valueOf(map.get("Currency")==null ? "" : map.get("Currency")));
		for(int j=0;j<moneyStr.length;j++){
			String value = "";
			if(map.get(moneyStr[j]) == null || "".equals(map.get(moneyStr[j]))){
				value = "0";
			}else{
				value = String.valueOf(map.get(moneyStr[j]));
			}
			cs.setString(6+j, value);
			BaseEnv.log.debug("IniAccMgt updateAccBalanceIni : 参数：" + value);
		}
		int lastNum = moneyStr.length+6;
		cs.setString(lastNum, "");
		cs.setString(lastNum+1, sunClassCode);
		BaseEnv.log.debug("IniAccMgt updateAccBalanceIni : 参数：" + "");
		BaseEnv.log.debug("IniAccMgt updateAccBalanceIni : 参数：" + sunClassCode);
		cs.registerOutParameter(lastNum+2, Types.INTEGER);
		cs.registerOutParameter(lastNum+3, Types.VARCHAR);
		cs.execute();
		rs.setRetCode(cs.getInt(lastNum+2));
		rs.setRetVal(cs.getString(lastNum+3));
		if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != 2601) {  
			BaseEnv.log.debug("IniAccMgt updateAccBalanceIni Info: " + cs.getString(lastNum+3));
            rs.setRetCode(cs.getInt(lastNum+2));
            rs.setRetVal(cs.getString(lastNum+3));
            return rs;
        }
		return rs;
	}
	
	/**
	 * 查询应收账款，应付账款科目代码和科目名称
	 * @param local        多语言
	 * @return
	 */
	public Result getCalName(final String accNumber,final String fieldName,final String moduleType,final String fieldVal,final String locale){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						
						try {						
							String sql = "";
							if(fieldName.equals("DepartmentCode")){
								sql = "select classCode from tblDepartment where deptFullName = ?";
							}else if(fieldName.equals("EmployeeID")){
								sql = "select id from tblEmployee where empFullName = ?";
							}else if(fieldName.equals("StockCode")){
								sql = "select classCode from tblStock where stockFullName = ?";
							}else if(fieldName.equals("ProjectCode")){
								sql = "select a.id from tblProject a join tblLanguage b on a.ProjectName=b.id where "+locale+" = ?";
							}else if(fieldName.equals("Currency")){
								sql = "select id,IsBaseCurrency from tblCurrency where CurrencyName =? ";
							}else if(fieldName.equals("CompanyCode") && moduleType.equals("Customer")){
								sql = "select classCode from TblCompany where comFullName  = ? and moduleType in (2,3)";
							}else if(fieldName.equals("CompanyCode") && moduleType.equals("Supplier")){
								sql = "select classCode from TblCompany where comFullName  = ? and moduleType in (1,3)";
							}							
							PreparedStatement st = conn.prepareStatement(sql.toString());
							st.setString(1, fieldVal);
							ResultSet rset = st.executeQuery();
							if(rset.next()){
								rs.retVal = rset.getString(1);
								if(fieldName.equals("Currency")){
									//外币要检查是否是本位币
									if(rset.getString(2).equals("1")){
										rs.retVal="";
									}
								}
							}else{
								rs.retCode = ErrorCanst.ER_NO_DATA;
								rs.retVal="数据不正确";
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 查询应收账款，应付账款科目代码和科目名称
	 * @param local        多语言
	 * @return
	 */
	public Result queryAccCode(final String local){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn)
							throws SQLException {
						try {
							String sql = "select tblAccTypeInfo.AccNumber as accCode,tblLanguage."+local+" as AccFullName from tblAccTypeInfo left join tblLanguage ";
							sql += "on tblLanguage.id=tblAccTypeInfo.AccFullName where AccNumber in ('1122','2202')";
							PreparedStatement ps = conn.prepareStatement(sql);
							ResultSet rset = ps.executeQuery();
							List valueList = new ArrayList();
							while(rset.next()){
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rset.getMetaData().getColumnCount();i++){
	                        		Object obj=rset.getObject(i);
	                        		if(obj==null){
	                        			map.put(rset.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			map.put(rset.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
								valueList.add(map);
							}
							rs.setRetVal(valueList);
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			                rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	
	
	/**
	 * 往来期初处理
	 * @param conn
	 * @param accNumber
	 * @param list
	 * @param sunClassCode
	 * @param lg
	 * @return
	 */
	public Result addCompanybeginning(Connection conn,String accNumber,List list,String sunClassCode,LoginBean lg)
			throws Exception{
		Result rs = new Result();
		String time = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
		for(int i=0;i<list.size();i++){
			HashMap map = (HashMap)list.get(i);
			StringBuffer sql = new StringBuffer("select count(0) as count from tblcompanyini where tblCompanyini.period=-1 and CompanyCode='"+map.get("CompanyCode")+"'");
			Statement st = conn.createStatement();
			ResultSet rset = st.executeQuery(sql.toString());
			int count = 0;
			if(rset.next()){
				count = rset.getInt("count");
			}
			//当不存在往来记录时，增加一条记录
			if(count == 0){
				sql = new StringBuffer("insert into tblCompanyIni(id,CompanyCode,Period,PeriodYear,PeriodMonth,BillDate,");
				sql.append("BillType,ReceiveBegin,ReceiveTotalDebit,ReceiveTotalLend,ReceiveTotalRemain,PreReceiveBegin,");
				sql.append("PreReceiveTotalDebit,PreReceiveTotalLend,PreReceiveTotalRemain,PayBegin,PayTotalDebit,PayTotalLend,");
				sql.append("PayTotalRemain,PrePayBegin,PrePayTotalDebit,PrePayTotalLend,PrePayTotalRemain,Currency,CurrencyRate,");
				sql.append("FcRecBegin,FcRecTotalDebit,FcRecTotalCredit,FcRecTotalRemain,FcPreRecBegin,FcPreRecTotalDebit,");
				sql.append("FcPreRecTotalCredit,FcPreRecTotalRemain,FcPayBegin,FcPayTotalDebit,FcPayTotalCredit,FcPayTotalRemain,");
				sql.append("FcPrePayBegin,FcPrePayTotalDebit,FcPrePayTotalCredit,FcPrePayTotalRemain,createBy,lastUpdateBy,");
				sql.append("createTime,lastUpdateTime,SCompanyID,ItemNo) values('"+map.get("id")+"','"+map.get("CompanyCode")+"',-1,-1,-1,-1,-1,0,0,0,0,");
				sql.append("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,'"+lg.getId()+"','"+lg.getId()+"','"+time+"'");
				sql.append(",'"+time+"','"+sunClassCode+"',1)");
				st.executeUpdate(sql.toString());
			}
			
			String strValue = "a.BeginAmount1;a.TotalDebit1;a.TotalLend1;a.TotalRemain1;a.CurBeginAmount1;a.CurTotalDebit1;a.CurTotalLend1;a.CurTotalRemain1";
			String str = "";
			int numberType = 0;
			if("2202".equals(accNumber)){
				//应付账款
				str = "PayBegin;PayTotalDebit;PayTotalLend;PayTotalRemain;FcPayBegin;FcPayTotalDebit;FcPayTotalCredit;FcPayTotalRemain";
				numberType = 1;
			}else if("1122".equals(accNumber)){
				//应收账款
				str = "ReceiveBegin;ReceiveTotalDebit;ReceiveTotalLend;ReceiveTotalRemain;FcRecBegin;FcRecTotalDebit;FcRecTotalCredit;FcRecTotalRemain";
				numberType = 2;
			}else if("1123".equals(accNumber)){
				//预付账款
				str= "PrePayBegin;PrePayTotalDebit;PrePayTotalLend;PrePayTotalRemain;FcPrePayBegin;FcPrePayTotalDebit;FcPrePayTotalCredit;FcPrePayTotalRemain";
				numberType = 3;
			}else if("2203".equals(accNumber)){
				//预收账款
				str= "PreReceiveBegin;PreReceiveTotalDebit;PreReceiveTotalLend;PreReceiveTotalRemain;FcPreRecBegin;FcPreRecTotalDebit;FcPreRecTotalCredit;FcPreRecTotalRemain";
				numberType = 4;
			}
			sql = new StringBuffer("update tblCompanyIni set ");
			for(int j=0;j<strValue.split(";").length;j++){
				sql.append(" "+str.split(";")[j]+" = "+strValue.split(";")[j]+",");
			}
			sql.append("RelationDocID='"+map.get("id")+"',Currency=a.Currency1,CurrencyRate=a.CurrencyRate1,");
			sql.append("createBy='"+lg.getId()+"',createTime='"+time+"',lastUpdateBy='"+lg.getId()+"',lastUpdateTime='"+time+"',");
			sql.append("SCompanyID='"+sunClassCode+"',BillType='tblIniAccDet' ");
			sql.append("from (select CompanyCode as CompanyCode1,BeginAmount as BeginAmount1,TotalDebit as TotalDebit1,TotalLend as TotalLend1,");
			sql.append("TotalRemain  as TotalRemain1,CurBeginAmount as CurBeginAmount1,CurTotalDebit as CurTotalDebit1,CurTotalLend as CurTotalLend1,");
			sql.append("CurTotalRemain as CurTotalRemain1,isnull(Currency,'') as Currency1,CurrencyRate as CurrencyRate1 from ");
			sql.append("tblIniAccDet where id='"+map.get("id")+"' ) a where CompanyCode=a.CompanyCode1 and Period=-1");
			sql.append("and PeriodYear=-1 and PeriodMonth=-1");
			
			//更加不同的账款修改往来金额
			st.executeUpdate(sql.toString());
			
			//执行存储过程proc_addComBalanceDefine更新期初以后的往来明细数据
			CallableStatement cs = conn.prepareCall("{call proc_addComBalanceDefine(@id=?,@Ini_DocType=?,@BillDate=?,@CompanyCode=?,@retCode=?,@retVal=?)}") ;
			cs.setString(1, String.valueOf(map.get("id")));
			cs.setInt(2, numberType);
			cs.setString(3, "-1");
			cs.setString(4, String.valueOf(map.get("CompanyCode")));
			cs.registerOutParameter(5, Types.INTEGER);
			cs.registerOutParameter(6, Types.VARCHAR);
			cs.execute();
			rs.setRetCode(cs.getInt(5));
			rs.setRetVal(cs.getString(6));
			if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != 2601) {  
				BaseEnv.log.debug("IniAccMgt dealCompanybeginning Info: " + cs.getString(5));
                rs.setRetCode(cs.getInt(5));
                rs.setRetVal(cs.getString(6));
                return rs;
            }
			
			//执行存储过程proc_updateCompanyTotalDefine更新往来汇总
//			cs = conn.prepareCall("{call proc_updateCompanyTotalDefine(@id=?,@Type=?,@operation=?,@retCode=?,@retValue=?)}") ;
//			cs.setString(1, String.valueOf(map.get("id")));
//			cs.setString(2, String.valueOf(numberType));
//			cs.setString(3, "add");
//			cs.registerOutParameter(4, Types.INTEGER);
//			cs.registerOutParameter(5, Types.VARCHAR);
//			cs.execute();
//			rs.setRetCode(cs.getInt(4));
//			rs.setRetVal(cs.getString(5));
//			if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != 2601) {  
//				BaseEnv.log.debug("IniAccMgt addCompanybeginning Info: " + cs.getString(4));
//                rs.setRetCode(cs.getInt(4));
//                rs.setRetVal(cs.getString(5));
//                return rs;
//            }
		}
		return rs;
	}
	
	
	/**
	 * 往来进行反处理
	 * @param conn
	 * @param accNumber
	 * @param list
	 * @param sunClassCode
	 * @param lg
	 * @return
	 * @throws Exception
	 */
	public Result delCompanybeginning(Connection conn,String accNumber,List list,String sunClassCode,LoginBean lg)
			throws Exception{
		
		Result rs = new Result();
		StringBuffer sql = new StringBuffer("select id from tblIniAccDet where tblIniAccDet.accCode like '"+accNumber+"%'");
		Statement st = conn.createStatement();
		ResultSet rset = st.executeQuery(sql.toString());
		List isList = new ArrayList();
		while(rset.next()){
			isList.add(rset.getString("id"));
		}
		int numberType = 0;
		String str = "";
		if("2202".equals(accNumber)){
			//应付账款
			str = "PayBegin;PayTotalDebit;PayTotalLend;PayTotalRemain;FcPayBegin;FcPayTotalDebit;FcPayTotalCredit;FcPayTotalRemain;FcPrePayBegin";
			numberType = 1;
		}else if("1122".equals(accNumber)){
			//应收账款
			str = "ReceiveBegin;ReceiveTotalDebit;ReceiveTotalLend;ReceiveTotalRemain;FcRecBegin;FcRecTotalDebit;FcRecTotalCredit;FcRecTotalRemain";
			numberType = 2;
		}else if("1123".equals(accNumber)){
			//预付账款
			str= "PrePayBegin;PrePayTotalDebit;PrePayTotalLend;PrePayTotalRemain;FcPrePayBegin;FcPrePayTotalDebit;FcPrePayTotalCredit;FcPrePayTotalRemain";
			numberType = 3;
		}else if("2203".equals(accNumber)){
			//预收账款
			str= "PreReceiveBegin;PreReceiveTotalDebit;PreReceiveTotalLend;PreReceiveTotalRemain;FcPreRecBegin;FcPreRecTotalDebit;FcPreRecTotalCredit;FcPreRecTotalRemain";
			numberType = 4;
		}
		CallableStatement cs = null;
		for(int i=0;i<isList.size();i++){
			//执行存储过程proc_updateCompanyTotalDefine更新往来汇总
			cs = conn.prepareCall("{call proc_updateCompanyTotalDefine(@id=?,@Type=?,@operation=?,@retCode=?,@retValue=?)}") ;
			cs.setString(1, String.valueOf(isList.get(i)));
			cs.setString(2, String.valueOf(numberType));
			cs.setString(3, "del");
			cs.registerOutParameter(4, Types.INTEGER);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.execute();
			rs.setRetCode(cs.getInt(4));
			rs.setRetVal(cs.getString(5));
			if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != 2601) {  
				BaseEnv.log.debug("IniAccMgt delCompanybeginning Info: " + cs.getString(4));
	            rs.setRetCode(cs.getInt(4));
	            rs.setRetVal(cs.getString(5));
	            return rs;
	        }
			
			//执行存储过程proc_deleteComIniUbDefine更新期初以后的往来明细数据
			cs = conn.prepareCall("{call proc_deleteComIniUbDefine(@id=?,@Ini_DocType=?,@retCode=?,@retVal=?)}");
			cs.setString(1, String.valueOf(isList.get(i)));
			cs.setInt(2, numberType);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			rs.setRetCode(cs.getInt(3));
			rs.setRetVal(cs.getString(4));
			if (rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && rs.getRetCode() != 2601) {  
				BaseEnv.log.debug("IniAccMgt delCompanybeginning Info: " + cs.getString(3));
	            rs.setRetCode(cs.getInt(3));
	            rs.setRetVal(cs.getString(4));
	            return rs;
	        }
			
			//修改往来金额
			sql = new StringBuffer("update tblCompanyIni set ");
			for(int j=0;j<str.split(";").length;j++){
				sql.append(" "+str.split(";")[j]+" = 0");
				if(j != str.split(";").length-1){
					sql.append(",");
				}
			}
			sql.append(" from tblcompanyini where Period=-1 and PeriodYear=-1 and id='"+String.valueOf(isList.get(i))+"'");
			st.executeUpdate(sql.toString());
		}
		return rs;
	}
}
