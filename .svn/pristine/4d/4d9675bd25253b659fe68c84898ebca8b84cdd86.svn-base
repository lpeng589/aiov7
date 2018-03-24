package com.menyi.aio.web.optimize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.RoleBean;
import com.menyi.web.util.*;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.security.MessageDigest;

/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com

 * @version 1.0
 */
public class OptimizeAction extends Action {
    OptimizeMgt mgt = new OptimizeMgt();
    public OptimizeAction() {
    }

    /**
     * 试用不需进行权限判断
     * @param req HttpServletRequest
     * @param mapping ActionMapping
     * @return ActionForward
     */
    protected ActionForward doAuth(HttpServletRequest req,
                                   ActionMapping mapping) {
        return null;
    }


    /**
     * exe 控制器入口函数
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.huawei.sms.web.util.BaseAction method
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        ActionForward forward = null;
        
        forward = optimize(mapping, form, request, response);
        return forward;
    }

    /**
     * 添加前的准备
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward optimize(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws
        Exception {     
    	String optimize = request.getParameter("optimize");
    	request.setAttribute("optimize", optimize);
        if ("freeProcCache".equals(optimize)) {
        	//执行清除sql server的缓存
        	Result rs = mgt.freeProcCache();
        	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		request.setAttribute("MSG", getMessage(request, "common.msg.clearSucceed"));
        	}else{
        		request.setAttribute("MSG", getMessage(request, "common.msg.clearFailure"));
        	}
        }else if("runTime".equals(optimize)){
        	Result rs = mgt.runTime();
        	ArrayList list = (ArrayList)rs.retVal;
        	if(list !=null && list.size() > 0 ){
        		request.setAttribute("first", list.get(0));
        		request.setAttribute("runTime", "true");
        		request.setAttribute("column", ((Object[])list.get(0)).length-1);
        	}
        	request.setAttribute("result", rs.retVal);
        	
        	
        	int onlineUser = 0;
        	for(OnlineUser user:OnlineUserInfo.getAllUserList()){
	        	if("0".equals(user.statusId) && !"".equals(user.sysName)){
		            if(user.isOnline()){
		            	onlineUser ++;
		            }
	        	}
        	}
        	request.setAttribute("onlineUser", onlineUser);
        	int wxqyonlineUser = 0;
        	for(OnlineUser user:OnlineUserInfo.getAllWxqyUserList()){
	        	if("0".equals(user.statusId) && !"".equals(user.sysName)){
		            if(user.isOnline()){
		            	onlineUser ++;
		            }
	        	}
        	}
        	request.setAttribute("wxqyonlineUser", wxqyonlineUser);
        	
        }else if("tableRows".equals(optimize)){
        	Result rs = mgt.tableRows();
        	ArrayList list = (ArrayList)rs.retVal;
        	if(list !=null && list.size() > 0 ){
        		request.setAttribute("first", list.get(0));
        		request.setAttribute("column", ((Object[])list.get(0)).length-1);
        	}
        	request.setAttribute("result", rs.retVal);
        }else if("tableInfo".equals(optimize)){
        	String tableName = request.getParameter("tableName");
        	if("true".equals(request.getParameter("isSave"))){
        		String[] columns = request.getParameterValues("column");
        		ArrayList list = new ArrayList();
        		list.add(new String[]{null,request.getParameter("tableDesc")});
        		for(String col:columns){
        			list.add(new String[]{col,request.getParameter("col"+col)});
        		}
        		String path = request.getRealPath("/tableRemark.sql");
        		mgt.saveDesc(tableName, list,path);
        	}
        	Result rs = mgt.getTableDesc(tableName);
        	request.setAttribute("tableDesc", rs.retVal);
        	rs = mgt.tableInfo(tableName);
        	ArrayList list = (ArrayList)rs.retVal;
        	if(list !=null && list.size() > 0 ){
        		request.setAttribute("first", list.get(0));
        		request.setAttribute("column", ((Object[])list.get(0)).length-1);
        	}
        	request.setAttribute("result", rs.retVal);
        	
        	rs = mgt.getTableIndex(tableName);
        	ArrayList index = (ArrayList)rs.retVal;
        	if(index !=null && index.size() > 0 ){
        		request.setAttribute("indexfirst", index.get(0));
        		request.setAttribute("indexcolumn", ((Object[])index.get(0)).length-1);
        		request.setAttribute("indexresult", rs.retVal);
        	}
        	
        	
        	request.setAttribute("tableName", tableName);
        	return getForward(request, mapping, "tableInfo");
        }else if("indexUse".equals(optimize)){
        	Result rs = mgt.indexUse();
        	ArrayList list = (ArrayList)rs.retVal;
        	if(list !=null && list.size() > 0 ){
        		request.setAttribute("first", list.get(0));
        		request.setAttribute("column", ((Object[])list.get(0)).length-1);
        	}
        	request.setAttribute("result", rs.retVal);
        }else if ("indexFragmentation".equals(optimize)) {
        	//执行清除sql server的缓存
        	Result rs = mgt.indexFragmentation();
        	if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		request.setAttribute("MSG", "碎片整理成功");
        	}else{
        		request.setAttribute("MSG", "碎片整理失败"+rs.retVal);
        	}
        }else if("block".equals(optimize)){
        	Result rs = mgt.block();
        	ArrayList list = (ArrayList)rs.retVal;
        	if(list !=null && list.size() > 0 ){
        		request.setAttribute("first", list.get(0));
        		request.setAttribute("block", "true");
        		request.setAttribute("column", ((Object[])list.get(0)).length-1);
        	}
        	request.setAttribute("result", rs.retVal);
        }else if("acc".equals(optimize)){
        	Result rs = mgt.acc();
        	ArrayList list = (ArrayList)rs.retVal;
        	if(list !=null && list.size() > 0 ){
        		request.setAttribute("first", list.get(0));
        		request.setAttribute("block", "true");
        		request.setAttribute("column", ((Object[])list.get(0)).length-1);
        	}
        	
        	request.setAttribute("result", rs.retVal);
        }else if("bug".equals(optimize)){
        	Result rs = mgt.bug();
        	String str = (String)rs.retVal;               	
        	Random rd = new Random();
	        rd.setSeed(System.currentTimeMillis());
	        int rdi = rd.nextInt(4000);        
	        String s =CallSoftDll.get(rdi+"");        
	        byte[] mb= new byte[16] ;
	        mb =SecurityLock2.hexStringToBytes(s);    	
	    	rdi +=5;
	    	for(int i=0;i<mb.length ;i++){    		
	    		mb[i] = (byte)(mb[i]-rdi);
	    		if(i>8){
	    			mb[i] =(byte)(mb[i] -2);
	    		}
	    	}    	  	
	        //校验机器码
	    	String realPcNo = SecurityLock2.bytesToHexString(mb);	    	
	    	str +="\n"+CallSoftDll.get("KoronSeward")+"::M="+CallSoftDll.get("KoronSewardM")+"::S="+s+"::realPcNo="+realPcNo+"::加密狗="+SystemState.instance.pcNo;
	    	request.setAttribute("MSG", str);
	    	
	    	rs = mgt.certHistory();
        	ArrayList list = (ArrayList)rs.retVal;
        	if(list !=null && list.size() > 0 ){
        		request.setAttribute("first", list.get(0));
        		request.setAttribute("column", ((Object[])list.get(0)).length-1);
        	}
        	request.setAttribute("result", rs.retVal);
        	
        }else if("data".equals(optimize)){
//        	Result rs = mgt.dataTable();
//        	String str = (String)rs.retVal;        	
//        	request.setAttribute("MSG", str);
        }
    	
        return getForward(request, mapping, "optimize");
    }
    
	protected ActionForward getForward(HttpServletRequest req,
			ActionMapping mapping, String key) {
		return mapping.findForward(key);
	}
    
	/**
	 * 获取资源文件
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param key
	 *            String
	 * @return String
	 */
	public String getMessage(HttpServletRequest req, String key) {
		try {
			MessageResources mr = this.getResources(req, "userResource");
			if (mr != null) {
				// 优先取用户自定义资源文件
				String str = mr.getMessage(getLocale(req), key);
				if (str != null) {
					return str;
				}
			}
		} catch (Exception e) {
		}

		MessageResources mr = this.getResources(req);
		return mr.getMessage(getLocale(req), key);
	}

