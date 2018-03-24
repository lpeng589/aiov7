package com.koron.oa.album;


import com.koron.oa.bean.AlbumBean;
/**
 * 
 * <p>Title:图片vo</p> 
 * <p>Description: </p>
 * @Date:2011-4-13
 * @Copyright: 科荣软件
 * @Author 毛晶
 * @preserve all
 */
public class PhotoInfoVo {

	private String id;
	private String tempName;//照片临时名称
	private String beginName;//照片开始名称
	private String phoneDesc;//照片描述
	private String createTime;//创建时间
	private String uploadBy;//上传人
	private String lastUpdateTime;//最后修改时间
	private Integer agreeNum = 0;//被赞的数量
	private Integer replyCount;//被点评的数量
	private AlbumBean album;//相册
	private Integer isCover; //--是否是相册封面 0不是 1是;
	private String isSaveReading;//,--是否保存阅读痕迹
	public String getIsSaveReading() {
		return isSaveReading;
	}
	public void setIsSaveReading(String isSaveReading) {
		this.isSaveReading = isSaveReading;
	}
	
	public Integer getAgreeNum() {
		return agreeNum;
	}
	public void setAgreeNum(Integer agreeNum) {
		this.agreeNum = agreeNum;
	}
	public AlbumBean getAlbum() {
		return album;
	}
	public void setAlbum(AlbumBean album) {
		this.album = album;
	}
	public String getBeginName() {
		return beginName;
	}
	public void setBeginName(String beginName) {
		this.beginName = beginName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getIsCover() {
		return isCover;
	}
	public void setIsCover(Integer isCover) {
		this.isCover = isCover;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getPhoneDesc() {
		return phoneDesc;
	}
	public void setPhoneDesc(String phoneDesc) {
		this.phoneDesc = phoneDesc;
	}
	public String getTempName() {
		return tempName;
	}
	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
	public String getUploadBy() {
		return uploadBy;
	}
	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
	}
	public Integer getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}
	
}
