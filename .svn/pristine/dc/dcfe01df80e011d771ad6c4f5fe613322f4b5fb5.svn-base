package com.koron.openplatform.jd;

import java.util.ArrayList;
import java.util.List;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.domain.delivery.LogisticsCompanies;
import com.jd.open.api.sdk.domain.delivery.LogisticsCompany;
import com.jd.open.api.sdk.request.delivery.DeliveryLogisticsGetRequest;
import com.jd.open.api.sdk.response.delivery.DeliveryLogisticsGetResponse;
import com.koron.openplatform.Authentication;
import com.koron.openplatform.MessageBean;
import com.koron.openplatform.Method;
import com.koron.openplatform.vos.LogisticsCompanyVo;

/* *
 *类名：ImplMethodJdGetLogisticsId
 *@author mj(QQ 458257849)
 *功能：得到所有的物流公司信息 
 *详细：从auth证书里面获取对应的证书信息。然后obj数组里得到对应字段 京东的具体返回信息请查看对应的文档信息
 *版本：1.0 
 *日期：2013-02-22
 *说明：代码仅供参考。有些注释的地方 是表示 可能今后会用到的代码。可以删除掉
 */
public class ImplMethodJdGetLogisticsId<T> extends Method<T> {

	@Override
	public boolean check(Object... obj) {
		return true;
	}

	@Override
	public MessageBean<T> invoke(Authentication auth, Object... obj) {
		MessageBean<T> msg = new MessageBean<T>();
		try {
			JdClient client = new DefaultJdClient(
					auth.get("server").toString(), auth.get("accessToken")
							.toString(), auth.get("appKey").toString(), auth
							.get("appSecret").toString());
			List<LogisticsCompanyVo> vos = new ArrayList<LogisticsCompanyVo>();
			try {
				DeliveryLogisticsGetRequest request = new DeliveryLogisticsGetRequest();
				DeliveryLogisticsGetResponse response = client.execute(request);
				LogisticsCompanies loginstics = response
						.getLogisticsCompanies();
				List<LogisticsCompany> list = loginstics.getLogisticsList();

				for (int i = 0; i < list.size(); i++) {
					LogisticsCompany company = list.get(i);
					company.getLogisticsId();
					LogisticsCompanyVo vo = new LogisticsCompanyVo();//
					vo.setLogistics_id("" + company.getLogisticsId());
					vo.setLogistics_name(company.getLogisticsName());
					vo.setLogistics_remark(company.getLogisticsRemark());
					vo.setSequence(company.getSequence());
					vos.add(vo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			msg.setCode(1);// one on behalf of success, zero on behalf failure
			msg.setData((T) vos);// jos文档示例值 可能无效
			msg.setDescription("得到所有的物流公司信息总数为" + vos.size());
			msg.setMessage("获得所有的物流公司信息成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setCode(0);
			msg.setDescription("获得所有的新六公司信息发生错误：" + e.getMessage());
			msg.setMessage("获得所有的物流公司信息失败！失败信息：" + e.getMessage());
		}
		return msg;
	}
}
