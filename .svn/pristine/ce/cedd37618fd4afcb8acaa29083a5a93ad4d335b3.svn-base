package com.menyi.web.util;

import org.apache.struts.action.*;
import javax.servlet.ServletRequest;

/**
 *
 * <p>Title: 基本form</p>
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
public class BaseForm extends ActionForm{
    private int operation;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
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
        this.operation = 0;//设置为默认值
    }
}
