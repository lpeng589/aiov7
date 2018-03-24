package com.koron.mobile.bean;

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
public class MobileEmail {
	private String id;
	private String subject;
	private String content;
	private String type;
	private String contentMime;
	private String to;
	private String from;
	private String receivedTime;
	private boolean unread;
	private boolean attach;
	private String importance;
	private boolean intranet;
	private String folders;
	private String attrs;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public boolean isIntranet() {
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
	public boolean getAttach() {
		return attach;
	}
	public void setAttach(boolean attach) {
		this.attach = attach;
	}
	public String getAttrs() {
		return attrs;
	}
	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}
	public String getContentMime() {
		return contentMime;
	}
	public void setContentMime(String contentMime) {
		this.contentMime = contentMime;
	}
	public String getFolders() {
		return folders;
	}
	public void setFolders(String folders) {
		this.folders = folders;
	}
	public String getImportance() {
		return importance;
	}
	public void setImportance(String importance) {
		this.importance = importance;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isUnread() {
		return unread;
	}
	public void setUnread(boolean unread) {
		this.unread = unread;
	}

}