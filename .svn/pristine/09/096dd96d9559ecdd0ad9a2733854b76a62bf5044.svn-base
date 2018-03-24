package com.menyi.msgcenter.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.mail.Header;

import com.dbfactory.Result;
import com.koron.oa.bean.MessageBean;
import com.menyi.aio.bean.BaseDateFormat;

import com.menyi.msgcenter.msgif.*;

import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

public class MSGSession {

	private static Logger log;

	public String userId;

	public String userName;
 
	public int sequence_Id;

	// 用于存储终端的最后活动时间
	public long lastActiveTime;

	// 用于存储丢失心跳的总次数
	public int lostHeartbeatTimes;

	public MSGConnectSocket connect;

	public MSGConnectSocket fileTranConnect; // 专用于文件传输能连接

	public FileShakeReq fileShakeReq; // 文件传输的包含信息

	// 会话池
	private HashMap<String, MSGSession> sessionPool;

	private HashMap<String, MSGSession> fileSessionPool;

	public boolean isActive = true;

	MsgMgt msgMgt = new MsgMgt();

	public MSGSession(String userId, String userName, Logger log,
			HashMap sessionPool, HashMap fileSessionPool,
			MSGConnectSocket connect) {
		this.userId = userId;
		this.userName = userName;
		this.log = log;
		this.connect = connect;
		this.sessionPool = sessionPool;
		this.fileSessionPool = fileSessionPool;
		this.lastActiveTime = System.currentTimeMillis();
		this.sequence_Id = 0;
		isActive = true;
	}

	public void init(String userName, MSGConnectSocket connect) {
		this.userName = userName;
		this.connect = connect;
		this.fileTranConnect = null;
		this.lastActiveTime = System.currentTimeMillis();
		this.lostHeartbeatTimes = 0;
		isActive = true;
	}

	/**
	 * 初始化文件传输通道
	 * 
	 * @param msg
	 * @param fileTranConnect
	 */
	public void initFileTran(FileShakeReq msg, MSGConnectSocket fileTranConnect) {
		this.connect = null;
		this.fileTranConnect = fileTranConnect;
		this.fileTranConnect.clientType = MSGConnectSocket.CLIENT_FILETRAN;// 标识通道为文件传输通道
		this.fileShakeReq = msg;

		this.lastActiveTime = System.currentTimeMillis();
		this.lostHeartbeatTimes = 0;
		isActive = true;

		String path = BaseEnv.FILESERVERPATH + "/message/" + msg.sendId + "/";
		if (FileShakeReq.OFFLINE_SEND == msg.direct) {
			int nPos = msg.fileName.lastIndexOf(".");
			if (nPos == -1) {
				nPos = msg.fileName.length();
			}
			String name = msg.fileName.substring(0, nPos);
			String ext = msg.fileName.substring(nPos);
			String num = "";
			int n = 0;
			while (new File(path + name + num + ext).exists()) {
				n++;
				num = "(" + n + ")";
			}
			msg.fileName = path + name + num + ext;
		} else if (FileShakeReq.OFFLINE_RECV == msg.direct) {
			msg.fileName = path + msg.fileName;
		}

		if (msg.direct == FileShakeReq.RECEIVE
				|| msg.direct == FileShakeReq.OFFLINE_RECV) {
			this.userId = msg.receiveId;
		} else {
			this.userId = msg.sendId;
		}
	}

	/**
	 * 关闭文件传输通道
	 * 
	 */

