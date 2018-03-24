package com.koron.oa.albumTree;

/**
 * 
 * <p>Title:tree vo</p> 
 * <p>Description: </p>
 * @Date:2011-4-13
 * @Copyright: 科荣软件
 * @Author 毛晶
 * @preserve all
 */
public class TreeVo {

	private String tempName;//临时名称
	private String lastUpdateTime;//最后修改时间
	private Long fileSize;//文件大小
	private String path;//路径
	private Integer tNo;//编号 在展示图片上下张的时候有用
	private Integer width;
	private Integer height;
	
	private String fileDesc;//	文件类型描述
	
	private String fileIcon;//类型图标路径
	
	
	public String getFileDesc() {
		return fileDesc;
	}
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}
	public String getFileIcon() {
		return fileIcon;
	}
	public void setFileIcon(String fileIcon) {
		this.fileIcon = fileIcon;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getTNo() {
		return tNo;
	}
	public void setTNo(Integer no) {
		tNo = no;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getTempName() {
		return tempName;
	}
	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
}
