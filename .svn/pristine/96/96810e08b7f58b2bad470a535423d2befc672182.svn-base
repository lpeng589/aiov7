package com.menyi.web.util;

import java.sql.*;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.web.customize.CustomizePYM;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * TextBoxList控件获取需要的数据
 * @author zhou
 *
 */
public class TextBoxUtil {
	
	/**
	 * 根据所有职员拼接成textBoxList控件需要的信息
	 * @param userType       职员类型（空代表所有保存在内存中的职员，openSystem代表可以登陆系统的职员）
	 * @return
	 */
	public ArrayList<String[]> getUsersValues(String userType){
		ArrayList<String[]> list = new ArrayList<String[]>();
		OnlineUser[] userList = (OnlineUser[]) OnlineUserInfo.cloneMap().values().toArray(new OnlineUser[0]);
		for (OnlineUser user : userList) {
			if(!"-1".equals(user.statusId)){
				String[] str = new String[4];
				str[0] = user.getId();						//隐藏的值 			如：职员ID
				str[1] = user.getName();					//页面显示的值			如：职员名称
				str[2] = user.pingying;
				str[3] = "";
				if(userType != null && "openSystem".equals(userType)){
					if(user.sysName != null && !"".equals(user.sysName)){
						list.add(str);
					}
				}else{
					list.add(str);
				}
			}			
		}
		return list;
	}
	
	public ArrayList<String[]> getUsersValues(){
		return getUsersValues("");
	}
	
	/**
	 * 获取全部部门的数据
	 * @return
	 */
	public ArrayList<String[]> getDeptValues(){
		ArrayList<String[]> list = new ArrayList<String[]>();
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select classCode,DeptFullName,DeptCode from tblDepartment where statusId!=-1";
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql.toString());
							List deptList = new ArrayList<String[]>();
							while(rset.next()){
								String[] str = new String[4];
								str[0] = rset.getString("classCode");
								str[1] = rset.getString("DeptFullName");
								str[2] = rset.getString("DeptCode");
								str[3] = CustomizePYM.getFirstLetter(rset.getString("DeptFullName"))+","+ChineseSpelling.getSelling(rset.getString("DeptFullName"));;
								deptList.add(str);
							}
							result.setRetVal(deptList);
						} catch (Exception e) {
							e.printStackTrace();
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		if(retCode == ErrorCanst.DEFAULT_SUCCESS){
			list = (ArrayList<String[]>)result.getRetVal();
		}
		return list;
	}
	
	/**
	 * 查询存在email的职员数据（通讯录）
	 * @return
	 */
	public ArrayList<String[]> getUsersEmailValues(){
		ArrayList<String[]> list = new ArrayList<String[]>();
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,empFullName,email,empNumber from tblEmployee where statusId=0 and email is not null and email!=''";
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql.toString());
							List empList = new ArrayList();
							while(rset.next()){
								String[] str = new String[4];
								str[0] = rset.getString("id");
								str[1] = rset.getString("empFullName")+"&lt;"+rset.getString("email")+"&gt;";
								str[2] = CustomizePYM.getFirstLetter(rset.getString("empFullName"))+","+ChineseSpelling.getSelling(rset.getString("empFullName"));
								str[3] = rset.getString("empNumber");
								empList.add(str);
							}
							result.setRetVal(empList);
						} catch (Exception e) {
							e.printStackTrace();
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		if(retCode == ErrorCanst.DEFAULT_SUCCESS){
			list = (ArrayList<String[]>)result.getRetVal();
		}
		return list;
	}
	
	/**
	 * 查询客户联系人
	 * @return
	 */
	public ArrayList<String[]> getClientValues(){
		ArrayList<String[]> list = new ArrayList<String[]>();
		final Result result = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "select id,clientEmail,userName from CRMClientInfoDet where clientEmail is not null and clientEmail != ''";
							Statement st = conn.createStatement();
							ResultSet rset = st.executeQuery(sql.toString());
							List clientList = new ArrayList();
							while(rset.next()){
								String[] str = new String[3];
								str[0] = rset.getString("id");
								str[1] = rset.getString("userName")+"&lt;"+rset.getString("clientEmail")+"&gt;";
								str[2] = CustomizePYM.getFirstLetter(rset.getString("userName"))+","+ChineseSpelling.getSelling(rset.getString("userName"));
								clientList.add(str);
							}
							result.setRetVal(clientList);
						} catch (Exception e) {
							e.printStackTrace();
							result.setRetCode(ErrorCanst.DEFAULT_FAILURE);
						}
					}
				});
				return result.getRetCode();
			}
		});
		if(retCode == ErrorCanst.DEFAULT_SUCCESS){
			list = (ArrayList<String[]>)result.getRetVal();
		}
		return list;
	}
}
