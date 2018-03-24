package com.koron.oa.albumTree;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.oa.bean.DirectorySetting;
import com.koron.oa.util.FileOperateUtil;
import com.koron.oa.util.FileUtils;
import com.koron.oa.util.ImgManagerUtil;
import com.menyi.aio.bean.BaseDateFormat;
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
 * Title: ��ҵ���
 * </p>
 * 
 * @Copyright: �������
 * 
 * @author ë��
 * 
 */
public class AlbumTreeAction extends MgtBaseAction {

	PhotoInfoMgt pMgt = new PhotoInfoMgt();
	AlbumTreeMgt amMgt = new AlbumTreeMgt();

	private static Gson gson;// mj
	private static final Integer MIN_PAGE_RECORDS = 20; //ÿҳ��С��ʾ����������
	static {
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}

	/**
	 * exe ��������ں���
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

		// ���ݲ�ͬ�������ͷ������ͬ��������
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
		// �ϴ�ҳ��
		case OperationConst.OP_IMPORT_PREPARE:
			forward = uploadPrepare(mapping, form, request, response);
			break;
		// �ϴ���ɺ�
		case OperationConst.OP_UPDATE_PREPARE:
			forward = pre_updatePhoto(mapping, form, request, response);
			break;
		// �޸�
		case OperationConst.OP_UPDATE:
			String updateType = getParameter("updateType", request);
			if ("updateNode".equals(updateType)) {
				forward = updateNode(mapping, form, request, response);
			}
			break;
		// ��ѯ
		case OperationConst.OP_QUERY:
			String requestType = request.getParameter("requestType");
			if ("ajax".equals(requestType)) {
				forward = showPhotosOfAlbum(mapping, form, request, response);
			} else if ("showPic".equals(requestType)) {
				forward = showPic(mapping, form, request, response);
			} else if ("toChangePhos".equals(requestType)) {
				forward = showPhosAjax(mapping, form, request, response);
			} else {
				forward = query(mapping, form, request, response);
			}
			break;
		// ɾ��
		case OperationConst.OP_DELETE:
			String delType = getParameter("delType", request);
			if ("delNode".equals(delType)) {
				forward = delNode(mapping, form, request, response);
			} else {
				forward = delFiles(mapping, form, request, response);
			}
			break;
		default:
			forward = query(mapping, form, request, response);
		}
		return forward;
	}

	/**
	 * ���ļ����Ͻ�������
	 * 
	 * @param files
	 *            file[]
	 * @param key
	 *            �����ֶ�
	 * @param sortType
	 *            asc or desc
	 * @return file[]
	 * @author mj
	 */
	@SuppressWarnings("unchecked")
	public File[] sortCollection(File[] files, int key, int sortType) {
		// SortCollection sort = new SortCollection(phos);
		// sort.addSortField("lastUpdateTime", SortCollection.ASC);
		// sort.sort();
		// filesȡ�õı�����ǰ�������
		Arrays.sort(files, new FileUtils.CompratorBySortType(sortType, key));
		return files;
	}

	/**
	 * ��ҳ
	 * 
	 * @param list
	 * @param currentPage
	 * @return
	 */
	public Object[] pageList(List<File> list, int currentPage, int pageRecords) {
		PageUtil<File> pu = new PageUtil<File>(list, pageRecords,this.MIN_PAGE_RECORDS);
		List<File> files = pu.getResult(currentPage);
		Object[] arr = new Object[4];
		arr[0] = files;
		arr[1] = pu;
		return arr;
	}

