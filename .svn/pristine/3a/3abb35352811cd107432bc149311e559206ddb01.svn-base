package com.menyi.aio.web.upgrade;

import java.util.*;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.ErrorCanst;
import com.dbfactory.hibernate.DBUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.Session;
import java.sql.PreparedStatement;
import com.dbfactory.hibernate.IfDB;
import java.sql.Connection;
import org.hibernate.jdbc.Work;
import java.text.SimpleDateFormat;
import com.menyi.web.util.SecurityLock;
import com.menyi.web.util.BaseEnv;

/**
 * <p>Title: </p>
 *
 * <p>Description: 单位管理的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class RegisterMgt extends DBManager {

    private boolean checkExsit() {
        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String sql =
                            "SELECT   count(*)   FROM   sysobjects   where   name='tblRegisters'";

                        PreparedStatement psmt = conn.prepareStatement(sql);
                        ResultSet rst = psmt.executeQuery();
                        if (rst.next()) {
                            int count = rst.getInt(1);
                            res.retVal = count;
                        } else {
                            res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res.retCode == ErrorCanst.DEFAULT_SUCCESS && Integer.parseInt(res.retVal.toString()) > 0 ? true : false;

    }
    
    public Result continueEval() {
        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String sql =
                            "delete tblRegisters where regFlag<>1";

                        PreparedStatement psmt = conn.prepareStatement(sql);
                        psmt.executeUpdate();
                        
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;

    }
    
    public Result stopUser() {
        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String sql =
                            "update tblEmployee set openFlag=0,sysName=null where id<>'1' ";

                        PreparedStatement psmt = conn.prepareStatement(sql);
                        psmt.executeUpdate();
                        
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;

    }

    /**
     *
     * @param type int 0: 正式版，1 试用版
     * @return boolean
     */
    public Result checkRegister() {
    	if (!checkExsit()) {
            create();
        }

        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;                        
                    	//检查是否有过正式使用
                        String sql =
                            "SELECT   *   FROM   tblRegisters where regFlag = 2";

                        PreparedStatement psmt = conn.prepareStatement(sql);
                        ResultSet rst = psmt.executeQuery();
                        if (rst.next()) {
                        	res.setRetCode(ErrorCanst.DATA_ALREADY_USED);
                        	return ;
                        }
                    	 sql =
                             "SELECT   *   FROM   tblRegisters where regFlag = 1";

                         psmt = conn.prepareStatement(sql);
                         rst = psmt.executeQuery();
                         if (rst.next()) {
                        	 int regflag = rst.getInt("regFlag");
                             String date = rst.getString("evalDate");
                             String no = rst.getString("eNo");
                             if (regflag == 1 && date != null && date.trim().length() != 0 ) {
                                 res.setRetVal(date + ":" + no);
                             }
                         } else {                	
                             //建立时间
                             SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                             String d = sdf.format(new Date());
                             sql = " insert tblRegisters(evalDate,eNo,regFlag) values(?,?,1) ";
                             psmt = conn.prepareStatement(sql);
                             psmt.setString(1, d);
                             psmt.setString(2, "");
                             psmt.executeUpdate();
                             res.setRetVal(d + ":" + "");
                         }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    }

    /**
     * 正式使时时检查最后使用时间，如果时间不存在则建立
     * @return boolean
     */
    public String checkUserTime(final String dNo) {
        if (!checkExsit()) {
            create();
        }
        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String sql =
                            "SELECT   useDate   FROM   tblRegisters where dNo=?";

                        PreparedStatement psmt = conn.prepareStatement(sql);
                        psmt.setString(1, dNo);
                        ResultSet rst = psmt.executeQuery();
                        String d = null;
                        if (rst.next()) {
                            d = rst.getString(1);
                            if (d == null || d.trim().length() == 0) {
                                //建立时间
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                d = sdf.format(new Date());
                                sql = " update tblRegisters set useDate=?,regFlag=2 where dNo=? ";
                                psmt = conn.prepareStatement(sql);
                                psmt.setString(1, d);
                                psmt.setString(2, dNo);
                                psmt.executeUpdate();
                            }
                        }else{                        	
                            //建立时间
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                            d = sdf.format(new Date());
                            sql = " insert tblRegisters(useDate,dNo,regFlag) values(?,?,2) ";
                            psmt = conn.prepareStatement(sql);
                            psmt.setString(1, d);
                            psmt.setString(2, dNo);
                            psmt.executeUpdate();
                        }
                        
                        res.setRetVal(d);
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res.retCode == ErrorCanst.DEFAULT_SUCCESS ? (String) res.getRetVal() : null;
    }

 
    /**
    *
    * @param companyName String
    * @param companyAddress String
    * @param companyUrl String
    * @param connectorName String
    * @param email String
    * @param qq String
    * @param tel String
    * @param regFlag String 1 试用注册，2正式注册,3离线注册，4，重写register表，不更新加密狗
    * @return Result
    */
