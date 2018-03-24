package com.menyi.msgcenter.msgif;

import java.util.ArrayList;
/**
 * 
 * @preserve all
 */
public class NoteReq extends MsgHeader {
	public short num;
	public ArrayList<NoteItem> noteList = new ArrayList<NoteItem>();
	/**
	 * @roseuid 509C9C6301DC
	 */
	public NoteReq() {
		this.command_Id = IfCommand.NOTE;
	}

	public byte[] encode() {
		this.total_Length = 12+2+(noteList!=null?noteList.size()*254:0);
		num=(noteList!=null?(short)noteList.size():0);	// PWY
		byte[] b = new byte[total_Length];
		System.arraycopy(encodeHead(), 0, b, 0, 12);
		int pos = 12;
		MsgHeader.shortToBytes(num, b, pos);
		pos += 2;
		for (int i = 0;noteList!=null && i < noteList.size(); i++) {
			NoteItem item =  noteList.get(i);
			byte[] tb = item.encode();
			int itemLength = 254;
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
			int itemLength = 254;
			NoteItem item = new NoteItem();
			byte[] temp = new byte[itemLength];
			System.arraycopy(b, pos, temp, 0, itemLength);
			item.decode(temp);
			noteList.add(item);
			pos += itemLength;
		}

	}

	public String toString() {

		String msg = "noteList: ";
		for(int i=0;i<noteList.size();i++){
			NoteItem  nItem=noteList.get(i);
			msg += "adviceId= " + nItem.adviceId + "; ";
			msg += "advice= " + nItem.advice + "; ";
		}
		return msg;

	}
}
