package com.menyi.web.util;



/**
 * 加密规则：
 * 一个逻辑位=8个实际位.
 * 0:系统位，用于寻址及验证加密狗是否插入。
 * 1-9:模块位。共9个用于表示不同模块功能
 * 10-19:多语言。共10个用于表示多语言的数量。
 *
 * 20-28：共9个表示用户数的个位数
 * 29-37：共9个表示用户数的十位数
 * 38-46：共9个位表示用户数的百位数
 * 47-50：共4个位表示用户数的千位1,2,3,4
 * 51   : 用户自定义
 * 52   : 分支机构
 * 53   : 多币种
 * 54   : 生产
 * 55-57:共3个位40个字节预留
 * 58-62: 共5个位40字节，保存最多20个汉字的中方公司名称
 * 63:存储注册时间
 */
import java.util.ArrayList;
//import com.myusbkey.jyutianex;

public class SecurityLock {
    public static final int SALE_MODULE = 1; //进销存基本模块
    public static final int OA_MODULE = 2; //OA模块
    public static final int CRM_MODULE = 3; //CRM模块
    public static final int HUMAN_MODULE = 4; //人力资源模块

    public static final int FUNCTION_INIT = 20;
    public static final int FUNCTION_NEXT = 50;

    public static final int M0 = 1003; //公共模块总数
    public static final int M1 = 251; //进销存模块总数
    public static final int M2 = 105; //OA模块总数
    public static final int M3 = 99; //CRM模块总数
    public static final int M4 = 99; //人力资源模块总数


    //========================================================

    private static String keyPath = null; //加密狗路径
    private static Object oLock = new Object();

    public static int readKeyId() {
        return 0;
    }

    public static int readKeyVesion() {
        return 1;
    }


    /**
     * 读取储存在指定加密锁中的授权号
     * @param pos int
     * @return String[]
     */
    public static String[] F_ReadKey(int pos) {
        return new String[] {};
    }

    private static byte uniteBytes(byte src0, byte src1) {
        return 0;
    }


    private static byte[] HexString2Bytes(String src) {
        byte[] ret = new byte[4];
        return ret;
    }

    /**
     * 将授权号写入到加密锁的储存器中
     * @param HKey String
     * @param LKey String
     * @param pos int
     * @return boolean
     */
    public static boolean F_WriteKey(String HKey, String LKey,
                                     int pos) {
        return true;

    }

    /**
     * 写入注册时间
     * @param date String
     * @return boolean
     */
    public static boolean writeRegiste(String date,String comp) {
        if (date == null || date.trim().length() == 0 ) {
            return false;
        }
        return true;
    }
    /**
     * 读注册时间
     * @return String
     */
    public static String readRegiste(){

        return "20090101:科荣开发环境";//
    }

    /**
     * 输出函数Ini:
     * 目的：初步检查是否存在用户锁，如果有存在，提示用户插入用户锁。在程序初始化时，一定要使用这段代码
     * @param decArray byte[]
     * @return boolean
     */
    public static boolean init() { 
        return true;
    }

    /**
     * 执行运算表达式
     * @param decArray byte[]
     * @param e_Hkey String
     * @param e_Lkey String
     * @return byte[]
     */
    public static byte[] calculate(byte[] decArray, String e_Hkey,
                                   String e_Lkey, byte a, byte b, byte c,
                                   byte d) {
            return new byte[] {};
    }

    /**
     * 模块注册
     * 验证
     * 写
     * @param type int
     * @param hkey String
     * @param lkey String
     * @return boolean
     */
    public static boolean writeModule(int type, String hkey, String lkey) {
        return true;
    }




    /**
     * 查询系统中的模块
     * @return int[]
     */
    public static ArrayList getModule() {
        ArrayList list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");        
        return list;
    }

    /**
     * 读自定义
     * @return boolean
     */
    public static boolean getUserDefine() {
        return true;
    }
    /**
     * 写自定义
     * @param hkey String
     * @param lkey String
     * @return boolean
     */
    public static boolean writeUserDefine(String hkey,
                                          String lkey) {
        return true;
    }
    /**
     * 读分支机构
     * @return boolean
     */
    public static boolean getFenZhi() {
        return true;
    }
    /**
     * 写分支机构
     * @param hkey String
     * @param lkey String
     * @return boolean
     */
    public static boolean writeFenZhi(String hkey,
                                      String lkey) {
        return true;
    }
    /**
     * 读多币种
     * @return boolean
     */
    public static boolean getMoreCurrency() {
        return true;
    }
    /**
     * 写多币种
     * @param hkey String
     * @param lkey String
     * @return boolean
     */
    public static boolean writeMoreCurrency(String hkey,
                                            String lkey) {
        return true;
    }
    /**
     * 读生产
     * @return boolean
     */
    public static boolean getProduct() {
        return true;
    }
    /**
     * 写生产
     * @param hkey String
     * @param lkey String
     * @return boolean
     */
    public static boolean writeProduct(String hkey,
                                       String lkey) {
        return true;
    }


    /**
     * 根据指定的用户数计算出用户需输入的key的个数，一个授权码要两个keys
     * @param userNum int
     * @return int
     */
    public static int getKeyNum(int userNum) {
        String str = userNum + "";
        str = str.replaceAll("0", "");
        return str.length();
    }

    /**
     * 把指定的用户数，写入加密狗中
     * 1、验证数据数据的正确性
     * 2、写空所有用户数据区。
     * 3、重写数据
     * @param num int
     */
    public static boolean writeUserNum(int num, String[] keys) {
        return true;
    }

    /**
     * 读取系统用户数
     * @return int
     */
    public static int getUserNum() {

        return 10000 ;
    }

    /**
     * 把指定的语言数，写入加密狗中
     * 1、验证数据数据的正确性
     * 2、写空所有用户数据区。
     * 3、重写数据
     * 10-19:多语言。共10个用于表示多语言的数量。
     * @param num int
     */
    public static boolean writeLanguageNum(int num, String[] keys) {
        return true;
    }

    /**
     * 读取语言数
     * 10-19:多语言。共10个用于表示多语言的数量。
     * @return int
     */
    public static int getLanguageNum() {
        return 3;
    }
    /**
     * 取加密狗的版本
     * @return
     */
    public static int getVersion(){
    	int version = 0;
    	
    	return version;
    }
    public static byte[] getData(){    	
    	byte[] bs = new byte[128];
    	return bs;
    }
    public static boolean writeDate(byte[] bs){
        
        return true;
   }
}
