package com.koron.oa.publicMsg.newsInfo;

import java.util.*;

import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.koron.oa.bean.OANewsInfoReplyBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.dbfactory.hibernate.IfDB;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import com.menyi.web.util.IDGenerater;



/**
 * <p>Title: </p>
 *
 * <p>Description: ������</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 * @author �Խ� and MJ
 * @version 1.0
 */
public class OANewsInfoMgt extends DBManager {

    /**
     * ��ѯ �ڲ����ű�(OACompanyNewsInfo)
     * @param loginId String
     * @return Result
     */
    public Result sel_CompanyNewsInfo(int pageNo,int pageSize) {
    	AIODBManager aioMgt=new AIODBManager();
    	String sql = "select a.id,a.NewsType,a.NewsTitle,a.NewsContext,a.IsUsed,b.EmpFullName,a.ReleaseTime,a.createBy,ROW_NUMBER() over( order by a.IsUsed asc,a.lastUpdateTime desc) as row_id "+
		 "from OACompanyNewsInfo a left join tblEmployee b on a.UserName=b.id where 1=1";
    	Result rs=aioMgt.sqlListMap(sql, null,pageNo,pageSize);
    	List listMap=(List)rs.retVal;
    	List ls = new ArrayList();
    	for(int i=0;i<listMap.size();i++){
    		HashMap map=(HashMap)listMap.get(i);
    		String value[] = new String[8];
            value[0] = map.get("id").toString(); //��id����
            value[1] =map.get("NewsType").toString(); //�������
            value[2] = map.get("NewsTitle").toString(); //���ű���
            value[3] =map.get("NewsContext").toString(); //���Ń���
            value[4] = map.get("IsUsed").toString(); //�Ƿ񷢲�
            value[5] = map.get("EmpFullName").toString(); //������
            value[6] = map.get("ReleaseTime").toString(); //����ʱ��
            value[7] = map.get("createBy").toString();    //������
            ls.add(value);
    	}
    	rs.setRetVal(ls);
        return rs;
    }
    
    /**
     * ��ѯ�û�ID��ѯ�ɿ����ڲ����ű�(OACompanyNewsInfo)
     * @param loginId String
     * @return Result
     */
    public Result sel_CompanyNewsInfoByUserId(final String userId,final String classCode,final String empGroup,final String temp,int pageNo,int pageSize) {
    	String sql = "select a.id,a.NewsType,a.NewsTitle,a.NewsContext,a.IsUsed,b.EmpFullName,a.ReleaseTime,ROW_NUMBER() over(order by a.IsUsed asc,a.lastUpdateTime desc) as row_id,a.createBy from OACompanyNewsInfo a left join tblEmployee b on a.UserName=b.id " +
			"where 1=1 and (a.IsUsed = '1' or a.createBy=? or '1'=?) and (a.isAlonePopedmon='0' or a.popedomDeptIds like ? or a.popedomUserIds like ? or a.popedomEmpGroupIds like ? or a.popedomEmpGroupIds in ("+temp+")) ";
    	ArrayList param=new ArrayList();
    	param.add(userId);
    	param.add(userId);
    	param.add("%"+classCode+"%");
    	param.add("%"+userId+"%");
    	param.add("%"+empGroup+"%");
    	AIODBManager aioMgt=new AIODBManager();
    	Result rs=aioMgt.sqlListMap(sql, param, pageNo, pageSize);
    	List listMap=(List)rs.retVal;
    	List ls = new ArrayList();
    	for(int i=0;i<listMap.size();i++){
    		HashMap map=(HashMap)listMap.get(i);
    		 String value[] = new String[8];
             value[0] = map.get("id").toString(); //��id����
             value[1] =map.get("NewsType").toString(); //�������
             value[2] = map.get("NewsTitle").toString(); //���ű���
             value[3] =map.get("NewsContext").toString(); //���Ń���
             value[4] =map.get("IsUsed").toString(); //�Ƿ񷢲�
             value[5] = map.get("EmpFullName").toString(); //������
             value[6] = map.get("ReleaseTime").toString(); //����ʱ��
             value[7] = map.get("createBy").toString();    //������
             ls.add(value);
    	}
    	rs.setRetVal(ls);
        return rs;
    }


