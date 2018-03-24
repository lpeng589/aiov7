package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class DeptInfoReq  extends MsgHeader  {

	public DepartmentItem dItem=new DepartmentItem();
	
	
	public DeptInfoReq() {
		this.command_Id = IfCommand.DeptInfo;
	}

	public byte[] encode() {
		byte[] b =dItem.encode();
		return b;
	}

	
	public void decode(byte b[]) {
		decodeHead(b);
		int pos = 12;
		int itemLength = this.total_Length-12;
		byte[] temp = new byte[itemLength];
		System.arraycopy(b, pos, temp, 0, itemLength);
		dItem.decode(temp);
	}
	

	public String toString() {
		String msg = "DeptInfoReq: ";
		msg += "deptName = " + dItem.name + "; ";
		return msg;

	}
}
