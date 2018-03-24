package com.menyi.msgcenter.msgif;

import java.util.ArrayList;
/**
 * 
 * @preserve all
 */
public class StatusReq extends MsgHeader {
	public short num;
	public ArrayList<EmpStatusItem> statusList = new ArrayList<EmpStatusItem>();
	/**
	 * @roseuid 509C9C6301DC
	 */
	public StatusReq() {
		this.command_Id = IfCommand.STATUS;
	}

	public byte[] encode() {
		this.total_Length = 12+2+(statusList!=null?statusList.size()*56:0);
		num=(statusList!=null?(short)statusList.size():0);	// PWY
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;
		MsgHeader.shortToBytes(num, b, pos);
		pos += 2;
		for (int i = 0;statusList!=null&& i < statusList.size(); i++) {
			EmpStatusItem item =  statusList.get(i);
			byte[] tb = item.encode();
			int itemLength = 56;
			System.arraycopy(tb, 0, b, pos, itemLength);
			pos += itemLength;
		}
		return b;
	}

	public void decode(byte b[]) {
		decodeHead(b);
		int pos = 12;
		num = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		for (int i = 0; i < num; i++) {
			int itemLength = 56;
			EmpStatusItem item = new EmpStatusItem();
			byte[] temp = new byte[itemLength];
			System.arraycopy(b, pos, temp, 0, itemLength);
			item.decode(temp);
			statusList.add(item);
			pos += itemLength;
		}
	}

	public String toString() {

		String msg = "StatusReq : ";
		for(int i=0;i<statusList.size();i++){
			EmpStatusItem fItem=statusList.get(i);
			msg += "userId= " + fItem.userId + "; ";
			msg += "loginStatus= " + fItem.loginStatus + "; ";
			msg += "loginType= " + fItem.loginType + "; ";
		}
		return msg;

	}
}
