package com.koron.oa.album;

import java.io.File;
import java.util.*;

import javax.servlet.http.*;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.AlbumBean;
import com.koron.oa.bean.PhotoInfoBean;
import com.menyi.aio.bean.*;
import com.menyi.aio.web.login.*;
import com.menyi.web.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * 
 * <p>
 * Title: 企业相册
 * </p>
 * 
 * @Copyright: 科荣软件
 * 
 * @author 毛晶
 * 
 */
public class AlbumAction extends MgtBaseAction {
	AlbumMgt amMgt = new AlbumMgt();
	private static Gson gson;// mj
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}
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
		String noback = request.getParameter("noback");// 不能返回
		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		switch (operation) {
		case OperationConst.OP_ADD:
			String typeAdd = request.getParameter("addType");
			if ( "whetherExistName".equals(typeAdd) ) {
				forward = whetherExistName(mapping, form, request, response);
			} else {
				forward = addAlbum(mapping, form, request, response);
			}
			break;
		// 新增前的准备
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		// 上传页面
		case OperationConst.OP_IMPORT_PREPARE:
			forward = uploadPrepare(mapping, form, request, response);
			break;
		// 上传完成后
		case OperationConst.OP_UPDATE_PREPARE:
			String type = request.getParameter("managerType");
			if ( "managerPhos".equals(type)) {
				forward = modityPhosByManager(mapping, form, request, response);
			} else {
				forward = pre_updatePhoto(mapping, form, request, response);
			}
			break;
		// 修改
		case OperationConst.OP_UPDATE:
			forward = updateAlbum(mapping, form, request, response);
			break;
		// //查询
		case OperationConst.OP_QUERY:
			String type_query = request.getParameter("type_query");
			if ("".equals(type_query) || type_query == null) { // 按条件查询
				forward = query(mapping, form, request, response);
			}
			break;
		// 删除
		case OperationConst.OP_DELETE:
			forward = delAlbumInfo(mapping, form, request, response);
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * 添加相册mj
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
	protected ActionForward addAlbum(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 当前相册
		String currentAlbum = request.getParameter("currentAlbum");
		String userId = this.getLoginBean(request).getId(); // 获得登陆者ID
		String name = request.getParameter("albumName"); // 相册名称
		if (StringUtils.isNotBlank(name)) {
			name = GlobalsTool.toChinseChar(name);
		} else {

			return query(mapping, form, request, response);
			
		}
		
		String albumDesc = request.getParameter("albumDesc"); // 相册描述
		if(StringUtils.isNotBlank(albumDesc)){
			albumDesc = GlobalsTool.toChinseChar(albumDesc);
		}
		String isSaveReading = request.getParameter("isSaveReading");// 是否保存阅读痕迹
		if (StringUtils.isBlank(isSaveReading)) {
			isSaveReading = "1";
		}		
		// 给bean赋值
		AlbumBean bean = new AlbumBean();
		bean.setAgreeNum(0);
		bean.setAlbumDesc(albumDesc);
		bean.setCover(null);// 可以给一个默认的封面
		bean.setCreateBy(userId);
		String time = BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		bean.setCreateTime(time);
		bean.setLastUpdateTime(time);
		bean.setId(IDGenerater.getId());
		bean.setIsSaveReading(isSaveReading);
		bean.setName(name);
		Result rs_add = amMgt.add(bean);
		
//		String orderType = request.getParameter("orderType");
//		request.setAttribute("orderType", orderType);
		if (rs_add.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (StringUtils.isNotBlank(currentAlbum)) {
				// 从upload页面创建的 调整到upload.jsp中
				return uploadPrepare(mapping, form, request, response);
			} else {
				// 添加成功
				EchoMessage.success().add(
						getMessage(request, "common.msg.addSuccess"))
						.setBackUrl("/AlbumQueryAction.do").setAlertRequest(
								request);
				return getForward(request, mapping, "message");
			}
			
		} else {
			
			return query(mapping, form, request, response);
			/*创建完新的相册后立即点击本页显示15个，报：对不起，你未被授权
			// 添加失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.addFailture"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");*/
		}
	}

	/**
	 * 修改相册mj
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
	protected ActionForward updateAlbum(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String name = request.getParameter("albumName"); // 相册名称
		if (StringUtils.isNotBlank(name)) {
			name = GlobalsTool.toChinseChar(name);
		}
		String albumDesc = request.getParameter("albumDesc"); // 相册描述
		if (StringUtils.isNotBlank(albumDesc)) {
			albumDesc = GlobalsTool.toChinseChar(albumDesc);
		}
		String isSaveReading = request.getParameter("isSaveReading");// 是否保存阅读痕迹
		if (StringUtils.isBlank(isSaveReading)) {
			isSaveReading = "1";
		}
		String albumId = request.getParameter("albumId");
		// 给bean赋值
		AlbumBean bean = (AlbumBean) amMgt.load(albumId).getRetVal();
		bean.setAlbumDesc(albumDesc);
		String time = BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		bean.setLastUpdateTime(time);
		bean.setName(name);
		Result rs_update = amMgt.update(bean);
		
		if (rs_update.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return query(mapping, form, request, response);
		} else {
			// 添加失败
			EchoMessage.error().add(getMessage(request, "common.msg.failure"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	
	}

	/**
	 * 相册首页 mj
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

		// 取出用户在该模块中的权限
		LoginBean loginBean = this.getLoginBean(request);
		int pageNo = getParameterInt("pageNo", request);
		int pageSize = getParameterInt("pageSize", request);
		String orderType = request.getParameter("orderType");
		MOperation mop = (MOperation) loginBean.getOperationMap().get(
				"/AlbumQueryAction.do");
		request.setAttribute("add", mop.add()); // 增加权限
		request.setAttribute("del", mop.delete()); // 删权限
		request.setAttribute("upd", mop.update()); // 改权限
		// 获得所有的相册信息
		// Result rs = amMgt.query(pageNo<=0?1:pageNo,
		// pageSize<=0?100:pageSize);方法一
		orderType = orderType == null ? "desc" : orderType;
		Result rs = amMgt.queryBySql(orderType,
				pageNo <= 0 ? 1 : pageNo, pageSize <= 0 ? 100 : pageSize);// 方法二
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<AlbumBean> albumList = (List<AlbumBean>) rs.getRetVal();
			request.setAttribute("albumList", albumList);
			request.setAttribute("orderType", orderType == null ? "desc" : orderType);
		} else { // 失败
			EchoMessage.error().add(
					getMessage(request, "com.revert.to.failure"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		request.setAttribute("pageBar", this.pageBar(rs, request));
		String url = request.getRequestURI();
		String favoriteURL = java.net.URLEncoder.encode(url);
		request.setAttribute("favoriteURL", favoriteURL+"?1=1");
		request.setAttribute("orderType", orderType);
		return getForward(request, mapping, "albumList");
	}

	/**
	 * 新增前的准备 mj 暂时未用到
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
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return getForward(request, mapping, "albumAdd");//可以不要 对应的js也可以不要 因为添加写在了层里面了
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
		// 查询所有的相册
		// 当前相册
		String currentAlbum = request.getParameter("currentAlbum");
		request.setAttribute("currentAlbum", currentAlbum);
		String pageType = request.getParameter("pageType");
		request.setAttribute("pageType", pageType);
		request.setAttribute("orderType", request.getParameter("orderType"));
		// 获得所有的相册信息
		Result rs = amMgt.query(1, 100, false);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<AlbumBean> albumList = (List<AlbumBean>) rs.getRetVal();
			request.setAttribute("albumList", albumList);
		} else { // 失败
			EchoMessage.error().add(
					getMessage(request, "com.revert.to.failure"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

		return getForward(request, mapping, "uploadPhoto");
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

		String userId = this.getLoginBean(request).getId(); // 获得登陆者ID

		String albumSelectId = request.getParameter("albumSelectId");// 被选择的相册
		AlbumBean album = (AlbumBean) amMgt.getAlbumById(albumSelectId)
				.getRetVal();
		request.setAttribute("albumSelectId", albumSelectId);

		String picFiles = request.getParameter("picFiles");// 上传的照片
		picFiles = picFiles == null ? "" : picFiles;
		// 需删除的附件
		String delFiles = request.getParameter("delPicFiles");
		// 需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0 && picFiles.indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/album/img/"
						+ del);
				aFile.delete();
				File bFile = new File(BaseEnv.FILESERVERPATH + "/album/img/small/"
						+ del);
				bFile.delete();
			}
		}
		// 保存所有的上传的数据到数据库 在此基础上再去修改每张照片的信息
		String[] picArr = picFiles.split(";");
		Result rs = amMgt.saveOrUpdate(picArr, album, userId);
		String orderType = request.getParameter("orderType");
		request.setAttribute("orderType", orderType);
		if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<PhotoInfoBean> PhotoInfoBeanList = (List<PhotoInfoBean>) rs
					.getRetVal();
			request.setAttribute("picList", PhotoInfoBeanList);
		} else { // 失败
			EchoMessage.error().add(
					getMessage(request, "com.revert.to.failure"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "uploadPhotoEnd");
	}
	
	/**
	 * 相片管理
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
	protected ActionForward modityPhosByManager(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String albumSelectId = request.getParameter("albumSelectId");// 被选择的相册
		request.setAttribute("albumSelectId", albumSelectId);
		String[] pIds = request.getParameterValues("pho");
		List<PhotoInfoBean> photoInfoBeanList = new ArrayList<PhotoInfoBean>();
		for (String pId : pIds) {
			PhotoInfoBean bean = (PhotoInfoBean) amMgt.getPhotoInfoById(pId).getRetVal();
			photoInfoBeanList.add(bean);
		}
		String orderType = request.getParameter("orderType");
		request.setAttribute("orderType", orderType);
		request.setAttribute("picList", photoInfoBeanList);
		return getForward(request, mapping, "uploadPhotoEnd");
	}

	/**
	 * 删除相册mj
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
	protected ActionForward delAlbumInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// 获得页面参数-->需要删除到ID
		String albumId = request.getParameter("keyId");
		Result result = amMgt.queryBegnameByAlbumId("desc",albumId,1,100);
		List<PhotoInfoBean> list = (List<PhotoInfoBean>)result.getRetVal();
		for (int i = 0; i < list.size(); i++) {
			PhotoInfoBean bean = list.get(i);
			String beginName = bean.getBeginName();
			File aFile = new File(BaseEnv.FILESERVERPATH + "/album/img/"+ beginName);
			aFile.delete();
			aFile = new File(BaseEnv.FILESERVERPATH + "/album/img/small/"+ beginName);
			aFile.delete();
		}
		Result rs_newsInfo = amMgt.del_photo(albumId);
		// 删除相册
		rs_newsInfo = amMgt.deleteBean(albumId);
		//if (rs_newsInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// 首页
			return query(mapping, form, request, response);
		//} else {
			// 添加失败
			//EchoMessage.error().add(getMessage(request, "common.msg.delError"))
				//	.setAlertRequest(request);
			//return getForward(request, mapping, "message");
		//}
	}
	@Override
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {

		LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            BaseEnv.log.debug("OANewInfoAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
        //进行唯一用户验证，如果有重复登陆的，则后进入用户踢出前进入用户
        if (!OnlineUserInfo.checkUser(req)) {
            //需踢出
            EchoMessage.error().setAlertRequest(req);
            return getForward(req, mapping, "doubleOnline");
        }      
        
        int operation = getOperation(req);
        MOperation mop = (MOperation) loginBean.getOperationMap().get(
		"/AlbumQueryAction.do");
        if(operation == OperationConst.OP_IMPORT_PREPARE||operation == OperationConst.OP_UPDATE_PREPARE){//上传 
        	if (mop.add) {
        		return null;
        	}
        }
		return super.doAuth(req, mapping) ;
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
		String name = request.getParameter("albumName"); // 相册名称
		if (StringUtils.isNotBlank(name)) {
			name = GlobalsTool.toChinseChar(name);
		} else {
			return query(mapping, form, request, response);
		}
		int count = amMgt.getQueryCount("tblAlbum","name",name);
		String json = gson.toJson(count);
		request.setAttribute("msg", json);
		return getForward(request, mapping, "blank");
	}
	
	/**
	 * DBTableInfoBean 按tbleName排序 mj
	 */
//	public class SortDBTable implements Comparator {
//		public int compare(Object o1, Object o2) {
//			DBTableInfoBean table1 = (DBTableInfoBean) o1;
//			DBTableInfoBean table2 = (DBTableInfoBean) o2;
//
//			if (table1 == null || table2 == null) {
//				return 0;
//			}
//
//			String tableName1 = table1.getTableName();
//			String tableName2 = table2.getTableName();
//
//			return tableName1.compareToIgnoreCase(tableName2);
//		}
//	}

}
