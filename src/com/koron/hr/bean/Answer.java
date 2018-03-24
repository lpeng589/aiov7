package com.koron.hr.bean;

public class Answer {
	
	private String id;
	private String description;
	private char sign;
	private char answerMarke;
	private String isCorrect;
	private boolean userAnswer;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String isCorrect() {
		return isCorrect;
	}
	public void setCorrect(String isCorrect) {
		this.isCorrect = isCorrect;
	}
	public char getAnswerMarke() {
		return answerMarke;
	}
	public void setAnswerMarke(char answerMarke) {
		this.answerMarke = answerMarke;
	}
	public String getIsCorrect() {
		return isCorrect;
	}
	public void setIsCorrect(String isCorrect) {
		this.isCorrect = isCorrect;
	}
	public boolean isUserAnswer() {
		return userAnswer;
	}
	public void setUserAnswer(boolean userAnswer) {
		this.userAnswer = userAnswer;
	}
	public char getSign() {
		return sign;
	}
	public void setSign(char sign) {
		this.sign = sign;
	}

}