    /**
     * ��ѯ �ѷ������ڲ����ű�(OACompanyNewsInfo)
     *
     * @param loginId String
     * @return Result
     */
    public Result sel_CompanyNewsInfo_Used() {

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
                            String sql = "select * from dbo.OACompanyNewsInfo where 1=1 and IsUsed ='1' order by lastUpdateTime desc";
                            ResultSet rss = st.executeQuery(sql);
                            while (rss.next()) {
                                String value[] = new String[8];
                                value[0] = rss.getString("id"); //��id����
                                value[1] = rss.getString("NewsType"); //�������
                                value[2] = rss.getString("NewsTitle"); //���ű���
                                value[3] = rss.getString("NewsContext"); //���Ń���
                                value[4] = rss.getString("IsUsed"); //�Ƿ񷢲�
                                value[5] = rss.getString("UserName"); //������
                                value[6] = rss.getString("ReleaseTime"); //����ʱ��
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
     * �������� ��ѯ �ڲ����ű�(OACompanyNewsInfo)
     *
     * @param loginId String
     * @return Result
     */
    public Result sel_CompanyNewsInfo(String str_userId,String str_classCode,
    								  String str_newsType, String str_newsTitle,
                                      String str_beginTime, String str_endTime) {
        final String newsType = str_newsType; //��������
        final String newsTitle = str_newsTitle; //���ű���
        final String beginTime = str_beginTime; //����ʱ��
        final String endTime = str_endTime; //����ʱ��
        final String userId = str_userId ;
        final String classCode = str_classCode ;
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            List ls = new ArrayList();
                            String sql =
                                    "select * from dbo.OACompanyNewsInfo where 1=1 and (IsUsed = '1' or createBy=?) " +
                                    "and (isAlonePopedmon='0' or popedomDeptIds like ? or popedomUserIds like ?)";
                            
                            //�ж������Ƿ�Ϊ�գ�Ϊ���򲻼�
                            if (!"".equals(newsType) && null != newsType) {
                                sql += " and NewsType ='" + newsType + "'";
                            }
                            if (!"".equals(newsTitle) && null != newsTitle) {
                                sql += " and (NewsTitle like '%" + newsTitle +
                                        "%' or NewsContext like'%" + newsTitle +
                                        "%') ";
                            }
                            if (!"".equals(endTime) && null != endTime) {
                                sql += " and ReleaseTime <= '" + endTime + "' ";
                            }
                            if (!"".equals(beginTime) && null != beginTime) {
                                sql += " and ReleaseTime >='" + beginTime + "' ";
                            }
                            sql += " order by IsUsed asc, lastUpdateTime desc";
                            PreparedStatement ps = conn.prepareStatement(sql) ;
                            ps.setString(1, userId) ;
                            ps.setString(2, "%"+classCode+"%") ;
                            ps.setString(3, "%"+userId+"%") ;
                            
                            ResultSet rss = ps.executeQuery();
                            while (rss.next()) {
                                String value[] = new String[7];
                                value[0] = rss.getString("id"); //��id����
                                value[1] = rss.getString("NewsType"); //�������
                                value[2] = rss.getString("NewsTitle"); //���ű���
                                value[3] = rss.getString("NewsContext"); //���Ń���
                                value[4] = rss.getString("IsUsed"); //�Ƿ񷢲�
                                value[5] = rss.getString("UserName"); //������
                                value[6] = rss.getString("ReleaseTime"); //����ʱ��
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
     * �������� ��ѯ �ڲ����ű�(OACompanyNewsInfo)(����)���һ�������˵�����
     * @param loginId String
     * @return Result
     */
    public Result sel_CompanyNewsInfo(String str_userId,String str_classCode,
    								  String str_newsType, String str_newsTitle,
                                      String str_beginTime, String str_endTime,String str_proUser,String str_empGroup,String str_temp) {
        final String newsType = str_newsType; //��������
        final String newsTitle = str_newsTitle; //���ű���
        final String beginTime = str_beginTime; //����ʱ��
        final String endTime = str_endTime; //����ʱ��
        final String userId = str_userId ;
        final String classCode = str_classCode ;
        final String proUser=str_proUser;
        final String empGroup=str_empGroup;
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
                            String sql =
                                    "select * from dbo.OACompanyNewsInfo  n left join tblEmployee e on(n.UserName=e.id) where 1=1 and (n.IsUsed = '1' or n.createBy=? or '1'=?) " +
                                    "and (isAlonePopedmon='0' or popedomDeptIds like ? or popedomUserIds like ? or popedomEmpGroupIds like ? or popedomEmpGroupIds in ("+temp+"))";
                            
                            //�ж������Ƿ�Ϊ�գ�Ϊ���򲻼�
                            if (!"".equals(newsType) && null != newsType) {
                                sql += " and NewsType ='" + newsType + "'";
                            }
                            if (!"".equals(newsTitle) && null != newsTitle) {
                                sql += " and NewsTitle like '%" + newsTitle +
                                        "%' ";
                            }
                            if (!"".equals(endTime) && null != endTime) {
                                sql += " and ReleaseTime <= '" + endTime + "' ";
                            }
                            if (!"".equals(beginTime) && null != beginTime) {
                                sql += " and ReleaseTime >='" + beginTime + "' ";
                            }
                            if (!"".equals(proUser) && null != proUser) {
                                sql += " and e.EmpFullName like '%" + proUser + "%' ";
                            }
                            sql += " order by IsUsed asc, n.lastUpdateTime desc";
                            PreparedStatement ps = conn.prepareStatement(sql) ;
                            ps.setString(1, userId) ;
                            ps.setString(2, userId);
                            ps.setString(3, "%"+classCode+"%") ;
                            ps.setString(4, "%"+userId+"%") ;
                            ps.setString(5, "%"+empGroup+"%") ;
                            
                            ResultSet rss = ps.executeQuery();
                            while (rss.next()) {
                                String value[] = new String[8];
                                value[0] = rss.getString("id"); //��id����
                                value[1] = rss.getString("NewsType"); //�������
                                value[2] = rss.getString("NewsTitle"); //���ű���
                                value[3] = rss.getString("NewsContext"); //���Ń���
                                value[4] = rss.getString("IsUsed"); //�Ƿ񷢲�
                                value[5] = rss.getString("UserName"); //������
                                value[6] = rss.getString("ReleaseTime"); //����ʱ��
                                value[7] = rss.getString("createBy");   //������
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
     * ��������id ��ѯ �ڲ����ű�(OACompanyNewsInfo)
     *
     * @param loginId String
     * @return Result
     */
    public Result sel_CompanyNewsInfo_By_newsId(String str_newsId) {

        final String newsId = str_newsId; //����ID

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
                                    "select * from dbo.OACompanyNewsInfo where 1=1 and id ='" +
                                    newsId + "'";
                            ResultSet rss = st.executeQuery(sql);
                            String value[] = new String[16];
                            if (rss.next()) {
                                value[0] = rss.getString("id"); //��id����
                                value[1] = rss.getString("NewsType"); //�������
                                value[2] = rss.getString("NewsTitle"); //���ű���
                                value[3] = rss.getString("NewsContext"); //���Ń���
                                value[4] = rss.getString("IsUsed"); //�Ƿ񷢲�
                                value[5] = rss.getString("UserName"); //������
                                value[6] = rss.getString("ReleaseTime"); //����ʱ��
                                value[7] = rss.getString("wakeupType") ;//���ѷ�ʽ
                                value[8] = rss.getString("popedomUserIds") ; //֪ͨ����
                                value[9] = rss.getString("popedomDeptIds") ; //֪ͨ����
                                value[10] = rss.getString("isAlonePopedmon") ;//�Ƿ񵥶���Ȩ
                                value[11] = rss.getString("createBy") ;//������
                                value[12] = rss.getString("popedomEmpGroupIds") ;//ְԱ����
                                value[13] = rss.getString("isSaveReading") ;//�Ƿ񱣴��Ķ��ۼ�
                                value[14] = rss.getString("picFiles") ;//�Ƿ񱣴��Ķ��ۼ�
                                value[15] = rss.getString("whetherAgreeReply");//�Ƿ�����ظ�mj
                                
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
     * ��������id ��ѯ �ڲ����ű�(OACompanyNewsInfo)
     *
     * @param loginId String
     * @return Result
     */
    public Result getNewsById(String str_newsId) {
        final String newsId = str_newsId; //����ID
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
                                    "select IsUsed from dbo.OACompanyNewsInfo where 1=1 and id ='" +
                                    newsId + "'";
                            ResultSet rss = st.executeQuery(sql);
                            String value[] = new String[1];
                            if (rss.next()) {
                                value[0] = rss.getString("IsUsed");
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
     * ������������(OACompanyNewsInfo)
     * �������ű��⣬����������Ń��ݣ�������ID,�û�ID
     * @param loginId String
     * @return Result
     */
    public Result ins_used_News(final String id,final String newstitle,
                                final String newsType, 
                                final String newsContent,
                                final String userName,
                                final String employeeId,
                                final String isAlonePopedmon,
                                final String popedomUserIds,
                                final String popedomDeptIds,
                                final String wakeupType,
                                final String empGroupId,final String isSaveReading,
                                final String picFiles,final String whetherAgreeReply) {
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
                            String newsTime = BaseDateFormat.format(new java.
                                    util.Date(), BaseDateFormat.yyyyMMdd);
                            //mj
                            String ins_sql = "insert into dbo.OACompanyNewsInfo(id, NewsType, NewsTitle, NewsContext, IsUsed, " +
                            			"createBy, lastUpdateBy, createTime, lastUpdateTime, statusId, UserName, ReleaseTime,isAlonePopedmon,popedomUserIds,popedomDeptIds,wakeupType,popedomEmpGroupIds,isSaveReading,picFiles,whetherAgreeReply) " +
                            			"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            PreparedStatement ps = conn.prepareStatement(ins_sql) ;
                            ps.setString(1, id) ;
                            ps.setString(2, newsType) ;
                            ps.setString(3, newstitle) ;
                            ps.setString(4, newsContent) ;
                            ps.setString(5, "1") ;
                            ps.setString(6, employeeId) ;
                            ps.setString(7, employeeId) ;
                            ps.setString(8, creatTime) ;
                            ps.setString(9, creatTime) ;
                            ps.setString(10, "0") ;
                            ps.setString(11, userName) ;
                            ps.setString(12, newsTime) ;
                            ps.setString(13, isAlonePopedmon) ;
                            ps.setString(14, popedomUserIds) ;
                            ps.setString(15, popedomDeptIds) ;   
                            ps.setString(16, wakeupType) ;  
                            ps.setString(17, empGroupId) ;  
                            ps.setString(18, isSaveReading);
                            ps.setString(19, picFiles);
                            ps.setString(20, whetherAgreeReply);
                            int n = ps.executeUpdate() ;
                            if(n>0){
                            	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                            	rs.setRetVal(id) ;
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
     * ����δ��������(OACompanyNewsInfo)
     * �������ű��⣬����������Ń��ݣ�������ID,�û�ID
     * @param loginId String
     * @return Result
     */
    public Result ins_notUsed_News( final String newstitle,
    								final String newsType, 
    								final String newsContent,
                                    final String employeeId,
                                    final String wakeupType,
                                    final String isAlonePopedmon,
                                    final String popedomUserIds,
                                    final String popedomDeptIds,
                                    final String empGroupId,final String isSaveReading,final String picFiles,final String whetherAgreeReply) {
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
                            //mj
                            String ins_sql = "insert into dbo.OACompanyNewsInfo(id, NewsType, NewsTitle, NewsContext, IsUsed, createBy, lastUpdateBy, " +
                            				"createTime, lastUpdateTime, statusId,wakeupType,isAlonePopedmon,popedomUserIds,popedomDeptIds,popedomEmpGroupIds,isSaveReading,picFiles,whetherAgreeReply)" +
                                             "values('" + id + "','" + newsType +
                                             "','" + newstitle + "','" +
                                             newsContent + "','0','" +
                                             employeeId + "','" + employeeId +
                                             "','" + creatTime + "','" +
                                             creatTime + "','0','"+wakeupType+"','"+isAlonePopedmon+"','"+popedomUserIds+"','"+popedomDeptIds+"','"+empGroupId+"','"+isSaveReading+"','"+picFiles+"','"+whetherAgreeReply+"')";
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
     * ɾ�����Żظ�
     * �������Żظ�id
     * @param loginId String
     * @return Result
     */
    public Result delReplyNewsInfoById(String replyId) {
        return deleteBean(replyId, OANewsInfoReplyBean.class, "id");
    }

    /**
     * ɾ������(OACompanyNewsInfo)
     * ��������id
     * @param loginId String
     * @return Result
     */
    public Result del_newsInfo(String str_newsInfoId) {
        final String newsInfoId = str_newsInfoId; //id

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
                            PreparedStatement ps = conn.prepareStatement(del_reply_sql) ;
                            ps.setString(1,newsInfoId) ;
                            ps.executeUpdate();
                            String del_sql =
                                    "delete OACompanyNewsInfo where 1=1 and id = '" +
                                    newsInfoId + "'";
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
     * ��δ�������ŷ���(OACompanyNewsInfo)
     * ��������id,������ID���û�ID
     * @param loginId String
     * @return Result
     */
    public Result upd_newsInfo_used( final String newsInfoId, final String userName,
                                    final String userId ) {

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
                                    util.Date(), BaseDateFormat.yyyyMMdd);
                            String del_sql =
                                    "update OACompanyNewsInfo set IsUsed ='1',UserName ='" +
                                    userName + "',ReleaseTime ='" + creatTime +
                                    "' where 1=1 and id ='" + newsInfoId + "'";
                                st.execute(del_sql);
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
     * �޸�����(OACompanyNewsInfo)
     * ��������id,���ű��⣬����������Ń��ݣ���½��,�û�ID
     * @param loginId String
     * @return Result
     */
    public Result upd_NewsInfo(String str_newsId, String str_title,
                               String str_newsType, String str_newsContent,
                               String str_isUsed, String str_userName,
                               String userId,String wakeupType,
                               String str_isAlonePopedmon,
                               String str_popedomUserIds,
                               String str_popedomDeptIds,
                               String str_popedomEmpGroupId,String str_isSaveReading,final String picFiles,final String whetherAgreeReply) {
        final String newsId = str_newsId; //����id
        final String newstitle = str_title; //���ű���
        final String newsType = str_newsType; //�������
        final String newsContent = str_newsContent; //���Ń���
        final String isUsed = str_isUsed; //�Ƿ񷢲�
        final String userName = str_userName; //��½��
        final String employeeId = userId; //��½��ID
        final String wakeupTypes = wakeupType ; //���ѷ�ʽ
        final String isAlonePopedmon = str_isAlonePopedmon; //�Ƿ񵥶���Ȩ
        final String popedomUserIds = str_popedomUserIds ; //֪ͨ����
        final String popedomDeptIds = str_popedomDeptIds ;// ֪ͨ����
        final String popedomEmpGroupId=str_popedomEmpGroupId;//ְ֪ͨԱ����
        final String isSaveReading=str_isSaveReading;//�Ƿ񱣴��Ķ��ۼ�
        final Result rs = new Result();

        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                           // Statement st = conn.createStatement();
                            String id = IDGenerater.getId(); //�Զ�����һ��ID
                            String creatTime = BaseDateFormat.format(new java.
                                    util.Date(), BaseDateFormat.yyyyMMddHHmmss);
                            String newsTime = ""; //����ʱ��
                            String releaseName = ""; //������
                            if ("1".equals(isUsed)) { //���Ϊ�������õ�ǰϵͳʱ��
                                newsTime = BaseDateFormat.format(new java.
                                        util.Date(), BaseDateFormat.yyyyMMdd);
                                releaseName = userName;
                            }
//                            String upd_sql2 =
//                                    "update OACompanyNewsInfo set NewsType='" +
//                                    newsType + "',NewsTitle ='" + newstitle +
//                                    "',NewsContext='" + newsContent +
//                                    "', IsUsed ='" + isUsed +
//                                    "',lastUpdateBy='" + employeeId +
//                                    "',lastUpdateTime='" + creatTime +
//                                    "', UserName ='" + releaseName +
//                                    "',ReleaseTime ='" + newsTime +
//                                    "',wakeupType ='"+wakeupTypes+
//                                    "',isAlonePopedmon='"+isAlonePopedmon+
//                                    "',popedomUserIds='"+popedomUserIds +
//                                    "',popedomDeptIds='"+popedomDeptIds+
//                                    "',popedomEmpGroupIds='"+popedomEmpGroupId+
//                                    "' where 1=1 and id ='" + newsId + "'";
                            String upd_sql =
                                "update OACompanyNewsInfo set NewsType=?,NewsTitle =?,NewsContext=?,IsUsed =?,lastUpdateBy=?,lastUpdateTime=?, UserName =?,ReleaseTime =?,wakeupType =?,isAlonePopedmon=?,popedomUserIds=?,popedomDeptIds=?,popedomEmpGroupIds=?,isSaveReading=?,picFiles=?,whetherAgreeReply = ? where 1=1 and id =?";
                           //mj
                            PreparedStatement ps = conn.prepareStatement(upd_sql) ;
                            ps.setString(1, newsType) ;
                            ps.setString(2, newstitle) ;
                            ps.setString(3, newsContent) ;
                            ps.setString(4, isUsed) ;
                            ps.setString(5, employeeId) ;
                            ps.setString(6, creatTime) ;
                            ps.setString(7, releaseName) ;
                            ps.setString(8, newsTime) ;
                            ps.setString(9, wakeupTypes) ;
                            ps.setString(10, isAlonePopedmon) ;
                            ps.setString(11, popedomUserIds) ;
                            ps.setString(12, popedomDeptIds) ;
                            ps.setString(13, popedomEmpGroupId) ;
                            ps.setString(14, isSaveReading) ;      
                            ps.setString(15, picFiles);
                            ps.setString(16, whetherAgreeReply) ;
                            ps.setString(17, newsId);
                            int bool=ps.executeUpdate();
                            //boolean bool = st.execute(upd_sql);
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
     * mj ��ѯ���е����ŵĻظ���������Ϣ
     * @param newsId
     * @param pageNo
     * @param pageSize
     * @return result
     */
	public Result queryReplyNewsInfo(String newsId, String bbsUserId,String orderAttribute,
			String orderType, int pageNo, int pageSize) {
		List<String> param = new ArrayList<String>();
		String hql = "select bean from OANewsInfoReplyBean bean where bean.newsId=? ";
		param.add(newsId);

		// if(bbsUserId != null && bbsUserId.trim().length() > 0 ){
		if (StringUtils.isNotBlank(bbsUserId)) {
			hql += " and bean.bbsUser.id=? ";
			param.add(bbsUserId);
		}
		
		if ("desc".equals(orderType)) {
			hql += " order by bean."+orderAttribute+" desc";
		} else {
			hql += " order by bean."+orderAttribute+" asc";
		}
		return list(hql, param, pageNo, pageSize, true);
	}
	
	/**
	 * �鿴�ظ�����Ϣ mj
	 */
	public Result getReplyById(String id) {
		return loadBean(id, OANewsInfoReplyBean.class);
	}
	
	
	
	/**
	 * �޸Ļظ�����Ϣ mj
	 */
	public Result updateReply(OANewsInfoReplyBean bean) {
		return updateBean(bean);
	}
	
	
	/**
	 * �ظ� ����mj
	 * @param forumBean
	 * @return
	 */
	public Result replyNewsInfo(OANewsInfoReplyBean replyBean) {
		Result result = addBean(replyBean) ;
//		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
//			/*�����û�����*/
//			result = updateBBSUserInfo(replyBean.getBbsUser().getId(), "reply") ;
//		}
		return result ;
	}
	/**
	 * ����û����� �ظ����� mj
	 * @param classCode
	 * @return
	 */
	public Result updateBBSUserInfo(final String userId,final String type){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "" ;
							if("add".equals(type)){
								sql = "update OABBSUsers set userscore=userscore+(select top 1 addNewTopic from OABBSSetting) where id=?" ;
							}else{
								sql = "update OABBSUsers set userscore=userscore+(select top 1 addResponseTopic from OABBSSetting) where id=?" ;
							}
							PreparedStatement pss = conn.prepareStatement(sql) ;
							pss.setString(1, userId) ;
							pss.executeUpdate() ;
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OANewsInfoMgt updateTopicInfo : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result ;
	}
}
