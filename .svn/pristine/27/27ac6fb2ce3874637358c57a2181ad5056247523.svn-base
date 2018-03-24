package com.koron.oa.individual.workPlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OAWorkPopedomeBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

public class OAWorkPopedomeMgt extends AIODBManager {
    /**
     * 查询 查看人列表
     * @param userId
     * @return
     */
    public Result querySeePopedome(){
    	final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try { 
                        	String sql = "SELECT * FROM OAJobPopedomSetting";
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	List<OAWorkPopedomeBean> list=new ArrayList<OAWorkPopedomeBean>();
                        	while(rss.next()){
                        		OAWorkPopedomeBean wpb=new OAWorkPopedomeBean();
                        		wpb.setId(rss.getString(1));
                        		wpb.setSeePersonId(rss.getString(2));
                        		wpb.setDesContent(rss.getString(3));
                        		wpb.setCreateTime(rss.getString(4));
                        		wpb.setLastUpdateTime(rss.getString(5));
                        		wpb.setBySeeUserID(rss.getString(6));
                        		wpb.setBySeeDeptOfClassCode(rss.getString(7));
                        		wpb.setBySeeEmpGroup(rss.getString(8));
                        		list.add(wpb);
                        	}
                        	rs.setRetVal(list) ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("OAWorkPlanMgt queryAssClient method "+ex) ;
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
    //查询所有 查看人列表
	public Result query() {
		// 创建参数
		List param = new ArrayList();
		String hql = "select bean from OAWorkPopedomeBean as bean order by lastUpdateTime desc";
		// 调用list返回结果
		return list(hql, param);
	}
	
	//添加查看人
	public Result addSeePerson(OAWorkPopedomeBean bean){
		return addBean(bean);
	}
	
	//	
	public Result load(String setId, Class classBean) {
	Result rs = loadBean(setId, classBean);
	return rs;
	}
	/**
	 * 修改
	 * @param bean
	 * @return
	 */
	public Result updateSeePerson(OAWorkPopedomeBean bean){
		return updateBean(bean);
	}
	//删除
	public Result deleteBean(String[] id) {
		return deleteBean(id, OAWorkPopedomeBean.class, "id");
	}
	
	//获取 部门 列表 中的职员
	
	 public Result queryDeptTree(final String UserId,final String seeType){
	    	final Result rs = new Result();
	        int retCode = DBUtil.execute(new IfDB() {
	            public int exec(Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection conn) throws SQLException {
	                        try { 
	                        	String sql = "SELECT * FROM OAJobPopedomSetting ops WHERE ops.SeePersonID LIKE '%"+UserId+",%'AND (ops.SeeType=? OR ops.SeeType=0)";
	                        	PreparedStatement pss = conn.prepareStatement(sql) ;
	                        	pss.setString(1, seeType);
	                        	ResultSet rss = pss.executeQuery() ;
	                        	List<OAWorkPopedomeBean> list=new ArrayList<OAWorkPopedomeBean>();
	                        	while(rss.next()){
	                        		OAWorkPopedomeBean wpb=new OAWorkPopedomeBean();
	                        		wpb.setId(rss.getString(1));
	                        		wpb.setSeePersonId(rss.getString(2));
	                        		wpb.setDesContent(rss.getString(3));
	                        		wpb.setCreateTime(rss.getString(4));
	                        		wpb.setLastUpdateTime(rss.getString(5));
	                        		wpb.setBySeeUserID(rss.getString(6));
	                        		wpb.setBySeeDeptOfClassCode(rss.getString(7));
	                        		wpb.setBySeeEmpGroup(rss.getString(8));
	                        		list.add(wpb);
	                        	}
	                        	rs.setRetVal(list) ;
	                        } catch (SQLException ex) {
	                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                            BaseEnv.log.error("OAWorkPlanMgt queryAssClient method "+ex) ;
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
	 
	 public Result getGroupClassCode(final String groupId){
		 final Result rs = new Result();
	        int retCode = DBUtil.execute(new IfDB() {
	            public int exec(Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection conn) throws SQLException {
	                        try { 
	                        	String sql = "SELECT DepartmentCode ,userID FROM tblEmpGroupUser tegu WHERE tegu.f_ref=?";
	                        	PreparedStatement pss = conn.prepareStatement(sql) ;
	                        	pss.setString(1, groupId);
	                        	ResultSet rss = pss.executeQuery() ;
	                        	List<String> codes=new ArrayList<String>();
	                        	while(rss.next()){
	                        		codes.add(rss.getString(1)+"&&"+rss.getString(2));
	                        	}
	                        	rs.setRetVal(codes) ;
	                        } catch (SQLException ex) {
	                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                            BaseEnv.log.error("OAWorkPlanMgt queryAssClient method "+ex) ;
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
