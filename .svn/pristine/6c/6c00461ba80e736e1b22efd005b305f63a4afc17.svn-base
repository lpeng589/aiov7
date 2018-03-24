package com.koron.oa.bean;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * <p>Title: 投票答案选项</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-23
 * @Copyright: 科荣软件
 * @Author 文小钱
 * @preserve all
 */
@Entity
@Table(name="OABBSVoteAnswer")
public class OABBSVoteAnswerBean {

	@Id
	private String id ;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="voteId")		/*投票主题ID*/
	private OABBSVoteBean voteBean ;	
	private String voteAnswer ;		/*投票答案*/
	private int voteCount ;			/*投票总数*/
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVoteAnswer() {
		return voteAnswer;
	}
	public void setVoteAnswer(String voteAnswer) {
		this.voteAnswer = voteAnswer;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	public OABBSVoteBean getVoteBean() {
		return voteBean;
	}
	public void setVoteBean(OABBSVoteBean voteBean) {
		this.voteBean = voteBean;
	}

}
