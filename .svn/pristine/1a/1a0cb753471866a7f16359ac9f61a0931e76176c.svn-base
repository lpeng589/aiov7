package com.koron.openplatform.jd;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.order.OrderSopWaybillUpdateRequest;
import com.jd.open.api.sdk.response.order.OrderSopWaybillUpdateResponse;
import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;

/* *
 *类名：ImplMethodJdUpdateWayBill
 *@author mj(QQ 458257849)
 *功能：修改运单号
 *详细：从auth证书里面获取对应的证书信息。然后obj数组里得到对应字段 京东的具体返回信息请查看对应的文档信息
 *版本：1.0 
 *日期：2013-02-22
 *说明：代码仅供参考。有些注释的地方 是表示 可能今后会用到的代码。可以删除掉
 */
public class ImplMethodJdUpdateWayBill<T> extends Method<T> {

	@Override
	public boolean check(Object... obj) {
		return true;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {
		MessageBean<T> msg = new MessageBean<T>();
		String orderId = (String) obj[0];
		String logisticsId = (String) obj[1];
		String wayBill = (String) obj[2];
		try {
			JdClient client = new DefaultJdClient(
					auth.get("server").toString(), auth.get("accessToken")
							.toString(), auth.get("appKey").toString(), auth
							.get("appSecret").toString());
			try {
				OrderSopWaybillUpdateRequest request = new OrderSopWaybillUpdateRequest();
				request.setOrderId(orderId);
				request.setLogisticsId(logisticsId);
				request.setWaybill(wayBill);
				OrderSopWaybillUpdateResponse response = client
						.execute(request);
				String code = response.getCode();
				String genDesc = response.getEnDesc();
				Boolean bool = false;
				if ("0".equals(code)) {
					bool = true;
					msg.setCode(1);// one on behalf of success, zero on behalf
									// failure
					msg.setData((T) bool);
					msg.setDescription("修改运单号成功orderId=" + orderId
							+ "|wayBill=" + wayBill);
					msg.setMessage("修改运单号成功orderId=" + orderId + "|wayBill="
							+ wayBill);

				} else {
					msg.setCode(0);
					msg.setData((T) bool);
					msg.setDescription("修改运单号失败orderId=" + orderId
							+ "|wayBill=" + wayBill + genDesc);
					msg.setMessage("修改运单号失败orderId=" + orderId + "|wayBill="
							+ wayBill + genDesc);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(0);
			msg.setDescription("修改运单号发生错误：" + e.getMessage());
			msg.setMessage("修改运单号信息失败！失败信息：" + e.getMessage());
		}
		return msg;
	}
}
