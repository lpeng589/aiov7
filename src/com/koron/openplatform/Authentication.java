package com.koron.openplatform;

import java.util.HashMap;
import java.util.List;

import com.koron.openplatform.jd.JdOpenPlatform;
import com.koron.openplatform.taobao.TaobaoOpenPlatform;
import com.koron.openplatform.vos.TblOrder;
import com.koron.openplatform.vos.TblProduct;

public class Authentication {
	/**
	 * 别名，在配置文件中配置，根据别名可以生成证书
	 */
	private String alias;
	/**
	 * 证书的提供商，比如：QQ商城，淘宝，京东
	 */
	private String provider;
	/**
	 * 证书中的各种属性均放在此映射表中
	 */
	private HashMap<String, Object> map = new HashMap<String, Object>();
	/**
	 * 用来存储字符串同开放平台的映射关系
	 */
	private static final HashMap<String,OpenPlatform> platformMap = new HashMap<String, OpenPlatform>();
	
	static
	{
		registerPlatform("taobao", new TaobaoOpenPlatform());
		registerPlatform("jd", new JdOpenPlatform());
	}
	
	public Object get(String key)
	{
		return map.get(key);
	}
	
	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
		if(provider.equals("jd"))
			put(JdOpenPlatform.SERVER, "http://gw.api.360buy.com/routerjson");
		else if(provider.equals("taobao"))
			put(TaobaoOpenPlatform.SERVER,"http://gw.api.taobao.com/router/rest");
	}
	
	private final OpenPlatform findPlatform(String provider)
	{
		return platformMap.get(provider);
	}
	
	public static boolean registerPlatform(String key,OpenPlatform c)
	{
		if(key!=null && c!=null)
		{
			platformMap.put(key, c);
			return true;
		}
		return false;
	}
	
	public <T> MessageBean<T> invoke(String methodName,Object... param)
	{
		OpenPlatform openPlatform = new OpenPlatformProxy(findPlatform(getProvider()));
		return openPlatform.callMethod(this, methodName, param);
	}
	
	public static void main(String[] args) {
		Authentication auth = new Authentication();
		
//		auth.setProvider("jd");
//		auth.put(JdOpenPlatform.ACCESSTOKEN, "e84aeb66-b1da-41c3-bad1-c462161198a6");
//		auth.put(JdOpenPlatform.APPKEY, "26E4D1F9AAD3303F05C57EB247442B97");
//		auth.put(JdOpenPlatform.APPSECRET, "b76771ee83de4a7eb60182ee37b140a9");
		
		auth.setProvider("taobao");
		auth.put(TaobaoOpenPlatform.SESSIONKEY, "6100d224626cea4de32d8dec15ed96177471ff6b0f32e6289365102");
		auth.put(TaobaoOpenPlatform.APPKEY, "12502029");
		auth.put(TaobaoOpenPlatform.APPSECRET, "68ce8bfd6c6c378b60bbb3faa5d17963");
		
//		MessageBean<?> ret = auth.invoke("getorderids","2013-07-22 00:00:00","WAIT_SELLER_SEND_GOODS");
//		System.out.println(ret.getData());
//		238407654006237,238403825817654
		
//		MessageBean<?> ret = auth.invoke("getorder","238407654006237","238403825817654");
//		List<TblOrder> list = (List<TblOrder>) ret.getData();
//		for (TblOrder tblOrder : list) {
//			System.out.println(tblOrder);
//		}
		//18778781936   3348511574
		
		MessageBean<?> ret = auth.invoke("getproductdetail","18778781936","3348511574");
		List<TblProduct> list = (List<TblProduct>) ret.getData();
		for (TblProduct product : list) {
			System.out.println(product);
		}
		
//以下的是京东的		
//		auth.put(JdOpenPlatform.SERVER, "http://gw.api.sandbox.360buy.com/routerjson");
//		auth.put(JdOpenPlatform.ACCESSTOKEN, "");
//		auth.put(JdOpenPlatform.APPKEY, "407D71B5341F3E5FC1EE3E19B5EC4A61");
//		auth.put(JdOpenPlatform.APPSECRET, "c690dcb253724723ae53369e50019d35");
//		MessageBean<?> ret = auth.invoke("getpage", "2013-07-18 0:0:0","2013-07-18 20:0:0","1","20","WAIT_SELLER_STOCK_OUT");
//		System.out.println(ret.getMessage());
//		System.out.println(ret.getDescription());
//		int[] str = (int[])ret.getData();
//		for (int string : str) {
//			System.out.print(string+"\t");
//		}
		
//		MessageBean<?> ret = auth.invoke("getproductids");
//		System.out.println(ret.getMessage());
//		System.out.println(ret.getDescription());
//		System.out.println(ret.getData());
		
//		MessageBean<List<TblProduct>> ret = auth.invoke("getproductdetail","1000019266","1000054937","1000019522","1000167562","1000025882");
//		System.out.println(ret.getMessage());
//		System.out.println(ret.getDescription());
//		List<TblProduct> list = ret.getData();
//		for (Object object : list) {
//			System.out.println(object);
//		}
		
//		MessageBean<?> ret = auth.invoke("getorderids","2013-07-19 00:00:00","WAIT_SELLER_STOCK_OUT");
//		System.out.println(ret.getMessage());
//		System.out.println(ret.getDescription());
//		System.out.println(ret.getData());
		
//		MessageBean<?> ret = auth.invoke("getorder","658489999","658390862","658343940");
//		System.out.println(ret.getMessage());
//		System.out.println(ret.getDescription());
//		List<?> list = (List<?>)ret.getData();
//		for (Object object : list) {
//			System.out.println(object);
//		}
		
	}
}
