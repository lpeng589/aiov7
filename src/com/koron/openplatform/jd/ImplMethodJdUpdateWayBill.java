package com.koron.openplatform.jd;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.order.OrderSopWaybillUpdateRequest;
import com.jd.open.api.sdk.response.order.OrderSopWaybillUpdateResponse;
import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;

/* *
 *������ImplMethodJdUpdateWayBill
 *@author mj(QQ 458257849)
 *���ܣ��޸��˵���
 *��ϸ����auth֤�������ȡ��Ӧ��֤����Ϣ��Ȼ��obj������õ���Ӧ�ֶ� �����ľ��巵����Ϣ��鿴��Ӧ���ĵ���Ϣ
 *�汾��1.0 
 *���ڣ�2013-02-22
 *˵������������ο�����Щע�͵ĵط� �Ǳ�ʾ ���ܽ����õ��Ĵ��롣����ɾ����
 */
public class ImplMethodJdUpdateWayBill<T> extends Method<T> {

	@Override
	public boolean check(Object... obj) {
		return true;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {
		MessageBean<T> msg = new MessageBean<T>();
		String orderId = (String) obj[0];
		String logisticsId = (String) obj[1];
		String wayBill = (String) obj[2];
		try {
			JdClient client = new DefaultJdClient(
					auth.get("server").toString(), auth.get("accessToken")
							.toString(), auth.get("appKey").toString(), auth
							.get("appSecret").toString());
			try {
				OrderSopWaybillUpdateRequest request = new OrderSopWaybillUpdateRequest();
				request.setOrderId(orderId);
				request.setLogisticsId(logisticsId);
				request.setWaybill(wayBill);
				OrderSopWaybillUpdateResponse response = client
						.execute(request);
				String code = response.getCode();
				String genDesc = response.getEnDesc();
				Boolean bool = false;
				if ("0".equals(code)) {
					bool = true;
					msg.setCode(1);// one on behalf of success, zero on behalf
									// failure
					msg.setData((T) bool);
					msg.setDescription("�޸��˵��ųɹ�orderId=" + orderId
							+ "|wayBill=" + wayBill);
					msg.setMessage("�޸��˵��ųɹ�orderId=" + orderId + "|wayBill="
							+ wayBill);

				} else {
					msg.setCode(0);
					msg.setData((T) bool);
					msg.setDescription("�޸��˵���ʧ��orderId=" + orderId
							+ "|wayBill=" + wayBill + genDesc);
					msg.setMessage("�޸��˵���ʧ��orderId=" + orderId + "|wayBill="
							+ wayBill + genDesc);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(0);
			msg.setDescription("�޸��˵��ŷ�������" + e.getMessage());
			msg.setMessage("�޸��˵�����Ϣʧ�ܣ�ʧ����Ϣ��" + e.getMessage());
		}
		return msg;
	}
}
