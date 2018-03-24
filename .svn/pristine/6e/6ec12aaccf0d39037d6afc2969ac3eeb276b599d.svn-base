package com.koron.crm.setting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.crm.bean.ClientModuleBean;
import com.koron.crm.bean.ClientModuleViewBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.FieldScopeSetBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.customize.DBTableInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
public class ClientSetingMgt extends AIODBManager{
	
	/**
	 * 查询所有的客户模板
	 * @return
	 */
	public Result queryAllModules(){
		String hql=" from ClientModuleBean  where id!='0' order by createTime ";
		 //调用list返回结果
		return list(hql, null);
	}
	
	
	/**
	 * 根据模版名称查询客户模板
	 * @return
	 */
	public Result findmoduleByName(String moduleName){
		String hql=" from ClientModuleBean  where moduleName='" + moduleName + "'";
        return list(hql, null);
	}
	
	/**
	 * 根据模版视图名称查询客户模板
	 * @return
	 */
	public Result findmoduleViewByName(String viewName,String moduleId){
		String hql=" from ClientModuleViewBean  where viewName='" + viewName + "' and moduleId='"+moduleId+"'";
        return list(hql, null);
	}
	
	
	/**
	 * 查询登录人所拥有的客户模板
	 * @return
	 */
	public Result queryMyModules(final LoginBean login){
       final Result rst = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection conn) throws  SQLException {
                       try {
							String sql="select id,moduleName,defPageSize from CRMClientModule  where id!='0' ";
//							if(!"1".equals(login.getId())){
//								sql += " and (isAlonePopedmon='0' or dbo.exist_dept(popedomDeptIds,?)='true' or popedomUserIds like ? or popedomEmpGroupIds like ? ) " ;
//							}
							sql += " order by createTime";
							PreparedStatement pss = conn.prepareStatement(sql);
//							if(!"1".equals(login.getId())){
//								pss.setString(1, login.getDepartCode());
//								pss.setString(2, '%'+login.getId()+'%');
//								String group=" ";
//								if(!"".equals(login.getGroupId()))
//									group=login.getGroupId();
//									
//								pss.setString(3, '%'+group+'%');
//							}
							ResultSet rss = pss.executeQuery();
							ArrayList<String[]> moduleList = new ArrayList<String[]>();
							while(rss.next()){
								String[] module = new String[3];
								module[0] = rss.getString("moduleName");
								module[1] = rss.getString("id");
								module[2] = rss.getString("defPageSize");
								moduleList.add(module);
							}
							rst.setRetVal(moduleList);
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
	 * 查询所有的字段设置
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Result queryAllSet(int pageNo, int pageSize,String viewId){
		String hql=" from FieldScopeSetBean where viewId = '"+viewId+"' order by createTime desc ";
		 //调用list返回结果
        return list(hql, null, pageNo, pageSize,true);
	}
	
	/**
	 * 添加客户模板
	 * @param crmModuleBean
	 * @return
	 */
	public Result addCrmModule(ClientModuleBean crmModuleBean){
		return addBean(crmModuleBean);
	}
	
	/**
	 * 根据Id查看某个模版
	 * @param crmModuleBean
	 * @return
	 */
	public Result detailCrmModule(String keyId){
		return this.loadBean(keyId,ClientModuleBean.class);
	}
	
	/**
	 * 修改客户模版
	 * @param crmModuleBean
	 * @return
	 */
	public Result updateCrmModule(ClientModuleBean crmModuleBean){
		return updateBean(crmModuleBean);
	}
	
	/**
	 * 删除客户模版
	 * @param keyIds
	 * @return
	 */
	 public Result deleteCrmModule(final String[] keyIds,final List<String> tableIds,final List<String> enumerIds) {
	        final Result rs=new Result();
	        int retCode = DBUtil.execute(new IfDB() {
	             public int exec(Session session) {
	                session.doWork(new Work() {
	                    public void execute(Connection connection) throws
	                            SQLException {
	                        Connection conn = connection;
	                        try{
	                        	Statement stmt = conn.createStatement();
	                        	for(String keyId : keyIds){
	                        		stmt.addBatch("delete from CRMClientModule where id='"+keyId+"'");
	                        		stmt.addBatch("delete from CRMClientModuleView where moduleId='"+keyId+"'");
	                        		stmt.addBatch("delete from CRMCommonTable where moduleId='"+keyId+"'");
	                        		stmt.addBatch("delete from CRMFieldScopeSet where moduleId='"+keyId+"'");
	                        	}
	                        	for(String tableId : tableIds){
	                        		stmt.addBatch("delete from tblDBFieldInfo where tableid='"+tableId+"'");
	                        		stmt.addBatch("delete from tblDBTableInfo where id='"+tableId+"'");
	                        	}
	                        	for(String enumerId : enumerIds){//只有以extent_开头字段自己创建的才可以删除枚举
	                        		stmt.addBatch("delete from tblDBEnumerationItem where enumId in (select b.id from tblDBEnumerationItem a join tblDBEnumeration b  on a.enumId=b.id where charindex('extent_',b.enumName) = 1 and b.id='"+enumerId+"' ) ");
	                        		stmt.addBatch("delete from tblDBEnumeration where id='"+enumerId+"'  and charindex('extent_',enumName) = 1");
	                        	}
	                        	stmt.executeBatch();
	                        }catch(Exception ex){
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
	 * 添加字段权限设置
	 * @param crmModuleBean
	 * @return
	 */
	public Result addFieldScope(FieldScopeSetBean fBean){
		return addBean(fBean);
	}
	
	/**
	 * 根据Id查看某个字段权限设置
	 * @param crmModuleBean
	 * @return
	 */
	public Result detailFieldScope(String keyId){
		return this.loadBean(keyId,FieldScopeSetBean.class);
	}
	
	/**
	 * 修改字段权限设置
	 * @param crmModuleBean
	 * @return
	 */
	public Result updateFieldScope(FieldScopeSetBean fBean){
		return updateBean(fBean);
	}
	
	/**
	 * 删除字段权限设置
	 * @param keyIds
	 * @return
	 */
	public Result deleteFieldScope(String[] keyIds) {
		return deleteBean(keyIds, FieldScopeSetBean.class, "id");
	}
	

	
	/**
	 * 修改字段操作(包含修改多语言)
	 * @param fields
	 * @param values
	 * @return
	 */
	public Result updateFields(final String fields,final String values,final String str,final String tableInfo,final String pageSql) {
	       final Result rst = new Result();
	       int retCode = DBUtil.execute(new IfDB() {
	           public int exec(Session session) {
	               session.doWork(new Work() {
	                   public void execute(Connection conn) throws
	                           SQLException {
	                       try {
	                    	   
	                           Statement stmt = conn.createStatement();
	                           String sql="";
	                           if("zh_TW".equals(str)  || "en".equals(str) ||  "zh_CN".equals(str) ){ //修改多语言
	                        	   sql="UPDATE tblLanguage set "+str+"='"+values+"' where id='"+fields+"'";	
	                           }else{  //修改其他字段
	                        	   String newfield=fields;
	                        	   if(newfield.indexOf("contact")>-1){
	                        		   newfield=newfield.replaceAll("contact", "");
	                        		   sql="UPDATE tbldbfieldInfo SET "+newfield+"='"+values+"' where fieldName='"+str+"' and  tableId IN (SELECT id FROM tblDBTableInfo WHERE tblDBTableInfo.tableName IN ('"+tableInfo.split(":")[1] +"'))";
	                        	   }else{
	                        		   sql="UPDATE tbldbfieldInfo SET "+newfield+"='"+values+"' where fieldName='"+str+"' and  tableId IN (SELECT id FROM tblDBTableInfo WHERE tblDBTableInfo.tableName IN ('"+tableInfo.split(":")[0] +"'))"; 
	                          
	                        	   }
	                           }
	                           stmt.addBatch(sql);
	                           if(!"".equals(pageSql)){
	                        	   stmt.addBatch(pageSql);
	                           }
	                           int[] row = stmt.executeBatch(); 
	                           if (row.length > 0) {
	                              rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
	                           }
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
	 * 查询客户和客户从表所有的选项数据
	 * @return
	 */
	public TreeMap queryCRMEnumerate(){
		List<String> param=new ArrayList<String>();
		String sql="SELECT t1.tableName,t2.enumName,t3.zh_CN,t2.id FROM tblDBFieldInfo  t "+
					" LEFT JOIN tblDBTableInfo t1 ON t.tableId=t1.id  "+
					" LEFT JOIN tblDBEnumeration t2 ON t.refEnumerationName=t2.enumName  "+
					" LEFT JOIN tblLanguage t3 ON t2.languageId=t3.id  "+
					" WHERE(t1.tableName='CRMClientInfo' OR';'+t1.perantTableName+';' LIKE '%;CRMClientInfo;%')  AND t.inputType='1'  "+
					" AND t1.tableName NOT IN ('CRMClientCallLogView','CRMMailInfoView','CRMSamplerequestDet','ViewCRMOutNote','CRMMemoryDay','CRMSendSMSView','CRMPeriodicExamine') "+
					" ORDER BY  t1.tableType";
		Result result = this.sqlList(sql, param);
		GlobalsTool gt=new GlobalsTool();
		TreeMap<String, List<String[]>> enumMap = new TreeMap<String, List<String[]>>() ;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			List objList = (List) result.retVal ;
			for(int i=0;i<objList.size();i++){
				String tableName = gt.get(objList.get(i),0).toString();
				String enumName = gt.get(objList.get(i),1).toString();
				String enumZH = gt.get(objList.get(i),2).toString();
				String enumId = gt.get(objList.get(i),3).toString();
				if(enumMap.get(tableName)==null){
					List<String[]> enumList = new ArrayList<String[]>() ;
					enumList.add(new String[]{enumName,enumZH,enumId}) ;
					enumMap.put(tableName, enumList) ;
				}else{
					List<String[]> enumList = enumMap.get(tableName) ;
					enumList.add(new String[]{enumName,enumZH,enumId}) ;
				}
			}
		}
		return enumMap;
	}
	
	/**
	 * 获取客户的导入，导出，打印的权限设置
	 * @return
	 */
	public Result getCRmScope(String viewId){
		List<String> param=new ArrayList<String>();
		//String sql="select * from CRMCommonTable where id in('crm_exportScope_1','crm_importScope_1','crm_printScope_1') and moduleId='"+moduleId+"' ORDER BY id  DESC ";
		String sql="select * from CRMCommonTable where viewId='"+viewId+"' ORDER BY id  DESC";
		return this.sqlList(sql, param);
		
	}
	
	 /**
	  * 修改客户打印，导入，导出权限设置
	  * @param isALL
	  * @param depts
	  * @param users
	  * @param groups
	  * @return
	  */
    public Result updCRMScope(final String[] Ids,final String[] isAll,final String[] depts , final String[] users ,final String[] groups,final String viewId ) {
        final Result rs=new Result();
        int retCode = DBUtil.execute(new IfDB() {
             public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                            SQLException {
                        Connection conn = connection;
                        try{
                        	long time = new Date().getTime();
                        	Statement stmt = conn.createStatement();
                        	stmt.addBatch("DELETE FROM CRMCommonTable WHERE viewId='"+viewId+"'");
                        	for(int i=0;i<Ids.length;i++){
                        		stmt.addBatch("insert into CRMCommonTable(id,fields1,fields2,fields3,fields4,viewId)values ('"+Ids[i]+"','"+isAll[i]+"','"+depts[i]+"','"+users[i]+"','"+groups[i]+"','"+viewId+"')");
	                        	//stmt.addBatch("update CRMCommonTable set fields1='"+isAll[i]+"',fields2='"+depts[i]+"',fields3='"+users[i]+"',fields4='"+groups[i]+"',moduleId='"+moduleId+"' where id='"+Ids[i]+"'");
                        	}
                        	int[] counts2 = stmt.executeBatch(); 
                        	if(counts2.length>0){
                        		System.out.println("客户权限修改成功");
                        	}else{
                        		System.out.println("客户权限修改失败");
                        	}
                        }catch(Exception ex){
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
    
	public Result findTabelInfoBean(String tableName){
		List list = new ArrayList();
		String sql = "select id from tbldbtableInfo where tablename='"+tableName+"'";
		return this.sqlList(sql, list);
	}
	
	
	public Result copyModule(final DBTableInfoBean mainTableInfo,final DBTableInfoBean childTableInfo,final String tempNo,final String localeStr,final Hashtable allTables,final ClientModuleBean moduleBean,final List<String> stateList,final HashMap map) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement state = conn.createStatement();
							for(String str :stateList){
								state.addBatch(str);
							}
							List<String> scopeList = (List<String>)map.get("scopeSql");
							for(String sql :scopeList){
								state.addBatch(sql);
							}
							state.executeBatch();
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (RuntimeException e) {
							BaseEnv.log.error("ClientSetting copyModule mehtod:", e) ;
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				//保存主表与从表
                if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
                    Result rtable = addBean(mainTableInfo,session);
                    rtable = addBean(childTableInfo,session);
                    rtable = addBean(moduleBean,session);
                    rtable = addBean((ClientModuleViewBean)map.get("bean"),session);
                    rs.setRetCode(rtable.getRetCode());
                }
                return rs.getRetCode();
			}
		});
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			allTables.put(mainTableInfo.getTableName(), mainTableInfo);
			allTables.put(childTableInfo.getTableName(), childTableInfo);
		}
		rs.setRetCode(retCode);
		return rs;
	}
	
	/**
	 * 添加客户模板
	 * @param crmModuleBean
	 * @return
	 */
	public Result addCopyMou(DBTableInfoBean tableInfoBean){
		return addBean(tableInfoBean);
	}
	
	
	/**
	 * 修改分组值
	 * 
	 * @param keyIds 客户的Id
	 * @return
	 */
    public Result updateGroupNames(final List<String> list) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{

							Statement state = conn.createStatement();
							for(int i=0;i<list.size();i++){
								state.addBatch(list.get(i));
							}
							state.executeBatch();
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt shareClientEmp mehtod:", ex) ;
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 新增列
	 * 
	 * @param keyIds 客户的Id
	 * @return
	 */
    public Result addFieldBeans(final List<DBFieldInfoBean> fieldList,final List<String> sqlList) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				for(DBFieldInfoBean bean : fieldList){
            		addBean(bean,session);
            		
            	}
				
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{

							Statement state = conn.createStatement();
							for(int i=0;i<sqlList.size();i++){
								state.addBatch(sqlList.get(i));
							}
							state.executeBatch();
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt shareClientEmp mehtod:", ex) ;
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 分组获得客户模板信息
	 * @param tableName
	 * @return
	 */
	public Result infosGroupByModule(){
		List list = new ArrayList();
		String sql = "select moduleId,count(moduleId) from crmclientinfo group by moduleId";
		return this.sqlList(sql, list);
	}
	
	public int checkCols(String colName){
		String sql ="select count(*) from sys.columns where object_id in (select object_id from sys.tables where name='CRMClientInfo') and (name like '"+colName+"%')";
		Result result =  this.sqlList(sql, new ArrayList());
		int colNum = 0;
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			colNum = Integer.valueOf(((Object[])((List<Object>)result.retVal).get(0))[0].toString());
		}
		return colNum;
	}
	
	/**
	 *删除字段
	 *
	 */
	public Result delField(final List<String> sqlList){
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							Statement state = conn.createStatement();
							for(String sql : sqlList){
								state.addBatch(sql);
							}
							state.executeBatch();
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt shareClientEmp mehtod:", ex) ;
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	public Result selectGroup(String groupName){
		String sql ="select groupName from tblDBFieldInfo where tableId=(select id from tblDBTableInfo where tableName='"+groupName+"') group by groupName";
		return this.sqlList(sql, new ArrayList());
	}
	
	
	/**
	 * 删除分组后,更新分组信息
	 * 
	 * @param keyIds 客户的Id
	 * @return
	 */
    public Result updateDelGroupName(final String tableName,final String firstId,final String delGroups) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							String sql = "update tblDBFieldInfo set groupName = '"+firstId+"' where tableId=(select id from tblDBTableInfo where tableName= '"+tableName+"') and groupName in ("+delGroups+")" ;
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.executeUpdate();
						}catch (Exception ex) {
							BaseEnv.log.error("CRMClientMgt shareClientEmp mehtod:", ex) ;
							ex.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 根据模板ID查询模板视图
	 * @return
	 */
	public Result queryModuleViewsByModuleId(LoginBean loginBean,String moduleId){
		String hql=" from ClientModuleViewBean  where id!='0' and moduleId='"+moduleId+"' ";
		List<String> param = new ArrayList<String>();
		if(!"1".equals(loginBean.getId())){
			hql += " and (isAlonePopedmon='0' or dbo.exist_dept(popedomDeptIds,?)='true' or popedomUserIds like ? or popedomEmpGroupIds like ? ) " ;
			param.add(""+loginBean.getDepartCode()+"");
			param.add('%'+loginBean.getId()+'%');
			String group=" ";
			if(!"".equals(loginBean.getGroupId())){
				group=loginBean.getGroupId();
			}
			param.add('%'+group+'%');
		}
		hql +=" order by createTime";
        return list(hql,param);//调用list返回结果
	}
	
	/**
	 * 根据权限查询所有模板视图
	 * @return
	 */
	public Result queryAllModuleViews(LoginBean loginBean,String moduleId){
		String hql=" from ClientModuleViewBean  where id!='0' and moduleId ='"+moduleId+"'";
		List<String> param = new ArrayList<String>();
		if(!"1".equals(loginBean.getId())){
			hql += " and (isAlonePopedmon='0' or dbo.exist_dept(popedomDeptIds,?)='true' or popedomUserIds like ? or popedomEmpGroupIds like ? ) " ;
			param.add(""+loginBean.getDepartCode()+"");
			param.add('%'+loginBean.getId()+'%');
			String group=" ";
			if(!"".equals(loginBean.getGroupId())){
				group=loginBean.getGroupId();
			}
			param.add('%'+group+'%');
		}
		hql +=" order by createTime";
        return list(hql,param);//调用list返回结果
	}
	
	/**
	 * 根据ID查询视图
	 * @return
	 */
	public Result loadModuleView(String viewId){
        return loadBean(viewId, ClientModuleViewBean.class);
	}
	
//	/**
//	 * 新增模板视图
//	 * @return
//	 */
//	public Result addModuleView(ClientModuleViewBean moduleViewBean){
//        return addBean(moduleViewBean);
//	}
	
	/**
	  * 新增模板视图
	  * @param isALL
	  * @param depts
	  * @param users
	  * @param groups
	  * @return
	  */
   public Result addModuleView(final List<String> scopeSqls,final ClientModuleViewBean moduleViewBean ) {
       final Result rs=new Result();
       int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection connection) throws
                           SQLException {
                       Connection conn = connection;
                       try{
                       	long time = new Date().getTime();
                       	Statement stmt = conn.createStatement();
                       	for(String sql : scopeSqls){
                       		stmt.addBatch(sql);
                       	}
                       	int[] counts2 = stmt.executeBatch(); 
                       	if(counts2.length>0){
                       		System.out.println("客户权限修改成功");
                       	}else{
                       		System.out.println("客户权限修改失败");
                       	}
                       }catch(Exception ex){
                           ex.printStackTrace();
                           rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                           rs.setRetVal(ex.getMessage());
                           return;
                       }
                   }
               });
               addBean(moduleViewBean,session);
               return rs.getRetCode();
           }
       });
       rs.setRetCode(retCode);
       return rs;
   }	
	
	/**
	 * 更新模板视图
	 * @return
	 */
	public Result updModuleView(ClientModuleViewBean moduleViewBean){
        return updateBean(moduleViewBean);
	}
	
	/**
	 * 删除模板视图
	 * @return
	 */
   public Result delModuleView(final String viewId) {
       final Result rs=new Result();
       int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection connection) throws
                           SQLException {
                       Connection conn = connection;
                       try{
                    	   String sql = "DELETE FROM CRMCommonTable WHERE viewId='"+viewId+"'" ;
						PreparedStatement pss = conn.prepareStatement(sql);
						pss.executeUpdate();
                   }catch(Exception ex){
                       ex.printStackTrace();
                       rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                       rs.setRetVal(ex.getMessage());
                       return;
                   }
               }
           });
           deleteBean(viewId, ClientModuleViewBean.class, "id",session);
           return rs.getRetCode();
           }
       });
       rs.setRetCode(retCode);
       return rs;
   }	
   
   
   public Result queryModuleGroupBy(final LoginBean loginBean){
       final Result rst = new Result();
       int retCode = DBUtil.execute(new IfDB() {
           public int exec(Session session) {
               session.doWork(new Work() {
                   public void execute(Connection conn) throws  SQLException {
                       try {
							String sql="select moduleId,count(*) as viewCount from crmclientmoduleView where id!='0'";
							if(!"1".equals(loginBean.getId())){
								sql += " and (isAlonePopedmon='0' or dbo.exist_dept(popedomDeptIds,'"+loginBean.getDepartCode()+"')='true' or popedomUserIds like '%"+loginBean.getId()+"%' " ;
								//职员分组判断
								if(loginBean.getGroupId() !=null && !"".equals(loginBean.getGroupId())){
									for(String groupId : loginBean.getGroupId().split(";")){
										sql += " or popedomEmpGroupIds like '%"+groupId+"%'";
									}
								}
								sql +=" ) ";
							}
							sql += " group by moduleId";
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery();
							LinkedHashMap<String, Integer> moduleCountMap = new LinkedHashMap<String, Integer>();
							while(rss.next()){
								moduleCountMap.put(rss.getString("moduleId"), rss.getInt("viewCount"));
							}
							rst.setRetVal(moduleCountMap);
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
    * 向排序表中添加记录
    * @param list
    * @param f_ref
    * @return
    */
   public Result addNeighbour(final String[] list,final String f_ref) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							//删除排序表中原有的记录
							Statement state = conn.createStatement();
							String sql="delete from tblNeighbourDetail where f_ref= '"+f_ref+"'";
							int num = state.executeUpdate(sql) ;
							if(num>=0){
								System.out.println("num的值:"+num);
							}
							//添加记录
							for(int i=0;i<list.length;i++){
								String tabName=list[i];
								sql = "insert into tblNeighbourDetail(id,f_ref,DetailName,OrderBy) " +
                				"values('"+IDGenerater.getId()+"','"+f_ref+"','"+tabName+"','"+i+"')" ;
								state.addBatch(sql) ;
							}
							state.executeBatch();
						}catch (Exception ex) {
						    rs.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
                            rs.setRetVal(ex.getMessage());
							ex.printStackTrace();
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
    * 获取用户创建的兄弟表排序记录的Id
    * @param createBy
    * @param mainName
    * @param viewId
    * @return
    */
   public  String getNeighbourMainId(String createBy,String mainName,String viewId){
	   List<String> param=new ArrayList<String>();
	   String sql="select id from tblNeighbourMain where createBy=? and mainName=?  and viewId= ?";
	   param.add(""+createBy+"");
	   param.add(""+mainName+"");
	   param.add(""+viewId+"");
	   Result rs=this.sqlList(sql, param);
	   List  list= (List) rs.retVal;
	   String mainId="";
	   if(list.size()>0){
		   Object[] obj = (Object[]) list.get(0) ;
		   mainId=obj[0].toString();
	   }else{
		   List<String> paramAdd=new ArrayList<String>();
		   String id=IDGenerater.getId();
		   String time=BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
		   String addSql="insert into tblNeighbourMain(id,mainName,createBy,createTime,finishTime,viewId) values(?,?,?,?,?,?)";
		   paramAdd.add(""+id+"");
		   paramAdd.add(""+mainName+"");
		   paramAdd.add(""+createBy+"");
		   paramAdd.add(""+time+"");
		   paramAdd.add(""+time+"");
		   paramAdd.add(""+viewId+"");
		   Result rsAdd=this.msgSql(addSql, paramAdd);
		   if(rsAdd.retCode==ErrorCanst.DEFAULT_SUCCESS){
			   System.out.println("插入新纪录成功");
		   }
		   mainId=id;
	   }
	   return mainId;
   }
	/**
	 * jdbc调用公共方法
	 * @param sql
	 * @param param
	 * @return
	 */
	public Result msgSql(final String sql, final List param) {
      final Result rst = new Result();
      int retCode = DBUtil.execute(new IfDB() {
          public int exec(Session session) {
              session.doWork(new Work() {
                  public void execute(Connection conn) throws
                          SQLException {
                      try {
                           PreparedStatement pstmt = conn.prepareStatement(sql);
                           for(int i = 1;i<=param.size();i++){
                               pstmt.setObject(i,param.get(i-1));
                           }
                          int row = pstmt.executeUpdate();
                          if (row > 0) {
                             rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                             rst.setRealTotal(row);
                          }
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
	 * 根据moduleId查找客户转移映射信息 
	 * @param moduleId
	 * @return
	 */
	public Result findClientTransferMap(String moduleId){
		List param = new ArrayList();
		String hql = "from CRMClientTransferMapBean where moduleId=?";
		param.add(""+moduleId+"");
		return list(hql, param);
	}
	
	/**
	 * 根据表结构表名与字段名查询本字段在表结构中的数量
	 * @param tableNameStr 表结构表名
	 * @param fieldName 字段名
	 * @return
	 */
	public Result checkFieldCount(String tableNameStr,String fieldName){
		String sql = "select count(id) from tblDBFieldInfo where tableId in (select id from tblDBTableInfo where tableName in("+tableNameStr+")) and fieldName = '"+fieldName+"'";
		return sqlList(sql, new ArrayList());
	}
	
	/**
	 * 根据视图ID查找邻居表排序信息
	 * @param viewId
	 * @return
	 */
	public Result brotherSortQueryByViewId(String viewId){
		ArrayList list = new ArrayList(); 
		String sql = "select id,brotherTab,viewId from CRMBrotherSort where viewId=?";
		list.add(viewId);
		return sqlList(sql,list);
	}
	
	/**
	 * 添加邻居表排序信息
	 * @param viewId
	 * @param loginBean
	 * @return
	 */
	public Result addBrotherSort(String viewId,LoginBean loginBean){
		List<String> paramAdd=new ArrayList<String>();
		String addSql="insert into CRMBrotherSort(id,brotherTab,createBy,createTime,lastUpdateBy,lastUpdateTime,viewId) values(?,?,?,?,?,?,?)";
		String id=IDGenerater.getId();
		String time=BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
		paramAdd.add(""+id+"");
		paramAdd.add("");
		paramAdd.add(""+loginBean.getId()+"");
		paramAdd.add(""+time+"");
		paramAdd.add(""+loginBean.getId()+"");
		paramAdd.add(""+time+"");
		paramAdd.add(""+viewId+"");
		Result rsAdd=this.msgSql(addSql, paramAdd);
		return rsAdd;
	}
	
	/**
	 * 更新邻居表排序信息
	 * @param viewId
	 * @param loginBean
	 * @return
	 */
	public Result updateBrotherSort(String viewId,String orderTab){
		List<String> paramAdd=new ArrayList<String>();
		String addSql="update CRMBrotherSort set brotherTab = ? where viewId = ?";
		paramAdd.add(""+orderTab+"");
		paramAdd.add(""+viewId+"");
		Result rs=this.msgSql(addSql, paramAdd);
		return rs;
	}
	
	/**
	 * 根据权限获取能看到的模板
	 * @return
	 */
	public Result getFilterModules(LoginBean loginBean){
		String sql="select CRMClientModuleView.moduleId,CRMClientModule.moduleName,CRMClientModule.tableInfo,CRMClientModule.clientFieldsMapping,count(CRMClientModuleView.id),CRMClientModule.createTime " +
				"from CRMClientModuleView left join CRMClientModule on CRMClientModuleView.moduleId = CRMClientModule.id  " +
				"where CRMClientModuleView.id != '0' ";
		List<String> param = new ArrayList<String>();
		if(!"1".equals(loginBean.getId())){
			sql += " and (isAlonePopedmon='0' or dbo.exist_dept(CRMClientModuleView.popedomDeptIds,?)='true' or CRMClientModuleView.popedomUserIds like ? or CRMClientModuleView.popedomEmpGroupIds like ? ) " ;
			param.add(""+loginBean.getDepartCode()+"");
			param.add('%'+loginBean.getId()+'%');
			String group=" ";
			if(!"".equals(loginBean.getGroupId())){
				group=loginBean.getGroupId();
			}
			param.add('%'+group+'%');
		}
		sql +=" group by CRMClientModuleView.moduleId,CRMClientModule.moduleName,CRMClientModule.createTime,CRMClientModule.tableInfo,CRMClientModule.clientFieldsMapping order by CRMClientModule.createTime";
        return sqlList(sql,param);//调用list返回结果
	}
}
