package com.menyi.aio.web.bol88;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.NoteSetBean;
import com.menyi.aio.bean.SendSMSBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.BusinessException;
import com.menyi.web.util.MailPoolThread;

public class CRMMailObj {
	
	private static ArrayList list = new ArrayList();
	
	public static synchronized String getCRMMail(){
		//如果内存中没有数据，先从数据库中读取
		if(list.size() ==0){
			int retCode = DBUtil.execute(new IfDB() {
	            public int exec(Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                            SQLException {
	                        try {
	                            Connection conn = connection;
	                            String sql =
	                                    "select ClientEmail from CRMClientInfoDet where ClientEmail is not null and len(ClientEmail)>0";
	                            PreparedStatement pstmt = conn.prepareStatement(sql);
	                            ResultSet rs = pstmt.executeQuery();
	                            while (rs.next()) {
	                                list.add(rs.getString(1));
	                            }
	                        } catch (Exception ex) {
	                        	BaseEnv.log.error("CRMMailObj.getCRMMail Error :", ex);	                            
	                            return;
	                        }
	                    }
	                });
	                return 0;
	            }
	        });
		}
		
		if(list.size() > 0){
			return list.remove(0).toString();
		}
		return "";
	}    
}
