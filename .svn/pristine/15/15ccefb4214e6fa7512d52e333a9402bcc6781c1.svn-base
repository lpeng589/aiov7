package com.menyi.aio.web.bol88;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.dbfactory.Result;
import com.menyi.email.EMailMgt;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.BusinessException;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MailPoolThread;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;

/**
 *
 * <p>Title:登陆 </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class AIOBOL88EMailAction extends MgtBaseAction {
    AIOBOL88Mgt aioBol88Mgt = new AIOBOL88Mgt();
    public AIOBOL88EMailAction() {

    }




    /**
     * exe 控制器入口函数
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.huawei.sms.web.util.BaseAction method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        //跟据不同操作类型分配给不同函数处理
        int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_ADD_PREPARE:
        	//电子商务的邮件池群发使用与增加一个权限
        	String type = request.getParameter("type");
        	if("mailInfo".equals(type)){
        		forward = mailInfo(mapping, form, request, response);
        	}else if("mailWrite".equals(type)){
        		forward = mailWrite(mapping, form, request, response);
        	}else if("keyword".equals(type)){        	
        		forward = mailKeyword(mapping, form, request, response);
        	}
            break;
        case  OperationConst.OP_ADD:
        	forward = mailWriteSave(mapping, form, request, response);
        	break;
        }
        return forward;
    }

  

    public static String toMD5(String arg) {
        try {
            StringBuffer sb = new StringBuffer();
            MessageDigest code;

            code = MessageDigest.getInstance("MD5");

            code.update(arg.getBytes());
            byte[] bs = code.digest();
            for (int i = 0; i < bs.length; i++) {
                int v = bs[i] & 0xff;
                if (v < 16) {
                    sb.append(0);
                } else {
                    sb.append(Integer.toHexString(v));
                }
            }
            arg = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return arg;
    }

    protected ActionForward mailKeyword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 
    	request.setAttribute("query", request.getParameter("query"));
    	
    	MailPoolForm myform  = (MailPoolForm)form;
    	String mailkeyword = myform.getMailkeyword();
    	
    	if(mailkeyword != null && mailkeyword.length() > 0 ){    	
    		String rs = aioBol88Mgt.getKeyword(mailkeyword);
    		if(rs != null && rs.length() >0 && !rs.equals("NoData")){
    			String patternString = "<k><t>([^<>]*)</t><i>([^<>]*)</i><n>([^<>]*)</n></k>";
				Pattern pattern = Pattern.compile(patternString,
						Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(rs);
				ArrayList list = new ArrayList();
				while (matcher.find()) {
					list.add(new String[]{matcher.group(1),matcher.group(2),matcher.group(3)});
				}
				request.setAttribute("result",list);
    		}
    	}
		
		return getForward(request, mapping, "keyword");
	}
    
    protected ActionForward mailInfo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if("true".equals(request.getParameter("stop"))){
			aioBol88Mgt.stop(this.getLoginBean(request).getSunCmpClassCode(),"bol88");
		}
		Result rs = aioBol88Mgt.getMailSet(this.getLoginBean(request).getSunCmpClassCode(),"bol88");
		MailPoolForm f = (MailPoolForm)rs.getRetVal();
		request.setAttribute("MailPoolForm",f);	
		
		MailPoolThread mpt = (MailPoolThread)BaseEnv.mailPoolThreadMap.get(this.getLoginBean(request).getSunCmpClassCode()+":BOL88");
		String info = mpt==null?"":mpt.INFO;
		request.setAttribute("MailPoolInfo",info);
		request.setAttribute("curCount",mpt==null?0:mpt.totalCount);
		
		//检查是否收费
		int i = aioBol88Mgt.checkRight();
		request.setAttribute("FEE", i);
		request.setAttribute("username", request.getParameter("username"));
		request.setAttribute("JSESSION", request.getParameter("JSESSION"));
		return getForward(request, mapping, "mailInfo");
	}
    
    protected ActionForward mailWrite(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
    	MailPoolForm myform  = (MailPoolForm)form;
    	
		String[] keyIds = request.getParameterValues("keyId");
		
		String curKeywordId = "";
		if(keyIds != null && keyIds.length >0){
			for(String k:keyIds){
				curKeywordId += ";"+k;
			}
			curKeywordId = curKeywordId.substring(1);
		}	
		String from = request.getParameter("from");
		if(from == null || from.length() ==0){
			from = "BOL88";
		}
		Result rs = aioBol88Mgt.getMailSet(this.getLoginBean(request).getSunCmpClassCode(),from);
		MailPoolForm f = (MailPoolForm)rs.getRetVal();
		if(f == null) {
			f = myform; 
			f.setStatus(0);
		}
		f.setCurKeywordId(curKeywordId);
		f.setCurKeywordName(myform.getCurKeywordName());
		f.setCurKwMailNum(myform.getCurKwMailNum());
		
		request.setAttribute("MailPoolForm",f);		
		
		EMailMgt mgt = new EMailMgt();
		rs = mgt.getALLMailinfoSetting(this.getLoginBean(request).getId());
		request.setAttribute("MailinfoSetting",rs.getRetVal());
		
		MailPoolThread mpt = (MailPoolThread)BaseEnv.mailPoolThreadMap.get(this.getLoginBean(request).getSunCmpClassCode()+":"+from);
		String info = mpt==null?"":mpt.INFO;
		request.setAttribute("MailPoolInfo",info);
		request.setAttribute("curCount",mpt==null?0:mpt.totalCount);
		
		request.setAttribute("from",from);		
		return getForward(request, mapping, "mailWrite");
	}
    
    protected ActionForward mailWriteSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
    	MailPoolForm myform  = (MailPoolForm)form;
    	
    	if(myform.getStatus() == 0){
			aioBol88Mgt.stop(this.getLoginBean(request).getSunCmpClassCode(),myform.getFrom());
			
			EchoMessage.success().add(
					getMessage(request, "com.auto.succeed"))
					.setBackUrl("/AIOBOL88EMailAction.do?operation="+OperationConst.OP_ADD_PREPARE+"&type=mailWrite&from="+myform.getFrom()+"&winCurIndex="+request.getParameter("winCurIndex"))
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}
    	
		String attachPath = "bol88";//以BOL88为路径
		
		String accessories = request.getParameter("attachFiles");
		accessories = accessories== null?"":accessories;

		//需删除的附件
		String delFiles = request.getParameter("delFiles");
		//需删除附件中，如果在附件中也存在，则是用户删除后又重新添加的，所以不能真正删除 
		String[] dels = delFiles==null?new String[0]:delFiles.split(";");
		for(String del:dels){
			if(del != null && del.length() > 0 && accessories.indexOf(del) == -1){
				File aFile = new File(BaseEnv.FILESERVERPATH + "/email/" + attachPath+"/" + del);
				aFile.delete();
			}
		}
		
		String mailAttaches="";
		String[] attach=request.getParameterValues("attachFile");
		if(attach!=null){
			for(int i=0;i<attach.length;i++){
				mailAttaches+=attach[i]+";";
			}
		}
		
		//保存
		Result rs = aioBol88Mgt.updateMailSet(myform.getMailaccount(),myform.getTitle(), myform.getContent(), mailAttaches, myform.getCcMail(), 
				myform.getCurKeywordName(), myform.getCurKeywordId(), myform.getCurKwMailNum(), myform.getTotalSend(), 
				myform.getLeaveDate(), myform.getStartTime(), myform.getEndTime(), myform.getStatus(),myform.getStartSendDate(),myform.getEndSendDate(),myform.getThreadNum(), 
				this.getLoginBean(request).getSunCmpClassCode(),myform.getFrom());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			EchoMessage.success().add(
					getMessage(request, "com.auto.succeed"))
					.setBackUrl("/AIOBOL88EMailAction.do?operation="+OperationConst.OP_ADD_PREPARE+"&type=mailWrite&from="+myform.getFrom()+"&winCurIndex="+request.getParameter("winCurIndex"))
					.setAlertRequest(request);
			return getForward(request, mapping, "alert");
		}else{
			throw new BusinessException("common.msg.error",
					"/AIOBOL88EMailAction.do?operation="+OperationConst.OP_ADD_PREPARE+"&type=mailWrite&from="+myform.getFrom()+"&winCurIndex="+request.getParameter("winCurIndex"));
		}
		
	}
}
