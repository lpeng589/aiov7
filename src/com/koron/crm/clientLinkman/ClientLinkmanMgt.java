package com.koron.crm.clientLinkman;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

public class ClientLinkmanMgt extends AIODBManager{
	
	/**
	 * 查询所有姓氏并整理百家姓
	 * @param id
	 * @return
	 */
	public Result queryFamilyName() {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {

							String sql = "select  substring(UserName,1,1) from CRMClientInfoDet where userName is not null and len(userName)>0 group by substring(UserName,1,1)";
							PreparedStatement p = conn.prepareStatement(sql);
							ResultSet rss = p.executeQuery();
							Statement state = conn.createStatement() ;
							sql = "delete from tblFirstName" ;
							state.addBatch(sql) ;
							while(rss.next()){
								String userName = rss.getString(1) ;
								String pym = CustomizePYM.getFirstLetter(userName);								
								if(pym.length()>0){
									sql = "insert into tblFirstName(id,letter,firstName) values('"+IDGenerater.getId()+"','"+pym+"','"+userName+"')" ;
									state.addBatch(sql) ;
								}
							}
							state.executeBatch() ;
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("ClientLinkmanMgt queryFamilyName:",e) ;
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
     * 查询客户的Email,手机号码
     * @param tableName 主表
     * @param childTableName 子表
     * @param keyIds  要改变客户的Id
     * @param createBy 归属人
     * @return
     */
    public Result selectSMSorEmailById(final String keyIds,final String type) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select "+type+" from CRMClientInfoDet where id in ("+keyIds+")" ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ResultSet rss = ps.executeQuery() ;
							ArrayList<String> strList = new ArrayList<String>() ;
							while(rss.next()){
								strList.add(rss.getString(type)) ;
							}
							rs.setRetVal(strList) ;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace() ;
							BaseEnv.log.error("ClientLinkmanMgt---selectSMSorEmailById method :"+ ex);
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
	 * 查询百家姓
	 * @return
	 */
	public Result queryBJX() {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							TreeMap<String, ArrayList<String>> map = new TreeMap<String, ArrayList<String>>();
							for(String s:GlobalsTool.getAllLetter()){
								map.put(s.toUpperCase(), new ArrayList<String>());
							}
							String sql = "select letter,firstName from tblFirstName";
							PreparedStatement p = conn.prepareStatement(sql);
							ResultSet r = p.executeQuery();
							while(r.next()){
								String strName = r.getString("letter").toUpperCase() ;
								ArrayList<String> list = map.get(strName) ;
								if(list!=null){
									list.add(r.getString("firstName"));
									map.put(strName, list) ;
								}
							}
							rst.setRetVal(map) ;
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("ClientLinkmanMgt queryBJX:",e) ;
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
	 * 删除
	 * @param keyIds
	 * @return
	 */
	public Result deleteClientLinkMan(final String[] keyIds) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							StringBuffer ids = new StringBuffer("");
							int i = 0;
							for (String s : keyIds) {
								if (i == 0) {
									ids.append("'" + s + "'");
									i++;
								} else {
									ids.append(",'" + s + "'");
								}
							}
							Statement state = conn.createStatement() ;
							/*删除联系人纪念日*/
							String strSQL = "delete from CRMMemoryDayDet where f_ref in (select id from CRMMemoryDay " +
									"where f_brother in (select f_ref from CRMClientInfoDet where id in ("+ids.toString()+")) and mainUser in ("+ids.toString()+"))" ;
							state.addBatch(strSQL) ;
							strSQL = "delete from CRMMemoryDay where f_brother in (select f_ref from CRMClientInfoDet where id in ("+ids.toString()+")) and mainUser in ("+ids.toString()+")" ;
							state.addBatch(strSQL) ;
							/*删除联系人*/
							strSQL = "delete from CRMClientInfoDet where id in (" + ids.toString() + ")";
							state.addBatch(strSQL) ;
							state.executeBatch() ;
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("ClientLinkmanMgt deleteClientLinkMan:",e) ;
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
	 * 查询联系人
	 * @param firstName
	 * @param ClientName
	 * @param ClientNo
	 * @param telephone
	 * @param userName
	 * @return
	 */
	public Result queryLinkMan(final String locale,final String firstName,final String ClientName,
			final String clientNo,final String telephone,final String userName,final String clientId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select c.ClientName,det.UserName,det.Telephone,det.id as userId,c.id as clientId,det.Role,det.ClientEmail,det.mainUser,det.Mobile" +
									" from CRMClientInfoDet as det left join CRMClientInfo as c on det.f_ref = c.id where 1=1";
							ArrayList<String> param = new ArrayList<String>();
							if(clientId !=null && !"".equals(clientId)){
								sql += " and c.id = ?" ;
								param.add(clientId) ;
							}
							if(firstName != null && !"".equals(firstName)){
								sql += " and det.UserName like ?";
								param.add(firstName+'%');
							}
							if(ClientName != null && !"".equals(ClientName)){
								sql += " and c.ClientName like ?";
								param.add('%'+ClientName+'%');
							}
							if(clientNo != null && !"".equals(clientNo)){
								sql += " and c.ClientNo = ?";
								param.add(clientNo);
							}
							if(telephone != null && !"".equals(telephone)){
								sql += " and det.Telephone like ?";
								param.add(telephone+'%');
							}
							if(userName != null && !"".equals(userName)){
								sql += " and det.UserName like ?";
								param.add('%'+userName+'%');
							}
							PreparedStatement p = conn.prepareStatement(sql);
							for(int j = 0;j<param.size();j++){
								p.setString(j+1, param.get(j));
							}
							ResultSet rss = p.executeQuery();
							ArrayList<String[]> listClient = new ArrayList<String[]>();
							while(rss.next()){
								String[] strArry = new String[9] ;
								strArry[0] = rss.getString("ClientName");//客户名称
								strArry[1] = rss.getString("UserName");//联系人名
								strArry[2] = rss.getString("Telephone");//联系人电话
								strArry[3] = rss.getString("userId");//联系人ID
								strArry[4] = rss.getString("clientId");//客户ID
								strArry[5] = GlobalsTool.getEnumerationItemsDisplay("duty", rss.getString("Role"),locale);//角色
								strArry[6] = rss.getString("ClientEmail");//邮件
								strArry[7] = GlobalsTool.getEnumerationItemsDisplay("YesNo", rss.getString("mainUser"), locale);//是否主联系人
								strArry[8] = rss.getString("Mobile") ;/*手机*/
								listClient.add(strArry);
							}
							rst.setRetVal(listClient);
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("ClientLinkmanMgt queryLinkMan:",e) ;
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
	 * 查询联系人
	 * @param firstName
	 * @param ClientName
	 * @param ClientNo
	 * @param telephone
	 * @param userName
	 * @return
	 */
	public Result queryLinkMan(final String ClientName,final String telephone,final String userName,final String mobile,
			final int pageSize,final int pageNo,final String userId,final String clientSql) {
		String sql = "select c.ClientName,det.UserName,det.Telephone,det.id as userId,c.id as clientId,det.Role,det.ClientEmail,det.qq,det.Mobile,det.gender ," +
									" c.moduleId from CRMClientInfoDet as det left join CRMClientInfo as c on det.f_ref = c.id where 1=1 ";
		
		sql +=" and det.f_ref in ("+clientSql +")";
		
		ArrayList<String> param = new ArrayList<String>();
		
		/*
		if (firstName != null && !"".equals(firstName)) {
			sql += " and det.UserName like ?";
			param.add(firstName + "%");
		}
		
		if (clientNo != null && !"".equals(clientNo)) {
			sql += " and c.ClientNo like ?";
			param.add("%" + ClientName + "%");
		}
		*/
		if (ClientName != null && !"".equals(ClientName)) {
			sql += " and c.ClientName like ?";
			param.add("%" + ClientName + "%");
		}
		if (telephone != null && !"".equals(telephone)) {
			sql += " and det.Telephone like ?";
			param.add("%" + telephone + "%");
		}
		if (mobile != null && !"".equals(mobile)){
			sql += " and det.mobile like ? ";
			param.add("%" + mobile + "%") ;
		}
		if (userName != null && !"".equals(userName)) {
			sql += " and det.UserName like ?";
			param.add("%" + userName + "%");
		}
		return sqlList(sql, param, pageSize, pageNo, true) ;
	}
	
	

}
