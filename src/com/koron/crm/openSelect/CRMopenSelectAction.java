package com.koron.crm.openSelect;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koron.crm.bean.WorkProfessionBean;
import com.koron.crm.bean.WorkTradeBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * <p>
 * Title: CRM行业地区弹出框
 * </p>
 * 
 * @Copyright: 科荣软件
 * 
 * @author 徐洁俊
 * 
 */
public class CRMopenSelectAction extends MgtBaseAction {
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	private CRMopenSelectMgt tradeMgt = new CRMopenSelectMgt();
	/**
	 * exe 控制器入口函数
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 * @todo Implement this com.huawei.sms.web.util.BaseAction method
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		String type = (String)request.getParameter("type");
		request.setAttribute("isFirst", "false");
		ActionForward forward = null;

		switch (operation) {
		case OperationConst.OP_ADD:
				if("workTrade".equals(type)){
					forward = addWorkTrade(mapping, form, request, response);
				}else{
					forward = addProfession(mapping, form, request, response);
				}
			break;
		case OperationConst.OP_DELETE:
			if("workTrade".equals(type)){
				forward = delWorkTrade(mapping, form, request, response);
			}else{
				forward = delProfession(mapping, form, request, response);
			}
		break;
		case OperationConst.OP_QUERY:
			if("area".equals(type)){
				forward = findAreas(mapping, form, request, response);
			}else if("tradeCount".equals(type)){
				forward = tradeCount(mapping, form, request, response);
			}else if("popSelect".equals(type)){
				forward = popSelectInfo(mapping, form, request, response);
			}else{
				forward = queryDistrict(mapping, form, request, response);
			}
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	
	@SuppressWarnings("unchecked")
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Result result = tradeMgt.findWordTrades();
		List<WorkTradeBean> workTrades = (ArrayList<WorkTradeBean>)result.retVal;
		request.setAttribute("workTrades", workTrades);
		request.setAttribute("isIndexEnter", request.getParameter("isIndexEnter") == null ? "false" :"true");
		request.setAttribute("loginBean", this.getLoginBean(request));
		LoginBean loginBean = this.getLoginBean(request);
		return getForward(request, mapping, "workTrade");
	}
	
	protected ActionForward queryCityandArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String province = URLDecoder.decode(request.getParameter("province"),"UTF-8");
		String city = URLDecoder.decode(request.getParameter("city"),"UTF-8");
		Result districtResult = tradeMgt.findDistricts(province,city);
		HashMap<String, List> map = new HashMap<String, List>();
		List list = (List)districtResult.retVal;
		String isIndexEnter = request.getParameter("isIndexEnter") == null ? "" : request.getParameter("isIndexEnter");
		if(city==null || "".equals(city)){
			List newList = new ArrayList();
			newList.add(province);
			newList.add(isIndexEnter);
			map.put("privinceName", newList);
			map.put("citys",list);
			String json = gson.toJson(map);
			request.setAttribute("msg", json);
		}else{
			String json = gson.toJson(list);
			request.setAttribute("msg", json);
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 重写权限判断的方法,因为此模块不需要权限判断,所以返回null
	 */
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
	    LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
		return null;
	}
	
	
	protected ActionForward queryDistrict(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		long start = System.currentTimeMillis();
		String keyWord = request.getParameter("keyWord");
		String selOption = request.getParameter("selOption");
		if(keyWord !=null){
			keyWord = URLDecoder.decode(keyWord,"UTF-8");
		}
		//查找省市信息
		Result rs = tradeMgt.findDis("province",keyWord,selOption);
		List<Object> prolist = (List<Object>)rs.retVal;
		rs = tradeMgt.findDis("city",keyWord,selOption);
		List<Object> citylist = (List<Object>)rs.retVal;
		List areaList = new ArrayList();
		if(selOption !=null && "area".equals(selOption) || "all".equals(selOption)){
			rs = tradeMgt.findDis("area",keyWord,selOption);
			areaList = (List<Object>)rs.retVal;
		}
		//封装成JSON格式
		List zNodesList = new ArrayList();
		for(int i=0;i<prolist.size();i++){
			List childList = new ArrayList();
			LinkedHashMap zNodeMap = new LinkedHashMap();
			zNodeMap.put("id", ((Object[])prolist.get(i))[1].toString());
			zNodeMap.put("name", ((Object[])prolist.get(i))[0].toString());
			zNodeMap.put("districtFullName", ((Object[])prolist.get(i))[2].toString());
			zNodeMap.put("isCataLog", "0");
			if("city".equals(selOption)){
				zNodeMap.put("open", "true");
			}
			for(int j=0;j<citylist.size();j++){
				LinkedHashMap chlidMap = new LinkedHashMap();
				String cityId = ((Object[])citylist.get(j))[1].toString();
				if(((Object[])prolist.get(i))[1].toString().equals(cityId.substring(0,cityId.length()-5))){
					chlidMap.put("id", cityId);
					chlidMap.put("name", ((Object[])citylist.get(j))[0].toString());
					chlidMap.put("pId", ((Object[])prolist.get(i))[1].toString());
					chlidMap.put("districtFullName", ((Object[])citylist.get(j))[2].toString());
					childList.add(chlidMap);
					//citylist.remove(citylist.get(j));
				}
			}
			
			if(selOption==null || !"city".equals(selOption) || childList.size() != 0){
				zNodeMap.put("nodes", childList);
				zNodesList.add(zNodeMap);
			}
			
		}
		
		request.setAttribute("zNodes", gson.toJson(zNodesList));
		request.setAttribute("areaList", areaList);
		request.setAttribute("keyWord", keyWord);
		request.setAttribute("selOption", selOption);
		
		System.out.println("all cast time : " + (System.currentTimeMillis() - start));
		String isMultiple = request.getParameter("isMultiple");
		if("true".equals(isMultiple)){
			return getForward(request, mapping, "districtMul");
		}
		return getForward(request, mapping, "district");
	}
	
	protected ActionForward findAreas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String cityId = request.getParameter("cityId");
	    Result result = tradeMgt.findareas(cityId);
    	List areaList = (List) result.retVal ;
    	HashMap maps = new HashMap();
    	maps.put("areaList", areaList);
		request.setAttribute("msg",gson.toJson(maps));	
	    return getForward(request, mapping, "blank");
		
	}
	
	/**
	 * 添加行业
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addProfession(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String workTradeId = getParameter("workTradeId", request);
		String professionName = "";
		try {
			professionName = URLDecoder.decode(request.getParameter("professionName"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		WorkTradeBean workTradeBean = (WorkTradeBean)(tradeMgt.loadWorkTrade(workTradeId).retVal);
		WorkProfessionBean professionBean = new WorkProfessionBean();
		String professionId = IDGenerater.getId();
		professionBean.setId(professionId);
		professionBean.setName(professionName);
		professionBean.setWorkTradeBean(workTradeBean);
		Result rs = tradeMgt.addProfession(professionBean);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			//更新内存的行业MAP
			HashMap tradeMap = (HashMap)request.getSession().getServletContext().getAttribute("workProfessionMap");
			tradeMap.put(professionId, professionName);
			request.getSession().getServletContext().setAttribute("workProfessionMap", tradeMap);
			request.setAttribute("msg",professionId);
		} else {
			request.setAttribute("msg","no");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 删除行业
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delProfession(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String professionId = getParameter("professionId", request);
		Result rs  = tradeMgt.delProfession(professionId);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg","ok");
		} else {
			request.setAttribute("msg","no");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 删除行业
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delWorkTrade(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String workTradeId = getParameter("workTradeId", request);
		Result rs  = tradeMgt.delWorkTrade(workTradeId);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg","ok");
		} else {
			request.setAttribute("msg","no");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 添加行业总类
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addWorkTrade(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String workTradeName = "";
		String professionNames = "";
		String isIndexEnter = request.getParameter("isIndexEnter");//判断是否要有复选框
		HashMap tradeMap = (HashMap)request.getSession().getServletContext().getAttribute("workProfessionMap");
		
		try {
			workTradeName = URLDecoder.decode(request.getParameter("workTradeName"),"UTF-8");
			professionNames = URLDecoder.decode(request.getParameter("professionNames"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String workTradeId = IDGenerater.getId();
		List<String> sqlList = new ArrayList<String>();//插入的insert语句
		//拼接字符串，显示到页面上
		String showDiv ="<div class=\"subitem_hy\"><h4 name=\"h4\">" + workTradeName + "&nbsp;&nbsp;<span style=\"cursor: pointer;font-weight: normal;color: #006699\" onclick=\"addProfession('"+workTradeId+"',this)\">添加</span> <span style=\"cursor: pointer;font-weight: normal;color: #006699\" onclick=\"delWorkTrade('"+workTradeId+"',this)\">删除</span> </h4><ul class=\"cf\">";
		sqlList.add("INSERT INTO CRMWorkTrade(id,name,createTime) VALUES('"+workTradeId+"','"+workTradeName+"','"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"')");
		
		if(!"".equals(professionNames)){
			for(String str : professionNames.split(",")){
				String professionId = IDGenerater.getId();
				sqlList.add("INSERT INTO CRMWorkProfession(id,name,workTradeId) VALUES('"+professionId+"','"+str+"','"+workTradeId+"')");
				showDiv += "<li>";
				if("true".equals(isIndexEnter)){
					showDiv+="<input type=\"checkbox\" name=\"tradeId\" value=\""+professionId+"\" tradeName=\""+str+"\" id=\""+professionId+"\"/>";
				}
				showDiv +="<a href=\"#\" onclick=\"selectPro('"+professionId+"','"+str+"')\" style=\"cursor: pointer;\"><label for=\"a1\">"+str+"</label></a><img src=\"/style/images/plan/del_02.gif\"  style=\"cursor: pointer;margin-left: 10px;margin-top:5px;\" onclick=\"delProfession('"+professionId+"',this)\"/></li>";
				tradeMap.put(professionId, str);//更新内存行业map
			}
			showDiv +="</ul></div>";
		}
		Result rs = tradeMgt.addWorkTrade(sqlList);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			
			request.getSession().getServletContext().setAttribute("workProfessionMap", tradeMap);
			request.setAttribute("msg",showDiv);
			System.out.println(showDiv);
		} else {
			request.setAttribute("msg","no");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 查看客户是否有使用本行业
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward tradeCount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String tradeId = getParameter("tradeId", request);
		String checkFlag = getParameter("checkFlag", request);
		Result rs  = tradeMgt.checkTradeCount(tradeId,checkFlag);
		int count = (Integer)rs.retVal;
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("msg",count);
		} else {
			request.setAttribute("msg","no");
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward popSelectInfo(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		String popName = getParameter("popName", request);
		String selIds = getParameter("selIds", request);
		String str = "";
		String infoName ="";
		String enterPage=getParameter("enterPage", request);
		HashMap<String,String> InfoMap = new HashMap<String, String>();
		if("trade".equals(popName)){
			InfoMap = (HashMap)request.getSession().getServletContext().getAttribute("workProfessionMap");
		}else if("district".equals(popName)){
			InfoMap = (HashMap)request.getSession().getServletContext().getAttribute("districtMap");
		}
		for(String selId : selIds.split(",")){
			if(selId !=null && !"".equals(selId)){
				if("districtMul".equals(enterPage)){
					str+="<ul id='"+selId+"'  ><li><a href=\"#\" onclick=\"imgdel(this);\"  style=\"cursor: pointer;\"></a><span title='"+InfoMap.get(selId)+"'>"+InfoMap.get(selId).substring(InfoMap.get(selId).lastIndexOf("-")+1)+"</span></li></ul>";
				}else{
					infoName = (String)InfoMap.get(selId);
					if("district".equals(popName)){
						if(selId.length() == 15){
							infoName = infoName.substring(infoName.indexOf("-")+1, infoName.length());
						}else if(selId.length() == 20){
							infoName = infoName.substring(infoName.indexOf("-")+1, infoName.length());
							infoName = infoName.substring(infoName.indexOf("-")+1, infoName.length());
						}
					}
					str += "<li><span id='"+selId+"'>"+infoName+"</span><img src='/style/images/plan/del_02.gif' onclick=\"delPopId(this,'"+selId+"','"+popName+"')\"></li>";
				}
			}
		}
		request.setAttribute("msg", str);
		return getForward(request, mapping, "blank");
	}
}
