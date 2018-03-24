package com.menyi.aio.web.login;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Column;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 天创软件</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class LoginBean implements Serializable{


    private String id;
    private String name;
    private String sunCompany;//分支机构ID
    private String sunCmpClassCode;//分支机构的ClassCode
//    private List operation;
    private HashMap menus;
    private String loginTime;
    private HashMap operationMap;
    private HashMap operationMapKeyId;
    private String defSys;
    private String defLoc ;
    private ArrayList roleModuleList;
    private HashMap roleModuleScopeMap;
    private ArrayList<LoginScopeBean> allScopeRight; //存储应用于所有模块的范围权限
    private String lbxMonitorCh;//用户管理的来电通道
    private String telPrefix;//电话前缀，如外线号
    private String telAreaCode;//用户打电话时所在地的区位码。
    private String defStyle;
    private String empFullName;
    private String departmentName ;
    private String departmentManager ;
    private String groupId ;
    private String departCode ;
    private boolean promptSound = true;//声音提醒
    private String photo ;
    private String mobile;
    private String shopName ;
    private String shopPwd ;
    private String system ;
    private String defaultPage ;
    private String telpop;
    private String extension;
    private String viewTopMenu;
    private String viewLeftMenu;
    private String jessionid ;
    private String moduleId ;//默认模板Id
    private String titleId; //职位Id
	private int canJxc;
	private int canOa;
	private int canCrm;
	private int canHr;
	private int canOrders;
	private String defDesk;
	private String email;
	private String qq;
	private String showDesk;	//显示那个桌面
	private String firstShow;	//第一次显示帮助信息
	private int showWebNote;
	
	
	private Hashtable sessMap = new Hashtable();
	
	
	
	private String hiddenField;//隐藏成本价等信息
	
	
	
	private HashMap propMap = new HashMap(); //用于存储和用户相关的一些会话变量
	
	
	
	public int getShowWebNote() {
		return showWebNote;
	}

	public void setShowWebNote(int showWebNote) {
		this.showWebNote = showWebNote;
	}

	public ArrayList<LoginScopeBean> getAllScopeRight() {
		return allScopeRight;
	}

	public void setAllScopeRight(ArrayList<LoginScopeBean> allScopeRight) {
		this.allScopeRight = allScopeRight;
	}

	public HashMap getPropMap(){
		return propMap;		
	}
    
	public ArrayList getModules(){
		ArrayList list = new ArrayList();
		if(canJxc==1){
			list.add(1);
		}
		if(canOa==1){
			list.add(2);
		}
		if(canCrm==1){
			list.add(3);
		}
		if(canHr==1){
			list.add(4);
		}
		return list;
	}

	public String getShowDesk() {
		return showDesk;
	}

	public void setShowDesk(String showDesk) {
		this.showDesk = showDesk;
	}

	public int getCanCrm() {
		return canCrm;
	}
	public void setCanCrm(int canCrm) {
		this.canCrm = canCrm;
	}
	public int getCanHr() {
		return canHr;
	}
	public void setCanHr(int canHr) {
		this.canHr = canHr;
	}
	public int getCanJxc() {
		return canJxc;
	}
	public void setCanJxc(int canJxc) {
		this.canJxc = canJxc;
	}
	public int getCanOa() {
		return canOa;
	}
	public void setCanOa(int canOa) {
		this.canOa = canOa;
	}
	public int getCanOrders() {
		return canOrders;
	}
	public void setCanOrders(int canOrders) {
		this.canOrders = canOrders;
	}
	public String getViewLeftMenu() {
		return viewLeftMenu;
	}
	public void setViewLeftMenu(String viewLeftMenu) {
		this.viewLeftMenu = viewLeftMenu;
	}
	public String getViewTopMenu() {
		return viewTopMenu;
	}
	public void setViewTopMenu(String viewTopMenu) {
		this.viewTopMenu = viewTopMenu;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getDefaultPage() {
		return defaultPage;
	}
	public void setDefaultPage(String defaultPage) {
		this.defaultPage = defaultPage;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public boolean getPromptSound() {
		return promptSound;
	}
	public void setPromptSound(boolean promptSound) {
		this.promptSound = promptSound;
	}
	public String getDefStyle() {
		return defStyle;
	}
	public void setDefStyle(String defStyle) {
		this.defStyle = defStyle;
	}
    public LoginBean(String id,String name){
        this.id = id;
        this.name = name;
        this.sunCmpClassCode="00001";
    }
    public LoginBean(){
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSunCompany() {
          return sunCompany;
    }
    public String getSunCmpClassCode() {
         return sunCmpClassCode;
    }
    public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopPwd() {
		return shopPwd;
	}
	public void setShopPwd(String shopPwd) {
		this.shopPwd = shopPwd;
	}
	public String getLoginTime() {
        return loginTime;
    }
    public HashMap getOperationMap() {
        return operationMap;
    }
    public HashMap getOperationMapKeyId() {
        return operationMapKeyId;
    }
    public String getDefSys() {
        return defSys;
    }
    

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSunCompany(String sunCompany) {
         this.sunCompany = sunCompany;
    }
    public void setSunCmpClassCode(String sunCmpClassCode) {
       this.sunCmpClassCode = sunCmpClassCode;
  }


//    public void setOperation(List operation) {
//
//        this.operation = operation;
//    }


    public void setMenus(HashMap menus) {
        this.menus = menus;
    }
    public ArrayList getMenus(String sysType) {
        if(this.menus == null) return null;
        return (ArrayList)this.menus.get(sysType);
    }
    public HashMap getMenus(){
    	return this.menus;
    }


    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public void setOperationMap(HashMap operationMap) {
        this.operationMap = operationMap;
    }

    public void setOperationMapKeyId(HashMap operationMapKeyId) {
        this.operationMapKeyId = operationMapKeyId;
    }

    public void setDefSys(String defSys) {
        this.defSys = defSys;
    }

	public String getLbxMonitorCh() {
		return lbxMonitorCh;
	}
	public void setLbxMonitorCh(String lbxMonitorCh) {
		this.lbxMonitorCh = lbxMonitorCh;
	}
	public String getEmpFullName() {
		return empFullName;
	}
	public void setEmpFullName(String empFullName) {
		this.empFullName = empFullName;
	}
	public String getDefLoc() {
		return defLoc;
	}
	public void setDefLoc(String defLoc) {
		this.defLoc = defLoc;
	}
	public String getTelAreaCode() {
		return telAreaCode;
	}
	public void setTelAreaCode(String telAreaCode) {
		this.telAreaCode = telAreaCode;
	}
	public String getTelPrefix() {
		return telPrefix;
	}
	public void setTelPrefix(String telPrefix) {
		this.telPrefix = telPrefix;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}


	public String getTelpop() {
		return telpop;
	}
	public void setTelpop(String telpop) {
		this.telpop = telpop;
	}
	public String getDepartmentManager() {
		return departmentManager;
	}
	public void setDepartmentManager(String departmentManager) {
		this.departmentManager = departmentManager;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getDepartCode() {
		return departCode;
	}
	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getJessionid() {
		return jessionid;
	}
	public void setJessionid(String jessionid) {
		this.jessionid = jessionid;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getTitleId() {
		return titleId;
	}
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public String getDefDesk() {
		return defDesk;
	}

	public void setDefDesk(String defDesk) {
		this.defDesk = defDesk;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public ArrayList getRoleModuleList() {
		return roleModuleList;
	}

	public void setRoleModuleList(ArrayList roleModuleList) {
		this.roleModuleList = roleModuleList;
	}


	public HashMap getRoleModuleScopeMap() {
		return roleModuleScopeMap;
	}

	public void setRoleModuleScopeMap(HashMap roleModuleScopeMap) {
		this.roleModuleScopeMap = roleModuleScopeMap;
	}

	public String getHiddenField() {
		return hiddenField;
	}

	//设置隐藏成本价等
	public void setHiddenField(String hiddenField) {
		if(this.hiddenField==null){
			this.hiddenField = "";
		}
		if(hiddenField != null){	
			String[] hfs = hiddenField.split(",");
			for(String hf:hfs){
				if(hf.length() > 0 && !(","+this.hiddenField).contains(","+hf)){
					this.hiddenField +=","+hf;
				}
			}
		}
	}

	public Hashtable getSessMap() {
		return sessMap;
	}

	public void setSessMap(Hashtable sessMap) {
		this.sessMap = sessMap;
	}

	public String getFirstShow() {
		return firstShow;
	}

	public void setFirstShow(String firstShow) {
		this.firstShow = firstShow;
	}

	
	
}
