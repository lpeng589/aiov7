package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class ShakeWindowReq extends MsgHeader {

	public String fromUserId;//char(50)

	public String toObj; //char(50)
	
	public ShakeWindowReq() {
		this.command_Id = IfCommand.SHAKEWINDOW;
	}

	public void decode(byte[] b) {

		decodeHead(b);
		int pos = 12;
		fromUserId = byte2String(b, pos, 50);
		pos += 50;
		toObj = byte2String(b, pos, 50);
		pos += 50;
	}

	public byte[] encode() {

		this.total_Length = 12 + 50 +50;
		byte[] b = new byte[total_Length];

		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;
        stringToByte(fromUserId, b, pos);
		pos += 50;
		stringToByte(toObj, b, pos);
		pos += 50;

		return b;
	}

	public String toString() {

		String msg = "ShakeWindowReq: ";
		msg += "fromUserId = " + fromUserId + "; ";
		msg += "toObj = " + toObj + "; ";
		return msg;
	}
}