	public void fileTranClose() {
		try {
			this.fileTranConnect.closeFileConnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.fileTranConnect = null;
		this.fileShakeReq = null;
	}

	/**
	 * 文件传输
	 * 
	 * @param bs
	 * @param count
	 * @param hasFinish
	 *            是否传输完毕
	 */
	public void fileTransport(byte[] bs, int count, boolean hasFinish) {
		// if (fileShakeReq.direct == FileShakeReq.OFFLINE) {
		// // 离线消息，保存在离线文件区
		// /**
		// * ********************************************未实现
		// */
		// } else {
		// 文件发送
		String recvKey = fileShakeReq.sessionKey + "$$"
				+ fileShakeReq.receiveId;
		MSGSession receiveSession = (MSGSession) sessionPool.get(recvKey);
		if (receiveSession != null) {
			receiveSession.fileTranConnect.write(bs, count);
		}
		if (hasFinish) {
			receiveSession.fileTranClose();
		}
		// }
	}

	public void close() {
		/* 客户端下线时通知在线用户已下线 */
		isActive = false;
		if (null != sessionPool) {
			sessionPool.remove(userId);
		}
		if (null != connect) {
			EmployeeItem eItem = MSGConnectCenter.employeeMap.get(userId); // 更新当前登录用户的登录状态和登录方式
			if (null == eItem) {
				return;
			}

			byte nType = 0;
			byte nStatus = EmployeeItem.OFFLINE;
			OnlineUser user = OnlineUserInfo.getUser(userId);
			if (null != user) {
				String type = user.getType();
				if ("web".equals(type)) {
					nType = EmployeeItem.BS_LOGIN;
					nStatus = EmployeeItem.ONLINE;
				} else if ("mobile".equals(type)) {
					nType = EmployeeItem.MOBILE_LOGIN;
					nStatus = EmployeeItem.ONLINE;
				} else {
					nType = EmployeeItem.CS_LOGIN;
					nStatus = EmployeeItem.OFFLINE;
				}
			}
			if (eItem.loginType != nType) {
				eItem.loginType = nType;
				eItem.loginStatus = nStatus;
				MSGConnectCenter.userStatus(userId,
						nType, nStatus);
			}
		
		}
	}

	/**
	 * 根据接收的数据判断手机终端的请求，并调用不同的处理方法
	 */
	public void clientMsgHandler(byte[] code) {

		// 只要有消息过来，都更新活动时间
		int command = MsgHeader.getCommand(code);
		this.lastActiveTime = System.currentTimeMillis();
		try {
			switch (command) {
			case IfCommand.HANDSHAKE:
				HandShakeReq handShakeReq = new HandShakeReq();
				handShakeReq.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到握手消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + handShakeReq.toString());				
				handShake(handShakeReq);
				break;
			case IfCommand.FILE:
				FileReq fileReq = new FileReq();
				fileReq.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到文件消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + fileReq.toString());			
				handFileReq(fileReq);
				break;
			case IfCommand.FILE_RSP:
				FileRsp fileRsp = new FileRsp();
				fileRsp.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到文件应答消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + fileRsp.toString());			
				handFileRsp(fileRsp);
				break;
			case IfCommand.FILECANCEL:
				FileCancelReq fileNoteReq = new FileCancelReq();
				fileNoteReq.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到文件取消消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + fileNoteReq.toString());			
				handFileCancelReq(fileNoteReq);
				break;
//			case IfCommand.FRIEND:
//				FriendReq friendReq = new FriendReq();
//				friendReq.decode(code);
//				log.debug("MSGSession.clientMsgHandler() 接收到好友消息  userID:" + this.userId + ":userName:"
//						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + friendReq.toString());			
//				handFriendReq(friendReq);
//				break;
			case IfCommand.GROUP:
				GroupReq groupReq = new GroupReq();
				groupReq.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到群组消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + groupReq.toString());			
				handGroupReq(groupReq);
				break;
			case IfCommand.MSG:
				MsgReq msgReq = new MsgReq();
				msgReq.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到消息消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + msgReq.toString());			
				handMsgReq(msgReq);
				break;
			case IfCommand.MSG_RSP:
				MsgRsp msgRsp = new MsgRsp();
				msgRsp.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到消息应答消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + msgRsp.toString());			
				handMsgRsp(msgRsp);
				break;
			case IfCommand.HEART_RSP:
				HeartRsp heartRsp = new HeartRsp();
				heartRsp.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到心跳应答消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + heartRsp.toString());			
				handHeartbeat(heartRsp);
				break;
			case IfCommand.NOTE_RSP:
				NoteRsp noteRsp = new NoteRsp();
				noteRsp.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到通知应答消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + noteRsp.toString());			
				handNoteRsp(noteRsp);
				break;
			case IfCommand.STATUS:
				StatusReq statusReq = new StatusReq();
				statusReq.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到状态消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + statusReq.toString());			
				handStatusReq(statusReq);
				break;
			case IfCommand.STATUS_RSP:
				log.debug("MSGSession.clientMsgHandler() 接收到状态应答消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" );			
				break;
			case IfCommand.EmpInfo:
				EmpInfoReq empInfReq = new EmpInfoReq();
				empInfReq.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到职员消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + empInfReq.toString());			
				handEmpInfoReq(empInfReq);
				break;
			case IfCommand.DeptInfo:
				DeptInfoReq deptInfReq = new DeptInfoReq();
				deptInfReq.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到部门消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + deptInfReq.toString());			
				handDeptInfoReq(deptInfReq);
				break;
			case IfCommand.SHAKEWINDOW:
				ShakeWindowReq req = new ShakeWindowReq();
				req.decode(code);
				log.debug("MSGSession.clientMsgHandler() 接收到拌动消息  userID:" + this.userId + ":userName:"
						+ userName + "时间:"+BaseDateFormat.getNowTimeLong()+":" + req.toString());			
				handShakeWindow(req);
				break;
			case IfCommand.SHAKEWINDOW_RSP:
			case IfCommand.CANCELMSG_RSP:
				break;
			default:
				log.warn("MSGSession.clientMsgHandler receive unKnow Command ID "
						+ command);
				break;
			}
		} catch (Exception e) {
			log.error("MSGSession.clientMsgHandler, command:"
					+ (command << 1) + " " + ((command & 0x0fffffff) > 0)
					+ "\n" + e);
		}
	}

	/**
	 * 个人资料修改
	 * 
	 * @param req
	 */
	public void handEmpInfoReq(EmpInfoReq req) {
		EmpInfoRsp empRsp = new EmpInfoRsp();
		req.eItem.dataTime = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		Result rs = msgMgt.updateEmp(req);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			EmployeeItem eItem = MSGConnectCenter.employeeMap
					.get(req.eItem.userId);
			eItem.imageNum = req.eItem.imageNum;
			eItem.signContext = req.eItem.signContext;
			eItem.dataTime = req.eItem.dataTime;
			empRsp.result = IfResult.SUCCESS;
		} else {
			empRsp.result = IfResult.FAIL;
		}
		empRsp.sequence_Id = req.sequence_Id;
		connect.write(empRsp);
	}

	/**
	 * 部门资料修改
	 * 
	 * @param req
	 */
	public void handDeptInfoReq(DeptInfoReq req) {
		DeptInfoRsp deptRsp = new DeptInfoRsp();
		req.dItem.dataTime = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		Result rs = msgMgt.updateDept(req, userId);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			DepartmentItem dItem = MSGConnectCenter.departmentMap
					.get(req.dItem.deptId);
			dItem.remark = req.dItem.remark;
			dItem.dataTime = req.dItem.dataTime;
			deptRsp.result = IfResult.SUCCESS;
		} else {
			deptRsp.result = IfResult.FAIL;
		}
		deptRsp.sequence_Id = req.sequence_Id;
		connect.write(deptRsp);

		// 修改本地数据库中的部门时间
		MSGConnectCenter.sysDeptTime = req.dItem.dataTime;
	}

	/**
	 * 窗口抖动
	 * 
	 * @param req
	 */
	public void handShakeWindow(ShakeWindowReq req) {
		ShakeWindowRsp rsp = new ShakeWindowRsp();
		rsp.sequence_Id = req.sequence_Id;
		connect.write(rsp);

		MSGSession ms = (MSGSession) MSGConnectCenter.sessionPool
				.get(req.toObj);
		if (ms != null) {
			ms.connect.write(req);
		}

	}

	/**
	 * 向手机终端发送心跳
	 */
	public void sendHeartbeat() {
		lostHeartbeatTimes++;
		HeartReq heartReq = new HeartReq();
		if (connect != null)
			connect.write(heartReq);
		if (fileTranConnect != null)
			fileTranConnect.write(heartReq);
	}

	/**
	 * 用于接收手机终端的心跳应答
	 */
	public void handHeartbeat(HeartRsp rsp) {
		this.lastActiveTime = System.currentTimeMillis();
		OnlineUserInfo.refresh(userId, null);
		this.lostHeartbeatTimes = 0;
	}

	/**
	 * 发送文件处理
	 * 
	 * @param msg
	 */
	public void fileShake(FileShakeReq msg) {
		FileShakeRsp frsp = new FileShakeRsp();
		String sendKey = msg.sessionKey + "$$" + msg.sendId;
		String receiveKey = msg.sessionKey + "$$" + msg.receiveId;
		MSGSession msgSend = (MSGSession) fileSessionPool.get(sendKey);
		MSGSession msgReceive = (MSGSession) fileSessionPool.get(receiveKey);

		frsp.result = MsgHeader.RECEIVE;
		if (msgSend != null && msgReceive != null) {
			frsp.result = MsgHeader.SUCCESS;
			msgSend.fileTranConnect.write(frsp);
			msgReceive.fileTranConnect.write(frsp);
		} else {
			if (FileShakeReq.OFFLINE_SEND == msg.direct
					|| FileShakeReq.OFFLINE_RECV == msg.direct) {
				frsp.result = MsgHeader.SUCCESS;
			}
			fileTranConnect.write(frsp);
		}
	}

	/**
	 * 握手消息处理， 1、根据更新时间，从MSGConnectCenter的三个缓存map中取更新数据，下发给客户端。
	 * 2、把当前所有用户的状态信息下发给该用户(改造OnlineUserInfo类，所有用户当前状态信息都存入此类中)
	 * 
	 * @param msg
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void handShake(HandShakeReq msg) {
		long curTime = System.currentTimeMillis();
		
		HandShakeRsp rsp = new HandShakeRsp();
		rsp.showShortName = (byte) ("true".equals(GlobalsTool
				.getSysSetting("KKShowEmpName")) ? 1 : 0);
		Date deptTime = MsgHeader.StringToDate(msg.deptLastTime);
		Date empTime = MsgHeader.StringToDate(msg.empLastTime);
		Date groupTime = MsgHeader.StringToDate(msg.groupLastTime);
		// Date friendTime = MsgHeader.StringToDate(msg.friendLastTime);

		EmployeeItem userItem = MSGConnectCenter.employeeMap.get(userId);

		// Date newFriendTime = MsgHeader.StringToDate(userItem.friendTime);
		// if (friendTime == null || newFriendTime.after(friendTime)) {
		// Map map = MSGConnectCenter.userFriend;
		// ArrayList<FriendItem> friendList = (ArrayList<FriendItem>) map
		// .get(msg.userId);
		// if (friendList != null) {
		// rsp.friendList.addAll(friendList);
		// }
		// }

		/*--群组--*/
		List<GroupItem> groupList = new ArrayList<GroupItem>();
		if (groupTime == null
				|| groupTime.before(MsgHeader
						.StringToDate("1900-10-10 10:00:00"))) {
			groupList.addAll(msgMgt.getInitGroup(userId));
		} else {
			groupList = msgMgt.getNewGroup(groupTime, userId); // 获取系统中所有的群组
		}
		rsp.groupList.addAll(groupList);

		/*--用户--*/
		if (empTime == null
				|| empTime
						.before(MsgHeader.StringToDate("1900-10-10 10:00:00"))) {
			msgMgt.getInitData("emp");
			rsp.empList.addAll(MSGConnectCenter.employeeMap.values());
		} else {
			List<EmployeeItem> empList = msgMgt.getNewUser(empTime);
			rsp.empList.addAll(empList);
		}
		MSGConnectCenter.sysEmpTime = msgMgt.getNewTime("tblemployee",
				"employee");

		/*--部门--*/
		if (deptTime == null
				|| deptTime.before(MsgHeader
						.StringToDate("1900-10-10 10:00:00"))) {
			msgMgt.getInitData("dept");

			TreeMap<String, DepartmentItem> deptMap = new TreeMap<String, DepartmentItem>(); // 缓存所有的部门信息，包括父级
			Map map = MSGConnectCenter.departmentMap;
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				DepartmentItem deptItem = (DepartmentItem) entry.getValue();
				deptMap.put(deptItem.deptCode, deptItem);
			}
			rsp.deptList.addAll(deptMap.values());
		} else {
			List<DepartmentItem> deptList = msgMgt.getNewDept(msg.deptLastTime);
			rsp.deptList.addAll(deptList);
		}
		MSGConnectCenter.sysDeptTime = msgMgt.getNewTime("tblDepartment",
				"dept");

		rsp.deptLastTime = MSGConnectCenter.sysDeptTime;
		rsp.empLastTime = MSGConnectCenter.sysEmpTime;
		rsp.friendLastTime = userItem.friendTime;
		rsp.groupLastTime = userItem.groupTime;
		rsp.sequence_Id = msg.sequence_Id;
		
		log.debug("MSGSession.handShake() 应答:" + userId + ":userName:"
				+ userName + ":时长:"+(System.currentTimeMillis()-curTime) );
		
		connect.write(rsp);

		final List<EmployeeItem> list = rsp.empList;
		new Thread(new Runnable() {

			public void run() {
				for (int i = 0; i < list.size(); i++) {
					try {
						Thread.sleep(100); // 慢点发，别把正常的请求给堵了
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					EmployeeItem emp = list.get(i);
					if (emp.imageNum < 0
							&& MsgHeader.OBJ_DELETE != emp.operateType) {
						String picUrl = BaseEnv.FILESERVERPATH
								+ "/pic/tblEmployee/" + emp.userId + "_140.jpg";

						MsgReq msgReq = new MsgReq();
						msgReq.fromUserId = "";
						msgReq.toObjType = MsgReq.OBJ_EMP;
						msgReq.toObj = "";
						sendPicMsg(picUrl, msgReq, "", "USER_HEAD/"
								+ emp.userId + ".jpg", userId,userName);
					}
				}
			}
		}).start();

		EmployeeItem loginItem = MSGConnectCenter.employeeMap.get(msg.userId); // 更新当前登录用户的登录状态和登录方式
		loginItem.loginStatus = EmployeeItem.ONLINE;
		loginItem.loginType = EmployeeItem.CS_LOGIN;

		if (msg.shakeType == msg.SHAKE_LOGIN) {
			MSGConnectCenter.userStatus(loginItem.userId, loginItem.loginType,
					loginItem.loginStatus);

			// 获取所有用户的登录状态和登录方式
			StatusReq statusReq = new StatusReq();
			Map map = MSGConnectCenter.employeeMap;
			Iterator iter = map.entrySet().iterator();
			ArrayList<EmpStatusItem> statuList = new ArrayList();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				EmployeeItem empItem = (EmployeeItem) entry.getValue();
				EmpStatusItem sItem = new EmpStatusItem();
				sItem.loginStatus = empItem.loginStatus;
				sItem.loginType = empItem.loginType;
				sItem.userId = empItem.userId;
				statuList.add(sItem);
			}
			statusReq.statusList.addAll(statuList);
			connect.write(statusReq);

			// 首次握手时，获取用户的所有未读通知消息
			NoteReq noteReq = new NoteReq();
			ArrayList<NoteItem> noteList = new ArrayList();
			Result rsAdvice = msgMgt.getAllAdvice(userId);
			if (rsAdvice.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				List adviceList = (List) rsAdvice.retVal;
				for (int i = 0; i < adviceList.size(); i++) {
					Object[] obj = (Object[]) adviceList.get(i);
					NoteItem nItem = new NoteItem();
					nItem.adviceId = (String) obj[0];
					nItem.advice = (String) obj[1];
					if (nItem.advice.contains("RES<")) {
						nItem.advice = nItem.advice.split("RES<")[0] + "您有新的邮件";
					}
					noteList.add(nItem);
				}
			}
			if (noteList.size() > 0) {
				noteReq.noteList.addAll(noteList);
				connect.write(noteReq);
			}

			// 首次握手时，获取登录用户的所有未读信息
			Result rsMsg = msgMgt.getNoReadMsg(userId);
			if (rsMsg.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				List msgList = (List) rsMsg.retVal;
				for (int i = 0; msgList.size() > 0 && i < msgList.size(); i++) {
					Object[] obj = (Object[]) msgList.get(i);
					MsgReq msgReq = new MsgReq();
					msgReq.fromUserId = (String) obj[1];
					msgReq.msgType = msgReq.TEXT; // 文本类型
					if ("group".equals((String) obj[4])) { // 群组聊天信息
						msgReq.toObjType = MsgReq.OBJ_GROUP;
						msgReq.toObj = (String) obj[10];
					} else if ("person".equals((String) obj[4])) { // 好友聊天信息
						msgReq.toObjType = MsgReq.OBJ_EMP;
						msgReq.toObj = (String) obj[2];
					} else if ("note".equals((String) obj[4])) { // 通知消息，自动回复
						String affix = (String) obj[11];
						
						boolean hasFile = null != affix && !"NULL".equals(affix)
						&& !"null".equals(affix) && !"".equals(affix);
						if(hasFile){ //检查文件是否存在
							String affs[] = affix.split(";");
							for (String aff : affs) {
								File f = new File(
										BaseEnv.FILESERVERPATH
										+ "/message/" + obj[10]
										+ "/" + aff);
								if(!f.exists()){
									hasFile = false;
									break;
								}
							}
						}
						if (hasFile) {
							String affs[] = affix.split(";");
							for (String aff : affs) {
								try {
									File f = new File(
											BaseEnv.FILESERVERPATH
											+ "/message/" + obj[10]
											+ "/" + aff);
									
										FileReq req = new FileReq();
										req.fromUserId = (String) obj[10];
										req.toUserId = userId;
										req.sessionKey = (String) obj[0];
										req.type = FileReq.TRAN_OFFLINE;
										req.fileName = aff;
										req.fileSize = getFileSizes(f);
										req.ip = "";
										req.setSequenceID();
										connect.write(req);
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							continue;
						} else {
							msgReq.toObjType = MsgReq.OBJ_EMP;
							msgReq.msgType = msgReq.SYS_NOTE; // 通知
							msgReq.toObj = (String) obj[2];
						}
					} else { // 部门聊天信息
						msgReq.toObjType = MsgReq.OBJ_DEPT;
						msgReq.toObj = (String) obj[10];
					}
					String sendTime = (String) obj[8];
					msgReq.sendTime = sendTime;
					String msgData = (String) obj[3];
					// 包含图片(网页端发送的图片和客户端发送的图片),则把图片发至客户端
					if (msgData.indexOf("<img ") > -1
							&& msgData.indexOf("src") > -1) {
						String data[] = msgData.split("<img ");
						for (String d : data) {
							if (d.indexOf("src") > -1
									&& !d.contains("/js/plugins/emoticons/images/")) {
								String picUrl = "";
								String imgName = "";
								if (d.contains("type=")) { // 客户端发送的图片
									String src[] = d.split("src=");
									imgName = src[1].toString().split("\"")[1];
									picUrl = BaseEnv.FILESERVERPATH
											+ "/msgPic/" + imgName;
								} else { // 网页端发送的图片
									String src[] = d.split("src=");
									imgName = src[1].toString().split("\"")[1];
									/*picUrl = BaseEnv.FILESERVERPATH
											+ "/../website/upload/" + imgName;*/
									if(!"".equals(imgName)){									
										picUrl = BaseEnv.FILESERVERPATH
										+ "/pic/tblEmployee/" + imgName;
										//兼容以前未读，防止close
										if(!new File(picUrl).exists()){
											picUrl = BaseEnv.FILESERVERPATH
											+ "/../website/upload/" + imgName;
										}	
									}								
								}
								File f = new File(picUrl);
								if (f.exists()) {
									sendPicMsg(picUrl, msgReq, sendTime,
											imgName, userId,userName);
								}
							}
						}
					}

					// 如果包含网页端发来的QQ表情，则去除表情前的路径
					if (msgData.contains("/js/plugins/emoticons/images/")) {
						msgData = msgData.replace(
								"src=\"/js/plugins/emoticons/images/",
								"type=\"face\" src=\"");
					}
					msgReq.dataStr = msgData;
					msgReq.msgId = (String) obj[0];
					connect.write(msgReq);
				}
			}

		}
	}

	public static long getFileSizes(File f) throws Exception {// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

	public static void sendPicMsg(String picUrl, MsgReq msgReq,
			String sendTime, String imgName, String userId,String userName) {
		byte[] picData;
		try {
			picData = MSGSession.getFileContent(picUrl);
			if(picData.length == 0){
				log.info("MSGSession.sendPicMsg() 发送图片 文件不存在" + userId + ":userName:"
						+ userName +":"+picUrl );
				return;
			}
			MsgReq msgReq2 = new MsgReq();
			msgReq2.fromUserId = msgReq.fromUserId;
			msgReq2.toObjType = msgReq.toObjType;
			msgReq2.toObj = msgReq.toObj;
			msgReq2.sendTime = sendTime;
			msgReq2.dataLength = picData.length;
			msgReq2.msgId = imgName;
			msgReq2.dataFile = picData;
			msgReq2.msgType = MsgReq.PIC; // 图片类型
			MSGSession ms = (MSGSession) MSGConnectCenter.sessionPool
					.get(userId);
			if (null != ms) {
				ms.connect.write(msgReq2);
			}
		} catch (IOException e) {
			log.error("MSGSession.sendPicMsg() 发送头像失败!"+ userId + ":userName:"
					+ userName +":"+picUrl );
		}

	}

	/**
	 * 客户端状态发生变化 1、通知所有其它客户端，调用（MSGConnectCenter.userStatus(String userId,byte
	 * loginType,byte status)）
	 * 
	 * @param msg
	 */
	public void handStatusReq(StatusReq msg) {
		List<EmpStatusItem> empSList = msg.statusList;
		EmpStatusItem eItem = empSList.get(0);
		EmployeeItem loginItem = MSGConnectCenter.employeeMap.get(eItem.userId); // 更新当前登录用户的登录状态和登录方式
		loginItem.loginStatus = eItem.loginStatus;
		loginItem.loginType = eItem.loginType;

		MSGConnectCenter.userStatus(loginItem.userId, loginItem.loginType,
				loginItem.loginStatus);
	}

	/**
	 * 处理消息发送
	 * 
	 * @param req
	 */
	public void handMsgRsp(MsgRsp req) {
		if (IfResult.SUCCESS == req.result) {
			MSGConnectCenter.receiveMsg(req.msgId);
		}
	}

	/**
	 * 通知应答 1、更新通知已读状态
	 * 
	 * @param msg
	 */
	public void handNoteRsp(NoteRsp msg) {
		NoteReq req = new NoteReq();
		req.sequence_Id = msg.sequence_Id;
		if (msg.result != IfResult.SUCCESS) {
			req.sequence_Id = msg.sequence_Id;
			connect.write(req);
		}
		System.out.println("userID:" + userId + ":userName:" + userName + ":"
				+ msg.toString());
	}

	// // 单人聊天
	// public static void personMsg(Map map, String receiveId, MsgReq req,
	// String sendTime) {
	// EmployeeItem eItem = (EmployeeItem) map.get(req.toObj);
	// if (eItem != null) { // 判断用户是否还存在
	// if (eItem.loginStatus != EmployeeItem.OFFLINE
	// && eItem.loginType == EmployeeItem.CS_LOGIN) {
	// MSGSession ms = (MSGSession) MSGConnectCenter.sessionPool
	// .get(req.toObj);
	// if (req.msgType == MsgReq.TEXT || req.msgType == MsgReq.SYS_NOTE) {
	// req.msgId = receiveId;
	// }
	// req.sendTime = sendTime;
	// if (null != ms) {
	// req.setSequenceID();
	// ms.connect.write(req); // PWY
	// }
	// }
	// }
	// }

	// 部门、群组聊天
	public static void sendChatMsg(MsgReq req, HashMap<String, String> mapId) {
		Set<Entry<String, String>> set = mapId.entrySet();
		Iterator<Entry<String, String>> it = set.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it
					.next();
			String key = entry.getKey();
			String val = entry.getValue();

			if (!req.fromUserId.equals(key)) {
				MSGSession ms = (MSGSession) MSGConnectCenter.sessionPool
						.get(key);
				if (null == ms)
					continue;
				MsgReq reqNew = new MsgReq();
				reqNew.dataStr = req.dataStr;
				reqNew.toObjType = req.toObjType;
				reqNew.fromUserId = req.fromUserId;
				reqNew.toObj = req.toObj;
				reqNew.dataLength = req.dataLength;
				reqNew.msgType = req.msgType;
				reqNew.dataFile = req.dataFile;
				reqNew.setSequenceID();
				if (req.msgType == MsgReq.TEXT) {
					reqNew.msgId = val;
				} else {
					reqNew.msgId = req.msgId;
				}
				reqNew.sendTime = req.sendTime;
				ms.connect.write(reqNew);
			}
		}
	}

	//
	// // 群组聊天
	// public static void groupMsg(Map map, MsgReq req, String sendTime,
	// HashMap<String, String> groupMsgId) {
	// GroupItem gItem = MSGConnectCenter.groupMap.get(req.toObj);
	// if (gItem != null) { // 判断群组是否还存在
	// List<FriendItem> flist = gItem.userList;
	// for (int i = 0; i < flist.size(); i++) {
	// FriendItem fItem = flist.get(i);
	// EmployeeItem eItem = (EmployeeItem) map.get(fItem.userId);
	// if (eItem != null
	// // && eItem.loginStatus != EmployeeItem.OFFLINE //
	// 不用再判断，下面ms是否为空已经包括，而且这里有可能误判
	// && !req.fromUserId.equals(eItem.userId)) {
	// MSGSession ms = (MSGSession) MSGConnectCenter.sessionPool
	// .get(eItem.userId);
	// if (null == ms)
	// continue;
	//
	// MsgReq reqNew = new MsgReq();
	// reqNew.dataStr = req.dataStr;
	// reqNew.toObjType = req.toObjType;
	// reqNew.fromUserId = req.fromUserId;
	// reqNew.toObj = req.toObj;
	// reqNew.dataLength = req.dataLength;
	// reqNew.msgType = req.msgType;
	// reqNew.dataFile = req.dataFile;
	// reqNew.setSequenceID();
	// if (req.msgType == MsgReq.TEXT) {
	// reqNew.msgId = groupMsgId.get(eItem.userId);
	// } else {
	// reqNew.msgId = req.msgId;
	// }
	// reqNew.sendTime = sendTime;
	// ms.connect.write(reqNew);
	// }
	// }
	// }
	// }

	/**
	 * 处理消息发送应答 1、更新消息状态
	 * 
	 * @param req
	 */
	@SuppressWarnings("static-access")
	public void handMsgReq(MsgReq req) {

		// 应答
		MsgRsp rsp = new MsgRsp();
		rsp.sequence_Id = req.sequence_Id;
		rsp.result = IfResult.RECEIVE; // 标识已收到
		connect.write(rsp);

		String type = "";
		switch (req.toObjType) {
		case 1:
			type = "person";
			break;
		case 2:
			type = "dept";
			break;
		case 3:
			type = "group";
			break;
		default:
			break;
		}
		if (MsgReq.SYS_NOTE == req.msgType) {
			type = "note";
		}

		if (req.msgType == req.PIC) { // 消息中如有图片，则在服务器端保存该图片
			try {
				if ("".equals(req.toObj)) { // 头像
					String path = BaseEnv.FILESERVERPATH + "/pic/tblEmployee/";
					createFile(path + req.fromUserId + ".jpg", req.dataFile);
					MSGConnectCenter.compressPic(req.fromUserId);
				} else { // 普通聊天图片
					String picUrl = BaseEnv.FILESERVERPATH + "/msgPic/"
							+ req.msgId;
					createFile(picUrl, req.dataFile);
				}

			} catch (Exception e) {
				log.error("写图片失败!", e);
			}

			// 用户头像上传时无接收ID
			if ("".equals(req.toObj))
				return;

		} else {
			MSGConnectCenter.sendMsg(req.fromUserId, req.toObj, req.dataStr,
					type);
		}

	}

	/**
	 * 更改用户的部门时间
	 * 
	 * @param userId
	 */
	public void userGroupTime(String userId, String time) {
		EmployeeItem userItem = MSGConnectCenter.employeeMap.get(userId);
		if (userItem != null)
			userItem.groupTime = time;
	}

	/**
	 * 更新用户所在群组信息
	 * 
	 * @param userId
	 * @param groupId
	 * @param type
	 *            包含添加群组，和去除群组
	 */
	public void userGroupList(String userId, String groupId, String type) {
		String userGroupStr = MSGConnectCenter.empGroupMap.get(userId);
		if ("delete".equals(type)) {
			userGroupStr = userGroupStr.replaceAll(groupId + ",", "");
		} else {
			userGroupStr += groupId + ",";
		}
		MSGConnectCenter.empGroupMap.put(userId, userGroupStr);
	}

	/**
	 * 将群组操作信息广播给群组组员
	 */
	public void noticeGroupUser(GroupItem gItem, GroupReq req) {
		// 通知群组成员
		for (int i = 0; i < gItem.userList.size(); i++) {
			FriendItem fItem = gItem.userList.get(i);
			EmployeeItem eItem = MSGConnectCenter.employeeMap.get(fItem.userId);
			if (eItem.loginStatus != EmployeeItem.OFFLINE) {
				MSGSession ms = (MSGSession) sessionPool.get(fItem.userId);
				if (null == ms)
					continue;
				req.setSequenceID();
				ms.connect.write(req);
			}
		}
	}

	public void updOperate(Map opMap, String groupId, String time,
			String userId, String createUser) {
		// 更新删除群组操作记录缓存
		if (opMap.get("group") == null) {
			List<String[]> opList = new ArrayList<String[]>();
			opList.add(new String[] { groupId, time, userId, createUser });
			opMap.put("group", opList);
		} else {
			List<String[]> opList2 = (List<String[]>) opMap.get("group");
			opList2.add(new String[] { groupId, time, userId, createUser });
		}

	}

	/**
	 * 建立修改群组应答
	 * 
	 * @param req
	 */
	@SuppressWarnings("unchecked")
	public void handGroupReq(GroupReq req) {
		Map opMap = MSGConnectCenter.operateMap; // 删除操作记录
		GroupRsp rsp = new GroupRsp();
		String time = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss); // 群组操作时间
		GroupItem g = req.gItem; // 客户端传来的群组信息
		g.dataTime = time;
		if (g.operateType == MsgHeader.OBJ_ADD) { // 群组增加操作
			g.createTime = time;
			g.groupId = IDGenerater.getId();
			// 数据库保存新添加的群组
			GroupItem gItem = new GroupItem();
			gItem.groupId = g.groupId;
			gItem.groupName = g.groupName;
			gItem.createBy = g.createBy;
			gItem.createTime = g.createTime;
			gItem.remark = g.remark;
			gItem.dataTime = g.createTime;
			gItem.userCount = g.userCount;
			gItem.userList.addAll(g.userList);
			Result rs = msgMgt.addGroup(gItem);

			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) { // 更新群组缓存
				// 创建群组成功后，将初始成员加入群组
				Result rsUser = msgMgt.addGroupUser(gItem);
				if (rsUser.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					// 向内存中添加群组的信息
					MSGConnectCenter.groupMap.put(gItem.groupId, gItem);
					ArrayList<FriendItem> listErr = new ArrayList<FriendItem>();
					for (int i = 0; i < gItem.userCount; i++) {
						FriendItem fItem = gItem.userList.get(i);
						if (null != MSGConnectCenter.employeeMap
								.get(fItem.userId)) {
							// 更新群组用户的群组时间
							userGroupTime(fItem.userId, time);
							// 更新用户所在群组信息
							userGroupList(fItem.userId, gItem.groupId, "add");
						} else {
							listErr.add(fItem);
						}
					}
					gItem.userList.removeAll(listErr);
				} else {
					rsp.result = IfResult.FAIL;
				}
			} else {
				rsp.result = IfResult.FAIL;
			}
			rsp.sequence_Id = req.sequence_Id;
			connect.write(rsp);

			// 通知群组成员
			noticeGroupUser(gItem, req);

		} else if (g.operateType == MsgHeader.OBJ_DELETE) { // 删除操作(群主解散群组、组员自动退出)

			GroupItem gItem = MSGConnectCenter.groupMap.get(g.groupId); // 此处保存群组缓存，以便使用
			if (gItem == null) { // 群组不存在，直接返回失败信息
				rsp.sequence_Id = req.sequence_Id;
				rsp.result = IfResult.FAIL;
				connect.write(rsp);
				return;
			}
			GroupItem oldGroupItem = new GroupItem(); // 保存原来的群组中组员信息，便于广播信息
			oldGroupItem.userList.addAll(gItem.userList);
			oldGroupItem.userCount = (short) oldGroupItem.userList.size();

			if (userId.equals(gItem.createBy)) { // 解散群组
				Result rs = msgMgt.delteteGroup(g.groupId);
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					Result rsDel = msgMgt.addOperate(g); // 添加群主解散群组的记录

					boolean flag = msgMgt.delMsgs("group", "relationId",
							g.groupId);
					if (flag == true) {
						System.out.println("删除群组未读消息成功!");
					}

					if (rsDel.retCode == ErrorCanst.DEFAULT_SUCCESS) {
						for (int i = 0; i < g.userList.size(); i++) {
							FriendItem fItem = g.userList.get(i);
							// 更新删除群组操作记录缓存
							updOperate(opMap, g.groupId, time, fItem.userId,
									g.createBy);
							// 更新群组成员的群组时间
							userGroupTime(fItem.userId, time);
							// 更新用户所在群组信息
							userGroupList(fItem.userId, g.groupId, "delete");
						}
						MSGConnectCenter.groupMap.remove(g.groupId);

						// 删除群组中的组员
						Result rsDel2 = msgMgt.deleteUserByGroupId(g.groupId);
						if (rsDel2.retCode == ErrorCanst.DEFAULT_SUCCESS) {
							System.out.println("解散群组，删除群成员成功！");
							rsp.result = IfResult.SUCCESS;
						} else {
							rsp.result = IfResult.FAIL;
						}
					} else {
						rsp.result = IfResult.FAIL;
					}
				}
				rsp.sequence_Id = req.sequence_Id;
				connect.write(rsp);

				// 通知群组成员
				g.operateType = MsgHeader.OBJ_DELETE;
				noticeGroupUser(oldGroupItem, req);

			} else { // 组员自动退出群组

				boolean flag = msgMgt.delMsgs("person", "receive", userId);
				if (flag == true) {
					System.out.println("删除用户未读消息成功!");
				}

				Result rs = msgMgt.removeGroupUser(userId, g.groupId);
				if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					for (int i = 0; i < gItem.userList.size(); i++) {
						FriendItem fItem = gItem.userList.get(i);
						// 更新群组用户的群组时间
						userGroupTime(fItem.userId, time);
					}
					// 更新删除群组操作记录缓存
					updOperate(opMap, g.groupId, time, userId, "");
					// 群组内存中删除退出的组员
					for (int i = 0; i < gItem.userList.size(); i++) {
						FriendItem f = gItem.userList.get(i);
						if (userId.equals(f.userId)) {
							gItem.userList.remove(f);
							break;
						}
					}
					for (int i = 0; i < g.userList.size(); i++) {
						FriendItem f = g.userList.get(i);
						if (userId.equals(f.userId)) {
							g.userList.remove(f);
							break;
						}
					}

					gItem.dataTime = time;
					g.dataTime = time;
					int num = gItem.userList.size();
					byte[] b = new byte[20];
					gItem.userCount = MsgHeader.intToShort(num, b, 0);
					g.userCount = gItem.userCount;

					// 更新用户所在群组信息
					userGroupList(userId, gItem.groupId, "delete");

					// 更新群组最后修改时间
					Result rsUpdTime = msgMgt.updateGroupTime(time,
							gItem.groupId);
					if (rsUpdTime.retCode != ErrorCanst.DEFAULT_SUCCESS) {
						System.out.println("更新组最后修改时间失败!");
					}

					// 添加从群组中删除组员的记录（组员自动退出的）
					Result rsDel = msgMgt.addOperate(IDGenerater.getId(),
							"group", time, g.groupId, userId, "");
					if (rsDel.retCode != ErrorCanst.DEFAULT_SUCCESS) {
						System.out.println("组员退出群组失敗!");
					}
					rsp.result = IfResult.SUCCESS;
				} else {
					rsp.result = IfResult.FAIL;
				}
				rsp.sequence_Id = req.sequence_Id;
				connect.write(rsp);

				// 此处特地为退出群的组员广播一次
				GroupReq gReq = new GroupReq();
				gReq.gItem.dataTime = time;
				gReq.gItem.operateType = MsgHeader.OBJ_DELETE;
				gReq.gItem.groupId = g.groupId;
				connect.write(gReq);

				g.operateType = MsgHeader.OBJ_UPDATE;
				// 通知群组成员
				noticeGroupUser(gItem, req);
			}
		} else if (g.operateType == MsgHeader.OBJ_UPDATE) { // 修改操作
			GroupItem gItem = MSGConnectCenter.groupMap.get(g.groupId);
			if (gItem == null) {// 群组不存在，直接返回失败信息
				rsp.sequence_Id = req.sequence_Id;
				rsp.result = IfResult.FAIL;
				connect.write(rsp);
				return;
			}
			List<FriendItem> allFItem = new ArrayList<FriendItem>();
			allFItem.addAll(gItem.userList);
			List<FriendItem> addFriends = new ArrayList<FriendItem>();

			// 根据群组Id修改群组公告
			if (!userId.equals(gItem.createBy)) { // 判断当前用户是不是群主
				rsp.sequence_Id = req.sequence_Id;
				rsp.result = IfResult.FAIL;
				connect.write(rsp);
				return;
			}
			Result rsUpdGroup = msgMgt.updateGroup(g);
			if (rsUpdGroup.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				gItem.groupName = g.groupName;
				gItem.remark = g.remark;
				gItem.dataTime = g.dataTime;

				// 删除的组员
				GroupItem gDel = new GroupItem();
				gDel.groupId = gItem.groupId;
				gDel.createBy = gItem.createBy;
				ArrayList<FriendItem> listErr = new ArrayList<FriendItem>();
				for (FriendItem f : gItem.userList) {
					if (null == MSGConnectCenter.employeeMap.get(f.userId)) {
						listErr.add(f);
						continue;
					}
					boolean flag = false;
					for (FriendItem f2 : g.userList) {
						if (f.userId.equals(f2.userId)) {
							flag = true;
						}
					}

					// 剔除用户时，删除未读消息
					boolean flag2 = msgMgt.delMsgs("person", "receive",
							f.userId);
					if (flag2 == true) {
						System.out.println("删除用户未读消息成功!");
					}

					if (!flag) {
						// 更新删除用户群组信息
						userGroupList(f.userId, gItem.groupId, "delete");

						// 更新删除群组操作记录缓存
						updOperate(opMap, g.groupId, time, f.userId, g.createBy);
						gDel.userList.add(f);
					}
				}
				gItem.userList.removeAll(listErr);

				// 从群组内存中剔除删除的成员
				gItem.userList.removeAll(gDel.userList);

				// 添加的组员
				for (FriendItem f : g.userList) {
					boolean flag = false;
					for (FriendItem f2 : allFItem) {
						if (f.userId.equals(f2.userId)) {
							flag = true;
						}
					}
					if (!flag) {
						// 更新添加用户群组信息
						userGroupList(f.userId, gItem.groupId, "add");
						addFriends.add(f);
						gItem.userList.add(f);
					}
				}

				System.out.println("增加组员:" + addFriends.size());
				if (gDel.userList.size() > 0) { // 增加群组删除成员操作记录
					Result rs = msgMgt.addOperate(gDel);
					if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS) {
						System.out.println("修改群组---删除群成员失败！");
					}
				}
				rsp.result = IfResult.SUCCESS;
			} else {
				rsp.result = IfResult.FAIL;
			}
			rsp.sequence_Id = req.sequence_Id;
			connect.write(rsp);

			g.operateType = MsgHeader.OBJ_UPDATE;

			// 通知所有群组成员(包含群组中此操作删除的组员)
			allFItem.addAll(addFriends);
			for (int i = 0; i < allFItem.size(); i++) {
				FriendItem fItem = allFItem.get(i);
				EmployeeItem eItem = (EmployeeItem) MSGConnectCenter.employeeMap
						.get(fItem.userId);
				if (eItem == null)
					continue;
				// 更新所属群组组员的群组时间
				this.userGroupTime(fItem.userId, time);
				if (eItem.loginStatus != EmployeeItem.OFFLINE) { // 判断被添加组员是否在线，在线的话转发
					MSGSession ms = (MSGSession) sessionPool.get(eItem.userId);
					if (null == ms)
						continue;
					req.setSequenceID();
					ms.connect.write(req);
				}
			}
		}
	}

