package com.menyi.web.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.http.HttpServletRequest;
import sun.misc.BASE64Encoder;
/**
 * <p>Title:加密 符合汇讯单点登录（调用客户端对话框）的用户名 密码 对方用户名加密方式</p> 
 * <p>Description: </p>
 * @Date:
 * @Copyright: 科荣软件
 * @Author 毛晶
 * @preserve all
 */
public class CommonUtil {
	private static SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");

	public static String validateFileType(File file) {
		if (file == null)
			return "";

		String fileType = null;
		try {
			byte[] b = new byte[50];
			InputStream iStream = new FileInputStream(file);
			iStream.read(b);
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < b.length; i++) {
				int v = b[i] & 0xFF;
				String hv = Integer.toHexString(v);
				if (hv.length() < 2) {
					stringBuilder.append(0);
				}
				stringBuilder.append(hv);
			}
			String filetypeHex = String.valueOf(stringBuilder.toString());
			if (filetypeHex.toUpperCase().startsWith("D0CF11E0"))
				fileType = "xls";
			iStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileType;
	}
	
	
	
	/**
	 * 真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	
	/**
	 * 回去当前日期 格式为 yyyyMMdd
	 * @return
	 */
	public static String getDate(){
		return sdf.format(new Date());
	}
	
	/**
	 * 对字符串进行BASE64加密
	 */
	public static String encryptByBASE64(String encryptString) throws Exception {
		if(encryptString == null || encryptString.equals("")) {
			return null;
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(encryptString.getBytes());
	}
	
	/**
	 * 对字符串进行DES加密
	 */
	public static String encryptByDES(String encryptString) throws Exception {
		if(encryptString == null || encryptString.equals("")) {
			return null;
		}
		Security.addProvider(new com.sun.crypto.provider.SunJCE()); 
		Cipher ecipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		String date = getDate();
		DESKeySpec desKeySpec = new DESKeySpec(date.getBytes("utf-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(desKeySpec);
		ecipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptBytes = ecipher.doFinal(encryptString.getBytes("utf-8"));
		return byte2hex(encryptBytes);
	}
	
	/** 
     * 将二进制转化为16进制字符串 
     *  
     * @param b 
     *            二进制字节数组 
     * @return String 
     */  
    private static String byte2hex(byte[] b) {  
        String hs = "";  
        String stmp = "";  
        for (int n = 0; n < b.length; n++) {  
            stmp = (Integer.toHexString(b[n] & 0XFF));  
            if (stmp.length() == 1) {  
                hs = hs + "0" + stmp;  
            } else {  
                hs = hs + stmp;  
            }  
        }  
        return hs.toUpperCase();  
    }
    /**
     * by dw
     * @param par
     * @return
     */
    public static String killHackSqlIn(String par){
		String[] x = {"'"};
		String[] y = {"''"};
		String str = par;
		for(int i=0;i<x.length;i++){
			str = str.replaceAll(x[i], y[i]);
		}		
		return str;
	}
	
	public static void main(String args[]){		
		//System.out.println(CommonUtil.ConvertToEnglish(""));
		try {
			//System.out.println(new SimpleDateFormat("yyyyMMdd").format(new Date()));
			System.out.println(URLEncoder.encode(CommonUtil.encryptByBASE64("admin"), "utf-8"));
			//System.out.println(CommonUtil.encryptByDES("admin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
