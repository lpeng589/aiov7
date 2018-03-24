package com.menyi.aio.bean;

import javax.persistence.*;

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
@Table(name="tblReportsDet")
public class ReportsDetBean {
    @Id
    @Column(nullable=false,length=30)
    private String id;
    @ManyToOne
    @JoinColumn(name="f_ref")
    private ReportsBean referBean; //参考的主表代号
    @Column(nullable=false,length=100)
    private String FormatFileName;
    @Column(nullable=false,length=100)
    private String FormatName;
    @Column(nullable=true,length=10)
    private String newFlag;
    @Column(nullable=true,length=10)
    private String languageType ;
    @Column(nullable=true,length=8000)
    private String userIds ;
    @Column(nullable=true,length=8000)
    private String deptIds ;
    
    @Column(nullable=true,length=1000)
    private String filterSql ;
    
    
    public String getFilterSql() {
		return filterSql;
	}

	public void setFilterSql(String filterSql) {
		this.filterSql = filterSql;
	}

	public String getFormatFileName() {
        return FormatFileName;
    }

    public String getId() {
        return id;
    }

    public ReportsBean getReferBean() {
        return referBean;
    }

    public String getNewFlag() {
        return newFlag;
    }

    public String getFormatName() {
        return FormatName;
    }

    public void setFormatFileName(String FormatFileName) {
        this.FormatFileName = FormatFileName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReferBean(ReportsBean referBean) {
        this.referBean = referBean;
    }

    public void setNewFlag(String newFlag) {
        this.newFlag = newFlag;
    }

    public void setFormatName(String FormatName) {
        this.FormatName = FormatName;
    }

	public String getLanguageType() {
		return languageType;
	}

	public void setLanguageType(String languageType) {
		this.languageType = languageType;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
}
