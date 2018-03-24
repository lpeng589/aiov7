package com.menyi.aio.web.upload;

import com.menyi.web.util.BaseForm;
import org.apache.struts.upload.FormFile;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class UploadForm extends BaseForm {
    private FormFile fileName;
    private String type;

    public FormFile getFileName() {
        return fileName;
    }

    public String getType() {
        return type;
    }

    public void setFileName(FormFile fileName) {
        this.fileName = fileName;
    }

    public void setType(String type) {
        this.type = type;
    }
}
