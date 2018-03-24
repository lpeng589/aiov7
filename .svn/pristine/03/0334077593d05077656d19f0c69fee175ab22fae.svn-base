package com.menyi.aio.web.importData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OAWorkFlowTemplate;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.TestRW;

import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.web.userFunction.UserFunctionMgt;
import com.menyi.aio.bean.ImportDataBean;
import com.menyi.aio.bean.ImportFieldBean;
import com.menyi.web.util.BusinessException;
import com.menyi.aio.dyndb.DDLOperation;;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 *
 * <p>
 * Company: 周新宇
 * </p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class ImportDataMgt extends AIODBManager {

    //用于保存导出有关的错误代码
    public final static int ENUM_VALUE_ERROR = -5201; //枚举值找不到
    public final static int USER_CANCEL = -5202; //用户取消
    public final static int MAIN_TABLE_ERROR = -5203; //主表选择字段不存在或异常
    public final static int NOT_FIND_PARENTCODE = -5204 ;//未找到此父类parentCode
    public final static int VALUE_NOT_NULL = -5205 ;	//不能为空值
    public final static int EXIST_SPECIAL_CHAR = -5206 ; //特殊字符
    public final static int DATE_FORMAT_ERROR = -5206 ; //日期格式错误
    public final static int STRING_TO_NUMBER_ERROR = -5207 ;// 字符串转数字错误
    public final static int SEQ_NUMBER_ERROR = -5208 ;// 序列号的个数不等于数量
    public final static int SAVE_PARENT_ERROR = -5209 ;// 弹出窗存在子类不允许提交
    public final static int STR_SPEC_ERROR = -5210 ; //包含特殊字符

    public ImportDataMgt() {
    }

    DynDBManager dynMg = new DynDBManager();


    /**
     * 添加excel模板同表的映射
     *
     * @param mappedBean
     *            TableMappedBean
     * @return Result
     */
    public void addImportData(final ImportDataBean importDataBean,final String path) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                            String sql = "insert into tblImportData(id,name,targetTable,flag,createBy,createTime)"+" values(?,?,?,?,?,?)";
                            PreparedStatement st = conn.prepareStatement(sql);
                            st.setString(1,importDataBean.getId());
                            st.setString(2,importDataBean.getName());
                            st.setString(3,importDataBean.getTargetTable());
                            st.setInt(4,importDataBean.getFlag());
                            st.setString(5,importDataBean.getCreateBy());
                            st.setString(6,importDataBean.getCreateTime());
                            st.execute();
                            /*自动产生生成模板脚本*/
                            String templateSQL = "-----"+importDataBean.getName()+"模块脚本\n" ;
                            templateSQL += "delete from tblImportField where f_ref=(select id from tblImportData where targetTable='"+importDataBean.getTargetTable()+"');\n" ;
                            templateSQL += "delete from tblImportData where targetTable='"+importDataBean.getTargetTable()+"';\n" ;
                            templateSQL = "insert into tblImportData(id,name,targetTable,flag,createBy,createTime) " +
                            		"values('"+importDataBean.getId()+"','"+importDataBean.getName()+"','"+importDataBean.getTargetTable()+"',"+importDataBean.getFlag()+",'"+importDataBean.getCreateBy()+"','"+importDataBean.getCreateTime()+"');\n" ;
                            for (ImportFieldBean field:importDataBean.getField()) {
                               sql = "insert into tblImportField(id,f_ref,tableName,fieldName,viewTableName,viewFieldName,viewSaveField,flag,name)"+
                                     " values(?,?,?,?,?,?,?,?,?)";
                               st = conn.prepareStatement(sql);
                               st.setString(1,field.getId());
                               st.setString(2,importDataBean.getId());
                               st.setString(3,field.getTableName());
                               st.setString(4,field.getFieldName());
                               st.setString(5,field.getViewTableName());
                               st.setString(6,field.getViewFieldName());
                               st.setString(7,field.getViewSaveField());
                               st.setInt(8,field.getFlag());
                               st.setString(9,field.getName());
                               st.execute();
                               templateSQL += "insert into tblImportField(id,f_ref,tableName,fieldName,viewTableName,viewFieldName,viewSaveField,flag,name) " +
                               		"values('"+field.getId()+"','"+importDataBean.getId()+"','"+field.getTableName()+"','"+field.getFieldName()+"','"+field.getViewTableName()+"','"+field.getViewFieldName()+"','"+field.getViewSaveField()+"',"+field.getFlag()+",'"+field.getName()+"');\n" ;
                            }
                            TestRW.saveToSql(path, templateSQL);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);

        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
            throw new BusinessException("common.msg.addFailture");
        }
    }

    public Result updateImportData(final ImportDataBean importDataBean,final String path) {
            final Result rs = new Result();
            int retCode = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection conn) throws SQLException {
                            try {
                            	String sql = "delete from tblImportField where f_ref=(select id from tblImportData where targetTable=?)";
                                PreparedStatement st = conn.prepareStatement(sql);
                                st.setString(1,importDataBean.getTargetTable());
                                st.executeUpdate();
                                
                                sql = "delete from tblImportData where targetTable=?";
                                st = conn.prepareStatement(sql) ;
                                st.setString(1,importDataBean.getTargetTable());
                                st.executeUpdate();
                                
                                sql = "insert into tblImportData(id,name,targetTable,flag,createBy,createTime)"+
                                	" values(?,?,?,?,?,?)";
			                    st = conn.prepareStatement(sql);
			                    st.setString(1,importDataBean.getId());
			                    st.setString(2,importDataBean.getName());
			                    st.setString(3,importDataBean.getTargetTable());
			                    st.setInt(4,importDataBean.getFlag());
			                    st.setString(5,importDataBean.getCreateBy());
			                    st.setString(6,importDataBean.getCreateTime());
			                    st.execute();
			                    /*自动产生生成模板脚本*/
	                            String templateSQL = "-----"+importDataBean.getName()+"模块脚本\n" ;
	                            templateSQL += "delete from tblImportField where f_ref=(select id from tblImportData where targetTable='"+importDataBean.getTargetTable()+"');\n" ;
	                            templateSQL += "delete from tblImportData where targetTable='"+importDataBean.getTargetTable()+"';\n" ;
	                            templateSQL += "insert into tblImportData(id,name,targetTable,flag,createBy,createTime) " +
	                            		"values('"+importDataBean.getId()+"','"+importDataBean.getName()+"','"+importDataBean.getTargetTable()+"',"+importDataBean.getFlag()+",'"+importDataBean.getCreateBy()+"','"+importDataBean.getCreateTime()+"');\n" ;
			                    for (ImportFieldBean field:importDataBean.getField()) {
			                       sql = "insert into tblImportField(id,f_ref,tableName,fieldName,viewTableName,viewFieldName,viewSaveField,flag,name)"+
			                            " values(?,?,?,?,?,?,?,?,?)";
			                       st = conn.prepareStatement(sql);
			                       st.setString(1,field.getId());
			                       st.setString(2,importDataBean.getId());
			                       st.setString(3,field.getTableName());
			                       st.setString(4,field.getFieldName());
			                       st.setString(5,field.getViewTableName());
			                       st.setString(6,field.getViewFieldName());
			                       st.setString(7,field.getViewSaveField());
			                       st.setInt(8,field.getFlag());
			                       st.setString(9,field.getName());
			                       st.execute();
			                       templateSQL += "insert into tblImportField(id,f_ref,tableName,fieldName,viewTableName,viewFieldName,viewSaveField,flag,name) " +
                              		"values('"+field.getId()+"','"+importDataBean.getId()+"','"+field.getTableName()+"','"+field.getFieldName()+"','"+field.getViewTableName()+"','"+field.getViewFieldName()+"','"+field.getViewSaveField()+"',"+field.getFlag()+",'"+field.getName()+"');\n" ;
			                    }
			                    TestRW.saveToSql(path, templateSQL);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                                rs.setRetVal(ex.getMessage());
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

    public Result loadImportData(final String id,final String importDataError_1) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            String sql = "select * from tblImportData where id = ?";
                            PreparedStatement st = conn.prepareStatement(sql);
                            st.setString(1,id);
                            ResultSet rset = st.executeQuery();
                            ImportDataBean idb = new ImportDataBean();
                            if(rset.next()){
                                idb.setField(new ArrayList<ImportFieldBean>());
                                idb.setFlag(rset.getInt("flag"));
                                idb.setName(rset.getString("name"));
                                idb.setTargetTable(rset.getString("targetTable"));
                                idb.setId(rset.getString("id"));
                            }else{
                                rs.setRetCode(ErrorCanst.RET_ID_NO_VALUE_ERROR);
                                return;
                            }
                            sql = "select * from tblImportField where f_ref=? order by detOrderNo";
                            st = conn.prepareStatement(sql);
                            st.setString(1,id);
                            rset = st.executeQuery();

                            while (rset.next()) {
                                ImportFieldBean ifb = new ImportFieldBean();
                                ifb.setFieldName(rset.getString("fieldName"));
                                ifb.setFlag(rset.getInt("flag"));
                                ifb.setId(rset.getString("id"));
                                ifb.setTableName(rset.getString("tableName"));
                                ifb.setViewFieldName(rset.getString("viewFieldName"));
                                ifb.setViewSaveField(rset.getString("viewSaveField"));
                                ifb.setViewTableName(rset.getString("viewTableName"));
                                ifb.setName(rset.getString("name"));
                                idb.getField().add(ifb);
                            }
                            rs.setRetVal(idb);
                        } catch (Exception ex) {
                            BaseEnv.log.error("ImportDataMgt.updatePrepare Error",ex);
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rs.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);

        if(rs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS){
            //弹出错误信息,加载导入信息错误
            throw new BusinessException(importDataError_1);
        }
        return rs;
    }
    
    public Result loadImportDataByTable(final String tableName) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            String sql = "select * from tblImportData where targetTable = ?";
                            PreparedStatement st = conn.prepareStatement(sql);
                            st.setString(1,tableName);
                            ResultSet rset = st.executeQuery();
                            ImportDataBean idb = new ImportDataBean();
                            if(rset.next()){
                                idb.setField(new ArrayList<ImportFieldBean>());
                                idb.setFlag(rset.getInt("flag"));
                                idb.setName(rset.getString("name"));
                                idb.setTargetTable(rset.getString("targetTable"));
                                idb.setId(rset.getString("id"));
                            }else{
                                rs.setRetCode(ErrorCanst.RET_ID_NO_VALUE_ERROR);
                                return;
                            }
                            sql = "select * from tblImportField where f_ref=? order by detOrderNo"; 
                            st = conn.prepareStatement(sql);
                            st.setString(1,idb.getId());
                            rset = st.executeQuery();

                            while (rset.next()) {
                                ImportFieldBean ifb = new ImportFieldBean();
                                ifb.setFieldName(rset.getString("fieldName"));
                                ifb.setFlag(rset.getInt("flag"));
                                ifb.setId(rset.getString("id"));
                                ifb.setTableName(rset.getString("tableName"));
                                ifb.setViewFieldName(rset.getString("viewFieldName"));
                                ifb.setViewSaveField(rset.getString("viewSaveField"));
                                ifb.setViewTableName(rset.getString("viewTableName"));
                                ifb.setName(rset.getString("name"));
                                idb.getField().add(ifb);
                            }
                            rs.setRetVal(idb);
                        } catch (Exception ex) {
                            BaseEnv.log.error("ImportDataMgt.updatePrepare Error",ex);
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rs.setRetVal(ex.getMessage());
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

    /**
     * 判断是否存在模板
     * @param tableName
     * @return
     */
    public Result selectExistImportDataByTable(final String tableName) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            String sql = "select * from tblImportData where targetTable = ? and flag=1 ";
                            PreparedStatement st = conn.prepareStatement(sql);
                            st.setString(1,tableName);
                            ResultSet rset = st.executeQuery();
                            if(rset.next()){
                                rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
                            }else{
                                rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            }
                        } catch (Exception ex) {
                            BaseEnv.log.error("ImportDataMgt.updatePrepare Error",ex);
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rs.setRetVal(ex.getMessage());
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
 
    
    /**
     * 根据tableName或display查询tblExcelTableInfo表
     *
     * @param name
     *            String
     * @return Result
     */
    public Result queryImportData(final String name,final int pageSize,final int pageNo) {
    	String sql = "select id,flag,name,targetTable,moduleType,moduleParam from tblImportData";
    	ArrayList param = new ArrayList();
        // 如标题不为空，则做标题模糊查询
        if ((name != null && name.length() != 0)) {
            sql += " where name like '%" + name.trim()
                    + "%' or targetTable like'%" + name.trim() + "%'";
        }
        sql +=" order by flag,targetTable ";
        Result rs = this.sqlList(sql, param, pageSize, pageNo, true);
        
        if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
        	ArrayList list = new ArrayList();
        	for(Object[] os:(ArrayList<Object[]>)rs.retVal){
        		ImportDataBean imp = new ImportDataBean();
                imp.setId(os[0].toString());
                imp.setFlag(Integer.parseInt(os[1].toString()));
                imp.setName(os[2].toString());
                imp.setTargetTable(os[3].toString());
                imp.setModuleType((String)os[4]);
                imp.setModuleParam((String)os[5]);
                list.add(imp);
        	}
        	rs.retVal = list;
        }
        return rs;
    }
    
    /**
     * 根据tableName或display查询tblExcelTableInfo表
     *
     * @param name
     *            String
     * @return Result
     */
    public Result updateFlag(final boolean isStop ,final String[] keyIds) {    	
        final Result rs = new Result();
        if(keyIds == null && keyIds.length == 0){
        	return rs;
        }
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	String sql = "update tblImportData set flag = "+(isStop?2:1)+" where id=? ";
                            PreparedStatement st = conn.prepareStatement(sql);
                            for(String id:keyIds){
	                            st.setString(1,id);
	                            st.addBatch();
                            }
                            st.executeBatch();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
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
    
    
    
    /**
     * 根据tableName或display查询tblExcelTableInfo表
     *
     * @param name
     *            String
     * @return Result
     */
    public Result getImportDataByTableName(final String name,final String moduleType) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            String sql = "select * from tblImportData where targetTable=?";
                            if(moduleType!= null && moduleType.length() >0){
                            	sql += " and moduleType=? ";
                            }
                            PreparedStatement st = conn.prepareStatement(sql);
                            st.setString(1, name) ;
                            if(moduleType!= null && moduleType.length() >0){
                            	st.setString(2, moduleType) ;
                            }
                            ResultSet rset = st.executeQuery();
                            ImportDataBean imp = new ImportDataBean();
                            if (rset.next()) {
                                imp.setId(rset.getString("id"));
                                imp.setFlag(rset.getInt("flag"));
                                imp.setName(rset.getString("name"));
                                imp.setTargetTable(rset.getString("targetTable"));
                                imp.setModuleType(rset.getString("moduleType"));
                                imp.setModuleParam(rset.getString("moduleParam"));
                                rs.setRetVal(imp);
                            }else{
                            	rs.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
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

    public Result getMainTable(final String viewTableName,final String viewFieldName,
                               final String viewSaveField,final String val,final String locale) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                            SQLException {
                        try {
                            String sql = "";
                            if(locale.length()>0){
                            	sql = "select a."+viewSaveField+" from "+viewTableName+" a ,tblLanguage b where a."+viewFieldName+"=b.id and b."+locale+" = ?"; ;
                            }else{
                            	sql = "select "+viewSaveField+" from "+viewTableName+" where "+viewFieldName+" = ?";
                            }
                            PreparedStatement st = conn.prepareStatement(sql);
                            st.setString(1,val);
                            ResultSet rset = st.executeQuery();
                            if (rset.next()) {
                                rs.setRetVal(rset.getString(1));
                            } else {
                                rs.setRetCode(ErrorCanst.ER_NO_DATA);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
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
    
    public Result insertMainTable(final String viewTableName,final String viewFieldName,
            final String viewSaveField,final String val,final HashMap<String,ExcelFieldInfoBean> excelMap,
            final String locale,MessageResources resources,LoginBean loginBean,String path,
            final Hashtable props,final MOperation mop) {
    	final Result rs = new Result();
    	int retCode = DBUtil.execute(new IfDB() {
    		public int exec(Session session) {
    			session.doWork(new Work() {
    				public void execute(Connection conn) throws  SQLException {
    					try {
    						String sql="select "+viewSaveField+" from  "+viewTableName+" where  ";
    						List<DBFieldInfoBean> list=DDLOperation.getTableInfo(BaseEnv.tableInfos, viewTableName).getFieldInfos();
    						for(int k=0;k<list.size();k++){
    							DBFieldInfoBean dbf=list.get(k);
    							if(dbf.getDisplay()!=null&&excelMap.get(dbf.getDisplay().get(locale))!=null){
    								ExcelFieldInfoBean valTemp=excelMap.get(dbf.getDisplay().get(locale));
    								if(valTemp!=null&&valTemp.getValue()!=null&&valTemp.getValue().toString().length()>0){
    									sql+=dbf.getFieldName()+"='"+valTemp.getValue()+"' and ";
    								}
    							}
    						}
    						sql=sql.substring(0,sql.length()-4);    
    						BaseEnv.log.debug(sql);
    						Statement st = conn.createStatement();
    						ResultSet rset=st.executeQuery(sql);
    						if(rset.next()){
    							rs.setRetVal(rset.getString(1));
    						}else {
    							rs.setRetCode(ErrorCanst.ER_NO_DATA);
    						}
    					} catch (Exception ex) {
    						ex.printStackTrace();
    						rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
    						rs.setRetVal(ex.getMessage());
    						return;
    					}
    				}
    			});
    			return rs.getRetCode();
    		}
    	});
    	if(rs.getRetCode()==ErrorCanst.ER_NO_DATA){
    		OAWorkFlowTemplate workFlow = BaseEnv.workFlowInfo.get(viewTableName);
    		UserFunctionMgt userMgt = new UserFunctionMgt();
    		DynDBManager dbmgt = new DynDBManager();
    		HashMap values = new HashMap();
    		List<DBFieldInfoBean> list=DDLOperation.getTableInfo(BaseEnv.tableInfos, viewTableName).getFieldInfos();
    		for(int k=0;k<list.size();k++){
    			DBFieldInfoBean dbf=list.get(k);
    			if(dbf.getDisplay()!=null){
    				ExcelFieldInfoBean valTemp=(ExcelFieldInfoBean)excelMap.get(dbf.getDisplay().get(locale));
    				if(valTemp!=null&&valTemp.getValue()!=null&&valTemp.getValue().toString().length()>0){
    					values.put(dbf.getFieldName(), valTemp.getValue());
    				}else{
    					DBFieldInfoBean bean = DDLOperation.getFieldInfo(
    							BaseEnv.tableInfos, viewTableName,dbf.getFieldName());
    					if (bean != null) {
    						if (bean.getDefaultValue() != null) {
    							values.put(dbf.getFieldName(), bean.getDefValue());
    						} else {
    							values.put(dbf.getFieldName(), "");
    						}
    					}
    				}
    			}
	  	 	 }
    		DBTableInfoBean insertTable = DDLOperation.getTableInfo(
    				BaseEnv.tableInfos, viewTableName);
    		Result rs_initDBField = userMgt.initDBFieldInfo(insertTable,loginBean, values, "", workFlow);

		
    		try{
    			new UserFunctionMgt().setDefault(insertTable, values, loginBean.getId());
    			Locale loc =new Locale(locale);	
    			rs_initDBField = dbmgt.add(insertTable.getTableName(),BaseEnv.tableInfos, 
    					values,loginBean.getId(), path, "",resources, loc ,"",loginBean,workFlow,"false",props);
    			rs.setRetVal(values.get(viewSaveField));
    		}catch(Exception ex){
    			ex.printStackTrace();
    			rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
    		}
    	}

    	rs.setRetCode(retCode);
		return rs;
	}



    /**
     * 根据id删除表及所有字段
     *
     * @param referId
     *            String
     * @return Result
     */
    public Result deleteImportData(final String[] ids) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            for(String id:ids){
                                String sql = "delete tblImportData where id=?";
                                // 如标题不为空，则做标题模糊查询

                                PreparedStatement st = conn.prepareStatement(
                                        sql);
                                st.setString(1,id);
                                st.execute();

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
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


    /**
     * 查看excel模板是否启用
     *
     * @param tableName
     *            String
     * @return boolean
     */
    public Result isUsed(final String id) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try {
                            String sql = "select name,targetTable from tblImportData where flag=1 and id = ?";
                            PreparedStatement st = conn.prepareStatement(sql);
                            st.setString(1, id);
                            ResultSet rset = st.executeQuery();
                            if (rset.next()) {
                            	rs.setRetVal(rset.getString("name")+"||"+rset.getString("targetTable")) ;
                            } else {
                                rs.setRetCode(ErrorCanst.RET_ID_NO_VALUE_ERROR);
                                return;
                            }
                        } catch (Exception ex) {
                            BaseEnv.log.error("ImportDataMgt.isUsed Error",ex);
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            rs.setRetVal(ex.getMessage());
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.setRetCode(retCode);
        return rs ;
    }
    
    /**
	 * 查询父类的classCode
	 *
	 * @param tableInfo DBTableInfoBean
	 * @return boolean
	 */
	public Result getParentCode(final String value,final DBTableInfoBean tableInfo) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select classCode from "+tableInfo.getTableName()+" where " ;
							for(DBFieldInfoBean field : tableInfo.getFieldInfos()){
								if("RowMarker".equals(field.getFieldSysType())){
									sql += field.getFieldName()+" = '"+value+"' or " ;
								}
							}
							sql = sql.substring(0,sql.length()-3) ;
							PreparedStatement pss = conn.prepareStatement(sql) ;
							BaseEnv.log.debug("ImportDataMgt.getParentCode 导入时查找父类:"+sql);
							ResultSet rss = pss.executeQuery() ;
							if(rss.next()){
								rs.setRetVal(rss.getString("classCode")) ;
							}else{
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ImportDataMgt.getParentCode Error", ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs ;
	}
	
	public HashMap mappRelation(final List<String []> title,final String targetTable,final String locale){
		final HashMap map=new HashMap();
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select tableName from tblDBTableInfo where  languageId in (select id from tblLanguage where "+locale+" in (";
							for(int i=0;i<title.size();i++){
								String []tit=title.get(i);
								sql+="'"+tit[0]+"',";
							}
							sql=sql.substring(0,sql.length()-1);
							sql+=")) order by tableType";
							
							PreparedStatement pss = conn.prepareStatement(sql) ;
							ResultSet rss = pss.executeQuery() ;
							ArrayList resource=new ArrayList();
							while(rss.next()){
								resource.add(rss.getString(1));
							}
							
							//查询主表映射
							sql = "select childTableField,mostlyTableField from tbltableMapped where mostlyTable='"+
							resource.get(0)+"' and childTable='" + targetTable + "'";
							
							pss = conn.prepareStatement(sql) ;
							rss = pss.executeQuery() ;
							while(rss.next()){
								map.put(targetTable+"_"+rss.getString(1), resource.get(0)+"_"+rss.getString(2));
							}
							
							//查询明细表映射
							ArrayList childTables=DDLOperation.getChildTables(targetTable, BaseEnv.tableInfos);
							for(int i=0;i<childTables.size();i++){
								DBTableInfoBean child=(DBTableInfoBean)childTables.get(i);
								for(int j=1;j<resource.size();j++){
									sql = "select childTableField,mostlyTableField from tbltableMapped where mostlyTable='"+
									resource.get(j)+"' and childTable='" + child.getTableName() + "'";
									pss = conn.prepareStatement(sql) ;
									rss = pss.executeQuery() ;
									boolean flag=false;
									while(rss.next()){
										flag=true;
										map.put(child.getTableName()+"_"+rss.getString(1), resource.get(j)+"_"+rss.getString(2));
									}
									if(flag)break;
								}
							}
							
							//设置源表中字段的显示名
							sql = "select tableName,fieldName,name from tblImportField where f_ref=(select id from tblImportData where targetTable=?)";
							pss = conn.prepareStatement(sql);
							pss.setString(1,resource.get(0).toString());
                            rss = pss.executeQuery();
                            HashMap resourceImport=new HashMap();
                            while (rss.next()) {
                            	resourceImport.put(rss.getString(1)+"_"+rss.getString(2), rss.getString(3));
                            }
							
                            //设置目标表对应源表的显示名
							Iterator it=map.keySet().iterator();
							while(it.hasNext()){
								Object ttableField=it.next();
								Object stableField=map.get(ttableField);
								if(resourceImport.get(stableField)!=null){
									map.put(ttableField, resourceImport.get(stableField));
								}
							}
						} catch (Exception ex) {
							BaseEnv.log.error("ImportDataMgt.getParentCode Error", ex);
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							rs.setRetVal(ex.getMessage());
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);		
		return map;
	}
	
	
   public Result queryPopSql(final String tsql,final PopupSelectBean selectBean,final String locale) {	   
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = tsql;
							sql = sql.replaceAll("(?i)\\bEMPRIGHT\\b", ""); //去掉语句中的部门临时标志
							sql = sql.replaceAll("(?i)\\bNORIGHT\\b", ""); //去掉语句中的部门临时标志
			                //如果是分级报表，且不能选择父级，
							boolean isSavePf = false;
			                if(selectBean.getClassCode() != null && selectBean.getClassCode().length() > 0 && !selectBean.isSaveParentFlag()){
			                	String scl = selectBean.getClassCode();
			                	scl = scl.replaceFirst("classCode", "isCatalog");
			                	sql = "select \n"+scl+",\n"+sql.substring(sql.indexOf("select")+6);
			                	isSavePf = true;
			                	BaseEnv.log.debug("ImportDataMgt.queryPopSql 如果是分级报表，查询是否选择了父级:\r\n"+sql);
			                }
			                
							
							KRLanguageQuery klQuery = new KRLanguageQuery();
							Statement stmt = connection.createStatement();
							ResultSet rss = stmt.executeQuery(sql);
							if (rss.next()) {
								int size = selectBean.getAllFields().size();
								if(isSavePf){
									size++;
								}
								Object[] os  = new Object[size];
								int cid = 0;
								for (int i = 0; i < size; i++) { //如果是分级报表，且不能选择父级，则把值放入cid数组的最后
									if(isSavePf && i == 0){
										os[size-1] = rss.getObject(i + 1);
									}else{
										Object value = rss.getObject(i + 1);
										value = value == null ? "" : value.toString().trim();
										os[cid] = value;
										PopField field = (PopField) selectBean.getAllFields().get(cid);
										DBFieldInfoBean fb = GlobalsTool.getFieldBean(field.fieldName.substring(0,field.fieldName.indexOf(".")),field.fieldName.substring(field.fieldName.indexOf(".") + 1));
										if (fb != null && fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
											klQuery.addLanguageId(value.toString());
										}
										cid++;	
									}
								}
								// 根据languageID取得显示名
								HashMap map = klQuery.query(connection);
								for (int j = 0; j < selectBean.getAllFields().size(); j++) {
									PopField field = (PopField) selectBean.getAllFields().get(j);
									DBFieldInfoBean fb = GlobalsTool.getFieldBean(field.fieldName.substring(0,field.fieldName.indexOf(".")),field.fieldName.substring(field.fieldName.indexOf(".") + 1));
									if (fb != null && fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
										KRLanguage lan = (KRLanguage) map.get(os[j]);
										if (lan != null) {
											os[j] = lan.get(locale);
										}
									}
								}
								rs.setRetVal(os) ;
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
   
   public boolean goodsExist(final String goodsNumber) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "select * from tblGoods where GoodsNumber=?";
							PreparedStatement stmt = connection.prepareStatement(sql);
							stmt.setString(1, goodsNumber);
							ResultSet rss = stmt.executeQuery();
							if (rss.next()) {
								rs.setRetVal( "true");
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs.retVal != null && rs.retVal.equals("true")?true:false;
   }
   /**
    * 针对信德缘导入
    * @param pp
    * @param ycz
    * @param lb
    * @param bscz
    * @param bsys
    * @param ccgg
    * @param ksd
    * @param kh
    * @param dl
    * @return
    */
   public boolean goodsExist(String pp,String ycz,String lb,String bscz,String bsys,String ccgg,String ksd,String kh,String dl){
		boolean ret = false;		
		ArrayList param = new ArrayList();
		param.add(pp==null?"":pp);
		param.add(ycz==null?"":ycz);
		param.add(lb==null?"":lb);
		param.add(bscz==null?"":bscz);
		param.add(bsys==null?"":bsys);
		param.add(ccgg==null?"":ccgg);
		param.add(ksd==null?"":ksd);
		param.add(kh==null?"":kh);
		param.add(dl==null?"":dl);
		String sql="select id from tblGoods where pp=? and ycz=? and lb=? and bscz=? and bsys=? and ccgg=? and ksd=? and kh=? and dl=?";
		
		 
		Result rs = this.sqlList(sql, param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
			if(((ArrayList<Object[]>)rs.retVal).size()==0){
				ret = true;
			}
		}		
		return ret;
  }
   
   	public Result findDistrictByFullName(String fucllName){
		String sql = "select classCode from tblDistrict where DistrictFullName = '" + fucllName + "'";
		List list = new ArrayList();
		return this.sqlList(sql, list);
	}
   	
   	public Result findTradeByFullName(String fucllName){
		String hql = "from WorkProfessionBean  where name  = '" + fucllName + "'";
		List list = new ArrayList();
		return this.list(hql, list);
	}
   	
   	/**
   	 * 根据中文查询人员或部门ID
   	 * @param name
   	 * @param selectType
   	 * @return
   	 */
   	public String getEmpIdOrDeptCodeByName(String name,String selectType){
   		String returnVal = "";
   		String sql = "SELECT id FROM tblEmployee WHERE EmpFullName=?";//查询人员
   		if("dept".equals(selectType)){
   			sql = "SELECT classCode FROM tblDepartMent WHERE DeptFullName=?";//查询部门
   		}
   		ArrayList param = new ArrayList();
   		param.add(name);
   		Result rs = sqlList(sql, param);
   		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
   			ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
   			if(list!=null && list.size()>0){
   				returnVal = String.valueOf(GlobalsTool.get(list.get(0),0));
   			}
   		}
   		
   		return returnVal;
   	}
}
