package com.menyi.aio.web.moduleFlow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.dbfactory.Result;
import com.menyi.aio.web.login.*;
import com.menyi.aio.bean.*;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:模块导航的接口类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: 张雪
 * </p>
 * 
 * @author 张雪
 * @version 1.0
 */
public class ModuleFlowMgt extends AIODBManager {
	
	
	public HashMap<String,String> copySysDest(Connection conn ,String userId,LoginBean loginBean,String locale) throws SQLException{
		//是否需要复制
		String sql = " select count(url) from tblMyDest where userId=?";
		PreparedStatement pst =conn.prepareStatement(sql);
		pst.setString(1, userId);
		ResultSet rs =pst.executeQuery();
		int count=0;
		if(rs.next()){
			count = rs.getInt(1);
		}
		if(count > 0){
			return null; //未发生过变化时，不返回
		}else{
			HashMap<String,String> idmap = new HashMap<String,String>();
			//复制分类
			sql = "select bean.id,bean.className  from tblMyDestClass bean where len(bean.userId)=0 ";
			pst =conn.prepareStatement(sql);
			rs =pst.executeQuery();
			while(rs.next()){
				String className = rs.getString("className");
				String newId = IDGenerater.getId();
				sql = "insert into tblMyDestClass(id,className,userId) values(?,?,?)";
				pst =conn.prepareStatement(sql);
				pst.setString(1, newId);
				pst.setString(2, className);
				pst.setString(3, userId);
				pst.executeUpdate();
				idmap.put(rs.getString("id")+"_cc", newId);
			}
			
			sql = "select bean.id,bean.title,bean.url,bean.pic,bean.orderNo,bean.languageId,bean.classCode "
				+ " from tblMyDest bean where bean.userId='' order by bean.orderNo";
			pst =conn.prepareStatement(sql);
			rs =pst.executeQuery();
			
			KRLanguageQuery krQuery = new KRLanguageQuery();
			ArrayList<String[]> list = new ArrayList<String[]>();
			while(rs.next()){
				list.add(new String[]{rs.getString("id"),rs.getString("title"),rs.getString("url"),rs.getString("pic"),rs.getString("orderNo"),rs.getString("languageId"),rs.getString("classCode")});
				krQuery.addLanguageId(rs.getString("languageId"));
			}
			HashMap map = krQuery.query(conn);
			for (int i = 0; i < list.size(); i++) {
				String[] flowBean = (String[]) list.get(i);
				if(flowBean[5] != null){
					KRLanguage lan = (KRLanguage) map.get(flowBean[5]
							.toString());
					if (lan != null) {
						flowBean[1] = lan.get(locale);
					}
				}
			}
				
			for(String[] o : list){
				//进行权限判断
				String link = o[2];
				if(link == null || link.length() ==0){
					continue;
				}
				boolean isRight=false;
				if(link.indexOf("/moduleFlow.do?operation=docFlow") > -1 ){
					isRight = true;
				}else{
					MOperation mop = (MOperation) loginBean.getOperationMap().get(link);
					if(mop != null && mop.query){
						isRight = true;
					}                        
				}
				if(isRight){
					String newId = IDGenerater.getId();
					sql = "insert into tblMyDest(id,title,url,pic,orderNo,userId,classCode) values(?,?,?,?,?,?,?)";
					pst =conn.prepareStatement(sql);
					pst.setString(1, newId);
					pst.setString(2, o[1]);
					pst.setString(3, o[2]);
					pst.setString(4, o[3]);
					pst.setString(5, o[4]);
					pst.setString(6, userId);
					pst.setString(7, idmap.get(o[6]+"_cc"));
					pst.executeUpdate();
					
					idmap.put(o[0], newId);
				}
			}
			return idmap;
		}		
	}

