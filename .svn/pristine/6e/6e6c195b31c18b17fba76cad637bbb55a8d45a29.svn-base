package com.menyi.aio.web.alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBManager;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;

public class TimeNoteSetMgt extends DBManager
{
	/**
	 * 查询定时通知列表
	 * @param enu
	 * @param isAvail
	 * @return
	 */
	public Result list(final List enu, final List isAvail,final String alertId,final String ModuleType)
	{
		final Result rs=new Result();
		int regCode=DBUtil.execute(new IfDB(){
			public int exec(Session session)
			{
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException
					{ 
						try
						{
							final List alertSetList=new LinkedList();
							StringBuffer sql=new StringBuffer("select a.sqlDefineName,a.timeSet,a.Type,a.status,isnull(b.employeeName,''),a.id from tblTimingMsg a left join tblTimingMsgDet b on a.id=b.f_ref ");
							sql.append((null==ModuleType||ModuleType.equals(""))?"":" where (a.model='"+ModuleType+"' or a.model='0')");
							PreparedStatement ps=conn.prepareStatement(sql.toString());
							ResultSet rss=ps.executeQuery();
							ArrayList list=new ArrayList();
							while(rss.next()){
								String []str=new String[6];
								str[0]=rss.getString(1);
								str[1]=rss.getString(2);
								str[2]=rss.getString(3);
								str[3]=rss.getString(4);
								str[4]=rss.getString(5);
								str[5]=rss.getString(6);
								list.add(str);
							}
							
							HashMap<String,String[]> map=new HashMap<String, String[]>();
							
							for(int i=0;i<list.size();i++){
								String []str=(String[])list.get(i);
								
								if(map.containsKey(str[0])){
									String []str2=(String [])map.get(str[0]);
									if(str2[4]!=null&&str2[4].length()>0)
										str2[4]=str2[4]+";"+str[4];
								}else{
									map.put(str[0], str);
								}
							}
							
							rs.setRetVal(map);
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException e)
						{
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
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
	 * 启用或者禁用定时通知模板
	 * @param keyIds
	 * @param status
	 */
	public Result updateStatus(final String keyIds,final int status)
	{
		final Result rs=new Result();
		int retCode=DBUtil.execute(new IfDB(){
			public int exec(Session session)
			{
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException
					{
						try
						{
							//更改预警状态
							String sql="update tblTimingMsg set Status=? where id in ("+keyIds+")";
							PreparedStatement ps=conn.prepareStatement(sql);
							ps.setInt(1, status);
							int row=ps.executeUpdate();
							rs.setRetCode(row>0?ErrorCanst.DEFAULT_SUCCESS:ErrorCanst.DEFAULT_FAILURE);
						} catch (SQLException e)
						{
							e.printStackTrace();
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
	 * 更改预警方式
	 * @return
	 */
	public Result updateParMode(final String alertId,final String modes)
	{
		final Result rs=new Result();
		int retCode=DBUtil.execute(new IfDB(){
			public int exec(Session session)
			{
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException
					{
						try
						{
							String sql="update tblTimingMsg set type=? where id=?";
							PreparedStatement pre=conn.prepareStatement(sql);
							pre.setString(1, modes);
							pre.setString(2, alertId);
							int row=pre.executeUpdate();
							rs.setRetCode(row>0?ErrorCanst.DEFAULT_SUCCESS:ErrorCanst.DEFAULT_FAILURE);
						} catch (SQLException e)
						{
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}
	/**
	 * 更改预警时间
	 * @return
	 */
	public Result updateTime(final String alertId,final String time)
	{
		final Result rs=new Result();
		int retCode=DBUtil.execute(new IfDB(){
			public int exec(Session session)
			{
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException
					{
						try
						{
							String sql="update tblTimingMsg set timeSet=? where id=?";
							PreparedStatement pre=conn.prepareStatement(sql);
							pre.setString(1, time);
							pre.setString(2, alertId);
							int row=pre.executeUpdate();
							rs.setRetCode(row>0?ErrorCanst.DEFAULT_SUCCESS:ErrorCanst.DEFAULT_FAILURE);
						} catch (SQLException e)
						{
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}
	/**
	 * 列出用户
	 * @return
	 */
	public Result listUser(final String keyId)
	{
		final Result rs=new Result();
		int retCode=DBUtil.execute(new IfDB(){
			public int exec(Session session)
			{
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException
					{
						List empList=new ArrayList();
						final String sql="select a.id,a.EmpFullName,b.DeptFullName,(select count(d.employeeId) from tblTimingMsg c ,tblTimingMsgDet d where c.id=d.f_ref "+
								"and c.id='"+keyId+"' and a.id=d.employeeId) from tblEmployee a left join tblDepartment b on a.DepartmentCode=b.classCode where 1=1 and a.statusId!=-1 and a.openFlag='1'";
						PreparedStatement pre=conn.prepareStatement(sql);
						ResultSet rss=pre.executeQuery();
						while(rss.next())
						{
							Object[] emps=new Object[4];
							emps[0]=rss.getString(1);
							emps[1]=rss.getString(2);
							emps[2]=rss.getString(3);
							emps[3]=rss.getString(4);
							empList.add(emps);
						}
						rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						rs.setRetVal(empList);
					}
				});
				return rs.getRetCode();
			}
		});
		rs.setRetCode(retCode);
		return rs;
	}
	
	public Result updateParUser(final String alertId,final String[] empids)
	{
		final Result rs=new Result();
		int retCode=DBUtil.execute(new IfDB(){
			public int exec(Session session){
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException{
						//先删除原有的通知对象
						String asql="delete from tblTimingMsgDet where f_ref='"+alertId+"'";
						Statement st=conn.createStatement();
						st.executeUpdate(asql);
						if(empids!=null && empids.length>0){
							String emps="";
							for(int i=0;i<empids.length;i++){
								emps+="'"+empids[i]+"',";
							}
							String sql="";
							if(emps.length()>0){
								emps=emps.substring(0,emps.length()-1);
								sql="select id,empFullName,Mobile,Email from tblEmployee where id in ("+emps+")";
								ResultSet rst=st.executeQuery(sql);
								ArrayList list=new ArrayList();
								while(rst.next()){
									String [] obj=new String[4];
									obj[0]=rst.getString(1);
									obj[1]=rst.getString(2);
									obj[2]=rst.getString(3);
									obj[3]=rst.getString(4);
									list.add(obj);
								}
								rst.close();
								sql="insert into tblTimingMsgDet (id,f_ref,email,phoneNumber,employeeId,employeeName) values (?,?,?,?,?,?)";
								PreparedStatement ps=conn.prepareStatement(sql);
								for(int i=0;i<list.size();i++){
									String []obj=(String[])list.get(i);
									ps.setString(1, IDGenerater.getId());
									ps.setString(2, alertId);
									ps.setString(3, obj[3]);
									ps.setString(4, obj[2]);
									ps.setString(5, obj[0]);
									ps.setString(6, obj[1]);
									ps.executeUpdate();
								}
							}
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
	 * 修改预警模板参数设置
	 * @param alertId
	 * @param useParam
	 * @return
	 */
	public Result updateUseParam(final String alertId,final String useParam)
	{
		final Result rs=new Result();
		int retCode=DBUtil.execute(new IfDB(){
			public int exec(Session session)
			{
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException
					{
						try
						{
							String sql="update tblSysAlert set UseParam=? where id=?";
							PreparedStatement pre=conn.prepareStatement(sql);
							if(null==useParam||"".equals(useParam)||useParam.matches("[0-9]*"))
							{
								//开始更新参数值								
								pre.setString(1, useParam);
								pre.setString(2, alertId);
								int row=pre.executeUpdate();
								rs.setRetCode(row>0?ErrorCanst.DEFAULT_SUCCESS:ErrorCanst.DEFAULT_FAILURE);
							}
							else
							{
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							}
						} catch (Exception e)
						{
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}
	
	/**
	 * 查询定时通知的通知方式
	 * @param enu
	 * @param isAvail
	 * @return
	 */
	public Result timeNoteModel(final String alertId )
	{
		final Result rs=new Result();
		int regCode=DBUtil.execute(new IfDB(){
			public int exec(Session session)
			{
				session.doWork(new Work(){
					public void execute(Connection conn) throws SQLException
					{ 
						try
						{
							Statement st=conn.createStatement();
							ResultSet rst=st.executeQuery("select sqlDefineName,isnull(type,''),timeSet from tblTimingMsg where id='"+alertId+"'");
							if(rst.next()){
								rs.setRetVal(new String []{rst.getString(1),rst.getString(2),rst.getString(3)});
							}
							
							rs.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
						} catch (SQLException e)
						{
							e.printStackTrace();
							rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							return;
						}
					}
				});
				return rs.getRetCode();
			}
		});
		return rs;
	}
}
