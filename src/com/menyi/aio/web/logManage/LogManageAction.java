package com.menyi.aio.web.logManage;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.MOperation;
import com.menyi.email.util.AIOEMail;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.*;


/**
 * 
 * <p>Title:��־����Action</p> 
 * <p>Description: </p>
 *
 * @Date:2013-9-22
 * @Copyright: �������
 * @Author fjj
 */
public class LogManageAction extends MgtBaseAction{

	LogManageMgt mgt = new LogManageMgt();
	private String fileUrl = "../logs/";
	private static String url = "../../config/log4j.properties";
	
	/**
	 * 
	 * �������
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// ���ݲ�ͬ�������ͷ������ͬ��������
		int operation = getOperation(request);
		
		String opType = request.getParameter("opType");						//�����������½��־��ѯ��ϵͳ��־��ѯ��������־��
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_QUERY:
			if(opType != null && "operateLog".equals(opType)){
				//������־
				forward = queryLogList(mapping, form, request, response);
			}else if(opType != null && "loginLog".equals(opType)){
				//��¼��־��ѯ
				forward = queryLoginLogList(mapping, form, request, response);
			}else if(opType != null && "runLog".equals(opType)){
				//������־
				forward = runLog(mapping, form, request, response);
			}else if(opType != null && "tradeLog".equals(opType)){
				//������־
				forward = tradeLog(mapping, form, request, response);
			}else if(opType != null && "tradeAjax".equals(opType)){
				//������־
				forward = tradeAjax(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_DELETE:
			//ɾ��
			if(opType != null && "deleteLoginLog".equals(opType)){
				//ɾ����¼��־
				forward = deleteLoginLog(mapping, form, request, response);
			}else if(opType != null && "deleteLog".equals(opType)){
				//ɾ��������־
				forward = deleteLog(mapping, form, request, response);
			}else if(opType != null && "deleteLogFile".equals(opType)){
				//ɾ��������־��myLog�ļ���
				forward = deleteLogFile(mapping, form, request, response);
			}
			break;
		default:
			if(opType != null && "setLevel".equals(opType)){
				//������־����
				forward = setLevel(mapping, form, request, response);
			}else{
				//��־�������ҳ
				forward = frame(mapping, form, request, response);
			}
			break;
		}
		return forward;
	}

	/**
	 * ��ҳ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward frame(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		
		return getForward(request, mapping, "logIndex");
	}
	
	/**
	 * ��¼��־��ѯ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryLoginLogList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		LoginlogSearchForm searchForm = (LoginlogSearchForm)form;
		MOperation mop = (MOperation) (getLoginBean(request).getOperationMap().get("/LogManageAction.do"));
		request.setAttribute("MOID",mop.getModuleId())   ;
		Result rs = mgt.queryLoginLogList(searchForm);
		if(rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("pageBar", this.pageBar(rs, request));
			request.setAttribute("loginlogList", rs.getRetVal());
		}
		return getForward(request, mapping, "loginlogList");
	}
	
	
	/**
	 * ������־
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward queryLogList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		LogSearchForm searchForm = (LogSearchForm)form;
		Result rs = mgt.queryLogList(searchForm);
		if(rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("pageBar", this.pageBar(rs, request));
			request.setAttribute("logList", rs.getRetVal());
		}
		return getForward(request,mapping, "logList");
	}
	
	
	/**
	 * ������־
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward runLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
        List fileList = new ArrayList();
		//��ȡlog�ļ����µ�������־�ļ�
		File file = new File(fileUrl);
		if(file.exists()){
			File[] f = file.listFiles();
			for(int i=0;i<f.length;i++){
				if(f[i].isFile()){
					File childFile = f[i];
					//������־�ļ�
					fileList.add(BaseDateFormat.format(new Date(childFile.lastModified()),BaseDateFormat.yyyyMMddHHmmss)+";"+childFile.getName());
				}
			}
		}
		//��������޸�ʱ��
		for(int i=0;i<fileList.size()-1;i++){
			for(int j=1;j<fileList.size()-i;j++){
				String o1 = String.valueOf(fileList.get(j-1));
				String date1 = o1.split(";")[0];
				String o2 = String.valueOf(fileList.get(j));
				String date2 = o2.split(";")[0];
				if(date1.compareTo(date2)<0){
					String temp = String.valueOf(fileList.get(j-1));
					fileList.set((j-1), fileList.get(j));
					fileList.set(j, temp);
				}
			}
		}
		List list = new ArrayList();
		for(int i =0;i<fileList.size();i++){
			String o = String.valueOf(fileList.get(i));
			list.add(o.split(";")[1]);
		}
		request.setAttribute("fileList", list);
		request.setAttribute("logUrl", fileUrl);
		
		//��ȡ���õ���־����
		request.setAttribute("level", BaseEnv.log.getLevel());
		return getForward(request,mapping, "runList");
	}
	
	/**
	 * ������־
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward tradeLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setAttribute("path", request.getParameter("path"));
		request.setAttribute("fileName", request.getParameter("fileName"));        
		return getForward(request,mapping, "tradeInfo");
	}
	
	/**
	 * ������־
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward tradeAjax(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setAttribute("path", request.getParameter("path"));
		request.setAttribute("fileName", request.getParameter("fileName"));  
		
		File file = new File(request.getParameter("path") + request.getParameter("fileName"));
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		int length = dis.available();
		long rl = 1024 * 1024;

		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(file, "r");
			//�ļ����ȡ��ֽ���
			long fileLength = randomFile.length();
			//���ļ�����ʼλ��
			long beginIndex = (fileLength > rl) ? (fileLength-rl) : 0;
			//�����ļ��Ŀ�ʼλ���Ƶ�beginIndexλ��
			randomFile.seek(beginIndex);
			byte[] bs = new byte[0];
			byte[] b = new byte[1024];
			int byteread = 0;
			//		 һ�ζ�10���ֽڣ�����ļ����ݲ���10���ֽڣ����ʣ�µ��ֽڡ�
			//		 ��һ�ζ�ȡ���ֽ�������byteread
			while ((byteread = randomFile.read(b)) != -1) {
				byte[] temp = bs;
				bs = new byte[temp.length + byteread];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, byteread);
			}
			String str = new String(bs,"GB2312");
			//str =  str.substring(str.indexOf("\n")); //ȥ����һ�����У���Ϊ�����ǽضϵ�
			request.setAttribute("msg", str);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e1) {
				}
			}
		}
		
		return getForward(request,mapping, "blank");
	}
	
	
	/**
	 * ɾ����¼��־��¼
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward deleteLoginLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		//ɾ����¼��־30������ļ�¼
		Result rs = mgt.deleteLoginLog();
		String url = "/LogManageQueryAction.do?operation=4&opType=loginLog";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//ɾ���ɹ�
			EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl(url).setAlertRequest(request);
		}else {
			EchoMessage.success().add(getMessage(request, "common.msg.delError")).setBackUrl(url).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * ɾ��������־��¼
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward deleteLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		//ɾ��������־��30������ļ�¼��
		Result rs = mgt.deleteLog();
		String url = "/LogManageAction.do?operation=4&opType=operateLog";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//ɾ���ɹ�
			EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl(url).setAlertRequest(request);
		}else {
			EchoMessage.success().add(getMessage(request, "common.msg.delError")).setBackUrl(url).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * ɾ����־�ļ�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward deleteLogFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//�õ�����
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(calendar.DATE, -30);
		Date date = calendar.getTime();
		
		//�õ����е���־�ļ�
		File file = new File(fileUrl);
		if(file.exists()){
			File[] f = file.listFiles();
			for(int i=0;i<f.length;i++){
				if(f[i].isFile()){
					File childFile = f[i];
					//������־�ļ�
					long time = childFile.lastModified();			//��־������޸�ʱ��
					Date fileDate = new Date(time);
					if(fileDate.before(date)){
						//ɾ��30��֮ǰ����־�ļ�
						childFile.delete();
					}
				}
			}
		}
		return runLog(mapping, form, request, response);
	}
	
	
	/**
	 * ������־����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward setLevel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String rootLogger = request.getParameter("rootLogger");
		String myLog = request.getParameter("myLog");
		String temp = "";
		String msg = "�ر�";
	    try {
	    	if("debug".equals(rootLogger)){
	    		msg = "��";
	    		//�����ڴ��е�bug����
	    		BaseEnv.log.setLevel(Level.DEBUG);
	    		AIOEMail.emailLog.setLevel(Level.DEBUG);
	    		MSGConnectCenter.log.setLevel(Level.DEBUG);
	    	}else{
	    		BaseEnv.log.setLevel(Level.INFO);
	    		AIOEMail.emailLog.setLevel(Level.INFO);
	    		MSGConnectCenter.log.setLevel(Level.INFO);
	    	}
	    	
	    	//����־�ļ�log4j.properties���Ӧ�����ݸ���
	    	File file = new File(url);
	    	if(!file.canWrite()){
	    		request.setAttribute("msg", msg+"����ģʽʧ��,�ļ�"+url+"ֻ�������޸�Ϊ��д");
				return getForward(request, mapping, "blank");
	    	}
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();
            // �������ǰ�������
            while((temp=br.readLine()) != null){
            	if(temp.indexOf("log4j.logger.MYLog=")>-1){
            		buf.append("log4j.logger.MYLog="+myLog+",A2\n");
            	}else if(temp.indexOf("log4j.logger.EmailLog=")>-1){
            		buf.append("log4j.logger.EmailLog="+myLog+",EmailLog\n");
            	}else if(temp.indexOf("log4j.logger.MSGLog=")>-1){
            		buf.append("log4j.logger.MSGLog="+myLog+",MSGLog\n");
            	}else{
            		buf.append(temp+"\n");
            	}
            }
            br.close();
            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            pw.close();
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", msg+"����ģʽʧ��");
			return getForward(request, mapping, "blank");
		}
		request.setAttribute("msg", msg+"����ģʽ�ɹ�");
		return getForward(request, mapping, "blank");
	}	
}
