package com.koron.oa.office.car.carOut;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.office.car.carInfo.CarInfoMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class CarOperateAction extends BaseAction{
	CarOperateMgt mgt = new CarOperateMgt();
	CarInfoMgt InfoMgt = new CarInfoMgt();
	AdviceMgt admgt = new AdviceMgt();
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
		ActionForward forward = null;
		switch (operation) {
		// 新增前准备
		case OperationConst.OP_ADD_PREPARE:
			forward = outPrepare(mapping, form, request, response);
			break;	
		// 新增操作
		case OperationConst.OP_ADD:
			forward = outCar(mapping, form, request, response);
			break;	
		// 修改前准备
		case OperationConst.OP_UPDATE_PREPARE:		
				forward = updateOutPre(mapping, form, request, response);			
			break;
		// 修改
		case OperationConst.OP_UPDATE:		
				forward = updateOut(mapping, form, request, response);			
			break;
		// 根据条件查询
		case OperationConst.OP_QUERY:			
            	forward = queryOut(mapping, form, request, response);           															
			break;
		// 明细操作
		case OperationConst.OP_DETAIL:			
				forward = allOut(mapping, form, request, response);
			break;
		// 删除操作
		case OperationConst.OP_DELETE:			
				forward = delOut(mapping, form, request, response);
			break;
		// guihuan操作
		case OperationConst.OP_QUOTE:			
				forward = backOut(mapping, form, request, response);
			break;
		//默认
		default:	
				forward = queryOut(mapping, form, request, response); 
		}
		return forward;
	}

	private ActionForward backOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		if(id!=null && !"".equals(id)){
			OACarOperateBean bean = (OACarOperateBean)mgt.loadCarOperate(id).retVal;
			Result rs = mgt.backCar(bean,getLoginBean(request).getEmpFullName());
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add("归还成功！").setBackUrl("/CarOperateAction.do").setAlertRequest(request);		    	
			}
		}
		return getForward(request, mapping, "message");
	}

	private ActionForward allOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		OACarOperateBean bean = (OACarOperateBean)mgt.loadCarOperate(id).retVal;
		if(bean.getDynamic() !=null && !"".equals(bean.getDynamic())){
			String[] list = bean.getDynamic().split(";");
			request.setAttribute("List", list);
		}
		return getForward(request, mapping, "allOut");
	}

	private ActionForward outPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String carFlag = request.getParameter("carFlay");
		String id = request.getParameter("id");
		if(carFlag!=null && "PAI".equals(carFlag)){
			request.setAttribute("carFlag", carFlag);
			request.setAttribute("name", "派车");
		}else{
			request.setAttribute("name", "车辆领用");
		}
		if(id!=null && !"".equals(id)){
			Result pre = mgt.loadCarOperate(id);
			request.setAttribute("perList", pre.retVal);
		}
		
		Result rs = mgt.getFreeCar();
		Result ext = InfoMgt.queryCheck();		
		request.setAttribute("nameList", ext.retVal);
		
		request.setAttribute("carList", rs.retVal);
		return getForward(request, mapping, "addOutPre");
	}

	private ActionForward outCar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		CarOperateForm lvForm = (CarOperateForm)form;
		OACarOperateBean bean = new OACarOperateBean();
		String nowTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
		if(lvForm !=null ){
			read(lvForm, bean);
			bean.setId(IDGenerater.getId());
			bean.setOutCarDate(lvForm.getOutCarDate());
			bean.setOverCarDate(lvForm.getOverCarDate());
			bean.setUserCarPerson(lvForm.getUserCarPerson());
			bean.setUserCarReason(lvForm.getUserCarReason());
			bean.setDynamic(nowTime+"被"+GlobalsTool.getEmpFullNameByUserId(bean.getUserCarPerson())+" 申请领用;");
			
			Result rs = mgt.addCarOperate(bean, getLoginBean(request).getId());
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//发送审批人通知 
				admgt.add(bean.getUserCarPerson(), bean.getCarNo()+"被申请领用，请及时审批!",
						"<a href=\"javascript:mdiwin('/CarOperateAction.do?operation=4','RES<派车>')\">"+bean.getCarNo()+"被申请领用，请及时审批!</a>", bean.getApprover(), bean.getId(), "car");
				EchoMessage.success().add("申请成功！").setBackUrl("/CarOperateAction.do").setAlertRequest(request);
		    	lvForm.reset(mapping, request);
			}
		}
		return getForward(request, mapping, "message");
	}

	private ActionForward updateOutPre(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String carFlag = request.getParameter("carFlay");
		String id = request.getParameter("id");
		if(carFlag!=null && "UPDATE".equals(carFlag)){
			request.setAttribute("name", "领用修改");
		}
		if(id!=null && !"".equals(id)){
			Result pre = mgt.loadCarOperate(id);
			request.setAttribute("perList", pre.retVal);
		}
		Result rs = mgt.getFreeCar();
		Result ext = InfoMgt.queryCheck();		
		request.setAttribute("nameList", ext.retVal);
		
		request.setAttribute("carList", rs.retVal);
		return getForward(request, mapping, "addOutPre");
	}
	/**
	 * 派车或否决,修改申请
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward updateOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String Outflag = request.getParameter("Outflag");
		String id = request.getParameter("id");
		String carFlag = request.getParameter("carFlay");
		String nowTime = BaseDateFormat.format(new java.util.Date(),BaseDateFormat.yyyyMMddHHmmss);
		OACarOperateBean bean = (OACarOperateBean)mgt.loadCarOperate(id).retVal;
		String reason = request.getParameter("reason");
		bean.setReason(reason);
		//启用审核
		if(Outflag!=null && !"".equals(Outflag)){
			Result rs = mgt.orNoCarOperate(bean,Outflag);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				admgt.deleteByRelationId(bean.getId(), bean.getApprover());
				if(bean.getStatus() !=null && "1".equals(bean.getStatus())){
					admgt.add(bean.getApprover(), bean.getCarNo()+"被申请领用审批通过!",
							"<a href=\"javascript:\">"+bean.getCarNo()+"被申请领用审批通过!</a>", bean.getUserCarPerson(), bean.getId(), "car");
				}
				if(bean.getStatus() !=null && "2".equals(bean.getStatus())){
					admgt.add(bean.getApprover(), bean.getCarNo()+"被申请领用审批未通过!原因"+bean.getReason(),
							"<a href=\"javascript:\">"+bean.getCarNo()+"被申请领用审批未通过!原因"+bean.getReason()+"</a>", bean.getUserCarPerson(), bean.getId(), "car");
				}
				EchoMessage.success().add("操作成功！").setBackUrl("/CarOperateAction.do").setAlertRequest(request);
			}
		}
		//不启用审核
		if(carFlag !=null && "UPDATE".equals(carFlag)){
			CarOperateForm lvForm = (CarOperateForm)form;
			String fl = bean.getStatus();
			String reson = bean.getDynamic();
			read(lvForm, bean);	
			bean.setId(id);
			bean.setOutCarDate(lvForm.getOutCarDate());
			bean.setOverCarDate(lvForm.getOverCarDate());
			bean.setUserCarPerson(lvForm.getUserCarPerson());
			bean.setUserCarReason(lvForm.getUserCarReason());
			bean.setStatus(fl);
			bean.setDynamic(reson+nowTime+"被修改;");
			Result rs = mgt.updateCarOperate(bean,getLoginBean(request).getId());	
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add("修改成功！").setBackUrl("/CarOperateAction.do")
		                .setAlertRequest(request);
			}
		}
		
		return getForward(request, mapping, "message");
	}
	/**
	 * 删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ActionForward delOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String Flay = request.getParameter("Flay");
		if(Flay !=null && "One".equals(Flay)){
			String id = request.getParameter("id");
			//查询是否有有借没还
			Result jh = mgt.sigStatus(id);
			ArrayList jhrs = (ArrayList)jh.retVal;
			Object fg = ((Object[])jhrs.get(0))[0];
			if(fg == null){
				EchoMessage.error ().add("不能删除，被领用或没完成！").setBackUrl("/CarOperateAction.do")
		                .setAlertRequest(request);
			}else{				
				Result rs = mgt.delOneCarOperate(id);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add("删除成功 ！").setBackUrl("/CarOperateAction.do")
			                .setAlertRequest(request);
				}
			}	
		}
		if(Flay !=null && "All".equals(Flay)){
			//查询是否有有借没还
			String[] keyId = request.getParameterValues("keyId");
			if(keyId!=null && keyId.length>0){
				Result jh = mgt.outStatus(keyId);
				ArrayList jhrs = (ArrayList)jh.retVal;
				if(jhrs !=null && jhrs.size()>0){
					boolean fy = true;
					for (int i = 0; i < jhrs.size(); i++) {
						Object fg = ((Object[])jhrs.get(i))[0];
						if(fg == null || "1".equals(fg.toString())  || "0".equals(fg.toString())){
							fy=false;
						}
					}
					if(fy){
						Result rs = mgt.delMoreCarOperate(keyId);
						if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
							EchoMessage.success().add("删除成功 ！").setBackUrl("/CarOperateAction.do")
					                .setAlertRequest(request);
						}
					}else{
						EchoMessage.error ().add("不能删除，被领用或没完成！").setBackUrl("/CarOperateAction.do")
				                .setAlertRequest(request);
					}
				}
			}					
		}
		return getForward(request, mapping, "message");
	}

	private ActionForward queryOut(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		CarOperateForm lvForm = (CarOperateForm)form;
		LoginBean login = getLoginBean(request);
		String loginid = login.getId();
		//和比较审批人比较 加载模块
		Result ext = InfoMgt.queryCheck();	
		ArrayList parm = (ArrayList)ext.retVal;
		String loadFlag = "false";
		if(parm!=null && parm.size()>0 && !"".equals(parm)){
			for (int i = 0; i < parm.size(); i++) {
				Object fy = ((Object[])parm.get(i))[2];
				String spId = fy==null?"":fy.toString();
				if(!loginid.equals(spId)){
					loadFlag="true";
				}
			}
		}		
		request.setAttribute("nameList", ext.retVal);
		request.setAttribute("loadFlag", "1".equals(loginid)?"true":loadFlag);
		String fTimes = request.getParameter("fTimes");
		String eTimes = request.getParameter("eTimes");
		String sortInfo  = getParameter("sortInfo", request) ;//排序信息 排序标识,排序名
		HashMap<String,String> conMap = (HashMap) request.getAttribute("conMap") ;
		if(conMap == null){
			conMap = new HashMap<String, String>() ;
		}
		conMap.put("sortInfo", sortInfo) ;
		request.setAttribute("conMap", conMap) ;		
		Result rs = mgt.queryCarOperate(lvForm, fTimes, eTimes, sortInfo);
		request.setAttribute("list", rs.retVal);
		request.setAttribute("lvForm", lvForm);
		request.setAttribute("fTimes", fTimes);
		request.setAttribute("eTimes", eTimes);
		request.setAttribute("loginId", loginid);
		return getForward(request, mapping, "carOut");
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		return null;
	}
}
