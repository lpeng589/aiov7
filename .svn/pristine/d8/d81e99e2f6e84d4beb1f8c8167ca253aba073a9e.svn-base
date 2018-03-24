package com.menyi.msgcenter.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.JsonObject;
import com.koron.oa.bean.MessageBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.msgcenter.msgif.DepartmentItem;
import com.menyi.msgcenter.msgif.DeptInfoReq;
import com.menyi.msgcenter.msgif.EmpInfoReq;
import com.menyi.msgcenter.msgif.EmployeeItem;
import com.menyi.msgcenter.msgif.FriendItem;
import com.menyi.msgcenter.msgif.FriendReq;
import com.menyi.msgcenter.msgif.GroupItem;
import com.menyi.msgcenter.msgif.MsgHeader;
import com.menyi.web.util.AIODBManager;
import com.menyi.web.util.AppleApnsSend;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.SystemState;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;
public class MsgMgt extends AIODBManager {
	
	/**
	 * 刷新部门内存，本方法可用于define调用
	 * @param deptClassCode
	 * @param deptName
	 */
	public static void refreshMemDept(String classCode,Connection conn) throws Exception{
		String sql="SELECT top 1 id,deptFullName,classCode,createTime,lastUpdateTime,remark,deptCode FROM tblDepartment  where  classCode = ? ";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setObject(1,classCode);
        
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {            
			DepartmentItem deptItem = new DepartmentItem();
			deptItem.deptId = rs.getString(1);
			deptItem.name = rs.getString(2);
			deptItem.operateType = MsgHeader.OBJ_INIT;
			deptItem.classCode = rs.getString(3);
			deptItem.deptCode = rs.getString(7);
			deptItem.remark = rs.getString(6);
			deptItem.dataTime = rs.getString(5);
			deptItem.createTime = rs.getString(4);
			MSGConnectCenter.departmentMap.put(deptItem.deptId, deptItem);
			MSGConnectCenter.departmentMap2.put(deptItem.classCode, deptItem);
			
			HashMap<String,String> oldDeptMap = (HashMap)BaseEnv.servletContext.getAttribute("deptMap");
			oldDeptMap.put(classCode, deptItem.name);
		}		 
				
	}
	
	public static Result refreshMemDept(final String classCode){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							refreshMemDept(classCode,conn);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :", ex);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
	
	/**
	 * 专供define调用的，刷新职员内存
	 * @param conn
	 * @param empIds
	 * @throws Exception
	 */
	public static void refreshMemEmployee(Connection conn,String empIds) throws Exception{
		
		empIds = empIds.trim();
		if(empIds.startsWith("select")){ //这是通过传入sql语句 来查询哪些职员要停用
			PreparedStatement pstmt1 = conn.prepareStatement(empIds);
	        ResultSet rset =pstmt1.executeQuery();
	        while(rset.next()){
	        	refreshMemEmp(conn,rset.getString(1)); 
	        }
		}else{
			refreshMemEmp(conn,empIds);
		}
		
		
	}
	
	/**
	 * 刷新用户信息，可用于define调用
	 * @param conn
	 * @param empIds
	 * @throws Exception
	 */
	public static void refreshMemEmp(Connection conn,String ...empIds) throws Exception{
		String time = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		
		String ids="";
		for(int i=0;i<empIds.length;i++){
			if(i+1==empIds.length){
				ids+="'"+empIds[i]+"'";
			}else{
				ids+="'"+empIds[i]+"'"+",";
			}
		}
		String sql="SELECT te.id,te.EmpFullName,te.TitleID,te.Email,te.lastUpdateTime,te.BornDate,te.DepartmentCode,te.Tel, te.Mobile,"+
					" te.Gender,te.imgeNum,te.[signature],te.createTime,te.EmpNumber,te.photo,te.EmpName FROM tblEmployee  te  where  id in ("+ids+") ";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
        
        ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			EmployeeItem empItem = new EmployeeItem();
			empItem.userId = rs.getString(1);
			empItem.name = rs.getString(2);
			empItem.shortName = (String) rs.getString(16);
			if (null == empItem.shortName) {
				empItem.shortName = "";
			}
			empItem.operateType = MsgHeader.OBJ_ADD;
			String duty = GlobalsTool.getEnumerationItemsDisplay("duty",
					rs.getString(3), "zh_CN");
			empItem.titleID = duty;
			empItem.email = rs.getString(4);
			empItem.dataTime = time;
			empItem.birth = rs.getString(6);
			empItem.departmentCode = rs.getString(7);
			empItem.telPhone = rs.getString(8);
			empItem.mobile = rs.getString(9);
			empItem.sEmpNumber = rs.getString(14);
			empItem.createTime = rs.getString(13);
			empItem.groupTime = time;
			empItem.friendTime = time;
			String sex = rs.getString(10);
			if ("".equals(sex) || sex == null || "null".equals(sex)) {
				empItem.sex = 0;
			} else if ("1".equals(sex)) {
				empItem.sex = 1;
			} else {
				empItem.sex = 2;
			}
			String sPhoto = rs.getString(15);
			if (null == sPhoto || "".equals(sPhoto) || "null".equals(sPhoto)) {
				empItem.imageNum = 0;
			} else {
				empItem.imageNum = -1;
			}

			String content = (String) rs.getString(12);
			if ("".equals(content) || content == null || "null".equals(content)) {
				content = "";
			}
			empItem.signContext = content;
			empItem.loginStatus = EmployeeItem.OFFLINE;
			empItem.loginType = EmployeeItem.CS_LOGIN;
			MSGConnectCenter.employeeMap.put(empItem.userId, empItem);
			
			//更新用户最后更新时间
			String strsql="UPDATE tblEmployee SET lastUpdateTime = ? WHERE id = ?";
			PreparedStatement pstmt1 = conn.prepareStatement(strsql);
			pstmt1.setString(1, ""+BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss)+"");
			pstmt1.setString(2, empItem.userId);
	        pstmt1.executeUpdate();
	        
	        //修改用户部门情况下，需要删除所有我的，除了该部门之外的信息
			strsql="DELETE FROM OAMessage WHERE OperType='dept' AND receive= ?  and relationId!= ?";
			pstmt1 = conn.prepareStatement(strsql);
			pstmt1.setString(1, empItem.userId);
			pstmt1.setString(2, empItem.departmentCode);
	        pstmt1.executeUpdate();
	        strsql="DELETE FROM OAMessage2 WHERE OperType='dept' AND receive= ?  and relationId!= ?";
			pstmt1 = conn.prepareStatement(strsql);
			pstmt1.setString(1, empItem.userId);
			pstmt1.setString(2, empItem.departmentCode);
	        pstmt1.executeUpdate();
	        
		}
		
		
		//更新用户在线信息
		final HashMap oldOnlineUserMap = OnlineUserInfo.cloneMap();
        OnlineUserInfo.clearUser();
		
