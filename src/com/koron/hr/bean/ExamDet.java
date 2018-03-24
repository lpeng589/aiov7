package com.koron.hr.bean;

import java.util.TreeMap;

public class ExamDet {

	private String id;//�����id
	private String f_ref;
	private int totalProblem;//��Ŀ������ȷ�Ĵ������ڿ�����ϸ��
	private String createBy;//������
	private String createTime;//����ʱ��
	private int limitTime; //����ʱ��
	private String startTime;
	private String endTime;
	private TreeMap<Integer, Problem> problemMap;
	
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
	public int getTotalProblem() {
		return totalProblem;
	}
	public void setTotalProblem(int totalProblem) {
		this.totalProblem = totalProblem;
	}
	public String getF_ref() {
		return f_ref;
	}
	public void setF_ref(String f_ref) {
		this.f_ref = f_ref;
	}
	public int getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(int limitTime) {
		this.limitTime = limitTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public TreeMap<Integer, Problem> getProblemMap() {
		return problemMap;
	}
	public void setProblemMap(TreeMap<Integer, Problem> problemMap) {
		this.problemMap = problemMap;
	}
}
