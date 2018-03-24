package com.koron.crm.carefor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.bean.CareforActionBean;
import com.koron.crm.bean.CareforActionDelBean;
import com.koron.crm.bean.CareforBean;
import com.koron.crm.bean.CareforDelBean;
import com.koron.crm.memoryDay.ChineseCalendar;
import com.koron.oa.bean.MailinfoSettingBean;
import com.koron.oa.bean.OADayWorkPlanBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.email.EMailMgt;
import com.menyi.email.util.AIOEMail;
import com.menyi.email.util.EMailMessage;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.NotifyFashion;
/**
 * 客户关怀业务逻辑类
 * 
 * @author 徐磊
 * 
 */
public class CareforMgt extends AIODBManager {

	/**
	 * 查询客户关怀方案
	 * 
	 * @param paramLists
	 * @return
	 */
	public Result queryCarefor(String name, String status) {
		ArrayList paramLists = new ArrayList();
		String sql = "select bean from CareforBean bean where 1=1";
		if (name != null&& !name.equals("")) {
			paramLists.add(name);
			sql += " and bean.name like '%'+?+'%'";
		}
		if (status != null && !status.equals("")) {
			paramLists.add(status);
			sql += " and bean.status = ?";
		}
		Result rs = list(sql, paramLists);

		return rs;
	}
	
	/**
	 * 查询客户关怀方案
	 * 
	 * @param paramLists
	 * @return
	 */
	public Result querySelectedCarefor() {
		
		String sql = " select id,name from tblCarefor where status = 0 and id in (select ref_id from tblCareforDel ) ";
		
		Result rs = sqlList(sql, new ArrayList());

		return rs;
	}
	
	/**
	 * 查询客户关怀方案
	 * 
	 * @param paramLists
	 * @return
	 */
	public Result queryOneCarefor(String name) {
		ArrayList paramLists = new ArrayList();
		String sql = "select bean from CareforBean bean where 1=1 and bean.name = ?";
		paramLists.add(name);
		Result rs = list(sql, paramLists);

		return rs;
	}

	/**
	 * 通过ID查询客户关怀方案
	 * 
	 * @param paramLists
	 * @return
	 */
	public Result loadCarefor(String id) {
		return loadBean(id, CareforBean.class);
	}

	/**
	 * 得到所有的 客户关怀方案
	 * 
	 * @return
	 */
	public Result queryAllCarefor() {
		String sql = "select bean from CareforBean bean ";
		Result rs = this.list(sql, new ArrayList());

		return rs;
	}

	/**
	 * 根据 客户关怀方案id 得到客户关怀方案明细
	 * 
	 * @param paramLists
	 * @return
	 */
	public Result queryCareforDelForCareforId(String ref_id, String actionName) {
		ArrayList paramLists = new ArrayList();
		String sql = "select bean from CareforDelBean bean where 1=1";
		if (ref_id == null) {
			Result rs = new Result();
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			return rs;
		} else {
			paramLists.add(ref_id);
			sql += " and bean.ref_id = ?";
		}
		if (actionName != null && actionName.length() > 0) {
			sql += " and bean.actionName = ?";
			paramLists.add(actionName);
		}

		return list(sql, paramLists);

	}

	/**
	 * 得到客户关怀方案明细
	 * 
	 * @param paramLists
	 * @return
	 */
	public Result loadCareforDel(String id) {
		return this.loadBean(id, CareforDelBean.class);
	}

	/**
	 * 填加 一条 客户关怀方案
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result addCarefor(CareforBean bean) {
		Result rs = new Result();
		String id = IDGenerater.getId(); // 自动生成一个ID
		bean.setId(id);

		Result r = queryOneCarefor(bean.getName());
		ArrayList<CareforBean> cs = (ArrayList<CareforBean>) r.getRetVal();

		if (cs.size() != 0) {
			rs.setRetCode(ErrorCanst.MULTI_VALUE_ERROR);// 名称不能重复
			return rs;
		}

		// 调用基类方法addBean执行插入操作
		rs = addBean(bean);
		rs.setRetVal(id);
		return rs;
	}

	/**
	 * 修改 一条 客户关怀方案
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result updateCarefor(CareforBean bean) {

		Result rs = new Result();
		Result r = queryOneCarefor(bean.getName());
		ArrayList<CareforBean> cs = (ArrayList<CareforBean>) r.getRetVal();
		if (cs.size() != 0 && !cs.get(0).getId().equals(bean.getId())) {
			rs.setRetCode(ErrorCanst.MULTI_VALUE_ERROR);// 名称不能重复
			return rs;
		}

		return updateBean(bean);
	}

	/**
	 * 填加 一条 客户关怀方案明细
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result addCareforDel(CareforDelBean bean) {
		Result rs = new Result();
		String id = IDGenerater.getId(); // 自动生成一个ID
		bean.setId(id);
		ArrayList param = new ArrayList();
		param.add(bean.getRef_id());
		Result r = queryCareforDelForCareforId(bean.getRef_id(), bean.getActionName());
		ArrayList<CareforDelBean> cs = (ArrayList<CareforDelBean>) r.getRetVal();
		if (cs.size() != 0) { // 同一个路径里不能有同名的事件
			rs.setRetCode(ErrorCanst.MULTI_VALUE_ERROR);// 名称不能重复
			return rs;
		}

		// 调用基类方法addBean执行插入操作
		return addBean(bean);
	}

	/**
	 * 修改 一条 客户关怀方案明细
	 * 
	 * @param id
	 *            long
	 * @param name
	 *            String
	 * @return Result
	 */
	public Result updateCareforDel(CareforDelBean bean) {
		Result rs = new Result();
		ArrayList param = new ArrayList();
		param.add(bean.getRef_id());
		Result r = queryCareforDelForCareforId(bean.getRef_id(), bean.getActionName());
		ArrayList<CareforDelBean> cs = (ArrayList<CareforDelBean>) r.getRetVal();

		if (cs.size() != 0 && !cs.get(0).getId().equals(bean.getId())) { // 同一个路径里不能有同名的事件
			rs.setRetCode(ErrorCanst.MULTI_VALUE_ERROR);// 名称不能重复
			return rs;
		}

		return updateBean(bean);
	}

