package com.koron.oa.discuss;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.bean.OAItemsBean;
import com.koron.oa.bean.OAWorkLogBean;
import com.koron.oa.bean.OATaskBean;
import com.koron.oa.oaItems.OAItemsMgt;
import com.koron.oa.oaTask.OATaskMgt;
import com.koron.oa.oaWorkLog.OAWorkLogMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;
import com.menyi.web.util.OperationConst;

/**
 * 
 * <p>Title:通用评论功能</p> 
 * <p>Description: </p>
 *
 * @Date:2013/11/5
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 徐杰俊
 */
public class DiscussAction extends BaseAction{
	
	DiscussMgt mgt = new DiscussMgt();
	
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
		
		switch (operation) {
			case OperationConst.OP_ADD:	
				forward = add(mapping, form, request, response);//添加
				break;
			case OperationConst.OP_DELETE:	
				forward = delete(mapping, form, request, response);//添加
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
	
	private ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String keyId = getParameter("keyId",request);
		String tableName = getParameter("tableName",request);
		
		Result rs = mgt.delDiscuss(tableName, keyId);
		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg","success");
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}

	/**
	 * 
	 * @param userId 当前用户
	 * @param id 主键
	 * @param nowTime 当前时间
	 * @param tableName 表名
	 * @param f_ref 外键
	 * @param content 内容
	 * @param commentId 评论id
	 * @param replyId 回复id
	 * @param delFlag 是否删除标识，1：不可删除，0可以删除
	 * @return
	 */
	public static boolean add(String userId, String id, String nowTime, String tableName, String f_ref, String content, String commentId, String replyId,int delFlag) {
		OnlineUser user = OnlineUserInfo.getUser(userId);
		if (null == user) {
			return false;
		}
				
		String sql = "INSERT INTO "+tableName+"(id,f_ref,content,createBy,createTime,commentId,replyId,delFlag) VALUES(?,?,?,?,?,?,?,?)";
		
		if (null == id || "".equals(id)) {
			id = IDGenerater.getId(); 
		}
		if (null == nowTime || "".equals(nowTime)) {
			nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
		}
		ArrayList param = new ArrayList();
		param.add(id);
		param.add(f_ref);
		param.add(content);
		param.add(userId);
		param.add(nowTime);
		param.add(commentId);
		param.add(replyId);
		param.add(delFlag);
		
		Result rs = new DiscussMgt().operationSql(sql, param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			
			if("OAMeetingDebateLog".equals(tableName)){
				return true;
			}								
			//通知消息
			Result rest = new Result();
			String receiveIds = "";//存放收到通知的职员ids;
			String title = "";//标题
			String url ="";//连接地址
			String receiveContent = "";//通知内容
			
			if("OAItemsLog".equals(tableName)){
				//处理我的项目
				rest = new OAItemsMgt().loadOAItem(f_ref);
				OAItemsBean itemBean = (OAItemsBean)rest.retVal;
				if(itemBean.getExecutor()!=null && !"".equals(itemBean.getExecutor())){
					receiveIds = itemBean.getExecutor()+",";
				}
				
				if(commentId!=null && !"".equals(commentId)){
					title = user.getName()+"回复了【"+itemBean.getTitle()+"】"+GlobalsTool.getEmpFullNameByUserId(replyId)+"的动态";
					receiveIds +=replyId+",";
				}else{
					title = user.getName()+"发表了项目【"+itemBean.getTitle()+"】动态";
					
					//参与者IDs
					if(itemBean.getParticipant()!=null && !"".equals(itemBean.getParticipant())){
						for(String employeeId : itemBean.getParticipant().split(",")){
							receiveIds += employeeId +",";
						}
					}
				}
				url = "/OAItemsAction.do?operation=5&itemId="+itemBean.getId();
				receiveContent = "<a href=\"javascript:mdiwin('" + url + "','"+itemBean.getTitle()+"')\">"+title+"</a>";//内容
			}else if("OATaskLog".equals(tableName)){
				//处理我的任务
				rest = new OATaskMgt().loadTaskBean(f_ref);
				OATaskBean taskBean = (OATaskBean)rest.retVal;
				
				//负责人
				if(taskBean.getExecutor()!=null && !"".equals(taskBean.getExecutor()) && !userId.equals(taskBean.getExecutor())){
					receiveIds += taskBean.getExecutor()+",";
				}
				
				//创建人
				if(!taskBean.getCreateBy().equals(taskBean.getExecutor()) && !userId.equals(taskBean.getCreateBy())){
					receiveIds += taskBean.getCreateBy()+",";
				}
				
				//验收人
				if(taskBean.getSurveyor()!=null && !"".equals(taskBean.getSurveyor()) && !userId.equals(taskBean.getSurveyor())){
					receiveIds += taskBean.getSurveyor()+",";
				}
				
				if(commentId!=null && !"".equals(commentId)){
					title = user.getName()+"回复了任务【"+taskBean.getTitle()+"】"+GlobalsTool.getEmpFullNameByUserId(replyId)+"的评论";
					receiveIds +=replyId+",";
				}else{
					title = user.getName()+"评论了任务【"+taskBean.getTitle()+"】";
					
					//参与者IDs
					if(taskBean.getParticipant()!=null && !"".equals(taskBean.getParticipant())){
						for(String employeeId : taskBean.getParticipant().split(",")){
							receiveIds += employeeId +",";
						}
					}
				}
				url = "/OATaskAction.do?operation=5&taskId="+taskBean.getId();
				receiveContent = "<a href=\"javascript:mdiwin('" + url + "','任务:"+taskBean.getTitle()+"')\">"+title+"</a>";//内容
			}else if("OAWorkLogDiscuss".equals(tableName)){
				//处理我的任务
				rest = new OAWorkLogMgt().loadWorkLogBean(f_ref);
				OAWorkLogBean workLogBean = (OAWorkLogBean)rest.retVal;
				receiveIds+=workLogBean.getCreateBy()+",";
				
				if(commentId!=null && !"".equals(commentId)){
					title = user.getName()+"回复了"+workLogBean.getWorkLogDate()+"的日志";
					receiveIds +=replyId+",";
				}else{
					title = user.getName()+"评论了"+workLogBean.getWorkLogDate()+"的日志";
					
					/*参与者IDs
					if(workLogBean.getShareBy()!=null && !"".equals(workLogBean.getShareBy())){
						for(String employeeId : workLogBean.getShareBy().split(",")){
							receiveIds += employeeId +",";
						}
					}
					*/
				}
				url = "/OAWorkLogAction.do?operation=5&workLogId="+workLogBean.getId();
				receiveContent = "<a href=\"javascript:mdiwin('" + url + "','"+workLogBean.getWorkLogDate()+"的日志')\">"+title+"</a>";//内容
			}else if("CRMSaleFollowUpLog".equals(tableName)){
				
				//处理联系记录
				Object obj = new DiscussMgt().queryFollowUpCreateBy(f_ref);
				if(obj!=null){
					receiveIds = GlobalsTool.get(obj,0)+",";
				}
				
				String followUpTitle = String.valueOf(GlobalsTool.get(obj,1));
				
				if(commentId!=null && !"".equals(commentId)){
					title = user.getName()+"回复了【"+followUpTitle+"】"+GlobalsTool.getEmpFullNameByUserId(replyId)+"的评论";
					receiveIds +=replyId+",";
				}else{
					title = user.getName()+"评论了联系记录【"+followUpTitle+"】";
					
				}
				url = "/CRMBrotherAction.do?operation=5&tableName=CRMSaleFollowUp&keyId="+f_ref;
				receiveContent = "<a href=\"javascript:mdiwin('" + url + "','联系记录详情')\">"+title+"</a>";//内容
			}else if("CRMClientInfoDiscuss".equals(tableName)){
				
				//处理客户资料
				Object obj = new DiscussMgt().queryClientCreateBy(f_ref);
				String clientName = "";
				if(obj!=null){
					receiveIds = GlobalsTool.get(obj,0)+",";
					clientName = String.valueOf(GlobalsTool.get(obj,1));
				}
				
				if(commentId!=null && !"".equals(commentId)){
					title = user.getName()+"回复了【"+clientName+"】"+GlobalsTool.getEmpFullNameByUserId(replyId)+"的评论";
					receiveIds +=replyId+",";
				}else{
					title = user.getName()+"评论了客户【"+clientName+"】";
					
				}
				url = "/CRMClientAction.do?operation=5&type=detailNew&keyId="+f_ref ;
				receiveContent = "<a href=\"javascript:mdiwin('" + url + "','"+clientName+"')\">"+title+"</a>";//内容
			}
			
			receiveIds = filterReceiveIdByLoginId(receiveIds, userId);//过滤包含我的通知人
			
			new AdviceMgt().add(userId, title, receiveContent, receiveIds, f_ref, tableName);
			
			return true;
		}
		
		return false;
	}

	
	/**
	 * 添加评论或回复
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String tableName = getParameter("tableName",request);//表名
		String f_ref = getParameter("f_ref",request);//外键id
		String content = getParameter("content",request);//内容
		String commentId = getParameter("commentId",request);//评论ID
		String replyId = getParameter("replyId",request);//存放回复评论人的ID
		LoginBean loginBean = getLoginBean(request);
		
		String userId = loginBean.getId();		
		String id = IDGenerater.getId(); 
		String nowTime = BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
				
		if(add(userId, id, nowTime, tableName, f_ref, content, commentId, replyId,0)){
			
			//根据创建人查询是否有头像信息并封装信息
			String photo = GlobalsTool.checkEmployeePhoto("48", loginBean.getId());
			String msgInfo ="";//封装评论DIV，直接显示到对应位置
			if(commentId!=null && !"".equals(commentId)){
				//回复封装
				msgInfo = "<div class='in-block' id='"+id+"'><div class='in-block'><a class='in-phone' href='#'><img src='"+photo+"' /></a><div class='in-dv'>" +
						"<span><a class='in-name' href='#'>"+loginBean.getEmpFullName()+"</a>";
						
				if(replyId!=null && !"".equals(replyId)){
					msgInfo+="回复<a class='in-name' href='#'>"+GlobalsTool.getEmpFullNameByUserId(replyId)+"</a>";
				}
				
				msgInfo+=":"+content+"</span><a class='a-reply' onclick=\"addReply(this,'"+commentId+"','"+userId+"');\">回复</a><a class='a-del discussDel' delType='reply' keyId='"+id+"'>删除</a><em class='in-time'>"+nowTime+"</em></div></div>";
			}else{
				
				//评论封装
				msgInfo = "<div class='t-block' id='"+id+"'><a class='t-phone' href='#'><img src='"+photo+"'/></a><div class='t-dv'><a class='t-name' href='#'>"+loginBean.getEmpFullName()+"</a>" +
				" <span class='t-content'>"+content+"</span><a class='a-reply' onclick=\"addReply(this,'"+id+"','"+userId+"');\">回复</a><a class='a-del discussDel' delType='comment' keyId='"+id+"'>删除</a><em class='t-time'>"+nowTime+"</em><div class='in-reply-wrap'></div></div>";
			}
			request.setAttribute("msg",msgInfo);
			
		}else{
			request.setAttribute("msg","error");
		}
		
		return getForward(request, mapping, "blank");
	}



	private ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		String tableName = getParameter("tableName",request);//物理表名称
		String f_ref = getParameter("f_ref",request);//评论对应的外键id
		
		//先查询查所有评论
		String sql = "SELECT id,f_ref,content,createBy,createTime,commentId,replyId,delFlag FROM "+tableName+" WHERE isNull(commentId,'') = '' and f_ref = ? ORDER BY createTime DESC";
		
		ArrayList param = new ArrayList();
		param.add(f_ref);
		Result rs = mgt.publicSqlQuery(sql, param);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			ArrayList<Object> commentList = (ArrayList<Object>)rs.retVal;
			if(commentList!=null && commentList.size()>0){
				String commentIds = "";//存放所有评论ID,查询回复记录
				for(Object obj:commentList){
					commentIds += "'"+GlobalsTool.get(obj,0)+"',";
				}
				if(commentIds.endsWith(",")){
					commentIds = commentIds.substring(0,commentIds.length()-1);
					
				}
				
				if(!"".equals(commentIds)){
					rs = mgt.queryReplyByCommentIds(commentIds, tableName);
					request.setAttribute("replyMap",rs.retVal);
				}
				
			}
			request.setAttribute("commentList",commentList);
			request.setAttribute("f_ref",f_ref);
			request.setAttribute("tableName",tableName);
			request.setAttribute("loginBean",getLoginBean(request));
		}
		request.setAttribute("parentIframeName",getParameter("parentIframeName",request));//父类iframe名称，如果有名称，添加评论或回复时刷新父类页面
		
		new AdviceMgt().readOverByRelationId(f_ref, getLoginBean(request).getId());
		return getForward(request, mapping, "index");
	}



	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		return null;
	}

	/**
	 * 过滤通知消息包含当前登陆用户的ID
	 * @param receiveIds
	 * @param userId
	 * @return
	 */
	public static String filterReceiveIdByLoginId(String receiveIds,String userId){
		if(receiveIds==null || "".equals(receiveIds)){
			receiveIds = "";
		}else{
			//过滤通知当前用户的消息。
			receiveIds = ","+receiveIds;
			receiveIds = receiveIds.replaceAll(","+userId+",",",");
			receiveIds =receiveIds.substring(1);
		}
		
		return receiveIds;
	}
}
