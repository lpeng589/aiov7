package com.menyi.aio.web.menu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.AdviceBean;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.bean.ModuleBean;
import com.menyi.aio.bean.RoleModuleBean;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.moduleFlow.ModuleFlowMgt;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.CallSoftDll;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.PublicMgt;
import com.menyi.web.util.SystemState;
/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com
 * @version 1.0
 */
public class MenuManageAction extends BaseAction {


    protected ActionForward doAuth(HttpServletRequest req,ActionMapping mapping) {
        ActionForward forward = null;
        Object obj = req.getSession().getAttribute("LoginBean");
        if (obj == null) {
            BaseEnv.log.debug("MenuManager.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage"); 
        }
        return forward;
    }

    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        //跟据不同操作类型分配给不同函数处理
        int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        	
	        case OperationConst.OP_QUERY:
	        	forward = query(mapping, form, request, response);
	            break;
	        default:
	        	forward = query(mapping, form, request, response);
        }
        return forward;
    }
    
    /**
     * 进入旧的系统界面
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ActionForward query(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws  Exception {

    	try{
	    	float diskSpace = CallSoftDll.getDiskTotalSpace();
	    	diskSpace = ((int)(diskSpace *100))/100.0f;
	    	if(diskSpace<2){  
	    		request.setAttribute("AlertMsg", this.getMessage(request, "diskSpace.msg",diskSpace+""));
	    	}
	    	if(SystemState.bakState != ErrorCanst.DEFAULT_SUCCESS){
	    		request.setAttribute("AlertMsg", SystemState.bakStateStr);
	    	}
    	}catch(Exception e){e.printStackTrace();}
    	
        LoginBean loginBean = getLoginBean(request);
        //*******查询当前会计期间********//
        Hashtable sessionSet = BaseEnv.sessionSet;
		Hashtable hashSession = (Hashtable) sessionSet.get(loginBean.getId());
		AccPeriodBean accPeriodBean = (AccPeriodBean) hashSession.get("AccPeriod");
		request.setAttribute("accPeriod", accPeriodBean);
		//***********end************//
        
        String strDefSys = getParameter("sysType", request);
        if(strDefSys==null){
        	strDefSys = "1";
        }
        loginBean.setDefSys(strDefSys);
        
        
        if(loginBean.getFirstShow() == null || !loginBean.getFirstShow().contains(";0;")){
        	request.setAttribute("showFirst", "true");
        }
        
        String logoPath = request.getSession().getServletContext().getRealPath("/style/v7/user") ;
    	File logoFile = new File(logoPath);
    	if(logoFile.isDirectory()){
    		File[] listFile = logoFile.listFiles() ;
    		for(File file :listFile){
    			if(file.getName().contains("company")){
    				request.setAttribute("companyLogo", "/style/v7/user/"+file.getName());
    			}
    		}
    	}
        
        

        ArrayList userMenu = null;
        HashMap userMenus = new HashMap(); //保存所有菜单，包括oa,crm
        HashMap operationMap = new HashMap();
        HashMap operationMapKeyId = new HashMap();

        //Hashtable sessionSet = BaseEnv.sessionSet;
        //Hashtable session=((Hashtable)sessionSet.get(loginBean.getId()));
        ///session.put("sysType", getParameter("sysType", request));
        String locale = GlobalsTool.getLocale(request).toString();
        /**
         * 取用户菜单和权限
         * 1、取一级菜单放入Hash表中
         * 2、再根据每级菜单读下级菜单
         * 3、读取用户权限表
         * 4、计算用户菜单和权限表
         */

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

                    userMouleRec((ArrayList) BaseEnv.moduleMap.get(defSys +""), 
                    			  rmBean.getModuleOpBean().getModuleBean().getId(), rtemp);
                    if (rtemp.retCode != ErrorCanst.DEFAULT_SUCCESS) {
                        //找到对应的菜单操作项
                        ModuleBean moduleBean = (ModuleBean)rtemp.getRetVal();
                        //通过ModuleBean 将整个菜单树，一级级添加到菜单树中去
                        ModuleBean lastBean = userMenuRec(moduleBean,userMenu);

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
            setWorkFlowRight(locale,loginBean,operationMap,operationMapKeyId);
            //设置工作计划 和 事件计划 的权限
            setWorkPlanRight(loginBean, operationMap,operationMapKeyId) ;
            
        }else if("admin".equalsIgnoreCase(loginBean.getName()) &&loginBean.getMenus() == null){
            //设置系统默认的所有权限
             loginBean.setOperationMap(BaseEnv.adminOperationMap);
             loginBean.setOperationMapKeyId(BaseEnv.adminOoperationMapKeyId);
             
             //setBrotherTableRight(loginBean, BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId) ;
             //设置工作流的权限，这里修改后只在第一次登陆时设置一次，如权限发生变化，用户需重新登陆一次 -- 周新宇
             setWorkFlowRight(locale,loginBean,BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId);
             //设置工作计划 和 事件计划 的权限
             setWorkPlanRight(loginBean, BaseEnv.adminOperationMap,BaseEnv.adminOoperationMapKeyId) ;
             
        }
      
        
        if("true".equals(request.getParameter("loadMenu"))){
	        //生成菜单--------------------------
	        long cur = System.currentTimeMillis();
	        StringBuffer sb = new StringBuffer();
	        //如果非admin用户
	        ArrayList menu;
	        if(!"admin".equalsIgnoreCase(loginBean.getName())){
	            menu = loginBean.getMenus(loginBean.getDefSys());
	        }else{
	            //admin用户直接取系统菜单
	            menu = (ArrayList) BaseEnv.moduleMap.get(loginBean.getDefSys());
	        }
	        if (menu!=null && menu.size() > 0) {
	            for (int i = 0; i < menu.size(); i++) {
	                ModuleBean moduleBean = (ModuleBean) menu.get(i);
	                if(moduleBean.getIsHidden()!=1){
	                	boolean falg = false;
	                	if(moduleBean.getChildList() != null){
	                		ArrayList moduleList = moduleBean.getChildList();
	                		for(int j=0;j<moduleList.size();j++){
	                			ModuleBean childBean = (ModuleBean)moduleList.get(j);
	                			if(moduleBean.getIsHidden()!=1 && childBean.getChildList() != null && childBean.getChildList().size()>0){
	                				falg = true;
	                				break;
	                			}
	                		}
	                	}
	                	rec(sb, moduleBean, true,request,loginBean.getDefSys(),i,"menu",falg);
	                }
	            }
	        }
	       // System.out.println("---------menu gen ="+(System.currentTimeMillis() - cur));
	
	        //这里改为采用ajax 来调用菜单，
	        //System.out.println(sb.toString());
	        request.setAttribute("msg", sb.toString());
	        return getForward(request, mapping, "menuListAjax");
        }else{
        	//客户端登录
            String adviceType = getParameter("adviceType",request);
            if(StringUtils.isNotBlank(adviceType)){
            	String id = getParameter("id", request);
            	if(StringUtils.equals(adviceType,"advice")){
            		if (StringUtils.isNotBlank(id)) {
            			AdviceMgt mgt = new AdviceMgt();
                		Result rs = mgt.detail(id);
            			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
            				if(id != null &&id.length() > 0){
            	        		new AdviceMgt().readOverById(id);
            	        	}
            				AdviceBean msgBean = (AdviceBean) rs.retVal;
            				String content = msgBean.getContent();
            				String mdiwin = null;
            				if (content.contains("mdiwin")) {
            					int beg = content.indexOf("'/");
            					int end = content.indexOf("',",beg);
            					mdiwin = content.substring(beg, end+1);
            					request.setAttribute("linkUrl", mdiwin);
            					if(id.length()>0){
            						return getForward(request, mapping, "adviceforward");
            					}
            				}else if (content.toLowerCase().contains("http://")) {//可能是个外部链接
            					int pos = content.indexOf("\"",content.toLowerCase().indexOf("http://"));
            					int pos2= content.indexOf("'",content.toLowerCase().indexOf("http://"));
            					if(pos <0){
            						pos = pos2;
            					}else if(pos > 0 && pos > pos2 && pos2 > 0){
            						pos = pos2;
            					}
            					String http = content.substring(content.toLowerCase().indexOf("http://"),pos);            					
            					request.setAttribute("linkUrl", "'"+http+"'");
                        		return getForward(request, mapping, "adviceforward");  
            				}else if (content.toLowerCase().contains("https://")) {//可能是个外部链接
            					int pos = content.indexOf("\"",content.toLowerCase().indexOf("https://"));
            					int pos2= content.indexOf("'",content.toLowerCase().indexOf("https://"));
            					if(pos <0){
            						pos = pos2;
            					}else if(pos > 0 && pos > pos2 && pos2 > 0){
            						pos = pos2;
            					}
            					String http = content.substring(content.toLowerCase().indexOf("https://"),pos);            					
            					request.setAttribute("linkUrl", "'"+http+"'");
                        		return getForward(request, mapping, "adviceforward");  
            				}
            			}
            		}else {	// if (StringUtils.isNotBlank(id))
                		return new ActionForward("/AdviceAction.do?operation=4&selectType=noRead");           						
					}
            	}else if(StringUtils.equals(adviceType,"login")) {   //主页面		if(StringUtils.equals(adviceType,"advice"))
            		request.setAttribute("linkUrl", "'/MenuQueryAction.do?sysType=1&system=new'");
            		return getForward(request, mapping, "adviceforward");
            	}else if(StringUtils.equals(adviceType,"advise")) {
            		//如果是预警 这个时候 需要先查询对应的预警详情中的mdiwin 保存session中 （注意在 welcomeHome.jsp里面加入js代码，仿照上面if的对应的此页面js代码
            		
            	}else{
            		System.out.println("汇讯从AIo图表里面链接过来会经过这里");
            	}
            }
        	//跳转到body.jsp时，加载呼叫中心设置
    		Result rs = new PublicMgt().ccSetQuery();
    		HashMap map = new HashMap();
    		for(int i=0;rs.retVal != null && i< ((List)rs.retVal).size();i++){
    			Object o = ((List)rs.retVal).get(i);
    			map.put(((Object[])o)[0], ((Object[])o)[1]);
    		}    		
    		request.getSession().setAttribute("callCenter", map);
			Result result = new ModuleFlowMgt().queryMyMenu(getLoginBean(request).getId()) ;
	    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
	    		request.setAttribute("myMenu", result.retVal) ;
	    	}
	    	String myPhoto = GlobalsTool.checkEmployeePhoto("48",loginBean.getId());       		
    		request.setAttribute("myPhoto", myPhoto) ; 
    		ActionForward forward = null;
    		/*if("newOA".equals(loginBean.getShowDesk())){
    			if("true".equals(getParameter("enterOA", request))){
    				forward = getForward(request, mapping, "body_new") ;
    			}else{
    				forward = getForward(request, mapping, "bodyOA") ;
    			}   			
    		}else{*/
    			forward = getForward(request, mapping, "body_new") ;
    		//}    		
    			
			ArrayList<String[]> fastQuerylist = new ArrayList();
	        for(String[] ss : BaseEnv.fastQuery){
	        	if(ss[1].indexOf("UserFunctionQueryAction.do") > 0){
	        		//数据表列表，要进行权限控制/UserFunctionQueryAction.do?tableName=tblCompany&moduleType=2&ClientFlag=2
	        		Pattern pattern = Pattern.compile("[\\?|&]tableName=([^&]*)");
					Matcher matcher = pattern.matcher(ss[1]);
					String tableName="";
					if (matcher.find()) {
						tableName = matcher.group(1);
					}
					pattern = Pattern.compile("&moduleType=([^&]*)");
					matcher = pattern.matcher(ss[1]);
					String moduleType="";
					if (matcher.find()) {
						moduleType = matcher.group(1);
					}
	        		String url = "/UserFunctionQueryAction.do?tableName="+tableName;
	        		if(moduleType.length() > 0){
	        			url += "&moduleType="+moduleType;
	        		}
	        		if(this.getLoginBean(request).getOperationMap().get(url) != null){
	        			fastQuerylist.add(ss);
	        		}	
	        	}else if(ss[1].indexOf("ReportDataAction.do") > 0){
	        		//这是报表 /ReportDataAction.do?reportNumber=tblVirtualStock
	        		String url  = ss[1].substring(0,ss[1].indexOf("&"));
	        		if(this.getLoginBean(request).getOperationMap().get(url) != null){
	        			fastQuerylist.add(ss);
	        		}	        		
	        	}else if(ss[1].indexOf("/UserFunctionAction.do") == -1 && !ss[1].startsWith("http")){
	        		//非自定义类型，以LinkType参数为界面，取前面的代码来做权限判断。
	        		String url;
	        		if(ss[1].indexOf("LinkType=") >0){
	        			url = ss[1].substring(0,ss[1].indexOf("LinkType=")-1);
	        		}else{
	        			url = ss[1];
	        			BaseEnv.log.debug("快速查询链接："+url+"无参数&LinkType=");
	        		}
	        		if(this.getLoginBean(request).getOperationMap().get(url) != null){
	        			fastQuerylist.add(ss);
	        		}	
	        	}else {
	        		//这可能是弹出窗,或其它http外链接
	        		fastQuerylist.add(ss);
	        	}	        	
	        }
	        request.setAttribute("fastQuery", fastQuerylist);	
	    	return forward ;
        }
    }
    
    public static void setWorkFlowRight(String locale,LoginBean loginBean,HashMap operationMap,HashMap operationMapKeyId){
    	PublicMgt oaMgt=new PublicMgt();
        //查询工作流设计中目标用户中存在当前用户的工作流,并且已经做了流程设计
        Result rs=oaMgt.queryClass(loginBean);
        if(rs.getRetCode()==ErrorCanst.DEFAULT_SUCCESS){
        	ArrayList workFlows=(ArrayList)rs.retVal;
        	for(int i=0;i<workFlows.size();i++){
        		Object[] workFlow=(Object[])workFlows.get(i);
        		
        		if(workFlow[2].toString().equals("1")&&BaseEnv.workFlowDesignBeans.get(workFlow[0])!=null){   
        			MOperation mop=new MOperation();
            		mop.moduleUrl="/UserFunctionQueryAction.do?tableName="+workFlow[8];
            		mop.moduleId=workFlow[0].toString();
            		operationMap.put(mop.moduleUrl, mop);
                    operationMapKeyId.put(mop.moduleId, mop);
                    BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
                    BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
                    
        			if(operationMap.get("/UserFunctionQueryAction.do?tableName="+workFlow[8])!=null){
        				MOperation oldMop=(MOperation)operationMap.get("/UserFunctionQueryAction.do?tableName="+workFlow[8]);
        				mop.addScope=oldMop.getScope(MOperation.M_ADD);
        				mop.updateScope =oldMop.getScope(MOperation.M_UPDATE);
        				mop.deleteScope=oldMop.getScope(MOperation.M_DELETE);
        				mop.queryScope=oldMop.getScope(MOperation.M_QUERY);
        				
        			}
        			mop.add=true;
                    mop.delete=true;
                    mop.update=true;
                    mop.print=true;
                    mop.query=true;
                    mop.sendEmail=true;
                    mop.oaWorkFlow=true;
                    /*个性化界面不需要 列配置*/
                    
        		}
        	}
        } 
    }
    
    /**
     * 设置工作计划和事件计划的权限
     * @param loginBean
     * @param operationMap
     * @param operationMapKeyId
     */
    public static void setWorkPlanRight(LoginBean loginBean,HashMap operationMap,HashMap operationMapKeyId){
    	
    	//事件计划
    	MOperation mop2 = (MOperation) operationMap.get("/OAEventWorkPlanAction.do") ;
    	if(mop2!=null){
    		MOperation mop=new MOperation();
    		mop = cloneObject(mop2) ;
    		mop.moduleUrl="/UserFunctionQueryAction.do?tableName=tblDayWorkPlan2";
    		operationMap.put(mop.moduleUrl, mop);
            operationMapKeyId.put(mop.moduleId, mop);
            BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
            BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
		}
    	//工作计划
    	MOperation mop3 = (MOperation) operationMap.get("/OAWorkPlanAction.do") ;
    	if(mop3!=null){
    		MOperation mop=new MOperation();
    		mop = cloneObject(mop3) ;
    		mop.moduleUrl="/UserFunctionQueryAction.do?tableName=tblDayWorkPlan";
    		operationMap.put(mop.moduleUrl, mop);
            operationMapKeyId.put(mop.moduleId, mop);
            BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
            BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
            
            
            //CRM工作计划视图权限与工作计划权限一样,虚拟权限
            MOperation crmWorkPlanViewMop=new MOperation();
            crmWorkPlanViewMop = cloneObject(mop3) ;
            crmWorkPlanViewMop.add = false;
            crmWorkPlanViewMop.delete = false;
            crmWorkPlanViewMop.update = false;
            crmWorkPlanViewMop.moduleUrl="/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=CRMWorkPlanView";
    		operationMap.put(crmWorkPlanViewMop.moduleUrl, crmWorkPlanViewMop);
            operationMapKeyId.put(crmWorkPlanViewMop.moduleId, crmWorkPlanViewMop);
            BaseEnv.adminOperationMap.put(crmWorkPlanViewMop.moduleUrl, crmWorkPlanViewMop);
            BaseEnv.adminOoperationMapKeyId.put(crmWorkPlanViewMop.moduleId, crmWorkPlanViewMop);
		}
    	
    	//客户列表
    	MOperation mop6 = (MOperation) operationMap.get("/CRMClientAction.do") ;
    	if(mop6!=null){
    		MOperation mop=new MOperation();
    		mop = cloneObject(mop6) ;
    		mop.moduleUrl="/UserFunctionQueryAction.do?tableName=CRMClientInfo";
    		operationMap.put(mop.moduleUrl, mop);
            operationMapKeyId.put(mop.moduleId, mop);
            BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
            BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
		}
    	
    	//CRM兄弟表
    	MOperation mop8 = (MOperation) operationMap.get("/CRMBrotherAction.do?tableName=CRMSaleFollowUp") ;
    	if(mop8!=null){
    		MOperation mop=new MOperation();
    		mop = cloneObject(mop8) ;
    		mop.moduleUrl="/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=CRMSaleFollowUp";
    		operationMap.put(mop.moduleUrl, mop);
            operationMapKeyId.put(mop.moduleId, mop);
            BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
            BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
		}
    	
    	//凭证
    	MOperation mop7 = (MOperation) operationMap.get("/VoucherManageAction.do") ;
    	if(mop7!=null){
    		MOperation mop=new MOperation();
    		mop = cloneObject(mop7) ;
    		mop.moduleUrl="/UserFunctionQueryAction.do?tableName=tblAccMain";
    		operationMap.put(mop.moduleUrl, mop);
            operationMapKeyId.put(mop.moduleId, mop);
            BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
            BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
		}
    	
    	//高级功能权限
    	MOperation mop9 = (MOperation) operationMap.get("/AdvanceAction.do") ;
    	if(mop9!=null){
    		String[] str = new String[]{"/CustomQueryAction.do","/ReportSetQueryAction.do","/UserFunctionQueryAction.do?tableName=tblModules","/tableMappedQueryAction.do","/UserFunctionQueryAction.do?tableName=tblTimingMsg","/UserFunctionQueryAction.do?tableName=tblModuleFlow","/UserFunctionQueryAction.do?tableName=tblModuleField","/AlertSetAction.do","/EnumerationQueryAction.do"};
    		for(String s : str){
    			MOperation mop = new MOperation();
    			mop = cloneObject(mop9) ;
    			mop.moduleUrl=s;
    			operationMap.put(mop.moduleUrl, mop);
    			operationMapKeyId.put(mop.moduleId, mop);
    			BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
    			BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
    		}
		}
    	
    	//部门职员
    	MOperation mop10 = (MOperation) operationMap.get("/EmployeeDepartmentAction.do") ;
    	if(mop10!=null){
    		String[] str = new String[]{"/UserFunctionQueryAction.do?tableName=tblDepartment","/UserFunctionQueryAction.do?tableName=tblEmployee"};
    		for(String s : str){
    			MOperation mop = new MOperation();
    			mop = cloneObject(mop10) ;
    			mop.moduleUrl=s;
    			operationMap.put(mop.moduleUrl, mop);
    			operationMapKeyId.put(mop.moduleId, mop);
    			BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
    			BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
    		}
		}
    }
    
    /**
	   * 克隆一个MOperation对象
	   * @param mop
	   * @return
	   */
	public static MOperation cloneObject(MOperation mop) {
		MOperation field = new MOperation();
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(mop);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			field = (MOperation) in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return field ;
	}
    
    /**
     * 注：以前兄弟表是根据主表权限来的，此方法用来设置兄弟单据权限，
     *    不依赖于主表的权限
     * @param loginBean
     * @param operationMap
     * @param operationMapKeyId
     */
    public void setBrotherTableRight(LoginBean loginBean,HashMap operationMap,HashMap operationMapKeyId){
    	
    	ArrayList<MOperation> listMop = new ArrayList<MOperation>() ;
    	Iterator iter = operationMap.keySet().iterator() ;
    	while(iter.hasNext()){
    		String moduleUrl = (String) iter.next() ;
    		if(moduleUrl!=null && moduleUrl.startsWith("/UserFunctionQueryAction.do?parentTableName=")){
    			MOperation mop = new MOperation() ;
    			MOperation mop2 = (MOperation) operationMap.get(moduleUrl) ;
    			if(moduleUrl.indexOf("tableName=")!=-1){
    				mop = mop2 ;
    				String tableName = moduleUrl.substring(moduleUrl.indexOf("tableName="),moduleUrl.length()) ;
	    			mop.setModuleUrl("/UserFunctionQueryAction.do?"+tableName) ;
	    			listMop.add(mop) ;
    			}
    		}
    	}
    	
    	for(MOperation mop : listMop){
	    	operationMap.put(mop.moduleUrl, mop);
	        operationMapKeyId.put(mop.moduleId, mop);
	        BaseEnv.adminOperationMap.put(mop.moduleUrl, mop);
	        BaseEnv.adminOoperationMapKeyId.put(mop.moduleId, mop);
    	}
    }
    
    /**
     * 通过ModuleBean 将整个菜单树，一级级添加到菜单树中去
     * @param moduleBean ModuleBean
     * @param userMenu ArrayList
     * @return ModuleBean
     */
    public static ModuleBean userMenuRec(ModuleBean moduleBean, ArrayList userMenu) {
        if (moduleBean.getParentModuleBean() == null) {
            //为一级菜单,新建moduleBean 并按顺序放入userMenu中
            ModuleBean menuBean = null;
            for (Object o : userMenu) {
                //查找对应bean是否存在
                ModuleBean mtemp = (ModuleBean) o;
                if (mtemp.getId().equals(moduleBean.getId())) {
                    menuBean = mtemp;
                    break;
                }
            }
            //moduleBean.getIsHidden()!=1 表明此模块不是隐藏模块，用户可以通过其他的链接方式访问，但在页面上没有菜单显示
            if (menuBean == null&&moduleBean.getIsHidden()!=1) {
                //该bean还未加入菜单，按顺序加入
                menuBean = new ModuleBean();
                menuBean.setClassCode(moduleBean.getClassCode());
                menuBean.setId(moduleBean.getId());
                menuBean.setLinkAddress(moduleBean.getLinkAddress());
                menuBean.setModelDisplay(moduleBean.getModelDisplay());
                menuBean.setOrderBy(moduleBean.getOrderBy());
                menuBean.setParentModuleBean(moduleBean.getParentModuleBean());
                menuBean.setTblName(moduleBean.getTblName());
                menuBean.setMainModule(moduleBean.getMainModule()) ;
                //寻找插入位置
                boolean found = false;
                for (int i = 0; i < userMenu.size(); i++) {
                    ModuleBean temp = (ModuleBean) userMenu.get(i);
                    if (menuBean.getOrderBy() < temp.getOrderBy()) {
                        found = true;
                        userMenu.add(i, menuBean);
                        break;
                    }
                }
                if (!found) {
                    userMenu.add(menuBean);
                }
            }
            //返回对应的菜单bean
            return menuBean;
        } else {
            ModuleBean parentMenuBean = userMenuRec(moduleBean.getParentModuleBean(), userMenu);
            if(parentMenuBean!=null){
	            ModuleBean menuBean = null;
	            if (parentMenuBean.getChildList() != null) {
	                for (Object o : parentMenuBean.getChildList()) {
	                    //查找对应bean是否存在
	                    ModuleBean mtemp = (ModuleBean) o;
	                    if (mtemp.getId().equals(moduleBean.getId())) {
	                        menuBean = mtemp;
	                        break;
	                    }
	                }
	            } else {
	                parentMenuBean.setChildList(new ArrayList());
	            }
	            if (menuBean == null&&moduleBean.getIsHidden()!=1) {
	                //该bean还未加入菜单，按顺序加入
	                menuBean = new ModuleBean();
	                menuBean.setClassCode(moduleBean.getClassCode());
	                menuBean.setId(moduleBean.getId());
	                menuBean.setLinkAddress(moduleBean.getLinkAddress());
	                menuBean.setModelDisplay(moduleBean.getModelDisplay());
	                menuBean.setOrderBy(moduleBean.getOrderBy());
	                menuBean.setParentModuleBean(moduleBean.getParentModuleBean());
	                menuBean.setTblName(moduleBean.getTblName());
	                menuBean.setMainModule(moduleBean.getMainModule()) ;
	                //寻找插入位置
	                boolean found = false;
	                for (int i = 0; i < parentMenuBean.getChildList().size(); i++) {
	                    ModuleBean temp = (ModuleBean) parentMenuBean.getChildList().get(i);
	                    if (menuBean.getOrderBy() < temp.getOrderBy()) {
	                        found = true;
	                        parentMenuBean.getChildList().add(i, menuBean);
	                        break;
	                    }
	                }
	                if (!found) {
	                    parentMenuBean.getChildList().add(menuBean);
	                }
	            }
	            //返回对应的菜单bean
	            return menuBean;
            }
            return null;
        }
    }

    /**
     * 根据 moduleOperationId 从菜单中找到对象的ModuleOperationBean 对象
     * @param menu ArrayList
     * @param moduleOpId String
     * @param rs Result
     */
    public static void userMouleRec(ArrayList menu, String moduleBeanId, Result rs) {
        if (rs.retCode != ErrorCanst.DEFAULT_SUCCESS||menu==null) {
            return;
        }
        for (Object o : menu) {
            ModuleBean mb = (ModuleBean) o;

            if(moduleBeanId!=null && moduleBeanId.trim().equals(mb.getId().trim())){
                rs.retVal = mb;
                rs.retCode = ErrorCanst.DEFAULT_FAILURE;
                return;
            }

            userMouleRec(mb.getChildList(), moduleBeanId, rs);
        }
    }

    private void rec(StringBuffer sb, ModuleBean moduleBean, boolean root,HttpServletRequest request,String sysType,int seq,String moduleType,boolean falg) {
		if("child".equals(moduleType)){
			sb.append("<div class=\"second-third-menu\">");
			sb.append("<em>"+(moduleBean.getModelDisplay() != null?moduleBean.getModelDisplay().get(getLocale(request).toString()):"")+"</em><ul>");     				
		}else{
			String name = moduleBean.getModelDisplay() != null ? moduleBean.getModelDisplay().get(getLocale(request).toString()) : "";
			sb.append("<li class=\"first-li\" id=\"top_link_"+(seq+1)+"\"");
			sb.append("><em ");
			if("1".equals(moduleBean.getMainModule()) && !"报表".equals(name)){
				sb.append("onClick=\"showModule('/newMenu.do?name="+name+"&menuType=newMenu&moduleType="+moduleBean.getMainModule()+"','"+moduleBean.getMainModule()+"');\"");    				
			}
			sb.append(">"+name+"</em>");
			sb.append("<div class=\"second-menu-div\"><div class=\"common-memu\"><ul>");
		}
        for (int i = 0; moduleBean.getChildList() != null && i < moduleBean.getChildList().size(); i++) {
            ModuleBean childBean = (ModuleBean) moduleBean.getChildList().get(i);
            if(childBean.getIsHidden()!=1){
	            if (childBean.getChildList() != null && childBean.getChildList().size() > 0) {
	                rec(sb, childBean, false,request,sysType,i,"child",falg);
	            } else {
		                String url = childBean.getLinkAddress();
		                if(url.startsWith("javascript:")){
		                	String uname=childBean.getModelDisplay()==null?"null":childBean.getModelDisplay().get(getLocale(request).toString());
		                    url = url.substring("javascript:".length());
		                	sb.append("<li><em onClick=\"" + url +";\" title=\""+uname+"\">"+uname+"</em></li>");
	                	}else if (url != null && url.length() > 0) {
		                	
		                    if (url.indexOf("src=menu") == -1) {
		                        if (url != null && url.indexOf("?") > 0) {
		                            url += "&src=menu";
		                        } else {
		                            url += "?src=menu";
		                        }
		                    }
		                    String uname=childBean.getModelDisplay()==null?"null":childBean.getModelDisplay().get(getLocale(request).toString());
		                    sb.append("<li><em onClick=\"showreModule('" + uname + "','" + url + "');\" title=\""+uname+"\">"+uname+"</em><b class=\"icons\" onclick=\"addMyDest('"+url+"','"+uname+"');\" title=\"收藏此菜单\"></b></li>");
		                } else {
		                	String uname=childBean.getModelDisplay()==null?"null":childBean.getModelDisplay().get(getLocale(request).toString());
		                	sb.append("<li><em onClick=\"alert('"+getMessage(request, "menu.error.nullMenu")+"');\">"+uname+"</em></li>");
		                }
	            }
	        }
        } 
    	sb.append("</ul>");
    	if("child".equals(moduleType)){
    		sb.append("</div>");
    	}else{
    		sb.append("</div></li>");
    	}
    }

}
