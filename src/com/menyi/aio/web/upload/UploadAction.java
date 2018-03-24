package com.menyi.aio.web.upload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dbfactory.Result;
import com.menyi.aio.bean.UnitBean;
import com.menyi.web.util.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import java.util.ArrayList;

/**
 * <p>Title: �ļ��ϴ�</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public class UploadAction extends BaseAction {

    protected ActionForward doAuth(HttpServletRequest req,
                                   ActionMapping mapping) {
        return null;
    }


    /**
     * exe ��������ں���
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     * @todo Implement this com.huawei.sms.web.util.BaseAction method
     */
    protected ActionForward exe(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        //���ݲ�ͬ�������ͷ������ͬ��������
        int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
        case OperationConst.OP_ADD_PREPARE:
            forward = addPrepare(mapping, form, request, response);
            break;
        case OperationConst.OP_ADD:
            forward = add(mapping, form, request, response);
            break;
        case OperationConst.OP_DELETE:
            forward = delete(mapping, form, request, response);
            break;
        case OperationConst.OP_DELETE_PREPARE:
            forward = deletePrepare(mapping, form, request, response);
            break;

        }
        return forward;
    }

    /**
     * ���ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward addPrepare(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
            Exception {
        request.removeAttribute("operation");
        String type = request.getParameter("type");
        request.setAttribute("type", type);
        return getForward(request, mapping, "upload");
    }

    /**
     * ���
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward add(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        UploadForm myForm = (UploadForm) form;
        //ִ����Ӳ���

        FormFile formFile = myForm.getFileName();
        String fileName = null;
        if(formFile != null){
            fileName = FileHandler.writeTemp(formFile.getFileName(),
                    formFile.getFileData());
        }
        ActionForward forward = getForward(request, mapping, "alert");
        if (fileName != null) {
            //�ϴ��ɹ�
            ArrayList list ;
            if("PIC".equals(myForm.getType())){
                Object o = request.getSession().getAttribute("PIC_UPLOAD");
                if(o == null){
                    list = new ArrayList();
                    request.getSession().setAttribute("PIC_UPLOAD",list);
                }else{
                    list = (ArrayList)o;
                }
            }else{
                Object o = request.getSession().getAttribute("AFFIX_UPLOAD");
                if(o == null){
                    list = new ArrayList();
                    request.getSession().setAttribute("AFFIX_UPLOAD",list);
                }else{
                    list = (ArrayList)o;
                }
            }
            list.add(fileName);
			
            request.setAttribute("fileName",fileName+":"+formFile.getFileName());

            return getForward(request, mapping, "uploadSuccess");
        } else {
            //�ϴ�ʧ��
            EchoMessage.error().add(getMessage(
                    request, "common.msg.uploadFailure")).
                    setAlertRequest(request);
        }
        return forward;
    }

    /**
     * ���ǰ��׼��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward deletePrepare(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws
            Exception {
        request.removeAttribute("operation");
        String fileName = request.getParameter("fileName");
        String tempFile = request.getParameter("tempFile"); //�Ƿ�����ʱ�ļ�
        String tableName = request.getParameter("tableName"); //ɾ����ʽ�ļ�ʱҪ�ṩ����
        String type = request.getParameter("type"); //ɾ����ʽ�ļ�ʱҪ�ṩ����
        
        request.setAttribute("fileName",fileName);
        request.setAttribute("tempFile",tempFile);
        request.setAttribute("tableName",tableName);
        request.setAttribute("type",type);

        return getForward(request, mapping, "delete");
    }


    /**
     * ɾ��
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward delete(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws
            Exception {
        ActionForward forward = null;
        String fileName = request.getParameter("fileName");
        String tempFile = request.getParameter("tempFile"); //�Ƿ�����ʱ�ļ�
        String tableName = request.getParameter("tableName"); //ɾ����ʽ�ļ�ʱҪ�ṩ����
        String type = request.getParameter("type"); //ɾ����ʽ�ļ�ʱҪ�ṩ����
        fileName = GlobalsTool.toChinseChar(fileName);
        
        if (fileName != null && fileName.length() != 0) {
            boolean rs = false;
            if("true".equals(tempFile)){
                //ɾ����ʱ�ļ�
                rs = FileHandler.deleteTemp(fileName);
            }else{
                rs = true;
                //���ﲻ����ʵɾ����ʽ�ļ����������ύʱ�ж��ļ���ɾ��������ʽɾ��
                //���������ʵɾ���ˣ����û�δ�ύ����£��������ݲ�һ��
//                rs = FileHandler.delete(tableName,
//                                        "PIC".equals(type) ? FileHandler.TYPE_PIC :
//                                        FileHandler.TYPE_AFFIX, fileName);
            }
            if (rs) {
                //ɾ���ɹ�
                //��ʱ�ļ�Ҫɾ���ڴ�����
                if("true".equals(tempFile)){
                    ArrayList list;
                    if ("PIC".equals(type)) {
                        Object o = request.getSession().getAttribute(
                                "PIC_UPLOAD");
                        if (o == null) {
                            list = new ArrayList();
                            request.getSession().setAttribute("PIC_UPLOAD",
                                    list);
                        } else {
                            list = (ArrayList) o;
                        }
                    } else {
                        Object o = request.getSession().getAttribute(
                                "AFFIX_UPLOAD");
                        if (o == null) {
                            list = new ArrayList();
                            request.getSession().setAttribute("AFFIX_UPLOAD",
                                    list);
                        } else {
                            list = (ArrayList) o;
                        }
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if(fileName.equals(list.get(i).toString())){
                            list.remove(i);
                            break;
                        }
                    }
                }
//                request.setAttribute("fileName",fileName);
//                return getForward(request, mapping, "deleteSuccess");

            } else {
                //ɾ��ʧ��
//                EchoMessage.error().add(getMessage(request,
//                        "common.msg.delError")).
//                        setRequest(request);
//                forward = getForward(request, mapping, "message");
            }
            request.setAttribute("fileName",fileName);
                return getForward(request, mapping, "deleteSuccess");
        }
        return forward;

    }
}
