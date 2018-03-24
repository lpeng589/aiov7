package com.menyi.web.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumericTool {
	public String format(String val,String format){				
		if(val == null){
			return "";
		}		
		try{
			Double value = Double.parseDouble(val);
			NumberFormat formatter = new DecimalFormat(format); 
			
			return formatter.format(value);
		} catch(Exception e){
			return val;
		}
		
	}
	
	public String format(Object val,String format){
				
		if(val == null){
			return "";
		}
		String v = "";
		if(val instanceof BigDecimal){
			v = ((BigDecimal)val).toString();
		}
		
		try{
			Double value = Double.parseDouble(v);
			NumberFormat formatter = new DecimalFormat(format); 
			
			return formatter.format(value);
		} catch(Exception e){
			return "";
		}
		
	}
}
