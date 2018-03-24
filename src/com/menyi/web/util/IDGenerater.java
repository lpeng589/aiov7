package com.menyi.web.util;

import java.net.*;
import java.security.MessageDigest;
import java.util.Random;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: 周新宇</p>
 *
 * @author 周新宇
 * @version 1.0
 */
public class IDGenerater {
    private static String head;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
    private static int curNo = 0;
    private static Object oLock = new Object();

    private static java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();

    /**
     * id产生器
     * 根据主机IP地址和当前时间和5位随机数，生成MD5摘要。保证不同机器，不是时间启动，产生不同的头
     * 加上时间到毫秒。
     * 加上10位序列数。
     * @return String
     */
    public static String getId() {
        if(head == null){
            head = getHead();
            nf.setMinimumIntegerDigits(4);
            nf.setGroupingUsed(false);
        }

        int no = 0;
        synchronized(oLock){
            curNo ++;
            no = curNo;
            if(curNo>=9999){
                curNo = 0;
            }
        }



        return head+"_"+sdf.format(new Date())+nf.format(no);
    }

    private static String getHead(){
        String ip;
        try {
            ip = "localhost";
            try {
                java.net.InetAddress ipa = java.net.InetAddress.getLocalHost();
                ip = ipa.getHostAddress();
            } catch (Exception ex) {
            }

            Random rd = new Random();
            String seek = ip + System.currentTimeMillis() + "" + rd.nextInt();

            byte[] now = seek.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update((System.currentTimeMillis() + "").getBytes());
            md.update(now);

            String head = toHex(md.digest());
            return head;
        } catch (NoSuchAlgorithmException ex1) {
            return System.currentTimeMillis()+"";
        }
    }

    private static String toHex(byte[] buffer) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < buffer.length; i+=2) {
            //sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit((buffer[i]+buffer[i+1]) & 0x0f, 16));
        }

        return sb.toString();
    }

    public static void main(String[] args){
        System.out.println(getId()+"----"+getId().length());
    }

}
