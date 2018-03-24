package com.menyi.msgcenter.msgif;

/**
 * 
 * @preserve all
 */
public class CancelMsgReq extends MsgHeader {
	public static Byte TYPE_CHAT = 1; // 聊天消息

	public static Byte TYPE_SYS = 2; // 通知消息

	public byte nType; // 消息类型

	public String sMsgId; // 消息ID char(50)

	public CancelMsgReq() {
		this.command_Id = IfCommand.CANCELMSG;
	}

	public void decode(byte[] b) {

		decodeHead(b);

		int pos = 12;
		nType = b[pos];
		pos += 1;
		sMsgId = byte2String(b, pos, 50);
		pos += 50;
	}

	public byte[] encode() {

		this.total_Length = 12 + 1 + 50;
		byte[] b = new byte[total_Length];

		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;
		b[pos] = nType;
		pos += 1;
		stringToByte(sMsgId, b, pos);
		pos += 50;

		return b;
	}

	public String toString() {

		String msg = "CancelMsgReq: ";
		msg += "Type = " + nType + "; ";
		msg += "MsgId = " + sMsgId + "; ";
		return msg;
	}
}
