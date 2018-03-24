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
@Table(name="OABBSVoteUser")
public class OABBSVoteUserBean {

	@Id
	private String id ;
	@ManyToOne  //(cascade=CascadeType.ALL)
	@JoinColumn(name="userId",referencedColumnName="id")
	private OABBSUserBean bbsUser ;	/*������*/
	private String voteId ;		/*ͶƱ��*/
	private String answerId ;			/*ͶƱ����*/
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAnswerId() {
		return answerId;
	}
	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}
	public OABBSUserBean getBbsUser() {
		return bbsUser;
	}
	public void setBbsUser(OABBSUserBean bbsUser) {
		this.bbsUser = bbsUser;
	}
	public String getVoteId() {
		return voteId;
	}
	public void setVoteId(String voteId) {
		this.voteId = voteId;
	}


}
