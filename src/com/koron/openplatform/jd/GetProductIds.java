package com.koron.openplatform.jd;

import java.util.ArrayList;
import java.util.List;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.request.ware.WareInfoByInfoRequest;
import com.jd.open.api.sdk.response.ware.WareInfoByInfoSearchResponse;
import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;
import com.koron.openplatform.vos.LogisticsCompanyVo;

/* *
 *类名：ImplMethodJdGetLogisticsId
 *@author mj(QQ 458257849)
 *功能：获取商品的id字符串，用逗号隔开
 *详细：从auth证书里面获取对应的证书信息。然后obj数组里得到对应字段 京东的具体返回信息请查看对应的文档信息
 *版本：1.0 
 *日期：2013-02-22
 *说明：代码仅供参考。有些注释的地方 是表示 可能今后会用到的代码。可以删除掉
 */
public class GetProductIds<T> extends Method<T> {

	@Override
	public boolean check(Object... obj) {
		return true;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {
		MessageBean<T> msg = new MessageBean<T>();
		StringBuilder sb = new StringBuilder();
		try {
			JdClient client = new DefaultJdClient(
					auth.get("server").toString(), auth.get("accessToken")
							.toString(), auth.get("appKey").toString(), auth
							.get("appSecret").toString());
			boolean hasMore = false;
			int page = 1;
			do {
				WareInfoByInfoRequest wareInfoByInfoRequest = new WareInfoByInfoRequest();
				wareInfoByInfoRequest.setPage(String.valueOf(page++));
				wareInfoByInfoRequest.setPageSize("20");
				WareInfoByInfoSearchResponse res = client
						.execute(wareInfoByInfoRequest);
				hasMore = false;
				if (res.getCode().equals("0"))// 方法调用正常
				{
					for (Ware ware : res.getWareInfos()) {
						sb.append(ware.getWareId()+",");
					}
					if (res.getTotal() > page * 20)
						hasMore = true;
				}
			} while (hasMore);
		} catch (Exception e) {
			msg.setMessage(e.getMessage());
			msg.setCode(0);
			for (StackTraceElement el : e.getStackTrace()) {
				if (el.getClass().equals(GetProductIds.class))
					msg.setDescription(el.getFileName() + "\t"
							+ el.getMethodName() + "\t" + el.getLineNumber());

			}
		}
		msg.setCode(1);// one on behalf of success, zero on behalf failure
		msg.setData((T)sb);// jos文档示例值 可能无效
		msg.setMessage("获得产品ID成功！");

		return msg;
	}
}
