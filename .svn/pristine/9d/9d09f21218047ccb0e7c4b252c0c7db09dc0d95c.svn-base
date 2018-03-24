package com.koron.openplatform.taobao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;

/* *
 *类名：ImplMethodTaoBaoGetOrderByPage
 *@author mj(QQ 458257849)
 *功能：搜索当前会话用户作为卖家已卖出的交易数据 
 *详细：从auth证书里面获取对应的证书信息。然后obj数组里得到对应字段 京东的具体返回信息请查看对应的文档信息
 *版本：1.0
 *日期：2013-02-22
 *说明：代码仅供参考。有些注释的地方 是表示 可能今后会用到的代码。可以删除掉
 */
public class TaobaoGetOrderIds<T> extends Method<T> {

	@Override
	public boolean check(Object... obj) {
		return true;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {

		MessageBean<T> msg = new MessageBean<T>();
		String beginTime = (String) obj[0];
		String status = (String) obj[1];
		
		
		StringBuilder sb = new StringBuilder();
		int codeError = 0;
		long page = 1;
		boolean hasNext = false;
		long period=-1;
		
		if(obj.length>2)
		{
			try {
				period = Integer.parseInt(String.valueOf(obj[2]));
				if(period> 86400*1000)
					period = -1;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		try {
			TaobaoClient client = new DefaultTaobaoClient(auth.get(
					TaobaoOpenPlatform.SERVER).toString(), auth.get(
					TaobaoOpenPlatform.APPKEY).toString(), auth.get(
					TaobaoOpenPlatform.APPSECRET).toString());
			do {
				TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
				req.setFields("tid");
				Date d= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beginTime);
				req.setStartModified(d);
				if(period==-1)period = 86400*1000;
				req.setEndModified(new Date(d.getTime()+period));
				
				req.setStatus(status);
				req.setPageNo(page++);
				req.setPageSize(40L);
				req.setUseHasNext(true);
				TradesSoldIncrementGetResponse response = client.execute(req,
						auth.get(TaobaoOpenPlatform.SESSIONKEY).toString());
				hasNext = response.getHasNext();
				List<Trade> list = response.getTrades();
				if(list!=null)
				for (int i = 0; i < list.size(); i++) {
					Trade info = list.get(i);
					if (info != null) {
						sb.append(info.getTid().toString()).append(",");// 订单数据
																		// 订单id
					}
				}
			} while (hasNext);
			msg.setCode(1);// one on behalf of success, zero on behalf failure
			msg.setData((T) sb);
			msg.setDescription("taobao根据当前的页和时间段获得对应的订单 总数：");
			msg.setMessage("taobao根据当前的页和时间段获得对应的订单成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(codeError);
			msg.setDescription("taobao根据当前的页和时间段获得对应的订单 返回信息码为" + codeError);
			msg.setMessage("taobao根据当前的页和时间段获得对应的订单失败 错误信息！" + e.getMessage());
		}
		return msg;
	}
}
