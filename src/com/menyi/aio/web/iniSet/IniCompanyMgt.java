package com.menyi.aio.web.iniSet;

import java.util.*;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.menyi.aio.bean.CompanyBean;
import com.menyi.web.util.ErrorCanst;
import com.dbfactory.hibernate.DBUtil;
import com.menyi.web.util.BaseEnv;
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
import com.menyi.web.util.GlobalsTool;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.bean.SetExchangeBean;
import com.menyi.web.util.*;
import com.menyi.aio.dyndb.GlobalMgt;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.report.ReportDataMgt;
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
public class IniCompanyMgt extends DBManager  {

    /**
     * 修改单个往来单位的所有的币种的期初信息
     * @return Result
     */
    public Result updateCompany(final String loginId,final String sunCompany,final String  companyCode,final String []year,
                        final String []borrow,
                        final String []lend,
                        final String []balance,
                        final String type,
                        final String []currencyID,
                        final String []currencyRate,
                        final String []curyear,final String []curborrow,final String []curlend,final String []curbalance){
       final Result rs=new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection conn) throws
                           SQLException {

                           Statement st = conn.createStatement();
                           ResultSet rss = null;
                           String sql = "";
                           //查询此往来单位的期初信息
                           sql = "select id, ReceiveTotalRemain ,PayTotalRemain,PrePayTotalRemain,PreReceiveTotalRemain,currencyID "
                                 +" from tblCompanyTotal where companyCode='" +
                                 companyCode + "' and period=-1";

                           rss = st.executeQuery(sql);
                           ArrayList list = new ArrayList();
                           while (rss.next()) {
                               Object[] obj = new Object[6];
                               obj[0] = rss.getString(1);
                               obj[1] = rss.getDouble(2);
                               obj[2] = rss.getDouble(3);
                               obj[3] = rss.getDouble(4);
                               obj[4] = rss.getDouble(5);
                               obj[5] = rss.getString(6);
                               list.add(obj);
                           }

                           String longTime = BaseDateFormat.format(new Date(),
                                   BaseDateFormat.yyyyMMddHHmmss);

                           //更新此往来单位的期初金额
                           if(currencyID!=null){//启用了外币核算
                               for (int i = 0; i < list.size(); i++) {
                                   Object[] obj = (Object[]) list.get(i);
                                   boolean flag = false;
                                   for (int j = 0; j < currencyID.length; j++) {
                                       String cur1 = "";
                                       String cur2 = "";
                                       if (obj[5] == null){
                                           cur1 = "";
                                           obj[5]="";
                                       }
                                       else
                                           cur1 = obj[5].toString();
                                       if (currencyID[j] == null)
                                           cur2 = "";
                                       else
                                           cur2 = currencyID[j];
                                       if (cur1.equals(cur2)) {
                                           flag = true;
                                           break;
                                       }
                                   }
                                   double receive = Double.parseDouble(obj[1].toString());
                                   double pay = Double.parseDouble(obj[2].toString());
                                   double preReceive = Double.parseDouble(obj[3].
                                                                       toString());
                                   double prePay = Double.parseDouble(obj[4].toString());
                                   sql = "";
                                   if (!flag) { //如果列表中已经删除此币种的期初，
                                       if (type.equals("receive")) {
                                           if (pay != 0 || preReceive != 0 ||
                                               prePay != 0) {
                                               sql = "update tblCompanyTotal  set ReceiveBegin=0,ReceiveTotalDebit=0, ReceiveTotalLend=0,ReceiveTotalRemain=0,CurReceiveBegin=0,CurReceiveTotalDebit=0,CurReceiveTotalLend=0,CurReceiveTotalRemain=0";
                                           }
                                       } else if (type.equals("pay")) {
                                           if (receive != 0 || preReceive != 0 ||
                                               prePay != 0) {
                                               sql = "update tblCompanyTotal set PayBegin=0,PayTotalDebit=0, PayTotalLend=0, PayTotalRemain=0,CurPayBegin=0,CurPayTotalDebit=0,CurPayTotalLend=0,CurPayTotalRemain=0 ";
                                           }
                                       } else if (type.equals("preReceive")) {
                                           if (pay != 0 || receive != 0 ||
                                               prePay != 0) {
                                               sql = "update tblCompanyTotal set PreReceiveBegin=0,PreReceiveTotalDebit=0,PreReceiveTotalLend=0,PreReceiveTotalRemain=0,CurPreReceiveBegin=0,CurPreReceiveTotalDebit=0,CurPreReceiveTotalLend=0,CurPreReceiveTotalRemain=0 ";
                                           }
                                       } else if (type.equals("prePay")) {
                                           if (pay != 0 || receive != 0 ||
                                               prePay != 0) {
                                               sql = "update tblCompanyTotal set PrePayBegin=0,PrePayTotalDebit=0, PrePayTotalLend=0, PrePayTotalRemain=0,CurPrePayBegin=0,CurPrePayTotalDebit=0,CurPrePayTotalLend=0,CurPrePayTotalRemain=0";
                                           }
                                       }
                                       if (sql.length() == 0) {
                                           if (sunCompany == null ||
                                               sunCompany.length() == 0) {
                                               sql =
                                                       "delete from tblCompanyTotal where companyCode='" +
                                                       companyCode +
                                                       "' and period=-1 and currencyID='" +
                                                       obj[5] + "'";
                                           } else {
                                               sql =
                                                       "delete from tblCompanyTotal where companyCode='" +
                                                       companyCode +
                                                       "' and SCompanyID='" +
                                                       sunCompany +
                                                       "' and period=-1 and currencyID='" +
                                                       obj[5] + "'";
                                           }

                                       } else {
                                           if (sunCompany == null ||
                                               sunCompany.length() == 0) {
                                               sql += " where companyCode='" +
                                                       companyCode +
                                                       "' and period=-1 and currencyID='" +
                                                       obj[5] + "'";
                                           } else {
                                               sql += "  where companyCode='" +
                                                       companyCode +
                                                       "' and SCompanyID='" +
                                                       sunCompany +
                                                       "' and period=-1 and currencyID='" +
                                                       obj[5] + "'";
                                           }

                                       }
                                       st.execute(sql);
                                   }
                               }

                               for (int i = 0; i < currencyID.length; i++) {
                                   String id = "";
                                   for (int j = 0; j < list.size(); j++) {
                                       Object[] obj = (Object[]) list.get(j);
                                       String cur1 = "";
                                       String cur2 = "";
                                       if (obj[5] == null)
                                           cur1 = "";
                                       else
                                           cur1 = obj[5].toString();

                                       if (currencyID[i] == null){
                                           cur2 = "";
                                           currencyID[i]="";
                                       }
                                       else
                                           cur2 = currencyID[i];

                                       if (cur1.equals(cur2)) {
                                           id = obj[0].toString();
                                           break;
                                       }
                                   }

                                   if (id.length() > 0) { //在数据库中往来单位的此币种的信息已存在（修改）
                                       if (type.equals("receive")) {
                                           sql =
                                                   "update tblCompanyTotal set  ReceiveBegin=" +
                                                   year[i] +
                                                   ", ReceiveTotalDebit=" +
                                                   borrow[i] +
                                                   ", ReceiveTotalLend=" +
                                                   lend[i] +
                                                   ", ReceiveTotalRemain=" +
                                                   balance[i] +
                                                   ",CurReceiveBegin=" +
                                                   curyear[i] +
                                                   ", CurReceiveTotalDebit=" +
                                                   curborrow[i] +
                                                   ", CurReceiveTotalLend=" +
                                                   curlend[i] +
                                                   ", CurReceiveTotalRemain=" +
                                                   curbalance[i];
                                       } else if (type.equals("pay")) {
                                           sql =
                                                   "update tblCompanyTotal set  PayBegin=" +
                                                   year[i] + ", PayTotalDebit=" +
                                                   borrow[i] +
                                                   ", PayTotalLend=" +
                                                   lend[i] +
                                                   ", PayTotalRemain=" +
                                                   balance[i] +
                                                   ",CurPayBegin=" + curyear[i] +
                                                   ", CurPayTotalDebit=" +
                                                   curborrow[i] +
                                                   ", CurPayTotalLend=" +
                                                   curlend[i] +
                                                   ", CurPayTotalRemain=" +
                                                   curbalance[i];
                                       } else if (type.equals("preReceive")) {
                                           sql =
                                                   "update tblCompanyTotal set  PreReceiveBegin=" +
                                                   year[i] +
                                                   ", PreReceiveTotalDebit=" +
                                                   borrow[i] +
                                                   ", PreReceiveTotalLend=" +
                                                   lend[i] +
                                                   ", PreReceiveTotalRemain=" +
                                                   balance[i] +
                                                   ",CurPreReceiveBegin=" +
                                                   curyear[i] +
                                                   ", CurPreReceiveTotalDebit=" +
                                                   curborrow[i] +
                                                   ", CurPreReceiveTotalLend=" +
                                                   curlend[i] +
                                                   ", CurPreReceiveTotalRemain=" +
                                                   curbalance[i];

                                       } else if (type.equals("prePay")) {
                                           sql =
                                                   "update tblCompanyTotal set  PrePayBegin=" +
                                                   year[i] +
                                                   ", PrePayTotalDebit=" +
                                                   borrow[i] +
                                                   ", PrePayTotalLend=" +
                                                   lend[i] +
                                                   ", PrePayTotalRemain=" +
                                                   balance[i] +
                                                   ",CurPrePayBegin=" +
                                                   curyear[i] +
                                                   ", CurPrePayTotalDebit=" +
                                                   curborrow[i] +
                                                   ", CurPrePayTotalLend=" +
                                                   curlend[i] +
                                                   ", CurPrePayTotalRemain=" +
                                                   curbalance[i];

                                       }
                                       sql += ",currencyRate="+currencyRate[i]+"  where id='" + id + "'";
                                   } else { //此币种不存在 （新增）
                                       sql = "insert into tblCompanyTotal (id,CompanyCode,Period,PeriodYear,PeriodMonth,ReceiveBegin,ReceiveTotalDebit" +
                                             ",ReceiveTotalLend,ReceiveTotalRemain,PayBegin,PayTotalDebit,PayTotalLend,PayTotalRemain,PrePayBegin" +
                                             ",PrePayTotalDebit,PrePayTotalLend,PrePayTotalRemain,PreReceiveBegin,PreReceiveTotalDebit,PreReceiveTotalLend" +
                                             ",PreReceiveTotalRemain,YReceiveTotalDebit,YReceiveTotalLend,YPayTotalDebit,YPayTotalLend,YPreReceiveTotalDebit,YPreReceiveTotalLend," +
                                             "YPrePayTotalDebit,YPrePayTotalLend,CurReceiveBegin,CurReceiveTotalDebit" +
                                             ",CurReceiveTotalLend,CurReceiveTotalRemain,CurPayBegin,CurPayTotalDebit,CurPayTotalLend,CurPayTotalRemain,CurPrePayBegin" +
                                             ",CurPrePayTotalDebit,CurPrePayTotalLend,CurPrePayTotalRemain,CurPreReceiveBegin,CurPreReceiveTotalDebit,CurPreReceiveTotalLend" +
                                             ",CurPreReceiveTotalRemain,CurYReceiveTotalDebit,CurYReceiveTotalLend,CurYPayTotalDebit,CurYPayTotalLend,CurYPreReceiveTotalDebit,CurYPreReceiveTotalLend," +
                                             "CurYPrePayTotalDebit,CurYPrePayTotalLend,currencyID,currencyRate,SCompanyID,createBy,lastUpdateBy,createTime,lastUpdateTime) values ('" +
                                             IDGenerater.getId() + "','" +
                                             companyCode + "',-1,-1,-1,";
                                       if (type.equals("receive")) {
                                           sql += year[i] + "," + borrow[i] +
                                                   "," +
                                                   lend[i] + "," + balance[i] +
                                                   ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
                                                   curyear[i] + "," +
                                                   curborrow[i] +
                                                   "," + curlend[i] + "," +
                                                   curbalance[i] +
                                                   ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,";
                                       } else if (type.equals("pay")) {
                                           sql += "0,0,0,0," + year[i] + "," +
                                                   borrow[i] + "," + lend[i] +
                                                   "," +
                                                   balance[i] +
                                                   ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
                                                   curyear[i] + "," +
                                                   curborrow[i] +
                                                   "," + curlend[i] + "," +
                                                   curbalance[i] +
                                                   ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,";
                                       } else if (type.equals("prePay")) {
                                           sql += "0,0,0,0,0,0,0,0," + year[i] +
                                                   "," + borrow[i] + "," +
                                                   lend[i] +
                                                   "," + balance[i] +
                                                   ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
                                                   curyear[i] + "," +
                                                   curborrow[i] +
                                                   "," + curlend[i] + "," +
                                                   curbalance[i] +
                                                   ",0,0,0,0,0,0,0,0,0,0,0,0,";
                                       } else if (type.equals("preReceive")) {
                                           sql += "0,0,0,0,0,0,0,0,0,0,0,0," +
                                                   year[i] + "," + borrow[i] +
                                                   "," +
                                                   lend[i] + "," + balance[i] +
                                                   ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0," +
                                                   curyear[i] + "," +
                                                   curborrow[i] +
                                                   "," + curlend[i] + "," +
                                                   curbalance[i] +
                                                   ",0,0,0,0,0,0,0,0,";
                                       }
                                       sql += "'" + currencyID[i] + "'," +
                                               currencyRate[i] + ",'" +
                                               sunCompany +
                                               "','" + loginId + "','" +
                                               loginId +
                                               "','" + longTime + "','" +
                                               longTime +
                                               "')";
                                   }
                                   st.execute(sql);
                               }
                           }else{
                               if (type.equals("receive")) {
                                           sql =
                                                   "update tblCompanyTotal set  ReceiveBegin=" +
                                                   year[0] +
                                                   ", ReceiveTotalDebit=" +
                                                   borrow[0] +
                                                   ", ReceiveTotalLend=" +
                                                   lend[0] +
                                                   ", ReceiveTotalRemain=" +
                                                   balance[0]+"" ;
                                       } else if (type.equals("pay")) {
                                           sql =
                                                   "update tblCompanyTotal set  PayBegin=" +
                                                   year[0] + ", PayTotalDebit=" +
                                                   borrow[0] +
                                                   ", PayTotalLend=" +
                                                   lend[0] +
                                                   ", PayTotalRemain=" +
                                                   balance[0] ;
                                       } else if (type.equals("prePay")) {
                                           sql =
                                                   "update tblCompanyTotal set  PrePayBegin=" +
                                                   year[0] +
                                                   ", PrePayTotalDebit=" +
                                                   borrow[0] +
                                                   ", PrePayTotalLend=" +
                                                   lend[0] +
                                                   ", PrePayTotalRemain=" +
                                                   balance[0] ;
                                       } else if (type.equals("preReceive")) {
                                           sql =
                                                   "update tblCompanyTotal set  PreReceiveBegin=" +
                                                   year[0] +
                                                   ", PreReceiveTotalDebit=" +
                                                   borrow[0] +
                                                   ", PreReceiveTotalLend=" +
                                                   lend[0] +
                                                   ", PreReceiveTotalRemain=" +
                                                   balance[0] ;
                                       }


                                       sql += " where companyCode='" +
                                               companyCode +
                                               "' and SCompanyID='" +
                                               sunCompany + "' and period=-1";

                                       st.execute(sql);

                           }
                           //得到往来明细中要修改的数据和要新增的数据
                           sql="select id from tblCompanyIni where CompanyCode='"+companyCode+"' and SCompanyID='"+sunCompany+"' and period=-1";
                           rss=st.executeQuery(sql);
                           ArrayList delList=new ArrayList();
                           while(rss.next()){
                        	   delList.add(rss.getString(1));
                           }
                           // 向往来明细中新增数据
                           sql="select ReceiveTotalRemain,payTotalRemain,prepaytotalRemain,prereceivetotalRemain,CurReceiveTotalRemain,CurpayTotalRemain,"+
               	                   "CurprepaytotalRemain,CurprereceivetotalRemain,currencyRate,currencyID from tblCompanyTotal where CompanyCode='"+companyCode+"' and SCompanyID='"+sunCompany+"' and period=-1";
                           rss=st.executeQuery(sql);
                           ArrayList addList=new ArrayList();
                           while(rss.next()){
                        	   String[] ini = new String[10];
                        	   for (int i = 0; i<=8; i++) {
                        		   ini[i] = String.valueOf(rss.getDouble(i +1));
               	               }
                        	   ini[9]=rss.getString(10);
                        	   addList.add(ini);
               	           }   
                           IniCompanyMgt mgt=new IniCompanyMgt();
                           Result rs2=mgt.setCompanyLast(conn, companyCode, sunCompany, loginId, delList, addList, type);
                           rs.setRetCode(rs2.getRetCode());
                           //修改父类数据
                           try{
                           		mgt.updateAfter(conn,sunCompany,companyCode,type);
                   		   }catch(Exception ex){
                   			   ex.printStackTrace();
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
     * 修改往来明细以后的结存数，以及往来总表中的数据
     * @param conn
     * @param companyCode
     * @param sunCompany
     * @param loginId
     * @param delList
     * @param addList
     * @param type
     * @return
     */
    public Result setCompanyLast(Connection conn,String companyCode,String sunCompany,String loginId, ArrayList delList,ArrayList addList,String type){
    	//修改此往来单位以后的结存数
        //删除往来明细中的数据
    	Result rs=new Result();
    	try{
	        CallableStatement cbs=conn.prepareCall("{call proc_deleteComIniUb(?,?,?,?)}");	        
	        CallableStatement updateComTotalcbs=conn.prepareCall("{call proc_updateCompanyTotal(?,?)}");
	        cbs.registerOutParameter(3,java.sql.Types.INTEGER);
	        cbs.registerOutParameter(4,java.sql.Types.VARCHAR);
        	Statement st=conn.createStatement();

	        for(int i=0;i<delList.size();i++){
	     	   String id=delList.get(i).toString();
	     	   if(type.equals("pay")){
	     		   //修改应付                        	   
	     		   cbs.setString(1,id);
	     		   cbs.setInt(2,1);
	     		   cbs.execute();
	     	   }else if(type.equals("receive")){
	     		   //修改应收
	     		   cbs.setString(1,id);
	     		   cbs.setInt(2,2);
	     		   cbs.execute();
	     	   }else if(type.equals("prePay")){
	     		   //修改预付
	     		   cbs.setString(1,id);
	     		   cbs.setInt(2,3);
	     		   cbs.execute();
	     	   }else if(type.equals("preReceive")){
	     		   //修改预收
	     		   cbs.setString(1,id);
	     		   cbs.setInt(2,4);
	     		   cbs.execute();
	     	   }
	     	   updateComTotalcbs.setString(1, id);
	     	   updateComTotalcbs.setString(2, "delete");
	     	   updateComTotalcbs.execute();
	        }
	        String sql="";
	    	String longTime = BaseDateFormat.format(new Date(),
	                BaseDateFormat.yyyyMMddHHmmss);
	    	String ids="";
	    	for(int i=0;i<delList.size();i++){
	    		ids+="'"+delList.get(i)+"',";
	    	}
	    	if(ids.length()>0){
		    	ids=ids.substring(0,(ids.length()-1));
		    	sql="delete from tblCompanyIni where  id in ("+ids+")";
		    	st.execute(sql);
	    	}
	    	cbs=conn.prepareCall("{call proc_addComBalance(?,?,?,?)}");	
	        for(int i=0;i<addList.size();i++){
	     	   String []ini=(String[])addList.get(i);
	     	   String id=IDGenerater.getId();
	            sql="insert into tblCompanyIni (id,CompanyCode,ReceiveBegin,ReceiveTotalDebit,"+
	                  "ReceiveTotalLend,ReceiveTotalRemain,PayBegin,PayTotalDebit,PayTotalLend,PayTotalRemain"+
	                  ",PreReceiveBegin,PreReceiveTotalDebit,PreReceiveTotalLend,PreReceiveTotalRemain,PrePayBegin,PrePayTotalDebit,"+
	                  "PrePayTotalLend,PrePayTotalRemain,Period,periodYear,periodMonth,ItemNo,BillDate,createBy,"+
	                  "lastUpdateBy,createTime,lastUpdateTime,SCompanyID,CurrencyTypeID,CurrencyRate,FcRecBegin,"+
	                  "FcRecTotalDebit,FcRecTotalCredit,FcRecTotalRemain,FcPayBegin,FcPayTotalDebit,FcPayTotalCredit,FcPayTotalRemain,FcPrePayBegin,"+
	                  "FcPrePayTotalDebit,FcPrePayTotalCredit,FcPrePayTotalRemain,"+
	                  "FcPreRecBegin,FcPreRecTotalDebit,FcPreRecTotalCredit"+
	                  ",FcPreRecTotalRemain,BillType)"+
	                  "values ('"+id+"','"+companyCode+"',0,"+ini[0]+",0,"+ini[0]+",0,0,"+ini[1]+","+ini[1]+",0,0,"+ini[3]+
	                  ","+ini[3]+",0,"+ini[2]+",0,"+ini[2]+",-1,-1,-1,1,'','"+loginId+"','"+loginId+"','"+longTime+"','"+
	                  longTime+"','"+sunCompany+"','"+ini[9]+"',"+ini[8]+",0,"+ini[4]+",0,"+ini[4]+",0,0,"+ini[5]+
	                  ","+ini[5]+",0,"+ini[6]+",0,"+ini[6]+",0,0,"+ini[7]+","+ini[7]+",'-1')";
	             st.execute(sql);
	
	             if(type.equals("pay")){	                            	 
	                //更新应付
	                cbs.setString(1,id);
	                cbs.setInt(2,1);
	                cbs.setString(3,"");
	                cbs.setString(4,companyCode);
	             }
	
	             if(type.equals("receive")){
	                //更新应收
	                cbs.setString(1,id);
	                cbs.setInt(2,2);
	                cbs.setString(3,"");
	                cbs.setString(4,companyCode);
	             }
	              
	             if(type.equals("prePay")){
	                //更新预付
	                cbs.setString(1,id);
	                cbs.setInt(2,3);
	                cbs.setString(3,"");
	                cbs.setString(4,companyCode);
	             }
	
	             if(type.equals("preReceive")){
	                //更新预收
	                cbs.setString(1,id);
	                cbs.setInt(2,4);
	                cbs.setString(3,"");
	                cbs.setString(4,companyCode);
	             }
	             cbs.execute();
	             updateComTotalcbs.setString(1, id);
		     	 updateComTotalcbs.setString(2, "add");
		     	 updateComTotalcbs.execute();
	        }
        }catch(Exception ex){
        	ex.printStackTrace();
        	rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        }
    	return rs;
    }
    /**
     * 修改往来单位期初值
     * @param id long
     * @param name String
     * @return Result
     */
    public Result update(final String sunCompany,final String id,
                         final double year,
                         final double borrow,
                         final double lend,
                         final double balance,
                         final String type,
                         final String currencyID,
                         final double currencyRate,
                         final double curyear,final double curborrow,final double curlend,final double curbalance,final String loginId) {

        String temp = "";

            if (type.equals("receive")) {
                temp = " ReceiveBegin=" + year + ",ReceiveTotalDebit=" + borrow +
                       ",ReceiveTotalLend=" + lend + ",ReceiveTotalRemain=" +
                       balance+",CurReceiveBegin=" + curyear + ",CurReceiveTotalDebit=" + curborrow +
                       ",CurReceiveTotalLend=" + curlend + ",CurReceiveTotalRemain=" +
                       curbalance;
            } else if (type.equals("pay")) {
                temp = " PayBegin=" + year + ",PayTotalDebit=" + borrow +
                       ",PayTotalLend=" + lend + ",PayTotalRemain=" + balance+
                       ",CurPayBegin=" + curyear + ",CurPayTotalDebit=" + curborrow +
                       ",CurPayTotalLend=" + curlend + ",CurPayTotalRemain=" + curbalance;
            } else if (type.equals("preReceive")) {
                temp = " PreReceiveBegin=" + year + ",PreReceiveTotalDebit=" +
                       borrow + ",PreReceiveTotalLend=" + lend +
                       ",PreReceiveTotalRemain=" + balance+",CurPreReceiveBegin=" +
                       curyear + ",CurPreReceiveTotalDebit=" +
                       curborrow + ",CurPreReceiveTotalLend=" + curlend +
                       ",CurPreReceiveTotalRemain=" + curbalance;
            } else if (type.equals("prePay")) {
                temp = " PrePayBegin=" + year + ",PrePayTotalDebit=" + borrow +
                       ",PrePayTotalLend=" + lend + ",PrePayTotalRemain=" + balance+
                       ",CurPrePayBegin=" + curyear + ",CurPrePayTotalDebit=" + curborrow +
                       ",CurPrePayTotalLend=" + curlend + ",CurPrePayTotalRemain=" + curbalance;
            }
            //设置SQL修改往来单位汇总表中期初值
            final String sql = "update tblCompanyTotal set  currencyID='"+currencyID+"',currencyRate="+
                               currencyRate+"," + temp +
                               " where id='" +id+ "' ";

            //修改记录

            final Result rs=new Result();
            int retCode = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                                SQLException {
                            Connection conn = connection;
                            try {
                                Statement cs = conn.createStatement();
                                String companyCode="";
                                //得到往来单位的CLASSCODE
                                String sqlCode="select CompanyCode from tblCompanyTotal where id='"+id+"'";
                                ResultSet rset=cs.executeQuery(sqlCode);
                                if(rset.next()){
                                    companyCode=rset.getString(1);
                                }
                                //修改往来单位汇总表中期初值
                                cs.execute(sql);
                                
                                //得到往来明细中要修改的数据和要新增的数据
                                ArrayList delList=new ArrayList();
                                String sql="select id from tblCompanyIni where SCompanyID='"+sunCompany+
                                	"' and companyCode='"+companyCode+"' and period=-1 and CurrencyTypeID='"+currencyID+"'";
                                ResultSet rss=cs.executeQuery(sql);
                                if(rss.next()){
                                	delList.add(rss.getString(1));
                                }
                                // 向往来明细中新增数据
                                sql="select ReceiveTotalRemain,payTotalRemain,prepaytotalRemain,prereceivetotalRemain,CurReceiveTotalRemain,CurpayTotalRemain,"+
                    	                   "CurprepaytotalRemain,CurprereceivetotalRemain,currencyRate,currencyID from tblCompanyTotal where id='"+id+"'";
                                rss=cs.executeQuery(sql);
                                ArrayList addList=new ArrayList();
                                if(rss.next()){
                             	   String[] ini = new String[10];
                             	   for (int i = 0; i<=8; i++) {
                             		   ini[i] = String.valueOf(rss.getDouble(i +1));
                    	               }
                             	   ini[9]=rss.getString(10);
                             	   addList.add(ini);
                    	        }
                                
                                IniCompanyMgt mgt=new IniCompanyMgt();
                                mgt.setCompanyLast(conn, companyCode, sunCompany, loginId, delList, addList, type);
                                mgt.updateAfter(conn,sunCompany,companyCode,type);

                                rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                rs.setRetCode(ErrorCanst.
                                               DEFAULT_FAILURE);
                                return;
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
     * 修改当前的往来单位期初数据后，相应修改此往来单位的父级数据，修改应收，应付，预收，预付科目余额信息，如果启用了分支机构修改相应父类分支机构的数据
     * @param conn Connection
     * @param sunCompany String
     * @param companyCode String
     * @param currencyId String
     * @param type String
     * @throws SQLException
     */
    public void updateAfter(Connection conn,String sunCompany,String companyCode,String type) throws
            SQLException {
        Statement cs = conn.createStatement();
        String query="";
        CallableStatement cbs = conn.prepareCall(
                "{call proc_updateSuper(?,?,?,?,?,?,?,?)}");
        cbs.registerOutParameter(7, java.sql.Types.INTEGER);
        cbs.registerOutParameter(8, java.sql.Types.VARCHAR);

        String field = "ReceiveBegin=sum(ReceiveBegin)@SPFieldLink:ReceiveTotalDebit=sum(ReceiveTotalDebit)@SPFieldLink:ReceiveTotalLend=sum(ReceiveTotalLend)" +
                       "@SPFieldLink:ReceiveTotalRemain=sum(ReceiveTotalRemain)@SPFieldLink:PayBegin=sum(PayBegin)@SPFieldLink:PayTotalDebit=sum(PayTotalDebit)@SPFieldLink:PayTotalLend=sum(PayTotalLend)" +
                       "@SPFieldLink:PayTotalRemain=sum(PayTotalRemain)@SPFieldLink:PreReceiveBegin=sum(PreReceiveBegin)" +
                       "@SPFieldLink:PreReceiveTotalDebit=sum(PreReceiveTotalDebit)@SPFieldLink:PreReceiveTotalLend=sum(PreReceiveTotalLend)" +
                       "@SPFieldLink:PreReceiveTotalRemain=sum(PreReceiveTotalRemain)@SPFieldLink:PrePayBegin=sum(PrePayBegin)" +
                       "@SPFieldLink:PrePayTotalDebit=sum(PrePayTotalDebit)@SPFieldLink:PrePayTotalLend=sum(PrePayTotalLend)" +
                       "@SPFieldLink:PrePayTotalRemain=sum(PrePayTotalRemain)@SPFieldLink:CurReceiveBegin=sum(CurReceiveBegin)"+
                       "@SPFieldLink:CurReceiveTotalDebit=sum(CurReceiveTotalDebit)@SPFieldLink:CurReceiveTotalLend=sum(CurReceiveTotalLend)" +
                       "@SPFieldLink:CurReceiveTotalRemain=sum(CurReceiveTotalRemain)@SPFieldLink:CurPayBegin=sum(CurPayBegin)@SPFieldLink:CurPayTotalDebit=sum(CurPayTotalDebit)@SPFieldLink:CurPayTotalLend=sum(CurPayTotalLend)" +
                       "@SPFieldLink:CurPayTotalRemain=sum(CurPayTotalRemain)@SPFieldLink:CurPreReceiveBegin=sum(CurPreReceiveBegin)" +
                       "@SPFieldLink:CurPreReceiveTotalDebit=sum(CurPreReceiveTotalDebit)@SPFieldLink:CurPreReceiveTotalLend=sum(CurPreReceiveTotalLend)" +
                       "@SPFieldLink:CurPreReceiveTotalRemain=sum(CurPreReceiveTotalRemain)@SPFieldLink:CurPrePayBegin=sum(CurPrePayBegin)" +
                       "@SPFieldLink:CurPrePayTotalDebit=sum(CurPrePayTotalDebit)@SPFieldLink:CurPrePayTotalLend=sum(CurPrePayTotalLend)" +
                       "@SPFieldLink:CurPrePayTotalRemain=sum(CurPrePayTotalRemain)";
        //存储过程参数1：修改的表名，参数2：分类的表(没有为null)，参数3：前面两个表关联的条件 (没有为null)，参数4：修改依据的条件 (没有为null)，参数5：要修改的字段，参数6：要修改的分类代码

        //修改往来单位汇总中父类
        cbs.setString(1, "tblCompanyTotal");
        cbs.setString(2, "tblCompany");
        cbs.setString(3, "tblCompanyTotal.CompanyCode=tblCompany.classCode");

        query = "period=-1 and tblCompanyTotal.SCompanyID='" +
                    sunCompany + "'";

        cbs.setString(4, query);
        cbs.setString(5, field);
        cbs.setString(6, companyCode);
        cbs.execute();

        //修改应收应付科目余额，科目表
        StringBuffer sql = new StringBuffer("");
        sql.append("select sum(ReceiveTotalRemain) IniReceive ,sum(ReceiveBegin) receiveYear,sum(ReceiveTotalDebit) receiveDebit,sum(ReceiveTotalLend) receiveCredit,");
        sql.append("sum(PayTotalRemain) IniPay,sum(PayBegin) payYear,sum(PayTotalDebit) payDebit,sum(PayTotalLend) payCredit,sum(PreReceiveTotalRemain) IniPreReceive,");
        sql.append("sum(PreReceiveBegin) preReceiveYear,sum(PreReceiveTotalDebit) preReceiveDebit,sum(PreReceiveTotalLend) preReceiveCredit,");
        sql.append("sum(PrePayTotalRemain) IniPrePay,sum(PrePayBegin) prePayYear,sum(PrePayTotalDebit) prePayDebit,sum(PrePayTotalLend) prePayCredit ");

        sql.append(" from tblCompanyTotal  where len(CompanyCode)=5 and period=-1").append("  and SCompanyID='" + sunCompany + "'");

        String currYIniBase = "0";
        String currYIniDebitSumBase = "0";
        String currYIniCreditSumBase = "0";
        String currYIniBalaBase ="0";

        String subCode = "";
        ResultSet rss = cs.executeQuery(sql.toString());
        if (rss.next()) {
            if (type.equals("receive")) {
                currYIniBalaBase = rss.getString(
                        "IniReceive");
                currYIniBase = rss.getString("receiveYear");
                currYIniDebitSumBase = rss.getString(
                        "receiveDebit");
                currYIniCreditSumBase = rss.getString(
                        "receiveCredit");

                subCode = "1122";
            }
            if (type.equals("pay")) {
                currYIniBalaBase = rss.getString("IniPay");
                currYIniBase = rss.getString("payYear");
                currYIniDebitSumBase = rss.getString(
                        "payDebit");
                currYIniCreditSumBase = rss.getString(
                        "payCredit");

                subCode = "2202";
            }
            if (type.equals("preReceive")) {
                currYIniBalaBase = rss.getString(
                        "IniPreReceive");
                currYIniBase = rss.getString(
                        "preReceiveYear");
                currYIniDebitSumBase = rss.getString(
                        "preReceiveDebit");
                currYIniCreditSumBase = rss.getString(
                        "preReceiveCredit");

                subCode = "2203";
            }
            if (type.equals("prePay")) {
                currYIniBalaBase = rss.getString(
                        "IniPrePay");
                currYIniBase = rss.getString("prePayYear");
                currYIniDebitSumBase = rss.getString(
                        "prePayDebit");
                currYIniCreditSumBase = rss.getString(
                        "prePayCredit");

                subCode = "1123";
            }

        }

        StringBuffer upAccBal = new StringBuffer(""); //科目余额表SQL

        upAccBal.append(" update tblAccBalance set ");
        upAccBal.append(" CurrYIniBase=").append(currYIniBase).
                append(",CurrYIniDebitSumBase=").append(currYIniDebitSumBase);
        upAccBal.append(",CurrYIniCreditSumBase=").append(currYIniCreditSumBase).
                append(
                        ",CurrYIniBalaBase=").append(currYIniBalaBase);

        upAccBal.append(" where SubCode='").append(subCode).
                append("' and Period=-1").append(" and SCompanyID='" + sunCompany + "'");

        cs.execute(upAccBal.toString());

        String sqls = "select classCode from tblAccTypeInfo where accNumber='" +
                      subCode + "' and SCompanyID='"+sunCompany+"'";
        String accCode = "";
        rss = cs.executeQuery(sqls);
        if (rss.next()) {
            accCode = rss.getString(1);
            //修改科目余额表中父类
            cbs.setString(1, "tblAccBalance");
            cbs.setString(2, "tblAccTypeInfo");
            cbs.setString(3,
                          "tblAccBalance.subCode=tblAccTypeInfo.accNumber and tblAccTypeInfo.SCompanyID='"
                                           +sunCompany+"' and tblAccBalance.SCompanyID='"+sunCompany+"'");
            query ="period=-1";

            cbs.setString(4, query);
            field = "currYIniBase=sum(currYIniBase)@SPFieldLink:currYIniDebitSumBase=sum(currYIniDebitSumBase)" +
                    "@SPFieldLink:CurrYIniCreditSumBase=sum(CurrYIniCreditSumBase)" ;
            cbs.setString(5, field);
            cbs.setString(6, accCode);
            cbs.execute();
            
            if(sunCompany.length()>5){
            	//修改父类分支机构科目余额表数据
            	GlobalMgt mgt = new GlobalMgt();
            	mgt.updateAccParentSum(conn, accCode, sunCompany,-1,-1);
            }
        }

    }
    /**
     * 查询单位期初记录
     * @param name String
     * @param pageNo int
     * @param pageSize int
     * @return Result
     */
    public Result query(String sunCompany,String listType,String companyCode,Hashtable map,String companyNo,String companyName,String dimQuery,String type,final int pageNo,final int pageSize) {

        StringBuffer hql = new StringBuffer(
                "select a.classCode,a.ComNumber,a.ComFullName,");

        String tempyear = "";
        String tempborrow = "";
        String templend = "";
        String tempBalance = "";
        String tempCurBalance = "";
        String tempQuery = "";
        final String []field=new String[6];
        //查询不同类型（应收，应付，预收，预付）期初信息
        if (type.equals("receive")) {
            tempyear = "b.ReceiveBegin";
            tempborrow = "b.ReceiveTotalDebit";
            templend = "b.ReceiveTotalLend";
            tempBalance = "b.ReceiveTotalRemain";
            tempCurBalance = "b.CurReceiveTotalRemain";
            tempQuery = "a.ClientFlag in (2,3) ";

            field[0]="ReceiveBegin";
            field[1]="ReceiveTotalDebit";
            field[2]="ReceiveTotalLend";
            field[3]="ReceiveTotalRemain";
            field[4]="CurReceiveTotalRemain";

        } else if (type.equals("pay")) {
            tempyear = "b.PayBegin";
            tempborrow = "b.PayTotalDebit";
            templend = "b.PayTotalLend";
            tempBalance = "b.PayTotalRemain";
            tempCurBalance = "b.CurPayTotalRemain";
            tempQuery = "a.ClientFlag in (1,3) ";

            field[0]="PayBegin";
            field[1]="PayTotalDebit";
            field[2]="PayTotalLend";
            field[3]="PayTotalRemain";
            field[4]="CurPayTotalRemain";


        } else if (type.equals("preReceive")) {
            tempyear = "b.PreReceiveBegin";
            tempborrow = "b.PreReceiveTotalDebit";
            templend = "b.PreReceiveTotalLend";
            tempBalance = "b.PreReceiveTotalRemain";
            tempCurBalance = "b.CurPreReceiveTotalRemain";
            tempQuery = "a.ClientFlag in (2,3) ";

            field[0]="PreReceiveBegin";
            field[1]="PreReceiveTotalDebit";
            field[2]="PreReceiveTotalLend";
            field[3]="PreReceiveTotalRemain";
            field[4]="CurPreReceiveTotalRemain";


        } else if (type.equals("prePay")) {
            tempyear = "b.PrePayBegin";
            tempborrow = "b.PrePayTotalDebit";
            templend = "b.PrePayTotalLend";
            tempBalance = "b.PrePayTotalRemain";
            tempCurBalance = "b.CurPrePayTotalRemain";
            tempQuery = "a.ClientFlag in (1,3) ";

            field[0]="PrePayBegin";
            field[1]="PrePayTotalDebit";
            field[2]="PrePayTotalLend";
            field[3]="PrePayTotalRemain";
            field[4]="CurPrePayTotalRemain";

        }
        field[5]="currencyRate";
        hql.append("sum("+tempyear+")" + ",sum(" + tempborrow + "),sum(" + templend + "),sum(" + tempBalance+"),sum(" + tempCurBalance + ")");

        hql.append(",b.currencyRate ,(select count(*) from tblCompany c where c.classCode like a.classCode+'_____%') from tblCompanyTotal b left join tblCompany a on a.classCode=b.companyCode ");
        hql.append("  where b.period=-1 and ");

        hql.append(tempQuery);

        String query = "";



        if (sunCompany != null && sunCompany.length() > 0) { //如果启用了分支机构，加上分支机构的条件
            hql.append(" and b.SCompanyID like '" + sunCompany + "%'");
        }

        if (listType == null || listType.equals("query")) {
            String dimQuerySql = "";
            //根据查询条件查询

            if (companyNo != null && companyNo.length() > 0) {
                query += "  and  upper(a.ComNumber)='" +
                        companyNo.trim().toUpperCase() + "'";
            }
            if (companyName != null && companyName.length() != 0) {
                query += " and upper(a.ComFullName)='" +
                        companyName.trim().toUpperCase() + "' ";
            }

            //模糊查询遍历表中所有字段
            if (dimQuery != null && dimQuery.length() > 0) {
                DBTableInfoBean tableInfo = DDLOperation.getTableInfo(map,
                        "tblCompany");
                List list = tableInfo.getFieldInfos();

                for (int i = 0; i < list.size(); i++) {
                    DBFieldInfoBean fi = (DBFieldInfoBean) tableInfo.getFieldInfos().
                                         get(i);
                    dimQuerySql += "or upper(a." + fi.getFieldName() +
                            ") like '%" + dimQuery.trim().toUpperCase() + "%' ";
                }
                query += dimQuerySql.replaceFirst("or", " and(") + ") ";
            }

        }
        if(companyCode.length()==0){
            ReportDataMgt reportMgt = new ReportDataMgt();
            String hqlMin="select min(len(a.classCode)) "+hql.toString().substring(hql.toString().lastIndexOf("from"));
            Result rs2=reportMgt.getMinClassLen(hqlMin, new ArrayList());
            if(rs2.getRetCode()!=ErrorCanst.DEFAULT_SUCCESS){
                return rs2;
            }else{
            	if((companyNo==null||companyNo.length()==0)&&(companyName==null||companyName.length()==0)&&(dimQuery==null||dimQuery.length()==0)){
                hql.append(" and  len(a.classCode)="+rs2.getRetVal());
            	}
            }
        }else{
            hql.append(" and a.classCode like '" + companyCode + "_____'");
        }

        hql.append(query).append(" group by a.classCode,a.ComNumber,a.ComFullName,b.currencyRate");

        //调用list返回结果

        final String sql=hql.toString();
        final Result rs=new Result();

        int retCode = DBUtil.execute(new IfDB() {
          public int exec(Session session) {
              session.doWork(new Work() {
                  public void execute(Connection connection) throws
                          SQLException {
                      Connection conn = connection;
                      try {
                          Statement cs= conn.createStatement();
                          ResultSet rss=cs.executeQuery(sql);

                          List values=new ArrayList();
                          while(rss.next())
                          {
                              Object []value=new Object[10];
                              value[0]=rss.getString(1);
                              value[1]=rss.getString(2);
                              value[2]=rss.getString(3);
                              for(int i=3;i<=8;i++){
                                  value[i]=rss.getObject (i+1);
                                  if(value[i]==null){
                                      value[i]="0";
                                  }
                                  value[i]=GlobalsTool.formatNumber(value[i], false, true,
                                                    "true".equals(BaseEnv.
                                                    systemSet.get("intswitch").
                                                    getSetting()),"tblCompanyTotal",field[i-3],true);
                              }
                              value[9] = rss.getInt(10);

                              values.add(value);
                          }
                          rs.setRetVal(values);

                          //设置分页信息
                          rs.setRealTotal(values.size());
                          rs.setPageSize(pageSize);

                          int pageNos=pageNo>0?pageNo:1;
                          double totalPage=(double)rs.getRealTotal()/(double)rs.getPageSize();

                          rs.setPageNo(pageNos);
                          rs.setTotalPage((int)Math.ceil(totalPage));

                          int startNo=1+(rs.getPageNo()-1)*rs.getPageSize();
                          int endNo=rs.getPageSize()+(rs.getPageNo()-1)*rs.getPageSize();

                          ArrayList newList=new ArrayList();
                          for(int i=startNo-1;i<endNo&&i<values.size();i++)
                          {
                              Object obj=values.get(i);
                              newList.add(obj);
                          }
                          rs.setRetVal(newList);


                          rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                      } catch (SQLException ex) {
                          rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                          ex.printStackTrace();
                          return ;
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
     * 编辑是检查是否存在下级
     * @param ids
     * @return
     */
    public Result checkEditList(final String[] ids) {
		final Result rs = new Result();
		
		int retCode = DBUtil.execute(new IfDB() {
			
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						
						Statement stmt = conn.createStatement();
						try {
							String values="";
                            for(int i=0;i<ids.length;i++){                            	
                            	if(i!=ids.length-1){
                            		values=ids[i];
                            		
                            	}else{
                            		values=ids[i];
                            	}
                            }
                            String sql = "select  iscatalog from tblCompany where  classcode like '%"+values+"%'";
                            
                            ResultSet rss=stmt.executeQuery(sql);
                          
                            String code="";
                          
                            while(rss.next()){
                             
                            	code=rss.getString(1);
                            	
                            	if(code.equals("1")){
                            		
                            		rs.setRetCode(ErrorCanst.DATA_ALREADY_USED);
                            		break;
                            	}
                            }
                           // if(childCount>1){
                            //	rs.setRetCode(ErrorCanst.DATA_ALREADY_USED);
                            //}
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
		
		return rs;

	}
    /**
     * 查询单位期初记录
     * @param name String
     * @param pageNo int
     * @param pageSize int
     * @return Result
     */
    public Result getEditList(String []keyId,String type) {
        StringBuffer hql=new StringBuffer("select b.id, a.ComNumber,a.ComFullName,a.classCode,");
        hql.append("b.currencyID,c.CurrencyName,b.currencyRate,");
        String tempyear="";
        String tempborrow="";
        String templend="";
        String tempBalance="";
        String curtempyear = "";
        String curtempborrow = "";
        String curtemplend = "";
        String curtempBalance = "";

        //查询不同类型（应收，应付，预收，预付）期初信息
        if(type.equals("receive"))
        {
            tempyear="b.ReceiveBegin";
            tempborrow="b.ReceiveTotalDebit";
            templend="b.ReceiveTotalLend";
            tempBalance="b.ReceiveTotalRemain";

            curtempyear = "b.CurReceiveBegin";
            curtempborrow = "b.CurReceiveTotalDebit";
            curtemplend = "b.CurReceiveTotalLend";
            curtempBalance = "b.CurReceiveTotalRemain";
        }
        else if(type.equals("pay"))
        {
            tempyear="b.PayBegin";
            tempborrow="b.PayTotalDebit";
            templend="b.PayTotalLend";
            tempBalance="b.PayTotalRemain";

            curtempyear = "b.CurPayBegin";
            curtempborrow = "b.CurPayTotalDebit";
            curtemplend = "b.CurPayTotalLend";
            curtempBalance = "b.CurPayTotalRemain";

        }
        else if(type.equals("preReceive"))
        {
            tempyear="b.PreReceiveBegin";
            tempborrow="b.PreReceiveTotalDebit";
            templend="b.PreReceiveTotalLend";
            tempBalance="b.PreReceiveTotalRemain";

            curtempyear = "b.CurPreReceiveBegin";
            curtempborrow = "b.CurPreReceiveTotalDebit";
            curtemplend = "b.CurPreReceiveTotalLend";
            curtempBalance = "b.CurPreReceiveTotalRemain";

        }
        else if(type.equals("prePay"))
        {
            tempyear="b.PrePayBegin";
            tempborrow="b.PrePayTotalDebit";
            templend="b.PrePayTotalLend";
            tempBalance="b.PrePayTotalRemain";

            curtempyear = "b.CurPrePayBegin";
            curtempborrow = "b.CurPrePayTotalDebit";
            curtemplend = "b.CurPrePayTotalLend";
            curtempBalance = "b.CurPrePayTotalRemain";

        }
        hql.append(tempyear+","+tempborrow+","+templend+","+tempBalance);
        hql.append("," + curtempyear + "," + curtempborrow + "," + curtemplend +
                  "," + curtempBalance);
       hql.append(" from tblCompanyTotal b left join tblCompany a on a.classCode=b.companyCode left join tblCurrency c on  b.currencyID=c.id");

        hql.append(" where  b.CompanyCode in(");
        for(int i=0;i<keyId.length;i++){
            hql.append("'"+keyId[i]+"',");
        }
        hql.deleteCharAt(hql.length()-1);
        hql.append(") and period=-1 and a.isCatalog=0 order by b.CompanyCode");

        //调用list返回结果

        final String sql=hql.toString();
        final Result rs=new Result();

        int retCode = DBUtil.execute(new IfDB() {
          public int exec(Session session) {
              session.doWork(new Work() {
                  public void execute(Connection connection) throws
                          SQLException {
                      Connection conn = connection;
                      try {
                          Statement cs= conn.createStatement();
                          ResultSet rss=cs.executeQuery(sql);

                          List values=new ArrayList();
                          while(rss.next())
                          {
                              Object []value=new Object[15];
                              value[0]=rss.getString(1);
                              value[1]=rss.getString(2);
                              value[2]=rss.getString(3);
                              value[3]=rss.getString(4);
                              value[4]=rss.getString(5);
                              value[5]=rss.getString(6);
                              if (value[5] == null ||
                                  value[5].toString().length() == 0) {
                                  value[5]="&nbsp;";
                              }

                              for (int i = 6; i <= 14; i++) {
                                  value[i]=rss.getObject(i+1);
                                  if(value[i]==null){
                                      value[i]="0";
                                  }
                                  value[i]=GlobalsTool.formatNumber(value[i], false, false,
                                                    "true".equals(BaseEnv.
                                                    systemSet.get("intswitch").
                                                    getSetting()),null,null,true);
                              }
                              values.add(value);
                          }
                          if(values.size()==0){
                              rs.setRetCode(ErrorCanst.ER_NO_DATA);
                              return ;
                          }

                          rs.setRetVal(values);
                          rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                      } catch (SQLException ex) {
                          rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                          ex.printStackTrace();
                          return ;
                      }
                  }
              });
              return rs.getRetCode();
          }
      });
        rs.setRetCode(retCode);
        return rs;
    }


    public Result detail(String sunCompany,String companyCode,String type) {

        String hql="select b.id, a.ComNumber,a.ComFullName, b.currencyID,b.currencyRate ,";
        final String []field=new String[9];
        field[0]="currencyRate";
        //查询往来单位不同类型（应收，应付，预收，预付）期初信息
        if(type.equals("receive"))
        {
            hql+= "b.ReceiveBegin,b.ReceiveTotalDebit,b.ReceiveTotalLend,b.ReceiveTotalRemain,b.CurReceiveBegin,b.CurReceiveTotalDebit,b.CurReceiveTotalLend,b.CurReceiveTotalRemain from tblCompany a,tblCompanyTotal b";
            field[1]="ReceiveBegin";
            field[2]="ReceiveTotalDebit";
            field[3]="ReceiveTotalLend";
            field[4]="ReceiveTotalRemain";
            field[5]="CurReceiveBegin";
            field[6]="CurReceiveTotalDebit";
            field[7]="CurReceiveTotalLend";
            field[8]="CurReceiveTotalRemain";
        }
        else if(type.equals("pay"))
        {
            hql+= "b.PayBegin,b.PayTotalDebit,b.PayTotalLend,b.PayTotalRemain,b.CurPayBegin,b.CurPayTotalDebit,b.CurPayTotalLend,b.CurPayTotalRemain from tblCompany a,tblCompanyTotal b";
            field[1]="PayBegin";
            field[2]="PayTotalDebit";
            field[3]="PayTotalLend";
            field[4]="PayTotalRemain";
            field[5]="CurPayBegin";
            field[6]="CurPayTotalDebit";
            field[7]="CurPayTotalLend";
            field[8]="CurPayTotalRemain";

        }
        else if(type.equals("preReceive"))
        {
            hql+= "b.PreReceiveBegin,b.PreReceiveTotalDebit,b.PreReceiveTotalLend,b.PreReceiveTotalRemain,b.CurPreReceiveBegin,b.CurPreReceiveTotalDebit,b.CurPreReceiveTotalLend,b.CurPreReceiveTotalRemain from tblCompany a,tblCompanyTotal b";
            field[1]="PreReceiveBegin";
            field[2]="PreReceiveTotalDebit";
            field[3]="PreReceiveTotalLend";
            field[4]="PreReceiveTotalRemain";
            field[5]="CurPreReceiveBegin";
            field[6]="CurPreReceiveTotalDebit";
            field[7]="CurPreReceiveTotalLend";
            field[8]="CurPreReceiveTotalRemain";

        }
        else if(type.equals("prePay"))
        {
            hql+= "b.PrePayBegin,b.PrePayTotalDebit,b.PrePayTotalLend,b.PrePayTotalRemain,b.CurPrePayBegin,b.CurPrePayTotalDebit,b.CurPrePayTotalLend,b.CurPrePayTotalRemain from tblCompany a,tblCompanyTotal b  ";
            field[1]="PrePayBegin";
            field[2]="PrePayTotalDebit";
            field[3]="PrePayTotalLend";
            field[4]="PrePayTotalRemain";
            field[5]="CurPrePayBegin";
            field[6]="CurPrePayTotalDebit";
            field[7]="CurPrePayTotalLend";
            field[8]="CurPrePayTotalRemain";

        }
        hql+=" where b.CompanyCode='"+companyCode+"' and a.classCode=b.CompanyCode and period=-1";

        final String sql=hql;
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                          Statement cs= conn.createStatement();
                          ResultSet rss=cs.executeQuery(sql);

                          ArrayList  values=new ArrayList();
                          while(rss.next())
                          {
                              Object []value=new Object[13];;
                              value[0]=rss.getString(1);
                              value[1]=rss.getString(2);
                              value[2]=rss.getString(3);
                              value[3]=rss.getString(4);
                              for(int i=4;i<13;i++){
                                  value[i]=rss.getObject(i+1);
                                  if(value[i]==null||value[i].toString().length()==0){
                                      value[i]="0";
                                  }
                                  value[i]=GlobalsTool.formatNumber(value[i], false, false,
                                                    "true".equals(BaseEnv.
                                                    systemSet.get("intswitch").
                                                    getSetting()),"tblCompanyTotal",field[i-4],true);

                              }
                              values.add(value);
                          }
                          rs.setRetVal(values);

                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                            return;
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
