package com.koron.oa.publicMsg.newadvice;

import com.menyi.web.util.BaseForm;

/**
 * @author Administrator
 *
 */
public class OAAdviceForm extends BaseForm {

	private static final long serialVersionUID = 1L;
	private String adviceId;
	private String adviceType;
	private String adviceTitle;
	private String adviceContext;
	private String publisher;
	private String publishDate; 
	private int statusId;
	private String scompanyId;
	private String wakeupMode;
	private String accepter;
	private String isAlonePopedmon;
	private String popedomDeptIds;
	private String popedomEmpGroupIds;  
	private String isSaveReading;  // «∑Ò±£¥Ê‘ƒ∂¡∫€º£
	private String fileName;
	private String filePath;
	private String whetherAgreeReply;// «∑Ò‘ –Ì∆¿¬€
	public String getAdviceContext() {
		return adviceContext;
	}
	public void setAdviceContext(String adviceContext) {
		this.adviceContext = adviceContext;
	}
	public String getAdviceId() {
		return adviceId;
	}
	public void setAdviceId(String adviceId) {
		this.adviceId = adviceId;
	}
	public String getAdviceTitle() {
		return adviceTitle;
	}
	public void setAdviceTitle(String adviceTitle) {
		this.adviceTitle = adviceTitle;
	}
	public String getAdviceType() {
		return adviceType;
	}
	public void setAdviceType(String adviceType) {
		this.adviceType = adviceType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getIsAlonePopedmon() {
		return isAlonePopedmon;
	}
	public void setIsAlonePopedmon(String isAlonePopedmon) {
		this.isAlonePopedmon = isAlonePopedmon;
	}
	public String getIsSaveReading() {
		return isSaveReading;
	}
	public void setIsSaveReading(String isSaveReading) {
		this.isSaveReading = isSaveReading;
	}
	public String getPopedomDeptIds() {
		return popedomDeptIds;
	}
	public void setPopedomDeptIds(String popedomDeptIds) {
		this.popedomDeptIds = popedomDeptIds;
	}
	public String getPopedomEmpGroupIds() {
		return popedomEmpGroupIds;
	}
	public void setPopedomEmpGroupIds(String popedomEmpGroupIds) {
		this.popedomEmpGroupIds = popedomEmpGroupIds;
	}
	
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getScompanyId() {
		return scompanyId;
	}
	public void setScompanyId(String scompanyId) {
		this.scompanyId = scompanyId;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getWakeupMode() {
		return wakeupMode;
	}
	public void setWakeupMode(String wakeupMode) {
		this.wakeupMode = wakeupMode;
	}
	public String getWhetherAgreeReply() {
		return whetherAgreeReply;
	}
	public void setWhetherAgreeReply(String whetherAgreeReply) {
		this.whetherAgreeReply = whetherAgreeReply;
	}
	public String getAccepter() {
		return accepter;
	}
	public void setAccepter(String accepter) {
		this.accepter = accepter;
	}
}
