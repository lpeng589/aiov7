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
 * Title:Ԥ������Mgt
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-08-15 ���� 11:30
 * @Copyright: �������
 * @Author fjj
 * @preserve all
 */
public class AlertTotalMgt extends AIODBManager {
	
	
	/**
	 * ��ѯ��������
	 * 
	 * 1.�Ȳ�ѯϵͳԤ���������õ�Ԥ��
	 * 2.����Ԥ�������Զ����������Ĵ����ȡ����
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
							
							/* ��ѯ���õ�Ԥ������ */
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
		
		/* �������õ�Ԥ����ѯָ�������� */
		for(int y = 0;y<list.size();y++){
			HashMap<String,String> map = (HashMap<String,String>)list.get(y);
			
			HttpServletRequest requests = request;
			requests.removeAttribute("parameterMap");
			requests.setAttribute("sysAlert", "true");
			
			/* ������������ʱ����������ֵ */
			String condition = map.get("condition");
	    	
	        AlertCenterMgt mgt=new AlertCenterMgt();
	        /**
			 * ��ȡ�������sql���ʹ����Ĳ�����һЩ����
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
