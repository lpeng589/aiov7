package com.koron.oa.bbs.forum;

import java.util.List;

import org.apache.struts.upload.FormFile;

import com.menyi.web.util.BaseForm;
/**
 * 
 * <p>Title:��̳ ���ӹ���</p> 
 * <p>Description: </p>
 *
 * @Date:2011-4-12
 * @Copyright: �������
 * @Author ��СǮ
 */
public class OABBSUserForm extends BaseForm{

	private String nickName ;
	private String signature ;
	private FormFile imageFile;
	
	public FormFile getImageFile() {
		return imageFile;
	}
	public void setImageFile(FormFile imageFile) {
		this.imageFile = imageFile;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
		
}
