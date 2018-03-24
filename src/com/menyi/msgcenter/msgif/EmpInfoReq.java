package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class EmpInfoReq  extends MsgHeader  {

	public EmployeeItem eItem=new EmployeeItem();
	
	
	public EmpInfoReq() {
		this.command_Id = IfCommand.EmpInfo;
	}

	public byte[] encode() {
		byte[] b =eItem.encode();
		return b;
	}

	
	public void decode(byte b[]) {
		decodeHead(b);
		int pos = 12;
		int itemLength = this.total_Length-12;
		byte[] temp = new byte[itemLength];
		System.arraycopy(b, pos, temp, 0, itemLength);
		eItem.decode(temp);
	}
	

	public String toString() {
		String msg = "EmpInfoReq: ";
	
		return msg;

	}
}
