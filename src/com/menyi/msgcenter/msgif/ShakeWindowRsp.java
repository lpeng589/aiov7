package com.menyi.msgcenter.msgif;

/**
 * 
 * @preserve all
 */
public class ShakeWindowRsp extends MsgHeader {

	public ShakeWindowRsp() {
		this.command_Id = IfCommand.SHAKEWINDOW_RSP;
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

		String msg = "ShakeWindowRsp: ";
		return msg;

	}
}
