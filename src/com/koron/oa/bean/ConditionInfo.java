package com.koron.oa.bean;
/**
 * <p>Title:工作流关于条件节点信息类 </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 科荣软件</p>
 *
 * @author 雷永辉
 * @version 1.0
 * @preserve all
 */
public class ConditionInfo {
    private String to;
    private String execId;
    public String getExecId() {
            return execId;
    }
    public void setExecId(String execId) {
            this.execId = execId;
    }
    public String getTo() {
            return to;
    }
    public void setTo(String to) {
            this.to = to;
	}
}
