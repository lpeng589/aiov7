package com.menyi.web.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.io.*;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.http.HttpServletResponse;

import antlr.StringUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * <p>Title: </p>
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
public class FileHandler {
    public static final int TYPE_PIC = 0;
    public static final int TYPE_AFFIX = 1;

    private static long curtime;
    private static Object oLock = new Object();

    /**
     * 写临时文件
     * @param oldFileName String
     * @param filebytes byte[]
     * @return String
     */
    public static String writeTemp(String oldFileName,
                                   byte[] filebytes) {
        if (filebytes == null ||
            filebytes.length == 0) {
            return null;
        }
        //生成文件名
        long time;
        synchronized (oLock) {
            time = System.currentTimeMillis();
            while (time <= curtime) {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                }
                time = System.currentTimeMillis();
            }
            curtime = time;
        }
        String path = getPathTemp(oldFileName, time) ;
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(filebytes);
            fos.close();
            return path.substring(path.lastIndexOf("/") + 1);
        } catch (Exception ex1) {
            BaseEnv.log.error("FileHandler.writeTemp Error ", ex1);
        }
        return null;
    }

    /**
     * 移入正式目录
     * @param tableName String
     * @param type int
     * @param fileName String
     * @return boolean
     */
    public static boolean copy(String tableName, int type,String tempFileName, String fileName) {
        if (tableName == null || tableName.length() == 0 || fileName == null ||
            fileName.length() == 0) {
            return false;
        }

        if(fileName.indexOf(":")!=-1){
        	fileName = fileName.split(":")[0] ;
        }
        String path = getPathReal(tableName, type, fileName);
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            File oldFile = new File(getPathTempRead(tempFileName));
            if(!oldFile.exists()){
            	return false;
            }
            FileInputStream fis = new FileInputStream(oldFile);
            FileOutputStream fos = new FileOutputStream(file);
            int i = 0;
            byte[] bs = new byte[8192];
            while ((i = fis.read(bs)) > -1) {
                fos.write(bs, 0, i);
            }
            fis.close();
            fos.close();
            return true;
        } catch (Exception ex1) {
            BaseEnv.log.error("FileHandler.move Error ", ex1);
        }
        return false;
    }

    public static void copyFile(String file1,String file2){
    	try {
            File oldFile = new File(file1);
            FileInputStream fis = new FileInputStream(oldFile);
            FileOutputStream fos = new FileOutputStream(file2);
            int i = 0;
            byte[] bs = new byte[8192];
            while ((i = fis.read(bs)) > -1) {
                fos.write(bs, 0, i);
            }
            fis.close();
            fos.close();
        } catch (Exception ex1) {
            BaseEnv.log.error("FileHandler.copyFile Error ", ex1);
        }
    }

    public static void readFile(String fileName,HttpServletResponse resp) throws Exception{
    	//Content-Length: 222996
    	File existFile = new File(fileName);
    	if(!existFile.exists()){
    		BaseEnv.log.error("文件："+fileName+"-不存在");
    		return ;
    	}
        FileInputStream fis = new FileInputStream(fileName);
        resp.setContentLength(fis.available());
        OutputStream out = resp.getOutputStream();
        byte[] b = new byte[20 * 1024];
        int i = 0;
        while ((i = fis.read(b)) > -1) {
        	out.write(b, 0, i);
        }
        fis.close();
    }
    
    public static void compressPic(String srcFilePath,HttpServletResponse resp)  
    {  
        try {
            File srcfile = new File(srcFilePath);
            if (!srcfile.exists()) {
                System.out.println("文件不存在");
                return;
            }
            BufferedImage image = ImageIO.read(srcfile);

            // 获得缩放的比例
            double ratio = 1.0;
            // 判断如果高、宽都不大于设定值，则不处理
            if (image.getHeight() > 100 || image.getWidth() > 100) {
                if (image.getHeight() > image.getWidth()) {
                    ratio = 100.0 / image.getHeight();
                } else {
                    ratio = 100.0 / image.getWidth();
                }
            }
            // 计算新的图面宽度和高度
            int newWidth = (int) (image.getWidth() * ratio);
            int newHeight = (int) (image.getHeight() * ratio);

            BufferedImage bfImage = new BufferedImage(newWidth, newHeight,
                    BufferedImage.TYPE_INT_RGB);
            bfImage.getGraphics().drawImage(
                    image.getScaledInstance(newWidth, newHeight,
                            Image.SCALE_SMOOTH), 0, 0, null);

            OutputStream os = resp.getOutputStream();
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
            encoder.encode(bfImage);
            os.close();
        } catch (Exception e) {
            BaseEnv.log.debug("FileHandler.compressPic",e);
        }
    }  
    
    
    
    public static String readFile(String fileName) throws Exception{
    	File existFile = new File(fileName);
    	if(!existFile.exists()){
    		BaseEnv.log.error("文件："+fileName+"-不存在");
    		return "";
    	}
    	StringBuilder sb = new StringBuilder();
    	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
    	String line = br.readLine();
    	while(line != null){
    		sb.append(line+"<br>");
    		line = br.readLine();
    	}
    	return sb.toString();
    }
    
    public static boolean deleteTemp(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return false;
        }
        String path = getPathTempRead(fileName);
        File file = new File(path);
        return file.delete();
    }

    public static boolean delete(String tableName, int type, String fileName) {
        if (fileName == null || fileName.length() == 0 || tableName == null ||
            tableName.length() == 0) {
            return false;
        }
        String path = getPathReal(tableName, type, fileName);
        File file = new File(path);
        return file.delete();
    }


    /**
     * 读取临时文件的路径
     * @param fileName String
     * @return String
     */
    public static String getPathTempRead(String fileName) {
        String path = BaseEnv.FILESERVERPATH;
        if (!path.trim().endsWith("/")) {
            path = path.trim() + "/";
        }
        path += "temp/" + fileName;
        return path;
    }

    /**
     * 取读文件时的路径,正式路径
     * @param tableName String
     * @param type int
     * @param fileName String
     * @return String
     */
    public static String getPathReal(String tableName, int type,
                                      String fileName) {
    	
    	if(fileName != null && fileName.startsWith("DISK")){
    		String disk = fileName.substring(4,5);
    		fileName = fileName.substring(5);
    		if(fileName.startsWith(":")){
    			fileName = disk+""+fileName;
    		}else{
    			fileName = disk+":"+fileName;
    		}
    		//这是文件服务器文件   	
    		BaseEnv.log.debug("取本地文件:"+fileName);
    		return fileName;
    	}
    	if(fileName != null && fileName.startsWith("PICPATH")){
    		fileName = fileName.substring(8);
    		String picPath = GlobalsTool.getSysSetting("picPath");
        	if(picPath.endsWith("\\")|| picPath.endsWith("/")){
        		picPath = picPath.substring(0,picPath.length()  -1);
        	}
    		fileName = picPath+"\\"+fileName;
    		//这是文件服务器文件   	
    		BaseEnv.log.debug("取本地文件:"+fileName);
    		return fileName;
    	}
        String path = BaseEnv.FILESERVERPATH;
        if (!path.trim().endsWith("/")) {
            path = path.trim() + "/";
        }
        if (type == TYPE_PIC) {
            path = path + "pic/";
        } else {
            path = path + "affix/";
        }
        if(fileName.indexOf(";") > 0){
        	fileName = fileName.substring(0,fileName.indexOf(";"));
        }
        String billFilePath  = GlobalsTool.getSysSetting("billFilePath");
        if("false".equals(billFilePath)){
        	path += fileName; //不分单据保存
        }else{
        	path += tableName + "/" + fileName;
        }
        return path;
    }
    
    //如果重名，需自动改名字
    public static String getRealFileName(String tableName, int type,
            String fileNameStr){
    	String oldfileName = getPathReal(tableName,type,fileNameStr);
    	
        String fileName = oldfileName;
        File f = new File(fileName);                        
        for(int i=1;f.exists()&& i<1000;i++){ 
        	//文件名存在，改名
        	fileName = oldfileName.substring(0,oldfileName.lastIndexOf("."))+"("+i+")"+oldfileName.substring(oldfileName.lastIndexOf("."));
        	f = new File(fileName); 
        }
        return f.getName();
    }

    /**
     * 写临时文件时的路径
     * @param oldFileName String
     * @param time long
     * @return String
     */
    private static String getPathTemp(String oldFileName, long time) {
        String path = BaseEnv.FILESERVERPATH;
        if (!path.trim().endsWith("/")) {
            path = path.trim() + "/";
        }

        path += "temp/" + time +
                oldFileName.substring(oldFileName.lastIndexOf("."));
        return path;
    }

    private static String getPathTemp(String oldFileName) {
        String path = BaseEnv.FILESERVERPATH;
        if (!path.trim().endsWith("/")) {
            path = path.trim() + "/";
        }

        path += "temp/" +oldFileName;
        return path;
    }
    

    /**
	 * 压缩图片方法
	 * 
	 * @param oldFile 将要压缩的图片
	 * @param width 压缩宽
	 * @param height 压缩高
	 * @param quality 压缩清晰度 <b>建议为1.0</b>
	 * @param smallIcon 压缩图片后,添加的扩展名（在图片后缀名前添加）
	 * @param percentage 是否等比压缩 若true宽高比率将将自动调整
	 * @author zhengsunlei
	 * @return 如果处理正确返回压缩后的文件名 null则参数可能有误
	 */
	public static String doCompress(String oldFile, String newName, String newPath, int nSize) {
		if (oldFile != null) {

			File fPath = new File(newPath);
			if (!fPath.exists()) {
				fPath.mkdirs();
			}
			
			/* 压缩后的文件名 */
			String newImage = null;
			if (newName.indexOf(".") == -1) {
				newName += ".jpg";
			}
			newImage = newPath + newName;

			Image srcFile = null;
			File file = new File(oldFile);
			if(!file.exists()) return null ;
			if (null == newPath && "".equals(newPath))
				newPath = oldFile.substring(0, oldFile.lastIndexOf('/'));
			
			try {
				/* 读取图片信息 */
				srcFile = ImageIO.read(file);
				if (null == srcFile) {
					return null;
				}
				if (oldFile.equals(newImage) && srcFile.getWidth(null) <= nSize && srcFile.getHeight(null) <= nSize) {
					// 已经符合规定的大小
					return newImage;
				}
				
				int new_w = nSize;
				int new_h = nSize;
				// 为等比缩放计算输出的图片宽度及高度
				double rate1 = ((double) srcFile.getWidth(null))
							/ (double) nSize;
				double rate2 = ((double) srcFile.getHeight(null))
							/ (double) nSize;
				double rate = rate1 > rate2 ? rate1 : rate2;
				new_w = (int) (((double) srcFile.getWidth(null)) / rate);
				new_h = (int) (((double) srcFile.getHeight(null)) / rate);
				
				/* 宽高设定 */
				BufferedImage tag = new BufferedImage(new_w, new_h,
						BufferedImage.TYPE_INT_RGB);
				tag.getGraphics().drawImage(srcFile, 0, 0, new_w, new_h, null);


				/* 压缩之后临时存放位置 */
				File f = new File(newImage);
				if (!f.getParentFile().exists()) {
					f.mkdirs();
				}
				FileOutputStream out = new FileOutputStream(newImage);

				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);

				/* 压缩质量 */
				jep.setQuality(1, true);
				encoder.encode(tag, jep);

				out.close();

			}catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != srcFile)
					srcFile.flush();
			}
			return newImage;
		} else {
			return null;
		}
	}
}
