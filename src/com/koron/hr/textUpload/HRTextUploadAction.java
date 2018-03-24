package com.koron.hr.textUpload;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.dbfactory.Result;
import com.koron.hr.bean.HRCardRecordPositionBean;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

public class HRTextUploadAction extends MgtBaseAction {
	
	 HRTextUploadMgt hRTextUploadMgt = new HRTextUploadMgt();
	
	 @SuppressWarnings("unused")
	 private ImportTextUpload textMs = new ImportTextUpload();
	 public static ActionMapping mappin = new ActionMapping();
	 
	 public HRTextUploadAction(){
		 
	 }
	
	@Override
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		int operation = getOperation(request);
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_ADD:
			forward = importData(mapping, form, request, response);
			break;
		default:
			forward = textLoad(mapping, form, request, response);
		}

		return forward;
	}

	protected ActionForward textLoad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Result hrCardRecordPositionBeanResult = hRTextUploadMgt.findHRCardRecordPosition() ;
		HRCardRecordPositionBean hrCardRecordPosition = (HRCardRecordPositionBean)hrCardRecordPositionBeanResult.retVal;
		request.setAttribute("cardRecordPosition", hrCardRecordPosition) ;
        return getForward(request, mapping, "textUpload");
	}
	
	/**
	 * txt文件导入
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected ActionForward importData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		HRCardRecordPositionBean hRCardRecordPositionBean = new HRCardRecordPositionBean();
		HRTextUploadForm txtFrom = (HRTextUploadForm)form;
		FormFile file = txtFrom.getFileName();
        List<String> lines = IOUtils.readLines(file.getInputStream(),"GBK");
        Iterator<String> it = lines.iterator();
        while(it.hasNext()){
        	String line = it.next();
        	//String[] strold = line.split("\\s{1,}");
        	String[] str = new String[4];
        	if(line.length() >= txtFrom.getTimeEnd()-1){
        		try {
					str[0] = line.substring(0,txtFrom.getNumStart()-1).trim();
					str[1] = line.substring(txtFrom.getNumStart()-1,txtFrom.getNumEnd());
					try {
						str[2] = changeDateFormat(line.substring(txtFrom.getDateStart()-1,txtFrom.getDateEnd()));
					} catch (RuntimeException e) {
						EchoMessage.error().add("日期转换有误.请查看文件或输入的日期位置")
								.setAlertRequest(request);
						return getForward(request, mapping, "message");
					}
					str[3] = line.substring(txtFrom.getTimeStart()-1,txtFrom.getTimeEnd());
				} catch (StringIndexOutOfBoundsException e) {
					EchoMessage.error().add("存在输入的位置超越文本数据长度,请检查输入的位置")
					.setAlertRequest(request);
					return getForward(request, mapping, "message");
				}
        		textMs.addTextData(str);
        	}else{
        		EchoMessage.error().add("输入的时间结束位置超越文本数据长度,请检查输入的位置")
				.setAlertRequest(request);
				return getForward(request, mapping, "message");
        	}
        }
        
        hRCardRecordPositionBean.setId("1");
        hRCardRecordPositionBean.setNumStart(txtFrom.getNumStart());
        hRCardRecordPositionBean.setNumEnd(txtFrom.getNumEnd());
        hRCardRecordPositionBean.setDateStart(txtFrom.getDateStart());
        hRCardRecordPositionBean.setDateEnd(txtFrom.getDateEnd());
        hRCardRecordPositionBean.setTimeStart(txtFrom.getTimeStart());
        hRCardRecordPositionBean.setTimeEnd(txtFrom.getTimeEnd());
        hRTextUploadMgt.updateHRCardRecordPosition(hRCardRecordPositionBean);
		return getForward(request, mapping, "importComplete"); 
	}
	
	public String changeDateFormat(String str){
		if(str.length() > 9){
			if(str.indexOf("/") != -1){
				str = str.replaceAll("/","-");
			}
			if(str.indexOf("\\") != -1){
				str = str.replaceAll("\\\\","-");
			}
		}else if(str.length() < 10){
			str = str.substring(0, 4) + "-" + str.substring(4, 6) + "-" +str.substring(6, 8);
		}
		
		return str;
	}
}
