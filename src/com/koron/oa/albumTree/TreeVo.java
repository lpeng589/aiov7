package com.koron.oa.albumTree;

/**
 * 
 * <p>Title:tree vo</p> 
 * <p>Description: </p>
 * @Date:2011-4-13
 * @Copyright: �������
 * @Author ë��
 * @preserve all
 */
public class TreeVo {

	private String tempName;//��ʱ����
	private String lastUpdateTime;//����޸�ʱ��
	private Long fileSize;//�ļ���С
	private String path;//·��
	private Integer tNo;//��� ��չʾͼƬ�����ŵ�ʱ������
	private Integer width;
	private Integer height;
	
	private String fileDesc;//	�ļ���������
	
	private String fileIcon;//����ͼ��·��
	
	
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
