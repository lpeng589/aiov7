package com.menyi.msgcenter.msgif;

/**
 * 
 * @preserve all
 */
public class FileReq extends MsgHeader {
//	public static Byte TRAN_LOCAL = 1; // 直连
//
//	public static Byte TRAN_SERVER = 2; // 服务器中转
	
	public static Byte TRAN_ONLINE = 3; // 直连

	public static Byte TRAN_OFFLINE = 4; // 服务器中转

	public String fromUserId; // char(50)

	public String toUserId; // char(50)

	public short fileNameLength;

	public String fileName;

	public long fileSize;

	public byte type; // 传输方式

	public String sessionKey;// char(50)

	public int fileMark; // 请求序号

	public String ip; // char(25)

	/**
	 * @roseuid 509C9C630269
	 */
	public FileReq() {
		this.command_Id = IfCommand.FILE;
	}

	public void decode(byte[] b) {

		decodeHead(b);
		int pos = 12;
		fromUserId = byte2String(b, pos, 50);
		pos += 50;
		toUserId = byte2String(b, pos, 50);
		pos += 50;
		fileNameLength = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		fileName = byte2String(b, pos, fileNameLength);
		pos += fileNameLength;
		fileSize = byteToLong(b, pos);
		pos += 8;
		type = b[pos];
		pos += 1;
		sessionKey = byte2String(b, pos, 50);
		pos += 50;
		fileMark = MsgHeader.bytesToInt(b, pos);
		pos += 4;
		ip = byte2String(b, pos, 25);
		pos += 25;

	}

	public byte[] encode() {

		if (null == fileName) {
			fileName = "";
		}
		fileNameLength = (short) bytesLength(fileName);

		this.total_Length = 12 + 50 + 50 + 2 + fileNameLength + 8 + 1 + 50 + 4
				+ 25;
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;
		stringToByte(fromUserId, b, pos);
		pos += 50;
		stringToByte(toUserId, b, pos);
		pos += 50;
		shortToBytes(fileNameLength, b, pos);
		pos += 2;
		stringToByte(fileName, b, pos);
		pos += fileNameLength;
		longToByte(fileSize, b, pos);
		pos += 8;
		b[pos] = type;
		pos += 1;
		stringToByte(sessionKey, b, pos);
		pos += 50;
		MsgHeader.intToBytes(fileMark, b, pos);
		pos += 4;
		stringToByte(ip, b, pos);
		pos += 25;
		return b;
	}

	public String toString() {

		String msg = "FileReq: ";
		msg += "fileName = " + fileName + "; ";
		msg += "fileSize = " + fileSize + "; ";
		msg += "fromUserId = " + fromUserId + "; ";
		msg += "toUserId = " + toUserId + "; ";
		msg += "type = " + type + "; ";
		msg += "ip = " + ip + "; ";

		return msg;

	}
}
