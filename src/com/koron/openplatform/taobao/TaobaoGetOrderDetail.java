package com.koron.openplatform.taobao;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;
import com.koron.openplatform.vos.OrderProduct;
import com.koron.openplatform.vos.TblOrder;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.PromotionDetail;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.response.TradeFullinfoGetResponse;

/* *
 *类名：ImplMethodTaoBaoGetOrderInfo
 *@author mj(QQ 458257849)
 *功能：根据订单号得到订单详细信息，并封装成通用的tblOrder实体里返回
 *详细：从auth证书里面获取对应的证书信息。然后obj数组里得到对应字段 京东的具体返回信息请查看对应的文档信息
 *版本：1.0
 *日期：2013-02-22
 *说明：代码仅供参考。有些注释的地方 是表示 可能今后会用到的代码。可以删除掉
 */
public class TaobaoGetOrderDetail<T> extends Method<T> {

	@Override
	public boolean check(Object... obj) {
		if(obj!=null)
		return true;
		return false;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {
		
		MessageBean<T> msg = new MessageBean<T>();
		
		
		int codeError = 0;
		try {
			TaobaoClient client = new DefaultTaobaoClient(auth.get(
					TaobaoOpenPlatform.SERVER).toString(), auth.get(
					TaobaoOpenPlatform.APPKEY).toString(), auth.get(
					TaobaoOpenPlatform.APPSECRET).toString());
			List<TblOrder> list = new ArrayList<TblOrder>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Object object : obj) {
				TradeFullinfoGetRequest req =new TradeFullinfoGetRequest();
				req.setFields(TaobaoOpenPlatform.fields);
				req.setTid(Long.parseLong(String.valueOf(object)));
				TradeFullinfoGetResponse response = client.execute(req , auth.get(TaobaoOpenPlatform.SESSIONKEY).toString());
				Trade trade = response.getTrade();
				TblOrder order = new TblOrder();
				order.setOrderID(String.valueOf(trade.getTid()));
				order.setOrderTime(sdf.format(trade.getPayTime()));
				order.setPayMethod("支付宝");
				order.setDeliverMethod(trade.getShippingType());
				order.setAmountMoney(trade.getTotalFee());
				order.setPay(trade.getPayment());
//				order.setDeliverPay(order.getPost());
				List<PromotionDetail> proList = trade.getPromotionDetails();
				Double disCount = 0.;
				if(proList!=null)
				{
					for (PromotionDetail detail : proList) {
						String count = detail.getDiscountFee();
						try {
							disCount += Double.parseDouble(count);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
				order.setFavourableMoney(new DecimalFormat("#.##").format(disCount));
				Properties p;
				order.setDeliverPay(trade.getPostFee());
				order.setNickName(trade.getBuyerAlipayNo());
				order.setReceiver(trade.getReceiverName());
				order.setMobile(trade.getReceiverMobile());
				order.setPhoneNum(trade.getReceiverPhone());
				order.setAddress(trade.getReceiverAddress());
				order.setProvince(trade.getReceiverState());
				order.setCity(trade.getReceiverCity());
				order.setSection(trade.getReceiverDistrict());
				order.setPost(trade.getReceiverZip());
				order.setLeaveWord(trade.getBuyerMessage());
				
				order.setCerCode(auth.getAlias());
				order.setOrderStatus(trade.getStatus());
				order.setInSign("false");
				order.setHideSign("false");
				order.setRemark(trade.getBuyerMemo());
				
				List<Order> orders = trade.getOrders();
				if(orders!=null)
				{
					for (Order order2 : orders) {
						OrderProduct op = new OrderProduct();
						op.setPrice(order2.getPrice());
						op.setProductId(order2.getOuterIid());
						op.setTotal(String.valueOf(order2.getNum()));
						op.setPid(String.valueOf(order2.getNumIid()));
						if(op.getProductId()==null || op.getProductId().equals(""))
							op.setProductId(op.getPid());
						order.addProuct(op);
					}
				}
				list.add(order);
			}
			
			msg.setCode(1);// one on behalf of success, zero on behalf failure
			msg.setData((T) list);
			msg.setDescription("根据订单编号查询taobao详细信息");
			msg.setMessage("根据订单编号查询taobao订单成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(codeError);
			msg.setDescription("根据订单编号查询taobao详细信息 返回信息码为" + codeError);
			msg.setMessage("根据订单编号查询taobao订单详细错误信息！" + e.getMessage());
		}
		return msg;
	}
}
