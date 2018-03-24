package com.koron.openplatform.jd;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.order.OrderSopOutstorageRequest;
import com.jd.open.api.sdk.response.order.OrderSopOutstorageResponse;
import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;

/* *
 *类名：ImplMethodJdStockOut
 *@author mj(QQ 458257849)
 *功能：出库 订单编号 运单号 物流号等
 *详细：从auth证书里面获取对应的证书信息。然后obj数组里得到对应字段 京东的具体返回信息请查看对应的文档信息
 *版本：1.0 
 *日期：2013-02-22
 *说明：代码仅供参考。有些注释的地方 是表示 可能今后会用到的代码。可以删除掉
 */
public class SOPStockOut<T> extends Method<T> {
	private static byte[] forSYNC = new byte[4096];
	@Override
	public boolean check(Object... obj) {
		String orderId = (String) obj[0];
		String logisticsId = (String) obj[1];
		String waybill = (String) obj[2];
		if (orderId == null || logisticsId == null || waybill == null || "".equals(orderId) || "".equals(logisticsId) || "".equals(waybill)) {
			return false;
		}
		return true;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {
		
		MessageBean<T> msg = new MessageBean<T>();
	
		String orderId = (String) obj[0];
		String logisticsId = (String) obj[1];
		String waybill = (String) obj[2];
		
		int codeError = 0;
		Boolean bool = false;
		try {
			JdClient client = new DefaultJdClient(
					auth.get("server").toString(), auth.get("accessToken")
							.toString(), auth.get("appKey").toString(), auth
							.get("appSecret").toString());
			OrderSopOutstorageRequest request = new OrderSopOutstorageRequest();
			request.setOrderId(orderId);
			request.setLogisticsId(logisticsId);
			if(waybill != null && !"".equals(waybill)){
				request.setWaybill(waybill);
			}
			OrderSopOutstorageResponse response = client.execute(request);
			String code = response.getCode(); 
			String venderId = response.getVenderId();// 商家ID
			String modified = response.getModified();// sop出库时间
			String zhDesc = response.getZhDesc();
			String desc = "京东出库详细"+zhDesc;
			if(!"0".equals(code)){
				msg.setCode(codeError);// one on behalf of success, zero on behalf failure
				msg.setData((T) bool);
				msg.setDescription("京东出库失败：orderId="+orderId+"| logisticsId ="+logisticsId+" waybill = "+waybill);
				msg.setMessage("京东出库失败：orderId="+orderId+"| logisticsId ="+logisticsId+" waybill = "+waybill);
			} 
			bool = true;
			msg.setCode(1);// one on behalf of success, zero on behalf failure
			msg.setData((T) bool);
			msg.setDescription("京东出库失败：orderId="+orderId+"| logisticsId ="+logisticsId+" waybill = "+waybill);
			msg.setMessage("京东出库失败：orderId="+orderId+"| logisticsId ="+logisticsId+" waybill = "+waybill);
		
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(codeError);
			msg.setDescription("京东出库失败 返回信息码为" + codeError);
			msg.setMessage("京东出库失败 错误信息！" + e.getMessage());
		}
		return msg;
	}
}
