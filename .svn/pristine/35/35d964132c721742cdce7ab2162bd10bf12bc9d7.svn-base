package com.koron.oa.framework;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.Department;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

public class FrameworkMgt extends DBManager{

	/**
     * 查询部门
     */
    public Result queryFolder() {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "select id,classCode,deptFullName,remark,isCatalog from tblDepartment where classCode is not null and statusid=0 order by deptCode";
						try {
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ResultSet rs = ps.executeQuery();
							ArrayList list=new ArrayList();
							while(rs.next()){
								String []str=new String[5];
								str[0]=rs.getString(1);
								str[1]=rs.getString(2);
								str[2]=rs.getString(3);
								str[3]=rs.getString(4);
								str[4]=rs.getString(5);
								list.add(str);
							}
							rst.setRetVal(list);
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
		rst.setRetCode(retCode);
		return rst ;
	}
    
    /**
     * 根据部门名称查询详细信息
     * @param deptName
     * @return
     */
    public Result getDepartMent(final String deptName) {
    	final Result rs = new Result();

    	int retCode = DBUtil.execute(new IfDB() {
    		public int exec(Session session) {
    			session.doWork(new Work() {
    				public void execute(Connection conn) throws
    				SQLException {
    					Statement st=conn.createStatement();
    					ResultSet rss=st.executeQuery("select a.DeptFullName,b.empFullName,a.Responsibility,a.classCode from tblDepartment a " +
    												  "left join tblEmployee b on a.departmentManager=b.id where a.DeptFullName='"+deptName+"'");
    					if(rss.next()){
    						String[] depart = new String[4] ;
    						depart[0] = rss.getString(1) ;
    						depart[1] = rss.getString(2) ;
    						depart[2] = rss.getString(3) ;
    						depart[3] = rss.getString(4) ;
    						rs.setRetVal(depart);
    					}
    				}
    			});
    			return rs.getRetCode();
				}
			});
    	rs.setRetCode(retCode);
    	return rs;
    }
    
    public Result getDepartMentById(final String deptId) {
    	final Result rs = new Result();

    	int retCode = DBUtil.execute(new IfDB() {
    		public int exec(Session session) {
    			session.doWork(new Work() {
    				public void execute(Connection conn) throws
    				SQLException {
    					Statement st=conn.createStatement();
    					ResultSet rss=st.executeQuery("select a.DeptFullName,b.empFullName,a.Responsibility,a.classCode from tblDepartment a " +
    												  "left join tblEmployee b on a.departmentManager=b.id where a.id='"+deptId+"'");
    					if(rss.next()){
    						String[] depart = new String[4] ;
    						depart[0] = rss.getString(1) ;
    						depart[1] = rss.getString(2) ;
    						depart[2] = rss.getString(3) ;
    						depart[3] = rss.getString(4) ;
    						rs.setRetVal(depart);
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
     * 获取部门人员整数
     * @param deptName
     * @return
     */
    public Result getDeptEmpNumber(final String deptCode) {
    	final Result rs = new Result();

    	int retCode = DBUtil.execute(new IfDB() {
    		public int exec(Session session) {
    			session.doWork(new Work() {
    				public void execute(Connection conn) throws
    				SQLException {
    					Statement st=conn.createStatement();
    					ResultSet rss=st.executeQuery("select count(*) from tblEmployee where statusid=0 and departmentCode='"+deptCode+"'");
    					if(rss.next()){
    						rs.setRetVal(rss.getInt(1));
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
     * 根据部门主管Id查找部门ClassCode
     * @param departCode
     * @return
     */
    public Result getDeptClassCodeByManager(final String userId) {
    	final Result rs = new Result();
    	int retCode = DBUtil.execute(new IfDB() {
    		public int exec(Session session) {
    			session.doWork(new Work() {
    				public void execute(Connection conn) throws SQLException {
    					String sql = "select classCode from tblDepartment where departmentManager=?" ;
    					PreparedStatement pss=conn.prepareStatement(sql) ;
    					pss.setString(1, userId) ;
    					ResultSet rss=pss.executeQuery() ;
    					ArrayList<String> list = new ArrayList<String>() ;
    					while(rss.next()){
    						list.add(rss.getString("classCode")) ;
    					}
    					rs.setRetVal(list);
    				}
    			});
    			return rs.getRetCode();
				}
			});
    	rs.setRetCode(retCode);
    	return rs;
    }

    /**
     * 根据部门classCode查找职员
     * 传id
     * @param loginId String
     * @return Result
     */
    public Result selectDeptNameByID(final String classCode) {
        ArrayList paramlist = new ArrayList() ;
        String hql = "select bean from EmployeeBean bean where bean.DepartmentCode=? and bean.statusId=-1" ;
        paramlist.add(classCode) ;
        return list(hql, paramlist) ;
    }
    
    public Result loadEmployeeBean(final String id) {
       final Result rs = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection conn) throws
                           SQLException {
                       try {
                    	   String sql = "select a.id,a.EmpFullName,b.DeptFullName,a.Responsibility,b.departmentManager,a.TitleID,a.Tel,a.Mobile,a.Email,a.BornDate from tblEmployee a "
               	   						+"left join tblDepartment b on a.departmentCode=b.classcode where a.id=?" ;
                    	   PreparedStatement pss = conn.prepareStatement(sql) ;
                    	   pss.setString(1, id) ;
                    	   ResultSet rss = pss.executeQuery() ;
                    	   if(rss.next()){
                    		   String[] emp = new String[10] ;
                    		   emp[0] = rss.getString("id")  ;
                    		   emp[1] = rss.getString("EmpFullName")  ;
                    		   emp[2] = rss.getString("DeptFullName")  ;
                    		   emp[3] = rss.getString("Responsibility")  ;
                    		   emp[4] = rss.getString("departmentManager")  ;
                    		   emp[5] = rss.getString("TitleID")  ;
                    		   emp[6] = rss.getString("Tel")  ;
                    		   emp[7] = rss.getString("Mobile")  ;
                    		   emp[8] = rss.getString("Email")  ;
                    		   emp[9] = rss.getString("BornDate")  ;
                    		   rs.setRetVal(emp) ;
                    	   }
                       } catch (SQLException ex) {   
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
     * 调用存储过程查询用户详细到临时表中
     * @param tableName
     * @return
     */
    public Result queryEmpInfo(final String language,final String userId,final String strYear,
    						   final String strMonth) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;  
                        try {
                            CallableStatement cs = conn.prepareCall("{call SP_EMPCounting(?,?,?,?)}") ;
                            cs.setString(1, language) ;
                            cs.setString(2, userId) ;
                            cs.setString(3, strYear) ;
                            cs.setString(4, strMonth) ;
                            cs.execute() ;
                            String sql = "select * from tblEmpCounts where languageType=? and DisUser=? order by DisType,orderBy" ;
                            PreparedStatement pss = conn.prepareStatement(sql) ;
                            pss.setString(1, language) ;
                            pss.setString(2, userId) ;
                            ResultSet rss = pss.executeQuery() ;
                            ArrayList<String[]> empList = new ArrayList<String[]>() ;
                            while(rss.next()){
                            	String[] empcounts = new String[3] ;
                            	empcounts[0] = rss.getString("DisType") ;
                            	empcounts[1] = rss.getString("DisLabel") ;
                            	empcounts[2] = rss.getString("DisCount") ;
                            	empList.add(empcounts) ;
                            }
                            rs.setRetVal(empList) ;
                        } catch (SQLException ex) {   
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace() ;
                            BaseEnv.log.error("FrameworkMgt queryEmpInfo",ex) ;
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
     * 调用存储过程查询部门详细到临时表中
     * @param tableName
     * @return
     */
    public Result queryDeptInfo(final String language,final String userId,final String strYear,
    						   final String strMonth) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;  
                        try {
                            CallableStatement cs = conn.prepareCall("{call SP_DEPTCounting(?,?,?,?)}") ;
                            cs.setString(1, language) ;
                            cs.setString(2, userId) ;
                            cs.setString(3, strYear) ;
                            cs.setString(4, strMonth) ;
                            cs.execute() ;
                            String sql = "select * from tblDeptCounts where languageType=? and DisUser=? order by DisType,orderBy" ;
                            PreparedStatement pss = conn.prepareStatement(sql) ;
                            pss.setString(1, language) ;
                            pss.setString(2, userId) ;
                            ResultSet rss = pss.executeQuery() ;
                            ArrayList<String[]> empList = new ArrayList<String[]>() ;
                            while(rss.next()){
                            	String[] empcounts = new String[3] ;
                            	empcounts[0] = rss.getString("DisType") ;
                            	empcounts[1] = rss.getString("DisLabel") ;
                            	empcounts[2] = rss.getString("DisCount") ;
                            	empList.add(empcounts) ;
                            }
                            rs.setRetVal(empList) ;
                        } catch (SQLException ex) {   
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace() ;
                            BaseEnv.log.error("FrameworkMgt queryEmpInfo",ex) ;
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
     * 查一条系统用户的详细信息
     *
     * @param notepadId
     *            long 代号
     * @return Result 返回结果
     */
    public Result detail(String id) {
        // 装载着user表中的数据，userrole表中的数据，usermodule表中的数据
        return loadBean(id, EmployeeBean.class);
    }
    
    /**
     * 查询所有用户
     * @return
     */
    public Result queryAllEmployee(){
    	String hql = "select bean from EmployeeBean as bean where bean.statusId!=-1 order by bean.createTime" ;
    	List param = new ArrayList() ;
    	return list(hql, param);
    }
    
    /**
     * 查询所有部门
     * @return
     */
    public Result queryAllDetp() {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                     	   String sql = "select * from tblDepartment where statusid!=-1 order by DeptCode, classCode" ;
                     	   PreparedStatement pss = conn.prepareStatement(sql) ;
                     	   ResultSet rss = pss.executeQuery() ;
                     	   List<Department> listDept = new ArrayList<Department>() ;
                     	   while(rss.next()){
                     		   Department dept = new Department() ;
                     		   dept.setid(rss.getString("id"))  ;
                     		   dept.setDeptFullName(rss.getString("DeptFullName"))  ;
                     		   dept.setDeptCode(rss.getString("deptCode"));
                     		   dept.setclassCode(rss.getString("classCode"))  ;
                     		   listDept.add(dept) ;
                     	   }
                     	  rs.setRetVal(listDept) ;
                        } catch (SQLException ex) {   
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace() ;
                            BaseEnv.log.error("FrameworkMgt queryAllDept method :",ex) ;
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
     * 查询指定目录下的部门
     * @return
     */
    public Result queryDetpByClassCode(final String classcode) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                     	   String sql = "select * from tblDepartment where classcode like '"+classcode+"%' order by deptCode" ;
                     	   PreparedStatement pss = conn.prepareStatement(sql) ;
                     	   ResultSet rss = pss.executeQuery() ;
                     	   List<Department> listDept = new ArrayList<Department>() ;
                     	   while(rss.next()){
                     		   Department dept = new Department() ;
                     		   dept.setid(rss.getString("id"))  ;
                     		   dept.setDeptFullName(rss.getString("DeptFullName"))  ;
                     		   dept.setclassCode(rss.getString("classCode"))  ;
                     		   listDept.add(dept) ;
                     	   }
                     	  rs.setRetVal(listDept) ;
                        } catch (SQLException ex) {   
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace() ;
                            BaseEnv.log.error("FrameworkMgt queryAllDept method :",ex) ;
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
