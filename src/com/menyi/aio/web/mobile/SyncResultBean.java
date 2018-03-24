package com.menyi.aio.web.mobile;

import com.koron.wechat.common.util.BaseResultBean;
/**
 * 返回同步结果
 * @author Administrator
 *
 */
public class SyncResultBean extends BaseResultBean{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
