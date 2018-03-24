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
 *������ImplMethodTaoBaoGetOrderInfo
 *@author mj(QQ 458257849)
 *���ܣ����ݶ����ŵõ�������ϸ��Ϣ������װ��ͨ�õ�tblOrderʵ���ﷵ��
 *��ϸ����auth֤�������ȡ��Ӧ��֤����Ϣ��Ȼ��obj������õ���Ӧ�ֶ� �����ľ��巵����Ϣ��鿴��Ӧ���ĵ���Ϣ
 *�汾��1.0
 *���ڣ�2013-02-22
 *˵������������ο�����Щע�͵ĵط� �Ǳ�ʾ ���ܽ����õ��Ĵ��롣����ɾ����
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
				order.setPayMethod("֧����");
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
			msg.setDescription("���ݶ�����Ų�ѯtaobao��ϸ��Ϣ");
			msg.setMessage("���ݶ�����Ų�ѯtaobao�����ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(codeError);
			msg.setDescription("���ݶ�����Ų�ѯtaobao��ϸ��Ϣ ������Ϣ��Ϊ" + codeError);
			msg.setMessage("���ݶ�����Ų�ѯtaobao������ϸ������Ϣ��" + e.getMessage());
		}
		return msg;
	}
}
