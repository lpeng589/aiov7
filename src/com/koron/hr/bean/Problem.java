package com.koron.hr.bean;

import java.util.List;

public class Problem {
	
	private String id;
	private int serialNum;
	private String titleType;
	private String title;
	private String answerType;
	private List Answers;
	private String userOption;
	private int score;
	
	public String getUserOption() {
		return userOption;
	}
	public void setUserOption(String userOption) {
		this.userOption = userOption;
	}
	public int getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}
	public List getAnswers() {
		return Answers;
	}
	public void setAnswers(List answers) {
		Answers = answers;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleType() {
		return titleType;
	}
	public void setTitleType(String titleType) {
		this.titleType = titleType;
	}
	public String getAnswerType() {
		return answerType;
	}
	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
}
