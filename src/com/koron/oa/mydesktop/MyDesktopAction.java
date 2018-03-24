package com.koron.oa.mydesktop;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.dbfactory.Result;
import com.koron.oa.bean.MyDeskBean;
import com.koron.oa.oaCollection.OACollectionMgt;
import com.koron.oa.util.AttentionMgt;
import com.koron.oa.workflow.OAMyWorkFlowForm;
import com.koron.oa.workflow.OAMyWorkFlowMgt;
import com.menyi.aio.bean.BaseDateFormat;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;
import com.menyi.msgcenter.server.MSGConnectCenter;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.EchoMessage;
import com.menyi.web.util.ErrorCanst;
import com.menyi.web.util.FileHandler;
import com.menyi.web.util.GlobalsTool;
import com.menyi.web.util.MgtBaseAction;
import com.menyi.web.util.OperationConst;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * <p>Title:OA����̨</p> 
 * <p>Description: </p>
 *
 * @Date:Jul 6, 2012
 * @Copyright: �����п���������޹�˾
 * @Author ǮС��
 */
public class MyDesktopAction extends MgtBaseAction {
	
	MyDesktopMgt mgt = new MyDesktopMgt() ;
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
        
    	int operation = getOperation(request);
        ActionForward forward = null;
        switch (operation) {
	        //����ǰִ�з���
	        case OperationConst.OP_QUERY:
	            forward = queryAll(mapping, form, request, response);
	            break;
	        case OperationConst.OP_OA_VIEW_SINLE:
	            forward = querySingle(mapping, form, request, response);
	            break;
	        case OperationConst.OP_SET_PREPARE:
	        	forward = setPrepare(mapping, form, request, response) ;
	        	break ;
	        case OperationConst.OP_SET:
	        	forward = set(mapping, form, request, response) ;
	        	break ;
	        case OperationConst.OP_UPLOAD_PREPARE:
	        	String uploadFlag = request.getParameter("uploadFlag");
	        	if("ajaxType".equals(uploadFlag)){
	        		forward = ajaxUpload(mapping, form, request, response) ;
	        	}else{
	        		forward = uploadPrepare(mapping, form, request, response) ;
	        	}	        	
	        	break ;
	        case OperationConst.OP_UPLOAD:
	        	forward = upload(mapping, form, request, response) ;
	        	break ;
	        case OperationConst.OP_DELETE:
	        	forward = delete(mapping, form, request, response) ;
	        	break ;
	        case OperationConst.OP_ADD:
	        	forward = countDown(mapping, form, request, response) ;
	        	break ;
	        case OperationConst.OP_UPDATE:
	        	forward = update(mapping, form, request, response) ;
	        	break ;
	        	//���ٰ���ϸ
	        case OperationConst.OP_DETAIL:
	        	forward = queryfameWish(mapping, form, request, response) ;
	        	break ; 
	        default:
	        	forward = queryAll(mapping, form, request, response);
        }
        return forward;
    }
    
    /**
	 * �鿴���ٰ�����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward queryfameWish(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		String status=request.getParameter("status");
		Result result=null;
		if(status.equals("1")){
			result=mgt.queryAllfameTop(1);
			request.setAttribute("status", 0);
		}else if(status.equals("0")){
			result=mgt.queryAllfameTop(0);
			request.setAttribute("status", 1);
		}
		Result rss=mgt.queryAllfameTopWish();
		request.setAttribute("listfameTop", result.retVal);
		request.setAttribute("listfameTopWish", rss.retVal);
		return getForward(request, mapping, "queryfameTopDet");
	}
	
    /**
     * ��ѯ�ҵĹ�ע
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward attentionList(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		String userId = this.getLoginBean(request).getId();
		
		String delkey[] = request.getParameterValues("keyId");
		if(delkey != null && delkey.length > 0){
			for(String key:delkey){
				new AttentionMgt().deleteAttention(key);
			}
		}
		
		String title = request.getParameter("title");
		if(title == null) title="";
		request.setAttribute("title", title);
		/*Result rs = mgt.getAttention(userId, title);
		request.setAttribute("result", rs.getRetVal());
		return getForward(request, mapping, "attentionList") ;*/		
		Result rs = new OACollectionMgt().oaMydsCollection(getLoginBean(request).getId(), title);
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("ctList", rs.retVal);
			request.setAttribute("pageBar", pageBar2(rs, request));
		}
		//����
		Result res = new OACollectionMgt().queryType();
		request.setAttribute("TypeList", res.retVal);
		//�����ɫ
		HashMap<String, String> map = new HashMap<String, String>();
		//��װmap
		String[] color = {"df7ba6","47a91c","db8933","b54143","3796bf","e5acae","aedfa3","f3d1a8","a5daea"};
		ArrayList mapres = (ArrayList)res.retVal;
		if(mapres!=null && mapres.size()>0){
			for (int i = 0; i < mapres.size(); i++) {
				String key = ((Object[])mapres.get(i))[0].toString();			
				map.put(key, color[i]);
			}
		}
		request.setAttribute("mapList", map);		
		//ʱ���ж�
		Calendar c = Calendar.getInstance();                 				
		c.add(Calendar.DAY_OF_MONTH, -1);
		request.setAttribute("yestoday",BaseDateFormat.format(c.getTime(), BaseDateFormat.yyyyMMdd));
		c.add(Calendar.DAY_OF_MONTH, -1);
		request.setAttribute("oldthree",BaseDateFormat.format(c.getTime(), BaseDateFormat.yyyyMMdd));		
		return getForward(request, mapping, "queryCollection");
	}
    
    /**
     * ����ʱ���� 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward countDown(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		if("true".equals(request.getParameter("add"))){
			String countdownType=request.getParameter("countdownType");
			String countdownTitle=request.getParameter("countdownTitle");
			String countdownDate=request.getParameter("countdownDate");
			
			Result rs = mgt.updateCountDown(countdownType, countdownTitle, countdownDate);
			if (rs.retCode == ErrorCanst.DEFAULT_SUCCESS) {
	            //��ӳɹ�
				request.setAttribute("dealAsyn", "true");
	            EchoMessage.success().add(getMessage(request, "common.msg.updateSuccess")).
	                setAlertRequest(request);
	        }else {
	            //���ʧ��
	            EchoMessage.error().add(getMessage(request, "common.msg.updateFailture")).
	                setAlertRequest(request);
	        }
			return getForward(request, mapping, "alert") ;
		}else{
			Result rs = mgt.getCountDown();
			request.setAttribute("countDown", rs.getRetVal());
			return getForward(request, mapping, "countdown") ;
		}
	}
    
    /**
     * �ҵĹ���̨������
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward setPrepare(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request,HttpServletResponse response){
    	
    	/*�ҵ�����*/
    	Result result = mgt.queryMyDesk(getLoginBean(request).getId(), "OA") ;
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		request.setAttribute("deskMap", result.retVal) ;
    		HashMap<Integer, List<MyDeskBean>> map = (HashMap<Integer, List<MyDeskBean>>) result.retVal ;
    		if(map.get(1)!=null && map.get(1).size()>0){
				request.setAttribute("width1", map.get(1).get(0).getWidth()) ;
			}
			if(map.get(2)!=null && map.get(2).size()>0){
				request.setAttribute("width2", map.get(2).get(0).getWidth()) ;
			}
			if(map.get(3)!=null && map.get(3).size()>0){
				request.setAttribute("width3", map.get(3).get(0).getWidth()) ;
			}
    	}
    	
    	result = mgt.queryDefaultDesk(getLoginBean(request).getId(),"OA") ;
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		request.setAttribute("noDesk", result.retVal) ;
    	}
    	return getForward(request, mapping, "set") ;
    }
    
    /**
     * ɾ������̨��ĳ��ģ��
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward delete(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request,HttpServletResponse response){
    	
    	String moduleId = getParameter("moduleId",request) ;
    	mgt.deleteDeskModule(moduleId, "OA",getLoginBean(request).getId()) ;
    	return getForward(request, mapping, "blank") ;
    }
    
    /**
     * �޸Ĺ���̨ģ����ʾ��¼��
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward update(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request,HttpServletResponse response){
    	
    	String moduleId = getParameter("moduleId",request) ;
    	String rowCount = getParameter("rowCount", request) ;
    	Result result = mgt.updateDeskRowCount(moduleId, rowCount, getLoginBean(request).getId()) ;
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		request.setAttribute("msg", "YES") ;
    	}else{
    		request.setAttribute("msg", "NO") ;
    	}
    	return getForward(request, mapping, "blank") ;
    }
    
    /**
     * �ϴ�ͷ��֮ǰ
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    private ActionForward uploadPrepare(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request,HttpServletResponse response) throws IOException{
    	LoginBean loginBean = getLoginBean(request) ;
    	request.setAttribute("loginId", loginBean.getId());
    	String myPhoto_140 = "";
    	String myPhoto_48 = "";
    	String type = request.getParameter("type");
    	String empId = request.getParameter("empId");
    	String showPhotoId = loginBean.getId();
    	if(type != null && "employee".equals(type)){
    		if(empId != null && !"".equals(empId)){
    			myPhoto_140 = GlobalsTool.checkEmployeePhoto("140",empId);
    			myPhoto_48 = GlobalsTool.checkEmployeePhoto("48",empId); 
    			showPhotoId = empId;
    		}else{
    			request.setAttribute("eixtHere", "NO");   
    		}
    	}else{
    		myPhoto_140 = GlobalsTool.checkEmployeePhoto("140",loginBean.getId());    
        	myPhoto_48 = GlobalsTool.checkEmployeePhoto("48",loginBean.getId()); 
    	}

    	if(!"/style/images/no_head.gif".equals(myPhoto_140) && !"/style/images/no_head.gif".equals(myPhoto_48)){
    		try {   			
    			String fileName_140 = BaseEnv.FILESERVERPATH+"/pic/tblEmployee/"+showPhotoId+"_140.jpg";    	          
            	String newPath_140 = request.getSession().getServletContext().getRealPath("/msgPic/"+showPhotoId+"_140.jpg");
    			if(!copyPicToWb(fileName_140,newPath_140)){
    				request.setAttribute("eixtHere", "NO");
    			}else{
    				request.setAttribute("myPhoto_140", myPhoto_140);
    			}
    			String fileName_48 = BaseEnv.FILESERVERPATH+"/pic/tblEmployee/"+showPhotoId+"_48.jpg";    	          
            	String newPath_48 = request.getSession().getServletContext().getRealPath("/msgPic/"+showPhotoId+"_48.jpg");
            	if(!copyPicToWb(fileName_48,newPath_48)){
    				request.setAttribute("eixtHere", "NO");
    			}else{
    				request.setAttribute("myPhoto_48", myPhoto_48);
    			}
			} catch (Exception e) {
				e.printStackTrace();				
			}   		
    	}else{   		
    		request.setAttribute("myPhoto", "/style/images/no_head.gif");
    	}    
    	request.setAttribute("type", type);
        request.setAttribute("empId", empId);
    	return getForward(request, mapping, "uploadImage") ;
    }
    
    /**
     * ��ͼƬ���Ƶ�website�»�ȡ
     * @param fileName
     * @param wbFileName
     * @return
     */
    public boolean copyPicToWb(String fileName,String wbFileName){
    	try {
    		FileOutputStream fos;    
    		File filePath = new File(wbFileName);
        	if(!(filePath.getParentFile().exists())){
        		filePath.getParentFile().mkdirs();
        	}
        	File filem = new File(fileName);
        	fos = new FileOutputStream(filePath);
        	if(filem.exists()){          		
            	FileInputStream imageInput=new FileInputStream(fileName);
            	byte[] buf=new byte[imageInput.available()];
            	imageInput.read(buf);                	
            	imageInput.close();
            	fos.write(buf);
            	return true;
        	}          
        	fos.close();       	
		} catch (Exception e) {
			return false;
		}
		return false;
    }
    /**
     * �ϴ�ͷ��
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings("static-access")
	private ActionForward upload(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request,HttpServletResponse response){
    	try {
    		//��ȡx��y���ֵ�Ϳ��w��h
        	int x = Integer.parseInt(request.getParameter("x"));
        	int y = Integer.parseInt(request.getParameter("y"));
        	int divH = Integer.parseInt(request.getParameter("divH"));//��ʾdiv�ĸ�
        	int divW = Integer.parseInt(request.getParameter("divW"));//��ʾdiv��kuan
        	int w = Integer.parseInt(request.getParameter("w"));
        	int h = Integer.parseInt(request.getParameter("h"));
        	String type = request.getParameter("type");
        	String empId = request.getParameter("empId");
        	String loginId = getLoginBean(request).getId();
        	if(type != null && "employee".equals(type)){
        		if(empId != null && !"".equals(empId)){
        			loginId = empId;
        		}
        	}
        	//�����������Ϊ��½Id��ͼƬ��ɾ���ٸ��� 
        	String oldPath = BaseEnv.FILESERVERPATH+"/pic/tblEmployee/"+loginId+".jpg";
        	File oldSoure = new File(oldPath);
        	if(oldSoure.exists()){
        		FileHandler.deleteTemp(oldPath);
        	}       	
        	//��ȡ�ϴ���temp������ͼƬ
        	String photoPath = BaseEnv.FILESERVERPATH+"/temp/"+loginId+".jpg";
        	File fileSoure = new File(photoPath);
        	//������� ���Ƶ�tblEmployee
    		if(fileSoure.exists()){   	
    			  		           	
            	/*String fn = FileHandler.getRealFileName("tblEmployee", FileHandler.TYPE_PIC,loginId+".jpg");               
                FileHandler.copy("tblEmployee", FileHandler.TYPE_PIC,loginId+".jpg",fn);    	             
                FileHandler.deleteTemp(photoPath);*/
                String imagePath = BaseEnv.FILESERVERPATH+"/pic/tblEmployee/";
                if(!new File(imagePath).exists()){
                	new File(imagePath).mkdirs();
                }
            	//��ȡ����ͼƬ   	
            	this.abscut(photoPath, x, y, w,h,divH,divW,imagePath+loginId+"_140.jpg",140);
            	this.abscut(photoPath, x, y, w, h,divH,divW,imagePath+loginId+"_48.jpg",48);
            	FileHandler.deleteTemp(photoPath); 	    	
                Result result = mgt.updateEmployeeImage(loginId, loginId+".jpg") ;
                if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
                	//kk����
                	new MSGConnectCenter().refreshEmpInfo(loginId);
                	
                	LoginBean loginBean = getLoginBean(request) ;
                	loginBean.setPhoto(loginId+".jpg") ;
                	request.getSession().setAttribute("LoginBean", loginBean);
                	EchoMessage.success().add(getMessage(
                            request, "common.msg.uploadSuccess")).
                            setBackUrl("window.parent.parent.location.reload();").
                            setAlertRequest(request);  
                	request.setAttribute("msg", "1");//�ɹ�
                }else{
                	request.setAttribute("msg", "0");
                }	
    		}else{
    			request.setAttribute("msg", "0");
    		}	       	       	                 				
		} catch (Exception e) {
			e.printStackTrace();
		}      
		return getForward(request, mapping, "blank");
    }
    private void abscut(String imagePath, int x,int y,int destWidth,int destHeight,int divH,int divW,String fileName,int yaW){
    	try {
    		Image img;
    		ImageFilter cropFilter;
    		// ��ȡԴͼ��
    		BufferedImage bi = ImageIO.read(new File(imagePath));
    		int srcWidth = bi.getWidth(); // Դͼ���
    		
    		int srcHeight = bi.getHeight(); // Դͼ�߶�
    		System.out.println(srcWidth+"-----"+srcHeight);
    		if (srcWidth >= destWidth && srcHeight >= destHeight) {
	    		Image image = bi.getScaledInstance(srcWidth, srcHeight,Image.SCALE_DEFAULT);
	
	    		/*
	    		 �ж�ԭͼ�Ŀ�ߺ�DIV��ߵĴ�С
	    		 ����ͼƬ���DIV�Ŀ�ȣ�ѡ�����ʼ��������Ա仯
	    		*/
	    		int x1 = x*srcWidth/divW;
	    		int y1 = y*srcHeight/divH;
	    		int w = destWidth*srcWidth/divW;
	    		int h = destHeight*srcHeight/divH;
	
	    		// �ĸ������ֱ�Ϊͼ���������Ϳ��
	    		// ��: CropImageFilter(int x,int y,int width,int height)
	    		cropFilter = new CropImageFilter(x1, y1, w, h);
	    		img = Toolkit.getDefaultToolkit().createImage(
	    				new FilteredImageSource(image.getSource(), cropFilter));
	    		
	    		//ѹ��ͼƬΪ140*140   48*48
	    		int new_wM = w;
	    		int new_hM = h;
	    		if (yaW >= new_wM) {
	    		    if (yaW < new_hM) {
	    		    	 new_wM = (int) (new_wM * new_hM / new_hM);
	    		    	 new_hM = yaW;
	    		    }
	    		}else{
	    		      if (yaW >= new_hM) {
	    		    	  new_hM = (int) (new_hM * yaW / new_wM);
	    		    	  new_wM = yaW;
	    		      } else {
	    		       if (new_hM > new_wM) {
	    		    	   new_wM = (int) (new_wM * yaW / new_hM);
	    		    	   new_hM = yaW;
	    		       } else {
	    		    	   new_hM = (int) (new_hM * yaW / new_wM);
	    		    	   new_wM = yaW;
	    		       }
		    		}
	    		}
    		   BufferedImage img2 = new BufferedImage(new_wM, new_hM,
    			       BufferedImage.TYPE_INT_RGB);
    		   img2.getGraphics().drawImage(img.getScaledInstance(new_wM, new_hM, Image.SCALE_SMOOTH), 0, 0, new_wM, new_hM, null);
    		   FileOutputStream newimage=new FileOutputStream(fileName); //������ļ���
    		   JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage); 
               JPEGEncodeParam jep=JPEGCodec.getDefaultJPEGEncodeParam(img2); 
                /* ѹ������ */
               jep.setQuality((float)0.7, true); 
               encoder.encode(img2, jep); 
    		   
    		   /*FileOutputStream out = new FileOutputStream(fileName);  
    		   JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
    		   encoder.encode(img2);  
    		   out.close();*/
    		   ImageIO.write(img2, "jpg", new File(fileName));
    		}else{
    			   BufferedImage img2 = new BufferedImage(yaW, yaW,
        			       BufferedImage.TYPE_INT_RGB);
        		   img2.getGraphics().drawImage(bi.getScaledInstance(yaW, yaW, Image.SCALE_SMOOTH), 0, 0, yaW, yaW, null); 
        		   FileOutputStream newimage=new FileOutputStream(fileName); //������ļ���
        		   JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage); 
                   JPEGEncodeParam jep=JPEGCodec.getDefaultJPEGEncodeParam(img2); 
                    /* ѹ������ */ 
                   jep.setQuality((float)0.7, true); 
                   encoder.encode(img2, jep); 
        		   //ImageIO.write(img2, "jpg", new File(fileName));
    		}   		
    		
           
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    } 
    
    public ActionForward ajaxUpload(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request,HttpServletResponse response){
    	UploadImageForm fileIamge = (UploadImageForm)form;
    	FormFile files = fileIamge.getImageFile();   	
        try {  
        	String type = request.getParameter("type");
        	String empId = request.getParameter("empId");
        	String loginId = getLoginBean(request).getId();
        	if(type != null && "employee".equals(type)){
        		if(empId != null && !"".equals(empId)){
        			loginId = empId;
        		}
        	}

        	String path = BaseEnv.FILESERVERPATH+"/temp/"+loginId+".jpg";
            InputStream is = files.getInputStream();          
            System.out.println(files.getFileName());
            File file = new File(path);             
            /*
             * ָ���ļ��洢��·�����ļ��� 
             * �ж��Ƿ���� ����������һ���ļ���
             * ����ԭ���ʹ��ڣ���ɾ��ԭ����ͼƬ
             */
        	if(!(file.getParentFile().exists())){
        		file.getParentFile().mkdirs();
        	}else{
        		if(file.exists()){
        			FileHandler.deleteTemp(path); 
        		}
        	}       	
        	        	
        	/*
        	 *����һ��ͼƬ 
        	 */
            OutputStream os = new FileOutputStream(file); 
            
            byte[] b = new byte[is.available()];  
            int len = 0;  
            while((len = is.read(b)) != -1){  
                os.write(b, 0, len);  
            }  
            os.close();  
            is.close();     
            
            BufferedImage bi = ImageIO.read(new File(path));
    		int srcWidth = bi.getWidth(); // Դͼ���  		
    		int srcHeight = bi.getHeight(); // Դͼ�߶�
    		if(srcWidth > srcHeight){
    			request.setAttribute("whFlag", "yes");//�ж��Ǹߴ��ڿ���С�ڿ�
    		}else{
    			request.setAttribute("whFlag", "no");
    		}
            request.setAttribute("updateFlag", "update");
            request.setAttribute("myPhoto", loginId+".jpg");
            request.setAttribute("type", type);
            request.setAttribute("empId", empId);
        } catch (Exception e) {  
            e.printStackTrace();            
        }              
		return getForward(request, mapping, "uploadImage");
    }
    
    /**
     * �ҵĹ���̨������
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward set(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request,HttpServletResponse response){
    	
    	String column1 = getParameter("column1", request) ;
    	String column2 = getParameter("column2", request) ;
    	String column3 = getParameter("column3", request) ;
    	int width1 = getParameterInt("width1", request) ;
    	int width2 = getParameterInt("width2", request) ;
    	int width3 = getParameterInt("width3", request) ;
    	
    	Result result = mgt.addMyDesk(column1, column2, column3,width1,width2,width3, getLoginBean(request).getId(),"OA") ;
    	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    		request.setAttribute("dealAsyn", "true");
			EchoMessage.success().add(getMessage(
                    request, "common.msg.updateSuccess")).
                    setBackUrl("/MyDesktopAction.do").
                    setAlertRequest(request);
		}else{
			EchoMessage.error().add(getMessage(
                    request, "common.msg.updateErro")).
                    setBackUrl("/MyDesktopAction.do").
                    setAlertRequest(request);
		}
    	return getForward(request, mapping, "alert");
    }

    /**
     * ����ģ��������ʾ
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward querySingle(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) {
        
    	String type = getParameter("type", request) ;
        String deskId = getParameter("deskId", request) ;
        Integer rowCount = getParameterInt("rowCount", request) ;
        if(rowCount==null || rowCount==0){
        	rowCount = 5 ;
        }
    	LoginBean login = getLoginBean(request) ;
        if(type == null){
        	return getForward(request, mapping, "blank") ;
        }
        //ͼƬ����
        if("news".equals(type)){
        	Result result = mgt.getPicFromNews(deskId, login.getId()) ;
        	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS && result.retVal != null){
        		request.setAttribute("newsList", result.retVal) ;
        	}
        	return getForward(request, mapping, "picNews") ;
        }else if("workflow".equals(type)){
        	OAMyWorkFlowMgt flowMgt = new OAMyWorkFlowMgt();
    		OAMyWorkFlowForm flowForm = new OAMyWorkFlowForm();
    		flowForm.setPageSize(rowCount);
    		flowForm.setPageNo(1);
    		flowForm.setFlowBelong("other"); 
    		Result rs = flowMgt.query(flowForm, login.getId(),"transing",this.getLocale(request).toString(),true);
    		if(rs.retCode==ErrorCanst.DEFAULT_SUCCESS){
    			request.setAttribute("workFlowList", rs.getRetVal());
    		}
    		return getForward(request, mapping, "workflow") ;
        }else if("person".equals(type)){   
    		String myPhoto = GlobalsTool.checkEmployeePhoto("140", getLoginBean(request).getId());   		
        	request.setAttribute("myPhoto", myPhoto);
    		/*if(myPhoto==null || "".equals(myPhoto)){
    			EmployeeBean employee = (EmployeeBean) new UserMgt().detail(login.getId()).getRetVal() ;
    			if(employee.getPhoto()!=null && !"".equals(employee.getPhoto())){
    				myPhoto = employee.getPhoto().split(":")[0] ;
    			}
    		}
    		request.setAttribute("myPhoto", myPhoto) ; */
        	//ȡ��������
            String wisdom = new UserMgt().getWisdom();
            request.setAttribute("wisdom", wisdom);
            //����ʱ
            Result rs = mgt.getCountDown();
            if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS && rs.retVal != null){
            	String[] cds = (String[])rs.retVal;
            	String countDownType = cds[0];
            	String countDownTitle = cds[1];
            	String countDownDate = cds[2];
            	try{
	            	long baseDate= 0;
	            	if("1".equals(countDownType)){
	            		countDownTitle = this.getMessage(request, "countDown.lb.leaveMunt");
	            		Calendar lastDate = Calendar.getInstance();   
	            		lastDate.set(Calendar.DAY_OF_MONTH,1);//��Ϊ��ǰ�µ�1��  
	            		lastDate.add(Calendar.MONTH,1);//��һ���£���Ϊ���µ�1�� 
	            		lastDate.add(Calendar.DAY_OF_MONTH,-1);//��ȥһ�죬��Ϊ�������һ�� 	            	
	            		baseDate = BaseDateFormat.parse(BaseDateFormat.format(lastDate.getTime(), BaseDateFormat.yyyyMMdd), BaseDateFormat.yyyyMMdd).getTime();
	            	}else if("2".equals(countDownType)){
	            		countDownTitle =this.getMessage(request, "countDown.lb.leaveyear"); 
	            		Date date = new Date();   
	            		SimpleDateFormat   dateFormat   =   new   SimpleDateFormat("yyyy");//���Է�����޸����ڸ�ʽ      
	            		String  years  = dateFormat.format(date);      
	            		baseDate = BaseDateFormat.parse(years+"-12-31", BaseDateFormat.yyyyMMdd).getTime();
	            	}else{
	            			baseDate = BaseDateFormat.parse(countDownDate, BaseDateFormat.yyyyMMdd).getTime();
	            	}
	            	long curDate = new Date().getTime();
	            	
	            	long leaveDate = ((baseDate - curDate) / (24*60*60000));
	            	leaveDate ++;  
	            	request.setAttribute("countDownTitle", countDownTitle);
	            	request.setAttribute("countDownValue", leaveDate+"");
	            }catch(Exception e){
	            	 e.printStackTrace() ;
	            }
            }
            return getForward(request, mapping, "person") ;
        }else if("wish".equals(type)){
        	 /* �������еĹ��ٰ����Ϣ*/
    		Result result = new MyDesktopMgt().queryAllfameTop(0);
    		if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
    			request.setAttribute("listfameTop", result.getRetVal());
    		}
    		return getForward(request, mapping, "wish") ;
        }else if("chart".equals(type) || "chart2".equals(type) || "yibiaopan".equals(type)){
        	request.setAttribute("chartType", type) ;
        	return getForward(request, mapping, "mysales") ;
        }else if("common".equals(type) || "news".equals(type)){
        	Result result = mgt.queryDesk(deskId, rowCount, login.getId()) ;
        	if(result.retCode == ErrorCanst.DEFAULT_SUCCESS){
        		request.setAttribute("msgList", result.retVal) ;
        	}
        	return getForward(request, mapping, "common") ;
        }else if("attentionList".equals(type)){
        	return attentionList(mapping, form, request, response) ;
        }else if("msgCount".equals(request.getParameter("type"))){
    		return getMsgCount(mapping, form, request, response) ;
    	}
        return getForward(request, mapping, "blank") ;
    }
    
    /**
	 * ��ȡ��Ϣ������
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward getMsgCount(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response){
		String userId = this.getLoginBean(request).getId();
		Result rs = new MyDesktopMgt().queryNoReadMsg(userId) ;
		if(rs.retCode == ErrorCanst.DEFAULT_SUCCESS){
			request.setAttribute("msg", rs.retVal);
		}
		return getForward(request, mapping, "blank") ;
	}
	

    /**
     * ��ת������̨
     *
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    private ActionForward queryAll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) {
		
    	//�Զ�������
		Result myDesk = mgt.queryMyDesk(getLoginBean(request).getId(),"OA");
		if (myDesk.retCode==ErrorCanst.DEFAULT_SUCCESS) {
			request.setAttribute("deskMap", myDesk.retVal);
			HashMap<Integer, List<MyDeskBean>> map = (HashMap<Integer, List<MyDeskBean>>) myDesk.retVal ;
			if(map.get(1)!=null && map.get(1).size()>0){
				request.setAttribute("width1", map.get(1).get(0).getWidth()) ;
			}
			if(map.get(2)!=null && map.get(2).size()>0){
				if(map.get(3)==null){
					request.setAttribute("width2", map.get(2).get(0).getWidth()-1) ;
				}else{
					request.setAttribute("width2", map.get(2).get(0).getWidth()) ;
				}
			}
			if(map.get(3)!=null && map.get(3).size()>0){
				request.setAttribute("width3", map.get(3).get(0).getWidth()-1) ;
			}
		}
        return mapping.findForward("main");
    }

	/**
	 * ����Ȩ���������
	 */
	protected ActionForward doAuth(HttpServletRequest req, ActionMapping mapping) {
		LoginBean loginBean = getLoginBean(req) ;
        if (loginBean == null) {
            BaseEnv.log.debug("MgtBaseAction.doAuth() ---------- loginBean is null");
            return getForward(req, mapping, "indexPage");
        }
		return null ;
	}

    
}
