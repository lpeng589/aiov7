package com.koron.oa.mydesktop;

import org.apache.struts.upload.FormFile;

import com.menyi.web.util.BaseForm;


/**
 * 
 * <p>Title:�ϴ��ҵ�ͷ��</p> 
 * <p>Description: </p>
 *
 * @Date:Feb 14, 2011
 * @Copyright: �������
 * @Author ��СǮ
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