	/**
	 * ���Ѿ���ҳ������List[File]���з�װ
	 * 
	 * @param files
	 * @param isCompress
	 * @return
	 */
	public List<TreeVo> getFilesByPath(HttpServletRequest request,String path, List<File> files,
			boolean isCompress) {

		List<TreeVo> phos = new ArrayList<TreeVo>();
		try {
			if (isCompress) {
				long beginAll = System.currentTimeMillis();
				int j = 0;
				for (int i = 0, length = files.size(); i < length; i++) {
					File f = files.get(i);
					String fileName = f.getName();
					TreeVo bean = new TreeVo();
					bean.setTempName(fileName);
					bean.setLastUpdateTime(BaseDateFormat.format(new Date(f
							.lastModified()), BaseDateFormat.yyyyMMddHHmmss));
					bean.setFileSize(f.length());
					// String pathRep = path;
					long begin = System.currentTimeMillis();
					ImgManagerUtil.compressPicSim(request,path, f, "_small", fileName,
							133, 98, (float) 0.7);
					long end = System.currentTimeMillis();
					System.out.println("compress image :" + (end - begin));

					String p = f.getPath();
					int idx = p.lastIndexOf("\\");
					p = p.substring(0, idx);
					p = p.replaceAll("\\\\", "\\\\\\\\");
					bean.setTNo(j);
					bean.setPath(p);
					phos.add(bean);
					j++;
				}
				long endAll = System.currentTimeMillis();
				System.out.println("consume time  :" + (endAll - beginAll));
			} else {
				long beginAll = System.currentTimeMillis();
				int j = 0;
				for (int i = 0, length = files.size(); i < length; i++) {
					File f = files.get(i);
					String fileName = f.getName();
					TreeVo bean = new TreeVo();
					bean.setTempName(fileName);
					bean.setLastUpdateTime(BaseDateFormat.format(new Date(f
							.lastModified()), BaseDateFormat.yyyyMMddHHmmss));
					bean.setFileSize(f.length());
					// String pathRep = path;
					long begin = System.currentTimeMillis();
					Image img = ImageIO.read(f);
					if (img == null) {//�ų���ͼƬ��ͼƬ��ʽ�ļ� ��1.jpg��Ϊд��
						continue;
					}
					bean.setWidth(img.getWidth(null));
					bean.setHeight(img.getHeight(null));
					long end = System.currentTimeMillis();
					System.out.println("ImageIO.read :" + (end - begin));
					// pathRep = pathRep.replaceAll("\\\\",
					// "\\\\\\\\");
					String p = f.getPath();
					int idx = p.lastIndexOf("\\");
					p = p.substring(0, idx);
					p = p.replaceAll("\\\\", "\\\\\\\\");
					bean.setTNo(j);
					bean.setPath(p);
					phos.add(bean);
					j++;
				}
				long endAll = System.currentTimeMillis();
				System.out.println("consume time  :" + (endAll - beginAll));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return phos;
	}

	public List<TreeVo> getFilesByPath(HttpServletRequest request,String path, boolean isCompress) {
		List<TreeVo> phos = new ArrayList<TreeVo>();
		try {
			if (isCompress) {
				long beginAll = System.currentTimeMillis();
				File file = new File(path);
				String formatOfImgs = ImgManagerUtil
						.getAllowFormatByType("image");
				File[] files = file.listFiles();
				int j = 0;
				for (int i = 0, length = files.length; i < length; i++) {
					File f = files[i];
					if (f.isFile()) {
						String fileName = f.getName();
						String fileExtend = fileName.substring(
								fileName.lastIndexOf(".") + 1).toLowerCase();
						if (Arrays.<String> asList(formatOfImgs.split(","))
								.contains(fileExtend)) {
							TreeVo bean = new TreeVo();
							bean.setTempName(fileName);
							bean.setLastUpdateTime(BaseDateFormat.format(
									new Date(file.lastModified()),
									BaseDateFormat.yyyyMMddHHmmss));
							bean.setFileSize(f.length());
							// String pathRep = path;

							long begin = System.currentTimeMillis();
							ImgManagerUtil.compressPicSim(request,path, f, "_small",
									fileName, 133, 98, (float) 0.7);
							long end = System.currentTimeMillis();
							System.out.println("compress image :"
									+ (end - begin));

							String p = f.getPath();
							int idx = p.lastIndexOf("\\");
							p = p.substring(0, idx);
							p = p.replaceAll("\\\\", "\\\\\\\\");
							bean.setTNo(j);
							bean.setPath(p);
							phos.add(bean);
							j++;
						}
					}
				}
				long endAll = System.currentTimeMillis();
				System.out.println("consume time  :" + (endAll - beginAll));
			} else {

				long beginAll = System.currentTimeMillis();
				File file = new File(path);
				String formatOfImgs = ImgManagerUtil
						.getAllowFormatByType("image");
				File[] files = file.listFiles();
				int j = 0;
				for (int i = 0, length = files.length; i < length; i++) {
					File f = files[i];
					if (f.isFile()) {
						String fileName = f.getName();
						String fileExtend = fileName.substring(
								fileName.lastIndexOf(".") + 1).toLowerCase();
						if (Arrays.<String> asList(formatOfImgs.split(","))
								.contains(fileExtend)) {
							TreeVo bean = new TreeVo();
							bean.setTempName(fileName);
							bean.setLastUpdateTime(BaseDateFormat.format(
									new Date(file.lastModified()),
									BaseDateFormat.yyyyMMddHHmmss));
							bean.setFileSize(f.length());
							// String pathRep = path;

							long begin = System.currentTimeMillis();
							Image img = ImageIO.read(f);
							bean.setWidth(img.getWidth(null));
							bean.setHeight(img.getHeight(null));
							long end = System.currentTimeMillis();
							System.out.println("ImageIO.read :" + (end - begin));
							// pathRep = pathRep.replaceAll("\\\\",
							// "\\\\\\\\");
							String p = f.getPath();
							int idx = p.lastIndexOf("\\");
							p = p.substring(0, idx);
							p = p.replaceAll("\\\\", "\\\\\\\\");
							bean.setTNo(j);
							bean.setPath(p);
							phos.add(bean);
							j++;
						}
					}
				}

				long endAll = System.currentTimeMillis();
				System.out.println("consume time  :" + (endAll - beginAll));

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return phos;
	}

	/**
	 * ��ʾ�������������е���Ƭ mj
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	protected ActionForward showPhotosOfAlbum(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String pId = getParameter("id", request);
			String dateId=getParameter("dateId",request);
			if (StringUtils.isBlank(pId)) {
				EchoMessage.error().add(getMessage(request, "news.not.find"))
						.setNotAutoBack().setAlertRequest(request);
				return getForward(request, mapping, "message");
				//return getForward(request, mapping, "photoInfoList");// ��ת����ǰ���ҳ��
			}
			
			AlbumTreeForm myForm = (AlbumTreeForm) form;
			int key = myForm.getKey();
			int sortType = myForm.getSortType();

			int currentPage = myForm.getPageNo();
			int pageRecords = myForm.getPageSize();
			if (key == 0 && sortType == 0) {// ͨ�����ӷ�ʽ���͵�����ajax

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
			File[] files = FileOperateUtil.getFilesByPath(pId, "image",false);
			
			if (key != 0) {
				// �ǵ�һ�� ��һ�� ��������
				files = sortCollection(files, key, sortType);
			} else {
				pageRecords = this.MIN_PAGE_RECORDS;
			}
			pageRecords = pageRecords == 15 ? this.MIN_PAGE_RECORDS:pageRecords;
			
			
			Long endTime = System.currentTimeMillis();
			System.out.println("�����ʱ��" + (endTime - begTime));

			Long begPage = System.currentTimeMillis();
			List<File> list = new ArrayList<File>();
			
			for (File f : files) {
				if (f != null) {
					list.add(f);
				}

			}
			Object[] obj = pageList(list, currentPage, pageRecords);
			list = (List<File>) obj[0];
			PageUtil page = (PageUtil) obj[1];

			Long endPage = System.currentTimeMillis();
			System.out.println("��ҳ��ʱ��" + (endPage - begPage));

			List<TreeVo> phos = getFilesByPath(request,pId, list, true);
			request.setAttribute("key", key == 0 ? 3 : key);
			request.setAttribute("sortType", sortType == 0 ? 1 : sortType);
			
			Result rs = new Result();
			rs.setPageNo(page.getCurrentPage());
			rs.setPageSize(page.getPageRecords());
			rs.setRealTotal(page.getTotalRecord());
			rs.setTotalPage(page.getTotalPage());
			request.setAttribute("pageBar", pageBars(rs, request));
			request.setAttribute("page", page);
			request.setAttribute("photos", phos);
			request.setAttribute("parPath", pId);
			
			pId = pId.replaceAll("\\\\", "\\\\\\\\");
			request.setAttribute("pId", pId);
			
			//��pId���д��� ������ʾ��Ŀ¼��ԭʼ·��
			LoginBean loginBean = this.getLoginBean(request);
			String curNodeName = getParameter("curNodeName", request);
			if (StringUtils.isNotBlank(curNodeName)) {
				curNodeName = GlobalsTool.toChinseChar(curNodeName);
			}
			
			String viewPath = amMgt.replaceStr(pId, loginBean,curNodeName);
			int i = viewPath.lastIndexOf("\\\\");
			String view = viewPath.substring(i+2);
			
			if (StringUtils.isBlank(view)) {
				viewPath = viewPath.replace("\\", "");
				
			}
			request.setAttribute("viewPath", viewPath);
			MOperation mop = (MOperation) loginBean.getOperationMap().get(
					"/AlbumTreeQueryAction.do");
			
			
			
			request.setAttribute("add", mop.add()); // ����Ȩ��
			request.setAttribute("del", mop.delete()); // ɾȨ��
			request.setAttribute("upd", mop.update()); // ��Ȩ��
			
			
			String  userId=loginBean.getId();
			String 	deptId=loginBean.getDepartCode();
			String  groupId=loginBean.getGroupId();
			List<DirectorySetting> targetNetDisk=amMgt.getDepartment(dateId);
			String  downLoadUserIds=null;
			String 	downLoadDepteIds=null;
			String  downLoadgroupIds=null;
			if(targetNetDisk!=null && targetNetDisk.size()>0 ){
				downLoadUserIds=targetNetDisk.get(0).getDownLoadUserId();
				downLoadDepteIds=targetNetDisk.get(0).getDownLoadDeptOfClassCode();
			    downLoadgroupIds=targetNetDisk.get(0).getDownLoadEmpGroup();
			}
			
			/*�жϿ�ʼ:�жϵ�ǰ��¼�û��Ƿ������ظ�����Ȩ��:onDownΪTrue����Ȩ��*/
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
			
			if("1".equals(userId))  //����ǹ���Ա������Ȩ��
				onDown=true;
			
			
			/*���û��ѡ��������Ȩ�Ĳ��ź͸��ˣ���Ĭ�Ͽ��Կ�����¼��Ϣ���û���ӵ�����ظ�����Ȩ��*/
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
		
			/*�жϽ���*/
			request.setAttribute("onDown", onDown);
			
			
			MOperation mopSetting = (MOperation) loginBean.getOperationMap().get(
			"/DirectorySettingAlbumQueryAction.do");
			if (mopSetting != null) {
				request.setAttribute("query", mopSetting.query());
			}

		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
					.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "photoInfoList");// ��ת����ǰ���ҳ��

	}

	/**
	 * չʾͼƬ
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
	@SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
	protected ActionForward showPic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String pageName = getParameter("pageName", request);
		String onDown=getParameter("onDown",request);
		try {
			String path = getParameter("path", request);
			path = GlobalsTool.toChinseChar(path);

			String tempName = getParameter("tempName", request);
			tempName = GlobalsTool.toChinseChar(tempName);

			int key = getParameterInt("keyURL", request);
			int sortType = getParameterInt("sortTypeURL", request);
			int currentPage = getParameterInt("pageNoURL", request);
			int pageRecords = getParameterInt("pageSizeURL", request);

			if ("open.jsp".equals(pageName)) {
				// �˿� ��Ҫ�õ��� ·���µ����е�ͼƬ ��װΪ����
				int idx = path.lastIndexOf("\\");
				String parpath = path.substring(0, idx);
				File[] files = FileOperateUtil.getFilesByPath(parpath, "image",false);
				// �ǵ�һ�� ��һ�� �������� ���Բ����� ֻҪ�ͻ��Ͽ� ���Ч�� ��
				files = sortCollection(files, key, sortType);
				List<File> list = new ArrayList<File>();
				for (File f : files) {
					if (f != null) {
						list.add(f);
					}
				}
				request.setAttribute("phos",
						getFilesByPath(request,parpath, (List<File>) pageList(list,
								currentPage, pageRecords)[0], false));
				request.setAttribute("parpath", parpath);// ���游Ŀ¼ download
				// need
			} else {
				request.setAttribute("keyURL", key);
				request.setAttribute("sortTypeURL", sortType);
				request.setAttribute("pageNoURL", currentPage);
				request.setAttribute("pageSizeURL", pageRecords);
				path = path + "\\" + tempName;
			}
			// path = path.replaceAll("\\\\", "\\\\\\\\");
			request.setAttribute("pId", path);
			request.setAttribute("tempName", tempName);
			LoginBean loginBean = this.getLoginBean(request);
			MOperation mop = (MOperation) loginBean.getOperationMap().get(
			"/AlbumTreeQueryAction.do");
			request.setAttribute("add", mop.add()); // ����Ȩ��
			request.setAttribute("del", mop.delete()); // ɾȨ��
			request.setAttribute("upd", mop.update()); // ��Ȩ��
			request.setAttribute("onDown", onDown);//����Ȩ��
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
					.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

		if ("open.jsp".equals(pageName)) {
			return getForward(request, mapping, "open_control");
		} else {
			return getForward(request, mapping, "open");
		}

	}

	/**
	 * ����ɾ����Ƭ
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

		String pho = request.getParameter("pho");
		String path = request.getParameter("path");
		String[] pIds = pho.split(",");
		for (String pId : pIds) {
			File file = new File(path + "/" + pId);
			if (file.exists()) {
				file.delete();
			}
		}
		request.setAttribute("msg", "true");
		return getForward(request, mapping, "blank");
	}

	

	/**
	 * �����ҳ mj
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
		"/AlbumTreeQueryAction.do");
		
		
		String moUrl = "/DirectorySettingAlbumQueryAction.do";
		MOperation mopSetting = (MOperation) loginBean.getOperationMap().get(
				moUrl);
		request.setAttribute("query",mopSetting == null ? false : mopSetting.query() );
		
		request.setAttribute("add", mop.add()); // ����Ȩ��
		request.setAttribute("del", mop.delete()); // ɾȨ��
		request.setAttribute("upd", mop.update()); // ��Ȩ��
		
		return getForward(request, mapping, "albumTreeList");
	}

	/**
	 * �ϴ�ͼƬǰ mj
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
		// ��·��������ϴ�ҳ��
		String path = getParameter("path", request);
		if (StringUtils.isNotBlank(path)) {
			path = GlobalsTool.toChinseChar(path);
		}
		path = path.replaceAll("\\\\", "\\\\\\\\");
		request.setAttribute("path", path);
		return getForward(request, mapping, "uploadTreePhoto");
	}

	/**
	 * �ϴ�ͼƬ�� mj
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
		String picFiles = getParameter("picFiles", request);// �ϴ�����Ƭ

		picFiles = picFiles == null ? "" : picFiles;
		// ��ɾ���ĸ���
		String delFiles = request.getParameter("delPicFiles");
		// ��ɾ�������У�����ڸ�����Ҳ���ڣ������û�ɾ������������ӵģ����Բ�������ɾ��
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
		// ����Ψһ�û���֤��������ظ���½�ģ��������û��߳�ǰ�����û�
		if (!OnlineUserInfo.checkUser(req)) {
			// ���߳�
			EchoMessage.error().setAlertRequest(req);
			return getForward(req, mapping, "doubleOnline");
		}

		int operation = getOperation(req);
		MOperation mop = (MOperation) loginBean.getOperationMap().get(
				"/AlbumTreeQueryAction.do");
		if (operation == OperationConst.OP_IMPORT_PREPARE
				|| operation == OperationConst.OP_UPDATE_PREPARE) {// �ϴ�
			if (mop.add) {
				return null;
			}
		}
		return super.doAuth(req, mapping);
	}

	/**
	 * ajax����jsp��Ϣ
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
		String name = request.getParameter("albumName"); // �������
		if (StringUtils.isNotBlank(name)) {
			name = GlobalsTool.toChinseChar(name);
		}
		int count = amMgt.getQueryCount("tblAlbum", "name", name);
		String json = gson.toJson(count);
		request.setAttribute("msg", json);
		return getForward(request, mapping, "blank");
	}

	/**
	 * ��ʾ�������������е���Ƭ mj
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

			File[] files = FileOperateUtil.getFilesByPath(pId, "image",false);
			
			files = sortCollection(files, key, sortType);
			List<File> list = new ArrayList<File>();
			for (File f : files) {
				if (f != null) {
					list.add(f);
				}

			}
			
			List<TreeVo> phos = getFilesByPath(request,pId, (List<File>)pageList(list, currentPage, pageRecords)[0], true);
			
			String json = gson.toJson(phos);
			request.setAttribute("msg", json);
		
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
					.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		return getForward(request, mapping, "blank");

	}

	/**
	 * ����½ڵ㣨Ŀ¼��
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

		String path = getParameter("path", request); // ��Ҫ��ӵ�Ŀ¼
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
	 * �޸��½ڵ㣨Ŀ¼��
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

		String oldName = getParameter("oldName", request); // ��Ҫ��ӵ�Ŀ¼
		String newName = getParameter("newName", request);

		String pId = getParameter("pId", request);
		String result = null;
		if (StringUtils.isNotBlank(pId)) {
			File file = null;
			pId = GlobalsTool.toChinseChar(pId);
			oldName = GlobalsTool.toChinseChar(oldName);
			newName = GlobalsTool.toChinseChar(newName);
			file = new File(pId );
			String parPath = pId.substring(0,pId.lastIndexOf("\\"));
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
	 * ɾ���½ڵ㣨Ŀ¼��
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
	protected ActionForward delNode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String path = getParameter("path", request);
		String result = null;

		if (StringUtils.isNotBlank(path)) {
			path = GlobalsTool.toChinseChar(path);
			path = path.substring(0,path.lastIndexOf("\\"));
			FileOperateUtil.delAllFile(path); // ɾ����������������
			File file = new File(path);
			if (file.exists()) {
				if (file.delete()) {
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
}
