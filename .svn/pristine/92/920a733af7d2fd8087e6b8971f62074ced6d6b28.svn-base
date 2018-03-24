package com.koron.crm.printSet;

import java.io.Serializable;
import javax.persistence.*;
import org.dom4j.tree.AbstractEntity;

/**
 * 
 * <p>Title:版块</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-14
 * @Copyright: 科荣软件
 * @Author wyy
 * @preserve all
 */
@Entity
@Table(name="CRMPrintSetting")
public class CRMPrintSetBean extends AbstractEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String moduleName; //快递单模板名称
	private String status;//操作状态
	private String settingDetail;//打印设置参数
	private String ref_moduleId; //关联模板Id
	private String ref_moduleViewId; // 关联模板视图
	private String createBy; //创建人
	
	
	public String getRef_moduleId() {
		return ref_moduleId;
	}
	public void setRef_moduleId(String ref_moduleId) {
		this.ref_moduleId = ref_moduleId;
	}
	public String getRef_moduleViewId() {
		return ref_moduleViewId;
	}
	public void setRef_moduleViewId(String ref_moduleViewId) {
		this.ref_moduleViewId = ref_moduleViewId;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSettingDetail() {
		return settingDetail;
	}
	public void setSettingDetail(String settingDetail) {
		this.settingDetail = settingDetail;
	}
	

}
