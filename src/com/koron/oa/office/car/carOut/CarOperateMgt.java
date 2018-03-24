package com.koron.oa.office.car.carOut;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.office.car.carInfo.OACarInfoBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

public class CarOperateMgt extends AIODBManager{

	/**
	 * 查询车辆接环信息
	 * @param lvForm
	 * @return
	 */
	public Result queryCarOperate(CarOperateForm lvForm,String fTimes,String eTimes,String sortInfo){
		String sql = "from OACarOperateBean  where '1'=?";
		if(lvForm.getApprover() !=null && !"".equals(lvForm.getApprover())){
			sql +=" and approver = '"+lvForm.getApprover()+"'";
		}
		if(lvForm.getCarNo() !=null && !"".equals(lvForm.getCarNo())){
			sql +=" and CarNo like '%"+lvForm.getCarNo()+"%'";
		}
		if(lvForm.getUserCarPerson() !=null && !"".equals(lvForm.getUserCarPerson())){
			sql +=" and userCarPerson like '%"+lvForm.getUserCarPerson()+"%'";
		}
		if(fTimes !=null && !"".equals(fTimes)){		
			Calendar cal=Calendar.getInstance(); 
			String fTime =BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);				
			sql +=" and outCarDate >= '"+fTime+"'";			
		}
		if(eTimes !=null && !"".equals(eTimes)){		
			Calendar cal=Calendar.getInstance(); 	
			String eTime =BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
			sql +=" and outCarDate <='"+eTime+"'";					
		}
		if(sortInfo == null || "".equals(sortInfo)){
			sql += " order by id desc";
		}else{
			sql += " order by "+sortInfo.split(",")[1] + " " + sortInfo.split(",")[0];
		}	
		ArrayList param = new ArrayList();
		param.add("1");
		return list(sql, param, lvForm.getPageNo(), lvForm.getPageSize(), true);
	}
	/**
	 * 添加车辆领用信息
	 * @param bean
	 * @return
	 */
	public Result addCarOperate(final OACarOperateBean bean,final String loginId){	
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		
                    		// 跟新车辆信息
                    		String hql = "select id from oacarinfo where carno = '"+bean.getCarNo()+"'";
                    		PreparedStatement ps = conn.prepareStatement(hql);
                    		ResultSet rss = ps.executeQuery() ;
                        	String id = "" ;
                        	if(rss.next()){
                        		id += rss.getString("id");
                        	}
                        	OACarInfoBean foBean = (OACarInfoBean)loadBean(id, OACarInfoBean.class,session).retVal;
                    		foBean.setFlag("1");
                    		bean.setStatus("0");
                        	updateBean(foBean, session);                    	      			
                    		addBean(bean, session);
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("CarInfoMgt addCheck : ", ex) ;
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
	 * 更新
	 * @param bean
	 * @return
	 */
	public Result updateCarOperate(final OACarOperateBean bean,final String loginId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {
                    		// 跟新车辆信息
                    		String hql = "select id from oacarinfo where carno = '"+bean.getCarNo()+"'";
                    		PreparedStatement ps = conn.prepareStatement(hql);
                    		ResultSet rss = ps.executeQuery() ;
                        	String id = "" ;
                        	if(rss.next()){
                        		id += rss.getString("id");
                        	}
                        	OACarInfoBean foBean = (OACarInfoBean)loadBean(id, OACarInfoBean.class,session).retVal;
                    		foBean.setFlag("1");
                        	updateBean(foBean, session);
                    		
                    		if(bean.getApprover() !=null && !"".equals(bean.getApprover())){
                    			bean.setStatus("0");                  			
                    		}        			                   		                  	
                    		updateBean(bean, session);         					
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("CarOperateMgt updateCarOperate : ", ex) ;
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
	 * 审核操作
	 * @param bean
	 * @return
	 */
	public Result orNoCarOperate(final OACarOperateBean bean,final String Outflag){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		String nowTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
                    		if("yes".equals(Outflag)){
                    			bean.setStatus("1");
                    			bean.setDynamic(bean.getDynamic()+nowTime+" 申请领用被"+GlobalsTool.getEmpFullNameByUserId(bean.getApprover())+"批准;");
                    	
                    		}
                    		if("no".equals(Outflag)){
                    			bean.setStatus("2");
                    			bean.setDynamic(bean.getDynamic()+nowTime+" 申请领用未被"+GlobalsTool.getEmpFullNameByUserId(bean.getApprover())+"批准,原因("+bean.getReason()+");");
                    			// 跟新车辆信息
                        		String hql = "select id from oacarinfo where carno = '"+bean.getCarNo()+"'";
                        		PreparedStatement ps = conn.prepareStatement(hql);
                        		ResultSet rss = ps.executeQuery() ;
                            	String id = "" ;
                            	if(rss.next()){
                            		id += rss.getString("id");
                            	}
                            	OACarInfoBean foBean = (OACarInfoBean)loadBean(id, OACarInfoBean.class,session).retVal;
                        		foBean.setFlag("0");
                            	updateBean(foBean, session);
                    		}            
                    		updateBean(bean, session);           					
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("CarOperateMgt orNoCarOperate : ", ex) ;
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
	 * 是否被领用
	 * @param id
	 * @return
	 */
	public Result delOrNo(String id){
		String sql="select  count(P.id) as tol from oacarinfo F left join oacaroperate P on p.CarNo = F.CarNo " +
				" where 1=? and F.id in("+id+")";
		ArrayList param = new ArrayList();
		param.add("1");
		return sqlList(sql, param);
	}
	/**
	 * 删除dan
	 * @param id
	 * @return
	 */
	public Result delOneCarOperate(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		
                			// 跟新车辆信息
                			OACarOperateBean bean = (OACarOperateBean)loadBean(id, OACarOperateBean.class, session).retVal;
                    		String hql = "select id from oacarinfo where carno = '"+bean.getCarNo()+"'";
                    		PreparedStatement ps = conn.prepareStatement(hql);
                    		ResultSet rss = ps.executeQuery() ;
                        	String ids = "" ;
                        	if(rss.next()){
                        		ids += rss.getString("id");
                        	}
                        	OACarInfoBean foBean = (OACarInfoBean)loadBean(ids, OACarInfoBean.class,session).retVal;
                    		foBean.setFlag("0");
                        	updateBean(foBean, session);
							
                    		deleteBean(id, OACarOperateBean.class, "id",session);                   		
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("CarOperateMgt delOneCarOperate : ", ex) ;
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
	 * 删除duo车辆
	 * @param id
	 * @return
	 */
	public Result delMoreCarOperate(final String[] id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		for (String key : id) {
                    			// 跟新车辆信息
                    			OACarOperateBean bean = (OACarOperateBean)loadBean(key, OACarOperateBean.class, session).retVal;
                        		String hql = "select id from oacarinfo where carno = '"+bean.getCarNo()+"'";
                        		PreparedStatement ps = conn.prepareStatement(hql);
                        		ResultSet rss = ps.executeQuery() ;
                            	String ids = "" ;
                            	if(rss.next()){
                            		ids += rss.getString("id");
                            	}
                            	OACarInfoBean foBean = (OACarInfoBean)loadBean(ids, OACarInfoBean.class,session).retVal;
                        		foBean.setFlag("0");
                            	updateBean(foBean, session);
                            	deleteBean(key, OACarOperateBean.class, "id",session); 
							}               		                  		
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("CarOperateMgt delMoreCarOperate : ", ex) ;
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
	 *  加载car
	 * @param id
	 * @return
	 */
	public Result loadCarOperate(String id){
		return loadBean(id, OACarOperateBean.class);
	}
	/**
	 * 获取空闲车辆
	 * @return
	 */
	public Result getFreeCar(){
		String sql = "from OACarInfoBean where '1'=? and flag = '0' and carStatus = '1'";
		ArrayList param = new ArrayList();
		param.add("1");
		return list(sql, param);
	}
	/**
	 * 多个判断状态
	 * @param keyId
	 * @return
	 */
	public Result outStatus(String[] keyId){		
		String sql ="select status from oacaroperate where 1=? and id in (";
		for (String key : keyId) {
			sql += "'"+key+"'";
		}
		sql += ")";
		ArrayList param = new ArrayList();
		param.add("1");
		return sqlList(sql, param);
	}
	/**
	 * 单个判断状态
	 * @param keyId
	 * @return
	 */
	public Result sigStatus(String keyId){		
		String sql ="select status from oacaroperate where 1=? and id ='"+keyId+"'";
		ArrayList param = new ArrayList();
		param.add("1");
		return sqlList(sql, param);
	}
	
	public Result backCar(final OACarOperateBean bean,final String loginName){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		String nowTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);                   	
                			bean.setStatus("3");
                			bean.setBackDate(nowTime);
                			bean.setBackRole(loginName);
                			bean.setDynamic(bean.getDynamic()+nowTime+" "+loginName+" 归还该车辆;");
                			// 跟新车辆信息
                    		String hql = "select id from oacarinfo where carno = '"+bean.getCarNo()+"'";
                    		PreparedStatement ps = conn.prepareStatement(hql);
                    		ResultSet rss = ps.executeQuery() ;
                        	String id = "" ;
                        	if(rss.next()){
                        		id += rss.getString("id");
                        	}
                        	OACarInfoBean foBean = (OACarInfoBean)loadBean(id, OACarInfoBean.class,session).retVal;
                    		foBean.setFlag("0");
                        	updateBean(foBean, session);                  		            
                    		updateBean(bean, session);           					
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("CarOperateMgt backCar : ", ex) ;
        				}
                    }
                });				
				return result.getRetCode();	
			}
		});
		result.retCode = retCode;
		return result;	
	}
}
