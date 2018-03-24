package com.menyi.msgcenter.msgif;

import java.util.ArrayList;
/**
 * 
 * @preserve all
 */
public class GroupItem {
	
	public int itemLength;
	public String groupId; //char(50)
	public byte operateType;//char(1)
	public String groupName; //char(50)
	public String createBy ;//char(50)
	public String createTime ;//char(19)
	public short remarkLength; //char(2)
	public String remark;
	public String dataTime;//char(19)
	public short userCount;
	public ArrayList<FriendItem> userList;	
	
	public GroupItem(){
		this.itemLength = 0;
		this.groupId = "";
		this.operateType = 0;
		this.groupName = "";
		this.createBy = "";
		this.createTime = "";
		this.remarkLength = 0;
		this.remark = "";
		this.dataTime = "";
		this.userCount = 0;
		this.userList = new ArrayList<FriendItem>();
	}
	public GroupItem(int itemLength, String groupId, byte operateType, String groupName, String createBy, String createTime, short remarkLength, String remark, String dataTime, short userCount, ArrayList<FriendItem> userList) {
		super();
		this.itemLength = itemLength;
		this.groupId = groupId;
		this.operateType = operateType;
		this.groupName = groupName;
		this.createBy = createBy;
		this.createTime = createTime;
		this.remarkLength = remarkLength;
		this.remark = remark;
		this.dataTime = dataTime;
		this.userCount = userCount;
		this.userList = userList;
	}

	public byte[] encode() {
		remarkLength = (short)MsgHeader.bytesLength(remark);
		userCount=(userList!=null?(short)userList.size():0);	// PWY
		itemLength = 4+1+50+50+50+19+2+remarkLength+19+2+(userList!=null?userList.size()*54:0);
		byte[] b = new byte[itemLength];
		int pos=0;
		MsgHeader.intToBytes(itemLength, b, pos);
		pos += 4;
		MsgHeader.stringToByte(groupId, b, pos);
		pos += 50;
		b[pos] = operateType;
		pos += 1;
		MsgHeader.stringToByte(groupName, b, pos);
		pos += 50;
		MsgHeader.stringToByte(createBy, b, pos);
		pos += 50;
		MsgHeader.stringToByte(createTime, b, pos);
		pos += 19;
		MsgHeader.shortToBytes(remarkLength, b, pos);
		pos += 2;
		MsgHeader.stringToByte(remark, b, pos);
		pos += remarkLength;
		
		MsgHeader.stringToByte(dataTime, b, pos);
		pos += 19;
		MsgHeader.shortToBytes(userCount, b, pos);
		pos += 2;
		
		for (int i = 0;userList!=null&& i < userList.size(); i++) {
			FriendItem item =  userList.get(i);
			byte[] tb = item.encode();
			int itemLength = 54;
			System.arraycopy(tb, 0, b, pos, itemLength);
			pos += itemLength;
		}
		return b;
	}

	public void decode(byte b[]) {
		int pos = 0;
		
		itemLength = MsgHeader.bytesToInt(b, pos);
		pos += 4;
		groupId = MsgHeader.byte2String(b, pos,50);
		pos += 50;
		operateType = b[pos];
		pos++;
		groupName = MsgHeader.byte2String(b, pos,50);
		pos += 50;
		createBy = MsgHeader.byte2String(b, pos,50);
		pos += 50;
		createTime = MsgHeader.byte2String(b, pos,19);
		pos += 19;
		remarkLength = MsgHeader.bytesToShort(b, pos);
		pos += 2;
		remark = MsgHeader.byte2String(b, pos, remarkLength);
		pos += remarkLength;
		
		dataTime = MsgHeader.byte2String(b, pos,19);
		pos += 19;
		userCount = MsgHeader.bytesToShort(b, pos);
		pos += 2;

		for (int i = 0; i < userCount; i++) {
			int itemLength = MsgHeader.bytesToInt(b, pos);
			FriendItem item = new FriendItem();
			byte[] temp = new byte[itemLength];
			System.arraycopy(b, pos, temp, 0, itemLength);
			item.decode(temp);
			//System.out.println("³¤¶È--:"+item.itemLength+"ºÃÓÑId--£º"+item.userId);
			userList.add(item);
			pos += itemLength;
		}
	}

	public String toString() {

		String msg = "GroupItem: ";
		msg += "groupId = " + groupId + "; ";
		msg += "groupName = " + groupName + "; ";
		msg += "dataTime = " + dataTime + "; ";
		msg += "createBy = " + createBy + "; ";
		msg += "createTime = " + createTime + "; ";
		msg += "userCount = " + userCount + "; ";
		for(int i=0;i<userList.size();i++){
			FriendItem fItem=userList.get(i);
			msg += "FriendId = " + fItem.userId + "; ";
		}
		msg += "operateType = " + operateType + "; ";
		return msg;

	}
}
