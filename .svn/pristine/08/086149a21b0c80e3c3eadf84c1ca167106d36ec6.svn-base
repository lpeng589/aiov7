package com.menyi.aio.web.logo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.dbfactory.Result;
import com.menyi.aio.dyndb.DynDBManager;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
import com.menyi.web.util.PublicMgt;
/**
 * 
 * <p>Title:企业logo设置</p> 
 * <p>Description: 企业logo设置</p>
 *
 * @Date:2010-7-26
 * @Copyright: 科荣软件
 * @Author 文小钱
 */
public class LogoSetAction extends MgtBaseAction {

	protected ActionForward exe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//跟据不同操作类型分配给不同函数处理
        int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
	        case OperationConst.OP_ADD:
	        	String typeFlag = request.getParameter("typeFlag");
	        	if("change".equals(typeFlag)){
	        		/*添加公司名称和链接*/
	        		forward = changeAdd(mapping, form, request, response);
	        	}else{
	        		forward = add(mapping, form, request, response);
	        	}	            
	            break;	       
	        case OperationConst.OP_DELETE:
	        	forward = delete(mapping,form,request,response) ;
	        	break ;
	        default:
	            forward = query(mapping, form, request, response);
        }
        return forward;
	}
    
	private ActionForward changeAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String companyName = GlobalsTool.toChinseChar(request.getParameter("companyName"));
		String companyLink = GlobalsTool.toChinseChar(request.getParameter("companyLink"));
		Result rs = new PublicMgt().changeAdd(companyName, companyLink, getLoginBean(request).getId());
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", 3);
			new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),"公司名称:"+companyName+";公司网址:"+companyLink,"tblCompanySet", "界面设置","");
		}
		return getForward(request, mapping, "blank");
	}

	/**
     * 添加
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
        LogoSetForm logoForm = (LogoSetForm) form ;
        FormFile file = null ;
        String logoname = request.getParameter("logoname");
        if("loginLogo_1".equals(logoForm.getLogoType())){
        	file = logoForm.getLoginLogo_1() ;
        }else if("loginLogo_2".equals(logoForm.getLogoType())){
        	file = logoForm.getLoginLogo_2() ;
        }else if("loginLogo_3".equals(logoForm.getLogoType())){
        	file = logoForm.getLoginLogo_3() ;
        }else if("company".equals(logoForm.getLogoType())){
        	file = logoForm.getCompany() ;
        }else{
        	file = logoForm.getPrintLogo() ;
        }
        String ext = file.getFileName().substring(file.getFileName().indexOf(".")+1) ;
        if(!"printLogo".equals(logoForm.getLogoType())){
	        /*上传的logo图片类型必须是JPG，GIF，PNG，BMP*/
	        if(!("jpg".equalsIgnoreCase(ext) || "gif".equalsIgnoreCase(ext) 
	        				|| "png".equalsIgnoreCase(ext) ||  "bmp".equalsIgnoreCase(ext))){
	        	EchoMessage.info().add(getMessage(request, "com.logo.type")).setBackUrl("/LogoSetAction.do")
				  				  .setAlertRequest(request);
	        	return getForward(request, mapping, "alert") ;
	        }
        }else{
        	if(!"bmp".equals(ext)){
        		EchoMessage.info().add(getMessage(request, "com.logo.bmp.type")).setBackUrl("/LogoSetAction.do")
		  				  .setAlertRequest(request);
        		return getForward(request, mapping, "alert") ;
        	}
        }
        /*上传的logo不能大于500K*/
        if(file.getFileSize()>500*1024){
        	EchoMessage.info().add(getMessage(request, "com.logo.size")).setBackUrl("/LogoSetAction.do")
        					  .setAlertRequest(request);
        	return getForward(request, mapping, "alert") ;
        }
        
        String path = "" ;
        String logoName = "";
        if(!"printLogo".equals(logoForm.getLogoType())){
        	//界面设置
        	 path = request.getSession().getServletContext().getRealPath("/style/v7/user/") ;
        	 File image = new File(path);
             if (!image.exists()) {
             	image.mkdirs();
             }           
         	
     		File[] listFile = image.listFiles() ;
     		for(File logo :listFile){
     			if(logo.getName().contains(logoname)){
     				new File(path+"\\"+logo.getName()).delete();
     			}
     		}
         	/*
             System.out.println(path+"\\"+logoname+"."+ext);
             if(new File(path+"\\"+logoname+"."+ext).exists()){
            	 new File(path+"\\"+logoname+"."+ext).delete();
             }*/
     		
             if("company".equals(logoname)){
            	 logoName = "公司登陆LOGO";
            	 path = image+"\\"+logoForm.getLogoType()+file.getFileName().substring(file.getFileName().lastIndexOf(".")); 
             }
             if("loginLogo_1".equals(logoname)){
            	 logoName = "登陆背景图片1";
            	 path = image+"\\"+logoForm.getLogoType()+file.getFileName().substring(file.getFileName().lastIndexOf(".")); 
             }
             if("loginLogo_2".equals(logoname)){
            	 logoName = "登陆背景图片2";
            	 path = image+"\\"+logoForm.getLogoType()+file.getFileName().substring(file.getFileName().lastIndexOf(".")); 
             }
             if("loginLogo_3".equals(logoname)){
            	 logoName = "登陆背景图片3";
            	 path = image+"\\"+logoForm.getLogoType()+file.getFileName().substring(file.getFileName().lastIndexOf(".")); 
             }                                             
        }else{
        	//打印
        	path = BaseEnv.FILESERVERPATH;
    		if (!path.trim().endsWith("/")) {
    			path = path.trim() + "/";
    		}
    		path = path + "pic/logo.bmp" ;
    		logoName = "打印LOGO";
        }
        
        try {
        	File picFile = new File(path) ;
        	if(!picFile.getParentFile().exists()){
        		picFile.getParentFile().mkdirs() ;
        	}
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(file.getFileData());
            fos.close();
        } catch (Exception ex) {
            BaseEnv.log.error("LogoSetAction  Error ", ex);
        }
        EchoMessage.success().add(getMessage(request,"com.logo.sucess"))
        					 .setBackUrl("/LogoSetAction.do")
		  				  	 .setAlertRequest(request);
        new DynDBManager().addLog(1, getLoginBean(request).getId(), getLoginBean(request).getEmpFullName(), getLoginBean(request).getSunCmpClassCode(),logoName+" 修改了！","tblCompanySet", "界面设置","");
        return getForward(request, mapping, "alert") ;
    }

    /**
     * 添加
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward query(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
    	String logoPath = request.getSession().getServletContext().getRealPath("/style/v7/user/") ;
    	File logoFile = new File(logoPath) ;
    	if(logoFile.isDirectory()){
    		File[] listFile = logoFile.listFiles() ;
    		for(File file :listFile){
    			if(file.getName().contains("loginLogo_1")){
    				request.setAttribute("loginLogo_1", file.getName()) ;
    			}else if(file.getName().contains("loginLogo_2")){
    				request.setAttribute("loginLogo_2", file.getName()) ;
    			}else if(file.getName().contains("loginLogo_3")){
    				request.setAttribute("loginLogo_3", file.getName()) ;
    			}else if(file.getName().contains("company")){
    				request.setAttribute("company", file.getName()) ;
    			}
    		}
    	}
    	String fileName = BaseEnv.FILESERVERPATH;
		if (!fileName.trim().endsWith("/")) {
			fileName = fileName.trim() + "/";
		}
		fileName = fileName + "pic/logo.bmp" ;
		File file = new File(fileName) ;
		if(file.isFile()){
			request.setAttribute("printLogo", file.getName()) ;
		}
		
		/*查询公司*/
		Result rs = new PublicMgt().getCompany();
		ArrayList rsRs = (ArrayList)rs.retVal;
		if(rsRs !=null && rsRs.size() >0){
			request.setAttribute("companyName", ((Object[])rsRs.get(0))[0] !=null?((Object[])rsRs.get(0))[1].toString():"");
			request.setAttribute("companyLink", ((Object[])rsRs.get(0))[0] !=null?((Object[])rsRs.get(0))[0].toString():"");
		}				
    	return getForward(request, mapping, "logo");
    }

    /**
     * 删除
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception
     */
    protected ActionForward delete(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
    	String logoType = getParameter("logoType", request) ;
    	String logoName = getParameter("logoName", request);
    	if("loginLogo".equals(logoType)){
    		String path = request.getSession().getServletContext().getRealPath("/style/v7/user/") ;
       	 	File image = new File(path);
            if (image.exists()) {
            	System.out.println(path+"\\"+logoName);
            	new File(path+"\\"+logoName).delete();
            }
    	}else if("company".equals(logoType)){
    		String path = request.getSession().getServletContext().getRealPath("/style/v7/user/") ;
    		File image = new File(path);
            if (image.exists()) {
            	System.out.println(path+"\\"+logoName);
            	new File(path+"\\"+logoName).delete();
            }
    	}else{
    		String fileName = BaseEnv.FILESERVERPATH;
    		if (!fileName.trim().endsWith("/")) {
    			fileName = fileName.trim() + "/";
    		}
    		fileName = fileName + "pic/logo.bmp" ;
    		File image = new File(fileName);
    		image.delete() ;
    	}
    	return query(mapping, form, request, response) ;
    }
}
