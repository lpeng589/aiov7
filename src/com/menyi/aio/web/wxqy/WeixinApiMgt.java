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
	 * ��ȡ�û�Ȩ��
	 * @param request
	 * @return
	 */
	public static LoginBean getLoginBean(HttpServletRequest request,LoginBean loginBean,EmployeeBean userBean){
		
		if (null == userBean) {
			return null;
		}
        ArrayList roleModuleList = new ArrayList();
        //�����û�Ȩ��
        //������ֻ���ҽ�ɫȨ��
        //�������Ա�����ò��ɫ��ֱ��ӵ������Ȩ��
        ArrayList allScopeRight = new ArrayList(); //��¼Ӧ��������ģ��ķ�ΧȨ��
        HashMap roleModuleScopeMap = new HashMap();
        if(!"admin".equalsIgnoreCase(userBean.getSysName())){
            ArrayList roles = (ArrayList) new LoginAction().getRoles(userBean, "1");
        	//�û�����Ҳ��һ����ɫ
        	RoleBean selfRb = new RoleBean();
        	selfRb.setId(userBean.getId());
        	selfRb.setRoleName(userBean.getEmpFullName());
        	selfRb.setRoleDesc(userBean.getEmpFullName());
        	selfRb.setHiddenField(userBean.getHiddenField());                	
        	roles.add(selfRb);
        	
            for (Object o : roles) {
                RoleBean rb = (RoleBean) o;
                
                //��ѯ��ɫģ��Ȩ��
                Result result = new RoleMgt().queryRoleModuleByRoleid(rb.getId(),userBean.getId());
                if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                	//�ϲ�����ģ��Ȩ��
                	roleModuleList.addAll((List)result.getRetVal());
                }
                
                //��ѯ��ɫ��ΧȨ��
                result = new RoleMgt().queryRoleScopyByRoleid(rb.getId(),userBean.getId(),userBean.getEmpFullName(),userBean.getDepartmentCode());
                if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
                	//�ϲ����з�ΧȨ��
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
        
            //��ѯӦ��������ģ�鷶ΧȨ��------------------------------------
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
        
        //ְԱ����ID
        String groupIds = (String) new DynDBManager().getGroupIds(userBean.getId()).getRetVal() ;
        loginBean.setGroupId(groupIds) ;
        
//        //�����ȡmenuΪ�գ�˵����δ���ɹ��˵����������������в˵�������ֱ��ȡ�˵�����
        String locale = GlobalsTool.getLocale(request).toString();

        ArrayList userMenu = null;
        HashMap userMenus = new HashMap(); //�������в˵�������oa,crm
        HashMap operationMap = new HashMap();
        HashMap operationMapKeyId = new HashMap();
        //�����ȡmenuΪ�գ�˵����δ���ɹ��˵����������������в˵�������ֱ��ȡ�˵�����
        if(!"admin".equalsIgnoreCase(loginBean.getName())&&loginBean.getMenus() == null){

            for (int defSys = 0; defSys <= 4; defSys++) {
                //���ɲ˵�
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
                    //���� ModuleBeanId �Ӳ˵����ҵ������ModuleBean ����
                    //�ҵ��û��ĵ�ǰϵͳ��������ϵĬ��һ��

                    MenuManageAction.userMouleRec((ArrayList) BaseEnv.moduleMap.get(defSys +""), 
                    			  rmBean.getModuleOpBean().getModuleBean().getId(), rtemp);
                    if (rtemp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        //�ҵ���Ӧ�Ĳ˵�������
                        ModuleBean moduleBean = (ModuleBean)rtemp.getRetVal();
                        //ͨ��ModuleBean �������˵�����һ������ӵ��˵�����ȥ
                        ModuleBean lastBean = MenuManageAction.userMenuRec(moduleBean,userMenu);

                        //ȡ���������operationMap��,����ģ�鰴Ť��Ȩ
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
                            operationMapKeyId.put(mop.moduleId, mop); //���ڵ������ļ�Ȩ
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
            //���login�н�ɫ���ͽ�ɫȨ���ڴ�
            loginBean.setRoleModuleList(null);
            
            loginBean.setMenus(userMenus);
            loginBean.setOperationMap(operationMap);
            
            loginBean.setOperationMapKeyId(operationMapKeyId);
            
            //setBrotherTableRight(loginBean, operationMap,operationMapKeyId) ;
            //���ù�������Ȩ�ޣ������޸ĺ�ֻ�ڵ�һ�ε�½ʱ����һ�Σ���Ȩ�޷����仯���û������µ�½һ�� -- ������
            MenuManageAction.setWorkFlowRight(locale,loginBean,operationMap,operationMapKeyId);
            //���ù����ƻ� �� �¼��ƻ� ��Ȩ��
            MenuManageAction.setWorkPlanRight(loginBean, operationMap,operationMapKeyId) ;
            
        }else if("admin".equalsIgnoreCase(loginBean.getName())&&loginBean.getOperationMap() == null){
            //����ϵͳĬ�ϵ�����Ȩ��
            loginBean.setOperationMap(BaseEnv.adminOperationMap);
            loginBean.setOperationMapKeyId(BaseEnv.adminOoperationMapKeyId);
            
            //setBrotherTableRight(loginBean, BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId) ;
            //���ù�������Ȩ�ޣ������޸ĺ�ֻ�ڵ�һ�ε�½ʱ����һ�Σ���Ȩ�޷����仯���û������µ�½һ�� -- ������
            MenuManageAction.setWorkFlowRight(locale,loginBean,BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId);
            //���ù����ƻ� �� �¼��ƻ� ��Ȩ��
            MenuManageAction.setWorkPlanRight(loginBean, BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId) ;
            
       }
        return loginBean ;
	}
	/**
	 * ��ҵ�ŷ���json��ʽ����
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
	 * �ж��ǲ���΢����ҵ����������
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
