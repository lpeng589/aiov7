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
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 * @preserve all
 */

@Entity
@Table(name="tblDBFieldInfo")
public class DBFieldInfoBean implements Serializable{
    public static final byte FIELD_INT=0; //����
    public static final byte FIELD_DOUBLE=1; //����
    public static final byte FIELD_ANY=2; //�����ַ�
    public static final byte FIELD_TEXT=3; //�ı�
    public static final byte FIELD_ENGLISH=4; //Ӣ���ַ�
    public static final byte FIELD_DATE_SHORT=5; //10λ����  8λ
    public static final byte FIELD_DATE_LONG=6; //19λ���� 14λ
    public static final byte FIELD_IP=7; //  ip��ַ30λ
    public static final byte FIELD_EMAIL=8;  //email 50λ
    public static final byte FIELD_TEL=9; //�绰 20λ
    public static final byte FIELD_MOBILE=10; //�ֻ�20λ
    public static final byte FIELD_PHONE=11; //�绰
    public static final byte FIELD_URL=12; //url 50λ
    public static final byte FIELD_PIC=13;  //ͼƬ
    public static final byte FIELD_AFFIX=14;  //����
    public static final byte FIELD_ZIP=15;  //�ʱ� 15λ
    public static final byte FIELD_HTML=16;  //�ʱ� 15λ
    public static final byte FIELD_DATE_TIME=17;//ʱ��(ʱ���֣���)
    public static final byte FIELD_ONETEXT=18;//ռһ�е������ı�
    public static final byte FIELD_PASSWORD=19;//����
    public static final byte FIELD_DOUBLE_TEXT=20;//���ַ� ռ������ͨ�ı���Ŀ��
   
    public static final byte INPUT_NORMAL=0;
    public static final byte INPUT_ENUMERATE=1;
    public static final byte INPUT_MAIN_TABLE=2;
    public static final byte INPUT_HIDDEN=3;
    public static final byte INPUT_NO=100;
    public static final byte INPUT_LANGUAGE=4;
    public static final byte INPUT_CHECKBOX=5;//��ѡ��
    public static final byte INPUT_HIDDEN_INPUT=6;//���ؿ���ʾ
    public static final byte INPUT_PYM=7; //����ƴ����
    public static final byte INPUT_ONLY_READ=8;//ֻ��
    public static final byte INPUT_JISUAN=9;//���㺬����������ı��ʽ��ֵ
    public static final byte INPUT_RADIO=10;//��ѡ��
    public static final byte INPUT_FUNCTION=11; //���ܷ���
    public static final byte INPUT_DEPARTMENT=14; //���ŵ�����
    public static final byte INPUT_EMPLOYEE=15; //ְԱ������
    public static final byte INPUT_DOWNLOAD_SELECT=16; //����ѡ��
    public static final byte INPUT_CUT_LINE=17; //�ָ���
    
    public static final byte USER_TYPE=0;
    public static final byte SYSTEM_TYPE=1;

    public static final byte IS_NULL=0;
    public static final byte NOT_NULL=1;

    public static final byte UNIQUE_NO=0;
    public static final byte UNIQUE_YES=1;

    public static final byte STAT_NO=0;
    public static final byte STAT_YES=1;
    
    
    public static final String FIELD_IDENTIFIER="BillNo";		//�ֶα�ʶ-���ݱ��
    
