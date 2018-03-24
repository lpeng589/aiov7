package com.menyi.msgcenter.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.dbfactory.Result;
import com.google.gson.JsonObject;
import com.koron.oa.bean.MessageBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.msgcenter.msgif.DepartmentItem;
import com.menyi.msgcenter.msgif.EmpStatusItem;
import com.menyi.msgcenter.msgif.EmployeeItem;
import com.menyi.msgcenter.msgif.FriendItem;
import com.menyi.msgcenter.msgif.GroupItem;
import com.menyi.msgcenter.msgif.MsgHeader;
import com.menyi.msgcenter.msgif.MsgReq;
import com.menyi.msgcenter.msgif.NoteItem;
import com.menyi.msgcenter.msgif.NoteReq;
import com.menyi.msgcenter.msgif.StatusReq;
import com.menyi.msgcenter.msgif.CancelMsgReq;
import com.menyi.web.util.AppleApnsSend;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.SystemState;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 消息中心主类
 * 
 * @author Administrator
 * 
 */
public class MSGConnectCenter {
	static MsgMgt msgMgt = new MsgMgt();

	public static HashMap sessionPool; // 用户会话池

	public static HashMap fileSessionPool; // 文件内容发送池

	private HeatbeatThread heatbeatThread; // 心跳线程
	private HeatbeatThread fileheatbeatThread; // 心跳线程
	MSGConnectServer server;

	public static HashMap<String, GroupItem> groupMap = new HashMap<String, GroupItem>(); // 缓存所有的聊天群组信息
	public static HashMap<String, EmployeeItem> employeeMap = new HashMap<String, EmployeeItem>(); // 缓存所有的用户信息
	public static HashMap<String, DepartmentItem> departmentMap = new HashMap<String, DepartmentItem>(); // 缓存所有的部门信息，包括父级，key值为id
	public static TreeMap<String, DepartmentItem> departmentMap2 = new TreeMap<String, DepartmentItem>(); // 缓存所有的部门信息，包括父级，key值为classCode
	public static HashMap<String, String> empGroupMap = new HashMap<String, String>(); // 缓存用户所在群组信息
//	public static HashMap<String, List<FriendItem>> userFriend = new HashMap<String, List<FriendItem>>(); // 缓存所有的好友记录
	public static HashMap<String, List<String[]>> operateMap = new HashMap<String, List<String[]>>(); // 缓存所有的操作记录（包含部门、用户、群组的）
	public static String sysDeptTime; // 此处保存系统所有部门最新时间
	public static String sysEmpTime; // 此处保存系统所有用户最新时间
	public static MSGConnectCenter instance;
	public static Logger log;

	/**
	 * 初始化
	 * 
	 * @return boolean
	 * @roseuid 49D19047036B
	 */
	public boolean init(Logger log, int port) {
		
		this.log = log;
		log.debug("开始启动KK消息中心,端口"+port);
		if (null != instance) {
			instance.server.destroyObj();
			instance = null;
		}
		
		if (!init()) {
			return false;
		}		

		sessionPool = new HashMap();
		fileSessionPool = new HashMap();
		server = new MSGConnectServer();
		if (!server.init(port, sessionPool, log, fileSessionPool)) {
			log.error("MSGConnectCenter.init MSGConnectServer Error");
			return false;
		} else {
			log.debug("MSGConnectCenter.init MSGConnectServer Successfull");
			server.start();
		}
		instance = this;

		heatbeatThread = new HeatbeatThread(); // 普通心跳
		if (!heatbeatThread.init(sessionPool, log)) {
			log.error("MSGConnectCenter.init(common) SessionClearThread Error");
			return false;
		} else {
			log.debug("MSGConnectCenter.init(common) SessionClearThread Successfull");
			heatbeatThread.start();
		}
		fileheatbeatThread = new HeatbeatThread(); // 文件传输心跳
		if (!fileheatbeatThread.init(fileSessionPool, log)) {
			log.error("MSGConnectCenter.init(file) SessionClearThread Error");
			return false;
		} else {
			log.debug("MSGConnectCenter.init(file) SessionClearThread Successfull");
			fileheatbeatThread.start();
		}
		return true;
	}
	
