package com.menyi.web.util;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import com.menyi.aio.web.login.LoginBean;
import com.menyi.msgcenter.msgif.EmployeeItem;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.msgcenter.server.MSGSession;

import java.util.ArrayList;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class OnlineUserInfo {
	private static HashMap map = new HashMap();
	/**
	 * 微信企业号在线map
	 */
    private static HashMap wxqyMap = new HashMap();
    //private static HashMap<String,LoginBean> loginBeanMap = new HashMap<String,LoginBean> ();

    private static boolean sysLock = false;
    private static String LockOper = "";

    //用于确定要跳转到的锁界面（月结时跳转到月结界面，其他动作还是原来的通用锁界面）
    public static void setLockOper(String lockOper){
    	LockOper = lockOper;
    }
    public static String getLockOper(){
    	return LockOper;
    }
    /**
     * 取锁状态
     * @return boolean
     */
    public static boolean lockState(){
        return sysLock;
    }

    /**
     * 锁系统，当反开帐，开帐，月结，反月结，年结存时必须调用此方法锁住系统，以免其它用户进行操作
     */
    public static void lockSystem(){
        sysLock = true;
    }
    /**
     * 解锁系统，当反开帐，开帐，月结，反月结，年结存时必须锁住系统，以免其它用户进行操作，但执行完毕后需解锁
     */
    public static void unlockSystem(){
        sysLock = false;
    }

    /**
     * 判断系统是否有在线用户
     * @return boolean
     */
    public static boolean hasOnlineUser() {
        Date curDate = new Date();
        for (OnlineUser user : getAllUserList()) {
            if (user.isOnline())
                return true;
        }
        return false;
    }
    
    public static LoginBean getLoginBean(String id,String name){
    	return new LoginBean(id,name);
//    	LoginBean lb = loginBeanMap.get(id);
//    	if(lb == null){
//    		lb = new LoginBean(id,name);
//    		return lb;
//    	}
//    	clearLoginBean(lb);
//    	return lb;
    }
    private static void clearLoginBean(LoginBean lb){
    	lb.setDefaultPage(null);
    	lb.setDefLoc(null);
    	lb.setDefStyle(null);
    	lb.setDefSys(null);
    	lb.setSunCompany(null);
    	lb.setSunCmpClassCode(null);
    	lb.setMenus(null);
    	lb.setLoginTime(null);
    	lb.setOperationMap(null);
    	lb.setOperationMapKeyId(null);
    	lb.setRoleModuleList(null);
    	lb.setLbxMonitorCh(null);
    	lb.setTelPrefix(null);
    	lb.setTelAreaCode(null);
    	lb.setDefStyle(null);
    	lb.setEmpFullName(null);
    	lb.setDepartmentName (null);
    	lb.setDepartmentManager (null);
    	lb.setGroupId (null);
    	lb.setDepartCode (null);
    	lb.setPromptSound(true);
    	lb.setPhoto (null);
    	lb.setMobile(null);
    	lb.setShopName (null);
    	lb.setShopPwd (null);
        
    	lb.setDefaultPage (null);
        
    	lb.setTelpop(null);
        
    	lb.setExtension(null);    	
    }

    public static void userLogin(LoginBean loginBean, String session) {
        OnlineUser user = (OnlineUser)map.get(loginBean.getId());
        if(user != null){
	        user.session = session;
	        user.activeDate = new Date();
        }
    }
    /**
     * 微信企业号登陆添加在线
     * @param loginBean
     * @param session
     */
    public static void wxqyUserLogin(LoginBean loginBean, String session) {
        OnlineUser user = (OnlineUser)wxqyMap.get(loginBean.getId());
        if(user != null){
	        user.session = session;
	        user.activeDate = new Date();
        }
    }
    /**
     * 设置非不线的用户s
     * @param id String
     * @param name String
     * @param departmentName String
     */
    public static void putUser(String id, String name,String deptId,String departmentName,String session,
    		Date activeDate,String titleID,String statusId,String sysName,String photo, String empNumber,String post,String gender,int isPublic) {
        map.put(id, new OnlineUser(id, name, deptId,departmentName,session,activeDate,titleID,statusId,sysName,photo, empNumber,post,gender,isPublic));
        putWxqyUser( id,  name, deptId, departmentName, session,
        		 activeDate, titleID, statusId, sysName, photo,  empNumber, post, gender,isPublic);
    }
    /**
     * 初始化微信企业号不在线用户
     * @param id String
     * @param name String
     * @param departmentName String
     */
    private static void putWxqyUser(String id, String name,String deptId,String departmentName,String session,
    		Date activeDate,String titleID,String statusId,String sysName,String photo, String empNumber,String post,String gender,int isPublic) {
        wxqyMap.put(id, new OnlineUser(id, name, deptId,departmentName,session,activeDate,titleID,statusId,sysName,photo, empNumber,post,gender,isPublic));
    }

    public static void offlineUser(String id) {
        OnlineUser user = (OnlineUser) map.get(id);
        if(user != null)
            user.getActiveDate().setTime(0l);
    }

    public static boolean checkUser(HttpServletRequest request) {
        Object o = request.getSession().getAttribute("LoginBean");
        if (o != null) {
            LoginBean bean = (LoginBean) o;
            OnlineUser user = (OnlineUser) map.get(bean.getId());
            if (user != null) {
                if (user.session.equals(request.getSession().getId())) {
                    return true;
                }
            }else{
            	return true;
            }
            /* 微信端不控制重复登陆
            user = (OnlineUser) wxqyMap.get(bean.getId());
            if (user != null) {
                if (user.session.equals(request.getSession().getId())) {
                    return true;
                }
            } */
        }
        return false;
    }
    

    public static void refresh(String id,String type) {
        Object o = map.get(id);
        if (o != null && o instanceof OnlineUser) {
        	OnlineUser user = (OnlineUser) o;
			if (type != null && "mobile".equals(type)) {
				user.mobileActiveDate = new Date();
			} else {
				user.activeDate = new Date();
			}
        }
    }
    
    public static void clearUser(){
        map.clear();
    }

    public static HashMap cloneMap(){
        return (HashMap)map.clone();
    }

    public static OnlineUser[] getAllUserList() {
    	OnlineUser[] arrayUser = (OnlineUser[]) map.values().toArray(new OnlineUser[0]);
        return arrayUser;
    }
    public static OnlineUser[] getAllWxqyUserList() {
    	OnlineUser[] arrayUser = (OnlineUser[]) wxqyMap.values().toArray(new OnlineUser[0]);
        return arrayUser;
    }

    public static String[] getAllDept(){
        HashMap depmap = new HashMap();
        for(OnlineUser user:getAllUserList()){
            depmap.put(user.getDepartmentName(),user.getDepartmentName());
        }
        return (String[])depmap.values().toArray(new String[0]);
    }
    
    public static OnlineUser[] getAllDept2(){
        HashMap depmap = new HashMap();
        for(OnlineUser user:getAllUserList()){
            depmap.put(user.getDepartmentName(),user);
        }
        return (OnlineUser[])depmap.values().toArray(new OnlineUser[0]);
    }
    
    public static OnlineUser getDept(String deptId){
    	for(OnlineUser user:getAllUserList()){
    		if(deptId!=null && deptId.equals(user.getDeptId())){
    			return user ;
    		}
    	}
        return null ;
    }
    
    /**
     * 判断部门或父级部门是否存在，
     * @param deptId
     * @return
     */
    public static OnlineUser existDept(String deptId){
    	for(OnlineUser user:getAllUserList()){
    		if(deptId!=null && user.getDeptId().indexOf(deptId)==0){
    			return user ;
    		}
    	}
        return null ;
    }
    
    public static ArrayList<OnlineUser> getTitleUsers(String titleId){
    	ArrayList<OnlineUser> titleUser = new ArrayList<OnlineUser>();
        for(OnlineUser user:getAllUserList()){
        	if(user.getTitleID().equals(titleId)){
        		titleUser.add(user);
        	}
        }
        return titleUser;
    }
    
    public static ArrayList<OnlineUser> getPostUsers(String postId){
    	ArrayList<OnlineUser> titleUser = new ArrayList<OnlineUser>();
        for(OnlineUser user:getAllUserList()){
        	if(user.post.equals(postId)){
        		titleUser.add(user);
        	}
        }
        return titleUser;
    }
    
    
    /**
     * 取在线用户和离线用户，用数据同时返回，避免因计算不同步导致用户数不正确
     * @return ArrayList[] 0 在线用户，1离线用户
     */
    public static ArrayList[] getOnlineOfflineUser(String departmentName){
        ArrayList onlinelist = new ArrayList();
        ArrayList offlinelist = new ArrayList();
        Date curDate = new Date();
      
        for(OnlineUser user:getAllUserList()){
        	if("0".equals(user.statusId) && !"".equals(user.sysName) && (departmentName==null || departmentName.length() == 0 || departmentName.equals(user.getDepartmentName()))){
	            if(user.isOnline()){
	                onlinelist.add(user);
	            }else{
	                offlinelist.add(user);
	            }
        	}
        }

    	Collections.sort(onlinelist, new Comparator<OnlineUser>() {

			public int compare(OnlineUser arg0, OnlineUser arg1) {
				// TODO Auto-generated method stub
				return arg0.getEmpNumber().compareTo(arg1.getEmpNumber());
			}
		});
        return new ArrayList[]{onlinelist,offlinelist};
    }
    
    
    /**
     * 得到某个部门 下所有人
     * @param deptId
     * @return
     */
    public static ArrayList<OnlineUser> getDeptUser(String deptId){
        ArrayList<OnlineUser> deptUser = new ArrayList<OnlineUser>();
        for(OnlineUser user:getAllUserList()){
            if(deptId!=null &&user.getDeptId().startsWith(deptId) ){
            	deptUser.add(user) ;
            }
        }
        return deptUser ;
    }
    
    public static OnlineUser getUser(String id){
    	OnlineUser user = (OnlineUser) map.get(id);
        return user ;
    }
    
    public static OnlineUser getUserBySysName(String sysName){
    	for(OnlineUser user:getAllUserList()){
            if(user.getSysName().equals(sysName)){
            	return user ;
            }
        }
        return null ;
    }
    
    public static OnlineUser getUserByEmpName(String empName){
    	for(OnlineUser user:getAllUserList()){
            if(user.getName().equals(empName)){
            	return user ;
            }
        }
        return null ;
    }
    
    public static class OnlineUser {
        public String id;
        public String name;
        public String session;
        public String departmentName;
        public String TitleID;
        public String post;//岗位
        public String deptId ;
        public String statusId;
        public String sysName;
        public String pingying;
        public String photo;
        public String empNumber;//职员编号
        public String gender;//性别
        public int isPublic = 0;

        /**************以下代码不要轻易修改*****************/
        private Date activeDate;	// 记录网页有没超时
        private Date mobileActiveDate;	// 记录手机有没超时
        
		public String getType() {
			// 显示优先级：kk,mobile,web			
			if (null != MSGConnectCenter.sessionPool.get(this.id)) {
				return "kk";
			}

	        Date curDate = new Date();
            if (null != this.mobileActiveDate && curDate.getTime() - 10 * 60000 <= this.mobileActiveDate.getTime()) {
            	return "mobile";
            } else if (null != this.activeDate && curDate.getTime() - 2 * 60000 <= this.activeDate.getTime()) {
            	return "web";
            } else {
            	return "offline";
            }
		}
		
		public boolean isOnline() {
			return !"offline".equals(getType());
		}

		public Date getActiveDate() {
            return activeDate;
        }

		public Date getMobileActiveDate() {
            return mobileActiveDate;
        }
		
        /**************以上代码不要轻易修改*****************/

		
        public OnlineUser(String id, String name,String deptId,
                          String departmentName,String session,Date activeDate,String TitleID,
                          String statusId,String sysName,String photo,String empNumber,String post,String gender,int isPublic) {
            this.id = id;
            this.name = name;
            this.activeDate = activeDate;
            this.deptId = deptId ;
            this.departmentName = departmentName;
            this.session = session;
            this.TitleID=TitleID;
            this.statusId=statusId;
            this.sysName=sysName;
            this.pingying = ChineseSpelling.getSelling(name) + "," + ChineseSpelling.getPYM(name) ;
            this.photo = photo;
            this.empNumber = empNumber;
            this.post = post;
            this.gender = gender;
            this.isPublic=isPublic;
        }
        
        
        
		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSession() {
			return session;
		}

		public void setSession(String session) {
			this.session = session;
		}

		public String getDepartmentName() {
			return departmentName;
		}

		public void setDepartmentName(String departmentName) {
			this.departmentName = departmentName;
		}

		public String getTitleID() {
			return TitleID;
		}

		public void setTitleID(String titleID) {
			TitleID = titleID;
		}

		public String getPost() {
			return post;
		}

		public void setPost(String post) {
			this.post = post;
		}

		public String getDeptId() {
			return deptId;
		}

		public void setDeptId(String deptId) {
			this.deptId = deptId;
		}

		public String getStatusId() {
			return statusId;
		}

		public void setStatusId(String statusId) {
			this.statusId = statusId;
		}

		public String getSysName() {
			return sysName;
		}

		public void setSysName(String sysName) {
			this.sysName = sysName;
		}

		public String getPingying() {
			return pingying;
		}

		public void setPingying(String pingying) {
			this.pingying = pingying;
		}

		public String getPhoto() {
			return photo;
		}

		public void setPhoto(String photo) {
			this.photo = photo;
		}

		public String getEmpNumber() {
			return empNumber;
		}

		public void setEmpNumber(String empNumber) {
			this.empNumber = empNumber;
		}

		
	
    }
}