	/**
	 * struts获取请求
	 * 
	 * @param req
	 * @param key
	 * @return
	 */
	public String getMessage2(HttpServletRequest req, String key) {
		Object o = req.getSession().getServletContext().getAttribute(
				org.apache.struts.Globals.MESSAGES_KEY);
		if (o instanceof MessageResources) {
			MessageResources mr = (MessageResources) o;
			return mr.getMessage(getLocale(req), key);
		}
		return "";
	}

	/**
	 * 获取资源文件
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param key
	 *            String
	 * @param param1
	 *            String
	 * @return String
	 */
	public String getMessage(HttpServletRequest req, String key, String param1)

	{
		try {
			MessageResources mr = this.getResources(req, "userResource");
			if (mr != null) {
				// 优先取用户自定义资源文件
				String str = mr.getMessage(getLocale(req), key, param1);
				if (str != null) {
					return str;
				}
			}
		} catch (Exception e) {
		}
		MessageResources mr = this.getResources(req);
		return mr.getMessage(getLocale(req), key, param1);
	}

	/**
	 * 获取资源文件
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param key
	 *            String
	 * @param param1
	 *            String
	 * @return String
	 */
	public String getMessage(HttpServletRequest req, String key, String param1,
			String param2)

	{
		try {
			MessageResources mr = this.getResources(req, "userResource");
			if (mr != null) {
				// 优先取用户自定义资源文件
				String str = mr.getMessage(getLocale(req), key, param1, param2);
				if (str != null) {
					return str;
				}
			}
		} catch (Exception e) {
		}
		MessageResources mr = this.getResources(req);
		return mr.getMessage(getLocale(req), key, param1, param2);
	}

	/**
	 * 获取资源文件
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param key
	 *            String
	 * @param param1
	 *            String
	 * @return String
	 */
	public String getMessage(HttpServletRequest req, String key, String param1,
			String param2, String param3)

	{
		try {
			MessageResources mr = this.getResources(req, "userResource");
			if (mr != null) {
				// 优先取用户自定义资源文件
				String str = mr.getMessage(getLocale(req), key, param1, param2,
						param3);
				if (str != null) {
					return str;
				}
			}
		} catch (Exception e) {
		}
		MessageResources mr = this.getResources(req);
		return mr.getMessage(getLocale(req), key, param1, param2, param3);
	}
}
