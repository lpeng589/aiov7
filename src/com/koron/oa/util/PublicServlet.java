package com.koron.oa.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dbfactory.Result;
import com.koron.oa.bean.DirectorySetting;
import com.koron.oa.directorySeting.DirectorySettingMgt;
import com.koron.oa.individual.workPlan.OADateWorkPlanMgt;
import com.menyi.aio.web.label.LabelMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.LoginScopeBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.email.EMailMgt;
import com.menyi.msgcenter.server.MsgMgt;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.OnlineUserInfo;
import com.menyi.web.util.OnlineUserInfo.OnlineUser;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:
 * @Copyright: 科荣软件
 * @Author 毛晶
 * @preserve all
 */
public class PublicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// 因为此类将被频繁由ajax调用，故在此更新用户的心跳信息
		Object o = req.getSession().getAttribute("LoginBean");
		if (o != null) {
			LoginBean bean = (LoginBean) o;
			OnlineUserInfo.refresh(bean.getId(),null);
		}

		String operation = req.getParameter("operation");
		if ("treeData".equals(operation)) {
			getTreeData(req, resp);
		} else if ("downLoadFiles".equals(operation)) {
			downLoadFiles(req, resp);
		} else if ("showEmp".equals(operation)) {
			showEmp(req, resp);
		} else if ("selectSeq".equals(operation)) {
			selectSeq(req, resp);
		} else if("selectSeqMax".equals(operation)){
			selectSeqMax(req, resp);
		} else if("deptTree".equals(operation)){
			getDeptTree(req, resp) ;
		} else if("address".equals(operation)){
			getClientAddress(req, resp);
		} else if("ajaxTextboxListEmp".equals(operation)){
			ajaxTextboxListEmp(req, resp);
		}
	}
	
	/**
	 * 加载部门 树形结构
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getDeptTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		try {
			/*查看权限*/
			MOperation mop = (MOperation)getLoginBean(request).getOperationMap().get("/OAWorkPlanAction.do");
			String empSQL = "" ;
			String deptSQL = "" ;
			String strSQL = "" ;
			if(mop!=null){
				ArrayList scopeRight = mop.getScope(MOperation.M_QUERY);
				if (scopeRight != null && scopeRight.size()>0) {
					strSQL = " and (" ;
					for (Object o : scopeRight) {
						LoginScopeBean lsb = (LoginScopeBean) o;
						if(lsb!=null && "1".equals(lsb.getFlag())){
							empSQL += " b.id in (" ;
							for(String strId : lsb.getScopeValue().split(";")){
								empSQL += "'"+strId+"'," ;
							}
							empSQL = empSQL.substring(0, empSQL.length()-1) ;
							empSQL += ") or " ;
							strSQL += empSQL ;
						}
						if(lsb!=null && "5".equals(lsb.getFlag())){
							for(String strId : lsb.getScopeValue().split(";")){
								deptSQL += " a.classCode like '"+strId+"%' or " ;
							}
							//deptSQL = deptSQL.substring(0, deptSQL.length()-3) ;
							strSQL += deptSQL ;
						}
					}
					if(strSQL.endsWith("or ")){
						strSQL = strSQL.substring(0,strSQL.length()-3) ;
					}
					strSQL += ")" ;
				}
			}
			if((strSQL!=null && strSQL.trim().length()>0) || "1".equals(getLoginBean(request).getId())){
				Result rss = new OADateWorkPlanMgt().queryEmployeeByDept(strSQL);
				StringBuffer strTree = new StringBuffer();
				if (rss.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
					HashMap<String, List<String[]>> deptMap = (HashMap<String, List<String[]>>) rss.retVal ;
					strTree.append("[") ;
					for(String key : deptMap.keySet()){
						List<String[]> empList = deptMap.get(key) ;
						if(empList==null || empList.size()==0) continue ;
						strTree.append("{id:'',name:'"+key+"',isParent:true,nodes:[") ;
						for(String[] emp : empList){
							strTree.append("{id:'"+emp[3]+"',name:'"+emp[2]+"'},") ;
						}
						strTree.delete(strTree.length()-1, strTree.length()) ;
						strTree.append("]},");
					}
					if(strTree.toString().endsWith(",")){
						strTree.delete(strTree.length()-1, strTree.length()) ;
					}
					strTree = strTree.append("]");
				}
				reponse(request, response, strTree.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 读取图片任意
	// 加载tree
	@SuppressWarnings( { "unchecked", "static-access" })
	public void getTreeData(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {
			String pId = request.getParameter("id");
			
			String pName = request.getParameter("name");
			String type = request.getParameter("type");
			Integer isRoot = Integer.parseInt(type);
			// 查询根节点数据
			DirectorySettingMgt mgt = new DirectorySettingMgt();
			Object o = request.getSession().getAttribute("LoginBean");
			LoginBean bean = (LoginBean) o;
			Result rs = mgt.getTreeRootData(bean, isRoot);
			List<DirectorySetting> ds = (List<DirectorySetting>) rs.getRetVal();
			// 构造数据，可以从数据库中查询
			boolean isParent = true;
			String str = "[";
			if (pId == null || pName == null) {
				pId = "0";
				pName = "";
				for (int i = 0; i < ds.size(); i++) {
					DirectorySetting directory = ds.get(i);
					String dateId=directory.getId();
					String nId = directory.getPath();
					String nName = directory.getName();
					nId = nId.replaceAll("'", "\\\\'") ;//需要进行转换，js才能识别
					nName = nName.replaceAll("'", "\\\\'");
					nId = nId.replaceAll("\\\\", "\\\\\\\\");
					str += "{'id':'" + nId + "','name':'" + nName
							+ "','icon':'1'" + ",'isParent':" + isParent + ",'dateId':'"+dateId+"'"+"},";
				}
				// +",'open':true"
			} else {
				File file = new File(pId);
				String dateId = request.getParameter("dateId");
				if (file.exists()) {
					File[] list = file.listFiles();

					for (int i = 0; list != null && i < list.length; i++) {
						File f = list[i];
						if (f.isDirectory()) {
							
							String nId = f.getPath();
							String nName = f.getName();
							nId = nId.replaceAll("'", "\\\\'") ;//需要进行转换，js才能识别
							nName = nName.replaceAll("'", "\\\\'");
							if (!"small".equals(nName)) {
								nId = nId.replaceAll("\\\\", "\\\\\\\\");
								str += "{'id':'" + nId + "','name':'" + nName
										+ "','icon':''" + ",'isParent':"
										+ isParent +  ",'dateId':'"+dateId+"'"+"},";
							}

						}
					}
				}
			}
			str = str.substring(0, str.length() - 1) + "]";
			reponse(request, response, str);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void reponse(HttpServletRequest request,
			HttpServletResponse response, Object msg) throws Exception {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write(msg.toString());
		out.flush();
		out.close();
	}

	public static List<File> getFiles(String path, String type) {
		File file = new File(path);
		String formatOfImgs = ImgManagerUtil.getAllowFormatByType(type);
		List<File> phos = new ArrayList<File>();

		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isFile()) {
				String fileName = f.getName();
				String fileExtend = fileName.substring(
						fileName.lastIndexOf(".") + 1).toLowerCase();
				if (Arrays.<String> asList(formatOfImgs.split(",")).contains(
						fileExtend)) {
					phos.add(f);
				}
			}
		}
		return phos;
	}

	// 文件打包下载
	public static HttpServletResponse downLoadFiles(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String path = request.getParameter("path");
			path = GlobalsTool.toChinseChar(path);

			String pho = request.getParameter("pho");
			List<File> files = new ArrayList<File>();

			String isNeedDebar = request.getParameter("isNeedDebar");

			if (pho == null) {
				files = getFiles(path, "image");
			} else {
				List<File> filesByPath = null;
				if (StringUtils.equals("true", isNeedDebar)) {
					filesByPath = getFiles(path, "image");// 获得所有照片
				}
				pho = GlobalsTool.toChinseChar(pho);
				String[] pIds = pho.split(",");
				for (String pId : pIds) {

					File file = new File(path + "/" + pId);
					if (file.exists()) {
						files.add(file);
					}
				}
				if (filesByPath != null) {
					if (filesByPath.removeAll(files)) {
						files = filesByPath;
					}
				}
			}
			
			String tempName = request.getParameter("downName");
			/**
			 * 创建一个临时压缩文件， 我们会把文件流全部注入到这个文件中 这里的文件你可以自定义是.rar还是.zip
			 */
			tempName = GlobalsTool.toChinseChar(tempName);
			tempName = "files";
			File file = new File("c:/" + tempName + ".rar");
			if (!file.exists()) {
				file.createNewFile();
			}
			response.reset();
			// response.getWriter()
			// 创建文件输出流
			FileOutputStream fous = new FileOutputStream(file);
			org.apache.tools.zip.ZipOutputStream zipOut = new org.apache.tools.zip.ZipOutputStream(
					fous);
			// ZipOutputStream zipOut = new ZipOutputStream(fous); 該java自帶的
			// 會有中文亂碼問題
			/**
			 * 这个方法接受的就是一个所要打包文件的集合， 还有一个ZipOutputStream
			 */
			// 設置編碼
			boolean isWindows = GlobalsTool.isWindowsOS(); // 判断是否是windows系统
			if (isWindows) {
				zipOut.setEncoding("GBK");// 设置为GBK后在windows下就不会乱码了，如果要放到Linux或者Unix下就不要设置了
			}
			GlobalsTool.zipFile(files, zipOut);
			zipOut.close();
			fous.close();
			return GlobalsTool.downloadZip(file, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * 直到文件的打包已经成功了， 文件的打包过程被我封装在UtilServlet.zipFile这个静态方法中， 接下来的就是往客户端写数据了
		 */
		// OutputStream out = response.getOutputStream();
		return response;
	}

	/**
	 * 判断是否存在序列号
	 * 
	 * @param request
	 * @param response
	 */
	public void selectSeq(HttpServletRequest request,
			HttpServletResponse response) {

		try {
			String seq = request.getParameter("seq");
			String id = request.getParameter("id");
			LabelMgt mgt = new LabelMgt();
			Result result = mgt.getSeqEcho(seq);
			Result re = mgt.getSeq(seq,id);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS
					&& re.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				if ((result.retVal != null && !"".equals(result.retVal))
						|| (re.retVal != null && !"".equals(re.retVal))) {
					this.reponse(request, response, "echo");
				}
				this.reponse(request, response, "OK");
			} else {
				this.reponse(request, response, "Error");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private LoginBean getLoginBean(HttpServletRequest request) {
		Object o = request.getSession().getAttribute("LoginBean");
		if (o != null) {
			return (LoginBean) o;
		}
		return null;
	}



	/**
	 * 写邮件的人员选择
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException
	 * 
	 */
	public void showEmp(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html; charset=utf-8");
		String tabType = request.getParameter("tabType"); // tab类型
		String keyWord = request.getParameter("keyWord");
		if (keyWord != null) {
			keyWord = GlobalsTool.toChinseChar(keyWord);
		}

		ArrayList[] userList = OnlineUserInfo.getOnlineOfflineUser("");
		ArrayList<OnlineUser> allUser = new ArrayList<OnlineUser>();
		allUser.addAll(userList[0]);
		allUser.addAll(userList[1]);
		request.setAttribute("totleNum", (userList[0].size() + userList[1]
				.size()));// 总人数量
		request.setAttribute("onLineNum", userList[0].size());// 在线人数量

		LoginBean loginBean = getLoginBean(request);
		// 这里只查1、在线人员，2、所有部门，3、职员分组,4、搜索用户
		if (keyWord != null && keyWord.length() > 0) {
			PrintWriter out = response.getWriter();
			StringBuilder searchStr = new StringBuilder();
			for (OnlineUser user : allUser) {
				if (user.getName().contains(keyWord)
						|| user.pingying.contains(keyWord)) {
					String click = "onClick=\"insertUser('person','"
							+ user.getId() + "','" + user.getName() + "')\"";
					String mobile = "<img src=\"/style/images/mobile.gif\"/>";

					if (!"mobile".equals(user.getType())) {
						mobile = "";
					}
					if (1 == 1 || user.isOnline()) {
						searchStr.append("<div " + click
								+ "><span class=\"c1\">" + user.getName()
								+ mobile + "</span></div>");
					} else {
						searchStr.append("<div " + click
								+ "><span class=\"c2\">" + user.getName()
								+ "</span></div>");
					}
				}
			}
			out.print(searchStr.toString());
			out.flush();
			return;
		} else if ("deptTab".equals(tabType)) {
			// 获取所有的部门信息
			PrintWriter out = response.getWriter();
			request.setAttribute("deparList", OnlineUserInfo.getAllDept());
			StringBuilder deptList = new StringBuilder();
			Result rs = new UserMgt().queryDept();
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				ArrayList<String[]> listDept = (ArrayList<String[]>) rs.retVal;
				for (String[] group : listDept) {
					if (group[0].equals(loginBean.getDepartCode())) {
						deptList
								.append("<li><span   onclick=\"insertUser('dept','"
										+ group[0]
										+ "','"
										+ group[1]
										+ "');\">" + group[1] + "</span><ul>");
					} else {
						deptList
								.append("<li><span>" + group[1] + "</span><ul>");
					}
					for (OnlineUser onlineUser : allUser) {
						if (onlineUser.departmentName.equals(group[1])) {
							String click = "onClick=\"insertUser('person','"
									+ onlineUser.getId() + "','"
									+ onlineUser.name + "')\"";
							String mobile = "<img src=\"/style/images/mobile.gif\"/>";

							if (!"mobile".equals(onlineUser.getType())) {
								mobile = "";
							}
							if (onlineUser.isOnline()) {
								deptList.append("<li " + click
										+ "><span class=\"c1\">"
										+ onlineUser.name + mobile
										+ "</span> </li>");
							} else {
								deptList.append("<li " + click
										+ "><span class=\"c2\">"
										+ onlineUser.name + "</span></li>");
							}
						}
					}
					deptList.append("</ul></li>");
				}
			}
			out.print(deptList.toString());
			out.flush();
			return;
		} else if ("groupTab".equals(tabType)) {
			PrintWriter out = response.getWriter();
			// 职员分组，需取职员分组信息
			Result rs = new MsgMgt().getMsgGroup(loginBean.getId());
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				StringBuilder groupList = new StringBuilder();
				HashMap<String, String> gmap = new HashMap<String, String>();
				for (Object[] os : (ArrayList<Object[]>) rs.retVal) {
					gmap.put((String) os[3], (String) os[2]);
				}
				for (String group : gmap.keySet()) {
					groupList
							.append("<li id=\"userId\"><span  onClick=\"cc('group','"
									+ group
									+ "','"
									+ gmap.get(group)
									+ "')\">"
									+ gmap.get(group) + "</span><ul>");
					for (Object[] os : (ArrayList<Object[]>) rs.retVal) {
						if (group.equals(os[3])) {
							OnlineUser onlineUser = OnlineUserInfo
									.getUser((String) os[0]);
							String click = "onClick=\"insertUser('person','"
									+ os[0] + "','" + onlineUser.name + "')\"";

							if (onlineUser != null) {
								if (onlineUser.isOnline()) {
									groupList
											.append("<li " + click
													+ "><span class=\"c1\">"
													+ onlineUser.name
													+ "</span> </li>");
								} else {
									groupList.append("<li " + click
											+ "><span class=\"c2\">"
											+ onlineUser.name + "</span></li>");
								}
							}
						}
					}
					groupList.append("</ul></li>");
				}
			/*	MOperation mop = (MOperation) loginBean.getOperationMap().get(
						"/UserFunctionQueryAction.do?tableName=tblEmpGroup");
				if (mop != null && mop.query) {
					groupList
							.append("<li id=\"userId\"><span><a href='javascript:mdiwin(\"/UserFunctionQueryAction.do?tableName=tblEmpGroup\",\"职员分组\");'>创建职员分组</a></span></li>");
				}*/
				out.print(groupList.toString());
				out.flush();
				return;
			}
		} else if ("onlineTab".equals(tabType)) {
			// 显示所有人员
			PrintWriter out = response.getWriter();
			StringBuilder onlineList = new StringBuilder();
			for (int i = 0; i < allUser.size(); i++) {
				OnlineUser online = (OnlineUser) allUser.get(i);
				String click = "onClick=\"insertUser('person','"
						+ online.getId() + "','" + online.name + "')\"";
				String mobile = "<img src=\"/style/images/mobile.gif\"/>";

				if (!"mobile".equals(online.getType())) {
					mobile = "";
				}
				onlineList.append("<div " + click + "><span class=\"c1\">"
						+ online.name + "</span>" + mobile + "</div>");
			}
			out.print(onlineList.toString());
			out.flush();
			return;
		} else if ("historyTab".equals(tabType)) {

			Result result = new EMailMgt().hisEmpOfMail(loginBean.getId(), "1","");// 内部邮件的前三十个
			// 发件人作为最近联系人
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				PrintWriter out = response.getWriter();
				StringBuilder onlineList = new StringBuilder();
				List<Object[]> empMap = (List<Object[]>) result.retVal;
				for (Object[] userId : empMap) {
					if (userId == null)
						continue;
					String click = "onClick=\"insertUser('person','"
							+ userId[0] + "','" + userId[0]+ "')\"";
					onlineList.append("<div " + click
								+ "><span class=\"c1\">" + userId[0]
								+ "</span>" + "" + "</div>");
				}
				out.print(onlineList.toString());
			}
			return;
		}
		request.setAttribute("type", "emp");
		request.setAttribute("onlineUsers", allUser);
		request.getRequestDispatcher("/vm/oa/mail/empList.jsp").forward(
				request, response);
	}


	/**
	 * 生成序列号
	 * @param request
	 * @param response
	 */
	public void selectSeqMax(HttpServletRequest request , HttpServletResponse response){
		
		try {			
			LabelMgt mgt = new LabelMgt();
			Result ts = mgt.sysnSeq();
			if(ts.retCode == ErrorCanst.DEFAULT_SUCCESS){
				this.reponse(request, response, ts.retVal);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 查询客户的地址
	 * @param request
	 * @param response
	 */
	public void getClientAddress(HttpServletRequest request , HttpServletResponse response){
		try {
			String keyId = request.getParameter("keyId");
			Result result = new AttentionMgt().getClientAddress(keyId);
			String address = "" ;
			if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
				address = (String)result.retVal;
			}
			reponse(request, response, address);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 柯志良  
	 * TextboxList 人员列表  
	 */
	public void ajaxTextboxListEmp(HttpServletRequest request , HttpServletResponse response){
		response.setContentType("text/html; charset=utf-8");
		String keyWord = request.getParameter("keyWord");
		if (keyWord != null) {
			keyWord = GlobalsTool.toChinseChar(keyWord);
		}

		ArrayList[] userList = OnlineUserInfo.getOnlineOfflineUser("");
		ArrayList<OnlineUser> allUser = new ArrayList<OnlineUser>();
		allUser.addAll(userList[0]);
		allUser.addAll(userList[1]);
		LoginBean loginBean = getLoginBean(request);
		
		String isJson=request.getParameter("ShowType");
		if (isJson !=null && isJson.equals("json") && keyWord != null && keyWord.length() > 0) {
			PrintWriter out=null;
			try {
				out = response.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringBuilder searchStr = new StringBuilder();
			for (OnlineUser user : allUser) {
				if (user.getName().contains(keyWord)
						|| user.pingying.contains(keyWord)) {
					searchStr.append("<li id='"+user.getId()+"' >"+user.getName()+"</li>");				
				}
			}
			out.print(searchStr.toString());
			out.flush();
			return;
		} 
	}
}
