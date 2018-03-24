package com.koron.openplatform.taobao;

import java.util.ArrayList;
import java.util.List;

import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;
import com.koron.openplatform.vos.TblProduct;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.ItemImg;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.response.ItemGetResponse;

/* *
 *类名：ImplMethodTaoBaoGetOrderByPage
 *@author mj(QQ 458257849)
 *功能：搜索当前会话用户作为卖家已卖出的交易数据 
 *详细：从auth证书里面获取对应的证书信息。然后obj数组里得到对应字段 京东的具体返回信息请查看对应的文档信息
 *版本：1.0
 *日期：2013-02-22
 *说明：代码仅供参考。有些注释的地方 是表示 可能今后会用到的代码。可以删除掉
 */
public class GetWareDetail<T> extends Method<T> {

	@Override
	public boolean check(Object... obj) {
		if (obj != null)
			return true;
		return false;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {

		MessageBean<T> msg = new MessageBean<T>();

		try {
			TaobaoClient client = new DefaultTaobaoClient(auth.get(TaobaoOpenPlatform.SERVER).toString(), auth.get(
					TaobaoOpenPlatform.APPKEY).toString(), auth.get(TaobaoOpenPlatform.APPSECRET).toString());
			List<TblProduct> products = new ArrayList<TblProduct>();

			for (Object object : obj) {
				try {
					ItemGetRequest req = new ItemGetRequest();
					req.setFields("num_iid,outer_id,title,item_size,item_weight,price,pic_url,outerid,volume,itemimgs,validity");
					req.setNumIid(Long.parseLong(String.valueOf(object)));
					ItemGetResponse response = client.execute(req, auth.get(TaobaoOpenPlatform.SESSIONKEY).toString());
					Item item = response.getItem();
					TblProduct tmp = new TblProduct();
					tmp.setCerCode(auth.getAlias());
					tmp.setGoodsID(String.valueOf(item.getNumIid()));
					tmp.setTittle(item.getTitle());
					// tmp.setDescription(item.getDesc());
					if (item.getOuterId() != null && !item.getOuterId().equals(""))
						tmp.setSkus(item.getOuterId());
					else
						tmp.setSkus(String.valueOf(item.getNumIid()));
					tmp.setSize(String.valueOf(item.getVolume()));
					tmp.setPrice(item.getPrice());
					List<ItemImg> imgs = item.getItemImgs();
					tmp.setPicture(item.getPicUrl());
					tmp.setValidity(String.valueOf(item.getValidThru()));
					products.add(tmp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			msg.setCode(1);// one on behalf of success, zero on behalf failure
			msg.setData((T) products);
			msg.setDescription("taobao根据当前的页和时间段获得对应的订单 总数：");
			msg.setMessage("taobao根据当前的页和时间段获得对应的订单成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(-1);
			msg.setDescription("taobao根据当前的页和时间段获得对应的订单 返回信息码为");
			msg.setMessage("taobao根据当前的页和时间段获得对应的订单失败 错误信息！");
		}
		return msg;
	}
}
