package com.menyi.web.util;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.menyi.aio.web.billNumber.BillNoManager;
import com.menyi.aio.web.usermanage.UserMgt;

import org.hibernate.Session;
import java.sql.Connection;
import java.sql.SQLException;
import org.hibernate.jdbc.Work;
import java.sql.Types;
import java.sql.CallableStatement;

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
public class CodeGenerater {
    public CodeGenerater() {
    }

    /**
     * 编号成功器，由常量，日期，序列组成
     * 编码    解释                    类型           举例       位数
     * @y2     Year                      Year        96        2
     * @y4     Year                      Year        1996      4
     * @M      Month in year             Month       07        2
     * @d      Day in month              Number      10        2
     * @H      Hour in day (0-23)        Number      00        2
     * @k      Hour in day (1-24)        Number      24        2
     * @m      Minute in hour            Number      30        2
     * @s      Second in minute          Number      55        2
     * @S      Millisecond               Number      978       3
     * @iynCode@ I表示自增长序号，y表示清零时间（y按年清，M按月清，d按日清，n不清零），n为数字表示序列的位数（最多9位），
     * code表示该序列的名称，如@iy5ACC则生成00009的序号，
     * 系统调用存储过程，存储过程维护表tblCodeGenerate(code varchar(50),curValue int,clearDate Datetime)
     * 存储过程名proc_getGenerate(code varchar(50),clearFlag char(1),curValue int out)
     * 存储过程实现功能。
     * 判断code在表tblCodeGenerate中是否存在，如果不存在插入记录，并置curValue为1，cleardate为当前时间
     * 如果存在，则取相应记录的curValue返回，并将curValue加1.
     * 根据clearFlag与clearDate进行相应的读数器清1的动作。
     * @param str String
     * @return String
     */
    public static String generater(String str){
        SimpleDateFormat df;
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        //先处理序号
        if(str.indexOf("@i")>-1){
            //找出序号表达式
            int pos = str.indexOf("@i");
            int pos2 = str.indexOf("@",pos+2);
            //格式不正确，原样返回
            if(pos2<0) return str;
            String nostr = str.substring(pos,pos2+1);
            if(nostr.length()<6) return str;
            String y = nostr.substring(2,3);
            if("yMdn".indexOf(y)==-1){
                return str;
            }
            //第4位必须是数字
            String n =nostr.substring(3,4);
            if("0123456789".indexOf(n)==-1){
                return str;
            }
            String code = nostr.substring(4,nostr.length()-1);
            str=str.replaceAll(nostr,getCode(code,y,Integer.parseInt(n)));
        }
        str=str.replaceAll("@y2",formatInt(c.get(Calendar.YEAR),2));
        str=str.replaceAll("@y4",formatInt(c.get(Calendar.YEAR),4));
        str=str.replaceAll("@M",formatInt(c.get(Calendar.MONTH)+1,2));
        str=str.replaceAll("@d",formatInt(c.get(Calendar.DAY_OF_MONTH),2));
        str=str.replaceAll("@H",formatInt(c.get(Calendar.HOUR_OF_DAY),2));
        str=str.replaceAll("@k",formatInt(c.get(Calendar.HOUR_OF_DAY)+1,2));
        str=str.replaceAll("@m",formatInt(c.get(Calendar.MINUTE),2));
        str=str.replaceAll("@s",formatInt(c.get(Calendar.SECOND),2));
        str=str.replaceAll("@S",formatInt(c.get(Calendar.MILLISECOND),3));
        return str;
    }

    /**
     * 获取编号
     * @param code
     * @param clear
     * @param length
     * @return
     */
    private static String getCode(final String code,final String clear,int length){
        final Result rs = new Result();
        rs.retCode = DBUtil.execute(new IfDB() {
            public int exec(Session session) {
                session.doWork(new Work() {
                    public void execute(Connection connection) throws
                        SQLException {
                        try {
                        	/*单据编号是否连续*/
                        	String isSequence = BaseEnv.systemSet.get("BillNoSequence").getSetting() ;
                            CallableStatement cs = connection.prepareCall("{call proc_getGenerate(?,?,?,?)}");

                            cs.setString(1, code);
                            cs.setString(2, clear);
                            cs.setString(3, isSequence);
                            cs.registerOutParameter(4, Types.INTEGER);
                            cs.execute();
                            rs.setRetVal(cs.getInt(4));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
                            return;
                        }
                    }
                });
                return rs.retCode;
            }
        });
        if(rs.retVal!=null){
            return formatInt(Integer.parseInt(rs.retVal.toString()),length);
        }else{
            return "";
        }
    }

