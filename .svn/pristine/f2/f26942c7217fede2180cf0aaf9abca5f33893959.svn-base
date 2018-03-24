package com.koron.oa.directorySeting;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.koron.oa.bean.Department;
import com.koron.oa.bean.DirectorySetting;
import com.koron.oa.bean.Employee;
import com.koron.oa.publicMsg.advice.OAAdviceMgt;
import com.koron.oa.util.EmployeeMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * 
 * <p>
 * Title: 目录设置
 * </p>
 * @Copyright: 科荣软件
 * @author 毛晶
 * 
 */
public class DirectorySettingAction extends MgtBaseAction {

	private DirectorySettingMgt drtMgt = new DirectorySettingMgt();
	private OAAdviceMgt adviceMgt = new OAAdviceMgt();
	private EmployeeMgt empMgt = new EmployeeMgt(); 

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
	 * @todo 
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD:
			forward = addDirectory(mapping, form, request, response);
			break;
		case OperationConst.OP_ADD_PREPARE:
			forward = addDirPre(mapping, form, request, response);
			break;
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updateDirectoryPre(mapping, form, request, response);
			break;

		case OperationConst.OP_UPDATE:
			forward = updateDirectory(mapping, form, request, response);
			break;

		case OperationConst.OP_QUERY:
			String type = request.getParameter("type");
			if (StringUtils.equals("whetherExistPath", type)) {
				forward = whetherExistPath(mapping, form, request, response);
			} else if (StringUtils.equals("whetherExistName", type)) {
				forward = whetherExistName(mapping, form, request, response);
			} else {
				forward = query(mapping, form, request, response);
			}
			break;

		case OperationConst.OP_DELETE:
			String delType = request.getParameter("type");
			if ("dels".equals(delType)) {
				forward = delFiles(mapping, form, request, response);
			} else {
				forward = delById(mapping, form, request, response);
			}
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * 批量删除
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
	protected ActionForward delFiles(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String[] dirs = request.getParameterValues("dir");
		drtMgt.delete(dirs, DirectorySetting.class, "id");
		return query(mapping, form, request, response);
		

	}
	
