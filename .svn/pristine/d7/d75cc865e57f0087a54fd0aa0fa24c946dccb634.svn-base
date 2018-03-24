package com.menyi.aio.web.customize;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.*;

import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.bean.KRLanguage;
import com.menyi.aio.bean.SystemSettingBean;
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

@Entity
@Table(name="tblDBFieldInfo")
public class DBFieldInfoBean implements Serializable{
    public static final byte FIELD_INT=0; //整形
    public static final byte FIELD_DOUBLE=1; //浮点
    public static final byte FIELD_ANY=2; //任意字符
    public static final byte FIELD_TEXT=3; //文本
    public static final byte FIELD_ENGLISH=4; //英文字符
    public static final byte FIELD_DATE_SHORT=5; //10位日期  8位
    public static final byte FIELD_DATE_LONG=6; //19位日期 14位
    public static final byte FIELD_IP=7; //  ip地址30位
    public static final byte FIELD_EMAIL=8;  //email 50位
    public static final byte FIELD_TEL=9; //电话 20位
    public static final byte FIELD_MOBILE=10; //手机20位
    public static final byte FIELD_PHONE=11; //电话
    public static final byte FIELD_URL=12; //url 50位
    public static final byte FIELD_PIC=13;  //图片
    public static final byte FIELD_AFFIX=14;  //附件
    public static final byte FIELD_ZIP=15;  //邮编 15位
    public static final byte FIELD_HTML=16;  //邮编 15位
    public static final byte FIELD_DATE_TIME=17;//时间(时，分，秒)
    public static final byte FIELD_ONETEXT=18;//占一行的输入文本
    public static final byte FIELD_PASSWORD=19;//密码
    public static final byte FIELD_DOUBLE_TEXT=20;//中字符 占两个普通文本框的宽度
   
    public static final byte INPUT_NORMAL=0;
    public static final byte INPUT_ENUMERATE=1;
    public static final byte INPUT_MAIN_TABLE=2;
    public static final byte INPUT_HIDDEN=3;
    public static final byte INPUT_NO=100;
    public static final byte INPUT_LANGUAGE=4;
    public static final byte INPUT_CHECKBOX=5;//复选框
    public static final byte INPUT_HIDDEN_INPUT=6;//隐藏可显示
    public static final byte INPUT_PYM=7; //输入拼音码
    public static final byte INPUT_ONLY_READ=8;//只读
    public static final byte INPUT_JISUAN=9;//计算含有四则运算的表达式的值
    public static final byte INPUT_RADIO=10;//单选框
    public static final byte INPUT_FUNCTION=11; //功能方法
    public static final byte INPUT_DEPARTMENT=14; //部门弹出框
    public static final byte INPUT_EMPLOYEE=15; //职员弹出框
    public static final byte INPUT_DOWNLOAD_SELECT=16; //下拉选择
    public static final byte INPUT_CUT_LINE=17; //分隔线
    
    public static final byte USER_TYPE=0;
    public static final byte SYSTEM_TYPE=1;

    public static final byte IS_NULL=0;
    public static final byte NOT_NULL=1;

    public static final byte UNIQUE_NO=0;
    public static final byte UNIQUE_YES=1;

    public static final byte STAT_NO=0;
    public static final byte STAT_YES=1;
    
    
    public static final String FIELD_IDENTIFIER="BillNo";		//字段标识-单据编号
    
