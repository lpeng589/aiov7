package com.menyi.sms.setting;

import com.menyi.aio.bean.NoteSetBean;
import com.dbfactory.Result;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.text.*;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ÷‹–¬”Ó</p>
 *
 * @author ÷‹–¬”Ó
 * @version 1.0
 */
public class SMSsetMgt {
    public SMSsetMgt() {
    }
    //≤Â»Î”Ôæ‰
   public Result Insert(final NoteSetBean noteSet){
       final Result rst = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection connection) throws
                        SQLException {
                     try{
                         Connection conn = connection;
                         PreparedStatement pstmt=conn.prepareStatement("insert into tblNoteSet (id,classCode,url,gouPass,createBy,lastUpdateBy,createTime,lastUpdateTime,statusId,SCompanyID,smsSign) values(?,?,?,?,?,?,?,?,?,?,?)");
                         pstmt.setString(1, noteSet.getId());
                         pstmt.setString(2, noteSet.getClassCode());
                         pstmt.setString(3, noteSet.getUrl());
                         pstmt.setString(4, noteSet.getGouPass());
                         pstmt.setString(5, noteSet.getCreateBy());
                         pstmt.setString(6, noteSet.getLastUpdateBy());
                         pstmt.setString(7, noteSet.getCreateTime());
                         pstmt.setString(8, noteSet.getLastUpdateTime());
                         pstmt.setInt(9, noteSet.getStatusId());
                         pstmt.setString(10, noteSet.getSCompanyID());
                         pstmt.setString(11, noteSet.getSmsSign());
                         int row = pstmt.executeUpdate();
                         if (row>0)
                         {
                            rst.setRetVal(true);
                            rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                         }
                        }catch (Exception ex){
                           ex.printStackTrace();
                           rst.setRetVal(false);
                           rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                           return;
                       }
                    }
                 });
                 return rst.getRetCode();
              }
           });
           rst.setRetCode(retCode);
           return rst;
   }

// ≤È—Ø”Ôæ‰
   public Result Select( final String id){
       final Result rst = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection connection) throws
                        SQLException {
                     try{
                         Connection conn = connection;
                         PreparedStatement pstmt=conn.prepareStatement("select * from tblSendSMS where 1=1 and id=?");
                         pstmt.setString(1,id);
                         ResultSet rs=pstmt.executeQuery();
                         NoteSetBean obj=new NoteSetBean();
                         if(rs.next())
                         {
                        	 obj.setId(rs.getString("id"));
                             obj.setClassCode(rs.getString("classCode"));
                             obj.setWorkFlowNode(rs.getString("workFlowNode"));
                             obj.setWorkFlowNodeName(rs.getString("workFlowNodeName"));
                             obj.setUrl(rs.getString("url"));
                             obj.setGouPass(rs.getString("gouPass"));
                             obj.setCreateBy(rs.getString("createBy"));
                             obj.setLastUpdateBy(rs.getString("lastUpdateBy"));
                             obj.setCreateTime(rs.getString("createTime"));
                             obj.setLastUpdateTime(rs.getString("lastUpdateTime"));
                             obj.setStatusId(rs.getInt("statusId"));
                             obj.setSCompanyID(rs.getString("SCompanyID"));
                             obj.setSmsSign(rs.getString("smsSign"));
                         }
                         rst.setRetVal(obj);
                         rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                        }catch (Exception ex){
                           ex.printStackTrace();
                           rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                           return;
                       }
                    }
                 });
                 return rst.getRetCode();
              }
           });
           rst.setRetCode(retCode);
           return rst;
   }
// –ﬁ∏ƒ”Ôæ‰
   public Result Update( final NoteSetBean tblnoteset){
       final Result rst = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection connection) throws
                        SQLException {
                     try{
                         Connection conn = connection;
                         PreparedStatement pstmt=conn.prepareStatement("update tblNoteSet set classCode=?,workFlowNode=?,workFlowNodeName=?,url=?,gouPass=?,createBy=?,lastUpdateBy=?,createTime=?,lastUpdateTime=?,statusId=?,SCompanyID=?,smsSign=? where 1=1 and id=?");
                         pstmt.setString(1, tblnoteset.getClassCode());
                         pstmt.setString(2, tblnoteset.getWorkFlowNode());
                         pstmt.setString(3, tblnoteset.getWorkFlowNodeName());
                         pstmt.setString(4, tblnoteset.getUrl());
                         pstmt.setString(5, tblnoteset.getGouPass());
                         pstmt.setString(6, tblnoteset.getCreateBy());
                         pstmt.setString(7, tblnoteset.getLastUpdateBy());
                         pstmt.setString(8, tblnoteset.getCreateTime());
                         pstmt.setString(9, tblnoteset.getLastUpdateTime());
                         pstmt.setInt(10, tblnoteset.getStatusId());
                         pstmt.setString(11, tblnoteset.getSCompanyID());
                         pstmt.setString(12, tblnoteset.getSmsSign());
                         pstmt.setString(13, tblnoteset.getId());
                         int row = pstmt.executeUpdate();
                         if (row>0)
                         {
                            rst.setRetVal(true);
                            rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                         }
                        }catch (Exception ex){
                           ex.printStackTrace();
                           rst.setRetVal(false);
                           rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                           return;
                       }
                    }
                 });
                 return rst.getRetCode();
              }
           });
         rst.setRetCode(retCode);
           return rst;
   }

   public Result SelectAll(){
       final Result rst = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection connection) throws
                        SQLException {
                     try{
                         Connection conn = connection;
                         PreparedStatement pstmt=conn.prepareStatement("select * from tblNoteSet where 1=1");
                         List<NoteSetBean> list=new ArrayList<NoteSetBean>();
                         ResultSet rs=pstmt.executeQuery();
                         while(rs.next())
                         {
                        	 NoteSetBean obj=new NoteSetBean();
                            obj.setId(rs.getString("id"));
                            obj.setClassCode(rs.getString("classCode"));
                            obj.setWorkFlowNode(rs.getString("workFlowNode"));
                            obj.setWorkFlowNodeName(rs.getString("workFlowNodeName"));
                            //String url = rs.getString("url");
                            obj.setUrl(BaseEnv.bol88URL+"/services");
                            obj.setGouPass(rs.getString("gouPass"));
                            obj.setCreateBy(rs.getString("createBy"));
                            obj.setSmsSign(rs.getString("smsSign"));
                            obj.setLastUpdateBy(rs.getString("lastUpdateBy"));
                            obj.setCreateTime(rs.getString("createTime"));
                            obj.setLastUpdateTime(rs.getString("lastUpdateTime"));
                            obj.setStatusId(rs.getInt("statusId"));
                            obj.setSCompanyID(rs.getString("SCompanyID"));
                            list.add(obj);
                         }
                         rst.setRetVal(list);
                         rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                        }catch (Exception ex){
                           rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                           return;
                       }
                    }
                 });
                 return rst.getRetCode();
              }
           });
       rst.setRetCode(retCode);
           return rst;
   }
}
