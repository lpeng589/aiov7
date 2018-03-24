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
 *������ImplMethodTaoBaoGetOrderByPage
 *@author mj(QQ 458257849)
 *���ܣ�������ǰ�Ự�û���Ϊ�����������Ľ������� 
 *��ϸ����auth֤�������ȡ��Ӧ��֤����Ϣ��Ȼ��obj������õ���Ӧ�ֶ� �����ľ��巵����Ϣ��鿴��Ӧ���ĵ���Ϣ
 *�汾��1.0
 *���ڣ�2013-02-22
 *˵������������ο�����Щע�͵ĵط� �Ǳ�ʾ ���ܽ����õ��Ĵ��롣����ɾ����
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
						sb.append(info.getTid().toString()).append(",");// ��������
																		// ����id
					}
				}
			} while (hasNext);
			msg.setCode(1);// one on behalf of success, zero on behalf failure
			msg.setData((T) sb);
			msg.setDescription("taobao���ݵ�ǰ��ҳ��ʱ��λ�ö�Ӧ�Ķ��� ������");
			msg.setMessage("taobao���ݵ�ǰ��ҳ��ʱ��λ�ö�Ӧ�Ķ����ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(codeError);
			msg.setDescription("taobao���ݵ�ǰ��ҳ��ʱ��λ�ö�Ӧ�Ķ��� ������Ϣ��Ϊ" + codeError);
			msg.setMessage("taobao���ݵ�ǰ��ҳ��ʱ��λ�ö�Ӧ�Ķ���ʧ�� ������Ϣ��" + e.getMessage());
		}
		return msg;
	}
}