//   public Result register(final String id, final String companyName, final String companyAddress, final String companyUrl,
//                          final String connectorName, final String email, final String qq,
//                          final String tel, final String date, final int regFlag) {
//       final Result res = new Result();
//       //判断表是否存在，如果不存在创建表。
//       if (!checkExsit()) {
//           if (!create()) {
//               res.retCode = ErrorCanst.DEFAULT_FAILURE;
//               return res;
//           }
//       }
//       if (regFlag == 2 || regFlag == 3) { //正式注册
//           if (!SecurityLock.writeRegiste(date, companyName)) {
//               BaseEnv.log.error("RegisterMgt register write to dog error ");
//               res.retCode = ErrorCanst.DEFAULT_FAILURE;
//               return res;
//           }
//
//       }
//       int retCode = DBUtil.execute(new IfDB() {
//           public int exec(Session session) {
//               session.doWork(new Work() {
//                   public void execute(Connection connection) throws
//                       SQLException {
//                       Connection conn = connection;                        
//                       int rf = regFlag;
//                       if(rf == 3 || rf == 4){
//                       	rf =2;
//                       } 
//                       String sql = "select * from tblRegisters where regFlag=" + rf ;
//                       if (regFlag == 1) { //试用
//                           sql += " and eNo = '" +id + "' ";
//                       } else if (regFlag == 2 ||regFlag == 3 ||regFlag == 4) { //正式注册
//                           sql += " and  dNo = '" + id + "' ";
//                       }
//                       PreparedStatement psmt = conn.prepareStatement(sql);
//                       ResultSet rs = psmt.executeQuery();
//                       if(rs.next()){
//	                       sql = "update tblRegisters set companyName = '" + companyName + "' , companyAddress = '" +
//	                                    companyAddress +
//	                                    "' , companyUrl = '" + companyUrl + "' , connectorName = '" + connectorName + "' , email = '" +
//	                                    email + "' , qq = '" + qq + "' , tel = '" + tel +
//                                    "' , regFlag = " + rf + " , ";
//
//	                       if (regFlag == 1) { //试用
//	                           sql += " evalDate = '" + date + "' , eNo = '" +
//	                               id + "' ";
//	                       } else if (regFlag == 2 ||regFlag == 3 ||regFlag == 4) { //正式注册
//	                           sql += " useDate = '" + date + "' , dNo = '" +
//	                               id + "' ";
//	                       }
//                       }else{
//                    	   sql = "insert into tblRegisters(companyName,companyAddress,companyUrl,connectorName,email,qq,tel,regFlag,evalDate,eNo,useDate,dNo)  values(" +
//                    	   		" '" + companyName + "' ,  '" + companyAddress + "' ,  '" + companyUrl + "' ,  '" + connectorName + "' ,  '" +
//                    	   		email + "' ,   '" + qq + "' ,   '" + tel + "' ,   " + rf + " , ";
//
//			              if (regFlag == 1) { //试用
//			                  sql += "  '" + date + "' , '" +id + "','','') ";
//			              } else if (regFlag == 2 ||regFlag == 3 ||regFlag == 4) { //正式注册
//			                  sql += "  '','', '" + date + "' , '" +
//			                      id + "') ";
//			              }
//                       }
//                       System.out.println("sql=" + sql);
//                       psmt = conn.prepareStatement(sql);
//                       int rst = psmt.executeUpdate();
//                       if (rst < 1) {
//                           res.setRetCode(ErrorCanst.DEFAULT_FAILURE);
//                       }
//                       res.retVal = rst;
//                   }
//               });
//               return res.getRetCode();
//           }
//       });
//       res.setRetCode(retCode);
//       return res;
//   }
  
    private boolean create() {
        final Result res = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String sql =
                            "                        CREATE TABLE [tblRegisters] ( " +
                            "                            [id] [int] IDENTITY (1, 1) NOT NULL , " +
                            "                            [companyName] [varchar] (200) COLLATE Chinese_PRC_CI_AS NULL , " +
                            "                            [companyAddress] [varchar] (200) COLLATE Chinese_PRC_CI_AS NULL , " +
                            "                            [companyUrl] [varchar] (200) COLLATE Chinese_PRC_CI_AS NULL , " +
                            "                            [connectorName] [varchar] (50) COLLATE Chinese_PRC_CI_AS NULL , " +
                            "                            [email] [varchar] (150) COLLATE Chinese_PRC_CI_AS NULL , " +
                            "                            [qq] [varchar] (50) COLLATE Chinese_PRC_CI_AS NULL , " +
                            "                            [tel] [varchar] (50) COLLATE Chinese_PRC_CI_AS NULL , " +
                            "                            [regFlag] [int] NULL , " +
                            "                            [evalDate] [varchar] (50) COLLATE Chinese_PRC_CI_AS NULL , " +
                            "                            [useDate] [varchar] (50) COLLATE Chinese_PRC_CI_AS NULL , " +
                            "                            [dNo] [varchar] (50) COLLATE Chinese_PRC_CI_AS NULL , " +
                            "                            [eNo] [varchar] (50) COLLATE Chinese_PRC_CI_AS NULL  " +
                            ") ON [PRIMARY] ";
                        PreparedStatement psmt = conn.prepareStatement(sql);
                        int r = psmt.executeUpdate();                        
                        res.retVal = r;
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res.retCode == ErrorCanst.DEFAULT_SUCCESS && Integer.parseInt(res.retVal.toString()) > 0 ? true : false;

    }

}
