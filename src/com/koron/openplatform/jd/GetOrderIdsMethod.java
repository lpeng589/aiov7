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
 *������ImplMethodJdGetPages
 *@author ��־��
 *���ܣ����ݶ���ʱ��εõ����ж���ID
 *��ϸ����auth֤�������ȡ��Ӧ��֤����Ϣ��Ȼ��obj������õ���Ӧ�ֶ� �����ľ��巵����Ϣ��鿴��Ӧ���ĵ���Ϣ
 *������1����ʼʱ��2��״̬3��ʱ������λΪ�룩ȱʡΪ1����
 *�汾��1.0
 *���ڣ�2013-07-19
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
		int period = -1;//ȡ������ʱ��
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
				request.setStartDate(start);//��ʼʱ��
				request.setEndDate(sdf.format(c.getTime()));
				request.setOrderState(orderState);
				request.setPage(String.valueOf(page++));
				request.setPageSize(String.valueOf(pageSize));
				OrderSearchResponse res = client.execute(request);
				
				hasMore = false;
				if (res.getCode().equals("0"))// ������������
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
			msg.setDescription("����ʱ�䣺"+start+"\t�����"+period+"\t����״̬"+orderState+"ʱ���ѯID�ɹ�");
			msg.setMessage("����ʱ�䣺"+start+"\t�����"+period+"\t����״̬"+orderState+"ʱ���ѯID�ɹ�");
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
			msg.setMessage("����ʱ�䣺"+start+"\t�����"+period+"\t����״̬"+orderState+"ʱ���ѯIDʧ��");
		}
		return msg;
	}
}
