package com.koron.oa.office.goods.backed;

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
import com.koron.oa.office.goods.applyUse.ApplyUseMgt;
import com.koron.oa.office.goods.applyUse.OAApplyGoodsDetBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class BackedAction extends MgtBaseAction{

	BackedMgt aioBack = new BackedMgt();
	ApplyUseMgt aioApply = new ApplyUseMgt();
	
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
		MOperation mop = (MOperation) this.getLoginBean(request).getOperationMap().get("/BackGoodsAction.do");
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
			forward = addBack(mapping, form, request, response);
			break;	
		// 修改前准备
		case OperationConst.OP_UPDATE_PREPARE:		
				forward = updatePrepare(mapping, form, request, response);			
			break;
		// 修改
		case OperationConst.OP_UPDATE:		
				forward = updateBack(mapping, form, request, response);			
			break;
		// 根据条件查询
		case OperationConst.OP_QUERY:
			String type = request.getParameter("GoodsNO");
			if(null != type && !"".equals(type) && "condition".equals(type)){
            	forward = reqBack(mapping, form, request, response);
            }else if(null != type && !"".equals(type) && "queryAll".equals(type)){
            	forward = allBack(mapping, form, request, response);
            }
            else{
            	forward = queryBack(mapping, form, request, response);
            }
															
			break;
			// 查询领用人操作
		case OperationConst.OP_QUOTE:			
				forward = qyApply(mapping, form, request, response);
			break;
		case OperationConst.OP_DELETE:			
				forward = delBack(mapping, form, request, response);
			break;
		//默认
		default:	
				forward = queryBack(mapping, form, request, response); 
		}
		return forward;
	}
	/**
	 * 查询领用人
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward qyApply(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		GoodsSearchForm lvForm = (GoodsSearchForm)form;	
		String flag = getParameter("flag", request);
		String applyRole = getParameter("quRole", request);
		String applyGoods = getParameter("qugoods", request);
		String applyDate = getParameter("dateTime", request);
		Result rs = aioBack.getApplyDet(lvForm,applyRole,applyGoods,applyDate);		
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {				
			request.setAttribute("logList", rs.retVal);
			request.setAttribute("flag", flag);
			request.setAttribute("pageBar", pageBar(rs, request));
				
		}
		request.setAttribute("qugoods", applyGoods);
		request.setAttribute("quRole", applyRole);
		request.setAttribute("dateTime", applyDate);
		return getForward(request, mapping, "selectBack");
	}
	
	/**
	 * 根据条件查询
	 */
	private ActionForward reqBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		GoodsSearchForm lvForm = (GoodsSearchForm)form;	
		String id="";
		String back_title = getParameter("back_title", request);
		//先转换title为归还表id
		if(back_title!=null && !"".equals(back_title)){
			Result turnId = aioBack.turnId(back_title);
			ArrayList paramId = (ArrayList)turnId.retVal;	
			if(paramId.size()>0){
				for (int i = 0; i < paramId.size(); i++) {
					Object param = ((Object[])paramId.get(i))[0];
					id += ",'"+param.toString()+"'";
				}
			}
		}
						
		Result rs = aioBack.getBackBy(lvForm,id==""?id:id.substring(1));	
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){				
			request.setAttribute("backList", rs.retVal);
			request.setAttribute("pageBar", pageBar(rs, request));					
		}
		request.setAttribute("back_title", back_title);
		request.setAttribute("beginTime", lvForm.getBeginTime());
		request.setAttribute("endTime", lvForm.getEndTime());
		request.setAttribute("backedRole", lvForm.getBackedRole());
		return getForward(request, mapping, "BackedGoods");							
	}

	/**
	 * 删除归还表和明细表
	 */
	protected ActionForward delBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String[] id = getParameters("flay", request);
		
		String flay = getParameter("part", request);
		if(flay != null && !"".equals(flay) && flay.equals("ALLDEL")){
			String[] idDet = getParameters("keyId", request);
			String[] backId = getParameters("backId", request);			
			//更新归还表记录
			OABackedGoodsBean buybean = (OABackedGoodsBean)aioBack.loadBack(backId[0]).retVal;
			if(idDet.length>0){
				double qty = 0;
				for (int i = 0; i < idDet.length; i++) {
					OABackedGoodsDetBean beandet = (OABackedGoodsDetBean)aioBack.loadDet(idDet[i]).retVal;
					qty += beandet.getBackedQty();					
				}
				buybean.setBack_qty(buybean.getBack_qty()-qty);
			}
			Result uprs = aioBack.updateBack(buybean);
			Result rs = aioBack.deleteBack(null,idDet);
			
			if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add(getMessage(
		                request, "common.msg.delSuccess")).setBackUrl("/BackGoodsAction.do?operation=4&GoodsNO=queryAll&backId="+backId[0]).setAlertRequest(request);							
			}
			return getForward(request, mapping, "message");	
		}else if(flay != null && !"".equals(flay) && flay.equals("PARENT")){			
				String[] idDet = getParameters("keyId", request);
				Result detID = aioBack.getByBackArr(idDet);
				ArrayList detIDList = (ArrayList)detID.retVal;
				//获取明细id数组
				String[] detArr = new String[detIDList.size()];
				if(detIDList.size()>0){		
					for(int i=0;i<detIDList.size();i++){
						String detid = ((Object[]) detIDList.get(i))[0].toString();
						detArr[i] = detid;
					}
					
					Result res = aioBack.deleteBack(idDet,detArr);
					if(res.retCode==ErrorCanst.DEFAULT_SUCCESS){
						EchoMessage.success().add(getMessage(
				                request, "common.msg.delSuccess")).setBackUrl("/BackGoodsAction.do").setAlertRequest(request);							
					}
				}else{
					Result res = aioBack.delBack(idDet);
					if(res.retCode==ErrorCanst.DEFAULT_SUCCESS){
						EchoMessage.success().add(getMessage(
				                request, "common.msg.delSuccess")).setBackUrl("/BackGoodsAction.do").setAlertRequest(request);							
					}
				}				
			
			return getForward(request, mapping, "message");	
		}else{
			Result detID = aioBack.getByBackID(id[0]);
			ArrayList detIDList = (ArrayList)detID.retVal;
			String[] detArr = new String[detIDList.size()];
			if(detIDList.size()>0){	
				for(int i=0;i<detIDList.size();i++){
					String d = ((Object[]) detIDList.get(i))[0].toString();	
					detArr[i] = d;
				}			
				Result goodsRs = aioBack.deleteBack(id,detArr);
				if(goodsRs.retCode==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(
			                request, "common.msg.delSuccess")).setBackUrl("/BackGoodsAction.do").setAlertRequest(request);
					return getForward(request, mapping, "message");
				}else{
					EchoMessage.error().add(getMessage(
		                    request, "common.msg.delError")).setBackUrl("/BackGoodsAction.do")
		                    .setAlertRequest(request);		
					return getForward(request, mapping, "message");
				}
			}else{
				Result goodsRs = aioBack.deleteBack(id,detArr);
				if(goodsRs.retCode==ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(
			                request, "common.msg.delSuccess")).setBackUrl("/BackGoodsAction.do").setAlertRequest(request);					
				}
				return getForward(request, mapping, "message");
			}
		}	
	}
	
	/**
	 * 
	 * 查询归还表
	 */
	protected ActionForward queryBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
			GoodsSearchForm lvForm = (GoodsSearchForm)form;	
			Result rs = aioBack.getBack(lvForm);			
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				request.setAttribute("backList", rs.retVal);
				request.setAttribute("pageBar", pageBar(rs, request));
			}			
			return getForward(request, mapping, "BackedGoods");							
	}
	
	/**
	 * 明细查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward allBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		GoodsSearchForm lvForm = (GoodsSearchForm)form;
		String id = getParameter("backId", request);
		if(!"".equals(id) && id!=null){
			Result Det = aioBack.getByBackID(id);
			ArrayList DetList = (ArrayList) Det.retVal;
			String Det_id = "";
			if(DetList.size()>0){	
				String idlist = "";
				for(int j=0;j<DetList.size();j++){
					String detID = ((Object[]) DetList.get(j))[0].toString();
					idlist +=",'"+detID+"'";
					}
				Det_id = idlist.substring(1);
				Result rs = aioBack.getDetById(lvForm,Det_id);
				if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
					request.setAttribute("allList", rs.retVal);
					request.setAttribute("pageBar", pageBar(rs, request));
					
				}							
			}
		}
		return getForward(request, mapping, "allBack");
	}

	/**
	 * 添加归还用品
	 * 
	 */
	protected ActionForward addBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		GoodsSearchForm lvForm = (GoodsSearchForm)form;
		OABackedGoodsBean backbean = new OABackedGoodsBean();
		List<OABackedGoodsDetBean> detList =new  ArrayList<OABackedGoodsDetBean>();	
		String back_title = getParameter("back_title", request);
		String back_qty = getParameter("back_qty", request);
		String backNO = getParameter("backNO", request);
		String backedRole = getParameter("backedRole", request);
		String backDate = getParameter("backDate", request);
		String[] goodsId = getParameters("goodsId", request);//领用明细表id 
		String[] backedQty= getParameters("backedQty", request);
		String[] B_remark= getParameters("B_remark", request);
		
		//填装bean
	
		backbean.setId(IDGenerater.getId());
		backbean.setBackNO(backNO);
		backbean.setBackedRole(backedRole);
		backbean.setBackDate(backDate);
		backbean.setBack_title(back_title);
		backbean.setBack_qty(GlobalsTool.round(Float.parseFloat(back_qty),GlobalsTool.getDigits()));
		if(goodsId !=null && goodsId.length>0){			
				
		for (int i = 0; i <goodsId.length; i++) {								
			//封装det	
			OABackedGoodsDetBean detbean = new OABackedGoodsDetBean();
			detbean.setId(IDGenerater.getId());				
			detbean.setBackedQty(GlobalsTool.round(Float.parseFloat(backedQty[i]==""?"0":backedQty[i]),GlobalsTool.getDigits()));
			detbean.setB_remark(B_remark[i]);
			detbean.setBackId(goodsId[i]);
										
			detbean.setBackedGoodsBean(backbean);									
			detList.add(detbean);
		}
		backbean.setBackedGoodsDetBean(detList);
		Result rs = aioBack.saveBack(backbean,backedQty,goodsId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(getMessage(
	                request, "common.msg.addSuccess")).setBackUrl("/BackGoodsAction.do").setAlertRequest(request);
	    	lvForm = null;
			
		}				
			
	}else{		
		EchoMessage.success().add("不能添加为空的记录").setBackUrl("/BackGoodsAction.do").setAlertRequest(request);
	}
	return getForward(request, mapping, "message");
	
	}
	
	/**
	 * 修改归还订单  --先全部删除再全部添加，库存删除的时候减少删除的数量，添加的时候增加库存
	 */
	protected ActionForward updateBack(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		GoodsSearchForm lvForm = (GoodsSearchForm)form;
		
		OABackedGoodsBean backbean = new OABackedGoodsBean();
		List<OABackedGoodsDetBean> detList =new  ArrayList<OABackedGoodsDetBean>();	
		String back_title = getParameter("back_title", request);
		String old_qty = getParameter("old_qty", request);
		String back_qty = getParameter("back_qty", request);
		String backNO = getParameter("backNO", request);
		String backedRole = getParameter("backedRole", request);
		String backDate = getParameter("backDate", request);
		String[] goodsId = getParameters("goodsId", request);//领用明细表id 
		String[] backedQty= getParameters("backedQty", request);
		String[] B_remark= getParameters("B_remark", request);
		
		if(goodsId !=null && goodsId.length>0){
			//获取原来明细表的id	
			Result backrs = aioBack.getBackID(backNO);
			ArrayList asList = (ArrayList) backrs.retVal;
			String backrsID = ((Object[]) asList.get(0))[0].toString();
			Result Det = aioBack.getByBackID(backrsID);
			ArrayList DetList = (ArrayList) Det.retVal;
			String[] detId = new String[DetList.size()];
			if(DetList.size()>0){			
				for(int j=0;j<DetList.size();j++){
					String detID = ((Object[]) DetList.get(j))[0].toString();				
					detId[j] = detID;	
				}
			}		
			backbean.setId(IDGenerater.getId());
			backbean.setBackNO(backNO);
			backbean.setBackedRole(backedRole);
			backbean.setBackDate(backDate);
			backbean.setBack_title(back_title);
			backbean.setBack_qty(GlobalsTool.round(Float.parseFloat(back_qty),GlobalsTool.getDigits()));
			
			for (int i = 0; i <goodsId.length; i++) {
				//封装det			
				OAApplyGoodsDetBean applyDetbean = (OAApplyGoodsDetBean)aioApply.loadApplyDet(goodsId[i]).retVal;
				OABackedGoodsDetBean detbean = new OABackedGoodsDetBean();
				detbean.setId(IDGenerater.getId());				
				detbean.setBackedQty(GlobalsTool.round(Float.parseFloat(backedQty[i]==""?"0":backedQty[i]),GlobalsTool.getDigits()));
				detbean.setB_remark(B_remark[i]);
				detbean.setBackId(goodsId[i]);
							
				detbean.setBackedGoodsBean(backbean);									
				detList.add(detbean);										
			}
					
			backbean.setBackedGoodsDetBean(detList);
			Result rs = aioBack.delUpBack(backrsID,detId,backbean,backedQty,goodsId);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				EchoMessage.success().add("更新成功").setBackUrl("/BackGoodsAction.do").setAlertRequest(request);
				lvForm = null;			
			}
		}else{
			EchoMessage.success().add("不能添加为空的记录").setBackUrl("/BackGoodsAction.do")
	                .setNotAutoBack().setAlertRequest(request);
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
		DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
		LoginBean loginbean = getLoginBean(request);
		request.setAttribute("roleName", loginbean.getName());
		String dateTime = fmt.format(new Date());
		request.setAttribute("dateTime", dateTime);
		return getForward(request, mapping, "to_addBack");
	}
	/**
	 * 修改前获取数据
	 */
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		GoodsSearchForm lvForm = (GoodsSearchForm)form;
		String id = getParameter("id", request);
		Result rs = aioBack.loadBack(id);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("BackGoods", rs.retVal);
		}
		Result rss = aioBack.getByBackID(id);
		ArrayList rsList = (ArrayList) rss.retVal;
		String new_id =""; 
		if(rsList.size()>0){			
			for(int i=0;i<rsList.size();i++){
				Object d = ((Object[]) rsList.get(i))[0];
				new_id+=",'"+d.toString()+"'";
			}
			String idList=new_id.substring(1);
			Result res = aioBack.getDetById(lvForm,idList);
			if(res.retCode==ErrorCanst.DEFAULT_SUCCESS){				
				request.setAttribute("UpdateList", res.retVal);
				request.setAttribute("pageBar", pageBar(res, request));
				
			}
		}
		return getForward(request, mapping, "to_updateBack");	
	}
}
