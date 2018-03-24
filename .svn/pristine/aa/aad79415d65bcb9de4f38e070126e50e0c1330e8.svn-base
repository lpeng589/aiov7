package com.menyi.msgcenter.server;
import org.apache.log4j.Logger;

import com.menyi.msgcenter.msgif.EmployeeItem;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
 
public class HeatbeatThread extends Thread {
    private HashMap sessionPool;
    private boolean go = true;
    private Logger log;

    public boolean init(HashMap sessionPool,Logger log) {
        this.sessionPool = sessionPool;
        this.log = log;
        this.setName("SessionClearThread");
        return true;
    }

    public void destroyObj(){
        go = false;
    }

    /**
     * @roseuid 49D1D64202EE
     */
    public void run() {
        while (go) {
            try {
                Thread.sleep(10000);
                //执行清除
                heartbeat();
                
               //遍历网页端在线用户，判断是否有下 线的
				for (OnlineUser user : OnlineUserInfo.getAllUserList()) {
					if ((MSGSession) MSGConnectCenter.sessionPool.get(user
							.getId()) == null) {
						EmployeeItem eItem = MSGConnectCenter.employeeMap
								.get(user.getId());
						if (eItem == null)
							continue;
						String type = user.getType();
						byte nType = 0;
						byte nStatus = EmployeeItem.OFFLINE;
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
						if (eItem.loginType != nType
								|| eItem.loginStatus != nStatus) {
							eItem.loginType = nType;
							eItem.loginStatus = nStatus;
							MSGConnectCenter.userStatus(user.getId(), nType,
									nStatus);
						}
					}
				}
            } catch (Exception ex) {
                log.error("SessionClearThread.run Error ",ex);
            }
        }
    }
	/**
	 * 给每个客户端发送心跳
	 *
	 */
	public void heartbeat() {
		try {
			List<MSGSession> list = new ArrayList<MSGSession>();
			Iterator it = sessionPool.keySet().iterator();
			while(it.hasNext()) {
				Object o = it.next();				
				MSGSession ms = (MSGSession)sessionPool.get(o) ;
				if(!ms.isActive){
					continue;
				}
				if (ms.lostHeartbeatTimes > 3) {
					list.add(ms);
				} else if ((System.currentTimeMillis() - ms.lastActiveTime) >= 30 * 1000) {
					ms.sendHeartbeat();
					ms.lastActiveTime=System.currentTimeMillis();
				}
				
			}
			
			for (MSGSession ms : list) {
				if (null != ms.connect) {
					ms.connect.close("失去心跳");
				} else if (null != ms.fileTranConnect) {
					ms.fileTranConnect.close("失去心跳");
				} else {
					ms.close();
				}
			}
		} catch (Exception ex) {
			log.error("MSGConnectCenter.heartbeat Error ", ex);
		}
	}
}
