package com.menyi.aio.web.finance.voucherSetting;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import com.dbfactory.Result;
import com.menyi.aio.bean.AccMainSettingBean;
import com.menyi.aio.bean.AccPeriodBean;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.finance.voucher.VoucherMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.web.util.*;

/**
 * 
 * <p>
 * Title:凭证设置Action
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date: 2013-03-14 上午 11:30
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
public class VoucherSettingAction extends MgtBaseAction {

	VoucherMgt mgt = new VoucherMgt();
	UserMgt user_mgt = new UserMgt();
	
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
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		
		switch (operation) {
		case OperationConst.OP_UPDATE_PREPARE:
			//修改前
			forward = updateVoucherSettingPre(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE:
			//修改
			forward = updateVoucherSetting(mapping, form, request, response);
			break;
		default:
			forward = updateVoucherSettingPre(mapping, form, request, response);
		}
		return forward;
	}
	
	/**
	 * 修改凭证设置前
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateVoucherSettingPre(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		/* 查询凭证设置*/
		Result result = mgt.queryVoucherSetting();
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//查询成功
			AccMainSettingBean bean = (AccMainSettingBean)result.retVal;
			if(bean != null){
				//审核人
				List<EmployeeBean> auditingList = new ArrayList<EmployeeBean>();
				if(bean.getAuditingPersont() != null && !"".equals(bean.getAuditingPersont())){
					for(String downUser : bean.getAuditingPersont().split(",")){
						EmployeeBean employee = user_mgt.queryEmployee2(downUser);
						if(null != employee){
							auditingList.add(employee);
						}
					}
				}
				request.setAttribute("auditingList", auditingList);
				//反审核人
				List<EmployeeBean> reverseAuditingList = new ArrayList<EmployeeBean>();
				if(bean.getReverseAuditing() != null && !"".equals(bean.getReverseAuditing())){
					for(String downUser : bean.getReverseAuditing().split(",")){
						EmployeeBean employee = user_mgt.queryEmployee2(downUser);
						if(null != employee){
							reverseAuditingList.add(employee);
						}
					}
				}
				request.setAttribute("reverseAuditingList", reverseAuditingList);
				//过账人
				List<EmployeeBean> binderList = new ArrayList<EmployeeBean>();
				if(bean.getBinderPersont() != null && !"".equals(bean.getBinderPersont())){
					for(String downUser : bean.getBinderPersont().split(",")){
						EmployeeBean employee = user_mgt.queryEmployee2(downUser);
						if(null != employee){
							binderList.add(employee);
						}
					}
				}
				request.setAttribute("binderList", binderList);
				
				//反过账人
				List<EmployeeBean> reverseBinderList = new ArrayList<EmployeeBean>();
				if(bean.getReverseBinder() != null && !"".equals(bean.getReverseBinder())){
					for(String downUser : bean.getReverseBinder().split(",")){
						EmployeeBean employee = user_mgt.queryEmployee2(downUser);
						if(null != employee){
							reverseBinderList.add(employee);
						}
					}
				}
				request.setAttribute("reverseBinderList", reverseBinderList);
				
				//复核人
				List<EmployeeBean> checkPersontList = new ArrayList<EmployeeBean>();
				if(bean.getCheckPersont() != null && !"".equals(bean.getCheckPersont())){
					for(String downUser : bean.getCheckPersont().split(",")){
						EmployeeBean employee = user_mgt.queryEmployee2(downUser);
						if(null != employee){
							checkPersontList.add(employee);
						}
					}
				}
				request.setAttribute("checkPersontList", checkPersontList);
				//指定现金流量人
				List<EmployeeBean> cashPersontList = new ArrayList<EmployeeBean>();
				if(bean.getCashPersont() != null && !"".equals(bean.getCashPersont())){
					for(String downUser : bean.getCashPersont().split(",")){
						EmployeeBean employee = user_mgt.queryEmployee2(downUser);
						if(null != employee){
							cashPersontList.add(employee);
						}
					}
				}
				request.setAttribute("cashPersontList", cashPersontList);
			}
			request.setAttribute("setting", result.retVal);
		}
		
		return getForward(request, mapping, "updateVoucherSetting");
	}
	
	/**
	 * 修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateVoucherSetting(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		/* 修改操作*/
		VoucherSettingForm forms = (VoucherSettingForm)form;
		AccMainSettingBean bean = new AccMainSettingBean();
		read(forms, bean);
		Result result = new Result();
		LoginBean lg = (LoginBean) request.getSession().getAttribute("LoginBean");
		Hashtable sessionSet = (Hashtable)BaseEnv.sessionSet.get(lg.getId()) ;
		
		/* 会计期间*/
		AccPeriodBean accPeriodBean = (AccPeriodBean)sessionSet.get("AccPeriod");
		/* 复选框值为空时默认为0*/
		if(bean.getIsAuditing()==null){
			bean.setIsAuditing(0);
			bean.setIsAccountAuditing(0);
			result = new VoucherMgt().queryNoBind(accPeriodBean);
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				if(result.realTotal>0){
	            	String errorMessage = GlobalsTool.revertTextCode2("凭证管理中存在未审核或未过账凭证，请先进行审核或过账，才可以修改启用审核");
	            	EchoMessage echo = EchoMessage.error().add(errorMessage);
	            	echo.setAlertRequest(request);
	            	return getForward(request, mapping, "alert");
				}
			}
		}
		if(bean.getIsAccountAuditing()==null){
			bean.setIsAccountAuditing(0);
		}
		if(bean.getIsCash()==null){
			bean.setIsCash(0);
		}
		if(bean.getIsCheck()==null){
			bean.setIsCheck(0);
		}
		//String isflag = new GlobalsTool().getSysSetting("standardAcc");
		//if(isflag != null && "true".equals(isflag)){
		//	bean.setIsAuditing(1);
		//	bean.setIsAccountAuditing(1);
		//}else if(isflag != null && "false".equals(isflag)){
		//	bean.setIsCheck(0);
		//}
		result = mgt.updateSetting(bean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			new DynDBManager().addLog(1, lg.getId(), lg.getEmpFullName(), lg.getSunCmpClassCode(),"修改凭证设置","tblAccMainSetting", "凭证设置","");
			EchoMessage.success().add(getMessage(
                    request, "common.msg.updateSuccess"))
                    .setBackUrl("/VoucherSettingAction.do?operation=7").
                    setAlertRequest(request);
		}else {
	         EchoMessage.success().add(getMessage(
                     request, "common.msg.updateErro"))
                     .setBackUrl("/VoucherSettingAction.do?operation=7").
                     setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
}
