package com.koron.oa.albumTree;

import java.util.ArrayList;
import java.util.List;

import com.koron.oa.bean.OANewsInfoReplyBean;
import com.koron.oa.bean.PhotoInfoBean;
import com.menyi.web.util.BaseSearchForm;
/**
 * 
 * <p>Title:form
 * <p>Description: </p>
 * @Date:2011-4-13
 * @Copyright: ¿ÆÈÙÈí¼þ
 * @Author Ã«¾§
 * @preserve all
 */

public class AlbumTreeForm extends BaseSearchForm {
	private static final long serialVersionUID = 1L;
	private int key ;
	private int sortType;
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public int getSortType() {
		return sortType;
	}
	public void setSortType(int sortType) {
		this.sortType = sortType;
	}
	
	
}
