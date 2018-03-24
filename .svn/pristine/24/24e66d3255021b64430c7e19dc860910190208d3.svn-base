package com.koron.openplatform.jd;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.domain.order.OrderSearchInfo;
import com.jd.open.api.sdk.request.order.OrderSearchRequest;
import com.jd.open.api.sdk.response.order.OrderSearchResponse;
import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;


/* *
 *类名：ImplMethodJdGetPages
 *@author 方志文
 *功能：根据订单时间段得到所有订单ID
 *详细：从auth证书里面获取对应的证书信息。然后obj数组里得到对应字段 京东的具体返回信息请查看对应的文档信息
 *参数：1、开始时间2、状态3、时长（单位为秒）缺省为1个月
 *版本：1.0
 *日期：2013-07-19
 */
public class GetOrderIdsMethod<T> extends Method<T> {

	
	@Override
	public boolean check(Object... obj) {
		if(obj!=null && obj.length>1)
		return true;
		
		return false;
	}
	
	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {
		MessageBean<T> msg = new MessageBean<T>();
		String start = (String)obj[0];
		String orderState = (String)obj[1];
		int period = -1;//取订单的时长
		int pageSize =20;
		if(obj.length>2)
		{
			try {
				period = Integer.parseInt(String.valueOf(obj[2]));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(start));
			if(period==-1)
				c.add(Calendar.MONTH, 1);
			else
				c.add(Calendar.SECOND, -1);
			
			boolean hasMore = false;
			int page = 1;
			JdClient client = new DefaultJdClient(
					auth.get("server").toString(), auth.get("accessToken")
							.toString(), auth.get("appKey").toString(), auth
							.get("appSecret").toString());
			StringBuilder sb = new StringBuilder();
			
			do {
				OrderSearchRequest request = new OrderSearchRequest();
				request.setStartDate(start);//开始时间
				request.setEndDate(sdf.format(c.getTime()));
				request.setOrderState(orderState);
				request.setPage(String.valueOf(page++));
				request.setPageSize(String.valueOf(pageSize));
				OrderSearchResponse res = client.execute(request);
				
				hasMore = false;
				if (res.getCode().equals("0"))// 方法调用正常
				{
					for (OrderSearchInfo info: res.getOrderInfoResult().getOrderInfoList()) {
						sb.append(info.getOrderId()+",");
					}
					if (res.getOrderInfoResult().getOrderTotal() > page * pageSize)
						hasMore = true;
				}
			} while (hasMore);
			
			msg.setCode(1);
			msg.setData((T) sb);
			msg.setDescription("根据时间："+start+"\t间隔："+period+"\t订单状态"+orderState+"时间查询ID成功");
			msg.setMessage("根据时间："+start+"\t间隔："+period+"\t订单状态"+orderState+"时间查询ID成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(0);
			for (StackTraceElement el : e.getStackTrace()) {
				if (el.getClass().equals(GetProductDetail.class))
				{
					msg.setDescription(el.getFileName() + "\t"
							+ el.getMethodName() + "\t" + el.getLineNumber());
					break;
				}
			}
			msg.setMessage("根据时间："+start+"\t间隔："+period+"\t订单状态"+orderState+"时间查询ID失败");
		}
		return msg;
	}
}
