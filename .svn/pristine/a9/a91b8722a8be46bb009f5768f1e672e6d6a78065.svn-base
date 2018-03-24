package com.menyi.aio.bean;

import java.util.Iterator;
import java.util.Map;

import javax.persistence.*;

import org.dom4j.tree.AbstractEntity;


/**
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
@Table(name="tblGoodsPropEnumItem")
public class GoodsPropEnumItemBean extends AbstractEntity {
    @Id
     private String id;

     @Column(nullable=false,length=100)
     private String enumValue;
     @Column(nullable=false,length=4)
     private int isUsed;
     @Column(nullable=false,length = 30)
     private String SCompanyID;
     @ManyToOne(cascade=CascadeType.ALL)
     @JoinColumn(name="propId")
     private GoodsPropInfoBean propBean;
     @Column(nullable=false,length=100)
     private String groupName;
     @Column(nullable = true, length = 30)
     private String languageId;

     @Transient
     private KRLanguage display;



     public KRLanguage getDisplay() {
         return display;
     }

     public String getEnumValue() {
         return enumValue;
     }


     public String getId() {
         return id;
     }

    public int getIsUsed() {
        return isUsed;
    }

    public GoodsPropInfoBean getPropBean() {
         return propBean;
     }

    public String getLanguageId() {
        return languageId;
    }

    public void setId(String id) {
         this.id = id;
     }

     public void setEnumValue(String enumValue) {
         this.enumValue = enumValue;
     }

     public void setDisplay(KRLanguage display) {
         this.display = display;
     }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public void setPropBean(GoodsPropInfoBean propBean) {
         this.propBean = propBean;
     }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String handerDisplay(){
        return display.toString();
}

	public String getSCompanyID() {
		return SCompanyID;
	}

	public void setSCompanyID(String companyID) {
		SCompanyID = companyID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
