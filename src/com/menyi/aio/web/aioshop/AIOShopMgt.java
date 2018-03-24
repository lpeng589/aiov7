/**
 * 
 */
package com.menyi.aio.web.aioshop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.AIOShopBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;

/**
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Jun 2, 2011
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class AIOShopMgt extends DBManager {

	/**
	 * 添加电子商务网站地址
	 * @param shopBean
	 */
	public Result addAIOShop(AIOShopBean shopBean){
		return addBean(shopBean) ;
	}
	
	/**
	 * 添加电子商务网站地址
	 * @param shopBean
	 */
	public Result updateAIOShop(AIOShopBean shopBean){
		return updateBean(shopBean) ;
	}
	
	/**
	 * 查询电子商务网站地址
	 * @return
	 */
	public Result queryAIOShop(){
		return list("from AIOShopBean bean", new ArrayList()) ;
	}
	
	/**
	 * 设置AIOSHOP提醒方式
	 * @param sqls
	 * @return
	 */
	public Result setShopAlert(final String orderMobile,final String orderMsgId,final String orderEmail,
							final String memberMobile,final String memberMsgId,final String memberEmail,
							final String consultMobile,final String consultMsgId,final String consultEmail) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) {
						String sql = "";
						try {
							sql = "delete from tblShopAlert" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.executeUpdate() ;
							
							sql = "insert into tblShopAlert(id,orderMobile,orderMsgId,orderEmail,memberMobile,memberMsgId," +
									"memberEmail,consultMobile,consultMsgId,consultEmail) values(?,?,?,?,?,?,?,?,?,?);" ;
							pss = conn.prepareStatement(sql) ;
							pss.setString(1, IDGenerater.getId()) ;
							pss.setString(2, orderMobile) ;
							pss.setString(3, orderMsgId) ;
							pss.setString(4, orderEmail) ;
							
							pss.setString(5, memberMobile) ;
							pss.setString(6, memberMsgId) ;
							pss.setString(7, memberEmail) ;
							
							pss.setString(8, consultMobile) ;
							pss.setString(9, consultMobile) ;
							pss.setString(10, consultEmail) ;
							pss.executeUpdate() ;
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log.debug("UserFunctionMgt setShopAlert Error sql :"+ sql);
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
	 * 查询AIOSHOP提醒方式
	 * @param sqls
	 * @return
	 */
	public Result queryShopAlert() {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) {
						String sql = "";
						try {
							sql = "select * from tblShopAlert" ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet rss = pss.executeQuery() ;
							if(rss.next()){
								String[] alert = new String[9] ;
								alert[0] = rss.getString(2) ;
								alert[1] = rss.getString(3) ;
								alert[2] = rss.getString(4) ;
								alert[3] = rss.getString(5) ;
								alert[4] = rss.getString(6) ;
								alert[5] = rss.getString(7) ;
								alert[6] = rss.getString(8) ;
								alert[7] = rss.getString(9) ;
								alert[8] = rss.getString(10) ;
								rs.setRetVal(alert) ;
							}else{
								rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.retCode = ErrorCanst.DEFAULT_FAILURE;
							BaseEnv.log.debug("UserFunctionMgt setShopAlert Error sql :"+ sql);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
}
