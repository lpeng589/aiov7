package com.menyi.web.util;

import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import java.security.Security;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.InputStream;

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
public class ClientConnection extends HttpServlet {
    //添加新安全算法,如果用JCE就要把它添加进去
    private static byte[] keyBytes = {0x21, 0x12, 0xF, 0x58, (byte) 0x88, 0x10, 0x40, 0x38
                              , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                              , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2}; //24字节的密钥

    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish
    //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);

            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decryptMode(byte[] src) {
        try { //生成密钥
            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    public static String bytesToHexString(byte[] src){  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    }  
    /** 
     * Convert hex string to byte[] 
     * @param hexString the hex string 
     * @return byte[] 
     */  
    public static byte[] hexStringToBytes(String hexString) {  
        if (hexString == null || hexString.equals("")) {  
            return null;  
        }  
        hexString = hexString.toUpperCase();  
        int length = hexString.length() / 2;  
        char[] hexChars = hexString.toCharArray();  
        byte[] d = new byte[length];  
        for (int i = 0; i < length; i++) {  
            int pos = i * 2;  
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
        }  
        return d;  
    } 
    private static byte charToByte(char c) {  
        return (byte) "0123456789ABCDEF".indexOf(c);  
    } 

    public void init() throws ServletException {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
    }


    public void service(HttpServletRequest req, HttpServletResponse resp) throws
        ServletException, IOException {
        InputStream is = req.getInputStream();
        byte[] inbuf = new byte[1024];
            byte[] bs = new byte[0];
            int rn;
            while ((rn = is.read(inbuf)) > 0) {
                byte[] temp = bs;
                bs = new byte[rn + bs.length];
                System.arraycopy(temp, 0, bs, 0, temp.length);
                System.arraycopy(inbuf, 0, bs, temp.length, rn);
            }

            is.close();
            bs = decryptMode(bs);
            if(bs == null) {
                System.out.println("ClientConnection get null Data ");
                return;
            }
            String rsb = new String(bs);
            System.out.println("" + rsb);
            String operation = rsb.substring(rsb.indexOf("<operation>")+"<operation>".length(),rsb.indexOf("</operation>"));
            String ret = "";
            if("evaluate".equals(operation)){
                ret = "<result>10000</result>";
            }else if("formal".equals(operation)){
                ret = "<result>ok</result>";
            }else if("refresh".equals(operation)){
                ret = "rem:com.menyi.web.util.SystemState.FAC245323456BC442D5B35B4D.JAVA";
            }
            byte[] sendbs = encryptMode(ret.getBytes());
            if(sendbs == null) {
                System.out.println("ClientConnection return Data encrypt null");
                return;
            }
            resp.getOutputStream().write(sendbs);
    }
}
