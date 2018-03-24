package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class DepartmentItem {
	public int itemLength;
	public String deptId;
	public String classCode; //char(50)   //部门编码
	public String deptCode;//char(50)  //部门编号
	public byte operateType; //char(1)
	public short nameLength;
	public String name;
	public short remarkLength;
	public String remark;
	public String dataTime;
	public String createTime;
	
	
	public DepartmentItem() {
		this.itemLength = 0;
		this.deptId = "";
		this.classCode = "";
		this.deptCode = "";
		this.operateType = 0;
		this.nameLength = 0;
		this.name = "";
		this.remarkLength = 0;
		this.remark = "";
		this.dataTime = "";
		this.createTime = "";
	}

	public byte[] encode() {
		nameLength = (short)MsgHeader.bytesLength(name);
		remarkLength = (short)MsgHeader.bytesLength(remark);
		itemLength = 4 + 50+50+50+1+ 2 + nameLength + 2 + remarkLength + 19 ;
		byte[] b = new byte[itemLength];
		int pos = 0;
		MsgHeader.intToBytes(itemLength, b, pos);
		pos += 4;
		MsgHeader.stringToByte(deptId, b, pos);
		pos += 50;
		MsgHeader.stringToByte(classCode, b, pos);
		pos += 50;
		MsgHeader.stringToByte(deptCode, b, pos);
		pos += 50;
		b[pos] = operateType;
		pos += 1;
		MsgHeader.shortToBytes(nameLength, b, pos);
		pos += 2;
		MsgHeader.stringToByte(name, b, pos);
		pos += nameLength;
		MsgHeader.shortToBytes(remarkLength, b, pos);
		pos += 2;
		MsgHeader.stringToByte(remark, b, pos);
		pos += remarkLength;
		MsgHeader.stringToByte(dataTime, b, pos);
		pos += 19;
		return b;
	}

	public void decode(byte b[]) {
		int pos = 0;
		itemLength = MsgHeader.bytesToInt(b, pos);
		pos += 4;
		deptId= MsgHeader.byte2String(b, pos, 50);
		pos += 50;
		classCode = MsgHeader.byte2String(b, pos, 50);
		pos += 50;
		deptCode = MsgHeader.byte2String(b, pos, 50);
		pos += 50;
		operateType = b[pos];
		pos++;
		nameLength = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		name = MsgHeader.byte2String(b, pos, nameLength);
		pos += nameLength;
		remarkLength = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		remark = MsgHeader.byte2String(b, pos, remarkLength);
		pos += remarkLength;
		dataTime = MsgHeader.byte2String(b, pos,19);
		pos += 19;
	}

	public String toString() {

		String msg = "DepartmentItem: ";
		msg += "classCode = " + classCode + "; ";
		msg += "deptCode = " + deptCode + "; ";
		msg += "name = " + name + "; ";
		msg += "remark = " + remark + "; ";
		msg += "dataTime = " + dataTime + "; ";
		msg += "operateType = " + operateType + "; ";
		return msg;

	}
}
