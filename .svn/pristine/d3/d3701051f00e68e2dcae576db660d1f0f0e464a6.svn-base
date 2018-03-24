package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class FriendItem {
	public int itemLength;
	public String userId; //char(50)
	

	public byte[] encode() {
		itemLength =4+50;
		byte[] b = new byte[itemLength];
		int pos = 0;
		MsgHeader.intToBytes(itemLength, b, pos);
		pos += 4;
		MsgHeader.stringToByte(userId, b, pos);
		pos += 50;
		return b;
	}

	public void decode(byte b[]) {
		int pos = 0;
		itemLength = MsgHeader.bytesToInt(b, pos);
		pos += 4;
		userId = MsgHeader.byte2String(b, pos, 50);
		pos += 50;
	}

	public String toString() {
		String msg = "FriendItem: ";
		msg += "userId = " + userId + "; ";
		return msg;
	}
}
