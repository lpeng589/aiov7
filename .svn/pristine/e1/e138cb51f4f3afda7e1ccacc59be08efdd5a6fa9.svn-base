package com.koron.oa.album;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.*;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.AlbumBean;
import com.koron.oa.bean.OANewsInfoReplyBean;
import com.koron.oa.bean.PhotoInfoBean;
import com.koron.oa.publicMsg.newsInfo.OANewsInfoMgt;
import com.menyi.aio.bean.*;
import com.menyi.aio.web.login.*;
import com.menyi.web.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author mj
 * @version 1.0
 */
public class PhotoInfoAction extends MgtBaseAction {

	PhotoInfoMgt pMgt = new PhotoInfoMgt();

	AlbumMgt amMgt = new AlbumMgt();

	OANewsInfoMgt newsMgt = new OANewsInfoMgt();

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
		String replyId = request.getParameter("replyId");

		if (noback != null && noback.equals("true")) {
			request.setAttribute("noback", true);
		} else {
			request.setAttribute("noback", false);
		}
		switch (operation) {
		// 修改
		case OperationConst.OP_UPDATE:
			String type_upd = request.getParameter("type_upd");
			if (!"".equals(type_upd) && type_upd != null) {
				if (type_upd.equals("uploadEnd")) { // 修改
					forward = updateOfUploadEnd(mapping, form, request,
							response);
				} else if (type_upd.equals("moveToAlbum")) {// 移动
					forward = moveToAlbum(mapping, form, request, response);
				}
				break;
			} else {
				forward = updatePhoto(mapping, form, request, response);
				break;
			}
			// //查询
		case OperationConst.OP_QUERY:
			String reqType = request.getParameter("requestType");
			if ("ajax".equals(reqType)) {
				forward = changeInfoByAjax(mapping, form, request, response);
			} else {
				forward = showPhotosOfAlbum(mapping, form, request, response);
			}
			break;
		// 详细
		case OperationConst.OP_DETAIL:
			forward = detail(mapping, form, request, response);
			break;
		// 删除
		case OperationConst.OP_DELETE:
			String type = request.getParameter("type");
			if ("delPhotoReply".equals(type)) {
				forward = delReplyPhoto(mapping, form, request, response);
			} else if ("delPhotos".equals(type)) {
				forward = delPhotos(mapping, form, request, response, replyId);
			} else {
				forward = delPhoto(mapping, form, request, response, replyId);
			}
			break;
		default:
			forward = updateOfUploadEnd(mapping, form, request, response);
		}
		return forward;
	}
	/**
	 * 删除照片
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
	protected ActionForward delPhoto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String replyId) throws Exception {

		Result result = null;

		String pId = request.getParameter("pId");
		request.setAttribute("isDel", "true");

		// 删除图片
		PhotoInfoBean bean = (PhotoInfoBean) pMgt.getPhotoInfoById(pId)
				.getRetVal();
		if (bean != null) {
			String beanName = bean.getBeginName();
			if (StringUtils.isNotBlank(beanName)) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/album/img/"
						+ beanName);
				aFile.delete();
				aFile = new File(BaseEnv.FILESERVERPATH + "/album/img/small/"
						+ beanName);
				aFile.delete();
			}
		}
		result = pMgt.del_photo(pId);
		// 展示下一张图片
		String delPhoedShowId = request.getParameter("delPhoedShowId");
		request.setAttribute("delPhoedShowId", delPhoedShowId);
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if (StringUtils.isNotBlank(delPhoedShowId)) {
				return detail(mapping, form, request, response);
			} else {
				return showPhotosOfAlbum(mapping, form, request, response);
			}
		} else {
			EchoMessage.success().add(
					getMessage(request, "com.revert.to.failure")).setBackUrl(
					"/PhotoAction.do").setAlertRequest(request);

		}
		return getForward(request, mapping, "message");
	}
	/**
	 * 批量删除照片
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
	protected ActionForward delPhotos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String replyId) throws Exception {

		Result result = null;

		String[] pIds = request.getParameterValues("pho");

		for (String pId : pIds) {
			// 删除图片
			PhotoInfoBean bean = (PhotoInfoBean) pMgt.getPhotoInfoById(pId)
					.getRetVal();
			if (bean != null) {
				String beanName = bean.getBeginName();
				if (StringUtils.isNotBlank(beanName)) {
					File aFile = new File(BaseEnv.FILESERVERPATH
							+ "/album/img/" + beanName);
					aFile.delete();
					aFile = new File(BaseEnv.FILESERVERPATH
							+ "/album/img/small/" + beanName);
					aFile.delete();
				}
				result = pMgt.del_photo(pId);
			}
		}
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {

			return showPhotosOfAlbum(mapping, form, request, response);

		} else {
			EchoMessage.success().add(
					getMessage(request, "com.revert.to.failure")).setBackUrl(
					"/PhotoAction.do").setAlertRequest(request);

		}
		return getForward(request, mapping, "message");
	}

	/**
	 * 修改照片
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 * 
	 */
	protected ActionForward updatePhoto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String pId = request.getParameter("pId");

		String tempName = request.getParameter("tempName");
		String photoDesc = request.getParameter("photoDesc");

		if (StringUtils.isNotBlank(tempName)) {
			tempName = GlobalsTool.toChinseChar(tempName);
		}
		if (StringUtils.isNotBlank(photoDesc)) {
			photoDesc = GlobalsTool.toChinseChar(photoDesc);
		}
		String isCover = request.getParameter("isCover");
		String isSaveReading = request.getParameter("isSaveReading");

		PhotoInfoBean bean = (PhotoInfoBean) pMgt.load(pId).getRetVal();
		int newCover = Integer.parseInt(isCover);
		int oldCover = bean.getIsCover();
		if (newCover != oldCover) {
			bean.setIsCover(newCover);// 有变动修改
			if (newCover == 1) {
				// 此时让原来是封面的变成非封面
				String albumId = pMgt.getAIdByPId(pId);
				pMgt.updatePhoCoverByAId(albumId);
			}
		}

		if (StringUtils.isNotBlank(isSaveReading)) {
			bean.setIsSaveReading(isSaveReading);
		} else {
			bean.setIsSaveReading("1");
		}

		bean.setLastUpdateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		bean.setPhoneDesc(photoDesc);
		bean.setTempName(tempName);

		// 修改
		Result rs_newsInfo = pMgt.update(bean);
		if (rs_newsInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			String replyType = request.getParameter("replyType");
			if (StringUtils.equals("tophotoInfoList", replyType)) {
				return showPhotosOfAlbum(mapping, form, request, response);

			}
			return detail(mapping, form, request, response);
		} else {
			// 修改失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateErro"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}

	/**
	 * 移动照片mj
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 * 
	 */
	protected ActionForward moveToAlbum(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 目标相册
		String albumSelect = request.getParameter("albumSelect");
		AlbumBean album = (AlbumBean) amMgt.load(albumSelect).getRetVal();
		// 本相册
		// String albumId = request.getParameter("albumId");
		request.setAttribute("albumName", album.getName());
		request.setAttribute("albumId", albumSelect);

		String pId = request.getParameter("pId");
		PhotoInfoBean bean = (PhotoInfoBean) pMgt.load(pId).getRetVal();
		bean.setAlbum(album);
		bean.setLastUpdateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		// 修改
		Result rs_newsInfo = pMgt.update(bean);
		if (rs_newsInfo.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			return detail(mapping, form, request, response);
		} else {
			// 修改失败
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateErro"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}

	/**
	 * 上传成功后 保存修改信息（添加) mj
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
	protected ActionForward updateOfUploadEnd(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			// 获得 所有的 photo
			String phoId[] = request.getParameterValues("phoId");
			String isCoverPId = request.getParameter("isCover");
			for (String pId : phoId) {
				String tempName = request.getParameter(pId + "tempName");
				String phoneDesc = request.getParameter(pId + "phoneDesc");
				PhotoInfoBean bean = (PhotoInfoBean) pMgt.getPhotoInfoById(pId)
						.getRetVal();
				if (StringUtils.equals(pId, isCoverPId)) {
					bean.setIsCover(1);// 封面
					// 此时让原来是封面的变成非封面
					String albumId = pMgt.getAIdByPId(pId);
					pMgt.updatePhoCoverByAId(albumId);

				} else {
					bean.setIsCover(0);
				}
				bean.setLastUpdateTime(BaseDateFormat.format(new Date(),
						BaseDateFormat.yyyyMMddHHmmss));
				bean.setTempName(tempName);
				bean.setPhoneDesc(phoneDesc);
				// 修改信息
				pMgt.update(bean);
			}
			return showPhotosOfAlbum(mapping, form, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
					.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}

	/**
	 * 详细mj
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
	protected ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String pId = request.getParameter("pId");
		String albumName = request.getParameter("albumName");
		String replyType = request.getParameter("replyType");
		String albumName2 = (String) request.getAttribute("albumName");
		String type = request.getParameter("type");// 删除点评的时候传过来的

		String delPhoedShowId = (String) request.getAttribute("delPhoedShowId");
		if (StringUtils.isNotBlank(delPhoedShowId)) {
			pId = delPhoedShowId;
		}
		if (StringUtils.isNotBlank(albumName2)) {// 移动后跳转到此
			albumName = albumName2;
		} else {
			if (!"photoUpdate".equals(replyType)
					&& !"delPhotoReply".equals(type)
					&& !StringUtils.isNotBlank(delPhoedShowId)) {
				albumName = GlobalsTool.toChinseChar(albumName);
			}
		}
		String albumId = request.getParameter("albumId");
		String albumId2 = (String) request.getAttribute("albumId");
		if (StringUtils.isNotBlank(albumId2)) {// 移动后跳转到此
			albumId = albumId2;
		}
		int pageNo = getParameterInt("pageNo", request);
		int pageSize = getParameterInt("pageSize", request);
		LoginBean loginBean = this.getLoginBean(request);
		MOperation mop = (MOperation) loginBean.getOperationMap().get(
				"/AlbumQueryAction.do");
		request.setAttribute("add", mop.add()); // 增加权限
		request.setAttribute("del", mop.delete()); // 删权限
		request.setAttribute("upd", mop.update()); // 改权限
		System.out.println("pageNo = " + pageNo);
		// 所有点评
		// 是否只查看该用户的
		String auth = request.getParameter("auth");
		if (StringUtils.isNotBlank(pId)) {
			/*
			 * 评论暂时屏蔽 Result result = newsMgt.queryReplyNewsInfo(pId, auth,
			 * "createTime", "desc", pageNo <= 0 ? 1 : pageNo, pageSize <= 0 ?
			 * 100 : pageSize);// 代码公用 if (result.retCode ==
			 * ErrorCanst.DEFAULT_SUCCESS) { request.setAttribute("replayList",
			 * result.retVal); // if(result.realTotal >0){ //
			 * request.setAttribute("pageBar", pageBar(result, request)) ; // } }
			 */
			PhotoInfoBean bean = (PhotoInfoBean) pMgt.load(pId).getRetVal();
			request.setAttribute("photo", bean);
			// 报保存所有的相册 移动不要
			// Result rs = amMgt.queryBySql("desc", pageNo <= 0 ? 1 : pageNo,
			// pageSize <= 0 ? 100 : pageSize);// 方法二
			// if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			// List<AlbumBean> albumList = (List<AlbumBean>) rs.getRetVal();
			// request.setAttribute("albumList", albumList);
			// } else { // 失败
			// EchoMessage.error().add(
			// getMessage(request, "com.revert.to.failure"))
			// .setAlertRequest(request);
			// return getForward(request, mapping, "message");
			// }
			// 保存该相册里面的所有照片 修订后添加
			// 查询该相册对应的 所有的 照片信息
			Result result = pMgt.queryPhosOrderById(albumId, pId);
			List list = new ArrayList<PhotoInfoBean>();
			list.add(bean);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				list.addAll((List<PhotoInfoBean>) result.retVal);
				request.setAttribute("photos", list);
			}
			request.setAttribute("albumName", albumName);
			request.setAttribute("albumId", albumId);
			request
					.setAttribute("orderType", request
							.getParameter("orderType"));

		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");

		}
		return getForward(request, mapping, "photoInfoDetail");
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
	protected ActionForward showPhotosOfAlbum(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			// AlbumForm myForm = (AlbumForm)form;//未用到form
			int pageNo = getParameterInt("pageNo", request);
			int pageSize = getParameterInt("pageSize", request);
			request.setAttribute("pageNo", pageNo <= 0 ? 1 : pageNo);
			request.setAttribute("pageSize", pageSize <= 0 ? 100 : pageSize);
			String albumSelectId = request.getParameter("albumSelectId");// 当前相册
			request.setAttribute("albumSelectId", albumSelectId);
			String showPhotosOfAlbum = (String) request
					.getAttribute("showPhotosOfAlbum");
			if (StringUtils.isNotBlank(showPhotosOfAlbum)) {
				albumSelectId = showPhotosOfAlbum;
			}
			String orderType = request.getParameter("orderType");
			LoginBean loginBean = this.getLoginBean(request);
			MOperation mop = (MOperation) loginBean.getOperationMap().get(
					"/AlbumQueryAction.do");
			request.setAttribute("add", mop.add()); // 增加权限
			request.setAttribute("del", mop.delete()); // 删权限
			request.setAttribute("upd", mop.update()); // 改权限
			request.setAttribute("album", (AlbumBean) amMgt.load(albumSelectId)
					.getRetVal());
			// 查询该相册对应的 所有的 照片信息
			Result result = pMgt.queryPhotosByAlbumId(
					orderType == null ? "desc" : orderType, albumSelectId,
					pageNo <= 0 ? 1 : pageNo, pageSize <= 0 ? 100 : pageSize);
			if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
				request.setAttribute("photos", result.retVal);
				if (result.realTotal > 0) {
					request.setAttribute("orderType",
							orderType == null ? "desc" : orderType);
					request.setAttribute("pageBar", pageBar(result, request));
				}
			}
			String url = request.getRequestURI();
			String favoriteURL = java.net.URLEncoder.encode(url+"?noback=true&operation=4&albumSelectId="+ albumSelectId);
			request.setAttribute("favoriteURL", favoriteURL);
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
					.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "photoInfoList");// 跳转到当前相册页面

	}

	/**
	 * 删除点评mj
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
	protected ActionForward delReplyPhoto(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Result result = null;
		// 回复id
		String replyId = request.getParameter("replyId");
		result = newsMgt.delReplyNewsInfoById(replyId);
		// if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
		return detail(mapping, form, request, response);
		// }else{
		// EchoMessage.error().add(getMessage(request, "common.msg.delError"))
		// .setBackUrl("/PhotoAction.do?operation=5").setAlertRequest(request);
		// return getForward(request, mapping, "message");
		// }
	}

	/**
	 * 把java对象转化成的json数据,
	 * 
	 * @param obj
	 * @param response
	 * @throws IOException
	 */
	private void writeJson(Object obj, HttpServletResponse response)
			throws Exception {
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(obj);
		out.println(json);
		System.out.println(json);
		out.flush();
		out.close();
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
	protected ActionForward changeInfoByAjax(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String pId = request.getParameter("pId");
		// String albumName = request.getParameter("albumName");
		// //albumName = GlobalsTool.toChinseChar(albumName);
		// int pageNo = getParameterInt("pageNo", request);
		// int pageSize = getParameterInt("pageSize", request);
		// 所有点评
		if (StringUtils.isNotBlank(pId)) {
			Result result = null;
			// newsMgt.queryReplyNewsInfo(pId, auth,"createTime",
			// "desc", pageNo <= 0 ? 1 : pageNo, pageSize <= 0 ? 100
			// : pageSize);// 代码公用 暂时不需要
			// if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			PhotoInfoBean bean = (PhotoInfoBean) pMgt.load(pId).getRetVal();
			/*
			 * 另一种思路 可删除 PhotoInfoVo pVo = new PhotoInfoVo();
			 * pVo.setId(bean.getId());
			 * 
			 * pVo.setAgreeNum(bean.getAgreeNum());
			 * //pVo.setAlbum(bean.getAlbum());
			 * pVo.setBeginName(bean.getBeginName());
			 * pVo.setCreateTime(bean.getCreateTime());
			 * pVo.setIsCover(bean.getIsCover());
			 * pVo.setLastUpdateTime(bean.getLastUpdateTime());
			 * pVo.setPhoneDesc(bean.getPhoneDesc());
			 * pVo.setTempName(bean.getTempName());
			 * pVo.setUploadBy(bean.getUploadBy());
			 */
			AlbumForm vo = new AlbumForm();
			System.out.println(bean + "sssssssssss");
			bean.setAlbum(new AlbumBean());
			vo.setPhoto(bean);// photo
			// vo.setReplys((List<OANewsInfoReplyBean>)
			// result.retVal);//暂时不需要评论
			String json = gson.toJson(vo);
			request.setAttribute("msg", json);
			return getForward(request, mapping, "blank");
		} else {
			EchoMessage.error().add(getMessage(request, "common.msg.error"))
					.setRequest(request);
			return getForward(request, mapping, "message");
		}

		// } else {
		// EchoMessage.error().add(getMessage(request, "common.msg.error"))
		// .setRequest(request);
		// return getForward(request, mapping, "message");
		//
		// }

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
				"/AlbumQueryAction.do");
		if (operation == OperationConst.OP_UPDATE) {// 上传
			if (mop.add) {
				return null;
			}
		}
		return super.doAuth(req, mapping);
	}
}
