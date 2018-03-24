package com.menyi.msgcenter.msgif;

/**
 * 
 * @preserve all
 */
public class FileShakeReq extends MsgHeader {
	public static byte RECEIVE = 2;

	public static byte SEND = 1;
	
	public static byte OFFLINE_SEND = 3;
	public static byte OFFLINE_RECV = 4;
	public String sendId; //char(50)	
	public String receiveId; //char(50)
	public String sessionKey;
	public byte direct;
	
	public short fileNameLength;
	public String fileName;
	
	/**
	 * @roseuid 509C9C6300D5
	 */
	public FileShakeReq() {
		this.command_Id = IfCommand.FILESHAKE;
	}
	public void decode(byte[] b) {

		decodeHead(b);
		int pos = 12;
        sendId = byte2String(b, pos, 50);
		pos += 50;
		receiveId = byte2String(b, pos, 50);
		pos += 50;
		sessionKey = byte2String(b, pos, 50);
		pos += 50;
		fileNameLength = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		fileName = byte2String(b, pos, fileNameLength);
		pos += fileNameLength;
		direct = b[pos];
		pos += 1;
	}
	public byte[] encode() {

		if (null == fileName) {
			fileName = "";
		}
		fileNameLength = (short) bytesLength(fileName);
		
		this.total_Length = 12 + 1+ 50 +50+ 50 + 2 + fileNameLength;
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;
        stringToByte(sendId, b, pos);
		pos += 50;
		stringToByte(receiveId, b, pos);
		pos += 50;
		stringToByte(sessionKey, b, pos);
		pos += 50;
		shortToBytes(fileNameLength, b, pos);
		pos += 2;
		stringToByte(fileName, b, pos);
		pos += fileNameLength;
		b[pos]=direct;
		pos += 1;


		return b;
	}

	public String toString() {
		String msg = "FileNoteReq: ";
		msg += "fromUserId = " + sendId + "; ";
		msg += "toUserId = " + receiveId + "; ";
		msg += "direct = " + direct + "; ";
		return msg;

	}
}
