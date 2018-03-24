package com.koron.oa.publicMsg.advice;

import java.util.*;

import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.koron.oa.bean.Department;
import com.koron.oa.bean.Employee;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.ErrorCanst;
import com.dbfactory.hibernate.IfDB;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import java.sql.*;
import java.util.*;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;


/**
 * <p>Title: </p>
 *
 * <p>Description: ֪ͨͨ����</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 * @author �Խ� and mj
 * @version 1.0
 */
public class OAAdviceMgt extends DBManager {

    /**
     * ��ѯ ֪ͨͨ���(OAAdviceInfo)
     *����֪ͨ���⣬֪ͨ���֪ͨ����,֪ͨ���ѷ�ʽ,֪ͨ����,�û�ID
     * @param loginId String
     * @return Result
     */
    public Result sel_AdviceInfo(String str_title,
                                 String str_Type, String str_Content,
                                 String str_accepters,
                                 String userId) {
        final String title = str_title; //֪ͨ����
        final String type = str_Type; //֪ͨ���
        final String content = str_Content; //֪ͨ����
        final String accepterIds = str_accepters; //֪ͨ����ID
        final String employeeId = userId; //���ܵ�½��ID

        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {

                            Statement st = conn.createStatement();
                            String sql =
                                    "select top 1 * from OAAdviceInfo where 1=1 and AdviceType ='" +
                                    type + "' and AdviceTitle='" + title +
                                    "'and Accepter='" + accepterIds +
                                    "' and AdviceContext='" + content +
                                    "' and IsPulished =1 and Pulisher ='" +
                                    employeeId +
                                    "' order by lastUpdateTime desc";
                            ResultSet rss = st.executeQuery(sql);
                            String value[] = new String[7];
                            if (rss.next()) {
                                value[0] = rss.getString("id"); //��id����
                                value[1] = rss.getString("AdviceType"); //֪ͨ����
                                value[2] = rss.getString("AdviceTitle"); //֪ͨ����
                                value[3] = rss.getString("AdviceContext"); //֪ͨ����
                                value[4] = rss.getString("IsPulished"); //�Ƿ񷢲�
                                value[5] = rss.getString("Pulisher"); //������
                                value[6] = rss.getString("PulishDate"); //����ʱ��
                            }
                            rs.setRetVal(value);
                            rs.setRealTotal(value.length);
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

    /**
     * ����֪ͨ(OAAdviceSends)
     * ����֪ͨ����ID��֪ͨID,�û�ID
     * @param loginId String
     * @return Result
     */
    public Result ins_AdviceSend(String str_acceperId,
                                 String str_adviceId,
                                 String userId) {
        final String acceperId = str_acceperId; //֪ͨ����id
        final String adviceId = str_adviceId; //֪ͨID
        final String employeeId = userId; //���ܵ�½��ID
        final Result rs = new Result();

        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            Statement st = conn.createStatement();
                            String id = IDGenerater.getId(); //�Զ�����һ��ID
                            String creatTime = BaseDateFormat.format(new java.
                                    util.Date(), BaseDateFormat.yyyyMMddHHmmss);
                            String ins_sql = "insert into OAAdviceSends(id, IsRead, ToUserId, AdviceId, createBy, lastUpdateBy, createTime, lastUpdateTime, statusId)" +
                                             "values('" + id + "',0,'" +
                                             acceperId + "','" + adviceId +
                                             "','" + employeeId + "','" +
                                             employeeId + "','" + creatTime +
                                             "','" + creatTime + "',0)";
                            boolean bool = st.execute(ins_sql);
                            if (bool) { //���ִ�гɹ�
                                rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }
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


    /**
     * ����δ����֪ͨ(OAAdviceInfo)
     * ����֪ͨ���⣬֪ͨ���֪ͨ����,֪ͨ���ѷ�ʽ,֪ͨ����,�û�ID
     * @param loginId String
     * @return Result
     */
    public Result ins_notPulish_Advice(String str_title,
                                       String str_Type, String str_Content,
                                       String str_wakeUpModes,
                                       String str_accepters,
                                       String userId,
                                       String str_fileName,
                                       String str_filePath,
                                       String str_isAlonePopedmon,
                                       String str_popedomDeptIds,String str_popedomEmpGroupIds,String str_isSaveReading,String str_whetherAgreeReply) {
        final String title = str_title; //֪ͨ����
        final String type = str_Type; //֪ͨ���
        final String content = str_Content; //֪ͨ����
        final String wakeUpModes = str_wakeUpModes; //֪ͨ���ѷ�ʽ
        final String accepterIds = str_accepters; //֪ͨ����ID
        final String employeeId = userId; //���ܵ�½��ID
        final String fileName = str_fileName ;
        final String filePath = str_filePath ;
        final String isAlonePopedmon = str_isAlonePopedmon ; //�Ƿ񹫿�
        final String popedomDeptIds = str_popedomDeptIds ;
        final String popedomEmpGroupIds = str_popedomEmpGroupIds ;
        final String isSaveReading=str_isSaveReading;
        final String whetherAgreeReply = str_whetherAgreeReply;//mj
        final Result rs = new Result();

        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            //Statement st = conn.createStatement();
                            String id = IDGenerater.getId(); //�Զ�����һ��ID
                            String creatTime = BaseDateFormat.format(new java.
                                    util.Date(), BaseDateFormat.yyyyMMddHHmmss);
//                            String ins_sql2 = "insert into OAAdviceInfo(id, AdviceType, AdviceTitle, AdviceContext, IsPulished, WakeUpMode, Accepter, createBy, lastUpdateBy, createTime, lastUpdateTime, statusId,fileName,filePath,isAlonePopedmon,popedomDeptIds,popedomEmpGroupIds)" +
//                                             "values('" + id + "','" + type +
//                                             "','" + title + "','" + content +
//                                             "',0,'" + wakeUpModes + "','" +
//                                             accepterIds + "','" + employeeId +
//                                             "','" + employeeId + "','" +
//                                             creatTime + "','" + creatTime +
//                                             "',0,'"+fileName+"','"+filePath+"','"+isAlonePopedmon+"','"+popedomDeptIds+"','"+popedomEmpGroupIds+"')";
                            String ins_sql = "insert into OAAdviceInfo(id, AdviceType, AdviceTitle, AdviceContext, IsPulished, WakeUpMode, Accepter, createBy, lastUpdateBy, createTime, lastUpdateTime, statusId,fileName,filePath,isAlonePopedmon,popedomDeptIds,popedomEmpGroupIds,isSaveReading,whetherAgreeReply)" +
                    		"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
                            PreparedStatement ps = conn.prepareStatement(ins_sql) ;
                            ps.setString(1, id) ;
                            ps.setString(2, type) ;
                            ps.setString(3, title) ;
                            ps.setString(4, content) ;
                            ps.setInt(5, 0) ;
                            ps.setString(6, wakeUpModes) ;
                            ps.setString(7, accepterIds) ;
                            ps.setString(8, employeeId) ;
                            ps.setString(9, employeeId) ;
                            ps.setString(10, creatTime) ;
                            ps.setString(11, creatTime) ;
                            ps.setInt(12, 0) ;
                            ps.setString(13, fileName) ;
                            ps.setString(14, filePath) ;
                            ps.setString(15, isAlonePopedmon) ;
                            ps.setString(16, popedomDeptIds) ;
                            ps.setString(17, popedomEmpGroupIds) ;
                            ps.setString(18, isSaveReading);
                            ps.setString(19, whetherAgreeReply);
                            
                            boolean bool=ps.execute();
                            if (bool) { //���ִ�гɹ�
                                rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }
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

    /**
     * ����֪ͨͨ��(OAAdviceInfo)
     * ����֪ͨ���⣬֪ͨ���֪ͨ����,֪ͨ���ѷ�ʽ,֪ͨ����,������,�û�ID
     * @param loginId String
     * @return Result
     */
    public Result ins_pulish_Advice(final String str_id,String str_title,
                                    String str_Type, String str_Content,
                                    String str_wakeUpModes,
                                    String str_accepters, String str_pulisher,
                                    String userId,String str_fileName,String str_filePath,
                                    String str_isAlonePopedmon,
                                    String str_popedomDeptIds,String str_empGroupId,String str_isSaveReading,String whetherAgreeReply) {
        final String title = str_title; //֪ͨ����
        final String type = str_Type; //֪ͨ���
        final String content = str_Content; //֪ͨ����
        final String wakeUpModes = str_wakeUpModes; //֪ͨ���ѷ�ʽ
        final String accepterIds = str_accepters; //֪ͨ����ID
        final String pulisher = str_pulisher; //������ID
        final String employeeId = userId; //���ܵ�½��ID
        final String fileName = str_fileName ; //������
        final String filePath = str_filePath ;//�ļ�·��
        final String isAlonePopedmon = str_isAlonePopedmon ;//�Ƿ񹫿�
        final String popedomDeptIds = str_popedomDeptIds ; //��֪����ID
        final String empGroupIds=str_empGroupId;//��ȨְԱ����ID
        final String isSaveReading=str_isSaveReading;
        final Result rs = new Result();
        final String thiswhetherAgreeReply = whetherAgreeReply;//mj

        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            String creatTime = BaseDateFormat.format(new java.
                                    util.Date(), BaseDateFormat.yyyyMMddHHmmss);
                            String newsTime = BaseDateFormat.format(new java.
                                    util.Date(), BaseDateFormat.yyyyMMdd);
                            String ins_sql = "insert into OAAdviceInfo(id, AdviceType, AdviceTitle, AdviceContext, IsPulished, WakeUpMode, PulishDate, Accepter, createBy, lastUpdateBy, createTime, lastUpdateTime, statusId,Pulisher,fileName,filePath,isAlonePopedmon,popedomDeptIds,popedomEmpGroupIds,isSaveReading,whetherAgreeReply) " +
                            		"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
                            PreparedStatement ps = conn.prepareStatement(ins_sql) ;
                            ps.setString(1, str_id) ;
                            ps.setString(3, title) ;
                            ps.setString(2, type) ;
                            ps.setString(4, content) ;
                            ps.setInt(5, 1) ;
                            ps.setString(6, wakeUpModes) ;
                            ps.setString(7, newsTime) ;
                            ps.setString(8, accepterIds) ;
                            ps.setString(9, employeeId) ;
                            ps.setString(10, employeeId) ;
                            ps.setString(11, creatTime) ;
                            ps.setString(12, creatTime) ;
                            ps.setInt(13, 0) ;
                            ps.setString(14, pulisher) ;
                            ps.setString(15, fileName) ;
                            ps.setString(16, filePath) ;
                            ps.setString(17, isAlonePopedmon) ;
                            ps.setString(18, popedomDeptIds) ; 
                            ps.setString(19, empGroupIds) ; 
                            ps.setString(20, isSaveReading);
                            ps.setString(21, thiswhetherAgreeReply);
                            
                            
                            boolean bool = ps.execute() ;
                            if (bool) { //���ִ�гɹ�
                                rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }
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

    /**
     * ��ѯ ֪ͨͨ���(OAAdviceInfo)
     *
     * @param loginId String
     * @return Result
     */
    public Result sel_AdviceInfo(int pageNo,int pageSize) {
    	String sql = "select a.id,a.AdviceType,a.AdviceTitle,a.AdviceContext,a.IsPulished,b.EmpFullName,a.PulishDate,a.Accepter,a.fileName,a.filePath,a.isAlonePopedmon,a.popedomDeptIds,ROW_NUMBER() over(order by a.IsPulished asc, a.lastUpdateTime  desc) as row_id " +
		" from  OAAdviceInfo a left join tblEmployee b on a.CreateBy = b.id where 1=1  ";
    	AIODBManager aioMgt=new AIODBManager();
    	Result rs=aioMgt.sqlListMap(sql, null, pageNo, pageSize);
    	List listMap=(List)rs.getRetVal();
    	List ls = new ArrayList();
    	for(int i=0;i<listMap.size();i++) {
    		HashMap map=(HashMap)listMap.get(i);
            String value[] = new String[13];
            value[0] =map.get("id").toString(); //��id����
            value[1] = map.get("AdviceType").toString(); //֪ͨ����
            value[2] =map.get("AdviceTitle").toString(); //֪ͨ����
            value[3] = map.get("AdviceContext").toString(); //֪ͨ����
            value[4] = map.get("IsPulished").toString(); //�Ƿ񷢲�
            value[5] = map.get("EmpFullName").toString(); //������
            value[6] = map.get("PulishDate").toString(); //����ʱ��
            value[7] = map.get("Accepter").toString(); //֪ͨ����
            value[9] =map.get("fileName").toString() ;//����
            value[10] =map.get("filePath").toString() ;
            value[11] = map.get("isAlonePopedmon").toString() ; //�Ƿ񹫿�����
            value[12] = map.get("popedomDeptIds").toString() ;
            ls.add(value);
        }
    	rs.setRetVal(ls);
        return rs;
    }

    /**
       * ��ѯĳ�û���֪ͨ(OAAdviceInfo)
       *�����û�id
       * @param loginId String
       * @return Result
       */
      public Result sel_Advice_By_userId(String str_userId,String str_classCode,String str_empGroup1,String str_temp) {

          final String userId =str_userId;
          final String classCode = str_classCode;
          final String empGroup=str_empGroup1;
          final String temp=str_temp;
          final Result rs = new Result();
          int retCode = DBUtil.execute(new IfDB() {
              public int exec(Session session) {
                  session.doWork(new Work() {
                      public void execute(Connection connection) throws
                              SQLException {
                          Connection conn = connection;
                          try {
                              List ls = new ArrayList();
                              String sql = "select a.id,a.AdviceType,a.AdviceTitle,a.AdviceContext,a.IsPulished,b.EmpFullName,a.PulishDate,a.Accepter,a.fileName,a.filePath,a.isAlonePopedmon,a.popedomDeptIds " +
                              				" from OAAdviceInfo a left join tblEmployee b on a.CreateBy=b.id where 1=1 and (a.IsPulished = '1' or '1'=? or a.createBy=?) " +
                              				" and (a.isAlonePopedmon='0' or a.popedomDeptIds like ? or a.Accepter like ? or a.popedomEmpGroupIds like ? or a.popedomEmpGroupIds in ("+temp+"))  order by a.IsPulished asc, a.lastUpdateTime  desc" ;
                              PreparedStatement ps = conn.prepareStatement(sql) ;
                              ps.setString(1, userId) ;
                              ps.setString(2, userId) ;
                              ps.setString(3, "%"+classCode+"%") ;
                              ps.setString(4, "%"+userId+"%") ;
                              ps.setString(5, "%"+empGroup+"%") ;
                              ResultSet rss = ps.executeQuery();
                              while (rss.next()) {
                                  String value[] = new String[13];
                                  value[0] = rss.getString("id"); //��ͨ�淢��id����
                                  value[1] = rss.getString("AdviceType"); //֪ͨ����
                                  value[2] = rss.getString("AdviceTitle"); //֪ͨ����
                                  value[3] = rss.getString("AdviceContext"); //֪ͨ����
                                  value[4] = rss.getString("IsPulished"); //�Ƿ񷢲�
                                  value[5] = rss.getString("EmpFullName"); //������
                                  value[6] = rss.getString("PulishDate"); //����ʱ��
                                  value[7] = rss.getString("Accepter"); //֪ͨ����
                                  value[9] = rss.getString("fileName") ;//����
                                  value[10] = rss.getString("filePath") ;
                                  value[11] = rss.getString("isAlonePopedmon") ;
                                  value[12] = rss.getString("popedomDeptIds") ;
                                  ls.add(value);
                              }
                              rs.setRetVal(ls);
                              rs.setRealTotal(ls.size());
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


    /**
     * �����û�ID��ѯ�û���(tblEmployee)
     * @param loginId String
     * @return Result
     */
    public Result sel_employee(String employeeId) {
        final String userId = employeeId; //���ܵ�½��ID
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {

                            Statement st = conn.createStatement();
                            //��ѯ��Ա��(oABBSUser)
                            String sql =
                                    "select * from dbo.tblEmployee where 1=1 and id = '" +
                                    userId + "'";
                            ResultSet rss = st.executeQuery(sql);
                            String value[] = new String[3];
                            if (rss.next()) {
                                value[0] = rss.getString("id"); //��id����
                                value[1] = rss.getString("EmpFullName"); //����ʵ��������
                                value[2] = rss.getString("sysName"); //����½������

                            }
                            rs.setRetVal(value);
                            rs.setRealTotal(value.length);
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
    
    /**
     * ��ѯ�û���(tblEmployee)
     * @param loginId String
     * @return Result
     */
    public Result sel_allEmployee() {

        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        List listEmp = new ArrayList() ;
                        try {

                            Statement st = conn.createStatement();
                            //��ѯ��Ա��(oABBSUser)
                            String sql =
                                    "select * from dbo.tblEmployee where statusId!=-1";
                            ResultSet rss = st.executeQuery(sql);
                            while (rss.next()) {
                            	listEmp.add(rss.getString("id")); //��id����
                            }
                            rs.setRetVal(listEmp);
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

    /**
     * ɾ��֪ͨ(OAAdviceInfo)
     * ����֪ͨid
     * @param loginId String
     * @return Result
     */
    public Result del_advice(String str_adviceId) {
        final String adviceId = str_adviceId; //id
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            Statement st = conn.createStatement();
                            //��ɾ����Ӧ�����Żظ���Ϣ mj
                            String del_reply_sql = "delete  from oaNewsInfoReply where newsId =?";
                            PreparedStatement pss = conn.prepareStatement(del_reply_sql) ;
                            pss.setString(1,adviceId) ;
                            pss.executeUpdate();
                            
                            String del_sql =
                                    "delete OAAdviceInfo where 1=1 and id ='" +
                                    adviceId + "'";
                            boolean bool = st.execute(del_sql);
                            if (bool) { //���ִ�гɹ�
                                rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }
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

    /**
     * ����֪ͨ��ϢIDɾ��֪ͨ���ͱ�(OAAdviceSends)
     * ����֪ͨid
     * @param loginId String
     * @return Result
     */
    public Result del_adviceSend(String str_adviceId) {
        final String adviceId = str_adviceId; //id
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            Statement st = conn.createStatement();

                            String del_sql =
                                    "delete OAAdviceSends where 1=1 and AdviceId ='" +
                                    adviceId + "'";
                            boolean bool = st.execute(del_sql);
                            if (bool) { //���ִ�гɹ�
                                rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }
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
    /**
        * ɾ��֪ͨ���ͱ�(OAAdviceSends)
        * ����֪ͨ����id
        * @param loginId String
        * @return Result
        */
       public Result del_adviceSend_By_Id(String str_adviceSendId) {
           final String adviceSendId = str_adviceSendId; //id
           final Result rs = new Result();
           int retCode = DBUtil.execute(new IfDB() {
               public int exec(Session session) {
                   session.doWork(new Work() {
                       public void execute(Connection connection) throws
                               SQLException {
                           Connection conn = connection;
                           try {  
                        	   //��ɾ����Ӧ�����Żظ���Ϣ mj
                               String del_reply_sql = "delete  from oaNewsInfoReply where newsId =?";
                               PreparedStatement pss = conn.prepareStatement(del_reply_sql) ;
                               pss.setString(1,adviceSendId) ;
                               pss.executeUpdate();
                        	  
                               String del_sql = "delete OAAdviceInfo where 1=1 and id = ?" ; 
                        	   PreparedStatement ps = conn.prepareStatement(del_sql) ;
                               ps.setString(1, adviceSendId) ;
                               int num = ps.executeUpdate() ;
                               if (num>0) { //���ִ�гɹ�
                                   rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                               }
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
    /**
     * �������� ��ѯ ͨ���(OAAdviceInfo)
     *
     * @return Result
     */
    public Result sel_AdviceInfo_By_condition(String str_Type, 
    										  String str_Title,
                                              String str_beginTime,
                                              String str_endTime,
                                              String str_userId) {
    	

        final String type = str_Type; // ͨ������
        final String title = str_Title; // ͨ�����
        final String beginTime = str_beginTime; //����ʱ��
        final String endTime = str_endTime; //����ʱ��
        final String userId = str_userId ;
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            List ls = new ArrayList();
                            Statement st = conn.createStatement();
                            String sql =
                                    "select * from dbo.OAAdviceInfo a left join tblEmployee b on a.CreateBy=b.id where 1=1";
                            //�ж������Ƿ�Ϊ�գ�Ϊ���򲻼�
                            if (!"".equals(type) && null != type) {
                                sql += " and a.AdviceType ='" + type + "'";
                            }
                            if (!"".equals(title) && null != title) {
                                sql += " and a.AdviceTitle like '%" + title +
                                        "%' ";
                            }
                            if (!"".equals(endTime) && null != endTime) {
                                sql += " and a.PulishDate <= '" + endTime + "' ";
                            }
                            if (!"".equals(beginTime) && null != beginTime) {
                                sql += " and a.PulishDate >='" + beginTime + "' ";
                            }
                            if(userId!="1"){                            	
                            sql += " and (a.isAlonePopedmon='0' or a.Accepter like '%"+userId+"%') and (a.IsPulished = '1' or a.createBy='"+userId+"')  order by a.IsPulished asc,a.lastUpdateTime desc";
                            }
                            ResultSet rss = st.executeQuery(sql);
                            while (rss.next()) {
                                String value[] = new String[7];
                                value[0] = rss.getString("id"); //��id����
                                value[1] = rss.getString("AdviceType"); //֪ͨ����
                                value[2] = rss.getString("AdviceTitle"); //֪ͨ����
                                value[3] = rss.getString("AdviceContext"); //֪ͨ����
                                value[4] = rss.getString("IsPulished"); //�Ƿ񷢲�
                                value[5] = rss.getString("Pulisher"); //������
                                value[6] = rss.getString("PulishDate"); //����ʱ��

                                ls.add(value);
                            }
                            rs.setRetVal(ls);
                            rs.setRealTotal(ls.size());
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
    /**
     * �������� ��ѯ ͨ���(OAAdviceInfo)
     *
     * @return Result
     */
    public Result sel_AdviceInfo_By_condition(String type,String title,String beginTime,String endTime,
                                              String userId,String classCode,String publisher,String empGroup,String temp,int pageNo,int pageSize) {
    	AIODBManager aioMgt=new AIODBManager();
        String sql =
                "select a.id,a.AdviceType,a.AdviceTitle,a.AdviceContext,a.IsPulished,a.Pulisher,a.PulishDate,a.Accepter,a.fileName,a.createBy"+
                	",a.filePath,a.isAlonePopedmon,a.popedomDeptIds,ROW_NUMBER() over(order by a.IsPulished asc,a.lastUpdateTime desc) as row_id from dbo.OAAdviceInfo a left join tblEmployee b on(a.CreateBy=b.id) where 1=1";
        //�ж������Ƿ�Ϊ�գ�Ϊ���򲻼�
        if (!"".equals(type) && null != type) {
            sql += " and a.AdviceType ='" + type + "'";
        }
        if (!"".equals(title) && null != title) {
            sql += " and a.AdviceTitle like '%!" + title +
                    "%' escape '!'";
        }
        if (!"".equals(endTime) && null != endTime) {
            sql += " and a.PulishDate <= '" + endTime + "' ";
        }
        if (!"".equals(beginTime) && null != beginTime) {
            sql += " and a.PulishDate >='" + beginTime + "' ";
        }
        if (!"".equals(publisher) && null != publisher) {
            sql += " and b.EmpFullName like '%" + publisher + "%' ";
        }
        if(!userId.equals("1")){                            	
        	sql += " and (a.isAlonePopedmon='0' or a.Accepter like '%"+userId+"%' or a.popedomDeptIds like '%"+classCode+"%' or a.popedomEmpGroupIds like '%"+empGroup+"%' or a.popedomEmpGroupIds in("+temp+")) and (a.IsPulished = '1' or a.createBy='"+userId+"') ";
        }
        
        Result rs=aioMgt.sqlListMap(sql, null, pageNo, pageSize);
        List listMap=(List)rs.retVal;
        List ls = new ArrayList();
        for(int i=0;i<listMap.size();i++){
        	HashMap map=(HashMap)listMap.get(i);
	        String value[] = new String[14];
	        value[0] =map.get("id").toString(); //��ͨ�淢��id����
	        value[1] = map.get("AdviceType").toString(); //֪ͨ����
	        value[2] =map.get("AdviceTitle").toString(); //֪ͨ����
	        value[3] = map.get("AdviceContext").toString(); //֪ͨ����
	        value[4] = map.get("IsPulished").toString(); //�Ƿ񷢲�
	        value[5] = map.get("Pulisher").toString(); //������
	        value[6] = map.get("PulishDate").toString(); //����ʱ��
	        value[7] = map.get("Accepter").toString(); //֪ͨ����
	        value[9] = map.get("fileName").toString() ;//����
	        value[10] =map.get("filePath").toString() ;
	        value[11] =map.get("isAlonePopedmon").toString() ;
	        value[12] =map.get("popedomDeptIds").toString() ;
	        value[13] = map.get("createBy").toString();   //������
	        ls.add(value);
        }
        rs.setRetVal(ls);
        return rs;
    }

    /**
     * ����֪ͨid ��ѯ ͨ���(OAAdviceInfo)
     *
     * @param loginId String
     * @return Result
     */
    public Result sel_AdviceInfo_By_adviceId(String str_adviceId) {

        final String adviceId = str_adviceId; //ID
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {

                            Statement st = conn.createStatement();
                            String sql =
                                    "select * from OAAdviceInfo where 1=1 and id ='" +
                                    adviceId + "'";
                            ResultSet rss = st.executeQuery(sql);
                            String value[] = new String[17];
                            if (rss.next()) {
                                value[0] = rss.getString("id"); //��id����
                                value[1] = rss.getString("AdviceType"); //֪ͨ����
                                value[2] = rss.getString("AdviceTitle"); //֪ͨ����
                                value[3] = rss.getString("AdviceContext"); //֪ͨ����
                                value[4] = rss.getString("IsPulished"); //�Ƿ񷢲�
                                value[5] = rss.getString("Pulisher"); //������
                                value[6] = rss.getString("PulishDate"); //����ʱ��
                                value[7] = rss.getString("WakeUpMode"); //���ѷ�ʽ
                                value[8] = rss.getString("Accepter"); //֪ͨ����
                                value[9] = rss.getString("fileName") ; //������
                                value[10] = rss.getString("filePath") ;
                                value[11] = rss.getString("popedomDeptIds") ;//֪ͨ����
                                value[12] = rss.getString("isAlonePopedmon") ;//�Ƿ񵥶���Ȩ
                                value[13]=rss.getString("createBy");//������
                                value[14] = rss.getString("popedomEmpGroupIds") ;//֪ͨ�û�����
                                value[15] = rss.getString("isSaveReading") ;//�Ƿ񱣴��Ķ��ۼ�
                                value[16] = rss.getString("whetherAgreeReply");//�Ƿ�����ظ�mj
                            }
                            rs.setRetVal(value);
                            rs.setRealTotal(value.length);
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

    /**
     * �޸�֪ͨ(OAAdviceInfo)
     * ����֪ͨID��֪ͨ���⣬֪ͨ���֪ͨ����,֪ͨ���ѷ�ʽ,֪ͨ����,������,�û�ID
     * @param loginId String
     * @return Result
     */
    public Result upd_AdviceInfo(String str_adviceId, String str_title,
                                    String str_Type, String str_Content,
                                    String str_wakeUpModes,
                                    String str_accepters, String str_pulisher,String str_isPulish,
                                    String userId,String str_fileName,String str_filePath,
                                    String str_popedomDeptIds,
                                    String str_isAlonePopedmon,String str_empGroupId,String str_isSaveReading,String whetherAgreeReply) {

        final String adviceId = str_adviceId; //֪ͨID
        final String title = str_title; //֪ͨ����
        final String type = str_Type; //֪ͨ���
        final String content = str_Content; //֪ͨ����
        final String wakeUpModes = str_wakeUpModes; //֪ͨ���ѷ�ʽ
        final String accepterIds = str_accepters; //֪ͨ����ID
        final String pulisher = str_pulisher; //������ID
        final String isPulish = str_isPulish;//�Ƿ񷢲�
        final String employeeId = userId; //���ܵ�½��ID
        final String fileName = str_fileName ; //����
        final String filePath = str_filePath ; //·��
        final String popedomDeptIds = str_popedomDeptIds ;//֪ͨ����
        final String isAlonePopedmon = str_isAlonePopedmon ;//�Ƿ񵥶���Ȩ
        final String empGroupId=str_empGroupId;//��Ȩ�û�����
        final String isSaveReading =str_isSaveReading ;
        final String thisWhetherAgreeReply = whetherAgreeReply;//mj
        final Result rs = new Result();

        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            String creatTime = BaseDateFormat.format(new java.
                                    util.Date(), BaseDateFormat.yyyyMMddHHmmss);
                            String newsTime = ""; //����ʱ��
                            String publishId = "";
                            if ("1".equals(isPulish)) { //���Ϊ�������õ�ǰϵͳʱ��
                                newsTime = BaseDateFormat.format(new java.
                                        util.Date(), BaseDateFormat.yyyyMMdd);
                                publishId = pulisher;
                            }
                            String upd_sql =
                                    "update dbo.OAAdviceInfo set AdviceType=?,AdviceTitle=?,AdviceContext=?,IsPulished=?,WakeUpMode=?,PulishDate=?," +
                                    "Accepter=?,lastUpdateBy=?,lastUpdateTime=?,Pulisher=?,fileName=?,filePath=?,popedomDeptIds=?,isAlonePopedmon=?,popedomEmpGroupIds=?,isSaveReading=?,whetherAgreeReply=? where 1=1 and id =?";
                            PreparedStatement ps = conn.prepareStatement(upd_sql) ;
                            ps.setString(1, type) ;
                            ps.setString(2, title) ;
                            ps.setString(3, content) ;
                            ps.setString(4, isPulish) ;
                            ps.setString(5, wakeUpModes) ;
                            ps.setString(6, newsTime) ;
                            ps.setString(7, accepterIds) ;
                            ps.setString(8, employeeId) ;
                            ps.setString(9, creatTime) ;
                            ps.setString(10, publishId) ;
                            ps.setString(11, fileName) ;
                            ps.setString(12, filePath) ;
                            ps.setString(13, popedomDeptIds) ;
                            ps.setString(14, isAlonePopedmon) ;
                            ps.setString(15, empGroupId) ;
                            ps.setString(16, isSaveReading);
                            ps.setString(17, thisWhetherAgreeReply);
                            ps.setString(18, adviceId) ;

                            int bool = ps.executeUpdate() ;
                            if (bool>0) { //���ִ�гɹ�
                                rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            }
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
       /**
        * ����֪ͨ(OAAdviceInfo)
        * ����֪ͨid���û�ID
        * @param loginId String
        * @return Result
        */
       public Result pulished(String str_adviceId,
                                       String str_userId) {
           final String adviceId = str_adviceId; //id
           final String userId = str_userId; //�û�id
           final Result rs = new Result();

           int retCode = DBUtil.execute(new IfDB() {
               public int exec(Session session) {
                   session.doWork(new Work() {
                       public void execute(Connection connection) throws
                               SQLException {
                           Connection conn = connection;
                           try {
                               Statement st = conn.createStatement();
                               String creatTime = BaseDateFormat.format(new java.
                                       util.Date(), BaseDateFormat.yyyyMMddHHmmss);
                               String  newTime = BaseDateFormat.format(new java.
                                       util.Date(), BaseDateFormat.yyyyMMdd);
                               String del_sql =
                                       "update dbo.OAAdviceInfo set IsPulished='1',PulishDate='"+newTime+"',lastUpdateBy='"+userId+"',lastUpdateTime='"+creatTime+"',Pulisher='"+userId+"' where 1=1 and id ='"+adviceId+"'";
                               boolean bool = st.execute(del_sql);
                               if (bool) { //���ִ�гɹ�
                                   rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                               }
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

    /**
    * ����֪ͨID��������ID ��ѯ ͨ�淢�ͱ�(OAAdviceSends)
    *
    * @param loginId String
    * @return Result
    */
   public Result sel_AdviceSends(String str_adviceId,String str_userId) {
       final String  adviceId =str_adviceId;//֪ͨID
       final String  userId =str_userId;//������ID
       final Result rs = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection connection) throws
                           SQLException {
                       Connection conn = connection;
                       try {
                           Statement st = conn.createStatement();
                           String sql = "select * from dbo.OAAdviceSends where 1=1  and AdviceId ='"+adviceId+"' and ToUserId='"+userId+"'";
                           ResultSet rss = st.executeQuery(sql);
                           String value[] = new String[4];
                           if (rss.next()) {
                               value[0] = rss.getString("id"); //��id����
                               value[1] = rss.getString("AdviceId"); //֪ͨID
                               value[2] = rss.getString("ToUserId"); //������ID
                               value[3] = rss.getString("IsRead"); //�Ƿ��Ѷ�
                           }
                           rs.setRetVal(value);
                           rs.setRealTotal(value.length);
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

       /**
         * �����Ķ�״̬(OAAdviceSends)
         * ����֪ͨ����ID
         * @param loginId String
         * @return Result
         */
        public Result upd_AdviceSends(String str_adviceSendId,String str_userId) {
            final String adviceSendId = str_adviceSendId; //id
              final String userId = str_userId; //�û�id
            final Result rs = new Result();

            int retCode = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                                SQLException {
                            Connection conn = connection;
                            try {
                                Statement st = conn.createStatement();
                                String creatTime = BaseDateFormat.format(new java.
                                        util.Date(), BaseDateFormat.yyyyMMddHHmmss);
                                String  newTime = BaseDateFormat.format(new java.
                                        util.Date(), BaseDateFormat.yyyyMMdd);
                                String del_sql =
                                        "update OAAdviceSends set IsRead ='1',lastUpdateBy ='"+userId+"' where 1=1 and id = '"+adviceSendId+"'";
                                boolean bool = st.execute(del_sql);
                                if (bool) { //���ִ�гɹ�
                                    rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                                }
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

        /**
         * ���ݹ���IDɾ��������
         * ����֪ͨ����ID
         * @param loginId String
         * @return Result
         */
        public Result del_AdviceSends(String str_adviceSendId) {
            final String adviceSendId = str_adviceSendId; //id
            final Result rs = new Result();

            int retCode = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                                SQLException {
                            Connection conn = connection;
                            try {
                            	String del_sql = "delete from OAAdviceSends where AdviceId=?" ;
                                PreparedStatement ps = conn.prepareStatement(del_sql) ;
                                ps.setString(1, adviceSendId) ;
                                int num = ps.executeUpdate() ;
                                if (num>0) { //���ִ�гɹ�
                                    rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                                }
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
    //-----------------------------------------------------------------------

        /**
         * ���ݲ���ClassCode��ѯ�ò����µ�����ְԱ
         */
        public List<Employee> queryAllEmployeeByClassCode(final String deptClassCode) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String strsql = "select emp.id,emp.EmpFullName from dbo.tblEmployee emp,dbo.tblDepartment dep " +
										"where 1=1 and dep.classCode=emp.DepartmentCode and emp.openFlag=1 and emp.statusId!=-1";
						if (null != deptClassCode&& !"".equals(deptClassCode)) {
							strsql += " and dep.classCode like '" + deptClassCode+ "%'";
						}
						try {
							Statement s = conn.createStatement();
							ResultSet rs = s.executeQuery(strsql);
							List<Employee> list = new ArrayList<Employee>();
							while (rs.next()) {
								Employee obj = new Employee();
								obj.setid(rs.getString("id"));
								obj.setEmpFullName(rs.getString("EmpFullName"));
								list.add(obj);
							}
							rst.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + strsql, ex);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		List<Employee> list = null ;
		if(retCode==ErrorCanst.DEFAULT_SUCCESS){
			list = (List<Employee>) rst.getRetVal() ;
		}
		return list ;
	}
        /**
         * ���ݷ��� ��ѯ�÷����µ�����ְԱ
         */
        public List<Employee> queryAllEmployeeByGroup(final String group) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String strsql = "select userID from tblEmpGroup a,tblEmpGroupUser b where a.id=b.f_ref and a.id='"+group+"'";
						try {
							Statement s = conn.createStatement();
							ResultSet rs = s.executeQuery(strsql);
							List list = new ArrayList ();
							while (rs.next()) {
								list.add(rs.getString(1));
							}
							rst.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + strsql, ex);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		List<Employee> list = null ;
		if(retCode==ErrorCanst.DEFAULT_SUCCESS){
			list = (List<Employee>) rst.getRetVal() ;
		}
		return list ;
	}

        /**
         * �����û�ID��ѯ����ClassCode
         */
        public String queryClassCodeByUserId(final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String sql = "select dep.classCode from dbo.tblEmployee emp,dbo.tblDepartment dep where 1=1 and dep.classCode=emp.DepartmentCode and emp.id = ?";
						try {
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ps.setString(1, userId) ;
							ResultSet rs = ps.executeQuery();
							String classCode = "" ;
							if(rs.next()){
								classCode = rs.getString("classCode") ;
							}
							rst.setRetVal(classCode);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + sql, ex);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		String classCode = null ;
		if(retCode==ErrorCanst.DEFAULT_SUCCESS){
			classCode = (String)rst.getRetVal() ;
		}
		return classCode ;
	}
    
        // ����ID��ѯְԱ��Ϣ
    	public Department getDepartmentByClassCode(final String classCode) {
    		final Result rst = new Result();
    		int retCode = DBUtil.execute(new IfDB() {
    			public int exec(Session session) {
    				session.doWork(new Work() {
    					public void execute(Connection connection)
    							throws SQLException {
    						Connection conn = connection;
    						String sql = "select id,DeptFullName,DeptCode,classCode from tblDepartment where classCode = ? ";
    						try {
    							PreparedStatement ps = conn.prepareStatement(sql);
    							ps.setString(1, classCode);
    							ResultSet rs = ps.executeQuery();
    							if (rs.next()) {
    								Department dept = new Department();
    								dept.setid(rs.getString("id"));
    								dept.setDeptFullName(rs.getString("DeptFullName"));
    								dept.setDeptCode(rs.getString("DeptCode"));
    								dept.setclassCode(rs.getString("classCode")) ;
    								rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
    								rst.setRetVal(dept);
    							}
    						} catch (Exception ex) {
    							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
    							BaseEnv.log.error("Query data Error :" + sql, ex);
    							return;
    						}
    					}
    				});
    				return rst.getRetCode();
    			}
    		});
    		Department dept = null;
    		if (retCode == ErrorCanst.DEFAULT_SUCCESS) {
    			dept = (Department) rst.getRetVal();
    		}
    		return dept;
    	}
        

}
