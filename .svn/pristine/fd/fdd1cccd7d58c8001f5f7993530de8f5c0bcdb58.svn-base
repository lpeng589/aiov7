package com.koron.oa.toDo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.koron.oa.OACalendar.OACalendaMgt;
import com.koron.oa.OACalendar.OACalendarBean;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.sun.swing.internal.plaf.basic.resources.basic;

public class ToDoMgt extends AIODBManager{
	
	/**
	 * 查询待办
	 * @param lvForm
	 * @return
	 */
	public Result queryToDo(ToDoForm lvForm, String tab,
			String loginId,String findcontxt,String selType){
		ArrayList param = new ArrayList();
		String sql ="from OAToDoBean where createBy =?";
		param.add(loginId);	
		if(findcontxt !=null && !"".equals(findcontxt)){
			sql += " and title like '%"+findcontxt+"%'";
		}
		if(selType !=null && !"".equals(selType)){
			String[] types = selType.split(";");
			sql += " and type in (";
			for (String key : types) {
				sql += "'"+key+"',";
			}
			sql += "'')";
		}
		if(lvForm.getId() !=null && !"".equals(lvForm.getId())){
			sql += " and id = '"+lvForm.getId()+"'";		
		}
		if(tab !=null && "over".equals(tab)){
			sql +=" and Status = 1 order by finishTime desc"; 
		}else{
			sql +=" and Status = 0 order by createTime desc";
			//return list(sql, param);
		}
		
		System.out.println(sql);			
		return list(sql, param, lvForm.getPageNo(), 15, true);
		
	}
	
