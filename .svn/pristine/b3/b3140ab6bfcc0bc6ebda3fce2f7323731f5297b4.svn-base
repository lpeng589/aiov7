package com.menyi.aio.dyndb;

import java.util.*;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.menyi.aio.bean.CompanyBean;
import com.menyi.web.util.ErrorCanst;
import com.dbfactory.hibernate.DBUtil;
import com.menyi.web.util.BaseEnv;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.hibernate.Session;

import com.dbfactory.hibernate.IfDB;

import java.sql.Connection;

import org.hibernate.jdbc.Work;

import com.menyi.aio.dyndb.DDLOperation;

import java.sql.CallableStatement;

import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.GlobalsTool;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.bean.SetExchangeBean;
import com.menyi.web.util.*;
import com.menyi.aio.bean.AccPeriodBean;
/**
 * <p>Title: </p>
 *
 * <p>Description: 往来期初的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class GlobalMgt extends DBManager  {
    /**
     * 得到所有币种
     * @return Result
     */
    public Result getCurrency(){
        Result rs=null;
        rs=this.list("select bean.id,bean.CurrencyName from CurrencyBean bean where bean.IsBaseCurrency=2",new ArrayList());
        return rs;
    }

    /**
     * 得到本位币
     * @return Result
     */
    public Object[]  getBaseCurrency()throws Exception{
        Result rs=null;
        rs=this.list("select bean.id,bean.CurrencyName from CurrencyBean bean where bean.IsBaseCurrency=1",new ArrayList());
        if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
            if(rs.getRealTotal()>0){
                List list = (List) rs.getRetVal();
                return (Object[]) list.get(0);
            }else{
                return new Object[2];
            }
        }else{
            throw new Exception();
        }
    }

    /**
     * 修改科目余额表中更新了的科目的父类分支机构的数据
     * @param conn Connection
     * @param classCode String  修改的科目余额的分类代码
     * @param sunCompany String 当前登陆的分支机构的分类代码
     */
    public void updateAccParentSum(Connection conn, String classCode,
                                   String sunCompany, int period,int periodYear) throws
            SQLException {
        CallableStatement cbs = conn.prepareCall("{call proc_updateAllBalance(?)}");
        cbs.setString(1, sunCompany);
        cbs.execute();
    }
    /**
     * 修改商品更新了的库存总表的父类分支机构的数据
     * @param conn Connection
     * @param classCode String  修改的商品的分类代码
     * @param sunCompany String 当前登陆的分支机构的分类代码
     */
    public void updateGoodsParentSum(Connection conn,String classCode,String sunCompany) throws SQLException {
        String field="";
        CallableStatement cbs = conn.prepareCall("{call proc_updateSuper(?,?,?,?,?,?,?,?)}");
        cbs.registerOutParameter(7, java.sql.Types.INTEGER);
        cbs.registerOutParameter(8, java.sql.Types.VARCHAR);

        while (classCode.length() >= 5) {
            //修改库存总表商品父类
            cbs.setString(1, "tblStockTotal");
            cbs.setString(2, "tblSunCompanys");
            cbs.setString(3,
                          "tblStockTotal.SCompanyID=tblSunCompanys.classCode");
            cbs.setString(4, "tblStockTotal.GoodsCode='" + classCode + "'");
            field =
                    "Qty=sum(Qty)@SPFieldLink:Price=case sum(Qty)" +
                    " when 0 then 0 else sum(Amount)/sum(Qty) end@SPFieldLink:Amount=sum(Amount)";

            cbs.setString(5, field);
            cbs.setString(6, sunCompany);
            cbs.execute();
            classCode = classCode.substring(0,classCode.length() - 5);
        }
    }
    //取当前会计期间的期间年，月，期间
    public List currPeriod(String sCompanyID){
        List list=new ArrayList();
        ArrayList paramLists=new ArrayList();
        String periods="select bean from  AccPeriodBean bean where bean.statusId=1 and bean.SCompanyID='"+sCompanyID+"' order by bean.AccMonth";
        Result rs=this.list(periods,paramLists);
        if(rs.getRealTotal()>0){
            list=(List)rs.getRetVal();
        }
        return list;
   }

    public  float getCurrencyRate(String currencyID,String sunCompany){
        Result rs=null;
        float exchang=0;
        String sql="select bean.RecordExchange from SetExchangeBean bean "+
                     "where bean.Period=(select bean2.AccPeriod from AccPeriodBean bean2 where bean2.IsBegin=1 and bean2.SCompanyID='"+
                     sunCompany+"') and bean.PeriodYear=(select bean2.AccYear from AccPeriodBean bean2 where bean2.IsBegin=1 and bean2.SCompanyID='"+
                     sunCompany+"') and bean.SCompanyID='"+sunCompany+"' and bean.Currency='"+currencyID+"'";
        rs=this.list(sql,new ArrayList());
        if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
            if(rs.getRealTotal()>0){
                List list=(List)rs.getRetVal();
                Object obj=(Object)list.get(0);
                int digits = Integer.parseInt(BaseEnv.systemSet.
                                       get("digits").getSetting());
                String ex="";
                if (obj == null || obj.toString().length() == 0) {
                     ex= "0";
                }else{
                    ex=obj.toString();
                }
                exchang =Float.parseFloat(GlobalsTool.formatNumberS(new Float(ex), false, false,"Price",""));
            }
        }
        return exchang;
    }
    public Result updateStyle(final String style,final String userId){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            String sql="update tblEmployee set defStyle='"+style+"' where id='"+userId+"'";
                            cs.executeUpdate(sql);
                            SystemSettingBean setting= BaseEnv.systemSet.get("style");
                            setting.setDefaultValue(style);
                            setting.setSetting(style);
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            return  ;
                        }
                    }
                });
                return 0;
            }
         });
        return rs;
    }

    public Result updateLoc(final String loc,final String userId){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            String sql="update tblEmployee set defLoc='"+loc+"' where id='"+userId+"'";
                            int n = cs.executeUpdate(sql);

                           /* SystemSettingBean setting= BaseEnv.systemSet.get("loc");
                            setting.setDefaultValue(loc);
                            setting.setSetting(loc);*/
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            return  ;
                        }
                    }
                });
                return 0;
            }
         });
        return rs;
    }

    public Result updateLocByName(final String loc,final String userName){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            String sql="update tblEmployee set defLoc='"+loc+"' where sysName='"+userName+"'";
                            int n = cs.executeUpdate(sql);

                           /* SystemSettingBean setting= BaseEnv.systemSet.get("loc");
                            setting.setDefaultValue(loc);
                            setting.setSetting(loc);*/
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            return  ;
                        }
                    }
                });
                return 0;
            }
         });
        return rs;
    }

    public static Result getCurrStyle(final String userName){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            String sql="select defStyle from  tblEmployee  where sysName='"+userName+"'";
                            ResultSet rss=cs.executeQuery(sql);
                            if(rss.next()){
                            	rs.setRetVal(rss.getString(1));
                            }
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            return  ;
                        }
                    }
                });
                return 0;
            }
         });
        return rs;
    }
    public static Result getUserInfo(final String userName){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select MACAddress,userKeyId from  tblEmployee  where sysName=? ";
                        	PreparedStatement cs = conn.prepareStatement(sql);
                            cs.setString(1, userName);
                            ResultSet rss=cs.executeQuery();
                            if(rss.next()){
                                String mac=rss.getString("MACAddress");
                                String ukey = rss.getString("userKeyId");
                            	rs.setRetVal("<userStr1>"+(mac==null?"":mac)+"</userStr1>"+"<userKeyId>"+(ukey==null?"":ukey)+"</userKeyId>");
                            }
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            return  ;
                        }
                    }
                });
                return 0;
            }
         });
        return rs;
    }
    public static String getMacList(){
        final Result rs=new Result();
        rs.retVal = "";
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            String sql="select macaddress from  tblmacfilter  where statusId='0' ";
                            ResultSet rss=cs.executeQuery(sql);
                            String mac = "";
                            while(rss.next()){
                                mac +=rss.getString("macaddress")+",";
                            }
                            rs.retVal = mac;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            return  ;
                        }
                    }
                });
                return 0;
            }
         });
        return rs.retVal.toString();
    }
    public static Result getUserLocStyle(final String userName){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            String sql="select defLoc,defStyle from  tblEmployee  where sysName='"+userName+"'";
                            ResultSet rss=cs.executeQuery(sql);
                            if(rss.next()){
                                String defLoc=rss.getString("defLoc");
                                String defStyle = rss.getString("defStyle");
                            	rs.setRetVal("<defLoc>"+(defLoc==null?"":defLoc)+"</defLoc>"+"<defStyle>"+(defStyle==null?"":defStyle)+"</defStyle>");
                            }
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            return  ;
                        }
                    }
                });
                return 0;
            }
         });
        return rs;
    }
    public static Result getCurrLoc(final String userName){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            String sql="select defLoc from  tblEmployee  where sysName='"+userName+"'";
                            ResultSet rss=cs.executeQuery(sql);
                            if(rss.next()){
                            	rs.setRetVal(rss.getString(1));
                            }
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            return  ;
                        }
                    }
                });
                return 0;
            }
         });
        return rs;
    }

    public static Result setPrintCount(final String billTable,final String billId){
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            String sql="update "+billTable+"  set printCount=isnull(printCount,0)+1 where id='"+billId+"'";
                            cs.execute(sql);
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            return  ;
                        }
                    }
                });
                return 0;
            }
         });
        return rs;
    }

    public static int getPrintCount(final String billTable,final String billId){
        final Result rs=new Result();
        DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        int printCount = 0;
                        try {
                            Statement cs = conn.createStatement();
                            String sql = "select printCount from  "+billTable+" where id='"+billId+"'";
                            ResultSet rss=cs.executeQuery(sql);
                            if(rss.next()){
                            	printCount = rss.getInt(1);
                            }
                        } catch (Exception ex) {
                        	printCount = 0;
                            BaseEnv.log.error("GlobalMgt.getPrintCount Error: "+billTable+" 没有printCount字段");
                        }
                        rs.setRetVal(printCount);
                    }
                });
                return 0;
            }
         });
        int printCount = 0;
        try{
        	printCount = Integer.parseInt(rs.retVal.toString());
        }catch (Exception e) {
        	printCount = 0;
		}
        return printCount;
    }
    
}
