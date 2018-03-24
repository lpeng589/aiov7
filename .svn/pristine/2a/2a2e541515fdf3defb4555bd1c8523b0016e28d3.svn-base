package com.menyi.msgcenter.msgif;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @preserve all
 */

public abstract class MsgHeader implements IfCommand, IfResult {

    public byte result;

    public static byte OBJ_INIT=0;
    public static byte OBJ_ADD=1;
    public static byte OBJ_DELETE=2;
    public static byte OBJ_UPDATE=3;
    
	public int total_Length;

	public int command_Id;

	public int sequence_Id;

	private static int curSeq = 1;

	private static Object oLock = new Object();

	public void setSequenceID() {
		if (sequence_Id == 0) {
			synchronized (oLock) {
				sequence_Id = curSeq;
				curSeq++;
				if (curSeq == 0x7fffffff)
					curSeq = 1;
			}
		}
	}

	public int changeSequenceID(){
		return this.sequence_Id++;
	}
	
	public static void initSequenceID(int num) {
		curSeq = num;
	}

	public abstract byte[] encode();

	public abstract void decode(byte[] b);

	public byte[] encodeHead() {
		setSequenceID();

		byte[] b = new byte[12];
		intToBytes(total_Length, b, 0);
		intToBytes(command_Id, b, 4);
		intToBytes(sequence_Id, b, 8);
		return b;
	}

	public void decodeHead(byte[] b) {
		total_Length = bytesToInt(b, 0);
		command_Id = bytesToInt(b, 4);
		sequence_Id = bytesToInt(b, 8);
	}

	/**
	 * 求字符串转换为byte数组后的数组长度
	 *
	 * @param str
	 *            String
	 * @return int
	 */
	public static int bytesLength(String str) {
		try {
			return str.getBytes("UTF-8").length;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public static String byte2String(byte buf[], int offset, int len) {
		byte[] b = new byte[len];
		System.arraycopy(buf, offset, b, 0, len);
		try {
			return new String(b, "UTF-8").trim();
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return "";
		}
	}

	public static void stringToByte(String str, byte buf[], int offset) {
		if (str != null) {
			byte[] b = null;
			try {
				b = str.getBytes("UTF-8");
				System.arraycopy(b, 0, buf, offset, b.length);
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static int getCommand(byte[] b) {
		return bytesToInt(b, 4);
	}

	public static int getLength(byte[] b) {
		return bytesToInt(b, 0);
	}
	
	public static Date StringToDate(String s){	
		Date time=new Date();		
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);		
		try{			
			time=sd.parse(s);		
		}catch (ParseException e) { 		
			time=null;
		}		
		return time;
	}
	
	public static void shortToBytes(short i, byte[] buf, int offset) {
		buf[offset + 0] = (byte) (0xff & i);
		buf[offset + 1] = (byte) ((0xff00 & i) >> 8);
	}

	public static short bytesToShort(byte abyte0[], int offset) {
		return (short) ((short) ((0xff & abyte0[offset + 1]) << 8) | (short) (0xff & abyte0[offset + 0]));
	}

	public static void intToBytes(int i, byte[] buf, int offset) {
		buf[offset + 0] = (byte) (0xff & i);
		buf[offset + 1] = (byte) ((0xff00 & i) >> 8);
		buf[offset + 2] = (byte) ((0xff0000 & i) >> 16);
		buf[offset + 3] = (byte) ((0xff000000 & i) >> 24);
	}

	public static short intToShort(int i,byte buf[], int offset){
		buf[offset + 0] = (byte) (0xff & i);
		buf[offset + 1] = (byte) ((0xff00 & i) >> 8);
		buf[offset + 2] = (byte) ((0xff0000 & i) >> 16);
		buf[offset + 3] = (byte) ((0xff000000 & i) >> 24);
		return (short) ((short) ((0xff & buf[offset + 1]) << 8) | (short) (0xff & buf[offset + 0]));
	}
	
	public static int bytesToInt(byte abyte0[], int offset) {
		return (0xff & abyte0[offset + 3]) << 24
				| (0xff & abyte0[offset + 2]) << 16
				| (0xff & abyte0[offset + 1]) << 8 
				| 0xff & abyte0[offset + 0];
	}

	public static void longToByte(long i, byte[] b, int offset) {
		b[offset + 0] = (byte) (i & 0x00000000000000ff);
		b[offset + 1] = (byte) ((i >> 8) & 0x00000000000000ff);
		b[offset + 2] = (byte) ((i >> 16) & 0x00000000000000ff);
		b[offset + 3] = (byte) ((i >> 24) & 0x00000000000000ff);
		b[offset + 4] = (byte) ((i >> 32) & 0x00000000000000ff);
		b[offset + 5] = (byte) ((i >> 40) & 0x00000000000000ff);
		b[offset + 6] = (byte) ((i >> 48) & 0x00000000000000ff);
		b[offset + 7] = (byte) ((i >> 56) & 0x00000000000000ff);
	}

	public static long byteToLong(byte[] bs, int offset) {
		long i1 = (long) (bs[offset + 0] & 0x00ff);
		long i2 = (long) (bs[offset + 1] & 0x00ff) << 8;
		long i3 = (long) (bs[offset + 2] & 0x00ff) << 16;
		long i4 = (long) (bs[offset + 3] & 0x00ff) << 24;
		long i5 = (long) (bs[offset + 4] & 0x00ff) << 32;
		long i6 = (long) (bs[offset + 5] & 0x00ff) << 40;
		long i7 = (long) (bs[offset + 6] & 0x00ff) << 48;
		long i8 = (long) (bs[offset + 7] & 0x00ff) << 56;

		return i1 | i2 | i3 | i4 | i5 | i6 | i7 | i8;
	}

	public static void bytesCopy(byte src[], byte des[], int srcPos, long length, int desPos) {
		int i1 = 0;
		for (int l = srcPos; l < src.length && l < srcPos+length; l++) {
			des[desPos + i1] = src[l];
			i1++;
		}
	}

	/**
	 * 解码对象
	 *
	 * @param b
	 *            byte[]
	 * @return MsgHeader
	 */
	public static MsgHeader decodeMsg(byte[] b) {
		MsgHeader msg = null;
		int command_Id = bytesToInt(b, 4);
		switch (command_Id) {
		case IfCommand.HANDSHAKE:
			msg = new HandShakeReq();
			break;
		case IfCommand.HANDSHAKE_RSP:
			msg = new HandShakeRsp();
			break;
		}
		if (msg != null)
			msg.decode(b);
		return msg;
	}

	/**
	 * 将byte数组转为16进制
	 * @param b
	 *            byte[]
	 * @return String
	 */
	public static String bytes2hex(byte[] b) {
		String s = "";
		if (b != null) {
			for (int i = 0; i < b.length; i++) {
				s = s + " " + byteHEX(b[i]);
			}
		}
		return s;
	}

	public static String byteHEX(byte byte0) { // 将byte转为16进制


		char ac[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char ac1[] = new char[2];
		ac1[0] = ac[byte0 >>> 4 & 0xf];
		ac1[1] = ac[byte0 & 0xf];
		String s = new String(ac1);
		return s;
	}
	
	public static void main(String[] args){
		int  i= 245;
		byte[] bs = new byte[4];
		MsgHeader.intToBytes(i, bs, 0);
		System.out.println("");
	}

}