    /**
     * 获取单据编号
     * @param str
     * @param conn
     * @return
     */
    public static String generater(String str,Connection conn,String userId){
    	Result rs = new UserMgt().getLoginBean(userId, conn);
    	Object obj = new Object();
    	if(rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS){
    		obj = rs.getRetVal();
    	}
        str = BillNoManager.find(str, obj, conn);
    	return str;
    }
    
    
    public static String generater(String str,Connection conn){
       SimpleDateFormat df;
       Calendar c = Calendar.getInstance();
       c.setTime(new Date());

       //先处理序号
       if(str.indexOf("@i")>-1){
           //找出序号表达式
           int pos = str.indexOf("@i");
           int pos2 = str.indexOf("@",pos+2);
           //格式不正确，原样返回
           if(pos2<0) return str;
           String nostr = str.substring(pos,pos2+1);
           if(nostr.length()<6) return str;
           String y = nostr.substring(2,3);
           if("yMdn".indexOf(y)==-1){
               return str;
           }
           //第4位必须是数字
           String n =nostr.substring(3,4);
           if("0123456789".indexOf(n)==-1){
               return str;
           }
           String code = nostr.substring(4,nostr.length()-1);
           if(conn != null)
               str=str.replaceAll(nostr,getCode(code,y,Integer.parseInt(n),conn));
           else
               str=str.replaceAll(nostr,getCode(code,y,Integer.parseInt(n)));
       }
       str=str.replaceAll("@y2",formatInt(c.get(Calendar.YEAR),2));
       str=str.replaceAll("@y4",formatInt(c.get(Calendar.YEAR),4));
       str=str.replaceAll("@M",formatInt(c.get(Calendar.MONTH)+1,2));
       str=str.replaceAll("@d",formatInt(c.get(Calendar.DAY_OF_MONTH),2));
       str=str.replaceAll("@H",formatInt(c.get(Calendar.HOUR_OF_DAY),2));
       str=str.replaceAll("@k",formatInt(c.get(Calendar.HOUR_OF_DAY)+1,2));
       str=str.replaceAll("@m",formatInt(c.get(Calendar.MINUTE),2));
       str=str.replaceAll("@s",formatInt(c.get(Calendar.SECOND),2));
       str=str.replaceAll("@S",formatInt(c.get(Calendar.MILLISECOND),3));
       
       return str;
   }


    private static String getCode(final String code,final String clear,int length,Connection conn){
        final Result rs = new Result();

        try {
        	/*单据编号是否连续*/
        	String isSequence = BaseEnv.systemSet.get("BillNoSequence").getSetting() ;
        	
            CallableStatement cs = conn.prepareCall("{call proc_getGenerate(?,?,?,?)}");
            cs.setString(1, code);
            cs.setString(2, clear);
            cs.setString(3, isSequence) ;
            cs.registerOutParameter(4, Types.INTEGER);
            cs.execute();
            rs.setRetVal(cs.getInt(4));
        } catch (Exception ex) {
            ex.printStackTrace();
            rs.setRetCode(ErrorCanst.DEFAULT_FAILURE);
        }

        if(rs.retVal!=null){
            return formatInt(Integer.parseInt(rs.retVal.toString()),length);
        }else{
            return "";
        }
    }


    private static String formatInt(int i,int length){
        String str = i+"";
        if(str.length()>length){
            return str.substring(str.length()-length);
        }else if(str.length()<length){
            while (str.length() < length) {
                str = "0" + str;
            }
        }
        return str;
    }

    public static void main(String[] args){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        System.out.println(""+sdf.format(new Date()));
        System.out.println(""+CodeGenerater.generater("aa@y2:@y4-@M-@d @H:@k:@m:@s:@S"));
    }
}
