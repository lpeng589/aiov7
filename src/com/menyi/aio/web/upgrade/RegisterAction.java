package com.menyi.aio.web.upgrade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.RoleBean;
import com.menyi.web.util.*;

import org.apache.struts.action.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.io.FileOutputStream;
import java.security.MessageDigest;

/**
 * Description:
 * <br/>Copyright (C), 2008-2012, Justin.T.Wang
 * <br/>This Program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @autor Justin.T.Wang  yao.jun.wang@hotmail.com

 * @version 1.0
 */
public class RegisterAction extends MgtBaseAction {
    RegisterMgt mgt = new RegisterMgt();
    public RegisterAction() {
    }

    /**
     * ���ò������Ȩ���ж�
     * @param req HttpServletRequest
     * @param mapping ActionMapping
     * @return ActionForward
     */
    protected ActionForward doAuth(HttpServletRequest req,
                                   ActionMapping mapping) {
    	if(SystemState.instance.dogState == SystemState.DOG_FORMAL ||
    		SystemState.instance.dogState == SystemState.DOG_EVALUATE ){
    		return super.doAuth(req, mapping);
    	}
    	return null;
    }


    /**
     * exe ��������ں���
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
        ActionForward forward = null;
        //���ݲ�ͬ�������ͷ������ͬ��������
        String evalpost = request.getParameter("evalpost");
        String applayPcNo = request.getParameter("applayPcNo");
        if("true".equals(applayPcNo)){
        	forward = applayPcNo(mapping, form, request, response);
        }else if ("true".equals(evalpost)) {
            forward = eval(mapping, form, request, response);
        } else {
            forward = evalPrepare(mapping, form, request, response);
        }
        return forward;
    }
    
    protected ActionForward applayPcNo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws
            Exception {
    	ActionForward forward = getForward(request, mapping, "alert");
		//������»�����
		ServerConnection conn = new ServerConnection(
        		BaseEnv.bol88URL+"/ClientConnection", new byte[] {0x21, 0x12,
                0xF, 0x58,
                (byte) 0x88, 0x10, 0x40, 0x38
                , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2});
		String posStr = "<operation>applyChangePcNo</operation>" +
    		"<dogId>" + SystemState.instance.dogId + "</dogId>"+
    		"<pcNo>" + request.getParameter("pcNo") + "</pcNo>"+
    		"<tel>" + request.getParameter("tel") + "</tel>";
        String ret = conn.send(posStr);
        if (ret != null && "ok".equals(getValue(ret, "result"))) {
        	EchoMessage.success().add("����ɹ�����ȴ���˽��������ϵ�ͷ�����").
        	setBackUrl("/RegisterAction.do?regFlag=2&from=offline&encryptionType=3").setNotAutoBack().
            setAlertRequest(request);
        	return forward;      	
        }else{
        	EchoMessage.error().add("�������,������ķ�������������Internet��ʹ����������").
        	setBackUrl("/RegisterAction.do?regFlag=2&from=offline&encryptionType=3").
            setAlertRequest(request);
        	return forward;        
        }
	
    	
    	
    }

    /**
     * ���ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward evalPrepare(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws
        Exception {
        //���ﲻ��Ч���Ƿ�������á���Ϊϵͳ�������ʼ��ʱ�������ý��顣
        request.setAttribute("regFlag",getParameter("regFlag",request));
        request.setAttribute("encryptionType", request.getParameter("encryptionType"));
        request.setAttribute("from",getParameter("from",request));
        if("3".equals(request.getParameter("encryptionType")) || "0".equals(request.getParameter("encryptionType"))){
    		Random rd = new Random();
	        rd.setSeed(System.currentTimeMillis());
	        int rdi = rd.nextInt(4000);    	        
	        String s =CallSoftDll.get(rdi+"");
	        System.out.println(s); 
	        byte[] bs= new byte[16] ;
	    	bs =CallSoftDll.hexStringToBytes(s);
	    	rdi +=5;
	    	for(int i=0;i<bs.length ;i++){    		
	    		bs[i] = (byte)(bs[i]-rdi);
	    		if(i>8){
	    			bs[i] =(byte)(bs[i] -2);
	    		}
	    	}    	
    		request.setAttribute("pcNo", CallSoftDll.bytesToHexString(bs));
    		
    		request.setAttribute("update", request.getParameter("update"));
    	}
        
        if("offline".equals(request.getParameter("from"))){
        	if("3".equals(request.getParameter("encryptionType")) ){
        		//�������������������£���ѯ���һ�θ���״̬
        		ServerConnection conn = new ServerConnection(
                		BaseEnv.bol88URL+"/ClientConnection", new byte[] {0x21, 0x12,
                        0xF, 0x58,
                        (byte) 0x88, 0x10, 0x40, 0x38
                        , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                        , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2});
        		String posStr = "<operation>queryChangePcNo</operation>" +
            		"<dogId>" + SystemState.instance.dogId + "</dogId>";
	            String ret = conn.send(posStr);
	            String msg = "��ǰ��δ����";
	            if (ret != null ) {
	            	String createTime = getValue(ret, "time");
	            	//createTime = BaseDateFormat.format(BaseDateFormat.parse(createTime, BaseDateFormat.yyyyMMddHHmmss2),BaseDateFormat.yyyyMMddHHmmss);
	            	String state = getValue(ret, "state");
	            	request.setAttribute("createTime", createTime);
	            	request.setAttribute("appstate", state);
	            }else{
	            	request.setAttribute("appstate", "");
	            }
        	}
        	
            String session = SystemState.instance.dogId+":"+System.currentTimeMillis();
    		byte[] b = ClientConnection.encryptMode(session.getBytes());
    		
            session = ClientConnection.bytesToHexString(b).toUpperCase();
            request.setAttribute("validcode", session);
        	
            request.setAttribute("regFlag","3");
            request.setAttribute("utype",getParameter("utype",request));
            return getForward(request, mapping, "offlineregister");
        }else if("two".equals(request.getParameter("step"))){        	
            return getForward(request, mapping, "registerTwo");
        }else if("continueEval".equals(request.getParameter("step"))){
        	mgt.continueEval();
        	SystemState.instance.dogState = SystemState.DOG_RESTART; //����������־            
            EchoMessage.success().add("ִ�гɹ�")
                	.setBackUrl("/forwardIndex.jsp").
                	setAlertRequest(request);
            return getForward(request, mapping, "alert");
        }else if("stopUser".equals(request.getParameter("step"))){
        	mgt.stopUser();
        	SystemState.instance.dogState = SystemState.DOG_RESTART; //����������־            
            EchoMessage.success().add("ִ�гɹ�")
                	.setBackUrl("/forwardIndex.jsp").
                	setAlertRequest(request);
            return getForward(request, mapping, "alert");
        }else{
        
            return getForward(request, mapping, "register");
        }
    }


    /**
     * ���ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward eval(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        Exception {
    	String encryptionType = request.getParameter("encryptionType");
    	request.setAttribute("encryptionType", encryptionType);
    	
        ActionForward forward = getForward(request, mapping, "alert");

        RegisterForm myForm = (RegisterForm) form;
        String id = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        if("offline".equals(request.getParameter("from")))
        {
        	if("true".equals(request.getParameter("updateDog"))){
        		//����֤��
        		ServerConnection conn = new ServerConnection(
                		BaseEnv.bol88URL+"/ClientConnection", new byte[] {0x21, 0x12,
                        0xF, 0x58,
                        (byte) 0x88, 0x10, 0x40, 0x38
                        , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                        , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2});
        		String posStr = "<operation>upgrade</operation>" +
            		"<dogId>" + SystemState.instance.dogId + "</dogId>";
	            String ret = conn.send(posStr);
	            if (ret != null && "ok".equals(getValue(ret, "result"))) {
	            	String restr = getValue(ret, "data");
	            	byte[] bs = SecurityLock2.hexStringToBytes(restr);
	            	if("2".equals(encryptionType)){
	            		//д���ܹ�
	            		if(!SecurityLock.writeDate(bs)){
	            			EchoMessage.error().add("����д���ܹ�����").setBackUrl("/RegisterAction.do?regFlag=2&from=offline&encryptionType="+encryptionType+"&utype="+getParameter("utype",request)).
	                        setAlertRequest(request);
	    	            	return forward; 
	            		}
	            	}else{
		            	FileOutputStream fos = new FileOutputStream("aio.cert");
		            	fos.write(bs);
		            	fos.close();
	            	}
	            	SystemState.instance.dogState = SystemState.DOG_RESTART; //����������־
	            	EchoMessage.success().add(getMessage(
	                        request, "common.msg.registerSuccess"))
	                        	.setBackUrl("/forwardIndex.jsp").
	                        	setAlertRequest(request);
	                        
	                    return forward;
	            }else{
	            	EchoMessage.error().add("��������,������ķ�������������Internet��ʹ����������").setBackUrl("/RegisterAction.do?regFlag=2&from=offline&encryptionType="+encryptionType+"&utype="+getParameter("utype",request)).
                    setAlertRequest(request);
	            	return forward;        
	            }
        	}else if("upload".equals(request.getParameter("updateDog"))){
        		//����ܵ�����ע��        		
        		byte bs[] = myForm.getCertFile()==null?null:myForm.getCertFile().getFileData();
        		if(bs==null){
        			 EchoMessage.error().add(getMessage(
 	                        request, "common.msg.offlineregisterWebFailture")).
 	                        setAlertRequest(request);
 	                return forward;
        		}
        		
        		if("2".equals(encryptionType)){
            		//д���ܹ�
            		if(!SecurityLock.writeDate(bs)){
            			EchoMessage.error().add("����д���ܹ�����").setBackUrl("/RegisterAction.do?regFlag=2&from=offline&encryptionType="+encryptionType+"&utype="+getParameter("utype",request)).
                        setAlertRequest(request);
    	            	return forward; 
            		}
            	}else{
	            	FileOutputStream fos = new FileOutputStream("aio.cert");
	            	fos.write(bs);
	            	fos.close();
            	}        		
        	}else{
        	
	            //����ע��.�Ƚ�ע���
	            String offlineNo= getParameter("offlineNo",request).trim();
	            String companyName= getParameter("companyName",request).trim();
	            String dogId = ""+SecurityLock.readKeyId();
	            String md5 = getMD5(dogId,companyName);
	            if(!offlineNo.equals(md5)){
	                //����ע����빫˾��Ϣ����
	                EchoMessage.error().add(getMessage(
	                        request, "common.msg.offlineregisterWebFailture")).
	                        setAlertRequest(request);
	                return forward;
	            }
	            
	            if (!SecurityLock.writeRegiste(date, companyName)) {
	            	EchoMessage.error().add("д���ܹ�����").
                    	setAlertRequest(request);
	            	return forward;    
	            }
        	}
        }else{
            ServerConnection conn = new ServerConnection(
            		BaseEnv.bol88URL+"/ClientConnection", new byte[] {0x21, 0x12,
                    0xF, 0x58,
                    (byte) 0x88, 0x10, 0x40, 0x38
                    , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                    , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2});

           
            String posStr = "<companyName>" + myForm.getCompanyName() +"</companyName>" + 
            	"<companyAddress>" +myForm.getCompanyAddress() +"</companyAddress>" + 
            	"<companyUrl>" + myForm.getCompanyUrl() +"</companyUrl>" + 
            	"<connectorName>" + myForm.getConnectorName() + "</connectorName>" + 
            	"<email>" +myForm.getEmail() + "</email>" +
	            "<qq>" + myForm.getQq() + "</qq>" + 
	            "<tel>" + myForm.getTel() +"</tel>" + 
	            "<date>" + date + "</date>";
            if (myForm.getEncryptionType() == 0) { //��Ѱ�ע��
                posStr = "<operation>free</operation>"+
                	"<pcNo>" + request.getParameter("pcNo") + "</pcNo>" + 
                	"<validCode>" + request.getParameter("validCode") + "</validCode>" + 
                	posStr;
                
                String ret = conn.send(posStr);
                BaseEnv.log.debug("RegisterAction.eval ret="+ret);
                //"<result>ok</result><evalNo>" + evalNo + "</evalNo>"
                
                if (ret != null && "ok".equals(getValue(ret, "result"))) {
                    String dogId = getValue(ret, "dogId");
                    String seriesPassword = getValue(ret, "seriesPassword");
                    
                    String restr = getValue(ret, "data");
                    byte[] bs = SecurityLock2.hexStringToBytes(restr);
                	FileOutputStream fos = new FileOutputStream("aio.cert");
                	fos.write(bs);
                	fos.close();
                	fos = new FileOutputStream("aio.cert.txt");
                	fos.write(("���к�:"+dogId+"\r\n����:"+seriesPassword).getBytes());
                	fos.close();
                	
                	SystemState.instance.dogState = SystemState.DOG_RESTART; //����������־
                                          
                	EchoMessage.success().add(getMessage(
                            request, "common.msg.registerSuccess"))
                            	.setBackUrl("/vm/upgrade/freeRegisterSuccess.jsp?dogId="+dogId+"&seriesPassword="+seriesPassword).
                            	setAlertRequest(request);    
                	
                    return forward;
                }else {
                	String reason = getValue(ret, "reason");
                	if(reason != null && reason.length() > 0){
                		EchoMessage.error().add(reason).
                                setAlertRequest(request);
                	}else{
                		EchoMessage.error().add(getMessage(
                            request, "common.msg.registerWebFailture")).
                            setAlertRequest(request);
                	}
                    return forward;
                }
                
            }else if (myForm.getEncryptionType() == 3) {
            	String replacePcNo = request.getParameter("replacePcNo");
            	if("true".equals(replacePcNo)){
            		//�ظ�ע���ѡ���������PCNo
            		String posStr2 = "<operation>applyChangePcNo</operation>" +
                		"<dogId>" + request.getParameter("seriesNo") + "</dogId>"+
                		"<pcNo>" + request.getParameter("pcNo") + "</pcNo>"+
                		"<tel>" + request.getParameter("tel") + "</tel>";
                    String ret2 = conn.send(posStr2);
                    if (ret2 != null && "ok".equals(getValue(ret2, "result"))) {
                    	//����֤��
                    	posStr2 = "<operation>upgrade</operation>" +
	                		"<dogId>" + request.getParameter("seriesNo") + "</dogId>";
	    	            ret2 = conn.send(posStr2);
	    	            if (ret2 != null && "ok".equals(getValue(ret2, "result"))) {
	    	            	String restr = getValue(ret2, "data");
	    	            	byte[] bs = SecurityLock2.hexStringToBytes(restr);
	    	            	
    		            	FileOutputStream fos = new FileOutputStream("aio.cert");
    		            	fos.write(bs);
    		            	fos.close();
    		            	try{
    		            	fos = new FileOutputStream("aio.cert.txt");
    	                	fos.write(("���к�:"+request.getParameter("seriesNo")+"\r\n����:"+request.getParameter("password")).getBytes());
    	                	fos.close();
    		            	}catch(Exception e){}
	    		            	
	    	            	SystemState.instance.dogState = SystemState.DOG_RESTART; //����������־
	    	            	EchoMessage.success().add("����ɹ��������ӿͷ������������ɹ�ˢ��֤�鼴������ʹ��")
	    	                        	.setBackUrl("/forwardIndex.jsp").
	    	                        	setAlertRequest(request);	    	                        
	    	                return forward;
	    	            }else{
	    	            	EchoMessage.error().add("����֤�����,������ķ�������������Internet��ʹ����������").
	                        	setAlertRequest(request);
	    	            	return forward;        
	    	            }
                    	
                    }else{
                    	EchoMessage.error().add("�������,������ķ�������������Internet��ʹ����������").
                    		setAlertRequest(request);
                    	return forward;     
                    }
            	}
            	
            	//�����ע��
                posStr = "<operation>newRegister</operation><encryptionType>3</encryptionType>" + posStr+
                	"<dogId>" + request.getParameter("seriesNo") + "</dogId>"+
                	"<password>" + request.getParameter("password") + "</password>"+
                	"<pcNo>" + request.getParameter("pcNo") + "</pcNo>";
                
                String ret = conn.send(posStr);
                BaseEnv.log.debug("RegisterAction.eval ret="+ret);
                if (ret != null && "ok".equals(getValue(ret, "result"))) {
                	String restr = getValue(ret, "data");
                	byte[] bs = SecurityLock2.hexStringToBytes(restr);
                	FileOutputStream fos = new FileOutputStream("aio.cert");
                	fos.write(bs);
                	fos.close();
                	fos = new FileOutputStream("aio.cert.txt");
                	fos.write(("���к�:"+request.getParameter("seriesNo")+"\r\n����:"+request.getParameter("password")).getBytes());
                	fos.close();
                	
                }else if (ret != null && "NoDataError".equals(getValue(ret, "result"))) {
                	EchoMessage.error().add("���кŲ����ڣ���ȷ�����Ƿ�������ȷ").
                            setAlertRequest(request);
                    return forward;                	
                }else if (ret != null && "PasswordError".equals(getValue(ret, "result"))) {
                	EchoMessage.error().add("����������벻��ȷ").
		                    setAlertRequest(request);
		            return forward;                	
		        }else if (ret != null && "HadRegisterError".equals(getValue(ret, "result"))) {
		        	String reason = getValue(ret,"reason");
		        	if("CompanyNameNoSame".equals(reason)){
		        		EchoMessage.error().add("�����к��Ѿ���ע�ᣬ��ע�ṫ˾������������Ĺ�˾����һ�£����鹫˾�����Ƿ���ȷ������Ƿ�ע��").
	                    	setAlertRequest(request);
		        		return forward;      	
		        	}else{
		        		
		        		EchoMessage.confirm().add("�����к��Ѿ���ע�ᣬ��������������֮һ������Ҫ���������֤�顣\\n1������������\\n2������������ĳһӲ��\\n3����װ����ϵͳ\\n �����������֤���밴��ȷ������Ť").
		                    setJsConfirmYes("parent.document.form.replacePcNo.value='true';parent.document.form.submit();").setAlertRequest(request);
		            	return forward; 
		        	}
		        }else {
                	EchoMessage.error().add(getMessage(
                            request, "common.msg.registerWebFailture")).
                            setAlertRequest(request);
                    return forward;
                }
                
            }else if (myForm.getEncryptionType() == 2) {
            	//�ڶ�������
                posStr = "<operation>newRegister</operation><encryptionType>2</encryptionType>" + posStr+
                	"<dogId>" + SecurityLock.readKeyId() + "</dogId>";
                
                String ret = conn.send(posStr);
                BaseEnv.log.debug("RegisterAction.eval ret="+ret);
                if (ret != null && "ok".equals(getValue(ret, "result"))) {
                	if (!SecurityLock.writeRegiste(date, myForm.getCompanyName())) {
		            	EchoMessage.error().add("д���ܹ�����").
	                    	setAlertRequest(request);
		            	return forward;    
		            }
                }else if (ret != null && "NoDataError".equals(getValue(ret, "result"))) {
                	EchoMessage.error().add("���ܹ��Ų����ڣ���ȷ�ϴ˹�Ϊ������������").
                            setAlertRequest(request);
                    return forward;                	
                }else {
                	EchoMessage.error().add(getMessage(
                            request, "common.msg.registerWebFailture")).
                            setAlertRequest(request);
                    return forward;
                }
                
            } else  {
                //��ʽ���ϼ��ܹ�Ҫ�ϴ�����
                String modStr = "";
                for (Object o : SystemState.instance.getModuleList()) {
                    modStr += o.toString() + ",";
                }
                if (modStr.length() > 0) {
                    modStr = modStr.substring(0, modStr.length() - 1);
                }
                id = SecurityLock.readKeyId() + "";
                //�����ú�

                posStr = "<operation>formal</operation><dogId>" + id +
                         "</dogId><modules>" + modStr +
                         "</modules><userNum>" + SystemState.instance.userNum +
                         "</userNum><languageNum>" + SystemState.instance.languageNum +
                         "</languageNum><userDefine>" +
                         SystemState.instance.funUserDefine + "</userDefine><moreCurrency>" +
                         SystemState.instance.funMoreCurrency +
                         "</moreCurrency><product>" + SystemState.instance.funProduct +
                         "</product><evalNo>" + "" +
                         "</evalNo>" +
                         posStr;

                String ret = conn.send(posStr);
                BaseEnv.log.debug("RegisterAction.eval ret="+ret);
                //ret = "<result>ok,doubleValue,Error</result>";
                if (ret == null) {
                    EchoMessage.error().add(getMessage(
                            request, "common.msg.registerWebFailture")).
                            setAlertRequest(request);
                    return forward;
                } else {
                    ret = getValue(ret, "result");
                    System.out.println("ret = " + ret);

                    if ("doubleValue".equals(ret)) {
                        EchoMessage.error().add(getMessage(
                                request, "common.msg.registerDoubleValue")).
                                setAlertRequest(request);
                        return forward;
                    } else if (!"ok".equals(ret)) {
                        EchoMessage.error().add(getMessage(
                                request, "common.msg.registerWebFailture")).
                                setAlertRequest(request);
                        return forward;
                    }
                }
                if(myForm.getEncryptionType() == 1 || myForm.getEncryptionType() == 2){
	                if (!SecurityLock.writeRegiste(date, myForm.getCompanyName())) {
		            	EchoMessage.error().add("д���ܹ�����").
	                    	setAlertRequest(request);
		            	return forward;    
		            }
                }
            }
        }
        
    	SystemState.instance.dogState = SystemState.DOG_RESTART; //����������־
        
        EchoMessage.success().add(getMessage(
            request, "common.msg.registerSuccess"))
            	.setBackUrl("/forwardIndex.jsp").
            	setAlertRequest(request);
            
        return forward;
    }

    public String getValue(String xml, String name) {
        try {
            return xml.substring(xml.indexOf("<" + name + ">") +
                                 ("<" + name + ">").length(),
                                 xml.indexOf("</" + name + ">"));
        } catch (Exception ex) {
            return null;
        }
    }

    private String getMD5(String dogId, String companyName) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((dogId + "KoronAIOOffLiNeReGiSter" + companyName).
                      getBytes("UTF-8"));
            byte[] bs = md.digest();
            return toHex(bs);
        } catch (Exception ex) {
            return null;
        }
    }

    private static String toHex(byte[] buffer) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
        }
        return sb.toString().toUpperCase();
    }
}
