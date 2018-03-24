package com.menyi.web.util;

import org.apache.struts.action.*;
import javax.servlet.ServletRequest;

/**
 *
 * <p>Title:基本的查询类 </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class BaseSearchForm extends ActionForm {
    private int operation;
    private int pageNo=1;
    private int pageSize = GlobalsTool.getPageSize();
    private int totalPage;
    private String name;

    public int getOperation() {
        return operation;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public String getName() {
        return name;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String trimString(String getString) {
        if (getString != null && getString.length() != 0) {
            return getString.trim();
        } else {
            return getString;
        }
    }

    /**
     * Reset all bean properties to their default state.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @todo Implement this org.apache.struts.action.ActionForm method
     */
    public void reset(ActionMapping mapping , ServletRequest request)
    {
        this.operation = 0;
        this.pageNo = 0;
        this.pageSize = 10;
        this.totalPage = 0;
        this.name = "";
    }
}
