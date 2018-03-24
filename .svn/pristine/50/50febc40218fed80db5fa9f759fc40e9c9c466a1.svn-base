package com.menyi.web.util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 软件加密
 * @author Administrator
 * 软加密的在线注册流程
 * 输入序列号及公司信息及特征码，转送至bol88，并取得该序列号对应证书写入当前目录
 * 软加密离线注册流程
 * 离线注册界面显示特征码。bol88离线注册，输入序列号及公司信息及特征码。下载证书，aio导入证书。
 * 软加密在线升级。
 * 公司总部改写功能信息。aio点击升级，传送序列号到bol88,并取得新证书写入当前目录。
 * 软加密离线升级。bol88下载新的证书，aio导入。
 * 
 * 证书文件内容，MD5（32位（针对加密内容）），加密内容（序列号（32），机器特征码(32),功能点），
 * 读取证书后，
 * 1、要校验内容的MD5码与证书中前32位是否一致，
 * 2、核验机器特征码是否一致。
 * 每次启动时，要传送MD5,序列号，机器特殊码是否一致
 * 
 */
public class SecurityLock2 {
	/**
	 * 版本：0 ：工贸版1，专业版2，布匹版3  单字节
	 * 进销存财务用户:1 双字节
	 * 生产用户:3 双字节
	 * 电商用户:5 双字节
	 * OA用户:7 双字节
	 * CRM用户:9 双字节
	 * 下单用户:11 双字节
	 * HR用户:13 双字节
	 * 时间控制:15 四字节
	 * 
	 * 基本模块：19：    进销存基本模块1，OA模块2，CRM模块4，HR8， 用户自定义16，分支机构32，多币种64，生产128. ，进行异或操作 单字节
	 * 多语言：20：    单字节
	 */
	private byte[] mbs;
	public byte[] md5;
	public String mId;
	public String seriesId;
	

	/**
	 * 加载软证书。	 
	 * 证书文件内容，MD5（32位（针对加密内容）），加密内容（序列号（32），机器特征码(32),功能点），
	 * 1、读取文件前32位，记录为MD5。
	 * 2、读取文件32位后。
	 * 3、校验内容的MD5码与证书中前32位是否一致，
	 * 3、DES解码
	 * 1、取序列号
	 * 5、取机器特征码
	 * 2、核验机器特征码是否一致。
	 * 7、加载功能码。
	 * @return -1:MD5不一致，-2机器码不一致。
	 *
	 */
	
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
        return stringBuilder.toString().toUpperCase();  
    } 
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
	private byte[] getDK(){
		return new byte[] { (byte) -113, (byte) -63, (byte) -128, (byte) -78, (byte) -36, (byte) -93, (byte) -122, (byte) 5, (byte) -62, (byte) -21, (byte) 16, (byte) -66, (byte) 20, (byte) 24, (byte) -69, (byte) 76};
	}
	
	public byte get(int pos){
		return mbs[pos];
	}
	public short getS(int pos){
		return bytesToShort(mbs,pos);
	}
	public int getI(int pos){
		return bytesToInt(mbs,pos);
	}
	public void put(int pos,byte b){
		mbs[pos] = b;
	}
	public void putS(int pos,short s){
		shortToBytes(s,mbs,pos);
	}
	public void putI(int pos,int s){
		intToBytes(s,mbs,pos);
	}
	/**
	 * 重写软证书
	 * 软注册升级后，重写证书
	 */
	public void reWrite(byte[] bs) throws Exception{
		FileOutputStream fos = new FileOutputStream("aio.cert");
		fos.write(bs);
		fos.close();
	}
	
	private  void shortToBytes(short i, byte[] buf, int offset) {
		buf[offset + 0] = (byte) (0xff & i);
		buf[offset + 1] = (byte) ((0xff00 & i) >> 8);
	}

	public static  short bytesToShort(byte abyte0[], int offset) {
		return (short) ((short) ((0xff & abyte0[offset + 1]) << 8) | (short) (0xff & abyte0[offset + 0]));
	}
	
	public static void intToBytes(int i, byte[] buf, int offset) {
		buf[offset + 0] = (byte) (0xff & i);
		buf[offset + 1] = (byte) ((0xff00 & i) >> 8);
		buf[offset + 2] = (byte) ((0xff0000 & i) >> 16);
		buf[offset + 3] = (byte) ((0xff000000 & i) >> 24);
	}
	public static int bytesToInt(byte abyte0[], int offset) {
		return (0xff & abyte0[offset + 3]) << 24
				| (0xff & abyte0[offset + 2]) << 16
				| (0xff & abyte0[offset + 1]) << 8 
				| 0xff & abyte0[offset + 0];
	}
	
	public static void main(String[] args){
		System.out.println(Short.MAX_VALUE);
		byte b = (byte)1|4|8|16;
		System.out.println(b);
		System.out.println(b&0x80);
		System.out.println(b&0x01);
		System.out.println(b&0x02);
		System.out.println(b&0x04);
		System.out.println(b&0x08);
		System.out.println(b&0x10);
		System.out.println(b&0x20);
		System.out.println(b&0x40);
	}
	
		
}