	/**
	 * 查询钱获取数据
	 * @param id
	 * @return
	 */
	public Result queryPre(String id){
		String sql="select D.title,D.type,D.alertId,D.relationId,D.status,D.alertTime,D.createTime,D.ref_taskId,D.uploadFile from oatodo D  where '1'=? " ;			
		
		if(id !=null && !"".equals(id)){
			 sql += " and D.id= '"+id+"'";
			 
		}
		ArrayList param = new ArrayList();
		param.add("1");
		System.out.println(sql);	
		return sqlList(sql, param);
	}
	/**
	 * 获取客户名字
	 * @param id
	 * @return
	 */
	public Result clientName(String id){
		String[] ids =id.split(",");
		String sql="";
		if(ids.length>0){
			sql="select clientName from CRMClientInfo where '1'=? and id in (" ;
			for (String key : ids) {
				sql += "'"+key+"',";
			}
			sql += "'')";
		}							
		ArrayList param = new ArrayList();
		param.add("1");
		System.out.println(sql);	
		return sqlList(sql, param);
	}
	/**
	 * 添加
	 * @param bean
	 * @return
	 */
	public Result addToDo(final OAToDoBean bean){	
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		                                      	
                    		if(bean.getAlertTime() !=null && !"".equals(bean.getAlertTime())){
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
                    			alertBean.setAlertContent(bean.getTitle()+" 待办时间到了");
                    			String url = "/ToDoAction.do?operation=4&id="+bean.getId();                               		
                    	        String context="<a href=\"javascript:mdiwin('"+url+"','"+bean.getTitle()+ "待办时间到了')\">"+bean.getTitle()+ "待办时间到了</a>";
                    			alertBean.setAlertUrl(context);                   			
                    			alertBean.setIsLoop("no");	                   				            					
            					alertBean.setLoopTime(0);            			                    			
                    			alertBean.setAlertType("4,");
                    			alertBean.setCreateBy(bean.getCreateBy());
                    			alertBean.setCreateTime(BaseDateFormat.format(new Date(),
                    					BaseDateFormat.yyyyMMddHHmmss));
                    			alertBean.setRelationId(bean.getId());                			
                    			alertBean.setStatusId(0);
                    			String popedomUserIds = bean.getCreateBy()+",";                   		
                    			alertBean.setPopedomUserIds(popedomUserIds);
                    			addBean(alertBean,session);
                    			bean.setAlertId(alertBean.getId());                			
                    		}
                    		addBean(bean, session); 
                    		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);                 		     		        					
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        					BaseEnv.log.error("ToDoMgt addToDo : ", ex) ;
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
	public Result updateToDo(final OAToDoBean bean){	
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	                   		                  		               			               			             
                    		

                    		if("1".equals(bean.getStatus())){        			
                    			bean.setFinishTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
                    			if(bean.getAlertId() !=null && !"".equals(bean.getAlertId())){
                        			String sql = "delete tblalert where id=?";
                      		  		PreparedStatement ps = conn.prepareStatement(sql);	
                      		  		ps.setString(1,bean.getAlertId());              		  		
                      		  		ps.executeUpdate();   
                        		}
                    			bean.setAlertId("");
                    			bean.setAlertTime("");
                    		}
                    		
                    		//调用任务接口
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
              		  		                   		
                    		if(bean.getAlertTime() !=null && !"".equals(bean.getAlertTime())){
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
                    			
                    			alertBean.setAlertContent("待办时间到了:"+bean.getTitle());
                    			String url = "/ToDoAction.do?operation=4&id="+bean.getId();                               		
                    	        String context="<a href=\"javascript:mdiwin('"+url+"','待办时间到了:"+bean.getTitle()+ "')\">待办时间到了:"+bean.getTitle()+ "</a>";
                    			alertBean.setAlertUrl(context);                   			
                    			alertBean.setIsLoop("no");	                   				            					
            					alertBean.setLoopTime(0);            			                    			
                    			alertBean.setAlertType("4,");
                    			alertBean.setRelationTable("OAToDo");
                    			alertBean.setCreateBy(bean.getCreateBy());
                    			alertBean.setCreateTime(BaseDateFormat.format(new Date(),
                    					BaseDateFormat.yyyyMMddHHmmss));
                    			alertBean.setRelationId(bean.getId());                			
                    			alertBean.setStatusId(0);
                    			String popedomUserIds = bean.getCreateBy()+",";                   		
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
                    		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        					BaseEnv.log.error("ToDoMgt updateToDo : ", ex) ;
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
	 *  快速添加
	 * @return
	 */
	public Result qukAdd(OAToDoBean bean){		
		return addBean(bean);
	}
	/**
	 * 加载
	 * @param id
	 * @return
	 */
	public Result loadToDo(String id){
		return loadBean(id, OAToDoBean.class);
	}
	/**
	 * 链接查询
	 * @param id
	 * @return
	 */
	public Result getByOutId(String id,String loginId){
		
		ArrayList param = new ArrayList();
		String sql ="from OAToDoBean where createBy =?";
		param.add(loginId);	
		if(id !=null && !"".equals(id)){
			sql += " and id=?";
			param.add(id);	
		}			
		System.out.println(sql);				
		return list(sql, param);
	}
	
	/**
	 * 删除
	 * @param bean
	 * @return
	 */
	public Result delToDo(final String id){	
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {
                    		//查询alertId
                    		OAToDoBean bean = (OAToDoBean)loadBean(id, OAToDoBean.class, session).retVal;
                    		
                			String sql = "delete from tblalert where id = ?";
              		  		PreparedStatement ps = conn.prepareStatement(sql);	
              		  		ps.setString(1, bean.getAlertId());
              		  		ps.executeUpdate();
              		  		deleteBean(id, OAToDoBean.class, "id",session);
              		  		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        					BaseEnv.log.error("ToDoMgt delToDo : ", ex) ;
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
	public Result addType(final String id,final String loginId,final String type,final String color){	
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                			String sql = "insert into OAToDoType(id,type,userId,color)values(?,?,?,?)";
              		  		PreparedStatement ps = conn.prepareStatement(sql);	
              		  		ps.setString(1,id);
              		  		ps.setString(2, type);
              		  		ps.setString(3,loginId);
              		  		ps.setString(4,color);
              		  		ps.executeUpdate();    
              		  		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        					BaseEnv.log.error("ToDoMgt addType : ", ex) ;
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
	 * 删除分类  
	 * @param type
	 * @return
	 */
	public Result delType(final String type){	
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		//删除todo表的此分类所有数据
                    		Statement ps = conn.createStatement();   
                    		String sql = "delete from OAToDo where type = '"+type+"'";                    		               		
                    		ps.addBatch(sql); 
                    		
                			sql = "delete from OAToDoType where type= '"+type+"'";              		  			             		  		             		  		
              		  		ps.addBatch(sql); 
              		  		
              		  		ps.executeBatch();
              		  		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        					BaseEnv.log.error("ToDoMgt delType : ", ex) ;
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
		String sql = "select * from OAToDoType where userId = ? order by id asc";
  		ArrayList param = new ArrayList();
  		param.add(loginId);
        return sqlList(sql, param);
    }
	/**
	 * 获取分类Id
	 * @param type
	 * @param loginId
	 * @return
	 */
	public Result getTypeColor(String type,String loginId){			
		String sql = "select id from OAToDoType where type = ? and userId = '"+loginId+"'";
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
                    		String sql = "update OAToDoType set color = ? where type=? and userId = ?";
                	  		PreparedStatement ps = conn.prepareStatement(sql);	
                	  		ps.setString(1,color);  
                	  		ps.setString(2,type); 
                	  		ps.setString(3,loginId); 
                	  		ps.executeUpdate();   	
                	  		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        					BaseEnv.log.error("ToDoMgt changeColor : ", ex) ;
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
     * 获取待办 完成数量
     * @param loginId
     * @return
     */
	public Result getSum(String loginId,String types){		
		String type = "";
		if(types !=null && !"".equals(types)){
			String[] typeArr = types.split(";");
			type = " and type in (";
			for (String key : typeArr) {
				type += "'"+key+"',";
			}
			type += "'')";
		}
		String sql = "select count(*) ,(select count(*) from oatodo " +
				"where createBy = '"+loginId+"'"+type+")  from oatodo where status=0 and 1=? and  createBy = '"+loginId+"'"+type;
  		ArrayList param = new ArrayList();
  		param.add("1");
  		System.out.println(sql);
        return sqlList(sql, param);
    }
	
	/**
	 * 跟新类型
	 * @param bean
	 * @return
	 */
	public Result updateType(OAToDoBean bean){
			return updateBean(bean);		
	}
	
	/**
	 * 从日程获取详细 
	 * @param eventId
	 * @return
	 */
	public Result detailByEvent(final String eventId){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {	
				session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {  
                    	try {	
                    		String sql = "select relationId from oaCalendar where id=?";
                	  		PreparedStatement ps = conn.prepareStatement(sql);	
                	  		ps.setString(1,eventId);  
                	  		ResultSet rs = ps.executeQuery();
                	  		String toId = "";
                	  		while(rs.next()){
                	  			toId = rs.getString("relationId");
                	  		}
                	  		String hql ="select title,type,alertTime,finishTime,relationId,ref_taskId,status,uploadFile from oaToDo where id=?";	
                	  		PreparedStatement pss = conn.prepareStatement(hql);	
                	  		pss.setString(1,toId);  
                	  		ResultSet res = pss.executeQuery();
                	  		HashMap<String, String> maps = new HashMap<String, String>();
                	  		while(res.next()){
                	  			String finishTime = res.getString("finishTime");
                	  			if(!"".equals(res.getString("alertTime")) && res.getString("alertTime") !=null){
                	  				finishTime = res.getString("alertTime");
                	  			}
                	  			maps.put("finishTime",finishTime);
                	  			maps.put("title",res.getString("title"));
                	  			maps.put("type",res.getString("type"));   
                	  			maps.put("status", res.getString("status"));
                	  			maps.put("uploadFile", res.getString("uploadFile"));
                	  			String relationId = res.getString("relationId");
                	  			String clientName ="";
                	  			if(relationId!=null && !"".equals(relationId)){
                	  				String rsql = "select clientName from CRMClientInfo where id=?";
                	  				PreparedStatement prs = conn.prepareStatement(rsql);
                	  				System.out.println(relationId.substring(0, relationId.length()-1));
                	  				prs.setString(1,relationId.substring(0, relationId.length()-1));  
                        	  		ResultSet rps = prs.executeQuery();                      	  	
                        	  		if(rps.next()){
                        	  			clientName = rps.getString("clientName");
                        	  		}
                	  			}
                	  			maps.put("relationId",clientName);
                	  			String ref_task = res.getString("ref_taskId");
                	  			if(ref_task !=null && !"".equals(ref_task)){
                	  				ref_task = ref_task.split(";")[1];
                	  			}
                	  			maps.put("ref_taskId",ref_task);
                	  		}
                	  		result.setRetVal(maps);
                	  		result.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
        				}catch (Exception ex) {
        					ex.printStackTrace();
        					result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        					BaseEnv.log.error("ToDoMgt changeColor : ", ex) ;
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
