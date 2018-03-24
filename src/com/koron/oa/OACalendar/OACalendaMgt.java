package com.koron.oa.OACalendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.toDo.OAToDoBean;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.DefineSQLBean;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

public class OACalendaMgt extends AIODBManager{

	/**
	 * 根据关联Id删除日程
	 * @param id
	 * @return
	 */
	public void delByRelationId(String id){
		String calendarId="";
		String sql ="select id from OACalendar where relationId = ?";
		ArrayList param = new ArrayList();
		param.add(id);
		Result rs = sqlList(sql, param);
		ArrayList res = (ArrayList)rs.retVal;
		if(res.size()>0){
			Object codeId = ((Object[])res.get(0))[0];
			calendarId = codeId.toString();
			deleteBean(calendarId, OACalendarBean.class, "id");
		}
		
	}
	/**
	 * 外部添加接口
	 * @param title
	 * @param type
	 * @param startTime
	 * @param finish
	 * @param relationId
	 * @return
	 */
	public Result outAddCalendar(final String userId,final String title,final String type,final String startTime,
			final String finishTime,final String relationId,final String participant,final String delStatus,final String finishStatus,final String clientId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {
                    		//检查是否已经存在
                    		String existId ="";
                			String sql = "select id from OACalendar where relationId = ?";
              		  		PreparedStatement ps = conn.prepareStatement(sql);	
              		  		ps.setString(1, relationId);
              		  		ResultSet rss = ps.executeQuery();
              		  		if(rss.next()){
              		  			existId = rss.getString("id");
              		  		}
              		  		if(!"".equals(existId)){
              		  			deleteBean(existId, OACalendarBean.class, "id",session);
              		  		}
              		  		OACalendarBean bean = new OACalendarBean();
              				bean.setId(IDGenerater.getId());
              				bean.setTitle(title);
              				bean.setType(type);
              				bean.setStratTime(startTime);
              				bean.setFinishTime(finishTime);
              				bean.setRelationId(relationId);
              				bean.setUserId(userId);
              				bean.setClientId(clientId);
              				if(delStatus !=null && !"".equals(delStatus)){
              					bean.setDelStatus(delStatus);
              				}else{
              					bean.setDelStatus("1");//不可
              				}
              				if(finishStatus !=null && !"".equals(finishStatus)){
              					bean.setFinishStatus(finishStatus);
              				}else{
              					bean.setFinishStatus("0");//不可
              				}	
              				bean.setParticipant(participant);
              				addBean(bean,session);
              				result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
              		  		           		              		  		
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        					BaseEnv.log.error("ToCalendarMgt outAddCalendar : ", ex) ;
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
	 * 外部跟新接口
	 * @param title
	 * @param type
	 * @param startTime
	 * @param finishTime
	 * @param relationId
	 * @return
	 */
	public String updateByRelationId(String title,String type,String startTime,String finishTime,
			String relationId,String participant,String finishStatus){
		String calendarId="";
		String sql ="select id from OACalendar where relationId = ?";
		ArrayList param = new ArrayList();
		param.add(relationId);
		Result rs = sqlList(sql, param);
		ArrayList res = (ArrayList)rs.retVal;
		if(res.size()>0){
			Object codeId = ((Object[])res.get(0))[0];
			calendarId = codeId.toString();
			OACalendarBean bean = (OACalendarBean)loadBean(calendarId, OACalendarBean.class).retVal;
			bean.setTitle(title);
			bean.setType(type);
			bean.setStratTime(startTime);
			bean.setFinishTime(finishTime);
			bean.setRelationId(relationId);
			bean.setParticipant(participant);
			if(finishStatus !=null && !"".equals(finishStatus)){
				bean.setFinishStatus(finishStatus);
			}			
			updateBean(bean);
			return "ok";
		}else{
			return "no";
		}			
	}
	
	/**
	 * 查询
	 * @param lvForm//
	 * @return
	 */
	public Result queryCalendar(final String start, final String end, final String loginId,final String deptCode,String type,String keyId,String crmEnter,String clientId){		
		List param = new ArrayList();
		String sql = "select O.id,O.title,O.type,O.userId,O.stratTime,O.finishTime,O.delStatus,O.relationId,O.clientId,O.finishStatus from OACalendar O "
			+" where '1'=? and (O.userId = '"+loginId+"' or O.participant like '%"+loginId+";%' or O.participant like '%"+deptCode+";%') and (O.stratTime >= '"+start+"' and O.stratTime <= '"+end+"')";
		if(type!=null && !"".equals(type)){
			String[] types =type.split(";");
			if(types.length>0 && !"".equals(types[0])){
				sql += " and O.type in(";
				for (String keys : types) {
					sql += "'"+keys+"',";
				}
				sql += "'')";
			}
		}		
		if(keyId !=null && !"".equals(keyId)){
			sql += " and O.id = '"+keyId+"'";
		}
		
		if(clientId !=null && !"".equals(clientId)){
			sql += " and O.clientId = '"+clientId+"'";
		}
		
		/*if(!"true".equals(crmEnter)){
			sql += " and O.type <> '客户日程'";
		}*/
		System.out.println(sql);
		param.add("1");
		return sqlList(sql, param);
	}
	/*添加后重复获取数据准备*/
	public Result getCalendar(String keyId,String loginId){		
		List param = new ArrayList();
		String sql = "select O.id,O.title,O.type,O.userId,O.stratTime,O.finishTime,O.alertTime,O.clientId,O.relationId from OACalendar O"
			+" where '1'=? and (O.userId = '"+loginId+"' or O.participant like '%"+loginId+";%')";		
		if(keyId !=null && !"".equals(keyId)){
			sql += " and O.id = '"+keyId+"'";
		}
		System.out.println(sql);
		param.add("1");
		return sqlList(sql, param);
	}
	/**
	 * 添加
	 * @param bean
	 * @return
	 */
	public Result addCalendar(final OACalendarBean bean){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {
                    		String times = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
                			if(bean.getAlertTime() !=null && !"".equals(bean.getAlertTime()) ){
                				//插入提醒
                				Calendar cal = Calendar.getInstance();
                    			cal.setTime(BaseDateFormat.parse(bean.getAlertTime(), BaseDateFormat.yyyyMMddHHmmss));
                    			int hours = cal.get(Calendar.HOUR_OF_DAY);
                    			int mintues = cal.get(Calendar.MINUTE);
                    			AlertBean alertBean = new AlertBean();  
                    			alertBean.setAlertDate(BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd));
                    			alertBean.setAlertHour(hours);
                    			alertBean.setAlertMinute(mintues);                   		                			           					                					
                				alertBean.setNextAlertTime(bean.getAlertTime());              			                                    			                   			
                    			alertBean.setId(IDGenerater.getId());
                    			alertBean.setAlertContent(bean.getTitle()+"提醒");
                    			String url = "/OACalendarAction.do?operation=4&id="+bean.getId();                               		
                    	        String context="<a href=\"javascript:mdiwin('"+url+"','日程提醒')\">日程提醒</a>";
                    			alertBean.setAlertUrl(context);                   			
                    			alertBean.setIsLoop("no");	                   				            					
            					alertBean.setLoopTime(1);            			                    			
                    			alertBean.setAlertType("4,");
                    			alertBean.setCreateBy(bean.getUserId());
                    			alertBean.setCreateTime(BaseDateFormat.format(new Date(),
                    					BaseDateFormat.yyyyMMddHHmmss));
                    			alertBean.setRelationId(bean.getId());                			
                    			alertBean.setStatusId(0);
                    			String popedomUserIds = bean.getUserId()+",";                   		
                    			alertBean.setPopedomUserIds(popedomUserIds);
                    			addBean(alertBean,session);
                    			bean.setAlertId(alertBean.getId()); 
                			} 
                			addBean(bean, session);
                			
                			DefineSQLBean defineSqlBean = (DefineSQLBean) BaseEnv.defineSqlMap.get("OACalendar_add");
							if (defineSqlBean != null) {
								HashMap map = new HashMap();
								map.put("id", bean.getId());
								Result rs3 = defineSqlBean.execute(conn, map,bean.getUserId(), null, null, "");
								if (rs3.retCode != ErrorCanst.DEFAULT_SUCCESS) {
									BaseEnv.log.debug(rs3.retVal);
									result.setRetVal(rs3.retVal);
									result.setRetCode(rs3.retCode);
									return;
								}
							}
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("ToCalendarMgt addCalendar : ", ex) ;
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
	 * 跟新
	 * @param bean
	 * @return
	 */
	public Result updateCalendar(final OACalendarBean bean){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		//判断alertbean是否为空
                    		int num=0;
                    		if(!"".equals(bean.getAlertId())){
                    			String sql = "select count(*) as idSum from tblalert where id = ?";
                  		  		PreparedStatement ps = conn.prepareStatement(sql);	
                  		  		ps.setString(1, bean.getAlertId());
                  		  		ResultSet rss = ps.executeQuery() ;
	                  		  	if(rss.next()){
	                    			num = rss.getInt("idSum");
	                    		}
                    		}  
                    		String times = BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
                			if(bean.getAlertTime() !=null && !"".equals(bean.getAlertTime()) ){
                				//插入提醒
                				Calendar cal = Calendar.getInstance();
                    			cal.setTime(BaseDateFormat.parse(bean.getAlertTime(), BaseDateFormat.yyyyMMddHHmmss));
                    			int hours = cal.get(Calendar.HOUR_OF_DAY);
                    			int mintues = cal.get(Calendar.MINUTE);
                    			AlertBean alertBean = new AlertBean();  
                    			alertBean.setAlertDate(BaseDateFormat.format(cal.getTime(), BaseDateFormat.yyyyMMdd));
                    			alertBean.setAlertHour(hours);
                    			alertBean.setAlertMinute(mintues);                   		                			           					                					
                				alertBean.setNextAlertTime(bean.getAlertTime());              			                                    			                   			
                    			alertBean.setId(IDGenerater.getId());
                    			alertBean.setAlertContent(bean.getTitle()+"提醒");
                    			String url = "/OACalendarAction.do?operation=4&id="+bean.getId();                               		
                    	        String context="<a href=\"javascript:mdiwin('"+url+"','日程提醒')\">日程提醒</a>";
                    			alertBean.setAlertUrl(context);                   			
                    			alertBean.setIsLoop("no");	                   				            					
            					alertBean.setLoopTime(1);            			                    			
                    			alertBean.setAlertType("4,");
                    			alertBean.setCreateBy(bean.getUserId());
                    			alertBean.setCreateTime(BaseDateFormat.format(new Date(),
                    					BaseDateFormat.yyyyMMddHHmmss));
                    			alertBean.setRelationId(bean.getId());                			
                    			alertBean.setStatusId(0);
                    			String popedomUserIds = bean.getUserId()+",";                   		
                    			alertBean.setPopedomUserIds(popedomUserIds);
                    			if(num>=1){
                    				alertBean.setId(bean.getAlertId());
                    				updateBean(alertBean,session); 
                    			}else{
                    				alertBean.setId(IDGenerater.getId());
                    				addBean(alertBean, session);
                    			}
                    			bean.setAlertId(alertBean.getId());               		
                			}else{
                				//删除
                				if(num>0){               					
                					String sql = "delete from tblalert where id = ?";
                      		  		PreparedStatement ps = conn.prepareStatement(sql);	
                      		  		ps.setString(1, bean.getAlertId());
                      		  		bean.setAlertId("");
                      		  		bean.setAlertTime("");
                      		  		ps.executeUpdate();
                				}               				
                			} 
                			updateBean(bean, session);
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("ToCalendarMgt updateCalendar : ", ex) ;
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
	 * 加载
	 * @param id
	 * @return
	 */
	public Result loadCalendar(String id){
		return loadBean(id, OACalendarBean.class);
	}
	
	/**
	 * 删除
	 * @param bean
	 * @return
	 */
	public Result delCalendar(final String id){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {      
                    		if(id !=null && !"".equals(id)){
                    			for (String keyId : id.split(";")) {
    								if(!"".equals(keyId)){
    									//查询alertId
    		                    		OACalendarBean bean = (OACalendarBean)loadBean(keyId, OACalendarBean.class, session).retVal;
    		                    		
    		                			String sql = "delete from tblalert where id = ?";
    		              		  		PreparedStatement ps = conn.prepareStatement(sql);	
    		              		  		ps.setString(1, bean.getAlertId());
    		              		  		ps.executeUpdate();
    		              		  		deleteBean(keyId, OACalendarBean.class, "id",session);
    		              		  		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
    								}
    							}  
                    		}
                			                 		                   		                   		             		  		
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        					BaseEnv.log.error("ToCalendarMgt delCalendar : ", ex) ;
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
	 * 添加分类 
	 * @param loginId
	 * @param type
	 * @return
	 */
	public Result addType(final String id,final String loginId,final String type,
			final String color){	
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                			String sql = "insert into OACalendarType(id,type,userId,color)values(?,?,?,?)";
              		  		PreparedStatement ps = conn.prepareStatement(sql);	
              		  		ps.setString(1,id);
              		  		ps.setString(2, type);
              		  		ps.setString(3,loginId);
              		  		ps.setString(4,color);             		
              		  		ps.executeUpdate();      		  		
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("ToCalendarMgt addType : ", ex) ;
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
	 * 根据分类判断日程是否存在
	 * @param type
	 * @param loginId
	 * @return
	 */
	public Result getExist(String type,String loginId){
		String sql ="select id as idSum from OACalendar where type =? and userId ='"+loginId+"'";
		ArrayList param = new ArrayList();
		param.add(type);
		return sqlList(sql, param);
	}
	
	/**
	 * 删除分类  
	 * @param type
	 * @return
	 */
	public Result delType(final String typeId){	
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	                  	
                			String sql = "delete OACalendarType where id=?";
              		  		PreparedStatement ps = conn.prepareStatement(sql);	
              		  		ps.setString(1,typeId);              		  		
              		  		ps.executeUpdate();      		  		
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("ToCalendarMgt delType : ", ex) ;
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
	 * 查询分类
	 * @param loginId
	 * @return
	 */
	public Result selectType(String loginId){			
		String sql = "select * from OACalendarType where userId = ? or isnull(userId,'')='' order by id asc";
  		ArrayList param = new ArrayList();
  		param.add(loginId);
        return sqlList(sql, param);
    }
	
	/**
	 * 根据类型获取数量
	 * @param loginId
	 * @return
	 */
	public Result getNumByType(final String loginId,final String start,final String end){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {
                    		/*查询出所有分类*/
                    		String hql = "select type from oaCalendarType where userId=? or isnull(userId,'')=''";
                	  		PreparedStatement pss = conn.prepareStatement(hql);	
                	  		pss.setString(1,loginId);                 	  		
                	  		ResultSet rs= pss.executeQuery();
                	  		HashMap<String, String> maps = new HashMap<String, String>();
                	  		while(rs.next()){
                	  			String type = rs.getString("type");             	  			              	  			
                	  			maps.put(type, "0");
                	  		}
                    		
                    		/*查出有数据的分类*/
                    		String sql = "select type,count(type) as num  from oaCalendar where userId=? and stratTime>=? and finishTime<=? group by type";
                	  		PreparedStatement ps = conn.prepareStatement(sql);	
                	  		ps.setString(1,loginId);   
                	  		ps.setString(2,start);
                	  		ps.setString(3,end);
                	  		ResultSet res= ps.executeQuery();
                	  		//HashMap<String, String> maps = new HashMap<String, String>();
                	  		int allNum = 0;
                	  		while(res.next()){
                	  			String type = res.getString("type");
                	  			String num = res.getString("num");
                	  			allNum += Integer.parseInt(num);
                	  			maps.put(type, num);
                	  		}
                	  		maps.put("allDays", String.valueOf(allNum));
                	  		result.setRetVal(maps);
                	  		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        					BaseEnv.log.error("ToCalendarMgt changeColor : ", ex) ;
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
	 * 判断分类唯一
	 * @param type
	 * @param loginId
	 * @return
	 */
	public Result getTypeColor(String type,String loginId){			
		String sql = "select id from OACalendarType where type = ? and userId = '"+loginId+"'";
  		ArrayList param = new ArrayList();
  		param.add(type);
        return sqlList(sql, param);
    }
	/**
	 * 改变颜色 
	 * @param type
	 * @param color
	 * @param loginId
	 * @return
	 */
    public Result changeColor(final String type,final String color,final String loginId){	
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		String sql = "update OACalendarType set color = ? where id=? and userId = ?";
                	  		PreparedStatement ps = conn.prepareStatement(sql);	
                	  		ps.setString(1,color);  
                	  		ps.setString(2,type); 
                	  		ps.setString(3,loginId); 
                	  		ps.executeUpdate();   		  		
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("ToCalendarMgt changeColor : ", ex) ;
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
     * 根据id组获取分类
     * @param ids
     * @return
     */
    public Result getTypeName(String ids){
    	String[] id = ids.split(",");
    	ArrayList param = new ArrayList(); 		
    	String sql = "select type from OACalendarType where '1'=? and id in(";
    	for (String keyId : id) {
			sql += "'"+keyId+"',";
		}
    	sql +="'')";
    	param.add("1");
    	return sqlList(sql, param);
    }
    
    /**
     * 日程列表查询
     * @param lvForm
     * @param loginId
     * @return
     */
    public Result queryList(OACalendarForm lvForm,String loginId,String clientId,String condition){
    	String sql = "select  D.id,D.title,D.type,substring(D.stratTime,1,10) as stratTime,substring(D.finishTime,1,10) as finishTime,D.userId,D.delStatus,D.alertTime,D.relationId,D.clientId,C.clientName,D.finishStatus,D.taskId" +
		" from oacalendar D left join crmClientInfo C on D.clientId = C.id left join tblEmployee on D.userId=tblEmployee.id where (D.userId = ? "+condition+" or D.participant like ?)";   	
    	List param = new ArrayList();
    	param.add(loginId);
    	param.add("%"+loginId+"%");
    	if(lvForm.getUserId()!=null&& !"".equals(lvForm.getUserId())){
    		sql +=" and tblEmployee.EmpFullName like '%'+?+'%'";
			param.add(lvForm.getUserId());
    	}
		if(lvForm.getType() !=null && !"".equals(lvForm.getType())){
			sql +=" and D.type =?";
			param.add(lvForm.getType());
		}
		if(lvForm.getListTitle() !=null && !"".equals(lvForm.getListTitle())){
			sql +=" and D.title like ?";
			param.add("%"+lvForm.getListTitle()+"%");
		}
		if(lvForm.getCreateTime() !=null && !"".equals(lvForm.getCreateTime())){
			sql +=" and substring(D.stratTime,1,10) >= ?";
			param.add(lvForm.getCreateTime());
		}
		if(lvForm.getEndTime() !=null && !"".equals(lvForm.getEndTime())){
			sql +=" and substring(D.finishTime,1,10) <= ?";
			param.add(lvForm.getEndTime());
		}
		if(lvForm.getClientName() !=null && !"".equals(lvForm.getClientName())){
			sql +=" and C.clientName like ?";
			param.add("%"+lvForm.getClientName()+"%");
		}
		if(lvForm.getFinishStatus() !=null && !"".equals(lvForm.getFinishStatus())){
			sql +=" and D.finishStatus = ?";
			param.add(lvForm.getFinishStatus());
		}
		
		if(clientId !=null && !"".equals(clientId)){
			sql +=" and D.clientId = ?";
			param.add(clientId);
		}
		sql += " order by D.stratTime desc";
		System.out.println(sql);	
		return sqlList(sql, param,15,lvForm.getPageNo(), true);
    }
    
    /**
     * 日程列表分类获取
     * @param loginId
     * @return
     */
    public Result queryTypeList(String loginId){			
		String sql = "select * from OACalendarType where userId = ? or isNull(userId,'') =''  order by id asc";
  		ArrayList param = new ArrayList();
  		param.add(loginId);
        return sqlList(sql, param);
    }
    /**
     * 日程点击事件
     * @param id
     * @return
     */
    public Result eventCalendar(String id){			
		String sql = "select  D.id,D.title,D.type,D.stratTime,D.finishTime,D.userId,D.delStatus,D.alertTime,D.relationId,D.clientId,C.clientName,D.finishStatus,D.taskId  " +
				" from oacalendar D left join crmClientInfo C on D.clientId = C.id where D.Id= ?";
  		ArrayList param = new ArrayList();
  		param.add(id);
        return sqlList(sql, param);
    }
    
    /**
     * 获取关联ID个数
     * @param relationId
     * @param type
     * @return
     */
    public String queryRelationCount(String relationId,String type){
    	String count = "0";
    	String sql = "SELECT count(id) FROM OACalendar WHERE relationId=? and type=?";
		ArrayList param = new ArrayList();
		param.add(relationId);
		param.add(type);
		Result rs = sqlList(sql, param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList list = (ArrayList)rs.retVal;
			if(list!=null && list.size()>0){
				count = String.valueOf(GlobalsTool.get(list.get(0),0));
			}
		}
		return count;
    }
    
    /**
	 * 根据ID获取名称
	 * @param clientId
	 * @return
	 */
	public String getClientNameById(String clientId){
		String clientName="";
		if(clientId!=null && !"".equals(clientId)){
			List param = new ArrayList();
			String sql = "SELECT moduleId,clientName FROM CRMClientInfo WHERE id=?";
			param.add(""+clientId+"");
			Result rs = sqlList(sql, param);
			ArrayList<Object> list = (ArrayList<Object>)rs.retVal;
			if(list!=null && list.size()>0){
				clientName = String.valueOf(GlobalsTool.get(list.get(0),1));
			}
		}
		return clientName;
	} 
    
	public OACalendarBean getCalendarByRelationId(String keyId,String relationId){
		OACalendarBean bean = null;
		
		if(keyId!=null && !"".equals(keyId)){
			Result rs = loadBean(keyId, OACalendarBean.class);
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				bean = (OACalendarBean)rs.retVal;
			}
		}else{
			String hql = "FROM OACalendarBean WHERE relationId =?";
			ArrayList param = new ArrayList();
			param.add(relationId);
			Result rs = list(hql, param);
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				ArrayList<OACalendarBean> list = (ArrayList<OACalendarBean>)rs.retVal;
				if(list!=null && list.size()>0){
					bean = list.get(0);
				}
			}
		}
		
		return bean;
	}
	
	public Result updBatchOperation(final String finishStatusVal,final String ids){	
    	final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		String idsStr = "";
                    		for(String str : ids.split(",")){
                    			idsStr += "'"+str+"',";
                    		}
                    		if(idsStr.endsWith(",")){
                    			idsStr = idsStr.substring(0,idsStr.length()-1);
                    		}
                    		String sql = "update OACalendar set finishStatus = ?,finishTime=? where id in("+idsStr +")";
                	  		PreparedStatement ps = conn.prepareStatement(sql);	
                	  		ps.setString(1,finishStatusVal);  
                	  		ps.setString(2,BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)); 
                	  		ps.executeUpdate();   		  		
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					BaseEnv.log.error("ToCalendarMgt changeColor : ", ex) ;
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
