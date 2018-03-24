package com.koron.openplatform.jd;

import java.util.ArrayList;
import java.util.List;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.domain.order.ItemInfo;
import com.jd.open.api.sdk.domain.order.OrderDetailInfo;
import com.jd.open.api.sdk.domain.order.OrderInfo;
import com.jd.open.api.sdk.request.order.OrderGetRequest;
import com.jd.open.api.sdk.response.order.OrderGetResponse;
import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;
import com.koron.openplatform.vos.OrderProduct;
import com.koron.openplatform.vos.TblOrder;

/* *
 *类名：ImplMethodJdGetOrderInfo
 *@author mj(QQ 458257849)
 *功能：根据订单号得到订单详细信息，并封装成通用的tblOrder实体里返回
 *详细：从auth证书里面获取对应的证书信息。然后obj数组里得到对应字段 京东的具体返回信息请查看对应的文档信息
 *版本：1.0
 *日期：2013-02-22
 *说明：代码仅供参考。有些注释的地方 是表示 可能今后会用到的代码。可以删除掉
 */
public class GetOrderInfoMethod<T> extends Method<T> {

	@Override
	public boolean check(Object... obj) {
		if (obj != null)
			return true;
		return false;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {

		MessageBean<T> msg = new MessageBean<T>();
		String orderId = (String) obj[0];
		List<TblOrder> list = new ArrayList<TblOrder>();
		int codeError = 0;
		try {
			JdClient client = new DefaultJdClient(
					auth.get("server").toString(), auth.get("accessToken")
							.toString(), auth.get("appKey").toString(), auth
							.get("appSecret").toString());
			for (Object object : obj) {
				OrderGetRequest request = new OrderGetRequest();
				request.setOrderId(String.valueOf(object));
				OrderGetResponse response = client.execute(request);
				OrderDetailInfo detailInfo = response.getOrderDetailInfo();
				OrderInfo info = detailInfo.getOrderInfo();
				TblOrder tmp = new TblOrder();
				
				tmp.setOrderStatus(info.getOrderState());
				tmp.setOrderID(info.getOrderId());
				tmp.setOrderTime(info.getOrderStartTime());
				tmp.setPayMethod(info.getPayType());
				tmp.setDeliverMethod(info.getDeliveryType());
				tmp.setFavourableMoney(info.getSellerDiscount());
				tmp.setDeliverPay(info.getFreightPrice());
				tmp.setPay(info.getOrderSellerPrice());
				tmp.setAmountMoney(info.getOrderTotalPrice());
				tmp.setNickName(info.getPin());
				tmp.setoPeople(info.getPin());
				
				tmp.setPhoneNum(info.getConsigneeInfo().getTelephone());
				tmp.setMobile(info.getConsigneeInfo().getMobile());
				tmp.setAddress(info.getConsigneeInfo().getFullAddress());
				tmp.setProvince(info.getConsigneeInfo().getProvince());
				tmp.setCity(info.getConsigneeInfo().getCity());
				tmp.setReceiver(info.getConsigneeInfo().getFullname());
				
				tmp.setRemark(info.getOrderRemark());
				tmp.setCerCode(auth.getAlias());
//				tmp.setOrderStatus(info.getOrderStateRemark());
				tmp.setInSign("false");
				tmp.setHideSign("false");
				List<ItemInfo> items = info.getItemInfoList();
				if (items != null) {
					for (ItemInfo itemInfo : items) {
						OrderProduct op = new OrderProduct();
						op.setPrice(itemInfo.getJdPrice());
						op.setProductId(itemInfo.getSkuId());
						op.setTotal(itemInfo.getItemTotal());
						op.setPid(itemInfo.getWareId());
						if(op.getProductId()==null || op.getProductId().equals(""))
							op.setProductId(itemInfo.getWareId());
						tmp.addProuct(op);
					}
				}
				list.add(tmp);
			}

			msg.setCode(1);// one on behalf of success, zero on behalf failure
			msg.setData((T) list);
			msg.setDescription("根据订单编号" + orderId + "查询京东详细信息 返回信息码为");
			msg.setMessage("根据订单编号:" + orderId + "查询京东订单订单详细数成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(codeError);
			msg.setDescription("根据订单编号" + orderId + "查询京东详细信息 返回信息码为"
					+ codeError);
			msg.setMessage("根据订单编号:" + orderId + "查询京东订单订单详细失败 错误信息！"
					+ e.getMessage());
		}
		return msg;
	}
}
