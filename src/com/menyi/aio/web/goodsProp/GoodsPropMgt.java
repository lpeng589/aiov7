package com.menyi.aio.web.goodsProp;

import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.Result;
import com.menyi.web.util.ConvertToSql;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.TestRW;

import org.hibernate.Session;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.KRLanguage;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.dyndb.DDLOperation;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.customize.PopField;
import com.menyi.aio.web.customize.PopupSelectBean;
import com.menyi.aio.bean.GoodsPropInfoBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import com.dbfactory.DBCanstant;
import org.hibernate.Query;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import com.menyi.web.util.BaseEnv;
import java.sql.Connection;
import org.hibernate.jdbc.Work;
import com.menyi.aio.bean.GoodsPropEnumItemBean;
import java.util.Map;
import java.util.Iterator;
import java.sql.ResultSet;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.web.util.KRLanguageQuery;
import java.util.HashMap;

/**
 * <p>Title: </p>
 *
 * <p>Description:
 *    ��Ʒ���Թ���ģ������ݿ�����ࣺ
 *    tblGoodsPropInfo��Ʒ���Ա�
 *    tblGoodsPropEnumItem��Ʒ����ֵ��(��Ӧ����ΪtblGoodsPropInfo)
 * </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: �����</p>
 *
 * @author �����
 * @version 1.0
 */

