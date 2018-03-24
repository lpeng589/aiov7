package com.koron.oa.workflow.template;

import com.menyi.web.util.BaseSearchForm;
import org.apache.struts.upload.FormFile;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ÷‹–¬”Ó</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class OAWorkFlowTempForm extends BaseSearchForm{
    private String templateName;
    private int templateType;
    private String templateClass;
    private int templateStatus;
    private String templateFile;
    private String workFlowFile;
    private String fileName ;
    private String fileYear ;
    private String fileNumber ;
    private String id;
    private FormFile file;
    private int finishTime ;

    public String getTemplateClass() {
        return templateClass;
    }

    public String getTemplateName() {
        return templateName;
    }

    public int getTemplateType() {
        return templateType;
    }

    public int getTemplateStatus() {
        return templateStatus;
    }

    public String getId() {
        return id;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public String getWorkFlowFile() {
        return workFlowFile;
    }

    public FormFile getFile() {
        return file;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public void setTemplateStatus(int templateStatus) {
        this.templateStatus = templateStatus;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setTemplateClass(String templateClass) {
        this.templateClass = templateClass;
    }

    public void setWorkFlowFile(String workFlowFile) {
        this.workFlowFile = workFlowFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileYear() {
		return fileYear;
	}

	public void setFileYear(String fileYear) {
		this.fileYear = fileYear;
	}

	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public int getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}
	
}
