package com.menyi.web.util;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.security.Security;
import java.net.URL;
import java.io.File;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * <p>Title: </p>
 *
 * <p>Description: 用于和服务器连接，发送数据，并接收返回结果，发送和接收的数据必须加密,具体协议为
 * <operation>操作类型</operation><数据标识>数据</数据标识>
 * </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class ServerConnection {
    private String serverurl = "";
    //添加新安全算法,如果用JCE就要把它添加进去
    private byte[] keyBytes = {0x21, 0x12, 0xF, 0x58, (byte) 0x88, 0x10, 0x40, 0x38
                              , 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD, 0x55, 0x66
                              , 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37, (byte) 0xE2}; //24字节的密钥

    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish
    //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）
    public byte[] encryptMode(byte[] src) {
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
    public byte[] decryptMode(byte[] src) {
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

    //转换成十六进制字符串
    public String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = hs + ":";
        }
        return hs.toUpperCase();
    }

    public static void main(String[] args) {
        String szSrc = "This is a 3DES test. 测试";
        System.out.println("加密前的字符串:" + szSrc);
        ServerConnection con = new ServerConnection("", new byte[] {0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10, 0x40, 0x38
            , 0x28, 0x25, 0x79, 0x51, (byte) 0xCB, (byte) 0xDD, 0x55, 0x66
            , 0x77, 0x29, 0x74, (byte) 0x98, 0x30, 0x40, 0x36, (byte) 0xE2});

        byte[] encoded = con.encryptMode(szSrc.getBytes());
        System.out.println("加密后的字符串:" + new String(encoded));
        byte[] srcBytes = con.decryptMode(encoded);
        System.out.println("解密后的字符串:" + (new String(srcBytes)));
    }


    public ServerConnection(String url, byte[] keyBytes) {
        this.serverurl = url;
        this.keyBytes = keyBytes;
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
    }

    /**
     * 方法是同步的
     * @return String
     */
    public String send(String sendStr) {

        try {
            //对sendStr加密
            byte[] sendbs = encryptMode(sendStr.getBytes("GB2312"));
            if(sendbs == null) return null;

            URL url = new URL(serverurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(sendbs);
            os.flush();
            os.close();

            //得到返回的信息
            InputStream is = conn.getInputStream();

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
            if(bs == null) return null;
            String rsb = new String(bs);
            return rsb;
        }
        catch (Exception ee) {
//        	BaseEnv.log.error("ServerConnection.send Error",ee);
//            ee.printStackTrace();
        }
        return null;
    }
    
    /**
     * 方法是同步的
     * 反送后，返回未加密byte数组
     * @return String
     */
    public byte[] sendNoEncrypt(String sendStr) {

        try {
            //对sendStr加密
            byte[] sendbs = encryptMode(sendStr.getBytes("GB2312"));
            if(sendbs == null) return null;

            URL url = new URL(serverurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(sendbs);
            os.flush();
            os.close();

            //得到返回的信息
            InputStream is = conn.getInputStream();

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
            return bs;
        }
        catch (Exception ee) {
            ee.printStackTrace();
        }
        return null;
    }

}
