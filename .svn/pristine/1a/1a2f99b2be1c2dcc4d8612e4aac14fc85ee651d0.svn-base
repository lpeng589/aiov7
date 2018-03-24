package com.menyi.aio.web.mobile;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.*;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.SystemSettingBean;
import com.menyi.aio.web.customize.DBFieldInfoBean;
import com.menyi.web.util.*;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 * @preserve all
 */

public class FieldBean implements Serializable {

	private String id;
	private String tableName;

	private String fieldName;

	private byte isNull;

	private String defaultValue; //默认值

	private byte fieldType;//  字段数据类型，0：整形，1：浮点型，2字符，，3：大文本型，

	private int maxLength;//数据库的最大长度,与输入项的宽度不同

	private int width;

	private byte inputType;//  输入类型0：输入，1:枚举型，2：数据表中选择，3：从表中选择,4：关联显示，100： 不输入（用于如flag不在本表录入字段）

	private byte inputTypeOld;//存放最原始的输入类型，数据同上

	
	private String refEnumerationName;

	private String inputValue;

	private byte digits;

	private byte udType; //0 系统标准字段；1用户自定义字段，系统标准字段不永许修改，删除，表是系统标准表，但字段还是可能是自定义的

	private String display;//不同语言显示

	private List EnumItem;
	
	
	private ArrayList PopupDis;	

	public ArrayList getPopupDis() {
		return PopupDis;
	}

	public void setPopupDis(ArrayList popupDis) {
		PopupDis = popupDis;
	}

	public List getEnumItem() {
		return EnumItem;
	}

	public void setEnumItem(List enumItem) {
		EnumItem = enumItem;
	}

	/**
	 * 获取默认值
	 * @return String
	 */
	public String getDefValue() {
		if (defaultValue == null) {
			return "";
		} else if (defaultValue.startsWith("@DATE:")) {
			if (fieldType == DBFieldInfoBean.FIELD_DATE_SHORT) {
				return BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
			} else {
				return BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
			}
		} else if (defaultValue.startsWith("@MEM:")) {
			//内存变量
			SystemSettingBean bean = (SystemSettingBean) (BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
			return bean == null ? "" : bean.getSetting();
		} else if (defaultValue.startsWith("@CODE:")) {
			//编码规则
			return CodeGenerater.generater(defaultValue.substring("@CODE:".length()));
		} else {
			//常量
			return defaultValue;
		}
	}

	/**
	 * 获取默认值
	 * @return String
	 */
	public String getDefValue(Connection conn) {
		if (defaultValue == null) {
			return "";
		} else if (defaultValue.startsWith("@DATE:")) {
			if (fieldType == DBFieldInfoBean.FIELD_DATE_SHORT) {
				return BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
			} else {
				return BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
			}
		} else if (defaultValue.startsWith("@MEM:")) {
			//内存变量
			SystemSettingBean bean = (SystemSettingBean) (BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
			return bean == null ? "" : bean.getSetting();
		} else if (defaultValue.startsWith("@CODE:")) {
			//编码规则
			return CodeGenerater.generater(defaultValue.substring("@CODE:".length()), conn);
		} else {
			//常量
			return defaultValue;
		}
	}

	/**
	 * 获取某特定用户下的 默认变量值
	 * @param id
	 * @return
	 */
	public String getDefValue(String id) {
		if (defaultValue == null) {
			return "";
		} else if (defaultValue.startsWith("@DATE:")) {
			if (fieldType == DBFieldInfoBean.FIELD_DATE_SHORT) {
				return BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
			} else {
				return BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
			}
		} else if (defaultValue.startsWith("@MEM:")) {
			//内存变量
			SystemSettingBean bean = (SystemSettingBean) (BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
			return bean == null ? "" : bean.getSetting();
		} else if (defaultValue.startsWith("@CODE:")) {
			//编码规则
			return CodeGenerater.generater(defaultValue.substring("@CODE:".length()));
		} else if (defaultValue.startsWith("@Sess:")) {
			//用户变量值
			String sessStr = defaultValue.substring(defaultValue.indexOf(":") + 1);
			String sessValue = "";
			if (((Hashtable) BaseEnv.sessionSet.get(id)).get(sessStr) != null) {
				sessValue = ((Hashtable) BaseEnv.sessionSet.get(id)).get(sessStr).toString();
			}
			return sessValue;
		} else {
			//常量
			return defaultValue;
		}
	}

	public String getDefValue(String id, Connection conn) {
		if (defaultValue == null) {
			return "";
		} else if (defaultValue.startsWith("@DATE:")) {
			if (fieldType == DBFieldInfoBean.FIELD_DATE_SHORT) {
				return BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMdd);
			} else {
				return BaseDateFormat.format(new Date(), BaseDateFormat.yyyyMMddHHmmss);
			}
		} else if (defaultValue.startsWith("@MEM:")) {
			//内存变量
			SystemSettingBean bean = (SystemSettingBean) (BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
			return bean == null ? "" : bean.getSetting();
		} else if (defaultValue.startsWith("@CODE:")) {
			//编码规则
			return CodeGenerater.generater(defaultValue.substring("@CODE:".length()), conn);
		} else if (defaultValue.startsWith("@Sess:")) {
			//用户变量值
			String sessStr = defaultValue.substring(defaultValue.indexOf(":") + 1);
			String sessValue = "";
			if (((Hashtable) BaseEnv.sessionSet.get(id)).get(sessStr) != null) {
				sessValue = ((Hashtable) BaseEnv.sessionSet.get(id)).get(sessStr).toString();
			}
			return sessValue;
		} else {
			//常量
			return defaultValue;
		}
	}

	public String getSringNull() {
		return isNull == DBFieldInfoBean.IS_NULL ? "true" : "false";
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public byte getDigits() {
		return digits;
	}

	public void setDigits(byte digits) {
		this.digits = digits;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public byte getFieldType() {
		return fieldType;
	}

	public void setFieldType(byte fieldType) {
		this.fieldType = fieldType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte getInputType() {
		return inputType;
	}

	public void setInputType(byte inputType) {
		this.inputType = inputType;
	}

	public byte getInputTypeOld() {
		return inputTypeOld;
	}

	public void setInputTypeOld(byte inputTypeOld) {
		this.inputTypeOld = inputTypeOld;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public byte getIsNull() {
		return isNull;
	}

	public void setIsNull(byte isNull) {
		this.isNull = isNull;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getRefEnumerationName() {
		return refEnumerationName;
	}

	public void setRefEnumerationName(String refEnumerationName) {
		this.refEnumerationName = refEnumerationName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public byte getUdType() {
		return udType;
	}

	public void setUdType(byte udType) {
		this.udType = udType;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
