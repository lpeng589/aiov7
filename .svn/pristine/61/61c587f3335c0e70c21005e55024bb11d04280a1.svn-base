package com.menyi.msgcenter.msgif;

/**
 * 
 * @preserve all
 */
public class FileRsp extends MsgHeader {
	public static Byte RESULT_AGREE = 0; // 接收者同意

	public static Byte RESULT_REJECT = 1; // 接收者拒绝

	public static Byte RESULT_ANSWER = 2; // 请求已接收，等待接收者应答

	public static Byte RESULT_REJECT_SIZE = 3; // 服务器拒绝，文件大小限制

	public static Byte RESULT_REJECT_TRANS = 4; // 服务器拒绝，不允许传输

//	public static Byte TRAN_LOCAL = 1; // 直连
//
//	public static Byte TRAN_SERVER = 2; // 服务器中转
	
	public static Byte TRAN_ONLINE = 3; // 在线传输

	public static Byte TRAN_OFFLINE = 4; // 离线传输

	public String fromUserId; // char(50) 发送者ID

	public String toUserId; // char(50) 接收者Id

	public int fileMark; // 文件标识

	public byte type;

	public String sessionKey; // char(50) 接收者Id

	public String ip; // CHAR(25)

	/**
	 * @roseuid 509C9C63029F
	 */
	public FileRsp() {
		this.command_Id = IfCommand.FILE_RSP;
	}

	public void decode(byte[] b) {

		decodeHead(b);
		int pos = 12;
		fromUserId = byte2String(b, pos, 50);
		pos += 50;
		toUserId = byte2String(b, pos, 50);
		pos += 50;
		result = b[pos];
		pos += 1;
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

		this.total_Length = 12 + 50 + 50 + 4 + 1 + 1 + 50 + 25;
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;

		stringToByte(fromUserId, b, pos);
		pos += 50;
		stringToByte(toUserId, b, pos);
		pos += 50;
		b[pos] = result;
		pos += 1;
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

		String msg = "FileRsp: ";
		msg += "result = " + result + "";
		msg += "type = " + type + "";
		return msg;

	}
}
