package com.menyi.msgcenter.msgif;

/**
 * 
 * @preserve all
 */
public interface IfCommand 
{
	public static int HANDSHAKE = 0x00000001;
	public static int HANDSHAKE_RSP = 0x80000001;
	public static int HEART = 0x00000002;
	public static int HEART_RSP = 0x80000002;
	public static int FILE = 0x00000003;
	public static int FILE_RSP = 0x80000003;
	public static int MSG = 0x00000004;
	public static int MSG_RSP = 0x80000004;
	public static int GROUP = 0x00000005;
	public static int GROUP_RSP = 0x80000005;
	public static int FRIEND = 0x00000006;
	public static int FRIEND_RSP = 0x80000006;
	public static int NOTE = 0x00000007;
	public static int NOTE_RSP = 0x80000007;
	public static int STATUS = 0x00000008;
	public static int STATUS_RSP = 0x80000008;
	public static int FILECANCEL = 0x00000009;
	public static int FILECANCEL_RSP = 0x80000009;
	public static int EmpInfo = 0x0000000A;
	public static int EmpInfo_RSP = 0x8000000A;
	public static int DeptInfo = 0x0000000B;
	public static int DeptInfo_RSP = 0x8000000B;
	public static int FILESHAKE = 0x0000000C;
	public static int FILESHAKE_RSP = 0x8000000C;
	public static int FORCEOFFLINE = 0x0000000D;
	public static int FORCEOFFLINE_RSP = 0x8000000D;
	public static int CANCELMSG = 0x0000000E;
	public static int CANCELMSG_RSP = 0x8000000E;
	public static int SHAKEWINDOW = 0x0000000F;
	public static int SHAKEWINDOW_RSP = 0x8000000F;
}
