package com.koron.oa.oaItems;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.OAItemsBean;
import com.koron.oa.discuss.DiscussAction;
import com.koron.oa.util.AttentionMgt;
import com.menyi.aio.bean.AlertBean;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.TextBoxUtil;

/**
 * 
 * <p>Title:我的项目</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/2
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class OAItemsAction extends BaseAction{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();	
	OAItemsMgt mgt = new OAItemsMgt();
	AdviceMgt adviceMgt = new AdviceMgt();
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		String noback = request.getParameter("noback");// 不能返回
		String type = getParameter("type", request);
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		/*是否添加body2head.js*/		
		request.setAttribute("addTHhead", getParameter("addTHhead", request));
		switch (operation) {
			case OperationConst.OP_ADD:	
					if("addWarn".equals(type)){
						forward = addWran(mapping, form, request, response);
					}else{
						forward = addItem(mapping, form, request, response);
					}
				break;
			case OperationConst.OP_UPDATE:
				if("changeStatus".equals(type)){
					forward = changeStatus(mapping, form, request, response);//异步改变状态
				}else if("participantsInfo".equals(type)){
					forward = updateParticipantsInfoInfo(mapping, form, request, response);
				}else if("updateSingle".equals(type)){
					forward = updateSingleField(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_DETAIL:	
				forward = detailItem(mapping, form, request, response);
				break;
			case OperationConst.OP_DELETE:	
				if("affix".equals(type)){
					forward = delAffix(mapping, form, request, response);
				}else if("participant".equals(type)){
					forward = delParticipant(mapping, form, request, response);
				}else if("delAlert".equals(type)){
					forward = delAlert(mapping, form, request, response);
				}else{
					forward = delItem(mapping, form, request, response);
				}
				break;
			case OperationConst.OP_UPLOAD:	
				forward = uploadAffix(mapping, form, request, response);
				break;
			case OperationConst.OP_QUERY:	
					forward = query(mapping, form, request, response);
				break;
			default:
				forward = query(mapping, form, request, response);//首页查询
				break;
		}
		return forward;
	}

	/**
	 * 删除参与人
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delParticipant(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String employeeId = getParameter("employeeId",request);//职员ID
		String participants = getParameter("participants",request);//存放其余的参与者
		String itemId = getParameter("itemId",request);//项目ID
		
		Result rs = mgt.delParticipant(itemId, employeeId,participants);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 更新单个字段
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateSingleField(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		String fieldName = getParameter("fieldName",request);//字段名
		String fieldVal = getParameter("fieldVal",request);//字段值
		String itemId = getParameter("itemId",request);//项目ID
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.loadOAItem(itemId);
		OAItemsBean itemBean = (OAItemsBean)rs.retVal;
		
		String sql = "UPDATE OAItems SET "+fieldName+" = ?,lastUpdateBy=?,lastUpdateTime=? WHERE id = ?";
		ArrayList param = new ArrayList();
		param.add(fieldVal);
		param.add(loginBean.getId());
		param.add(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		param.add(itemId);
		
		rs = mgt.operationSql(sql, param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			if(!"schedule".equals(fieldName)){
				String nowDate = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
				String content = GlobalsTool.getEmpFullNameByUserId(loginBean.getId())+"把";
				if("beginTime".equals(fieldName)){
					content +="开始时间【"+itemBean.getBeginTime()+"】";
				}else if("endTime".equals(fieldName)){
					content +="截止时间【"+itemBean.getEndTime()+"】";
				}else if("remark".equals(fieldName)){
					content +="描述详情";
					
					if(itemBean.getRemark()==null || "".equals(itemBean.getRemark())){
						content +="【空】";
					}else{
						content +="【"+itemBean.getRemark()+"】";
					}
				}else{
					content +="标题【"+itemBean.getTitle()+"】";
				}
				content +="修改为【"+fieldVal+"】";
				new DiscussAction().add(loginBean.getId(), null, null, "OAItemsLog", itemId, content, null,null,1);
			}
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}

	/**
	 * 添加提醒
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addWran(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String keyId = getParameter("keyId",request);//单据ID
		String alterDay = getParameter("alterDay",request);//提醒日期
		int alterHour = getParameterInt("alterHour",request);//提醒小时
		int alterMinutes = getParameterInt("alterMinutes",request);//提醒分钟
		//String isSetTime = getParameter("isSetTime",request);//是否自定义时间
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.loadOAItem(keyId);
		OAItemsBean itemBean = (OAItemsBean)rs.retVal;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AlertBean alertBean = new AlertBean();        

		//先把已经提醒的删除
		String delSql = "DELETE FROM tblAlert WHERE relationId = ?";
		ArrayList param = new ArrayList();
		param.add(keyId);
		mgt.operationSql(delSql, param); 
		
		
		String nextAlertTime = alterDay+" "+alterHour+":"+alterMinutes+":00";
		
		String alertContent = "项目提醒，请查看";
		if(itemBean!=null && !"".equals(itemBean.getTitle())){
			alertContent = "项目【"+itemBean.getTitle()+"】提醒，请查看。";
		}
		
		alertBean.setId(IDGenerater.getId());
		alertBean.setAlertDate(alterDay);
		alertBean.setAlertHour(alterHour);        
		alertBean.setAlertMinute(alterMinutes);	
		alertBean.setNextAlertTime(nextAlertTime);
		alertBean.setIsLoop("no");	                   				            					
		alertBean.setLoopTime(0);            			                    			
		alertBean.setAlertType("4,");
		alertBean.setCreateBy(loginBean.getId());
		alertBean.setCreateTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
		alertBean.setRelationId(keyId);                			
		alertBean.setStatusId(0);
		alertBean.setLoopType("day");
		alertBean.setLoopTime(1);
		String popedomUserIds = loginBean.getId();                   		
		alertBean.setPopedomUserIds(popedomUserIds);
		alertBean.setAlertContent(alertContent);
		
		String url = "/OAItemsAction.do?operation=5&itemId="+keyId;
		String content = "<a href=\"javascript:mdiwin('" + url + "','"+itemBean.getTitle()+"')\">"+alertContent+"</a>";//内容
		alertBean.setAlertUrl(content);                   			
		
		rs = mgt.addWarn(alertBean,keyId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","<span title='提醒时间："+nextAlertTime+"'>已设提醒</span>");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}


	/**
	 * 获取参与者信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateParticipantsInfoInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String itemId = getParameter("itemId",request);
		String itemTitle = getParameter("itemTitle",request);//项目名称
		String employeeIds = getParameter("employeeIds",request);
		LoginBean loginBean = getLoginBean(request);
		
		Result rs = mgt.updateParticipants(employeeIds, loginBean, itemId);
		if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
			//封装用户ID查找头像信息
			String keyIds = "";
			for(String employeeId : employeeIds.split(",")){
				if(employeeId!=null && !"".equals(employeeId)){
					keyIds +="'"+employeeId+"',";
				}
			}
			if(keyIds.endsWith(",")){
				keyIds = keyIds.substring(0,keyIds.length()-1);
			}
			
			String sql = "SELECT id,empFullName,photo FROM tblEmployee WHERE id in("+keyIds+")";
			rs = mgt.publicSqlQuery(sql, new ArrayList());
			if(rs.retCode== ErrorCanst.DEFAULT_SUCCESS){
				String msg = "";//用于存放拼装的参与者信息
				ArrayList<Object> rsList = (ArrayList<Object>)rs.retVal;
				if(rsList!=null && rsList.size()>0){
					for(Object obj : rsList){
						String id = GlobalsTool.get(obj,0)+"";//职员ID
						if(id!=null && !"".equals(id)){
							String empFullName = GlobalsTool.get(obj,1)+"";//职员全称
							String photo = GlobalsTool.checkEmployeePhoto("48",String.valueOf(GlobalsTool.get(obj,0)));//头像信息
							msg += "<li empId='"+id+"' class='mesOnline'><a href='javascript:top.msgCommunicate(\""+id+"\",\""+empFullName+"\")' class='lf Image'><img src='"+photo+"' /></a> <i class='lf'><b>"+empFullName+"</b></i><b class='icons b-del-t'></b></li>";
						}
					}
					request.setAttribute("msg",msg);
					
					//向参与者发送通知消息
					String title = loginBean.getEmpFullName()+"邀请您加入项目【"+itemTitle+"】";
					String url = "/OAItemsAction.do?operation=5&itemId="+itemId;
					String content = "<a href=\"javascript:mdiwin('" + url + "','"+itemTitle+"')\">"+title+"</a>";//内容
					
					employeeIds = new DiscussAction().filterReceiveIdByLoginId(employeeIds, loginBean.getId());//过滤包含我的通知人
					adviceMgt.add(loginBean.getId(), title, content, employeeIds, itemId, "OAItemsParticipant");
				}
			}else{
				request.setAttribute("msg","error");
			}
		}else{
			request.setAttribute("msg","error");
		}
       
		return getForward(request, mapping, "blank");
	}

	/**
	 * 异步删除附件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	
	private ActionForward delAffix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String itemId = getParameter("itemId",request);//项目ID
		String fileName = getParameter("fileName",request);//删除的文件名
		String tempFile = getParameter("tempFile",request);//是否临时文件,true:是,false:否
		
		if (fileName != null && fileName.length() != 0) {
			
			String sql = "UPDATE oaitems SET  affix = replace(affix,'"+fileName+";','') where id = '"+itemId+"'";
			ArrayList param = new ArrayList();
			Result rs = mgt.operationSql(sql, param);
			
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				if("false".equals(tempFile)){
					//删除正式的
					FileHandler.delete("OAItems",FileHandler.TYPE_AFFIX, fileName);
				}else{
					//删除临时文件
					FileHandler.deleteTemp(fileName);
				}
				request.setAttribute("msg","success");
			}else{
				request.setAttribute("msg","error");
			}
			
		}
	          
		return getForward(request, mapping, "blank");
	}
		
	/**
	 * 附件上传
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward uploadAffix(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
			
		String itemId = getParameter("itemId",request);//项目ID
		String uploadStrs = getParameter("uploadStrs",request);//图片信息
		LoginBean loginBean = getLoginBean(request);
		String discussContent = GlobalsTool.getEmpFullNameByUserId(loginBean.getId())+"上传了附件:";//评论内容
		if(uploadStrs!=null && !"".equals(uploadStrs)){
			int handleType = FileHandler.TYPE_AFFIX;//附件
			
			for(String str :uploadStrs.split(";")){
				discussContent += "<a href='/ReadFile?fileName="+str+"&realName="+str+"&tempFile=false&type=AFFIX&tableName=OAItems'>"+str+"</a>、";	
				FileHandler.copy("OAItems", FileHandler.TYPE_AFFIX, str, str);
				FileHandler.deleteTemp(str);
			}
			
			if(discussContent.endsWith("、")){
				discussContent = discussContent.substring(0,discussContent.length()-1);
			}
		}
		
		Result rs = mgt.loadOAItem(itemId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			String newAffix = "";
			OAItemsBean itemBean = (OAItemsBean)rs.retVal;
			if(itemBean.getAffix() == null || "".equals(itemBean.getAffix())){
				newAffix = uploadStrs;
			}else{
				newAffix = itemBean.getAffix()+uploadStrs;
			}
			
			itemBean.setAffix(newAffix);
			rs = mgt.updateOAItem(itemBean);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				request.setAttribute("msg","success");
			}else{
				request.setAttribute("msg","error");
			}
			
			new DiscussAction().add(getLoginBean(request).getId(), null, null, "OAItemsLog", itemId, discussContent, null,null,1);
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 详情页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward detailItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String itemId = getParameter("itemId",request);//项目ID
		Result rs = mgt.loadOAItem(itemId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			OAItemsBean itemBean = (OAItemsBean)rs.retVal;
			
			if(itemBean.getNextAlertTime()!=null && !"".equals(itemBean.getNextAlertTime())){
				//判断提醒时间是否超过当前。是删除
				String alertTime = itemBean.getNextAlertTime();
				Date alertDate = new Date();
				try {
					alertDate = BaseDateFormat.parse(itemBean.getNextAlertTime(), BaseDateFormat.yyyyMMddHHmmss);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				if(alertDate.before(new Date())){
					rs = mgt.delAlert(itemBean.getId());
					if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
						itemBean.setNextAlertTime("");
					}
				}
			}
			
			//处理提醒时间
			if(itemBean.getNextAlertTime()!=null && !"".equals(itemBean.getNextAlertTime())){
				String alertDate = "";
				int alertHour = 0;
				int alterMinutes = 0;
				alertDate = itemBean.getNextAlertTime().substring(0,10);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Calendar ca = Calendar.getInstance();
				try {
					ca.setTime(sdf.parse(itemBean.getNextAlertTime()));
					alertHour = ca.get(Calendar.HOUR_OF_DAY);
					alterMinutes = ca.get(Calendar.MINUTE);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				request.setAttribute("alertDate",alertDate);
				request.setAttribute("alertHour",alertHour);
				request.setAttribute("alterMinutes",alterMinutes);
			}
			
			
			/*查看是否收藏过帖子*/
			Result isExist = new AttentionMgt().isAttention(getLoginBean(request).getId(), itemId, "OAItems");
			if(isExist.retCode == ErrorCanst.MULTI_VALUE_ERROR){
				request.setAttribute("attentionCard", "attentionCard");
			}
			
			//获取所有职员信息,用于textBox控件
			ArrayList<String[]> list = new TextBoxUtil().getUsersValues();
			request.setAttribute("textBoxValues",gson.toJson(list));
			
			
			request.setAttribute("clientName",mgt.findClientNameById(itemBean.getClientId()));
			request.setAttribute("itemBean",itemBean);
			request.setAttribute("loginBean", getLoginBean(request));
			return getForward(request, mapping, "detail");
		}else{
			request.setAttribute("noAlert", "true");
			
            EchoMessage.error().add("查询详情错误!").setAlertRequest(request);
            return getForward(request, mapping, "alert");
		}
		
	}

	/**
	 * 删除项目
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String itemId = getParameter("itemId",request);//项目ID
		Result rs = mgt.delItem(itemId);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{  
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 异步改变状态
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward changeStatus(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String id = getParameter("id",request);//项目ID
		String status = getParameter("status",request);//改变后状态值
		String feedbackContent = getParameter("feedbackContent",request);//反馈情况
		
		LoginBean loginBean = getLoginBean(request);
		Result rs = mgt.loadOAItem(id);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			OAItemsBean itemBean = (OAItemsBean)rs.retVal;
			itemBean.setStatus(status);
			if("2".equals(status)){
				//状态完成加上完成时间，
				itemBean.setFinishTime(BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss));
				itemBean.setSchedule("100");
			}else{
				itemBean.setFinishTime("");
				itemBean.setSchedule("0");
			}
			rs = mgt.updateOAItem(itemBean);
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				//评论操作
				String content = loginBean.getEmpFullName()+"在"+BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss)+"把项目标记为";
				if("2".equals(status)){
					content+="完成";
				}else{
					content+="重启";
				}
				content +="，情况描述:"+feedbackContent;
				new DiscussAction().add(loginBean.getId(), null, null, "OAItemsLog", itemBean.getId(), content, null,null,1);
				
				request.setAttribute("msg",status);
			}else{
				request.setAttribute("msg","error");
			}
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	/**
	 * 查询方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		OAItemsSearchForm oaItemsform = (OAItemsSearchForm) form;
		LoginBean loginBean = getLoginBean(request);
		request.setAttribute("loginBean", loginBean);
		StringBuilder condition = new StringBuilder();
		String isTHItems = getParameter("isTHItems",request);//是否是天华主页的
		
		int pageSize = getParameterInt("pageSize", request);
		int pageNo = getParameterInt("pageNo", request);
        if (pageNo == 0 ) {
        	pageNo = 1;
        }
        if (pageSize == 0) {
        	pageSize = 4;
        }
        
        String userId = loginBean.getId();

		//tab头部查询
        String tabSelectName = oaItemsform.getTabSelectName();
		if(tabSelectName==null || "".equals(tabSelectName)){
			//默认取我负责的
			condition.append(" and executor= '").append(userId).append("' ");
			oaItemsform.setTabSelectName("executor");
		}else{
			condition.append(" and ");	
			if("participant".equals(tabSelectName)){
				condition.append(" ','+ ").append(tabSelectName).append(" like '%,").append(userId).append(",%' ");
			}else{
				condition.append(tabSelectName).append(" = '").append(userId).append("' ");
			}
		}
        
		
		//若有模糊查询就不管状态了
		if(oaItemsform.getHasSearchCondition()==null || "".equals(oaItemsform.getHasSearchCondition())){
			//状态查询
			if(oaItemsform.getSearchStatus()==null || "".equals(oaItemsform.getSearchStatus()) || "1".equals(oaItemsform.getSearchStatus())){
				//默认与状态==1  默认查询执行中
				condition.append(" and status = '1' ");
				oaItemsform.setSearchStatus("1");
			}else if("all".equals(oaItemsform.getSearchStatus())){
				//全部
			}else{
				//启动或完成
				condition.append(" and status = '").append(oaItemsform.getSearchStatus()).append("' ");
			}
		}else{
			oaItemsform.setSearchStatus("");
		}
		
		
		//编号
		if(oaItemsform.getSearchItemOrder()!=null && !"".equals(oaItemsform.getSearchItemOrder())){
			condition.append(" and itemOrder like '%").append(oaItemsform.getSearchItemOrder()).append("%' ");
		}
		
		
		//关键字
		if(oaItemsform.getSearchKeyWord()!=null && !"".equals(oaItemsform.getSearchKeyWord())){
			condition.append(" and (title like '%").append(oaItemsform.getSearchKeyWord()).append("%' or remark like '%").append(oaItemsform.getSearchKeyWord()).append("%') ");
		}
		
		//时间查询
		String beginStartTime = oaItemsform.getSearchBeginStartTime();
		String beginOverTime = oaItemsform.getSearchBeginOverTime();
		String endStartTime = oaItemsform.getSearchEndStartTime();
		String endOverTime = oaItemsform.getSearchEndOverTime();
		
		//处理开始时间
		if(beginStartTime!=null && !"".equals(beginStartTime)){
			condition.append(" and beginTime >= '").append(beginStartTime).append("' ");
		}
		
		if(beginOverTime!=null && !"".equals(beginOverTime)){
			condition.append(" and beginTime <= '").append(beginOverTime).append("' ");
		}
		
		//处理结束时间
		if(endStartTime!=null && !"".equals(endStartTime)){
			condition.append(" and endTime >= '").append(endStartTime).append("' ");
		}
		
		if(endOverTime!=null && !"".equals(endOverTime)){
			condition.append(" and endTime <= '").append(endOverTime).append("' ");
		}
		
		//客户
		if(oaItemsform.getSearchClientId()!=null && !"".equals(oaItemsform.getSearchClientId())){
			condition.append(" and clientId = '").append(oaItemsform.getSearchClientId()).append("' ");
			request.setAttribute("clientName", mgt.findClientNameById(oaItemsform.getSearchClientId()));
		}
		
		Result rs = mgt.itemsQuery(condition.toString(),pageNo,pageSize);
		
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("itemsList", rs.retVal);
			request.setAttribute("pageBar",pageBar2(rs, request)) ;
			request.setAttribute("oaItemsform",oaItemsform);
			if("true".equals(isTHItems)){
				return getForward(request, mapping, "oaitems");
			}
			return getForward(request, mapping, "query");
		}else{
			request.setAttribute("noAlert", "true");
            EchoMessage.error().add(getMessage(request, "sms.query.error")).setAlertRequest(request);
            return getForward(request, mapping, "alert");
		}
	}
	
	/**
	 * 添加项目
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		OAItemsBean itemBean = new OAItemsBean();
		String beginTime = getParameter("beginTime",request);//开始时间
		String endTime = getParameter("endTime",request);//结束时间
		LoginBean loginBean = getLoginBean(request);
		
		
		String title = request.getParameter("title");//标题
		String remark = request.getParameter("remark");//详情描述
		String schedule = request.getParameter("schedule");//项目进度
		String clientId = request.getParameter("clientId");//客户id
		String nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		String userId = loginBean.getId();
		String participant = userId+",";//默认参与者
		
		itemBean.setId(IDGenerater.getId());
		itemBean.setTitle(title);
		itemBean.setRemark(remark);
		itemBean.setBeginTime(beginTime);
		itemBean.setEndTime(endTime);
		itemBean.setExecutor(userId);//默认负责人是创建人
		itemBean.setStatus("1");//默认状态为执行中
		itemBean.setCreateBy(userId);
		itemBean.setCreateTime(nowTime);
		itemBean.setLastUpdateBy(userId);
		itemBean.setLastUpdateTime(nowTime);
		itemBean.setSchedule(schedule);
		itemBean.setClientId(clientId);
		//itemBean.setParticipant(participant);
		
		Result rs = mgt.addOAItem(itemBean);
		
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		if(getLoginBean(req)==null){
			 return getForward(req, mapping, "indexPage");//提出	
		}
		
		return null;
	}
	
	/**
	 * 删除提醒
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward delAlert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String keyId = getParameter("keyId",request);
		
		Result rs = mgt.delAlert(keyId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		return getForward(request, mapping, "blank");
	}
	
	
}
