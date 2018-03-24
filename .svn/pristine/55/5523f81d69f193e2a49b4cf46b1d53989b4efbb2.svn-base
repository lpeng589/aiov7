package com.koron.oa.controlPanel;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koron.oa.bbs.forum.OABBSForumMgt;
import com.koron.oa.bean.OABBSUserBean;
import com.menyi.aio.bean.EmployeeBean;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * <p>
 * Title: 控制面板
 * </p>
 * 
 * @Copyright: 科荣软件
 * 
 * @author 徐洁俊
 * 
 */
public class ControlPanelAction extends MgtBaseAction {
	
	
	private static Gson gson;
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	ControlPanelMgt controlPanelMgt = new ControlPanelMgt();		
	OABBSForumMgt mgt = new OABBSForumMgt() ;
	
	
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

		String noback = request.getParameter("noback");// 不能返回
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		String type = (String)request.getParameter("type");
		
		ActionForward forward = null;

		switch (operation) {

		case OperationConst.OP_ADD:
			forward = saveImg(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			if("updateUser".equals(type)){
				forward = updatePrepare(mapping, form, request, response);
			}else{
				forward = nickUpdatePrepare(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_UPDATE:
			if("updateUser".equals(type)){
				forward = update(mapping, form, request, response);
			}else{
				forward = nickNameUpdate(mapping, form, request, response);
			}
			break;	
		case OperationConst.OP_DELETE:
			this.deleteImg(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	
	protected ActionForward nickNameUpdate(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		LoginBean loginBean = this.getLoginBean(request);
		
//		//更新签名与昵称
//		String signature = getParameter("signature", request) ; 	/*用户签名*/
//		String nickName = getParameter("nickName", request) ; //昵称
//		Result result = new Result() ;
//		Result userBeanResult = controlPanelMgt.findOABBSUserByEmployeeId(loginBean.getId()) ;
//		OABBSUserBean userBean = ((ArrayList<OABBSUserBean>)userBeanResult.retVal).get(0);
//		
//		if(userBean!=null){
//			userBean.setNickName(nickName);
//			userBean.setSignature(signature) ;
//			result = mgt.updateUser(userBean) ;
//		}
//		
//		
//		//更新图片
//		Result  employeeResult = controlPanelMgt.loadEmployee(loginBean.getId());
//		EmployeeBean employee = (EmployeeBean)employeeResult.retVal;
//		String saveBigPicSrc = getParameter("saveBigPicSrc", request) ; 	/*大截图*/
//		String saveSamllSrc = getParameter("saveSamllSrc", request) ; 	/*小截图*/
//		String cutBPicName = saveBigPicSrc.substring(saveBigPicSrc.lastIndexOf("/")+1,saveBigPicSrc.length());
//		String cutSPicName = saveSamllSrc.substring(saveSamllSrc.lastIndexOf("/")+1,saveSamllSrc.length());
//		String employPhoto = cutBPicName+":"+cutSPicName;
//		if(!employPhoto.equals(employee.getPhoto())){
//			Result resultPhoto =  controlPanelMgt.updateEmployeeImage(getLoginBean(request).getId(),employPhoto );
//			if(resultPhoto.retCode == ErrorCanst.DEFAULT_SUCCESS){
//			//	loginBean = getLoginBean(request) ;
//				String[] oldCutPic = employee.getPhoto().split(":");
//				for(int i=0;i<oldCutPic.length;i++){
//					String savePicURL = BaseEnv.FILESERVERPATH + "/pic/tblEmployee/";
//					savePicURL += oldCutPic[i];
//					File oldFile = new File(savePicURL);
//					if(oldFile.exists()){
//						oldFile.delete();
//					}
//				}
//				loginBean.setPhoto(cutBPicName) ;
//				request.getSession().setAttribute("LoginBean", loginBean);
//			}
//		}
//		request.setAttribute("fileName", cutBPicName) ;
//		if(userBean !=null && result.retCode == ErrorCanst.DEFAULT_SUCCESS){
//			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setBackUrl(
//					"/ControlPanelAction.do?operation=7&isnickUpdate=1").setRequest(request);
//		}else{
//			EchoMessage.success().add(getMessage(request, "common.msg.updateErro")).setBackUrl(
//					"/ControlPanelAction.do?operation=7&isnickUpdate=1").setRequest(request);
//		}
		
		return getForward(request, mapping, "message") ;
	}
	
	
	@SuppressWarnings("unchecked")
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return getForward(request, mapping, "frame");
	}
	
	@SuppressWarnings("unchecked")
	protected ActionForward updatePrepare(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		LoginBean loginBean = this.getLoginBean(request);
		Result  employeeResult = controlPanelMgt.loadEmployee(loginBean.getId());
		EmployeeBean employee = (EmployeeBean)employeeResult.retVal;
		request.setAttribute("employee", employee);
		request.setAttribute("department", loginBean.getDepartmentName());
		return getForward(request, mapping, "updateEmployee");
	}

	@SuppressWarnings("unchecked")
	protected ActionForward nickUpdatePrepare(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		LoginBean loginBean = this.getLoginBean(request);
		Result result = new Result() ;
		Result userBeanResult = controlPanelMgt.findOABBSUserByEmployeeId(loginBean.getId()) ;
		OABBSUserBean userBean = ((ArrayList<OABBSUserBean>)userBeanResult.retVal).get(0);
		
		Result  employeeResult = controlPanelMgt.loadEmployee(loginBean.getId());
		EmployeeBean employee = (EmployeeBean)employeeResult.retVal;
		
		if(employee.getPhoto() !=null && !"".equals(employee.getPhoto())){
			String[] employPhoto = employee.getPhoto().split(":");
			String saveBigPicSrc = employPhoto[0]; 	/*大截图*/
			String saveSamllPicSrc = employPhoto[1]; 	/*小截图*/
			request.setAttribute("saveBigPicSrc", saveBigPicSrc);
			request.setAttribute("saveSamllPicSrc", saveSamllPicSrc);
		}
		
		request.setAttribute("userBean", userBean);
		return getForward(request, mapping, "nickname");
	}
	
	@SuppressWarnings("unchecked")
	protected ActionForward update(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		LoginBean loginBean = this.getLoginBean(request);
		Result  employeeResult = controlPanelMgt.loadEmployee(loginBean.getId());
		EmployeeBean employee = (EmployeeBean)employeeResult.retVal;
		ControlPanelForm controlPanelForm = (ControlPanelForm)form;
		read(controlPanelForm, employee);
		controlPanelMgt.update(employee);
		
		EchoMessage.success().add(
				getMessage(request, "common.msg.updateSuccess")).setBackUrl(
				"/ControlPanelAction.do?operation=7&type=updateUser").setAlertRequest(request);
		return getForward(request, mapping, "message"); 
	}
	
	@SuppressWarnings("unchecked")
	protected ActionForward saveImg(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		response.setContentType("text/html");   
		response.setCharacterEncoding("UTF-8");
		String realDir = request.getSession().getServletContext().getRealPath("");
		String cutPicSrc = request.getParameter("cutPicSrc");//获取截图的图片路径
		//获得相应的坐标值,用于截图
		Integer x1 = Integer.parseInt(request.getParameter("x1"));
		Integer y1 = Integer.parseInt(request.getParameter("y1"));
		Integer w = Integer.parseInt(request.getParameter("w"));
		Integer h = Integer.parseInt(request.getParameter("h"));
		
		if(cutPicSrc !=null && !"".equals(cutPicSrc)){
			
			String realFileName = cutPicSrc.substring(cutPicSrc.lastIndexOf("/")+1,cutPicSrc.length());
			String rePicURL = realDir +cutPicSrc;//原图路径
			String savePicURL = BaseEnv.FILESERVERPATH + "/pic/tblEmployee/" + realFileName;//截图路径
			SaveImage saveImage = new SaveImage();//此类可根据坐标截图
			if(saveImage.saveImage(new File(rePicURL),savePicURL,y1,x1,w,h)){
				File delDir = new File(rePicURL);
				if(delDir.exists()){
					delDir.delete();//保存截图后,将原图删除
				}
			}
		}
		
		return getForward(request, mapping, "frame"); 
	}
	
	protected ActionForward deleteImg(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		response.setContentType("text/html");   
		response.setCharacterEncoding("UTF-8");
		String realDir = request.getSession().getServletContext().getRealPath("");
		String cutPicSrc = request.getParameter("cutPicSrc");
		String rePicURL = realDir + "\\" +cutPicSrc;
		File delDir = new File(rePicURL);
		if(delDir.exists()){
			delDir.delete();
		}
		String json = gson.toJson("OK");
		request.setAttribute("msg", json);
		return getForward(request, mapping, "blank");
	
		
	}
	
	/**
	 * 重写权限判断的方法,因为此模块不需要权限判断,所以返回null
	 */
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
	    LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
		return null;
	}
	
}
