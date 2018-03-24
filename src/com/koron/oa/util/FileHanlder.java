package com.koron.oa.util;

import java.io.File;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;

import com.koron.oa.bean.FileBean;

import java.util.List;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;
/**
 *
 * <p>Title: 文件处理类</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 科荣软件</p>
 *
 * @author 雷永辉
 * @version 1.0
 */
public class FileHanlder {
    public FileHanlder() {
    }

    //修改附件的方法
    //nowAccessories:修改以后的附件字符串
    //newAccessories:新上传的附件字符串
    //oldAccessories：数据库里原有的附件字符串
    public String updateAccessories(HttpServletRequest request,
                                    String nowAccessories,
                                    String newAccessories,
                                    String oldAccessories) {
        String[] oldacc_array = null;
        String[] nowacc_array = null;
        //得到原有的附件字符串
        if (null != oldAccessories && !"".equals(oldAccessories)&&!"null".equals(oldAccessories)&&!"NULL".equals(oldAccessories)) {
            oldacc_array = oldAccessories.split(";");
        }
        //得到现在有的附件字符串
        if (null != nowAccessories && !"".equals(nowAccessories)&&!"null".equals(oldAccessories)&&!"NULL".equals(oldAccessories)) {
            nowacc_array = nowAccessories.split(";");
        }
        //当原有的字符串不为空时
        if (null != oldacc_array) {
            //要删除的文件
            String delted = "";
            for (String a : oldacc_array) {
                //当原来有的附件名，而现在没有的时候
                if (!this.getDletedFile(nowacc_array, a)) {
                    delted += a + ";";
                }
            }
            //从服务器删除已经没有的附件名称的文件
            deleteAccessories(delted, request);
        }
        return (nowAccessories==null?"":nowAccessories) + newAccessories;
    }

