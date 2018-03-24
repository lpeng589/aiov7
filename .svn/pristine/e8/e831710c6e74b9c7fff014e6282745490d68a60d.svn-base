package com.koron.oa.netDisk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.albumTree.TreeVo;
import com.koron.oa.bean.DirectorySetting;
import com.koron.oa.bean.OAOrdainGroupBean;
import com.koron.oa.publicMsg.newsInfo.OANewsMgt;
import com.koron.oa.util.FileOperateUtil;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * <p>
 * Title: 网络硬盘
 * </p>
 * 
 * @Copyright: 科荣软件
 * 
 * @author 毛晶
 * 
 */
public class NetDiskAction extends MgtBaseAction {

	NetDiskMgt ndMgt = new NetDiskMgt();

	private static Gson gson;// mj
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
	private static final Integer MIN_PAGE_RECORDS = 20; //每页最小显示多少条数据

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
		case OperationConst.OP_ADD:
			String typeAdd = request.getParameter("addType");
			if ("whetherExistName".equals(typeAdd)) {
				forward = ifExistName(mapping, form, request, response);
			} else if ("addNode".equals(typeAdd)) {
				forward = addNode(mapping, form, request, response);
			}
			break;
		// 上传页面
		case OperationConst.OP_IMPORT_PREPARE:
			forward = uploadPrepare(mapping, form, request, response);
			break;
		// 上传完成后
		case OperationConst.OP_UPDATE_PREPARE:
			forward = pre_updatePhoto(mapping, form, request, response);
			break;
		// 修改
		case OperationConst.OP_UPDATE:
			String updateType = getParameter("updateType", request);
			if ("updateNode".equals(updateType)) {
				forward = updateNode(mapping, form, request, response);
			}
			break;
		// 查询
		case OperationConst.OP_QUERY:
			String requestType = request.getParameter("requestType");
			if ("ajax".equals(requestType)) {
				forward = showFiles(mapping, form, request, response);
			} else if ("toChangePhos".equals(requestType)) {
				forward = showPhosAjax(mapping, form, request, response);
			} else {
				forward = query(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_DELETE:
			forward = delFile(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * 显示该路径里面的所有的文件 mj
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	protected ActionForward showFiles(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			String pId = getParameter("id", request);
			String dateId=getParameter("dateId",request);
			request.setAttribute("index",  getParameter("index",request));
			if (StringUtils.isBlank(pId)) {
				return getForward(request, mapping, "filesList");// 跳转到当前相册页面
			}
			NetDiskForm myForm = (NetDiskForm) form;
			int key = myForm.getKey();
			int sortType = myForm.getSortType();

			int currentPage = myForm.getPageNo();
			int pageRecords = myForm.getPageSize();
			if (key == 0 && sortType == 0) {// 通过连接方式发送的请求ajax
				key = getParameterInt("keyURL", request);
				sortType = getParameterInt("sortTypeURL", request);
				currentPage = getParameterInt("pageNoURL", request);
				pageRecords = getParameterInt("pageSizeURL", request);
			}
			String parPath = getParameter("parPath", request);
			if (StringUtils.isBlank(parPath)) {
				pId = GlobalsTool.toChinseChar(pId);
			} else {
				pId = parPath;
				pId = pId.replaceAll("\\\\\\\\", "\\\\");
			}
			Long begTime = System.currentTimeMillis();
			File[] files = FileOperateUtil.getFilesByPath(pId, "file", true);// 第二个参数无效 不需要过滤
																				
			// 获得文件夹下的所有文件夹
			if (key != 0) {
				// 非第一次 第一次 名称排序
				files = ndMgt.sortCollection(files, key, sortType);
			} else {
				pageRecords = this.MIN_PAGE_RECORDS;
			}
			Long endTime = System.currentTimeMillis();
			System.out.println("排序耗时：" + (endTime - begTime));

			Long begPage = System.currentTimeMillis();
			List<File> list = new ArrayList<File>();
			for (File f : files) {
				if (f != null) {
					list.add(f);
				}
			}
			pageRecords = pageRecords == 15 ? this.MIN_PAGE_RECORDS:pageRecords;
			Object[] obj = ndMgt.pageList(list, currentPage, pageRecords,this.MIN_PAGE_RECORDS);
			list = (List<File>) obj[0];
			PageUtil page = (PageUtil) obj[1];
			Long endPage = System.currentTimeMillis();
			System.out.println("分页耗时：" + (endPage - begPage));
			List<TreeVo> phos = ndMgt.getFilesByPath(list);
			request.setAttribute("key", key == 0 ? 3 : key);
			request.setAttribute("sortType", sortType == 0 ? 1 : sortType);
			Result rs = new Result();
			rs.setPageNo(page.getCurrentPage());
			rs.setPageSize(page.getPageRecords());
			rs.setRealTotal(page.getTotalRecord());
			rs.setTotalPage(page.getTotalPage());

			request.setAttribute("pageBar", pageBars(rs, request));

			request.setAttribute("page", page);
			request.setAttribute("files", phos);
			request.setAttribute("parPath", pId);

			pId = pId.replaceAll("\\\\", "\\\\\\\\");
			request.setAttribute("pId", pId);

			// 对pId进行处理 不能显示根目录的原始路径
			LoginBean loginBean = this.getLoginBean(request);
			String curNodeName = getParameter("curNodeName", request);
			
			if (StringUtils.isNotBlank(curNodeName)) {
				curNodeName = GlobalsTool.toChinseChar(curNodeName);
			}
			String viewPath = ndMgt.replaceStr(pId, loginBean,curNodeName);
			int i = viewPath.lastIndexOf("\\\\");
			String view = viewPath.substring(i + 2);

			if (StringUtils.isBlank(view)) {
				viewPath = viewPath.replace("\\", "");
			}
			request.setAttribute("viewPath", viewPath);
			MOperation mop = (MOperation) loginBean.getOperationMap().get(
					"/NetDiskQueryAction.do");
			request.setAttribute("add", mop.add()); // 增加权限
			request.setAttribute("del", mop.delete()); // 删权限
			request.setAttribute("upd", mop.update()); // 改权限

			//String  pathId=ndMgt.replaceStrAll(pId, loginBean);

			 
			String  userId=loginBean.getId();
			String 	deptId=loginBean.getDepartCode();
			String  groupId=loginBean.getGroupId();
			String cheshi=getParameter("cheshi", request);
			String dateId2=dateId;
			List<DirectorySetting> targetNetDisk=ndMgt.getDepartment(dateId);
			int targe=targetNetDisk.size();
			String  downLoadUserIds=null;
			String 	downLoadDepteIds=null;
			String  downLoadgroupIds=null;
			if(targetNetDisk!=null && targetNetDisk.size()>0 ){
				downLoadUserIds=targetNetDisk.get(0).getDownLoadUserId();
				downLoadDepteIds=targetNetDisk.get(0).getDownLoadDeptOfClassCode();
			    downLoadgroupIds=targetNetDisk.get(0).getDownLoadEmpGroup();
			}
			/*判断开始:判断当前登录用户是否有下载附件的权限:onDown为True则有权限*/
			boolean onDown = false;
			
			if(downLoadUserIds==null){
				downLoadUserIds="";
			}
			if(downLoadDepteIds==null){
				downLoadDepteIds="";
			}
			if(downLoadgroupIds==null){
				downLoadgroupIds="";
			}
			if("1".equals(userId))  //如果是管理员，则有权限
				onDown=true;
			
			/*如果没有选中下载授权的部门和个人，则默认可以看到记录信息的用户都拥有下载附件的权限*/
			if("".equals(downLoadUserIds) && "".equals(downLoadDepteIds) && "".equals(downLoadgroupIds))
				onDown=true;
			else{
				String[] downDepts = downLoadDepteIds.split(",");
				String[] downUsers = downLoadUserIds.split(",");
				String[] downGroup = downLoadgroupIds.split(",");
				for(String dept : downDepts){
					if(deptId.startsWith(dept) && !"".equals(dept)){
						onDown =true;
						break;
					}
				}
				if(onDown!=true){
					for(String user : downUsers){
						if(userId.equals(user) && !"".equals(user)){
							onDown =true;
							break;
						}
					}
				}
				if(onDown!=true){
					for(String group:downGroup ){
						if(groupId.equals(group) && !"".equals(group)){
							onDown =true;
							break;
						}
					}
				}
			}
			
			
			/*判断结束*/
			request.setAttribute("onDown", onDown);
			request.setAttribute("dateId", dateId);
			
			 
	 

			MOperation mopSetting = (MOperation) loginBean.getOperationMap().get(
			"/DirectorySettingNetDiskQueryAction.do");
			if (mopSetting != null) {
				request.setAttribute("query", mopSetting.query());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
					.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "filesList");// 跳转到当前相册页面

	}

	/**
	 * 硬盘首页
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
	@SuppressWarnings("unchecked")
	protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginBean loginBean = getLoginBean(request);
		MOperation mop = (MOperation) loginBean.getOperationMap().get(
				"/NetDiskQueryAction.do");
		request.setAttribute("add", mop.add()); // 增加权限
		request.setAttribute("del", mop.delete()); // 删权限
		request.setAttribute("upd", mop.update()); // 改权限
		
		MOperation mopSetNetDisk = (MOperation) loginBean.getOperationMap().get(
		"/DirectorySettingNetDiskQueryAction.do");
		request.setAttribute("querySetDisk", mopSetNetDisk == null ? false : mopSetNetDisk.query()); // 增加权限
		return getForward(request, mapping, "netDiskIndex");
	}

	/**
	 * 上传图片前 mj
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
	@SuppressWarnings("unchecked")
	protected ActionForward uploadPrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 将路径共享给上传页面
		String path = getParameter("path", request);
		if (StringUtils.isNotBlank(path)) {
			path = GlobalsTool.toChinseChar(path);
		}
		path = path.replaceAll("\\\\", "\\\\\\\\");
		request.setAttribute("path", path);
		return getForward(request, mapping, "uploadTreePhoto");
	}

	/**
	 * 上传图片后 mj
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
	@SuppressWarnings("unchecked")
	protected ActionForward pre_updatePhoto(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String path = getParameter("path", request);
		String picFiles = getParameter("picFiles", request);// 上传的照片

		picFiles = picFiles == null ? "" : picFiles;
		// 需删除的附件
		String delFiles = request.getParameter("delPicFiles");
		// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0 && picFiles.indexOf(del) == -1) {
				File aFile = new File(path + "/" + del);
				aFile.delete();

			}
		}

		return getForward(request, mapping, "blank");
	}

	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {

		LoginBean loginBean = getLoginBean(req);
		if (loginBean == null) {
			BaseEnv.log
					.debug("OANewInfoAction.doAuth() ---------- loginBean is null");
			return getForward(req, mapping, "indexPage");
		}
		// 进行唯一用户验证，如果有重复登陆的，则后进入用户踢出前进入用户
		if (!OnlineUserInfo.checkUser(req)) {
			// 需踢出
			EchoMessage.error().setAlertRequest(req);
			return getForward(req, mapping, "doubleOnline");
		}

		int operation = getOperation(req);
		MOperation mop = (MOperation) loginBean.getOperationMap().get(
				"/NetDiskQueryAction.do");
		if (operation == OperationConst.OP_IMPORT_PREPARE
				|| operation == OperationConst.OP_UPDATE_PREPARE) {// 上传
			if (mop.add) {
				return null;
			}
		}
		return super.doAuth(req, mapping);
	}

	/**
	 * ajax跟新jsp消息
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
	@SuppressWarnings("unchecked")
	protected ActionForward ifExistName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String name = request.getParameter("albumName"); // 相册名称
		if (StringUtils.isNotBlank(name)) {
			name = GlobalsTool.toChinseChar(name);
		}
		int count = ndMgt.getQueryCount("tblAlbum", "name", name);
		String json = gson.toJson(count);
		request.setAttribute("msg", json);
		return getForward(request, mapping, "blank");
	}

	/**
	 * 显示该相册里面的所有的照片 mj
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
	@SuppressWarnings("unchecked")
	protected ActionForward showPhosAjax(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String pId = request.getParameter("id");
			pId = GlobalsTool.toChinseChar(pId);

			if (pId == null) {
				EchoMessage.error().add(getMessage(request, "news.not.find"))
						.setNotAutoBack().setAlertRequest(request);
				return getForward(request, mapping, "message");
			}

			int key = getParameterInt("keyURL", request);
			int sortType = getParameterInt("sortTypeURL", request);
			int currentPage = getParameterInt("pageNoURL", request);
			int pageRecords = getParameterInt("pageSizeURL", request);

			File[] files = FileOperateUtil.getFilesByPath(pId, "file", true);

			files = ndMgt.sortCollection(files, key, sortType);
			List<File> list = new ArrayList<File>();
			for (File f : files) {
				if (f != null) {
					list.add(f);
				}

			}
			List<TreeVo> phos = ndMgt.getFilesByPath((List<File>) ndMgt
					.pageList(list, currentPage, pageRecords,this.MIN_PAGE_RECORDS)[0]);

			String json = gson.toJson(phos);
			request.setAttribute("msg", json);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return getForward(request, mapping, "blank");

	}

	/**
	 * 添加新节点（目录）
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
	@SuppressWarnings("unchecked")
	protected ActionForward addNode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String path = getParameter("path", request); // 需要添加的目录
		String result = null;
		if (StringUtils.isNotBlank(path)) {
			path = GlobalsTool.toChinseChar(path);
			File file = new File(path);
			if (!file.exists()) {
				if (file.mkdir()) {
					result = "true";
				} else {
					result = "false";
				}
			} else {
				result = "false";
			}

		} else {
			result = "false";
		}

		request.setAttribute("msg", result);
		return getForward(request, mapping, "blank");
	}

	/**
	 * 修改新节点（目录）
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
	@SuppressWarnings("unchecked")
	protected ActionForward updateNode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String oldName = getParameter("oldName", request); // 需要添加的目录
		String newName = getParameter("newName", request);

		String pId = getParameter("pId", request);
		String result = null;
		if (StringUtils.isNotBlank(pId)) {
			File file = null;
			pId = GlobalsTool.toChinseChar(pId);
			oldName = GlobalsTool.toChinseChar(oldName);
			newName = GlobalsTool.toChinseChar(newName);
			file = new File(pId);
			String parPath = pId.substring(0, pId.lastIndexOf("\\"));
			boolean bool = file.renameTo(new File(parPath + "\\" + newName));
			if (bool) {
				result = "true";
			} else {
				result = "false";
			}
		} else {
			result = "false";
		}

		request.setAttribute("msg", result);
		return getForward(request, mapping, "blank");
	}

	/**
	 * 删除文件
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
	protected ActionForward delFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String fileName = request.getParameter("fileName");
		String path = request.getParameter("path");
		File file = new File(path + "/" + fileName);
		if (file.exists()) {
			file.delete();
		}
		request.setAttribute("msg", "true");
		return getForward(request, mapping, "blank");
	}

}
