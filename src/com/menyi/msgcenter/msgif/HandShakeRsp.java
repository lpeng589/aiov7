package com.menyi.msgcenter.msgif;

import java.util.ArrayList;
/**
 * 
 * @preserve all
 */
public class HandShakeRsp extends MsgHeader {
	
	public String deptLastTime;//char(19)

	public String empLastTime;//char(19)

	public String friendLastTime;//char(19)

	public String groupLastTime;//char(19)
	
	public ArrayList<DepartmentItem> deptList = new ArrayList<DepartmentItem>();

	public ArrayList<EmployeeItem> empList = new ArrayList<EmployeeItem>();

	public  ArrayList<FriendItem> friendList = new ArrayList<FriendItem>();

	public ArrayList<GroupItem> groupList = new ArrayList<GroupItem>();

	public byte showShortName;	//  «∑Òœ‘ æºÚ≥∆
	/**
	 * @roseuid 509C9C63022F
	 */
	public HandShakeRsp() {
		this.command_Id = IfCommand.HANDSHAKE_RSP;
	}

	public void decode(byte[] b) {

		decodeHead(b);
		int pos = 12;
		deptLastTime = byte2String(b, pos, 19);
		pos += 19;
		empLastTime = byte2String(b, pos, 19);
		pos += 19;
		friendLastTime = byte2String(b, pos, 19);
		pos += 19;
		groupLastTime = byte2String(b, pos, 19);
		pos += 19;
		
		short lsize = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		for (int i = 0; i < lsize; i++) {
			int itemLength = MsgHeader.bytesToInt(b, pos);
			DepartmentItem item = new DepartmentItem();
			byte[] temp = new byte[itemLength];
			System.arraycopy(b, pos, temp, 0, itemLength);
			item.decode(temp);
			deptList.add(item);
			pos += itemLength;
		}
		lsize = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		for (int i = 0; i < lsize; i++) {
			int itemLength = MsgHeader.bytesToInt(b, pos);
			EmployeeItem item = new EmployeeItem();
			byte[] temp = new byte[itemLength];
			System.arraycopy(b, pos, temp, 0, itemLength);
			item.decode(temp);
			empList.add(item);
			pos += itemLength;
		}
		lsize = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		for (int i = 0; i < lsize; i++) {
			int itemLength = MsgHeader.bytesToInt(b, pos);
			FriendItem item = new FriendItem();
			byte[] temp = new byte[itemLength];
			System.arraycopy(b, pos, temp, 0, itemLength);
			item.decode(temp);
			friendList.add(item);
			pos += itemLength;
		}
		lsize = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		for (int i = 0; i < lsize; i++) {
			int itemLength = MsgHeader.bytesToInt(b, pos);
			GroupItem item = new GroupItem();
			byte[] temp = new byte[itemLength];
			System.arraycopy(b, pos, temp, 0, itemLength);
			item.decode(temp);
			groupList.add(item);
			pos += itemLength;
		}
		showShortName = b[pos];
		pos++;
	}

	public byte[] encode() {

		this.total_Length = 512*1024;
		byte[] b = new byte[total_Length];
		
		int pos = 12;
		stringToByte(deptLastTime, b, pos);
		pos += 19;
		stringToByte(empLastTime, b, pos);
		pos += 19;
		stringToByte(friendLastTime, b, pos);
		pos += 19;
		stringToByte(groupLastTime, b, pos);
		pos += 19;
		short num=(deptList!=null?(short)deptList.size():0);	// PWY
		MsgHeader.shortToBytes(num, b, pos);
		pos += 2;
		for (int i = 0;deptList!=null&& i < deptList.size(); i++) {
			DepartmentItem item =  deptList.get(i);
			byte[] tb = item.encode();
			int itemLength = item.itemLength;
			System.arraycopy(tb, 0, b, pos, itemLength);
			pos += itemLength;
		}
		num=(empList!=null?(short)empList.size():0);	// PWY
		MsgHeader.shortToBytes(num, b, pos);
		pos += 2;
		for (int i = 0;empList!=null&& i < empList.size(); i++) {
			EmployeeItem item =  empList.get(i);
			byte[] tb = item.encode();
			int itemLength = item.itemLength;
			System.arraycopy(tb, 0, b, pos, itemLength);
			pos += itemLength;
		}
		num=(friendList!=null?(short)friendList.size():0);	// PWY
		MsgHeader.shortToBytes(num, b, pos);
		pos += 2;
		for (int i = 0;friendList!=null&& i < friendList.size(); i++) {
			FriendItem item =  friendList.get(i);
			byte[] tb = item.encode();
			int itemLength = 54;
			System.arraycopy(tb, 0, b, pos, itemLength);
			pos += itemLength;
		}
		num=(groupList!=null?(short)groupList.size():0);	// PWY
		MsgHeader.shortToBytes(num, b, pos);
		pos += 2;
		for (int i = 0;groupList!=null&& i < groupList.size(); i++) {
			GroupItem item =  groupList.get(i);
			byte[] tb = item.encode();
			int itemLength = item.itemLength;
			System.arraycopy(tb, 0, b, pos, itemLength);
			pos += itemLength;
		}
		b[pos++] = showShortName;
		byte bs [] = new byte[pos];
		System.arraycopy(b, 0, bs, 0, pos);
		this.total_Length=pos;
		System.arraycopy(encodeHead(), 0, bs, 0, 12);
		return bs;
	}

	public String toString() {

		String msg = "------------------HandShakeRsp: ";
		msg += "result = " + result + ";";
		msg += "deptLastTime = " + deptLastTime + "; ";
		msg += "empLastTime = " + empLastTime + "; ";
		msg += "friendLastTime = " + friendLastTime + "; ";
		msg += "groupLastTime = " + groupLastTime + "; ";
		msg += "deptList.size = " + deptList.size() + "; ";		
		for (int i = 0; i < deptList.size(); i++) {
			DepartmentItem item = deptList.get(i);
			msg += item.toString();
		}
		msg += "empList.size = " + empList.size() + "; ";		
		for (int i = 0; i < empList.size(); i++) {
			EmployeeItem item = empList.get(i);
			msg += item.toString();
		}
		msg += "friendList.size = " + friendList.size() + "; ";		
		for (int i = 0; i < friendList.size(); i++) {
			FriendItem item = friendList.get(i);
			msg += item.toString();
		}
		msg += "groupList.size = " + groupList.size() + "; ";		
		for (int i = 0; i < groupList.size(); i++) {
			GroupItem item = groupList.get(i);
			msg += item.toString();
		}
		return msg;

	}
}
