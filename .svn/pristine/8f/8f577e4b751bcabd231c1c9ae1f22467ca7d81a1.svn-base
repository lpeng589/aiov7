package com.menyi.web.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;

import java.util.Locale;
import java.util.HashMap;


/**
 *
 * <p>Title: 信息提示类</p>
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
public class EchoMessage {
    private String content;
    private int type;
    private String backUrl = "";
    private boolean isPopWin = false;
    private boolean isRefresh = false;
    private String function = "";
    private String goUrl = null;
    private String jsConfirmYes = null;
    private String jsConfirmNo = "";
    private int autoBack = 1;
    private boolean immediately = false;
    private int comeAdd = 0;
    private String firstFocus = "";
    private boolean noBackButton = false;
    private String popWin = "";
    private String alertpopWin = "";//关闭弹出窗，且弹出提示
    private String popWinRefresh = "";
    private boolean reLogin = false;

    private static final int ECHO_SUCCESS = 0;
    private static final int ECHO_ERROR = 1;
    private static final int ECHO_INFO = 2;
    private static final int ECHO_CONFIRM = 3;

    private static ErrorMessage errorMessage = new ErrorMessage();

    public static EchoMessage success() {
        return new EchoMessage(ECHO_SUCCESS);
    }

    public static EchoMessage error() {
        return new EchoMessage(ECHO_ERROR);
    }

    public static EchoMessage info() {
        return new EchoMessage(ECHO_INFO);
    }

    public static EchoMessage confirm() {
        return new EchoMessage(ECHO_CONFIRM);
    }

    public EchoMessage setNotAutoBack() {
        this.autoBack = 0;
        return this;
    }
    
    public EchoMessage setImmediatelyBack(){
    	this.immediately = true;
    	return this;
    }
   
    public EchoMessage setcomeAdd(int comeAdd){
    	this.comeAdd = comeAdd;//0:正常 1:弹出框
    	return this;
    }

    public EchoMessage(int type) {
        this.type = type;
        content = "";
    }

    public EchoMessage add(String content) {
        this.content += content;
        return this;
    }

    public EchoMessage add(int errorCanst, HttpServletRequest request) {
        return add(errorMessage.toString(errorCanst, getLocale(request).toString()));
    }
	private static Locale getLocale(HttpServletRequest req) {
		Locale loc = null;
		Object obj = req.getSession(true).getAttribute(org.apache.struts.Globals.LOCALE_KEY);
		if (obj != null) {
			loc = (Locale) obj;
			return loc;
		}
		if (loc == null) {
			loc = req.getLocale();
		}
		if (loc == null) {
			loc = Locale.getDefault();
		}
		return loc;
	}


    public EchoMessage setBackUrl(String backUrl) {
        this.backUrl = backUrl;
        return this;
    }

    public EchoMessage setGoUrl(String goUrl) {
        this.goUrl = goUrl;
        return this;
    }

    public EchoMessage setJsConfirmYes(String jsConfirmYes) {
        this.jsConfirmYes = jsConfirmYes;
        return this;
    }

    public EchoMessage setJsConfirmNo(String jsConfirmNo) {
        this.jsConfirmNo = jsConfirmNo;
        return this;
    }
    
	public EchoMessage setNoBackButton() {
		this.noBackButton = true;
		return this;
	}

    /**
     * 设置返回页面的焦点
     * @param firstFocus
     * @return
     */
    public EchoMessage setFirstFocus(String firstFocus) {
        this.firstFocus = firstFocus;
        return this;
    }

    /**
     * 设置弹出信息后刷新父页面
     * @param isRefresh boolean
     * @return EchoMessage
     */
    public EchoMessage setRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
        return this;
    }
    public EchoMessage reLogin() {
        this.reLogin = true;
        return this;
    }

    public EchoMessage setFunction(String function) {
        this.function = function;
        return this;
    }

    public String getFunction() {
        return this.function;
    }



    /**
     * 返回弹出错误提示框
     * @param req HttpServletRequest
     */
    public void setAlertRequest(HttpServletRequest req) {
        setRequest(req); //两个方法合并，消除alert 和message的差异性
    }

    public EchoMessage setClose() {
        this.isPopWin = true;
        return this;
    }
    
    /**
     * 转换成alert弹出窗需要的转码格式
     * @param strValue
     * @return
     */
    private String encodeTextCode(String strValue){
    	if(strValue == null || strValue.length()==0){
    		return strValue ;
    	}
    	strValue = strValue.replaceAll("'", "\\\\47") ;
		strValue = strValue.replaceAll("\"", "\\\\42") ;
		strValue = strValue.replaceAll("&#39;", "\\\\47") ;
		strValue = strValue.replaceAll("&#34;", "\\\\42") ;
		return strValue ;
    }

    /**
     * 返回非弹出的提示框
     * @param req HttpServletRequest
     */
    public void setRequest(HttpServletRequest req) {

        if (content == null || content.length() == 0) {
            Object o = req.getSession().getServletContext().
                       getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
            if (o instanceof MessageResources) {
                MessageResources resource = (MessageResources) o;
                content = resource.getMessage(GlobalsTool.getLocale(req),
                                              "common.msg.error");
            }
            if (content == null || content.length() == 0)
                content = "Error";
        }
        req.getSession().setAttribute("MESSAGE_TYPE", new Integer(type));
        req.getSession().setAttribute("MESSAGE_CONTENT", content);
        req.getSession().setAttribute("MESSAGE_RELOGIN", reLogin);
        req.getSession().setAttribute("MESSAGE_AUTOBACK", autoBack);
        req.getSession().setAttribute("noBackButton", noBackButton);
        req.getSession().setAttribute("firstFocus", firstFocus);
        req.getSession().setAttribute("immediately", immediately);
        req.getSession().setAttribute("comeAdd", comeAdd);
        req.getSession().setAttribute("directJump", req.getAttribute("directJump"));
        req.getSession().setAttribute("fresh", req.getAttribute("fresh")) ;
        req.getSession().setAttribute("keySearch", req.getAttribute("keySearch")) ;
        String isWorkFlowEnter = (String)req.getAttribute("isWorkFlowEnter");
        String checkFlag = (String)req.getAttribute("checkFlag");
        String checkBackUrl = (String)req.getAttribute("checkBackUrl");
        String loadFresh = (String)req.getAttribute("loadFresh");
        String crmFreash = (String)req.getAttribute("crmFreash");
        String defineFresh = (String)req.getAttribute("defineFresh");
        String dealAsyn = (String)req.getAttribute("dealAsyn");
        String noAlert = (String)req.getAttribute("noAlert");
        if(req.getAttribute("noback") != null && (Boolean)req.getAttribute("noback")){
        	req.getSession().setAttribute("noback", true);
        }else{
        	req.getSession().setAttribute("noback", false);
        }

        //----------------------------------------------------------
        content = encodeTextCode(content) ;
        String msgHtml = "";
        //如果操作成功
        if (this.type == ECHO_SUCCESS || this.type==ECHO_INFO) {
            //设置页面跳转到结果页面
        	if (alertpopWin != null && alertpopWin.length() > 0) {
        		msgHtml = "alert(\"" + content + "\");";
        		msgHtml += "parent.closeWin('"+alertpopWin+"');";
            }else if (popWin != null && popWin.length() > 0) {
            	msgHtml += "parent.closeWin('"+popWin+"');";
            } else if (goUrl != null) {
                msgHtml = (content!=null&&content.length()>0?"alert(\"" + content + "\");":"")+ " goAction('" + goUrl + "');";
            }else if (this.isPopWin) {
            	msgHtml = "alert(\"" + content + "\");";
            	if("true".equals(isWorkFlowEnter)){
            		if("true".equals(checkFlag)){
            			msgHtml += "dealCheck('" + checkBackUrl+"');";
            		}else{
            			msgHtml += "dealWithSubmit();";
            		}
            	}else{
            		 msgHtml += "window.close();";
            	}
            }else if("true".equals(crmFreash)){
            	msgHtml = "alert('" + content + "');parent.parent.crmOpDeal()";
            }else if("true".equals(defineFresh)){
            	//自定义设置数据
            	msgHtml = "alert('" + content + "');"+checkBackUrl;
            }else if("true".equals(dealAsyn)){
            	//使用Asyncbox弹出框确定表单提交返回处理
            	if(noAlert!=null && "true".equals(noAlert)){
            		//直接跳转不alert	
            		if("true".equals(checkFlag)){
            			msgHtml += "dealCheck('" + checkBackUrl+"');";
            		}else if("define".equals(checkFlag)){
            			msgHtml = checkBackUrl;
            		}else{
            			msgHtml = "parent.parent.dealAsyn()";
            		}	
            	}else{
            		msgHtml = "alert('" + content + "');parent.parent.dealAsyn()";
            	}
            }else if(dealAsyn !=null && dealAsyn.length() >=0 && !"true".equals(dealAsyn)){
            	//使用Asyncbox弹出框确定表单提交返回处理----带参数
            	if(noAlert!=null && "true".equals(noAlert)){
            		//直接跳转不alert	
            		if("true".equals(checkFlag)){
            			msgHtml += "dealCheck('" + checkBackUrl+"');";
            		}else{
            			msgHtml = "parent.parent.dealAsyn('"+dealAsyn+"')";
            		}	
            	}else{
            		msgHtml = "alert('" + content + "');parent.parent.dealAsyn('"+dealAsyn+"')";
            	}
            }
            else {
                msgHtml = " goResultPage();";
            }
        }else if (this.type == ECHO_CONFIRM) {//如果做确认操作
            String jsy = "this.parent.location.href='" + goUrl + "';";
            if (jsConfirmYes != null) {
                jsy = jsConfirmYes;
            }
            msgHtml = " if(confirm(\"" + content + "\")){" +jsy +
                      "}else{\n " + jsConfirmNo + " }" + "\n";
            msgHtml += "setToken('" +req.getSession().getAttribute(org.apache.struts.Globals.TRANSACTION_TOKEN_KEY) +"');" + "\n";
            //将form设置为可以提交
            msgHtml += "setParentEnable();";
        }else {//如果是失败操作
            //设置提示信息
            msgHtml = "alert(\"" + content + "\");";
            //设置页面跳转到结果页面
            if (popWin != null && popWin.length() > 0) {
            	msgHtml += "parent.closeWin('"+popWin+"');";
            } else if (goUrl != null) {
                msgHtml += " goAction('" + goUrl + "');";
            } else if (isRefresh) {
                msgHtml +=
                        "this.parent.location.href = this.parent.location.href;";
            } else {
                if (req.getSession().getAttribute(org.apache.struts.Globals.TRANSACTION_TOKEN_KEY) != null) {
                    //重新设置页面Token
                    msgHtml += "setToken('" + req.getSession().getAttribute(org.apache.struts.Globals.TRANSACTION_TOKEN_KEY) +"');";
                }
                //将form设置为可以提交
                msgHtml += "setParentEnable();";
            }
            if (this.function != null && this.function.length() != 0) {
                msgHtml += " " + this.function + " ";
            }
            if(reLogin){
            	msgHtml = "top.reLogin()";
            }

        }
        req.setAttribute("MESSAGE_HTML", msgHtml);

        //如果没有设置返回页面，默认返回到集团首页
        if (backUrl == null || backUrl.length() == 0) {
            HashMap winIndexMap = (HashMap) req.getSession().getAttribute(
                    BaseEnv.WIN_MAP);
            String winCurIndex = req.getParameter("winCurIndex");
            //多窗口中每个窗口的首页保存在winIndexMap中
            if (winIndexMap != null && winCurIndex != null &&
                !winCurIndex.equals("-1")
                && winIndexMap.get(winCurIndex) != null) {
                backUrl = (String) winIndexMap.get(winCurIndex);
                //在返回连接处加上多窗口的索引
                if (backUrl.indexOf("?") > 0) {
                    backUrl += "&winCurIndex=" + winCurIndex;
                } else {
                    backUrl += "?winCurIndex=" + winCurIndex;
                }
            } else {
                backUrl = (String)req.getSession().getAttribute(BaseEnv.CUR_INDEX_URL) ;
            }
        }else{
        	if(backUrl.indexOf("winCurIndex")== -1){
        		String winCurIndex = req.getParameter("winCurIndex");
        		if(!"true".equals(loadFresh)){
        			if (backUrl.indexOf("?") > 0) {
                        backUrl += "&winCurIndex=" + winCurIndex;
                    } else {
                        backUrl += "?winCurIndex=" + winCurIndex;
                    }
        		}
        	}
        }

        if (this.popWinRefresh != null && this.popWinRefresh.length() > 0) {
            backUrl = "parent.document.form.submit(); parent.closeWin('"+popWinRefresh+"');";
            req.getSession().setAttribute("BUTTON", "CLOSE");
        }else if (this.popWin != null && this.popWin.length() > 0) {
            backUrl = "parent.closeWin('"+popWin+"');";
            req.getSession().setAttribute("BUTTON", "CLOSE");
        }else if (this.isPopWin) {
            backUrl = "window.close();";
            req.getSession().setAttribute("BUTTON", "CLOSE");
        } else {
            if (backUrl == null || backUrl.length() == 0) {
                backUrl = "closeWin();";
                req.getSession().setAttribute("noback", true);
            } else {
            	//如果进销存的单据，转交界面需要弹出，用户确定转交后要转到列表页面
            	
            	if(req.getAttribute("directJump")!=null){
            		String noback=req.getParameter("noback");
            	//	backUrl = "var retVal=window.showModalDialog('"+backUrl+"',this,'dialogWidth=750px;dialogHeight=450px');"+
            	//			" if(retVal=='deliverTo'||'"+req.getAttribute("from")+"'=='add'){"+
            	//			("true".equals(noback)?"this.parent.parent.win.removewin(this.parent.parent.win.currentwin);":"this.parent.location.href='"+req.getAttribute("retValUrl")+"'")+"}";
            	}else{
            		if("true".equals(loadFresh)){
            			backUrl = "alert('" + content + "');" + backUrl;
            			req.setAttribute("loadFresh", loadFresh);
            		}else{
            			backUrl = "location.href='" + backUrl + "'";
            		}
            	}
            } 
            req.getSession().setAttribute("BACK_URL", "BACK");
        } 
        req.getSession().setAttribute("retValUrl", req.getAttribute("retValUrl"));
        req.getSession().setAttribute("BACK_URL", backUrl);
        

        if (req.getAttribute("AUTO_JUMP") != null) {
            req.getSession().setAttribute("AUTO_JUMP", "true");
        } else {
            if (req.getSession().getAttribute("AUTO_JUMP") != null) {
                req.getSession().removeAttribute("AUTO_JUMP");
            }
        }
        if (req.getAttribute("LAST_SETTLEPERIOD") != null) {
            req.getSession().setAttribute("LAST_SETTLEPERIOD", "true");
        } else {
            if (req.getSession().getAttribute("LAST_SETTLEPERIOD") != null) {
                req.getSession().removeAttribute("LAST_SETTLEPERIOD");
            }
        }
    }
    /**
     * 弹出提示后关闭弹出窗
     * @param alertpopWin
     * @return
     */
    public EchoMessage setAlertAndClosePopWin(String alertpopWin){
    	this.alertpopWin = alertpopWin;
    	return this;
    } 
    public EchoMessage setClosePopWin(String popWin){
    	this.popWin = popWin;
    	return this;
    }
    /**
     * 关闭弹出窗，并刷新父窗口
     * @param popWin
     * @return
     */
    public EchoMessage setClosePopWinRefresh(String popWin){
    	this.popWinRefresh = popWin;
    	return this;
    }

    public void setPopWin(boolean isPopWin) {
        this.isPopWin = isPopWin;
    }

	public String getAlertpopWin() {
		return alertpopWin;
	}

	public EchoMessage setAlertpopWin(String alertpopWin) {
		this.alertpopWin = alertpopWin;
		return this;
	}

}