    @Id
    private String id;
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="tableId")
    private DBTableInfoBean tableBean; //��Ӧ����
    @Column(nullable=false,length=50)
    private String fieldName;

    private byte isCopy;
    private short listOrder;
    private byte isNull;
    private byte isReAudit;//����
    private byte isUnique;
    private byte isStat;
    @Column(nullable=true,length=100)
    private String defaultValue; //Ĭ��ֵ
    private byte fieldType;//  �ֶ��������ͣ�0�����Σ�1�������ͣ�2�ַ�����3�����ı��ͣ�
    private int maxLength;//���ݿ����󳤶�,��������Ŀ�Ȳ�ͬ
    private int width;
    private byte inputType;//  ��������0�����룬1:ö���ͣ�2�����ݱ���ѡ��3���ӱ���ѡ��,4��������ʾ��100�� �����루������flag���ڱ���¼���ֶΣ�
    private byte inputTypeOld;//�����ԭʼ���������ͣ�����ͬ��
    //����������Ϊö����ʱ������ָ��ö�ٴ���,
    @Column(nullable=true,length=50)       
    private String refEnumerationName;

    @Column(nullable=true,length=3000)
    private String inputValue;

    private byte digits;

    @Column(nullable=true,length=8000)
    private String calculate; //���㹫ʽ

    @Column(nullable=true,length=100)
    private String logicValidate; //���㹫ʽ

    private byte udType; //0 ϵͳ��׼�ֶΣ�1�û��Զ����ֶΣ�ϵͳ��׼�ֶβ������޸ģ�ɾ��������ϵͳ��׼�����ֶλ��ǿ������Զ����

    @Transient
    private KRLanguage display;//��ͬ������ʾ

    @Column(nullable=true,length=100)
    private String fieldSysType; //�ֶ�ϵͳ���ͣ��Ƿ��ǿ��˺���¼����ǰ�ı�ʶ���Ƿ���������Һ���ʱ��ʾ�ֶεı�ʶ��
    @Column(nullable=true,length=100)
    private String fieldIdentityStr; //��ʶ���ֶ��Ƿ�Ҫͳ������
    @Column(nullable=true,length=100)
    private String copyType ;	//������һ�еı�ʶ 
    @Column(nullable=false)
    private int statusId;	//CRM���ÿ��� 
    @Column(nullable=true,length=50)
    private String popupType ;
    public String getSringDefault(){
        return defaultValue != null && defaultValue.trim().length()>0?"true":"false";
    }

    @Column(nullable = true, length = 30)
    private String languageId;
    
    
    @Transient
    private KRLanguage groupDisplay;//�����������ʾ
    
    @Column(nullable = true, length = 50)
    private String groupName; //��������Դ���
    
    @Column(nullable = true, length = 100)
    private String insertTable; //��������Դ���
    
    @Column(nullable=false)
    private byte isLog; //�Ƿ��¼��־
    
    @Column(nullable=false)
    private Byte isMobile=2; //�Ƿ��¼��־
    
   
    
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
     * ��ȡĬ��ֵ
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
            //�ڴ����
            SystemSettingBean bean = (SystemSettingBean)(BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
            return bean == null?"":bean.getSetting();
        }else if(defaultValue.startsWith("@CODE:")){
            //�������
            return CodeGenerater.generater(defaultValue.substring("@CODE:".length()));
        }else{
            //����
            return defaultValue;
        }
    }
    
    /**
     * ��ȡĬ��ֵ
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
            //�ڴ����
            SystemSettingBean bean = (SystemSettingBean)(BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
            return bean == null?"":bean.getSetting();
        }else if(defaultValue.startsWith("@CODE:")){
            //�������
            return CodeGenerater.generater(defaultValue.substring("@CODE:".length()),conn);
        }else{
            //����
            return defaultValue;
        }
    }
    
    
    /**
     * ��ȡĳ�ض��û��µ� Ĭ�ϱ���ֵ
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
            //�ڴ����
            SystemSettingBean bean = (SystemSettingBean)(BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
            return bean == null?"":bean.getSetting();
        }else if(defaultValue.startsWith("@CODE:")){
            //�������
            return CodeGenerater.generater(defaultValue.substring("@CODE:".length()));
        }else if(defaultValue.startsWith("@Sess:")){
        	//�û�����ֵ
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
            //����
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
            //�ڴ����
            SystemSettingBean bean = (SystemSettingBean)(BaseEnv.systemSet.get(defaultValue.substring("@MEM:".length())));
            return bean == null?"":bean.getSetting();
        }else if(defaultValue.startsWith("@CODE:")){
            //�������
            return CodeGenerater.generater(defaultValue.substring("@CODE:".length()),conn);
        }else if(defaultValue.startsWith("@Sess:")){
        	//�û�����ֵ
        	String sessStr = defaultValue.substring(defaultValue.indexOf(":")+1) ;
        	String sessValue="";
        	if(((Hashtable)BaseEnv.sessionSet.get(id)).get(sessStr)!=null){
        		sessValue= ((Hashtable)BaseEnv.sessionSet.get(id)).get(sessStr).toString() ;
        	}
    		return sessValue ;
        }else{
            //����
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
    
	//�����ṩ��ģ��ʹ��һЩ���þ�̬����
	/**
	 * �����ֶ����͵��ַ���
	 * ����ֶ���NULL���򷵻ؿ��ַ���
	 * @param fi �ֶ�BEAN
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
