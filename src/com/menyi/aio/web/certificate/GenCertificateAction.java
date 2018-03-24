package com.menyi.aio.web.certificate;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.dbfactory.Result;
import com.menyi.aio.web.certTemplate.CertTemplateMgt;
import com.menyi.aio.web.colconfig.ColConfigMgt;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.login.MOperation;
import com.menyi.web.util.BaseAction;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.BusinessException;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class GenCertificateAction extends MgtBaseAction{

	private GenCertificateMgt mgt = new GenCertificateMgt() ;
	
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		ActionForward forward = null;
		if("gen".equals(request.getParameter("op"))){
			return genrator(mapping,form,request,response);
		}else{
			return certificate(mapping,form,request,response);
		}
	}
	
	private ActionForward certificate(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		Result rs = new CertTemplateMgt().query("0");
		MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().
                get("/GenCertificateAction.do"));
		request.setAttribute("MOID", mop.getModuleId());
		request.setAttribute("billList", rs.retVal);
		return getForward(request, mapping, "Certificate") ;
	}
	 /**
	  * ����ƾ֤
	  * @param mapping
	  * @param form
	  * @param request
	  * @param response
	  * @return
	  */
	private ActionForward genrator(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward = getForward(request, mapping, "alert");
		
		String tempNumber = request.getParameter("tempNumber");
		String tableDisplay = request.getParameter("tableDisplay");
		String tid = request.getParameter("tid");
		String keyIds = request.getParameter("keyIds");
		try {
			tableDisplay = new String(tableDisplay.getBytes("ISO8859_1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		String certificateType = request.getParameter("certificateType"); //1���������ɣ�2���ϲ�����
		if(keyIds==null || keyIds.length()==0){
			throw new BusinessException("��ѡ��������ƾ֤�ĵ���","/GenCertificateAction.do");
		}
		LoginBean lb = this.getLoginBean(request);
		//����ƾ֤
		Result rs = mgt.genCertificate(lb.getId(),lb.getSunCmpClassCode(),lb.getDepartCode(),tempNumber, keyIds, certificateType,this.getResources(request),this.getLocale(request));
		
		
		
		if(rs.retCode != ErrorCanst.DEFAULT_SUCCESS){
			throw new BusinessException(rs.retVal==null?"����ƾ֤����":""+rs.retVal,"/GenCertificateAction.do");
		}
		EchoMessage.success().add("����ƾ֤�ɹ�")
				.setBackUrl(
						"/GenCertificateAction.do?")
				.setAlertRequest(request);
		return forward;
	}
}
