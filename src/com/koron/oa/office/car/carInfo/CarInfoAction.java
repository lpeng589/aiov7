package com.koron.oa.office.car.carInfo;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OperationConst;

public class CarInfoAction extends BaseAction{
	CarInfoMgt mgt = new CarInfoMgt();
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
		/*MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/CarInfoAction.do");
		request.setAttribute("add", mop.add()); // 增加权限
		request.setAttribute("del", mop.delete()); // 删除权限
		request.setAttribute("update", mop.update()); // 查询权限
		request.setAttribute("query", mop.query()); // 修改权限*/
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);		
		ActionForward forward = null;
		switch (operation) {
		// 新增前准备
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;	
		// 新增操作
		case OperationConst.OP_ADD:
			forward = addCar(mapping, form, request, response);
			break;	
		// 修改前准备
		case OperationConst.OP_UPDATE_PREPARE:
			String updateFlag = request.getParameter("updateFlag");
			if("XY".equals(updateFlag)){
				forward = preOther(mapping, form, request, response);	
			}else{
				forward = updatePrepare(mapping, form, request, response);			
			}				
			break;
		// 修改
		case OperationConst.OP_UPDATE:
			String updateFg = request.getParameter("updateFlag");
			if("XY".equals(updateFg)){
				forward = updateOther(mapping, form, request, response);	
			}else{
				forward = updateCar(mapping, form, request, response);				
			}
						
			break;
		// 根据条件查询
		case OperationConst.OP_QUERY:			
				forward = queryCar(mapping, form, request, response);		           	           															
			break;
		// 删除操作
		case OperationConst.OP_DELETE:			
				forward = delCar(mapping, form, request, response);
			break;
		// 添加审核人qian 
		case OperationConst.OP_CHECK_PREPARE:			
				forward = addCheckPre(mapping, form, request, response);
			break;
		// 添加审核人 
		case OperationConst.OP_CHECK:			
				forward = addCheck(mapping, form, request, response);
			break;
		// 验证唯一 
		case OperationConst.OP_RECEIVE:			
				forward = uniqChecked(mapping, form, request, response);
			break;
		//默认
		default:	
				forward = queryCar(mapping, form, request, response); 
		}
		return forward;
	}

	/**
	 * 修改保养保险
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		CarInfoForm lvForm = (CarInfoForm)form;
		String openFlag = request.getParameter("openFlag");
		request.setAttribute("openFlag", openFlag);
		String id = request.getParameter("id");
		OACarInfoBean bean = (OACarInfoBean)mgt.loadCar(id).retVal;
		if("2".equals(openFlag)){
			bean.setInsureCost(lvForm.getInsureCost());
			bean.setInsureDate(lvForm.getInsureDate());
			bean.setOvreDate(lvForm.getOvreDate());
			bean.setDealPeople(lvForm.getDealPeople());
			bean.setCompany(lvForm.getCompany());
		}else{
			bean.setNextMaintainDate(lvForm.getNextMaintainDate());
			bean.setMaintainPeople(lvForm.getMaintainPeople());
		}
		Result rs = mgt.updateCar(bean);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(new DynDBManager().getDefSQLMsg(
					getLocale(request).toString(), "修改成功！")).setBackUrl("/CarInfoAction.do?operation=4&openFlag="+openFlag)
	                .setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	private ActionForward preOther(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String openFlag = request.getParameter("openFlag");
		request.setAttribute("openFlag", openFlag);
		String id = request.getParameter("id");
		Result rs = mgt.loadCar(id);
		request.setAttribute("rsList", rs.retVal);
		return getForward(request, mapping, "updateOther");
	}

	private ActionForward uniqChecked(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String carNO = request.getParameter("carNO");
		if(carNO!=null && !"".equals(carNO)){
			Result rs = mgt.uniqChecked(carNO);
			ArrayList rss = (ArrayList)rs.retVal;
			if(rss!=null && rss.size()>0){
				request.setAttribute("msg", "该车牌号已存在，请重新填写一个");
			}else{
				request.setAttribute("msg","3");
			}
		}
		return getForward(request, mapping, "blank");
	}

	private ActionForward addCheckPre(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{	
		Result ext = mgt.queryCheck();
		ArrayList param = (ArrayList)ext.retVal;
		StringBuffer name = new StringBuffer();
		StringBuffer nameId = new StringBuffer();
		if(param.size()>0 && param !=null){			
			for (int i=0;i<param.size();i++) {
				Object id = ((Object[])param.get(i))[2];
				Object role = ((Object[])param.get(i))[1];
				nameId.append(id==null?"":id.toString()+";");
				name.append(role==null?"":role.toString()+";");
			}			
		}
		request.setAttribute("name", name);	
		request.setAttribute("nameId",nameId);
		return getForward(request, mapping, "checkPre");
	}
	private ActionForward addCheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String checker = request.getParameter("checker");
		String checkId = request.getParameter("checkId");					
		Result rs = mgt.addCheck(checker,checkId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
	                request, "common.msg.addSuccess")).setBackUrl("/CarInfoAction.do").setAlertRequest(request);		    	
		}						
		return getForward(request, mapping, "message");
	}

	private ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{	
		Result ext = mgt.queryCheck();		
		request.setAttribute("list", ext.retVal);
		request.setAttribute("nameList", "添加车辆信息");
		return getForward(request, mapping, "addPre");
	}

	private ActionForward addCar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		CarInfoForm lvForm = (CarInfoForm)form;
		OACarInfoBean bean = new OACarInfoBean();
		if(lvForm !=null){
			//把图片从temp复制到文件夹下
			if(lvForm.getCarPicture() !=null && !"".equals(lvForm.getCarPicture() )){
				String table = "OACar";
				String src = lvForm.getCarPicture().substring(58);		
	        	String fn = FileHandler.getRealFileName(table, FileHandler.TYPE_PIC,src);     
	            FileHandler.copy(table, FileHandler.TYPE_PIC,src,fn);	        
	            FileHandler.deleteTemp(src);
	            lvForm.setCarPicture(src);
			}
				        	        
			read(lvForm, bean);
			bean.setId(IDGenerater.getId());
			bean.setNextMaintainDate(lvForm.getNextMaintainDate());
			bean.setOvreDate(lvForm.getOvreDate());
			bean.setPrice(lvForm.getPrice());
			bean.setRemark(lvForm.getRemark());
			bean.setRunPapersDate(lvForm.getRunPapersDate());
			bean.setSurvey(lvForm.getSurvey());
			bean.setSurveyDate(lvForm.getSurveyDate());
			bean.setMaintainPeople(lvForm.getMaintainPeople());
			bean.setFlag("0");//biaoshi kongxian  
			System.out.println(bean);
			Result rs= mgt.addCar(bean,getLoginBean(request).getId());
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add(getMessage(
		                request, "common.msg.addSuccess")).setBackUrl("/CarInfoAction.do").setAlertRequest(request);
		    	lvForm.reset(mapping, request);
			}
		}
		return getForward(request, mapping, "message");
	}

	private ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String id = request.getParameter("id");
		Result rs = mgt.loadCar(id);
		Result ext = mgt.queryCheck();		
		request.setAttribute("list", ext.retVal);	
		request.setAttribute("perList", rs.retVal);
		request.setAttribute("nameList", "修改车辆信息");
		return getForward(request, mapping, "addPre");
	}

	private ActionForward updateCar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		CarInfoForm lvForm = (CarInfoForm)form;
		String id = request.getParameter("id");
		OACarInfoBean bean = (OACarInfoBean)mgt.loadCar(id).retVal;
		if(lvForm !=null){
			//把图片从temp复制到文件夹下
			if(lvForm.getCarPicture() !=null && !"".equals(lvForm.getCarPicture() )){
				String table = "OACar";
				String src = lvForm.getCarPicture().substring(58);		
	        	String fn = FileHandler.getRealFileName(table, FileHandler.TYPE_PIC,src);     
	            FileHandler.copy(table, FileHandler.TYPE_PIC,src,fn);	        
	            FileHandler.deleteTemp(src);
	            lvForm.setCarPicture(src);
			}
			String flag = bean.getFlag();
			read(lvForm, bean);
			bean.setId(id);
			bean.setNextMaintainDate(lvForm.getNextMaintainDate());
			bean.setOvreDate(lvForm.getOvreDate());
			bean.setPrice(lvForm.getPrice());
			bean.setRemark(lvForm.getRemark());
			bean.setRunPapersDate(lvForm.getRunPapersDate());
			bean.setSurvey(lvForm.getSurvey());
			bean.setSurveyDate(lvForm.getSurveyDate());
			bean.setRemark(lvForm.getRemark());
			bean.setMaintainPeople(lvForm.getMaintainPeople());
			bean.setFlag(flag);
			Result rs= mgt.updateCar(bean);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add(new DynDBManager().getDefSQLMsg(
						getLocale(request).toString(), "修改成功！")).setBackUrl("/CarInfoAction.do").setAlertRequest(request);
		    	lvForm.reset(mapping, request);
			}
		}
		return getForward(request, mapping, "message");
	}

	private ActionForward delCar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String flay = request.getParameter("Flay");
		if("All".equals(flay)){
			String[] ids = request.getParameterValues("keyId");
			//判断是否能被删除
			String id = "''";
			for (String key : ids) {
				id += ",'"+key+"'";
			}
			Result rs = mgt.delOrNo(id);
			ArrayList parm = (ArrayList)rs.retVal;
			if(parm !=null && parm.size()>0){
				Object fy = ((Object[])parm.get(0))[0];
				String sum = fy==null?"0":fy.toString();
				if(Integer.parseInt(sum) > 0){
					EchoMessage.error().add(new DynDBManager().getDefSQLMsg(
							getLocale(request).toString(), "该车被领用过，信息不能删除！ ")).setBackUrl("/CarInfoAction.do").setAlertRequest(request);		    	
				}else{
					Result res = mgt.delMoreCar(ids);
					if(res.retCode == ErrorCanst.DEFAULT_SUCCESS){
						EchoMessage.success().add(new DynDBManager().getDefSQLMsg(
								getLocale(request).toString(), "删除成功！ ")).setBackUrl("/CarInfoAction.do").setAlertRequest(request);
					}
				}
			}
		}
		if("One".equals(flay)){
			String id = request.getParameter("id");
			//判断是否能被删除 
			Result rs = mgt.delOrNo("'"+id+"'");
			ArrayList parm = (ArrayList)rs.retVal;		
			if(parm !=null && parm.size()>0){
				Object fy = ((Object[])parm.get(0))[0];
				String sum = fy==null?"0":fy.toString();
				if(Integer.parseInt(sum) > 0){
					EchoMessage.error().add(new DynDBManager().getDefSQLMsg(
							getLocale(request).toString(), "该车被领用过，信息不能删除！ ")).setBackUrl("/CarInfoAction.do").setAlertRequest(request);		    	
				}else{
					Result res = mgt.delOneCar(id);
					if(res.retCode == ErrorCanst.DEFAULT_SUCCESS){
						EchoMessage.success().add(new DynDBManager().getDefSQLMsg(
								getLocale(request).toString(), "删除成功！ ")).setBackUrl("/CarInfoAction.do").setAlertRequest(request);
					}
				}
			}
		}
		return getForward(request, mapping, "message");
	}

	private ActionForward queryCar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String openFlag = request.getParameter("openFlag");
		String insureTime = request.getParameter("insureTime");
		String nextTime = request.getParameter("nextTime");
		String sortInfo  = getParameter("sortInfo", request) ;//排序信息 排序标识,排序名
		HashMap<String,String> conMap = (HashMap) request.getAttribute("conMap") ;
		if(conMap == null){
			conMap = new HashMap<String, String>() ;
		}
		conMap.put("sortInfo", sortInfo) ;
		request.setAttribute("conMap", conMap) ;
		
		CarInfoForm lvForm = (CarInfoForm)form;
		Result rs = mgt.queryCar(lvForm,insureTime,nextTime,sortInfo);
		request.setAttribute("list", rs.retVal);
		request.setAttribute("lvForm", lvForm);
		if(openFlag!=null && "2".equals(openFlag)){
			request.setAttribute("openFlag", "2");
		}
		if(openFlag!=null && "3".equals(openFlag)){
			request.setAttribute("openFlag", "3");
		}
		return getForward(request, mapping, "queryCar");
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		
		return null;
	}

}