	/**
	 * 初始化数据
	 * @return
	 */
	public static boolean init() {

		// 所有的部门信息
		Result rsDeptList = msgMgt.getAllDept();
		List deptList = (List) rsDeptList.retVal;
		for (int i = 0; i < deptList.size(); i++) {
			Object[] obj = (Object[]) deptList.get(i);
			DepartmentItem deptItem = new DepartmentItem();
			deptItem.deptId = (String) obj[0];
			deptItem.name = (String) obj[1];
			deptItem.operateType = MsgHeader.OBJ_INIT;
			String remark = (String) obj[5];
			if (remark == null || "null".equals(remark))
				remark = "";
			deptItem.remark = remark;
			deptItem.classCode = (String) obj[2];
			deptItem.deptCode = (String) obj[6];
			deptItem.dataTime = (String) obj[4];
			deptItem.createTime = (String) obj[3];
			departmentMap.put(deptItem.deptId, deptItem);
			departmentMap2.put(deptItem.classCode, deptItem);
		}

		// 所有的用户信息
		String time = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		Result rsEmpList = msgMgt.getAllSysUser();
		final List empList = (List) rsEmpList.retVal;
		for (int i = 0; i < empList.size(); i++) {
			Object[] obj = (Object[]) empList.get(i);
			EmployeeItem empItem = new EmployeeItem();
			empItem.userId = (String) obj[0];
			empItem.name = (String) obj[1];
			empItem.shortName = (String) obj[15];
			if (null == empItem.shortName) {
				empItem.shortName = "";
			}
			empItem.operateType = MsgHeader.OBJ_INIT;

			String duty = GlobalsTool.getEnumerationItemsDisplay("duty",
					(String) obj[2], "zh_CN");
			empItem.titleID = duty;
			empItem.email = (String) obj[3];
			empItem.dataTime = (String) obj[4];
			empItem.birth = (String) obj[5];
			empItem.departmentCode = (String) obj[6];
			empItem.telPhone = (String) obj[7];
			empItem.mobile = (String) obj[8];
			empItem.sEmpNumber = (String) obj[13];
			empItem.createTime = (String) obj[12];
			empItem.groupTime = time;
			empItem.friendTime = time;

			String sex = (String) obj[9];
			if ("".equals(sex) || sex == null || "null".equals(sex)) {
				empItem.sex = 0;
			} else if ("1".equals(sex)) {
				empItem.sex = 1;
			} else {
				empItem.sex = 2;
			}
			String sPhoto = (String) obj[14];
			if ("".equals(sPhoto) || sPhoto == null || "null".equals(sPhoto)) {
				empItem.imageNum = 0;
			} else {
				empItem.imageNum = -1;
			}

			String content = (String) obj[11];

			if ("".equals(content) || content == null || "null".equals(content)) {
				content = "";
			}
			empItem.signContext = content;
			empItem.loginStatus = EmployeeItem.OFFLINE; // 服务器启动时，用户都不在线
			empItem.loginType = EmployeeItem.CS_LOGIN;
			employeeMap.put(empItem.userId, empItem);
		}

		// 所有的群组信息
		Result rsGroupList = msgMgt.getAllmsgGroup();
		List groupList = (List) rsGroupList.retVal;
		for (int i = 0; i < groupList.size(); i++) {
			Object[] obj = (Object[]) groupList.get(i);
			GroupItem groupItem = new GroupItem();
			groupItem.groupId = (String) obj[0];
			groupItem.operateType = MsgHeader.OBJ_INIT;
			groupItem.groupName = (String) obj[1];
			groupItem.createBy = (String) obj[2];
			groupItem.createTime = (String) obj[3];
			String remark = (String) obj[4];
			if (remark == null || "null".equals(remark))
				remark = "";
			groupItem.remark = remark;
			groupItem.dataTime = (String) obj[5];
			Result rs = msgMgt.getFriendByGroupId(groupItem.groupId);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				boolean err = false;
				List list = (List) rs.retVal;
				List<FriendItem> friendList = new ArrayList<FriendItem>();
				for (int k = 0; k < list.size(); k++) {
					Object[] fobj = (Object[]) list.get(k);
					String id = (String) fobj[0];
					if (null == employeeMap.get(id)) { // 停用或删除的职员就删了吧
						err = true;
					} else {
						FriendItem fItem = new FriendItem();
						fItem.userId = id;
						friendList.add(fItem);
					}
				}

				byte[] b = new byte[20];
				groupItem.userCount = MsgHeader.intToShort(list.size(), b, 0);
				groupItem.userList.addAll(friendList);
				if (err) {
					msgMgt.updateGroup(groupItem);
				}

			}
			if (groupItem.userList.size() > 1) {
				groupMap.put(groupItem.groupId, groupItem);
			} else {
				msgMgt.delteteGroup(groupItem.groupId); // 没人的群组，删了
			}
		}

