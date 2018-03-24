package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class FriendReq extends MsgHeader {
	public static byte ADD = 2;

	public static byte DELETE = 1;

	public byte type;

	public String userId; //char(50)

	/**
	 * @roseuid 509C9C6300D5
	 */
	public FriendReq() {
		this.command_Id = IfCommand.FRIEND;
	}
	public void decode(byte[] b) {

		decodeHead(b);
		int pos = 12;
		type = b[pos];
        pos += 1;
        userId = byte2String(b, pos, 50);
		pos += 50;
	}
	public byte[] encode() {

		this.total_Length = 12 + 1+ 50 ;
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;
		b[pos]=type;
        pos += 1;
        stringToByte(userId, b, pos);
		pos += 50;

		return b;
	}

	public String toString() {

		String msg = "FriendReq: ";
		msg += "type = " + type + "; ";
		msg += "userId = " + userId + "; ";
		return msg;

	}
}
