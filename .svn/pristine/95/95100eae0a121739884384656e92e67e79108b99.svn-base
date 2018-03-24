package com.koron.openplatform;

public abstract class Method<T> {
	/**
	 * 方法名
	 */
	String methodName;
	/**
	 * 检查参数是否符合此方法所使用的类型
	 * 要调用方法前要先用这个参数进行验参数。以免调用参数不正确
	 * @param obj 传入方法的参数
	 * @return 参数是否正确
	 */
	public abstract boolean check(Object... obj);
	/**
	 * 调用方法,并返回消息以及数据
	 * @param auth 证书
	 * @param obj　参数
	 * @return　调用方法
	 */
	public abstract<T> MessageBean<T> invoke(Authentication auth,Object... obj);
	
	
}
