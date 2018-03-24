package com.koron.oa.publicMsg.newordain;

import java.io.File;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;

import com.koron.oa.bean.OAOrdainBean;
import com.koron.oa.bean.OAOrdainGroupBean;
import com.koron.oa.publicMsg.newordain.OAOrdainMgt;
import com.koron.oa.publicMsg.newsInfo.OANewsMgt;
import com.koron.oa.util.AttentionMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.EmployeeBean;


import com.menyi.aio.web.advice.AdviceMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;

import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.IDGenerater;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.NotifyFashion;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.PublicMgt;
/**
 * <p>
 * Title:�����ƶ�
 * <p>
 * <p>
 * Description:
 * </p>
 * 
 * @Date:2012-06-26
 * @CopyRight:�������
 * @Author:������
 */
public class OAOrdainAction extends MgtBaseAction {

	String username;
	OAOrdainMgt mgt = new OAOrdainMgt();
	OANewsMgt newsmgt=new OANewsMgt();
	AdviceMgt amgt=new AdviceMgt();

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
		// ��������
		case OperationConst.OP_ADD:
			String thetype=this.getParameter("thetype", request);
			if("getShouquan".equals(thetype))
				forward=getShouquan(mapping,request,response);
			else
				forward = add(mapping, form, request, response);
			break;
		// ���������ƶ�ǰ��׼��
		case OperationConst.OP_ADD_PREPARE:
			forward = addPrepare(mapping, form, request, response);
			break;
		// �޸�ǰ��׼��
		case OperationConst.OP_UPDATE_PREPARE:
			forward = updatePrepare(mapping, form, request, response);
			break;
		// �޸�
		case OperationConst.OP_UPDATE:
			forward = update(mapping, form, request, response);
			break;
		// ����������ѯ
		case OperationConst.OP_QUERY:
			String type=request.getParameter("dealType");
			String flag = request.getParameter("flag");//����ʶ��Ŀ¼�������õ����¼� 1.��ʾ����,����Ҫɸѡ 2��ʾ�¼�,��Ҫɸѡ
			if(flag ==null || "".equals(flag)){
				
				flag ="1";
			}
			request.setAttribute("flag", flag);
			
