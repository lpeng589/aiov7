package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class EmpStatusItem {
	public int itemLength;
	public String userId;
	public byte loginStatus;
	public byte loginType;
	
	public byte[] encode() {
		itemLength = 4 + 50+1+1 ;
		byte[] b = new byte[itemLength];
		int pos = 0;
		MsgHeader.intToBytes(itemLength, b, pos);
		pos += 4;
		MsgHeader.stringToByte(userId, b, pos);
		pos += 50;
		b[pos] = loginStatus;
		pos += 1;
		b[pos] = loginType;
		pos += 1;
		return b ;
	}
	
	public void decode(byte b[]) {
		int pos = 0;
		itemLength = MsgHeader.bytesToInt(b, pos);
		pos += 4;
		userId = MsgHeader.byte2String(b, pos, 50);
		pos += 50;
		loginStatus = b[pos];
		pos++;
		loginType = b[pos];
		pos++;
	}
	
	public String toString() {
		String msg = "EmpStatusItem: ";
		msg += "userId = " + userId + "; ";
		msg += "loginStatus = " + loginStatus + "; ";
		msg += "loginType = " + loginType + "; ";
		return msg;
	}
	
}
