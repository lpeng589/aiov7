/**
 * 科荣软件 短信猫
 */
package com.menyi.web.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.JsonObject;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;

/**
 * <p>
 * Title:短信猫 后台数据库操作类
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:Feb 19, 2014
 * @Copyright: 深圳市科荣软件有限公司
 * @Author PWY
 */
public class SmsMgt extends AIODBManager {
	/**
	 * 返回管理员密码
	 * 
	 * @return
	 */
	public Result queryAdminPsw() {
		String sql = "select password from tblEmployee where id = '1' ";
		return sqlList(sql, new ArrayList());
	}

	/**
	 * 更新相关参数
	 * 
	 * @param interval
	 *            时间间隔
	 * @param maxSend
	 *            每天发送上限
	 * @param allowTime
	 *            发送时间段
	 * @return
	 */
	public Result updateParams(final int interval, final int maxSend,
			final String allowTime) {

		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "update tblSysDeploy set Setting=? where SysCode=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss = conn.prepareStatement(sql);
							pss.setInt(1, interval);
							pss.setString(2, "smsInterval");
							pss.executeUpdate();

							pss = conn.prepareStatement(sql);
							pss.setInt(1, maxSend);
							pss.setString(2, "smsMaxSend");
							pss.executeUpdate();

							pss = conn.prepareStatement(sql);
							pss.setString(1, allowTime);
							pss.setString(2, "smsAllowTime");
							pss.executeUpdate();
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("SmsMgt updateParams mehtod", ex);
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
	 * 添加待发短信记录
	 * 
	 * @param userId
	 *            创建者ID
	 * @param content
	 *            短信内容
	 * @param users
	 *            接收人名字数组
	 * @param mobiles
	 *            接人电话
	 * @return
	 */
	public Result addSms(final String userId, final String content,
			final String[] users, final String[] mobiles,Connection conn) {

		final Result result = new Result();
		try {
			String time = BaseDateFormat.format(new Date(),
					BaseDateFormat.yyyyMMddHHmmss);
			for (int i = 0; i < mobiles.length; i++) {
				String sql = "insert into tblNoSendSMS(id,createBy,createTime,receiveMobile,receiveName,content,status,sendTime) values(?,?,?,?,?,?,?,?)";
				PreparedStatement pss = conn
						.prepareStatement(sql);
				pss.setString(1, IDGenerater.getId());
				pss.setString(2, userId);
				pss.setString(3, time);
				pss.setString(4, mobiles[i]);
				pss.setString(5, null == users ? "" : users[i]);
				pss.setString(6, content);
				pss.setInt(7, 0); // 未发送
				pss.setString(8, time); // 未发送
				pss.executeUpdate();
			}
		} catch (Exception ex) {
			result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
			BaseEnv.log.error("SmsMgt addSms mehtod", ex);
		}
		return result;
	}

	/**
	 * 取出一条最久没发的短信,并标记为正在发送
	 * 
	 * @return
	 */
	public Result querySms(final String sendMobile) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							ResultSet rs;
							// 取出一条最久没发的短信
							String sql = "select top 1 id, receiveMobile, content from tblNoSendSMS where status=? order By createTime ";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setInt(1, 0); // 未发送
							rs = pss.executeQuery();

							if (rs.next()) {
								String id = rs.getString(1);

								JsonObject json = new JsonObject();
								json.addProperty("id", id);
								json.addProperty("mobile", rs.getString(2));
								json.addProperty("content", rs.getString(3));
								result.setRetVal(json);

								// 将这条记录的状态标为正在发送送
								String time = BaseDateFormat.format(new Date(),
										BaseDateFormat.yyyyMMddHHmmss);
								sql = "update tblNoSendSMS set status=?,sendMobile=?,sendTime=? where id=?";
								pss = conn.prepareStatement(sql);
								pss.setInt(1, 1); // 正在发送
								pss.setString(2, sendMobile);
								pss.setString(3, time);
								pss.setString(4, id);
								pss.executeUpdate();
							}
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("SmsMgt querySms mehtod", ex);
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
	 * 把未发送表对应的数据插入已发送表
	 * 
	 * @param id
	 * @param status
	 *            发送结果，2：成功，-1：失败
	 * @return
	 */
	public Result updateSms(final String id, final int status) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "insert into tblSendSMS(id,createBy,createTime,receiveMobile,receiveName,content,sendMobile,sendTime,status,BusinessType) "
									+ "select id,createBy,createTime,receiveMobile,receiveName,content,sendMobile,sendTime,?,0 from tblNoSendSMS where id=? ";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setInt(1, status);
							pss.setString(2, id);
							pss.executeUpdate();
							
							sql = "delete from tblNoSendSMS where id=?";
							pss = conn.prepareStatement(sql);
							pss.setString(1, id);
							pss.executeUpdate();
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("SmsMgt updateSms mehtod", ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
}
