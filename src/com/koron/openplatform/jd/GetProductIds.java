package com.koron.openplatform.jd;

import java.util.ArrayList;
import java.util.List;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.request.ware.WareInfoByInfoRequest;
import com.jd.open.api.sdk.response.ware.WareInfoByInfoSearchResponse;
import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;
import com.koron.openplatform.vos.LogisticsCompanyVo;

/* *
 *������ImplMethodJdGetLogisticsId
 *@author mj(QQ 458257849)
 *���ܣ���ȡ��Ʒ��id�ַ������ö��Ÿ���
 *��ϸ����auth֤�������ȡ��Ӧ��֤����Ϣ��Ȼ��obj������õ���Ӧ�ֶ� �����ľ��巵����Ϣ��鿴��Ӧ���ĵ���Ϣ
 *�汾��1.0 
 *���ڣ�2013-02-22
 *˵������������ο�����Щע�͵ĵط� �Ǳ�ʾ ���ܽ����õ��Ĵ��롣����ɾ����
 */
public class GetProductIds<T> extends Method<T> {

	@Override
	public boolean check(Object... obj) {
		return true;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {
		MessageBean<T> msg = new MessageBean<T>();
		StringBuilder sb = new StringBuilder();
		try {
			JdClient client = new DefaultJdClient(
					auth.get("server").toString(), auth.get("accessToken")
							.toString(), auth.get("appKey").toString(), auth
							.get("appSecret").toString());
			boolean hasMore = false;
			int page = 1;
			do {
				WareInfoByInfoRequest wareInfoByInfoRequest = new WareInfoByInfoRequest();
				wareInfoByInfoRequest.setPage(String.valueOf(page++));
				wareInfoByInfoRequest.setPageSize("20");
				WareInfoByInfoSearchResponse res = client
						.execute(wareInfoByInfoRequest);
				hasMore = false;
				if (res.getCode().equals("0"))// ������������
				{
					for (Ware ware : res.getWareInfos()) {
						sb.append(ware.getWareId()+",");
					}
					if (res.getTotal() > page * 20)
						hasMore = true;
				}
			} while (hasMore);
		} catch (Exception e) {
			msg.setMessage(e.getMessage());
			msg.setCode(0);
			for (StackTraceElement el : e.getStackTrace()) {
				if (el.getClass().equals(GetProductIds.class))
					msg.setDescription(el.getFileName() + "\t"
							+ el.getMethodName() + "\t" + el.getLineNumber());

			}
		}
		msg.setCode(1);// one on behalf of success, zero on behalf failure
		msg.setData((T)sb);// jos�ĵ�ʾ��ֵ ������Ч
		msg.setMessage("��ò�ƷID�ɹ���");

		return msg;
	}
}
