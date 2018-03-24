package com.menyi.aio.bean;

import java.io.Serializable;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;


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
@Table(name="tblUnit")
public class UnitBean implements Serializable {

    /** identifier field */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /** persistent field */
    @Column(nullable = false )
    private String unitName;
    @Column(nullable = true )
    private String remark;




    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public Long getId() {
        return id;
    }

    public String getRemark() {
        return remark;
    }

    public String getUnitName() {
        return unitName;
    }





    public void setId(Long id) {
        this.id = id;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }



}
