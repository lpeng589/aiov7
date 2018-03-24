package com.koron.oa.mydesktop;

import org.apache.struts.upload.FormFile;

import com.menyi.web.util.BaseForm;


/**
 * 
 * <p>Title:上传我的头像</p> 
 * <p>Description: </p>
 *
 * @Date:Feb 14, 2011
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class UploadImageForm extends BaseForm {
	
	private FormFile imageFile ;

	public FormFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(FormFile imageFile) {
		this.imageFile = imageFile;
	}

	
}
