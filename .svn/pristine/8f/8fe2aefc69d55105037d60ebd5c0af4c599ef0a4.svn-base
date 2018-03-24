package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class MsgRsp extends MsgHeader {
	public String msgId;
//	public byte result;	// PWY

	/**
	 * @roseuid 509C9C63022F
	 */
	public MsgRsp() {
		this.command_Id = IfCommand.MSG_RSP;
	}

	public void decode(byte[] b) {

		decodeHead(b);
		int pos = 12;
		msgId = MsgHeader.byte2String(b, pos, 50);
		pos += 50;
		result = b[pos];
		pos += 1;
	}

	public byte[] encode() {

		this.total_Length = 13+50;
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;
		MsgHeader.stringToByte(msgId, b, pos);
		pos += 50;
		b[pos] = result;
		pos += 1;
		return b;
	}

	public String toString() {

		String msg = "MsgRsp: ";
		msg += "result = " + result + "";
		msg += "msgId = " + msgId + "";
		return msg;

	}
}
