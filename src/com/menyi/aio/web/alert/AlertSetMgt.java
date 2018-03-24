package com.menyi.aio.web.alert;

import java.sql.*;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.SysAlertBean;
import com.menyi.web.util.*;

/**
 * 
 * <p>Title:预警设置Mgt</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class AlertSetMgt extends AIODBManager{

	/**
	 * 添加预警设置
	 * @param sysAlertBean
	 * @return
	 */
	public Result addAlertSet(SysAlertBean sysAlertBean){
		final String alertNameId = sysAlertBean.getAlertName();						//预警名称多语言
		final String bewrite = sysAlertBean.getBewrite();							//条件概述
		if(alertNameId != null && !"".equals(alertNameId)){
			sysAlertBean.setAlertName(IDGenerater.getId());
		}else{
			sysAlertBean.setAlertName("");
		}
		if(bewrite != null && !"".equals(bewrite)){
			sysAlertBean.setBewrite(IDGenerater.getId());
		}else{
			sysAlertBean.setBewrite("");
		}
		//插入数据
		final Result result = new Result();
		Result rs = addBean(sysAlertBean);
		final SysAlertBean sysAlert = sysAlertBean;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//如果数据插入成功在添加多语言数据
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn) throws SQLException {
							try{
								//预警名称添加多语言
								KRLanguageQuery.saveToDB(dealLanguage(alertNameId), sysAlert.getAlertName(), conn);
								
								//条件概述添加多语言
								KRLanguageQuery.saveToDB(dealLanguage(bewrite), sysAlert.getBewrite(), conn);
								
							} catch (Exception ex) {
								result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								ex.printStackTrace();
							}
						}
					});
					return result.retCode ;
				}
			});
		}
		return result;
	}
	
	/**
	 * 把多语言组装成HashMap  zh_CN:xxx;en:xxx;zh_TW:xxx; 
	 * @param str
	 * @return
	 */
	public HashMap dealLanguage(String strs){
		HashMap map = new HashMap();
		if(strs != null && !"".equals(strs)){
			String[] str = strs.split(";");
			for(String s : str){
				if(s.length()>0){
					String[] v = s.split(":");
					if(v.length>1){
						map.put(v[0], v[1]);
					}
				}
			}
		}
		return map;
	}
	
	/**
	 * 修改预警设置
	 * @param sysAlertBean
	 * @param old_alertName
	 * @param old_bewrite
	 * @return
	 */
	public Result updateAlertSet(final SysAlertBean sysAlertBean, final String old_alertName, final String old_bewrite, final String alertType){
		final String alertNameId = sysAlertBean.getAlertName();
		final String bewrite = sysAlertBean.getBewrite();
		if("alertItemSet".equals(alertType)){
			if(alertNameId != null && !"".equals(alertNameId)){
				sysAlertBean.setAlertName(IDGenerater.getId());
			}else{
				sysAlertBean.setAlertName("");
			}
			if(bewrite != null && !"".equals(bewrite)){
				sysAlertBean.setBewrite(IDGenerater.getId());
			}else{
				sysAlertBean.setBewrite("");
			}
		}
		final SysAlertBean sysAlert = sysAlertBean;
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							Statement st = conn.createStatement();
							//删除子表数据
							StringBuffer sql = new StringBuffer("delete tblSysAlertDet where f_ref='"+sysAlert.getId()+"'");
							st.addBatch(sql.toString());
							
							if("alertItemSet".equals(alertType)){
								//删除多语言
								st.addBatch("delete tblLanguage where id in ('"+old_alertName+"','"+old_bewrite+"')");
							}
							st.executeBatch();
							if("alertItemSet".equals(alertType)){
								//预警名称添加多语言
								KRLanguageQuery.saveToDB(dealLanguage(alertNameId), sysAlert.getAlertName(), conn);
								
								//条件概述添加多语言
								KRLanguageQuery.saveToDB(dealLanguage(bewrite), sysAlert.getBewrite(), conn);
							}
						} catch (Exception ex) {
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
						}
					}
				});
				return rs.retCode ;
			}
		});
		rs.retCode = retCode ;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			return updateBean(sysAlert);
		}
		//修改数据
		return rs;
	}
	
	/**
	 * 根据预警Id加载预警信息
	 * @param keyid
	 * @return
	 */
	public Result loadBean(final String keyid){
		return loadBean(keyid, SysAlertBean.class);
	}
	
	/**
	 * 删除预警设置
	 * @param keyid
	 * @return
	 */
	public Result deleteAlertSet(final String keyid){
		return deleteBean(keyid, SysAlertBean.class,"id");
	}
	/**
	 * 查询预警设置数据
	 * @param keyids
	 * @param statusType
	 * @return
	 */
	public Result queryAlertData(final String locale){
		final Result result=new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							StringBuffer sql = new StringBuffer("select tblSysAlert.id, l."+locale+" as alertName, tblSysAlert.Status, tblSysAlert.ModuleType, ");
							sql.append("tblSysAlert.modelId, tblSysAlert.condition, tblSysAlert.ActionTime, tblSysAlert.ActionFrequency, tblSysAlert.AlertType, ");
							sql.append("tblLanguage."+locale+" as modelName,tblSysAlert.nextAlertTime,tblSysAlert.Remark,tblSysAlert.alertCode,la."+locale+" as bewrite,tblSysAlert.conditionStatus ");
							sql.append(" from tblSysAlert left join tblLanguage l on l.id=tblSysAlert.AlertName left join tblLanguage la on la.id=tblSysAlert.bewrite left join tblReports on tblReports.reportNumber=tblSysAlert.modelId ");
							sql.append("left join tblLanguage on tblLanguage.id=tblReports.reportName where tblSysAlert.isHidden=0 ");
                    		PreparedStatement ps = conn.prepareStatement(sql.toString());
                    		ResultSet rs = ps.executeQuery();
                    		ArrayList<Object[]> list = new ArrayList<Object[]>();
                    		while(rs.next()){
                    			Object[] obj = new Object[16];
                    			obj[0] = rs.getString("id");
                    			obj[1] = rs.getString("alertName");					//预警的名称
                    			obj[2] = rs.getInt("Status");						//预警的状态（启用0,停用1）
                    			obj[3] = rs.getString("ModuleType");
                    			obj[4] = rs.getString("modelId");
                    			obj[5] = rs.getString("condition");
                    			obj[6] = rs.getString("ActionTime");
                    			obj[7] = rs.getString("ActionFrequency");
                    			obj[8] = rs.getString("AlertType");
                    			obj[9] = rs.getString("modelName");
                    			obj[10] = rs.getString("nextAlertTime");
                    			obj[11] = rs.getString("Remark");
                    			obj[12] = rs.getString("alertCode");
                    			obj[13] = rs.getString("bewrite");
                    			obj[14] = rs.getInt("conditionStatus");
                    			list.add(obj);
                    		}
                    		
                    		//预警提醒用户
                    		sql = new StringBuffer("select id,f_ref,alertUser from tblSysAlertDet");
                    		ps = conn.prepareStatement(sql.toString());
                    		rs = ps.executeQuery();
                    		while(rs.next()){
                    			String f_ref = rs.getString("f_ref");
                    			for(Object[] obj : list){
                    				String detList = (String)obj[15];
                    				if(f_ref.equals(obj[0])){
                    					//当子表的f_ref等于主表的id时
                    					if(detList == null){
                    						detList = "";
                    					}
                    					obj[15] = detList+rs.getString("alertUser")+",";
                    					break;
                    				}
                    			}
                    		}
                    		result.setRetVal(list);
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
						}
					}
				});
				return result.retCode ;
			}
		});
		result.retCode = retCode ;
		return result;
	}
	
	/**
	 * 查询所有的分组
	 * @param locale
	 * @return
	 */
	public Result queryAlertGroup() {
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							//查询所有系统配置
							String sql = "select id,groupName from tblAlertGroup order by orderby ";
							PreparedStatement ps = conn.prepareStatement(sql);
							ResultSet rs = ps.executeQuery();
							List list = new ArrayList();
							while (rs.next()) {
								Object[] str = new Object[3];
								str[0] = rs.getString("id");
								str[1] = rs.getString("groupName");									
								list.add(str);
							}							
							result.setRetVal(list);
						} catch (Exception ex) {
							ex.printStackTrace();
							result.setRetCode(ErrorCanst.EXECUTE_DB_ERROR);
							result.setRetVal(ex.getMessage());
							return;
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
	 * 查询预警设置列表
	 * @return
	 */
	public Result queryAlertSet(final String locale,final String searType,final String searValue){
		StringBuffer sql = new StringBuffer("select tblSysAlert.id,l."+locale+" as AlertName, tblSysAlert.Status, tblSysAlert.ModuleType, ");
		sql.append("tblSysAlert.modelId, tblSysAlert.condition, tblSysAlert.ActionTime, tblSysAlert.ActionFrequency, tblSysAlert.AlertType, ");
		sql.append("tblLanguage."+locale+" as modelName,tblSysAlert.Remark,tblSysAlert.alertCode,la."+locale+" as bewrite,tblSysAlert.conditionStatus, ");
		sql.append("tblAlertGroup.groupName,tblSysAlert.ModuleType,tblSysAlert.isHidden,");
		sql.append("tblSysAlert.nextAlertTime from tblSysAlert left join tblLanguage l on tblSysAlert.AlertName=l.id left join tblReports on tblReports.reportNumber=tblSysAlert.modelId ");
		sql.append("left join tblLanguage on tblLanguage.id=tblReports.reportName left join tblLanguage la on tblSysAlert.bewrite=la.id ");
		sql.append(" left join tblAlertGroup on tblAlertGroup.id=tblSysAlert.ModuleType where 1=1 ");
		
		if(searType != null && "group".equals(searType) && !"all".equals(searValue)){
			//所属组查询
			sql.append(" and tblSysAlert.ModuleType='"+searValue+"' ");
		}else if(searType != null && "search".equals(searType)){
			//关键字查询
			sql.append(" and (tblSysAlert.ModuleType like '%"+searValue+"%' or l."+locale+" like '%"+searValue+"%' or tblLanguage."+locale+" like '%"+searValue+"%' or tblSysAlert.condition like '%"+searValue+"%' )");
		}else if(searType != null && "status".equals(searType) && !"all".equals(searValue)){
			//状态查询
			if("0".equals(searValue)){
				//显示
				sql.append(" and tblSysAlert.isHidden=0");
			}else if("1".equals(searValue)){
				//隐藏
				sql.append(" and tblSysAlert.isHidden=1");
			}else if("2".equals(searValue)){
				//启用
				sql.append(" and tblSysAlert.status=0");
			}else if("3".equals(searValue)){
				//停用
				sql.append(" and tblSysAlert.status=1");
			}
		}
		List paramList = new ArrayList();
		return sqlListMap(sql.toString(), paramList, 0, 0);
	}
	
	/**
	 * 查询预警单条记录
	 * @param keyid
	 * @return
	 */
	public Result loadAlertSet(final String keyid, final String locale){
		final Result result=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                    	try{
                    		StringBuffer sql = new StringBuffer("select tblSysAlert.*,l.id as alert_lan_id,('zh_TW:'+isnull(l.zh_TW,'')+';zh_CN:'+isnull(l.zh_CN,'')+';en:'+isnull(l.en,'')+';') as alert_lan,");
                    		sql.append("ll.id as bewrite_lan_id,('zh_TW:'+isnull(ll.zh_TW,'')+';zh_CN:'+isnull(ll.zh_CN,'')+';en:'+isnull(ll.en,'')+';') as bewrite_lan,");
                    		sql.append("tblLanguage."+locale+" as modelName,tblAlertGroup.groupName ");
                    		sql.append("from tblSysAlert left join tblReports on tblReports.reportNumber=tblSysAlert.modelId ");
                    		sql.append("left join tblLanguage on tblLanguage.id=tblReports.reportName ");
                    		sql.append("left join tblAlertGroup on tblSysAlert.ModuleType=tblAlertGroup.id ");
                    		sql.append("left join tblLanguage l on l.id=tblSysAlert.AlertName ");
                    		sql.append("left join tblLanguage ll on ll.id=tblSysAlert.bewrite ");
                    		sql.append("where tblSysAlert.id='"+keyid+"'");
                    		Statement st = conn.createStatement();
                    		ResultSet rs = st.executeQuery(sql.toString());
                    		HashMap map=new HashMap();
                    		if(rs.next()){
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				map.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				map.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			map.put(rs.getMetaData().getColumnName(i), obj);
                            		}
                            		if("AlertType".equals(rs.getMetaData().getColumnName(i))){
                            			if(obj!=null){
                            				List l = new ArrayList();
                            				String alertTypes = String.valueOf(obj);
                            				String[] alertTypeStr = alertTypes.split(",");
                            				for(String s : alertTypeStr){
                            					if(!"".equals(s)){
                            						l.add(s);
                            					}
                            				}
                            				map.put("alertTypes", l);
                            			}
                            		}
                            	}
                    		}
                    		String sqldet = "select tblSysAlertDet.id,tblSysAlertDet.f_ref,tblSysAlertDet.alertUser,tblEmployee.empFullName from tblSysAlertDet left join tblEmployee on tblSysAlertDet.alertUser=tblEmployee.id where tblSysAlertDet.f_ref='"+keyid+"'";
                    		rs = st.executeQuery(sqldet);
                    		List detList = new ArrayList();
                    		while(rs.next()){
                    			HashMap mapDet=new HashMap();
                            	for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                            		Object obj=rs.getObject(i);
                            		if(obj==null){
                            			if(rs.getMetaData().getColumnType(i)==Types.NUMERIC||rs.getMetaData().getColumnType(i)==Types.INTEGER){
                            				mapDet.put(rs.getMetaData().getColumnName(i), 0);
                            			}else{
                            				mapDet.put(rs.getMetaData().getColumnName(i), "");
                            			}
                            		}else{
                            			mapDet.put(rs.getMetaData().getColumnName(i), obj);
                            		}
                            	}
                            	detList.add(mapDet);
                    		}
                    		map.put("detList", detList);
                    		result.setRetVal(map);
                    	} catch (Exception ex) {
                    		result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                        }
                    }
            	});
            	return result.retCode ;
            }
    	});
    	result.retCode = retCode ;
        return result ;
	}
	
	/**
	 * 显示或者隐藏预警
	 * @param sysAlertBean
	 * @return
	 */
	public Result openOrStop(final String[] keyids,final String statusType){
		final Result result=new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try{
							int status = 0;
							if("stop".equals(statusType)){
								//隐藏
								status = 1;
							}
							String str = "";
							for(int i=0;i<keyids.length;i++){
								str += "'"+keyids[i]+"'";
								if(i<keyids.length-1){
									str += ",";
								}
							}
							StringBuffer sql = new StringBuffer("update tblSysAlert set [isHidden]="+status+" where id in ("+str+")");
							PreparedStatement ps = conn.prepareStatement(sql.toString());
							ps.executeUpdate();
						} catch (Exception ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
						}
					}
				});
				return result.retCode ;
			}
		});
		result.retCode = retCode ;
		return result;
	}
}
