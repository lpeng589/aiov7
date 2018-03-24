package com.menyi.msgcenter.msgif;
/**
 * 
 * @preserve all
 */
public class GroupReq  extends MsgHeader  {
	public GroupItem gItem=new GroupItem();
	
	
	public GroupReq() {
		this.command_Id = IfCommand.GROUP;
	}

	public byte[] encode() {
		byte[] tb = gItem.encode();
		int itemLength = gItem.itemLength;
		this.total_Length=12+itemLength;
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;	
		System.arraycopy(tb, 0, b, pos, itemLength);
		return b;
	}

	
	public void decode(byte b[]) {
		decodeHead(b);
		int pos = 12;
		int itemLength = this.total_Length-12;
		byte[] temp = new byte[itemLength];
		System.arraycopy(b, pos, temp, 0, itemLength);
		gItem.decode(temp);
	}
	

	public String toString() {
		String msg = "GroupReq: ";
		msg += "groupName = " + gItem.groupName + "; ";
		msg += "²Ù×÷ÀàÐÍ:opeateType = " + gItem.operateType+ "; ";
		return msg;
	}
}
