package com.menyi.aio.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <p>Title:单据编号历史记录</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: 科荣软件
 * @Author fjj
 * @preserve all
 */
@Entity
@Table(name="tblBillNoHistory")
public class TblBillNoHistoryBean {

	@Id
    private Integer id;						//id
	private String key;						//现在定规则为表名加字段名用下划线连接。例如：tblrole_id
	private Integer value;					//当前流水号值
	private String formatedString;			//系列号
	private long timestamp;					//获取数值的时间
	private Integer removed;				//0已使1空出来2已填空
	
	
	
	public String getFormatedString() {
		return formatedString;
	}
	public void setFormatedString(String formatedString) {
		this.formatedString = formatedString;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Integer getRemoved() {
		return removed;
	}
	public void setRemoved(Integer removed) {
		this.removed = removed;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	
}