//	/**
//	 * 好友操作应答
//	 * 
//	 * @param req
//	 */
//	@SuppressWarnings("unchecked")
//	public void handFriendReq(FriendReq req) {
//		FriendRsp rsp = new FriendRsp();
//		Map map = MSGConnectCenter.userFriend;
//		List<FriendItem> flist = new ArrayList<FriendItem>();
//
//		if ((List<FriendItem>) map.get(userId) == null) { // 判断用户当前是否拥有好友
//			map.put(userId, flist);
//		} else {
//			flist = (List<FriendItem>) map.get(userId);
//		}
//		FriendItem fItem = new FriendItem();
//		fItem.userId = req.userId;
//		Result rsAdd = msgMgt.opFriend(req, userId);
//		if (rsAdd.retCode == ErrorCanst.DEFAULT_SUCCESS) {
//			if (req.type == FriendReq.ADD) { // 添加好友
//				flist.add(fItem);
//			} else if (req.type == FriendReq.DELETE) { // 删除好友
//				for (FriendItem f : flist) {
//					if (f.userId.equals(fItem.userId)) {
//						fItem = f;
//						break;
//					}
//				}
//				flist.remove(fItem);
//			}
//			EmployeeItem userItem = MSGConnectCenter.employeeMap.get(userId); // 更新当前用户记录中的好友时间
//			userItem.friendTime = BaseDateFormat.format(new java.util.Date(),
//					BaseDateFormat.yyyyMMddHHmmss);
//			rsp.result = IfResult.SUCCESS;
//		} else {
//			rsp.result = IfResult.FAIL;
//		}
//		rsp.sequence_Id = req.sequence_Id;
//		connect.write(rsp);
//	}

	/**
	 * 获取文件内容，存入byte数组
	 */
	public static byte[] getFileContent(String filePath) throws IOException {
		File file = new File(filePath);
		if(!file.exists()){
			return new byte[0];
		}
		FileInputStream in = new FileInputStream(file);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] temp = new byte[1024];
		int size = 0;
		while ((size = in.read(temp)) != -1) {
			out.write(temp, 0, size);
		}
		in.close();
		byte[] bytes = out.toByteArray();
		return bytes;
	}

	/**
	 * 写入文件
	 * 
	 * @param path
	 * @param content
	 * @throws IOException
	 */
	public void createFile(String path, byte[] content) throws IOException {
		File f = new File(path);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(content);
		fos.close();
	}

	/**
	 * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
	 * 
	 * @param fileName
	 * @param content
	 */
	public void appendFile(String file, byte[] conent) throws IOException {

		FileOutputStream fos = new FileOutputStream(file, true);
		fos.write(conent);
		fos.close();
	}

	/**
	 * 文件传送准备
	 * 
	 * @param req
	 */
	public void handFileReq(FileReq req) {
		FileRsp rsp = new FileRsp();
		MSGSession ms = (MSGSession) sessionPool.get(req.toUserId);
		String isAllow = GlobalsTool.getSysSetting("FileTransfer");
		if ("false".equals(isAllow)) {// 判断服务器是否允许传输
			rsp.result = FileRsp.RESULT_REJECT_TRANS;
			rsp.fromUserId = req.fromUserId;
			rsp.toUserId = req.toUserId;
			rsp.fileMark = req.fileMark;
			rsp.sequence_Id = req.sequence_Id;
			connect.write(rsp);
		} else {
			// 应答请求方
			rsp.type = req.type;
			rsp.fromUserId = req.fromUserId;
			rsp.toUserId = req.toUserId;
			rsp.fileMark = req.fileMark;
			rsp.result = FileRsp.RESULT_ANSWER;
			if (FileRsp.TRAN_OFFLINE == req.type) {
				rsp.result = FileRsp.RESULT_AGREE;
				String attSize = GlobalsTool.getSysSetting("defaultAttachSize");
				long size = (long) (Double.parseDouble(attSize) * 1024 * 1024);
				if (req.fileSize > size && size > 0) {
					rsp.result = FileRsp.RESULT_REJECT_SIZE;
				}
				rsp.ip = "";
			}
			rsp.sessionKey = IDGenerater.getId(); // 生成会话Id
			rsp.sequence_Id = req.sequence_Id;
			connect.write(rsp);

			// 将请求转发给文件接收方
			if (ms != null && FileRsp.TRAN_ONLINE == req.type) {
				req.sequence_Id = rsp.sequence_Id;
				req.setSequenceID();
				ms.connect.write(req);
			}
		}
	}

	/**
	 * 文件传送准备应答
	 * 
	 * @param req
	 */
	public void handFileRsp(FileRsp rsp) {
		if (FileReq.TRAN_OFFLINE == rsp.type && IfResult.FAIL == rsp.result) {
			MSGConnectCenter.receiveMsg(rsp.sessionKey);
		}
		MSGSession ms = (MSGSession) sessionPool.get(rsp.fromUserId);
		if (ms != null) {
			ms.connect.write(rsp);
		}
	}

	/**
	 * 文件传送取消
	 * 
	 * @author PWY
	 * @param req
	 */
	public void handFileCancelReq(FileCancelReq req) {
		FileCancelRsp rsp = new FileCancelRsp();
		rsp.sequence_Id = req.sequence_Id;
		rsp.result = IfResult.RECEIVE;
		connect.write(rsp);

		MSGSession ms = (MSGSession) sessionPool.get(req.toUserId);
		// 将请求转发给文件接收方
		if (ms != null) {
			req.setSequenceID();
			ms.connect.write(req);
		}
	}

	public static void sendOfflineFile(String fromId, String toId, String dbId,
			String file) {
		MSGSession ms = (MSGSession) MSGConnectCenter.sessionPool.get(toId);
		if (null == ms) {
			return;
		}
		try {
			FileReq fileReq = new FileReq();
			fileReq.fromUserId = fromId;
			fileReq.toUserId = toId;
			fileReq.sessionKey = dbId;
			fileReq.type = FileReq.TRAN_OFFLINE;
			fileReq.fileName = file;
			fileReq.fileSize = MSGSession
					.getFileSizes(new File(BaseEnv.FILESERVERPATH + "/message/"
							+ fromId + "/" + file));
			fileReq.ip = "";
			fileReq.setSequenceID();
			ms.connect.write(fileReq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
