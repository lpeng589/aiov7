package com.menyi.aio.web.billNumber;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.TblBillNoBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.billNumber.BillNo.BillNoUnit;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.*;

/**
 * 
 * <p>Title:单据编号Action</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class BillNoAction extends MgtBaseAction{

	BillNoMgt mgt = new BillNoMgt();
	private static Map<String, String> map = new LinkedHashMap<String, String>();
	static {
		map.put("{date yyyy-MM-dd}", "年年年年-月月-日日");map.put("{date yyyy/MM/dd}", "年年年年/月月/日日");map.put("{date yyyyMMdd}", "年年年年月月日日");
		map.put("{date yyyy}", "年年年年");map.put("{date yy}", "年年");map.put("{date MM}", "月月");map.put("{date dd}", "日日");
		map.put("{date HH:mm:ss}", "时时:分分:秒秒");
		map.put("{login.getempFullName}", "用户全称");map.put("{login.getname}", "登录名");map.put("{login.getdepartCode}", "部门编号");
		map.put("{login.getdepartmentName}", "部门名称");	map.put("{serial 000}", "流水号格式[000]");map.put("{serial 0000}", "流水号格式[0000]");
		map.put("{serial 00000}", "流水号格式[00000]");
	}
	
	/**
	 * exe 控制器入口函数
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD_PREPARE:
			//增加前
			forward = addPreBillNo(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD:
			//增加
			forward = addBillNo(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			//修改前
			forward = updatePreBillNo(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			//修改
			forward = updateBillNo(mapping, form, request, response);
			break;
		case OperationConst.OP_QUERY:
			//查询列表
			forward = query(mapping, form, request, response);
			break;
		case OperationConst.OP_DETAIL:
			//详情
			forward = detailBillNo(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:
			//删除
			forward = deleteBillNo(mapping, form, request, response);
			break;
		default:
			String selectType = request.getParameter("selectType");
			if(selectType != null && "queryCase".equals(selectType)){
				//根据pattern转化格式
				forward = queryPattern(mapping, form, request, response);
			}else if(selectType != null && "createCase".equals(selectType)){
				//
				forward = createCase(mapping, form, request, response);
			}else if(selectType != null && "validateBillNO".equals(selectType)){
				//验证模块标识是否存在
				forward = validateBillNo(mapping, form, request, response);
			}else if(selectType != null && "resetBillNO".equals(selectType)){
				//重置单据编号
				forward = resetBillNO(mapping, form, request, response);
			}else if(selectType != null && "queryTable".equals(selectType)){
				//弹出框选择满足条件的所有表
				forward = queryTable(mapping, form, request, response);
			}else if(selectType != null && "queryField".equals(selectType)){
				//弹出框选择满足条件的所有字段
				forward = queryField(mapping, form, request, response);
			}else{
				//查询列表
				forward = query(mapping, form, request, response);
			}
		}
		return forward;
	}
	
	/**
	 * 查询单据编号列表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		BillNoSearchForm searchForm = (BillNoSearchForm)form;
		Result rs = mgt.query(searchForm);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("billList", rs.retVal);
			request.setAttribute("pageBar", this.pageBar(rs, request));
		}
		return getForward(request, mapping, "billNoList");
	}
	
	
	/**
	 * 增加单据编号前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addPreBillNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		return getForward(request, mapping, "dealBillNo");
	}
	
	/**
	 * 增加
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward addBillNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		BillNoForm billNoForm = (BillNoForm)form;
		TblBillNoBean billNoBean = new TblBillNoBean();
		String supplementNo = request.getParameter("supplementNo");
		if(supplementNo != null && "1".equals(supplementNo)){
			//保存时生成编号，isfillBack=true,setIsAddbeform=false;
			billNoBean.setIsfillback(true);
			billNoBean.setIsAddbeform(false);
		}else if(supplementNo != null && "2".equals(supplementNo)){
			//补号时，isfillBack=true,setIsAddbeform=true;
			billNoBean.setIsfillback(true);
			billNoBean.setIsAddbeform(true);
		}else{
			//新增时生成编号时，isfillBack=false,setIsAddbeform=false;
			billNoBean.setIsfillback(false);
			billNoBean.setIsAddbeform(false);
		}
		Integer start = billNoForm.getStart();
		billNoBean.setKey(billNoForm.getKey());
		billNoBean.setReset(billNoForm.getReset());
		billNoBean.setStart(start);
		billNoBean.setStep(billNoForm.getStep());
		billNoBean.setPattern(billNoForm.getPattern());
		billNoBean.setBillName(billNoForm.getBillName());
		billNoBean.setBillNO(billNoForm.getStart()-billNoForm.getStep());
		billNoBean.setLaststamp(System.currentTimeMillis());
		billNoBean.setExplain(this.getConverPattern(billNoForm.getPattern()));						//规则转换为文字
		billNoBean.setStatusId("0");
		billNoBean.setIsDefaultLoginPerson(billNoForm.getIsDefaultLoginPerson());
		Result rs = mgt.addBillNumber(billNoBean);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if(billNoBean.getIsfillback()){
				mgt.updateDbFieldIdentityStr(billNoBean.getKey(), DBFieldInfoBean.INPUT_ONLY_READ);
			}
			request.setAttribute("dealAsyn", "true");
			String noback = request.getParameter("noback");
			String url = "";
			if(noback != null && "true".equals(noback)){
				//保存并返回
				url = "/BillNo.do?operation=6";
			}else{
				url = "/BillNoAction.do?operation=4";
			}
			EchoMessage.success().add(getMessage(
                    request, "common.msg.addSuccess")).setBackUrl(url).setAlertRequest(request);
			
			new DynDBManager().addLog(0, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), billNoForm.getBillName(), "tblBillNo", "单据编号设置","");
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 修改前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward updatePreBillNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String key = request.getParameter("key");
		//根据Id加载数据
		Result result = mgt.onLoad(GlobalsTool.toChinseChar(key));
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("bean", result.retVal);
		}
		return getForward(request, mapping, "dealBillNo");
	}
	
	/**
	 * 修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward updateBillNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		BillNoForm billNoForm = (BillNoForm)form;
		TblBillNoBean billNoBean = new TblBillNoBean();
		String supplementNo = request.getParameter("supplementNo");
		if(supplementNo != null && "1".equals(supplementNo)){
			//保存时生成编号，isfillBack=true,setIsAddbeform=false;
			billNoBean.setIsfillback(true);
			billNoBean.setIsAddbeform(false);
		}else if(supplementNo != null && "2".equals(supplementNo)){
			//补号时，isfillBack=true,setIsAddbeform=true;
			billNoBean.setIsfillback(true);
			billNoBean.setIsAddbeform(true);
		}else{
			//新增时生成编号时，isfillBack=false,setIsAddbeform=false;
			billNoBean.setIsfillback(false);
			billNoBean.setIsAddbeform(false);
		}
		Integer start = billNoForm.getStart();
		//start--;
		billNoBean.setId(billNoForm.getId());
		billNoBean.setKey(billNoForm.getKey());
		billNoBean.setLaststamp(System.currentTimeMillis());
		billNoBean.setPattern(billNoForm.getPattern());
		billNoBean.setBillName(billNoForm.getBillName());
		billNoBean.setReset(billNoForm.getReset());
		billNoBean.setStart(start);
		billNoBean.setStep(billNoForm.getStep());
		billNoBean.setExplain(this.getConverPattern(billNoForm.getPattern()));						//规则转换为文字
		billNoBean.setStatusId("0");
		billNoBean.setIsDefaultLoginPerson(billNoForm.getIsDefaultLoginPerson());
		Result rs = mgt.updateBillNumber(billNoBean);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if(billNoBean.getIsfillback()){
				mgt.updateDbFieldIdentityStr(billNoBean.getKey(), DBFieldInfoBean.INPUT_ONLY_READ);
			}else{
				mgt.updateDbFieldIdentityStr(billNoBean.getKey(), DBFieldInfoBean.INPUT_NORMAL);
			}
			request.setAttribute("dealAsyn", "true");
			new BillNoManager().unRegister(billNoForm.getKey());						//清除Map BillNoManager类中
			EchoMessage.success().add("修改成功！如需立即生效，请点击重置，否则会继续按原规则编号").setBackUrl("/BillNoAction.do?operation=4").setAlertRequest(request);
			
			new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), billNoForm.getBillName(), "tblBillNo", "单据编号设置","");
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 根据pattern转化格式
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryPattern(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String patterns = request.getParameter("patterns");
		String serial = request.getParameter("serial");
		patterns = GlobalsTool.toChinseChar(patterns);
		LoginBean loginBean = this.getLoginBean(request);
		String result = new BillFormat(patterns).parseInt(Integer.parseInt(serial), new HashMap<String, String>(),loginBean,true);
		request.setAttribute("msg", result);
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 转化规则为文字
	 * @param pattern
	 * @return
	 */
	public String getConverPattern(String str){
		
		String pattern = str;
		pattern = pattern.replace("{","&{").replace("}","}&");
		String[] values = pattern.split("&");
		String svalue = "";
		for(int i=0;i<values.length;i++){
			String value = values[i];
			if(values[i].indexOf("{input.")!=-1){
				value = value.replace("{input.","").replace("}","");
			}
			Iterator iter = map.keySet().iterator();
			while(iter.hasNext()){
				Object o = iter.next();
				value = value.replace(String.valueOf(o), map.get(o));
			}
			svalue = svalue+value;
		}
		return svalue;
	}
	
	
	/**
	 * 详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward detailBillNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String key = request.getParameter("key");
		//根据Id加载数据
		Result result = mgt.onLoad(GlobalsTool.toChinseChar(key));
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("bean", result.retVal);
		}
		return getForward(request, mapping, "detailBillNo");
	}
	
	/**
	 * 删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward deleteBillNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String[] keyids = request.getParameterValues("keyId");
		mgt.delete(keyids);
		for(String key : keyids){
			new BillNoManager().unRegister(key);						//清除Map BillNoManager类中
		}
		request.setAttribute("dealAsyn", "true");
		return query(mapping, form, request, response);
	}
	
	/**
	 * 测试数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward createCase(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		String key = request.getParameter("key");
		LoginBean loginbean = this.getLoginBean(request);
		int count=0;
		while(count<10){
			BillNo billno = new BillNoManager().find(key);		//加载Map并赋值
			if(billno == null){
				return null;
			}else{
				BillNoUnit unit = billno.getNumber(new HashMap<String, String>(), loginbean);
				map.put(unit.getValue()+"_"+count, unit.getValStr());
				billno.remove(unit.getValStr());
				map.put(unit.getValue()+"_del_"+count, unit.getValStr());
				count++;
			}
		}
		String msg = "[";
		Iterator iter = map.keySet().iterator();
		while(iter.hasNext()){
			Object o = iter.next();
			msg += "{\"key\": \""+o+"\",\"value\":\""+map.get(o)+"\"},";
			
		}
		msg = msg.substring(0,msg.length()-1);
		msg += "]";
		request.setAttribute("msg", msg);
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 验证模块标识是否存在
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward validateBillNo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String key = request.getParameter("key");			//模块标识
		//根据key加载数据
		Result result = mgt.onLoad(GlobalsTool.toChinseChar(key));
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			if(result.getRetVal()==null){
				request.setAttribute("msg", "NO");
			}else{
				request.setAttribute("msg", "OK");
			}
		}
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * 重置单据编号
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward resetBillNO(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String key = request.getParameter("keyId");			//模块标识
		BillNo billNo = BillNoManager.find(key);
		billNo.reset();
		
		EchoMessage.success().add(getMessage(
                request, "common.msg.updateSuccess")).setBackUrl("/BillNoAction.do?operation=4").setAlertRequest(request);
		Result rs =mgt.onLoad(key);
		if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
			TblBillNoBean tbb  = (TblBillNoBean)rs.retVal;
			new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(), tbb.getBillName()+":单据编号重置", "tblBillNo", "单据编号设置","");
		}
		return getForward(request, mapping, "message");
	}
	
	
	/**
	 * 读取所有满足条件的表
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryTable(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String searchName = request.getParameter("searchName");
		Result result = mgt.selectTable(searchName);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("tableList", result.getRetVal());
		}
		request.setAttribute("searchName", searchName);
		return getForward(request, mapping, "selectTable");
	}
	
	/**
	 * 读取所有满足条件表的字段
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryField(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String searchName = request.getParameter("searchName");
		String tableId = request.getParameter("tableId");
		Result result = mgt.selectField(tableId);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("fields", result.getRetVal());
		}
		request.setAttribute("searchName", searchName);
		return getForward(request, mapping, "selectField");
	}
}
