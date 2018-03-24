package com.menyi.aio.web.label;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.LabelBean;
import com.menyi.aio.bean.ReportsBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.report.ReportDataMgt;
import com.menyi.aio.web.report.ReportSetMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

/**
 * 
 * 
 * <p>
 * Title:Erp标签打印功能
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2012-6-29
 * @Copyright: 科荣软件
 * @Author 方家俊
 */
public class LabelAction extends MgtBaseAction {

	LabelMgt mgt = new LabelMgt();
	private String sysCodeMeter = "Meter";
	private String sysCodeCoil = "Coil";
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			forward = addPreLabel(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			forward = addLabel(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			forward = updateLabel(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		default:
			forward = addPreLabel(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * 添加前的准备
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addPreLabel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String id = request.getParameter("id");
		if (id != null && !"".equals(id)) {
			Result r = mgt.loadlabel(id);
			request.setAttribute("label", r.retVal);
		}
		
		Result res = mgt.getGoodsAttribute();
		ArrayList list = new ArrayList();
		if (res.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			list = (ArrayList)res.retVal;
			//request.setAttribute("list", res.retVal);
		}
		
		String meter = BaseEnv.systemSet.get("Meter").getSetting();
		if("true".equals(meter)){
			String[] str= new String[2];
			str[0] = sysCodeMeter;
			list.add(str);
		}
		String[] pro = new String[2];
		pro[0] = "procedures";
		list.add(pro);
		request.setAttribute("Meter", meter);

		String coli = BaseEnv.systemSet.get("Coil").getSetting();;
		if("true".equals(coli)){
			String[] str= new String[2];
			str[0] = sysCodeCoil;
			list.add(str);
		}
		request.setAttribute("Coli", coli);
			
		request.setAttribute("list", list);
		String goodsName = request.getParameter("goodsName");
		if (goodsName != null && !"".equals(goodsName)) {
			String Name = GlobalsTool.toChinseChar(goodsName);
			request.setAttribute("goodsName", Name);
		}
		request.setAttribute("tableName", "tblLabel");
		Result ts = mgt.sysnSeq();
		if (ts.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("seq", ts.retVal);
		}
		Result re = mgt.getReport("ReporttblLabel");
		if (re.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("reportid", re.retVal);
		}
		Result rst = mgt.getNum();
		if (rst.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("num", rst.retVal);
		}
		

		/*
		 * Result al = mgt.getAllLabel(); if(al.retCode ==
		 * ErrorCanst.DEFAULT_SUCCESS){ request.setAttribute("LabelList",
		 * al.retVal); }
		 */
		return getForward(request, mapping, "addLabel");
	}

	/**
	 * 添加
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addLabel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//是否启用磅秤
		String scaleprint = request.getParameter("scaleprint");
		if("true".equals(scaleprint)){
			request.getSession().setAttribute("scaleprint", "true");
		}else{
			request.getSession().setAttribute("scaleprint", "false");
		}
		
		String autoScale = request.getParameter("autoScale");
		if(autoScale != null && autoScale.length() > 0){
			mgt.updateSysDeploy("AutoScale", autoScale.equals("start")?"true":"false");
			EchoMessage.success().add(
					getMessage(request, "common.msg.updateSuccess")).setBackUrl(
					"/LabelAction.do?operation=6").setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
		
		LoginBean loginBean = this.getLoginBean(request);
		LabelForm laform = (LabelForm) form;
		String id = IDGenerater.getId();
		LabelBean bean = new LabelBean();
		read(laform, bean);
		if(bean.getId()!= null && !"".equals(bean.getId())){
			mgt.delLabel(bean.getId());
			
		}
		bean.setId(id);
		bean.setCreateBy(loginBean.getId());
		bean.setCreateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));

		Result al = mgt.findAll();
		if (al.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<LabelBean> list = (List<LabelBean>) al.retVal;
			request.setAttribute("LabelList", list);
		}

		Result result = mgt.addLabel(bean);
		String name = request.getParameter("goodsName");
		if (name == null) {
			name = "";
		}
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			String locale = GlobalsTool.getLocale(request).toString();
			ReportDataMgt dataMgt = new ReportDataMgt();
			Result rs2 = dataMgt.getFormatList(request
					.getParameter("reportNumber"), locale,
					getLoginBean(request).getId(), getLoginBean(request)
							.getDepartCode());
			if (rs2.getRetCode() != ErrorCanst.DEFAULT_SUCCESS) {
				EchoMessage.error().add(rs2.getRetVal().toString()).setRequest(
						request);
				return getForward(request, mapping, "message");

			}
			Result print = new ReportSetMgt().getBillTable(request
					.getParameter("tableName"), "");
			if (print.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				if (print.getRealTotal() > 0) {
					List list = (List) print.getRetVal();
					ReportsBean report = (ReportsBean) list.get(0);
					request.setAttribute("BillRepNumber", report
							.getReportNumber());
					request.setAttribute("tableName", request
							.getParameter("tableName"));
					request.setAttribute("BillId", id);
					Cookie[] coks = request.getCookies();
					for (int i = 0; i < coks.length; i++) {
						Cookie cok = coks[i];
						if (cok.getName().equals("JSESSIONID")) {
							request.setAttribute("JSESSIONID", cok.getValue());
							break;
						}
					}
					request.setAttribute("SQLSave", request.getSession()
							.getAttribute("SQLSave"));
					List lists = (ArrayList) rs2.retVal;
					if (lists != null && lists.size() > 0) {
						String[] d = (String[]) lists.get(0);
						request.setAttribute("directJump", true);
						request.setAttribute("prints", true);
						request.setAttribute("BACK_URL",
								"this.parent.doPrint('" + d[0] + "@col" + d[1]
										+ "@row" + "','"
										+ request.getParameter("tableName")
										+ "','" + report.getReportNumber()
										+ "','" + id + "')");
					}
				}
			}
			EchoMessage.success().add(
					getMessage(request, "common.msg.addSuccess")).setBackUrl(
					"/LabelAction.do?operation=6").setAlertRequest(request);
		} else {
			EchoMessage.error().add(
					getMessage(request, "common.msg.addFailture")).setBackUrl(
					"/LabelAction.do?operation=6").setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}

	/**
	 * 更新
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward updateLabel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		if (id != null && !"".equals(id)) {
			Result r = mgt.getQueryId(id,0);
			List<String[]> list = (ArrayList<String[]>)r.retVal;
			if(list != null && list.size()>0){
				String[] str = (String[])list.get(0);
				StringBuffer msg = new StringBuffer();
				String[] s = new String[]{"id","goodsCode","unit","seq","batchNo","design","color","colorName","colorBit","coil","meter","qty","createBy","createTime","gram","breadth","density","user1","user2","goodsFullName","goodsNumber","trackNo","procedures","Design","User1","User2"};
				msg.append("[{");
				for(int i =0;i<s.length;i++){
					if(i>s.length-4){
						Result re = null;
						if(str[i]!= null && !"".equals(str[i])){
							re = mgt.getQueryProp(str[i],s[i]);
							if(re.retCode == ErrorCanst.DEFAULT_SUCCESS){
		        				msg.append("\""+s[i]+"\": \""+re.retVal+"\"");
		        			}else{
		        				msg.append("\""+s[i]+"\": \"\"");
		        			}
						}else{
							msg.append("\""+s[i]+"\": \"\"");
						}
					}else{
						msg.append("\""+s[i]+"\": \""+str[i]+"\"");
					}
					if(i<s.length-1){
						msg.append(",");
					}
				}
				msg.append("}]");
				request.setAttribute("msg", msg.toString());
			}
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 查询列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		LabelSearchForm searchForm = (LabelSearchForm)form;
		Result res = mgt.getGoodsAttribute();
		ArrayList list = new ArrayList();
		if (res.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			list = (ArrayList)res.retVal;
			//request.setAttribute("list", res.retVal);
		}
		
		String meter = BaseEnv.systemSet.get("Meter").getSetting();
		if("true".equals(meter)){
			String[] str= new String[2];
			str[0] = sysCodeMeter;
			str[1] = getMessage(request, "Label.meter");
			
			list.add(str);
		}
		//request.setAttribute("Meter", meter);

		String coli = BaseEnv.systemSet.get("Coil").getSetting();
		if("true".equals(coli)){
			String[] str= new String[2];
			str[0] = sysCodeCoil;
			str[1] = getMessage(request, "Label.coil");
			list.add(str);
		}
		//request.setAttribute("Coli", coli);
			
		StringBuffer msg = new StringBuffer("goodsCode,goodsFullName,seq,qty,trackNo,procedures");
		if(list != null && list.size()>0){
			msg.append(",");
			for(int i=0;i<list.size();i++){
				String[] str = (String[])list.get(i);
				msg.append(str[0].toLowerCase());
				if(i<list.size()-1){
					msg.append(",");
				}
			}
		}
		request.setAttribute("nameList", list);
		request.setAttribute("msg", msg.toString());
		String keyword =searchForm.getKeywordSearch();
		String types = request.getParameter("types");
		if(types!=null && !"".equals(types)){
			
		}else{
			keyword = keyword == null? "" : GlobalsTool.toChinseChar(keyword);
		}
		searchForm.setKeywordSearch(keyword);
		Result r = mgt.getQuery(keyword,searchForm.getPageNo(), searchForm.getPageSize());
		if (r.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<String[]> re = (ArrayList<String[]>)r.retVal;
			//if(list != null && list.size()>0){
			request.setAttribute("pageBar", pageBar(r, request));
			request.setAttribute("LabelList", re);
		}
		Result rst = mgt.getNum();
		if (rst.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("decimalnum", rst.retVal);
		}
		return getForward(request, mapping, "labelList");
	}
	
	/**
	 * 删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String id = request.getParameter("id");
		if(id != null && !"".equals(id)){
			Result rs = mgt.delLabel(id);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				return query(mapping, form, request, response);
			}
		}
		return query(mapping, form, request, response);
	}
}
