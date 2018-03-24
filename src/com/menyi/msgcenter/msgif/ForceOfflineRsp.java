package com.menyi.msgcenter.msgif;

/**
 * 
 * @preserve all
 */
public class ForceOfflineRsp extends MsgHeader {

	public ForceOfflineRsp() {
		this.command_Id = IfCommand.FORCEOFFLINE_RSP;
	}

	public void decode(byte[] b) {

		decodeHead(b);
	}

	public byte[] encode() {

		this.total_Length = 12;
		byte[] b = new byte[total_Length];

		System.arraycopy(encodeHead(), 0, b, 0, 12);

		return b;
	}

	public String toString() {

		String msg = "ForceOfflineRsp: ";
		return msg;

	}
}
