package com.menyi.web.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.omg.CORBA.Request;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.oa.bean.ApproveBean;
import com.koron.oa.bean.ConditionBean;
import com.koron.oa.bean.ConditionsBean;
import com.koron.oa.bean.FieldBean;
import com.koron.oa.bean.FlowNodeBean;
import com.koron.oa.bean.HTMLTemplate;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.koron.oa.bean.WorkFlowDesignBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.web.login.*;

/**
 * 
 * <p>Title:这是所有的页面的公用数据库操作类</p> 
 * <p>Description: </p>
 *
 * @Date:Aug 5, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class PublicMgt extends AIODBManager {

	
	public Result exec(final String mysql) {
        final Result res = new Result();
        
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        
                        try{	
	                        //判断是否是查询语句
                        	String sql = mysql.trim();
	                        boolean isSelect=sql.toLowerCase().startsWith("select ");
                        	PreparedStatement psmt = conn.prepareStatement(sql);
	                        
	                        if(!isSelect){	                        	
	                        	int r =psmt.executeUpdate();
	                        	res.retVal = "updateNum="+r;	                        	
	                        }else{
		                        ResultSet rst = psmt.executeQuery();
		                        ArrayList list = new ArrayList();
		                        res.retVal = list;		
		                        
		                        while (rst.next()) {
		                        	HashMap map=new HashMap();
	                            	for(int i=1;i<=rst.getMetaData().getColumnCount();i++){
	                            		Object obj=rst.getObject(i);
	                            		if(obj==null){
	                            			if(rst.getMetaData().getColumnType(i)==Types.NUMERIC||rst.getMetaData().getColumnType(i)==Types.INTEGER){
	                            				map.put(rst.getMetaData().getColumnName(i), 0);
	                            			}else{
	                            				map.put(rst.getMetaData().getColumnName(i), "");
	                            			}
	                            		}else{
	                            			map.put(rst.getMetaData().getColumnName(i), obj);
	                            		}
	                            	}
	                            	list.add(map);
								}
		                        res.retVal = list;
	                        }
                        }catch(Exception e){
                        	res.retCode = ErrorCanst.DEFAULT_FAILURE;
                        	res.retVal = "执行失败"+e.getMessage();
                        	BaseEnv.log.error("PublicMgt.exec ",e);
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    } 
	public Result execProc(final String mysql) {
        final Result res = new Result();
        
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        
                        try{	
	                        //判断是否是查询语句
                        	String sql = mysql.trim();
                        	if(sql.indexOf("exec") > -1){
                        		sql = sql.substring(0,sql.indexOf("exec")+4).trim();
                        	}
	                        String procName = sql.substring(0,sql.indexOf(" "));
	                        String param = sql.substring(sql.indexOf(" ")).trim();
	                        String[] params = param.split(",");
	                        sql  = "{call "+procName+"(";
	                        for(String p:params){
	                        	sql +="?,";
	                        }
	                        sql = sql.substring(0,sql.length() -1)+")";
	                        
	                        CallableStatement psmt = conn.prepareCall(sql);
	                        ArrayList<Integer> outputList = new ArrayList();
	                        
	                        HashMap outputMap = new HashMap();
                        	for(int i=1;i<=params.length;i++){
                        		if(params[i-1].toLowerCase().indexOf("output")>-1){
                        			psmt.registerOutParameter(i, Types.VARCHAR, 5000);
                        			outputList.add(i);
                        			String pn = params[i-1].trim();
                        			pn = pn.substring(0,pn.indexOf(" "));
                        			outputMap.put(i,pn );
                        		}else{
                        			psmt.setString(i, params[i-1]);
                        		}
	                        }
                        	ResultSet rst =psmt.executeQuery();
                        	for(Object i:outputList){
                        		String pn = outputMap.remove(i)+"";
                        		outputMap.put(pn,psmt.getString( (Integer)i));
                        	}
							
	                        ArrayList list = new ArrayList();
	                        while (rst.next()) {
	                        	HashMap map=new HashMap();
                            	for(int i=1;i<=rst.getMetaData().getColumnCount();i++){
                            		Object obj=rst.getObject(i);
                            		if(obj==null){
                            			if(rst.getMetaData().getColumnType(i)==Types.NUMERIC||rst.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rst.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rst.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rst.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	list.add(map);
							}
	                        res.retVal = new Object[]{outputMap, list};
	                        
                        }catch(Exception e){
                        	res.retCode = ErrorCanst.DEFAULT_FAILURE;
                        	res.retVal = "执行失败"+e.getMessage();
                        	BaseEnv.log.error("PublicMgt.execProc ",e);
                        }
                    }
                });
                return res.getRetCode();
            }
        });
        res.setRetCode(retCode);
        return res;
    } 
	
	/**
	 * 查模块地址
	 * @param moduleId
	 * @return
	 */
	public Result getModuleAddress(final String moduleId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "select linkAddress,classCode,mainModule from tblModules where id=?";
							PreparedStatement st = conn.prepareStatement(sql);
							st.setString(1, moduleId);							
							ResultSet rss = st.executeQuery();
							if (rss.next()) {
								rs.retVal = new String[]{rss.getString(1),rss.getString(2),rss.getString(3)};
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
	 * 查模块地址
	 * @param moduleId
	 * @return
	 */
	public Result getFlowByTableName(final String tableName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						Connection conn = connection;
						try {
							String sql = "select id from OAWorkFlowTemplate where templateFile=? and statusId=0";
							PreparedStatement st = conn.prepareStatement(sql);
							st.setString(1, tableName);							
							ResultSet rss = st.executeQuery();
							if (rss.next()) {
								rs.retVal = rss.getString(1);
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
	
	
	public Result startFlow(final String id,final String templateName,final String templateClass,final String templateFile,
            final String workFlowFile , final String retdefineName,final String defineName,final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
		  public int exec(Session session) {
		      session.doWork(new Work() {
		          public void execute(Connection conn) throws  SQLException {
		              try {
		                  String esql = "insert into OAWorkFlowTemplate(id,templateName,templateType,templateClass,templateFile,workFlowFile,allowVisitor," +
		                  		"templateStatus,wakeUp,finishTime,flowOrder,readLimit,detail,[affix],statusId,fileFinish,depMonitorScope,perMonitorScope," +
		                  		"depMonitor,perMonitor,titleTemp,nextWake,startWake,allWake,setWake,setWakeDept,setWakePer,setWakeGroup,stopStartWake," +
		                  		"stopSAllWake,stopSetWake,stopSetWakeDept,stopSetWakePer,stopSetWakeGroup,autoPass,retCheckPerRule,retdefineName,defineName," +
		                  		"version,sameFlow,createTime,updateBy,dispenseWake,reviewWake,defStatus) " +
		                  		"values(?,?,'1',?,?,?,',,,|,,,1,|,,,,,',0,'',0,0,0,'','',0,0,0,0,'','','',4,4,4,'','','1,','','','','','','',''," +
		                  		"'true',0,?,?,1,?,?,?,'','','all')";
		                  PreparedStatement pstmt = conn.prepareStatement(esql);
		                  pstmt.setString(1, id);
		                  pstmt.setString(2, templateName);
		                  pstmt.setString(3, templateClass);
		                  pstmt.setString(4, templateFile);
		                  pstmt.setString(5, workFlowFile);
		                  pstmt.setString(6, retdefineName) ;
		                  pstmt.setString(7, defineName) ;
		                  pstmt.setString(8, id) ;
		                  pstmt.setString(9, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss)) ;
		                  pstmt.setString(10, userId) ;
		                  pstmt.execute();
		              } catch (Exception ex) {
		                  ex.printStackTrace();
		                  rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		                  return;
		              }
		          }
		      });
		      if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS){
		    	  OAWorkFlowTemplate workFlow =(OAWorkFlowTemplate) loadBean(id, OAWorkFlowTemplate.class,session).retVal;
		    	  BaseEnv.workFlowInfo.put(workFlow.getId(), workFlow) ;
		      }
		      return rst.getRetCode();
		  }
		});
		rst.setRetCode(retCode);
		return rst;
	}
	
	
	/**
	 * 查询用户表(tblEmployee)
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
						List listEmp = new ArrayList();
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
	 * 根据Id查看某个模版
	 * @param crmModuleBean
	 * @return
	 */
	public Result detailCrmModule(String keyId) {
		return this.loadBean(keyId, ClientModuleBean.class);
	}

	/**
	 * CRM 根据模版视图名称查询客户模板
	 * @return
	 */
	public Result findmoduleViewByName(String viewName, String moduleId) {
		String hql = " from ClientModuleViewBean  where viewName='" + viewName + "' and moduleId='" + moduleId + "'";
		return list(hql, null);
	}

	/**
	 * CRM 根据权限查询所有模板视图
	 * @return
	 */
	public Result queryAllModuleViews(LoginBean loginBean, String moduleId) {
		String hql = " from ClientModuleViewBean  where id!='0' and moduleId ='" + moduleId + "'";
		List<String> param = new ArrayList<String>();
		if (!"1".equals(loginBean.getId())) {
			hql += " and (isAlonePopedmon='0' or dbo.exist_dept(popedomDeptIds,?)='true' or popedomUserIds like ? or popedomEmpGroupIds like ? ) ";
			param.add("" + loginBean.getDepartCode() + "");
			param.add('%' + loginBean.getId() + '%');
			String group = " ";
			if (!"".equals(loginBean.getGroupId())) {
				group = loginBean.getGroupId();
			}
			param.add('%' + group + '%');
		}
		hql += " order by createTime";
		return list(hql, param);//调用list返回结果
	}

	/**
	 * 工作流查询
	 * @param locale
	 * @param loginBean
	 * @return
	 */
	public Result queryClass(LoginBean loginBean) {

		ArrayList<String> param = new ArrayList<String>();
		String sql = "select work.id,work.templateName,work.templateType,a.ModuleName,work.templateStatus,work.templateClass,work.statusId,work.flowOrder,work.templateFile,work.allowAdd,work.timeType,work.timeVal,work.designType "
				+ " from OAWorkFlowTemplate work, tblWorkFlowType a where work.templateClass=a.classCode and a.classCode like '00002%' ";
		sql += " and work.fileFinish='1' and work.templateStatus=1 and work.statusId=0 ";
		if (!"1".equals(loginBean.getId())) {
			sql += " and ((charIndex('," + loginBean.getId() + ",',work.allowVisitor)>0 or charIndex('," + loginBean.getDepartCode() + ",',work.allowVisitor)>0 or charIndex(',"
					+ loginBean.getTitleId() + ",',work.allowVisitor)>0 ";
			String departCode = loginBean.getDepartCode();
			while (departCode.length() > 5) {
				departCode = departCode.substring(0, departCode.length() - 5);
				sql += " or charIndex('," + departCode + ",',work.allowVisitor)>0";
			}
			if (loginBean.getGroupId().length() > 0) {
				String[] groups = loginBean.getGroupId().split(";");
				for (int i = 0; i < groups.length; i++) {
					sql += " or charIndex('," + groups[i] + ",',work.allowVisitor)>0";
				}
			}
			sql += ") or work.allowVisitor like '')";
		}
		sql += " order by work.flowOrder";
		BaseEnv.log.debug("PublicMgt.queryClass 查询可新建工作流sql="+sql);
		//调用list返回结果  
		return sqlList(sql, param);
	}

	/**
	 * 根据部门ClassCode查询该部门下的所有职员
	 */
	public List<EmployeeBean> queryAllEmployeeByClassCode(final String deptClassCode) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String strsql = "select emp.id,emp.EmpFullName from dbo.tblEmployee emp,dbo.tblDepartment dep "
								+ "where 1=1 and dep.classCode=emp.DepartmentCode and emp.openFlag=1 and emp.statusId!=-1";
						if (null != deptClassCode && !"".equals(deptClassCode)) {
							strsql += " and dep.classCode like '" + deptClassCode + "%'";
						}
						try {
							Statement s = conn.createStatement();
							ResultSet rs = s.executeQuery(strsql);
							List<EmployeeBean> list = new ArrayList<EmployeeBean>();
							while (rs.next()) {
								EmployeeBean obj = new EmployeeBean();
								obj.setId(rs.getString("id"));
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
		List<EmployeeBean> list = null;
		if (retCode == ErrorCanst.DEFAULT_SUCCESS) {
			list = (List<EmployeeBean>) rst.getRetVal();
		}
		return list;
	}

	public String getDepartNameByCode(String classCode) {
		String name = "";
		String sql = "select DeptFullName from tblDepartment where classCode = ?";
		ArrayList param = new ArrayList();
		param.add(classCode);
		Result rs = sqlList(sql, param);
		if (rs != null && rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			ArrayList rsRs = (ArrayList) rs.retVal;
			name = GlobalsTool.get(rsRs.get(0), 0).toString();
		}
		return name;
	}

	/**
	 * 查询classCode
	 * @param strTableName
	 * @param strParentCode
	 * @return
	 */
	public String getLevelCode(final String strTableName, final String strParentCode) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							CallableStatement cs = conn.prepareCall("{call proc_getNewClassCode(?,?,?,?)}");
							cs.setString(1, strTableName);
							cs.setString(2, strParentCode);

							cs.registerOutParameter(3, Types.INTEGER);
							cs.registerOutParameter(4, Types.VARCHAR, 50);

							cs.execute();
							String newCode = cs.getString(4);
							rs.setRetVal(newCode);
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							return;
						}
					}
				});
				return 0;
			}
		});
		if (retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return rs.retVal.toString();
		} else {
			return "";
		}

	}

	public String downSelect(String sql) {
		Result rs = this.sqlList(sql, new ArrayList());
		String str = "";
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList) rs.retVal).size() > 0) {
			for (int i = 0; i < ((ArrayList) rs.retVal).size(); i++) {
				Object[] os = (Object[]) ((ArrayList) rs.retVal).get(i);
				str += os[0] + "#:#" + os[0] + "#;#";
			}
		}
		return str;
	}

	/**
	 * 上传工作流文件 改变文件名
	 * @param flowName
	 * @param fileFinish
	 * @return
	 */
	public Result updateFlowName(final String flowName, final String fileFinish) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							PreparedStatement pstmt = conn.prepareStatement("update OAWorkFlowTemplate set workFlowFile=id+'.xml',fileFinish=? where id=?");
							pstmt.setString(1, fileFinish);
							pstmt.setString(2, flowName.substring(0, flowName.indexOf(".")));
							int row = pstmt.executeUpdate();
							if (row > 0) {
								rst.setRetVal(true);
							}

							pstmt = conn.prepareStatement("select templateClass,templateFile from OAWorkFlowTemplate where id='" + flowName.substring(0, flowName.indexOf(".")) + "'");
							ResultSet rs = pstmt.executeQuery();
							if (rs.next()) {
								String type = rs.getString(1);
								String tableName = rs.getString(2);
								OAWorkFlowTemplate workFlow = null;
								if (type != null && type.equals("00001")) {
									workFlow = BaseEnv.workFlowInfo.get(tableName);
								} else {
									workFlow = BaseEnv.workFlowInfo.get(flowName.substring(0, flowName.indexOf(".")));
								}
								workFlow.setWorkFlowFile(flowName);
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
	 * 根据Email地址从通讯录中找到对应的联系人
	 * @param userId
	 * @return
	 */
	public Result selUserNameByEmail(final String email) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String strsql = "select empFullName from tblEmployee where email='" + email + "'";
							PreparedStatement ps = conn.prepareStatement(strsql);
							ResultSet rs = ps.executeQuery();
							String phone = "";
							if (rs.next()) {
								phone = rs.getString("empFullName");
							}
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							rst.setRetVal(phone);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 得到当前单据的当前节点
	 * @param values
	 * @return
	 */
	public Result getCurrNodeId(final String id, final String tableName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "select currentNode  from  OAMyWorkFlow where keyId=? and tableName=?";
						PreparedStatement pss = conn.prepareStatement(sql);
						pss.setString(1, id);
						pss.setString(2, tableName);
						ResultSet rst = pss.executeQuery();
						if (rst.next()) {
							rs.setRetVal(rst.getString(1));
						} else {
							rs.setRetVal("0");
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
	 * 得到当前单据此用户的最后办理的节点
	 * @param values
	 * @return
	 */
	public Result getUserLastNode(final String id, final String userId, final String tableName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "select nodeID from OAMyWorkFlowDet where f_ref=(select id from OAMyWorkFlow where keyId=? and tableName=?) and checkPerson=? and nodeId is not null order by sortOrder desc";
						PreparedStatement pss = conn.prepareStatement(sql);
						pss.setString(1, id);
						pss.setString(2, tableName);
						pss.setString(3, userId);
						ResultSet rst = pss.executeQuery();
						if (rst.next()) {
							rs.setRetVal(rst.getString(1));
						} else {
							rs.setRetVal("");
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
	 * 查询 当前单据的流程id
	 * @param values
	 * @return
	 */
	public String getDesignIdByKeyId(final String keyId, final String tableName) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						String sql = "select applyType  from  OAMyWorkFlow where keyId=? and tableName=?";
						PreparedStatement pss = conn.prepareStatement(sql);
						pss.setString(1, keyId);
						pss.setString(2, tableName);
						ResultSet rst = pss.executeQuery();
						if (rst.next()) {
							rs.setRetVal(rst.getString("applyType"));
						}
					}
				});
				return rs.getRetCode();
			}
		});
		if (retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return (String) rs.retVal;
		}
		return "";
	}

	/**
	 * 插入客户最新动态
	 * 
	 * @param keyIds 客户的Id
	 * @return
	 */
	public Result insertCRMCLientInfoLog(String clientId, String billType, String context, String loginId, Connection conn) {
		Result rs = new Result();
		try {

			String sql = "insert into CRMClientInfoLog(id,clientId,billType,context,createBy,createTime,relationId) values(?,?,?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, IDGenerater.getId());
			ps.setString(2, clientId);
			ps.setString(3, billType);
			ps.setString(4, context);
			ps.setString(5, loginId);
			ps.setString(6, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			ps.setString(7, clientId);
			ps.executeUpdate();

			//更新客户上次联系时间	
			String sql1 = "update CRMClientInfo set LastContractTime=? where id=?";
			PreparedStatement ps1 = conn.prepareStatement(sql1);
			ps1.setString(1, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			ps1.setString(2, clientId);
			ps1.executeUpdate();
		} catch (SQLException ex) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
			BaseEnv.log.error("CRMClientMgt---insertCRMCLientInfoLog method :" + ex);
		}
		return rs;
	}

	/**
	 * 插入客户最新动态
	 * 
	 * @param keyIds 客户的Id
	 * @return
	 */
	public Result insertCRMCLientInfoLog(final String clientId, final String billType, final String context, final String loginId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Result reslt = insertCRMCLientInfoLog(clientId, billType, context, loginId, conn);
						rs.setRetCode(reslt.getRetCode());
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}

	/**
	 * 查询呼叫中心设置
	 * @param keyId
	 * @return
	 */
	public Result ccSetQuery() {
		ArrayList param = new ArrayList();
		String sql = "select ckey,cvalue from tblCallCenterSet ";
		return this.sqlList(sql, param);
	}

	/**
	 * 根据用户ID查找相应用户下手机号码和邮件地址
	 * @param userId
	 * @return
	 */
	public Result selTelEmailByUserId(final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String strsql = "select Email,Mobile from dbo.tblEmployee where id= ?";
							PreparedStatement ps = conn.prepareStatement(strsql);
							ps.setString(1, userId);
							ResultSet rs = ps.executeQuery();
							String[] empArray = new String[2];
							if (rs.next()) {
								empArray[0] = rs.getString("Email");
								empArray[1] = rs.getString("Mobile");
							}
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							rst.setRetVal(empArray);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 根据用户ID查找相应用户下手机号码和邮件地址
	 * @param userId
	 * @return
	 */
	public Result selTelByUserId(final String userId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String strsql = "select emp.mobile,emp.Email from tblEmployee emp" + " where emp.id=?";
							PreparedStatement ps = conn.prepareStatement(strsql);
							ps.setString(1, userId);
							ResultSet rs = ps.executeQuery();
							String[] phone = new String[2];
							if (rs.next()) {
								phone[0] = rs.getString("mobile");
								phone[1] = rs.getString("Email");
							}
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							rst.setRetVal(phone);
						} catch (Exception e) {
							e.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 查询用户iPhone的token
	 * @param userName
	 * @return
	 */
	public Result queryTokenByUserIds(final String userIds) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						Result rs = queryTokenByUserIds(userIds, conn);
						result.setRetVal(rs.getRetVal());
						result.setRetCode(rs.retCode);
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}

	public Result queryTokenByUserIds(String userIds, Connection conn) {
		Result result = new Result();
		try {
			String strUserIds = "";
			String[] popedomUser = userIds.split(",");
			for (int i = 0; i < popedomUser.length; i++) {
				strUserIds += "'" + popedomUser[i] + "',";
			}
			if(strUserIds.length()==0){
				return result;
			}
			strUserIds = strUserIds.substring(0, strUserIds.length() - 1);
			String sql = "select userId,token,sendType,serverKey from tblAppleToken where userId in (" + strUserIds + ")";
			PreparedStatement pss = conn.prepareStatement(sql);
			ResultSet rss = pss.executeQuery();
			HashMap<String, ArrayList<String[]>> tokenMap = new HashMap<String, ArrayList<String[]>>();
			while (rss.next()) {
				String userId = rss.getString("userId");
				if (tokenMap.get(userId) == null) {
					ArrayList<String[]> userList = new ArrayList<String[]>();
					userList.add(new String[] { rss.getString("token"), rss.getString("serverKey") });
					tokenMap.put(rss.getString("userId"), userList);
				} else {
					ArrayList<String[]> userList = tokenMap.get(userId);
					userList.add(new String[] { rss.getString("token"), rss.getString("serverKey") });
					tokenMap.put(rss.getString("userId"), userList);
				}
			}
			result.setRetVal(tokenMap);
		} catch (Exception ex) {
			result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			ex.printStackTrace();
			BaseEnv.log.error("MOfficeMgt addIPhoneToken mehtod", ex);
		}
		return result;
	}

	/**
	 * 根据ID查找客户
	 * @param id
	 * @return
	 */
	public Result findClient(String clientId) {
		List list = new ArrayList();
		String sql = "select clientName from CRMClientInfo where id= '" + clientId + "'";
		return this.sqlList(sql, list);
	}

	/*
	 * 通过ID查询HTML模板
	 */
	public List queryHTMLTemplateID(final String id) {
		final List result = new ArrayList();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select * from tblHTMLModuleManager where id= ?";
							PreparedStatement cs = conn.prepareStatement(sql);
							cs.setString(1, id);
							ResultSet rs = cs.executeQuery();
							if (rs.next()) {
								HTMLTemplate tmp = new HTMLTemplate();
								tmp.setContent(rs.getString("content"));
								result.add(tmp);
							}
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
				});
				return 0;
			}
		});
		return result;
	}

	/*
	 * 查询所有HTML模板
	 */
	public List queryHTMLTemplate(final String id) {
		final List result = new ArrayList();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "select * from tblHTMLModuleManager where createBy='" + id + "'";
							ResultSet rs = cs.executeQuery(sql);
							while (rs.next()) {
								HTMLTemplate tmp = new HTMLTemplate();
								tmp.setId(rs.getString("id"));
								tmp.setName(rs.getString("name"));
								result.add(tmp);
							}
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
				});
				return 0;
			}
		});
		return result;
	}

	/**
	 * 根据盘点准备表Id查询准备日期，仓库
	 * @param id
	 * @return
	 */
	public Result queryStockCheckPre(final String id) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement cs = conn.createStatement();
							String sql = "select tblStockCheckPrepare.id as stockPreId,tblStockCheckPrepare.stockCode,";
							sql += "tblStockCheckPrepare.CheckDate as stockCheckDate,tblStock.StockFullName as stockName from tblStockCheckPrepare ";
							sql += " left join tblStock on tblStock.classCode=tblStockCheckPrepare.stockCode where tblStockCheckPrepare.id='" + id + "'";
							ResultSet rs = cs.executeQuery(sql);
							HashMap map = new HashMap();
							if (rs.next()) {
								for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
									Object obj = rs.getObject(i);
									if (obj == null) {
										map.put(rs.getMetaData().getColumnName(i), "");
									} else {
										map.put(rs.getMetaData().getColumnName(i), obj);
									}
								}
							}
							result.setRetVal(map);
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}

	/**
	 * 加载流程数据
	 * @param flowId
	 * @return
	 */
	public WorkFlowDesignBean getWorkFlowDesign(String flowId, Session session) {
		WorkFlowDesignBean designBean = null;
		String hql = "from FlowNodeBean flowNode where flowNode.flowId=?";
		ArrayList<String> param = new ArrayList<String>();
		param.add(flowId);
		Result result = new Result();
		if (session != null) {
			result = list(hql, param, session);
		} else {
			result = list(hql, param);
		}
		HashMap<String, FlowNodeBean> flowNodeMap = new HashMap<String, FlowNodeBean>();

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null) {
			ArrayList<FlowNodeBean> nodeList = (ArrayList<FlowNodeBean>) result.retVal;
			for (FlowNodeBean flowNode : nodeList) {
				// 由于未知原因，流程结点数据会产生很多重复的，这里先对数据进行重复处理
				HashMap map = new HashMap();
				flowNode.getApprovers().clear();
				flowNode.getNotePeople().clear();
				for (ApproveBean approver : flowNode.getApproveSet()) {
					String tempStr = approver.getFlowNode2().getId() + ":" + approver.getUser() + ":" + approver.getType();
					if (map.get(tempStr) == null) {
						map.put(tempStr, tempStr);
						if ("note".equals(approver.getCheckType())) {
							flowNode.getNotePeople().add(approver);
						}else{
							flowNode.getApprovers().add(approver);
						}
					} else {
//						System.out.println("结点" + flowNode.getDisplay() + "审核人" + approver.getUserName() + "重复");
					}
				}
				flowNode.setApproveSet(new HashSet<ApproveBean>()); // 清空set

				map = new HashMap();
				flowNode.getFields().clear();
				flowNode.getHiddenFields().clear();
				for (FieldBean field : flowNode.getFieldSet()) {
					String tempStr = field.getFlowNode().getId() + ":" + field.getFieldName() + ":" + field.getInputType() + ":" + field.isNotNull() + ":" + field.getFieldType();
					if (map.get(tempStr) == null) {
						map.put(tempStr, tempStr);
						if ("public".equals(field.getFieldType())) {
							flowNode.getHiddenFields().add(field.getFieldName());
						} else {
							flowNode.getFields().add(field);
						}
					} else {
//						System.out.println("结点" + flowNode.getDisplay() + "字段" + field.getFieldName() + "重复");
					}
				}
				flowNode.setFieldSet(new HashSet<FieldBean>());// 清空set

				map = new HashMap();
				flowNode.getConditionList().clear();
				for (ConditionsBean appB : flowNode.getConditionSet()) {
					String tempStr = appB.getFlowNode3().getId() + ":" + appB.getTo();
					if (map.get(tempStr) == null) {
						map.put(tempStr, tempStr);
						flowNode.getConditionList().add(appB);

						ArrayList delList = new ArrayList();
						for (ConditionBean cB : appB.getConditions()) {
							String cs = "DET:" + cB.getConditionsBean().getId() + ":" + cB.getFieldName() + ":" + cB.getAndOr() + ":" + cB.getValue() + ":" + cB.getRelation() + ":" + cB.getGroupId()
									+ ":" + cB.getGroupType();
							if (map.get(cs) == null) {
								map.put(cs, cs);
							} else {
								delList.add(cB);
//								System.out.println("结点" + flowNode.getDisplay() + "条件" + appB.getDisplay() + "字段" + cB.getDisplay() + "重复");
							}
						}
						for (Object o : delList) {
							appB.getConditions().remove(o);
						}
					} else {
//						System.out.println("结点" + flowNode.getDisplay() + "条件" + appB.getDisplay() + "重复");
					}
				}
				flowNode.setConditionSet(new HashSet<ConditionsBean>());// 清空set
				flowNodeMap.put(flowNode.getKeyId(), flowNode);
			}
			designBean = new WorkFlowDesignBean();
			designBean.setFlowNodeMap(flowNodeMap);
		}
		return designBean;
	}

	/**
	 * 获取客户列表权限
	 * @param loginBean
	 * @return
	 */
	public String getClientScope(LoginBean loginBean) {
		StringBuffer condition = new StringBuffer();
		String fieldValueSQL = "";
		if (!"1".equals(loginBean.getId())) {
			/*查看某字段值单据*/
			condition.append(" and (CRMClientInfo.createBy ='").append(loginBean.getId()).append("' or CRMClientInfo.id in(select f_ref from CRMClientInfoEmp " + "where employeeId ='").append(
					loginBean.getId()).append("' /*or (len(isnull(departmentCode,''))>0 and '").append(loginBean.getDepartCode()).append("' like departmentCode+'%')*/) ");
			MOperation mop = (MOperation) loginBean.getOperationMap().get("/UserFunctionQueryAction.do?tableName=CRMClientInfo");
			if (mop != null) {
				ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
				if (scopeRight != null && scopeRight.size() > 0) {
					for (Object o : scopeRight) {
						String strUserIds = "";
						String strDeptIds = "";
						LoginScopeBean lsb = (LoginScopeBean) o;
						if (lsb != null && "1".equals(lsb.getFlag())) {
							for (String strId : lsb.getScopeValue().split(";")) {
								strUserIds += "'" + strId + "',";
							}
							strUserIds = strUserIds.substring(0, strUserIds.length() - 1);
							condition.append(" or CRMClientInfo.createBy in (").append(strUserIds).append(")");
						}
						if (lsb != null && "5".equals(lsb.getFlag())) {
							for (String strId : lsb.getScopeValue().split(";")) {
								condition.append(" or departmentCode like '").append(strId).append("%' ");
							}
						}
						if (lsb != null && "6".equals(lsb.getFlag()) && "CRMClientInfo".equals(lsb.getTableName())) {
							if (lsb.getScopeValue() != null && lsb.getScopeValue().length() > 0) {
								if (lsb.getScopeValue().contains(";")) {
									String[] scopes = lsb.getScopeValue().split(";");
									String scopeSQL = "(";
									for (String str : scopes) {
										scopeSQL += "'" + str + "',";
									}
									scopeSQL = scopeSQL.substring(0, scopeSQL.length() - 1);
									scopeSQL += ")";
									fieldValueSQL = lsb.getTableName() + "." + lsb.getFieldName() + " in " + scopeSQL;
								}
							}
						}
					}
				}
			}
			condition.append(")");
		}
		if (fieldValueSQL.length() > 0) {
			condition.append(" and (").append(fieldValueSQL).append(")");
		}

		return condition.toString();

	}

	/**
	 * 获取当前单据相关的流程信息
	 * @param keyId
	 * @param tableName
	 * @return
	 */
	public HashMap getOAMyWorkFlowInfo(String keyId, String tableName) {
		String sql = "select a.createBy,applyType,a.checkPerson,nextNodeIds,currentNode,b.oaTimeLimitUnit,b.benchmarkTime,a.applyContent,a.departmentCode,a.tableName,a.keyId,a.id from OAMyWorkFlow a "
				+ " left join OAMyWorkFlowDet b on a.id=b.f_ref where a.keyId='" + keyId + "'";
		if (tableName != null && tableName.length() > 0) {
			sql += " and  a.tableName='" + tableName + "'";
		}
		AIODBManager aioMgt = new AIODBManager();
		Result rs = aioMgt.sqlListMap(sql, new ArrayList(), 0, 0);
		if (rs.retVal != null && ((ArrayList) rs.retVal).size() > 0) {
			return (HashMap) ((ArrayList) rs.retVal).get(0);
		}
		return null;
	}

	/**
	 * logo设置引用，在logo页面
	 * @param companyName
	 * @param companyLink
	 * @param loginId
	 * @return
	 */
	public Result changeAdd(final String companyName, final String companyLink, final String loginId) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "delete from tblCompanySet";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.executeUpdate();

							String hql = "insert into tblCompanySet(id,companyLink,companyName,createBy)values(?,?,?,?)";
							PreparedStatement ps = conn.prepareStatement(hql);
							ps.setString(1, IDGenerater.getId());
							ps.setString(2, companyLink);
							ps.setString(3, companyName);
							ps.setString(4, loginId);
							ps.executeUpdate();
							result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception ex) {
							ex.printStackTrace();
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("ToDoMgt changeAdd : ", ex);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result;
	}

	public Result getCompany() {
		String sql = "select companyLink,companyName from tblCompanySet where '1'=?";
		ArrayList param = new ArrayList();
		param.add("1");
		return sqlList(sql, param);
	}

	public String getWorkId(String userId) {
		String workId = "";
		String sql = "select id from oaworklog where type=? and workLogDate = '" + BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd) + "' and createBy='" + userId + "'";
		ArrayList param = new ArrayList();
		param.add("day");
		Result rs = sqlList(sql, param);
		if (rs != null && rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			ArrayList rsRs = (ArrayList) rs.retVal;
			if (rsRs.size() > 0) {
				workId = GlobalsTool.get(rsRs.get(0), 0).toString();
			}
		}
		return workId;
	}
}
