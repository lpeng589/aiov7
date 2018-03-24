package com.koron.crm.clientTransfer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import sun.security.action.GetLongAction;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConfirmBean;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.NotifyFashion;
import com.sun.corba.se.impl.copyobject.FallbackObjectCopierImpl;

/**
 * 
 * <p>
 * Title:客户移交数据库操作类
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:Oct 23, 2012
 * @Copyright: 深圳市科荣软件有限公司
 * @Author wyy
 */
public class CRMClientTransferMgt extends AIODBManager {
	/**
	 * 查询
	 * 
	 * @param lvForm
	 * @param loginId
	 * @param id
	 * @return
	 */
	public Result queryClientInfo(CRMClientTransferForm lvForm, String loginId,
			String id,String sortInfo) {

		String sql = "select T.Id,I.ClientName,I.ClientNo,I.Status,I.ClientType,T.TransferMan,T.TransferDate,T.statusId,T.finishTime,"
				+ " T.Audit,T.TransferTo,(select BusinessDistrict from CRMBusinessDistrict where id=I.businessDistrict ) as BusinessDistrict,"
				+ "I.ClientLabel,T.clientId from CRMClientTransfer T left join  CRMClientInfo I  on T.clientId=I.id  where 1=? ";
		if (!"1".equals(loginId)) {
			sql += " and  T.TransferTo='" + loginId + "'";
		} else {
			if (lvForm.getTransferTo() != null && !"".equals(lvForm.getTransferTo())) {
				String[] transferTo = lvForm.getTransferTo().split(",");
				sql += " and  T.TransferTo in (''" ;
				for (String key : transferTo) {
					sql += ",'"+key+"'";
				}
				sql += ")";
			}
		}
		if (lvForm.getTransMan() != null && !"".equals(lvForm.getTransMan())) {
			String[] transferMan = lvForm.getTransMan().split(",");
			sql += " and  T.TransferMan in (''" ;
			for (String key : transferMan) {
				sql += ",'"+key+"'";
			}
			sql += ")";
		}		
		if (lvForm.getAudit() != null && !"".equals(lvForm.getAudit()) && !"All4".equals(lvForm.getAudit())) {
			sql += " and  T.Audit=" + lvForm.getAudit();
		}else if(!"All4".equals(lvForm.getAudit())){
			sql += " and  T.Audit='0'";
		}
		if (lvForm.getBeginTime() != null && !"".equals(lvForm.getBeginTime())) {
			sql += " and  T.TransferDate>='" + lvForm.getBeginTime() + "'";
		}
		if (lvForm.getEndTime() != null && !"".equals(lvForm.getEndTime())) {
			sql += " and  T.TransferDate<='" + lvForm.getEndTime() + "'";
		}
		if (lvForm.getClientStatus() != null
				&& !"".equals(lvForm.getClientStatus())) {
			sql += " and  I.Status=" + lvForm.getClientStatus();
		}
		if (lvForm.getClientStyle() != null
				&& !"".equals(lvForm.getClientStyle())) {
			sql += " and  I.ClientType=" + lvForm.getClientStyle();
		}
		if (lvForm.getClientName() != null
				&& !"".equals(lvForm.getClientName())) {
			sql += " and  I.ClientName like '%" + lvForm.getClientName() + "%'";
		}
		if (id != null && !"".equals(id)) {
			sql += " and  T.id in(" + id + ")";
		}
		if (lvForm.getStatusId() != null && !"".equals(lvForm.getStatusId())) {
			sql += " and  T.StatusId='" + lvForm.getStatusId() + "'";
		}		
		if(sortInfo == null || "".equals(sortInfo)){
			sql += " order by T.transferDate desc";
		}else{
			sql += " order by "+sortInfo.split(",")[1] + " " + sortInfo.split(",")[0];
		}	
		
		ArrayList param = new ArrayList();
		param.add("1");
		System.out.println(sql);
		return sqlList(sql, param, lvForm.getPageSize(), lvForm.getPageNo(),
				true);
	}

	/**
	 * 接收或拒绝
	 * 
	 * @param lvForm
	 * @param loginId
	 * @param keyId
	 * @param flag
	 * @return
	 */
	public Result insertResult(final Hashtable allTables, final String[] keyIds,
			final LoginBean loginBean, final MessageResources resources,
			final Locale local,final String sqlName) {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {

							// 执行表自定义语句
							DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get(sqlName);

							if (defineSqlBean == null) {
								BaseEnv.log
										.error("Define Sql Not Exist :Name = "
												+ sqlName);
								result.setRetCode(ErrorCanst.RET_DEFINE_SQL_NAME);
							}else{
								for(String keyId : keyIds){
									HashMap values = new HashMap();
									values.put("id", keyId);
									values.put("CRMClientTransfer_id", keyId);
									Result ret = defineSqlBean.execute(conn, values,loginBean.getId(), resources, local, "");
									result.setRetCode(ret.getRetCode());
									result.setRetVal(ret.getRetVal());
								}
							}
						} catch (Exception ex) {
							BaseEnv.log
									.error(
											"CRMClientTransferMgt insertResult mehtod:",
											ex);
							ex.printStackTrace();
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

	public Result getIdByName(String fullname) {
		String sql = "select id from tblemployee where 1=? and empfullname like '%"+ fullname + "%'";
		ArrayList param = new ArrayList();
		param.add("1");
		return sqlList(sql, param);
	}
}
