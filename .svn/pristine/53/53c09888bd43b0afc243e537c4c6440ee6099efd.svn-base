package com.menyi.aio.web.customize;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.menyi.aio.bean.KRLanguage;


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
@Table(name="tblDBTableInfo")
public class DBTableInfoBean implements Serializable{
    public static final byte MAIN_TABLE=0;
    public static final byte CHILD_TABLE=1;
    public static final byte BROTHER_TABLE=2;

    public static final byte USER_TYPE=0;
    public static final byte SYSTEM_TYPE=1;

    public static final byte CAN_UPDATE=0;
    public static final byte NOT_UPDATE=1;


    @Id
    private String id;
    @Column(nullable=false,length=50)
    private String tableName;
    private byte tableType; //0为主单据形式，1为明细表形式

    @Column(nullable=true,length=50) 
    private String perantTableName;//当为明细表时，指明参考主表的代号,自动关联到主表的ID字段
    private byte udType; //0 系统标准表；1用户自定义表，系统标准表不永许修改，删除
    private byte updateAble;//0:用户可以修改此表，1：用户不可以修改此表， 用户表都可以修改，系统表部分基础数据不允许修改，部分可以修改，如商品表可以加字段等

    @Transient
    private KRLanguage display;

    @Column(nullable=false,length=30)
    private String createBy;
    @Column(nullable=false,length=14)
    private String createTime;
    @Column(nullable=true,length=30)
    private String lastUpdateBy;
    @Column(nullable=true,length=14)
    private String lastUpdateTime;
    @OneToMany(mappedBy="tableBean",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("listOrder")
    private List<DBFieldInfoBean> fieldInfos;

    @Column(nullable=true,length=3000)
    private String fieldCalculate;
    @Column(nullable=true)
    private String approveFlow;
    @Column(nullable=true)
    private String approveField;
    @Column(nullable=true)
    private int defRowCount;
    @Column(nullable=true)
    private int isSunCmpShare;//分支机构是否可以共享该表，0表示不可以，1表示可以
    @Column(nullable=true)
    private int isBaseInfo;//是否基础资料，0不是，1是
    @Column(nullable=true)
    private int needsCopy;//是否需要复制，0不需要，1需要
    @Column(nullable=true)
    private int wakeUp ; //是否支持通信提醒 0不需要，1需要
    @Column(nullable=true)
    private int hasNext; //是否支持上一篇，下一篇
    @Column(nullable=true)
    private int isView ;//是否是视图
    @Column(nullable=true)
    private int isNull ;//是否为空
    @Column(nullable=true)
    private int reAudit ;//是否支持复核
    @Column(nullable=true)
    private int isLayout ; //是否个性化布局
    @Column(nullable=true)
    private String layoutHTML ;//个性化布局HTML
    @Column(nullable=true)
    private String tableSysType ;//系统类型
    @Column(nullable=true)
    private byte classFlag;//macson add,0为否,1为是,是否支持分类
    @Column(nullable=true)
    private int classCount;//类别级数
    @Column(nullable=true)
    private byte draftFlag;//fhq add 0 为否，1为是，是否可以被存为草稿
    @Column(nullable=true,length=3000)
    private String extendButton;

    @Column(nullable=true,length=200)
    private String sysParameter;

    @Column(nullable = true, length = 30)
    private String languageId;
    
    @Column(nullable = true )
    private int triggerExpress;
    
    @Column(nullable = true, length = 1000)
    private String tableDesc;
    
    @Column(nullable = true, length = 10)
    private String MainModule;
    
    private String relationTable;//用于视图，视图不可以设置人员权限，设置一个和此视图相关联的表，那么视图就可以使用此表的权限
    
    private String relationView="";//和这个表相关联的视图的信息，在加载的时候分析，便于在使用时直接取数据
     
    private int isUsed=0;//是否启用，为0表示不启用该表，如明细表不启用，则界面上不再出现该表
    
    private int tWidth;//表单宽高
    private int tHeight;//表单宽高
    private int brotherType;//邻居表类型，0，一对一，1，一对多
    
    private int copyParent;
    
    

    public int getCopyParent() {
		return copyParent;
	}
	public void setCopyParent(int copyParent) {
		this.copyParent = copyParent;
	}
	public int getBrotherType() {
		return brotherType;
	}
	public void setBrotherType(int brotherType) {
		this.brotherType = brotherType;
	}
	public int getTHeight() {
		return tHeight;
	}
	public void setTHeight(int height) {
		tHeight = height;
	}
	public int getTWidth() {
		return tWidth;
	}
	public void setTWidth(int width) {
		tWidth = width;
	}
	public int getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}
	public String getRelationTable() {
		return relationTable;
	}
	public void setRelationTable(String relationTable) {
		this.relationTable = relationTable;
	}
	public int getTriggerExpress() {
		return triggerExpress;
	}
	public void setTriggerExpress(int triggerExpress) {
		this.triggerExpress = triggerExpress;
	}
	public void setFieldCalculate(String fieldCalculate){
        this.fieldCalculate = fieldCalculate;
    }
    public String getFieldCalculate(){
        return fieldCalculate;
    }
    
