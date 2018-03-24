package com.koron.oa.mySroce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.toDo.OAToDoBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
public class MYSroceMgt extends AIODBManager{
	
	public Result querySroce(String condition){
		ArrayList param = new ArrayList();
		return sqlList(condition, param);
	}
	/**
	 * 查询是否已打分
	 * @param workId
	 * @return
	 */
	public Result queryIsExsit(String workId,String userId){
		ArrayList param = new ArrayList();
		String sql = "select comment,sroce,type,id from OASroceWorkDet where ref_Id = ? and toSroceManId='"+userId+"'";
		param.add(workId);
		return sqlList(sql, param);
	}
	/**
	 * 查询显示方式
	 * @return
	 */
	public Result queryPattern(){
		String sql = "select CNorEN,sroceCNEN,sroceType,id from OASroceTypeSet ";
		return sqlList(sql, new ArrayList());
	}
	
	public Result addSroce(final String pingfenId,final String ref_Id,final String sroceManId,final String createPlanDate,
			final String sroceType,final String comments,final String sroces,final String userId,final String code){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {  
                    		if(pingfenId == null || !"".equals(pingfenId)){
                    			String hql = "delete from OASroceWorkDet where id = ? and toSroceManId = ?";
                        		PreparedStatement delps = conn.prepareStatement(hql);
                        		delps.setString(1, pingfenId);
                        		delps.setString(2, userId);
                        		delps.executeUpdate();
                    		}
                    		                    		
                    		String sql = "insert into OASroceWorkDet(id,type,comment,sroce,sroceManId,toSroceManId,sroceDate,createPlanDate,ref_Id,deptClassCode,sroceRef_Id)values(?,?,?,?,?,?,?,?,?,?,?)";
                    		PreparedStatement ps = conn.prepareStatement(sql);	
                    		ps.setString(1,IDGenerater.getId());
                    		ps.setString(2,sroceType.split(";")[0]);
                    		ps.setString(3,comments);
                    		ps.setString(4,sroces);
                    		ps.setString(5,sroceManId);
                    		ps.setString(6,userId);
                    		ps.setString(7,BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                    		ps.setString(8,createPlanDate);
                    		ps.setString(9,ref_Id);
                    		ps.setString(10,code);
                    		ps.setString(11,sroceType.split(";")[1]);
                    		ps.executeUpdate();
                    		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        					BaseEnv.log.error("MYSroceMgt addSroce : ", ex) ;
        				}
                    }
                });				
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;	
		
	}
}
