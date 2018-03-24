package com.menyi.web.util;

import java.util.Random;

/** 
 * @author sech 
 *  
 * @version 1.0 
 */  
public class CallSoftDll {  
  
    public native static String get(String str);  
  
    public native static float getDiskTotalSpace();  
    
    static {  
    	String cup = System.getProperty("sun.arch.data.model");
    	if("64".equals(cup)){
    		System.out.print("采用64位机");
    		System.loadLibrary("../../website/WEB-INF/AIOInfo64");  
    	}else{ 
    		System.out.print("采用32位机");
    		System.loadLibrary("../../website/WEB-INF/AIOInfo32");  
    	}
    }  
    
    public static String bytesToHexString(byte[] src){  
        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString().toUpperCase();  
    } 
    public static byte[] hexStringToBytes(String hexString) {  
        if (hexString == null || hexString.equals("")) {  
            return null;  
        }  
        hexString = hexString.toUpperCase();  
        int length = hexString.length() / 2;  
        char[] hexChars = hexString.toCharArray();  
        byte[] d = new byte[length];  
        for (int i = 0; i < length; i++) {  
            int pos = i * 2;  
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
        }  
        return d;  
    } 
    private static byte charToByte(char c) {  
        return (byte) "0123456789ABCDEF".indexOf(c);  
    } 
  
    public static void main(String[] args) {      	
    	
    	String cup = System.getProperty("sun.arch.data.model");
    	System.out.println("cup="+cup);
        // TODO 自动生成方法存根    
        CallSoftDll test = new CallSoftDll();  
        
        while(true){
        
	        float f= test.getDiskTotalSpace();
	        System.out.println(f); 
	        
	        String s =test.get("KoronSeward");
	        System.out.println(s); 
	        String relMD5 =test.get("KoronSewardM");
	        System.out.println(relMD5); 
	        
	        Random rd = new Random();
	        rd.setSeed(System.currentTimeMillis());
	        int rdi = rd.nextInt(4000);
	        
	        s =test.get(rdi+"");
	        System.out.println(s); 
	        
	        byte[] bs= new byte[16] ;
	    	bs =hexStringToBytes(s);
	    	
	    	rdi +=5;
	    	for(int i=0;i<bs.length ;i++){    		
	    		bs[i] = (byte)(bs[i]-rdi);
	    		if(i>8){
	    			bs[i] =(byte)(bs[i] -2);
	    		}
	    	}    	
	    	System.out.println(bytesToHexString(bs));
	    	
	    	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
        }
    	
//    	for(int j=0;j<4000;j++){
//    			rdi = j;
//    	        s =test.get(rdi+"");    	        
//    	        bs= new byte[16] ;
//    	    	bs =hexStringToBytes(s);
//    	    	
//    	    	rdi +=5;
//    	    	for(int i=0;i<bs.length ;i++){    		
//    	    		bs[i] = (byte)(bs[i]-rdi);
//    	    		if(i>8){
//    	    			bs[i] =(byte)(bs[i] -2);
//    	    		}
//    	    	}    
//    	    	String nmd5= bytesToHexString(bs);
//    	    	if(!nmd5.equals(relMD5)){
//    	    		System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjj:"+j);
//    	    	}
//    		
//    	}
                
        
    }  
    
}
