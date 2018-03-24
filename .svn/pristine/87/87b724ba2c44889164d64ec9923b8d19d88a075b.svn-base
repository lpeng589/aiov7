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
 * <p>Title:日志管理Action</p> 
 * <p>Description: </p>
 *
 * @Date:2013-9-22
 * @Copyright: 科荣软件
 * @Author fjj
 */
public class LogManageAction extends MgtBaseAction{

	LogManageMgt mgt = new LogManageMgt();
	private String fileUrl = "../logs/";
	private static String url = "../../config/log4j.properties";
	
	/**
	 * 
	 * 程序入口
	 */
	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 跟据不同操作类型分配给不同函数处理
		int operation = getOperation(request);
		
		String opType = request.getParameter("opType");						//处理操作（登陆日志查询，系统日志查询，运行日志）
		ActionForward forward = null;
		switch (operation) {
		case OperationConst.OP_QUERY:
			if(opType != null && "operateLog".equals(opType)){
				//操作日志
				forward = queryLogList(mapping, form, request, response);
			}else if(opType != null && "loginLog".equals(opType)){
				//登录日志查询
				forward = queryLoginLogList(mapping, form, request, response);
			}else if(opType != null && "runLog".equals(opType)){
				//运行日志
				forward = runLog(mapping, form, request, response);
			}else if(opType != null && "tradeLog".equals(opType)){
				//跟踪日志
				forward = tradeLog(mapping, form, request, response);
			}else if(opType != null && "tradeAjax".equals(opType)){
				//跟踪日志
				forward = tradeAjax(mapping, form, request, response);
			}
			break;
		case OperationConst.OP_DELETE:
			//删除
			if(opType != null && "deleteLoginLog".equals(opType)){
				//删除登录日志
				forward = deleteLoginLog(mapping, form, request, response);
			}else if(opType != null && "deleteLog".equals(opType)){
				//删除操作日志
				forward = deleteLog(mapping, form, request, response);
			}else if(opType != null && "deleteLogFile".equals(opType)){
				//删除运行日志（myLog文件）
				forward = deleteLogFile(mapping, form, request, response);
			}
			break;
		default:
			if(opType != null && "setLevel".equals(opType)){
				//设置日志级别
				forward = setLevel(mapping, form, request, response);
			}else{
				//日志管理的首页
				forward = frame(mapping, form, request, response);
			}
			break;
		}
		return forward;
	}

	/**
	 * 首页
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
	 * 登录日志查询
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
	 * 操作日志
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
	 * 运行日志
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward runLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
        List fileList = new ArrayList();
		//获取log文件夹下的所有日志文件
		File file = new File(fileUrl);
		if(file.exists()){
			File[] f = file.listFiles();
			for(int i=0;i<f.length;i++){
				if(f[i].isFile()){
					File childFile = f[i];
					//存在日志文件
					fileList.add(BaseDateFormat.format(new Date(childFile.lastModified()),BaseDateFormat.yyyyMMddHHmmss)+";"+childFile.getName());
				}
			}
		}
		//排序根据修改时间
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
		
		//获取设置的日志级别
		request.setAttribute("level", BaseEnv.log.getLevel());
		return getForward(request,mapping, "runList");
	}
	
	/**
	 * 跟踪日志
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
	 * 跟踪日志
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
			//文件长度、字节数
			long fileLength = randomFile.length();
			//读文件的起始位置
			long beginIndex = (fileLength > rl) ? (fileLength-rl) : 0;
			//将读文件的开始位置移到beginIndex位置
			randomFile.seek(beginIndex);
			byte[] bs = new byte[0];
			byte[] b = new byte[1024];
			int byteread = 0;
			//		 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
			//		 将一次读取的字节数赋给byteread
			while ((byteread = randomFile.read(b)) != -1) {
				byte[] temp = bs;
				bs = new byte[temp.length + byteread];
				System.arraycopy(temp, 0, bs, 0, temp.length);
				System.arraycopy(b, 0, bs, temp.length, byteread);
			}
			String str = new String(bs,"GB2312");
			//str =  str.substring(str.indexOf("\n")); //去掉第一个换行，因为可能是截断的
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
	 * 删除登录日志记录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward deleteLoginLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		//删除登录日志30天以外的记录
		Result rs = mgt.deleteLoginLog();
		String url = "/LogManageQueryAction.do?operation=4&opType=loginLog";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//删除成功
			EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl(url).setAlertRequest(request);
		}else {
			EchoMessage.success().add(getMessage(request, "common.msg.delError")).setBackUrl(url).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 删除操作日志记录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward deleteLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		//删除操作日志（30天以外的记录）
		Result rs = mgt.deleteLog();
		String url = "/LogManageAction.do?operation=4&opType=operateLog";
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			//删除成功
			EchoMessage.success().add(getMessage(request, "common.msg.delSuccess")).setBackUrl(url).setAlertRequest(request);
		}else {
			EchoMessage.success().add(getMessage(request, "common.msg.delError")).setBackUrl(url).setAlertRequest(request);
		}
		return getForward(request, mapping, "message");
	}
	
	/**
	 * 删除日志文件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward deleteLogFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//得到日期
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(calendar.DATE, -30);
		Date date = calendar.getTime();
		
		//得到所有的日志文件
		File file = new File(fileUrl);
		if(file.exists()){
			File[] f = file.listFiles();
			for(int i=0;i<f.length;i++){
				if(f[i].isFile()){
					File childFile = f[i];
					//存在日志文件
					long time = childFile.lastModified();			//日志的最后修改时间
					Date fileDate = new Date(time);
					if(fileDate.before(date)){
						//删除30天之前的日志文件
						childFile.delete();
					}
				}
			}
		}
		return runLog(mapping, form, request, response);
	}
	
	
	/**
	 * 设置日志级别
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
		String msg = "关闭";
	    try {
	    	if("debug".equals(rootLogger)){
	    		msg = "打开";
	    		//更改内存中的bug级别
	    		BaseEnv.log.setLevel(Level.DEBUG);
	    		AIOEMail.emailLog.setLevel(Level.DEBUG);
	    		MSGConnectCenter.log.setLevel(Level.DEBUG);
	    	}else{
	    		BaseEnv.log.setLevel(Level.INFO);
	    		AIOEMail.emailLog.setLevel(Level.INFO);
	    		MSGConnectCenter.log.setLevel(Level.INFO);
	    	}
	    	
	    	//把日志文件log4j.properties相对应的数据更改
	    	File file = new File(url);
	    	if(!file.canWrite()){
	    		request.setAttribute("msg", msg+"调试模式失败,文件"+url+"只读，请修改为可写");
				return getForward(request, mapping, "blank");
	    	}
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();
            // 保存该行前面的内容
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
			request.setAttribute("msg", msg+"调试模式失败");
			return getForward(request, mapping, "blank");
		}
		request.setAttribute("msg", msg+"调试模式成功");
		return getForward(request, mapping, "blank");
	}	
}
