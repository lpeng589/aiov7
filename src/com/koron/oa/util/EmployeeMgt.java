package com.koron.oa.util;

import java.sql.SQLException;
import org.hibernate.Session;
import java.sql.Connection;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.Result;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.koron.oa.bean.Employee;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.BaseEnv;

import java.util.List;
import com.dbfactory.hibernate.IfDB;
import org.hibernate.jdbc.Work;

import java.sql.PreparedStatement;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Aug 12, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class EmployeeMgt extends AIODBManager {
	
	
	/**
	 * 根据用户ID查找对应的职员组
	 * @param groupId
	 * @return
	 */
	public String selectEmpGroupByUid(final String uid){
		final Result rs = new Result() ;
		int retCode = DBUtil.execute(new IfDB() {
	          public int exec(Session session) {
	              session.doWork(new Work() {
	                  public void execute(Connection conn) throws SQLException {
	                      try {  
	                    	  String sql = "select g.id from  dbo.tblEmpGroup g left join tblEmpGroupUser e on(g.id=e.f_ref) where userID=?" ;
	                    	  PreparedStatement ps = conn.prepareStatement(sql) ;
	                    	  ps.setString(1, uid) ;
	                    	  ResultSet rss = ps.executeQuery() ;
	                    	  String groupIds="";
	                    	  while(rss.next()){
	                    		  String groupId = rss.getString("id") ;
	                    		  groupIds+=groupId+"," ;
	                    	  }
	                    	  rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
	                    	  rs.setRetVal(groupIds) ;
	                      } catch (SQLException ex) {
	                          rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                          BaseEnv.log.error("OAChatMgt--selectEmpGroupByUid()", ex) ;
	                          return ;
	                      }
	                  }
	              });
	              return rs.getRetCode();
	          }
	    });
		rs.setRetCode(retCode) ;
		String result = null ;
		if(retCode==ErrorCanst.DEFAULT_SUCCESS){
			result = (String)rs.getRetVal() ;
		}
		return result ;
	}
	
	/**
	 * 根据分组ID查找对应的职员
	 * @param groupId
	 * @return
	 */
	public String selectEmpByGroupId(final String groupId){
		final Result rs = new Result() ;
		int retCode = DBUtil.execute(new IfDB() {
	          public int exec(Session session) {
	              session.doWork(new Work() {
	                  public void execute(Connection conn) throws  SQLException {
	                      try {  
	                    	  String sql = "select userID from tblEmpGroupUser where f_ref=?" ;
	                    	  PreparedStatement ps = conn.prepareStatement(sql) ;
	                    	  ps.setString(1, groupId) ;
	                    	  ResultSet rss = ps.executeQuery() ;
	                    	  String userIds="";
	                    	  while(rss.next()){
	                    		  String userId = rss.getString("userID") ;
	                    		  userIds+=userId+"," ;
	                    	  }
	                    	  rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
	                    	  rs.setRetVal(userIds) ;
	                      } catch (SQLException ex) {
	                          rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                          BaseEnv.log.error("EmpGroupMgt--selectEmpByGroupId()", ex) ;
	                          return ;
	                      }
	                  }
	              });
	              return rs.getRetCode();
	          }
	    });
		rs.setRetCode(retCode) ;
		String result = "" ;
		if(retCode==ErrorCanst.DEFAULT_SUCCESS){
			result = (String)rs.getRetVal() ;
		}
		return result ;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	 public Result selectEmpGroupById(final String id){
		final Result rs = new Result() ;
		int retCode = DBUtil.execute(new IfDB() {
	          public int exec(Session session) {
	              session.doWork(new Work() {
	                  public void execute(Connection conn) throws SQLException {
	                      try {  
	                    	  String sql = "select * from  dbo.tblEmpGroup where id=?" ;
	                    	  PreparedStatement ps = conn.prepareStatement(sql) ;
	                    	  ps.setString(1, id) ;
	                    	  ResultSet rss = ps.executeQuery() ;
	                            String value[] = new String[5];
	                            if (rss.next()) {
	                                value[0] = rss.getString("id"); //将id保存
	                                value[1] = rss.getString("groupName"); //职员分组
	                                value[2] = rss.getString("groupRemark"); //职员分组备注
	                                value[3] = rss.getString("createBy"); //创建人
	                                value[4] = rss.getString("createTime"); //创建时间
	                            }
	                            rs.setRetVal(value);
	                            rs.setRealTotal(value.length);
	                            rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);;
	                      } catch (SQLException ex) {
	                          rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                          BaseEnv.log.error("OAChatMgt--selectEmpGroupById()", ex) ;
	                          return ;
	                      }
	                  }
	              });
	              return rs.getRetCode();
	          }
	    });
		rs.retCode = retCode;
		return rs ;
	}
	
	 /**
	  * 查询职员分组
	  * @return
	  */
	public Result getEmpGroup() {
    	final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement st=conn.createStatement();
						ResultSet set=st.executeQuery("SELECT id,groupName FROM tblEmpGroup");
						ArrayList list=new ArrayList();
						while(set.next()){
							String []str=new String[2];
							str[0]=set.getString(1);
							str[1]=set.getString(2);
							list.add(str);
						}
						rst.setRetVal(list);
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
        return rst;
    }
	 
	/**
	 * 根据用户ID查找所属部门的部门主管
	 * @param
	 * @return
	 */
	public Result selectDepartmentManagerByUid(final String strUserId,
			final Connection conn) {
		final Result rs = new Result();
		try {
			String sql = "select d.departmentManager from tblDepartment d left join tblEmployee e on d.classCode=DepartmentCode where e.id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, strUserId);
			ResultSet rss = ps.executeQuery();
			String departmentManager = null;
			if (rss.next()) {
				departmentManager = rss.getString("departmentManager");
			}
			rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
			rs.setRetVal(departmentManager);
		} catch (SQLException ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			BaseEnv.log.error("OAChatMgt--selectEmpGroupUserByGroupId()", ex);
		}
		return rs;
	}
	
	/**
	 * 根据用户ID查找所属部门的部门主管
	 * 
	 * @param
	 * @return
	 */
	public Result selectDepartmentManagerByUid(final String strUserId){
		final Result rs = new Result() ;
		int retCode = DBUtil.execute(new IfDB() {
	          public int exec(Session session) {
	              session.doWork(new Work() {
	                  public void execute(Connection conn) throws SQLException {
	                	  Result res = selectDepartmentManagerByUid(strUserId,conn) ;
	                	  rs.setRetCode(res.getRetCode()) ;
	                	  rs.setRetVal(res.getRetVal()) ;
	                  }
	              });
	              return rs.getRetCode();
	          }
	    });
		rs.setRetCode(retCode) ;
		return rs ;
	}
	

	//根据部门编号查找属于该部门的所有职员
	public Result queryEmpByDept(final String departmentcode) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						String strsql = "select * from dbo.tblEmployee where DepartmentCode like '%"+departmentcode+"%'";
						try {
							Statement s = conn.createStatement();
							ResultSet rs = s.executeQuery(strsql);
							List<Employee> list = new ArrayList<Employee>();
							while (rs.next()) {
								Employee obj = new Employee();
								obj.setid(rs.getString("id"));
								obj.setEmpFullName(rs.getString("EmpFullName"));
								obj.setEmpName(rs.getString("EmpName"));
								obj.setGraduateID(rs.getString("GraduateID"));
								obj.setTitleID(rs.getString("TitleID"));
								obj.setTopCost(rs.getFloat("TopCost"));
								obj.setTopCredit(rs.getString("TopCredit"));
								obj.setEmail(rs.getString("Email"));
								obj.setcreateBy(rs.getString("createBy"));
								obj.setlastUpdateBy(rs.getString("lastUpdateBy"));
								obj.setcreateTime(rs.getString("createTime"));
								obj.setlastUpdateTime(rs.getString("lastUpdateTime"));
								obj.setDepartmentCode(rs.getString("DepartmentCode"));
								obj.setBornDate(rs.getString("BornDate"));
								obj.setEmployDate(rs.getString("EmployDate"));
								obj.setEmpNumber(rs.getString("EmpNumber"));
								obj.setempCode(rs.getString("empCode"));
								obj.setsysName(rs.getString("sysName"));
								obj.setpassword(rs.getString("password"));
								obj.setopenFlag(rs.getInt("openFlag"));
								obj.setdefSys(rs.getString("defSys"));
								obj.setRemark(rs.getString("Remark"));
								obj.setAttachment(rs.getString("Attachment"));
								obj.setManagerITel(rs.getString("ManagerITel"));
								obj.setTel(rs.getString("Tel"));
								obj.setMobile(rs.getString("Mobile"));
								obj.setSCompanyID(rs.getString("SCompanyID"));
								obj.setclassCode(rs.getString("classCode"));
								obj.setRowON(rs.getString("RowON"));
								list.add(obj);
							}
							rst.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log
									.error("Query data Error :" + strsql, ex);
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
     * 查询所有用户(tblEmployee)
     * @param loginId String
     * @return Result
     */
    public Result sel_allEmployee() {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws SQLException {
                        Connection conn = connection;
                        List listEmp = new ArrayList() ;
                        try {
                            Statement st = conn.createStatement();
                            //查询会员表(oABBSUser)
                            String sql = "select * from dbo.tblEmployee where statusId!=-1";
                            ResultSet rss = st.executeQuery(sql);
                            while (rss.next()) {
                            	listEmp.add(rss.getString("id")); //将id保存
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
     * 根据部门ClassCode查询该部门下的所有职员
     */
    public List<Employee> queryAllEmployeeByClassCode(final String deptClassCode) {
	final Result rst = new Result();
	int retCode = DBUtil.execute(new IfDB() {
		public int exec(Session session) {
			session.doWork(new Work() {
				public void execute(Connection connection)
						throws SQLException {
					Connection conn = connection;
					String strsql = "select emp.id,emp.EmpFullName,emp.EmpFullNamePYM,emp.departmentcode from dbo.tblEmployee emp,dbo.tblDepartment dep " +
									"where 1=1 and dep.classCode=emp.DepartmentCode and emp.openFlag=1 and emp.statusId!=-1 ";
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
							obj.setEmpfullnamePym(rs.getString("EmpFullNamePYM"));
							obj.setDepartmentCode(rs.getString("departmentcode"));
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
     * 
     * @param id
     * @return
     */
    public EmployeeBean loadEmployee(String id) {
        Result result = loadBean(id, EmployeeBean.class);
        if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	return (EmployeeBean) result.retVal;
        }
        return null;
    }

    public Employee getEmployeeById(String userId){
    	Result result = queryEmployeeByUserId(userId);
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		return (Employee)result.retVal;
    	}
    	return null;
    }
	
	public Result queryEmployeeByUserId(final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							PreparedStatement pstmt = conn.prepareStatement("select * from tblEmployee where id=?");
							pstmt.setString(1, userId);
							ResultSet rs = pstmt.executeQuery();
							while (rs.next()) {
								Employee obj = new Employee();
								obj.setid(rs.getString("id"));
								obj.setEmpFullName(rs.getString("EmpFullName"));
								obj.setEmpName(rs.getString("EmpName"));
								obj.setGraduateID(rs.getString("GraduateID"));
								obj.setTitleID(rs.getString("TitleID"));
								obj.setTopCost(rs.getFloat("TopCost"));
								obj.setTopCredit(rs.getString("TopCredit"));
								obj.setEmail(rs.getString("Email"));
								obj.setcreateBy(rs.getString("createBy"));
								obj.setlastUpdateBy(rs.getString("lastUpdateBy"));
								obj.setcreateTime(rs.getString("createTime"));
								obj.setlastUpdateTime(rs.getString("lastUpdateTime"));
								obj.setDepartmentCode(rs.getString("DepartmentCode"));
								obj.setBornDate(rs.getString("BornDate"));
								obj.setEmployDate(rs.getString("EmployDate"));
								obj.setEmpNumber(rs.getString("EmpNumber"));
								obj.setempCode(rs.getString("empCode"));
								obj.setsysName(rs.getString("sysName"));
								obj.setpassword(rs.getString("password"));
								obj.setopenFlag(rs.getInt("openFlag"));
								obj.setdefSys(rs.getString("defSys"));
								obj.setRemark(rs.getString("Remark"));
								obj.setAttachment(rs.getString("Attachment"));
								obj.setManagerITel(rs.getString("ManagerITel"));
								obj.setTel(rs.getString("Tel"));
								obj.setMobile(rs.getString("Mobile"));
								obj.setSCompanyID(rs.getString("SCompanyID"));
								obj.setclassCode(rs.getString("classCode"));
								obj.setRowON(rs.getString("RowON"));
								rst.setRetVal(obj);
								rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							}
							
						} catch (Exception ex) {
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
	

	 /**
     * 根据分组 查询该分组下的所有职员
     */
	public List<Employee> queryAllEmployeeByGroup(final String group) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
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
	 * 查询所有的部门
	 * @return
	 */
	public Result getAllDetps() {
    	final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Statement st = conn.createStatement();
						ResultSet set=st.executeQuery("select classCode,DeptFullName,isCatalog from tblDepartment order by deptCode asc");
						ArrayList list=new ArrayList();
						while(set.next()){
							String []str=new String[3];
							str[0]=set.getString(1);
							str[1]=set.getString(2);
							str[2]=set.getString(3);
							list.add(str);
						}
						rst.setRetVal(list);
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
        return rst;
    }
}
