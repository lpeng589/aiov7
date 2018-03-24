package com.koron.openplatform.taobao;

import java.util.ArrayList;
import java.util.HashMap;

import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;
import com.koron.openplatform.OpenPlatform;
import com.koron.openplatform.jd.GetOrderInfoMethod;
import com.koron.openplatform.jd.GetOrderIdsMethod;
import com.koron.openplatform.jd.GetProductDetail;
import com.koron.openplatform.jd.GetProductIds;

/* *
 *������JdOpenPlatform
 *@author mj(QQ 458257849)
 *���ܣ�hm����������еľ���������
 *��ϸ�����õ�ʱ�� ��Ҫ����hm���KEY����ȡ��Ӧ�ķ���ʵ������е��á�
 *�汾��1.0
 *���ڣ�2013-02-22
 *˵������������ο�����Щע�͵ĵط� �Ǳ�ʾ ���ܽ����õ��Ĵ��롣����ɾ����
 */
public class TaobaoOpenPlatform implements OpenPlatform {
	
	public static final Long flag  = 5L;//��ʽ��ʱ���޸ĳ�Ϊ5
	
	public static final String SERVER = "serverUrl";
	public static final String APPKEY = "appKey";
	public static final String APPSECRET = "appSecret";
	public static final String SESSIONKEY = "sessionKey";
	
	
	HashMap<String, Method<Object>> hm = new HashMap<String, Method<Object>>();
	/**
	 * �Ա���Ҫ��ʾ���ֶ��б�
	 */
	public static final String fields = "buyer_alipay_no,seller_nick,buyer_nick,title,type,created,tid,seller_rate,buyer_rate,status,payment,discount_fee,adjust_fee,post_fee,total_fee,pay_time,end_time,modified,consign_time,buyer_obtain_point_fee,point_fee,real_point_fee,received_payment,commission_fee,pic_path,num_iid,num,price,cod_fee,cod_status,shipping_type,receiver_name,receiver_state,receiver_city,receiver_district,receiver_address,receiver_zip,receiver_mobile,receiver_phone,seller_flag,orders.title,orders.pic_path,orders.price,orders.num,orders.num_iid,orders.sku_id,orders.refund_status,orders.status,orders.oid,orders.total_fee,orders.payment,orders.discount_fee,orders.adjust_fee,orders.sku_properties_name,orders.item_meal_name,orders.buyer_rate,orders.seller_rate,orders.outer_iid,orders.outer_sku_id,orders.refund_id,orders.seller_type,trade_from,buyer_message,buyer_memo,seller_memo,trade_memo";

	public TaobaoOpenPlatform() {
		hm.put("getorderids", new TaobaoGetOrderIds<Object>());
		hm.put("getorder", new TaobaoGetOrderDetail<Object>());
		hm.put("getproductdetail", new GetWareDetail<Object>());
	}
	
	public MessageBean<? extends Object> callMethod(Authentication auth, String methodName, Object... param) {
		return getMethod(methodName).invoke(auth,param);
	}

	public ArrayList<Method<? extends Object>> getMethods() {
		ArrayList<Method<? extends Object>> al= new ArrayList<Method<? extends Object>>();
		al.addAll(hm.values());
		return al;
	}

	public Method<? extends Object> getMethod(String name) {
		return hm.get(name);
	}
}
