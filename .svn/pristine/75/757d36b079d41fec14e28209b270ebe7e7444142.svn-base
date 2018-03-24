package com.koron.openplatform.jd;

import java.util.ArrayList;
import java.util.HashMap;

import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;
import com.koron.openplatform.OpenPlatform;

/* *
 *类名：JdOpenPlatform
 *作者：方志文
 *功能：hm里面存入所有的京东方法。
 *详细：调用的时候 需要根据hm里的KEY来获取对应的方法实现类进行调用。
 *版本：1.0 
 *日期：2013-07-19 
 *说明：代码仅供参考。有些注释的地方 是表示 可能今后会用到的代码。可以删除掉
 */
public class JdOpenPlatform implements OpenPlatform {
	public static final String SERVER = "server";
	public static final String ACCESSTOKEN = "accessToken";
	public static final String APPKEY = "appKey";
	public static final String APPSECRET = "appSecret";
	
	
	HashMap<String, Method<Object>> hm = new HashMap<String, Method<Object>>();
	
	public JdOpenPlatform() {
		hm.put("getorderids", new GetOrderIdsMethod<Object>());
		hm.put("getorder", new GetOrderInfoMethod<Object>());
		hm.put("getproductids", new GetProductIds<Object>());
		hm.put("getproductdetail", new GetProductDetail<Object>());
		hm.put("SOPStockOut", new GetProductDetail<Object>());
	}
	
	public MessageBean<? extends Object> callMethod(Authentication auth, String methodName, Object... param) {
		return getMethod(methodName).invoke(auth,param);
	}

	public ArrayList<Method<? extends Object>> getMethods() {
		ArrayList<Method<? extends Object>> al= new ArrayList<Method<? extends Object>>();
		al.addAll(hm.values());
		return al;
	}

	public Method<? extends Object> getMethod(String name) {
		return hm.get(name);
	}
	
}
