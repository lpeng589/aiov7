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
 *类名：JdOpenPlatform
 *@author mj(QQ 458257849)
 *功能：hm里面存入所有的京东方法。
 *详细：调用的时候 需要根据hm里的KEY来获取对应的方法实现类进行调用。
 *版本：1.0
 *日期：2013-02-22
 *说明：代码仅供参考。有些注释的地方 是表示 可能今后会用到的代码。可以删除掉
 */
public class TaobaoOpenPlatform implements OpenPlatform {
	
	public static final Long flag  = 5L;//正式的时候修改成为5
	
	public static final String SERVER = "serverUrl";
	public static final String APPKEY = "appKey";
	public static final String APPSECRET = "appSecret";
	public static final String SESSIONKEY = "sessionKey";
	
	
	HashMap<String, Method<Object>> hm = new HashMap<String, Method<Object>>();
	/**
	 * 淘宝需要显示的字段列表
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