public class GoodsPropMgt extends DBManager {
    /**
     * ������Ʒ����
     * @param bean GoodsPropInfoBean
     * @return Result
     */
    public Result add(final GoodsPropInfoBean bean, final String localStr,final String path) {
        //���û��෽��addBeanִ�в������
        final Result rs = new Result();
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(final Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        try {
                            Result rstemp = addBean(bean, session);
                            //��Ӷ����Ա�
                            KRLanguageQuery.saveToDB(bean.getDisplay().map, bean.getDisplay().getId(), connection);
                            for (int i = 0; i < bean.getEnumItem().size(); i++) {
                                GoodsPropEnumItemBean enumBean = (GoodsPropEnumItemBean) bean.getEnumItem().get(i);
                                if (enumBean.getDisplay() != null)
                                	KRLanguageQuery.saveToDB(enumBean.getDisplay().map, enumBean.getDisplay().getId(), connection);
                            }
                            rs.retCode = rstemp.getRetCode();
                            if (rstemp.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                                //�������Գɹ����Զ�������Ʒ�����ӱ�
                                Statement cs = connection.createStatement();
                                String tableName = "tblGoodsProp_" + bean.getPropName();
                                String tableType = "0";
                                if (bean.getIsUsed() == 1 && bean.getJoinTable() == 1&&bean.getIsSequence()==2) { //�����Ʒ�������ö��ҹ�����Ʒ��������Ʒ��������ʾ
                                    tableType = "1";
                                }
                                String tableId = IDGenerater.getId();
                                String enumValueId = IDGenerater.getId();
                                String scID = IDGenerater.getId();
                                cs.addBatch("if exists(select * from sysobjects where name='" + tableName + "') begin " +
                                            "exec proc_deleteExistsTable @tableName='" + tableName + "' " + "drop table " + tableName +
                                            " end ");
                                cs.addBatch(" create table " + tableName + " (id varchar(30) not null ,f_ref varchar(30) null,workFlowNode varchar(30) null,workFlowNodeName varchar(30) null,enumValue varchar(60) not null,SCompanyID varchar(30) null, primary key (id),propId varchar(30) null)");
                                cs.addBatch(" insert into tblDBTableInfo (id,tableName,tableType,perantTableName,udType,updateAble,fieldCalculate,createBy,createTime,lastUpdateBy,lastUpdateTime,approveField,approveFlow,classFlag,draftFlag,extendButton,defRowCount,sysParameter,SCompanyID,isSunCmpShare,isBaseInfo,needsCopy,workFlowNodeName,workFlowNode,languageId) values('" +
                                            tableId + "','" + tableName + "','" + tableType + "','tblGoods','0','0','','" +
                                            bean.getCreateBy() + "','" + bean.getCreateTime() + "','" + bean.getCreateBy() + "','" +
                                            bean.getCreateTime() + "','','','0','0','','1','CommonFunction','','0','0','0','','','" + bean.getDisplay().getId() +
                                            "')");
                                cs.addBatch(" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                            IDGenerater.getId() + "','f_ref','2','','0','0','0','30','','0','" + tableId +
                                            "','','','0','0','0','','','0','0','','','')");
                                cs.addBatch(" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                            IDGenerater.getId() + "','workFlowNode','2','','100','0','0','30','','0','" + tableId +
                                            "','','','0','0','0','','','0','0','','','')");
                                cs.addBatch(" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                            IDGenerater.getId() + "','workFlowNodeName','2','','100','0','0','30','','0','" + tableId +
                                            "','','','0','0','0','','','0','0','','','')");
                                cs.addBatch(" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode,languageId) values('" +
                                            enumValueId + "','enumValue','2','','2','0','0','60','','0','" + tableId +
                                            "','','SelectEnumValue','200','0','0','','GoodsField','2','0','','','','"+bean.getDisplay().getId()+"')");
                                cs.addBatch(" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                            scID + "','SCompanyID','2','','100','0','100','30','','0','" + tableId +
                                            "','','','0','0','0','','','0','0','','','')");
                                cs.addBatch(" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                            IDGenerater.getId() + "','id','2','','0','1','0','30','','0','" + tableId +
                                            "','','','0','0','0','','','0','0','','','')");
                                String propDisplayId=IDGenerater.getId();
                                cs.addBatch(" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode,languageId) values('" +
                                        IDGenerater.getId() + "','propId','2','','3','0','0','30','','0','" + tableId +
                                        "','','','0','0','0','','','0','0','','','','"+propDisplayId+"')");
                                cs.addBatch("insert into tblLanguage(id,zh_CN,zh_TW,zh_HK,en) values('"+propDisplayId+"','����ID','����ID','����ID','Property ID')");
                                propDisplayId=IDGenerater.getId();
                                cs.addBatch(" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode,languageId) values('" +
                                        IDGenerater.getId() + "','groupName','2','','3','0','0','100','','0','" + tableId +
                                        "','','','0','0','0','','','0','0','','','','"+propDisplayId+"')");
                                cs.addBatch("insert into tblLanguage(id,zh_CN,zh_TW,zh_HK,en) values('"+propDisplayId+"','����','�M��','�M��','Group name')");
                                //����Լ������
                                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
                                String fk = "FK_f_ref_id" + sdf.format(new Date());
                                String contranst = "alter table " + tableName +
                                    " add constraint " + fk +
                                    " foreign key (f_ref) references tblGoods ON DELETE CASCADE ON UPDATE CASCADE ";
                                cs.addBatch(contranst);
                               
                                cs.executeBatch();
                                DDLOperation.updateRefreshTime("tableInfo", connection);//�����ڴ�
                                DDLOperation.updateRefreshTime("propInfo", connection);//�����ڴ�
                                String toSql="if exists(select * from sysobjects where name='" + tableName + "') begin " +
                                "exec proc_deleteExistsTable @tableName='" + tableName + "' " + "drop table " + tableName +
                                " end \n";
                                toSql+=" create table " + tableName + " (id varchar(30) not null ,f_ref varchar(30) null,workFlowNode varchar(30) null,workFlowNodeName varchar(30) null,enumValue varchar(60) not null,SCompanyID varchar(30) null, primary key (id))\n";
                                toSql+=" insert into tblDBTableInfo (id,tableName,tableType,perantTableName,udType,updateAble,fieldCalculate,createBy,createTime,lastUpdateBy,lastUpdateTime,approveField,approveFlow,classFlag,draftFlag,extendButton,defRowCount,sysParameter,SCompanyID,isSunCmpShare,isBaseInfo,needsCopy,workFlowNodeName,workFlowNode,languageId) values('" +
                                tableId + "','" + tableName + "','" + tableType + "','tblGoods','0','0','','" +
                                bean.getCreateBy() + "','" + bean.getCreateTime() + "','" + bean.getCreateBy() + "','" +
                                bean.getCreateTime() + "','','','0','0','','1','CommonFunction','','0','0','0','','','" + bean.getDisplay().getId() +
                                "')\n";
                                toSql+=" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                IDGenerater.getId() + "','f_ref','2','','0','0','0','30','','0','" + tableId +
                                "','','','0','0','0','','','0','0','','','')\n";
                                toSql+=" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                IDGenerater.getId() + "','workFlowNode','2','','100','0','0','30','','0','" + tableId +
                                "','','','0','0','0','','','0','0','','','')\n";
                                toSql+=" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                IDGenerater.getId() + "','workFlowNodeName','2','','100','0','0','30','','0','" + tableId +
                                "','','','0','0','0','','','0','0','','','')\n";
                                toSql+=" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode,languageId) values('" +
                                enumValueId + "','enumValue','2','','2','0','0','60','','0','" + tableId +
                                "','','SelectEnumValue','200','0','0','','GoodsField','2','0','','','','"+bean.getDisplay().getId()+"')\n";
                                toSql+=" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                scID + "','SCompanyID','2','','100','0','100','30','','0','" + tableId +
                                "','','','0','0','0','','','0','0','','','')\n";
                                toSql+=" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                IDGenerater.getId() + "','id','2','','0','1','0','30','','0','" + tableId +
                                "','','','0','0','0','','','0','0','','','')\n";
                                toSql+=" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                IDGenerater.getId() + "','propId','2','','0','0','0','30','','0','" + tableId +
                                "','','','0','0','0','','','0','0','','','')\n";
                                toSql+=" insert into tblDBFieldInfo (id,fieldName,fieldType,defaultValue,inputType,isNull,listOrder,maxLength,refEnumerationName,udType,tableId,calculate,inputValue,width,isunique,isStat,SCompanyID,fieldSysType,inputTypeOld,digits,fieldIdentityStr,workFlowNodeName,workFlowNode) values('" +
                                IDGenerater.getId() + "','groupName','2','','0','0','0','100','','0','" + tableId +
                                "','','','0','0','0','','','0','0','','','')\n";
                                toSql+=contranst;
                                String mainSql=ConvertToSql.getSqlOfAddProp(connection, bean);
                                TestRW.saveToSql(path, mainSql+toSql);
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
        rs.setRetCode(retVal);
        return rs;

    }

    /**
     * ��Ʒ���Բ�ѯ
     * @param name String
     * @param pageNo int
     * @param pageSize int
     * @return Result
     */
    public Result query(final String name, int pageNo, int pageSize) {
        //��������
        List param = new ArrayList();
        String hql = "select bean from GoodsPropInfoBean as bean ";
        //����ⲻΪ�գ���������ģ����ѯ
        final Result rs_id = new Result() ;
        if (name != null && name.length() != 0) {
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection connection)
								throws SQLException {
							Connection conn = connection;
							try {
								ArrayList<String> listId = new ArrayList<String>();
								String sql = "select languageId from tblGoodsPropInfo a left join tblLanguage b on a.languageId = b.id where zh_CN like ?";
								PreparedStatement ps = conn.prepareStatement(sql);
								ps.setString(1, "%" + name + "%");
								ResultSet rs = ps.executeQuery();
								while (rs.next()) {
									listId.add(rs.getString("languageId"));
								}
								rs_id.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
								rs_id.setRetVal(listId);
							} catch (Exception ex) {
								ex.printStackTrace();
								rs_id.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
								rs_id.setRetVal(ex.getMessage());
								return;
							}
						}
					});
					return rs_id.getRetCode();
				}
			});   
        }
		
        ArrayList<String> listId = new ArrayList<String>() ;
		if(rs_id.retCode == ErrorCanst.DEFAULT_SUCCESS){
			listId = (ArrayList<String>) rs_id.getRetVal();
		}
		if(listId!=null && listId.size()>0){
			hql += " where " ;
			for(String str : listId){
				hql += " bean.languageId = ? or " ;
				param.add(str) ;
			}
		}	
		if (name != null && name.length() != 0) {
			if(!hql.contains(" where")){
				hql+=" where " ;
			}
	        hql += " upper(bean.propName) like '%'||?||'%'";
	        param.add(name.trim().toUpperCase());
	    }
        //����list���ؽ��
        final Result rs = list(hql, param, pageNo, pageSize, true);
        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList) rs.getRetVal()).size() > 0) {
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            try {
                                KRLanguageQuery query = new KRLanguageQuery();
                                for (int i = 0; i < ((ArrayList) rs.getRetVal()).size(); i++) {
                                    GoodsPropInfoBean gib = (GoodsPropInfoBean) ((ArrayList) rs.getRetVal()).get(i);
                                    query.addLanguageId(gib.getLanguageId());
                                    for (int j = 0; j < gib.getEnumItem().size(); j++) {
                                        GoodsPropEnumItemBean geb = (GoodsPropEnumItemBean) gib.getEnumItem().get(j);
                                        query.addLanguageId(geb.getLanguageId());
                                    }
                                }
                                HashMap map = query.query(connection);
                                for (int i = 0; i < ((ArrayList) rs.getRetVal()).size(); i++) {
                                    GoodsPropInfoBean gib = (GoodsPropInfoBean) ((ArrayList) rs.getRetVal()).get(i);
                                    gib.setDisplay((KRLanguage) map.get(gib.getLanguageId()));
                                    for (int j = 0; j < gib.getEnumItem().size(); j++) {
                                        GoodsPropEnumItemBean geb = (GoodsPropEnumItemBean) gib.getEnumItem().get(j);
                                        geb.setDisplay((KRLanguage) map.get(geb.getLanguageId()));
                                    }
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
            rs.setRetCode(retVal);
        }
        return rs;
    }

    public Result queryEnumItem(String itemId) {
        //��������
        List param = new ArrayList();
        String hql = "select itemBean from GoodsPropEnumItemBean as itemBean,GoodsPropInfoBean as propBean where itemBean.enumValue=? and propBean.id=itemBean.propBean.id";
        param.add(itemId);
        final Result rs = list(hql, param);
        //����list���ؽ��
        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList) rs.getRetVal()).size() > 0) {

            int retVal = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            final KRLanguageQuery klQuery = new KRLanguageQuery();
                            for (int k = 0; k < ((ArrayList) rs.getRetVal()).size(); k++) {
                            	GoodsPropEnumItemBean eib = (GoodsPropEnumItemBean) ((ArrayList) rs.getRetVal()).get(k);
                                    klQuery.addLanguageId(eib.getLanguageId());
                            }
                            HashMap map = klQuery.query(connection);
                            for (int k = 0; k < ((ArrayList) rs.getRetVal()).size(); k++) {
                            	GoodsPropEnumItemBean eib = (GoodsPropEnumItemBean) ((ArrayList) rs.getRetVal()).get(k);
                                    eib.setDisplay((KRLanguage) map.get(eib.getLanguageId()));
                            }

                        }
                    });
                    return ErrorCanst.DEFAULT_SUCCESS;
                }
            });
            rs.setRetCode(retVal);
        }
        return rs;
    }

    /**
     * �鿴�������Ƿ����
     * @param propDisplay String
     * @return Result
     */
