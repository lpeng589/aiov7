package com.koron.oa.bean;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * <p>Title: ͶƱ��ѡ��</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-23
 * @Copyright: �������
 * @Author ��СǮ
 * @preserve all
 */
@Entity
@Table(name="OABBSVoteAnswer")
public class OABBSVoteAnswerBean {

	@Id
	private String id ;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="voteId")		/*ͶƱ����ID*/
	private OABBSVoteBean voteBean ;	
	private String voteAnswer ;		/*ͶƱ��*/
	private int voteCount ;			/*ͶƱ����*/
	
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
