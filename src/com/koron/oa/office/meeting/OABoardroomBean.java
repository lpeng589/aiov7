package com.koron.oa.office.meeting;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * <p>Title:h会议</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-14
 * @Copyright: 科荣软件
 * @Author kezhiliang
 * @preserve all
 */

@Entity
@Table(name="OABoardroom")
public class OABoardroomBean  implements Serializable{
	@Id
    private String boardroomId;
    private String boardroomName;
    private String address;
    private String describe;
   // private String device;
    private int peopleNumber;
    private String photopath;
	public String getBoardroomId() {
		return boardroomId;
	}
	public void setBoardroomId(String boardroomId) {
		this.boardroomId = boardroomId;
	}
	public String getBoardroomName() {
		return boardroomName;
	}
	public void setBoardroomName(String boardroomName) {
		this.boardroomName = boardroomName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public int getPeopleNumber() {
		return peopleNumber;
	}
	public void setPeopleNumber(int peopleNumber) {
		this.peopleNumber = peopleNumber;
	}
	public String getPhotopath() {
		return photopath;
	}
	public void setPhotopath(String photopath) {
		this.photopath = photopath;
	}
    
}
