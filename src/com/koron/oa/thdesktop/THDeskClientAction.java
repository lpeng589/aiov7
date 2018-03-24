package com.koron.oa.thdesktop;

import javax.servlet.http.*;
import org.apache.struts.action.*;

import com.dbfactory.Result;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.MgtBaseAction;

/**
 * 
 * <p>Title:OA����̨</p> 
 * <p>Description: </p>
 *
 * @Date:Jul 6, 2012
 * @Copyright: �����п���������޹�˾
 * @Author ǮС��
 */
public class THDeskClientAction extends MgtBaseAction {
	
	THDeskClientMgt mgt = new THDeskClientMgt() ;
    /**
     * Actionִ�к���
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.menyi.web.util.BaseAction method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
              HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String sql=request.getParameter("sql");
    	String forwardVal=request.getParameter("forwardVal");
    	Result rs=mgt.queryDesk(sql);
    	if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
    		request.setAttribute("rsVal", rs.retVal);
    	}else{
    		request.setAttribute("rsVal", "����SQL����Ƿ���ȷ��");
    	}
        return getForward(request, mapping, forwardVal);
    }    
}