    public String getTableDesc() {
		return tableDesc;
	}
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	public String getCreateBy() {
        return createBy;
    }
    public String getCreateTime() {
        return createTime;
    }
    public String getId() {
        return id;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getTableName() {
        return tableName;
    }

    public String getPerantTableName() {
        return perantTableName;
    }

    public byte getTableType() {
        return tableType;
    }

    public byte getUdType() {
        return udType;
    }

    public byte getUpdateAble() {
        return updateAble;
    }


    public KRLanguage getDisplay() {
        return display;
    }

    public String getApproveFlow() {
        return approveFlow;
    }

    public String getApproveField() {
        return approveField;
    }

    public byte getClassFlag() {
        return classFlag;
    }

    public byte getDraftFlag() {
        return draftFlag;
    }

    public String getExtendButton() {
        return extendButton;
    }

    public int getDefRowCount() {
        return defRowCount;
    }

    public String getSysParameter() {
        return sysParameter;
    }

    public int getIsSunCmpShare() {
        return isSunCmpShare;
    }

    public int getIsBaseInfo() {
        return isBaseInfo;
    }

    public int getNeedsCopy() {
        return needsCopy;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWakeUp() {
		return wakeUp;
	}
	public void setWakeUp(int wakeUp) {
		this.wakeUp = wakeUp;
	}
	public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUdType(byte udType) {
        this.udType = udType;
    }

    public void setTableType(byte tableType) {
        this.tableType = tableType;
    }

    public void setPerantTableName(String perantTableName) {
        this.perantTableName = perantTableName;
    }

    public void setDisplay(KRLanguage display) {
        this.display = display;
    }

    public void setUpdateAble(byte updateAble) {
        this.updateAble = updateAble;
    }

    public void setApproveFlow(String approveFlow) {
        this.approveFlow = approveFlow;
    }

    public void setApproveField(String approveField) {
        this.approveField = approveField;
    }

    public void setClassFlag(byte classFlag) {
        this.classFlag = classFlag;
    }

    public void setDraftFlag(byte draftFlag) {
        this.draftFlag = draftFlag;
    }

    public void setExtendButton(String extendButton) {
        this.extendButton = extendButton;
    }

    public void setDefRowCount(int defRowCount) {
        this.defRowCount = defRowCount;
    }

    public void setSysParameter(String sysParameter) {
        this.sysParameter = sysParameter;
    }

    public void setIsSunCmpShare(int isSunCmpShare) {
        this.isSunCmpShare = isSunCmpShare;
    }

    public void setIsBaseInfo(int isBaseInfo) {
        this.isBaseInfo = isBaseInfo;
    }

    public void setNeedsCopy(int needsCopy) {
        this.needsCopy = needsCopy;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }
	public int getClassCount() {
		return classCount;
	}
	public void setClassCount(int classCount) {
		this.classCount = classCount;
	}
	public String getTableSysType() {
		return tableSysType;
	}
	public void setTableSysType(String tableSysType) {
		this.tableSysType = tableSysType;
	}
	public List<DBFieldInfoBean> getFieldInfos() {
		return fieldInfos;
	}
	public void setFieldInfos(List<DBFieldInfoBean> fieldInfos) {
		this.fieldInfos = fieldInfos;
	}
	public int getHasNext() {
		return hasNext;
	}
	public void setHasNext(int hasNext) {
		this.hasNext = hasNext;
	}
	public int getIsView() {
		return isView;
	}
	public void setIsView(int isView) {
		this.isView = isView;
	}
	public String getRelationView() {
		return relationView;
	}
	public void setRelationView(String relationView) {
		this.relationView = relationView;
	}
	public int getIsLayout() {
		return isLayout;
	}
	public void setIsLayout(int isLayout) {
		this.isLayout = isLayout;
	}
	public String getLayoutHTML() {
		return layoutHTML;
	}
	public void setLayoutHTML(String layoutHTML) {
		this.layoutHTML = layoutHTML;
	}
	public int getIsNull() {
		return isNull;
	}
	public void setIsNull(int isNull) {
		this.isNull = isNull;
	}
	public int getReAudit() {
		return reAudit;
	}
	public void setReAudit(int reAudit) {
		this.reAudit = reAudit;
	}
	public String getMainModule() {
		return MainModule;
	}
	public void setMainModule(String mainModule) {
		MainModule = mainModule;
	}
	
	public String handerDisplay(){
        return display==null?"":display.toString();
    }
	
}
