package com.koron.oa.office.car.carInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

public class CarInfoMgt extends AIODBManager{

	/**
	 * 查询车辆信息
	 * @param lvForm
	 * @return
	 */
	public Result queryCar(CarInfoForm lvForm,String insureTime,String nextTime,String sortInfo){
		String sql = "from OACarInfoBean  where '1'=?";
		if(lvForm.getCarName() !=null && !"".equals(lvForm.getCarName())){
			sql +=" and CarName like '%"+lvForm.getCarName()+"%'";
		}
		if(lvForm.getCarStatus() !=null && !"".equals(lvForm.getCarStatus())){
			sql +=" and CarStatus = '"+lvForm.getCarStatus()+"'";
		}	
		if(lvForm.getCarNo() !=null && !"".equals(lvForm.getCarNo())){
			sql +=" and CarNo = '"+lvForm.getCarNo()+"'";
		}
		if(insureTime !=null && !"".equals(insureTime)){
			if("week".equals(insureTime)){
				Calendar cal=Calendar.getInstance(); 
				String fTime =BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
				cal.add(Calendar.DATE, 6);
				String eTime =BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
				sql +=" and OvreDate >= '"+fTime+"' and OvreDate <='"+eTime+"'";
			}else{
				Calendar cal=Calendar.getInstance(); 
				String fTime =BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
				cal.add(Calendar.DATE, 29);
				String eTime =BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
				sql +=" and OvreDate >= '"+fTime+"' and OvreDate <='"+eTime+"'";
			}			
		}
		if(nextTime !=null && !"".equals(nextTime)){
			if("week".equals(nextTime)){
				Calendar cal=Calendar.getInstance(); 
				String fTime =BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
				cal.add(Calendar.DATE, 6);
				String eTime =BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
				sql +=" and NextMaintainDate >= '"+fTime+"' and NextMaintainDate <='"+eTime+"'";				
			}else{
				Calendar cal=Calendar.getInstance(); 
				String fTime =BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
				cal.add(Calendar.DATE, 29);
				String eTime =BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd);
				sql +=" and NextMaintainDate >= '"+fTime+"' and NextMaintainDate <='"+eTime+"'";
			}			
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
	 * 添加b车辆信息
	 * @param bean
	 * @return
	 */
	public Result addCar(final OACarInfoBean bean,final String loginId){	
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		addBean(bean, session);
                    		/*String nowTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
                    		if(bean.getNextMaintainDate() !=null && !"".equals(bean.getNextMaintainDate())){
                    			String sql= " insert into tbladvice(id,send,title,receive,content,receiveName,sendName,relatMsgId,createBy,lastUpdateBy,createTime,lastUpdateTime,status,exist,type,relationId) "+
    							"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    							PreparedStatement pss = conn.prepareStatement(sql);						
    							pss.setString(1, IDGenerater.getId());
    							pss.setString(2, loginId);
    							pss.setString(3, bean.getCarName()+"保养到期了!");
    							pss.setString(4, bean.getMaintainPeople());
    							pss.setString(5, "<a href=\"javascript:mdiwin('/CarInfoAction.do?operation=4&openFlag=3)\">"+bean.getCarName()+"保养到期了!</a>");
    							pss.setString(6, GlobalsTool.getEmpFullNameByUserId(bean.getMaintainPeople()));
    							pss.setString(7,GlobalsTool.getEmpFullNameByUserId(loginId));
				    	        pss.setString(8, "");
    							pss.setString(9, loginId);
    							pss.setString(10, loginId);
    							pss.setString(11, bean.getNextMaintainDate());
    							pss.setString(12, nowTime);
				    	        pss.setString(13, "noRead");
				    	        pss.setString(14, "all");
				    	        pss.setString(15, "car");
				    	        pss.setString(16, bean.getId());
    							pss.executeUpdate();
                    		}else{
                    			String sql= " insert into tbladvice(id,send,title,receive,content,receiveName,sendName,relatMsgId,createBy,lastUpdateBy,createTime,lastUpdateTime,status,exist,type,relationId) "+
    							"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    			PreparedStatement pss = conn.prepareStatement(sql);						
    							pss.setString(1, IDGenerater.getId());
    							pss.setString(2, loginId);
    							pss.setString(3, bean.getCarName()+"保养到期了!");
    							pss.setString(4, bean.getMaintainPeople());
    							pss.setString(5, "<a href=\"javascript:mdiwin('/CarInfoAction.do?operation=4&openFlag=3)\">"+bean.getCarName()+"保养到期了!</a>");
    							pss.setString(6, GlobalsTool.getEmpFullNameByUserId(bean.getMaintainPeople()));
    							pss.setString(7,GlobalsTool.getEmpFullNameByUserId(loginId));
				    	        pss.setString(8, "");
    							pss.setString(9, loginId);
    							pss.setString(10, loginId);
    							pss.setString(11, nowTime);
    							pss.setString(12, nowTime);
				    	        pss.setString(13, "noRead");
				    	        pss.setString(14, "all");
				    	        pss.setString(15, "car");
				    	        pss.setStrin(16, bean.getId());
    							pss.executeUpdate();
                    		}   */              		        					
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
	 * xiugai xinxing
	 * @param bean
	 * @return
	 */
	public Result updateCar(final OACarInfoBean bean){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		updateBean(bean, session);
                    		
        					
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
	 *  添加车辆审批人 先进行全部删除再添加。
	 * @param bean
	 * @return
	 */
	public Result addCheck(final String name,final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		String sql = " TRUNCATE TABLE oacarcheck";
							PreparedStatement pss = conn.prepareStatement(sql);							
							pss.executeUpdate();
							
        					OACarCheckBean bean = new OACarCheckBean();
        					String[] checkId =id.split(";");
        					String[]  checker=name.split(";");
        					if(checkId !=null && !"".equals(checkId[0])){
        						int i=0;
            					for (String key : checker) {
            						String hql = "insert into oacarcheck(id,Role,r_ref) values(?,?,?)";
            						PreparedStatement ps = conn.prepareStatement(hql);	
        							ps.setString(1, IDGenerater.getId());
        							ps.setString(2, key);
        							ps.setString(3, checkId[i]);
        							ps.executeUpdate();       					
        							i++;
            					}       						
        					}
        					
        				} catch (Exception ex) {
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
	 *  chaxun审批人
	 * @return
	 */
	public Result queryCheck(){
		String sql = "select * from OACarCheck where 1=?";
		ArrayList param = new ArrayList();
		param.add("1");
		return sqlList(sql, param);
	}
	/**
	 * 跟新审批人
	 * @param bean
	 * @return
	 */
	public Result updateCheck(OACarCheckBean bean){		
		return updateBean(bean);
	}
	/**
	 * 是否唯一车牌
	 * @param carNO
	 * @return
	 */
	public Result uniqChecked(String carNO){
		String sql="select * from oacarinfo where 1=? and carno='"+carNO+"'";
		ArrayList param = new ArrayList();
		param.add("1");
		return sqlList(sql, param);
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
	 * 删除dan车辆
	 * @param id
	 * @return
	 */
	public Result delOneCar(String id){
		return deleteBean(id, OACarInfoBean.class, "id");
	}
	/**
	 * 删除duo车辆
	 * @param id
	 * @return
	 */
	public Result delMoreCar(String[] id){
		return deleteBean(id, OACarInfoBean.class, "id");
	}
	/**
	 *  加载car
	 * @param id
	 * @return
	 */
	public Result loadCar(String id){
		return loadBean(id, OACarInfoBean.class);
	}
}
