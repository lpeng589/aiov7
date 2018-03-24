package com.koron.oa.executive.job;

import java.sql.Connection;
import java.sql.Date;
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
import com.koron.oa.bean.OAJobAuditingBean;
import com.koron.oa.bean.OAJobBean;
import com.koron.oa.bean.OAjobRestoreBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: 工作协助单类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * 
 * @author 唐雪梅
 * @version 1.0
 */
public class OAJobMgt extends AIODBManager {

	/**
	 * 填加一条单位记录
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */

	public Result addJob(final OAJobBean oaJob) {
		return addBean(oaJob);
	}

	public Result addjobAuditing(final OAJobAuditingBean oajobAuditing) {
		return addBean(oajobAuditing);
	}

	public Result addjobRestore(final OAjobRestoreBean oajobRestoreBean) {
		return addBean(oajobRestoreBean);
	}

	/**
	 * 显示所有
	 * 
	 */
	public Result queryall(final String str_userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							List ls = new ArrayList();
							Statement st = conn.createStatement();
							String sql = "select * from OAJobodd where 1=1";
							ResultSet rss = st.executeQuery(sql);
							while (rss.next()) {
								String value[] = new String[12];
								value[0] = rss.getString("id"); // id
								value[1] = rss.getString("JobType"); // 协助类型
								value[2] = rss.getString("Jobtheme"); // 协助主题
								value[3] = rss.getString("JobBeginTime"); // 协助开始时间
								value[4] = rss.getString("JobEndTime"); // 协助结束时间
								value[5] = rss.getString("JobAttaches"); // 协助附件
								value[6] = rss.getString("ElaborateOn"); // 协助详细说明
								value[7] = rss.getString("IntterfixServer"); // 协助相关客户
								value[8] = rss.getString("state"); // 审核状态
								value[9] = rss.getString("review"); // 审核说明
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

	// 修改工作协助单+tangjing
	public Result updateOAJob(final OAJobBean oAJobBean) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							Connection conn = connection;
							PreparedStatement pstmt = conn
									.prepareStatement("UPDATE OAJobodd SET CreatePerson=?,Jobtheme=?,CreateTime=?,JobBeginTime=?,JobEndTime=?,JobType=?,Assessor=?,IntterfixServer=?,ElaborateOn=?,Attaches=?,participant=?,state=?,isSaveReading=? WHERE id=?");
							pstmt.setString(1, oAJobBean.getCreatePerson());
							pstmt.setString(2, oAJobBean.getJobtheme());
							pstmt.setString(3, oAJobBean.getCreateTime());
							pstmt.setString(4, oAJobBean.getJobBeginTime());
							pstmt.setString(5, oAJobBean.getJobEndTime());
							pstmt.setString(6, oAJobBean.getJobType());
							pstmt.setString(7, oAJobBean.getAssessor());
							pstmt.setString(8, oAJobBean.getIntterfixServer());
							pstmt.setString(9, oAJobBean.getElaborateOn());
							pstmt.setString(10, oAJobBean.getAttaches());
							pstmt.setString(11, oAJobBean.getParticipant());
							pstmt.setString(12, oAJobBean.getState());
							pstmt.setString(13, oAJobBean.getIsSaveReading());
							pstmt.setString(14, oAJobBean.getId());
							
							pstmt.executeUpdate();
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

	// 根据ID得到一个工作协助单+tangjing
	public Result getJob(final String id) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							PreparedStatement pstmt = conn
									.prepareStatement("select * from OAJobodd where 1=1 and id=?");
							pstmt.setString(1, id);
							ResultSet rs = pstmt.executeQuery();
							OAJobBean obj = new OAJobBean();
							if (rs.next()) {
								obj.setId(rs.getString("id"));// ID
								obj.setAssessor(rs.getString("Assessor"));// 审核人
								obj.setAttaches(rs.getString("Attaches"));// 附件
								obj.setCreatePerson(rs
										.getString("CreatePerson"));// 创建人
								obj.setCreateTime(rs.getString("CreateTime"));// 创建时间
								obj.setElaborateOn(rs.getString("ElaborateOn"));// 协助单详细说明
								obj.setIntterfixServer(rs
										.getString("IntterfixServer"));// 协助相关客户
								obj.setJobBeginTime(rs
										.getString("JobBeginTime"));// 协助开始时间
								obj.setJobEndTime(rs.getString("JobEndTime"));// 协助结束时间
								obj.setJobtheme(rs.getString("Jobtheme")); // 协助主题
								obj.setJobType(rs.getString("JobType"));// 协助类型
								obj.setParticipant(rs.getString("participant"));// 参与者
								obj.setState(rs.getString("state")); // 审核状态
							}
							rst.setRetVal(obj);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :" + ex);
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
	 * 查询
	 * 
	 * @param name
	 *            String
	 * @param pageNo
	 *            int
	 * @param pageSize
	 *            int
	 * @return Result
	 */
	public Result query(String loginName, String CreatePerson, String Jobtheme,
			String JobBeginTime, String JobEndTime) {
		// 创建参数
		List param = new ArrayList();
		String hql = "select oa from OAJobBean as oa where (CreatePerson='"
				+ loginName + "' or Assessor like '%" + loginName
				+ "%' or participant like '%" + loginName + "%') ";
		// 如标题不为空，则做标题模糊查询
		if (CreatePerson != null && CreatePerson.length() != 0) {
			hql += " and oa.createPerson='" + CreatePerson + "'";
		}
		if (Jobtheme != null && Jobtheme.length() != 0) {
			hql += " and upper(oa.jobtheme) like '%'||?||'%' ";
			param.add(Jobtheme.trim().toUpperCase());// trim()是去掉空格
		}
		if (!"".equals(JobBeginTime) && null != JobBeginTime) {
			hql += " and CreateTime >'" + JobBeginTime + " 00:00:00' ";
		}
		if (!"".equals(JobEndTime) && null != JobEndTime) {
			hql += " and CreateTime < '" + JobEndTime + " 24:00:00' ";
		}

		// 调用list返回结果
		return list(hql, param);
	}
	public Result query2(final String loginName, final String createPersonName,final String Jobtheme,
			final String JobBeginTime,final String JobEndTime,final String auditStatus,final int pageNo,final int pageSize) {
		StringBuffer sql=new StringBuffer("select oa.id,Assessor,Attaches,CreatePerson,oa.CreateTime,ElaborateOn,IntterfixServer,");
		sql.append("JobBeginTime,JobEndTime,Jobtheme,JobType,participant,state,oa.lastUpdateTime,ROW_NUMBER() over(order by oa.createTime desc,oa.lastUpdateTime desc) as row_id from OAJobodd as oa left join tblEmployee e on e.id=oa.createPerson where 1=1 and (CreatePerson='"+ loginName + "' or Assessor like '%" + loginName+ "%' or participant like '%" + loginName + "%') ");
		if (createPersonName != null && createPersonName.length() != 0) {
			sql.append("and e.EmpFullName like '%"+createPersonName+"%'");
		}
		if (Jobtheme != null && Jobtheme.length() != 0) {
			sql.append(" and oa.jobtheme like '%"+Jobtheme+"%' ");
		}
		if (!"".equals(JobBeginTime) && null != JobBeginTime) {
			sql.append(" and oa.CreateTime >'" + JobBeginTime + " 00:00:00' ");
		}
		if (!"".equals(JobEndTime) && null != JobEndTime) {
			sql.append(" and oa.CreateTime < '" + JobEndTime + " 24:00:00' ");
		}
		if (auditStatus != null && auditStatus.length() != 0&&!auditStatus.equals("all")) {
			sql.append(" and oa.state='"+auditStatus+"' ");
		}
		
		AIODBManager aioMgt=new AIODBManager();
		Result rss=aioMgt.sqlListMap(sql.toString(), null, pageNo, pageSize);
		
		List listMap=(List)rss.getRetVal();
		List<OAJobBean> list=new ArrayList<OAJobBean>();
		for(int i=0;i<listMap.size();i++){
			HashMap map=(HashMap)listMap.get(i);
			OAJobBean obj = new OAJobBean();			
			obj.setId(map.get("id").toString());// ID
			obj.setAssessor(map.get("Assessor").toString());// 审核人
			obj.setAttaches(map.get("Attaches").toString());// 附件
			obj.setCreatePerson(map.get("CreatePerson").toString());// 创建人
			obj.setCreateTime(map.get("CreateTime").toString());// 创建时间
			obj.setElaborateOn(map.get("ElaborateOn").toString());// 协助单详细说明
			obj.setIntterfixServer(map.get("IntterfixServer").toString());// 协助相关客户
			obj.setJobBeginTime(map.get("JobBeginTime").toString());// 协助开始时间
			obj.setJobEndTime(map.get("JobEndTime").toString());// 协助结束时间
			obj.setJobtheme(map.get("Jobtheme").toString()); // 协助主题
			obj.setJobType(map.get("JobType").toString());// 协助类型
			obj.setParticipant(map.get("participant").toString());// 参与者
			obj.setState(map.get("state").toString()); // 审核状态
			obj.setLastUpdateTime(map.get("lastUpdateTime").toString());
			list.add(obj);
		}
		rss.setRetVal(list);
		return rss;

	}
	/**
	 * 删除多条单位记录
	 * 
	 * @param ids
	 *            long[]
	 * @return Result
	 */
	public Result delete(String id) {
		return deleteBean(id, OAJobBean.class, "id");
	}

	/**
	 * 修改一条单位记录
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result update(String id, String Jobtheme, String JobType,
			String participant, String BeginTime, String EndTime,
			String IntterfixServer, String ElaborateOn, String Attaches) {
		// 先查出相应的单位记录
		Result rs = loadBean(new Long(id), OAJobBean.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			OAJobBean bean = (OAJobBean) rs.retVal;
			bean.setJobtheme(Jobtheme);
			bean.setJobType(JobType);
			bean.setParticipant(participant);
			bean.setJobBeginTime(BeginTime);
			bean.setJobEndTime(EndTime);
			bean.setIntterfixServer(IntterfixServer);
			bean.setElaborateOn(ElaborateOn);
			bean.setAttaches(Attaches);
			// 修改记录
			rs = updateBean(bean);
		}
		return rs;
	}

	/**
	 * 重载修改一条单位记录
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result update(OAJobBean o) {
		Result rs = null;
		// 修改记录
		rs = updateBean(o);
		return rs;
	}

	/**
	 * 根据条件查询协助表(OAJobodd)
	 * 
	 * @return Result
	 */
	private Result Joboodd_query(final String CreatePerson,
			final String Jobtheme, final String JobBeginTime,
			final String JobEndTime, final String JobType, final String userId) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							List ls = new ArrayList();
							Statement st = conn.createStatement();
							String sql = "select * from OAJobodd where 1=1";
							// 判断条件是否为空，为空则不加
							if (!"".equals(CreatePerson)
									&& null != CreatePerson) {
								sql += " and CreatePerson ='" + CreatePerson
										+ "'";
							}
							if (!"".equals(Jobtheme) && null != Jobtheme) {// 公文密级
								sql += " and Jobtheme ='" + Jobtheme + "'";
							}
							if (!"".equals(JobType) && null != JobType) {
								sql += " and JobType like '%" + JobType
										+ "%' or CreatePerson like '%"
										+ JobType + "%' ";
							}
							if (!"".equals(JobEndTime) && null != JobEndTime) {
								sql += " and JobEndTime < '" + JobEndTime
										+ " 24:00:00' ";
							}
							if (!"".equals(JobBeginTime)
									&& null != JobBeginTime) {
								sql += " and JobBeginTime >'" + JobBeginTime
										+ " 00:00:00' ";
							}
							if (!"".equals(userId) && null != userId) {
								sql += " and (CreatePerson = '" + userId
										+ "' or participant is null) ";
							}
							ResultSet rss = st.executeQuery(sql);
							while (rss.next()) {
								String value[] = new String[14];
								value[0] = rss.getString("id"); // id
								value[1] = rss.getString("JobType"); // 协助类型
								value[2] = rss.getString("Jobtheme"); // 协助主题
								value[3] = rss.getString("JobBeginTime"); // 协助开始时间
								value[4] = rss.getString("JobEndTime"); // 协助结束时间
								value[5] = rss.getString("JobAttaches"); // 协助附件
								value[6] = rss.getString("ElaborateOn"); // 协助详细说明
								value[7] = rss.getString("IntterfixServer"); // 协助相关客户
								value[8] = rss.getString("state"); // 审核状态
								value[9] = rss.getString("review"); // 审核说明
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
	 * 查一条的详细信息
	 * 
	 * @param notepadId
	 *            long 代号
	 * @return Result 返回结果
	 */
	public Result detail(Long id) {
		return loadBean(new Long(id), OAJobBean.class);
	}

	/**
	 * 
	 * 重载查一条详细信息
	 * 
	 * @param id
	 *            主键
	 * @return 返回一个Bean
	 */
	public Result detail(String id, java.lang.Class classType) {
		return loadBean(id, classType);
	}

	/**
	 * 查询参与者的回复
	 * 
	 * @param id
	 * @return
	 */
	public Result getRestore(String id) {
		final String oaJobid = id;
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement st = conn.createStatement();
							String sql = "select * from OAJoboddRestore where oajoboddId='"
									+ oaJobid + "\'";
							ResultSet rss = st.executeQuery(sql);
							List<OAjobRestoreBean> li = new ArrayList<OAjobRestoreBean>();
							// OAjobRestoreBean对象
							OAjobRestoreBean oabean = null;
							while (rss.next()) {
								oabean = new OAjobRestoreBean();
								oabean.setId(rss.getString("id"));
								oabean.setOajoboddId(rss
										.getString("oajoboddId"));
								oabean.setParticipantPerson(rss
										.getString("participantPerson"));
								oabean.setRestoreTime(rss
										.getString("restoreTime"));
								oabean.setParticipantRestore(rss
										.getString("participantRestore"));
								oabean.setAttaches(rss.getString("attaches"));
								li.add(oabean);
							}
							rs.setRetVal(li);
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
	 * 查询是否已回复
	 * 
	 * @param id
	 * @return
	 */
	public Result isRestore(String id, String participantPerson) {
		final String oaJobid = id;
		final String participantName = participantPerson;
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {

							Statement st = conn.createStatement();
							String sql = "select * from OAJoboddRestore where oajoboddId='"
									+ oaJobid
									+ "' and participantPerson='"
									+ participantName + ";'";
							ResultSet rss = st.executeQuery(sql);
							// OAJobAuditingBean对象
							OAjobRestoreBean oabean = new OAjobRestoreBean();
							if (rss.next()) {
								oabean.setId(rss.getString("id"));
								oabean.setOajoboddId(rss
										.getString("oajoboddId"));
								oabean.setParticipantPerson(rss
										.getString("participantPerson"));
								oabean.setParticipantRestore(rss
										.getString("participantRestore"));
							}
							rs.setRetVal(oabean);
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
	 * 查询是否已审核
	 * 
	 * @param id
	 * @return
	 */
	public Result isAuditing(final String id,final String assessor) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {

							Statement st = conn.createStatement();
							String sql = "select * from OAJoboddAuditing where oajoboddId='"
									+ id
									+ "' and assessor='"
									+ assessor + "' and state='pass'";
							ResultSet rss = st.executeQuery(sql);
							OAJobAuditingBean oabean = new OAJobAuditingBean();
							if (rss.next()) {
								oabean.setId(rss.getString("id"));
								oabean.setOajoboddId(rss
										.getString("oajoboddId"));
								oabean.setAssessor(rss.getString("assessor"));
								oabean.setAuditing(rss.getString("auditing"));
								oabean.setState(rss.getString("state"));
							}
							rs.setRetVal(oabean);
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
	 * 查询审核通过数
	 * 
	 * @param id
	 * @return
	 */
	public Result getPassCount(final String id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {

							Statement st = conn.createStatement();
							String sql = "select count(state) as passCount from OAJoboddAuditing group by oajoboddId,state having oajoboddId='"
									+ id + "' and state='pass'";
							ResultSet rss = st.executeQuery(sql);
							if (rss.next()) {
								rs.setRetVal(rss.getString("passCount")); 
							}						
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
	 * 查询审核通过数
	 * 
	 * @param id
	 * @return
	 */
	public Result getAuditeCount(final String id) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {

							Statement st = conn.createStatement();
							String sql = "select count(distinct assessor) from OAJoboddAuditing where oajoboddid='"+id+"' ";
							ResultSet rss = st.executeQuery(sql);
							if (rss.next()) {
								rs.setRetVal(rss.getInt(1)); 
							}						
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

	public String findClientNameById(String keyId){
		keyId=";"+keyId;
		List<String> param = new ArrayList<String>();
		String sql = "SELECT moduleId,clientName FROM CRMClientInfo WHERE ?  like '%;'+id+';%'";
		param.add(keyId);
		Result rs = this.sqlList(sql, param);
		String clientName="";
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && rs.getRetVal() != null) {
			List obj=(List) rs.retVal;
			if(obj!=null && obj.size()>0){
				String temp="";
				for(int i=0;i<obj.size();i++){
					Object[] objList=(Object[]) obj.get(i);
					temp+= objList[1].toString()+";";
				}
				clientName=temp;
			}
		}
		return clientName;
	}
	
	/**
	 * 查询审核者的审核说明
	 * 
	 * @param id
	 * @return
	 */
	public Result getAuditing(String id) {
		final String oaJobid = id;
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							Statement st = conn.createStatement();
							String sql = "select * from OAJoboddAuditing where oajoboddId='"
									+ oaJobid + "\'";
							ResultSet rss = st.executeQuery(sql);
							List<OAJobAuditingBean> li = new ArrayList<OAJobAuditingBean>();
							// OAjobRestoreBean对象
							OAJobAuditingBean oabean = null;
							while (rss.next()) {
								oabean = new OAJobAuditingBean();
								oabean.setId(rss.getString("id"));
								oabean.setOajoboddId(rss
										.getString("oajoboddId"));
								oabean.setAssessor(rss.getString("assessor"));
								oabean.setAuditing(rss.getString("auditing"));
								oabean.setAuditingTime(rss
										.getString("auditingTime"));
								oabean.setState(rss.getString("state"));
								oabean.setAttaches(rss.getString("attaches"));
								li.add(oabean);
							}
							rs.setRetVal(li);
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
	 * 根据ID查找关联单位，在CRM客户中查找
	 * 
	 * @param id
	 * @return
	 */
	public Result getIntterfixServerNameById(String id) {
		final String intterfixServerid = id;
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						Connection conn = connection;
						try {
							String sql = "select CompanyName from ViewCRMCompanyAll where id=?";
							PreparedStatement pst = conn.prepareStatement(sql);
							pst.setString(1, intterfixServerid);
							ResultSet rss = pst.executeQuery();
							String result = "";
							if (rss.next()) {
								result = rss.getString("CompanyName");
							}
							rs.setRetVal(result);
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

}