			if("ordainGroup".equals(type)){
				forward= queryGroup(mapping,form,request,response);
			}else if("addGroup".equals(type)){
				forward = addPrepareGroup(mapping, form, request, response);
			}else if("add".equals(type)){
				forward = addGroup(mapping, form, request, response);
			}else if("updateGroup".equals(type)){
				forward = updatePrepareGroup(mapping, form, request,response);
			}else if("update".equals(type)){
				forward = updateGroup(mapping, form, request, response);
			}else if("del".equals(type)){
				String check=this.getParameter("check", request);
				if("true".equals(check))
					forward=checkGroup(mapping,request,response);
				else
					forward = del(mapping, form, request, response);
			}else{		
				forward = query(mapping, form, request, response);
			}
			break;
		// �����ƶ���ϸ
		case OperationConst.OP_DETAIL:
			forward = detail(mapping, form, request, response);
			break;
		// ɾ������
		case OperationConst.OP_DELETE:
			forward = delete(mapping, form, request, response);
			break;
		//Ĭ��
		default:
			String opentype=getParameter("type",request);
			if(null != opentype && !opentype.equals("") )
				forward = toTree(mapping, form, request, response);
			else
				forward = frame(mapping, form, request, response); // �����ƶ���ҳ
		}
		return forward;
	}
   /**
    * ���ؿ��
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
    protected ActionForward frame(ActionMapping mapping,
                                                ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws
            Exception {
        return getForward(request, mapping, "frame");
    }
    
	/**
	 * �������ҳ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    protected ActionForward toTree(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
    	
        LoginBean loginBean=this.getLoginBean(request);
		//��õ�½�û����ڲ���	
		String depts=loginBean.getDepartCode();
		
		//��ȡ��½�û�ID
		String userId=loginBean.getId();
    	
    	   Result rs=mgt.queryFolderByUserId(userId,depts);
           if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
           	ArrayList list=(ArrayList)rs.retVal;
           	String folderTree="";
           	String folderCode="";
           	for(int i=0;i<list.size();i++){
           		
           		String []obj=(String[])list.get(i);     
           		folderCode += "@acute;"+obj[1]+"@acute;";
				if(i != list.size()-1){
					folderCode += ",";
				}
           		if(obj[1].length()==5){
           			folderTree+=this.folderTree(list,obj[2], obj[1],obj[4],obj[0]);
           		} 
           	}
           	request.setAttribute("fCode", folderCode);
           	request.setAttribute("result",folderTree); 
           }
        return getForward(request, mapping, "tree");
    }
    /**
     * ���������ڵ���
     * @param list
     * @param folderName
     * @param classCode
     * @param isCatalog
     * @param folderId
     * @return
     */
    private String folderTree(ArrayList list,String folderName,String classCode,String isCatalog,String folderId){
       	String folderTree="";
       	if(isCatalog!=null&&isCatalog.equals("1")){
       		folderTree="<li><span><a style=\"color:#000000\" href=\"javascript:goFarme('/OAnewOrdain.do?operation="+OperationConst.OP_QUERY+"&selectType=type&groupCode="+classCode+"')\" onclick=\"clearContent();insertType('"+classCode+"');\" ><font id=\"type_"+classCode+"\" style=\"color: black\">"+folderName+"</font></a><font  id=\"_1\"></font></span><ul>";
       		for(int i=0;i<list.size();i++){
       			String []obj=(String[])list.get(i);    
       			if(obj[1].substring(0,obj[1].length()-5).equals(classCode)){
       				folderTree+=this.folderTree(list,obj[2], obj[1],obj[4],obj[0]);
       			}
       		}
       		folderTree+="</ul></li>";
       	}else{
       		folderTree="<li><span><a style=\"color:#000000\" href=\"javascript:goFarme('/OAnewOrdain.do?operation="+OperationConst.OP_QUERY+"&selectType=type&groupCode="+classCode+"')\" onclick=\"clearContent();insertType('"+classCode+"');\" ><font id=\"type_"+classCode+"\" style=\"color: black\">"+folderName+"</font></a><font  id=\"_1\"></font></span></li>";
       	}
       	
       	return folderTree;
    }
	/**
	 * �����ƶ�������ҳ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    protected ActionForward query(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
        String selectId=this.getParameter("groupCode", request);   
		OAOrdainSearchForm ordainSearchForm = (OAOrdainSearchForm) form;
		String type=this.getParameter("selectType", request);
		
		String insertPla=this.getParameter("insertPlace", request);
		if(!"".equals(insertPla) && null!=insertPla){
			request.setAttribute("insertPlace", insertPla);
		}
		
		
		//�ж��Ƿ��ǹؼ��ֺ�������ѯ���룬����ǣ���selectType��ֵ
		if("keyword".equals(type) || "gaoji".equals(type))
			ordainSearchForm.setSelectType(type);
		
		//�������������ѡ�����
		String gName= this.getParameter("groupName", request);
		request.setAttribute("groupName", gName);
		
		//����ǹؼ�����������ȡ�ؼ�������
		
		if("keyword".equals(type) && null!=this.getParameter("keywordVal", request)){
			String kw = this.getParameter("keywordVal", request);
			kw = GlobalsTool.toChinseChar(kw);
			ordainSearchForm.setKeyWord(kw);
			}
			
		
		//�ж��Ƿ��������󷵻��б�ҳ��
		if("addreturn".equals(type)){
			ordainSearchForm.setSelectType("type");
			ordainSearchForm.setSelectId(this.getParameter("selectId", request));
		}
		
		//����ǹ����ƶ��������ѯ������ز�������SearchFrom	
		if("type".equals(type)){
			ordainSearchForm.setSelectType(type);
			ordainSearchForm.setSelectId(selectId);
		}
		
		/*�ж��Ƿ����״�ѡ���ѯ*/
		if(null == ordainSearchForm.getHavingType() && !"gaoji".equals(type)){
			ordainSearchForm.setHavingType(ordainSearchForm.getSelectType());
			ordainSearchForm.setHavingId(ordainSearchForm.getSelectId());
		}
		
		/*begin   ��ȡ���κ��ϴ�ѡ��Ĳ�ѯ���ͣ�ʱ��ؼ��֡������ƶ����ͣ�*/
		String x=ordainSearchForm.getSelectType();
		String y=ordainSearchForm.getHavingType();
		String a=ordainSearchForm.getSelectId();
		String b=ordainSearchForm.getHavingId();
		
		/*end*/
		
		//��¼��ҳ��ѡ��������ѯ���ͣ�Ĭ�Ͻ�ҳ����Ϊ��һҳ
		if(x!=null && y!=null && a!=null && b!=null && (!y.equals(x) || !a.equals(b)) && ordainSearchForm.getPageNo()!=1){
			
			ordainSearchForm.setPageNo(1);
			ordainSearchForm.setHavingType(x);
			ordainSearchForm.setHavingId(a);
		}
		
        //�ж��Ƿ��Ǵ�ϵͳģ����룬����ǣ������ǰ��Form����
		if ("menu".equals(getParameter("src", request))) {
			ordainSearchForm = new OAOrdainSearchForm();
			request.getSession().setAttribute("OrdainSearchForm", null);
		}
		LoginBean loginBean = this.getLoginBean(request);

		// ��ȡ��ǰ�û��Դ�ģ���Ȩ��
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/OAnewOrdainAction.do");
		request.setAttribute("add", mop.add()); // ����Ȩ��
		request.setAttribute("delete", mop.delete()); // ɾ��Ȩ��
		request.setAttribute("update", mop.update()); // �޸�Ȩ��
		request.setAttribute("query", mop.query());   //�鿴Ȩ��
		
		 //��õ�½�û����ڷ���
		String empGroup=loginBean.getGroupId();
		
		//��õ�½�û����ڲ���	
		String depts=loginBean.getDepartCode();
		
		//��ȡ��½�û�ID
		String userId=loginBean.getId();
		
		if("indexType".equals(this.getParameter("opentype", request)) || "returnindex".equals(this.getParameter("opentype", request)))
			ordainSearchForm.setSelectType(this.getParameter("opentype", request));
		
		Result result=mgt.queryOrdain(ordainSearchForm,null, userId,depts,null);
		
		if(null!=this.getParameter("selectOrdain", request))	
			request.setAttribute("selectOrdain", this.getParameter("selectOrdain", request));
		
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("ordainList", result.retVal);
			request.setAttribute("pageBar", pageBar(result, request));
			request.setAttribute("thetype", "queryindex");
		}
		return getForward(request, mapping, "ordainIndex");

	}
	/**
	 * ���������ƶ�����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		OAOrdainForm ordainForm = (OAOrdainForm) form;
		/* ��õ�½��ID */
		String userId = this.getLoginBean(request).getId();
		String loginname =this.getLoginBean(request).getEmpFullName();
	
		/* ����ʱ�� */
		String createTime = BaseDateFormat.format(new java.util.Date(),
				BaseDateFormat.yyyyMMddHHmmss);
		OAOrdainBean oaOrdain= new OAOrdainBean();
		read(ordainForm, oaOrdain);
		
		oaOrdain.setAccessories(this.getParameter("attachFiles", request));
		oaOrdain.setId(IDGenerater.getId());
		oaOrdain.setCreateBy(userId);
		oaOrdain.setCreateTime(createTime);
		oaOrdain.setLastupDateBy(userId);
		oaOrdain.setLastupDateTime(createTime);
		//��ȡ���ѷ�ʽ
		String[] wakeType = request.getParameterValues("wakeUpMode");

		// ��ɾ���ĸ���
		String delFiles = getParameter("delPicFiles",request);
		// ��ɾ�������У�����ڸ�����Ҳ���ڣ������û�ɾ������������ӵģ����Բ�������ɾ��
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0
					&& ordainForm.getAccessories().indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/news/" + del);
				aFile.delete();
			}
		}
		// ���ѷ�ʽ
		String wakeupType = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeupType += str + ",";
			}
		}
		oaOrdain.setWakeupType(wakeupType);
		Result result = new Result();	
		result = mgt.addOrdain(oaOrdain);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
		
				//��ȡ֪ͨ����
				String popedomUserIds=mgt.getPopedomUser(oaOrdain,userId);
				// ������ѷ�ʽ
				String title = loginname+GlobalsTool.getMessage(request, "oa.common.addbylaw")
						+ oaOrdain.getOrdainTitle();
				String url = "/OAnewOrdain.do";
				String favoriteURL = url + "?noback=true&operation=5&ordainId="
						+ oaOrdain.getId() + "&isEspecial=1";
				String content = "<a href=javascript:mdiwin('" + favoriteURL
						+ "','"
						+ GlobalsTool.getMessage(request, "oa.common.bylaw")
						+ "')>" + title + getMessage(request, "com.click.see")
						+ "</a>"; // ����
				//���û�������ѷ�ʽ
				if (wakeType != null && wakeType.length > 0) {
					for (String type : wakeType) {
						new Thread(new NotifyFashion(userId, title, content,
								popedomUserIds, Integer.parseInt(type), "yes",
								oaOrdain.getId(), null, null, "ordain")).start();
					}
				}
	
				// ȫ���ɹ���	
				EchoMessage.success().add(
						getMessage(request, "common.msg.addSuccess")).setBackUrl("/OAnewOrdain.do?operation=4&selectType=addreturn&selectId="+oaOrdain.getGroupId()).setAlertRequest(request);
				
			return getForward(request, mapping, "message");
		} else {
			// ���ʧ��
			EchoMessage.error().add(
					getMessage(request, "common.msg.addFailture"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
	}

	/**
	 * ���������ƶ�ǰ��׼��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		if("type".equals(this.getParameter("selectType", request))){
			Result  group=mgt.queryFolderByCode(this.getParameter("selectId", request));		
			List<OAOrdainGroupBean> obean=(List<OAOrdainGroupBean>) group.retVal;		
			request.setAttribute("selectType", obean.get(0).getGroupName());
			request.setAttribute("selectId",obean.get(0).getClassCode());
		}
		
	    
		return getForward(request, mapping, "to_addOrdain");
	}

	/**
	 * ɾ�������ƶ�����
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String[] ordainIds = getParameter("ordainId", request).split(",");
		OAOrdainBean oabean=(OAOrdainBean)mgt.loadOrdain(ordainIds[0]).getRetVal();
		OAOrdainSearchForm ordainSearchForm = (OAOrdainSearchForm) form;
		LoginBean loginBean=this.getLoginBean(request);
		//��õ�½�û����ڷ���
		String empGroup=loginBean.getGroupId();
		
		//��õ�½�û����ڲ���	
		String depts=loginBean.getDepartCode();
		
		//��ȡ��½�û�ID
		String userId=loginBean.getId();
		
        //��һ��
		String selectOrdain="";
		Result preResult = mgt.queryOrdain(ordainSearchForm,oabean.getLastupDateTime(),userId,depts,"detailPre");
		if (preResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			List<OAOrdainBean> preOrdain = (List<OAOrdainBean>) preResult.getRetVal();
			if (preOrdain != null && preOrdain.size() > 0) {
				selectOrdain= preOrdain.get(preOrdain.size()-1).getId();				
			}
		}
		
		Result result = mgt.deleteOrdain(ordainIds);
		
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			
			//ɾ����ɾ����¼��ص�֪ͨ��Ϣ
			String delIds=","+getParameter("ordainId", request)+",";
			amgt.deleteByRelationId(delIds, "");
			
			// ɾ���ɹ�
			EchoMessage.success().add(
					getMessage(request, "common.msg.delSuccess")).setBackUrl(
					"/OAnewOrdain.do?operation=4&selectOrdain="+selectOrdain).setAlertRequest(request);
			return getForward(request, mapping, "message");
		} else {
			// ɾ��ʧ��
			EchoMessage.error().add(getMessage(request, "common.msg.delError"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}
	
	/**
	 * �޸�ǰ��׼��
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward updatePrepare(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// ��ù����ƶ�ID
		String ordainId = getParameter("ordainId", request);
		// ���ݹ����ƶ�id��ѯ�����ƶ�
		Result ordain = mgt.loadOrdain(ordainId);
		OAOrdainBean oaordainBean = (OAOrdainBean) ordain.getRetVal();
		BaseEnv.log.error("classCode��ֵ:"+oaordainBean.getGroupId());
		Result groupResult =mgt.queryFolderByCode(oaordainBean.getGroupId());
		
		String[] wakeUpType = null;// ���ѷ�ʽ
		if (oaordainBean.getWakeupType() != null
				&& !"".equals(oaordainBean.getWakeupType())) {
			wakeUpType = oaordainBean.getWakeupType().split(",");
		}
		/*��ȡ֪ͨ����*/
		List<EmployeeBean> targetUsers =newsmgt.getEmployee(oaordainBean.getPopedomUserIds());
		List<Object> targetDept = newsmgt.getDepartment(oaordainBean.getPopedomDeptIds());
		List<Object> listEmpGroup = newsmgt.getEmpGroup(oaordainBean.getPopedomEmpGroupIds());

		request.setAttribute("ownOrdain", oaordainBean);
		request.setAttribute("targetUsers", targetUsers);
		request.setAttribute("targetDept", targetDept);
		request.setAttribute("targetEmpGroup", listEmpGroup);
		request.setAttribute("wakeUpType", wakeUpType); // ��ʾ��ʽ
		request.setAttribute("group",groupResult.retVal);//������
		//�ж��Ƿ��Ǵ���ϸҳ����е��޸Ĳ���
		request.setAttribute("position", this.getParameter("position", request));
		return getForward(request, mapping, "to_updateOrdain");
	}

	/**
	 * �޸Ĺ����ƶ�
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		OAOrdainForm ordainForm = (OAOrdainForm) form;
		/* ��õ�½��ID */
		String userId = this.getLoginBean(request).getId();
		String loginname =this.getLoginBean(request).getEmpFullName();
		String[] wakeType = request.getParameterValues("wakeUpMode");
		// ��ɾ���ĸ���
		String delFiles = getParameter("delPicFiles",request);
		// ��ɾ�������У�����ڸ�����Ҳ���ڣ������û�ɾ������������ӵģ����Բ�������ɾ��
		String[] dels = delFiles == null ? new String[0] : delFiles.split(";");
		for (String del : dels) {
			if (del != null && del.length() > 0
					&& ordainForm.getAccessories().indexOf(del) == -1) {
				File aFile = new File(BaseEnv.FILESERVERPATH + "/news/" + del);
				aFile.delete();
			}
		}
		//�ж��Ƿ��Ǵ���ϸҳ����е��޸Ĳ���
		String position=this.getParameter("position", request);
		// ���ع����ƶ�
		Result result = mgt.loadOrdain(ordainForm.getOrdainId());
		
		//�жϴ˼�¼�Ƿ񻹴���
		if(result.retCode != ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add(
					getMessage(request, "this.record.not exist"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		OAOrdainBean oaOrdainBean = (OAOrdainBean) result.getRetVal();
		
		// ���ѷ�ʽ
		String wakeupType = "";
		if (wakeType != null && wakeType.length > 0) {
			for (String str : wakeType) {
				wakeupType += str + ",";
			}
		}

		/* form��Bean�� */
		read(ordainForm, oaOrdainBean);
		oaOrdainBean.setAccessories(this.getParameter("attachFiles", request));
		oaOrdainBean.setLastupDateTime(BaseDateFormat.format(new Date(),
				BaseDateFormat.yyyyMMddHHmmss));
		oaOrdainBean.setLastupDateBy(getLoginBean(request).getId());
		oaOrdainBean.setWakeupType(wakeupType);
				
		String id=","+oaOrdainBean.getId()+",";
		amgt.deleteByRelationId(id, "");
		
		result = mgt.updateOrdain(oaOrdainBean);

		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			
				//��ȡ֪ͨ����
				String popedomUserIds=mgt.getPopedomUser(oaOrdainBean,userId);
				
				// ������ѷ�ʽ
				String title = loginname+GlobalsTool.getMessage(request, "oa.common.updateBylaw.add")
						+ oaOrdainBean.getOrdainTitle();
				String url = "/OAnewOrdain.do";
				String favoriteURL = url + "?noback=true&operation=5&ordainId="
						+ oaOrdainBean.getId() + "&isEspecial=1";
				String content = "<a href=javascript:mdiwin('" + favoriteURL
						+ "','"
						+ GlobalsTool.getMessage(request, "oa.common.bylaw")
						+ "')>" + title + getMessage(request, "com.click.see")
						+ "</a>"; // ����
				if (wakeType != null && wakeType.length > 0) {
					for (String type : wakeType) {
						new Thread(new NotifyFashion(userId, title, content,
								popedomUserIds, Integer.parseInt(type), "yes",
								oaOrdainBean.getId())).start();
					}
				}
		
			/*// �޸ĳɹ�
			if("detailpage".equals(position))  //�ж��Ƿ��Ǵ���ϸҳ����е��޸Ĳ���
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess")).setBackUrl(
						"/OAnewOrdain.do?operation=5&ordainId="+oaOrdainBean.getId()).setAlertRequest(request);
			else
				EchoMessage.success().add(
						getMessage(request, "common.msg.updateSuccess")).setBackUrl(
						"/OAnewOrdain.do?operation=4").setAlertRequest(request);
		    return getForward(request, mapping, "message");*/
			EchoMessage.success().add(
				getMessage(request, "common.msg.updateSuccess")).setBackUrl(
					"/OAnewOrdain.do?operation=5&ordainId="+oaOrdainBean.getId()).setAlertRequest(request);	
		    return getForward(request, mapping, "message");
				
		} else {
			// �޸�ʧ��
			EchoMessage.error().add(
					getMessage(request, "common.msg.updateErro"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}

	}

	/**
	 * �����ƶ���ϸ��Ϣ
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "deprecation","unchecked" })
	protected ActionForward detail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OAOrdainBean oaOrdainBean=new OAOrdainBean();
		Result oaOrdain=null;
		try {
			//��ȡ��������ҳ���·��
			String Especial=this.getParameter("isEspecial", request);
			
			//��ȡ���ҵ��ղص�������
			String myCollection=this.getParameter("isMyCollection", request);
			
			String innoback=this.getParameter("noback", request);
		
			String oaordainId = getParameter("ordainId",request);
			OAOrdainSearchForm ordainSearchForm = (OAOrdainSearchForm) form;
			LoginBean loginBean = this.getLoginBean(request);
			//��õ�½�û����ڷ���
			String empGroup=loginBean.getGroupId();
			
			//��õ�½�û����ڲ���	
			String depts=loginBean.getDepartCode();
			
			//��ȡ��½�û�ID
			String userId=loginBean.getId();
			
			//����ID��ù����ƶ���Ϣ	 
			oaOrdain = mgt.loadOrdain(oaordainId);
			oaOrdainBean=(OAOrdainBean)oaOrdain.getRetVal();
			
			if("1".equals(Especial) || "true".equals(innoback) ){	//�жϽ��뷽ʽ���Ƿ��Ǵ�֪ͨ��Ϣ�������ϸҳ��
				
				/*���б�ҳ�����ӵ���ϸҳ���ж��Ƿ���Ȩ��*/
				/*//��ȡ��Ϣ��������
				Result fuGroup=mgt.queryFolderByCode(oaOrdainBean.getGroupId().substring(0,5));
				List<OAOrdainGroupBean> fugroup=(List<OAOrdainGroupBean>)fuGroup.retVal;
				//��ȡ��Ȩ�޲鿴����Ϣ�ĸ���
				String[] getStr=mgt.getShouquan(fugroup.get(0).getPopedomDeptIds(),fugroup.get(0).getPopedomUserIds()).split("%%");

				if(getStr[1].indexOf(userId)<0 && !"1".equals(userId)){
					EchoMessage.error().add(getMessage(request, "common.msg.RET_NO_RIGHT_ERROR"))
					.setNotAutoBack().setAlertRequest(request);
					return getForward(request, mapping, "message");			
				}*/
				/*���б�ҳ�����ӵ���ϸҳ���ж��Ƿ���Ȩ��*/
				
			}else{					
				//��һ��
				Result preResult = mgt.queryOrdain(ordainSearchForm,oaOrdainBean.getLastupDateTime(),userId,depts,"detailPre");
				if (preResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					List<OAOrdainBean> preNews = (List<OAOrdainBean>) preResult.getRetVal();
					if (preNews != null && preNews.size() > 0) {
						request.setAttribute("preId", preNews.get(preNews.size()-1).getId());
						request.setAttribute("preTitle",preNews.get(preNews.size()-1).getOrdainTitle());
					}
				}
				// ��һ��
				Result nextResult = mgt.queryOrdain(ordainSearchForm,oaOrdainBean.getLastupDateTime(),userId,depts,"detailNext");
				if (nextResult.retCode == ErrorCanst.DEFAULT_SUCCESS) {
					List<OAOrdainBean> nextNews = (List<OAOrdainBean>) nextResult.getRetVal();
					if (nextNews != null && nextNews.size() > 0) {
						request.setAttribute("nextId", nextNews.get(0).getId());
						request.setAttribute("nextTitle",nextNews.get(0).getOrdainTitle());
					}
				}
			}
			
			/*��ȡ֪ͨ����*/
			List<EmployeeBean> targetUsers =newsmgt.getEmployee(oaOrdainBean.getPopedomUserIds());
			List<Object> targetDept = newsmgt.getDepartment(oaOrdainBean.getPopedomDeptIds());
			
			Result groupResult=mgt.queryFolderByCode(oaOrdainBean.getGroupId());
			
			/*�жϿ�ʼ:�жϵ�ǰ��¼�û��Ƿ������ظ�����Ȩ��:onDownΪTrue����Ȩ��*/
			boolean onDown = false;
			
			List<OAOrdainGroupBean> gblist=(List<OAOrdainGroupBean>)groupResult.getRetVal();
			//��ȡ�ü�¼���ڵ���
			OAOrdainGroupBean gb=gblist.get(0);
			if(gb.getDownDeptIds()==null )
				gb.setDownDeptIds("");
			if(gb.getDownUserIds()==null)
				gb.setDownUserIds("");
			String downDeptIds=gb.getDownDeptIds();
			String downUserIds=gb.getDownUserIds();
			
			/*���û��ѡ��������Ȩ�Ĳ��ź͸��ˣ���Ĭ�Ͽ��Կ�����¼��Ϣ���û���ӵ�����ظ�����Ȩ��*/
			if("".equals(downDeptIds) && "".equals(downUserIds))
				onDown=true;
			else{
				String[] downDepts = downDeptIds.split(",");
				String[] downUsers = downUserIds.split(",");
				for(String dept : downDepts){
					if(depts.startsWith(dept) && !"".equals(dept)){
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
			}
			if("1".equals(userId))  //����ǹ���Ա������Ȩ��
				onDown=true;
			
			/*�жϽ���*/
			request.setAttribute("onDown", onDown);
			request.setAttribute("group", groupResult.retVal);
			
			request.setAttribute("oaOrdain", oaOrdainBean);
			String url = request.getRequestURI();
			String favoriteURL = URLEncoder.encode(url + "?operation=5&ordainId="
					+ oaordainId + "&isEspecial=1", "utf-8");
			String myCollectionURL = URLEncoder.encode("&isMyCollection=1", "utf-8");
			request.setAttribute("favoriteURL", favoriteURL);
			request.setAttribute("myCollectionURL", myCollectionURL);

			String uri = java.net.URLEncoder.encode(url+ "?operation=5&ordainId=" + oaordainId);
			request.setAttribute("uri", uri);
			request.setAttribute("targetUsers", targetUsers);
			request.setAttribute("targetDept", targetDept);
		
			request.setAttribute("backtype", innoback);
			request.setAttribute("IsEspecial", Especial);
			request.setAttribute("isMyCollection", myCollection);
			
			//�ж��Ƿ��Ѿ������ղ�
			Result isexit = new AttentionMgt().isAttention(loginBean.getId(), oaordainId, "OAOrdain");		
			if(isexit.retCode == ErrorCanst.MULTI_VALUE_ERROR){
				request.setAttribute("attention", "OK");
			}
			
			request.setAttribute("messageTitle", this.getLoginBean(request)
					.getEmpFullName()
					+ getMessage(request, "request.read.ordain")
					+ oaOrdainBean.getOrdainTitle());	
			
					
		} catch (Exception e) {
			e.printStackTrace();
			EchoMessage.error().add(getMessage(request, "news.not.find"))
			.setNotAutoBack().setAlertRequest(request);
			return getForward(request, mapping, "message");			  
		}
		return getForward(request, mapping, "to_detailOrdain");

	}
	/**
	 * ��ѯ��
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
	
		String groupLen=this.getParameter("groupId", request);
		OAOrdainGroupSearchForm oaForm = (OAOrdainGroupSearchForm)form;
		String first = request.getParameter("first");
		if("1".equals(first) && first != null){
			oaForm.setGroupId("");
			oaForm.setPageNo(1);
		}
		//��ȡ��ǰ�û��Դ�ģ���Ȩ��
		LoginBean loginBean = this.getLoginBean(request);
		MOperation mop = (MOperation) loginBean.getOperationMap().get("/UserFunctionQueryAction.do?tableName=OAOrdainGroup");
		request.setAttribute("addGroup", mop.add()); // ����Ȩ��
		request.setAttribute("deleteGroup", mop.delete()); // ɾ��Ȩ��
		request.setAttribute("updateGroup", mop.update()); // �޸�Ȩ��
		
		//�ж��Ƿ񵥻��ķ����ϼ�
		if(groupLen !=null && groupLen.length()>=5 && "last".equals(this.getParameter("type", request))){	
			oaForm.setGroupId(oaForm.getGroupId().substring(0, groupLen.length()-5));
			
		}
			
		Result result = mgt.queryFolder(oaForm.getGroupId(),first,"enter");
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("groupList", result.retVal);
			request.setAttribute("groupLen", oaForm.getGroupId());
		}
		return getForward(request, mapping, "queryGroup");
	}
	/**
	 * �����ǰ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward addPrepareGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		String classCode=this.getParameter("folderId", request);
     	request.setAttribute("folderId", classCode);
     	
     	if(classCode.length()>=5){  //�ж��Ƿ�����¼�������ǣ���ȡ����Ȩ��
			Result fuResult=mgt.queryFolderByCode(classCode);
			List<OAOrdainGroupBean> fugroup=(List<OAOrdainGroupBean>)fuResult.getRetVal();
			
			String[] getStr=mgt.getShouquan(fugroup.get(0).getPopedomDeptIds(),fugroup.get(0).getPopedomUserIds()).split("%%");
			
			request.setAttribute("fuDept", fugroup.get(0).getPopedomDeptIds()); //��������Ȩ����
			request.setAttribute("UserIds", fugroup.get(0).getPopedomUserIds()); //��������Ȩ����
		    request.setAttribute("fuUser", getStr[1]);	 //�����ĸ�������Ȩ����
		    request.setAttribute("deptNames", getStr[2]);  //������Ȩ���ŵ�����
	    }
     
		request.setAttribute("dealType", "add");
		request.setAttribute("time", new Date());
		
		return getForward(request, mapping, "dealGroup");
	}
	
	/**
	 * ִ��ɾ������ʱ���жϸ�Ŀ¼���Ƿ�������ݣ���Ŀ¼�������ƶ���Ϣ��
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward checkGroup(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response){
		String thePlace=this.getParameter("insertPlace", request);
		String[] ordainIds = getParameter("keyIds", request).split(",");
		int sumnum=0;         //��ȡ��������
		String theGroup="";   //��ȡ������Ŀ¼������ƶȵ�Ŀ¼��
		OAOrdainGroupBean obean=new OAOrdainGroupBean();
		for(int i=0;i<ordainIds.length;i++){
			int num=0;
			Result orbean=null;
			
			if("listpage".equals(thePlace)){ //�ж��Ƿ�ӹ����ƶ�ҳ������ɾ��Ŀ¼����
				orbean=mgt.queryFolderByCode(ordainIds[i]);
				obean=(OAOrdainGroupBean)((List<OAOrdainGroupBean>)orbean.retVal).get(0);
			}else{
				orbean=mgt.loadGroup(ordainIds[i]); //��ȡ��
			    obean=(OAOrdainGroupBean) orbean.retVal;
			}
			Result allGroup=mgt.queryFolder(obean.getClassCode(), null, "delete"); //��ȡ���Ŀ¼��ص��Ӽ�Ŀ¼
			if(allGroup.realTotal>1){
				num +=allGroup.realTotal-1;
			}
			
			Result allOrdain=mgt.queryOrdainBygroup(obean.getClassCode()); //��ȡ���Ŀ¼��صĹ����ƶ���Ϣ
			if(allOrdain.realTotal >0){
				num+=allOrdain.realTotal;
			}
			
			if(num !=0){    //�ж������Ƿ�Ϊ0��������ǣ���ȡ��Ŀ¼��Ŀ¼�� 
				theGroup +=obean.getGroupName()+",";
			}
			sumnum+=num;
		}	
		
		request.setAttribute("msg", sumnum+";"+theGroup+";"+obean.getId());

		return  getForward(request, mapping, "blank");
		
	}
	/**
	 * ִ����ӻ�༭����ʱ������ѡ����飬�ҳ�������Ȩ�Ĳ��ź͸���
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward getShouquan(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response){
		String classCode=this.getParameter("groupCode", request);
		if(!"".equals(classCode)){
			Result fuResult=mgt.queryFolderByCode(classCode);
			List<OAOrdainGroupBean> fugroup=(List<OAOrdainGroupBean>)fuResult.getRetVal();
			String[] getStr=mgt.getShouquan(fugroup.get(0).getPopedomDeptIds(),fugroup.get(0).getPopedomUserIds()).split("%%");
			String deIds="";
			String userIds="";
			if("".equals(fugroup.get(0).getPopedomDeptIds()))	
				deIds="noDept";
			else
				deIds=fugroup.get(0).getPopedomDeptIds();
			if("".equals(fugroup.get(0).getPopedomUserIds()))
				userIds="noUser";
			else
				userIds=fugroup.get(0).getPopedomUserIds();
		    request.setAttribute("msg", getStr[1]+";"+getStr[2]+";"+deIds+";"+userIds);  
		}
		return  getForward(request, mapping, "blank");
		
	}
	/**
	 * �����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward addGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
	
		LoginBean loginBean = this.getLoginBean(request);
		OAOrdainGroupForm oaForm =(OAOrdainGroupForm) form;
		
		OAOrdainGroupBean bean = new OAOrdainGroupBean();
		read(oaForm, bean);
		
		if(bean.getClassCode() == null || "".equals(bean.getClassCode())){
			String strClassCode = new PublicMgt().getLevelCode("OAOrdainGroup", "");
			bean.setClassCode(strClassCode);
		}else{
			String strClassCode = new PublicMgt().getLevelCode("OAOrdainGroup", bean.getClassCode());
			bean.setClassCode(strClassCode);
		}
		
		bean.setId(IDGenerater.getId());
		bean.setCreateBy(loginBean.getId());
		bean.setCreateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		
		String theChildGroup=bean.getClassCode().substring(0,bean.getClassCode().length()-5);
			
		Result result = mgt.addGroup(bean);
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){	
			String classCode=bean.getClassCode();			
			if(classCode.length()>5){	
				Result checkRe=mgt.queryFolderByCode(classCode.substring(0,classCode.length()-5));  //�ж��ϼ�Ŀ¼��iscatalog�Ƿ�Ϊ1
				List<OAOrdainGroupBean> oalist =(List<OAOrdainGroupBean>)checkRe.retVal;
				OAOrdainGroupBean oagroupBean=oalist.get(0);
				if(oagroupBean.getIsCatalog()!=1){  //�����Ϊ1�����¸���Ŀ¼��isCatalog
					Result re = mgt.updateIsCatalog(classCode.substring(0,classCode.length()-5),"add");		
					if(re.retCode == ErrorCanst.DEFAULT_SUCCESS){
						
					}
				}
			}
			request.getSession().setAttribute("ordainSearchForm", null);
			request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(request, "common.msg.addSuccess")).setAlertRequest(request);
			return getForward(request, mapping, "alert");	
		}else {
			EchoMessage.success().add(getMessage(
                    request, "common.msg.addFailture")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * �޸���ǰ
	 * @param mapping
	 * @param from
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward updatePrepareGroup(ActionMapping mapping,
			ActionForm from, HttpServletRequest request, HttpServletResponse response){
		String thePlace=this.getParameter("insertPlace", request);
		String id = request.getParameter("folderId");
		OAOrdainGroupBean oagroupBean=null;
		Result result=null;
		if("listpage".equals(thePlace)){ //�ж��Ƿ�ӹ����ƶ�ҳ������ɾ��Ŀ¼����
			result=mgt.queryFolderByCode(id);
			oagroupBean=(OAOrdainGroupBean)((List<OAOrdainGroupBean>)result.retVal).get(0);
		}else{
			result = mgt.loadGroup(id);
			oagroupBean = (OAOrdainGroupBean)result.retVal;
		}
		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//��ȡ��Ŀ¼����Ŀ¼��Ȩ��
			if(oagroupBean.getClassCode().length()>5){
				Result fuResult=mgt.queryFolderByCode(oagroupBean.getClassCode().substring(0,oagroupBean.getClassCode().length()-5));
				List<OAOrdainGroupBean> fugroup=(List<OAOrdainGroupBean>)fuResult.getRetVal();
				
				String str=mgt.getShouquan(fugroup.get(0).getPopedomDeptIds(),fugroup.get(0).getPopedomUserIds());
				String[] getStr=str.split("%%");				
			    
				request.setAttribute("fuDept", fugroup.get(0).getPopedomDeptIds()); //��������Ȩ����
				request.setAttribute("UserIds", fugroup.get(0).getPopedomUserIds()); //��������Ȩ����
			    request.setAttribute("fuUser", getStr[1]);	 //�����ĸ�������Ȩ����
			    request.setAttribute("deptNames", getStr[2]);  //������Ȩ���ŵ�����
			}
			
			//����ѡ����û�
			List<EmployeeBean> downUsers =newsmgt.getEmployee(oagroupBean.getDownUserIds());
			request.setAttribute("downUsers", downUsers);

			//����ѡ��Ĳ���
			List<Object> downDept = newsmgt.getDepartment(oagroupBean.getDownDeptIds());
		    request.setAttribute("downDept", downDept);
			
			/*��ȡ֪ͨ����*/
			
			List<EmployeeBean> targetUsers =newsmgt.getEmployee(oagroupBean.getPopedomUserIds());
			request.setAttribute("targetUsers", targetUsers);
			
			List<Object> targetDept = newsmgt.getDepartment(oagroupBean.getPopedomDeptIds());
		    request.setAttribute("targetDept", targetDept);
					
			request.setAttribute("group", oagroupBean);
		}
		request.setAttribute("dealType", "update");
		return getForward(request, mapping, "dealGroup");
	}
	
	/**
	 * �޸���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward updateGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		LoginBean loginBean = this.getLoginBean(request);
		OAOrdainGroupForm oaForm =(OAOrdainGroupForm) form;
		Result re=mgt.loadGroup(oaForm.getGroupid());
		
		//�жϴ˼�¼�Ƿ񻹴���
		if(re.retCode != ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.error().add(
					getMessage(request, "this.record.not exist"))
					.setAlertRequest(request);
			return getForward(request, mapping, "message");
		}
		OAOrdainGroupBean bean = (OAOrdainGroupBean)re.getRetVal();
		
		read(oaForm, bean);
		
		bean.setLastUpdateBy(loginBean.getId());
		bean.setLastUpdateTime(BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss));
		
		Result result=null;
		
		result = mgt.updateGroup(bean);
		if(result.retCode== ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("dealAsyn", "true");
			request.getSession().setAttribute("ordainSearchForm", null);
			EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).setAlertRequest(request);
		}
		else{
			  EchoMessage.success().add(getMessage(request, "common.msg.updateErro")).setAlertRequest(request);
		}
		return getForward(request, mapping, "alert");
	}
	
	/**
	 * ɾ����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward del(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		String[] ordainIds = getParameter("keyIds", request).split(",");
		
		//�ж��Ƿ��Ǵӹ����ƶ��б�ҳ������ɾ������
		String thePlace=this.getParameter("insertPlace", request);
		
		//��ȡ��һ��ѡ�е���
		OAOrdainGroupBean obean= (OAOrdainGroupBean) mgt.loadGroup(ordainIds[0]).getRetVal();    
		
		//����ClassCode�ĳ����ж��Ƿ�����ϼ���
		if(obean.getClassCode().length()-5 !=0){
			
			//��ѯ�͵�ǰ��¼�������Ƶ�classCode�ļ�¼
			Result res3=mgt.queryFolder(obean.getClassCode().substring(0,obean.getClassCode().length()-5),null,"delete");  
			
			 //�ж�ѡ�м�¼�Ƿ���ͬһ����Ŀ¼�µ������Ӽ�������ǣ���ʱ���¸���Ŀ¼��Catalog
			if(res3.realTotal ==ordainIds.length +1){ 
				Result res4=mgt.updateIsCatalog(obean.getClassCode().substring(0,obean.getClassCode().length()-5),"delete");
				if(res4.retCode != ErrorCanst.DEFAULT_SUCCESS){
					EchoMessage.success().add(getMessage(request, "common.msg.delError")).setBackUrl("/OAOrdainGroup.do?operation=4&dealType=ordainGroup").
	                        setAlertRequest(request);
					 return getForward(request, mapping, "alert"); 	
				}
			}		
		}
	    
				
		Result result = mgt.delGroup(ordainIds); //ɾ����ǰѡ�еļ�¼	
		if (result.retCode == ErrorCanst.DEFAULT_SUCCESS) {
			if("listpage".equals(thePlace)){
				request.getSession().setAttribute("ordainSearchForm", null);
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl("/OAnewOrdain.do?insertPlace=del&operation=4").
	            setAlertRequest(request);
			}else{		
				EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl("/OAOrdainGroup.do?operation=4&dealType=ordainGroup").
	            setAlertRequest(request);
			}
			return getForward(request, mapping, "alert"); 	
		}else{	 
			if("listpage".equals(thePlace))
				EchoMessage.success().add(getMessage(request, "common.msg.delError")).setBackUrl("/OAnewOrdain.do?operation=4").
	            setAlertRequest(request);
			else
				EchoMessage.success().add(getMessage( request, "common.msg.delError")).setBackUrl("/OAOrdainGroup.do?operation=4&dealType=ordainGroup").
			        setAlertRequest(request);
			return getForward(request, mapping, "alert"); 	
		}
	}
}