    //下载附件的方法
    //downloadFileName：存放在服务器的需要下载文件名
    //goAction：下载过错后，反回的ActionForward（到哪个页面）
    public ActionForward downFile(String downloadFileName,
                                  ActionForward goAction,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
            Exception {

        String strFileName = downloadFileName;
        String path = request.getRealPath("/upload");
        File file = new File(path + "\\" + strFileName);
        System.out.println("file==========" + file);
        if (file.exists()) {
            try {
                BufferedInputStream bis = new BufferedInputStream(new
                        FileInputStream(file));
                byte[] buffer = new byte[1024];
                strFileName = java.net.URLEncoder.encode(strFileName, "UTF-8"); //处理中文文件名的问题
                strFileName = new String(strFileName.getBytes("UTF-8"), "GBK"); //处理中文文件名的问题
                response.reset();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/x-rar-compressed"); //不同类型的文件对应不同的MIME类型
                response.setHeader("Content-Disposition",
                                   "attachment; filename=" + strFileName);
                OutputStream os = response.getOutputStream();
                while (bis.read(buffer) > 0) {
                    os.write(buffer);
                }
                bis.close();
                os.close();
            } catch (Exception e) {
                System.out.println("下载文件时出错!");
            }
        } else {
            System.out.println("文件不存在!");
            request.setAttribute("stat", "exist");
        }
        return goAction;

    }


    public boolean getDletedFile(String[] str_array, String str) {
        boolean talg = false;
        if (null != str_array) {
            for (String s : str_array) {
                if (s.equals(str)) {
                    talg = true;
                }
            }
        }
        return talg;
    }

    //删除附件的方法
    //needDseletedAccessories:需要删除的附件字符串
    //path：服务器路径
    public boolean deleteAccessories(String needDseletedAccessories,
                                     HttpServletRequest request) {
        String path = request.getRealPath("/upload");
        boolean talg = true;
        if (null != needDseletedAccessories &&
            !"".equals(needDseletedAccessories)) {
            String[] delted_array = needDseletedAccessories.split(";");
            if (null != delted_array) {
                for (String s : delted_array) {
                    String[] _s = s.split(",");
                    String fileName = null;
                    if (_s.length > 1) {
                        fileName = _s[1];
                    }
                    if (null != fileName && !"".equals(fileName)) {
                        try {
                            File file = new File(path + "\\" + fileName);
                            file.delete();
                        } catch (Exception ex) {
                            talg = false;
                            ex.printStackTrace();
                        }
                    }

                }

            }
        }
        return talg;
    }
    //得到附件的方法
    public List<FileBean> getAccessories(String accessories) {
        List<FileBean> accessories_list = null;
        if (null != accessories && !"".equals(accessories)&&!"null".equals(accessories)&&!"NULL".equals(accessories)) {
            accessories_list = new ArrayList<FileBean>();
            String accessories_arry[] = accessories.split(";");
            for (String accessorie : accessories_arry) {
                if(null != accessorie && !"".equals(accessorie)&&!"null".equals(accessorie)&&!"NULL".equals(accessorie))
                {
                 String files[] = accessorie.split(",");
                 if(null!=files && files.length>=2)
                 {
                    FileBean fb = new FileBean();
                    fb.setFileName(files[0]);
                    fb.setDownLoadName(files[1]);
                    accessories_list.add(fb);

                 }


                }

            }
        }
        return accessories_list;

    }
    //得到附件的方法
    public List<FileBean> getAccessories(String accessories,HttpServletRequest request) {
        String path = request.getRealPath("/upload");
        List<FileBean> accessories_list = null;
        if (null != accessories && !"".equals(accessories)&&!"null".equals(accessories)&&!"NULL".equals(accessories)) {
            accessories_list = new ArrayList<FileBean>();
            String accessories_arry[] = accessories.split(";");
            for (String accessorie : accessories_arry) {
                if(null != accessorie && !"".equals(accessorie)&&!"null".equals(accessorie)&&!"NULL".equals(accessorie))
                {
                 String files[] = accessorie.split(",");
                  FileBean fb = new FileBean();
                  fb.setFileName(files[0]);
                  fb.setDownLoadName(files[1]);
                  File file = new File(path + "\\" + fb.getDownLoadName());
                  fb.setFile(file);
                  accessories_list.add(fb);

                }

            }
        }
        return accessories_list;

    }
    //拷贝附件
    public String copyFile(HttpServletRequest request, String oldaccessories) {
        String path = request.getRealPath("/upload");
        String newaccessories = "";
        List<FileBean> files = getAccessories(oldaccessories);
        if(null!=files)
        {
            for(FileBean f : files)
            {
                String oldFileName = path + "\\" + f.getDownLoadName();
                File file = new File(oldFileName);
                if (null != file) {
                    int substr_start = f.getDownLoadName().lastIndexOf(".");
                    String extendName = f.getDownLoadName().substring(substr_start);
                    int randomNumber =  (int)(new Random().nextDouble()*1000);
                    String newFileName = new java.util.Date().getTime() + ""+randomNumber+"" +extendName;
                    if (Copy(file, path + "\\"+newFileName)) {
                        newaccessories+=f.getFileName()+","+newFileName+";";
                    }
                }

            }
        }
        return newaccessories;
    }
    //拷贝附件
    public static String copyFile(HttpServletRequest request, File file) {
        String path = request.getRealPath("/upload");
        String newaccessories = "";

                if (null != file) {

                    int substr_start = file.getPath().lastIndexOf(".");
                    String extendName =  file.getPath().substring(substr_start);
                    int randomNumber =  (int)(new Random().nextDouble()*1000);
                    String newFileName = new java.util.Date().getTime() + ""+randomNumber+"" +extendName;
                    if (Copy(file, path + "\\"+newFileName)) {
                        newaccessories=file.getName()+","+newFileName+";";
                    }
                }
        return newaccessories;
    }
    public static boolean Copy(File oldfile, String newPath)
    {
        boolean talg = false;
        try {
            int bytesum = 0;
            int byteread = 0;
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldfile);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while (
                        (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            talg = true;
        } catch (Exception e)
        {
            System.out.println("error  ");
            e.printStackTrace();
            talg = false;
        }
        return talg;

    }
}
