package com.menyi.aio.web.certificate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.ColDisplayBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.sysAcc.SettleCostMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

public class GenCertificateMgt extends DBManager {

	public Result genCertificate(final String userId,final String SCompanyID,final String  departmentCode, final String tempNumber, final String keyIds, final String certificateType,final MessageResources resources,final Locale locale) {
		final ArrayList<String> idList = new ArrayList<String>();
		//°´µ¥
		for (String id : keyIds.split("#\\|\\|#")) {
			if (!isNull(id)) {
				idList.add(id.split("#;#")[0]);
			}
		}
		 
		
		final Result rs = new Result();
		rs.retVal = "";

		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						CallableStatement cs = null;
						try {
							if(idList.size()==1 || "1".equals(certificateType)){ 
								for (String id : idList) { 
									DynDBManager dyn = new DynDBManager();
									Result rr =dyn.genCertificate(userId,tempNumber,null, id, conn, resources, locale,false);
									if(rr.retCode != ErrorCanst.DEFAULT_SUCCESS){
										rs.retCode = rr.retCode;
										rs.retVal = rr.retVal;
										break;
									}								
								}
							}else{
								DynDBManager dyn = new DynDBManager();
								Result rr =dyn.genAllCertificate(userId, SCompanyID, departmentCode, tempNumber, idList, conn, resources, locale);
								if(rr.retCode != ErrorCanst.DEFAULT_SUCCESS){
									rs.retCode = rr.retCode;
									rs.retVal = rr.retVal;
								}		
							}
						} catch (Exception ex) {
							BaseEnv.log.error("GenCertificateMgt-------genCertificate", ex);
							rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
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

	private boolean isNull(String str) {
		return str == null || str.trim().length() == 0;
	}
}
