package com.koron.openplatform.jd;

import java.util.ArrayList;
import java.util.List;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.request.ware.WareGetRequest;
import com.jd.open.api.sdk.response.ware.WareGetResponse;
import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;
import com.koron.openplatform.vos.TblProduct;
import com.taobao.api.domain.Sku;

/* *
 *类名：ImplMethodJdGetProductDetail
 *作者：方志文
 *功能：获取商品的详细信息
 *详细：从auth证书里面获取对应的证书信息。
 *版本：1.0 
 *日期：2013-07-19
 *说明：代码仅供参考。有些注释的地方 是表示 可能今后会用到的代码。可以删除掉
 */
public class GetProductDetail<T> extends Method<T> {

	@Override
	public boolean check(Object... obj) {
		return true;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {
		MessageBean<T> msg = new MessageBean<T>();
		List<TblProduct> list = new ArrayList<TblProduct>();
		try {
			JdClient client = new DefaultJdClient(
					auth.get("server").toString(), auth.get("accessToken")
							.toString(), auth.get("appKey").toString(), auth
							.get("appSecret").toString());
			for (Object object : obj) {
				WareGetRequest  wareGetRequest= new WareGetRequest();
				wareGetRequest.setWareId(String.valueOf(object));
				wareGetRequest.setFields("");
				WareGetResponse res= client.execute(wareGetRequest);
				if (res.getCode().equals("0"))// 方法调用正常
				{
					Ware ware = res.getWare();
					TblProduct tmp = new TblProduct();
					tmp.setCerCode(auth.getAlias());
					tmp.setGoodsID(String.valueOf(ware.getWareId()));
					tmp.setTittle(ware.getTitle());
					List<com.jd.open.api.sdk.domain.ware.Sku> skus = ware.getSkus();
					if(skus!=null)
					{
						String tmpSku = "";
						for (com.jd.open.api.sdk.domain.ware.Sku sku : skus) {
							tmpSku += sku.getSkuId()+",";
						}
						if(tmpSku.endsWith(","))
							tmpSku = tmpSku.substring(0,tmpSku.length()-1);
						tmp.setSkus(tmpSku);
					}
					if(tmp.getSkus()==null || tmp.getSkus().equals(""))
						tmp.setSkus(String.valueOf(ware.getWareId()));
					tmp.setWeight(ware.getWeight());
					tmp.setSize(ware.getCubage());
					tmp.setPrice(ware.getJdPrice());
					tmp.setBuyPrice(ware.getCostPrice());
					tmp.setMarketPrice(ware.getMarketPrice());
					tmp.setPicture(ware.getLogo());
					list.add(tmp);
				}
			}

		} catch (Exception e) {
			msg.setMessage(e.getMessage());
			msg.setCode(0);
			for (StackTraceElement el : e.getStackTrace()) {
				if (el.getClass().equals(GetProductDetail.class))
				{
					msg.setDescription(el.getFileName() + "\t"
							+ el.getMethodName() + "\t" + el.getLineNumber());
					break;
				}
			}
		}
		msg.setCode(1);// one on behalf of success, zero on behalf failure
		msg.setData((T)list);  //jos文档示例值 可能无效
		msg.setMessage("获得产品ID成功！");

		return msg;
	}
}
