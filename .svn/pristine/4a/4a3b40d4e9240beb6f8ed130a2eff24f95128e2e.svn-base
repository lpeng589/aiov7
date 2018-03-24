package com.menyi.aio.web.stockcheck;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.GoodsPropInfoBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.dyndb.SaveErrorObj;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.*;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.web.util.*;

/**
 * 
 * <p>Title:盘点mgt</p> 
 * <p>Description: </p>
 *
 * @Date:2013-10-17
 * @Copyright: 科荣软件
 * @Author fjj
 */
public class StockCheckMgt extends AIODBManager{

	/**
	 * 查询所有的仓库
	 * @return
	 */
	public Result queryAllStock(final LoginBean lg,final MOperation mop){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer sql = new StringBuffer("select id,classCode,StockNumber,StockName,StockFullName from tblStock where isCatalog=1 ");
							/*设置范围权限*/
	        				ArrayList scopeRight = new ArrayList();
	        				scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
	        				scopeRight.addAll(lg.getAllScopeRight());
	                		sql = new StringBuffer(DynDBManager.scopeRightHandler("tblStock", "TABLELIST", "", lg.getId(), scopeRight, sql.toString(), null,""));
	            			
	                		sql.append(" order by classCode ");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							List stockList = new ArrayList();
							while(rs.next()){
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
	                        		Object obj=rs.getObject(i);
	                        		if(obj==null){
	                        			map.put(rs.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			map.put(rs.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
	                        	stockList.add(map);
							}
							result.retVal = stockList;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("StockCheckMgt queryAllStock:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 根据条件查询指定的仓库数据
	 * @param showType
	 * @param searchType
	 * @param searchValue
	 * @return
	 */
	public Result queryStock(final String searchType, final String searchValue, final LoginBean lg,
			final MOperation mop){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							/**
							 * 查询仓库
							 */
							StringBuffer sql = new StringBuffer("select tblStock.id,tblStock.classCode,tblStock.StockNumber,tblStock.StockName,tblStock.StockFullName ");
							sql.append("from tblStock where tblStock.isCatalog=0 and tblStock.statusId=0 ");
							//搜索数据（仓库搜索，关键字搜索）
							if(searchType != null && !"".equals(searchType)){
								if("item".equals(searchType)){
									//点击单个仓库
									if(!"all".equals(searchValue) && !"0".equals(searchValue)){
										sql.append(" and tblStock.classCode like '"+searchValue+"%'");
									}else{
										sql.append(" and len(tblStock.classCode)=5");
									}
								}else if("keyWord".equals(searchType)){
									//关键字搜索
									sql.append(" and (tblStock.classCode like '%"+searchValue+"%' or tblStock.StockNumber like '%"+searchValue+"%' or tblStock.StockFullName like '%"+searchValue+"%')");
								}
							}
							/*设置范围权限*/
							ArrayList scopeRight = new ArrayList();
	        				scopeRight.addAll(mop.getScope(MOperation.M_QUERY));
	        				scopeRight.addAll(lg.getAllScopeRight());
	                		sql = new StringBuffer(DynDBManager.scopeRightHandler("tblStock", "TABLELIST", "", lg.getId(), scopeRight, sql.toString(), null,""));
	            			
							
							sql.append(" order by tblStock.StockNumber ");
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							List stockList = new ArrayList();
							while(rs.next()){
								HashMap map=new HashMap();
								for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
									Object obj=rs.getObject(i);
									if(obj==null){
										map.put(rs.getMetaData().getColumnName(i), "");
									}else{
										map.put(rs.getMetaData().getColumnName(i), obj);
									}
								}
								
								/**
								 * 查询并处理仓库的状态（已准备，未准备）
								 */
								//需要准备的数据
								String sqls = "select id as stockPreId,isnull(CheckDate,'') as CheckDate,statusId from tblStockCheckPrepare where stockCode='"+map.get("classCode")+"'";
								sql = new StringBuffer(sqls);
								sql.append(" and isnull(statusId,0)!=1 and (tblStockCheckPrepare.id is null or tblStockCheckPrepare.statusId is null or ");
								sql.append("(SELECT count(0) FROM tblStockCheck a WHERE a.StockCode = tblStockCheckPrepare.stockCode AND isnull(a.statusId,0)!=1)=0 ");
								sql.append("or (SELECT count(0) FROM tblStockCheck a WHERE a.StockCode = tblStockCheckPrepare.stockCode)>0) and ");
								sql.append(" (SELECT count(0) FROM tblStockCheck a WHERE a.StockCode = tblStockCheckPrepare.stockCode AND isnull(a.statusId,0)='1' and a.PrepareId=tblStockCheckPrepare.id )=0 ");
								Statement s = conn.createStatement();
								ResultSet rset = s.executeQuery(sql.toString());
								if(!rset.next()){
									//不存在记录时
									map.put("stockPreStatus", "0");			//可准备
									stockList.add(map);
									continue;
								}
								
								//需要录入盘点单
								sql = new StringBuffer(sqls+" and statusId=0 and (SELECT count(0) FROM tblStockCheck a WHERE a.StockCode = tblStockCheckPrepare.stockCode AND isnull(a.statusId,0)='1' and a.PrepareId=tblStockCheckPrepare.id )=0");
								rset = s.executeQuery(sql.toString());
								if(rset.next()){
									map.put("CheckDate", rset.getString("CheckDate"));
									map.put("stockPreId", rset.getString("stockPreId"));
									map.put("stockInputStatus", "0");			//已准备要录入
								}
								
								//需要处理的
								sql = new StringBuffer(sqls+" and statusId=0 and (SELECT count(0) FROM tblStockCheck a WHERE a.StockCode = tblStockCheckPrepare.stockCode AND isnull(a.statusId,0)!=1 and a.workFlowNodeName != 'draft' )>0");
								rset = s.executeQuery(sql.toString());
								if(rset.next()){
									map.put("CheckDate", rset.getString("CheckDate"));
									map.put("stockPreId", rset.getString("stockPreId"));
									map.put("stockDealStatus", "0");			//已准备要处理
								}
								stockList.add(map);
							}
							List newList = new ArrayList();
							for(int i=0;i<stockList.size();i++){
								HashMap map = (HashMap)stockList.get(i);
								Object o = map.get("stockPreStatus");
								if(searchType != null && "pre".equals(searchType)){
									if("no".equals(searchValue)){
										if(o != null && "0".equals(o)){
											//未准备
											newList.add(map);
										}
									}
									if("yes".equals(searchValue)){
										//已准备
										Object o1 = map.get("stockInputStatus");
										Object o2 = map.get("stockDealStatus");
										if((o1 != null && "0".equals(o1)) || (o2 != null && "0".equals(o2))){
											newList.add(map);
										}
									}
								}else{
									newList.add(map);	
								}
							}
							result.retVal = newList;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("StockCheckMgt queryStock:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 盘点准备
	 * @param stockCode
	 * @return
	 */
	public Result stockPre(final String stockCode,final String userId,final String SCompanyID){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							/* 同一天同一个仓库只能一条准备记录。不能出现两条以上 */
							String sql = "select count(0) as count from tblStockCheckPrepare where stockCode='"+stockCode+"' and checkDate='"+BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd)+"'";
							ResultSet rset = st.executeQuery(sql);
							if(rset.next()){
								int count = rset.getInt("count");
								if(count>0){
									result.setRetCode(ErrorCanst.DATA_ALREADY_USED);
									result.setRetVal("同一天同一个仓库只能进行准备一次");
			                        return;
								}
							}
							
							DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("StockCheckPrepare");
							if (defineSqlBean != null) {
								HashMap map = new HashMap();
								map.put("StockCode", stockCode);
	
								Result rs3 = defineSqlBean.execute(conn, map,userId, null, null, "");
								if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
									if(rs3.retVal !=null && rs3.retVal instanceof Object[]){
										BaseEnv.log.debug(((Object[])rs3.retVal)[0]);
										result.setRetVal(((Object[])rs3.retVal)[0]);
									}else{
										BaseEnv.log.debug(rs3.retVal);
										result.setRetVal(rs3.retVal);
									}
									result.setRetCode(rs3.retCode);
									return;
								}
							}
							
							/**
							 * 执行仓库准备的存储过程
							 */
							StringBuffer procstr = new StringBuffer("{call proc_StockCheckPrepare(@UserId=?,@SCompanyID=?,");
							procstr.append("@StockCode=?,@retCode=?,@retValue=?)}");
							CallableStatement cs = conn.prepareCall(procstr.toString()) ;
							cs.setString(1, userId);
							cs.setString(2, SCompanyID);
							cs.setString(3, stockCode);
							cs.registerOutParameter(4, Types.INTEGER);
							cs.registerOutParameter(5, Types.VARCHAR);
							cs.execute();
							result.setRetCode(cs.getInt(4));
							result.setRetVal(cs.getString(5));
							BaseEnv.log.debug(procstr.toString());
							BaseEnv.log.debug("值1: "+userId);
							BaseEnv.log.debug("值2: "+SCompanyID);
							BaseEnv.log.debug("值3: "+stockCode);
							if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && result.getRetCode() != 2601) {  
								BaseEnv.log.debug("StockCheckMgt stockPre Info: " +cs.getString(4) + cs.getString(5));
								result.setRetCode(cs.getInt(4));
								result.setRetVal(cs.getString(5));
		                        return;
		                    }
							if(result.getRetVal()!=null && !"".equals(result.getRetVal())){
								result.setRetCode(ErrorCanst.DATA_ALREADY_USED);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("StockCheckMgt stockPre:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 取消盘点准备
	 * @param stockPreId
	 * @return
	 */
	public Result stockCancelPre(final String stockPreId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							
							/* 如果存在相应的盘点单，删除记录 */
							
							//盘点单明细
							StringBuffer sql = new StringBuffer("delete tblStockCheckDet where f_ref in (select id from tblStockCheck where PrepareId='"+stockPreId+"')");
							st.execute(sql.toString());
							
							//盘点单主表
							sql = new StringBuffer("delete tblStockCheck where PrepareId='"+stockPreId+"'");
							st = conn.createStatement();
							st.execute(sql.toString());
							
							//盘点准备明细表
							sql = new StringBuffer("delete tblStocksCheckGoods where f_ref='"+stockPreId+"'");
							st = conn.createStatement();
							st.execute(sql.toString());
							
							//盘点准备明细表序列号
							sql = new StringBuffer("delete tblStocksCheckGoodsSeq where f_ref='"+stockPreId+"'");
							st = conn.createStatement();
							st.execute(sql.toString());
							
							//盘点准备主表
							sql = new StringBuffer("delete tblStockCheckPrepare where id='"+stockPreId+"'");
							st = conn.createStatement();
							st.execute(sql.toString());
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("StockCheckMgt stockCancelPre:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 根据仓库classCode查询仓库的名称
	 * @param stockCode
	 * @return
	 */
	public Result queryStockFullName(final String stockCode){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							DBFieldInfoBean nameField = null;
					    	for(DBFieldInfoBean fb : BaseEnv.tableInfos.get("tblStock").getFieldInfos()){
					    		if("RowMarker".equals(fb.getFieldSysType())){
					    			nameField = fb;
					    		}
					    	}
					    	String StockMarkerName = "StockFullName";
					    	if(nameField != null){
					    		StockMarkerName = nameField.getFieldName();
					    	}
							
							StringBuffer sql = new StringBuffer("select "+StockMarkerName+" from tblStock where classCode=?");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, stockCode);
							ResultSet rs = ps.executeQuery();
							String stockFullName = "";
							while(rs.next()){
								stockFullName = rs.getString(1);
							}
							result.retVal = stockFullName;
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("StockCheckMgt queryStockFullName:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 查询盘点处理数据
	 * @param stockPreId				盘点准备单ID
	 * @param stockCode					仓库classCode
	 * @param stockCheckDate			盘点日期
	 * @return
	 */
	public Result stockCheckList(final String stockPreId,final String stockCode, final String stockCheckDate,
			final String workFlowNodeName,final int pageNo, final int pageSize){
		StringBuffer sql = new StringBuffer("select stock.id as stockcheckId,stock.BillNo,stock.BillDate,");
		sql.append("stock.StockCode,stock.statusId,tblStockCheckPrepare.CheckDate,tblStockCheckPrepare.id as stockcheckPreId, ");
		sql.append("stock.workFlowNodeName,tblStock.StockFullName,row_number() over(order by stock.workFlowNodeName,stock.createTime desc) as row_id ");
		sql.append(" from tblStockCheck stock left join tblStockCheckPrepare on stock.StockCode=tblStockCheckPrepare.StockCode");
		sql.append(" left join tblStock on tblStock.classCode=stock.StockCode ");
		sql.append(" where stock.workFlowNodeName != 'draft' ");
		
		//查询是否过账
		if(workFlowNodeName == null || "".equals(workFlowNodeName) || "0".equals(workFlowNodeName)){
			//默认显示未过账的数据
			sql.append(" and (stock.statusId is null or stock.statusId=0)");
		}else if("finish".equals(workFlowNodeName)){
			sql.append("and (stock.statusId=1)");
		}
		
		/* 准备表Id,仓库classCode,准备日期 */
		if(stockPreId != null && !"".equals(stockPreId)){
			sql.append(" and tblStockCheckPrepare.id='"+stockPreId+"'");
		}
		if(stockCode != null && !"".equals(stockCode)){
			sql.append(" and stock.StockCode='"+stockCode+"'");
		}
		if(stockCheckDate != null && !"".equals(stockCheckDate)){
			sql.append(" and tblStockCheckPrepare.CheckDate='"+stockCheckDate+"'");
		}
		return sqlListMaps(sql.toString(), new ArrayList(), pageNo, pageSize);
	}
	
	/**
	 * 盘点处理
	 * @param keyids			盘点准备单Id
	 * @param checkType			盘点方式
	 * @return
	 */
	public Result checkDeal(final String locale,final String SCompanyID,final String userId,
			final String stockPreId, final String checkType){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql="select COUNT(0) from tblStockCheckPrepare a,tblAccPeriod b where a.id=? and b.statusId=1 and a.CheckDate<periodBegin";
							CallableStatement cs = conn.prepareCall(sql) ;
							cs.setString(1, stockPreId);
							ResultSet rs=cs.executeQuery();
							if(rs.next()&&rs.getInt(1)>0){//如果当前盘点单日期小于当前期间，则不允许执行后续操作
								result.setRetCode(ErrorCanst.CURRENT_ACC_BEFORE_BILL);
								return;
							}
							
							StringBuffer procstr = new StringBuffer("{call proc_StockCheckOperation(@local=?,@SCompanyID=?,@UserId=?,@id=?,@Type=?,");
							procstr.append("@retCode=?,@retValue=?)}");
							cs = conn.prepareCall(procstr.toString()) ;
							cs.setString(1, locale);
							cs.setString(2, SCompanyID);
							cs.setString(3, userId);
							cs.setString(4, stockPreId);
							cs.setString(5, checkType);
							cs.registerOutParameter(6, Types.INTEGER);
							cs.registerOutParameter(7, Types.VARCHAR);
							cs.execute();
							result.setRetCode(cs.getInt(6));
							result.setRetVal(cs.getString(7));
							BaseEnv.log.debug(procstr.toString());
							BaseEnv.log.debug("值1: "+locale);
							BaseEnv.log.debug("值2: "+SCompanyID);
							BaseEnv.log.debug("值3: "+userId);
							BaseEnv.log.debug("值4: "+stockPreId);
							BaseEnv.log.debug("值5: "+checkType);
							if (result.getRetCode() != ErrorCanst.DEFAULT_SUCCESS && result.getRetCode() != 2601) {  
								BaseEnv.log.debug("StockCheckMgt checkDeal Info: " + cs.getString(1));
								result.setRetCode(cs.getInt(6));
								result.setRetVal(cs.getString(7));
								return;
							}
							if(result.getRetVal()!=null && !"".equals(result.getRetVal())){
								result.setRetCode(ErrorCanst.DATA_ALREADY_USED);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("StockCheckMgt checkDeal:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 查询可以导入数据的准备表数据
	 * @param stockCode
	 * @return
	 */
	public Result queryStockCheckPre(final String stockCode) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "select tblStockCheckPrepare.id as stockPreId,tblStockCheckPrepare.stockCode,";
							sql += "tblStockCheckPrepare.CheckDate as stockCheckDate,tblStock.StockName as StockName,tblStock.StockFullName as StockFullName from tblStockCheckPrepare ";
							sql += " left join tblStock on tblStock.classCode=tblStockCheckPrepare.stockCode where tblStockCheckPrepare.statusid=0";
							sql += " and tblStockCheckPrepare.stockCode='"+stockCode+"'";
							ResultSet rs = cs.executeQuery(sql);
							List list = new ArrayList();
							while(rs.next()) {
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
	                        		Object obj=rs.getObject(i);
	                        		if(obj==null){
	                        			map.put(rs.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			map.put(rs.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
	                        	list.add(map);
							}
							result.setRetVal(list);
						} catch (SQLException ex) {
							BaseEnv.log.error("StockCheckMgt queryStockCheckPre:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
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
	 * 根据外键值查询明细数据
	 * @param f_ref
	 * @return
	 */
	public Result queryStocksCheckGoodsId(final String id, final Integer moduleType) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							StringBuffer sql = new StringBuffer("select tblGoods.classCode,tblGoods.GoodsNumber,tblGoods.GoodsFullName,checkgoods.BatchNo,");
							sql.append("checkgoods.Inch,checkgoods.ProDate,checkgoods.yearNO,checkgoods.Hue,checkgoods.Availably,");
							sql.append("isnull(checkgoods.lastQty,0) as lastQty,tblGoods.BarCode,isnull(checkgoods.Qty,0) as Qty from tblGoods ");
							sql.append("left join (select BatchNo,Inch,ProDate,yearNO,Hue,Availably,GoodsCode,isnull(lastQty,0) as lastQty,0 as Qty from tblStocksCheckGoods ");
							sql.append("where f_ref='"+id+"' ) as checkgoods on tblGoods.classCode=checkgoods.GoodsCode ");
							sql.append("  ");
							sql.append("where tblGoods.isCatalog=0  and (tblGoods.seqIsUsed is null or tblGoods.seqIsUsed=1) ");
							if(moduleType == 2){
								//条形码
								sql.append(" and tblGoods.BarCode is not null and tblGoods.BarCode != ''");
							}else if(moduleType == 3){
								//序列号
								sql.setLength(0);
								sql.append(" select Seq from tblStocksCheckGoodsSeq where f_ref='"+id+"' order by id ");
							}
							ResultSet rs = cs.executeQuery(sql.toString());
							List list = new ArrayList();
							while(rs.next()) {
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
	                        		Object obj=rs.getObject(i);
	                        		if(obj==null){
                            			map.put(rs.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC){
	                        				obj = Double.parseDouble(obj.toString());
	                        				if("lastQty".equals(rs.getMetaData().getColumnName(i))){
	                        					obj = (int)Double.parseDouble(obj.toString());
	                        				}
	                        			}
	                        			if(rs.getMetaData().getColumnType(i)==Types.INTEGER){
	                        				obj = Integer.parseInt(obj.toString());
	                        			}
	                        			map.put(rs.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
	                        	list.add(map);
							}
							result.setRetVal(list);
						} catch (SQLException ex) {
							BaseEnv.log.error("StockCheckMgt queryStocksCheckGoodsId:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
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
	 * 删除盘点单
	 * @param id
	 * @return
	 */
	public Result delCheckBill(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement st = conn.createStatement();
							StringBuffer sql = new StringBuffer("select statusId,billno from tblStockCheck where id=?");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.setString(1, id);
							ResultSet rs = ps.executeQuery();
							int statusId = 0;
							while(rs.next()){
								statusId = rs.getInt("statusId");
							}
							if(statusId == 1){
								result.setRetVal("单据编号："+rs.getString("billno")+" 已盘点处理，不允许此操作");
								result.setRetCode(ErrorCanst.RET_HAS_AUDITING);
								return ;
							}
							
							/* 删除盘点单相应的数据 */
							sql = new StringBuffer("delete tblStockCheckDet where f_ref='"+id+"'");
							st.addBatch(sql.toString());
							sql = new StringBuffer("delete tblStockCheck where id='"+id+"'");
							st.addBatch(sql.toString());
							st.executeBatch();
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("StockCheckMgt delCheckBill:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 验证是否存在该仓库已准备记录
	 * @param stockCodes
	 * @return
	 */
	public Result queryStockCheckName(final String stockCodes) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "select tblStockCheckPrepare.id as PrepareId,tblStockCheckPrepare.StockCode,";
							sql += "tblStockCheckPrepare.CheckDate,tblStock.StockFullName as stockName from tblStockCheckPrepare ";
							sql += " left join tblStock on tblStock.classCode=tblStockCheckPrepare.stockCode where tblStockCheckPrepare.statusid=0";
							sql += " and (SELECT count(0) FROM tblStockCheck a WHERE a.StockCode = tblStockCheckPrepare.stockCode AND ";
							sql += "isnull(a.statusId,0)='1' and a.PrepareId=tblStockCheckPrepare.id)=0 and tblStock.classCode='"+stockCodes+"'";
							ResultSet rs = cs.executeQuery(sql);
							HashMap map=new HashMap();
							if(rs.next()) {
	                        	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
	                        		Object obj=rs.getObject(i);
	                        		if(obj==null){
	                        			map.put(rs.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			map.put(rs.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
							}
							result.setRetVal(map);
						} catch (SQLException ex) {
							BaseEnv.log.error("StockCheckMgt queryStockCheckName:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
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
	 * 导入验证商品是否有效
	 * @return
	 */
	public Result exportValidate(final HashMap values,final int moduleType,final String parepareId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
//							String prop = "";
//							for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
//								if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq")) {
//									prop += " and tblStocksCheckGoods."+gpBean.getPropName()+"='"+values.get(gpBean.getPropName())+"'";
//								}
//							}
							StringBuffer sql = new StringBuffer("select tblGoods.classCode,tblGoods.seqIsUsed" +
//									",tblStocksCheckGoods.id as exi " +
									" from tblGoods " +									
//									" left join tblStocksCheckGoods on tblGoods.classCode=tblStocksCheckGoods.GoodsCode " +
//									" and tblStocksCheckGoods.f_ref='"+parepareId+"' " +
//									prop+
									" where 1=1 ");
							if(moduleType==2){
								sql.append(" and tblGoods.BarCode ='"+values.get("BarCode")+"'");
							}else{//			tableName,fieldName,reportView,billView,popSel,keyword,popupView
								boolean hasCond = false;
					            for (String[] shows : BaseEnv.reportShowSet) { 
									if ("tblGoods".equals(shows[0]) && "1".equals(shows[6]) && "1".equals(shows[3])&&"1".equals(shows[5])) {
										//只有关键字才能进行查询
										if(values.get(shows[1]) != null && !values.get(shows[1]).equals("") )	{
											sql.append(" and tblGoods."+shows[1]+" ='"+values.get(shows[1])+"'");
											hasCond = true;
										}
									}
					            }
					            if(!hasCond){
					            	result.retCode = ErrorCanst.DEFAULT_FAILURE;
									result.retVal = "商品关键字字段没有录入数据";
					            }
							}
							Statement st = conn.createStatement();
							ResultSet rset =st.executeQuery(sql.toString());
							String goodsCode = "";
							if(rset.next()){
								goodsCode = rset.getString("classCode");
								String isSeq = rset.getString("seqIsUsed");
//								String exi = rset.getString("exi");
								if(isSeq != null && isSeq.length() > 0 && isSeq.equals("0")){
									result.retCode = ErrorCanst.DEFAULT_FAILURE;
									result.retVal = "序列号商品，请用序列号模板导入";
								}
//								else if((exi==null || exi.length() ==0) && !values.get("Qty").equals("0")){
//										result.retCode = ErrorCanst.DEFAULT_FAILURE;
//										result.retVal = "仓库中不存在即零库存，请用手工盘点单录入";
//								}
								else{
									result.setRetVal(goodsCode);
								}
							}else{
								if(moduleType==2){
									result.retCode = ErrorCanst.DEFAULT_FAILURE;
									result.retVal = "条码不存在，请先添加商品后再导入";
								}else{
									result.retCode = ErrorCanst.DEFAULT_FAILURE;
									result.retVal = "商品不存在，请先确认商品编号名称等相关信息是否录入正确";
								}
							}
							
						} catch (Exception ex) {
							BaseEnv.log.error("StockCheckMgt exportValidate:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							result.setRetVal("查询失败");
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
	 * 根据序列号查对应的商品属性
	 * @param seq
	 * @param valueList
	 * @param prepareId
	 * @param stockCode
	 * @return
	 */
	public Result checkSeq(final String seq ,final ArrayList<HashMap> valueList,final String prepareId,final String stockCode){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String prop = "";
							for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
								if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq")) {
									prop +=","+gpBean.getPropName();
								}
							}
							StringBuffer sql = new StringBuffer(" select b.GoodsCode"+prop+" from tblStocksCheckGoodsSeq a join tblStockDet b on a.seq=b.Seq where a.seq='"+seq+"' and a.f_ref='"+prepareId+"' and b.StockCode='"+stockCode+"' order by ItemNo desc");
							Statement st = conn.createStatement();
							ResultSet rset =st.executeQuery(sql.toString());
							String goodsCode = "";
							if(rset.next()){
								//在列表中查找是否有相同商品，相同属性产品	
								boolean found = false;
								for(HashMap map:valueList){
									if(rset.getString("GoodsCode").equals(map.get("GoodsCode"))) {
										boolean isSame = true;
										for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
											if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq") && 
													!rset.getString(gpBean.getPropName()).equals(map.get(gpBean.getPropName()))) {
												isSame = false;
												break;
											}
										}
										if(isSame){
											int Qty = Integer.parseInt(map.get("Qty").toString());
											map.put("Qty", ""+(Qty+1));
											String Seq = (String)map.get("Seq");
											map.put("Seq", Seq+seq+"~");
											found = true;
											break;
										}
									}
								}
								if(!found){
									HashMap map = new HashMap();
									valueList.add(map);
									map.put("GoodsCode", rset.getString("GoodsCode"));
									for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
										if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq") ) {
											map.put(gpBean.getPropName(), rset.getString(gpBean.getPropName()));
										}
									}
									map.put("Qty", "1");
									map.put("Seq", seq+"~");
								}
								
							}else{
								result.setRetCode(ErrorCanst.ER_NO_DATA);
								result.setRetVal("序列号不存在即零库存，请用手工盘点单录入");
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("StockCheckMgt exportValidate:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	
	public Result inputLastQty(final ArrayList<HashMap> valueList,final String prepareId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							
							for(HashMap map:valueList){
								String prop = "";
								for (GoodsPropInfoBean gpBean : BaseEnv.propList) {
									if (gpBean != null && !gpBean.getPropName().toLowerCase().equals("seq")) {
										prop +=" and "+gpBean.getPropName()+"='"+map.get(gpBean.getPropName())+"'";
									}
								}
								StringBuffer sql = new StringBuffer(" select LastQty from tblStocksCheckGoods where GoodsCode='"+map.get("GoodsCode")+
										"' and f_ref='"+prepareId+"' "+prop);
								Statement st = conn.createStatement();
								ResultSet rset =st.executeQuery(sql.toString());
								if(rset.next()){
									map.put("LastQty", rset.getString(1));
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("StockCheckMgt,inputLastQty:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 添加盘点单
	 * @param values
	 * @param lg
	 * @param tableInfo
	 * @param locale
	 * @param resources
	 * @param allTables
	 * @return
	 */
	public Result addStockCheck(final HashMap values,final LoginBean lg,final DBTableInfoBean tableInfo,
			final Locale locale,final MessageResources resources,final Hashtable allTables,HttpServletRequest request){
		//获取路径
		String path = request.getSession().getServletContext().getRealPath("DoSysModule.sql");
		Hashtable props = (Hashtable) request.getSession().getServletContext().getAttribute(BaseEnv.PROP_INFO);
		MOperation mop = GlobalsTool.getMOperationMap(request);
		
		
		values.put("BillDate", BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
		values.put("EmployeeID", lg.getId());
		values.put("DepartmentCode", lg.getDepartCode());
		String addMessage = GlobalsTool.getMessage(locale, "common.lb.add");
		try {
			return new UserFunctionMgt().add(tableInfo, values, lg, "", allTables, path, "", locale, addMessage, resources, props, mop, "10");		
		} catch (Exception e) {
			Result rs = new Result();
			rs.retCode = ErrorCanst.DEFAULT_FAILURE;
			rs.retVal = "导入失败";
			BaseEnv.log.error("导入盘点录入单失败",e);
			return rs;
		}
		
	}
	
}
