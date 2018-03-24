package com.koron.oa.bean;

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
@Table(name="OAMailGroup")
public class OAMailGroupBean implements Serializable {

    /** identifier field */
    @Id
    @Column(nullable = false, length = 50)
    private String id;
    /** persistent field */
    @Column(nullable = false )
    private String groupName;
    @Column(nullable = false )
    private String userId;
    @Column(nullable = true )
    private String account;




    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public String getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUserId() {
        return userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setAccount(String account) {
        this.account = account;
    }


}
