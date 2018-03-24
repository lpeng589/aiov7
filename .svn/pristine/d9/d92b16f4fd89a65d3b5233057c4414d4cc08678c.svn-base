package com.koron.hr.exam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.hr.bean.Answer;
import com.koron.hr.bean.Problem;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;

public class ExamMgt extends AIODBManager {
	
	/**
	 * 1.查询登陆用户可参加的考试，即可以考试的答卷
	 * @param conditions
	 * @return
	 */
	public Result queryExamByConditions(final String[] conditions,final int pageNo,final int pageSize){
    	String sql="select distinct tblExamManage.id as id,tblExamManage.startTime,tblExamManage.endTime,tblEmployee.empFullName as empName," +
    			"tblExamManage.createTime as createTime,tblExamManage.title as titleType,quantity,title as titleTypeEn,tblExamManageDet.id as detId,ROW_NUMBER() over(order by tblExamManage.startTime desc) as row_id  " +
    			"from tblExamManage,tblEmployee,tblExamManageDet " +
    			"where tblExamManage.createBy=tblEmployee.id " +
    			"and tblExamManage.id=tblExamManageDet.f_ref " +
    			"and getdate() between tblExamManage.startTime and tblExamManage.endTime " +
    			"and tblExamManageDet.totalProblem=0 " +
    			"and EmployeeID='"+ conditions[0] +"' ";
    	if (!conditions[1].equals("")) {
			sql+="and tblExamManage.startTime>'"+conditions[1]+"' " ;
		}
    	if (!conditions[2].equals("")) {
    		sql+="and tblExamManage.title='"+conditions[2]+"' " ;
    	}
    	if (!conditions[3].equals("")) {
    		sql+="and tblEmployee.empFullName='"+conditions[3]+"' " ;
    	}
    	AIODBManager aioMgt=new AIODBManager();
    	Result rs = aioMgt.sqlListMap(sql.toString(), null, pageNo, pageSize);
    	List listMap = (List)rs.retVal;
    	List list = new ArrayList();
    	for (int i = 0; i < listMap.size(); i++) {
    		HashMap map=(HashMap)listMap.get(i);
			String[] objs = new String[9];
			objs[0]=map.get("id").toString();
			objs[1]=map.get("titleType").toString();
			objs[2]=map.get("quantity").toString();
			objs[3]=map.get("startTime").toString();
			objs[4]=map.get("endTime").toString();
			objs[5]=map.get("empName").toString();
			objs[6]=map.get("createTime").toString();
			objs[7]=map.get("titleTypeEn").toString();
			objs[8]=map.get("detId").toString();
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
	
	/**
	 * 2.查询当前考卷
	 * @param id
	 * @return
	 */
	public Result queryExamById(final String id){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select distinct tblExamManage.id as id,startTime,endTime,tblExamManage.limitTime,tblEmployee.empFullName as empName,tblExamManage.createTime as createTime, tblExamManage.title as titleType,quantity " +
                        			"from tblExamManage,tblEmployee " +
                        			"where tblExamManage.createBy=tblEmployee.id " +
                        			"and tblExamManage.id='"+id+"'";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	String[] objs = null;
                        	if(rst.next()) {
                        		objs = new String[8];
								objs[0]=rst.getString("id");
								objs[1]=rst.getString("titleType");
								objs[2]=rst.getString("quantity");
								objs[3]=rst.getString("startTime");
								objs[4]=rst.getString("endTime");
								objs[5]=rst.getString("empName");
								objs[6]=rst.getString("createTime");
								objs[7]=rst.getString("limitTime");
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(objs) ;
                        } catch (Exception ex) {
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
	 * 3.查询出考卷所需要的符合题目类型的题目
	 * @param titleType
	 * @param quantity
	 * @return
	 */
	public Result queryProblemsManage(final String titleType,final Integer quantity){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql = null;
                        	if(quantity!=0){
                        		sql="select top "+quantity+" id,titleType,answerType,title,score from tblProblemsManage where titleType='"+titleType+"' order by newId()";
                        	}else{
                        		sql="select id,titleType,answerType,title,score from tblProblemsManage where titleType='"+titleType+"' order by newId()";
                        	}
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	//List list = new ArrayList();
                        	TreeMap<Integer, Problem> problemMap = new TreeMap<Integer, Problem>();
                        	Integer serialNum = 1;//题目序号
                        	while (rst.next()) {
								Problem obj = new Problem();
								obj.setId(rst.getString("id"));
								obj.setSerialNum(serialNum);
								obj.setTitleType(rst.getString("titleType"));
								obj.setTitle(rst.getString("title"));
								obj.setAnswerType(rst.getString("answerType"));
								obj.setScore(rst.getInt("score"));
								problemMap.put(serialNum, obj);
								serialNum++;
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(problemMap) ;
                        } catch (Exception ex) {
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
	 * 4.查询所有题目的选项
	 * @param refId
	 * @return
	 */
	public Result queryAPreSelectAnswer(final String refId){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select id,answerMarke,PreSelectAnswer,isCorrect from tblAPreSelectAnswer where f_ref='"+refId+"' order by detOrderNo asc";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	List list = new ArrayList();
                        	char sign = 'A';
                        	while (rst.next()) {
								Answer obj = new Answer();
								obj.setId(rst.getString("id"));
								obj.setSign(sign);
								obj.setAnswerMarke(rst.getString("answerMarke").charAt(0));
								obj.setDescription(rst.getString("preSelectAnswer"));
								obj.setCorrect(rst.getString("isCorrect"));
								obj.setUserAnswer(false);
								sign++;
								list.add(obj);
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(list) ;
                        } catch (Exception ex) {
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
	 * 查询问题类型
	 * @param refId
	 * @return
	 */
	public Result queryQuestionType(){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	String sql="select QuestionType from CRMQuestionType";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	List<String> list = new ArrayList<String>();
                        	while (rst.next()) {
								list.add(rst.getString("QuestionType"));
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(list) ;
                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 5.插入正确的用户答题数和用户此时的开始答题时间
	 * @param id
	 * @param total
	 * @return
	 */
	public Result updateExamDetOfTotalPros(final String id,final Integer total){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="update tblExamManageDet set totalProblem="+total+",startTime=convert(varchar(19),getdate(),120) " +
                        			"where id='"+id+"' and (startTime='' or startTime is null)";
                        	PreparedStatement ps = conn.prepareStatement(sql) ;
                    		ps.executeUpdate();
                        } catch (Exception ex) {
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
	 * 6.查询题目的正确答案
	 * @param problemId
	 * @return
	 */
	public Result queryCorrectAnswer(final String problemId){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select answerMarke from tblAPreSelectAnswer where f_ref='"+problemId+"' and isCorrect like '1,%'";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	String s = "";
                        	while (rst.next()) {
								s += rst.getString("answerMarke");
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(s) ;
                        } catch (Exception ex) {
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
	 * 7.查询当前用户是否做过这道题目,做过就把答案查出来
	 * @param strs
	 * @return
	 */
	public Result queryHistoryTest(final String[] strs){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select answer from tblHistoryTest where userId='"+strs[0]+"' and examManageDetId='"+strs[1]+"' and problemsManageId='"+strs[2]+"'";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	Object o = null;
                        	if (rst.next()) {
								o = rst.getString("answer");
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(o) ;
                        } catch (Exception ex) {
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
	 * 8.修改用户所做的答题
	 * @param strs
	 * @return
	 */
	public Result updateHistoryTest(final String[] strs){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="update tblHistoryTest set answer=? where userId=? and examManageDetId=? and problemsManageId=?";
                        	PreparedStatement ps = conn.prepareStatement(sql) ;
                    		ps.setString(1, strs[3]);
                    		ps.setString(2, strs[0]);
                    		ps.setString(3, strs[1]);
                    		ps.setString(4, strs[2]);
                    		ps.executeUpdate();
                        } catch (Exception ex) {
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
	 * 8.添加用户所做的答题
	 * @param strs
	 * @return
	 */
	public Result addHistoryTest(final String[] strs){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	String sql="insert into tblHistoryTest(userId,examManageDetId,problemsManageId,answer,correctAnswer,problemsScore) values(?,?,?,?,?,?)";
                        	PreparedStatement ps = conn.prepareStatement(sql) ;
                    		ps.setString(1, strs[0]);
                    		ps.setString(2, strs[1]);
                    		ps.setString(3, strs[2]);
                    		ps.setString(4, strs[3]);
                    		ps.setString(5, strs[4]);
                    		ps.setInt(6, Integer.parseInt(strs[5]));
                    		ps.executeUpdate();
                        } catch (Exception ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 9.查询考试剩余时间和开考时间
	 * @param detId
	 * @return
	 */
	public Result queryLeavingTime(final String detId){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select datediff(mi,convert(datetime,tblExamManageDet.startTime),convert(datetime,tblExamManage.endTime)) as leavingTime,tblExamManageDet.startTime as startTime from tblExamManage,tblExamManageDet " +
                        			"where tblExamManage.id=tblExamManageDet.f_ref and tblExamManageDet.id='"+detId+"'";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	String[] arr = new String[2];
                        	if(rst.next()) {
								arr[0] = rst.getString("leavingTime");
								arr[1] = rst.getString("startTime");
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(arr) ;
                        } catch (Exception ex) {
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
	 * 查询当前用户这次参加的考试的答题记录
	 * @param strs
	 * @return
	 */
	public Result queryHistoryTestAll(final String[] strs){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select id,answer,correctAnswer,problemsScore from tblHistoryTest where userId='"+strs[0]+"' and examManageDetId='"+strs[1]+"'";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	List list = new ArrayList();
                        	while (rst.next()) {
								String[] objs = new String[4];
								objs[0]=rst.getString("id");
								objs[1]=rst.getString("answer");
								objs[2]=rst.getString("correctAnswer");
								objs[3]=rst.getString("problemsScore");
								list.add(objs);
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(list) ;
                        } catch (Exception ex) {
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
	 * 
	 * @param id
	 * @param score
	 * @return
	 */
	public Result updateExamManageDet(final String id,final double score){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="update tblExamManageDet set score="+score+",endTime=convert(varchar(19),getdate(),120) " +
                        			",useTime=datediff(mi,convert(datetime,startTime),getdate()) " +
                        			"where id='"+id+"'";
                        	PreparedStatement ps = conn.prepareStatement(sql) ;
                    		ps.executeUpdate();
                        } catch (Exception ex) {
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
	 * 查询对应题目
	 */
	public Result queryProblemsManageById(final String id){
		final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                        	String sql="select id,title from tblProblemsManage where id='" + id + "'";
                        	Statement st = conn.createStatement();
                        	ResultSet rst = st.executeQuery(sql);
                        	List list = new ArrayList();
                        	while (rst.next()) {
								String[] objs = new String[2];
								objs[0]=rst.getString("id");
								objs[1]=rst.getString("title");
								list.add(objs);
							}
                        	rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                        	rs.setRetVal(list) ;
                        } catch (Exception ex) {
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
	
	

}