		sql = "select a.id,empFullName,DeptFullName,DepartmentCode,TitleID,a.statusId,a.SysName,a.photo,a.empNumber,a.responsibility,a.gender, isnull(a.isPublic,0) isPublic from tblEmployee a "
				+ "left join tblDepartment b on a.DepartmentCode= b.classCode ";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(sql);
		while (rset.next()) {
			OnlineUser ou = (OnlineUser) oldOnlineUserMap.get(rset.getString("id"));
			String osession = "";
			Date odate = new Date();
			odate.setTime(0l);
			if (ou != null) {
				osession = ou.session;
				odate = ou.getActiveDate();
			}
			String photo = rset.getString("photo");
			if (photo != null && photo.contains(":")) {
				photo = photo.split(":")[0];
			}
			OnlineUserInfo.putUser(rset.getString("id"), rset.getString("empFullName"), rset.getString("DepartmentCode"), rset.getString("DeptFullName"), osession, odate, rset.getString("TitleID"),
					rset.getString("statusId"), rset.getString("sysName"), photo, rset.getString("empNumber"), rset.getString("responsibility"), rset.getString("gender"), rset.getInt("isPublic"));
		}
		
	}
	/**
	 * 删除或停用用户，可作为define调用
	 * @param objId
	 */
	public static void delEmp(Connection conn,String objId) throws Exception{
		objId = objId.trim();
		if(objId.startsWith("select")){ //这是通过传入sql语句 来查询哪些职员要停用
			PreparedStatement pstmt1 = conn.prepareStatement(objId);
	        ResultSet rset =pstmt1.executeQuery();
	        while(rset.next()){
	        	delEmployee(conn,rset.getString(1));
	        }
		}else{
			delEmployee(conn,objId);
		}
	}
	
	/**
	 * 删除或停用用户
	 * @param objId
	 */
	public static void delEmployee(Connection conn,String ...objId) throws Exception{
		String time = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		for (String id : objId) {
			// 删除部门消息
			String strsql="DELETE FROM OAMessage WHERE OperType='dept' AND receive= ?  ";
			PreparedStatement pstmt1 = conn.prepareStatement(strsql);
			pstmt1.setString(1, id);
	        pstmt1.executeUpdate();
	        strsql="DELETE FROM OAMessage2 WHERE OperType='dept' AND receive= ? ";
			pstmt1 = conn.prepareStatement(strsql);
			pstmt1.setString(1, id);
	        pstmt1.executeUpdate();

			// 被删除的用户还在线，就让它掉线
			MSGSession ms = (MSGSession) MSGConnectCenter.sessionPool.get(id);
			if (null != ms) {
				ms.connect.close("被删除的用户还在线主动关闭");
			}

			// 群组，如果是成员，就删除，如果是群主，就把群主权限转移给admin
			Iterator iter = MSGConnectCenter.groupMap.values().iterator();
			while (iter.hasNext()) {
				GroupItem group = (GroupItem) iter.next();

				// 群主
				FriendItem itemAdmin = null;
				if (group.createBy.equals(id)) {
					group.createBy = "1";
					group.dataTime = time;

					EmployeeItem admin = MSGConnectCenter.employeeMap.get("1");
					admin.dataTime = time;
					if (null == itemAdmin) {
						itemAdmin = new FriendItem();
						itemAdmin.userId = "1";
						group.userList.add(itemAdmin);
					}
					group.userCount = (short) group.userList.size();
				}

				// 成员
				FriendItem itemUser = null;
				for (int i = 0; i < group.userList.size(); i++) {
					FriendItem item = group.userList.get(i);
					if (item.userId.equals(id)) {
						itemUser = item;
						break;
					}
				}
				if (null != itemUser) {
					group.userList.remove(itemUser);
					group.dataTime = time;
					//修改组信息
					List<String> param=new ArrayList<String>();
					strsql="update msgGroup set groupName=?,groupRemark=?,lastUpdateTime=? where id= ? ";
					pstmt1 = conn.prepareStatement(strsql);
					pstmt1.setString(1, group.groupName);
					pstmt1.setString(2, group.remark);
					pstmt1.setString(3, group.dataTime);
					pstmt1.setString(4, group.groupId);
			        pstmt1.executeUpdate();
					
			        //删除所有组员
			        strsql="delete from  msgGroupUser WHERE f_ref= ?";
			        pstmt1 = conn.prepareStatement(strsql);
					pstmt1.setString(1, group.groupId);
					pstmt1.executeUpdate();
					//重新添加所有组员
                	for(int i=0;i<group.userList.size();i++){
                		FriendItem fItem=group.userList.get(i);
                		strsql= "insert into msgGroupUser (id,userID,f_ref) values(?,?,?)" ;
                		pstmt1 = conn.prepareStatement(strsql);
                		pstmt1.setString(1, IDGenerater.getId());
    					pstmt1.setString(2, fItem.userId);
    					pstmt1.setString(3, group.groupId);
    			        pstmt1.executeUpdate();
                	}
				}
			}
			// 部门，不用处理
		}
		
		//更新用户在线信息
		final HashMap oldOnlineUserMap = OnlineUserInfo.cloneMap();
        OnlineUserInfo.clearUser();
		
		String sql = "select a.id,empFullName,DeptFullName,DepartmentCode,TitleID,a.statusId,a.SysName,a.photo,a.empNumber,a.responsibility,a.gender, isnull(a.isPublic,0) isPublic from tblEmployee a "
				+ "left join tblDepartment b on a.DepartmentCode= b.classCode ";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(sql);
		while (rset.next()) {
			OnlineUser ou = (OnlineUser) oldOnlineUserMap.get(rset.getString("id"));
			String osession = "";
			Date odate = new Date();
			odate.setTime(0l);
			if (ou != null) {
				osession = ou.session;
				odate = ou.getActiveDate();
			}
			String photo = rset.getString("photo");
			if (photo != null && photo.contains(":")) {
				photo = photo.split(":")[0];
			}
			OnlineUserInfo.putUser(rset.getString("id"), rset.getString("empFullName"), rset.getString("DepartmentCode"), rset.getString("DeptFullName"), osession, odate, rset.getString("TitleID"),
					rset.getString("statusId"), rset.getString("sysName"), photo, rset.getString("empNumber"), rset.getString("responsibility"), rset.getString("gender"),rset.getInt("isPublic"));
		}
	}
	/**
	 * 删除部门
	 * @param conn
	 * @param objId
	 * @throws Exception
	 */
	public static void delDept(Connection conn,String ...objId) throws Exception{
		for (String deptId : objId) {
			DepartmentItem dept = MSGConnectCenter.departmentMap.remove(deptId);
			MSGConnectCenter.departmentMap2.remove(dept.classCode);
			
			List<String> param=new ArrayList<String>();
			String sql="";
			
			sql=" DELETE FROM OAMessage WHERE OperType='dept' AND relationId= ? ";
			PreparedStatement pstmt1 = conn.prepareStatement(sql);
			pstmt1.setString(1, deptId);
	        pstmt1.executeUpdate();
		}
	}
	
	public static Result delDept(final String ...objId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							delDept(conn,objId);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :", ex);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
	
	/**
	 * 删除用户
	 * @param objId
	 * @return
	 */
	public static Result delEmployee(final String ...objId){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							delEmployee(conn,objId);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :", ex);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
	
	
	public static Result refreshMemEmp(final String ...empIds){
		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							refreshMemEmp(conn,empIds);
						} catch (Exception ex) {
							ex.printStackTrace();
							rst.setRetCode(ErrorCanst.DEFAULT_FAILURE);
							BaseEnv.log.error("Query data Error :", ex);
							return;
						}
					}
				});
				return rst.getRetCode();
			}
		});
		rst.setRetCode(retCode);
		return rst ;
	}
	
	/**
	 * 获取所有的部门
	 * @return
	 */
	public Result getAllDept(){
		List<String> param=new ArrayList<String>();
		String strsql="SELECT id,deptFullName,classCode,createTime,lastUpdateTime,remark,deptCode FROM tblDepartment  where isnull(ispublic,0)<>1 and  statusId!='-1' ORDER BY deptCode";
		return this.sqlList(strsql, param);
	}
	
	
	/**
	 * 获取系统所有用户
	 * @return
	 */
	public Result  getAllSysUser(){
		List<String> param=new ArrayList<String>();
		String strsql = "SELECT te.id,te.EmpFullName,te.TitleID,te.Email,te.lastUpdateTime,te.BornDate,te.DepartmentCode,te.Tel, te.Mobile,"+
						" te.Gender,te.imgeNum,te.[signature],te.createTime, te.EmpNumber, te.photo, te.EmpName FROM tblEmployee te  where isnull(te.ispublic,0)<>1 and  te.openflag=1 and statusId!=-1 order by te.lastUpdateTime desc ";
		return this.sqlList(strsql, param);
	}
	
	/**
	 * 获取所有的聊天群组
	 * @return
	 */
	public Result getAllmsgGroup(){
		List<String> param=new ArrayList<String>();
		String strsql="SELECT id, groupName,createBy,createTime,groupRemark,lastUpdateTime FROM msgGroup ";
		return this.sqlList(strsql, param);
		
	}
	
	/**
	 * 根据聊天群组获取该群组下的成员
	 * @return
	 */
	public Result getFriendByGroupId(String groupId){
		List<String> param=new ArrayList<String>();
		String strsql="SELECT m.userId,mg.id FROM msgGroupUser m "+
						" LEFT JOIN msgGroup mg ON m.f_ref=mg.id  WHERE mg.id=?";
		param.add(""+groupId+"");
		return this.sqlList(strsql, param);
	}
	
	/**
	 *获取所有的好友信息
	 * @return
	 */
	public Result getAllFriends(){
		List<String> param=new ArrayList<String>();
		String strsql="select * from msgFriend";
		return this.sqlList(strsql, param);
	}
	
	/**
	 * 获取所有的删除操作记录
	 * @return
	 */
	public Result getAllOperate(){
		List<String> param=new ArrayList<String>();
		String strsql="select * from msgOperate order by  operateTime desc";
		return this.sqlList(strsql, param);
	}
	
	/**
	 * 更新群组的最后修改时间
	 * @param time
	 * @param groupId
	 * @return
	 */
	public Result updateGroupTime(String time,String groupId){
		List<String> param=new ArrayList<String>();
		String sql="update msgGroup set lastupdateTime=? where Id=? ";
		param.add(""+time+"");
		param.add(""+groupId+"");
		return this.msgSql(sql, param);
	}
	
	/**
	 * 获取初始化的当前用户可以看到的群组
	 */
	public List<GroupItem> getInitGroup(String userId){
		ArrayList<GroupItem> groupList = new ArrayList();
		//获取当前用户可以看到的所有群组
		String allOwnGroup=MSGConnectCenter.empGroupMap.get(userId);
		if(allOwnGroup==null||allOwnGroup.length() ==0){
			return groupList;
		}
		Iterator iter = MSGConnectCenter.groupMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			GroupItem gItem = (GroupItem) entry.getValue();
			String groupId=","+gItem.groupId+",";
			if(allOwnGroup.contains(groupId)){
				gItem.operateType=MsgHeader.OBJ_INIT;
				groupList.add(gItem);
			}
		}
		return groupList;
	}
	
	/**
	 * 获取最新的群组信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<GroupItem> getNewGroup(Date groupTime,String userId){
		
		ArrayList<GroupItem> groupList = new ArrayList();
		//获取当前用户可以看到的所有群组
		String allOwnGroup=MSGConnectCenter.empGroupMap.get(userId);
		Iterator iter = MSGConnectCenter.groupMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			GroupItem gItem = (GroupItem) entry.getValue();
			Date gTime=MsgHeader.StringToDate(gItem.dataTime);  //群组最后修改时间
			Date cTime=MsgHeader.StringToDate(gItem.createTime);  //群组创建时间
			String groupId=","+gItem.groupId+",";
			if(allOwnGroup.contains(groupId)){
				if(null!=cTime && groupTime.before(cTime) ){  //增加
					gItem.operateType=MsgHeader.OBJ_ADD;
				}else if(null!=cTime && !groupTime.before(cTime) && null!=gTime && groupTime.before(gTime)){  //修改
					gItem.operateType=MsgHeader.OBJ_UPDATE;
				}else{
					continue;
				}
				groupList.add(gItem);
			}
		}
		
		//获取当前用户解散的群组、被剔除的群组、自动退出的群组
		Map operateMap =MSGConnectCenter.operateMap;
		List<String[]> operMap=(List<String[]> ) operateMap.get("group");
		if(operMap!=null){
			for(int i=0;i<operMap.size();i++){
				GroupItem gItem2 = new GroupItem();
				String[] strList=operMap.get(i);
				String groupId=strList[0];
				Date opTime=MsgHeader.StringToDate(strList[1]);   //删除操作时间
				String theUser=strList[2];  //从群组中退出的人（包含被剔除的）
				String createUser=strList[3]; //群组创建人
				
				if(null!=opTime && opTime.after(groupTime) && (theUser.equals(userId) || createUser.equals(userId)) ){ 
					if(userId.equals(theUser) && createUser.length()>0){  //1.用户被剔除的群组，2.群主解散的群组
						gItem2.operateType=MsgHeader.OBJ_DELETE;
						gItem2.groupId=groupId;
						gItem2.createBy=createUser;
						gItem2.dataTime=strList[1];
					}else if (userId.equals(theUser) && createUser.length()==0){ //用户自动退出的群组
						gItem2.operateType=MsgHeader.OBJ_DELETE;
						gItem2.groupId=groupId;
						gItem2.dataTime=strList[1];
					}else if (theUser.length()==0 && userId.equals(createUser)){  //用户解散的群组
						gItem2.operateType=MsgHeader.OBJ_DELETE;
						gItem2.groupId=groupId;
						gItem2.dataTime=strList[1];
					}else{
						continue;
					}
					groupList.add(gItem2);
				}else{
					continue;
				}
			}
		}
		
		return groupList;
	}
	
	/**
	 * 根据登录用户Id获取用户可以看到的所有群组
	 * @param userId
	 * @return
	 */
	public String getOwnGroup(String userId){
		List<String> param=new ArrayList<String>();
		String sql=" SELECT f_ref FROM msgGroupUser m  LEFT JOIN msgGroup t ON" +
				" m.f_ref=t.id WHERE m.userID=? ";
		param.add(""+userId+"");
		Result rs=this.sqlList(sql, param);
		List gList=(List) rs.retVal;
		String allGroup=",";
		for(int i=0;i<gList.size();i++){
			Object[] obj = (Object[]) gList.get(i) ;
			allGroup+=obj[0]+",";
		}
		return allGroup;
	}
	
	/**
	 * 获取最新的部门信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DepartmentItem> getNewDept(String deTime){
		ArrayList<DepartmentItem> deptList = new ArrayList();
		Date deptTime=MsgHeader.StringToDate(deTime);
		boolean flag=this.existSameDeptCode(deTime);  //判断删除操作表中是否存在和部门表中相同classCode的删除组的记录
		if(flag==true){
			this.getInitData("dept");
			deptList.addAll(MSGConnectCenter.departmentMap.values());
			return deptList;
		}
		
		
		Map map =MSGConnectCenter.departmentMap;
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			DepartmentItem deptItem = (DepartmentItem) entry.getValue();
			Date dTime=MsgHeader.StringToDate(deptItem.dataTime);
			Date cTime=MsgHeader.StringToDate(deptItem.createTime);
			if(null!=cTime && deptTime.before(cTime)){  //增加部门
				deptItem.operateType=MsgHeader.OBJ_ADD;
			}else if(null!=cTime && !deptTime.before(cTime) && null!=dTime && deptTime.before(dTime)){  //修改部门
				deptItem.operateType=MsgHeader.OBJ_UPDATE;
			}else{
				continue;
			}
			deptList.add(deptItem);
		}
		
		//获取删除的部门
		Map operateMap =MSGConnectCenter.operateMap;
		List<String[]> operMap=(List<String[]> ) operateMap.get("dept");
		if(operMap!=null){
			for(int i=0;i<operMap.size();i++){
				DepartmentItem deptItem2 = new DepartmentItem();
				String[] strList=operMap.get(i);
				Date opTime=MsgHeader.StringToDate(strList[1]);
				if(null!=opTime && opTime.after(deptTime)){  //判断是否是删除
					deptItem2.operateType=2;
					deptItem2.classCode=strList[0];
					deptItem2.dataTime=strList[1];
				}else{
					continue;
				}
				deptList.add(deptItem2);
			}
		}
		return deptList;
	}
	
	/**
	 * 获取最新的用户信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EmployeeItem> getNewUser(Date empTime){
		ArrayList<EmployeeItem> empList = new ArrayList();

		//获取删除的用户
		Map operateMap =MSGConnectCenter.operateMap;
		List<String[]> operMap = (List<String[]>)operateMap.get("employee");
		if(operMap!=null){
			for(int i=0;i<operMap.size();i++){
				EmployeeItem empItem2 = new EmployeeItem();
				String[] strList=operMap.get(i);
				Date opTime=MsgHeader.StringToDate(strList[1]);
				if(null!=opTime && opTime.after(empTime)){  //判断是否是删除
					empItem2.operateType=MsgHeader.OBJ_DELETE;
					empItem2.userId=strList[0];
					empItem2.dataTime=strList[1];
				}else{
					continue;
				}
				empList.add(empItem2);
			}
		}
		
		Map map =MSGConnectCenter.employeeMap;
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			EmployeeItem empItem = (EmployeeItem) entry.getValue();
			Date eTime=MsgHeader.StringToDate(empItem.dataTime);
			Date cTime=MsgHeader.StringToDate(empItem.createTime);
			if(null!=cTime && empTime.before(cTime)){  //增加用户
				empItem.operateType=MsgHeader.OBJ_ADD;
			}else if(null!=cTime && !empTime.before(cTime) && null!=eTime && empTime.before(eTime)){  //修改用户
				empItem.operateType=MsgHeader.OBJ_UPDATE;
			}else{
				continue;
			}
			empList.add(empItem);
		}

    	Collections.sort(empList, new SortDBTable()) ;
		
		return empList;
	}
    
    /**
	* EmployeeItem 按dataTime排序
	*/
	public class SortDBTable implements Comparator {
		public int compare(Object o1, Object o2) {
			EmployeeItem emp1 = (EmployeeItem) o1;
			EmployeeItem emp2 = (EmployeeItem) o2;
			
			if(null == emp1 || null == emp2){
				return 0 ;
			}

			Date t1 = MsgHeader.StringToDate(emp1.dataTime);
			Date t2 = MsgHeader.StringToDate(emp2.dataTime);

			return t1.compareTo(t2);
		}
	}

	/**
	 * 获取最新的lasttime
	 * @param tableName
	 * @param type
	 * @return
	 */
	public String getNewTime(String tableName,String type){
		String t1="";
		String t2="";
		Date time1=null;
		Date time2=null;
		List<String> param=new ArrayList<String>();
		String sql="";
		if("employee".equals(type)){
			sql="SELECT top 1(lastUpdateTime) FROM "+tableName+"  where openFlag='1' ORDER BY lastUpdateTime DESC ";
		}else{
			sql="SELECT top 1(lastUpdateTime) FROM "+tableName+"  ORDER BY lastUpdateTime DESC ";
		}
		Result rs=this.sqlList(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			List theList= (List) rs.retVal;
			if(theList.size()>0){
				Object[] obj = (Object[]) theList.get(0) ;
				t1=(String)obj[0];
				time1=MsgHeader.StringToDate(t1);
			}else{
				t1="";
			}
		}
		String sql2="SELECT TOP 1(operateTime) FROM msgOperate  WHERE position='"+type+"' ORDER BY operateTime DESC ";
		Result rs2=this.sqlList(sql2, param);
		if(rs2.retCode==ErrorCanst.DEFAULT_SUCCESS ){
			List theList2= (List) rs2.retVal;
			if(theList2.size()>0){
				Object[] obj2 = (Object[]) theList2.get(0) ;
				t2=(String)obj2[0];
				time2=MsgHeader.StringToDate(t2);
			}else{
				t2="";
			}
		}
		if("".equals(t1) && !"".equals(t2)){
			return t2;
		}
		if("".equals(t2) && !"".equals(t1)){
			return t1;
		}
		if(time1.after(time2))
			return t1;
		else
			return t2;
	}
	
	/**
	 * 分别获取部门、用户、职员的初始化数据
	 * @param type
	 */
	public void getInitData(String type){
		if("emp".equals(type)){
			Map map =MSGConnectCenter.employeeMap;
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				EmployeeItem empItem = (EmployeeItem) entry.getValue();
				empItem.operateType=MsgHeader.OBJ_INIT;
			}
		}else if("dept".equals(type)){
			Map map =MSGConnectCenter.departmentMap;
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				DepartmentItem deptItem = (DepartmentItem) entry.getValue();
				deptItem.operateType=MsgHeader.OBJ_INIT;
			}
		}else if("group".equals(type)){
			Map map =MSGConnectCenter.groupMap;
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				GroupItem gItem = (GroupItem) entry.getValue();
				gItem.operateType=MsgHeader.OBJ_INIT;
			}
		}
	}
	
	/**
	 * 添加群组
	 * @return
	 */
	public Result addGroup(GroupItem gItem){
		List<String> param=new ArrayList<String>();
		String sql="insert into msgGroup(id,groupName,createBy,createTime,groupRemark,lastUpdateTime)values(?,?,?,?,?,?)";
		param.add(""+gItem.groupId+"");
		param.add(""+gItem.groupName+"");
		param.add(""+gItem.createBy+"");
		param.add(""+gItem.createTime+"");
		param.add(""+gItem.remark+"");
		param.add(""+gItem.dataTime+"");
		return this.msgSql(sql, param);
	}
	/**
	 * 删除群组
	 * @return
	 */
	public Result delteteGroup(final String gId) {

		final Result rst = new Result();
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "delete from msgGroup where id=? ";
							PreparedStatement pstmt = conn
									.prepareStatement(sql);
							pstmt.setString(1, gId);
							pstmt.executeUpdate();

							sql = "delete from OAMessage where relationId=? ";
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, gId);
							pstmt.executeUpdate();

							sql = "delete from OAMessage2 where relationId=? ";
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, gId);
							pstmt.executeUpdate();
							
							rst.setRetCode(ErrorCanst.DEFAULT_SUCCESS);
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
	 * 向操作表中添加删除操作的记录(单条删除)
	 * @param id
	 * @param postion
	 * @param time
	 * @param obId
	 * @return
	 */
	public Result addOperate(String id,String postion,String time,String obId,String userId,String createUser){
		List<String> param=new ArrayList<String>();
		String sql="insert into msgOperate (id,position,operateTime,objId,field1,field2) values(?,?,?,?,?,?)";
		param.add(""+id+"");
		param.add(""+postion+"");
		param.add(""+time+"");
		param.add(""+obId+"");
		param.add(""+userId+"");
		param.add(""+createUser+"");
		return this.msgSql(sql, param);
	}
	
	
	/**
	 * 向操作表中批量添加移除用户记录
	 * @param id
	 * @param postion
	 * @param time
	 * @param obId
	 * @return
	 */
	 public Result addOperate(final GroupItem gItem) {
		 final String time=BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
	    	final Result rs=new Result();
	    	int retCode = DBUtil.execute(new IfDB() {
	            public int exec(Session session) {
	            	session.doWork(new Work() {
	                    public void execute(Connection conn) throws SQLException {
	                        try { 
	                        	Statement state = conn.createStatement() ;
	                        	for(int i=0;i<gItem.userList.size();i++){
	                        		FriendItem fItem=gItem.userList.get(i);
	                        		String userId=!(fItem.userId.equals(gItem.createBy))?fItem.userId:"";
	                        		String	sql = "insert into msgOperate (id,position,operateTime,objId,field1,field2) "+
	                        				" values('"+IDGenerater.getId()+"','group','"+time+"','"+gItem.groupId+"','"+userId+"','"+gItem.createBy+"')" ;
	                        		state.addBatch(sql) ;
	                        	}
	                        	state.executeBatch() ;
	                        } catch (SQLException ex) {
	                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
	                            ex.printStackTrace();
	                        }
	                    }
	                });
	            	return rs.retCode ;
	            }
	    	});
	    	rs.retCode = retCode ;
	        return rs ;
		 }
	/**
	 * 根据条件获取信息
	 * @param receive
	 * @param relationId
	 * @param time
	 * @return
	 */
	public String getMsgByReceive(String receive,String relationId,String time){
		List<String> param=new ArrayList<String>();
		String sql="select id from OAMessage where receive=? and relationId=? and createTime=?";
		param.add(""+receive+"");
		param.add(""+relationId+"");
		param.add(""+time+"");
		Result rs=this.sqlList(sql, param);
		String msgId="";
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			List list= (List) rs.retVal;
			if(list.size()>0){
				Object[] obj= (Object[]) list.get(0) ;
				msgId=(String)obj[0];
			}
		}
		return msgId;
	}
	
	/**
	 * 向群组中添加成员
	 * @param SeqId
	 * @return
	 */
	public Result addGroupUser(String Id,String userId,String groupId){
		List<String>param =new ArrayList<String>();
		
		String sql="insert into msgGroupUser(id,userID,f_ref) values(?,?,?)";
		param.add(""+Id+"");
		param.add(""+userId+"");
		param.add(""+groupId+"");
		return this.msgSql(sql, param);
	}
	
	/**
	 * 向群组中批量添加成员
	 * @param SeqId
	 * @return
	 */
	 public Result addGroupUser(final GroupItem gItem) {
    	final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try { 
                        	Statement state = conn.createStatement() ;
                        	for(int i=0;i<gItem.userList.size();i++){
                        		FriendItem fItem=gItem.userList.get(i);
                        		String	sql = "insert into msgGroupUser (id,userID,f_ref) values('"+IDGenerater.getId()+"','"+fItem.userId+"','"+gItem.groupId+"')" ;
                        		state.addBatch(sql) ;
                        	}
                        	state.executeBatch() ;
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                        }
                    }
                });
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
	 }
	/**
	 * 将组员移除群组
	 * @param SeqId
	 * @return
	 */
	public Result removeGroupUser(GroupItem gItem){
		String ids="";
		for(int i=0;i<gItem.userList.size();i++){
			FriendItem fItem=gItem.userList.get(i);
			if(i+1== gItem.userList.size()){
				ids+="'"+fItem.userId+"'";
			}else{
				ids+="'"+fItem.userId+"'"+",";
			}
		}
		
		List<String>param =new ArrayList<String>();
		String sql="delete from  msgGroupUser WHERE f_ref in (?) AND userID=?";
		param.add(""+ids+"");
		param.add(""+gItem.createBy+"");
		return this.msgSql(sql, param);
	}
	
	/**
	 * 自动退出群组
	 */
	public Result removeGroupUser(String userId,String groupId){
		List<String>param =new ArrayList<String>();
		String sql="delete from  msgGroupUser WHERE f_ref=? AND userID=?";
		param.add(""+groupId+"");
		param.add(""+userId+"");
		return this.msgSql(sql, param);
	}
	
	/**
	 * 根据群组Id修改群组公告
	 * @param gItem
	 * @return
	 */
	public Result updateGroup(GroupItem gItem){
		List<String> param=new ArrayList<String>();
		String sql="update msgGroup set groupName=?,groupRemark=?,lastUpdateTime=? where id= ? ";
		param.add(""+gItem.groupName+"");
		param.add(""+gItem.remark+"");
		param.add(""+gItem.dataTime+"");
		param.add(""+gItem.groupId+"");	
		Result ret = this.msgSql(sql, param);
		if (ret.retCode != ErrorCanst.DEFAULT_SUCCESS)
			return ret;
		ret = deleteUserByGroupId(gItem.groupId);
		if (ret.retCode != ErrorCanst.DEFAULT_SUCCESS)
			return ret;
		ret = addGroupUser(gItem);
		return ret;
		
	}
	
	/**
	 * 根据群组Id删除群组的所有组员
	 * @param groupId
	 * @return
	 */
	public Result deleteUserByGroupId(String groupId){
		List<String>param =new ArrayList<String>();
		String sql="delete from  msgGroupUser WHERE f_ref= ?";
		param.add(""+groupId+"");
		return this.msgSql(sql, param);
	}
