/**
 * ������� ����è
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
 * Title:����è ��̨���ݿ������
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:Feb 19, 2014
 * @Copyright: �����п���������޹�˾
 * @Author PWY
 */
public class SmsMgt extends AIODBManager {
	/**
	 * ���ع���Ա����
	 * 
	 * @return
	 */
	public Result queryAdminPsw() {
		String sql = "select password from tblEmployee where id = '1' ";
		return sqlList(sql, new ArrayList());
	}

	/**
	 * ������ز���
	 * 
	 * @param interval
	 *            ʱ����
	 * @param maxSend
	 *            ÿ�췢������
	 * @param allowTime
	 *            ����ʱ���
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
	 * ��Ӵ������ż�¼
	 * 
	 * @param userId
	 *            ������ID
	 * @param content
	 *            ��������
	 * @param users
	 *            ��������������
	 * @param mobiles
	 *            ���˵绰
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
				pss.setInt(7, 0); // δ����
				pss.setString(8, time); // δ����
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
	 * ȡ��һ�����û���Ķ���,�����Ϊ���ڷ���
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
							// ȡ��һ�����û���Ķ���
							String sql = "select top 1 id, receiveMobile, content from tblNoSendSMS where status=? order By createTime ";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setInt(1, 0); // δ����
							rs = pss.executeQuery();

							if (rs.next()) {
								String id = rs.getString(1);

								JsonObject json = new JsonObject();
								json.addProperty("id", id);
								json.addProperty("mobile", rs.getString(2));
								json.addProperty("content", rs.getString(3));
								result.setRetVal(json);

								// ��������¼��״̬��Ϊ���ڷ�����
								String time = BaseDateFormat.format(new Date(),
										BaseDateFormat.yyyyMMddHHmmss);
								sql = "update tblNoSendSMS set status=?,sendMobile=?,sendTime=? where id=?";
								pss = conn.prepareStatement(sql);
								pss.setInt(1, 1); // ���ڷ���
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
	 * ��δ���ͱ��Ӧ�����ݲ����ѷ��ͱ�
	 * 
	 * @param id
	 * @param status
	 *            ���ͽ����2���ɹ���-1��ʧ��
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
