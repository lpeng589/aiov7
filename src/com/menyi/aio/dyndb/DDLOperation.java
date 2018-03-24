package com.menyi.aio.dyndb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.web.customize.CustomizeAction;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ConvertToSql;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.TestRW;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public class DDLOperation extends DBManager {

    /**
         * �������ʱ������ñ������ɾ���ñ��ñ�������������
         * @param execddl String
         * @return Result
         */
        public Result deleteExistsTable(final String execddl,final String path)
       {
           final Result rs = new Result();
           int retVal = DBUtil.execute(new IfDB() {
               public int exec(Session session) {
                   session.doWork(new Work() {
                       public void execute(Connection connection) throws
                           SQLException {
                           Connection conn = connection;
                           try {
                               Statement cs = conn.createStatement();
                               BaseEnv.log.debug(execddl);
                               cs.execute(execddl);
                               //����Sql�ű�
                              TestRW.saveToSql(path,execddl);
                           } catch (SQLException ex) {

                               if (ex.getErrorCode() == 2714) {
                                   rs.setRetCode(ErrorCanst.
                                                 RET_TABLENAME_EXIST_ERROR);
                               } else {
                                   rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                               }
                               BaseEnv.log.error("delete Table Error :" + execddl,
                                                 ex);
                           }

                           updateRefreshTime("tableInfo",conn); //ˢ�±�ʱ��
                       }
                   });
                    return rs.getRetCode();
                }
           });
           rs.retCode = retVal;
            return rs;
   }
    /**
     * ���ݱ���Ϣ��������
     * create table tblDBEnumerationDisplay (id int identity not null,  referId int null, primary key (id))
     * @param allTables Hashtable �������б������
     * @param tableInfo DBTableInfoBean ����Ҫ����ı������
     * @return Result
     */
    public Result create(final Hashtable allTables,
                         final DBTableInfoBean tableInfo,final String path) { 
        HashMap nameMap = new HashMap();
        //����Ϊ�û���ϵͳ���������ⶨ��
        tableInfo.setUdType(DBTableInfoBean.USER_TYPE);
        final Result rs = new Result();
        String ddl = " create table " + tableInfo.getTableName() +
                     " ( ";
        if(tableInfo.getTableType() == DBTableInfoBean.CHILD_TABLE){
        	//��ϸ������Ϊ������
        	ddl += "id bigint not null IDENTITY (1, 1)";
        }else {
        	ddl += "id varchar(30) not null ";
        }

        //��Ϊ������������,��ֹ���������ô���
        if (tableInfo.getTableType() == DBTableInfoBean.MAIN_TABLE) {
            tableInfo.setPerantTableName("");
        }

        //����Ҫ�����ı��Ƿ����
        Iterator it = allTables.values().iterator();
        while (it.hasNext()) {
            DBTableInfoBean tb = (DBTableInfoBean) it.next();
            if (tb.getTableName().equalsIgnoreCase(tableInfo.getTableName())) {
                rs.setRetCode(ErrorCanst.RET_TABLENAME_EXIST_ERROR);
                return rs;
            }
        }
        
        ArrayList defConstraints=new ArrayList();
        for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
            DBFieldInfoBean fieldInfo = (DBFieldInfoBean) tableInfo.
                                        getFieldInfos().get(i);
            String fieldName = fieldInfo.getFieldName();

            //�ж������Ƿ��ظ�
            if (nameMap.get(fieldName) == null) {
                nameMap.put(fieldName, fieldName);
            } else {
                //�����ظ�
                rs.setRetCode(ErrorCanst.RET_FIELD_EXIST_ERROR);
                rs.setRetVal(fieldName) ;
                return rs;
            }
            if(fieldInfo.getInputType()!=DBFieldInfoBean.INPUT_CUT_LINE){ //�ָ��������ǲ��������ֶε�
	            //ȡ�ֶ����ͺͣ�Ĭ��ֵ
	            String fieldType = getTypeStr(fieldInfo);
	            
	
	            String fieldNull = fieldInfo.getIsNull() == DBFieldInfoBean.IS_NULL ?
	                               "null" : "not null";
	            ddl += "," + fieldName + " " + fieldType + " " + fieldNull;
            }

        }
        ddl += ")\n" ;
        if (tableInfo.getTableType() == DBTableInfoBean.CHILD_TABLE) {
        	ddl += "CREATE CLUSTERED INDEX Inx_"+tableInfo.getTableName()+"_f_ref ON "+tableInfo.getTableName()+"(f_ref,id) \n";
        }else if (tableInfo.getTableType() == DBTableInfoBean.BROTHER_TABLE){
        	ddl += "CREATE CLUSTERED INDEX Inx_"+tableInfo.getTableName()+"_f_ref ON "+tableInfo.getTableName()+"(f_brother,id) \n";
        }
        ddl +=" alter   table   "+tableInfo.getTableName()+" add   constraint   PK_"+tableInfo.getTableName()+"  primary   key   (id)\n";
        String contranst = "";
        //�����ӱ����뽨�������ϵ��
        //alter table tblDBFieldInfo add constraint FK301ECF6C21672895 foreign key (tableId) references tblDBTableInfo
        if (tableInfo.getTableType() == DBTableInfoBean.CHILD_TABLE) {
            String parentTableName = tableInfo.getPerantTableName();
            //����Լ������
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
            String[] parentTableNames=parentTableName.split(";");
            if(parentTableNames.length==1){
	            for(int i=0;i<parentTableNames.length;i++){
		            String fk = "FK_f_ref_id" + sdf.format(new Date());
		            contranst = "alter table " + tableInfo.getTableName() +
	                        " add constraint " + fk +
	                        " foreign key (f_ref) references " +
	                        parentTableNames[i] +
	                        " ON DELETE CASCADE ON UPDATE CASCADE ";
	            }
            }
        } else if (tableInfo.getTableType() == DBTableInfoBean.BROTHER_TABLE) {
            String parentTableName = tableInfo.getPerantTableName();
            //����Լ������
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
            String[] parentTableNames=parentTableName.split(";");
            if(parentTableNames.length==1){
	            for(int i=0;i<parentTableNames.length;i++){
		            String fk = "FK_f_ref_id" + sdf.format(new Date());
		            contranst = "alter table " + tableInfo.getTableName() +
	                        " add constraint " + fk +
	                        " foreign key (f_brother) references " +
	                        parentTableNames[i] +
	                        " ON DELETE CASCADE ON UPDATE CASCADE ";
	            }
            }
        }

        //�Զ�����ID�ֶε�fieldInfo��
        DBFieldInfoBean idField = new DBFieldInfoBean();
        
        idField.setId(IDGenerater.getId());
        idField.setFieldName("id");
        if(tableInfo.getTableType() == DBTableInfoBean.CHILD_TABLE){
        	//�����ϸ��id����������������
        	idField.setFieldType(DBFieldInfoBean.FIELD_INT);
        }else{
        	idField.setFieldType(DBFieldInfoBean.FIELD_ANY);
        }
        
        idField.setIsNull(DBFieldInfoBean.NOT_NULL);
        idField.setInputType(DBFieldInfoBean.INPUT_NO) ;
        idField.setMaxLength(30);
        idField.setListOrder((byte) 0);
        idField.setTableBean(tableInfo);
        idField.setUdType(DBFieldInfoBean.USER_TYPE);
        tableInfo.getFieldInfos().add(idField);
        if(tableInfo.getIsView()==1){
        	ddl = "";
        	contranst="";
        	defConstraints.clear();
        }
        final String execddl = ddl;
        final String execContranst = contranst;
        final ArrayList execDFCons=defConstraints;
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
	            
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try {
                        	Statement cs = conn.createStatement();
                        	if(tableInfo.getIsView()==0){
	                            
	                            BaseEnv.log.debug(execddl);
	                            cs.execute(execddl);
	                            if (execContranst != null &&
	                                execContranst.trim().length() > 0) {
	                                Statement cs2 = conn.createStatement();
	                                BaseEnv.log.debug(execContranst);
	                                cs2.execute(execContranst);
	                            }
	                            if (execDFCons != null &&
	                            		execDFCons.size() > 0) {
	                                Statement cs3 = conn.createStatement();
	                                for(int i=0;i<execDFCons.size();i++){
	                                BaseEnv.log.debug(execDFCons.get(i));
	                                cs3.execute(execDFCons.get(i).toString());
	                                }
	                            }
	                            //����sql�ı�
	                           String sqlTime=BaseDateFormat.format(new Date(),
	                                   BaseDateFormat.yyyyMMddHHmmss);
	                           String sql="--"+sqlTime+":�½���\nif exists(select * from sysobjects where name='"+tableInfo.getTableName()+"')\nbegin\n"+
	                           	     "delete from tblLanguage where id in (select f.languageId from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = '"+tableInfo.getTableName()+"') \n"+
	                           	     "delete from tblLanguage where id in (select f.groupName from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = '"+tableInfo.getTableName()+"') \n"+
	                           	     "delete from tblLanguage where id in (select t.languageId from tblDBTableInfo t where t.tableName = '"+tableInfo.getTableName()+"') \n" +
	                           	     		"exec proc_deleteExistsTable @tableName='"+tableInfo.getTableName()+"'\ndrop table "+tableInfo.getTableName()+"\nend\ngo\n";
	                           String lastSql=sql+execddl+"\n"+execContranst+"\n";
	                           for(int j=0;j<execDFCons.size();j++){
	                        	   lastSql+=execDFCons.get(j)+"\n";
	                           }
	                           TestRW.saveToSql(path,lastSql);
	                           
	                        }
                           //����ִ�ж����Բ���  �������ֶα�ʶ����ΪBillNo ���ݱ��ʱ����tblBillNo���һ������
                           KRLanguageQuery.saveToDB(tableInfo.getDisplay().map, tableInfo.getDisplay().getId(), connection);
                           for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
                               DBFieldInfoBean lfi = (DBFieldInfoBean)tableInfo.getFieldInfos().get(i);
                               if(lfi.getDisplay() != null){
                                   KRLanguageQuery.saveToDB(lfi.getDisplay().map, lfi.getDisplay().getId(), connection);
                               }
                               if(lfi.getGroupDisplay() != null){
                                   KRLanguageQuery.saveToDB(lfi.getGroupDisplay().map, lfi.getGroupDisplay().getId(), connection);
                               }
                               
                               //fjj �ж��ֶα�ʶ�Ƿ��ǵ��ݱ�ţ������һ������
                               if(lfi.getFieldIdentityStr()!=null && "BillNo".equals(lfi.getFieldIdentityStr())){
                            	   //��������ʱ
                            	   String sqlBillNo = "INSERT INTO tblbillno([key],billName,pattern,billNO,start,step,isfillback,[reset],laststamp,explain,isAddbeform) ";
                            	   sqlBillNo += "VALUES('"+tableInfo.getTableName()+"_"+lfi.getFieldName()+"','"+(tableInfo.getDisplay()==null?tableInfo.getLanguageId():tableInfo.getDisplay().get("zh_CN"))+"_"+(lfi.getDisplay()==null?lfi.getLanguageId():lfi.getDisplay().get("zh_CN"))+"','SS{date yy}{date MM}{date dd}{serial 0000}',0,1,1,0,2,'"+System.currentTimeMillis()+"','SS��������������ˮ�Ÿ�ʽ[0000]',1)";
                            	   cs.executeUpdate(sqlBillNo);
                               }
                           }
                        } catch (SQLException ex) {
                        	System.out.println("ss:"+ex.getErrorCode());
                            if (ex.getErrorCode() == 2714) {
                                rs.setRetCode(ErrorCanst.RET_TABLENAME_EXIST_ERROR);
                            }else if (ex.getErrorCode() == 102) {
                                rs.setRetCode(ErrorCanst.RET_FIELD_CANNOTSPEC_ERROR);
                            } else {
                                rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            }
                            BaseEnv.log.error("create Table Error :" + execddl,
                                              ex);
                        }
                        updateRefreshTime("tableInfo",conn); //ˢ�±�ʱ��
                    }
                });
	           

                //д�����ݿ�
                if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
                    Result rtable = addBean(tableInfo, session);
                    rs.setRetCode(rtable.getRetCode());
                }
                return rs.getRetCode();
            }
        });

        rs.retCode = retVal;
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            allTables.put(tableInfo.getTableName(), tableInfo);
            //����ű�
            
	          ConvertToSql.saveSqlByTableInfo(tableInfo,path);
	          ConvertToSql.saveSqlByTableDisplay(tableInfo,path);
	          ConvertToSql.saveSqlByDBField(tableInfo,path);
	          ConvertToSql.saveSqlByFieldDisplay(tableInfo,path);

        }

        return rs;
    }

    public static ArrayList<DBTableInfoBean> getChildTables(final String tableName,
                                           final Map allTables) {
        if (tableName == null || tableName.length() == 0) {
            return null;
        }
        ArrayList<DBTableInfoBean> list = new ArrayList<DBTableInfoBean>();
        //���ӱ�
        Iterator it = allTables.values().iterator();
        while (it.hasNext()) {
            DBTableInfoBean ti = (DBTableInfoBean) it.next();
            if (ti.getTableType() == DBTableInfoBean.CHILD_TABLE&&ti.getPerantTableName()!=null&&ti.getPerantTableName().length()>0 && ti.getIsUsed()!=0) {
            	String []parentNames=ti.getPerantTableName().split(";");
            	boolean isExist=false;
            	for(int i=0;i<parentNames.length;i++){
            		if(parentNames[i].equals(tableName)){
            			isExist=true;
            		}
            	}
            	if(isExist){
                list.add(ti);
            	}
            }
        }
        Collections.sort(list, new SortTableName()) ;
        return list;
    }

    /**
	 * �ӱ�tableName����
	 */
	public static class SortTableName implements Comparator{
		public int compare(Object o1, Object o2) {
			DBTableInfoBean tableInfo1 = (DBTableInfoBean) o1 ;
			DBTableInfoBean tableInfo2 = (DBTableInfoBean) o2 ;
			
			String tableName1 = tableInfo1.getTableName() ;
			String tableName2 = tableInfo2.getTableName() ;
			return tableName1.compareTo(tableName2) ;
		}
	}
	
    public static ArrayList getBrotherTables(final String tableName,
                                             final Map allTables) {
        if (tableName == null || tableName.length() == 0) {
            return null;
        }
        ArrayList list = new ArrayList();
        //���ӱ�
        Iterator it = allTables.values().iterator();
        while (it.hasNext()) {
            DBTableInfoBean ti = (DBTableInfoBean) it.next();
            if (ti.getTableType() == DBTableInfoBean.BROTHER_TABLE &&ti.getPerantTableName()!=null&&ti.getPerantTableName().length()>0) {
            	String []parentNames=ti.getPerantTableName().split(";");
            	boolean isExist=false;
            	for(int i=0;i<parentNames.length;i++){
            		if(parentNames[i].equals(tableName)){
            			isExist=true;
            		}
            	}
            	if(isExist){
                list.add(ti);
            	}
            }
        }
        return list;
    }

    /**<pre>
     * �ӱ��л�ȡ�������Ӧ�� {@link DBTableInfoBean}
     * ����Ҳ������Ӧ�ı����򷵻�null
     * </pre>
     * @param allTables ���б��MAP
     * @param tableName ����
     * @return ��ȡ������Ӧ��{@link DBTableInfoBean}
     * @see DBTableInfoBean
     */
    public static DBTableInfoBean getTableInfo(final Hashtable<String,DBTableInfoBean> allTables,
                                               final String tableName) {
        if (allTables == null || tableName == null || tableName.length() == 0) {
            return null;
        }
        return (DBTableInfoBean) allTables.get(tableName);
    }
	/**
	 * ���ݱ������ֶ����õ������ֶ�BEAN,
	 * ���������NULL�����ֶ�����NULL��������򷵻�null
	 * @param allTables 
	 * @param tableName ����
	 * @param fieldName �ֶ���
	 * @return ���ֶζ�Ӧ�� <b> {@link DBFieldInfoBean} </b>
	 * @see DBFieldInfoBean
	 */
	public static DBFieldInfoBean getFieldInfo(Hashtable<String, DBTableInfoBean> allTables, String tableName, String fieldName) {
		if (allTables==null || tableName==null || fieldName == null || !allTables.containsKey(tableName))
			return null;
		for (DBFieldInfoBean fi : allTables.get(tableName).getFieldInfos()) {
			if (fi.getFieldName().toLowerCase().equals(fieldName.toLowerCase())) {
				return fi;
			}
		}
		return null;
	}
	/**<pre>
	 * ����fieldName��ȡ���Ӧ��  {@link DBFieldInfoBean}
	 * fieldName�ĸ�ʽΪ <b>����.�ֶ���</b>,��:tblgoods���fullname�ֶ�дΪ tblgoods.fullname
	 * </pre>
	 * @param allTables ���б���
	 * @param fieldName �ֶΣ���ʽΪ������.�ֶ���
	 * @return ��Ӧ��{@link DBFieldInfoBean}
	 * @see DBFieldInfoBean
	 */
	public static DBFieldInfoBean getField(Hashtable<String,DBTableInfoBean> allTables, String fieldName) {
		int pos = -1;
		if(fieldName==null || (pos=fieldName.indexOf('.')) == -1)
			return null;
		return DDLOperation.getFieldInfo(allTables,fieldName.substring(0,pos),fieldName.substring(pos+1));
	}
	
	public Result deleteParentTable(final Hashtable allTables,final String parentTable,final DBTableInfoBean bean,final String path) {
		final Result rs = new Result();
		
		
		int retVal = DBUtil.execute(new IfDB() {
		    public int exec(Session session) {
		        session.doWork(new Work() {
		            public void execute(Connection connection) throws
		                SQLException {
		                Connection conn = connection;
		                try {
		                    //ɾ���ӱ����еĶ�����
		                	String oldP = bean.getPerantTableName();
		            		oldP = oldP.replaceFirst(parentTable+";", "");
		                	String sql =" update tblDBtableInfo set perantTableName=? where tableName=? ";
		                    PreparedStatement pst = conn.prepareStatement(sql);
		                    pst.setString(1, oldP);
		                    pst.setString(2, bean.getTableName());
		                    pst.executeUpdate();		
		                } catch (SQLException ex) {
		                    if (ex.getErrorCode() == 3726) {
		                        rs.setRetCode(ErrorCanst.
		                                      RET_EXIST_CHILD_TABLE_ERROR);
		                    } else {
		                        rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		                    }
		                    BaseEnv.log.error("deleteParentTable Error :" ,
		                                      ex);
		                }
		                updateRefreshTime("tableInfo",conn); //ˢ�±�ʱ��
		            }
		        });
		
		        
		
		        return rs.getRetCode();
		    }
		});
		
		rs.retCode = retVal;
		//ɾ���ڴ������
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			String oldP = bean.getPerantTableName();
    		oldP = oldP.replaceFirst(parentTable+";", "");
    		bean.setPerantTableName(oldP);
    		
		    String sqlTime=BaseDateFormat.format(new Date(),
		            BaseDateFormat.yyyyMMddHHmmss);
		    
		    String sql =
		        "--"+sqlTime+":ɾ����\n update tblDBtableInfo set perantTableName='"+oldP+"' where tableName='"+bean.getTableName()+"'  \n";
		    
		
		    TestRW.saveToSql(path, sql);
		}
		
		return rs;
	}	
	

    /**
     * ɾ����
     * @param allTables Hashtable
     * @param delTableId int
     * @return Result
     */
    public Result drop(final Hashtable allTables,
                       final String delTableName, final Hashtable tableViews,final String path) {
        final Result rs = new Result();
        DBTableInfoBean o = getTableInfo(allTables, delTableName);

        if (o == null) {
            //������
            rs.retCode = ErrorCanst.RET_TABLE_NOT_EXIST_ERROR;
            return rs;
        }

        //���Ҹñ��Ƿ�����Ӧ�ӱ�����,����дӱ�����ɾ����Ҫ���û���ɾ�ӱ���ɾ����
        DBTableInfoBean[] tableInfos = (DBTableInfoBean[]) allTables.values().
                                       toArray(new DBTableInfoBean[0]);
        for (int i = 0; i < tableInfos.length; i++) {
            if (tableInfos[i].getPerantTableName()!=null&&tableInfos[i].getPerantTableName().length()>0) {
            	String []parentNames=tableInfos[i].getPerantTableName().split(";");
            	boolean isExist=false;
            	for(int j=0;j<parentNames.length;j++){
            		if(parentNames[j].equals(delTableName)){
            			isExist=true;
            		}
            	}
            	if(isExist){
                rs.retCode = ErrorCanst.RET_EXIST_CHILD_TABLE_ERROR;
                return rs;
            	}
            }
        }

        //�ҵ�����

        final String delDDL =o.getIsView() == 1 ?"" : " drop table " + (o).getTableName();
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try {
                            //ɾ���ӱ����еĶ�����
                            String sql = "delete from tblLanguage where id in (select f.languageId from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = ?)";
                            PreparedStatement pst = conn.prepareStatement(sql);
                            pst.setString(1, delTableName);
                            pst.executeUpdate();
                            sql = "delete from tblLanguage where id in (select t.languageId from tblDBTableInfo t where t.tableName = ?)";
                            pst = conn.prepareStatement(sql);
                            pst.setString(1, delTableName);
                            pst.executeUpdate();
                            sql = "delete from tblLanguage where id in (select f.groupName from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = ?)";
                            pst = conn.prepareStatement(sql);
                            pst.setString(1, delTableName);
                            pst.executeUpdate();
                            //ɾ���ֶ���Ϣ
                            sql = "delete from tblDBFieldInfo where tableId in(select t.id from tblDBTableInfo t where t.tableName = ?)";
                            pst = conn.prepareStatement(sql);
                            pst.setString(1, delTableName);
                            pst.executeUpdate();
                            
                            //ɾ�����ݱ�Ź���
                            sql = "DELETE FROM tblBillNo WHERE [key] like '"+delTableName+"[_]%'";
                            System.out.println("sql:-------------------------------------"+sql);
                        	pst = conn.prepareStatement(sql);
                      	    pst.executeUpdate();
                            
                      	    if(delDDL != null && delDDL.length() > 0){
	                            Statement cs = conn.createStatement();
	                            BaseEnv.log.debug(delDDL);
	                            cs.execute(delDDL);
                      	    }


                        } catch (SQLException ex) {
                            if (ex.getErrorCode() == 3726) {
                                rs.setRetCode(ErrorCanst.
                                              RET_EXIST_CHILD_TABLE_ERROR);
                            } else {
                                rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            }
                            BaseEnv.log.error("drop Table Error :" + delDDL,
                                              ex);
                        }
                        updateRefreshTime("tableInfo",conn); //ˢ�±�ʱ��
                    }
                });

                //ɾ�����ݿ�
                if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
                    Result rtable = deleteBean(delTableName, DBTableInfoBean.class,
                                               "tableName", session);
                    rs.setRetCode(rtable.getRetCode());
                }

                return rs.getRetCode();
            }
        });

        rs.retCode = retVal;
        //ɾ���ڴ������
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            allTables.remove(o.getTableName());
            //ɾ���ɹ�,����sql�ı�

            String sqlTime=BaseDateFormat.format(new Date(),
                    BaseDateFormat.yyyyMMddHHmmss);
            
            String sql =
                "--"+sqlTime+":ɾ����\ndelete from tblLanguage where id in (select f.languageId from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = '" +
                delTableName + "') \n";
            sql += "delete from tblLanguage where id in (select f.groupName from tblDBFieldInfo f,tblDBTableInfo t where f.tableId = t.id and t.tableName = '" +
                delTableName + "') \n";
            sql += "delete from tblLanguage where id in (select t.languageId from tblDBTableInfo t where t.tableName = '" + delTableName +
                "') \n";

            sql += "exec proc_deleteExistsTable @tableName=" + delTableName +
                ";\ndrop table " + delTableName;
            TestRW.saveToSql(path, sql);
        }

        return rs;
    }
    public String getUpdateStr(Object src,Object des){
    	String ret ="" ;
		Field[] fds = src.getClass().getDeclaredFields();
		for(Field fd:fds){
			if(!Modifier.isFinal(fd.getModifiers())  ){
				String type = fd.getType().toString();
				if(type.endsWith("String")||type.endsWith("byte")||type.endsWith("int")||type.endsWith("short")){
					try {
						String name = fd.getName();  
						if("layoutHTML".equals(name)){ 
							continue;
						}
						Method md =src.getClass().getMethod("get"+name.substring(0,1).toUpperCase()+name.substring(1), null);
						if(md == null){
							continue;
						}
						Object srcVal = md.invoke(src, null);
						Object desVal = md.invoke(des, null);
						if((srcVal !=null && !srcVal.equals(desVal)) || (srcVal ==null && desVal != null)){
							//ֵ��ͬ
							if(desVal == null){
								ret += ","+name+"=null";
							}else{
								ret += ","+name+"='"+desVal+"'";
							}
						}
					}catch (Exception e) {
						BaseEnv.log.error("DDLOperation.getUpdateStr Error:",e);
					}
				}
			}
		}
    	return ret;
    }
    public String getUpdateLanguage(KRLanguage src,KRLanguage des){
    	String ret ="" ;
    	
    	//���id��һ��������ɾ���ϵģ������µ�
    	
    	if(src==null && des==null){
    		return "";
    	}else if(src == null){
    		ret += des.toAddSQL();
	    	return ret;
    	}else if(!src.getId().equals(des.getId())){
    		ret += "DELETE FROM tblLanguage WHERE ID ='"+src.getId()+"'\n";
    		ret += des.toAddSQL();
	    	return ret;
    	}
    	
    	
    	Iterator<String> it = src.map.keySet().iterator();	
    	//��ѯ��Щ�ֶη����仯
		while (it.hasNext()) {
			String key = it.next();
			if(!src.get(key).equals(des.get(key))){
				ret +=","+key+"='"+des.get(key)+"'";
			}
		}
		//��ѯ������ʲô����
		it = des.map.keySet().iterator();	
		while (it.hasNext()) {
			String key = it.next();
			if(src.get(key)==null||src.get(key).length()==0){
				ret +=","+key+"='"+des.get(key)+"'";
			}
		}
		if(ret.length() > 0){
			return "update tblLanguage set "+(ret.substring(1))+" where id='"+des.getId()+"'";
		}else{
			return "";
		}
		
    	
    }
    
    public String getFieldInsert(DBFieldInfoBean bean){
    	String fn ="" ;
    	String vl="";
    	Field[] fds = bean.getClass().getDeclaredFields();
		for(Field fd:fds){
			if(!Modifier.isFinal(fd.getModifiers())){
				String type = fd.getType().toString();
				if(type.endsWith("String")||type.endsWith("byte")||type.endsWith("int")||type.endsWith("short")){
					try {
						String name = fd.getName();
						Method md =bean.getClass().getMethod("get"+name.substring(0,1).toUpperCase()+name.substring(1), null);
						if(md == null){
							continue;
						}
						Object val = md.invoke(bean, null);
						fn += ","+name;
						vl +=",'"+(val==null?"":val)+"'";
					}catch (Exception e) {
						BaseEnv.log.error("DDLOperation.getUpdateStr Error:",e);
					}
				}
			}
		}
    	return "insert into tblDBFieldInfo(tableId"+fn+") values('"+bean.getTableBean().getId()+"'"+vl+")";
    }
    
    public static DBFieldInfoBean getFieldPYM(DBFieldInfoBean addFib){
    	KRLanguage lan = addFib.getDisplay();
    	DBFieldInfoBean fieldPYM = new DBFieldInfoBean();
    	fieldPYM.setId(CustomizeAction.getId(addFib.getId()));
    	fieldPYM.setFieldName(addFib.getFieldName()+"PYM");
    	fieldPYM.setDisplay(KRLanguageQuery.create(
    			lan.map.get("zh_CN")==null?"":lan.map.get("zh_CN")+"ƴ����", 
    			lan.map.get("en")==null?"":lan.map.get("en")+"PYM",
    			lan.map.get("zh_TW")==null?"":lan.map.get("zh_TW")+"ƴ���a"));
    	fieldPYM.setLanguageId(fieldPYM.getDisplay().getId());
    	if(addFib.getGroupName() != null && addFib.getGroupName().length() > 0){
    		lan = addFib.getGroupDisplay();
    		fieldPYM.setGroupDisplay(KRLanguageQuery.create(
    				lan.map.get("zh_CN")==null?"":lan.map.get("zh_CN"), 
    				lan.map.get("en")==null?"":lan.map.get("en"),
    				lan.map.get("zh_TW")==null?"":lan.map.get("zh_TW")));
        	fieldPYM.setGroupName(fieldPYM.getGroupDisplay().getId());
    	}
    	fieldPYM.setFieldType((byte)DBFieldInfoBean.FIELD_ANY);
    	fieldPYM.setMaxLength(30);
    	fieldPYM.setInputType(DBFieldInfoBean.INPUT_NORMAL);
    	fieldPYM.setIsNull(DBFieldInfoBean.IS_NULL);
    	fieldPYM.setWidth(100);
    	fieldPYM.setListOrder((short)((int)(addFib.getListOrder())));
    	fieldPYM.setUdType(DBFieldInfoBean.USER_TYPE);
    	
    	fieldPYM.setTableBean(addFib.getTableBean());
    	return fieldPYM;
    }

    /**
     * ��������ʲô����²�����ĵ��ֶ�tableName; tableType; perantTableId; associateFieldName;  udType;  updateAble;
     * ����������ֶΣ�ʲôʱ�������޸ġ�
     *
     * �ӱ�����ĵ���udType updateAble fieldType
     * @param allTables Hashtable
     * @param tableInfo DBTableInfoBean
     * @return Result
     */
    public Result alter(final DBTableInfoBean oldTableInfo,
                        final DBTableInfoBean tableInfo, final Hashtable tableViews,final String path,String locale) {
        HashMap nameMap = new HashMap();

        final Result rs = new Result();
        //Ҫ�����alter table�ű�
        if (oldTableInfo == null || tableInfo == null) {
            rs.retCode = ErrorCanst.RET_ILLEGAL_ARGUMENT_ERROR;
            rs.retVal = "Table is null ";
            return rs;
        }
        ArrayList logList = new ArrayList(); //��¼Ҫ���������־����Ϣ
        //�ҳ����ӵ��ֶ�
        ArrayList addList = new ArrayList();
        //���ֶ�
        ArrayList updateList = new ArrayList();
        ArrayList updateOldList = new ArrayList();
        
        final ArrayList alterList = new ArrayList(); //����Ҫ���µ����
        String contranst = "";
        if (tableInfo.getTableType() == DBTableInfoBean.CHILD_TABLE  &&tableInfo.getIsView() == 0) {        	
            String parentTableName = tableInfo.getPerantTableName();
            //����Լ������
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
            String[] parentTableNames=parentTableName.split(";");
            if(parentTableNames.length==1&& !parentTableName.equals(oldTableInfo.getPerantTableName())){
            	//������������ɾ�����ؽ�
            	contranst +="declare @name varchar(100) \n" +
            			" declare kdet cursor local for \n" +
            			" SELECT f.name AS foreign_key_name \n" +
            			" FROM sys.foreign_keys AS f INNER JOIN sys.foreign_key_columns AS fc ON f.object_id = fc.constraint_object_id \n" +
            			" WHERE f.parent_object_id = OBJECT_ID('"+tableInfo.getTableName()+"') and COL_NAME(fc.parent_object_id, fc.parent_column_id)='f_ref' \n" +
            			" open kdet fetch kdet into @name \n" +
            			" while(@@FETCH_STATUS=0) begin exec(' ALTER TABLE [dbo].["+tableInfo.getTableName()+"] DROP CONSTRAINT  '+@name); fetch kdet into @name end \n" +
            			" close kdet; \n";
	            for(int i=0;i<parentTableNames.length;i++){
	            	String fk = "FK_"+tableInfo.getTableName()+"_f_ref";
	            	contranst += "alter table " + tableInfo.getTableName() +
	                        " add constraint " + fk +
	                        " foreign key (f_ref) references " +
	                        parentTableNames[i] +
	                        " ON DELETE CASCADE ON UPDATE CASCADE ";
	            }
            }
        } else if (tableInfo.getTableType() == DBTableInfoBean.BROTHER_TABLE &&tableInfo.getIsView() == 0) {
            String parentTableName = tableInfo.getPerantTableName();
            //����Լ������
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
            String[] parentTableNames=parentTableName.split(";");
            if(parentTableNames.length==1 && !parentTableName.equals(oldTableInfo.getPerantTableName())){
            	//������������ɾ�����ؽ�
            	contranst +="declare @name varchar(100) \n" +
            			" declare kdet cursor local for \n" +
            			" SELECT f.name AS foreign_key_name \n" +
            			" FROM sys.foreign_keys AS f INNER JOIN sys.foreign_key_columns AS fc ON f.object_id = fc.constraint_object_id \n" +
            			" WHERE f.parent_object_id = OBJECT_ID('"+tableInfo.getTableName()+"') and COL_NAME(fc.parent_object_id, fc.parent_column_id)='f_ref' \n" +
            			" open kdet fetch kdet into @name \n" +
            			" while(@@FETCH_STATUS=0) begin exec(' ALTER TABLE [dbo].["+tableInfo.getTableName()+"] DROP CONSTRAINT  '+@name); fetch kdet into @name end \n" +
            			" close kdet; \n";
	            for(int i=0;i<parentTableNames.length;i++){
		            String fk = "FK_f_ref_id" + sdf.format(new Date());
		            contranst += "alter table " + tableInfo.getTableName() +
	                        " add constraint " + fk +
	                        " foreign key (f_brother) references " +
	                        parentTableNames[i] +
	                        " ON DELETE CASCADE ON UPDATE CASCADE ";
	            }
            }
        }
        if(contranst!= null && contranst.length() > 0){
        	alterList.add(contranst);
        }
        
        //���������µ��ֶ�
        String ret = getUpdateStr(oldTableInfo,tableInfo);
        if(ret.length() > 0){
        	ret = " update tblDBTableInfo set "+ret.substring(1) +" where tableName='"+tableInfo.getTableName()+"'";
        	alterList.add(ret);
        }
        //���������Ƿ��б仯
        ret = getUpdateLanguage(oldTableInfo.getDisplay(),tableInfo.getDisplay());
        if(ret.length() > 0){
        	alterList.add(ret);
        }
        

        for (int i = 0; i < tableInfo.getFieldInfos().size(); i++) {
            String tn = ((DBFieldInfoBean) tableInfo.getFieldInfos().get(i)).
                        getFieldName();
            boolean old = false;
            for (int j = 0; j < oldTableInfo.getFieldInfos().size(); j++) {
                String tn2 = ((DBFieldInfoBean) oldTableInfo.getFieldInfos().get(j)).
                	getFieldName();
                if (tn2.equals(tn)) {
                    old = true;
                    updateList.add(tableInfo.getFieldInfos().get(i));
                    updateOldList.add(oldTableInfo.getFieldInfos().get(j));
                    continue;
                }
            }
            if (!old) {
                addList.add(tableInfo.getFieldInfos().get(i));
            }
        }
        //ɾ�����ֶ�
        ArrayList delList = new ArrayList();
        for (int i = 0; i < oldTableInfo.getFieldInfos().size(); i++) {
            String tn = ((DBFieldInfoBean) oldTableInfo.getFieldInfos().get(i)).getFieldName();
            String name = ((DBFieldInfoBean) oldTableInfo.getFieldInfos().get(i)).getFieldName();
            if (!name.endsWith("PYM") && !name.equals("id")&&!name.equals("f_ref")&&!name.equals("f_brother")&&!name.equals("checkPersons")
            		&&!name.equals("classCode")&&!name.equals("workFlowNode")&&!name.equals("workFlowNodeName")&&!name.equals("createBy")&&!name.equals("lastUpdateTime")
            		&&!name.equals("SCompanyID")&&!name.equals("statusId")&&!name.equals("lastUpdateBy")&&!name.equals("createTime")&&!name.equals("isCatalog")&&!name.equals("finishTime")
            		&&!name.equals("printCount")) {
                boolean del = true;
                for (int j = 0; j < tableInfo.getFieldInfos().size(); j++) {
                    String tn2 = ((DBFieldInfoBean) tableInfo.getFieldInfos().
                                  get(j)).getFieldName();

                    if (tn.equals(tn2)) {
                        del = false;
                        continue;
                    }
                }
                if (del) {
                    delList.add(oldTableInfo.getFieldInfos().get(i));
                }
            }
        }
        //�����ɾ�����ֶ�
        if (delList.size() > 0) {
            if (oldTableInfo.getUdType() == DBTableInfoBean.SYSTEM_TYPE &&
                oldTableInfo.getUpdateAble() == DBTableInfoBean.NOT_UPDATE) {
                rs.retCode = ErrorCanst.RET_FORBID_UPDATE_ERROR;
                rs.retVal = " forbid delete Fields ";
                return rs;
            }
            for (int i = 0; i < delList.size(); i++) {
                DBFieldInfoBean delFib = (DBFieldInfoBean) delList.get(i);
                if (delFib.getUdType() == DBFieldInfoBean.SYSTEM_TYPE) {
                    rs.retCode = ErrorCanst.RET_FORBID_UPDATE_ERROR;
                    rs.retVal = " forbid delete Fields " + delFib.getFieldName();
                    return rs;
                } else {
                	//ȡԼ������
                    String contraint = getContrant(oldTableInfo.getTableName(),
                        delFib.getFieldName());
                    if(tableInfo.getIsView() == 0 && delFib.getInputType() != DBFieldInfoBean.INPUT_CUT_LINE){
	                    if (contraint != null && contraint.trim().length() > 0) {
	                        alterList.add("ALTER TABLE " +
	                                      oldTableInfo.getTableName() +
	                                      " DROP CONSTRAINT " + contraint);     
	                    }
	                    //ɾ�������ֶ�
                    	alterList.add("ALTER TABLE " + oldTableInfo.getTableName() +
                                  " DROP COLUMN " + delFib.getFieldName());  
                    }
                    //ɾ���ֶζ�����
                    alterList.add("DELETE FROM tblLanguage WHERE ID =(select languageId from tblDBFieldInfo WHERE tableid=(select id from tblDBTableInfo where tableName='"+tableInfo.getTableName()+"') and fieldName = '" +
                        		delFib.getFieldName() + "')") ;
                    if(delFib.getGroupName()!= null && delFib.getGroupName().length() > 0){
                        //ɾ���ֶζ�����
                        alterList.add("DELETE FROM tblLanguage WHERE ID =(select groupName from tblDBFieldInfo WHERE tableid=(select id from tblDBTableInfo where tableName='"+tableInfo.getTableName()+"') and fieldName = '" +
                        		delFib.getFieldName() + "')") ;
                    }
                    //ɾ����ṹ
                    alterList.add("DELETE FROM tblDBFieldInfo WHERE tableid=(select id from tblDBTableInfo where tableName='"+tableInfo.getTableName()+"') and fieldName = '" +
                        		delFib.getFieldName() + "'");    
                    //����ֶ��ǵ��ݱ�ţ�Ҫɾ�����
                    if(delFib.getFieldIdentityStr()!=null && "BillNo".equals(delFib.getFieldIdentityStr())){
                    	alterList.add("DELETE FROM tblBillNo WHERE [key]='"+tableInfo.getTableName()+"_"+delFib.getFieldName()+"'");
                    	logList.add(new String[]{"tblBillNo","���ݱ��","�޸ı�ṹɾ�����ݱ����Ϣ��"+delFib.getDisplay().get(locale)});
                    }
                    //����ֶ���ƴ�����ֶΣ�Ҫɾ��ƴ����
                    if(delFib.getInputType()==DBFieldInfoBean.INPUT_PYM|| delFib.getInputTypeOld()==DBFieldInfoBean.INPUT_PYM){
                        //ɾ�������ֶ�
                    	if(tableInfo.getIsView() == 0){
                    		alterList.add("ALTER TABLE " + oldTableInfo.getTableName() +
                                      " DROP COLUMN " + delFib.getFieldName()+"PYM");  
                    	}
                        //ɾ���ֶζ�����
                        alterList.add("DELETE FROM tblLanguage WHERE ID =(select languageId from tblDBFieldInfo WHERE  tableid=(select id from tblDBTableInfo where tableName='"+tableInfo.getTableName()+"') and fieldName = '" +
                        		delFib.getFieldName()+"PYM" + "')") ;
                        if(delFib.getGroupName()!= null && delFib.getGroupName().length() > 0){
                            //ɾ���ֶζ�����
                            alterList.add("DELETE FROM tblLanguage WHERE ID =(select groupName from tblDBFieldInfo WHERE  tableid=(select id from tblDBTableInfo where tableName='"+tableInfo.getTableName()+"') and fieldName = '" +
                            		delFib.getFieldName()+"PYM" + "')") ;
                        }
                        //ɾ����ṹ
                        alterList.add("DELETE FROM tblDBFieldInfo WHERE tableid=(select id from tblDBTableInfo where tableName='"+tableInfo.getTableName()+"') and fieldName = '" +
                        		delFib.getFieldName()+"PYM" + "'");  
                    }
                    
                }
            }
        }
        //��������ӵ��ֶ�
        if (addList.size() > 0) {
            if (oldTableInfo.getUdType() == DBTableInfoBean.SYSTEM_TYPE &&
                oldTableInfo.getUpdateAble() == DBTableInfoBean.NOT_UPDATE) {
                rs.retCode = ErrorCanst.RET_FORBID_UPDATE_ERROR;
                rs.retVal = " forbid add Fields ";
                return rs;
            }
            for (int i = 0; i < addList.size(); i++) {
                DBFieldInfoBean addFib = (DBFieldInfoBean) addList.get(i);
                if (nameMap.get(addFib.getFieldName()) == null) {
                    nameMap.put(addFib.getFieldName(), addFib.getFieldName());
                } else {
                    //�����ظ�
                    rs.setRetCode(ErrorCanst.RET_FIELD_EXIST_ERROR);
                    rs.setRetVal(addFib.getFieldName()) ;
                    return rs;
                }
                
                if(addFib.getFieldType()==1 && "Qty".equals(addFib.getFieldIdentityStr())){
                	addFib.setDigits(Byte.parseByte(GlobalsTool.getSysSetting("DigitsQty")));
				}else if(addFib.getFieldType()==1 && ("priceIdentifier".equals(addFib.getFieldIdentityStr()) || 
						"SPriceIdentifier".equals(addFib.getFieldIdentityStr()) ||"Price".equals(addFib.getFieldIdentityStr()) )){
					addFib.setDigits(Byte.parseByte(GlobalsTool.getSysSetting("DigitsPrice")));
				}else if(addFib.getFieldType()==1 && ("Amount".equals(addFib.getFieldIdentityStr()) || 
						"AmountIdentifier".equals(addFib.getFieldIdentityStr()) ||"SAmountIdentifier".equals(addFib.getFieldIdentityStr()) )){
					addFib.setDigits(Byte.parseByte(GlobalsTool.getSysSetting("DigitsAmount")));
				}
                
                String typeStr = getTypeStr(addFib);
                if (typeStr == null || typeStr.trim().length() == 0) {
                    //�����Ƿ�
                    rs.setRetCode(ErrorCanst.RET_ILLEGAL_ARGUMENT_ERROR);
                    return rs;
                }
                if(tableInfo.getIsView() == 0 && addFib.getInputType() != DBFieldInfoBean.INPUT_CUT_LINE){
                	alterList.add("ALTER TABLE " + tableInfo.getTableName() +
                              " ADD  " + addFib.getFieldName() + " " +
                              typeStr  + (addFib.getIsNull()==1?" not ":"")+ " null ");
                }
                //��ӱ�ṹ
                alterList.add(getFieldInsert(addFib));
                //������
                alterList.add(addFib.getDisplay().toAddSQL());
                if(addFib.getGroupName()!=null && addFib.getGroupName().length() > 0){
                    //������
                	alterList.add(addFib.getGroupDisplay().toAddSQL());
                }
                
                if(addFib.getFieldIdentityStr()!=null && "BillNo".equals(addFib.getFieldIdentityStr())){
                	//�ֶα�ʶ����Ϊ���ݱ��
                	alterList.add("if (SELECT count(0) as count FROM tblBillNo WHERE [key]='"+tableInfo.getTableName()+"_"+addFib.getFieldName()+"')=0 begin \n" +
                			"INSERT INTO tblBillNo([key],billName,pattern,billNO,start,step,isfillback,[reset],laststamp,explain,isAddbeform) "+
                     	    "VALUES('"+tableInfo.getTableName()+"_"+addFib.getFieldName()+"','"+tableInfo.getDisplay().get("zh_CN")+"_"+addFib.getDisplay().get("zh_CN")+"','SS{date yy}{date MM}{date dd}{serial 0000}',0,1,1,0,2,'"+System.currentTimeMillis()+"','SS��������������ˮ�Ÿ�ʽ[0000]',1)  \n" +
                			" end ");                	
                	logList.add(new String[]{"tblBillNo","���ݱ��","�޸ı�ṹ���ӵ��ݱ����Ϣ��"+addFib.getDisplay().get(locale)});
                }
                
                //����ֶ���ƴ�����ֶΣ�Ҫ����ƴ����
                if(addFib.getInputType()==DBFieldInfoBean.INPUT_PYM|| addFib.getInputTypeOld()==DBFieldInfoBean.INPUT_PYM){
                	DBFieldInfoBean fibPYM =getFieldPYM(addFib);                	
                	typeStr = getTypeStr(fibPYM);
                    if (typeStr == null || typeStr.trim().length() == 0) {
                        //�����Ƿ�
                        rs.setRetCode(ErrorCanst.RET_ILLEGAL_ARGUMENT_ERROR);
                        return rs;
                    }
                    if(tableInfo.getIsView() == 0){
                    	alterList.add("ALTER TABLE " + tableInfo.getTableName() +
                                  " ADD  " + fibPYM.getFieldName() + " " +
                                  typeStr  + (fibPYM.getIsNull()==1?" not ":"")+ " null ");
                    }
                    //��ӱ�ṹ
                    alterList.add(getFieldInsert(fibPYM));
                    //������
                    alterList.add(fibPYM.getDisplay().toAddSQL());
                    if(fibPYM.getGroupName()!=null && fibPYM.getGroupName().length() > 0){
                        //������
                    	alterList.add(fibPYM.getGroupDisplay().toAddSQL());
                    }
                }
                
            }
        }

        //�����Ƿ����޸ĵ��ֶ�
        if (updateOldList.size() > 0) {
            for (int i = 0; i < updateOldList.size(); i++) {
                DBFieldInfoBean uol = (DBFieldInfoBean) updateOldList.get(i);
                DBFieldInfoBean ul = (DBFieldInfoBean) updateList.get(i);
                
                if (!uol.getTableBean().getId().equals(ul.getTableBean().getId()) ||
                    !uol.getFieldName().equals(ul.getFieldName()) ||
                    uol.getUdType() != ul.getUdType()) {
                    rs.retCode = ErrorCanst.RET_FORBID_UPDATE_ERROR;
                    rs.retVal = " forbid update Fields ";
                    return rs;
                }
                if((uol.getInputType()==DBFieldInfoBean.INPUT_CUT_LINE&& ul.getInputType()!=DBFieldInfoBean.INPUT_CUT_LINE)||
                		(ul.getInputType()==DBFieldInfoBean.INPUT_CUT_LINE&& uol.getInputType()!=DBFieldInfoBean.INPUT_CUT_LINE)){
                	rs.retCode = ErrorCanst.RET_FORBID_UPDATE_ERROR;
                    rs.retVal = " �ָ������Ͳ������޸� ";
                    return rs;
                }
                //ϵͳ�ֶβ������޸�,������ֹ�޸ģ����ֶ�һ����ϵͳ��
                String opn = uol.getInputValue();
                String pn = ul.getInputValue();
                opn = opn == null ? "" : opn;
                pn = pn == null ? "" : pn;

                String oen = uol.getRefEnumerationName();
                String en = ul.getRefEnumerationName();
                oen = oen == null ? "" : oen;
                en = en == null ? "" : en;

                if (uol.getUdType() == DBFieldInfoBean.SYSTEM_TYPE) {
                    if (uol.getInputType() != ul.getInputType() ||
                        !oen.equals(en) ||
                        !opn.equals(pn)) {
                        rs.retCode = ErrorCanst.RET_FORBID_UPDATE_ERROR;
                        rs.retVal = " forbid update Fields ";
                        return rs;
                    }
                }
                //������
                ret = getUpdateLanguage(uol.getDisplay(), ul.getDisplay());
        		if(ret.length() > 0){
                	alterList.add(ret);
                }
                //ԭ����group,����û��group
        		if(uol.getGroupDisplay() != null && ul.getGroupDisplay()==null){
        			alterList.add("delete tbllanguage where id= '" + uol.getGroupName() + "'");
        		}else if(uol.getGroupDisplay() == null && ul.getGroupDisplay()!=null){ //ԭ��û�У�������
        			alterList.add(ul.getGroupDisplay().toAddSQL());
        		}else if(uol.getGroupDisplay() != null && ul.getGroupDisplay()!=null){ //�޸�
        			ret = getUpdateLanguage(uol.getGroupDisplay(), ul.getGroupDisplay());
            		if(ret.length() > 0){
                    	alterList.add(ret);
                    }
        		}
        		if((uol.getFieldIdentityStr() ==null && ul.getFieldIdentityStr() != null) || !uol.getFieldIdentityStr().equals(ul.getFieldIdentityStr()) ){
	        		if(ul.getFieldType()==1 && "Qty".equals(ul.getFieldIdentityStr())){
	        			ul.setDigits(Byte.parseByte(GlobalsTool.getSysSetting("DigitsQty")));
					}else if(ul.getFieldType()==1 && ("priceIdentifier".equals(ul.getFieldIdentityStr()) || 
							"SPriceIdentifier".equals(ul.getFieldIdentityStr()) ||"Price".equals(ul.getFieldIdentityStr()) )){
						ul.setDigits(Byte.parseByte(GlobalsTool.getSysSetting("DigitsPrice")));
					}else if(ul.getFieldType()==1 && ("Amount".equals(ul.getFieldIdentityStr()) || 
							"AmountIdentifier".equals(ul.getFieldIdentityStr()) ||"SAmountIdentifier".equals(ul.getFieldIdentityStr()) )){
						ul.setDigits(Byte.parseByte(GlobalsTool.getSysSetting("DigitsAmount")));
					}else{
						ul.setDigits((byte)0);
					}
        		}
                String updStr  = getUpdateStr(uol,ul);
                if(updStr.length() > 0){
                    
                	
                	ret = " update tblDBFieldInfo set "+updStr.substring(1) +" where tableid=(select id from tblDBTableInfo where tableName='"+tableInfo.getTableName()+"') and fieldName = '" +
                        		ul.getFieldName() + "'";
                	alterList.add(ret);
                	//��û�и���������
                	if(uol.getFieldType() != ul.getFieldType() || uol.getIsNull() != ul.getIsNull() || uol.getMaxLength() != ul.getMaxLength() ){
                		//�ֶ����Ͳ�һ����Ҫ�޸������ṹ
                		String typeStr = getTypeStr(ul);
                        if (typeStr == null || typeStr.trim().length() == 0) {
                            //�����Ƿ�
                            rs.setRetCode(ErrorCanst.RET_ILLEGAL_ARGUMENT_ERROR);
                            return rs;
                        }
                    	//ȡԼ������
                        String contraint = getContrant(oldTableInfo.getTableName(),
                            ul.getFieldName());
                        if(tableInfo.getIsView() == 0  && ul.getInputType() != DBFieldInfoBean.INPUT_CUT_LINE){
	                        if (contraint != null && contraint.trim().length() > 0) {
	                            alterList.add("ALTER TABLE " +
	                                          oldTableInfo.getTableName() +
	                                          " DROP CONSTRAINT " + contraint);     
	                        }
	                        
	                		alterList.add("ALTER TABLE " + tableInfo.getTableName() +
	                                " alter column  " + ul.getFieldName() + " " +
                                typeStr + (ul.getIsNull()==1?" not ":"")+ " null ");
                        }
                	}
                	
            		     
                	//�����ݱ��
            		if( (uol.getFieldIdentityStr()!=null && "BillNo".equals(uol.getFieldIdentityStr()) ) && (ul.getFieldIdentityStr()==null || !"BillNo".equals(ul.getFieldIdentityStr()) )){
            			//ԭ���Ǳ�ţ����ڲ���
            			alterList.add("DELETE FROM tblBillNo WHERE [key]='"+tableInfo.getTableName()+"_"+uol.getFieldName()+"'");
                    	logList.add(new String[]{"tblBillNo","���ݱ��","�޸ı�ṹɾ�����ݱ����Ϣ��"+uol.getDisplay().get(locale)});
            		}else if( (uol.getFieldIdentityStr()==null || !"BillNo".equals(uol.getFieldIdentityStr()) ) && (ul.getFieldIdentityStr()!=null && "BillNo".equals(ul.getFieldIdentityStr()) )){
            			//ԭ�����ǣ�������
            			//�ֶα�ʶ����Ϊ���ݱ��
                    	alterList.add("if (SELECT count(0) as count FROM tblBillNo WHERE [key]='"+tableInfo.getTableName()+"_"+ul.getFieldName()+"')=0 begin \n" +
                    			"INSERT INTO tblBillNo([key],billName,pattern,billNO,start,step,isfillback,[reset],laststamp,explain,isAddbeform) "+
                         	    "VALUES('"+tableInfo.getTableName()+"_"+ul.getFieldName()+"','"+tableInfo.getDisplay().get("zh_CN")+"_"+ul.getDisplay().get("zh_CN")+"','SS{date yy}{date MM}{date dd}{serial 0000}',0,1,1,0,2,'"+System.currentTimeMillis()+"','SS��������������ˮ�Ÿ�ʽ[0000]',1)  \n" +
                    			" end ");                	
                    	logList.add(new String[]{"tblBillNo","���ݱ��","�޸ı�ṹ���ӵ��ݱ����Ϣ��"+ul.getDisplay().get(locale)});
            		}
            		
                	//����ƴ����
            		if( (uol.getInputType()==DBFieldInfoBean.INPUT_PYM|| uol.getInputTypeOld()==DBFieldInfoBean.INPUT_PYM) && (ul.getInputType()!=DBFieldInfoBean.INPUT_PYM && ul.getInputTypeOld()!=DBFieldInfoBean.INPUT_PYM)){
            			//ɾ�������ֶ�
            			if(tableInfo.getIsView() == 0){
            				alterList.add("ALTER TABLE " + oldTableInfo.getTableName() +
                                      " DROP COLUMN " + ul.getFieldName()+"PYM");  
            			}
                        //ɾ���ֶζ�����
                        alterList.add("DELETE FROM tblLanguage WHERE ID =(select languageId from tblDBFieldInfo WHERE  tableid=(select id from tblDBTableInfo where tableName='"+tableInfo.getTableName()+"') and fieldName = '" +
                        		ul.getFieldName()+"PYM" + "')") ;
                        if(ul.getGroupName()!= null && ul.getGroupName().length() > 0){
                            //ɾ���ֶζ�����
                            alterList.add("DELETE FROM tblLanguage WHERE ID =(select groupName from tblDBFieldInfo WHERE  tableid=(select id from tblDBTableInfo where tableName='"+tableInfo.getTableName()+"') and fieldName = '" +
                            		ul.getFieldName()+"PYM" + "')") ;
                        }
                        //ɾ����ṹ
                        alterList.add("DELETE FROM tblDBFieldInfo WHERE tableid=(select id from tblDBTableInfo where tableName='"+tableInfo.getTableName()+"') and fieldName = '" +
                        		ul.getFieldName()+"PYM" + "'");  
            		}else if( (uol.getInputType()!=DBFieldInfoBean.INPUT_PYM&& uol.getInputTypeOld()!=DBFieldInfoBean.INPUT_PYM) && (ul.getInputType()==DBFieldInfoBean.INPUT_PYM || ul.getInputTypeOld()==DBFieldInfoBean.INPUT_PYM)){
            			DBFieldInfoBean fibPYM =getFieldPYM(ul);                	
                    	String typeStr = getTypeStr(fibPYM);
                        if (typeStr == null || typeStr.trim().length() == 0) {
                            //�����Ƿ�
                            rs.setRetCode(ErrorCanst.RET_ILLEGAL_ARGUMENT_ERROR);
                            return rs;
                        }
                        if(tableInfo.getIsView() == 0){
                        	alterList.add("ALTER TABLE " + tableInfo.getTableName() +
                                      " ADD  " + fibPYM.getFieldName() + " " +
                                      typeStr  + (fibPYM.getIsNull()==1?" not ":"")+ " null ");
                        }
                        //��ӱ�ṹ
                        alterList.add(getFieldInsert(fibPYM));
                        //������
                        alterList.add(fibPYM.getDisplay().toAddSQL());
                        if(fibPYM.getGroupName()!=null && fibPYM.getGroupName().length() > 0){
                            //������
                        	alterList.add(fibPYM.getGroupDisplay().toAddSQL());
                        }
            		}
                }
            }
        }
        
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;                      
                        for (int i = 0; i < alterList.size(); i++) {
                            String ddl = alterList.get(i).toString();
                            try {
                                Statement cs = conn.createStatement();
                                BaseEnv.log.debug(ddl);
                                cs.execute(ddl);
                            } catch (Exception ex) {
                                if (ex instanceof SQLException && ((SQLException) ex).getErrorCode() == 2714) {
                                    rs.setRetCode(ErrorCanst.
                                                  RET_TABLENAME_EXIST_ERROR);
                                } else {
                                    rs.setRetCode(ErrorCanst.
                                                  DEFAULT_FAILURE);
                                }
                                BaseEnv.log.error("alter Table Error :" +ddl, ex);
                                String msg = ex.toString();
                                if(msg.indexOf("Exception:") > 0){
                                	msg = msg.substring(msg.indexOf("Exception:")+"Exception:".length() );
                                	if(msg.indexOf("���ض��ַ��������������")>-1){
                                		msg = msg + ddl;                                		
                                	}
                                }
                                rs.retVal = msg;
                                return;
                            }
                        }
                        updateRefreshTime("tableInfo", conn); //ˢ�±�ʱ��
                    }
                });
                return rs.getRetCode();
            }
        });

        rs.retCode = retVal;
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            BaseEnv.tableInfos.put(tableInfo.getTableName(), tableInfo);
            String sqlTime=BaseDateFormat.format(new Date(),
                    BaseDateFormat.yyyyMMddHHmmss);
            TestRW.saveToSql(path,"--"+sqlTime+":�޸ı�\n");
            //�޸ĳɹ��󱣴�ű�
            for (int i = 0; i < alterList.size(); i++) {
                TestRW.saveToSql(path, alterList.get(i).toString()+"\n");
            }            
        }
        if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
        	rs.retVal = logList;
        }
        return rs;
    }
    
    public Result savePopDisSentence(final String tableName,final String viewsql,final String path){
    	String oldsql  = (String)BaseEnv.popDisSentence.get(tableName);
    	if(oldsql == null || oldsql.equals("null")){
    		oldsql = "";
    	}
    	if(oldsql.equals(viewsql==null?"":viewsql)){
    		return new Result();
    	}
    	
    	final String id = IDGenerater.getId();
    	final Result rs  = new Result();
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;         
                            try {
                            	String sql = " delete tblPopDisSentence where tableName=? ";
                            	PreparedStatement pstmt = conn.prepareStatement(sql);
                            	pstmt.setString(1, tableName);
                            	pstmt.execute();
                            	if(viewsql !=null && viewsql.length() > 0){
	                            	sql = "insert into tblPopDisSentence(id,tableName,sentence,createBy,statusId) values(?,?,?,'1',0)";
	                            	pstmt = conn.prepareStatement(sql);
	                            	pstmt.setString(1, id);
	                            	pstmt.setString(2, tableName);
	                            	pstmt.setString(3, viewsql);
	                            	pstmt.execute();
                            	}
                            } catch (Exception ex) {
                                BaseEnv.log.error("DDLOperation savePopDisSentence Error ",ex);
                                rs.retVal = ex.getMessage();
                                return;
                            }
                    }
                });
                return rs.getRetCode();
            }
        });

        rs.retCode = retVal;
        if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
        	BaseEnv.popDisSentence.put(tableName,viewsql);
            String sqlTime=BaseDateFormat.format(new Date(),
                    BaseDateFormat.yyyyMMddHHmmss);
            TestRW.saveToSql(path,"--"+sqlTime+":�޸ı�\n");
            TestRW.saveToSql(path," delete tblPopDisSentence where tableName='"+tableName+"' \n");
            if(viewsql !=null && viewsql.length() > 0){
            	TestRW.saveToSql(path," insert into tblPopDisSentence(id,tableName,sentence,createBy,statusId) \n"
            		+ " values('"+id+"','"+tableName+"','"+(viewsql.replaceAll("'", "''"))+"','1',0) \n");
            }
        }
        return rs;
    }

    private String getContrant(final String tableName, final String fieldName) {
        /* ��ͨ��������䣬���Լ���������������ɾ��
                    select so.name
         from sysobjects so JOIN sysconstraints sc ON so.id = sc.constid
         WHERE object_name(so.parent_obj) = 'tblTChildTable' AND so.xtype = 'D'
                    AND sc.colid =(SELECT colid FROM syscolumns WHERE id = object_id('tblTChildTable') AND name = 'field2')

         */
        final Result rs = new Result();
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        String sql = "" +
                                     " select so.name " +
                                     " from sysobjects so JOIN sysconstraints sc ON so.id = sc.constid " +
                                     " WHERE object_name(so.parent_obj) = '" +
                                     tableName + "' AND so.xtype = 'D' " +
                                     " AND sc.colid =(SELECT colid FROM syscolumns WHERE id = object_id('" +
                                     tableName + "') AND name = '" + fieldName +
                                     "') ";
                        Statement stmt = conn.createStatement();
                        ResultSet temprs = stmt.executeQuery(sql);
                        if (temprs.next()) {
                            rs.setRetVal(temprs.getString(1));
                        } else {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                        }
                    }
                });
                return rs.retCode;
            }
        });
        if (retVal == ErrorCanst.DEFAULT_SUCCESS) {
            return rs.retVal.toString();
        } else {
            return null;
        }
    }

    public static String getDefaultStr(DBFieldInfoBean fieldInfo) throws Exception {
        String fieldefault = "";
        switch (fieldInfo.getFieldType()) {
        case DBFieldInfoBean.FIELD_INT:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                fieldefault = fieldInfo.getDefaultValue().trim();
                try {
                    Integer.parseInt(fieldefault);
                } catch (Exception ex) {
                    //Ĭ��ֵ��ֵ��ʽ����ȷ
                    throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                }
            }
            break;
        case DBFieldInfoBean.FIELD_DOUBLE:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                fieldefault = fieldInfo.getDefaultValue().trim();
                try {
                    Float.parseFloat(fieldefault);
                } catch (Exception ex1) {
                    //Ĭ��ֵ��ֵ��ʽ����ȷ
                    throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                }
            }else{
                fieldefault="0";
            }
            break;
        case DBFieldInfoBean.FIELD_ANY:
        case DBFieldInfoBean.FIELD_DOUBLE_TEXT:
        case DBFieldInfoBean.FIELD_PASSWORD:
        case DBFieldInfoBean.FIELD_ONETEXT:
        case DBFieldInfoBean.FIELD_TEXT:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                fieldefault = "'" + fieldInfo.getDefaultValue().trim() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_ENGLISH:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                if (!fieldInfo.getDefaultValue().trim().matches(
                    "^[0-9A-Za-z]*$")) {
                    //Ĭ��ֵ��ֵ��ʽ����ȷ
                    throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                }

                fieldefault = "'" + fieldInfo.getDefaultValue() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_DATE_SHORT:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                if (!fieldInfo.getDefaultValue().trim().matches(
                    "^20[0-9]{2}-[0-1][0-9]-[0-3][0-9]")) {
                    //Ĭ��ֵ��ֵ��ʽ����ȷ
                    throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                }
                fieldefault = "'" + fieldInfo.getDefaultValue() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_DATE_LONG:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                if (!fieldInfo.getDefaultValue().trim().matches(
                    "^20[0-9]{2}-[0-1][0-9]-[0-3][0-9] [0-2][0-9]:[0-6][0-9]:[0-6][0-9]")) {
                    //Ĭ��ֵ��ֵ��ʽ����ȷ
                    throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                }

                fieldefault = "'" + fieldInfo.getDefaultValue() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_IP:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                if (!fieldInfo.getDefaultValue().trim().matches(
                    "\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                    //Ĭ��ֵ��ֵ��ʽ����ȷ
                    throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                }
                fieldefault = "'" + fieldInfo.getDefaultValue() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_EMAIL:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                if (!fieldInfo.getDefaultValue().trim().matches(
                    "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$")) {
                    //Ĭ��ֵ��ֵ��ʽ����ȷ
                    throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                }
                fieldefault = "'" + fieldInfo.getDefaultValue() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_PHONE:
        case DBFieldInfoBean.FIELD_TEL:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                if (!fieldInfo.getDefaultValue().trim().matches(
                    "^([0-9]{3,4}-)?[0-9]{7,8}(-[0-9]{1,3})*$")) {
                    //Ĭ��ֵ��ֵ��ʽ����ȷ
                    throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                }
                fieldefault = "'" + fieldInfo.getDefaultValue() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_MOBILE:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                if (!fieldInfo.getDefaultValue().trim().matches(
                    "^1[3,5][0-9]{9}$")) {
                    //Ĭ��ֵ��ֵ��ʽ����ȷ
                    throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                }

                fieldefault = "'" + fieldInfo.getDefaultValue() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_URL:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                if (!fieldInfo.getDefaultValue().trim().matches(
                    "^[a-zA-Z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$")) {
                    //Ĭ��ֵ��ֵ��ʽ����ȷ
                    throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                }
                fieldefault = "'" + fieldInfo.getDefaultValue() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_ZIP:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                if (!fieldInfo.getDefaultValue().trim().matches(
                    "^[0-9]{6}$")) {
                    //Ĭ��ֵ��ֵ��ʽ����ȷ
                    throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                }
                fieldefault = "'" + fieldInfo.getDefaultValue() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_PIC:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                fieldefault = "'" + fieldInfo.getDefaultValue().trim() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_AFFIX:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                fieldefault = "'" + fieldInfo.getDefaultValue().trim() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_HTML:
            if (fieldInfo.getDefaultValue() != null &&
                fieldInfo.getDefaultValue().trim().length() > 0) {
                fieldefault = "'" + fieldInfo.getDefaultValue().trim() + "'";
            }
            break;
        case DBFieldInfoBean.FIELD_DATE_TIME:
                   if (fieldInfo.getDefaultValue() != null &&
                       fieldInfo.getDefaultValue().trim().length() > 0) {
                       if (!fieldInfo.getDefaultValue().trim().matches(
                           "[0-2][0-9]:[0-6][0-9]:[0-6][0-9]")) {
                           //Ĭ��ֵ��ֵ��ʽ����ȷ
                           throw new Exception(ErrorCanst.RET_DEFAULT_TYPE_ERROR + "");
                       }
                       fieldefault = "'" + fieldInfo.getDefaultValue() + "'";
                   }
            break;
        default:

            //�����Ƿ�
            throw new Exception(ErrorCanst.RET_ILLEGAL_ARGUMENT_ERROR + "");
        }

        if (fieldefault == null || fieldefault.trim().length() == 0) {
            return "";
        } else {
            //return " default(" + fieldefault + ")";
            return fieldefault; //fhq�޸�
        }
    }


    public static String getTypeStr(DBFieldInfoBean fieldInfo) {
        if (fieldInfo.getFieldName().equals("f_ref")) {
            return "varchar(30)";
        }
        String fieldType = "";
        switch (fieldInfo.getFieldType()) {
        case DBFieldInfoBean.FIELD_INT:
            fieldType = "int";
            break;
        case DBFieldInfoBean.FIELD_DOUBLE:
            fieldType = "numeric(18, 8)";
            break;
        case DBFieldInfoBean.FIELD_ANY:
            fieldType = fieldInfo.getMaxLength()>8000?"text":"varchar(" + fieldInfo.getMaxLength() + ")";
            break;
        case DBFieldInfoBean.FIELD_TEXT:
            fieldType = fieldInfo.getMaxLength()>8000?"text":"varchar(" + fieldInfo.getMaxLength() + ")";
            break;
        case DBFieldInfoBean.FIELD_ONETEXT:
        	fieldType="varchar(" + fieldInfo.getMaxLength() + ")";
        	break;
        case DBFieldInfoBean.FIELD_ENGLISH:
            fieldType = "varchar(" + fieldInfo.getMaxLength() + ")";
            break;
        case DBFieldInfoBean.FIELD_DATE_SHORT:
            fieldType = "varchar(10)";
            break;
        case DBFieldInfoBean.FIELD_DATE_LONG:
            fieldType = "varchar(19)";
            break;
        case DBFieldInfoBean.FIELD_IP:
            fieldType = "varchar(30)";
            break;
        case DBFieldInfoBean.FIELD_EMAIL:
            fieldType = "varchar(50)";
            break;
        case DBFieldInfoBean.FIELD_TEL:
            fieldType = "varchar(20)";
            break;
        case DBFieldInfoBean.FIELD_MOBILE:
            fieldType = "varchar(20)";
            break;
        case DBFieldInfoBean.FIELD_PHONE:
            fieldType = "varchar(20)";
            break;
        case DBFieldInfoBean.FIELD_URL:
            fieldType = "varchar(50)";
            break;
        case DBFieldInfoBean.FIELD_ZIP:
            fieldType = "varchar(15)";
            break;
        case DBFieldInfoBean.FIELD_PIC:
            fieldType = "varchar(1000)";
            break;
        case DBFieldInfoBean.FIELD_AFFIX:
            fieldType = "varchar(1000)";
            break;
        case DBFieldInfoBean.FIELD_HTML:
            fieldType = "text";
            break;
        case DBFieldInfoBean.FIELD_DATE_TIME:
                   fieldType = "varchar(10)";
            break;
        case DBFieldInfoBean.FIELD_DOUBLE_TEXT:
            fieldType = fieldInfo.getMaxLength()>8000?"text":"varchar(" + fieldInfo.getMaxLength() + ")";
            break;
        case DBFieldInfoBean.FIELD_PASSWORD:
            fieldType = "varchar(" + fieldInfo.getMaxLength() + ")";
            break;
        default:
        }
        return fieldType;
    }

    public static boolean updateRefreshTime(String flag, Connection conn) {
        try {
            String sql = "update tblInitTime set lastTime = ? where initName=?";
            PreparedStatement cs = conn.prepareStatement(sql);
            BaseEnv.log.debug(sql);
            cs.setLong(1,System.currentTimeMillis());
            cs.setString(2,flag);
            cs.executeUpdate();
            return true;
        } catch (SQLException ex) {
            BaseEnv.log.error("updateRefreshTime :" + flag,
                              ex);
            return false;
        }

    }
    public static boolean updateRefreshTime(final String flag, Session session) {
        final Result rs  = new Result();
        session.doWork(new Work() {
            public void execute(Connection connection) throws
                SQLException {
                boolean bo = updateRefreshTime(flag,connection);
                if(!bo) rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
            }
        });
        if(rs.getRetCode() ==  ErrorCanst.DEFAULT_SUCCESS){
            return true;
        }else{
            return false;
        }
    }
    
    public Result refreshEnumeration(String enumName, Connection conn) {
    	Result rs = new Result();
		try {
			String sql = " select tblDBEnumerationItem.id,enumValue,enumId,languageId,enumOrder from tblDBEnumerationItem " + " where enumId in (select id from tblDBEnumeration where enumName=?) ";
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, enumName);
			ResultSet rset = pstmt.executeQuery();

			EnumerateBean bean = (EnumerateBean) BaseEnv.enumerationMap.get(enumName);

			ArrayList<EnumerateItemBean> list = new ArrayList<EnumerateItemBean>();
			final KRLanguageQuery klQuery = new KRLanguageQuery();
			while (rset.next()) {
				EnumerateItemBean it = new EnumerateItemBean();
				it.setId(rset.getString("id"));
				it.setEnumerateBean(bean);
				it.setLanguageId(rset.getString("languageId"));
				it.setEnumOrder(rset.getInt("enumOrder"));
				it.setEnumValue(rset.getString("enumValue"));
				list.add(it);
				klQuery.addLanguageId(it.getLanguageId());
			}

			HashMap klmap = klQuery.query(conn);

			for (int i = 0; i < list.size(); i++) {
				EnumerateItemBean eib = (EnumerateItemBean) list.get(i);
				eib.setDisplay((KRLanguage) klmap.get(eib.getLanguageId()));
			}
			bean.setEnumItem(list);
		} catch (Exception e) {
			rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
			rs.setRetVal("����ö�ٴ���"+e.getMessage());
		}
		return rs;
	} 

}
