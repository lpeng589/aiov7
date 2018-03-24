package com.menyi.aio.web.wxqy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.bean.RoleBean;
import com.menyi.aio.bean.RoleModuleBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginAction;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.menu.MenuManageAction;
import com.menyi.aio.web.role.RoleMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.SystemState;

public class WeixinApiMgt {
	/**
	 * 获取用户权限
	 * @param request
	 * @return
	 */
	public static LoginBean getLoginBean(HttpServletRequest request,LoginBean loginBean,EmployeeBean userBean){
		
		if (null == userBean) {
			return null;
		}
        ArrayList roleModuleList = new ArrayList();
        //查找用户权限
        //这里先只查找角色权限
        //如果管理员，不用查角色，直接拥有所有权限
        ArrayList allScopeRight = new ArrayList(); //记录应用于所有模块的范围权限
        HashMap roleModuleScopeMap = new HashMap();
        if(!"admin".equalsIgnoreCase(userBean.getSysName())){
            ArrayList roles = (ArrayList) new LoginAction().getRoles(userBean, "1");
        	//用户本身也是一个角色
        	RoleBean selfRb = new RoleBean();
        	selfRb.setId(userBean.getId());
        	selfRb.setRoleName(userBean.getEmpFullName());
        	selfRb.setRoleDesc(userBean.getEmpFullName());
        	selfRb.setHiddenField(userBean.getHiddenField());                	
        	roles.add(selfRb);
        	
            for (Object o : roles) {
                RoleBean rb = (RoleBean) o;
                
                //查询角色模块权限
                Result result = new RoleMgt().queryRoleModuleByRoleid(rb.getId(),userBean.getId());
                if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                	//合并所有模块权限
                	roleModuleList.addAll((List)result.getRetVal());
                }
                
                //查询角色范围权限
                result = new RoleMgt().queryRoleScopyByRoleid(rb.getId(),userBean.getId(),userBean.getEmpFullName(),userBean.getDepartmentCode());
                if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                	//合并所有范围权限
                	HashMap hm = (HashMap)result.getRetVal();
                	for(Object hmo:hm.keySet()){
                		ArrayList list = (ArrayList)roleModuleScopeMap.get(hmo);
                		if(list==null){
                			roleModuleScopeMap.put(hmo, hm.get(hmo));
                		}else{
                			list.addAll((List)hm.get(hmo));
                		}
                	}
                
                }
            }
        
            //查询应用于所有模块范围权限------------------------------------
            Result result = new RoleMgt().queryAllModScope(userBean.getId(),userBean.getDepartmentCode()) ;
            if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
            	allScopeRight =(ArrayList)result.retVal ;
            }
        }
        loginBean.setId(userBean.getId()) ;
        loginBean.setName(userBean.getSysName()) ;
        loginBean.setMobile(userBean.getMobile());
        loginBean.setDefSys(userBean.getDefSys());
        loginBean.setLoginTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
        loginBean.setEmpFullName(userBean.getEmpFullName());
        loginBean.setDepartCode(userBean.getDepartmentCode()) ;
        loginBean.setSunCmpClassCode("00001");
        loginBean.setSunCompany("1");
        loginBean.setAllScopeRight(allScopeRight);
        loginBean.setRoleModuleList(roleModuleList);
        loginBean.setRoleModuleScopeMap(roleModuleScopeMap);
        for(Object o:SystemState.instance.moduleList){
        	if(o.toString().equals("1") && userBean.getCanJxc()==1){
        		loginBean.setCanJxc(1);
        	}
        	if(o.toString().equals("2") && userBean.getCanOa()==1){
        		loginBean.setCanOa(1);
        	}
        	if(o.toString().equals("3") && userBean.getCanCrm()==1){
        		loginBean.setCanCrm(1);
        	}
        	if(o.toString().equals("4") && userBean.getCanHr()==1){
        		loginBean.setCanHr(1);
        	}
        	
        }
        if(SystemState.instance.funOrders && userBean.getCanOrders()==1){
    		loginBean.setCanOrders(1);
    	}
        
        //职员分组ID
        String groupIds = (String) new DynDBManager().getGroupIds(userBean.getId()).getRetVal() ;
        loginBean.setGroupId(groupIds) ;
        
