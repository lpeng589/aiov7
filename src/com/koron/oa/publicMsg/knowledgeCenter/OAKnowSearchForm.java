package com.koron.oa.publicMsg.knowledgeCenter;

import com.menyi.web.util.BaseSearchForm;


/**
 * 
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:2012-6-13
 * @Copyright: 科荣软件
 * @Author 方家俊
 */

public class OAKnowSearchForm extends BaseSearchForm{
	
	private String keyWord;
	private String folderCode;
	private String folderCodeOld;
	private String queryType;
	private int term;
	private String fileTitleSearch;   //标题搜索
	private String fileNameSearch;	  //文件名搜索
	private String descriptSearch;    //内容
	private String beginTimeSearch;  //创建开始时间
	private String endTimeSearch;    //创建结束时间
	private String createBySearch;    //创建者Id
	private String groupIdSearch;     //所属组
	private String groupNameSearch;   //所属组名称
	private String proUserName;		 //创建人名称
	
	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getFolderCode() {
		return folderCode;
	}

	public void setFolderCode(String folderCode) {
		this.folderCode = folderCode;
	}

	public String getFolderCodeOld() {
		return folderCodeOld;
	}

	public void setFolderCodeOld(String folderCodeOld) {
		this.folderCodeOld = folderCodeOld;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public String getDescriptSearch() {
		return descriptSearch;
	}

	public void setDescriptSearch(String descriptSearch) {
		this.descriptSearch = descriptSearch;
	}

	public String getFileNameSearch() {
		return fileNameSearch;
	}

	public void setFileNameSearch(String fileNameSearch) {
		this.fileNameSearch = fileNameSearch;
	}

	public String getFileTitleSearch() {
		return fileTitleSearch;
	}

	public void setFileTitleSearch(String fileTitleSearch) {
		this.fileTitleSearch = fileTitleSearch;
	}

	public String getBeginTimeSearch() {
		return beginTimeSearch;
	}

	public void setBeginTimeSearch(String beginTimeSearch) {
		this.beginTimeSearch = beginTimeSearch;
	}

	public String getCreateBySearch() {
		return createBySearch;
	}

	public void setCreateBySearch(String createBySearch) {
		this.createBySearch = createBySearch;
	}

	public String getEndTimeSearch() {
		return endTimeSearch;
	}

	public void setEndTimeSearch(String endTimeSearch) {
		this.endTimeSearch = endTimeSearch;
	}

	public String getGroupIdSearch() {
		return groupIdSearch;
	}

	public void setGroupIdSearch(String groupIdSearch) {
		this.groupIdSearch = groupIdSearch;
	}

	public String getGroupNameSearch() {
		return groupNameSearch;
	}

	public void setGroupNameSearch(String groupNameSearch) {
		this.groupNameSearch = groupNameSearch;
	}

	public String getProUserName() {
		return proUserName;
	}

	public void setProUserName(String proUserName) {
		this.proUserName = proUserName;
	}
	
}
