package com.myusbkey;

public class jyutianex {

    public static native int GetVersionEx(String InPath);

    public static native int GetIDEx(String InPath);

    public static native int get_LastError();

    public static native String FindPort(int start);

    public static native String FindPort_3(String filename, int pos);

    public static native String FindPort_4(String FileName, int pos,
                                           String HKey, String LKey);

    public static native int Cal_2(String FileName, short start, short len,
                                   byte a, byte b, byte c, byte d,
                                   String InPath, int cycle);

    public static native byte YReadEx(int Address, String InPath);

    public static native String YReadString(int Address, int len, String InPath);

    public static native int YWriteEx(byte inData, int Address, String InPath);

    public static native int YWriteString(String InString, int Address,
                                          String InPath);

    public static native byte get_A();

    public static native byte get_B();

    public static native byte get_C();

    public static native byte get_D();

    public static native int E_Dec_6(String HKey, String LKey, String InPath);

    public static native int CalculateEx_2(byte a, byte b, byte c, byte d,
                                           String InPath, int cycle);

    public static native void SetBufSize(int size);

    public static native void SetBuf(int pos, byte data);

    static {
        System.loadLibrary("AIO");
    }

}
