package com.koron.oa.thdesktop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import sun.security.jca.GetInstance;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.MyDeskBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

/**
 * 
 * <p>Title:我的桌面</p> 
 * <p>Description: </p>
 *
 * @Date:Sep 12, 2012
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class THDeskClientMgt extends AIODBManager {
	 
    // 注意，如果要自定义sql语句
    public Result queryDesk(final String sqlStr) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	String sql = "{call proc_defineData (@sql=?)}" ;
                        	PreparedStatement pss = conn.prepareCall(sql) ;
                            pss.setString(1, sqlStr) ;
                            ResultSet rss = pss.executeQuery() ;
                            ArrayList list=new ArrayList();
                            while(rss.next()){
                            	list.add(rss.getString(1));
                            }
	                        rs.setRetVal(list);
	                        rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    
}
