package com.menyi.aio.dyndb;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TableNameGenerate {
    private static int id;
    private static String curDate;

    private static SimpleDateFormat sd = new SimpleDateFormat("yyMMddHHmmss");

    public static synchronized String getTableName(){
        if(!sd.format(new Date()).equals(curDate)){
            curDate = sd.format(new Date());
            id = 0;
        }
        id ++;
        return "tb_"+curDate+id;
    }

    public static synchronized String getFieldName(int i){
        return "tb_"+sd.format(new Date())+"_"+i;
    }

}
