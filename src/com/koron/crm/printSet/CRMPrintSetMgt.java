package com.koron.crm.printSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.bean.ClientModuleViewBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.ErrorCanst;
/**
 * 
 * <p>Title:CRM��ݵ���ӡ</p> 
 * <p>Description: </p>
 *
 * @Date:Jun 17, 2013
 * @Copyright: �����п���������޹�˾
 * @Author wyy
 */
public class CRMPrintSetMgt extends AIODBManager{
	/**
	 * ��ѯ���д�ӡģ��
	 * @return
	 */
		public Result queryPrintSet(String loginId){
			String sql = "from CRMPrintSetBean as bean where bean.createBy=? or isnull(bean.createBy,'')='' order by bean.status desc";
			ArrayList param = new ArrayList();
			param.add(loginId);
			System.out.println(sql);
			return list(sql, param);			
		}
		/**
		 * ���ش�ӡ���ñ�
		 * @param id
		 * @return
		 */
		public Result getPrintSet(String id){		
			return loadBean(id, CRMPrintSetBean.class);			
		}
		/**
		 * ���´�ӡ��
		 * @param bean
		 * @return
		 */
		public Result updatePrintSet(CRMPrintSetBean bean){		
			return updateBean(bean);			
		}
		/**
		 * ����
		 * @param bean
		 * @return
		 */
		public Result savePrintSet(CRMPrintSetBean bean){			
			return addBean(bean);			
		}
		/**
		 * ɾ��
		 * @param id
		 * @return
		 */
		public Result delPrintSet(String id){			
			return deleteBean(id, CRMPrintSetBean.class, "id");			
		}
		/**
		 * ��ѯ���õ�ģ��
		 * @return
		 */
		public Result byPrintSet(String loginId,String moduleId,String moduleViewId){
			String sql = "from CRMPrintSetBean as bean where bean.createBy=? and ref_moduleId = '"+moduleId+"' and ref_moduleViewId ='"+moduleViewId+"' or isnull(bean.createBy,'')='' order by bean.id asc";
			ArrayList param = new ArrayList();
			param.add(loginId);
			return list(sql, param);			
		}
		/**
		 * �ж������Ƿ��ظ�
		 * @param name
		 * @return
		 */
		public Result existName(String name){
			String sql = "from CRMPrintSetBean as bean where '1'=? and moduleName='"+name+"'";
			ArrayList param = new ArrayList();
			param.add("1");
			return list(sql, param);			
		}
		/**
		 * ���ݿͻ�Id��ȡ��Ϣ
		 * @param id
		 * @return
		 */
		public Result getPrintDet(String id){
			String sql = "select  C.id as clientId,C.address,C.clientName,C.clientNo,D.id as contectId,D.userName,D.mobile,D.telephone,D.clientEmail,C.province,C.city,C.area,C.moduleId" +
					" from crmclientinfo C left join  CRMClientInfoDet  D  on D.f_ref = C.id where 1=? and C.id='"+id+"'";
			ArrayList param = new ArrayList();
			param.add("1");
			return sqlList(sql, param);			
		}
		/**
		 * ����Id��ȡ��Ϣ
		 * @param id
		 * @return
		 */
		public Result getPtDet(final String id){
			final Result rst = new Result();
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn) throws SQLException {
							try {
								HashMap<String,String> map=new HashMap<String,String>();						
								String sql="select *  from crmclientinfo C left join  CRMClientInfoDet  D  on  C.id=D.f_ref where D.id=?";
								PreparedStatement pss = conn.prepareStatement(sql);
								pss.setString(1, id);
								ResultSet rss = pss.executeQuery();
								java.sql.ResultSetMetaData rsm=rss.getMetaData();
		                        int colCount=rsm.getColumnCount();
								while(rss.next()){
									for(int i=1;i<=colCount;i++){
										String columnName=rsm.getColumnName(i);
										String columnVal=rss.getString(columnName);
										if("null".equals(columnVal) || columnVal==null){
											columnVal="";
										}
										map.put(columnName,columnVal);
									}
								}
								rst.setRetVal(map);
								rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
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
		 * ����ģ��Id��ȡ��ģ���µ���ͼ
		 * @param moduleId
		 * @return
		 */
		public Result getView(String moduleId){
			String sql = "select id,viewName from CRMClientModuleview where moduleId=? ";
			ArrayList param = new ArrayList();
			param.add(moduleId);
			return sqlList(sql, param);			
		}
		/**
		 * ������ͼ
		 * @param id
		 * @return
		 */
		public Result loadViewFiled(String id){
			return loadBean(id, ClientModuleViewBean.class);
		}
		/**
		 * ����ģ��
		 * @param id
		 * @return
		 */
		public Result loadModule(String id){
			return loadBean(id, ClientModuleBean.class);
		}
		
		/**
		 * ���ز�����
		 * @param id
		 * @return
		 */
		public Result loadPrintSet(String id){
			return loadBean(id, CRMPrintSetBean.class);
		}
		
		/**
		 * ����ģ��Id����ͼIdȷ����������
		 * @param moduleId
		 * @param moduleViewId
		 * @return
		 */
		public Result getViewParam(String moduleId,String moduleViewId){
			String sql = "select C.tableInfo,M.pageFields " +
					"from crmClientModule C left join crmClientModuleView M on C.id = M.moduleId  where C.id=? and  M.id=?";
			ArrayList param = new ArrayList();
			param.add(moduleId);
			param.add(moduleViewId);
			return sqlList(sql, param);	
		}
}