	public Result addToMyDest(final String title,final String url,final String pic,final String userId,final LoginBean loginBean,final String locale) {
		ArrayList param = new ArrayList();

		final Result rs_lan = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							if(!userId.equals("")){
								//修改前，如果该帐户所有导航信息是空的，先要执行一个复制功能						
								HashMap<String,String> idmap =copySysDest(conn ,userId,loginBean,locale);
							}
							//是否重复
							String sql = " select count(url) from tblMyDest where userId=? and url=?";
							PreparedStatement pst =conn.prepareStatement(sql);
							pst.setString(1, userId);
							pst.setString(2, url);
							ResultSet rs =pst.executeQuery();
							int count=0;
							if(rs.next()){
								count = rs.getInt(1);
							}
							if(count > 0){
								rs_lan.retCode = ErrorCanst.MULTI_VALUE_ERROR;
								return;
							}
							
							//找出最大排序号，插入到最后
							sql = " select max(orderNo) from tblMyDest where userId=? ";
							pst =conn.prepareStatement(sql);
							pst.setString(1, userId);
							rs =pst.executeQuery();
							count=0;
							if(rs.next()){
								count = rs.getInt(1)+1;
							}
							
							sql = "insert into tblMyDest(id,title,url,pic,orderNo,userId) values(?,?,?,?,?,?)";
							pst =conn.prepareStatement(sql);
							pst.setString(1, IDGenerater.getId());
							pst.setString(2, title);
							pst.setString(3, url);
							pst.setString(4, pic);
							pst.setInt(5, count);
							pst.setString(6, userId);
							pst.executeUpdate();	
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				return rs_lan.getRetCode();
			}
		});
		rs_lan.setRetCode(retCode);
		return rs_lan;
	}
	public Result cancelMyDest(final String id,final String userId,final LoginBean loginBean,final String locale) {
		ArrayList param = new ArrayList();

		final Result rs_lan = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String newId = id;
						if(!userId.equals("")){
						
							//修改前，如果该帐户所有导航信息是空的，先要执行一个复制功能						
							HashMap<String,String> idmap = copySysDest(conn ,userId,loginBean,locale);
							if(idmap != null){
								rs_lan.retVal = "refresh";
							}
							
							if(idmap != null ){
								newId = idmap.get(id);
							}
							if(newId == null){
								throw new SQLException("oldCanncelId return null newId");
							}
						}
						//是否重复
						String sql = " delete tblMyDest where id=?";
						PreparedStatement pst =conn.prepareStatement(sql);
						pst.setString(1, newId);
						pst.executeUpdate();												
					}
				});
				return rs_lan.getRetCode();
			}
		});
		rs_lan.setRetCode(retCode);
		return rs_lan;
	}
	public Result changePic(final String id,final String pic,final String userId,final LoginBean loginBean,final String locale) {
		ArrayList param = new ArrayList();

		final Result rs_lan = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String newId = id;
						if(!userId.equals("")){
							//修改前，如果该帐户所有导航信息是空的，先要执行一个复制功能					
							HashMap<String,String> idmap = copySysDest(conn ,userId,loginBean,locale);
							
							if(idmap != null ){
								newId = idmap.get(id);
							}
							if(newId == null){ 
								throw new SQLException("oldCanncelId return null newId");
							}
						}
						//是否重复
						String sql = " update tblMyDest set pic=? where id=?";
						PreparedStatement pst =conn.prepareStatement(sql);
						pst.setString(1, pic);
						pst.setString(2, newId);
						pst.executeUpdate();												
					}
				});
				return rs_lan.getRetCode();
			}
		});
		rs_lan.setRetCode(retCode);
		return rs_lan;
	}
	
	public Result changeClass(final String id,final String classCode,final String userId,final LoginBean loginBean,final String locale) {
		ArrayList param = new ArrayList();

		final Result rs_lan = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String newId = id;
						String myClassCode = classCode;
						if(!userId.equals("")){
							//修改前，如果该帐户所有导航信息是空的，先要执行一个复制功能					
							HashMap<String,String> idmap = copySysDest(conn ,userId,loginBean,locale);
							
							if(idmap != null ){
								newId = idmap.get(id);
								myClassCode = idmap.get(classCode+"_cc");
							}
							if(newId == null){ 
								throw new SQLException("oldCanncelId return null newId");
							}
						}
						//
						String sql = " update tblMyDest set classCode=? where id=?";
						PreparedStatement pst =conn.prepareStatement(sql);
						pst.setString(1, myClassCode);
						pst.setString(2, newId);
						pst.executeUpdate();												
					}
				});
				return rs_lan.getRetCode();
			}
		});
		rs_lan.setRetCode(retCode);
		return rs_lan;
	}
	
	public Result addClass(final String className,final String userId) {
		ArrayList param = new ArrayList();

		final Result rs_lan = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						
						//是否重复
						String sql = " insert into tblMydestClass(id,userId,className) values(?,?,?)";
						PreparedStatement pst =conn.prepareStatement(sql);
						pst.setString(1, IDGenerater.getId());
						pst.setString(2, userId);
						pst.setString(3, className);
						pst.executeUpdate();												
					}
				});
				return rs_lan.getRetCode();
			}
		});
		rs_lan.setRetCode(retCode);
		return rs_lan;
	}
	public Result updateClass(final String id,final String className) {
		ArrayList param = new ArrayList();

		final Result rs_lan = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						
						//是否重复
						String sql = " update tblMydestClass set className=? where id=?";
						PreparedStatement pst =conn.prepareStatement(sql);
						pst.setString(1, className);
						pst.setString(2, id);
						pst.executeUpdate();												
					}
				});
				return rs_lan.getRetCode();
			}
		});
		rs_lan.setRetCode(retCode);
		return rs_lan;
	}
	public Result getMyClass(final String userId) {
		ArrayList param = new ArrayList();
		String sql = "select id,className from tblMydestClass where userId=?";
		param.add(userId);
		return this.sqlList(sql, param);
	}
	public Result delClass(final String id) {
		ArrayList param = new ArrayList();

		final Result rs_lan = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String sql = "select count(*) from tblMyDest where classCode = ?";
						PreparedStatement pst =conn.prepareStatement(sql);			
						pst.setString(1, id);
						ResultSet rs  = pst.executeQuery();
						if(rs.next()){
							int count = rs.getInt(1);
							if(count > 0){
								rs_lan.retCode = ErrorCanst.DATA_ALREADY_USED;
								return;
							}
						}
						
						sql = " delete tblMyDestClass where id=?";
						int order=0;
						pst =conn.prepareStatement(sql);	
						pst.setString(1, id);				
						pst.executeUpdate();
					}
				});
				return rs_lan.getRetCode();
			}
		});
		rs_lan.setRetCode(retCode);
		return rs_lan;
	}
	public Result orderMyDest(final String ids,final String userId,final LoginBean loginBean,final String locale) {
		ArrayList param = new ArrayList();

		final Result rs_lan = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						//修改前，如果该帐户所有导航信息是空的，先要执行一个复制功能						
						HashMap<String,String> idmap = copySysDest(conn ,userId,loginBean,locale);
						if(idmap != null){
							rs_lan.retVal = "refresh";
						}
						String[] idarray = ids.split(",");
						String sql = " update tblMyDest set orderNo=? where id=?";
						int order=0;
						PreparedStatement pst =conn.prepareStatement(sql);						
						for(String id:idarray){						
							if(id != null && id.length() >0){								
								pst.setInt(1, order);
								if(idmap == null){
									pst.setString(2, id);
									pst.addBatch();
								}else{
									String newId = idmap.get(id);
									if(newId != null){
										pst.setString(2, newId);
										pst.addBatch();
									}
								}								
								order ++;
							}						
						}
						pst.executeBatch();
					}
				});
				return rs_lan.getRetCode();
			}
		});
		rs_lan.setRetCode(retCode);
		return rs_lan;
	}
	
	public Result getMyDest(LoginBean loginBean, final String locale) {
		ArrayList param = new ArrayList();
		String sql = "select bean.id,bean.title,bean.url,bean.pic,bean.orderNo,bean.classCode,bc.className "
				+ " from tblMyDest bean left join tblMyDestClass bc on bean.classCode=bc.id where bean.userId=? order by bean.orderNo";
		param.add(loginBean.getId());
		Result flow_rs = this.sqlList(sql, param);
		//如果查询的数据一条数据都没有
		//取系统默认导航,并进行权限过滤，如果是自用私人的导航，不需过滤
		if(flow_rs.retCode ==ErrorCanst.DEFAULT_SUCCESS && ((List)flow_rs.retVal).size() ==0){
			ArrayList list = new ArrayList();
			sql = "select bean.id,bean.title,bean.url,bean.pic,bean.orderNo,bean.classCode,bc.className,bean.languageId "
				+ " from tblMyDest bean left join tblMyDestClass bc on bean.classCode=bc.id  where bean.userId='' order by bean.orderNo";
			param.clear();
			final Result sysrs = this.sqlList(sql, param);
			
			if(sysrs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//取多语言数据
				final Result rs_lan = new Result();
				int retCode = DBUtil.execute(new IfDB() {
					public int exec(Session session) {
						session.doWork(new Work() {
							public void execute(Connection connection)
									throws SQLException {
								Connection conn = connection;
								KRLanguageQuery krQuery = new KRLanguageQuery();
								ArrayList flowList = (ArrayList) sysrs.getRetVal();
								for (int i = 0; i < flowList.size(); i++) {
									Object[] flowBean = (Object[]) flowList.get(i);
									if(flowBean[7] != null){
										krQuery.addLanguageId(flowBean[7].toString());
									}
								}
								HashMap map = krQuery.query(conn);
								for (int i = 0; i < flowList.size(); i++) {
									Object[] flowBean = (Object[]) flowList.get(i);
									if(flowBean[7] != null){
										KRLanguage lan = (KRLanguage) map.get(flowBean[7]
												.toString());
										if (lan != null) {
											flowBean[1] = lan.get(locale);
										}
									}
								}
								rs_lan.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
								rs_lan.setRetVal(flowList);
							}
						});
						return rs_lan.getRetCode();
					}
				});
				
				for(Object o : (List)sysrs.retVal){
					//进行权限判断
					String link = (String)(((Object[])o)[2]);
					if(link == null || link.length() ==0){
						continue;
					}
					if(link.indexOf("/moduleFlow.do?operation=docFlow") > -1 ){
						list.add(o);
					}else{
						MOperation mop = (MOperation) loginBean.getOperationMap().get(link);
						if(mop != null && mop.query){
							list.add(o);
						}                        
					}
				}
			}
			flow_rs.retVal=list;
		}else{
			flow_rs.retCode = 2;
		}		
		
		return flow_rs;
	}
	public Result getSystemDefaultDest(final String locale) {
		ArrayList param = new ArrayList();
		
		ArrayList list = new ArrayList();
		String sql = "select bean.id,bean.title,bean.url,bean.pic,bean.orderNo,bean.classCode,bc.className ,bean.languageId "
			+ " from tblMyDest bean left join tblMyDestClass bc on bean.classCode=bc.id   where bean.userId='' order by bean.orderNo";
		param.clear();
		final Result sysrs = this.sqlList(sql, param);
		
		if(sysrs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//取多语言数据
			final Result rs_lan = new Result();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection)
								throws SQLException {
							Connection conn = connection;
							KRLanguageQuery krQuery = new KRLanguageQuery();
							ArrayList flowList = (ArrayList) sysrs.getRetVal();
							for (int i = 0; i < flowList.size(); i++) {
								Object[] flowBean = (Object[]) flowList.get(i);
								if(flowBean[7] != null){
									krQuery.addLanguageId(flowBean[7].toString());
								}
							}
							HashMap map = krQuery.query(conn);
							for (int i = 0; i < flowList.size(); i++) {
								Object[] flowBean = (Object[]) flowList.get(i);
								if(flowBean[7] != null){
									KRLanguage lan = (KRLanguage) map.get(flowBean[7]
											.toString());
									if (lan != null) {
										flowBean[1] = lan.get(locale);
									}
								}
							}
							rs_lan.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							rs_lan.setRetVal(flowList);
						}
					});
					return rs_lan.getRetCode();
				}
			});			
			
		}		
		return sysrs;
	}

	public Result getLeftMenu(LoginBean loginBean, final String locale) {
		ArrayList param = new ArrayList();
		String sql = "select bean.id,bean.ModuleName,bean.ListOrder,bean.Icon,bean.LinkAddress "
				+ " from ModuleFlowBean bean where bean.statusId=0 and (bean.MainModule='"
				+ loginBean.getDefSys()
				+ "' or bean.MainModule='0') order by bean.ListOrder";
		final Result flow_rs = this.list(sql, param);

		final Result rs_lan = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						KRLanguageQuery krQuery = new KRLanguageQuery();
						ArrayList flowList = (ArrayList) flow_rs.getRetVal();
						for (int i = 0; i < flowList.size(); i++) {
							Object[] flowBean = (Object[]) flowList.get(i);
							krQuery.addLanguageId(flowBean[1].toString());
						}
						HashMap map = krQuery.query(conn);
						for (int i = 0; i < flowList.size(); i++) {
							Object[] flowBean = (Object[]) flowList.get(i);
							KRLanguage lan = (KRLanguage) map.get(flowBean[1]
									.toString());
							if (lan != null) {
								flowBean[1] = lan.get(locale);
							}
						}
						rs_lan.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						rs_lan.setRetVal(flowList);
					}
				});
				return rs_lan.getRetCode();
			}
		});
		return rs_lan;
	}

	public Result getReportList(final String keyId, final String local) {
		final String sql = "select "
				+ local
				+ ",Link from tblReportNavigation,tblLanguage "
				+ "where tblReportNavigation.reportName=tblLanguage.id and f_ref = ? order by Row";
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			@Override
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(sql);
						ps.setString(1, keyId);
						ResultSet rs = ps.executeQuery();
						List report = new ArrayList();
						while (rs.next()) {
							report.add(new ModuleFlowDetBean(rs
									.getString(local), rs.getString("Link")));
						}
						result.setRetVal(report);
					}
				});
				return 0;
			}
		});
		return result;
	}
	
	public Result getStatList(final String keyId,final String local){
		final String sql = "select "
			+ local
			+ ",Link from tblModuleFlowDetail,tblLanguage "
			+ "where tblModuleFlowDetail.reportName=tblLanguage.id and f_ref = ? order by Row";
		final Result result=new Result();
		int retCode=DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection con){
						try{
						PreparedStatement st=con.prepareStatement(sql);
						st.setString(1, keyId);
						ResultSet rs=st.executeQuery();
						List report=new ArrayList();
						while(rs.next()){
							report.add(new ModuleFlowDetBean(rs.getString(local),rs.getString("Link")));
						}
						result.setRetVal(report);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				});
				return 0;
			}
		});
		return result;
	}
	
	public Result getDetailList(final String keyId,final String local){
		final String sql = "select "
			+ local
			+ ",Link from tblModuleFlowDetailre,tblLanguage "
			+ "where tblModuleFlowDetailre.reportName=tblLanguage.id and f_ref = ? order by Row";
		final Result result =new Result();
		int retCode=DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection con){
						try{
						PreparedStatement ps=con.prepareStatement(sql);
						ps.setString(1, keyId);
						ResultSet rs=ps.executeQuery();
						List report=new ArrayList();
						while(rs.next()){
							report.add(new ModuleFlowDetBean(rs.getString(local),rs.getString("Link")));
						}
						result.setRetVal(report);
					}catch(Exception e){
						e.printStackTrace();
					}
					}
				});
				return 0;
			}
		});
		return result;
	}

	public Result getEmployees() {
		ArrayList param = new ArrayList();
		String sql = "select bean.id,bean.empFullName from EmployeeBean as bean";
		Result rs = this.list(sql, param);
		return rs;
	}

	public Result queryModuleFlowDetById(String Id) {
		ArrayList param = new ArrayList();
		String sql = "select bean.Link from ModuleFlowDetBean bean where bean.f_ref=?";
		param.add(Id);
		Result rs = this.list(sql, param);
		return rs;
	}

	public Result getDocFlow(String keyId, final String locale) {
		Result rs;
		String sql;
		ArrayList param = new ArrayList();
		param.add(keyId.trim());

		sql = "select max(bean.Row),max(bean.ListOrder) from ModuleFlowDetBean bean where bean.f_ref=? ";
		rs = this.list(sql, param);
		ArrayList max = (ArrayList) rs.getRetVal();

		Object[] values = new Object[3];
		if (max.size() > 0) {
			Object[] maxs = (Object[]) max.get(0);
			values[0] = maxs[0].toString();
			values[1] = maxs[1].toString();
		}

		sql = "select bean.id,bean.ModuleItemName,bean.Link,bean.IconName,bean.Row,bean.ListOrder from ModuleFlowDetBean bean where bean.f_ref=? order by bean.ListOrder ";
		final Result flow_rs = this.list(sql, param);

		final Result rs_lan = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						KRLanguageQuery krQuery = new KRLanguageQuery();
						ArrayList flowList = (ArrayList) flow_rs.getRetVal();
						for (int i = 0; i < flowList.size(); i++) {
							Object[] flowBean = (Object[]) flowList.get(i);
							krQuery.addLanguageId(flowBean[1].toString());
						}
						HashMap map = krQuery.query(conn);
						for (int i = 0; i < flowList.size(); i++) {
							Object[] flowBean = (Object[]) flowList.get(i);
							KRLanguage lan = (KRLanguage) map.get(flowBean[1]
									.toString());
							if (lan != null) {
								flowBean[1] = lan.get(locale);
							}
						}
						rs_lan.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						rs_lan.setRetVal(flowList);
					}
				});
				return rs_lan.getRetCode();
			}
		});
		values[2] = rs_lan.getRetVal();
		rs.setRetCode(retCode);
		rs.setRetVal(values);
		return rs;
	}

	public Result getModelName(final String linkAddress, final String locale) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select 'zh_CN:'+isnull(b.zh_CN,'')+';zh_TW:'+isnull(b.zh_TW,'')+';en:'+isnull(b.en,'') as modelName "
									+ "from tblModules a left join tblLanguage b on a.modelName=b.id where a.linkAddress=?";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, linkAddress);
							ResultSet rss = ps.executeQuery();
							if (rss.next()) {
								String str = rss.getString("modelName");
								int index = str.indexOf(locale);
								int end = str.indexOf(";", index);
								str = str.substring(
										index + locale.length() + 1, end);
								rs.setRetVal(str);
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							}
						} catch (Exception e) {
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	public Result getMainModel(final String linkAddress) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select mainmodule from tblModules where linkAddress=? and IsUsed=1 and mainmodule!=0";
							PreparedStatement ps = conn.prepareStatement(sql);
							ps.setString(1, linkAddress);
							ResultSet rss = ps.executeQuery();
							if (rss.next()) {
								String str = rss.getString("mainmodule");
								rs.setRetVal(str);
								rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							} else {
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						} catch (Exception e) {
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 查询我的常用菜单
	 * @param userId
	 * @return
	 */
	public Result queryMyMenu(final String userId) {
		ArrayList<String> param = new ArrayList<String>() ;
		String sql = "select title,url,pic from tblMyDest where userId=? order by orderNo " ;
		param.add(userId);
		Result result = sqlList(sql, param);
		if(result.retCode == ErrorCanst.DEFAULT_FAILURE || ((ArrayList)result.retVal).size()==0){
			sql = "select title,url,pic from tblMyDest where len(userId)=0 order by orderNo ";
			param = new ArrayList<String>() ;
			result = sqlList(sql, param);
		}
		return result ;
	}
	
	/**
	 * 查询导航菜单
	 * @param userId
	 * @return
	 */
	public Result queryModuleMenu(String defSys,String locale,int pageSize,int pageNo) {
		ArrayList<String> param = new ArrayList<String>() ;
		String sql = "select flow.id,lan."+locale+",mainModule from tblModuleFlow flow join tblLanguage lan " +
				"on flow.moduleName=lan.id where flow.mainModule in ('"+defSys+"','0') order by mainModule desc ,listOrder asc" ;
		return sqlList(sql,param,pageSize,pageNo,true);
	}
	
}
