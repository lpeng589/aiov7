package com.koron.oa.workflow;


import com.menyi.web.util.BaseForm;
import com.menyi.web.util.GlobalsTool;

/**
 * 
 * <p>Title:</p> 
 * <p>Description: </p>
 *
 * @Date:Dec 4, 2013
 * @Copyright: 深圳市科荣软件有限公司
 * @Author 钱小文
 */
public class OAMyWorkFlowForm extends BaseForm{

	private static final long serialVersionUID = 1L;
	private String beginTime;
	private String endTime;
	private String ebeginTime;
	private String eendTime;
	private String empFullName;
	private String deptFullName;
	private String deptCode;
	private String tableFullName;
	private String tableName;
	private String applyContent;
	private String approveStatus ;
	private String flowBelong;
	private String classCode;
	private String keyWord ;
	private String timeType;
	private String templateFile;
	
	private int pageNo=1;
    private int pageSize = GlobalsTool.getPageSize();
    private int totalPage;
		
    
    public String getTemplateFile() {
		return templateFile;
	}
	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}
	public String getFlowBelong() {
		return flowBelong;
	}
	public void setFlowBelong(String flowBelong) {
		this.flowBelong = flowBelong;
	}
	public String getApplyContent() {
		return applyContent;
	}
	public void setApplyContent(String applyContent) {
		this.applyContent = applyContent;
	}
	public String getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getDeptFullName() {
		return deptFullName;
	}
	public void setDeptFullName(String deptFullName) {
		this.deptFullName = deptFullName;
	}
	public String getEmpFullName() {
		return empFullName;
	}
	public void setEmpFullName(String empFullName) {
		this.empFullName = empFullName;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTableFullName() {
		return tableFullName;
	}
	public void setTableFullName(String tableFullName) {
		this.tableFullName = tableFullName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getEbeginTime() {
		return ebeginTime;
	}
	public void setEbeginTime(String ebeginTime) {
		this.ebeginTime = ebeginTime;
	}
	public String getEendTime() {
		return eendTime;
	}
	public void setEendTime(String eendTime) {
		this.eendTime = eendTime;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	
}