//    public Result queryPropDisplay(final String propDisplay,final String language) {
//        final Result rs = new Result();
//        int retCode = DBUtil.execute(new IfDB() {
//            public int exec(Session session) {
//                session.doWork(new Work() {
//                    public void execute(Connection connection) throws
//                        SQLException {
//                        Connection conn = connection;
//                        try {
//                            Statement cs = conn.createStatement();
//                            String sql = "select count(*) from tblLanguage where id in (select languageId from tblGoodsPropInfo) and "+language+"='"+propDisplay+"'";
//                            ResultSet rss = cs.executeQuery(sql);
//                            boolean ishave = false;
//                            if (rss.next()) {
//                                ishave = true;
//                            }
//                            rss.close();
//                            rs.setRetVal(ishave);
//                        } catch (SQLException ex) {
//                            rs.setRetCode(ErrorCanst.
//                                          DEFAULT_FAILURE);
//                            return;
//                        }
//                    }
//                });
//                return rs.getRetCode();
//            }
//        });
//        rs.setRetCode(retCode);
//        return rs;
//
//    }

    /**
     * �鿴���Ա�ʾ�Ƿ����
     * @param propName String
     * @return Result
     */
    public Result queryPropName(String propName) {
        //��������
        List param = new ArrayList();
        param.add(propName);
        String hql = "select bean from GoodsPropInfoBean as bean where bean.propName = ?";
        //����list���ؽ��
        final Result rs = list(hql, param);
        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList) rs.getRetVal()).size() > 0) {
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            try {
                                KRLanguageQuery query = new KRLanguageQuery();

                                GoodsPropInfoBean gib = (GoodsPropInfoBean)((ArrayList) rs.getRetVal()).get(0);
                                query.addLanguageId(gib.getLanguageId());
                                for (int j = 0; j < gib.getEnumItem().size(); j++) {
                                    GoodsPropEnumItemBean geb = (GoodsPropEnumItemBean) gib.getEnumItem().get(j);
                                    query.addLanguageId(geb.getLanguageId());
                                }
                                HashMap map = query.query(connection);

                                gib.setDisplay((KRLanguage) map.get(gib.getLanguageId()));
                                for (int j = 0; j < gib.getEnumItem().size(); j++) {
                                    GoodsPropEnumItemBean geb = (GoodsPropEnumItemBean) gib.getEnumItem().get(j);
                                    geb.setDisplay((KRLanguage) map.get(geb.getLanguageId()));
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
            });rs.setRetCode(retVal);
        }

        return rs;
    }
    
    /**
     * ��ƥ���к�ɨ����
     * @param sql
     * @param selectBean
     * @param locale
     * @param seq
     * @return
     */
    public Result queryGoodsBySeq(final String stockCode,final String seq) {    	
		final Result rs = new Result();
		if(seq == null || seq.length()==0){
    		return rs;
    	}
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = " select '' as StockCode,'' as StockFullName,tblLabel.GoodsCode,GoodsNumber from tblLabel join tblGoods on tblLabel.GoodsCode=tblGoods.classCode where seq=? " +
									" union select tblstockdet.StockCode,StockFullName,tblstockdet.GoodsCode,GoodsNumber from tblstockdet join tblGoods on tblstockdet.GoodsCode=tblGoods.classCode " +
									" join tblstock on tblstockdet.StockCode=tblstock.classCode where totalQty>0 and lastMarker=1 and seq=?  ";
							if(stockCode !=null && stockCode.length() > 0 ){
								sql += " and tblstockdet.stockCode=? ";
							}
							
							PreparedStatement stmt = connection.prepareStatement(sql);		
							stmt.setString(1, seq);
							stmt.setString(2, seq);
							if(stockCode !=null && stockCode.length() > 0 ){
								stmt.setString(3, stockCode);
							}
							ResultSet rss = stmt.executeQuery();
							if (rss.next()) {
								String[] str = new String[]{ rss.getString("StockCode"),rss.getString("StockFullName"),rss.getString("GoodsCode"),rss.getString("GoodsNumber")};								
								rs.setRetVal(str);
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
    
    /**
     * �ж�seq�Ƿ���ڣ�������ڣ���������Ʒ��Ϣ
     * @param sql
     * @param selectBean
     * @param locale
     * @param seq
     * @return
     */
    public Result queryGoodsBySeq(final String tsql,final PopupSelectBean selectBean,final String locale,final String seq) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = tsql;
							//ȥ��sql �����е�='@ValueofDB:
							while(sql.indexOf("='@ValueofDB:")>-1){
								int pos = sql.indexOf("='@ValueofDB:");
								int pos1 = sql.lastIndexOf("and ",pos);
								int pos2 = sql.indexOf("'",pos+3);
								sql = sql.substring(0,pos1)+sql.substring(pos2+1);
							}
							while(sql.indexOf("='@Sess:")>-1){
								int pos = sql.indexOf("='@Sess:");
								int pos1 = sql.lastIndexOf("and ",pos);
								int pos2 = sql.indexOf("'",pos+3);
								sql = sql.substring(0,pos1)+sql.substring(pos2+1);
							}
							KRLanguageQuery klQuery = new KRLanguageQuery();
							Statement stmt = connection.createStatement();
							ResultSet rss = stmt.executeQuery(sql);
							if (rss.next()) {
								int size = selectBean.getAllFields().size();
								Object[] os  = new Object[size];
								int cid = 0;
								for (int i = 0; i < size; i++) {
									Object value = rss.getObject(i + 1);
									value = value == null ? "" : value.toString().trim();
									os[cid] = value;
									cid++;
									PopField field = (PopField) selectBean.getAllFields().get(i);
									DBFieldInfoBean fb = GlobalsTool.getFieldBean(field.fieldName.substring(0,field.fieldName.indexOf(".")),field.fieldName.substring(field.fieldName.indexOf(".") + 1));
									if (fb != null && fb.getInputType() == DBFieldInfoBean.INPUT_LANGUAGE) {
										klQuery.addLanguageId(value.toString());
									}
								}
								// ����languageIDȡ����ʾ��
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
    public Result queryGoodsBySeq(final String sql,final PopupSelectBean selectBean,final String locale) {
        //����list���ؽ��
        final Result rs =new Result();
        rs.setRetVal(new ArrayList());
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            try {
                            	 KRLanguageQuery klQuery = new KRLanguageQuery();
                                Statement stmt=connection.createStatement();
                                ResultSet rss=stmt.executeQuery(sql);
                                while (rss.next()) {
                                	int size=selectBean.getAllFields().size();
                                	Object[] os ;
//                                	if(selectBean.getClassCode()!=null&&selectBean.getClassCode().length()>0){
//                                		os = new Object[size+1];
//                                	}else{
                               		os = new Object[size];
//                                	}
                                    int cid = 0;
                                    
                                    for (int i = 0;i < size;i++) {
                                        Object value = rss.getObject(i + 1);
                                        value = value == null ? "" : value.toString().trim();
                                        os[cid] = value;
                                        cid++;
                                        PopField field=(PopField)selectBean.getAllFields().get(i);
                                        DBFieldInfoBean fb=null;
                                        if(field.fieldName.indexOf(".")>=0){
                                        	fb= GlobalsTool.getFieldBean(field.fieldName.substring(0,field.fieldName.indexOf(".")),
                                        			field.fieldName.substring(field.fieldName.indexOf(".")+1));
                                        }
                                        if(fb==null && field.display !=null &&field.display.indexOf(".") > -1 ){
                                        	fb= GlobalsTool.getFieldBean(field.display.substring(0,field.display.indexOf(".")),
                                        			field.display.substring(field.display.indexOf(".")+1));
                                        }
                                    	
                                    	if(fb!=null&&fb.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE){
                                    		klQuery.addLanguageId(value.toString());
                                    	}
                                    }
                                    
//                                    if(selectBean.getClassCode()!=null&&selectBean.getClassCode().length()>0){
//                                    	os[cid]=rss.getString(cid+1);
//                                    }
                                    ((ArrayList) rs.getRetVal()).add(os);
                                }
                                //����languageIDȡ����ʾ��
                                HashMap map = klQuery.query(connection);
                                ArrayList list=(ArrayList)rs.getRetVal();
                                for(int i=0;i<list.size();i++){
                                	Object[] os=(Object[])list.get(i);
                                	for(int j=0;j<selectBean.getAllFields().size();j++){
                                		PopField field=(PopField)selectBean.getAllFields().get(j);
                                		 DBFieldInfoBean fb=null;
                                         if(field.fieldName.indexOf(".")>=0){
                                         	fb= GlobalsTool.getFieldBean(field.fieldName.substring(0,field.fieldName.indexOf(".")),
                                         			field.fieldName.substring(field.fieldName.indexOf(".")+1));
                                         }
                                         if(fb==null){
                                         	fb= GlobalsTool.getFieldBean(field.display.substring(0,field.display.indexOf(".")),
                                         			field.display.substring(field.display.indexOf(".")+1));
                                         }
                                    	if(fb!=null&&fb.getInputType()==DBFieldInfoBean.INPUT_LANGUAGE){
                                    		KRLanguage lan=(KRLanguage)map.get(os[j]);
                                    		if(lan!=null){
                                    		    os[j]=lan.get(locale);
                                    		}
                                    	}
                                	}
                                	
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
            });rs.setRetCode(retVal);
        return rs;
    }
    
    /**
	 * ��ѯ����ֵ
	 * 
	 * @param propName
	 * @return
	 */
	public Result queryPropValue(final String propId, final String propValue, final String goodsCode,
			final String local) {
		// ����list���ؽ��
		final Result rs = new Result();
		int retVal = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection connection) throws SQLException {
						try {
							String sql = "select PropEName from ViewAttribute where PropMid='" + propId
													+ "' and enumValue='" + propValue + "' and PropEName is not null and len(PropEName)>0";
							Statement stmt = connection.createStatement();
							ResultSet rss = stmt.executeQuery(sql);
							if (rss.next()) {
								rs.setRetVal(rss.getString(1));
							} else {
								rs.setRetVal(null);
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
		rs.setRetCode(retVal);
		return rs;
	}

    /**
	 * ��һ����λ����ϸ��Ϣ
	 * 
	 * @param notepadId
	 *            long ����
	 * @return Result ���ؽ��
	 */
    public Result detail(String id) {
        // ��������
        List param = new ArrayList();
        param.add(id);
        String hql = "select bean from GoodsPropInfoBean as bean where bean.id = ?";
        // ����list���ؽ��
        final Result rs = list(hql, param);
        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList) rs.getRetVal()).size() > 0) {
            rs.retVal = ((ArrayList) rs.getRetVal()).get(0);

            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            try {
                                KRLanguageQuery query = new KRLanguageQuery();

                                GoodsPropInfoBean gib = (GoodsPropInfoBean) rs.retVal;
                                query.addLanguageId(gib.getLanguageId());
                                for (int j = 0; j < gib.getEnumItem().size(); j++) {
                                    GoodsPropEnumItemBean geb = (GoodsPropEnumItemBean) gib.getEnumItem().get(j);
                                    query.addLanguageId(geb.getLanguageId());
                                }
                                HashMap map = query.query(connection);

                                gib.setDisplay((KRLanguage) map.get(gib.getLanguageId()));
                                for (int j = 0; j < gib.getEnumItem().size(); j++) {
                                    GoodsPropEnumItemBean geb = (GoodsPropEnumItemBean) gib.getEnumItem().get(j);
                                    geb.setDisplay((KRLanguage) map.get(geb.getLanguageId()));
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
            rs.setRetCode(retVal);
        }

        return rs;
    }

    /**
     * �޸�һ����λ��¼
     * @param id long
     * @param name String
     * @return Result
     */
//      public Result update(final GoodsPropInfoBean bean,final List<DBTableInfoBean> existTables) {
//          final Result rs = new Result();
//          int retVal = DBUtil.execute(new IfDB() {
//              public int exec(Session session) {
//
//                  Result rstemp = updateBean(bean,session);
//                  for(int i=0;i<existTables.size();i++)
//                  {
//                	  DBTableInfoBean tableInfo=existTables.get(i);
//                	  List<DBFieldInfoBean> fields=tableInfo.getFieldInfos();
//                	  for(int j=0;j<fields.size();j++)
//                	  {
//                		  DBFieldInfoBean field=fields.get(j);
//                		  if(field.getFieldName().equals(bean.getPropName()))
//                		  {
//                			  Map map=field.getDisplay();
//                			  Iterator it=map.keySet().iterator();
//                			  while(it.hasNext())
//                			  {
//                				  DBFieldDisplayBean display=(DBFieldDisplayBean)map.get(it.next());
//
//                			  }
//                		  }
//
//                	  }
//                  }
//                  if (rstemp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
//                      rs.retCode = rstemp.getRetCode();
//                      return rs.retCode;
//                  } else {
//                      rs.setRetVal(rstemp.getRetVal());
//                  }
//                  return rs.retCode;
//              }
//          });
//          rs.retCode = retVal;
//          return rs;
//    }
    public Result update(final GoodsPropInfoBean oldBean,final GoodsPropInfoBean bean, final List<DBTableInfoBean> existTables, final Hashtable tables,final HashMap lgIds) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(final Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try {
                        	 Statement cs = conn.createStatement();
                        	 for (int i = 0; i < oldBean.getEnumItem().size(); i++) {
                                 GoodsPropEnumItemBean itemBean = (GoodsPropEnumItemBean) oldBean.getEnumItem().get(i);
                                 String strSQL = "delete from tblLanguage where id='" + itemBean.getLanguageId()+ "'";
                                 BaseEnv.log.debug(strSQL);
                                 cs.executeUpdate(strSQL);
                             }
                             for (int i = 0; i < oldBean.getEnumItem().size(); i++) {
                                 GoodsPropEnumItemBean itemBean = (GoodsPropEnumItemBean) oldBean.getEnumItem().get(i);
                                 String strSQL = "delete from tblGoodsPropEnumItem where id='" + itemBean.getId() + "'";
                                 BaseEnv.log.debug(strSQL);
                                 cs.executeUpdate(strSQL);
                             }
                             String strSQL="delete from tblLanguage  where id='"+oldBean.getLanguageId()+"'";
                             cs.executeUpdate(strSQL);
                            Result rs2 = updateBean(bean, session); //�޸���Ʒ����
                            //��Ӷ����Ա�
                            KRLanguageQuery.saveToDB(bean.getDisplay().map, bean.getDisplay().getId(), connection);
                            for (int i = 0; i < bean.getEnumItem().size(); i++) {
                                GoodsPropEnumItemBean enumBean = (GoodsPropEnumItemBean) bean.getEnumItem().get(i);
                                if (enumBean.getDisplay() != null)
                                	KRLanguageQuery.saveToDB(enumBean.getDisplay().map, enumBean.getDisplay().getId(), connection);
                            }
                            Iterator itId=lgIds.keySet().iterator();
                            while(itId.hasNext()){//�޸�����ֵ������id
                            	Object key=itId.next();
                            	Object newId=lgIds.get(key);
                            	String upLgSql="update tblGoodsOfProp set languageId='"+newId.toString()+"' where languageId='"+key.toString()+"'";
                            	cs.executeUpdate(upLgSql);
                            }
                            if (rs2.getRetCode() != ErrorCanst.DEFAULT_FAILURE) {
                                //����ɹ����޸Ĵ��ڸ���Ʒ���Եı��еĸ��ֶ�����
                                for (int i = 0; i < existTables.size(); i++) {
                                    DBTableInfoBean tableInfo = existTables.get(i);
                                    List<DBFieldInfoBean> fields = tableInfo.getFieldInfos();
                                    for (int j = 0; j < fields.size(); j++) {
                                        DBFieldInfoBean field = fields.get(j);
                                        if (field.getFieldName().equals(bean.getPropName())) {
                                            //ɾ���ɵĶ�����
                                            String strSql =
                                                "delete from tbllanguage where id in (select languageId from tbldbfieldinfo where id ='" +
                                                field.getId() + "') ";
                                            cs.executeUpdate(strSql);
                                            //�����µĶ�����
                                            DBFieldInfoBean newField=new DBFieldInfoBean();
                                            KRLanguage krlan = new KRLanguage();
                                            krlan.setId(IDGenerater.getId());
                                            krlan.map = bean.getDisplay().map;
                                            newField.setDisplay(krlan);
                                            KRLanguageQuery.saveToDB(newField.getDisplay().map, newField.getDisplay().getId(), connection);
                                            strSql = "update tbldbfieldinfo set languageId='" + newField.getDisplay().getId() + "' where id ='" +
                                                field.getId()+"'";
                                            cs.executeUpdate(strSql);
                                        }

                                    }
                                }
                                //�޸�ʱ����Ʒ��������ӱ�ҲҪ�����޸�
                                Iterator it = tables.keySet().iterator();
                                while (it.hasNext()) {
                                    DBTableInfoBean tableInfo = (DBTableInfoBean) tables.get(it.next());
                                    if (("tblGoodsProp_" + bean.getPropName()).equals(tableInfo.getTableName())) {
                                        String joinTable = "0";
                                        String perantTableName="";
                                        if (bean.getJoinTable() == 1 && bean.getIsUsed() == 1&&bean.getIsSequence()==2) {
                                            joinTable = "1";
                                            perantTableName="tblGoods;";
                                        }
                                        String sql = "update tblDBTableInfo set tableType='" + joinTable + "',languageId='"+bean.getDisplay().getId()+"',perantTableName='"+perantTableName+"' where id='" + tableInfo.getId() +
                                            "'";
                                        cs.executeUpdate(sql);
                                        List<DBFieldInfoBean> fieldsInfo = tableInfo.getFieldInfos();
                                        for (int j = 0; j < fieldsInfo.size(); j++) {
                                            if (fieldsInfo.get(j).getFieldName().equals("enumValue")) {
                                                //ɾ���ɵĶ�����
                                                String strSql ="";
                                                strSql = "update tbldbfieldinfo set languageId='" + bean.getDisplay().getId() +
                                                    "' where id ='" + fieldsInfo.get(j).getId()+"'";
                                                cs.executeUpdate(strSql);
                                            }
                                        }
                                    }
                                }
                                rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                                DDLOperation.updateRefreshTime("tableInfo", connection);//�����ڴ�
                                DDLOperation.updateRefreshTime("propInfo", connection);//�����ڴ�
                            } else {
                                rs.setRetCode(rs2.getRetCode());
                                rs.setRetVal(rs2.getRetVal());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        return rs;

    }

    /**
     * ����ǰɾ���ɵ�����
     * @param rs Result
     * @param bean GoodsPropInfoBean
     * @return int
     */
//    public int execUpdateBeforDelOld(final Result rs, final GoodsPropInfoBean bean) {
//        int retCode = DBUtil.execute(new IfDB() {
//            public int exec(Session session) {
//                session.doWork(new Work() {
//                    public void execute(Connection connection) throws
//                        SQLException {
//                        Connection conn = connection;
//                        try {
//                            Statement cs = conn.createStatement();
//                            for (int i = 0; i < bean.getEnumItem().size(); i++) {
//                                GoodsPropEnumItemBean itemBean = (GoodsPropEnumItemBean) bean.getEnumItem().get(i);
//                                String strSQL = "delete from tblLanguage where id='" + itemBean.getLanguageId()+ "'";
//                                BaseEnv.log.debug(strSQL);
//                                cs.executeUpdate(strSQL);
//                            }
//                            for (int i = 0; i < bean.getEnumItem().size(); i++) {
//                                GoodsPropEnumItemBean itemBean = (GoodsPropEnumItemBean) bean.getEnumItem().get(i);
//                                String strSQL = "delete from tblGoodsPropEnumItem where id='" + itemBean.getId() + "'";
//                                BaseEnv.log.debug(strSQL);
//                                cs.executeUpdate(strSQL);
//                            }
//                            String strSQL="delete from tblLanguage  where id='"+bean.getLanguageId()+"'";
//                            cs.executeUpdate(strSQL);
//
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                            rs.setRetCode(ErrorCanst.
//                                          DEFAULT_FAILURE);
//                            return;
//                        }
//                    }
//                });
//                return rs.getRetCode();
//            }
//        });
//        return retCode;
//
//    }

    /**
     * ɾ��������λ��¼
     * @param ids long[]
     * @return Result
     */
    public Result delete(final List<GoodsPropInfoBean> list, final Hashtable tables,final String path) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(final Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            ArrayList delArray=new ArrayList();
                            delArray.add("--ɾ������\n");
                            for (int j = 0; j < list.size(); j++) {
                                GoodsPropInfoBean bean = list.get(j);
//                                for (int i = 0; i < bean.getEnumItem().size(); i++) {
//                                    GoodsPropEnumItemBean itemBean = (
//                                        GoodsPropEnumItemBean) bean.
//                                        getEnumItem().get(i);
//                                }
                                for (int i = 0; i < bean.getEnumItem().size(); i++) {
                                    GoodsPropEnumItemBean itemBean = (
                                        GoodsPropEnumItemBean) bean.
                                        getEnumItem().get(i);
                                    String strSQL =
                                        "delete from tblGoodsPropEnumItem where id='" +
                                        itemBean.getId() + "'";
                                    BaseEnv.log.debug(strSQL);
                                    cs.executeUpdate(strSQL);
                                    delArray.add(strSQL+"\n");
                                    strSQL="delete from tblLanguage where id='"+itemBean.getLanguageId()+"'";
                                    BaseEnv.log.debug(strSQL);
                                    cs.executeUpdate(strSQL);
                                    delArray.add(strSQL+"\n");
                                }
                                String strSQL =
                                    "delete from tblGoodsPropInfo where id='" +
                                    bean.getId() + "'";
                                BaseEnv.log.debug(strSQL);
                                cs.executeUpdate(strSQL);
                                delArray.add(strSQL+"\n");
                                strSQL =
                                    "delete from tblLanguage where id='" +
                                    bean.getLanguageId() + "'";
                                BaseEnv.log.debug(strSQL);
                                cs.executeUpdate(strSQL);
                                delArray.add(strSQL+"\n");
                                //ɾ�����ɵ���Ʒ�������ӱ�
                                String childPropTable = "tblGoodsProp_" + bean.getPropName();
                                strSQL = "drop table " + childPropTable;
                                cs.executeUpdate(strSQL);
                                delArray.add(strSQL+"\n\n");
                                Result result = deleteBean(childPropTable, DBTableInfoBean.class,
                                    "tableName", session);

                            }
                            DDLOperation.updateRefreshTime("tableInfo", connection);//�����ڴ�
                            DDLOperation.updateRefreshTime("propInfo", connection);//�����ڴ�
                            for(int i=0;i<delArray.size();i++){
                            	TestRW.saveToSql(path,delArray.get(i).toString());
                            	
                            }
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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

//    /**
//     * ��ѯ������
//     * @param id String
//     * @return Result
//     */
//    public Result queryDisplayById(final String id) {
//        final Result rs = new Result();
//        int retCode = DBUtil.execute(new IfDB() {
//            public int exec(Session session) {
//                session.doWork(new Work() {
//                    public void execute(Connection connection) throws
//                        SQLException {
//                        Connection conn = connection;
//                        try {
//                            Statement cs = conn.createStatement();
//                            String sql = "select display from tblGoodsPropDisplay where referid='" + id + "'";
//                            ResultSet rss = cs.executeQuery(sql);
//                            String display = "";
//                            if (rss.next()) {
//                                display = rss.getString("display");
//                            }
//                            rs.setRetVal(display);
//                        } catch (SQLException ex) {
//                            rs.setRetCode(ErrorCanst.
//                                          DEFAULT_FAILURE);
//                            return;
//                        }
//                    }
//                });
//                return rs.getRetCode();
//            }
//        });
//        return rs;
//
//    }

    /**
     * �����������
     * @param rs Result
     * @param fields Map
     * @return int
     */
    public Result propSetAdd(final Map<String, DBFieldInfoBean> fields,final Map<String, DBFieldInfoBean> vfields,final String path) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            Iterator it = fields.keySet().iterator();
                            ArrayList propSetSqls=new ArrayList();
                            propSetSqls.add("--��������(������Ե���)\n");
                            //ѭ����ÿһ������ֶ�
                            while (it.hasNext()) {
                                DBFieldInfoBean fieldInfo = fields.get(it.next());
                                String strSQL = "insert into tblDBFieldInfo(id,fieldName,fieldType,inputType,isNull,listOrder,maxLength,udType,tableId,isunique,isStat,width,inputValue,fieldSysType,defaultValue,languageId) values('" +
                                                fieldInfo.getId() + "','" + fieldInfo.getFieldName() + "'," + fieldInfo.getFieldType() +
                                                "," + fieldInfo.getInputType() + "," +
                                                fieldInfo.getIsNull() + "," + fieldInfo.getListOrder() + "," + fieldInfo.getMaxLength() +
                                                "," + fieldInfo.getUdType() + ",'" + fieldInfo.getTableBean().getId() + "'," +
                                                fieldInfo.getIsUnique() + "," + fieldInfo.getIsStat() + ",200,'" + fieldInfo.getInputValue() +
                                                "','GoodsField','','" + fieldInfo.getDisplay().getId() + "');";
                                BaseEnv.log.debug(strSQL);
                                cs.executeUpdate(strSQL);
                                propSetSqls.add(strSQL+"\n");
                                KRLanguageQuery.saveToDB(fieldInfo.getDisplay().map, fieldInfo.getDisplay().getId(), connection);
                                propSetSqls.add(ConvertToSql.getLanguageStr(fieldInfo.getDisplay())+"\n");
                                String sql = "ALTER TABLE " + fieldInfo.getTableBean().getTableName() + " ADD " + fieldInfo.getFieldName() +
                                             " varchar(5000) ALTER TABLE " + fieldInfo.getTableBean().getTableName() + " ADD  CONSTRAINT DF_" +
                                             fieldInfo.getTableBean().getTableName() + "_" + fieldInfo.getFieldName() +
                                             "  DEFAULT ('') FOR " + fieldInfo.getFieldName();
                                BaseEnv.log.debug(sql);
                                cs.executeUpdate(sql);
                                propSetSqls.add(sql+"\n");
                                sql="update "+fieldInfo.getTableBean().getTableName()+" set "+fieldInfo.getFieldName()+"=''";
                                cs.execute(sql);
                                propSetSqls.add("go\n"+sql+"\n");
                            }
                            ////////
                            Iterator vit = vfields.keySet().iterator();
                            propSetSqls.add("--֧������&ֵ�����������õ���ʱ�Զ�����һ���ֶ�(������Ե���)\n");
                            //ѭ����ÿһ������ֶ�
                            while (vit.hasNext()) {
                                DBFieldInfoBean fieldInfo = vfields.get(vit.next());
                                String strSQL = "insert into tblDBFieldInfo(id,fieldName,fieldType,inputType,isNull,listOrder,maxLength,udType,tableId,isunique,isStat,width,inputValue,fieldSysType,defaultValue,languageId) values('" +
                                                fieldInfo.getId() + "','" + fieldInfo.getFieldName() + "'," + fieldInfo.getFieldType() +
                                                "," + fieldInfo.getInputType() + "," +
                                                fieldInfo.getIsNull() + "," + fieldInfo.getListOrder() + "," + fieldInfo.getMaxLength() +
                                                "," + fieldInfo.getUdType() + ",'" + fieldInfo.getTableBean().getId() + "'," +
                                                fieldInfo.getIsUnique() + "," + fieldInfo.getIsStat() + ",100,'" + fieldInfo.getInputValue() +
                                                "','','','" + fieldInfo.getDisplay().getId() + "');";
                                BaseEnv.log.debug(strSQL);
                                cs.executeUpdate(strSQL);
                                propSetSqls.add(strSQL+"\n");
                                KRLanguageQuery.saveToDB(fieldInfo.getDisplay().map, fieldInfo.getDisplay().getId(), connection);
                                propSetSqls.add(ConvertToSql.getLanguageStr(fieldInfo.getDisplay())+"\n");
                                String sql = "ALTER TABLE " + fieldInfo.getTableBean().getTableName() + " ADD " + fieldInfo.getFieldName() +
                                             " varchar(5000) ALTER TABLE " + fieldInfo.getTableBean().getTableName() + " ADD  CONSTRAINT DF_" +
                                             fieldInfo.getTableBean().getTableName() + "_" + fieldInfo.getFieldName() +
                                             "  DEFAULT ('') FOR " + fieldInfo.getFieldName();
                                BaseEnv.log.debug(sql);
                                cs.executeUpdate(sql);
                                propSetSqls.add(sql+"\n");
                                sql="update "+fieldInfo.getTableBean().getTableName()+" set "+fieldInfo.getFieldName()+"=''";
                                cs.execute(sql);
                                propSetSqls.add("go\n"+sql+"\n");
                            }
                            DDLOperation.updateRefreshTime("tableInfo", connection);//�����ڴ�
                            for(int i=0;i<propSetSqls.size();i++){
                              TestRW.saveToSql(path, propSetSqls.get(i).toString());	
                            }
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
     * ɾ����������
     * @param propId String
     * @param keyIds String[]
     * @return Result
     */
    public Result propSetDeL(final GoodsPropInfoBean propBean, final List<DBTableInfoBean> list,final String path) {
        final Result rs = new Result();
        rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            ArrayList delArray=new ArrayList();
                            delArray.add("--��������(ɾ�����е������ֶ�)\n");
                            for (int i = 0; i < list.size(); i++) {
                                DBTableInfoBean tableInfo = list.get(i);
                                List<DBFieldInfoBean> fields = tableInfo.getFieldInfos();
                                DBFieldInfoBean fieldInfo=GlobalsTool.getFieldBean(tableInfo.getTableName(),propBean.getPropName());
                                String fieldId = fieldInfo.getId()==null?"":fieldInfo.getId();
                                String languageId=fieldInfo.getLanguageId()==null?"":fieldInfo.getLanguageId();
                                String sql = "ALTER TABLE " + tableInfo.getTableName() + " DROP CONSTRAINT DF_" + tableInfo.getTableName() +
                                             "_" + propBean.getPropName() + " delete from tblDBFieldInfo where id='" + fieldId +
                                             "' and tableId='" + tableInfo.getId() + "'";
                                BaseEnv.log.debug(sql);
                                cs.executeUpdate(sql);
                                delArray.add(sql+"\n");
                                sql = "delete from tblLanguage where id='" + languageId + "'";
                                BaseEnv.log.debug(sql);
                                cs.executeUpdate(sql);
                                delArray.add(sql+"\n");
                                sql = "alter table " + tableInfo.getTableName() + " drop column " + propBean.getPropName();
                                BaseEnv.log.debug(sql);
                                cs.executeUpdate(sql);
                                delArray.add(sql+"\n");
                                
                                DBFieldInfoBean nvFieldInfo= GlobalsTool.getFieldBean(tableInfo.getTableName(), propBean.getPropName()+"NV");
                                if(nvFieldInfo!=null){//ɾ�����ڵ�֧������&ֵ��������ʾ�ֶ�
                                	 delArray.add("--ɾ������&ֵ����ʾ�ֶ�");
                                	 String nvfieldId = nvFieldInfo.getId()==null?"":nvFieldInfo.getId();
                                     String nvlanguageId=nvFieldInfo.getLanguageId()==null?"":nvFieldInfo.getLanguageId();
                                     sql = "ALTER TABLE " + tableInfo.getTableName() + " DROP CONSTRAINT DF_" + tableInfo.getTableName() +
                                                  "_" + propBean.getPropName()+"NV" + " delete from tblDBFieldInfo where id='" + nvfieldId +
                                                  "' and tableId='" + tableInfo.getId() + "'";
                                     BaseEnv.log.debug(sql);
                                     cs.executeUpdate(sql);
                                     delArray.add(sql+"\n");
                                     sql = "delete from tblLanguage where id='" + nvlanguageId + "'";
                                     BaseEnv.log.debug(sql);
                                     cs.executeUpdate(sql);
                                     delArray.add(sql+"\n");
                                     sql = "alter table " + tableInfo.getTableName() + " drop column " + propBean.getPropName()+"NV";
                                     BaseEnv.log.debug(sql);
                                     cs.executeUpdate(sql);
                                     delArray.add(sql+"\n");
                                }
                            }
                            DDLOperation.updateRefreshTime("tableInfo", connection);//�����ڴ�
                            for(int i=0;i<delArray.size();i++){
                            	TestRW.saveToSql(path,delArray.get(i).toString());
                            }

                        } catch (SQLException ex) {

                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
     * ��������
     * @param propBean GoodsPropInfoBean
     * @param list List
     * @return Result
     */
    public Result propStart(final GoodsPropInfoBean propBean, final List<DBTableInfoBean> list,final String path) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            ArrayList propStartSqls=new ArrayList();
                            propStartSqls.add("--������ʾ����(�޸ı���Ϣ)\n");
                            for (int i = 0; i < list.size(); i++) {
                                String fieldId = "";
                                byte inputTypeOld = DBFieldInfoBean.INPUT_MAIN_TABLE;
                                DBTableInfoBean tableInfo = list.get(i);
                                List<DBFieldInfoBean> fields = tableInfo.getFieldInfos();
                                for (int j = 0; j < fields.size(); j++) {
                                    if (fields.get(j).getFieldName().equals(propBean.getPropName())) {
                                    	if(fields.get(j).getInputType()!=DBFieldInfoBean.INPUT_HIDDEN){
                                    		continue;
                                    	}else{
                                    		 fieldId = fields.get(j).getId();
                                             inputTypeOld = fields.get(j).getInputTypeOld();
                                             break;
                                    	}
                                    }
                                }
                                String sql = "update tblDBFieldInfo set inputType=" + inputTypeOld + ",inputTypeOld=NULL where id='" +
                                             fieldId + "' and tableId='" + tableInfo.getId() + "'";
                                BaseEnv.log.debug(sql);
                                cs.executeUpdate(sql);
                                propStartSqls.add(sql+"\n");
                                
                                DBFieldInfoBean nvField=GlobalsTool.getFieldBean(tableInfo.getTableName(), propBean.getPropName()+"NV");
                                if(nvField!=null){
                                	sql="update tblDBFieldInfo set inputType="+DBFieldInfoBean.INPUT_NORMAL+" where id='"+nvField.getId()+"' and tableId='"+tableInfo.getId()+"'";
                                	BaseEnv.log.debug(sql);
                                    cs.executeUpdate(sql);
                                    propStartSqls.add(sql+"\n");
                                }
                            }
                            DDLOperation.updateRefreshTime("tableInfo", connection);//�����ڴ�
                            for(int i=0;i<propStartSqls.size();i++){
                            	TestRW.saveToSql(path,propStartSqls.get(i).toString());
                            }
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
     * ����ͣ��
     * @param propBean GoodsPropInfoBean
     * @param list List
     * @return Result
     */
    public Result propStop(final GoodsPropInfoBean propBean, final List<DBTableInfoBean> list,final String path) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            ArrayList propStopSqls=new ArrayList();
                            propStopSqls.add("--���治��ʾ����(�޸ı���Ϣ)\n");
                            for (int i = 0; i < list.size(); i++) {
                                String fieldId = "";
                                byte inputType = DBFieldInfoBean.INPUT_HIDDEN;
                                DBTableInfoBean tableInfo = list.get(i);
                                String sql = "";
                                List<DBFieldInfoBean> fields = tableInfo.getFieldInfos();
                                for (int j = 0; j < fields.size(); j++) {
                                    if (fields.get(j).getFieldName().equals(propBean.getPropName())
                                    		  || fields.get(j).getFieldName().equals(propBean.getPropName()+"Name")) {
                                    	if(fields.get(j).getInputType()==DBFieldInfoBean.INPUT_HIDDEN){
                                    		continue;
                                    	}else{
                                        fieldId = fields.get(j).getId();
                                        inputType = fields.get(j).getInputType();
                                        break;
                                    	}
                                    }
                                }
                                sql = "update tblDBFieldInfo set inputType=" + DBFieldInfoBean.INPUT_HIDDEN + ",inputTypeOld=" + inputType +
                                      " where id='" + fieldId + "' and tableId='" + tableInfo.getId() + "'";
                                BaseEnv.log.debug(sql);
                                cs.executeUpdate(sql);
                                propStopSqls.add(sql+"\n");
                                //////
                                DBFieldInfoBean nvField=GlobalsTool.getFieldBean(tableInfo.getTableName(),propBean.getPropName()+"NV");
                                if(nvField!=null){
                                	sql = "update tblDBFieldInfo set inputType=" + DBFieldInfoBean.INPUT_HIDDEN + " where id='" + nvField.getId() + "' and tableId='" + tableInfo.getId() + "'";
		                              BaseEnv.log.debug(sql);
		                              cs.executeUpdate(sql);
		                              propStopSqls.add(sql+"\n");
                                }
                            }
                            DDLOperation.updateRefreshTime("tableInfo", connection);//�����ڴ�
                            for(int i=0;i<propStopSqls.size();i++){
                            	TestRW.saveToSql(path,propStopSqls.get(i).toString());
                            }

                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
     * ��ѯtblstocks����ĳ���Ե��Ƿ���ڿ��
     * @param id String
     * @return Result
     */
    public Result checkStopUse(final String propName) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        Connection conn = connection;
                        try {
                            Statement cs = conn.createStatement();
                            String sql = "select GoodsCode from tblStocks where " + propName + " is not null and " + propName +
                                         "!=''  and lastQty != 0";
                            ResultSet rss = cs.executeQuery(sql);
                            boolean ishave = false;
                            if (rss.next()) {
                                ishave = true;
                            }
                            rss.close();
                            if (!ishave) {
                                sql = "select * from tblStockdet where " + propName + " is not null and " + propName + "!='' ";
                                rss = cs.executeQuery(sql);
                                while (rss.next()) {
                                    ishave = true;
                                }
                            }
                            if (!ishave) {
                                rs.setRetVal("false");
                            } else {
                                rs.setRetVal("true");
                            }

                            rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.
                                          DEFAULT_FAILURE);
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
     * ��ѯ����Ʒ����ӳ����д��ڵ�ĳ���Ե�����ֵ
     * @param propId
     * @return
     */
    public Result queryPropValuesInTblGoodsOfprop(final String propId)
    {
       final Result rs=new Result();

        int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection connection) throws
                           SQLException {
                       Connection conn = connection;
                       try {
                         Statement cs= conn.createStatement();
                         String sql="select distinct(PropItemID) from tblGoodsOfprop where propId='"+propId+"'";
                         ResultSet rss=cs.executeQuery(sql);
                         List<String> propItemIDs=new ArrayList<String>();
                         while(rss.next())
                         {
                        	 propItemIDs.add(rss.getString("PropItemID"));
                         }
                         rs.setRetVal(propItemIDs);
                         rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                       } catch (SQLException ex) {
                           ex.printStackTrace();
                           rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                           return ;
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
     * ��ѯ��Ʒ�Ƿ����������кŶ��ұ�������
     * @param propName
     * @return
     */
    public Result queryGoodsSeqSet(final String goodsCode) {
        //����list���ؽ��
        final Result rs = new Result();
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            try {
                            	Statement stmt=connection.createStatement();
                            	String sql="select seqIsUsed as isUsed,1 as isNeed from tblGoods where classCode='"+goodsCode+"'";
                            	ResultSet rss=stmt.executeQuery(sql);
                            	String isUsed="";
                            	String isNeed="";
                            	if(rss.next()){
                            		isUsed=rss.getString("isUsed");
                            		isNeed=rss.getString("isNeed");
                            	}
                            	if("0".equals(isUsed)&&"0".equals(isNeed)){
                            		rs.setRetVal(true);
                            	}else{
                            		rs.setRetVal(false);
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
            });rs.setRetCode(retVal);
        return rs;
    }
    
    public boolean isGoodsSeqSet(final String goodsCode) {
        //����list���ؽ��
        final Result rs = new Result();
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            try {
                            	Statement stmt=connection.createStatement();
                            	String sql="select seqIsUsed as isUsed from tblGoods where classCode='"+goodsCode+"'";
                            	ResultSet rss=stmt.executeQuery(sql);
                            	String isUsed="";
                            	if(rss.next()){
                            		isUsed=rss.getString("isUsed");
                            	}
                            	if("0".equals(isUsed)){
                            		rs.retCode= ErrorCanst.DEFAULT_SUCCESS;
                            	}else{
                            		rs.retCode= ErrorCanst.DEFAULT_FAILURE;
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
         rs.setRetCode(retVal);
        return rs.retCode == ErrorCanst.DEFAULT_SUCCESS;
    }
    /**
     * ��ѯ��Ʒ��������
     * @param goodsCode
     * @return
     */
    public Result queryGoodsSeqSetN(final String goodsCode) {
        //����list���ؽ��
        final Result rs = new Result();
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            try {
                            	Statement stmt=connection.createStatement();
                            	String sql="select 1 as isContinue,1 as startIndex,1 as endIndex from tblGoods where classCode='"+goodsCode+"'";
                            	ResultSet rss=stmt.executeQuery(sql);
                            	String[] strs=new String[3];
                            	if(rss.next()){
                            		strs[0]=rss.getString("isContinue");
                            		strs[1]=rss.getString("startIndex");
                            		strs[2]=rss.getString("endIndex");
                            	}
                            	rs.setRetVal(strs);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                                return;
                            }
                        }
                    });
                    return rs.getRetCode();
                }
            });rs.setRetCode(retVal);
        return rs;
    }
    /**
     * ��ѯ��Ʒ���кŵ������������
     * @param goodsCode
     * @return
     */
    public Result queryLastContiPartOfSeq(final String seqFName,final String goodsCode,final int sIndex,final int eIndex) {
        //����list���ؽ��
        final Result rs = new Result();
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            try {
                            	Statement stmt=connection.createStatement();
                            	String sql="select top 1 subString("+seqFName+","+sIndex+","+eIndex+"-"+sIndex+"+1) as a from tblstockdet where goodscode='"+goodsCode+"' and InstoreQty>0 order by a desc";
                            	ResultSet rss=stmt.executeQuery(sql);
                            	String str="";
                            	if(rss.next()){
                                  str=rss.getString("a");
                            	}
                            	rs.setRetVal(str);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                                return;
                            }
                        }
                    });
                    return rs.getRetCode();
                }
            });rs.setRetCode(retVal);
        return rs;
    }
    /**
     * �޸����кŽ��в�ѯ����֤���к��Ƿ��Ѵ���
     * @param seqFName
     * @param goodsCode
     * @param seqs
     * @return
     */
    public Result validateSeqIn(final String seqFName,final ArrayList seqs) {
        //����list���ؽ��
        final Result rs = new Result();
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            try {
                            	Statement stmt=connection.createStatement();
                            	String sql="select "+seqFName+"  from tblstockdet where  instoreQty>0 and "+seqFName+"  in (";
                            	for(int i=0;i<seqs.size();i++){
                            		if(i==seqs.size()-1){
                            			sql+="'"+seqs.get(i)+"')";
                            		}else{
                            		    sql+="'"+seqs.get(i)+"',";
                            		}
                            	}
                            	ArrayList list=new ArrayList();
                            	ResultSet rss=stmt.executeQuery(sql);
                            	while(rss.next()){
                                 list.add(rss.getString(seqFName));
                            	}
                            	rs.setRetVal(list);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                                return;
                            }
                        }
                    });
                    return rs.getRetCode();
                }
            });rs.setRetCode(retVal);
        return rs;
    }
    /**
     * ��ѯ��Ʒ���к���������Ʒ���к�λ��
     * @param goodsCode
     * @return
     */
    public Result queryGoodsSeqDigit(final String goodsCode) {
        //����list���ؽ��
        final Result rs = new Result();
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            try {
                            	Statement stmt=connection.createStatement();
                            	String sql="select seqDigit from tblgoods where classCode='"+goodsCode+"'";
                            	ResultSet rss=stmt.executeQuery(sql);
                            	if(rss.next()){
                            		rs.setRetVal(rss.getInt(1));
                            	}else{
                            		rs.setRetVal(0);
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
            });rs.setRetCode(retVal);
        return rs;
    }
    /**
     * ��֤��Ʒ���к��Ƿ����
     * @param seqFname
     * @param seqs
     * @return
     */
    public Result queryGoodsSeqInStockdet(final String seqFname,final String[] seqs) {
        //����list���ؽ��
        final Result rs = new Result();
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws SQLException {
                            try {
                            	String sql="select seq from tblAllStockSeq where seq  in (";
                            	for(int i=0;i<seqs.length;i++){
                            		if(i==seqs.length-1){
                            			sql+="'"+seqs[i]+"')";
                            		}else{
                            		    sql+="'"+seqs[i]+"',";
                            		}
                            	}
                            	Statement stmt=connection.createStatement();
                            	ResultSet rss=stmt.executeQuery(sql);
                            	ArrayList existSeqs=new ArrayList();
                            	while(rss.next()){
                            	    existSeqs.add(rss.getString(1));
                            	}
                            	rs.setRetVal(existSeqs);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                                return;
                            }
                        }
                    });
                    return rs.getRetCode();
                }
            });rs.setRetCode(retVal);
        return rs;
    }
    
    /**
     * ��ѯ��Ʒgoodscode������������к�
     * @param seqFname
     * @param goodsCode
     * @return
     */
    public Result queryGoodsSeqInStockdetByGoodsCode(final String seqFname,final String goodsCode) {
        //����list���ؽ��
        final Result rs = new Result();
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(final Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            try {
                            	String sql="select distinct("+seqFname+") from tblStockDet where goodsCode='"+goodsCode+"' and  instoreqty>0";
                            	Statement stmt=connection.createStatement();
                            	ResultSet rss=stmt.executeQuery(sql);
                            	ArrayList existSeqs=new ArrayList();
                            	while(rss.next()){
                            	    existSeqs.add(rss.getString(1));
                            	}
                            	rs.setRetVal(existSeqs);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                                return;
                            }
                        }
                    });
                    return rs.getRetCode();
                }
            });rs.setRetCode(retVal);
        return rs;
    }
}
