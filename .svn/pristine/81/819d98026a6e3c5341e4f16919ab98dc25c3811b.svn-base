/*
 * Created on 2004-4-26
 */
package com.koron.oa.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * <p>Title:相册信息</p> 
 * <p>Description: </p>
 * @Date:2011-4-13
 * @Copyright: 科荣软件
 * @Author 毛晶
 * @preserve all
 */
@Entity
@Table(name = "tblAlbum")
public class AlbumBean {
	@Id
	@Column(nullable = false, length = 60)
	private String id;
	private String name;//相册名称
	private String albumDesc;//相册描述
	private String createTime;//创建时间
	private String createBy;//创建人
	private String lastUpdateTime;//最后修改时间
	private String cover;//相册封面
	private Integer agreeNum = 0;//被顶次数
	private String isSaveReading;//,--是否保存阅读痕迹
	@Transient
	private Integer totalReplyNum;//总回复数
	@Transient
	private Integer totalPhotoNum;//总照片数
	
	
	public Integer getTotalPhotoNum() {
		return totalPhotoNum;
	}
	public void setTotalPhotoNum(Integer totalPhotoNum) {
		this.totalPhotoNum = totalPhotoNum;
	}
	public Integer getTotalReplyNum() {
		return totalReplyNum;
	}
	public void setTotalReplyNum(Integer totalReplyNum) {
		this.totalReplyNum = totalReplyNum;
	}
	public String getIsSaveReading() {
		return isSaveReading;
	}
	public void setIsSaveReading(String isSaveReading) {
		this.isSaveReading = isSaveReading;
	}
	public AlbumBean(String id, String name, String albumDesc, String createTime, String createBy, String lastUpdateTime, String cover, Integer agreeNum) {
		super();
		this.id = id;
		this.name = name;
		this.albumDesc = albumDesc;
		this.createTime = createTime;
		this.createBy = createBy;
		this.lastUpdateTime = lastUpdateTime;
		this.cover = cover;
		this.agreeNum = agreeNum;
	}
	public AlbumBean() {
		super();
	}
	
	public Integer getAgreeNum() {
		return agreeNum;
	}
	public void setAgreeNum(Integer agreeNum) {
		this.agreeNum = agreeNum;
	}
	public String getAlbumDesc() {
		return albumDesc;
	}
	public void setAlbumDesc(String albumDesc) {
		this.albumDesc = albumDesc;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
