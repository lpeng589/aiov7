package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class EmployeeItem   {
	
    public static byte CS_LOGIN = 1;
    public static byte BS_LOGIN = 2;
    public static byte MOBILE_LOGIN=3;
	
    public static byte ONLINE = 1;
    public static byte OFFLINE = 2;
    public static byte LEAVE=3;
    
	public int itemLength;
	public String userId; // char(50)
	public byte operateType; //char(1)
	public short nameLength;  //char(2)
	public String name;
	public String departmentCode; // char(50)
	public byte sex;   // char(1)
	public String telPhone; // char(100)
	public String mobile; // char(100)
	public String email; // char(50)
	public short titleLength; //char(2)
	public String titleID;  //char(50)
	public String birth; // char(19)
	public short imageNum; // char(2)
	public short signLength;  // char(2)
	public String signContext;  
	public byte loginStatus;
	public byte loginType;
	public String dataTime; // char(19)
	public String groupTime;
	public String friendTime;
	public String createTime;
	
	public String sEmpNumber;  // 职员编号，用于排序 char(50)

	public short shortNameLength;  //char(2)
	public String shortName;	// 简称

	public EmployeeItem() {
		this.itemLength = 0;
		this.userId = "";
		this.operateType = 0;
		this.nameLength = 0;
		this.name = "";
		this.departmentCode = "";
		this.sex = 0;
		this.telPhone = "";
		this.mobile = "";
		this.email = "";
		this.titleLength = 0;
		this.titleID = "";
		this.birth = "";
		this.imageNum = 0;
		this.signLength = 0;
		this.signContext = "";
		this.loginStatus = OFFLINE;	//LWX2B
		this.loginType = 0;
		this.dataTime = "";
		this.groupTime = "";
		this.friendTime = "";
		this.createTime = "";
		this.sEmpNumber = "";
		
		this.shortNameLength = 0;
		this.shortName = "";
	}

	public byte[] encode() {
		
		nameLength = (short)MsgHeader.bytesLength(name);
		titleLength = (short)MsgHeader.bytesLength(titleID);
		signLength = (short)MsgHeader.bytesLength(signContext);
		shortNameLength = (short)MsgHeader.bytesLength(shortName);
		itemLength = 4 + 50+2+1+ nameLength+50+1+100+100+50+2+titleLength+19+2+2+signLength + 50 +19 + 2 + shortNameLength;
		byte[] b = new byte[itemLength];
		int pos = 0;
		MsgHeader.intToBytes(itemLength, b, pos);
		pos += 4;
		MsgHeader.stringToByte(userId, b, pos);
		pos += 50;
		b[pos] = operateType;
		pos += 1;
		MsgHeader.shortToBytes(nameLength, b, pos);
		pos += 2;
		MsgHeader.stringToByte(name, b, pos);
		pos += nameLength;
		MsgHeader.stringToByte(departmentCode, b, pos);
		pos += 50;
		b[pos] = sex;
		pos += 1;
		MsgHeader.stringToByte(telPhone, b, pos);
		pos += 100;
		MsgHeader.stringToByte(mobile, b, pos);
		pos += 100;
		MsgHeader.stringToByte(email, b, pos);
		pos += 50;
		MsgHeader.shortToBytes(titleLength, b, pos);
		pos += 2;
		MsgHeader.stringToByte(titleID, b, pos);
		pos += titleLength;
		MsgHeader.stringToByte(birth, b, pos);
		pos += 19;
		MsgHeader.shortToBytes(imageNum, b, pos);
		pos += 2;
		MsgHeader.shortToBytes(signLength, b, pos);
		pos += 2;
		MsgHeader.stringToByte(signContext, b, pos);
		pos += signLength;

		MsgHeader.stringToByte(sEmpNumber, b, pos);
		pos += 50;
		
		MsgHeader.stringToByte(dataTime, b, pos);
		pos += 19;

		MsgHeader.shortToBytes(shortNameLength, b, pos);
		pos += 2;
		MsgHeader.stringToByte(shortName, b, pos);
		pos += shortNameLength;
		return b;
	}

	public void decode(byte b[]) {
		int pos = 0;
		itemLength = MsgHeader.bytesToInt(b, pos);
		pos += 4;
		userId = MsgHeader.byte2String(b, pos, 50);
		pos += 50;
		operateType = b[pos];
		pos++;
		nameLength = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		name = MsgHeader.byte2String(b, pos, nameLength);
		pos += nameLength;
		departmentCode = MsgHeader.byte2String(b, pos, 50);
		pos += 50;
		sex = b[pos];
		pos++;
		telPhone = MsgHeader.byte2String(b, pos, 100);
		pos += 100;
		mobile = MsgHeader.byte2String(b, pos, 100);
		pos += 100;
		email = MsgHeader.byte2String(b, pos, 50);
		pos += 50;
		titleLength = (short) MsgHeader.bytesToInt(b, pos);
		pos += 2;
		titleID = MsgHeader.byte2String(b, pos, titleLength);
		pos += titleLength;
		birth = MsgHeader.byte2String(b, pos, 19);
		pos += 19;
		imageNum = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		signLength = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		signContext = MsgHeader.byte2String(b, pos, signLength);
		pos += signLength;
		
		sEmpNumber = MsgHeader.byte2String(b, pos, 50);
		pos += 50;
		
		dataTime = MsgHeader.byte2String(b, pos, 19);
		pos += 19;

		shortNameLength = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		shortName = MsgHeader.byte2String(b, pos, shortNameLength);
		pos += shortNameLength;
	}
	

	public String toString() {
		String msg = "EmployeeItem: ";
		msg += "userId = " + userId + "; ";
		msg += "name = " + name + "; ";
		msg += "shortName = " + shortName + "; ";
		msg += "titleID = " + titleID + "; ";
		msg += "operateType = " + operateType + "; ";
		msg += "EmpNumber = " + sEmpNumber + "; ";
		return msg;

	}
}
