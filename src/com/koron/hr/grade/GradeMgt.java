package com.koron.hr.grade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;

public class GradeMgt extends AIODBManager {

	/**
	 * 查询所有用户做的历史试卷
	 */
	public Result queryOldExam(final String[] conditions,final int pageNo,final int pageSize){
    	String sql="select distinct tblExamManage.id as id,empFullName,tblExamManage.title as titleType,score,totalProblem,tblExamManageDet.startTime as start,tblExamManageDet.employeeId as employeeId," +
    			"datediff(mi,convert(datetime,tblExamManageDet.startTime),convert(datetime,tblExamManageDet.endTime)) as useTime,ROW_NUMBER() over(order by tblExamManageDet.startTime desc) as row_id  from tblExamManageDet, " +
    			"tblExamManage,tblEmployee " +
    			"where tblEmployee.id=tblExamManageDet.employeeId " +
    			"and tblExamManage.id=tblExamManageDet.f_ref " +
    			"and totalProblem!=0 " +
    			"and datediff(mi,convert(datetime,tblExamManageDet.startTime),convert(datetime,tblExamManageDet.endTime))>=0";
    	if(!"1".equals(conditions[4])){
    		sql += " and tblExamManageDet.employeeId='"+conditions[4]+"' ";
    	}
    	if (!conditions[0].equals("")) {
			sql+="and tblExamManageDet.startTime>='"+conditions[0]+"' ";
		}
    	if (!conditions[1].equals("")) {
    		sql+="and score='"+conditions[1]+"' " ;
    	}
    	if (!conditions[2].equals("")) {
    		sql+="and tblExamManage.title='"+conditions[2]+"' ";
    	}
    	if (!conditions[3].equals("")) {
    		sql+="and empFullName='"+conditions[3]+"' ";
    	}
    	AIODBManager aioMgt=new AIODBManager();
    	Result rs = aioMgt.sqlListMap(sql.toString(), null, pageNo, pageSize);
    	List listMap = (List)rs.retVal;
    	List list = new ArrayList();
    	for (int i = 0; i < listMap.size(); i++) {
    		HashMap map=(HashMap)listMap.get(i);
			String[] objs = new String[8];
			objs[0]=map.get("id").toString();
			objs[1]=map.get("empFullName").toString();
			objs[2]=map.get("titleType").toString();
			objs[3]=map.get("score").toString();
			objs[4]=map.get("totalProblem").toString();
			objs[5]=map.get("start").toString();
			objs[6]=map.get("useTime").toString();
			objs[7]=map.get("employeeId").toString();
			list.add(objs);
		}
    	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
    	rs.setRetVal(list) ;
        return rs;
	}
	
	/**
	 * 查询答卷明细
	 * @param strs
	 * @return
	 */
	public Result queryHistoryTestDetail(final String detId,final String employeeId, final String title,final int pageNo,final int pageSize){
    	String sql="select tblHistoryTest.id as id,title,correctAnswer,answer,answerMarke,answerMarke+'. '+preSelectAnswer as op,isCorrect,ROW_NUMBER() over(order by tblHistoryTest.id,answerMarke) as row_id from tblHistoryTest " +
    			"left join tblProblemsManage on tblHistoryTest.problemsManageId=tblProblemsManage.id " +
    			"left join tblAPreSelectAnswer on tblProblemsManage.id=tblAPreSelectAnswer.f_ref " +
    			"where examManageDetId='"+detId + "' and userId='"+employeeId+"'";
    	if(title!=null && title.trim().length()>0){
    		sql += " and tblProblemsManage.title like '%" + title +"%'";
    	}
    	AIODBManager aioMgt=new AIODBManager();
    	Result rs = aioMgt.sqlListMap(sql.toString(), null, pageNo, pageSize);
    	List listMap = (List)rs.retVal;
    	List list = new ArrayList();
    	for (int i = 0; i < listMap.size(); i++) {
    		HashMap map=(HashMap)listMap.get(i);
			String[] objs = new String[7];
			objs[0]=map.get("id").toString();
			objs[1]=map.get("title").toString();
			objs[2]=map.get("answer").toString();
			objs[3]=map.get("correctAnswer").toString();
			objs[4]=map.get("op").toString();
			objs[5]=map.get("answerMarke").toString();
			if(objs[5].equals("A")||objs[5].equals("a")||objs[5].equals("1")){
				objs[6]="1";
			}else{
				objs[6]="0";
			}
			list.add(objs);
		}
    	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
    	rs.setRetVal(list) ;
        return rs;
	}
	
	public Result deleteExamDet(final String examDetId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("delete from tblExamManageDet where id=?");
							pstmt.setString(1, examDetId);
							int row = pstmt.executeUpdate();
							if (row > 0) {
								rst.setRetVal(true);
							}
						} catch (Exception ex) {
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
	
	public Result deleteHistory(final String examDetId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("delete from tblHistoryTest where examManageDetId=?");
							pstmt.setString(1, examDetId);
							int row = pstmt.executeUpdate();
							if (row > 0) {
								rst.setRetVal(true);
							}
						} catch (Exception ex) {
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
	
}
