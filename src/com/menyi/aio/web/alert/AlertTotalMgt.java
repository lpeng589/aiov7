package com.menyi.aio.web.alert;

import java.sql.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.web.login.*;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.aio.web.role.RoleMgt;
import com.menyi.aio.web.userFunction.ReportData;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:预警汇总Mgt
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-08-15 上午 11:30
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class AlertTotalMgt extends AIODBManager {
	
	
	/**
	 * 查询所有数据
	 * 
	 * 1.先查询系统预警表中启用的预警
	 * 2.根据预警调用自定义解析报表的代码获取行数
	 * 
	 * @return
	 */
	public Result queryData(final HttpServletRequest request,final LoginBean lg,final String locale){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							ResultSet rset = null;
							Statement st = null;
							
							/* 查询启用的预警设置 */
							StringBuffer sql=new StringBuffer("SELECT tblSysAlert.id,tblSysAlert.SqlDefineName,l."+locale+" as alertName,tblReports.sqlFileName,");
							sql.append("tblSysAlert.modelId,isnull(tblSysAlert.condition,'') AS condition,tblLanguage."+locale+" as modelName,isnull(tblReports.BillTable,'') as BillTable, ");
							sql.append(" isnull((select top 1 ReportNumber from tblReports as report where report.endClassNumber=tblReports.reportNumber),'') as mainNumber ");
							sql.append(" FROM tblSysAlert LEFT JOIN tblLanguage l on l.id=tblSysAlert.alertName");
							sql.append(" LEFT JOIN tblReports ON tblReports.reportNumber=tblSysAlert.modelId ");
							sql.append(" LEFT JOIN tblLanguage on tblLanguage.id=tblReports.reportName");
							sql.append(" WHERE tblSysAlert.Status=0 and tblSysAlert.isHidden=0 ");
							st = conn.createStatement();
							rset = st.executeQuery(sql.toString());
							List list = new ArrayList();
							while(rset.next()){
								HashMap<String,String> map = new HashMap<String,String>();
								map.put("id", rset.getString("id"));
								map.put("sqlDefineName", rset.getString("SqlDefineName"));
								map.put("alertName", rset.getString("alertName"));
								map.put("sqlFileName", rset.getString("sqlFileName"));
								map.put("reportNumber", rset.getString("modelId"));
								map.put("condition", rset.getString("condition"));
								map.put("modelName", rset.getString("modelName"));
								map.put("BillTable", rset.getString("BillTable"));
								map.put("mainNumber", rset.getString("mainNumber"));
								list.add(map);
							}
							result.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							BaseEnv.log.error("AlertTotalMgt queryData:",ex) ;
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.setRetCode(retCode);
		
		List list = (ArrayList)result.getRetVal();
		RoleMgt roleMgt = new RoleMgt();
		
		List newList = new ArrayList();
		
		/* 根据启用的预警查询指定的行数 */
		for(int y = 0;y<list.size();y++){
			HashMap<String,String> map = (HashMap<String,String>)list.get(y);
			
			HttpServletRequest requests = request;
			requests.removeAttribute("parameterMap");
			requests.setAttribute("sysAlert", "true");
			
			/* 当有搜索条件时，给条件附值 */
			String condition = map.get("condition");
	    	
	        AlertCenterMgt mgt=new AlertCenterMgt();
	        /**
			 * 获取解析后的sql语句和处理后的参数等一些数据
			 */
			ReportSetMgt setMgt = new ReportSetMgt();
			ReportsBean reportSetBean = (ReportsBean) setMgt.getReportSetInfo(map.get("reportNumber").toString(), "zh_CN").getRetVal();
			Result rsData;
			try {
				rsData = mgt.showData(map.get("reportNumber").toString(),condition);
				if (rsData.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					int count=((ArrayList)rsData.retVal).size();
					count=count>0&&reportSetBean.getReportType().equals("PROCLIST")?count-1:count;
					map.put("count",String.valueOf(count));
				}
				
		        int count = Integer.parseInt(map.get("count"));
		        if(count>0){
		        	newList.add(map);
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		result.setRetVal(newList);
		return result;
	}
	
}
