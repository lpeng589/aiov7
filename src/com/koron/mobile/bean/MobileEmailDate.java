package com.koron.mobile.bean;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Feb 22, 2012
 * @Copyright: 科荣软件
 * @Author 文小钱
 * @preserve all
 */
public class MobileEmailDate {
	private String id;
	private String subject;
	private String content;
	private String contentMime;
	private String to;
	private String from;
	private String cc;
	private String receivedTime;
	private boolean intranet;
	private boolean unread;
	private boolean attach;
	private int importance;
	private List<MobileAttrs> attrs;
	private List<HashMap<String, String>> listAttach;	// 附件列表，[{name：附件名，path：下载路径},{}]
	private String jpg;
	public String getJpg() {
		return jpg;
	}
	public void setJpg(String jpg) {
		this.jpg = jpg;
	}
	public boolean getAttach() {
		return attach;
	}
	public void setAttach(boolean attach) {
		this.attach = attach;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentMime() {
		return contentMime;
	}
	public void setContentMime(String contentMime) {
		this.contentMime = contentMime;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getImportance() {
		return importance;
	}
	public void setImportance(int importance) {
		this.importance = importance;
	}
	public boolean getIntranet() {
		return intranet;
	}
	public void setIntranet(boolean intranet) {
		this.intranet = intranet;
	}
	public String getReceivedTime() {
		return receivedTime;
	}
	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public boolean getUnread() {
		return unread;
	}
	public void setUnread(boolean unread) {
		this.unread = unread;
	}
	public List<MobileAttrs> getAttrs() {
		return attrs;
	}
	public void setAttrs(List<MobileAttrs> attrs) {
		this.attrs = attrs;
	}

	public List<HashMap<String, String>> getListAttach() {
		return listAttach;
	}
	public void setListAttach(List<HashMap<String, String>> listAttach) {
		this.listAttach = listAttach;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	
}
