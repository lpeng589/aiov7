package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class HandShakeReq extends MsgHeader {
	public static byte SHAKE_REFRESH = 0;
    public static byte SHAKE_LOGIN = 1;


	public String sessionKey;//char(50)
	
	public String userId;//char(50)
	
	public byte shakeType;
	
	public String deptLastTime;//char(19)

	public String empLastTime;//char(19)

	public String friendLastTime;//char(19)

	public String groupLastTime;//char(19)

	
	public HandShakeReq() {
		this.command_Id = IfCommand.HANDSHAKE;
	}

	public void decode(byte[] b) {

		decodeHead(b);
		int pos = 12;
		sessionKey = byte2String(b, pos, 50);
		pos += 50;
		userId = byte2String(b, pos, 50);
		pos += 50;
		shakeType= b[pos];
		pos += 1;
		deptLastTime = byte2String(b, pos, 19);
		pos += 19;
		empLastTime = byte2String(b, pos, 19);
		pos += 19;
		friendLastTime = byte2String(b, pos, 19);
		pos += 19;
		groupLastTime = byte2String(b, pos, 19);
		pos += 19;
		
	}

	public byte[] encode() {

		this.total_Length = 12 + 50 +50 + 1 + 19*4;
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;		
        stringToByte(sessionKey, b, pos);
		pos += 50;
		stringToByte(userId, b, pos);
		pos += 50;
		b[pos]=shakeType ;
		pos++;
		stringToByte(deptLastTime, b, pos);
		pos += 19;
		stringToByte(empLastTime, b, pos);
		pos += 19;
		stringToByte(friendLastTime, b, pos);
		pos += 19;
		stringToByte(groupLastTime, b, pos);
		pos += 19;

		return b;
	}

	public String toString() {

		String msg = "HandShakeReq-----: ";
		msg += "sessionKey = " + sessionKey + "; ";
		msg += "userId = " + userId + "; ";
		msg += "shakeType = " + shakeType + "; ";
		msg += "deptLastTime = " + deptLastTime + "; ";
		msg += "empLastTime = " + empLastTime + "; ";
		msg += "friendLastTime = " + friendLastTime + "; ";
		msg += "groupLastTime = " + groupLastTime + "; ";

		return msg;

	}
}
