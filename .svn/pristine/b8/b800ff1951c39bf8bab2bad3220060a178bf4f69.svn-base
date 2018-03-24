package com.koron.oa.individual.workPlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.bean.OADayWorkPlanBean;
import com.menyi.aio.bean.AdviceBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.msgcenter.msgif.CancelMsgReq;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.PublicMgt;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: 我的工作计划管理的数据库操作类</p>
 *
 * @Date:2010-5-7
 * @Copyright: 科荣软件
 * @Author 周新宇
 */
public class OADateWorkPlanMgt extends AIODBManager{

	private PublicMgt pubMgt = new PublicMgt();
	
    public Result loadWorkPlan(String keyId){
        //创建参数
        return this.loadBean(keyId, OADayWorkPlanBean.class);
    }

    
    public Result getWorkPlan(String planType,String employeeId,String startDate,String endDate,
    		String statusId,String keyWord,String planId,String flag,String createTime,String endTime,boolean flag2,String userId){
    	List param = new ArrayList();    	
        String hql = "select bean from OADayWorkPlanBean as bean where 1=1 ";        
        //如标题不为空，则做标题模糊查询
        if(planId !=null && planId.length()>0){
        	hql += " and bean.id=? ";
        	param.add(planId);
        }else{
        	hql += " and planType=? and employeeID=? " ;
	        param.add(planType);
	        param.add(employeeId);
	        if("search".equals(flag)){
	        	if(keyWord!=null && keyWord.trim().length()>0){
		        	hql += " and (bean.title like ?) " ;
		        	param.add("%"+keyWord+"%") ;
		         }
	        	hql += "and bean.beginDate >= ? and bean.endDate <= ? " ;
	        	param.add(createTime);
	            param.add(endTime);
	        }else{
		        if(keyWord!=null && keyWord.trim().length()>0){
		        	hql += " and (bean.title like ? or bean.content like ? or bean.summary like ?) " ;
		        	param.add("%"+keyWord+"%") ;
		        	param.add("%"+keyWord+"%") ;
		        	param.add("%"+keyWord+"%") ;
		        }else{
		        	hql += "and bean.beginDate >= ? and bean.endDate <= ? " ;
		        	param.add(startDate);
		            param.add(endDate);
		        }
		        if(statusId != null && statusId.length() > 0){
		        	hql += " and bean.statusId=? ";
		        	param.add(Integer.parseInt(statusId));
		        }
	        }
        }

        if(flag2==true){
        	hql += " and bean.createBy!=? ";
        	param.add(userId);
        }
        
        //按时间，自己，紧急排序
        hql +=" order by bean.beginDate ,bean.grade";
        //调用list返回结果     
        return list(hql, param);
    }
    
    public Result queryWorkPlan(String planType,String userId,String startDate,String endDate,String keyWord){
    	List param = new ArrayList();    	
        String hql = "select bean from OADayWorkPlanBean as bean where 1=1 ";        
        
        //关键字
    	if(keyWord!=null && keyWord.trim().length()>0){
    		hql += " and (bean.title like ? or bean.content like ? or bean.summary like ?) " ;
        	param.add("%"+keyWord+"%") ;
        	param.add("%"+keyWord+"%") ;
        	param.add("%"+keyWord+"%") ;
        }
    	if(planType!=null && planType.trim().length()>0){
	    	hql += " and planType=? " ;
	        param.add(planType);
    	}
    	if(startDate!=null && startDate.trim().length()>0){
    		hql += "and bean.beginDate >= ? " ;
	        param.add(startDate + " 00:00:00");
    	}
    	if(endDate!=null && endDate.trim().length()>0){
    		hql += "and bean.endDate <= ? " ;
	        param.add(endDate + " 23:59:59");
    	}
    	/*我自己创建的 或 知晓协同给我的*/
    	hql += " and (createBy=? or bean.id in (select item.f_ref from OAPlanAssItemBean item where item.associateId in ('1','2') and item.keyId=?))";
	    param.add(userId);
	    param.add(userId);
	    hql += " order by bean.beginDate desc";
        //调用list返回结果
        return list(hql, param);
    }
    
    public Result getAssociateWorkPlan(String planType,String employeeId,String startDate,String endDate,String associateId){
    	List param = new ArrayList();
    	//先查询需自己协同的计划
    	String sql = " select id from tbldayworkplan a where a.planType=? and a.beginDate >= ? and a.endDate <=? and a.id in(select f_ref from tblPlanAssItem where AssociateId=? and keyId=?) ";
    	param.add(planType);        
        param.add(startDate);
        param.add(endDate);
        param.add(associateId);
        param.add(employeeId);
        Result rs = sqlList(sql,param);
        String ids="";
        for(int i=0;rs.retCode==ErrorCanst.DEFAULT_SUCCESS&&rs.retVal != null &&i<((List)rs.retVal).size();i++){
        	ids += ",'"+((Object[])((List)rs.retVal).get(i))[0]+"'";
        }
    	if(ids.length() > 0){
    		ids = ids.substring(1);
    	}else{
    		rs.retVal = new ArrayList();
    		return rs;
    	}
        //创建参数
        param = new ArrayList();
        String hql = "select bean from OADayWorkPlanBean as bean where  id in ("+ids+") order by beginDate ,grade";
        //如标题不为空，则做标题模糊查询
        //调用list返回结果
        return list(hql, param);
    }
    
