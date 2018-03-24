package com.menyi.aio.bean;

import javax.persistence.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company:深圳市盟毅科技有限公司 </p>
 *
 * @author 王强
 * @version 1.0
 * @preserve all
 */

@Entity
@Table(name="tblGoodsAccProp")
public class GoodsAccPropBean {

    /** identifier field */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, length=50)
    private String goodsAccMethod;

    @Column(nullable = false, length=2)
    private int accID;

    @Column(nullable = false, length=2)
    private int incomeAccID;

    @Column(nullable = false, length=2)
    private int expendAccID;

    @Column(nullable = true, length=255)
    private String remark;

    @Column(nullable = true)
    private int createBy;

    @Column(nullable = true)
    private String createDatetime;

    @Column(nullable = true)
    private int lstUpdateBy;

    @Column(nullable = true)
    private String lstUpdateDatetime;

    public GoodsAccPropBean() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGoodsAccMethod(String goodsAccMethod) {
        this.goodsAccMethod = goodsAccMethod;
    }

    public void setAccID(int accID) {

        this.accID = accID;
    }

    public void setIncomeAccID(int incomeAccID) {

        this.incomeAccID = incomeAccID;
    }

    public void setExpendAccID(int expendAccID) {

        this.expendAccID = expendAccID;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public void setLstUpdateBy(int lstUpdateBy) {
        this.lstUpdateBy = lstUpdateBy;
    }

    public void setLstUpdateDatetime(String lstUpdateDatetime) {
        this.lstUpdateDatetime = lstUpdateDatetime;
    }

    public Integer getId() {
        return id;
    }

    public String getGoodsAccMethod() {
        return goodsAccMethod;
    }

    public int getAccID() {

        return accID;
    }

    public int getIncomeAccID() {

        return incomeAccID;
    }

    public int getExpendAccID() {

        return expendAccID;
    }

    public String getRemark() {
        return remark;
    }

    public int getCreateBy() {
        return createBy;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public int getLstUpdateBy() {
        return lstUpdateBy;
    }

    public String getLstUpdateDatetime() {
        return lstUpdateDatetime;
    }

}
