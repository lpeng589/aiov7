package com.koron.oa.office.meeting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.email.EMailMgt;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

public class OAMeetingMgt extends AIODBManager {
    
	private EMailMgt emgt=new EMailMgt();	
	/**
	 * 删除
	 */
    public Result deleteMeeting(final String meetingId){
    	final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement state = conn.createStatement();							
							String sql ="delete oameeting where id ='"+meetingId+"'";
							state.addBatch(sql);
							sql ="delete from oameetingsignin where meetingId='"+meetingId+"'";
							state.addBatch(sql);
							sql ="delete from tblAlert where relationId='"+meetingId+"'";
							state.addBatch(sql);
							state.executeBatch();
						} catch (Exception e) {
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rs;
 
		
	}
    
	/**
	 * 新增会议
	 * 
	 * @param OAMeetingBean
	 * @return
	 */
	public Result addMeeting(final OAMeetingBean meetingBean,final AlertBean alertBean){
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				try{
					 addBean(meetingBean,session);
					deleteBean(meetingBean.getId(), AlertBean.class, "relationId",session);
					addBean(alertBean, session);
				}catch(Exception e){
					result.retCode = ErrorCanst.DEFAULT_FAILURE;
				}
				return result.getRetCode();
			}
		});
		result.retCode = retCode;
		return result ;
	}
	
	
	/**
	 * 根据关系id 查询 会议  oameeting
	 *
	 * @param  String
	 * @return Result
	 */
	public Result loadMeeting(final String meetingId) {
		return loadBean(meetingId, OAMeetingBean.class);
	}

	/**
	 * 修改 会议  oameeting
	 * @return Result
	 */
	public Result update(final OAMeetingBean  meetingBean ,final AlertBean alertBean) {
		final Result rs = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(final Session session) {
				updateBean(meetingBean,session);
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							Statement state = conn.createStatement();
							
							deleteBean(meetingBean.getId(), AlertBean.class, "relationId",session);
							addBean(alertBean, session);
							 
							state.executeBatch();
						} catch (Exception e) {
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return ErrorCanst.DEFAULT_SUCCESS;
			}
		});
		return rs;
		
	}
	
	/**
	 * 修改 会议  oameeting
	 * @return Result
	 */
	public Result update(final OAMeetingBean  meetingBean ) {		
		return 	 updateBean(meetingBean);
		 
	}
	
	/**
	 * 查询会议室是否被占领
	 * @param startTime
	 * @param endTime
	 * @param boardroomId
	 * @return
	 */
	public int isOccupation(Date startTime ,Date endTime,String boardroomId){
		String sql = "select id from OAMeeting where boardroomId=? and  ((convert(varchar(100),startTime,120) <= ? and convert(varchar(100),endTime,120) >= ?) or (convert(varchar(100),startTime,120) <= ? and convert(varchar(100),startTime,120) >= ?))";
		List param = new ArrayList();
		String startT=BaseDateFormat.format(startTime, BaseDateFormat.yyyyMMddHHmmss);
		String endT=BaseDateFormat.format(endTime, BaseDateFormat.yyyyMMddHHmmss);
		param.add(boardroomId);
		param.add(startT);
		param.add(startT);
		param.add(endT);
		param.add(startT);
		System.out.println(sql);
		Result rs = sqlList(sql, param);
		ArrayList pa = (ArrayList)rs.retVal;
		if(pa !=null && pa.size()>0){
			return 1;
		}else{
			return 0;
		}		
	}
	
	/**
	 * 会议室周程表
	 */
	public Result queryweek(String boardroomId,List<Date> days,String userId,String deptCode){
		//创建参数
		List param = new ArrayList();
		String today=BaseDateFormat.format(days.get(0), BaseDateFormat.yyyyMMdd);
		
		String hql ="select * from (";
		String colunms="  select bean.id,bean.title,bean.meetingContent, bean.boardroomId, bean.toastmaster, bean.toastmasterName,bean.participant,bean.sponsor,bean.status,bean.signin";
		 hql += colunms+" ,bean.startTime,bean.endTime  ,convert(varchar(100),bean.endTime,23) as regularend   from OAMeeting bean where 1=1 and  regularMeeting =0   ";

		 hql += "  union  ";
		hql += colunms+" ,dateadd(wk,DATEDIFF(wk,bean.startTime,convert(datetime,?,120)),bean.startTime) as startTime ,dateadd(wk,DATEDIFF(wk,bean.endTime,convert(datetime,?,120)),bean.endTime) as endTime ,bean.regularend from OAMeeting bean where 1=1 and  regularMeeting =2    ";
		 param.add(today);
	     param.add(today);
	     hql += "  union  ";
			hql += colunms+" ,dateadd(mm,DATEDIFF(mm,bean.startTime,convert(datetime,?,120)),bean.startTime)  as startTime ,dateadd(mm,DATEDIFF(mm,bean.endTime,convert(datetime,?,120)),bean.endTime)  as endTime ,bean.regularend from OAMeeting bean where 1=1 and  regularMeeting =3 ";
			 param.add(today);
		     param.add(today); 
		   
		     hql += "  union  ";
				hql += colunms+" ,dateadd(dd,(DATEDIFF(dd,bean.startTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.startTime)  as startTime ,dateadd(dd,(DATEDIFF(dd,bean.endTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.endTime)  as endTime ,bean.regularend from OAMeeting bean where 1=1 and  regularMeeting =1   ";
				 param.add(BaseDateFormat.format(days.get(1), BaseDateFormat.yyyyMMdd));
			     param.add(BaseDateFormat.format(days.get(1), BaseDateFormat.yyyyMMdd)); 
			     hql += "  union  ";
					hql += colunms+" ,dateadd(dd,(DATEDIFF(dd,bean.startTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.startTime)  as startTime ,dateadd(dd,(DATEDIFF(dd,bean.endTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.endTime)  as endTime ,bean.regularend from OAMeeting bean where 1=1 and  regularMeeting =1   ";
					 param.add(BaseDateFormat.format(days.get(2), BaseDateFormat.yyyyMMdd));
				     param.add(BaseDateFormat.format(days.get(2), BaseDateFormat.yyyyMMdd)); 
				     hql += "  union  ";
						hql += colunms+" ,dateadd(dd,(DATEDIFF(dd,bean.startTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.startTime)  as startTime ,dateadd(dd,(DATEDIFF(dd,bean.endTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.endTime)  as endTime ,bean.regularend from OAMeeting bean where 1=1 and  regularMeeting =1   ";
						 param.add(BaseDateFormat.format(days.get(3), BaseDateFormat.yyyyMMdd));
					     param.add(BaseDateFormat.format(days.get(3), BaseDateFormat.yyyyMMdd)); 
					     hql += "  union  ";
							hql += colunms+" ,dateadd(dd,(DATEDIFF(dd,bean.startTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.startTime)  as startTime ,dateadd(dd,(DATEDIFF(dd,bean.endTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.endTime)  as endTime ,bean.regularend   from OAMeeting bean where 1=1 and  regularMeeting =1   ";
							 param.add(BaseDateFormat.format(days.get(4), BaseDateFormat.yyyyMMdd));
						     param.add(BaseDateFormat.format(days.get(4), BaseDateFormat.yyyyMMdd)); 
						     hql += "  union  ";
								hql += colunms+" ,dateadd(dd,(DATEDIFF(dd,bean.startTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.startTime)  as startTime ,dateadd(dd,(DATEDIFF(dd,bean.endTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.endTime)  as endTime ,bean.regularend from OAMeeting bean where 1=1 and  regularMeeting =1   ";
								 param.add(BaseDateFormat.format(days.get(5), BaseDateFormat.yyyyMMdd));
							     param.add(BaseDateFormat.format(days.get(5), BaseDateFormat.yyyyMMdd)); 
			   
		
		hql +=" ) bean where 1=1 and status is null   and   bean.starttime>=(select ben.starttime from oameeting ben where id=bean.id) and  bean.endtime <= dateadd(dd,1,convert(datetime,bean.regularend,120)) ";
		
		
		if(userId!=null){
		hql += " and (  toastmaster like ?  or participant like ?  or participant like '%"+deptCode+";%' or sponsor=?  )   ";
			param.add("%"+userId+";%");
			param.add("%"+userId+";%");
			param.add(userId);
		}
		if(boardroomId!=null){
			hql += "  and  bean.boardroomId=?    ";
			param.add(boardroomId);
		}
		
		hql += " and (  ( ? <= bean.startTime  and bean.startTime < ? ) ";
		hql += "  or ( ? <= bean.endTime  and bean.endTime < ? )";
		hql += "  or ( ? >= bean.startTime  and bean.endTime > ? )  )";
		param.add(new java.sql.Timestamp(days.get(0).getTime()));
		param.add(new java.sql.Timestamp(days.get(7).getTime()));
		param.add(new java.sql.Timestamp(days.get(0).getTime()));
		param.add(new java.sql.Timestamp(days.get(7).getTime()));
		param.add(new java.sql.Timestamp(days.get(0).getTime()));
		param.add(new java.sql.Timestamp(days.get(7).getTime()));
		
		System.out.println(hql);
		//调用list返回结果
		return sqlList(hql, param,  1000,1, true);
	}
	
	//查询  并例会
	public Result queryMeeting( OAMeetingSearchForm form,  String userId){
		/*打死*/
		ArrayList param = new ArrayList();
		String sql = "select * from OAMeeting where 1=1 ";
		if(form.getMeetId() !=null && !"".equals(form.getMeetId())){
			sql += " and id = ?";
			param.add(form.getMeetId());
		}
		if(form.getBoardroomId() !=null && !"".equals(form.getBoardroomId())){
			sql += " and boardroomId = ?";
			param.add(form.getBoardroomId());
		}
		if(form.getSelectStatus()>0){
			if(form.getSelectStatus()==1){
				//未开始
				sql += " and convert(varchar(100),startTime,20)>?";
				System.out.println(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
				param.add(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));			
			}else if(form.getSelectStatus()==2){
				//进行中
				sql += " and convert(varchar(100),startTime,20)<=? and convert(varchar(100),endTime,20) > ?";
				param.add(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
				param.add(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			}else if(form.getSelectStatus()==3){
				//已结束
				sql += " and convert(varchar(100),startTime,20)<? and convert(varchar(100),endTime,20) <= ?";
				param.add(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
				param.add(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
			}else if(form.getSelectStatus()==4){
				//已取消
				sql += " and status is not null";
			}
			
		}
		if(form.getUserType() !=null && !"".equals(form.getUserType())){
			if("5".equals(form.getUserType())){
				//不能出席的
				sql += " and signin is not null";
			}else if("2".equals(form.getUserType())){
				//参与的
				sql += " and participant like '%"+userId+";%'";
			}else if("3".equals(form.getUserType())){
				//主持的
				sql += " and toastmaster like '%"+userId+";%'";
			}else if("4".equals(form.getUserType())){
				//发起的
				sql += " and sponsor=?";
				param.add(userId);
			}
			
		}else{
			sql += " and (toastmaster like '%"+userId+";%' or participant like '%"+userId+";%' or sponsor=?)";			
			param.add(userId);
		}
		if(form.getMeetingStartTime() !=null && !"".equals(form.getMeetingStartTime())){
			sql += " and convert(varchar(100),startTime,23)>=?";
			param.add(form.getMeetingStartTime());
		}
		if(form.getMeetingEndTime() !=null && !"".equals(form.getMeetingEndTime())){
			sql += " and convert(varchar(100),endTime,23)<=?";
			param.add(form.getMeetingEndTime());
		}
		sql += " order by startTime desc";
		System.out.println(sql);
		return sqlList(sql, param,  form.getPageSize(),form.getPageNo(), true);		
	}
	
	/**
	 * 例会的签到问题
	 */
	public Result getSignin(String meetingId,String startTime){
		final String id=meetingId+startTime;
		return loadBean(id, OASigninBean.class);
	}
	
	public Result setSignin(OASigninBean bean,String meetingId,String startTime){
		Result result=null;
		if(bean.getId()==null){
			bean.setId(meetingId+startTime);
			bean.setMeetingId(meetingId);
			bean.setStartTime(startTime);
			result=this.addBean(bean);
		}else{
			result=this.updateBean(bean);
		}
		return result;
	}
	
	/**
	 * 例会根据今天不同，而变化的开会时间
	 */
	public List<Date> getMeetingTime(String date,String meetingId,int type){
		List<Date> meetingTime=new ArrayList();
		List param = new ArrayList();
		String sql=null;
		if(type==2){
			sql="  select dateadd(wk,DATEDIFF(wk,bean.startTime,convert(datetime,?,120)),bean.startTime) as startTime ,dateadd(wk,DATEDIFF(wk,bean.endTime,convert(datetime,?,120)),bean.endTime) as endTime  from OAMeeting bean where 1=1 and  regularMeeting =2  and id=?  ";
		}else if(type==3){
			sql = "  select dateadd(mm,DATEDIFF(mm,bean.startTime,convert(datetime,?,120)),bean.startTime)  as startTime ,dateadd(mm,DATEDIFF(mm,bean.endTime,convert(datetime,?,120)),bean.endTime)  as endTime  from OAMeeting bean where 1=1 and  regularMeeting =3 and id=?  ";
		}else{
			sql = "  select  dateadd(dd,(DATEDIFF(dd,bean.startTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.startTime)  as startTime ,dateadd(dd,(DATEDIFF(dd,bean.endTime,convert(datetime,?,120))/bean.regularDay)*bean.regularDay,bean.endTime)  as endTime  from OAMeeting bean where 1=1 and  regularMeeting =1 and id=?   ";
		}
		param.add(date);
		param.add(date);
		param.add(meetingId);
		Result result=sqlList(sql, param,  1,1, true);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<Object[]> temp=(List<Object[]>)result.getRetVal();
			meetingTime.add(new Date(((java.sql.Timestamp)temp.get(0)[0]).getTime()));
			meetingTime.add(new Date(((java.sql.Timestamp)temp.get(0)[1]).getTime()));
		}
		return meetingTime;
	}
	
	public Result meetRoomUsing(String dateTime){		
		//查询会议数据
		String sql = "select boardroomId,toastmasterName,title,convert(varchar,startTime,120) as startTime ,convert(varchar,endTime,120) as endTime " +
				"from oameeting where convert(varchar,endTime,120) > convert(varchar,getdate(),120) and isnull(regularMeeting,'')='' and isnull(regularend,'')='' and convert(varchar,startTime,23) = ?";		
  		ArrayList param = new ArrayList();
  		param.add(dateTime);
  		return sqlList(sql, param);
	}   
	
	public Result meetGularUsing(){		
		//查询例会数据
		String sql = "select boardroomId,toastmasterName,title,convert(varchar,startTime,120) as startTime ,convert(varchar,endTime,120) as endTime, regularMeeting,regularDay,regularend " +
				" from oameeting where '1'=? and isnull(regularMeeting,'') <> '' and isnull(regularDay,'') <> ''";	
  		ArrayList param = new ArrayList();
  		param.add("1");
  		return sqlList(sql, param);
	}  
	
	public Result getRoom(){		
		//查询会议shi数据
		String sql = "from OABoardroomBean where 1=?";		
  		ArrayList param = new ArrayList();
  		param.add(1);
  		return list(sql, param);
	}   
}