    /**
     * 查看是否存在关联我的 计划
     * @param planType
     * @param employeeId
     * @param startDate
     * @param endDate
     * @param associateId
     * @return
     */
    public Result existAssociateWorkPlan(String planType,String employeeId,String startDate,String endDate,String associateId){
    	final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try { 
                        	String sql = "" ;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("OAWorkPlanMgt queryAssPeopleName method "+ex) ;
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    
    
    public Result queryAssPeopleName(final String keyIds){
    	final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try { 
                        	String sql = "select empFullName from tblEmployee where id in "+keyIds;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	String peopleName = "" ;
                        	while(rss.next()){
                        		peopleName += rss.getString("empFullName")+";" ;
                        	}
                        	rs.setRetVal(peopleName) ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("OAWorkPlanMgt queryAssPeopleName method "+ex) ;
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    
    /**
     * 查询客户的名称
     * @param userId
     * @return
     */
    public Result queryAssClient(final String clientIds){
    	final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try { 
                        	String sql = "select clientName from CRMClientInfo where id in "+clientIds;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	ResultSet rss = pss.executeQuery() ;
                        	String clientName = "" ;
                        	while(rss.next()){
                        		clientName += rss.getString("clientName")+";" ;
                        	}
                        	rs.setRetVal(clientName) ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("OAWorkPlanMgt queryAssClient method "+ex) ;
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    
    public Result getEventWorkPlan(String department,String userId,String title,
    		String statusId,String beginDate,String endDate,String loginId,String scopeSQL,String typeId){
        //创建参数
        List param = new ArrayList();
        String hql = "select a.id,a.title,a.departmentCode,a.employeeId,a.beginDate,a.endDate,a.statusId from tbldayworkplan a "+
        	" where a.planType=? " ;
        	
        param.add("event") ;
        
        if("myPlan".equals(typeId)){
        	//查我的工作计划
        	hql += " and a.employeeId=? ";
        	param.add(loginId) ; 
        }else if(typeId != null && typeId.length() > 0){
        	hql += " and a.id in(select f_ref from tblPlanAssItem where AssociateId=? and keyId=?) ";
        	param.add(typeId) ; 
        	param.add(loginId) ;        	
        }else if((loginId!=null && loginId.trim().length()>0) && !"1".equals(loginId)){
        		hql += " and (a.employeeId=?";
        	if(scopeSQL!=null && scopeSQL.trim().length()>0){
        		hql +=  scopeSQL ;
        	}
        		hql += ")";
        	param.add(loginId) ;        
        }        
        if(department != null && department.length() > 0 ){
        	hql +=" and a.departmentCode = ? ";
        	param.add(department);
        	
        }
        if(userId != null && userId.length() > 0 ){
        	hql +=" and a.employeeId = ? ";
        	param.add(userId);
        }
        //如标题不为空，则做标题模糊查询
        if(title != null && title.length() > 0 ){
        	hql +=" and a.title like '%'+?+'%' ";
        	param.add(title);
        }
        if(statusId != null && statusId.length() > 0 ){
        	hql +=" and a.statusId =? ";
        	param.add(statusId);
        }
        if(beginDate != null && beginDate.length() > 0 ){
        	hql +=" and a.beginDate >=? ";
        	param.add(beginDate+" 00:00:00");
        }
        if(endDate != null && endDate.length() > 0 ){
        	hql +=" and a.endDate <=? ";
        	param.add(endDate+" 23:59:59");
        }
        hql +=" order by beginDate desc";
        //调用list返回结果
        return sqlList(hql, param);
    }
     
    public Result getEmployee(final String[] empIds){
       	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String ids = "";
							for(String s:empIds){
								ids +="'"+s+"',";
							}
							if(ids.length() > 0){
								ids = ids.substring(0,ids.length() -1);
								String sql = " select a.id,a.empFullName from tblemployee a   where id in("+ids+") ";
								PreparedStatement pss = conn.prepareStatement(sql);
								ResultSet r = pss.executeQuery();
								HashMap map = new HashMap();
								result.retVal = map;
								while(r.next()){
									map.put(r.getString("id"), r.getString("empFullName"));
								}
							}							
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt.getEmployee : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    public Result getPlanCommit(final String[] planIds){
       	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String ids = "";
							for(String s:planIds){
								ids +="'"+s+"',";
							}
							if(ids.length() > 0){
								ids = ids.substring(0,ids.length() -1);
								String sql = " select a.*,b.empFullName from tblPlanRemark a join tblemployee b on a.employeeId = b.id where f_ref in("+ids+") and (commitId is null or commitId='' or commitId ='DP') order by a.createTime ";
								PreparedStatement pss = conn.prepareStatement(sql);
								ResultSet r = pss.executeQuery();
								ArrayList list = new ArrayList();
								result.retVal = list;
								while(r.next()){
									list.add(new String[]{r.getString("id"),r.getString("f_ref"),r.getString("remarkType"),r.getString("content"),r.getString("employeeId"),r.getString("empFullName"),r.getString("createTime")});
								}
							}							
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt.getPlanAssItem : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    /**
     * 查询评论的回复
     * @param planIds
     * @return
     */
    public Result getPlanCommit2(final String[] planIds){
       	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String ids = "";
							for(String s:planIds){
								ids +="'"+s+"',";
							}
							if(ids.length() > 0){
								ids = ids.substring(0,ids.length() -1);
								String sql = " select a.*,b.empFullName from tblPlanRemark a join tblemployee b on a.employeeId = b.id where commitId in (select id from tblPlanRemark where f_ref in ("+ids+")) order by a.createTime ";
								PreparedStatement pss = conn.prepareStatement(sql);
								ResultSet r = pss.executeQuery();
								HashMap<String, ArrayList<String[]>> commitMap = new HashMap<String, ArrayList<String[]>>() ;
								while(r.next()){
									String commitId = r.getString("commitId") ;
									if(!commitMap.containsKey(commitId)){
										ArrayList list = new ArrayList();
										list.add(new String[]{r.getString("id"),r.getString("f_ref"),r.getString("remarkType"),r.getString("content"),r.getString("employeeId"),r.getString("empFullName"),r.getString("createTime")});
										commitMap.put(commitId, list) ;
									}else{
										ArrayList list = commitMap.get(commitId) ;
										list.add(new String[]{r.getString("id"),r.getString("f_ref"),r.getString("remarkType"),r.getString("content"),r.getString("employeeId"),r.getString("empFullName"),r.getString("createTime")}) ;
										commitMap.put(commitId, list) ;
									}
								}
								result.retVal = commitMap;
							}							
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt.getPlanAssItem : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    public Result delCommit(final String planRemarkId,final String adUserId,final String planId){
       	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = " delete tblPlanRemark where id =? ";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, planRemarkId);
							pss.executeUpdate();
							
//							sql= " delete tbladvice where receive=? and relationId=? ";
//							pss = conn.prepareStatement(sql);
//							pss.setString(1, adUserId);
//							pss.setString(2, planId);
//							pss.executeUpdate();
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt.getPlanAssItem : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    public Result getPlanAssItem(final String[] planIds,final String planType){
       	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String ids = "";
							for(String s:planIds){
								ids +="'"+s+"',";
							}
							if(ids.length() > 0){
								ids = ids.substring(0,ids.length() -1);
								String sql = " select * from tblPlanAssItem where f_ref in("+ids+") order by assTime desc";
								PreparedStatement pss = conn.prepareStatement(sql);
								ResultSet r = pss.executeQuery();
								if("event".equals(planType)){
									ArrayList list = new ArrayList();
									result.retVal = list;
									while(r.next()){
										list.add(new String[]{r.getString("id"),r.getString("associateId"),r.getString("keyId"),r.getString("keyName"),r.getString("f_ref")});
									}
								}else{
									HashMap<String, ArrayList<String[]>> map = new HashMap<String, ArrayList<String[]>>() ;
									while(r.next()){
										String f_ref = r.getString("f_ref") ;
										if(!map.containsKey(f_ref)){
											ArrayList list = new ArrayList();
											list.add(new String[]{r.getString("id"),r.getString("associateId"),r.getString("keyId"),r.getString("keyName"),r.getString("f_ref")});
											map.put(f_ref, list) ;
										}else{
											ArrayList list = map.get(f_ref) ;
											list.add(new String[]{r.getString("id"),r.getString("associateId"),r.getString("keyId"),r.getString("keyName"),r.getString("f_ref")});
											map.put(f_ref, list) ;
										}
									}
									result.retVal = map;
								}
							}							
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt.getPlanAssItem : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    /**
     * 判断此人是否在改部门里
     * @param name
     * @param numb
     * @return
     */
    public String isexsit(String name,String numb){   	
		String hql = "select departmentcode from tblemployee where empfullname=?";
		ArrayList param = new ArrayList();
		param.add(name);
		Result rs = sqlList(hql, param);
		ArrayList res = (ArrayList)rs.retVal;
		Object dpCode = ((Object[])res.get(0))[0];
		if(numb.equals(dpCode.toString())){
			return "true";
		}else{
			return "flase";
		}   	
    }
    /**
     * 判断此人的设置权限能看谁
     * @param name
     * @param numb
     * @return
     */
    public String queryPower(String loginId){   	
		String sql = "select BySeeUserID,BySeeDeptOfClassCode,BySeeEmpGroup from OAJobPopedomSetting where 1=? and seepersonid like '%"+loginId+"%'";
		ArrayList param = new ArrayList();
		param.add("1");	
		Result rs = sqlList(sql, param);
		ArrayList res = (ArrayList)rs.retVal;
		String personId = loginId;
		String personCode="";
		if(res.size()>0){
			for (int i = 0; i < res.size(); i++) {
				Object detpcode = ((Object[])res.get(i))[1];
				personCode += ","+detpcode.toString();
				Object bySeeUser = ((Object[])res.get(i))[0];
				Object bySeeGroup = ((Object[])res.get(i))[2];
				personId +=","+bySeeUser.toString()+","+bySeeGroup.toString();
			}
			//根据部门code查询epmloyee
			String[] pCode = personCode.split(",");
			if(pCode.length>0){
				String hql = "select id from tblemployee where 1=? and departmentcode in (";
				for (int i = 0; i < pCode.length; i++) {
					hql += "'"+pCode[i]+"',";					
				}				
				hql +="'')";
				ArrayList par = new ArrayList();
				par.add("1");	
				Result rss = sqlList(hql, par);
				ArrayList ress = (ArrayList)rss.retVal;				
				if(ress.size()>0){
					for (int j = 0; j < ress.size(); j++) {
						Object codePId = ((Object[])ress.get(j))[0];						
						personId +=","+codePId.toString();
					}
				}
			}	
		}
		return personId;			
    }
    
    public Result getPlanAssItemCount(final String planType,final String employeeId,final String startDate,final String endDate,final String associateId){
       	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
								//获取tblPlanAssociate为职员的关联代号wyy
								String hql = " select id from tblPlanAssociate  where statusId=? and isemployee=? ";
								PreparedStatement pssh = conn.prepareStatement(hql);
								pssh.setString(1,"0");
								pssh.setString(2, "1");
								ResultSet rh = pssh.executeQuery();
								List listArr=new ArrayList<String>();
								while(rh.next()){
									listArr.add(rh.getString("id"));									
								}
								List list=new ArrayList<String>();
								if(listArr.size()>0){
									for (int i = 0; i < listArr.size(); i++) {
										String sql = " select count (*)from tbldayworkplan a where a.planType=? and a.beginDate >=?  and a.endDate <=? and a.id in(select f_ref from tblPlanAssItem where AssociateId=? and keyId=?) ";
										PreparedStatement pss = conn.prepareStatement(sql);
										pss.setString(1,planType);
										pss.setString(2, startDate);
										pss.setString(3, endDate);
										pss.setString(4, listArr.get(i).toString());
										pss.setString(5, employeeId);
										ResultSet r = pss.executeQuery();										
										while(r.next()){
											list.add(r.getString(1));
										}
									}								
								}
								/*String sql = " select count (*)from tbldayworkplan a where a.planType=? and a.beginDate >=?  and a.endDate <=? and a.id in(select f_ref from tblPlanAssItem where AssociateId='1' and keyId=?) ";
								PreparedStatement pss = conn.prepareStatement(sql);
								pss.setString(1,planType);
								pss.setString(2, startDate);
								pss.setString(3, endDate);
								pss.setString(4, employeeId);
								ResultSet r = pss.executeQuery();
								List list=new ArrayList<String>();
								while(r.next()){
									list.add(r.getString(1));
								}
								String sql2 = " select count (*)from tbldayworkplan a where a.planType=? and a.beginDate >=?  and a.endDate <=? and a.id in(select f_ref from tblPlanAssItem where AssociateId='2' and keyId=?) ";
								PreparedStatement pss2 = conn.prepareStatement(sql2);
								pss2.setString(1,planType);
								pss2.setString(2, startDate);
								pss2.setString(3, endDate);
								pss2.setString(4, employeeId);
								ResultSet r2 = pss2.executeQuery();
								while(r2.next()){
									list.add(r2.getString(1));
								}*/
								result.retVal = list;
							 							
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt.getPlanAssItemCount: ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    public Result delAssItem(final String planId,final String assId,final String keyId){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = " delete tblPlanAssItem where f_ref=? and associateId=? and keyId=? ";
							PreparedStatement pss = conn.prepareStatement(sql);							
							pss.setString(1, planId);
							pss.setString(2, assId);
							pss.setString(3, keyId);
							pss.executeUpdate();
							
//							//这里存在多删除数据的可能，因为这里没有区分是不是人员关联项。
//							sql= " delete tbladvice where receive=? and relationId=? ";
//							pss = conn.prepareStatement(sql);
//							pss.setString(1, keyId);
//							pss.setString(2, planId);
//							pss.executeUpdate();
							
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt addDayWorkPlan : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    public Result addAssItem(final String planId,final String planType,final String assId,final String keyId,final String keyName,final String isemployee,final String assName,final String adviceTitle){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = " insert into tblPlanAssItem(id,f_ref,associateId,keyId,keyName,assTime) values(?,?,?,?,?,?) ";
							PreparedStatement pss = conn.prepareStatement(sql);
							
							pss.setString(1, IDGenerater.getId());
							pss.setString(2, planId);
							pss.setString(3, assId);
							pss.setString(4, keyId);
							pss.setString(5, keyName);
							pss.setString(6, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
							pss.executeUpdate();
							
//							if("1".equals(isemployee)){
//								sql= " insert into tbladvice(id,send,title,receive,content,receiveName,sendName,relatMsgId,createBy,lastUpdateBy,createTime,lastUpdateTime,status,exist,type,relationId) "+
//								"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//								pss = conn.prepareStatement(sql);
//								pss.setString(1, IDGenerater.getId());
//								pss.setString(2, "1");
//								pss.setString(3, adviceTitle+assName);
//								pss.setString(4, keyId);
//								String tres= "oa.mydesk.workPlan";
//								if("event".equals(planType)){
//									tres = "crm.event.plan";
//								}
//								pss.setString(5, "<a href=\"javascript:mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&planType="+planType+"&keyId="+planId+"','RES<"+tres+">')\">"+adviceTitle+assName+"</a>");
//								pss.setString(6, keyName);
//								pss.setString(7,"admin");
//				    	        pss.setString(8, "");
//				    	        pss.setString(9, "1");
//				    	        pss.setString(10, "1");
//				    	        pss.setString(11, BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss));
//				    	        pss.setString(12, BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss));
//				    	        pss.setString(13, "noRead");
//				    	        pss.setString(14, "all");
//				    	        pss.setString(15, "plan");
//				    	        pss.setString(16, planId);
//				    	        pss.executeUpdate();
//							}
							
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt addDayWorkPlan : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    public Result delDayWorkPlan(final String[] keyIds){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							for(String keyId:keyIds){
								String sql = " delete tblPlanAssItem where f_ref=? ";
								PreparedStatement pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								pss.executeUpdate();
								sql = " delete tblPlanGoalItem where f_ref=? ";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								pss.executeUpdate();
								sql = " delete tblPlanRemark where f_ref=? ";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								pss.executeUpdate();
								sql = " delete tblDayWorkPlan where id=? ";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								pss.executeUpdate();
								
//								sql= " select id,receive,title from  tbladvice where relationId='"+keyId+"'";	
//								pss = conn.prepareStatement(sql) ;
//	                        	ResultSet rss = pss.executeQuery() ;							
//								String id = "";
//								String receive = "";
//								String title = "";
//								while(rss.next()){
//									id=rss.getString("id");
//									receive=rss.getString("receive");
//									title=rss.getString("title");
//									//取消通知信息																			
//									//MSGConnectCenter.sendAdvice(receive, id, "被删除"+title);
//									MSGConnectCenter.CancelMsg(CancelMsgReq.TYPE_SYS, receive,id);
//								}																
//								sql= " delete tbladvice where relationId=? ";
//								pss = conn.prepareStatement(sql);
//								pss.setString(1, keyId);
//								pss.executeUpdate();
								
								sql = " delete from tblAlert where relationId=?";
								pss = conn.prepareStatement(sql);
								pss.setString(1, keyId);
								pss.executeUpdate();
								
							}							
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt addDayWorkPlan : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    public Result addDayWorkPlan(final OADayWorkPlanBean bean,final String[] assoicates,final String adviceTitle,final LoginBean loginBean){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				Result rs = addBean(bean,session);
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					return rs.retCode;
				}
				session.flush();
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
					    	List<String[]>listAdvice = new ArrayList<String[]>();
							if(assoicates !=null){
								for(String ass:assoicates){
									if(ass != null && ass.length() > 0 ){
										String sql = " insert into tblPlanAssItem(id,f_ref,associateId,keyId,keyName) values(?,?,?,?,?) ";
										PreparedStatement pss = conn.prepareStatement(sql);
										
										pss.setString(1, IDGenerater.getId());
										pss.setString(2, bean.getId());
										String[] keys = ass.split(":");
										pss.setString(3, keys[0]);
										pss.setString(4, keys[1]);
										pss.setString(5, keys[2]);
										pss.executeUpdate();
										
										String nowTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
										//如果是关联人和知晓人消息通知对方
										//XX的工作计划XX添加你为XX
										if("1".equals(keys[3])){
//											sql= " insert into tbladvice(id,send,title,receive,content,receiveName,sendName,relatMsgId,createBy,lastUpdateBy,createTime,lastUpdateTime,status,exist,type,relationId) "+
//												"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//											pss = conn.prepareStatement(sql);
//											String id = IDGenerater.getId();
//											pss.setString(1, id);
//											pss.setString(2, "1");
//											pss.setString(3, adviceTitle+keys[4]);
//											pss.setString(4, keys[1]);
											String tres= "oa.mydesk.workPlan";
											if(bean.getPlanType().equals("event")){
												tres = "crm.event.plan";
											}
//											pss.setString(5, "<a href=\"javascript:mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&flagNews=flagNews&planType="+bean.getPlanType()+"&keyId="+bean.getId()+"','RES<"+tres+">')\">"+adviceTitle+keys[4]+"</a>");
//											pss.setString(6, keys[2]);
//											pss.setString(7,"admin");
//							    	        pss.setString(8, "");
//							    	        pss.setString(9, "1");
//							    	        pss.setString(10, "1");
//							    	        pss.setString(11, nowTime);
//							    	        pss.setString(12, nowTime);
//							    	        pss.setString(13, "noRead");
//							    	        pss.setString(14, "all");
//							    	        pss.setString(15, "plan");
//							    	        pss.setString(16, bean.getId());
//							    	        pss.executeUpdate();
//							    	        // 给客户端用户发送通知消息
//											MSGConnectCenter.sendAdvice(keys[1], id, adviceTitle+keys[4]);
//											new AdviceMgt().add("1", adviceTitle+keys[4], 
//													"<a href=\"javascript:mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&flagNews=flagNews&planType="+bean.getPlanType()+"&keyId="+bean.getId()+"','RES<"+tres+">')\">"+adviceTitle+keys[4]+"</a>", 
//													keys[1], bean.getId(), "plan");
											String[] item = {"1", adviceTitle+keys[4], 
													"<a href=\"javascript:mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&flagNews=flagNews&planType="+bean.getPlanType()+"&keyId="+bean.getId()+"','RES<"+tres+">')\">"+adviceTitle+keys[4]+"</a>", 
													keys[1], bean.getId(), "plan"};
											listAdvice.add(item);
										}else if("2".equals(keys[3])){
											sql = "update CRMClientInfo set LastContractTime=? where id=?";
											pss = conn.prepareStatement(sql);
											pss.setString(1, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
											pss.setString(2, keys[1]);
											pss.executeUpdate();
										}
										
										//如果是关联客户，更新最新动态
										if("5".equals(keys[0])){
											String context = nowTime + " " + loginBean.getName()+"新增了工作计划";
											pubMgt.insertCRMCLientInfoLog(keys[1],"history", context, loginBean.getId(),conn);
										}
									}
								}
							}
							result.setRetVal(listAdvice);
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt addDayWorkPlan : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    public Result updateDayWorkPlan(final OADayWorkPlanBean bean,final String[] assoicates,final String[] goalItems,final String adviceTitle,final LoginBean loginBean){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				Result rs = updateBean(bean,session);
				if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
					return rs.retCode;
				}
				session.flush();
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
					    	List<String[]>listAdvice = new ArrayList<String[]>();
							//查出所有与人相关的关联项，用于比较如果被删除，则要同时删除相关消息
							String sql = "   select a.keyId from tblPlanAssItem a join tblPlanAssociate b on a.associateid=b.id and isEmployee=1  where  a.f_ref = ? ";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, bean.getId());
							ResultSet rset = pss.executeQuery();
							ArrayList<String> list = new ArrayList();
							while(rset.next()){
								list.add(rset.getString(1));
							}
							
							sql = " delete tblPlanAssItem where f_ref=? ";
							pss = conn.prepareStatement(sql);
							pss.setString(1, bean.getId());
							pss.executeUpdate();							
							if(assoicates !=null){
								for(String ass:assoicates){
									if(ass != null && ass.length() > 0 ){
										String[] keys = ass.split(":");
										for(String s:list){
											if(s.equals(keys[1])){
												list.remove(s);
												break;
											}
										}
										
										sql = " insert into tblPlanAssItem(id,f_ref,associateId,keyId,keyName) values(?,?,?,?,?) ";
										pss = conn.prepareStatement(sql);
										pss.setString(1, IDGenerater.getId());
										pss.setString(2, bean.getId());
										
										pss.setString(3, keys[0]);
										pss.setString(4, keys[1]);
										pss.setString(5, keys[2]);
										pss.executeUpdate();

										String nowTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
										//如果是关联人和知晓人消息通知对方
										//XX的工作计划XX添加你为XX
										if("1".equals(keys[3])){
//											sql= " insert into tbladvice(id,send,title,receive,content,receiveName,sendName,relatMsgId,createBy,lastUpdateBy,createTime,lastUpdateTime,status,exist,type,relationId) "+
//											"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//										pss = conn.prepareStatement(sql);
//										String id = IDGenerater.getId();
//										pss.setString(1, id);
//										pss.setString(2, "1");
//										pss.setString(3, adviceTitle+keys[4]);
//										pss.setString(4, keys[1]);
										String tres= "oa.mydesk.workPlan";
										if(bean.getPlanType().equals("event")){
											tres = "crm.event.plan";
										}
//										pss.setString(5, "<a href=\"javascript:mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&flagNews=flagNews&planType="+bean.getPlanType()+"&keyId="+bean.getId()+"','RES<"+tres+">')\">"+adviceTitle+keys[4]+"</a>");
//										pss.setString(6, keys[2]);
//										pss.setString(7,"admin");
//						    	        pss.setString(8, "");
//						    	        pss.setString(9, "1");
//						    	        pss.setString(10, "1");
//						    	        pss.setString(11, nowTime);
//						    	        pss.setString(12, nowTime);
//						    	        pss.setString(13, "noRead");
//						    	        pss.setString(14, "all");
//						    	        pss.setString(15, "plan");
//						    	        pss.setString(16, bean.getId());
//						    	        pss.executeUpdate();
//						    	        // 给客户端用户发送通知消息
//										MSGConnectCenter.sendAdvice(keys[1], id, adviceTitle+keys[4]);
//										new AdviceMgt().add(userId, title, content, receiveIds, relationId, type);
										String[] item = {"1", adviceTitle+keys[4], 
												"<a href=\"javascript:mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&flagNews=flagNews&planType="+bean.getPlanType()+"&keyId="+bean.getId()+"','RES<"+tres+">')\">"+adviceTitle+keys[4]+"</a>", 
												keys[1], bean.getId(), "plan"};
										listAdvice.add(item);
									}else if("2".equals(keys[3])){
											sql = "update CRMClientInfo set LastContractTime=? where id=?";
											pss = conn.prepareStatement(sql);
											pss.setString(1, BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
											pss.setString(2, keys[1]);
											pss.executeUpdate();
										}
										
										//如果是关联客户，更新最新动态
										if("5".equals(keys[0])){
											String context = nowTime + " " + loginBean.getName()+"更新了工作计划";
											pubMgt.insertCRMCLientInfoLog(keys[1],"history", context, loginBean.getId(),conn);
										}
									}
								}
							}
							
							String ids = "";
							//删除相关联的消息						
							for(String ds:list){
//								sql= " select id from  tbladvice where receive='"+ds+"' and relationId='"+bean.getId()+"'";	
//								pss = conn.prepareStatement(sql) ;
//	                        	ResultSet rss = pss.executeQuery() ;							
//								String id = "";
//								if(rss.next()){
//									id=rss.getString("id");
//								}
//								//取消通知信息
//								MSGConnectCenter.CancelMsg(CancelMsgReq.TYPE_SYS, ds,id);
//								sql= " delete tbladvice where receive=? and relationId=? ";
//								pss = conn.prepareStatement(sql);
//								pss.setString(1, ds);
//								pss.setString(2, bean.getId());
//								pss.executeUpdate();
								ids += ds + ",";
							}
							if (!"".equals(ids)) {
//								new AdviceMgt().deleteByRelationId(relationIds, userIds);
								String item[] = {bean.getId(), ids};
								listAdvice.add(item);
							}
							
							
							if(goalItems !=null){
								sql = " delete tblPlanGoalItem where f_ref=? ";
								pss = conn.prepareStatement(sql);
								pss.setString(1, bean.getId());
								pss.executeUpdate();
								sql = " insert into tblPlanGoalItem(id,f_ref,goalType,score,employeeId,departmentCode,billDate) values(?,?,?,?,?,?,?) ";
								pss = conn.prepareStatement(sql);
								for(String ass:goalItems){
									if(ass != null && ass.length() > 0 ){
										pss.setString(1, IDGenerater.getId());
										pss.setString(2, bean.getId());
										String[] keys = ass.split(":");
										pss.setString(3, keys[0]);
										pss.setString(4, keys[1]); 
										pss.setString(5, bean.getEmployeeID());
										pss.setString(6, bean.getDepartmentCode());
										pss.setString(7, bean.getBeginDate().substring(0,10));
										//pss.executeUpdate();
										pss.addBatch();
									}
								}
								pss.executeBatch();
							}							

							result.setRetVal(listAdvice);
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt addDayWorkPlan : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    public Result commit(final String keyId,final String commitType,final String commit,final String userId,final String createTime,
    		final String adviceTitle,final String adUserId,final String adUserName,final String planType,final String commitId,final List<String> userIds
    		,final String planTitle){
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) { 
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String loginId = userId;
							StringBuilder allUserId = new StringBuilder();
							if(!userId.equals(adUserId)){
								allUserId.append(adUserId+",");
							}							
							/*查询协同人*/
							String sql = "select keyId from tblPlanAssItem where f_ref = ? and AssociateId='1'";
							if(commitId==null || commitId.length()==0){
								sql = "select keyId from tblPlanAssItem where f_ref = ? and (AssociateId='1' or AssociateId='2')";
							}
							
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, keyId);
							ResultSet rss = pss.executeQuery();
							while(rss.next()){
								String userId = rss.getString("keyId") ;
								if(allUserId.indexOf(userId+",")==-1 && !userId.equals(adUserId) && !userId.equals(loginId)){
									allUserId.append(userId + ",") ;
								}
							}
							sql = " insert into tblPlanRemark(id,f_ref,remarkType,EmployeeID,content,createTime,commitId) values(?,?,?,?,?,?,?)";
							pss = conn.prepareStatement(sql);
							String planId=IDGenerater.getId();
							pss.setString(1, planId);
							pss.setString(2, keyId);
							pss.setString(3, commitType);
							pss.setString(4, userId);
							pss.setString(5, commit);
							pss.setString(6, createTime);
							pss.setString(7, commitId) ;
							pss.executeUpdate();
							String tres= "oa.mydesk.workPlan";
							String adviceTitle2=adUserName+"回复了工作计划为["+planTitle+"]的点评";
							if(planType.equals("event")){
								tres = "crm.event.plan";
								adviceTitle2=adUserName+"回复了工作计划为["+planTitle+"]的点评";
							}
							if(commitId!=null && commitId.length()>0){
								if(userIds != null){
									if(!userId.equals(userIds.get(0))){
										new Thread(new NotifyFashion(userId, adviceTitle2, "<a href=\"javascript:mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&planType="+planType+"&keyId="+keyId+"','RES<"+tres+">')\">"+adviceTitle2+"</a>",
											allUserId.toString(), 4, "yes",keyId,"","","plan")).start();
									}								
									if(!userId.equals(adUserId)){
										new Thread(new NotifyFashion(userId, adviceTitle, "<a href=\"javascript:mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&planType="+planType+"&keyId="+keyId+"','RES<"+tres+">')\">"+adviceTitle+"</a>",
											allUserId.toString(), 4, "yes",keyId,"","","plan")).start();
									}
								}
							}else{
								new Thread(new NotifyFashion(userId, adviceTitle, "<a href=\"javascript:mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&planType="+planType+"&keyId="+keyId+"','RES<"+tres+">')\">"+adviceTitle+"</a>",
									 allUserId.toString(), 4, "yes",keyId,"","","plan")).start();								
							}					
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt addDayWorkPlan : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    /**
     * 查询一个工作计划 评论回复的所有人。
     * @param planType
     * @param employeeId
     * @param startDate
     * @param endDate
     * @param associateId
     * @return
     */
    public Result  selComPerson(final String commitId){
    	final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try { 
            
                        	String sql = "SELECT employeeID FROM tblPlanRemark where id=?" ;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	pss.setString(1, commitId);
                        	ResultSet rss=pss.executeQuery();
                        	List<String> list=new ArrayList<String>();
                        	while(rss.next()){
                        		list.add(rss.getString(1));
                        	}
                        	rs.setRetVal(list);
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("OAWorkPlanMgt queryAssPeopleName method "+ex) ;
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    public Result getGoalTypeItem(final String locale,final String planId){ 
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {  
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select a.id,c.score,b."+locale+" from tblGoalType a join tbllanguage b on a.name = b.id left join tblPlanGoalItem c on a.id=c.goalType and c.f_ref=?  where datatype='1' and a.statusId='0' ";
														
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, planId);
							ResultSet rss = pss.executeQuery() ;
							ArrayList list = new ArrayList();
							while(rss.next()){ 
								list.add(new String[]{rss.getString(1),rss.getString(2),rss.getString(3)});
							}
							result.setRetVal(list) ;
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt queryTopics : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }   
    
    public String getTemplate(final String planType,final String userId,final String deptCode,final String planSum){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {  
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select content from tblPlanTemplate join tblPlanTemplateDet on tblPlanTemplate.id = tblPlanTemplateDet.f_ref  where  "+
" exists (select employeeID from tblPlanTemplateDet where tblPlanTemplateDet.f_ref=tblPlanTemplate.id and employeeID='"+userId+"' "+
"and charindex('"+planType+",',toplanType,0)>0 and charindex('"+planSum+",',toPlanSummary,0)>0) "+
" or exists (select departmentCode from tblPlanTemplateDet where tblPlanTemplateDet.f_ref=tblPlanTemplate.id and len(employeeID)=0 and '"+deptCode+
"' like departmentCode+'%'  and charindex('"+planType+",',toplanType,0)>0  and charindex('"+planSum+",',toPlanSummary,0)>0) order by len(employeeID) desc,len(departmentCode) desc ";
														
							PreparedStatement pss = conn.prepareStatement(sql);
							ResultSet rss = pss.executeQuery() ;
							String content = "";
							if(rss.next()){ 
								content = rss.getString(1);
							}
							result.setRetVal(content) ;
						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt queryTopics : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return (String)result.retVal;
    }
    
    public Result getPlanAssociate(){
    	//创建参数
        List param = new ArrayList();
        String hql = "select bean from OAPlanAssociateBean as bean where statusId=? order by listOrder";
        //如标题不为空，则做标题模糊查询
        param.add("0");
        //调用list返回结果
        return list(hql, param);
    }
    
    public Result getPlanAssociate(Collection<ArrayList<String[]>> itemList){
    	String itemIds = "" ;
    	for(ArrayList<String[]> item : itemList){
    		for(String[] array : item)
    		itemIds += "'"+array[1]+"'," ; 
    	}
    	if(itemIds.length()>0){
    		itemIds = itemIds.substring(0, itemIds.length()-1) ;
    	}
    	//创建参数
        List param = new ArrayList();
        String hql = "select bean from OAPlanAssociateBean as bean ";
        if(itemIds.length()>0){
        	hql += "where bean.id in ("+itemIds+") " ;
        }
        hql += " order by listOrder";
        //调用list返回结果
        return list(hql, param);
    }
    
    public Result getParam(){
    	//创建参数
        List param = new ArrayList();
        String hql = "select name,value from tblplanparam ";
        //如标题不为空，则做标题模糊查询
        //调用list返回结果
        return sqlList(hql, param);
    }
    
    public Result updateParam(final HashMap map){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {  
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = " update tblplanparam set value=? where name=? ";														
							PreparedStatement pss = conn.prepareStatement(sql);
							Iterator it =map.entrySet().iterator();
							while(it.hasNext()){
								 Entry o = (Entry)it.next();
								 pss.setString(1, (String)o.getValue());
								 pss.setString(2, (String)o.getKey());
								 pss.addBatch();
							}
							pss.executeBatch();

						} catch (SQLException ex) {
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							ex.printStackTrace();
							BaseEnv.log.error("OADateWorkPlanMgt queryTopics : ", ex) ;
							return;
						}
					}
				});
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
    	return result;
    }
    
    private HashMap<Integer,Integer> getHoliday(String month){
    	ArrayList param = new ArrayList();
    	String sql = "select Holiday from  tblHolidaySetting where Holiday like ?+'%' order by Holiday";
    	param.add(month);
    	Result rs = sqlList(sql,param);    
    	ArrayList holidays = (ArrayList)rs.retVal;
    	HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
    	for(int j=0;holidays != null &&j<holidays.size();j++){
			String hd = (String)(((Object[])holidays.get(j))[0]);
			int d = Integer.parseInt(hd.substring(hd.lastIndexOf("-")+1));
			map.put(d, d);
		}
    	return map;
    }
    /**
     * 跟据年和月，取该月的日期，包括节假日
     * @param year
     * @param month
     * @return
     */
    public ArrayList getMonthHead(String year,String month){
    	ArrayList dayList = new ArrayList();
    	try{
			Calendar calendar = Calendar.getInstance();   
			String m = month;
			if(m.length() == 1){
				m = "0"+m;
			}
			calendar.setTime(BaseDateFormat.parse(year+"-"+m+"-01",BaseDateFormat.yyyyMMdd)); 
		
			calendar.set(Calendar.DATE, 1);// 设为当前月的1 号  
			calendar.add(Calendar.MONTH, 1);// 加一个月，变为下月的1 号   
			calendar.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天  
			int lastD =calendar.get(Calendar.DATE);
			//取节假日
			HashMap<Integer,Integer> holidayMap = getHoliday(year+"-"+m);
			
			calendar.set(Calendar.DATE, 1);// 设为当前月的1 号  
			
			SimpleDateFormat sdf = new SimpleDateFormat("d");
			int curDay = Integer.parseInt(sdf.format(new Date()));
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			String curYD = sdf.format(new Date());
			
			for(int i=1;i<=lastD;i++){
				boolean isHolid = false;
				if(holidayMap.get(i) != null){
					isHolid = true;
				}				
				if(curYD.compareTo(year+"-"+m+"-"+(i<10?"0"+i:i+""))<0){
					isHolid = true;
				}
				//当前日期之后的日期都置为假期，不参与运算
				
				dayList.add(new CheckDay(""+i,(calendar.get(Calendar.DAY_OF_WEEK)-1)+"",isHolid?1:0,year+"-"+m+"-"+(i<10?"0"+i:""+i)));
				calendar.add(Calendar.DATE, 1);
			}
			
		}catch(Exception e){e.printStackTrace();}
		return dayList;
    }
    
    
    public Result queryEmployeeByDept(final String strSQL) {
        final Result rs = new Result();
        int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection conn) throws  SQLException {
                        try {
                        	LinkedHashMap<String, List<String[]>> deptMap = new LinkedHashMap<String, List<String[]>>() ;
                            Statement st = conn.createStatement();
                            String sql ="select a.deptCode,a.classCode,a.DeptFullName,b.EmpFullName,b.id from tblDepartment a left join tblEmployee b " +
                            		" on a.classCode=b.DepartmentCode where b.openFlag=1 and b.statusId=0 and a.statusId=0 " +
                            		 strSQL +
                            		" order by a.deptCode";
                         
                            ResultSet rss = st.executeQuery(sql);
                            while (rss.next()) {
                                String[] value = new String[6];
                                value[0] = rss.getString("classCode"); 
                                value[1] = rss.getString("DeptFullName"); 
                                value[2] = rss.getString("EmpFullName"); 
                                value[3] = rss.getString("id");                         
                                if(!deptMap.containsKey(value[1])){
                                	List<String[]> empList = deptMap.get(value[1]) ;
                                	if(empList==null){
                                		empList = new ArrayList<String[]>() ;
                                	}
                                	empList.add(value) ;
                                	deptMap.put(value[1], empList) ;
                                }else{
                                	
                                	List<String[]> empList = deptMap.get(value[1]) ;
                                	empList.add(value) ;
                                	deptMap.put(value[1], empList) ;
                                }
                                
                            }
                            rs.setRetVal(deptMap);
                            rs.setRealTotal(deptMap.size());
                            rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            BaseEnv.log.error("OAWorkPlanMgt queryIsExistPlan method "+ex) ;
                            return;
                        }
                    }
                });
                return rs.getRetCode();
            }
        });
        rs.retCode = retCode;
        return rs;
    }
    
   
    
    public Result planDayCheck(final OADayWorkPlanCheckSearchForm form,ArrayList<CheckDay> monthHead,
    		String scopeSQL,String loginId,String bySeeId){
    	DecimalFormat df=(DecimalFormat)NumberFormat.getInstance();
		df.setMaximumFractionDigits(1);
    	
    	//取检视参数
    	Result rs = getParam();
    	if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS|| rs.retVal == null){
    		return rs;
    	}
    	int dayplanhour = 0;
    	int dayplanmin = 0;
    	int daysumall = 0;
    	int daysumday = 0;
    	int daysumhour = 0;
    	int daysummin = 0;
    	int daysumnext = 0;
    	for(Object[] os:(ArrayList<Object[]>)rs.retVal){
    		if(os[0].equals("dayplanhour")){
    			dayplanhour = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("dayplanmin")){
    			dayplanmin = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("daysumall")){
    			daysumall = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("daysumday")){
    			daysumday = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("daysumhour")){
    			daysumhour = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("daysummin")){
    			daysummin = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("daysumnext")){
    			daysumnext = Integer.parseInt(os[1].toString());
    		}
    	}
    	
    	String dayplanhourStr = dayplanhour<10?"0"+dayplanhour:dayplanhour+"";
    	String dayplanminStr = dayplanmin<10?"0"+dayplanmin:dayplanmin+"";
    	String daysumhourStr = daysumhour<10?"0"+daysumhour:daysumhour+""; 
    	String daysumminStr = daysummin<10?"0"+daysummin:daysummin+"";
    	
    	if("day".equals(form.getPlanType())){
    		//日计划的检视
    		//1.取人员范围
    		//2.取时间范围段内，所有日或周工作计划
    		//3.取所有非工作日。
    		//4.循环比较每个人是否有延迟或的日工作计划
    		
    		//取所有职员
    		
    		//查询所有人员范围和日期内的日工作计划，再一个个比较处理
    		String m = form.getMonth()+"";
			if(m.length() == 1){
				m = "0"+m;
			}
    		String d1= form.getMyear()+"-"+m+"-01 00:00:00";
    		Calendar calendar = Calendar.getInstance();   	
    		try{
			calendar.setTime(BaseDateFormat.parse(d1,BaseDateFormat.yyyyMMddHHmmss)); 	
    		}catch(Exception e){}
			calendar.set(Calendar.DATE, 1);// 设为当前月的1 号  
			calendar.add(Calendar.MONTH, 1);// 加一个月，变为下月的1 号   
			calendar.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天  
			String d2 = BaseDateFormat.format(calendar.getTime(), BaseDateFormat.yyyyMMdd)+" 23:59:59";
			
    		ArrayList param = new ArrayList();
    		String sql = "  select b.id,b.empfullname,a.beginDate,a.endDate,a.createTime,a.summaryTime,a.statusId,a.time from tblemployee b join tbldepartment c on b.departmentCode=c.classCode "+
    				" left join tbldayworkplan a  on a.employeeId = b.id and a.planType='day' and a.beginDate>=?  and a.endDate<=? "+
    				"   where b.openFlag = '1' and b.statusId='0' ";    		
			param.add(d1);
			param.add(d2);
			/*admin 可以查询所有人的*/
			if(!"1".equals(loginId)){
				String[] bySeePerson = bySeeId.split(",");
				sql += " and (b.id in (" ;
				for (int i = 0; i < bySeePerson.length; i++) {
					sql += "'"+bySeePerson[i]+"',";
				}
				sql += "'')" ;
				if(scopeSQL!=null && scopeSQL.length()>0){
					sql += scopeSQL ;
				}
				sql += ")" ;				
			}
			if(form.getEmployee() != null && form.getEmployee().length() > 0){
				sql +=" and b.empFullName = ? ";
				param.add(form.getEmployee());
			}
			if(form.getDepartment() != null && form.getDepartment().length() > 0){
				sql +=" and c.classCode like ?+'%' ";
				param.add(form.getDepartment());
			}
			sql +="  order by b.id,a.beginDate,a.createTime ";
    		rs = sqlList(sql,param);
    		
    		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //查询成功
    			ArrayList<Object[]> list=(ArrayList<Object[]>)rs.retVal; 
    			
    			ArrayList<Object[]> rslist = new ArrayList<Object[]>();
    			String curId= "";
    			String curName="";
    			HashMap<String,PlanStatInfo> curMap = new HashMap();
    			
    			for(int i=0;list != null && i<list.size();i++){
    				Object[] os = list.get(i);
    				if(!os[0].equals(curId)){
    					//不同用户信息,切换用户
    					if(curId.length() > 0){
    						//处理用户信息 
    						rslist.add(new Object[]{curId,curName,curMap,0,0,0,0,0,0,0,0});
    					}
    					curId = os[0].toString();
    					curName = os[1].toString();
    					curMap = new HashMap();
    					
    				}
    				//处理计划信息
    				//取beginDate的日期信息
    				if(os[2] == null){
    					continue;
    				}
    				int day = Integer.parseInt(os[2].toString().substring(8,10));
    				PlanStatInfo statInfo = curMap.get(day+"");
    				if(statInfo==null){
    					statInfo = new PlanStatInfo();
    					statInfo.setDaysumall(daysumall);
    					curMap.put(day+"", statInfo);
    				}
    				//比较创建时间是否在延时范围内
    				if(statInfo.planStatus != 1){
    					//如果已经是正常计划则不做比较
    					String lastTime = os[2].toString().substring(0,10)+" "+dayplanhourStr+":"+dayplanminStr+":00";
    					if(os[4].toString().compareTo(lastTime)<=0){
    						//有正常的工作计划
    						statInfo.planStatus=1;
    					}
    				}
    				
    				statInfo.planNum ++;
    				if(os[7] != null && os[7].toString().length() > 0){
    					//计划总时间
    					statInfo.compTime +=Double.parseDouble((os[7].toString()));
    				}
    				
    				if(os[6]!=null && os[6].toString().equals("1")){
    					//计划完成数
    					statInfo.completeNum ++;
    				}
    				
    				if(os[5] != null && os[5].toString().length() > 0){
    					//有总结
    					statInfo.sumNum ++;
    					String lastTime = os[2].toString().substring(0,10)+" "+daysumhourStr+":"+daysumminStr+":00";
    					if(daysumnext==1){
    						//后几日总结
    						calendar = Calendar.getInstance();   
    						try{
    							calendar.setTime(BaseDateFormat.parse(lastTime,BaseDateFormat.yyyyMMddHHmmss)); 
    						}catch(Exception e){}    		
    						calendar.add(Calendar.DATE, daysumday);// 减去一天，变为当月最后一天  
    						lastTime = BaseDateFormat.format(calendar.getTime(), BaseDateFormat.yyyyMMddHHmmss);
    					}
    					if(os[5].toString().compareTo(lastTime)>0){
    						//有延时的工作计划
    						statInfo.sumStatus=2;
    					}
    				}
    			}	
    			if(curId.length() > 0){
					//处理用户信息
					rslist.add(new Object[]{curId,curName,curMap,0,0,0,0,0,0,0,0});
				}
    			
    			//进一步进行处理
    			for(Object[] os :rslist){
    				int noPlan=0;
    				int noTimePlan = 0;
    				int noSum = 0;
    				int noTimeSum = 0;
    				int planNum = 0;
    				int completeNum=0;
    				int compTime=0;
    				int planDay = 0;
    				HashMap<String,PlanStatInfo> map = (HashMap)os[2];
    				for(CheckDay cd :monthHead){
    					if(cd.getIsHoliday()==1){
    						//如果是假期不处理
    						continue;
    					}
    					PlanStatInfo psi =map.get(cd.getDay());					
    					if(psi==null){
    						//如果没有工作计划且不是假期，则是无工作计划，无总结
    						noPlan++;
    					}else{
    						if(psi.getPlanStatus() == 2){
    							//工作计划延时完成
    							noTimePlan++;
    						}
    						//--------------
    						if(psi.getDaysumall() == 1){
    							if(psi.getPlanNum() != psi.getSumNum() ){
    								//启用全部工作计划都要总结时，计划数和总结数不一致，则为未写工作总结
    								psi.setSumStatus(0);
    							}
    						}else{
    							if(psi.getSumNum() == 0){
    								//一个总都未写则，为未写工作总结
    								psi.setSumStatus(0);
    							}
    						}
    						if(psi.getSumStatus() == 2){
    							noTimeSum++;
    						}else if(psi.getSumStatus() == 0){
    							noSum++;
    						}
    						//计划总数，完成总数，时间
    						planNum += psi.planNum;
    						completeNum += psi.completeNum;
    						compTime += psi.compTime;
    						//psi.compTimeHour= df.format((double)psi.compTime/60);
    						psi.compTimeHour = df.format((double)psi.compTime);
    						planDay++;
    					}
    				}
    				os[3]=noPlan;
    				os[4]=noTimePlan;
    				os[5]=noSum;
    				os[6]=noTimeSum;
    				os[7]=planNum;
    				os[8]=completeNum;
    				os[9]=planDay==0?"":df.format(((double)compTime/planDay));
    				//os[9]=planDay==0?"":df.format(((double)compTime/planDay)/60);
    			}
    			
    			
    			rs.retVal = rslist;
    			return rs;
            }
    	}
    	return rs;
    }
    
    public Result planWeekCheck(final OADayWorkPlanCheckSearchForm form,ArrayList<CheckDay> monthHead,
    		ArrayList<WeekDay> weekHead,final String scopeSQL,final String loginId,String bySeeId){
    	//取检视参数
    	Result rs = getParam();
    	if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS|| rs.retVal == null){
    		return rs;
    	}
    	int weekplanday = 0;
    	int weekplanhour = 0;
    	int weekplanmin = 0;
    	int weeksumday = 0;
    	int weeksumhour = 0;
    	int weeksummin = 0;
    	int weeksumnext = 0;
    	for(Object[] os:(ArrayList<Object[]>)rs.retVal){
    		if(os[0].equals("weekplanday")){
    			weekplanday = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("weekplanhour")){
    			weekplanhour = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("weekplanmin")){
    			weekplanmin = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("weeksumday")){
    			weeksumday = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("weeksumhour")){
    			weeksumhour = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("weeksummin")){
    			weeksummin = Integer.parseInt(os[1].toString());
    		}else if(os[0].equals("weeksumnext")){
    			weeksumnext = Integer.parseInt(os[1].toString());
    		}
    	}
    	String weekplanhourStr = weekplanhour<10?"0"+weekplanhour:weekplanhour+"";
    	String weekplanminStr = weekplanmin<10?"0"+weekplanmin:weekplanmin+"";
    	String weeksumhourStr = weeksumhour<10?"0"+weeksumhour:weeksumhour+"";
    	String weeksumminStr = weeksummin<10?"0"+weeksummin:weeksummin+"";
    	
    	//合并周日期
		int wNum = 0;
		WeekDay curWeekd= null;
		for(CheckDay cd:monthHead){
			if(cd.getWeek().equals("0")||curWeekd==null){						
				wNum++;
				curWeekd=new WeekDay(wNum+"");
				weekHead.add(curWeekd);
				curWeekd.weekDay1 = cd.day;
				//算出该周的第一天和最后一天
				Calendar cal = Calendar.getInstance();
				try{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
					cal.setTime(sdf.parse(form.getMyear()+"-"+form.getMonth()+"-"+cd.day));
				}catch(Exception e){e.printStackTrace();}
				
				int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
				cal.add(Calendar.DATE, -day_of_week);
				curWeekd.beginDate = BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd)+" 00:00:00";				
				cal.add(Calendar.DATE, 6);
				curWeekd.endDate = BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd)+" 23:59:59";	
				//计算本周计划的最后提交时间
				cal.add(Calendar.DATE, -6);
				cal.add(Calendar.DATE, weekplanday - 1);
				
				curWeekd.lastPlanDate = curWeekd.beginDate;
				curWeekd.lastSumDate = curWeekd.endDate;
				
				//如果开始日期，小于这个月的开始日期，则置这个周为假期
				if((form.getMyear()+"-"+(form.getMonth()<10?"0":"")+form.getMonth()).compareTo(curWeekd.beginDate.substring(0,7)) > 0){
					curWeekd.isHoliday = 1;
				}
				//如果周的结束日期，大于当前日期，则为还未发生的日期，置假期
				if(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd).compareTo(curWeekd.endDate.substring(0,10))< 0){
					curWeekd.isHoliday = 1;
				}
			}
			curWeekd.setDayNum(curWeekd.getDayNum()+1);
		}
		
		//取节假日
		HashMap<Integer,Integer> holidayMap = getHoliday(form.getMyear()+"-"+(form.getMonth()<10?"0"+form.getMonth():""+form.getMonth()));
		
		//计算这几个周报的最后检查时间点
		for(WeekDay wd:weekHead){
			if(wd.isHoliday == 1){
				continue;
			}
			String beginDate = wd.getBeginDate();
			Calendar cal = Calendar.getInstance();
			try{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				cal.setTime(sdf.parse(wd.getBeginDate().substring(0,10)));
				cal.set(Calendar.DAY_OF_WEEK, weekplanday+1);
				
				wd.lastPlanDate = sdf.format(cal.getTime())+" "+weekplanhourStr+":"+weekplanminStr+":00";
				
				if(weeksumnext==1){
					//总结在下周
					cal.setTime(sdf.parse(wd.getBeginDate().substring(0,10)));
					cal.add(Calendar.WEEK_OF_MONTH, 1);
					cal.set(Calendar.DAY_OF_WEEK, weeksumday+1);					
					wd.lastSumDate = sdf.format(cal.getTime())+" "+weeksumhourStr+":"+weeksumminStr+":00";
				}else{
					//总结在本周
					cal.setTime(sdf.parse(wd.getBeginDate().substring(0,10)));
					cal.set(Calendar.DAY_OF_WEEK, weeksumday+1);					
					wd.lastSumDate = sdf.format(cal.getTime())+" "+weeksumhourStr+":"+weeksumminStr+":00";
				}
				//考虑节假日顺延的问题
				cal.setTime(sdf.parse(wd.lastPlanDate.substring(0,10)));
				int day = cal.get(cal.DAY_OF_MONTH);
				int i=0;
				while(holidayMap.get(day) != null){
					cal.add(cal.DATE, 1);
					day = cal.get(cal.DAY_OF_MONTH);
					i++;
					if(i>6){
						wd.isHoliday = 1;
						break;
					}
				}
				wd.lastPlanDate = sdf.format(cal.getTime())+" "+weekplanhourStr+":"+weekplanminStr+":00";
				
				
				if(wd.isHoliday == 1){
					continue;
				}
				cal.setTime(sdf.parse(wd.lastSumDate.substring(0,10)));
				day = cal.get(cal.DAY_OF_MONTH);
				i=0;
				while(holidayMap.get(day) != null){
					cal.add(cal.DATE, 1);
					day = cal.get(cal.DAY_OF_MONTH);
					i++;
					if(i>6){
						break;
					}
					if(day == 1){
						//这意味着，跨到下一个区间了
						holidayMap = getHoliday(form.getMyear()+"-"+((form.getMonth()+1)<10?"0"+(form.getMonth()+1):""+(form.getMonth()+1)));
					}
				}
				wd.lastSumDate = sdf.format(cal.getTime())+" "+weeksumhourStr+":"+weeksumminStr+":00";
				
				System.out.print("");
			}catch(Exception e){e.printStackTrace();}
		}
    	
    	
    	if("week".equals(form.getPlanType())){
    		String m = form.getMonth()+"";
			if(m.length() == 1){
				m = "0"+m;
			}
    		String d1= form.getMyear()+"-"+m+"-01 00:00:00";
    		Calendar calendar = Calendar.getInstance();   	
    		try{
			calendar.setTime(BaseDateFormat.parse(d1,BaseDateFormat.yyyyMMddHHmmss)); 	
    		}catch(Exception e){}
			calendar.set(Calendar.DATE, 1);// 设为当前月的1 号  
			calendar.add(Calendar.MONTH, 1);// 加一个月，变为下月的1 号   
			calendar.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天  
			String d2 = BaseDateFormat.format(calendar.getTime(), BaseDateFormat.yyyyMMdd)+" 23:59:59";
			
    		ArrayList param = new ArrayList();
    		String sql = "  select b.id,b.empfullname,a.beginDate,a.endDate,a.createTime,a.summaryTime from tblemployee b join tbldepartment c on b.departmentCode=c.classCode "+
    		" left join tbldayworkplan a  on a.employeeId = b.id and a.planType='week' and a.beginDate>=?  and a.endDate<=? "+
    		"   where b.openFlag = '1' and b.statusId='0' ";    		
			param.add(d1);
			param.add(d2);
			/*admin 可以查询所有人的*/
			if(!"1".equals(loginId)){
				String[] bySeePerson = bySeeId.split(",");
				sql += " and (b.id in (" ;
				for (int i = 0; i < bySeePerson.length; i++) {
					sql += "'"+bySeePerson[i]+"',";
				}
				sql += "'')" ;
				if(scopeSQL!=null && scopeSQL.length()>0){
					sql += scopeSQL ;
				}
				sql += ")" ;		
			}
			if(form.getEmployee() != null && form.getEmployee().length() > 0){
				sql +=" and b.empFullName = ? ";
				param.add(form.getEmployee());
			}
			if(form.getDepartment() != null && form.getDepartment().length() > 0){
				sql +=" and c.classCode like ?+'%' ";
				param.add(form.getDepartment());
			}
			sql +="  order by b.id,a.beginDate,a.createTime ";
    		rs = sqlList(sql,param);
    		
    		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                //查询成功
    			ArrayList<Object[]> list=(ArrayList<Object[]>)rs.retVal; 
    			
    			ArrayList<Object[]> rslist = new ArrayList<Object[]>();
    			String curId= "";
    			String curName="";
    			HashMap<String,PlanStatInfo> curMap = new HashMap();
    			
    			for(int i=0;list != null && i<list.size();i++){
    				Object[] os = list.get(i);
    				if(!os[0].equals(curId)){
    					//不同用户信息,切换用户
    					if(curId.length() > 0){
    						//处理用户信息
    						rslist.add(new Object[]{curId,curName,curMap,0,0,0,0});
    					}
    					curId = os[0].toString();
    					curName = os[1].toString();
    					curMap = new HashMap();
    					
    				}
    				//处理计划信息
    				//取beginDate的日期信息
    				if(os[2] == null){
    					continue;
    				}
    				
    				PlanStatInfo statInfo = curMap.get(os[2].toString());
    				if(statInfo==null){
    					statInfo = new PlanStatInfo();
    					
    					curMap.put(os[2].toString(), statInfo);
    				}
    				//比较创建时间是否在延时范围内
    				for(WeekDay wd:weekHead){
    					if(wd.beginDate.equals(os[2])){
    						if(wd.lastPlanDate.compareTo(os[4].toString())<0){
    							statInfo.planStatus = 2;
    						}else{
    							statInfo.planStatus = 1;
    						}
    						  
    						if(os[5] == null){
    							statInfo.sumStatus = 0;
    						}else if(wd.lastSumDate.compareTo(os[5].toString())<0){
    							statInfo.sumStatus = 2;
    						}else{
    							statInfo.sumStatus = 1;
    						}
    					}
    				}    				
    			}	
    			if(curId.length() > 0){
					//处理用户信息
					rslist.add(new Object[]{curId,curName,curMap,0,0,0,0});
				}
    			
    			//进一步进行处理
    			for(Object[] os :rslist){
    				int noPlan=0;
    				int noTimePlan = 0;
    				int noSum = 0;
    				int noTimeSum = 0;
    				HashMap<String,PlanStatInfo> map = (HashMap)os[2];
    				for(WeekDay wd:weekHead){
    					if(wd.getIsHoliday()==1){
    						//如果是假期不处理
    						PlanStatInfo psi = new PlanStatInfo();
    						psi.planStatus = 1;
    						psi.sumStatus = 1;
    						
    						map.put(wd.beginDate, psi);
    					}
    					PlanStatInfo psi =map.get(wd.beginDate);					
    					if(psi==null){
    						//如果没有工作计划且不是假期，则是无工作计划，无总结
    						noPlan++;
    						
    						psi = new PlanStatInfo();
    						psi.planStatus = 0;
    						psi.sumStatus = 0;
    						map.put(wd.beginDate, psi);
    					}else{
    						if(psi.getPlanStatus() == 2){
    							//工作计划延时完成
    							noTimePlan++;
    						}else if(psi.getPlanStatus() == 0){
    							noPlan++;
    						}
    						if(psi.getSumStatus() == 2){
    							noTimeSum++;
    						}else if(psi.getSumStatus() == 0){
    							noSum++;
    						}
    					}
    					psi.setDayStr(wd.beginDate.substring(0,10));
    				}
    				os[3]=noPlan;
    				os[4]=noTimePlan;
    				os[5]=noSum;
    				os[6]=noTimeSum;
    			}
    			
    			
    			rs.retVal = rslist;
    			return rs;
            }
    	}
    	return rs;
    }
    
    public class PlanStatInfo{
    	private int day;
    	//0,无，1正常完成，2延时完成
    	private int planStatus = 2;
    	//0,无，1正常完成，2延时完成，
    	//初始为1，当只要有延时的情况，则置为2，当再一次过滤时结果判断planNum和sumNum的情况
    	private int sumStatus = 1;
    	
    	//计划和总结的数据，用于比较当全部总结必须总结和一个总结都没有的情况
    	private int planNum=0;
    	private int sumNum=0;
    	private int completeNum=0;//总结的完成数
    	private double compTime=0;//任务的完成时间总数
    	private String compTimeHour="0";//任务的完成时间总数小时
    	
    	private int daysumall=0; //是否全部要总结
    	
    	private String dayStr = "";
    	
    	
    	
    	   	
		public String getDayStr() {
			return dayStr;
		}
		public void setDayStr(String dayStr) {
			this.dayStr = dayStr;
		}
		public String getCompTimeHour() {
			return compTimeHour;
		}
		public void setCompTimeHour(String compTimeHour) {
			this.compTimeHour = compTimeHour;
		}
		public int getCompleteNum() {
			return completeNum;
		}
		public void setCompleteNum(int completeNum) {
			this.completeNum = completeNum;
		}
		public double getCompTime() {
			return compTime;
		}
		public void setCompTime(double compTime) {
			this.compTime = compTime;
		}
		public int getDaysumall() {
			return daysumall;
		}
		public void setDaysumall(int daysumall) {
			this.daysumall = daysumall;
		}
		public int getPlanNum() {
			return planNum;
		}
		public void setPlanNum(int planNum) {
			this.planNum = planNum;
		}
		public int getSumNum() {
			return sumNum;
		}
		public void setSumNum(int sumNum) {
			this.sumNum = sumNum;
		}
		public int getDay() {
			return day;
		}
		public void setDay(int day) {
			this.day = day;
		}
		public int getPlanStatus() {
			return planStatus;
		}
		public void setPlanStatus(int planStatus) {
			this.planStatus = planStatus;
		}
		public int getSumStatus() {
			return sumStatus;
		}
		public void setSumStatus(int sumStatus) {
			this.sumStatus = sumStatus;
		}
    	
    	
    }
    
    public class CheckDay{
    	private String day;
    	private String week;
    	private int isHoliday;
    	
    	private String strDay;
    	
    	
    	
    	public String getStrDay() {
			return strDay;
		}
		public void setStrDay(String strDay) {
			this.strDay = strDay;
		}
		public CheckDay(String day,String week,int isHoliday,String strDay){
    		this.day = day;
    		this.week = week;
    		this.isHoliday = isHoliday;
    		this.strDay=strDay;
    	}
		public String getDay() {
			return day;
		}
		public void setDay(String day) {
			this.day = day;
		}

		public int getIsHoliday() {
			return isHoliday;
		}
		public void setIsHoliday(int isHoliday) {
			this.isHoliday = isHoliday;
		}
		public String getWeek() {
			return week;
		}
		public void setWeek(String week) {
			this.week = week;
		}
    }
    
    public class WeekDay{
    	private String week;
    	private int dayNum;
    	private String weekDay1;
    	private String beginDate;
    	private String endDate;
    	
    	private String lastPlanDate;
    	private String lastSumDate;
    	private int isHoliday;
    	
    	
    	public int getIsHoliday() {
			return isHoliday;
		}

		public void setIsHoliday(int isHoliday) {
			this.isHoliday = isHoliday;
		}

		public String getLastPlanDate() {
			return lastPlanDate;
		}

		public void setLastPlanDate(String lastPlanDate) {
			this.lastPlanDate = lastPlanDate;
		}

		public String getLastSumDate() {
			return lastSumDate;
		}

		public void setLastSumDate(String lastSumDate) {
			this.lastSumDate = lastSumDate;
		}

		public String getBeginDate() {
			return beginDate;
		}

		public void setBeginDate(String beginDate) {
			this.beginDate = beginDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getWeekDay1() {
			return weekDay1;
		}

		public void setWeekDay1(String weekDay1) {
			this.weekDay1 = weekDay1;
		}

		public WeekDay(String week){
    		this.week = week;
    	}
    	
		public int getDayNum() {
			return dayNum;
		}
		public void setDayNum(int dayNum) {
			this.dayNum = dayNum;
		}
		public String getWeek() {
			return week;
		}
		public void setWeek(String week) {
			this.week = week;
		}
    	
    	
    }
}
