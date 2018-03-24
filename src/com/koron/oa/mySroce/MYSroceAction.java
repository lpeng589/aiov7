package com.koron.oa.mySroce;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.koron.oa.util.DateUtil;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;

/**
 * 
 * <p>Title:OA打分</p> 
 * <p>Description: </p>
 *
 * @Date:Jul 6, 2014
 * @Copyright: 深圳市科荣软件有限公司
 * @Author wyy
 */
public class MYSroceAction extends BaseAction {		
	MYSroceMgt mgt = new MYSroceMgt();
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
              HttpServletRequest request,HttpServletResponse response) throws Exception {
        
    	int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
	        //增加前执行方法
	        case OperationConst.OP_QUERY:	        	
	        	forward = queryAll(mapping, form, request, response);	        	           
	            break;
	        case OperationConst.OP_ADD_PREPARE:	        	
	        	forward = addPre(mapping, form, request, response);	        	           
	            break;	       
	        case OperationConst.OP_ADD:
	            forward = addSroce(mapping, form, request, response);
	            break;
	        case OperationConst.OP_QUOTE:
	        	//打分设置
	            forward = updateSetPre(mapping, form, request, response);
	            break;		      
	        default:
	        	forward = queryAll(mapping, form, request, response);
        }
        return forward;
    }


	private ActionForward updateSetPre(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		return getForward(request, mapping, "setSroce");
	}

	private ActionForward addPre(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String workLogId = getParameter("workLogId", request);//日志id
		String sroceManId = getParameter("sroceManId", request);//被打分人
		String createPlanDate = getParameter("createPlanDate", request);//日志创建的日期
		request.setAttribute("workLogId",workLogId);
		request.setAttribute("sroceManId",sroceManId);
		request.setAttribute("createPlanDate",createPlanDate);
		//查询是否被打分
		if(workLogId !=null && !"".equals(workLogId)){
			Result isNul = mgt.queryIsExsit(workLogId,getLoginBean(request).getId());
			ArrayList isNuls = (ArrayList)isNul.retVal;
			if(isNuls != null && isNuls.size()>0){
				request.setAttribute("existSroce", isNuls.get(0));
			}									
		}
		//显示方式
		Result ptn = mgt.queryPattern();
		request.setAttribute("pattern", ptn.retVal);
		ArrayList ptns = (ArrayList)ptn.retVal;
		if(ptns != null && ptns.size() >0){
			String setting = GlobalsTool.get(ptns.get(0),1).toString();
			String[] settings = setting.split("\\|");
			request.setAttribute("setEN", settings[0]);	
			request.setAttribute("setCN", settings[1]);
			request.setAttribute("ENorCN", GlobalsTool.get(ptns.get(0),0).toString());
			HashMap<String, String> setMaps = new HashMap<String, String>();
			for (int i = 0; i < settings[0].split(";").length; i++) {
				setMaps.put(settings[1].split(";")[i], settings[0].split(";")[i]);
			}
			request.setAttribute("setMaps", setMaps);
		}
		
		return getForward(request, mapping, "mySroce");
	}

	private ActionForward addSroce(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String ref_Id = getParameter("workLogId", request);//id
		String sroceManId = getParameter("sroceManId", request);//被打分人
		String createPlanDate = getParameter("createPlanDate", request);//日志创建的日期
		String sroceType = getParameter("sroceType", request);
		String comments = getParameter("comments", request);
		String sroces = getParameter("sroces", request);
		String userId = getLoginBean(request).getId();
		String pingfenId = getParameter("pingfenId", request);
		String msg = "";	
		if(ref_Id !=null && !"".equals(ref_Id) && sroceManId != null && !"".equals(sroceManId) && createPlanDate !=null && !"".equals(createPlanDate)){
		//先判断pingfenId 是否为空 不则删除再添加
			Result rs = mgt.addSroce(pingfenId,ref_Id,sroceManId,createPlanDate,sroceType,comments,sroces,userId,GlobalsTool.getDeptCodeByUserId(userId));
			if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
				msg ="3";
			}
		}				
		request.setAttribute("msg", msg);
		return null;
	}

	private ActionForward queryAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String queryType = getParameter("queryType", request);//部门，个人
		String personId = getParameter("personId", request);//查询别人
		String queryDate = getParameter("queryDate", request);//时间查询
		String startDate = "";
		String endDate = "";
		
		if(queryDate == null || "".equals(queryDate)){
			String[] dates = new DateUtil().getMonthByDate(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
			startDate = dates[0];
			endDate = dates[1];
		}else if("zhou".equals(queryDate)){
			String[] dates = new DateUtil().getMondayAndSunday(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
			startDate = dates[0];
			endDate = dates[1];
		}else if("month".equals(queryDate)){
			String[] dates = new DateUtil().getMonthByDate(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd));
			startDate = dates[0];
			endDate = dates[1];
		}
		if(personId == null || "".equals(personId)){
			personId = this.getLoginBean(request).getId();//默认登录人
		}
		StringBuffer condition = new StringBuffer();
		if("dept".equals(queryType)){
			condition.append("select count(type) as type,sum(sroce) as sroce from OASroceWorkDet  where  deptClassCode = '"+personId+"' group by type ");
		}else{
			//个人得分
			condition.append("select count(type) as type,sum(sroce) as sroce from OASroceWorkDet  where sroceManId ='"+personId+"' and sroceDate>='"+startDate+"' and sroceDate<='"+endDate+"' group by type");
		}						
		Result sroceRs = mgt.querySroce(condition.toString());
		
		return getForward(request, mapping, "blank");
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean login = getLoginBean(req);
		if(login == null){
			BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
		}
		//进行唯一用户验证，如果有生复登陆的，则后进入用户踢出前进入用户
        if (!OnlineUserInfo.checkUser(req)) {
            //需踢出
            EchoMessage.error().setAlertRequest(req);
            return getForward(req, mapping, "doubleOnline");
        }
		return null;
	}
    
}