	/**
	 * 添加目录之前
	 */
	/**
	 * 添加目录mj
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
	protected ActionForward addDirPre(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("isRoot", getParameterInt("isRoot", request));
		return getForward(request, mapping, "add");
	}
	

	/**
	 * 添加目录mj
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
	protected ActionForward addDirectory(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String userId = this.getLoginBean(request).getId(); // 获得登陆者ID
		String name = getParameter("name", request); // 目录名称
		String path = getParameter("path", request); // 目录名称
		int isRoot = getParameterInt("isRoot", request);
		String shareuserId = getParameter("shareuserId", request); // 员工编号
		String shareDeptOfClassCode = getParameter("shareDeptOfClassCode",
				request); // 部门编号
		String shareEmpGroup = getParameter("shareEmpGroup", request); // 职员分组
		
		String downLoadUserId=request.getParameter("downLoadUserId");
		String downLoadDeptOfClassCode=request.getParameter("downLoadDeptOfClassCode");
		String downLoadEmpGroup=request.getParameter("downLoadEmpGroup");
		
		
		//int treeNo = drtMgt.getQueryCount("directorySetting", "1", "1");
		// 给bean赋值
		DirectorySetting bean = new DirectorySetting();
		bean.setIsRoot(isRoot);// 网络硬盘1 企业相册0
		bean.setUserId(userId);
		String time = BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		bean.setCreateTime(time);
		bean.setLastUpdateTime(time);
		bean.setId(IDGenerater.getId());
		bean.setName(name);
		bean.setPath(path);
		bean.setShareDeptOfClassCode(shareDeptOfClassCode);
		bean.setShareEmpGroup(shareEmpGroup);
		bean.setShareuserId(shareuserId);
		
		bean.setDownLoadUserId(downLoadUserId);
		bean.setDownLoadDeptOfClassCode(downLoadDeptOfClassCode);
		bean.setDownLoadEmpGroup(downLoadEmpGroup);
		
		bean.setTreeNo(1);// 节点的编号，在上移下移的时候需要（该功能字段方能以后扩展需要）
		Result rs_add = drtMgt.add(bean);
		if (rs_add.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			
			if (isRoot == 0) {
				EchoMessage.success().add(
						getMessage(request, "common.msg.addSuccess")).setBackUrl(
						"/DirectorySettingAlbumQueryAction.do?isRoot="+isRoot).setAlertRequest(request);
				return getForward(request, mapping, "message");
			} else {
				EchoMessage.success().add(
						getMessage(request, "common.msg.addSuccess")).setBackUrl(
						"/DirectorySettingNetDiskQueryAction.do?isRoot="+isRoot).setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
			
		} else {

			// 添加失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.addFailture"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}

	/**
	 * 修改前
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
	protected ActionForward updateDirectoryPre(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String dId = request.getParameter("id");
		// 获得所有的相册信息
		Result rs = drtMgt.load(dId, DirectorySetting.class);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {

			DirectorySetting ds = (DirectorySetting) rs.getRetVal();
			String classCodes = ds.getShareDeptOfClassCode();

			if (StringUtils.isNotBlank(classCodes)) {
				// 查询通知部门
				List<Department> targetDept = new ArrayList<Department>();
				String[] popedomDeptIds = classCodes.split(",");
				for (String classCode : popedomDeptIds) {
					Department dept = adviceMgt.getDepartmentByClassCode(classCode);
					if (dept != null) {
						targetDept.add(dept);
					}
				}
				ds.setShareDepts(targetDept);
			}

			String usersId = ds.getShareuserId();
			if (StringUtils.isNotBlank(usersId)) {
				// 查询通知员工
				List<Employee> targetUsers = new ArrayList<Employee>();
				String[] userArr = usersId.split(",");

				for (String uId : userArr) {
					Employee employee = empMgt.getEmployeeById(uId);
					if (employee != null) {
						targetUsers.add(employee);
					}
				}
				ds.setShareUserNames(targetUsers);
			}

			String empGroups = ds.getShareEmpGroup();
			// 查找职员分组
			if (StringUtils.isNotBlank(empGroups)) {
				List<String[]> listEmpGroup = new ArrayList<String[]>();
				String[] popedomEmpGroupIds = empGroups.split(",");
				for (String empGroup : popedomEmpGroupIds) {
					Result r = empMgt.selectEmpGroupById(empGroup);
					if (r.getRetVal() != null) {
						listEmpGroup.add((String[]) r.getRetVal());
					}
				}
				ds.setShareEmpGroups(listEmpGroup);
			}
			
			
			String downLoadClassCodes=ds.getDownLoadDeptOfClassCode();
			if(StringUtils.isNotBlank(downLoadClassCodes)){
				// 查询通知部门
				List<Department> targetDept1 = new ArrayList<Department>();
				String[] popedomDeptIds = downLoadClassCodes.split(",");
				for (String classCode : popedomDeptIds) {
					Department dept1 = adviceMgt.getDepartmentByClassCode(classCode);

					if (dept1 != null) {
						targetDept1.add(dept1);
					}
				}
				 ds.setDownLoadDepts(targetDept1);
			}
			
			String downLoadUserId=ds.getDownLoadUserId();
			if(StringUtils.isNotBlank(downLoadUserId)){
				List<Employee> targetUsers2=new ArrayList<Employee>();
				String[] downLoadUserArr=downLoadUserId.split(",");
				for(String empUserId:downLoadUserArr){
					Employee emp=empMgt.getEmployeeById(empUserId);
					if(emp!=null){
						targetUsers2.add(emp);
					}
				}
				ds.setDownLoadUserNames(targetUsers2);
			}
			
			String downLoadGroup=ds.getDownLoadEmpGroup();
			if(StringUtils.isNotBlank(downLoadGroup)){
				List<String[]> downGroup=new ArrayList<String[]>();
				String[] downLoadGroupId=downLoadGroup.split(",");
				for(String groupId:downLoadGroupId){
					Result r = empMgt.selectEmpGroupById(groupId);
					if (r.getRetVal() != null) {
						downGroup.add((String[]) r.getRetVal());
					}
				}
				ds.setDownLoadGroups(downGroup);
			}

			request.setAttribute("directorySetting", ds);
			String path = ds.getPath();
			path = path.replaceAll("\\\\", "\\\\\\\\");
			request.setAttribute("pathstr", path);
			request.setAttribute("isRoot", getParameter("isRoot", request));
			
		} else {
			EchoMessage.error().add(
					getMessage(request, "com.revert.to.failure"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

		return getForward(request, mapping, "update");
	}

	/**
	 * 修改
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
	protected ActionForward updateDirectory(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String userId = this.getLoginBean(request).getId(); // 获得登陆者ID
		String name = request.getParameter("name"); // 目录名称
		String path = request.getParameter("path"); // 目录名称
		int isRoot = getParameterInt("isRoot", request);
		String shareuserId = request.getParameter("shareuserId"); // 员工编号
		String shareDeptOfClassCode = request
				.getParameter("shareDeptOfClassCode"); // 部门编号
		String shareEmpGroup = request.getParameter("shareEmpGroup"); // 职员分组
		
		String downLoadUserId=request.getParameter("downLoadUserId");
		String downLoadDeptOfClassCode=request.getParameter("downLoadDeptOfClassCode");
		String downLoadEmpGroup=request.getParameter("downLoadEmpGroup");

		String dirId = request.getParameter("id");
		// 给bean赋值
		DirectorySetting bean = (DirectorySetting) drtMgt.load(dirId,
				DirectorySetting.class).getRetVal();

		bean.setName(name);
		bean.setPath(path);
		bean.setShareuserId(shareuserId);
		bean.setShareDeptOfClassCode(shareDeptOfClassCode);
		bean.setShareEmpGroup(shareEmpGroup);
		bean.setUserId(userId);
		bean.setDownLoadUserId(downLoadUserId);
		bean.setDownLoadDeptOfClassCode(downLoadDeptOfClassCode);
		bean.setDownLoadEmpGroup(downLoadEmpGroup);

		String time = BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		bean.setLastUpdateTime(time);
		bean.setName(name);
		Result rs_update = drtMgt.update(bean);
		if (rs_update.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (isRoot == 0) {
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess"))
						.setBackUrl("/DirectorySettingAlbumQueryAction.do?isRoot="+isRoot)
						.setAlertRequest(request);
				return getForward(request, mapping, "message");
			} else {
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess"))
						.setBackUrl("/DirectorySettingNetDiskQueryAction.do?isRoot="+isRoot)
						.setAlertRequest(request);
				return getForward(request, mapping, "message");
			}
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.failure"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}

	/**
	 * 设置首页 mj
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
		
		int pageNo = getParameterInt("pageNo", request);
		int pageSize = getParameterInt("pageSize", request);
		String isRootStr = request.getParameter("isRoot");
		int isRoot = isRootStr == null ? 1 :Integer.parseInt(isRootStr);
		request.setAttribute("isRoot", isRoot);
		
		// 获得所有的目录信息
		Result rs = drtMgt.query(pageNo <= 0 ? 1 : pageNo, pageSize <= 0 ? 20
				: pageSize, true,isRoot);

		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<DirectorySetting> directorySetingList = (List<DirectorySetting>) rs
					.getRetVal();
			for (DirectorySetting ds : directorySetingList) {
				String classCodes = ds.getShareDeptOfClassCode();
				if (StringUtils.isNotBlank(classCodes)) {
					// 查询通知部门
					List<Department> targetDept = new ArrayList<Department>();
					String[] popedomDeptIds = classCodes.split(",");
					for (String classCode : popedomDeptIds) {
						Department dept = adviceMgt
								.getDepartmentByClassCode(classCode);

						if (dept != null) {
							targetDept.add(dept);
						}
					}
					ds.setShareDepts(targetDept);
				}

				String usersId = ds.getShareuserId();
				if (StringUtils.isNotBlank(usersId)) {
					// 查询通知员工
					List<Employee> targetUsers = new ArrayList<Employee>();
					String[] userArr = usersId.split(",");

					for (String uId : userArr) {
						Employee employee = empMgt.getEmployeeById(uId);
						if (employee != null) {
							targetUsers.add(employee);
						}
					}
					ds.setShareUserNames(targetUsers);
				}

				String empGroups = ds.getShareEmpGroup();
				// 查找职员分组
				if (StringUtils.isNotBlank(empGroups)) {
					List<String[]> listEmpGroup = new ArrayList<String[]>();
					String[] popedomEmpGroupIds = empGroups.split(",");
					for (String empGroup : popedomEmpGroupIds) {
						Result r = empMgt.selectEmpGroupById(empGroup);
						if (r.getRetVal() != null) {
							listEmpGroup.add((String[]) r.getRetVal());
						}
					}
					ds.setShareEmpGroups(listEmpGroup);
				}
			}
			request.setAttribute("pageBar", pageBars(rs, request));
			request.setAttribute("directorySetingList", directorySetingList);
			LoginBean loginBean = this.getLoginBean(request);
			String moUrl = "/DirectorySettingAlbumQueryAction.do";
			if (StringUtils.equals("1", isRoot+"")) {
				moUrl = "/DirectorySettingNetDiskQueryAction.do";
			}
			MOperation mopSetting = (MOperation) loginBean.getOperationMap().get(
					moUrl);
			request.setAttribute("add",mopSetting == null ? false : mopSetting.add()); // 增加权限
			request.setAttribute("del",mopSetting == null ? false : mopSetting.delete()); // 删权限
			request.setAttribute("upd",mopSetting == null ? false : mopSetting.update()); // 改权限
			
			
		} else {
			EchoMessage.error().add(
					getMessage(request, "com.revert.to.failure"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		request.setAttribute("pageBar", this.pageBars(rs, request));
		return getForward(request, mapping, "index");
	}

	/**
	 * 删除
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
	protected ActionForward delById(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String directId = request.getParameter("id");
		drtMgt.deleteBean(directId, DirectorySetting.class);
		return query(mapping, form, request, response);

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
	protected ActionForward whetherExistPath(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String path = request.getParameter("path"); // 相册名称
		String result = null;
		if (StringUtils.isNotBlank(path)) {
			path = GlobalsTool.toChinseChar(path);
		} else {
			result = "false";
		}
		File file = new File(path);
		String isRoot = getParameter("isRoot",request);
	    int count = drtMgt.getQueryCount("DirectorySetting", "isRoot", isRoot, "path", path);
		if (!file.exists() || count > 0 ) {//已经有目录存在该路径
			result = "false";
		} else {
			result = "true";
		}
		request.setAttribute("msg", result);
		return getForward(request, mapping, "blank");
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
	protected ActionForward whetherExistName(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String isRoot = getParameter("isRoot",request);
		String key = getParameter("key",request);//根据目录名称或者路径（暂时只需要考虑 名称不能相同，路径可以相同）
		String type= getParameter("qtype", request);
		String result = null;
		int count = 0;
		if (StringUtils.isNotBlank(key)) {
			key = GlobalsTool.toChinseChar(key);
			
		}
		if (StringUtils.equals(type, "name")) {
			//根据目录名称判断
			count = drtMgt.getQueryCount("DirectorySetting", "isRoot", isRoot, "name", key);
		} else {
			count = drtMgt.getQueryCount("DirectorySetting", "isRoot", isRoot, "path", key);
			
		}
		if (count > 0 ) {//已经存在该路径或者目录 无法创建或者修改
			result = "false";
		} else {
			result = "true";
		}
		request.setAttribute("msg", result);
		return getForward(request, mapping, "blank");
	}

}
