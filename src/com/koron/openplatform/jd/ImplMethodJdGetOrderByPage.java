package com.koron.openplatform.jd;

import java.util.ArrayList;
import java.util.List;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.domain.order.OrderResult;
import com.jd.open.api.sdk.domain.order.OrderSearchInfo;
import com.jd.open.api.sdk.request.order.OrderSearchRequest;
import com.jd.open.api.sdk.response.order.OrderSearchResponse;
import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;

/* *
 *������ImplMethodJdGetOrderByPage
 *@author mj(QQ 458257849)
 *���ܣ����ݵ�ǰ��ҳ��ʱ��λ�ö�Ӧ�Ķ���
 *��ϸ����auth֤�������ȡ��Ӧ��֤����Ϣ��Ȼ��obj������õ���Ӧ�ֶ� �����ľ��巵����Ϣ��鿴��Ӧ���ĵ���Ϣ
 *�汾��1.0
 *���ڣ�2013-02-22
 *˵������������ο�����Щע�͵ĵط� �Ǳ�ʾ ���ܽ����õ��Ĵ��롣����ɾ����
 */
public class ImplMethodJdGetOrderByPage<T> extends Method<T> {

	@Override
	public boolean check(Object... obj) {
		return true;
//		String orderId = (String) obj[0];
//		String orderState = (String) obj[1];
//		if (orderId == null || orderState == null || "".equals(orderId)
//				|| "".equals(orderState)) {
//			return false;
//		}
//		boolean isExist = false;
//		for (OrderStatusJd status : OrderStatusJd.values()) {
//			System.out.println(status.name() + "-" + status.toString());
//			if (orderState.equals(status.toString())) {
//				isExist = true;
//			}
//		}
//		return isExist;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {

		MessageBean<T> msg = new MessageBean<T>();
		String begTimeStr = (String) obj[0];
		String endTimeStr = (String) obj[1];
		String page = (String) obj[2];
		String pageSize = (String) obj[3];
		String orderState = (String) obj[4];

		ArrayList<String> ids = new ArrayList<String>();
		int codeError = 0;
		try {
			JdClient client = new DefaultJdClient(
					auth.get("server").toString(), auth.get("accessToken")
							.toString(), auth.get("appKey").toString(), auth
							.get("appSecret").toString());
			OrderSearchRequest request = new OrderSearchRequest();
			request.setStartDate(begTimeStr);// "2012-09-30 17:15:00"
			request.setEndDate(endTimeStr);
			request.setOrderState(orderState);
			request.setPage(page);
			request.setPageSize(pageSize);
			// request.setOptionalFields("vender_id,order_id,pay_type");
			OrderSearchResponse response = client.execute(request);
			OrderResult rs = response.getOrderInfoResult();
			if (rs != null) {
				List<OrderSearchInfo> list = rs.getOrderInfoList();
				for (int i = 0; i < list.size(); i++) {
					OrderSearchInfo info = list.get(i);
					ids.add(info.getOrderId());// �������� ����id
				}
			}

			msg.setCode(1);// one on behalf of success, zero on behalf failure
			msg.setData((T) ids);
			msg.setDescription("���ݵ�ǰ��ҳ��ʱ��λ�ö�Ӧ�Ķ��� ������" + ids.size());
			msg.setMessage("���ݵ�ǰ��ҳ��ʱ��λ�ö�Ӧ�Ķ����ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(codeError);
			msg.setDescription("���ݵ�ǰ��ҳ��ʱ��λ�ö�Ӧ�Ķ��� ������Ϣ��Ϊ" + codeError);
			msg.setMessage("���ݵ�ǰ��ҳ��ʱ��λ�ö�Ӧ�Ķ���ʧ�� ������Ϣ��" + e.getMessage());
		}
		return msg;
	}
}
