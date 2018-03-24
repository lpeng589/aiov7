package com.koron.crm.memoryDay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

public class ClientLinkmanMemoryDayMgt extends AIODBManager{
	
	/**
	 * 查询联系人纪念日
	 * 
	 * @return
	 */
	public Result queryLinkManMemoryDay(final String locale,
			final String firstName, final String clientName, final String userName,
			final String loginId, final int pageSize, final int pageNo,final MOperation mop) {
		// 客户ID，客户名称，日期联系人名称,关联人,纪念日类型,日期，联系人ID,
		String sql = "select CRMClientInfo.id as clientId,CRMClientInfo.clientName,CRMClientInfoDet.UserName,CRMMemoryDayDet.dateType,CRMMemoryDayDet.MemoryType,"
				+ "CRMMemoryDayDet.date,CRMMemoryDay.id as memoryId,CRMMemoryDayDet.nextDate,CRMMemoryDayDet.id as detId from CRMMemoryDay "
				+ "left join CRMMemoryDayDet on CRMMemoryDay.id=CRMMemoryDayDet.f_ref "
				+ "left join CRMClientInfo on CRMClientInfo.id=CRMMemoryDay.f_brother "
				+ "left join CRMClientInfoDet on (CRMClientInfoDet.f_ref=CRMClientInfo.id and CRMClientInfoDet.ContactNo=CRMMemoryDay.mainUser) where 1=1 and CRMClientInfo.status != '1'";
		ArrayList<String> param = new ArrayList<String>();
		if (!"1".equals(loginId)) {
			/*查看某字段值单据*/
			String fieldValueSQL = "" ;
			sql += " and ('"+ loginId+ "' in (select employeeId from CRMClientInfoEmp where f_ref=CRMClientInfo.id)";
			if(mop!=null){
				ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
				if (scopeRight != null && scopeRight.size()>0) {
					for (Object o : scopeRight) {
						String strUserIds = "" ;
						LoginScopeBean lsb = (LoginScopeBean) o;
						if(lsb!=null && "1".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								strUserIds += "'"+strId+"'," ;
							}
							strUserIds = strUserIds.substring(0, strUserIds.length()-1) ;
							sql += " or CRMClientInfo.id in (select f_ref from CRMClientInfoEmp where employeeId in (" + strUserIds+"))";
						}
						if(lsb!=null && "5".equals(lsb.getFlag())){
							sql += "or CRMClientInfo.id in (select f_ref from CRMClientInfoEmp where " ;
							for(String strId : lsb.getScopeValue().split(";")){
								sql += " departmentCode like '"+strId+"%' or " ;
							}
							if(sql.endsWith("or ")){
								sql = sql.substring(0,sql.length()-3) ;
							}
							sql += ")" ;
						}
						if(lsb!=null && "6".equals(lsb.getFlag()) && "CRMClientInfo".equals(lsb.getTableName())){
							if (lsb.getScopeValue() != null && lsb.getScopeValue().length() > 0) {
								fieldValueSQL = lsb.getTableName() + "." + lsb.getFieldName() + "='" +lsb.getScopeValue() + "'";
				            }
						}
					}
				}
			}
			sql += ")" ;
			if(fieldValueSQL.length()>0){
				sql += " and ("+fieldValueSQL+")" ;
			}
		}
		if (firstName != null && !"".equals(firstName)) {
			sql += " and CRMClientInfoDet.UserName like ?";
			param.add(firstName + '%');
		}
		if (clientName != null && !"".equals(clientName)) {
			sql += " and CRMClientInfo.clientName like ? ";
			param.add('%' + clientName + '%');
		}
		if (userName != null && !"".equals(userName)) {
			sql += " and CRMClientInfoDet.UserName like '%'+?+'%'";
			param.add(userName);
		}
		sql += " order by CRMMemoryDayDet.nextDate asc" ;
		final Result result = sqlList(sql, param);
		final Result rs_update = new Result() ;
		/*更新下一次纪念日的日期*/
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn) throws SQLException {
							try {
								Statement state = conn.createStatement() ;
								ArrayList listResult = (ArrayList) result.retVal;
								String sql = "" ;
								for (int i = 0; i < listResult.size(); i++) {
									Object[] strArray = (Object[]) listResult.get(i);
									if(strArray[5]!=null && strArray[5].toString().length()>0){
										Calendar calendar = Calendar.getInstance();
										String strDate = calendar.get(Calendar.YEAR)+ strArray[5].toString().substring(strArray[5].toString().indexOf("-"),strArray[5].toString().length());
										/* 今年的提醒时间 */
										Calendar nextCalendar = Calendar.getInstance();
										nextCalendar.setTime(BaseDateFormat.parse(strDate,BaseDateFormat.yyyyMMdd));
										if (nextCalendar.getTime().getTime() < calendar.getTime().getTime()) {
											calendar.add(Calendar.YEAR, 1);
											strDate = calendar.get(Calendar.YEAR)+ strArray[5].toString().substring(strArray[5].toString().indexOf("-"),strArray[5].toString().length());
										}
										/* 设置下一次提醒时间 */
										if ("2".equals(strArray[3])) {
											calendar.setTime(BaseDateFormat.parse(strDate,BaseDateFormat.yyyyMMdd));
											String str = ChineseCalendar.sCalendarLundarToSolar(
														calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
											strArray[7] = str;
										} else {
											strArray[7] = strDate;
										}
										sql = "update CRMMemoryDayDet set nextDate='"+strArray[7]+"' where id='"+ strArray[8]+"'";
										state.addBatch(sql) ;
									}
								}
								state.executeBatch() ;
							} catch (Exception ex) {
								ex.printStackTrace();
								BaseEnv.log.error("queryLinkManMemoryDay delete:",ex) ;
							}
						}
					});
					return rs_update.getRetCode();
				}
			});
		}
		Result list_result = sqlList(sql, param, pageSize, pageNo, true);
		return list_result;
	}
	
	/**
	 * 删除联系人纪念日
	 * 
	 * @param id
	 * @return
	 */
	public Result delete(final String[] keyIds) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							for (int i=0;i<keyIds.length;i++) {
								String[] arrayKey = keyIds[i].split(":") ;
								String sql = "delete from CRMMemoryDayDet where id = ?";
								PreparedStatement pss = conn.prepareStatement(sql) ;
								pss.setString(1, arrayKey[0]) ;
								pss.executeUpdate() ;
								/*如果从表中的纪念日全删了，把主表记录也删了*/
								sql = "select * from CRMMemoryDayDet where f_ref = ?" ;
								pss = conn.prepareStatement(sql) ;
								pss.setString(1, arrayKey[1]) ;
								ResultSet rss = pss.executeQuery() ;
								if(!rss.next()){
									sql = "delete from CRMMemoryDay where id=?" ;
									pss = conn.prepareStatement(sql) ;
									pss.setString(1, arrayKey[1]) ;
									pss.executeUpdate() ;
								}
								result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
							}
						}catch (Exception e) {
							e.printStackTrace();
							BaseEnv.log.error("ClientLinkmanMemoryDayMgt delete:",e) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		return result;
	}
	

}