    @Id
    private String id;
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="tableId")
    private DBTableInfoBean tableBean; //对应主表
    @Column(nullable=false,length=50)
    private String fieldName;

    private byte isCopy;
    private short listOrder;
    private byte isNull;
    private byte isReAudit;//复核
    private byte isUnique;
    private byte isStat;
    @Column(nullable=true,length=100)
    private String defaultValue; //默认值
    private byte fieldType;//  字段数据类型，0：整形，1：浮点型，2字符，，3：大文本型，
    private int maxLength;//数据库的最大长度,与输入项的宽度不同
    private int width;
    private byte inputType;//  输入类型0：输入，1:枚举型，2：数据表中选择，3：从表中选择,4：关联显示，100： 不输入（用于如flag不在本表录入字段）
    private byte inputTypeOld;//存放最原始的输入类型，数据同上
    //当输入类型为枚举型时，这里指明枚举代号,
    @Column(nullable=true,length=50)       
    private String refEnumerationName;

    @Column(nullable=true,length=3000)
    private String inputValue;

    private byte digits;

    @Column(nullable=true,length=8000)
    private String calculate; //计算公式

    @Column(nullable=true,length=100)
    private String logicValidate; //计算公式

    private byte udType; //0 系统标准字段；1用户自定义字段，系统标准字段不永许修改，删除，表是系统标准表，但字段还是可能是自定义的

    @Transient
    private KRLanguage display;//不同语言显示

    @Column(nullable=true,length=100)
    private String fieldSysType; //字段系统类型（是否是开账后不能录开账前的标识，是否是启用外币核算时显示字段的标识）
    @Column(nullable=true,length=100)
    private String fieldIdentityStr; //标识该字段是否要统计条数
    @Column(nullable=true,length=100)
    private String copyType ;	//复制上一行的标识 
    @Column(nullable=false)
    private int statusId;	//CRM启用控制 
    @Column(nullable=true,length=50)
    private String popupType ;
    public String getSringDefault(){
        return defaultValue != null && defaultValue.trim().length()>0?"true":"false";
    }

    @Column(nullable = true, length = 30)
    private String languageId;
    
    
    @Transient
    private KRLanguage groupDisplay;//分组多语言显示
    
    @Column(nullable = true, length = 50)
    private String groupName; //分组多语言代号
    
    @Column(nullable = true, length = 100)
    private String insertTable; //分组多语言代号
    
    @Column(nullable=false)
    private byte isLog; //是否记录日志
    
    @Column(nullable=false)
    private Byte isMobile=2; //是否记录日志
    
   
    
    @Transient
    private List EnumItem;	
	
    @Transient
	private ArrayList PopupDis;	
		
    
    public List getEnumItem() {
		return EnumItem;
	}

	public void setEnumItem(List enumItem) {
		EnumItem = enumItem;
	}

	public ArrayList getPopupDis() {
		return PopupDis;
	}

	public void setPopupDis(ArrayList popupDis) {
		PopupDis = popupDis;
	}

	public Byte getIsMobile() {
		return isMobile;
	}

	public void setIsMobile(Byte isMobile) {
		this.isMobile = isMobile;
	}

	public KRLanguage getGroupDisplay() {
		return groupDisplay;
	}

	public void setGroupDisplay(KRLanguage groupDisplay) {
		this.groupDisplay = groupDisplay;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
     * 获取默认值
     * @return String
     */
    public String getDefValue(){
        if(defaultValue == null) {
            return "";
        }else if(defaultValue.startsWith("@DATE:")){
            if(fieldType == FIELD_DATE_SHORT){
                return BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
            }else{
                return BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
            }
        }else if(defaultValue.startsWith("@MEM:")){
            //内存变量
            SystemSettingBean bean = (SystemSettingBean)(BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
            return bean == null?"":bean.getSetting();
        }else if(defaultValue.startsWith("@CODE:")){
            //编码规则
            return CodeGenerater.generater(defaultValue.substring("@CODE:".length()));
        }else{
            //常量
            return defaultValue;
        }
    }
    
    /**
     * 获取默认值
     * @return String
     */
    public String getDefValue(Connection conn){
        if(defaultValue == null) {
            return "";
        }else if(defaultValue.startsWith("@DATE:")){
            if(fieldType == FIELD_DATE_SHORT){
                return BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
            }else{
                return BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
            }
        }else if(defaultValue.startsWith("@MEM:")){
            //内存变量
            SystemSettingBean bean = (SystemSettingBean)(BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
            return bean == null?"":bean.getSetting();
        }else if(defaultValue.startsWith("@CODE:")){
            //编码规则
            return CodeGenerater.generater(defaultValue.substring("@CODE:".length()),conn);
        }else{
            //常量
            return defaultValue;
        }
    }
    
    
    /**
     * 获取某特定用户下的 默认变量值
     * @param id
     * @return
     */
    public String getDefValue(String id){
        if(defaultValue == null) {
            return "";
        }else if(defaultValue.startsWith("@DATE:")){
            if(fieldType == FIELD_DATE_SHORT){
                return BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
            }else{
                return BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
            }
        }else if(defaultValue.startsWith("@MEM:")){
            //内存变量
            SystemSettingBean bean = (SystemSettingBean)(BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
            return bean == null?"":bean.getSetting();
        }else if(defaultValue.startsWith("@CODE:")){
            //编码规则
            return CodeGenerater.generater(defaultValue.substring("@CODE:".length()));
        }else if(defaultValue.startsWith("@Sess:")){
        	//用户变量值
        	String sessStr = defaultValue.substring(defaultValue.indexOf(":")+1) ;
        	String sessValue="";
        	if(((Hashtable)BaseEnv.sessionSet.get(id)).get(sessStr)!=null){
        		sessValue= ((Hashtable)BaseEnv.sessionSet.get(id)).get(sessStr).toString() ;
        	}
    		return sessValue ;
        }else{
        	if(fieldType == FIELD_DATE_SHORT && !defaultValue.equals("")){
                return BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
            }else if(fieldType == FIELD_DATE_LONG && !defaultValue.equals("")){
                return BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
            }
            //常量
            return defaultValue;
        }
    }
    public String getDefValue(String id,Connection conn){
        if(defaultValue == null) {
            return "";
        }else if(defaultValue.startsWith("@DATE:")){
            if(fieldType == FIELD_DATE_SHORT){
                return BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMdd);
            }else{
                return BaseDateFormat.format(new Date(),BaseDateFormat.yyyyMMddHHmmss);
            }
        }else if(defaultValue.startsWith("@MEM:")){
            //内存变量
            SystemSettingBean bean = (SystemSettingBean)(BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
            return bean == null?"":bean.getSetting();
        }else if(defaultValue.startsWith("@CODE:")){
            //编码规则
            return CodeGenerater.generater(defaultValue.substring("@CODE:".length()),conn);
        }else if(defaultValue.startsWith("@Sess:")){
        	//用户变量值
        	String sessStr = defaultValue.substring(defaultValue.indexOf(":")+1) ;
        	String sessValue="";
        	if(((Hashtable)BaseEnv.sessionSet.get(id)).get(sessStr)!=null){
        		sessValue= ((Hashtable)BaseEnv.sessionSet.get(id)).get(sessStr).toString() ;
        	}
    		return sessValue ;
        }else{
            //常量
            return defaultValue;
        }
    }

    public String getSringNull(){
        return isNull == IS_NULL?"true":"false";
    }
    public String getStringLength(){
        if(maxLength>0){
            return maxLength+"";
        }else{
            return "maxValue";
        }
    }

    public String getStringType() {
        switch (fieldType) {
        case FIELD_INT:
            return "int";
        case FIELD_DOUBLE:
            return "double";
        case FIELD_ANY:
            return "any";
        case FIELD_TEXT:
            return "any";
        case FIELD_ENGLISH:
            return "en";
        case FIELD_DATE_SHORT:
            return "date";
        case FIELD_DATE_LONG:
            return "datetime";
        case FIELD_IP:
            return "ip";
        case FIELD_EMAIL:
            return "mail";
        case FIELD_TEL:
            return "tel";
        case FIELD_MOBILE:
            return "mobile";
        case FIELD_URL:
            return "url";
        case FIELD_ZIP:
            return "zip";
        case FIELD_PIC:
            return "pic";
        case FIELD_PHONE:
            return "phone";
        case FIELD_AFFIX:
            return "affix";
        case FIELD_DATE_TIME:
            return "time";
        case FIELD_DOUBLE_TEXT:
        	return "any";
        case FIELD_HTML:
        	return "html";
        default:
            return "any";
        }
    }

    public PopupSelectBean getSelectBean(){
        return (PopupSelectBean)BaseEnv.popupSelectMap.get(inputValue);
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getId() {
        return id;
    }

    public byte getFieldType() {
        return fieldType;
    }
    public byte getInputType() {
        return inputType;
    }
    public byte getIsNull() {
        return isNull;
    }
    public short getListOrder() {
        return listOrder;
    }
    public DBTableInfoBean getTableBean() {
        return tableBean;
    }
    public byte getUdType() {
        return udType;
    }
    public String getRefEnumerationName() {

        return refEnumerationName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public KRLanguage getDisplay() {
        return display;
    }



    public String getCalculate() {
        return calculate;
    }

    public String getInputValue() {
        return inputValue;
    }

    public int getWidth() {
        return width;
    }

    public byte getIsUnique() {
        return isUnique;
    }

    public byte getIsStat() {
        return isStat;
    }

    public String getFieldSysType() {
        return fieldSysType;
    }

    public byte getInputTypeOld() {
        return inputTypeOld;
    }

    public byte getDigits() {
        return digits;
    }

    public String getFieldIdentityStr() {
        return fieldIdentityStr;
    }


    public void setId(String id) {
        this.id = id;
    }



    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }




    public void setUdType(byte udType) {
        this.udType = udType;
    }

    public void setTableBean(DBTableInfoBean tableBean) {
        this.tableBean = tableBean;
    }
    public void setListOrder(short listOrder) {
        this.listOrder = listOrder;
    }

    public void setIsNull(byte isNull) {
        this.isNull = isNull;
    }

    public void setInputType(byte inputType) {
        this.inputType = inputType;
    }

    public void setFieldType(byte fieldType) {
        this.fieldType = fieldType;
    }

    public void setRefEnumerationName(String refEnumerationName) {

        this.refEnumerationName = refEnumerationName;
    }

    public void setDisplay(KRLanguage display) {
        this.display = display;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }



    public void setCalculate(String calculate) {
        this.calculate = calculate;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setIsUnique(byte isUnique) {
        this.isUnique = isUnique;
    }

    public void setIsStat(byte isStat) {
        this.isStat = isStat;
    }

    public void setFieldSysType(String fieldSysType) {
        this.fieldSysType = fieldSysType;
    }

    public void setInputTypeOld(byte inputTypeOld) {
        this.inputTypeOld = inputTypeOld;
    }

    public void setDigits(byte digits) {
        this.digits = digits;
    }

    public void setFieldIdentityStr(String fieldIdentityStr) {
        this.fieldIdentityStr = fieldIdentityStr;
    }

	public String getLogicValidate() {
		return logicValidate;
	}

    public String getLanguageId() {
        return languageId;
    }

    public void setLogicValidate(String logicValidate) {
		this.logicValidate = logicValidate;
	}

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

	public String getPopupType() {
		return popupType;
	}

	public void setPopupType(String popupType) {
		this.popupType = popupType;
	}

	public String getInsertTable() {
		return insertTable;
	}

	public void setInsertTable(String insertTable) {
		this.insertTable = insertTable;
	}

	public byte getIsCopy() {
		return isCopy;
	}

	public void setIsCopy(byte isCopy) {
		this.isCopy = isCopy;
	}

	public String getCopyType() {
		return copyType;
	}

	public void setCopyType(String copyType) {
		this.copyType = copyType;
	}

	public byte getIsReAudit() {
		return isReAudit;
	}

	public void setIsReAudit(byte isReAudit) {
		this.isReAudit = isReAudit;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
    
	//以下提供各模块使用一些常用静态方法
	/**
	 * 返回字段类型的字符串
	 * 如果字段是NULL，则返回空字符串
	 * @param fi 字段BEAN
	 * @return
	 */
	public static final String getFieldTypeString(DBFieldInfoBean fi)
	{
		if(fi==null)
			return "";
		switch (fi.getFieldType()) {
         case DBFieldInfoBean.FIELD_INT:
             return "int";
         case DBFieldInfoBean.FIELD_DOUBLE:
             return "float";
         case DBFieldInfoBean.FIELD_DATE_LONG:
         case DBFieldInfoBean.FIELD_DATE_SHORT:
             return "date";
         default:
             return "string";
         }
	}

	public byte getIsLog() {
		return isLog;
	}

	public void setIsLog(byte isLog) {
		this.isLog = isLog;
	}
	
	public String handerDisplay(){
        return display==null?"":display.toString();
    }

	public String toString(){
		return fieldName;
	}
}
