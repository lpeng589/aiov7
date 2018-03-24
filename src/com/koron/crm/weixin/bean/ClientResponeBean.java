package com.koron.crm.weixin.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @preserve all
 * 
 */

public class ClientResponeBean extends ClientMessageBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	ArrayList<String> clientInfos; // 客户list clientId\nclientName

	ArrayList<String> followUpContList; //联系记录内容list

	public ArrayList<String> getClientInfos() {
		return clientInfos;
	}

	public void setClientInfos(ArrayList<String> clientInfos) {
		this.clientInfos = clientInfos;
	}

	public ArrayList<String> getFollowUpContList() {
		return followUpContList;
	}

	public void setFollowUpContList(ArrayList<String> followUpContList) {
		this.followUpContList = followUpContList;
	}

	
	
}