//	/**
//	 * 添加通知消息
//	 * @param id
//	 * @param send
//	 * @param title
//	 * @param receive
//	 * @param status
//	 * @param type
//	 * @return
//	 */
//	public Result addAdvice(String id,String send,String title,String receive,String status,String type){
//		List<String> param=new ArrayList<String>();
//		String sql="insert into tbladvice(id, Send, Title, Receive,Status, type) values(?,?,?,?,?)";
//		param.add(""+id+"");
//		param.add(""+send+"");
//		param.add(""+title+"");
//		param.add(""+receive+"");
//		param.add(""+status+"");
//		param.add(""+type+"");
//		return this.msgSql(sql, param);
//	}
//	
//	/**
//	 * 将未读信息插入到已读信息表中
//	 * @param SeqId
//	 * @return
//	 */
//	public boolean receiveMsg(String Id){
//		boolean flag=false;
//		List<String>param =new ArrayList<String>();
//		String sql="insert into OAMessage2 select * from OAMessage where id =?";
//		param.add(""+Id+"");
//		this.msgSql(sql, param);
//		
//		List<String>param2 =new ArrayList<String>();
//		String sql2="delete from OAMessage where id =?";
//		param2.add(""+Id+"");
//		if(this.msgSql(sql2, param).retCode==ErrorCanst.DEFAULT_SUCCESS){
//			flag= true;
//		}
//		return flag;
//	}
	
	/**
	 * 好友操作(包含添加好友、删除好友）
	 * @param userId
	 * @param objId
	 * @param type
	 * @return
	 */
	public Result opFriend(FriendReq req,String userId){
		List<String> param=new ArrayList<String>();
		String sql="";
		if(req.type == 2){  //添加好友
			sql="INSERT INTO msgfriend(id, userid, friendId) VALUES(?,?,?)";
			param.add(""+IDGenerater.getId()+"");
			param.add(""+userId+"");
			param.add(""+req.userId+"");
		}else{ //删除好友
			sql="delete from msgfriend where userId=? and friendId=?";
			param.add(""+userId+"");
			param.add(""+req.userId+"");
		}
		return this.msgSql(sql, param);
	}
	/**
	 * 获取登录用户未读信息
	 * @param userId
	 * @return
	 */
	public Result getNoReadMsg(String userId){
		List<String> param=new ArrayList<String>();
		String sql="select o.id,o.send,o.Receive,o.Content,o.OperType,o.SendName,o.ReceiveName,o.createBy,"+
				  " o.createTime,o.lastUpdateTime,o.relationId,o.affix from OAMessage o where receive=?  ORDER BY o.createTime asc ";
		param.add(""+userId+"");
		return this.sqlList(sql, param);
		
	}
	
	
	/*更改头像和签名*/
	public Result updateEmp(EmpInfoReq req){
		List<String> param=new ArrayList<String>();
		String sql;
		if (req.eItem.imageNum >= 0) {
			sql="update tblEmployee set imgeNum=?,signature=? ,lastUpdateTime =? where id=?";
			param.add(""+req.eItem.imageNum+"");
			param.add(""+req.eItem.signContext+"");
			param.add(""+req.eItem.dataTime+"");
			param.add(""+req.eItem.userId+"");
		} else {
			sql="update tblEmployee set imgeNum=?,signature=? ,lastUpdateTime =?, photo=? where id=?";
			param.add(""+req.eItem.imageNum+"");
			param.add(""+req.eItem.signContext+"");
			param.add(""+req.eItem.dataTime+"");
			param.add(""+req.eItem.userId+".jpg");
			param.add(""+req.eItem.userId+"");
		}
		return this.msgSql(sql, param);
	}
	
	
	public Result updateDept(DeptInfoReq req,String userId){
		List<String> param=new ArrayList<String>();
		String sql="update tblDepartment set remark=? ,lastupdateTime=? ,lastupdateBy=? where id=?";
		param.add(""+req.dItem.remark+"");
		param.add(""+req.dItem.dataTime+"");
		param.add(""+userId+"");
		param.add(""+req.dItem.deptId+"");
		return this.msgSql(sql, param);
	}
	
	
	/**
	 * 根据用户id查询用户未读通知消息
	 * @param userId
	 * @return
	 */
	public Result getAllAdvice(String userId){
		List<String> param=new ArrayList<String>();
		String sql="SELECT id ,title= CASE WHEN LEN(Title) > 60 THEN LEFT(Title, 60) + '...' " +
				" ELSE Title END  FROM tblAdvice  WHERE Status='noRead' AND Receive=? ORDER BY createTime DESC ";
		param.add(""+userId+"");
		return this.sqlList(sql, param);
	}
	
	
	/**
	 * 根据部门Id获取部门
	 * @param deptIds
	 * @return
	 */
	public Result getDeptByIds(String deptIds[]){
		List<String> param=new ArrayList<String>();
		String ids="";
		for(int i=0;i<deptIds.length;i++){
			if(i+1==deptIds.length){
				ids+="'"+deptIds[i]+"'";
			}else{
				ids+="'"+deptIds[i]+"'"+",";
			}
		}
		String strsql="select id,deptFullName,classCode,createTime,lastUpdateTime,remark,deptCode from tblDepartment where id in ("+ids+")";
		return this.sqlList(strsql, param);
	}
	
	
	/**
	 * 根据部门Id查询部门
	 */
	public Result getDeptById(String deptId){
		List<String> param=new ArrayList<String>();
		String strsql="select id,deptFullName,classCode,createTime,lastUpdateTime,remark,deptCode from tblDepartment where id=? and  statusId!='-1'";
		param.add(""+deptId+"");
		return this.sqlList(strsql, param);
	}
	
	/**
	 * 根据职员Id查询职员
	 */
	public Result getEmpById(String empId){
		List<String> param=new ArrayList<String>();
		String strsql="SELECT top 1 te.id,te.EmpFullName,te.TitleID,te.Email,te.lastUpdateTime,te.BornDate,te.DepartmentCode,te.Tel, te.Mobile,"+
					" te.Gender,te.imgeNum,te.[signature],te.createTime,te.EmpNumber,te.photo,te.EmpName FROM tblEmployee te  where id=?";
		param.add(""+empId+"");
		return this.sqlList(strsql, param);
	}
	