//        //如果所取menu为空，说明还未生成过菜单，必须先生成所有菜单，否则直接取菜单数据
        String locale = GlobalsTool.getLocale(request).toString();

        ArrayList userMenu = null;
        HashMap userMenus = new HashMap(); //保存所有菜单，包括oa,crm
        HashMap operationMap = new HashMap();
        HashMap operationMapKeyId = new HashMap();
        //如果所取menu为空，说明还未生成过菜单，必须先生成所有菜单，否则直接取菜单数据
        if(!"admin".equalsIgnoreCase(loginBean.getName())&&loginBean.getMenus() == null){

            for (int defSys = 0; defSys <= 4; defSys++) {
                //生成菜单
                if(!GlobalsTool.hasMainModule(defSys+"")){
                    continue;
                }
                if(defSys == 1 && loginBean.getCanJxc()!=1){
                	continue;
                }
                if(defSys == 2 && loginBean.getCanOa()!=1){
                	continue;
                }
                if(defSys == 3 && loginBean.getCanCrm()!=1){
                	continue;
                }
                if(defSys == 4 && loginBean.getCanHr()!=1){
                	continue;
                }

                userMenu = new ArrayList();
                for (int i = 0; i < loginBean.getRoleModuleList().size(); i++) {
                    RoleModuleBean rmBean = (RoleModuleBean) (loginBean.getRoleModuleList()).get(i);
                    Result rtemp = new Result();
                    //根据 ModuleBeanId 从菜单中找到对象的ModuleBean 对象
                    //找到用户的当前系统，如无则系默认一个

                    MenuManageAction.userMouleRec((ArrayList) BaseEnv.moduleMap.get(defSys +""), 
                    			  rmBean.getModuleOpBean().getModuleBean().getId(), rtemp);
                    if (rtemp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        //找到对应的菜单操作项
                        ModuleBean moduleBean = (ModuleBean)rtemp.getRetVal();
                        //通过ModuleBean 将整个菜单树，一级级添加到菜单树中去
                        ModuleBean lastBean = MenuManageAction.userMenuRec(moduleBean,userMenu);

                        //取操作项加入operationMap中,用于模块按扭鉴权
                        String url = moduleBean.getLinkAddress();
                        int lastIndex = 0 ;
                        if(url!=null && url.contains("UserFunctionQueryAction.do")){
                            if(url.indexOf("parentTableName=")!=-1){
                            	lastIndex = url.indexOf("&",url.indexOf("&tableName=")+11) ;
                            	if(lastIndex!=-1){
                            		url = url.substring(0,lastIndex) ;
                            	}
                            }else if(url.indexOf("&moduleType=")!=-1){
                            	lastIndex = url.indexOf("&", url.indexOf("&moduleType=")+12) ;
                            	if(lastIndex!=-1){
                            		url = url.substring(0,lastIndex) ;
                            	}
                            }else{
                            	if(url.contains("&")){
                            		url = url.substring(0,url.indexOf("&",url.indexOf("?tableName"))) ;
                            	}
                            }
                        }
                        MOperation mop = (MOperation) operationMap.get(url);
                        if (mop == null) {
                            mop = new MOperation();
                            mop.moduleUrl = url;
                            mop.moduleId = moduleBean.getId().trim();
                            mop.isMobile= moduleBean.getIsMobile()==null?2:moduleBean.getIsMobile();
                            mop.icon = moduleBean.getIcon();
                            operationMap.put(url, mop);
                            operationMapKeyId.put(mop.moduleId, mop); //用于弹出窗的鉴权
                        }
                        mop.setOperation(rmBean.getModuleOpBean().getOperationID());
                        ArrayList roleModuleSm = (ArrayList)loginBean.getRoleModuleScopeMap().get(rmBean.getModuleOpBean().getModuleOpId()+"");
                        if(roleModuleSm != null){                        	
                        	mop.setScope(rmBean.getModuleOpBean().getOperationID(),roleModuleSm);
                        }
                    }
                }
                userMenus.put(defSys+"", userMenu);               
            }
            //清空login中角色，和角色权限内存
            loginBean.setRoleModuleList(null);
            
            loginBean.setMenus(userMenus);
            loginBean.setOperationMap(operationMap);
            
            loginBean.setOperationMapKeyId(operationMapKeyId);
            
            //setBrotherTableRight(loginBean, operationMap,operationMapKeyId) ;
            //设置工作流的权限，这里修改后只在第一次登陆时设置一次，如权限发生变化，用户需重新登陆一次 -- 周新宇
            MenuManageAction.setWorkFlowRight(locale,loginBean,operationMap,operationMapKeyId);
            //设置工作计划 和 事件计划 的权限
            MenuManageAction.setWorkPlanRight(loginBean, operationMap,operationMapKeyId) ;
            
        }else if("admin".equalsIgnoreCase(loginBean.getName())&&loginBean.getOperationMap() == null){
            //设置系统默认的所有权限
            loginBean.setOperationMap(BaseEnv.adminOperationMap);
            loginBean.setOperationMapKeyId(BaseEnv.adminOoperationMapKeyId);
            
            //setBrotherTableRight(loginBean, BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId) ;
            //设置工作流的权限，这里修改后只在第一次登陆时设置一次，如权限发生变化，用户需重新登陆一次 -- 周新宇
            MenuManageAction.setWorkFlowRight(locale,loginBean,BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId);
            //设置工作计划 和 事件计划 的权限
            MenuManageAction.setWorkPlanRight(loginBean, BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId) ;
            
       }
        return loginBean ;
	}
	/**
	 * 企业号返回json格式数据
	 * @param request
	 * @param mapping
	 * @param c
	 * @return
	 */
	public static ActionForward wxqyResponse(HttpServletRequest request,ActionMapping mapping,Object c) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		request.setAttribute("msg", gson.toJson(c));
		return mapping.findForward("blank");
	}
	/**
	 * 判断是不是微信企业号来的请求
	 * @param request
	 * @return
	 */
	public static boolean checkWxqy(HttpServletRequest request) {
		if(request.getParameter("wxqycheck")!=null){
			return true;
		}else {
			return false;
		}
	}
}