		// 获取用户所在群组信息
		Iterator iter = employeeMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			EmployeeItem empItem = (EmployeeItem) entry.getValue();
			String allOwnGroup = msgMgt.getOwnGroup(empItem.userId);
			empGroupMap.put(empItem.userId, allOwnGroup);
		}

		// 获取所有的删除操作
		Result rsOperate = msgMgt.getAllOperate();
		List operateList = (List) rsOperate.retVal;
		for (int i = 0; i < operateList.size(); i++) {
			Object[] obj = (Object[]) operateList.get(i);
			String opType = (String) obj[1]; // 获取删除操作对象(用户，部门，群组)
			String opId = (String) obj[3]; // 获取删除对象Id
			String opTime = (String) obj[2]; // 获取删除对象时间
			String userId = (String) obj[4]; // 获取被删除的用户
			String createUser = (String) obj[5]; // 获取群主
			if (operateMap.get(opType) == null) {
				List<String[]> opList = new ArrayList<String[]>();
				opList.add(new String[] { opId, opTime, userId, createUser });
				operateMap.put(opType, opList);
			} else {
				List<String[]> opList2 = operateMap.get(opType);
				opList2.add(new String[] { opId, opTime, userId, createUser });
			}
		}

		return true;
	
	}

	/**
	 * 销毁消息中心
	 * 
	 */
	public void destroyObj() {
		heatbeatThread.destroyObj();
		server.destroyObj();
		sessionPool.clear();
	}

	/**
	 * 生成各种头像缩略图
	 * 
	 * @param pic
	 * @param userId
	 */
	public static void compressPic(String userId) {
		String path = BaseEnv.FILESERVERPATH + "/pic/tblEmployee/";
		if (new File(path + userId + ".jpg").exists()) {
			// 两个尺寸
			FileHandler.doCompress(path + userId + ".jpg", userId + "_140.jpg",
					path, 140);
			FileHandler.doCompress(path + userId + ".jpg", userId + "_48.jpg",
					path, 48);
		}
	}

	/**
	 * 用于外部状态更新，如手机端登陆状态,网页登陆状态的改变
	 * 
	 * @param userId
	 * @param loginType
	 *            LOGIN_BS=2,LOGIN_CS=1,LOGIN_MOBILE=3
	 * @param status
	 *            ONLINE=1,OFLINE=2,LEAVE=3
	 */

	public static void userStatus(String userId, byte loginType, byte status) {
		StatusReq msg = new StatusReq();
		ArrayList<EmpStatusItem> statuList = new ArrayList();
		EmpStatusItem sItem = new EmpStatusItem();
		sItem.loginStatus = status;
		sItem.loginType = loginType;
		sItem.userId = userId;
		statuList.add(sItem);
		msg.statusList.addAll(statuList);
		Iterator<MSGSession> it = sessionPool.values().iterator();
		while (it.hasNext()) {
			MSGSession se = it.next();
			if (se.isActive && se.connect != null) {
				se.connect.write(msg);
			}
		}
	}

	/**
	 * 同步网页版删除部门或用户
	 * 
	 * @param objId
	 * @param objType
	 */
	@SuppressWarnings("unchecked")
	public static void deleteObj(String objId[], String objType) {
		String time = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		if ("employee".equals(objType)) {
			msgMgt.delEmployee(objId);
		} else if ("dept".equals(objType)) {
			msgMgt.delDept(objId);
		}
		// 更新操作记录缓存
		Map opMap = MSGConnectCenter.operateMap; // 操作记录缓存
		for (String id : objId) {
			Result rsDel = msgMgt.addOperate(IDGenerater.getId(), objType,
					time, id, "", ""); // 添加删除用户或部门的删除操作记录
			if (rsDel.retCode != ErrorCanst.DEFAULT_SUCCESS) {
				System.out.println("添加用户或部门删除记录失败");
			}
			if (opMap.get(objType) == null) {
				List<String[]> opList = new ArrayList<String[]>();
				opList.add(new String[] { id, time, "", "" });
				opMap.put(objType, opList);
			} else {
				List<String[]> opList2 = (List<String[]>) opMap.get(objType);
				opList2.add(new String[] { id, time, "", "" });
			}
		}
	}

	/**
	 * 获取最新的用户
	 */
	@SuppressWarnings("static-access")
	public static void refreshEmpInfo(String ...empIds) {
		msgMgt.refreshMemEmp(empIds);		
	}
	
	/**
	 * 获取最新添加的部门
	 */
	@SuppressWarnings("static-access")
	public static void refreshDeptInfo(String classCode) {
		msgMgt.refreshMemDept(classCode);
	}

	/**
	 * 添加启用的部门
	 */
	@SuppressWarnings("static-access")
	public static void addEnableDept(String objId[]) {
		String time = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		boolean flag = msgMgt.updDeptTime(objId, time);
		if (flag == false) {
			System.out.println("更新启用部门时间失败!");
		}
		Result rsAddDept = msgMgt.getDeptByIds(objId);
		List deptList = (List) rsAddDept.retVal;
		for (int i = 0; i < deptList.size(); i++) {
			Object[] obj = (Object[]) deptList.get(i);
			DepartmentItem deptItem = new DepartmentItem();
			deptItem.deptId = (String) obj[0];
			deptItem.name = (String) obj[1];
			deptItem.operateType = MsgHeader.OBJ_INIT;
			deptItem.classCode = (String) obj[2];
			deptItem.deptCode = (String) obj[6];
			deptItem.remark = (String) obj[5];
			deptItem.dataTime = (String) obj[4];
			deptItem.createTime = (String) obj[3];
			departmentMap.put(deptItem.deptId, deptItem);
			departmentMap2.put(deptItem.classCode, deptItem);
		}

	}

	/**
	 * 获取最新修改的部门
	 */
	@SuppressWarnings("static-access")
	public static void updateDeptInfo(String deptId) {
		Result rsAddDept = msgMgt.getDeptById(deptId);
		List deptList = (List) rsAddDept.retVal;
		if (deptList.size() == 1) {
			Object[] obj = (Object[]) deptList.get(0);
			DepartmentItem deptItem = departmentMap.get(deptId);
			deptItem.name = (String) obj[1];
			deptItem.classCode = (String) obj[2];
			deptItem.deptCode = (String) obj[6];
			deptItem.remark = (String) obj[5];
			deptItem.dataTime = (String) obj[4];
		}

	}

	public static String getEmpNameById(String id) {
		EmployeeItem emp = employeeMap.get(id);
		if (null == emp) {
			return "";
		} else {
			return emp.name;
		}
	}

	public static String getGroupNameById(String id) {
		GroupItem group = groupMap.get(id);
		if (null == group) {
			return "";
		} else {
			return group.groupName;
		}
	}

	public static String getDeptNameByClassCode(String classCode) {
		DepartmentItem dept = departmentMap2.get(classCode);
		if (null == dept) {
			return "";
		} else {
			return dept.name;
		}
	}

	/**
	 * 发送聊天消息，网页、KK、手机统一调用这接口
	 * 
	 * @param from
	 * @param to
	 * @param msg
	 * @param type
	 */
	public static void sendMsg(String from, String to, String msg, String type) {
		MessageBean bean = new MessageBean();

		if (null == employeeMap.get(from)) { // 这人已不存在了
			return;
		}

		String time = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		Result rs = new Result();
		rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
		bean.setOperType(type);
		bean.setId(IDGenerater.getId());
		bean.setContent(msg);
		bean.setSend(from);
		bean.setSendName(getEmpNameById(from));
		bean.setReceive(to);
		bean.setRelationId(to);

		if ("person".equals(type) || "note".equals(type)) {
			bean.setReceiveName(getEmpNameById(to));
			bean.setRelationName(bean.getSendName());
		} else if ("file".equals(type)) {
			bean.setReceiveName(getEmpNameById(to));
			bean.setRelationName(bean.getSendName());
			bean.setAffix(msg);
			bean.setOperType("note");
			bean.setContent("发送了文件：" + msg);
		} else if ("group".equals(bean.getOperType())) {
			bean.setReceiveName(getGroupNameById(to));
			bean.setRelationName(bean.getReceiveName());
		} else if ("dept".equals(bean.getOperType())) {
			bean.setReceiveName(getDeptNameByClassCode(to));
			bean.setRelationName(bean.getReceiveName());
		} else {
			return;
		}

		bean.setCreateBy(from);
		bean.setCreateTime(time);
		bean.setStatus("read");

		HashMap<String, String> mapId = new HashMap<String, String>();

		// 个人聊天
		if ("person".equals(type) || "note".equals(type) || "file".equals(type)) {
			if (null == employeeMap.get(to)) { // 这人已不存在了
				return;
			}
			String recvDbid = IDGenerater.getId();
			mapId.put(to, recvDbid);
			if (null != msg) {
				rs = msgMgt.sendMsg(recvDbid, bean);
			}
		}

		// 群组聊天
		else if ("group".equals(type)) {
			// 获取群组组员
			String userGroupStr = empGroupMap.get(from);
			GroupItem gItem = groupMap.get(to);
			if (null == gItem || null == userGroupStr) { // 这群不存在了
				return;
			}
			if (!userGroupStr.contains(gItem.groupId)) { // 发送者不属于该群
				return;
			}
			for (int i = 0; i < gItem.userList.size(); i++) {
				FriendItem fItem = gItem.userList.get(i);
				mapId.put(fItem.userId, IDGenerater.getId());
			}
			if (null != msg) {
				rs = msgMgt.sendGroupMsg(mapId, bean);
			}
		}

		// 部门聊天
		else if ("dept".equals(type)) {
			// 获取部门成员
			EmployeeItem emp = employeeMap.get(from);
			if (!emp.departmentCode.startsWith(to)) { // 该部门已经不包含该用户
				return;
			}
			Result empRs = msgMgt.getEmpByDeptCode(to);
			if (empRs.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) { // 该部门都不存在了
				return;
			}
			List empList = (List) empRs.retVal;

			for (int i = 0; i < empList.size(); i++) {
				Object[] obj = (Object[]) empList.get(i);
				mapId.put((String) obj[0], IDGenerater.getId());
			}
			if (null != msg) {
				rs = msgMgt.sendDeptMsg(mapId, bean);
			}
			log.debug("MSGConnectCenter-------保存部门消息到数据库，部门：" + bean.getRelationName() + ", 人数：" + rs.getRealTotal());
		}

		else {
			return;
		}

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 发送到KK
			sendMsgToKK(bean, mapId);

			// 发送到手机
			sendMsgToMobile(bean, mapId);
		}

	}

	/**
	 * 发送聊天消息到KK
	 * 
	 * @param bean
	 * @param mapId
	 */
	private static void sendMsgToKK(MessageBean bean,
			HashMap<String, String> mapId) {
		byte toObjType = MsgReq.OBJ_EMP;
		byte msgType = MsgReq.TEXT;
		if ("person".equals(bean.getOperType())) {
			toObjType = MsgReq.OBJ_EMP;
		} else if ("note".equals(bean.getOperType())) {
			toObjType = MsgReq.OBJ_EMP;
			msgType = MsgReq.SYS_NOTE;
		} else if ("group".equals(bean.getOperType())) {
			toObjType = MsgReq.OBJ_GROUP;
		} else if ("dept".equals(bean.getOperType())) {
			toObjType = MsgReq.OBJ_DEPT;
			log.debug("MSGConnectCenter-------部门消息发送到KK前，部门：" + bean.getRelationName() + ", 人数：" + mapId.keySet().size());			
		} else {
			return;
		}

		Set<Entry<String, String>> set = mapId.entrySet();
		Iterator<Entry<String, String>> it = set.iterator();
		int online = 0;
		int offline = 0;
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it
					.next();
			String key = entry.getKey();
			String val = entry.getValue();
			
			if (!bean.getSend().equals(key) || toObjType == MsgReq.OBJ_EMP) { // 部门或群组就得排除自己
				MSGSession ms = (MSGSession) sessionPool.get(key);
				if (null == ms) {
					OnlineUser user = OnlineUserInfo.getUser(key);
					log.debug("MSGConnectCenter-------发送到KK，接收者[" + key + "," + user.getName() + "]不在线，类型：" + bean.getOperType() + "");
					offline++;
					continue;
				}
				online++;
				String msg = bean.getContent();

				if ("note".equals(bean.getOperType())
						&& msg.startsWith("发送了文件：")) {
					MSGSession.sendOfflineFile(bean.getSend(), key, val,
							msg.replaceAll("发送了文件：", ""));
					continue;
				}

				// 网页端发送的是图片消息
				if (msg.contains("<img") && msg.contains("src")) {
					String data[] = msg.split("<img ");
					for (String d : data) {

						if (d.indexOf("src") > -1
								&& !d.contains("/js/plugins/emoticons/images/")) {
							String picUrl = "";
							String imgName = "";
							if (d.contains("type=")) { // 客户端发送的图片
								String src[] = d.split("src=");
								imgName = src[1].toString().split("\"")[1];
								picUrl = BaseEnv.FILESERVERPATH + "/msgPic/"
										+ imgName;
							} else { // 网页端发送的图片
								String src[] = d.split("src=");
								imgName = src[1].toString().split("\"")[1];
								/*picUrl = BaseEnv.FILESERVERPATH
										+ "/../website/upload/" + imgName;*/
								picUrl = BaseEnv.FILESERVERPATH
								+ "/pic/tblEmployee/" + imgName;
							}
							try {
								FileInputStream in = new FileInputStream(picUrl);

								ByteArrayOutputStream out = new ByteArrayOutputStream(
										1024);
								byte[] temp = new byte[1024];
								int size = 0;
								while ((size = in.read(temp)) != -1) {
									out.write(temp, 0, size);
								}
								in.close();
								byte[] bytes = out.toByteArray();

								MsgReq picReq = new MsgReq();
								picReq.fromUserId = bean.getSend();
								picReq.toObj = key;
								picReq.toObjType = toObjType;
								picReq.msgType = MsgReq.PIC; // 图片类型
								picReq.msgId = imgName;
								picReq.sendTime = bean.getCreateTime();
								picReq.dataFile = bytes;

								ms.connect.write(picReq);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								// e.printStackTrace();
							}
						}
					}
				}

				MsgReq reqNew = new MsgReq();
				reqNew.fromUserId = bean.getSend();
				reqNew.toObj = key;
				if ("dept".equals(bean.getOperType())
						|| "group".equals(bean.getOperType())) {
					reqNew.toObj = bean.getRelationId();
				}
				reqNew.toObjType = toObjType;
				reqNew.msgType = msgType;
				reqNew.dataStr = msg;
				reqNew.msgId = val;
				reqNew.sendTime = bean.getCreateTime();
				ms.connect.write(reqNew);
			}
		}
		log.debug("MSGConnectCenter-------发送到KK，成功：" + online + "，失败：" + offline + "，类型：" + bean.getOperType() + "");
	}

	private static void sendMsgToMobile(MessageBean bean,
			HashMap<String, String> mapId) {
		String ids = "";
		Set<Entry<String, String>> set = mapId.entrySet();
		Iterator<Entry<String, String>> it = set.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it
					.next();
			ids += entry.getKey() + ",";
		}

		HashMap<String, ArrayList<String[]>> tokenMap = null;
		Result result = new PublicMgt().queryTokenByUserIds(ids);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			tokenMap = (HashMap<String, ArrayList<String[]>>) result.retVal;
		}

		String msg = "";
		if ("person".equals(bean.getOperType())) {
			msg = bean.getRelationName() + ":"
					+ GlobalsTool.replaceHTML3(bean.getContent());
		} else if ("note".equals(bean.getOperType())) {
			bean.setOperType("person");
			msg = bean.getRelationName() + ":"
					+ GlobalsTool.replaceHTML3(bean.getContent());
		} else if ("group".equals(bean.getOperType())) {
			msg = bean.getSendName() + "(" + bean.getRelationName() + "):"
					+ GlobalsTool.replaceHTML3(bean.getContent());
		} else if ("dept".equals(bean.getOperType())) {
			msg = bean.getSendName() + "(" + bean.getRelationName() + "):"
					+ GlobalsTool.replaceHTML3(bean.getContent());
		} else {
			return;
		}

		it = set.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it
					.next();

			ArrayList<String[]> tokenList = tokenMap.get(entry.getKey());
			if (null != tokenList) {
				//for (String[] str : tokenList) { // 一个人有可能有多个手机
				String[] str = tokenList.get(0);	
				JsonObject json = new JsonObject();
				json.addProperty("op", "add");
				json.addProperty("type", "chat");
				json.addProperty("userId", entry.getKey());
				json.addProperty("chatType", bean.getOperType());
				json.addProperty("relationId", bean.getRelationId());
				json.addProperty("send", bean.getSend());
				json.addProperty("sendName", bean.getSendName());
				json.addProperty("time", bean.getCreateTime());
				String content = bean.getSendName() + ":" + GlobalsTool.replaceHTML3(bean.getContent());
				content = msg.length() > 50 ? msg.substring(0, 50) + "..." : content;
				json.addProperty("msg", content);
					
				EmployeeItem item = employeeMap.get(entry.getKey());
				if (null != item) {
					if (item.loginType == EmployeeItem.CS_LOGIN && item.loginStatus == EmployeeItem.ONLINE) {
						// KK在线，推送，但不提醒
						json.addProperty("kkOnline", true);
					}
				}
				new AppleApnsSend(
						String.valueOf(SystemState.instance.dogId), str[0],
						msg, json.toString(), str[1]).start();
				//	}
			}
		}
	}

	/**
	 * 根据ID把一条未读消息标为已读，并通知客户端不再提醒
	 * 
	 * @param id
	 * @return
	 */
	public static Result receiveMsg(String id) {
		Result result = msgMgt.receiveMsg(id);
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			List msgList = (List) result.getRetVal();
			if (msgList != null) {
				for (int i = 0; i < msgList.size(); i++) {
					MessageBean bean = (MessageBean) msgList.get(i);
					cancelMsg(bean, "read");
				}
			}
		}
		return result;
	}

	/**
	 * 把未读消息标为已读，并通知客户端不再提醒
	 * 
	 * @param userId
	 *            接收者ID
	 * @param relationId
	 *            接收对象，职员ID、群组ID、部门ClassCode
	 * @param receiveTime
	 *            指定时间之后的
	 * @return
	 */
	public static Result receiveMsg(String userId, String relationId) {
		Result result = msgMgt.receiveMsg(userId, relationId);
		result.setRealTotal(0);
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			List msgList = (List) result.getRetVal();
			if (msgList != null) {
				for (int i = 0; i < msgList.size(); i++) {
					MessageBean bean = (MessageBean) msgList.get(i);
					cancelMsg(bean, "read");
				}
			}
			result.setRealTotal(msgList.size());
		}
		return result;
	}

	/**
	 * 删聊天消息
	 * 
	 * @param userId
	 * @param relationId
	 * @return
	 */
	public static Result delMsg(String userId, String relationId) {
		Result result = msgMgt.delMsg(userId, relationId);
		if (result.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			List msgList = (List) result.getRetVal();
			if (msgList != null) {
				for (int i = 0; i < msgList.size(); i++) {
					MessageBean bean = (MessageBean) msgList.get(i);
					cancelMsg(bean, "del");
				}
			}
		}
		return result;
	}

	private static void cancelMsg(MessageBean bean, String type) {
		// 通知KK
		cancelMsg(CancelMsgReq.TYPE_CHAT, bean.getReceive(), bean.getId());

//		// 通知手机
//		HashMap<String, ArrayList<String[]>> tokenMap = null;
//		Result result = new PublicMgt().queryTokenByUserIds(bean.getReceive());
//		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//			tokenMap = (HashMap<String, ArrayList<String[]>>) result.retVal;
//		}
//		ArrayList<String[]> tokenList = tokenMap.get(bean.getReceive());
//		if (null != tokenList) {
//			for (String[] str : tokenList) { // 一个人有可能有多个手机
//				JsonObject json = new JsonObject();
//				json.addProperty("op", type);
//				json.addProperty("type", "chat");
//				json.addProperty("userId", bean.getCreateBy());
////				json.addProperty("chatType", bean.getOperType());
//				json.addProperty("relationId", bean.getRelationId());
//				json.addProperty("msgId", bean.getId());
//				new AppleApnsSend(String.valueOf(SystemState.instance.dogId),
//						str[0], "", json.toString(), str[1]).start();
//			}
//		}
	}

	/**
	 * 给用户发送通知消息
	 */
	public static void sendAdvice(String userId, String adviceId, String advice) {
		NoteReq noteReq = new NoteReq();
		ArrayList<NoteItem> noteList = new ArrayList();
		NoteItem nItem = new NoteItem();
		nItem.adviceId = adviceId;
		if (advice.contains("RES<")) {
			advice = advice.split("RES<")[0] + "您有新的邮件";
		}
		nItem.advice = advice;
		noteList.add(nItem);
		noteReq.noteList.addAll(noteList);
		EmployeeItem eItem = (EmployeeItem) MSGConnectCenter.employeeMap
				.get(userId);
		if (eItem != null && eItem.loginStatus != EmployeeItem.OFFLINE) {
			MSGSession se = (MSGSession) sessionPool.get(userId);
			if (null != se) {
				se.connect.write(noteReq);
			}
		}
	}

	/**
	 * 通知客户端取消已读的聊天消息、通知消息 PWY
	 */
	public static void cancelMsg(byte nType, String sId, String sMsgId) {
		MSGSession se = (MSGSession) sessionPool.get(sId);
		if (null != se) {
			CancelMsgReq req = new CancelMsgReq();
			req.nType = nType;
			req.sMsgId = sMsgId;
			se.connect.write(req);
		}
	}

	/**
	 * 测试砖
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		MSGConnectCenter s = new MSGConnectCenter();
		s.initLog4j();
		Logger log4j = Logger.getLogger("MYLog");
		s.init(log4j, 12316);
	}

	private void initLog4j() {
		try {
			Properties prop = new Properties();
			InputStream is = new FileInputStream(
					"E:/work/aio/aio/config/log4j2.properties");
			this.getClass().getResourceAsStream("/log4j.properties");

			prop.load(is);
			PropertyConfigurator.configure(prop);
		} catch (Exception ex) {

		}
	}

}
