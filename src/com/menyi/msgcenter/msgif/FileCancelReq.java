package com.menyi.msgcenter.msgif;

/**
 * 
 * @preserve all
 */
public class FileCancelReq extends MsgHeader {


	public String fromUserId; //char(50)
	
	public String toUserId; //char(50)
	
	public int fileMark; //文件标识

	/**
	 * @roseuid 509C9C6300D5
	 */
	public FileCancelReq() {
		this.command_Id = IfCommand.FILECANCEL;
	}
	public void decode(byte[] b) {

		decodeHead(b);
		int pos = 12;
		fromUserId = byte2String(b, pos, 50);
		pos += 50;
		toUserId = byte2String(b, pos, 50);
		pos += 50;
		fileMark = MsgHeader.bytesToInt(b, pos);
		pos += 4;
	}
	public byte[] encode() {

		this.total_Length = 12 + 50+50+4;
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;		
		stringToByte(fromUserId, b, pos);
		pos += 50;
		stringToByte(toUserId, b, pos);
		pos += 50;
		MsgHeader.intToBytes(fileMark, b, pos);
		pos += 4;
		return b;
	}

	public String toString() {
		String msg = "FileCancelReq: ";
		msg += "fromUserId = " + fromUserId + "; ";
		msg += "toUserId = " + toUserId + "; ";
		msg += "toUserId = " + toUserId + "; ";
		return msg;

	}
}