//	public boolean updateEmpImgeNum(EmployeeItem emp){
//		List<String> param=new ArrayList<String>();
//		String strsql="UPDATE tblEmployee SET imgeNum = ?, lastUpdateTime = ? WHERE id = ?";
//		param.add(""+emp.imageNum+"");
//		param.add(""+BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss)+"");
//		param.add(""+emp.userId+"");
//		Result rs = this.msgSql(strsql, param);
//		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//			return rs.realTotal > 0 ? true : false;
//		} else {
//			return false;
//		}
//	}
	
	public void updateEmpTime(String userId) {
		List<String> param=new ArrayList<String>();
		String strsql="UPDATE tblEmployee SET lastUpdateTime = ? WHERE id = ?";
		param.add(""+BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss)+"");
		param.add(""+userId+"");
		this.msgSql(strsql, param);
	}
	

	
	
	/**
	 * 判断删除记录表中是否存在和部门表中相同的部门classCode
	 * @return
	 */
	public boolean existSameDeptCode(String deptTime){
		List<String> param=new ArrayList<String>();
		String sql="SELECT t.id, t.departmentName,t.classCode, m.id, m.position,m.objId FROM tblDepartment  t LEFT " +
				  " JOIN msgOperate m ON t.classCode=m.objId WHERE m.position='dept' and t.statusId!='-1' AND DATEDIFF(second, operateTime, '"+deptTime+"')<0";
		Result rs=this.sqlList(sql, param);
		boolean flag=false;
		List objList=(List) rs.retVal;
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && objList.size()>0){
			flag= true;
		}
		return flag;
	}
	
	/**
	 * 删除用户后，删除好友表中的相关信息
	 * @param empIds
	 * @return
	 */
	public boolean delFriends(String empIds[]){
		List<String> param=new ArrayList<String>();
		String ids="";
		for(int i=0;i<empIds.length;i++){
			if(i+1==empIds.length){
				ids+="'"+empIds[i]+"'";
			}else{
				ids+="'"+empIds[i]+"'"+",";
			}
		}
		String strsql="DELETE FROM msgFriend WHERE userid  in ("+ids+") OR friendId in ("+ids+") ";
		boolean flag=false;
		Result rs=this.msgSql(strsql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && rs.realTotal>0){
			flag= true;
		}
		return flag;
	}
	
	/**
	 * 启用部门后更改部门最后修改时间
	 */
	public boolean updDeptTime(String deptIds[],String time){
		List<String> param=new ArrayList<String>();
		String ids="";
		for(int i=0;i<deptIds.length;i++){
			if(i+1==deptIds.length){
				ids+="'"+deptIds[i]+"'";
			}else{
				ids+="'"+deptIds[i]+"'"+",";
			}
		}
		String strsql="update tbldepartment set lastupdateTime= ?  WHERE id  in ("+ids+")";
		param.add(""+time+"");
		boolean flag=false;
		Result rs=this.msgSql(strsql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && rs.realTotal>0){
			flag= true;
		}
		return flag;
	}
	
	/**
	 * 根据好友Id查询用户
	 * @param fId
	 * @return
	 */
	public Result getUserByFriendId(String fId){
		List<String>param=new ArrayList<String>();
		String sql="select userId from msgFriend where friendId=?";
		param.add(""+fId+"");
		return this.sqlList(sql, param);		
	}
	
	/**
	 * 根据部门Code查询部门用户
	 * @param deptCode
	 * @return
	 */
	public Result getEmpByDeptCode(String deptCode){
		List<String> param=new ArrayList<String>();
		String sql="select id from tblemployee where openFlag='1' AND statusId!='-1' and  DepartmentCode like ?";
		param.add(""+deptCode+"%");
		return this.sqlList(sql, param);
		
	}
	
	/**
	 * 删除部门、群组、好友未读消息
	 * @param deptId
	 * @return
	 */
	public boolean delMsgs(String type,String field,String Id){
		List<String> param=new ArrayList<String>();
		String sql="";
		if("person".equals(type)){
			sql=" DELETE FROM OAMessage WHERE OperType=? AND "+field+"= ?  or send = ? ";
			param.add(""+type+"");
			param.add(""+Id+"");
			param.add(""+Id+"");
		}else{
			sql=" DELETE FROM OAMessage WHERE OperType=? AND "+field+"= ? ";
			param.add(""+type+"");
			param.add(""+Id+"");
		}
		boolean flag=false;
		Result rs=this.msgSql(sql, param);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS && rs.realTotal>=0){
			flag= true;
		}
		return flag;
	}
	/**
	 * 修改用户部门后，删除该用户原来部门的未读消息
	 * @param userId
	 * @param deptCode
	 * @return
	 */
	public void delMsgByDept(String userId,String deptCode){
		List<String> param=new ArrayList<String>();
		String sql=" DELETE FROM OAMessage WHERE OperType='dept' AND receive= ?  and relationId!= ?";
		param.add(""+userId+"");
		param.add(""+deptCode+"");
		this.msgSql(sql, param);
		sql="DELETE FROM OAMessage2 WHERE OperType='dept' AND receive= ?  and relationId!= ?";
		this.msgSql(sql, param);

	}
	
	public Result getMsgGroup(String userId){
//    	final Result result = new Result();
    	List paramList = new ArrayList() ;
    	final String sql = " select a.userID,c.empFullName,b.groupName,b.id from msgGroupUser a join msgGroup b on a.f_ref=b.id " +
    			"join tblemployee c on a.userID=c.id  where c.openFlag=1 and b.id in (select f_ref from msgGroupUser where userId=?) order by c.empFullName";
    	paramList.add(userId) ;
    	return sqlList(sql,paramList);
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
                   public void execute(Connection conn) throws  SQLException {
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
     * 客户端好友聊天
     * @param id long
     * @param name String
     * @return Result
     */
    public Result sendMsg(final String recvDbid,final MessageBean bean) {
    	final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try {
                        	bean.setRelationId(bean.getReceive());
                        	String sql = "insert into OAMessage2(id,receive,receiveName,content,operType,status,send,sendName,createBy,createTime,affix,relationId,relationName) " +
                        			"values('"+bean.getId()+"','"+bean.getReceive()+"','"+bean.getReceiveName()+"',?,'"+bean.getOperType()+"','"+bean.getStatus()+"','"+bean.getSend()+"','"+bean.getSendName()+"','"+bean.getCreateBy()+"','"+bean.getCreateTime()+"','"+bean.getAffix()+"','"+bean.getRelationId()+"','"+bean.getReceiveName()+"')" ;
                        	PreparedStatement pss = conn.prepareStatement(sql) ;
                        	pss.setString(1, bean.getContent()) ;
                        	int i = pss.executeUpdate() ;
                        	MessageBean receive = bean ;
                        	receive.setCreateBy(bean.getReceive()) ;
                        	receive.setRelationId(bean.getSend());
                        	receive.setId(recvDbid) ;
                        	receive.setStatus("noRead") ;
                        	sql = "insert into OAMessage(id,receive,receiveName,content,operType,status,send,sendName,createBy,createTime,affix,relationId,relationName) " +
                				"values('"+receive.getId()+"','"+receive.getReceive()+"','"+receive.getReceiveName()+"',?,'"+receive.getOperType()+"','"+receive.getStatus()+"','"+receive.getSend()+"','"+receive.getSendName()+"','"+receive.getCreateBy()+"','"+receive.getCreateTime()+"','"+bean.getAffix()+"','"+bean.getRelationId()+"','"+bean.getSendName()+"')" ;
                        	pss = conn.prepareStatement(sql) ;
                        	pss.setString(1, bean.getContent()) ;
                        	pss.executeUpdate() ;
                        	
//                        	if (MSGConnectCenter.sessionPool.get(bean.getReceive()) == null) {	// KK不在线才推送到手机                    	
//	                        	/* 查询收信人是否已经注册了iPhone */
//	                        	HashMap<String, ArrayList<String>> tokenMap = new HashMap<String, ArrayList<String>>() ;
//	                			Result result = new PublicMgt().queryTokenByUserIds(bean.getReceive(),conn) ;
//	                			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
//	                				tokenMap = (HashMap<String, ArrayList<String>>) result.retVal ;
//	                			}
//	                			if(tokenMap!=null && tokenMap.get(bean.getReceive())!=null){
//	                				ArrayList<String> tokenList = tokenMap.get(bean.getReceive());
//	                				String strContent = bean.getContent();
//	        						strContent = bean.getSendName()+"发私信给你：" + GlobalsTool.replaceHTML(strContent);
//	        						for(String token : tokenList){
//	        							JsonObject json = new JsonObject();
//	        							json.addProperty("keyId", bean.getId());
//	        							json.addProperty("type", "chat");
//	        							new AppleApnsSend(String.valueOf(SystemState.instance.dogId), token, strContent, json.toString()).start();
//	        						}
//	        					}
//                        	}
                        
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                        }
                    }
                });
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
    }
    
    /**
     * 客户端群组聊天
     * @param arrayId
     * @param bean
     * @return
     */
    public Result sendGroupMsg(final HashMap arrayId,final MessageBean bean) {
    	final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try { 
                        	MessageBean message = bean ;
                        	Statement state = conn.createStatement() ;
                        	String sql= "select emp.id,emp.empFullName from msgGroupUser groups join tblEmployee emp on emp.id=groups.userID where groups.f_ref='"+bean.getReceive()+"'" ;
                        	
                        	ResultSet rss = state.executeQuery(sql) ;
                        	int num=0;
                        	while(rss.next()){
                        		String userId = rss.getString("id") ;
                        		String receiveId=(String) arrayId.get(userId);
                        		message.setId(receiveId) ;
                        		message.setReceive(userId) ;
                        		message.setReceiveName(rss.getString("empFullName")) ;
                        		message.setCreateBy(userId) ;
                        		String tableName = "OAMessage" ;
                    			message.setStatus("noRead") ;
                        		if(bean.getSend().equals(userId)){
                        			message.setStatus("read") ;
                        			tableName = "OAMessage2" ;
                        		}
                        		
                        		sql = "insert into "+tableName+"(id,receive,receiveName,content,operType,status,send,sendName,createBy,createTime,relationId,relationName) " +
                				"select '"+message.getId()+"','"+message.getReceive()+"','"+message.getReceiveName()+"',?,'"+message.getOperType()+"','"+message.getStatus()+"','"+message.getSend()+"','"+message.getSendName()+"','"+message.getCreateBy()+"','"+message.getCreateTime()+"','"+message.getRelationId()+"', groupName from msgGroup where id = '"+message.getRelationId()+"'" ;
                    	        PreparedStatement pss = conn.prepareStatement(sql) ;
	                        	pss.setString(1, message.getContent()) ;
	                        	pss.executeUpdate() ;
	                        	num++;
                        	}
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                        }
                    }
                });
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
    }
    
    /**
     * 客户端部门聊天
     * @param arrayId
     * @param bean
     * @return
     */
    public Result sendDeptMsg(final HashMap arrayId,final MessageBean bean) {
    	final Result rs=new Result();
    	int retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
            	session.doWork(new Work() {
                    public void execute(Connection conn) throws SQLException {
                        try { 
                        	MessageBean message = bean ;
                        	Statement state = conn.createStatement() ;
                        	String sql= "SELECT id,empFullName FROM tblemployee WHERE openFlag='1' AND statusId!='-1' and DepartmentCode LIKE '"+bean.getRelationId()+"%'";
                        	ResultSet rss = state.executeQuery(sql) ;
                        	int nTotal = 0;
                        	while(rss.next()){
                        		nTotal++;
                        		String userId = rss.getString("id") ;
                        		String receiveId=(String) arrayId.get(userId);
                        		message.setId(receiveId) ;
                        		message.setReceive(userId) ;
                        		message.setReceiveName(rss.getString("empFullName")) ;
                        		message.setCreateBy(userId) ;
                        		String tableName = "OAMessage" ;
                    			message.setStatus("noRead") ;
                        		if(bean.getSend().equals(userId)){
                        			message.setStatus("read") ;
                        			tableName = "OAMessage2" ;
                        		}
                        		
                        		sql = "insert into "+tableName+"(id,receive,receiveName,content,operType,status,send,sendName,createBy,createTime,relationId,relationName) " +
                				"select '"+message.getId()+"','"+message.getReceive()+"','"+message.getReceiveName()+"',?,'"+message.getOperType()+"','"+message.getStatus()+"','"+message.getSend()+"','"+message.getSendName()+"','"+message.getCreateBy()+"','"+message.getCreateTime()+"','"+message.getRelationId()+"', deptFullName from tblDepartment where classCode = '"+message.getRelationId()+"'" ;
                    	        PreparedStatement pss = conn.prepareStatement(sql) ;
	                        	pss.setString(1, message.getContent()) ;
	                        	pss.executeUpdate() ;
                        	}
                        	rs.setRealTotal(nTotal);
                        } catch (SQLException ex) {
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            ex.printStackTrace();
                        }
                    }
                });
            	return rs.retCode ;
            }
    	});
    	rs.retCode = retCode ;
        return rs ;
    }
    
    public Result receiveMsg(String id) {
		ArrayList param = new ArrayList();
    	String sql = "select bean from MessageBean bean where bean.id=? ";
		param.add(id);
		Result rs = this.list(sql, param);
		receiveMsg(rs);
		return rs;
    }
    
    /**
     * 
     * @param userId
     * @param relationId
     * @param receiveTime
     * @return
     */
    public Result receiveMsg(final String userId, final String relationId) {
		ArrayList param = new ArrayList();
		String sql = "select bean from MessageBean bean where bean.relationId=? and bean.createBy=? ";
		param.add(relationId);
		param.add(userId);
		sql += " order by bean.createTime asc";
		Result rs = this.list(sql, param);
		receiveMsg(rs);
		return rs;
	}

	private void receiveMsg(final Result rs) {
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.realTotal > 0) {
			int retCode = DBUtil.execute(new IfDB() {
				public int exec(Session session) {
					session.doWork(new Work() {
						public void execute(Connection conn)
								throws SQLException {
							try {
								List msgList = (List) rs.getRetVal();
								String msgIds = "";
								for (int i = 0; i < msgList.size(); i++) {
									MessageBean message = (MessageBean) msgList
											.get(i);
									msgIds += "'" + message.getId() + "',";
								}
								msgIds = msgIds.substring(0,
										msgIds.length() - 1);
								String sql = "insert into OAMessage2 select * from OAMessage where id in ("
										+ msgIds + ")";
								PreparedStatement pss = conn
										.prepareStatement(sql);
								pss.executeUpdate();
								sql = "delete from OAMessage where id in ("
										+ msgIds + ")";
								pss = conn.prepareStatement(sql);
								pss.executeUpdate();
							} catch (SQLException ex) {
								rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
								ex.printStackTrace();
								return;
							}
						}
					});
					return rs.getRetCode();
				}
			});
			rs.setRetCode(retCode);
		}
	}
    
    /**
     * 
     * @param userId
     * @param relationId
     * @param receiveTime
     * @return
     */
    public Result delMsg(final String userId, final String relationId) {
		ArrayList param = new ArrayList();
		String sql = "select bean from MessageBean bean where bean.relationId=? and bean.createBy=? ";
		param.add(relationId);
		param.add(userId);
		sql += " order by bean.createTime asc";
		Result rs = this.list(sql, param);
		int retCode = DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							String sql = "";
							sql = "delete from OAMessage2 where relationId=? and createBy=?";
							PreparedStatement pss = conn.prepareStatement(sql);
							pss.setString(1, relationId);
							pss.setString(2, userId);
							pss.executeUpdate();
						} catch (SQLException ex) {
							ex.printStackTrace();
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
	  * classCode转ID
	  * @param classCode
	  * @param tblNmae
	  * @return
	  */
	 public Result classCodeToId(String classCode, String tblNmae){
	       String sql="select top 1 id from " + tblNmae + " where classCode = '" + classCode + "'";
	       Result rs=this.sqlList(sql, new ArrayList());
	       return rs;
	 }
}
