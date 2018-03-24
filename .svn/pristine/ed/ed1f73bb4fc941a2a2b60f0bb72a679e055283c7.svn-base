package com.koron.oa.office.goods.applyUse;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.office.goods.GoodsSearchForm;
import com.koron.oa.office.goods.backed.BackedMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class ApplyUseAction extends MgtBaseAction{

	ApplyUseMgt aioApply = new ApplyUseMgt();
	BackedMgt aioBuy = new BackedMgt();
	
	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
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
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/ApplyGoodsAction.do");
		request.setAttribute("add", mop.add()); // 增加权限
		request.setAttribute("del", mop.delete()); // 删除权限
		request.setAttribute("update", mop.update()); // 查询权限
		request.setAttribute("query", mop.query()); // 修改权限
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
			forward = addApply(mapping, form, request, response);
			break;	
		// 修改前准备
		case OperationConst.OP_UPDATE_PREPARE:		
				forward = updatePrepare(mapping, form, request, response);			
			break;
		// 修改
		case OperationConst.OP_UPDATE:		
				forward = updateApply(mapping, form, request, response);			
			break;
		// 根据条件查询
		case OperationConst.OP_QUERY:
			String type = request.getParameter("GoodsNO");
			if(null != type && !"".equals(type) && "condition".equals(type)){
            	forward = reqApply(mapping, form, request, response);
            }else if(null != type && !"".equals(type) && "queryAll".equals(type)){
            	forward = allApply(mapping, form, request, response);
            }
            else{
            	forward = queryApply(mapping, form, request, response);
            }
															
			break;
		// 删除操作
		case OperationConst.OP_DELETE:			
				forward = delApply(mapping, form, request, response);
			break;
		//默认
		default:	
				forward = queryApply(mapping, form, request, response); 
		}
		return forward;
	}
	
	/**
	 * 根据条件查询
	 */
	private ActionForward reqApply(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		GoodsSearchForm lvForm = (GoodsSearchForm)form;	
		String id="";
		String apply_title = getParameter("apply_title", request);
		//先转换title为领用表id
		if(apply_title!=null && !"".equals(apply_title)){
			Result turnId = aioApply.turnId(apply_title);
			ArrayList paramId = (ArrayList)turnId.retVal;		
			if(paramId.size()>0){
				for (int i = 0; i < paramId.size(); i++) {
					Object param = ((Object[])paramId.get(i))[0];
					id += ",'"+param.toString()+"'";
				}
			}
		}				
		Result rs = aioApply.getApplyBy(lvForm,id==""?id:id.substring(1));	
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){				
			request.setAttribute("applyList", rs.retVal);
			request.setAttribute("pageBar", pageBar(rs, request));					
		}
		request.setAttribute("apply_title", apply_title);
		request.setAttribute("beginTime", lvForm.getBeginTime());
		request.setAttribute("endTime", lvForm.getEndTime());
		request.setAttribute("applyRole", lvForm.getApplyRole());
		return getForward(request, mapping, "ApplyGoods");							
	}

	/**
	 * 删除领用表和明细表！
	 */
	protected ActionForward delApply(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String id = getParameter("flay", request);
		String part = getParameter("part", request);
		if(part !=null && !"".equals(part) && part.equals("ALLDEL")){
			String[] idDet = getParameters("keyId", request);
			String[] applyId = getParameters("applyId", request);
			boolean flay =true;
	
			//查询此条单号明细下是否能被删除			
			for(int k = 0; k < idDet.length; k++){
				OAApplyGoodsDetBean bean = (OAApplyGoodsDetBean)aioApply.loadApplyDet(idDet[k]).retVal;
				if(bean.getBack_sign()>0){
					flay = false;
				}
			}						
			if(flay){
				//删除时库存增加
				Result res = aioApply.delDet(idDet,applyId[0]);							
				if(res.retCode==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(
			                request, "common.msg.delSuccess")).setBackUrl("/ApplyGoodsAction.do?operation=4&GoodsNO=queryAll&applyId="+applyId[0]).setAlertRequest(request);						
				}
			}else{
				EchoMessage.error().add("与归还记录关联，不能进行删除！").setBackUrl("/ApplyGoodsAction.do?operation=4&GoodsNO=queryAll&applyId="+applyId[0])
	                    .setAlertRequest(request);
			}	
		}else if(part != null && !"".equals(part) && part.equals("PARENT")){
			String[] idDet = getParameters("keyId", request);
			if(idDet != null && !"".equals(idDet)){
				Result detID = aioApply.getByApplyArr(idDet);
				ArrayList detIDList = (ArrayList)detID.retVal;
			
				//明细id数组
				String[] detArr = new String[detIDList.size()];
				if(detIDList.size()>0){		
					boolean flay =true;
					for(int i=0;i<detIDList.size();i++){
						String detid = ((Object[]) detIDList.get(i))[0].toString();
						detArr[i] = detid;
						OAApplyGoodsDetBean detbean = (OAApplyGoodsDetBean)aioApply.loadApplyDet(detid).retVal;
						if(detbean.getBack_sign() >0){
							flay = false;
							EchoMessage.error().add("与归还记录关联，不能进行删除！").setBackUrl("/ApplyGoodsAction.do")
				                    .setAlertRequest(request);
						}
						
					}
					if(flay){
						Result rs = aioApply.delApplyDet(detArr);					
						if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
							Result res = aioApply.delApply(idDet);
							if(res.retCode==ErrorCanst.DEFAULT_SUCCESS){
								EchoMessage.success().add(getMessage(
						                request, "common.msg.delSuccess")).setBackUrl("/ApplyGoodsAction.do").setAlertRequest(request);							
							}
						}
					}
										
				}else{
					//只有采购订单，没有记录
					Result res = aioApply.delApply(idDet);
					if(res.retCode==ErrorCanst.DEFAULT_SUCCESS){
						EchoMessage.success().add(getMessage(
				                request, "common.msg.delSuccess")).setBackUrl("/ApplyGoodsAction.do").setAlertRequest(request);							
					}
				}				
			}
			return getForward(request, mapping, "message");	
		}else{
			Result detID = aioApply.getByApplyID(id);
			ArrayList detIDList = (ArrayList)detID.retVal;
			String[] detArr = new String[detIDList.size()];
			if(detIDList.size()>0){	
				boolean flay = true;
				for(int i=0;i<detIDList.size();i++){
					Object back_sign = ((Object[]) detIDList.get(i))[2];
					if(back_sign !=null && !"".equals(back_sign.toString()) && !"0.0".equals(back_sign.toString())){
						flay = false;
						EchoMessage.error().add("与归还记录关联，不能进行删除！").setBackUrl("/ApplyGoodsAction.do")
			                    .setAlertRequest(request);
					}																														
				}
				if(flay){
					for(int i=0;i<detIDList.size();i++){
						String d = ((Object[]) detIDList.get(i))[0].toString();	
						detArr[i] = d;
					}
					Result goodsRs = aioApply.deleteApply(id,detArr);
					if(goodsRs.retCode==ErrorCanst.DEFAULT_SUCCESS){
						EchoMessage.success().add(getMessage(
				                request, "common.msg.delSuccess")).setBackUrl("/ApplyGoodsAction.do").setAlertRequest(request);						
					}					
				}
			}else{
				//当只有单号没记录情况
				String[] idArr = new String[1];
				idArr[0] = id;
				Result rs = aioApply.delApply(idArr);
				if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(
			                request, "common.msg.delSuccess")).setBackUrl("/ApplyGoodsAction.do").setAlertRequest(request);						
				}
			}
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 
	 * 查询领用表
	 */
	protected ActionForward queryApply(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
			GoodsSearchForm lvForm = (GoodsSearchForm)form;	
			Result rs = aioApply.getByApply(lvForm);			
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				request.setAttribute("applyList", rs.retVal);
				request.setAttribute("pageBar", pageBar(rs, request));
			}			
			return getForward(request, mapping, "ApplyGoods");							
	}
	
	/**
	 * 明细查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward allApply(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		GoodsSearchForm lvForm = (GoodsSearchForm)form;
		String id = getParameter("applyId", request);
		if(!"".equals(id) && id!=null){
			Result Det = aioApply.getByApplyID(id);
			ArrayList DetList = (ArrayList) Det.retVal;
			String Det_id = "";
			if(DetList.size()>0){	
				String idlist = "";
				for(int j=0;j<DetList.size();j++){
					String detID = ((Object[]) DetList.get(j))[0].toString();
					idlist +=",'"+detID+"'";
					}
				Det_id = idlist.substring(1);
				Result rs = aioApply.getDetById(lvForm,Det_id);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					request.setAttribute("allList", rs.retVal);
					request.setAttribute("pageBar", pageBar(rs, request));
					return getForward(request, mapping, "allApply");
				}							
			}
		}
		return getForward(request, mapping, "allApply");
	}

	/**
	 * 添加领用用品
	 * 
	 */
	protected ActionForward addApply(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		GoodsSearchForm lvForm = (GoodsSearchForm)form;
		OAApplyGoodsBean applybean = new OAApplyGoodsBean();
		List<OAApplyGoodsDetBean> detList =new  ArrayList<OAApplyGoodsDetBean>();	
		String apply_title = getParameter("apply_title", request);
		String apply_qty = getParameter("apply_qty", request);
		String applyNO = getParameter("applyNO", request);
		String applyRole = getParameter("applyRole", request);
		String applyDate = getParameter("applyDate", request);
		String[] applyQty= getParameters("applyQty", request);
		String[] unit= getParameters("unit", request);
		String[] a_use= getParameters("use", request);
		String[] goodsName= getParameters("goodsName", request);
		String[] type= getParameters("type", request);
		//主表填装bean	
		applybean.setId(IDGenerater.getId());
		applybean.setApplyNO(applyNO);
		applybean.setApplyRole(applyRole);
		applybean.setApplyDate(applyDate);
		applybean.setApply_title(apply_title);
		applybean.setApply_qty(GlobalsTool.round(Float.parseFloat(apply_qty),GlobalsTool.getDigits()));
		//从表bean
		if(goodsName!=null && goodsName.length>0){			
			for (int i =0;i<goodsName.length;i++) {		
				if(!"".equals(applyQty[i] ) || !"".equals(unit[i] ) || !"".equals(a_use[i] ) || !"".equals(goodsName[i] ) ||
						!"".equals(type[i] ) ){
					OAApplyGoodsDetBean detbean = new OAApplyGoodsDetBean();
					detbean.setId(IDGenerater.getId());				
					detbean.setApplyQty(GlobalsTool.round(Float.parseFloat(applyQty[i]==""?"0":applyQty[i]),GlobalsTool.getDigits()));
					detbean.setA_use(a_use[i]);
					detbean.setGoodsName(goodsName[i]);
					detbean.setUnit(unit[i]);
					detbean.setType(type[i]);
					detbean.setApplyGoodsBean(applybean);
					detList.add(detbean);	
				}								
			}
		}		
		if(detList.size()>0){
			applybean.setApplyGoodsDetBean(detList);
			Result rs = aioApply.saveApply(applybean);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add(getMessage(
		                request, "common.msg.addSuccess")).setBackUrl("/ApplyGoodsAction.do").setAlertRequest(request);
		    	lvForm.reset(mapping, request);
				
			}	
		}else{
			return getForward(request, mapping, "to_addApply");
		}
									
		return getForward(request, mapping, "message");
	}

	/**
	 * 修改领用订单  --先全部删除再全部添加
	 */
	protected ActionForward updateApply(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		GoodsSearchForm lvForm = (GoodsSearchForm)form;
		
		OAApplyGoodsBean applybean = new OAApplyGoodsBean();
		List<OAApplyGoodsDetBean> detList =new  ArrayList<OAApplyGoodsDetBean>();	
		String apply_title = getParameter("apply_title", request);
		String apply_qty = getParameter("apply_qty", request);
		String applyNO = getParameter("applyNO", request);
		String applyRole = getParameter("applyRole", request);
		String applyDate = getParameter("applyDate", request);
		String[] applyQty= getParameters("applyQty", request);
		String[] unit= getParameters("unit", request);
		String[] a_use= getParameters("use", request);
		String[] goodsName= getParameters("goodsName", request);
		String[] type= getParameters("type", request);
	
		//获取原来明细表的id		
		Result applyrs = aioApply.getApplyID(applyNO);
		ArrayList asList = (ArrayList) applyrs.retVal;
		String applyrsID = ((Object[]) asList.get(0))[0].toString();//主表id
		Result Det = aioApply.getByApplyID(applyrsID);
		ArrayList DetList = (ArrayList) Det.retVal;	
		String[] detId = new String[DetList.size()];
		String[] backFlay = getParameters("back_sign", request);//归还标记						
		//获取原来的id串	
		if(DetList!=null && DetList.size()>0){			
			for(int j=0;j<DetList.size();j++){
				Object detID = ((Object[]) DetList.get(j))[0];					
				detId[j] = detID.toString();										
			}
		}			
		//applybean.setId(applyrsID);
		applybean.setApplyNO(applyNO);
		applybean.setApplyRole(applyRole);
		applybean.setApplyDate(applyDate);
		applybean.setApply_title(apply_title);
		applybean.setApply_qty(GlobalsTool.round(Float.parseFloat(apply_qty),GlobalsTool.getDigits()));		
		//从表bean
		if(goodsName!=null && goodsName.length>0){			
			for (int i =0;i<goodsName.length;i++) {
				if(!"".equals(applyQty[i] ) || !"".equals(unit[i] ) || !"".equals(a_use[i] ) || !"".equals(goodsName[i] ) ||
							!"".equals(type[i] ) ){
						OAApplyGoodsDetBean detbean = new OAApplyGoodsDetBean();
						detbean.setId(IDGenerater.getId());				
						detbean.setApplyQty(GlobalsTool.round(Float.parseFloat(applyQty[i]==""?"0":applyQty[i]),GlobalsTool.getDigits()));
						detbean.setA_use(a_use[i]);
						detbean.setGoodsName(goodsName[i]);
						detbean.setUnit(unit[i]);
						detbean.setType(type[i]);
						if(backFlay !=null){
							if(backFlay.length > i){											
								detbean.setBack_sign(GlobalsTool.round(Float.parseFloat(backFlay[i]),GlobalsTool.getDigits()));//填充归还标记		
							}else{
								detbean.setId(IDGenerater.getId());	
							}
						}
						detbean.setApplyGoodsBean(applybean);
						detList.add(detbean);
				}
			}
		}					
		applybean.setApplyGoodsDetBean(detList);
		Result rs = aioApply.delUpApply(applyrsID,detId,applybean);			 
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add("更新成功！").setBackUrl("/ApplyGoodsAction.do").setAlertRequest(request);
	    	lvForm.reset(mapping, request);
			
		}						
		return getForward(request, mapping, "message");							
	}
	/**
	 * 添加前操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginBean loginbean = getLoginBean(request);
		request.setAttribute("roleName", loginbean.getName());
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = fmt.format(new Date());
		request.setAttribute("dateTime", dateTime);
		return getForward(request, mapping, "to_addApply");
	}
	/**
	 * 修改前获取数据
	 */
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		GoodsSearchForm lvForm = (GoodsSearchForm)form;
		String id = getParameter("id", request);
		Result rs = aioApply.loadApply(id);
				
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("ApplyGoods", rs.retVal);
		}
		Result rss = aioApply.getByApplyID(id);
		ArrayList rsList = (ArrayList) rss.retVal;
		String new_id =""; 
		if(rsList!=null && rsList.size()>0){	
			for(int i=0;i<rsList.size();i++){
				Object d = ((Object[]) rsList.get(i))[0];
				new_id+=",'"+d.toString()+"'";			
			}
	
			String idList=new_id.substring(1);
			Result res = aioApply.getDetById(lvForm,idList);
			if(res.retCode==ErrorCanst.DEFAULT_SUCCESS){				
				request.setAttribute("UpdateList", res.retVal);
				request.setAttribute("pageBar", pageBar(res, request));					
			}
		}	
		return getForward(request, mapping, "to_updateApply");		
	}
}
