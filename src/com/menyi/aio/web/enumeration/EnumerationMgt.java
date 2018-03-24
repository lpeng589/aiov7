package com.menyi.aio.web.enumeration;

import java.util.*;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.Result;
import com.menyi.aio.bean.EnumerateBean;
import com.menyi.aio.bean.KRLanguage;

import org.hibernate.Session;
import java.io.Serializable;
import com.menyi.aio.bean.EnumerateItemBean;
import com.menyi.web.util.ErrorCanst;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import org.hibernate.Query;
import com.dbfactory.DBCanstant;
import com.menyi.web.util.GlobalsTool;
import com.menyi.aio.dyndb.DDLOperation;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.jdbc.Work;
import java.sql.PreparedStatement;

import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.KRLanguageQuery;
import com.menyi.web.util.zh_CN_TO_zh_TW;

/**
 * <p>Title: </p>
 *
 * <p>Description: 单位管理的接口类</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class EnumerationMgt extends DBManager  {

    /**
     * 填加一条单位记录
     * @param id long
     * @param name String
     * @return Result
     */
    public Result add(final EnumerateBean bean) {
        //调用基类方法addBean执行插入操作
        final Result rs = new Result();
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                        SQLException {
                        bean.setLanguageId(bean.getDisplay().getId());
                        for (int i = 0; i < bean.getEnumItem().size(); i++) {
                            EnumerateItemBean itemBean = (EnumerateItemBean)bean.getEnumItem().get(i);
                            itemBean.setLanguageId(itemBean.getDisplay().getId());
                            KRLanguageQuery.saveToDB(itemBean.getDisplay().map, itemBean.getDisplay().getId(), conn);
                        }
                        KRLanguageQuery.saveToDB(bean.getDisplay().map, bean.getDisplay().getId(), conn);
                    }
                });

                Result rstemp = addBean(bean,session);
                if(rstemp.retCode != ErrorCanst.DEFAULT_SUCCESS){
                    rs.retCode = rstemp.getRetCode();
                    return rs.retCode;
                }else{
                    rs.setRetVal(rstemp.getRetVal());
                }
                DDLOperation.updateRefreshTime("Enumeration",session);
                return rs.retCode;
            }
        });
        rs.retCode = retVal;
        return rs;
    }

    protected int doBeforeExec(int type, Object bean,final Serializable id,
                                       Session session) {
        return ErrorCanst.DEFAULT_SUCCESS;
    }
    /**
     * 修改一条单位记录
     * @param id long
     * @param name String
     * @return Result
     */
    public Result update(final EnumerateBean bean) {
        //由于下面有子表，先删除子表数据，再添加
        //这里重载doBeforeExec完成删除工作
        final Result rs = new Result();
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                        SQLException {
                        String sql = "delete FROM  tblLanguage where id in( select languageId from tblDBEnumeration  WHERE id = ? ) ";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, bean.getId());
                        stmt.executeUpdate();

                        sql = "delete FROM  tblLanguage where id in( select languageId from tblDBEnumerationItem  WHERE enumid = ? ) ";
                        stmt = conn.prepareStatement(sql);
                        stmt.setString(1, bean.getId());
                        stmt.executeUpdate();

                        bean.setLanguageId(bean.getDisplay().getId());
                        for (int i = 0; i < bean.getEnumItem().size(); i++) {
                            EnumerateItemBean itemBean = (EnumerateItemBean) bean.getEnumItem().get(i);
                            KRLanguageQuery.saveToDB(itemBean.getDisplay().map, itemBean.getDisplay().getId(), conn);
                            itemBean.setLanguageId(itemBean.getDisplay().getId());
                        }
                        KRLanguageQuery.saveToDB(bean.getDisplay().map, bean.getDisplay().getId(), conn);
                    }
                });

                String hql = "delete EnumerateItemBean where enumerateBean.id =:id";
                Query query = session.createQuery(hql);
                query.setParameter("id", ((EnumerateBean) bean).getId());
                int r = query.executeUpdate();
                if (r <= 0) {
                    return DBCanstant.ER_NO_DATA;
                }

                Result rstemp = updateBean(bean,session);
                if (rstemp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                    rs.retCode = rstemp.getRetCode();
                    return rs.retCode;
                } else {
                    rs.setRetVal(rstemp.getRetVal());
                }
                DDLOperation.updateRefreshTime("Enumeration", session);
                return rs.retCode;
            }
        });
        rs.retCode = retVal;
        return rs;
    }

    /**
     * 删除多条单位记录
     * @param ids long[]
     * @return Result
     */
    public Result delete(final String[] ids) {
        final Result rs = new Result();
        int retVal = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                for (int i = 0; i < ids.length; i++) {
                    final String beanid = ids[i];
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            String sql = "delete FROM  tblLanguage where id in( select languageId from tblDBEnumeration  WHERE id = ? ) ";
                            PreparedStatement stmt = connection.prepareStatement(sql);
                            stmt.setString(1, beanid);
                            stmt.executeUpdate();

                            sql = "delete FROM  tblLanguage where id in( select languageId from tblDBEnumerationItem  WHERE enumid = ? ) ";
                            stmt = connection.prepareStatement(sql);
                            stmt.setString(1, beanid);
                            stmt.executeUpdate();
                        }
                    });

                    String hql = "delete EnumerateItemBean where enumerateBean.id =:id";
                    Query query = session.createQuery(hql);
                    query.setParameter("id", beanid);
                    int r = query.executeUpdate();
                    if (r <= 0) {
                        return DBCanstant.ER_NO_DATA;
                    }

                    Result rstemp = deleteBean(beanid, EnumerateBean.class, "id", session);
                    if (rstemp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        rs.retCode = rstemp.getRetCode();
                    }
                    DDLOperation.updateRefreshTime("Enumeration", session);
                }
                return rs.retCode;
            }
        });
        rs.retCode = retVal;
        return rs;
    }

    /**
     * 查询单位记录
     * @param name String
     * @param pageNo int
     * @param pageSize int
     * @return Result
     */
    public Result query(String name ,int pageNo, int pageSize,String mainModule) {
        //创建参数
        List param = new ArrayList();
        String hql = "select bean from EnumerateBean as bean where 1=1 ";
        if(!"-1".equals(mainModule)){//-1 查询全部
        	hql+=" and bean.mainModule = ? ";
        	param.add(Integer.parseInt(mainModule) );
        }
        
        if (name != null && name.length() != 0) {
            hql += " and upper(bean.enumName) like '%'||?||'%'";
            param.add(name.trim().toUpperCase());
        }

        //调用list返回结果
        final Result rs = list(hql, param, pageNo, pageSize, true);
        if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList) rs.getRetVal()).size() > 0) {

            int retVal = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            final KRLanguageQuery klQuery = new KRLanguageQuery();
                            for (int k = 0; k < ((ArrayList) rs.getRetVal()).size(); k++) {
                                EnumerateBean bean = (EnumerateBean)((ArrayList) rs.getRetVal()).get(k);
                                klQuery.addLanguageId(bean.getLanguageId());
                                for (int i = 0; i < bean.getEnumItem().size(); i++) {
                                    EnumerateItemBean eib = (EnumerateItemBean) bean.getEnumItem().get(i);
                                    klQuery.addLanguageId(eib.getLanguageId());
                                }
                            }

                            HashMap map = klQuery.query(connection);

                            for (int k = 0; k < ((ArrayList) rs.getRetVal()).size(); k++) {
                                EnumerateBean bean = (EnumerateBean)((ArrayList) rs.getRetVal()).get(k);
                                bean.setDisplay((KRLanguage) map.get(bean.getLanguageId()));
                                for (int i = 0; i < bean.getEnumItem().size(); i++) {
                                    EnumerateItemBean eib = (EnumerateItemBean) bean.getEnumItem().get(i);
                                    eib.setDisplay((KRLanguage) map.get(eib.getLanguageId()));
                                }
                            }

                        }
                    });
                    return ErrorCanst.DEFAULT_SUCCESS;
                }
            });
        }

        return rs;
    }
    
    public Result queryByDisplay(final String name ,final int pageNo,final  int pageSize,final String local ,final String mainModule) {
        final Result rs =new Result();
            int retVal = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                        	String sql="select a.* from tblDBEnumeration a left join tblLanguage b on a.languageid=b.id where b."+local+" like '%"+name+"%'";
                        	if(!"-1".equals(mainModule)){ /*-1=查询全部*/
                        		 sql +=" and mainModule in (0,"+mainModule+") ";
                        	}
                        	String itemSql="select * from tblDBEnumerationItem where enumId in ( ";
                        	PreparedStatement psmt=connection.prepareStatement(sql);
                        	ResultSet rss=psmt.executeQuery();
                        	ArrayList enumList=new ArrayList();
                        	Hashtable temp=new Hashtable();
                        	while(rss.next()){
                        		EnumerateBean enumBean=new EnumerateBean();
                        		enumBean.setId(rss.getString("id"));
                        		enumBean.setEnumName(rss.getString("enumName"));
                        		enumBean.setCreateBy(rss.getString("createBy"));
                        		enumBean.setCreateTime(rss.getString("createTime"));
                        		enumBean.setLastUpdateBy(rss.getString("lastUpdateBy"));
                        		enumBean.setLastUpdateTime(rss.getString("lastUpdateTime"));
                        		enumBean.setLanguageId(rss.getString("languageId"));
                        		enumBean.setEnumItem(new ArrayList());
                        	    enumList.add(enumBean);
                        	    temp.put(enumBean.getId(),enumBean); 
                        	}
                        	if(enumList.size()>0){
                        	for(int i=0;i<enumList.size();i++){
                        		EnumerateBean eb=(EnumerateBean)enumList.get(i);
                        		if(i!=enumList.size()-1){
                        		    itemSql+="'"+eb.getId()+"',";
                        		}else{
                        			itemSql+="'"+eb.getId()+"')";
                        		}
                        	}
                        
                        	psmt=connection.prepareStatement(itemSql);
                        	rss=psmt.executeQuery();
                        	while(rss.next()){
                        		EnumerateItemBean enumItem=new EnumerateItemBean();
                        		enumItem.setId(rss.getString("id"));
                        		enumItem.setEnumValue(rss.getString("enumValue"));
                        		enumItem.setLanguageId(rss.getString("languageId"));
                        		EnumerateBean enumBean=(EnumerateBean)temp.get(rss.getString("enumId"));
                        		enumItem.setEnumerateBean(enumBean);
                        		enumBean.getEnumItem().add(enumItem);
                        		temp.put(enumBean.getId(),enumBean);
                        		enumList.remove(enumBean);
                        		enumList.add(enumBean);
                        	}
                            final KRLanguageQuery klQuery = new KRLanguageQuery();
                            for (int k = 0; k < enumList.size(); k++) {
                                EnumerateBean bean = (EnumerateBean)enumList.get(k);
                                klQuery.addLanguageId(bean.getLanguageId());
                                for (int i = 0; i < bean.getEnumItem().size(); i++) {
                                    EnumerateItemBean eib = (EnumerateItemBean) bean.getEnumItem().get(i);
                                    klQuery.addLanguageId(eib.getLanguageId());
                                }
                            }

                            HashMap map = klQuery.query(connection);

                            for (int k = 0; k < enumList.size(); k++) {
                                EnumerateBean bean = (EnumerateBean)enumList.get(k);
                                bean.setDisplay((KRLanguage) map.get(bean.getLanguageId()));
                                for (int i = 0; i < bean.getEnumItem().size(); i++) {
                                    EnumerateItemBean eib = (EnumerateItemBean) bean.getEnumItem().get(i);
                                    eib.setDisplay((KRLanguage) map.get(eib.getLanguageId()));
                                }
                            }
                        	}
//                          设置分页信息
							rs.setRealTotal(enumList.size());
							rs.setPageSize(pageSize);

							int pageNos = pageNo > 0 ? pageNo : 1;
							double totalPage = (double) rs.getRealTotal()
									/ (double) rs.getPageSize();

							rs.setPageNo(pageNos);
							rs.setTotalPage((int) Math.ceil(totalPage));

							int startNo = 1 + (rs.getPageNo() - 1)
									* rs.getPageSize();
							int endNo = rs.getPageSize() + (rs.getPageNo() - 1)
									* rs.getPageSize();

							ArrayList newList = new ArrayList();
							for (int i = startNo - 1; i < endNo
									&& i < enumList.size(); i++) {
								Object obj = enumList.get(i);
								newList.add(obj);
							}
							rs.setRetVal(newList);
                        }
                    });
                    return ErrorCanst.DEFAULT_SUCCESS;
                   
                }
            });
            rs.setRetCode(retVal);
        return rs;
    }

    /**
     * 查一条单位的详细信息
     * @param notepadId long 代号
     * @return Result 返回结果
     */
    public Result detail(String id) {
        //创建参数
        List param = new ArrayList();
        param.add(id);
        String hql = "select bean from EnumerateBean as bean where bean.id = ?";
        //调用list返回结果
        final Result rs = list(hql, param);
        if(rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS && ((ArrayList)rs.getRetVal()).size()>0){

            int retVal = DBUtil.execute(new IfDB() {
                public int exec(Session session) {
                    session.doWork(new Work() {
                        public void execute(Connection connection) throws
                            SQLException {
                            rs.retVal = ((ArrayList) rs.getRetVal()).get(0);
                            EnumerateBean bean = (EnumerateBean) rs.retVal;
                            final KRLanguageQuery klQuery = new KRLanguageQuery();
                            klQuery.addLanguageId(bean.getLanguageId());
                            for (int i = 0; i < bean.getEnumItem().size(); i++) {
                                EnumerateItemBean eib = (EnumerateItemBean) bean.getEnumItem().get(i);
                                klQuery.addLanguageId(eib.getLanguageId());
                            }
                            HashMap map = klQuery.query(connection);
                            bean.setDisplay((KRLanguage)map.get(bean.getLanguageId()));
                            for (int i = 0; i < bean.getEnumItem().size(); i++) {
                                EnumerateItemBean eib = (EnumerateItemBean) bean.getEnumItem().get(i);
                                eib.setDisplay((KRLanguage)map.get(eib.getLanguageId()));
                            }
                        }
                    });
                    return ErrorCanst.DEFAULT_SUCCESS;
                }
            });
        }

        return rs;
    }
    
    /**
     * 当添加一个新的字段系统类型时，往系统参数设置中插入一条名字一样的值，默认值为false
     * @param sysCode
     * @param sysName
     * @return
     */
    public Result addFieldSysTypeBySysEnum(final String sysCode,final String sysName){
    	
    	/***往表tblSysValue插入参数值**/
    	final Result rs =new Result();
    	int retVal = DBUtil.execute(new IfDB() {
    		final Result rsValue = new Result() ;
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws
                        SQLException {
                    	String sysValueSQL =   "insert into tblSysValue(id,SysCode,SysNumerValue,SysChValue,createBy) values(?,?,?,?,'1')" ;
                    	try{
	                    	PreparedStatement ps = conn.prepareStatement(sysValueSQL) ;
	                    	ps.setString(1, IDGenerater.getId()) ;
	                    	ps.setString(2, sysCode) ;
	                    	ps.setString(3, "false") ;
	                    	ps.setString(4, "37ff2ea7_0909141657400310193CF") ;
	                    	ps.addBatch() ;
	                    	
	                    	ps.setString(1, IDGenerater.getId()) ;
	                    	ps.setString(2, sysCode) ;
	                    	ps.setString(3, "true") ;
	                    	ps.setString(4, "37ff2ea7_0909141657400310191CF") ;
	                    	ps.addBatch() ;
	                    	
	                    	int[] n = ps.executeBatch() ;
	                    	if(n.length==2){
	                    		rsValue.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
	                    	}else{
	                    		rsValue.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
	                    	}
                    	}catch (Exception e) {
                    		e.printStackTrace() ;
                    		rsValue.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
                    	}
                    }
                });
                return rsValue.retCode ;
            }
        });
    	
    	/**
    	 * 如果系统参数插入成功，则在系统配置他，系统参数设置中插入
    	 */
    	if(retVal==ErrorCanst.DEFAULT_SUCCESS){
			int retVal2 = DBUtil.execute(new IfDB() {
				final Result rsSys = new Result() ;
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn)
								throws SQLException {
							String sysDeploySQL = "insert into tblSysDeploy(id,SysCode,SysName,DefaultValue,Setting,SysChValue,createBy) values(?,?,?,'false','false','37ff2ea7_0909141657400310193CF','1')" ;
							try {
								PreparedStatement ps = conn.prepareStatement(sysDeploySQL) ;
								ps.setString(1, IDGenerater.getId()) ;
								ps.setString(2, sysCode) ;
								ps.setString(3, sysName) ;
								int n = ps.executeUpdate() ;
								if(n>0){
									String querySys = "select zh_CN from tblLanguage where id='"+sysName+"'" ;
									Statement state = conn.createStatement() ;
									ResultSet rs = state.executeQuery(querySys) ;
									if(rs.next()){
										String str_zh_CN = rs.getString("zh_CN") ;
										String sysSettingSQL = "insert into tblSysSetting(id,SysCode,SysName,DefaultValue,Setting,createBy) values(?,?,?,'false','false','1')" ;
										PreparedStatement psSetting = conn.prepareStatement(sysSettingSQL) ;
										psSetting.setString(1, IDGenerater.getId()) ;
										psSetting.setString(2, sysCode) ;
										psSetting.setString(3, str_zh_CN) ;
										int m = psSetting.executeUpdate() ;
										if(m>0){
											rsSys.setRetCode(ErrorCanst.DEFAULT_SUCCESS) ;
										}else{
											rsSys.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
										}
									}
								}else{
									rsSys.setRetCode(ErrorCanst.DEFAULT_FAILURE) ;
								}
							} catch (Exception e) {
								e.printStackTrace();
								rsSys.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						}
					});
					return rsSys.retCode;
				}
			});
			rs.setRetCode(retVal2) ;
    	}
        return rs ;
    }
  }
