package com.koron.oa.album;

import java.util.ArrayList;
import java.util.List;

import com.koron.oa.bean.OANewsInfoReplyBean;
import com.koron.oa.bean.PhotoInfoBean;
import com.menyi.web.util.BaseSearchForm;
/**
 * 
 * <p>Title:ͼƬvo �����������  photoInfoBean ���� ��Ϊ���� album��bean����json ���� ���캯����Ч�ȴ���</p> 
 * <p>Description: </p>
 * @Date:2011-4-13
 * @Copyright: �������
 * @Author ë��
 * @preserve all
 */

public class AlbumForm extends BaseSearchForm {
	private static final long serialVersionUID = 1L;
	PhotoInfoBean photo = new PhotoInfoBean();
	List<OANewsInfoReplyBean> replys = new ArrayList<OANewsInfoReplyBean>();
	
	private String aaa ;


	public String getAaa() {
		return aaa;
	}


	public void setAaa(String aaa) {
		this.aaa = aaa;
	}


	public PhotoInfoBean getPhoto() {
		return photo;
	}


	public void setPhoto(PhotoInfoBean photo) {
		this.photo = photo;
	}


	public List<OANewsInfoReplyBean> getReplys() {
		return replys;
	}


	public void setReplys(List<OANewsInfoReplyBean> replys) {
		this.replys = replys;
	}
	
	
	
}
