package com.koron.openplatform;

import java.util.ArrayList;

public class OpenPlatformProxy implements OpenPlatform {
	
	OpenPlatform openPlatform;
	
	public OpenPlatformProxy(OpenPlatform openPlatform) {
		super();
		this.openPlatform = openPlatform;
	}

	public MessageBean<? extends Object> callMethod(Authentication auth,String methodName, Object... param) {
		boolean check = getMethod(methodName).check(param);
		if(!check)
			return null;
		return openPlatform.callMethod(auth,methodName, param);
	}

	public ArrayList<Method<? extends Object>> getMethods() {
		return openPlatform.getMethods();
	}

	public Method<? extends Object> getMethod(String name) {
		return openPlatform.getMethod(name);
	}
}
