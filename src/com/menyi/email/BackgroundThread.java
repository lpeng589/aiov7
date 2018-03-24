package com.menyi.email;

import com.menyi.email.util.AIOEMail;
import com.menyi.email.util.EMailMessage;
import com.menyi.web.util.MailAutoReceive;
import com.menyi.web.util.NotifyFashion;

public class BackgroundThread  extends Thread{
	
	private AIOEMail sm;
	private String userId;
	private String emailType;
	public BackgroundThread(AIOEMail sm,String userId,String emailType){
		this.sm = sm;
		this.userId = userId;
		this.emailType = emailType;
		this.setName("BackgroundThreadForMail");
		this.start();
	}
	
	public void run(){
    	try{
	    	boolean hasMail = sm.newMsgList.size() >0;
	    	for(int i=sm.newMsgList.size()-1;i>=0;i--){		
	    		if(sm.isOpen()){
					EMailMessage em = sm.receiveNextHeader(); 
					MailAutoReceive.receiveContent(sm, userId, emailType);
	    		}
			}               					
			
			sm.closeInbox(); 	   
			if(hasMail){
				NotifyFashion notify = new NotifyFashion(userId,"RES<oa.mail.msg.newMail>","<a href=\"javascript:mdiwin('/EMailAction.do?operation=4','RES<oa.mail.myMail>')\">RES<oa.mail.msg.newMail></a>",userId,4,"OA","") ; 
				notify.sendAdvice(userId,"RES<oa.mail.msg.newMail>","<a href=\"javascript:mdiwin('/EMailAction.do?operation=4','RES<oa.mail.myMail>')\">RES<oa.mail.msg.newMail></a>","","","email");
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
