package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class MsgReq extends MsgHeader {
	
	public static byte TEXT = 1;

	public static byte PIC = 2;
	
	public static byte AUTOREPLY=3;
	
	public static byte FILE=4;
	
	public static byte SYS_NOTE = 3;	//PWY

	public static byte OBJ_EMP = 1;

	public static byte OBJ_DEPT = 2;

	public static byte OBJ_GROUP = 3;

	public byte msgType;

	public byte toObjType;

	public String fromUserId;//char(50)

	public String toObj; //char(50)
	public String sendTime;
	//1：类型为文本时，为数据库未读信息ID
	//2：类型为图片时，为图片名称
	public String msgId;  //
	public int dataLength;
	
	public String dataStr;

	
	/**
	 * 这个字段要特殊处理,因为可能传输的是文件，文件可能是很大的，
	 * 如果把所有数据全部加载完成后，再处理，则系统会内存爆掉，因此，这里应该在读取消息时边读边处理
	 */
	public byte[] dataFile; 

	/**
	 * @roseuid 509C9C6301DC
	 */
	public MsgReq() {
		this.command_Id = IfCommand.MSG;
	}

	//解包
	public void decode(byte[] b) {

		decodeHead(b);
		int pos = 12;
		msgType = b[pos];
        pos += 1;
        toObjType = b[pos];
        pos += 1;
		fromUserId = byte2String(b, pos, 50);
		pos += 50;
		toObj = byte2String(b, pos, 50);
		pos += 50;
		sendTime=byte2String(b, pos, 19);
		pos +=19;
		msgId=byte2String(b, pos, 50);
		pos +=50;
		dataLength = bytesToInt(b, pos);
		pos += 4;
		
		if(TEXT == msgType || SYS_NOTE == msgType){
			dataStr = MsgHeader.byte2String(b, pos, dataLength);
		}else{
			dataFile = new byte[dataLength];
			System.arraycopy(b, pos, dataFile, 0, dataLength);
		}
		
		pos += dataLength;
		
		
	}

	//打包
	public byte[] encode() {
		if(TEXT == msgType || SYS_NOTE == msgType){
			dataLength = MsgHeader.bytesLength(dataStr);
		}else{
			dataLength = dataFile.length;
		}
		this.total_Length = 12 + 1+1 + 50 + 50+4+dataLength+50+19;
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;
		b[pos]=msgType;
        pos += 1;
        b[pos]=toObjType;
        pos += 1;
        stringToByte(fromUserId, b, pos);
		pos += 50;
		stringToByte(toObj, b, pos);
		pos += 50;
		stringToByte(sendTime, b, pos);
		pos += 19;
		stringToByte(msgId, b, pos);
		pos += 50;
		intToBytes(dataLength,b,pos);
		pos += 4;
		if(TEXT == msgType || SYS_NOTE == msgType){
			MsgHeader.stringToByte(dataStr, b, pos);
		}else{
			System.arraycopy(dataFile, 0, b, pos, dataLength);
		}
		pos += dataLength;
		return b;
	}

	public String toString() {
		String msg = "MsgReq: ";
		msg += "total_Length = " + this.total_Length + "; ";
		msg += "msgType = " + msgType + "; ";
		msg += "toObjType = " + toObjType + "; ";
		msg += "fromUserId = " + fromUserId + "; ";
		msg += "toObj = " + toObj + "; ";
		msg += "msgId = " + msgId + "; ";
		msg += "sendTime = " + sendTime + "; ";
		msg += "dataLength = " + dataLength + "; ";
		if(msgType== TEXT){
			try{
			msg += "-----data = " + (dataStr.length()>100?dataStr.substring(0,100)+"...":dataStr) + "; ";
			}catch(Exception e){}
		}
		if(msgType== PIC){
			try{
			msg += "-----dataFile  ; ";
			}catch(Exception e){}
		}
		return msg;

	}
}
