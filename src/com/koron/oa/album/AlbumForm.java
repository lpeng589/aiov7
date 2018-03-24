package com.koron.oa.album;

import java.util.ArrayList;
import java.util.List;

import com.koron.oa.bean.OANewsInfoReplyBean;
import com.koron.oa.bean.PhotoInfoBean;
import com.menyi.web.util.BaseSearchForm;
/**
 * 
 * <p>Title:图片vo 用来存放数据  photoInfoBean 里面 因为存在 album的bean导致json 解析 构造函数无效等错误</p> 
 * <p>Description: </p>
 * @Date:2011-4-13
 * @Copyright: 科荣软件
 * @Author 毛晶
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
