package com.koron.oa.communicationNote;

import java.sql.*;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:�ҵ�ͨѶ¼Mgt
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-11-14 ���� 16��28
 * @Copyright: �������
 * @Author fjj
 * @preserve all
 */
public class CommunicationNoteMgt extends AIODBManager{
	
	/**
	 * ��ѯ���в�������
	 * @return
	 */
	public Result toTreeDeptList(){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select DeptCode,DeptFullName,classCode,isCatalog from tblDepartment where classCode is not null and statusId=0 order by classCode ";
							Statement st = conn.createStatement();
							ResultSet rs = st.executeQuery(sql.toString());
							List deptList = new ArrayList();
							while(rs.next()){
								HashMap map=new HashMap();
	                        	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
	                        		Object obj=rs.getObject(i);
	                        		if(obj==null){
	                        			map.put(rs.getMetaData().getColumnName(i), "");
	                        		}else{
	                        			map.put(rs.getMetaData().getColumnName(i), obj);
	                        		}
	                        	}
	                        	deptList.add(map);
							}
							result.setRetVal(deptList);
						} catch (Exception e) {
							e.printStackTrace();
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
	
	/**
	 * ��ѯְԱ��¼
	 * @param searchForm
	 * @param locale
	 * @return
	 */
	public Result queryList(final CommunicationNoteSearchForm searchForm,String locale){
		String sql = "select a.id,a.empNumber,a.empFullName,a.email,a.mobile,a.tel,d.deptFullName,a.photo,d.classCode,l."+locale+",";
		sql += "ROW_NUMBER() over(order by a.empNumber) as row_id from tblemployee a left join ";
		sql += "(select enumitem.enumValue,enumitem.languageId from tblDBEnumerationItem enumitem,";
		sql += "tblDBEnumeration enum where enumitem.enumId=enum.id and enum.enumName='duty') as ss on ss.enumValue=a.titleId";
		sql += " left join tblLanguage as l on l.id=ss.languageId left join ";
		sql += "tblDepartment d on a.departmentCode=d.classCode where a.SCompanyID='00001' ";
		
		if(searchForm.getSearchType() != null && "group".equals(searchForm.getSearchType())){
			//������ţ���ʾ�˲��ŵ�ְԱ
			sql += " and a.departmentCode like '"+searchForm.getSearchValue()+"%'";
		}else if(searchForm.getSearchType() != null && "keyWord".equals(searchForm.getSearchType())){
			//�ؼ��ֲ�ѯ
			sql += " and (a.empNumber like '%"+searchForm.getSearchValue()+"%'";
			sql += " or a.empFullName like '%"+searchForm.getSearchValue()+"%'";
			sql += " or a.empFullNamePYM like '%"+searchForm.getSearchValue()+"%'";
			sql += " or d.deptFullName like '%"+searchForm.getSearchValue()+"%')";
		}else if(searchForm.getSearchType() != null && "letter".equals(searchForm.getSearchType())){
			//���պ��ֵ���ĸ��ѯ
			if(!"0".equals(searchForm.getSearchValue())){
				sql += " and (a.empFullName "+ edhSql(searchForm.getSearchValue(), "tblEmployee", "empFullName")+" or a.empFullName like '"+searchForm.getSearchValue()+"%')";
			}
		}
		if(searchForm.getEmpStatus() != null && "leave".equals(searchForm.getEmpStatus())){
			//��ְԱ��
			sql += " and a.statusId=-1";
		}else{
			//Ĭ��������ְԱ��
			sql += " and a.statusId=0";			
		}
		
		return sqlListMaps(sql, null, searchForm.getPageNo(), searchForm.getPageSize());
	}
	
	 /**
	 * ����ID�鿴�Ƿ�����ϵ�������Ϣ
	 * @param moduleId
	 * @return
	 */
	public Result findContactByClientId(String clientId,String fieldName){
		String sql = "SELECT "+fieldName+" FROM tblEmployee WHERE id='"+clientId+"' and "+fieldName+" !=''";
		return this.sqlList(sql,new ArrayList());
	}
	
	/**
     * ���ݿͻ�Id��ѯ��ϵ����Ϣ
     * @param keyIds �ͻ���Id
     * @return
     */
    public Result selectClientDetById(final String[] keyIds,String msgType) {    	
    	String temp="";
    	for(int i=0;i<keyIds.length;i++){    		
    		temp+="'"+keyIds[i]+"',";
    	}
    	if(temp.endsWith(","))temp=temp.substring(0,temp.length()-1);
    	final String keyTemp=temp;
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select empFullName as Name,mobile as Phone,Email,id from tblEmployee where id in("+keyTemp+")  " ;
							PreparedStatement ps = conn.prepareStatement(sql) ;
							ResultSet rss = ps.executeQuery() ;
							ArrayList<String[]> strList = new ArrayList<String[]>() ;
							while(rss.next()){
								String[] arrayStr = new String[5] ;
								arrayStr[0] = rss.getString("Name") ;
								arrayStr[1] = rss.getString("Phone") ;
								arrayStr[2] = rss.getString("Email") ;
								arrayStr[3] = "";
								arrayStr[4] = rss.getString("id") ;
								strList.add(arrayStr) ;
							}
							rs.setRetVal(strList) ;
						} catch (SQLException ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace() ;
							BaseEnv.log.error("AddressListMgt---selectClientDetById method :"+ ex);
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
	 * ��ĸ��ѯ���÷���
	 * @param value		ֵ
	 * @param tableName		����
	 * @param tableField	�����ĸ��ֶ�����ѯ
	 * @return
	 */
	public String edhSql(String value, String tableName, String tableField){
		String sql = " in (select e."+tableField+" from "+tableName+" e," +
				"(select   '߹ '   chr, 'A '   letter   union   all   select   '�� ', 'B '   union   all"+
		        " select   '�� ', 'C '   union   all   select   '�� ', 'D '   union   all "+
		        " select   '�� ', 'E '   union   all   select   '�� ', 'F '   union   all "+
		        " select   '� ', 'G '   union   all   select   '�� ', 'H '   union   all "+
		        " select   'آ ', 'J '   union   all   select   '�� ', 'K '   union   all "+
		        " select   '�� ', 'L '   union   all   select   '�` ', 'M '   union   all "+
		        " select   '�� ', 'N '   union   all   select   '�� ', 'O '   union   all "+
		        " select   '�r ', 'P '   union   all   select   '�� ', 'Q '   union   all "+
		        " select   '�� ', 'R '   union   all   select   '�� ', 'S '   union   all "+
		        " select   '�� ', 'T '   union   all   select   '�� ', 'W '   union   all "+
		        " select   'Ϧ ', 'X '   union   all   select   'Ѿ ', 'Y '   union   all "+
		        " select   '�� ', 'Z ')   b where b.chr <=left(e."+tableField+",1) group by e."+tableField + 
		        " having   max(letter)= '"+value+"')";
		return sql;
	}
	
	
}
