package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class NoteItem {
	public int itemLength;
	public String adviceId; //char(50)
	public String advice;

	public byte[] encode() {
		itemLength = 4 + 50+200;
		byte[] b = new byte[itemLength];
		int pos = 0;
		MsgHeader.intToBytes(itemLength, b, pos);
		pos += 4;
		MsgHeader.stringToByte(adviceId, b, pos);
		pos += 50;
		MsgHeader.stringToByte(advice, b, pos);
		pos += 200;
	
		return b;
	}

	public void decode(byte b[]) {
		int pos = 0;
		itemLength = MsgHeader.bytesToInt(b, pos);
		pos += 4;
		adviceId = MsgHeader.byte2String(b, pos, 50);
		pos += 50;

		advice = MsgHeader.byte2String(b, pos, 100);
		pos += 200;
	}

	public String toString() {

		String msg = "NoteItem";
		msg += "adviceId = " + adviceId + "; ";
		msg += "advice = " + advice + "; ";
		return msg;
	}
}
