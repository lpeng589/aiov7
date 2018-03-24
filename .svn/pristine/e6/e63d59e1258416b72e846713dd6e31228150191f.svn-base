package com.menyi.web.util;

import java.rmi.*;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import com.dbfactory.hibernate.DBUtil;
import java.sql.SQLException;
import org.hibernate.Session;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import org.hibernate.jdbc.Work;
import com.dbfactory.Result;
import java.sql.Statement;
import java.sql.PreparedStatement;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.rpc.ServiceException;

public class AIOTelecomCenter {

	public static String smsSign="";

	/**
	 * @param dogId
	 * @param password
	 * @param MsgContent
	 * @param Mobiles
	 * @param SendTime
	 * @param smsType 0:广告类，1：通知类
	 * @return SendResp
	 * @roseuid 49CF15AD033C
	 */
	public int send(String msgContent, String[] mobiles, String userId) {
		saveSMStoDB(msgContent, mobiles, userId );
		return mobiles.length;
	}


	public int send(String msgContent, String[] mobiles, String userId, Connection conn) {
		try {
			saveSMStoDB(msgContent, mobiles, userId, conn);
		} catch (Exception ex) {
		}
		return mobiles.length;
	}

	

	/**
	 * 保存短信到本地数据库
	 * @param msgContent String
	 * @param mobiles String
	 * @param sourceID String
	 * @param msgId String 服务器返回的短信号，用于状态报告返回时的定位
	 * @param result String
	 */
	public void saveSMStoDB(final String msgContent, final String[] mobiles,			
			final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							saveSMStoDB(msgContent, mobiles,  userId,  connection);
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
	}

	public void saveSMStoDB(final String msgContent, final String[] mobiles,  
			final String userId,  Connection conn)
			throws Exception {

		String receiveName = "";
		Statement st = conn.createStatement();
		String ms = "";
		for(String s:mobiles){
			if(s!=null && s.length() > 0){
				ms += "'"+s+"',";
			}
		}
		ms = ms.substring(0,ms.length() -1);
		
		String sql = "select EmpFullName,Mobile from tblEmployee where Mobile in (" + ms + ")";
		ResultSet rss = st.executeQuery(sql);
		HashMap empMap = new HashMap();
		while (rss.next()) {
			empMap.put(rss.getString("Mobile"), rss.getString("EmpFullName"));
		}		
		rss.close();
		
		String[] rcns = new String[mobiles.length];
		for(int i=0;i<mobiles.length;i++){
			rcns[i] = (String)empMap.get(mobiles[i]);
		}		
		SmsMgt smsMgt = new SmsMgt();
		smsMgt.addSms(userId, msgContent, rcns, mobiles, conn);
	}


}
