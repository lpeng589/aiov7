package com.menyi.aio.web.call;

import com.menyi.web.util.MgtBaseAction;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;

import com.dbfactory.Result;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;


public  class ClientInfoAction extends MgtBaseAction {
    
	/**s
     * 来电去电信息分页查询
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        ActionForward forward = null;
        forward = callIn(mapping, form, request, response);
       
        return forward;
    }
    
    /**
     *    来电
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward callIn(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    	
    	ActionForward forward = null;
    	
        //获得要查询的页码
        int pageNo = Integer.parseInt(request.getParameter("pageNo"));
        //每页记录数
        int pageSize = 10;
        //获得本地/来电号码
        String txtLocal = request.getParameter("txtLocal");//本地号码

        String txtRemote = (String) request.getSession().getAttribute("txtRemote");

        //判断来电号码是否为空
        if(!(txtRemote==null||txtLocal==null||"".equals(txtLocal)||"".equals(txtRemote))){
            Result rs = new CallMgt().query(txtRemote,pageNo,pageSize) ;
            
            if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {//查询成功

                 request.setAttribute("txtLocal",txtLocal);//将被监听的本地线路号码存入request中
                 request.setAttribute("pageBar",this.pageBar(rs, request));//分页信息
                 request.setAttribute("rs", rs); //把查询出来的结果放入request     
                 forward = getForward(request,mapping,"clientInfo");//转向ClientInfo.jsp
             } else {
                 //查询失败
                 EchoMessage.error().add(getMessage(request, "common.msg.error")).
                     setRequest(request);
             }
        }
        //如果forward 为空返回message对应的页面
        if(forward==null){
            forward = getForward(request, mapping, "message");
        }
        return forward;
   }
}
