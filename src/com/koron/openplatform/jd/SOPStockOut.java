package com.koron.openplatform.jd;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.order.OrderSopOutstorageRequest;
import com.jd.open.api.sdk.response.order.OrderSopOutstorageResponse;
import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;

/* *
 *������ImplMethodJdStockOut
 *@author mj(QQ 458257849)
 *���ܣ����� ������� �˵��� �����ŵ�
 *��ϸ����auth֤�������ȡ��Ӧ��֤����Ϣ��Ȼ��obj������õ���Ӧ�ֶ� �����ľ��巵����Ϣ��鿴��Ӧ���ĵ���Ϣ
 *�汾��1.0 
 *���ڣ�2013-02-22
 *˵������������ο�����Щע�͵ĵط� �Ǳ�ʾ ���ܽ����õ��Ĵ��롣����ɾ����
 */
public class SOPStockOut<T> extends Method<T> {
	private static byte[] forSYNC = new byte[4096];
	@Override
	public boolean check(Object... obj) {
		String orderId = (String) obj[0];
		String logisticsId = (String) obj[1];
		String waybill = (String) obj[2];
		if (orderId == null || logisticsId == null || waybill == null || "".equals(orderId) || "".equals(logisticsId) || "".equals(waybill)) {
			return false;
		}
		return true;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {
		
		MessageBean<T> msg = new MessageBean<T>();
	
		String orderId = (String) obj[0];
		String logisticsId = (String) obj[1];
		String waybill = (String) obj[2];
		
		int codeError = 0;
		Boolean bool = false;
		try {
			JdClient client = new DefaultJdClient(
					auth.get("server").toString(), auth.get("accessToken")
							.toString(), auth.get("appKey").toString(), auth
							.get("appSecret").toString());
			OrderSopOutstorageRequest request = new OrderSopOutstorageRequest();
			request.setOrderId(orderId);
			request.setLogisticsId(logisticsId);
			if(waybill != null && !"".equals(waybill)){
				request.setWaybill(waybill);
			}
			OrderSopOutstorageResponse response = client.execute(request);
			String code = response.getCode(); 
			String venderId = response.getVenderId();// �̼�ID
			String modified = response.getModified();// sop����ʱ��
			String zhDesc = response.getZhDesc();
			String desc = "����������ϸ"+zhDesc;
			if(!"0".equals(code)){
				msg.setCode(codeError);// one on behalf of success, zero on behalf failure
				msg.setData((T) bool);
				msg.setDescription("��������ʧ�ܣ�orderId="+orderId+"| logisticsId ="+logisticsId+" waybill = "+waybill);
				msg.setMessage("��������ʧ�ܣ�orderId="+orderId+"| logisticsId ="+logisticsId+" waybill = "+waybill);
			} 
			bool = true;
			msg.setCode(1);// one on behalf of success, zero on behalf failure
			msg.setData((T) bool);
			msg.setDescription("��������ʧ�ܣ�orderId="+orderId+"| logisticsId ="+logisticsId+" waybill = "+waybill);
			msg.setMessage("��������ʧ�ܣ�orderId="+orderId+"| logisticsId ="+logisticsId+" waybill = "+waybill);
		
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(codeError);
			msg.setDescription("��������ʧ�� ������Ϣ��Ϊ" + codeError);
			msg.setMessage("��������ʧ�� ������Ϣ��" + e.getMessage());
		}
		return msg;
	}
}
