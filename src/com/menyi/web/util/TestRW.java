package com.menyi.web.util;

import java.io.IOException;
import java.io.FileWriter;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public class TestRW {
    public TestRW() {
    }
    public static void saveToSql(String path,String content)
      {
          /**����һ��FileWriter ����*/
          try {

        	  if(content !=null && !content.trim().equals("")){
	              FileWriter fw = new FileWriter(path, true);
	              fw.write(content);
	              fw.write("\n");
	              fw.close();
        	  }

          } catch (IOException ex) {
          }

      }
      public static void main(String[]args)
          {
            saveToSql("D:/leiyonghui_aio/aio/website/data.txt","fxe");
       }
}
