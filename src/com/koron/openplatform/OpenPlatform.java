package com.koron.openplatform;

import java.util.ArrayList;
/**
 * 所有开放平台均需继承此接口，具体每个方法实现的功能详见说明
 * @author fangzw
 *
 */
public interface OpenPlatform {
	/**<pre>
	 * 调用开放平台的方法。 
	 * 方法名对应{@link #getMethod(String)}的名称
	 * 调用方法前要注意先验证方法是否正确。即调用
	 * {@link #getMethod(String)}得到的方法的check方法
	 * 在调用参数的时候，需要把反馈的message消息可通过{@link #getLastMessage()}返回
	 * </pre>
	 * @param auth 证书
	 * @param methodName
	 *            　方法的名字
	 * @param method
	 *            参数
	 * @return
	 */
	public <T> MessageBean<T> callMethod(Authentication auth,String methodName, Object... param);

	/**
	 * 获取所有的方法
	 * @return 返回所有的方法
	 */
	public ArrayList<Method<? extends Object>> getMethods();
	/**
	 * 获取方法
	 * @param name 方法名
	 * @return 具体的方法
	 */
	public Method<? extends Object> getMethod(String name);
}