	/**
	 * 启用停用
	 * 
	 * @param status
	 * @param keyIds
	 * @return
	 */
	public Result stopOrEnable(final String status, final String[] keyIds) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						
						try {
							StringBuffer ids = new StringBuffer("");
							int i = 0;
							for (String s : keyIds) {
								if (i == 0) {
									ids.append("'" + s + "'");
									i++;
								} else {
									ids.append(",'" + s + "'");
								}
							}
							String strsql = "update tblCarefor set status = ? where id in ("
									+ ids.toString() + ")";
							PreparedStatement ps = connection.prepareStatement(strsql);
							ps.setString(1, status);
							ps.executeUpdate();
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("CareforMgt StopOrEnable:",e) ;
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
	 * 删除一条明细
	 * 
	 * @param id
	 * @return
	 */
	public Result deleteCareforDel(final String id) {
		return this.deleteBean(id, CareforDelBean.class, "id");
	}
	
	/**
	 * 判断事件是否被引用
	 * 
	 * @param id
	 * @return
	 */
	public Result isQuoteCareforDel(final String id) {
		ArrayList paramLists = new ArrayList();
		String hql = "select bean from CareforActionDelBean bean where 1=1";
		if (id != null) {
			hql += " and bean.eventId = ?";
			paramLists.add(id);
		}
		return list(hql, paramLists);
	}
	
	/**
	 * 客户关怀路径是否被引用
	 * 
	 * @param id
	 * @return
	 */
	public Result isQuoteCarefor(final String[] keyIds) {
		ArrayList paramLists = new ArrayList();
		String hql = "select bean from CareforActionBean bean where 1=1";
		if (keyIds != null && keyIds.length>0) {
			hql += " and (" ; 
			for(String userId : keyIds){
				hql += " bean.careforId = '"+userId+"' or";
			}
			hql = hql.substring(0,hql.length()-2) ;
			hql += ")" ;
		}
		return list(hql, paramLists);
	}

	/**
	 * 删除路径
	 * 
	 * @param status
	 * @param keyIds
	 *            需要删除的路径的id集合
	 * @return
	 */
	public Result deleteCarefor(final String[] keyIds) {
		if(keyIds == null && keyIds.length ==0){
			return new Result();
		}
		
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
					
						try {
							StringBuffer ids = new StringBuffer("");
							int i = 0;
							for (String s : keyIds) {
								if (i == 0) {
									ids.append("'" + s + "'");
									i++;
								} else {
									ids.append(",'" + s + "'");
								}
							}
							
							String sql ="select count(*) from tblCareforAction where careforId in ("
									+ ids.toString() + ")";							
							Statement st = connection.createStatement();
							ResultSet r1 = st.executeQuery(sql);
							if(r1.next()){
								int count = r1.getInt(1);
								if(count >0){
									rst.setRetCode(ErrorCanst.DATA_ALREADY_USED);
									return;
								}
								sql = "delete from tblCareforDel where ref_id in("
										+ ids.toString() + ")";
								st.addBatch(sql);
								String strsql = "delete from tblCarefor where id in ("
										+ ids.toString() + ")";
								st.addBatch(strsql);
								st.executeBatch();
								rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							}		
						} catch (Exception e) {
							BaseEnv.log.error("CareforMgt deleteCarefor:",e) ;
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
	 * 查询客户关怀执行状态
	 * 
	 * @param id
	 * @param sequence
	 * @param aspect
	 * @return
	 */
	public Result queryCareforAction(
			final String status, final String clientId) {
		String hql = " select bean from CareforActionBean bean where 1=1 ";
		ArrayList<String> param = new ArrayList<String>();
		if(clientId != null && clientId.length()>0){
			param.add(clientId);
			hql += " and  bean.clientId = ?";
		}
		
		if(status != null && status.length()>0){
			param.add(status);
			hql += " and bean.status = ?";
		}
		
		return this.list(hql, param);
	}
	
	/**
	 * 查询客户关怀执行状态
	 * 
	 * @param id
	 * @param sequence
	 * @param aspect
	 * @return
	 */
	public Result queryCareforActionByClientName(
			final String status, final String clientName,int pageSize,int pageNo) {
		String clientId=null;
		if(clientName != null && clientName.length() > 0){
			clientId = "";
			String sql = " select id from  CRMClientInfo where clientName like '%"+clientName+"%'";
			Result rs = this.sqlList(sql, new ArrayList());
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<Object[]> list = (ArrayList<Object[]>)rs.retVal;
				for(Object[] os:list){
					clientId +=",'"+os[0]+"'";
				}
			}
			if(clientId.length() > 0){
				clientId = clientId.substring(1);
			}
		}
		
		
		String hql = " select bean from CareforActionBean bean where 1=1 ";
		ArrayList<String> param = new ArrayList<String>();
		
		if(clientId != null&& clientId.length() > 0){
			hql += " and  bean.clientId in ("+clientId+")";
		}else if(clientId != null&& clientId.length() == 0){
			return new Result();
		}
		
		if(status != null && status.length()>0){
			param.add(status);
			hql += " and bean.status = ?";
		}
		
		return this.list(hql, param,pageNo,pageSize,true);
	}
	
	
	/**
	 * 查询一个客户执行的明细
	 * @param ClientNo
	 * @param status
	 * @param ClientId
	 * @return
	 */
	public Result queryOneCareforAction(final String id) {
		
		return this.loadBean(id,CareforActionBean.class);
	}

	/**
	 * 根据执行路径ID得到明细
	 * 
	 * @param id
	 * @return
	 */
	public Result queryCareforDelAction(final String id) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {

							String sql = "select del.status as delStatus,del.id as delId,caredet.actionName as detName from tblCareforActionDel as del "
									+ "left join tblCareforDel as caredet on del.eventId=caredet.id where 1=1 and del.ref_id = ?";

							PreparedStatement ps = connection
									.prepareStatement(sql);

							ps.setString(1, id);

							ResultSet rs = ps.executeQuery();
							ArrayList list = new ArrayList();
							while (rs.next()) {
								String[] s = new String[3];
								s[0] = rs.getString(3);// 事件名称
								s[1] = rs.getString(1);// 状态
								s[2] = rs.getString(2);// id
								list.add(s);
							}
							rst.setRetVal(list);
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("CareforMgt queryCareforDelAction:",e) ;
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
	 * 得到 客户关怀执行明细
	 * 
	 * @param id
	 * @return
	 */
	public Result loadCareforActionDel(String id) {
		return this.loadBean(id, CareforActionDelBean.class);
	}
      
	private long oneDay = 24 * 60 * 60 * 1000l;// 一天等于多少微秒

	/**
	 * 创建一个客户关怀执行，和它的明细，在生产工作计划
	 * 
	 * @param bean
	 * @return
	 * @throws ParseException
	 * @throws IllegalArgumentException
	 */
	public Result addCareforAction(CareforActionBean bean) {
		Result rst = new Result();
		try {
			String id = IDGenerater.getId(); // 自动生成一个ID
			bean.setId(id);
			// 1未执行
			bean.setStatus("1");
			
			String baselineDate = bean.getBaselineDate();// 基准日期
			String careforId = bean.getCareforId();// 路径ID
			
			ArrayList<CareforActionDelBean> delBeans = new ArrayList<CareforActionDelBean>();
			
			Result r = queryCareforDelForCareforId(careforId, null);
			if(r.retCode == ErrorCanst.DEFAULT_SUCCESS){
				
				ArrayList<CareforDelBean> cs = (ArrayList<CareforDelBean>) r
						.getRetVal();
				
				int index = 0;
				for (CareforDelBean c : cs) {
	
					// actionType varchar(1), --动作类型<!-- 具体事件 (0)，发短信(1)，发邮件(2) -->
					
					// dateType --<!-- 无日期(0)，绝对日期(1)，相对日期(2)，循环(3) -->
					if ("0".equals(c.getDateType())) {
						
						CareforActionDelBean delBean  = new CareforActionDelBean();
						index++;
						delBean.setId(id+index);
						delBean.setCareforAction(bean);
						delBean.setActor(bean.getActor());
						delBean.setStatus(1);
						delBean.setEventId(c.getId());
						delBean.setEventName(c.getActionName());
						delBean.setStartDate("");
						delBean.setEndDate("");
						delBeans.add(delBean);
					} else if ("1".equals(c.getDateType())) {
						CareforActionDelBean delBean  = new CareforActionDelBean();
						index++;
						delBean.setId(id+index);
						delBean.setCareforAction(bean);
						delBean.setActor(bean.getActor());
						delBean.setStatus(1);
						delBean.setEventId(c.getId());
						delBean.setEventName(c.getActionName());
						delBean.setStartDate(c.getStartDate());
						delBean.setEndDate(c.getEndDate());
						delBeans.add(delBean);						
					} else if ("2".equals(c.getDateType())) {
						Date d;
						try {
							d = BaseDateFormat.parse(baselineDate,
									BaseDateFormat.yyyyMMdd);
						} catch (Exception e) {
							e.printStackTrace();
							rst = new Result();
							rst.setRetCode(ErrorCanst.TIMER_COMPARE_ERROR);
							return rst;
						}
						for (int i = 0; i < c.getRunDates(); i++) {
							String start = BaseDateFormat.format(new Date(d.getTime() + c.getBaselineDate()+i*(c.getSpace())* oneDay), BaseDateFormat.yyyyMMdd);
							String end = BaseDateFormat.format(new Date(d.getTime() + c.getBaselineDate()+(i*(c.getSpace())+1)* oneDay), BaseDateFormat.yyyyMMdd);
							
							CareforActionDelBean delBean  = new CareforActionDelBean();
							index++;
							delBean.setId(id+index);
							delBean.setCareforAction(bean);
							delBean.setActor(bean.getActor());
							delBean.setStatus(1);
							delBean.setEventId(c.getId());
							delBean.setEventName(c.getActionName()+"_"+(i+1));
							delBean.setStartDate(start);
							delBean.setEndDate(end);
							delBeans.add(delBean);
						}
	
					} else if ("3".equals(c.getDateType())) {
						
						for(int j=0 ; j< c.getRunTimes() ; j++){
							Date d;
							try {
								d = BaseDateFormat.parse(baselineDate,
										BaseDateFormat.yyyyMMdd);
							} catch (Exception e) {
								e.printStackTrace();
								rst = new Result();
								rst.setRetCode(ErrorCanst.TIMER_COMPARE_ERROR);
								return rst;
							}
							d = new Date(d.getTime() + j*(365)* oneDay); //增加一年
							for (int i = 0; i < c.getRunDates(); i++) {
								String start = BaseDateFormat.format(new Date(d.getTime() + c.getBaselineDate()+i*(c.getSpace())* oneDay), BaseDateFormat.yyyyMMdd);
								String end = BaseDateFormat.format(new Date(d.getTime() + c.getBaselineDate()+(i*(c.getSpace())+1)* oneDay), BaseDateFormat.yyyyMMdd);
								
								CareforActionDelBean delBean  = new CareforActionDelBean();
								index++;
								delBean.setId(id+index);
								delBean.setCareforAction(bean);
								delBean.setActor(bean.getActor());
								delBean.setStatus(1);
								delBean.setEventId(c.getId());
								delBean.setEventName(c.getActionName()+"_"+(j+1)+"_"+(i+1));
								delBean.setStartDate(start);
								delBean.setEndDate(end);
								delBeans.add(delBean);
							}
						}
					}
				}
			}
			
			bean.setCareforActionDels(delBeans); 
			Result ra = addBean(bean);
			if (ra.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				return ra;
			}		
			/** 这里决定不产生客户关怀工作计划，改为在客户关怀事件线程中产生5天内的工作计划  **/
//			ra = createJobPlan(null, id,planName,null);
//			if (ra.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
//				return ra;
//			}
		} catch (Exception e) {
			BaseEnv.log.error("CareforMgt addCareforAction:",e) ;
			rst.setRetCode(ErrorCanst.RET_ILLEGAL_ARGUMENT_ERROR);
		}
		return rst;
	}


	/**
	 * 得到职员ID和部门Code的键值对
	 * 
	 * @param id
	 * @return
	 */
	public Result queryEmpIdAndDepaCode(final String[] empId) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							String empStr = "";
							for(String em:empId){
								empStr +=",'"+em+"'";
							}
							if(empStr.length() > 0 ){
								empStr = empStr.substring(1);
							}
							
							String sql = "select id,DepartmentCode from tblEmployee where id in("+empStr+")";

							PreparedStatement ps = connection.prepareStatement(sql);

							ResultSet rs = ps.executeQuery();
							HashMap<String, String> map = new HashMap<String, String>();
							while (rs.next()) {
								map.put(rs.getString(1), rs.getString(2));

							}
							rst.setRetVal(map);
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("CareforMgt queryEmpIdAndDepaCode:",e) ;
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
	 * 得到客户名称和编号
	 * 
	 * @param id
	 * @return
	 */
	public Result queryClientName(final String id) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {

							String sql = "select ClientNo,ClientName from CRMClientInfo where id = ?";

							PreparedStatement ps = connection.prepareStatement(sql);

							ps.setString(1, id);

							ResultSet rs = ps.executeQuery();
							String[] strs = new String[2];
							if (rs.next()) {
								strs[0] = rs.getString(1);
								strs[1] = rs.getString(2);
								rst.setRetVal(strs);
								rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							} else {
								rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("CareforMgt queryClientName:",e) ;
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
	
	public Result queryClientNameMap(final ArrayList<String> idList) {
		final Result rst = new Result();
		final HashMap map = new HashMap();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							String ids = "";
							for(String s:idList){
								if(s!=null && s.length() >0){
									ids +=",'"+s+"'";
								}
							}
							if(ids.length() >0){
								ids = ids.substring(1);
							}
							if(ids.length() ==0){
								return;
							}
							String sql = "select id,ClientName from CRMClientInfo where id in ("+ids+")";

							PreparedStatement ps = connection.prepareStatement(sql);

							ResultSet rs = ps.executeQuery();
							while (rs.next()) {
								map.put(rs.getString(1), rs.getString(2));
							} 
						} catch (Exception e) {
							BaseEnv.log.error("CareforMgt queryClientNameMap:",e) ;
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		rst.retVal = map;

		return rst;

	}

	/**
	 * 得到客户id
	 * 
	 * @param id
	 * @return
	 */
	public Result queryClientId(final String ClientNo) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {

							String sql = "select id from CRMClientInfo where ClientNo = ?";

							PreparedStatement ps = connection.prepareStatement(sql);

							ps.setString(1, ClientNo);

							ResultSet rs = ps.executeQuery();

							if (rs.next()) {

								rst.setRetVal(rs.getString(1));
								rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							} else {
								rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("CareforMgt queryClientId:",e) ;
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
	 * 得到执行人的名字
	 * 
	 * @param ClientNo
	 * @return
	 */
	public Result queryActorName(final String[] actor) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							StringBuffer actors = new StringBuffer("");
							int i = 0;
							for (String s : actor) {
								if (i == 0) {
									actors.append("'" + s + "'");
									i++;
								} else {
									actors.append(",'" + s + "'");
								}
							}
							String sql = "select EmpFullName from tblEmployee where id in("
									+ actors + ")";

							PreparedStatement ps = connection
									.prepareStatement(sql);

							ResultSet rs = ps.executeQuery();
							ArrayList<String> names = new ArrayList<String>();
							while (rs.next()) {
								names.add(rs.getString(1));
							}
							rst.setRetVal(names);
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("CareforMgt queryActorName:",e) ;
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
	 * 修改客户关怀明细的状态
	 */
	public Result updateCareforActionDel(String id,String startDate,String endDate,String createTime,String remark, int status) {
		Result rs = loadBean(id,CareforActionDelBean.class);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			CareforActionDelBean bean = (CareforActionDelBean)rs.retVal;
			if(status != -1){
				bean.setStatus(status);
			}
			if(startDate != null && startDate.length() >0){
				bean.setStartDate(startDate);
			}
			if(endDate != null && endDate.length() >0){
				bean.setEndDate(endDate);
			}
			if(createTime != null && createTime.length() >0){
				bean.setCreateTime(createTime);
			}
			if(remark != null && remark.length() >0){
				bean.setRemark(remark);
			}
			rs = this.updateBean(bean);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && status != -1){
				//检查当所有事伯执行完成时，更新主表的完成状态
				checkCareforActionStatus(bean.getCareforAction().getId());
			}
		}
		return rs;
	}
	
	public Result checkCareforActionStatus(final String id){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						
						try {						
							
							String sql = "select count(*) from tblCareforActionDel where status =1 and ref_id = ?";
							PreparedStatement stat = connection.prepareStatement(sql);
							stat.setString(1, id);
							ResultSet rs = stat.executeQuery();
							if(rs.next()){
								int count = rs.getInt(1);
								if(count == 0){
									sql = " update tblCareforAction set status = 2 where id= ? ";
									stat = connection.prepareStatement(sql);
									stat.setString(1, id);
									stat.execute();
								}
							}							
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							BaseEnv.log.error("CareforMgt stopAll:",e) ;
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
	 * 生成工作计划
	 * 
	 * @param id 客户关怀执行明细ID
	 * @param carefor 客户关怀执行ID
	 * @return
	 */
	public Result createJobPlan(final String id, final String date) {	
		//查执行明细
		Result rs = this.loadBean(id, CareforActionDelBean.class);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}
		final CareforActionDelBean bean = (CareforActionDelBean)rs.retVal;	
		//查事件
		rs = this.loadBean(bean.getEventId(), CareforDelBean.class);
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}
		final CareforDelBean eventBean = (CareforDelBean)rs.retVal;	
		//查客户名字
		rs = queryClientName(bean.getCareforAction().getClientId());
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}
		//查部门
		final String clientName = ((String[])rs.retVal)[1];
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}
		
		rs = queryEmpIdAndDepaCode(bean.getActor().split(",|;"));
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			return rs;
		}
		final HashMap<String, String> empMap = (HashMap<String, String>)rs.retVal;
		
		final Result rst = new Result();
//		int retCode = DBUtil.execute(new IfDB() {
//			public int exec(Session session) {
//				String tId = IDGenerater.getId();
//				String[] actor = bean.getActor().split(";");
//				for (int i=0;i<actor.length;i++ ) {
//					OADayWorkPlanBean wpb = new OADayWorkPlanBean();
//					wpb.setId(tId+i);
//					String startDate = date;
//					if(startDate == null || "".equals(startDate)){
//						startDate = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
//					}
//					wpb.setEmployeeID(actor[i]);
//					wpb.setDepartmentCode(empMap.get(actor[i]));
//					wpb.setTitle(clientName+": "+bean.getEventName());
//					wpb.setContent(eventBean.getEventContent());
//					wpb.setPlanType("event");
//					wpb.setCreateBy(actor[i]);
//					wpb.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
//
//					Result rs = addBean(wpb,session);
//					if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
//						return rst.getRetCode();
//					}
//				}
//				bean.setHasPlan(1); //已生成工作计划
//				Result rs = updateBean(bean,session);				
//				return rs.retCode;
//			}
//		});
//		rst.setRetCode(retCode);
		return rst;
	}

	/**
	 * 停止执行方案
	 * 
	 * @param keyIds
	 * @return
	 */
	public Result stopAll(final String[] keyIds) {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						
						try {
							StringBuffer ids = new StringBuffer("");
							int i = 0;
							for (String s : keyIds) {
								if (i == 0) {
									ids.append("'" + s + "'");
									i++;
								} else {
									ids.append(",'" + s + "'");
								}
							}
							Statement stat = connection.createStatement();
							String strsql = "delete from tblDayWorkPlan where id in(select w.id from tblCarefor as c right join tblCareforAction as ac on c.id=ac.careforId right join tblCareforActionDel as acd on acd.ref_id = ac.id right join tblDayWorkPlan as w on w.event=acd.id where c.id in("
									+ ids.toString() + "))";
							stat.addBatch(strsql);

							String strsql2 = "update tblCareforActionDel set status = '3' where status = '1' and ref_id in(select ac.id from tblCarefor as c right join tblCareforAction as ac on c.id=ac.careforId right join tblCareforActionDel as acd on acd.ref_id = ac.id where c.id in  ("
									+ ids.toString() + "))";
							stat.addBatch(strsql2);

							String strsql3 = "update tblCareforAction set status = '3' where status = '1' and id in(select ac.id from tblCareforAction as ac left join tblCarefor as c on c.id=ac.careforId left join tblCareforActionDel as acd on acd.ref_id = ac.id where c.id in ("
									+ ids.toString() + "))";
							stat.addBatch(strsql3);
							
							String strsql4 = "update tblCarefor set status = '-1' where  id in ("
								+ ids.toString() + ")";
							stat.addBatch(strsql4);

							stat.executeBatch();
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("CareforMgt stopAll:",e) ;
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
	 * 停止执行一个关怀
	 * 
	 * @param id
	 * @return
	 */
	public Result stopTheCarefor(final String id,boolean stop) {
		Result rs = this.loadBean(id, CareforActionBean.class);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			CareforActionBean bean  = (CareforActionBean)rs.getRetVal();
			bean.setStatus(stop?"3":"1");
			return this.updateBean(bean);
		}
		return rs;
	}
	
	
	 
	/**
	 * 自动发送短信和邮件
	 * 提前5天自动产生工作计划
	 */
	public void autoSend() {
		try{
			autoSendSms();
			autoSendMail();
			autoSendJobPlan();
		}catch(Exception e){
			BaseEnv.log.error("CareforMgt.autoSend()",e);
		}
		//处理联系人记念日
		try{
			autoMemoryDay();
		}catch(Exception e){
			BaseEnv.log.error("CareforMgt.autoSend()",e);
		}

	}
	
	/**
	 * 自动发送短信和邮件
	 * 提前5天自动产生工作计划
	 */
	public Result autoSendSms() {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						
						try {
							//type --动作类型<!-- 具体事件 (0)，发短信(1)，发邮件(2) -->
							String now = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
							String nowHour = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss).substring(11, 13);
							
							if (BaseEnv.telecomCenter != null) {
								//处理短信									
								String sql = "select action.id,adel.id as delId,action.clientid,action.receiver,cdel.smsContent,adel.actor, "+
								" adel.startDate,adel.endDate from tblCareforAction action join tblCareforActionDel adel on "+
								" action.id = adel.ref_id join tblCareforDel cdel on adel.eventId=cdel.id "+
								" where action.status=1 and cdel.actionType=1 and adel.status=1  and  (adel.startDate < ? or ( adel.startDate = ? and cdel.smsTime <= ? ) )";
								PreparedStatement ps = connection.prepareStatement(sql);
								ps.setString(1, now);
								ps.setString(2, now);
								ps.setString(3, nowHour);
							    ResultSet rs = ps.executeQuery();
							    ArrayList<Object[]> list = new ArrayList<Object[]>();
							    while(rs.next()){							    	
							    	try{			
							    		String rec = rs.getString("receiver");
							    		String clientId = rs.getString("clientid");
							    		String content = rs.getString("smsContent");
							    		String actor = rs.getString("actor");
							    		String delId = rs.getString("delId");
							    		String startDate = rs.getString("startDate");
							    		String endDate = rs.getString("endDate");
							    		
							    		if(actor.indexOf(";")>0){
							    			actor = actor.substring(0,actor.indexOf(";"));
							    		}
							    		if(actor.indexOf(",")>0){
							    			actor = actor.substring(0,actor.indexOf(","));
							    		}
							    		
							    		Object[] os = new Object[15];
							    		os[0] = content;
							    		os[1] = actor;
							    		os[2] = delId;
							    		os[3] = startDate;
							    		os[4] = endDate;
							    		
							    		os[10] = clientId;
							    		os[11] = rec;
							    		list.add(os); 
						    		}catch (Exception e) {
						    			BaseEnv.log.error("CareforMgt autoSend:",e) ;
									}
							    }
							    
							    for(Object[] os : list){
//							    	查询接收人号码
						    		ResultSet mrs = null;
						    		if(os[11]==null || os[11].toString().length() == 0){
						    			String msql = "select a.Mobile,a.UserName from CRMClientInfoDet a join CRMClientInfo b on a.f_ref = b.id where b.id = ? ";						    		
							    		PreparedStatement mps = connection.prepareStatement(msql);
							    		mps.setString(1, os[10].toString());
							    		mrs = mps.executeQuery();
						    		}else{
						    			String recStr="";
						    			for(String receiver:os[11].toString().split(";|,")){
						    				recStr +=",'"+receiver+"'";
						    			}
						    			if(recStr.length() >0){
						    				recStr = recStr.substring(1);
							    			String msql = "select a.Mobile,a.UserName from CRMClientInfoDet a where a.id in ("+recStr+") ";						    		
								    		PreparedStatement mps = connection.prepareStatement(msql);
								    		mrs = mps.executeQuery();
						    			}
						    		}
						    		boolean isSuccess = false;
						    		
						    		
						    		ArrayList mobiles = new ArrayList();
						    		os[5] = mobiles;
						    		if(mrs != null){
						    			while(mrs.next()){
						    				String mobile = mrs.getString(1);							    				
						    				if(mobile != null && mobile.length() > 0){
						    					mobiles.add(new String[]{mobile,mrs.getString(2)});
						    				}
						    			}
						    		} 
							    }
							    
							    rst.setRetVal(list);
						    }												
						} catch (Exception e) {
							BaseEnv.log.error("CareforMgt autoSend:",e) ;
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		
		if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS && rst.retVal != null){			
			for(Object[] o: (ArrayList<Object[]>)(rst.retVal)){
				boolean isSuccess = false;				
    			for(String[] mobile :(ArrayList<String[]>)o[5]){
					int resp = BaseEnv.telecomCenter.send(o[0].toString(),new String[]{mobile[0]},o[1].toString());												
					if (resp > 0) { // 发送成功
						isSuccess = true;//多个发人 只要有一个发送成功，就算成功 
					}
	    		}
	    		//执行完成							    		
	    		updateCareforActionDel(o[2].toString(),o[3].toString(),o[4].toString(),BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd),"", isSuccess?2:4);
			}
		}

		return rst;

	}
	
	/**
	 * 自动发送短信和邮件
	 * 提前5天自动产生工作计划
	 */
	public Result autoSendMail() {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						
						try {
							//type --动作类型<!-- 具体事件 (0)，发短信(1)，发邮件(2) -->
							String now = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
							String nowHour = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss).substring(11, 13);
							
							//处理邮件								
							String sql = "select action.id,adel.id as delId,action.clientid,action.receiver,"+
							"cdel.mailTitle,cdel.mailContent,cdel.addressType,cdel.ccSelf,adel.actor, "+
							" adel.startDate,adel.endDate from tblCareforAction action join tblCareforActionDel adel on "+
							" action.id = adel.ref_id join tblCareforDel cdel on adel.eventId=cdel.id "+
							" where action.status=1 and cdel.actionType=2 and adel.status=1  and adel.startDate <= ? ";
							PreparedStatement ps = connection.prepareStatement(sql);
							ps.setString(1, now);
						    ResultSet rs = ps.executeQuery();
						    ArrayList list = new ArrayList();
						    while(rs.next()){
						    	
						    	try{			
						    		String rec = rs.getString("receiver");
						    		String clientId = rs.getString("clientid");
						    		String actors = rs.getString("actor");
						    		String delId = rs.getString("delId");
						    		String startDate = rs.getString("startDate");
						    		String endDate = rs.getString("endDate");
						    		
						    		String mailTitle = rs.getString("mailTitle");
						    		String mailContent = rs.getString("mailContent");
						    		String addressType = rs.getString("addressType");
						    		String ccSelf = rs.getString("ccSelf");
						    		
						    		EMailMessage smes = new EMailMessage();
						    		
						    		String actor = actors;
						    		if(actor.indexOf(";")>0){
						    			actor = actor.substring(0,actor.indexOf(";"));
						    		}
						    		if(actor.indexOf(",")>0){
						    			actor = actor.substring(0,actor.indexOf(","));
						    		}
						    		
						    		
						    		
						    		//查询接收人邮件
						    		ResultSet mrs = null;
						    		if(rec==null || rec.length() == 0){
						    			String msql = "select a.ClientEmail from CRMClientInfoDet a join CRMClientInfo b on a.f_ref = b.id where b.id = ? ";						    		
							    		PreparedStatement mps = connection.prepareStatement(msql);
							    		mps.setString(1, clientId);
							    		mrs = mps.executeQuery();
						    		}else{
						    			String recStr="";
						    			for(String receiver:rec.split(";|,")){
						    				if(receiver.length() >0 )
						    					recStr +=",'"+receiver+"'";
						    			}
						    			if(recStr.length() >0){
						    				recStr = recStr.substring(1);
							    			String msql = "select a.ClientEmail from CRMClientInfoDet a where a.id in ("+recStr+") ";						    		
								    		PreparedStatement mps = connection.prepareStatement(msql);
								    		mrs = mps.executeQuery();
						    			}
						    		}
						    		
						    		String mailTo = "";
						    		if(mrs != null){
						    			while(mrs.next()){
						    				String mail = mrs.getString(1);
											if(mail != null && mail.length() > 0 ){
												smes.addRecipient(EMailMessage.TO, mail, "");
												mailTo +=mail+";";
											}
						    			}
						    		}
						    		
						    		String ccId="";
					    			for(String act:actors.split(";|,")){
					    				ccId +=",'"+act+"'";
					    			}
					    			if(ccId.length() >0){
					    				ccId = ccId.substring(1);
					    			}
						    		
						    		if("1".equals(ccSelf)){
						    			//需要抄送自己,查所有执行人自己邮件
						    			String msql = "select a.mailaddress from mailinfosetting a where a.defaultUser='1' and a.createBy in ("+ccId+") ";						    		
							    		PreparedStatement mps = connection.prepareStatement(msql);
							    		mrs = mps.executeQuery();
							    		while(mrs.next()){
						    				String mail = mrs.getString(1);
						    				smes.addRecipient(EMailMessage.CC, mail, ""); //抄送
						    			}					    			
						    		}
						    		
						    		
						    		
						    		//如果是企业统一邮箱isCorporationEmail为1，其它为0，所以isCorporationEmail进行降序排列，能第一条取到企业邮箱
						    		//如果没有企业统一邮箱，则任意取一个执行人的默认邮箱
						    		String mset = "select top 1 * from mailinfosetting where createBy in ("+ccId+")  ";
						    		//取送件信息
						    		if("1".equals(addressType)){
						    			//企业统一邮箱
						    			mset += " or isCorporationEmail = 1 ";
						    		}						    		
						    		mset +=" order by isCorporationEmail desc ,defaultUser asc ";
						    		PreparedStatement mps = connection.prepareStatement(mset);
						    		mrs = mps.executeQuery();
						    		Object[] os = new Object[15];
						    		os[0]=false;
						    		
						    		if(mrs.next()){
						    			try {						    				
						    				AIOEMail sm = new AIOEMail();
						    				boolean smtpauth = mrs.getInt("SmtpAuth") == 1 ? true : false;
						    				// 指定要使用的邮件服务器
						    				sm.setAccount(mrs.getString("Mailaddress"), mrs.getString("Pop3server"), mrs.getString("Pop3username"),
						    						mrs.getString("Pop3userpassword"),smtpauth, mrs.getString("Smtpserver"), mrs.getString("Smtpusername"),
						    						mrs.getString("Smtpuserpassword"), mrs.getInt("Pop3Port"), mrs.getInt("SmtpPort"), 
						    						mrs.getString("DisplayName"), mrs.getString("Id"),mrs.getString("Createby"),
						    						mrs.getInt("Popssl")==1?true:false,mrs.getInt("Smtpssl")==1?true:false,mrs.getString("AutoAssign")=="1"?true:false);
						    				// 指定帐号和密码
						    				smes.setSubject(mailTitle);
						    				smes.setContent(true, mailContent);
						    				
						    				os[0] = true;
						    				os[1] = sm;
						    				os[2] = smes;
						    				os[3] = mrs.getString("createBy");
						    				os[4] = mailTo;
						    				os[5] = mailTitle;
						    				os[6] = mailContent;
						    				os[7] = mrs.getString("id");						    				
						    			} catch (Exception ex) {
						    				BaseEnv.log.error("CareforMgt.autoSend mail ",ex);
						    			}
					    			}
						    		os[8] = delId;
						    		os[9] = startDate;
						    		os[10] = endDate;
						    		
						    		list.add(os);
						    		
						    	}catch (Exception e) {
					    			BaseEnv.log.error("CareforMgt autoSend:",e) ;
								}
						    }
						    rst.setRetVal(list);
						} catch (Exception e) {
							BaseEnv.log.error("CareforMgt autoSend:",e) ;
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		
		if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS && rst.retVal != null){			
			for(Object[] o: (ArrayList<Object[]>)(rst.retVal)){
				boolean isSuccess = false;
	    		if((Boolean)o[0]){
	    			try {
	    				((AIOEMail)o[1]).send((EMailMessage)o[2]);
	    				String createTime = BaseDateFormat.format(new Date(),
	    						BaseDateFormat.yyyyMMddHHmmss);
	    				// 将发送的数据保存至数据库
	    				Result rest;						    				
	    				EMailMgt emailMgt = new EMailMgt();
						emailMgt.addMail(o[3].toString(), o[4].toString(), o[5].toString(), createTime,
								o[6].toString(), 1, "", o[3].toString(), o[7].toString());
						isSuccess = true; //邮件发送成功
	    			} catch (Exception ex) {
	    				BaseEnv.log.error("CareforMgt.autoSend mail ",ex);
	    			}
				}
	    		//执行完成							    		
	    		updateCareforActionDel(o[8].toString(),o[9].toString(),o[10].toString(),BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd),"", isSuccess?2:4);
			}
		}
		return rst;

	}
	
	/**
	 * 自动发送短信和邮件
	 * 提前5天自动产生工作计划
	 */
	public Result autoSendJobPlan() {
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						
						try {
							//type --动作类型<!-- 具体事件 (0)，发短信(1)，发邮件(2) -->
							String now = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
							String nowHour = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss).substring(11, 13);							
						    
						    //处理工作计划								
							String sql = "select adel.id as delId, adel.startDate,adel.endDate from tblCareforAction action join tblCareforActionDel adel on "+
							" action.id = adel.ref_id join tblCareforDel cdel on adel.eventId=cdel.id "+
							" where action.status=1 and cdel.actionType=0 and adel.status=1 and adel.hasPlan = 0 and adel.startDate <> '' and adel.startDate is not null and adel.startDate <= ? ";
							PreparedStatement ps = connection.prepareStatement(sql);
							String now1 = BaseDateFormat.format(new Date(new Date().getTime()+5*24*60*60000), BaseDateFormat.yyyyMMdd);
							ps.setString(1, now1);
						    ResultSet rs = ps.executeQuery();
						    
						    ArrayList list = new ArrayList();
						    while(rs.next()){	
					    		String delId = rs.getString("delId");
					    		String startDate = rs.getString("startDate");	
					    		list.add(new String[]{delId,startDate});
						    }	
						    rst.setRetVal(list);
						} catch (Exception e) {
							BaseEnv.log.error("CareforMgt autoSend:",e) ;
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		
		if(rst.retCode == ErrorCanst.DEFAULT_SUCCESS && rst.retVal != null){
			ArrayList<String[]> list = (ArrayList<String[]>)rst.retVal;
			for(String[] strs :list){
				try{
					createJobPlan(strs[0],strs[1]);
				}catch(Exception e){
					BaseEnv.log.error("CareforMgt autoSendJobPlan:",e) ;
				}
			}
		}

		return rst;

	}
	
	/**
	 * 记念日提醒处理
	 * 1、查询记念日设置表
	 * 2、根据表中不同类型的记念日处理
	 * 3、如果有短信或邮件祝福，查询阳历当天过某类型记念日的数据，并发送短信或记念日。
	 * 4、如果有短信或邮件祝福，转换当天的阳历为阴历，并查询阴历当天过某类型记念日的数据，并发送短信或记念日。
	 * 5、如果有短信或邮件或即时消息提醒，则以当天前前几天为基础查询某类型记念日的数据，并发送提醒。
	 * 6、如果有短信或邮件或即时消息提醒，则以当天前前几天为基础转换成阴历查询某类型记念日的数据，并发送提醒。
	 *
	 */
	public void autoMemoryDay(){
		//查询记念日设置表
		String sql = " select MemoryType ,ifSMSSend,smsTime,smsContent,ifMailSend,ifccSelf,mailAccount,"+
			"mailTitle,mailContent,noteDate,ifsmsNote,ifmailNote,ifMsgNote,ifPlanNote,SCompanyID from MemoryDayNoteSet where statusId = 0 ";
		Result rs = sqlList(sql,new ArrayList());
		if(rs.retCode !=ErrorCanst.DEFAULT_SUCCESS || rs.retVal == null){
			BaseEnv.log.debug("CareforMgt.autoMemoryDay query MemoryDayNoteSet Error ");
			return;
		}
		
		String curDate = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
		
		ArrayList<Object[]> setList = (ArrayList<Object[]>)rs.retVal;
		for(Object[] setObjects:setList){
			String memoryType  =setObjects[0].toString();
			String ifSMSSend=setObjects[1].toString();
			String smsTime=setObjects[2].toString();
			String smsContent=setObjects[3].toString();
			String ifMailSend=setObjects[4].toString();
			String ifccSelf=setObjects[5].toString();
			String mailAccount=setObjects[6].toString();
			String mailTitle=setObjects[7].toString();
			String mailContent=setObjects[8].toString();
			String preNoteDate=setObjects[9].toString();
			String ifsmsNote=setObjects[10].toString();
			String ifmailNote=setObjects[11].toString();
			String ifMsgNote=setObjects[12].toString();
			String ifPlanNote=setObjects[13].toString();
			String sCompanyID=setObjects[14].toString();
			
			//未到短信发送时间，什么都不做，也即，邮件和短信都采用这一时间控制执行。因为跟单人提醒也需要这一时间来做
			String nowHour = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss).substring(11, 13);
			int execTime = 0;
			try{execTime = Integer.parseInt(smsTime); }catch(Exception e){}
			if(Integer.parseInt(nowHour) < execTime){				
				continue;
			}
			
			GlobalsTool gt = new GlobalsTool();
			String memoryName =  gt.getEnumerationItemsDisplay("MemoryType", memoryType,BaseEnv.defaultLocale.toString());
			//判断是否有需要进行提醒
			if("1".equals(ifsmsNote)||"1".equals(ifmailNote)||"1".equals(ifMsgNote)||"1".equals(ifPlanNote)){
				
				
				//计算提醒的日期 减少提前天数
				int preD = 0;
				try{preD = Integer.parseInt(preNoteDate); }catch(Exception e){}
				String needNoteDate = BaseDateFormat.format(new Date(new Date().getTime() + preD*24*60*60000), BaseDateFormat.yyyyMMdd);
				//查询过阳历提醒的联系人 公历:1;农历:2   MemoryType
				sql = " select c.ClientName,p.UserName ,e.EmployeeID, d.id,d.date,d.dateType from "+
					" CRMMemoryDay m join  CRMMemoryDayDet d on m.id=d.f_ref "+
					" join CRMClientInfoDet p on m.mainUser = p.contactNo "+
					" join CRMClientInfo c on m.ClientId = c.id "+
					" join CRMClientInfoEmp e on e.f_ref=c.id"+
					" where d.MemoryType=? and d.dateType = 1 and d.Date like ? and (d.updateFlag is null or (d.updateFlag <> ? and d.updateFlag <> ?))  and m.SCompanyID=? ";
				ArrayList param = new ArrayList();
				param.add(memoryType);
				param.add("%"+needNoteDate.substring(5));
				param.add(needNoteDate+"NOTE");
				param.add(needNoteDate+"SEND");
				param.add(sCompanyID);
				rs = sqlList(sql,param);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					ArrayList<Object[]> noteList = (ArrayList<Object[]>)rs.retVal;
					ArrayList<String> updateList = new ArrayList<String>();
					for(Object[] noteObjects:noteList){
						//提醒格式 格式如  客户+联第人+日期+类型名称
						String dataName = gt.getEnumerationItemsDisplay("dateType", noteObjects[5].toString(),BaseEnv.defaultLocale.toString());
						String format = noteObjects[0]+" "+noteObjects[1]+dataName+noteObjects[4]+memoryName;
						//短信提醒
						if("1".equals(ifsmsNote)){
							NotifyFashion nf = new NotifyFashion("1", format,format,noteObjects[2].toString(),1,"no",noteObjects[3].toString());
							nf.start();
						}
						//邮件提醒
						if("1".equals(ifsmsNote)){
							NotifyFashion nf = new NotifyFashion("1", format,format,noteObjects[2].toString(),2,"no",noteObjects[3].toString());
							nf.start();
						}
						//消息提醒
						if("1".equals(ifsmsNote)){
							NotifyFashion nf = new NotifyFashion("1", format,format,noteObjects[2].toString(),4,"no",noteObjects[3].toString());
							nf.start();
						}
						//工作计划
						if("1".equals(ifsmsNote)){
							NotifyFashion nf = new NotifyFashion("1", format,format,noteObjects[2].toString(),5,"no",noteObjects[3].toString());
							nf.start();
						}
						updateList.add(noteObjects[3].toString());
					}
					//更新记念日标志，表明此记录已处理 
					updateFlag(updateList,needNoteDate+"NOTE"); 
				}else{
					BaseEnv.log.error(" CareforMgt.autoMemoryDay query CRMClientInfoEmp Note Error");
				}
				
				//查询当阴历提醒的联系人
				
				//将needNoteDate 转化为阴历
				String lundarDate = ChineseCalendar.sCalendarSolarToLundar(Integer.parseInt(needNoteDate.substring(0,4)),
						Integer.parseInt(needNoteDate.substring(5,7)),Integer.parseInt(needNoteDate.substring(8)));
				//转为带符号数据
				lundarDate = lundarDate.substring(0,4)+"-"+lundarDate.substring(4,6)+"-"+lundarDate.substring(6);
				//查询过阳历提醒的联系人 公历:1;农历:2   MemoryType
				sql = " select c.ClientName,p.UserName ,e.EmployeeID, d.id,d.date,d.dateType from "+
				" CRMMemoryDay m join  CRMMemoryDayDet d on m.id=d.f_ref "+
				" join CRMClientInfoDet p on m.mainUser = p.contactNo "+
				" join CRMClientInfo c on m.ClientId = c.id "+
				" join CRMClientInfoEmp e on e.f_ref=c.id"+
				" where d.MemoryType=? and d.dateType = 2 and d.Date like ? and (d.updateFlag is null or (d.updateFlag <> ? and d.updateFlag <> ?)) and m.SCompanyID=? ";
				param = new ArrayList();
				param.add(memoryType);
				param.add("%"+lundarDate.substring(5));
				param.add(lundarDate+"NOTE");
				param.add(lundarDate+"SEND");
				param.add(sCompanyID);
				rs = sqlList(sql,param);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					ArrayList<Object[]> noteList = (ArrayList<Object[]>)rs.retVal;
					ArrayList<String> updateList = new ArrayList<String>();
					for(Object[] noteObjects:noteList){
						//提醒格式 格式如  客户+联第人+日期+类型名称 
						String dataName = gt.getEnumerationItemsDisplay("dateType", noteObjects[5].toString(),BaseEnv.defaultLocale.toString());
						String format = noteObjects[0]+" "+noteObjects[1]+dataName+noteObjects[4]+memoryName;
						//短信提醒
						if("1".equals(ifsmsNote)){
							NotifyFashion nf = new NotifyFashion("1", format,format,noteObjects[2].toString(),1,"no",noteObjects[3].toString());
							nf.start();
						}
						//邮件提醒
						if("1".equals(ifsmsNote)){
							NotifyFashion nf = new NotifyFashion("1", format,format,noteObjects[2].toString(),2,"no",noteObjects[3].toString());
							nf.start();
						}
						//消息提醒
						if("1".equals(ifsmsNote)){
							NotifyFashion nf = new NotifyFashion("1", format,format,noteObjects[2].toString(),4,"no",noteObjects[3].toString());
							nf.start();
						}
						//工作计划
						if("1".equals(ifsmsNote)){
							NotifyFashion nf = new NotifyFashion("1", format,format,noteObjects[2].toString(),5,"no",noteObjects[3].toString());
							nf.start();
						}
						
						updateList.add(noteObjects[3].toString());
					}
					//更新记念日标志，表明此记录已处理					
					updateFlag(updateList,lundarDate+"NOTE");
				}else{
					BaseEnv.log.error(" CareforMgt.autoMemoryDay query CRMClientInfoEmp Note luber Error");
				}
				
				
			}
			//判断是否有需要自动祝福事件 
			if(("1".equals(ifSMSSend) && smsContent!=null&& smsContent.length() > 0 ) || ("1".equals(ifMailSend) && mailAccount!=null&& mailAccount.length() > 0 )){
				EMailMgt mgt = new EMailMgt();
				Result r = mgt.loadAccount(mailAccount);
				if(r.retCode != ErrorCanst.DEFAULT_SUCCESS || r.retVal == null){
					continue;
				}
				MailinfoSettingBean obj = (MailinfoSettingBean)r.retVal;
				
				
				
				String needNoteDate = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
				
				//查询当天过阳历生日的联系人
				sql = " select c.ClientName,p.UserName ,p.Mobile,p.ClientEmail,d.id from "+
				" CRMMemoryDay m join  CRMMemoryDayDet d on m.id=d.f_ref "+
				" join CRMClientInfoDet p on m.mainUser = p.contactNo "+
				" join CRMClientInfo c on m.ClientId = c.id "+
				" where d.MemoryType=? and d.dateType = 1 and d.Date like ? and (d.updateFlag is null or d.updateFlag<>?) and m.SCompanyID=? ";
				ArrayList param = new ArrayList();
				param.add(memoryType);
				param.add("%"+needNoteDate.substring(5));
				param.add(needNoteDate+"SEND");
				param.add(sCompanyID);
				rs = sqlList(sql,param);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					ArrayList<Object[]> noteList = (ArrayList<Object[]>)rs.retVal;
					ArrayList<String> updateList = new ArrayList<String>();
					for(Object[] noteObjects:noteList){
						//
						if("1".equals(ifSMSSend) && smsContent!=null&& smsContent.length() > 0 ){
							//发短信
							if(noteObjects[2] != null&& !"".equals(noteObjects[2])){
								BaseEnv.telecomCenter.send(smsContent,new String[]{noteObjects[2].toString()},"1");
							}
						}
						if("1".equals(ifMailSend) && mailAccount!=null&& mailAccount.length() > 0){
							//发邮件
							if(noteObjects[3] != null&& !"".equals(noteObjects[3])){
								sendOuterMail(obj, noteObjects[3].toString(), mailTitle, mailContent);
							}
						}
						
						updateList.add(noteObjects[4].toString());
					}
					//更新记念日标志，表明此记录已处理					
					updateFlag(updateList,needNoteDate+"SEND");
				}else{
					BaseEnv.log.error(" CareforMgt.autoMemoryDay query CRMMemoryDay Note Error");
				}
				//查询当天过阴历生日的联系人
				String lundarDate = ChineseCalendar.sCalendarSolarToLundar(Integer.parseInt(needNoteDate.substring(0,4)),
						Integer.parseInt(needNoteDate.substring(5,7)),Integer.parseInt(needNoteDate.substring(8)));
				lundarDate = lundarDate.substring(0,4)+"-"+lundarDate.substring(4,6)+"-"+lundarDate.substring(6);
				
				//查询当天过阳历生日的联系人
				sql = " select c.ClientName,p.UserName ,p.Mobile,p.ClientEmail,d.id from "+
				" CRMMemoryDay m join  CRMMemoryDayDet d on m.id=d.f_ref "+
				" join CRMClientInfoDet p on m.mainUser = p.contactNo "+
				" join CRMClientInfo c on m.ClientId = c.id "+
				" where d.MemoryType=? and d.dateType = 2 and d.Date like ? and (d.updateFlag is null or d.updateFlag<>?) and m.SCompanyID=? ";
				param = new ArrayList();
				param.add(memoryType);
				param.add("%"+lundarDate.substring(5));
				param.add(lundarDate+"SEND");
				param.add(sCompanyID);
				rs = sqlList(sql,param);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					ArrayList<Object[]> noteList = (ArrayList<Object[]>)rs.retVal;
					ArrayList<String> updateList = new ArrayList<String>();
					for(Object[] noteObjects:noteList){
						//
						if("1".equals(ifSMSSend) && smsContent!=null&& smsContent.length() > 0 ){
							//发短信
							if(noteObjects[2] != null&& !"".equals(noteObjects[2])){
								BaseEnv.telecomCenter.send(smsContent,new String[]{noteObjects[2].toString()},"1");
							}
						}
						if("1".equals(ifMailSend) && mailAccount!=null&& mailAccount.length() > 0){
							//发邮件
							if(noteObjects[3] != null&& !"".equals(noteObjects[3])){
								sendOuterMail(obj, noteObjects[3].toString(), mailTitle, mailContent);
							}
						}						
						updateList.add(noteObjects[4].toString());
					}
					//更新记念日标志，表明此记录已处理					
					updateFlag(updateList,lundarDate+"SEND");
				}else{
					BaseEnv.log.error(" CareforMgt.autoMemoryDay query CRMMemoryDay luncy Note Error");
				}
			}
			
			
		}
	}
	
	private Result updateFlag(final ArrayList<String> ids,final String flag){
		if(ids.size() ==0) return new Result();
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection)
							throws SQLException {
						try {
							String sql = " update CRMMemoryDayDet set updateFlag=? where id=?";
							PreparedStatement ps = connection
							.prepareStatement(sql);
							for (String s : ids) {
								ps.setString(1, flag);
								ps.setString(2, s);
								ps.addBatch();
							}							
							ps.executeBatch();
							
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("CareforMgt updateFlag:",e) ;
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
	
	
	public Result sendOuterMail(MailinfoSettingBean obj,String to, String subject,
			String content) {
		Result rs = new Result();
		
		EMailMgt mgt = new EMailMgt();
		
		if (null != obj) {
			try {
				AIOEMail sm = new AIOEMail();
				boolean smtpauth = obj.getSmtpAuth() == 1 ? true : false;
				// 指定要使用的邮件服务器
				sm.setAccount(obj.getMailaddress(), obj.getPop3server(), obj
						.getPop3username(), obj.getPop3userpassword(),
						smtpauth, obj.getSmtpserver(), obj.getSmtpusername(),
						obj.getSmtpuserpassword(), obj.getPop3Port(), obj
						.getSmtpPort(), obj.getDisplayName(), obj
						.getId(),obj.getCreateby(),
						obj.getPopssl()==1?true:false,obj.getSmtpssl()==1?true:false,obj.getAutoAssign()==1?true:false);
				// 指定帐号和密码
				EMailMessage smes = new EMailMessage();
				smes.addRecipient(EMailMessage.TO, to, "");
				smes.setSubject(subject);
				smes.setContent(true, content);
				sm.send(smes);
				String createTime = BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss);
				// 将发送的数据保存至数据库
				Result rest;
			
				rest = mgt.addMail("1", to, subject, createTime,
						content, 1, "", "1", obj.getId());
				rs.setRetCode(rest.getRetCode());
			} catch (Exception ex) {
				ex.printStackTrace();
				rs.setRetCode(ErrorCanst.SEND_EMAIL_ERROR);
				rs.setRetVal("common.msg.sendError");
				return rs;

			}
		} else {
			rs.setRetCode(ErrorCanst.NOT_SET_EMAIL_ACCOUNT);
			rs.setRetVal("com.oa.notsetMail");
			return rs;

		}
		return rs;
	}
	

}
