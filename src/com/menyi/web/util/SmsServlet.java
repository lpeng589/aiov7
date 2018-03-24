/**
 * 科荣软件 短信猫
 */
package com.menyi.web.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.menyi.aio.bean.SystemSettingBean;

/**
 * <p>
 * Title:短信猫请求
 * </p>
 * <p>
 * Description: 请求与应答都得是JSON数据，并且使用DES加密
 * </p>
 * 
 * @Date:Feb 19, 2014
 * @Copyright: 深圳市科荣软件有限公司
 * @Author PWY
 */
public class SmsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Gson gson;
	private static SmsMgt mgt;
	static {
		mgt = new SmsMgt();
		gson = new GsonBuilder().setDateFormat("yyyy-MM-DD hh:mm:ss").create();
	}

	// 添加新安全算法,如果用JCE就要把它添加进去
	private static byte[] keyBytes = { 0x21, 0x12, 0xF, 0x58, (byte) 0x88,
			0x10, 0x40, 0x38, 0x28, 0x25, 0x79, 0x51, (byte) 0xC1, (byte) 0xDD,
			0x55, 0x66, 0x77, 0x29, 0x74, (byte) 0x28, 0x30, 0x40, 0x37,
			(byte) 0xE2 }; // 24字节的密钥
	private static final String Algorithm = "DESede"; // 定义 加密算法,可用
														// DES,DESede,Blowfish

	/**
	 * 所有接口的入口，根据不同的op调用不同的操作方法
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String data = request.getParameter("data");

		String test = request.getParameter("test");
		if (null != test) {
			JsonObject json = new JsonObject();
			json.addProperty("op", "smsResult");
			json.addProperty("deviceId", "13427698369-45651545");
			json.addProperty("dogId", "");
			json.addProperty("id", "78ee29b8_1402201020363870022");
			json.addProperty("status", 2);
			data = encode(json.toString());
		}

		JsonObject json = new JsonObject();
		if (null == data || data.length() == 0) {
			json.addProperty("code", -1);
			json.addProperty("msg", "错误的请求格式，请输入请求内容");
			writeJson(json, response);
			return;
		}

		String jsonData = decode(data);
		if (null == jsonData) {
			json.addProperty("code", -1);
			json.addProperty("msg", "数据解释不通过");
			writeJson(json, response);
			return;
		}

		BaseEnv.log.debug("--------短信猫--------请求：" + jsonData);

		HashMap mapData = gson.fromJson(jsonData, HashMap.class);
		if (null == mapData) {
			json.addProperty("code", -1);
			json.addProperty("msg", "数据解释不通过");
			writeJson(json, response);
			return;
		}

		if (!checkParameter("op,deviceId,dogId", mapData, response)) {
			return;
		}

		String op = mapData.get("op").toString();
		if ("login".equals(op)) { // 登录验证
			login(mapData, response);
		} else if ("params".equals(op)) { // 设置参数
			setParams(mapData, response);
		} else if ("smsReq".equals(op)) { // 请求一条短信
			reqSms(mapData, response);
		} else if ("smsResult".equals(op)) { // 短信发送结果
			handleSmsResult(mapData, response);
		}
	}

	/**
	 * keybyte为加密密钥，长度为24字节
	 * 
	 * @param src
	 *            被加密的数据缓冲区（源）
	 * @return
	 */
	private static byte[] encryptMode(byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * keybyte为加密密钥，长度为24字节
	 * 
	 * @param src
	 *            加密后的缓冲区
	 * @return
	 */
	private static byte[] decryptMode(byte[] src) {
		try { // 生成密钥
			SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 先用DES加密，再用BASE把加密的字节转成字符串
	 * 
	 * @param in
	 * @return
	 */
	private String encode(String in) {
		if (null == in) {
			return null;
		}
		try {
			return new BASE64Encoder().encode(encryptMode(in.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 先把字符串用BASE转成字节，再用DES解密，最后生成字符串
	 * 
	 * @param in
	 * @return
	 */
	private String decode(String in) {
		if (null == in) {
			return null;
		}
		try {
			byte[] b = decryptMode(new BASE64Decoder().decodeBuffer(in));
			if (null == b) {
				return null;
			}
			return new String(b, "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 把数据返回给手机端
	 * 
	 * @param json
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void writeJson(JsonObject json, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String str = json.toString();
		BaseEnv.log.debug("--------短信猫--------应答：" + str);
		out.println(encode(str));
		out.flush();
		out.close();
	}

	/**
	 * 在map中检查是否全包含params的Key,没有则直接返回到手机
	 * 
	 * @param params
	 * @param map
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	protected boolean checkParameter(String params, HashMap map,
			HttpServletResponse response) throws ServletException, IOException {
		String notExistParam = "";
		String[] sP = params.replace(" ", "").split(",");
		for (int i = 0; i < sP.length; i++) {
			if (map.get(sP[i]) == null) {
				notExistParam += sP[i] + ",";
			}
		}
		if (notExistParam.length() > 0) {
			notExistParam = notExistParam.substring(0,
					notExistParam.lastIndexOf(","));
		}

		if (notExistParam.length() > 0) {
			JsonObject json = new JsonObject();
			json.addProperty("code", -1);
			json.addProperty("msg", "缺少参数以下参数：" + notExistParam);
			writeJson(json, response);
			return false;
		}
		return true;
	}

	/**
	 * 登录验证
	 * 
	 * @param map
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void login(HashMap mapData, final HttpServletResponse response)
			throws ServletException, IOException {
		if (!checkParameter("psw", mapData, response)) {
			return;
		}

		Result rs = mgt.queryAdminPsw();
		String adminPsw = (String) (((List<Object[]>) rs.getRetVal()).get(0))[0];

		JsonObject json = new JsonObject();
		if (mapData.get("psw").equals(adminPsw)) {
			int interval = (int) Float.parseFloat(GlobalsTool
					.getSysSetting("smsInterval"));
			int maxSend = (int) Float.parseFloat(GlobalsTool
					.getSysSetting("smsMaxSend"));
			String allowTime = GlobalsTool.getSysSetting("smsAllowTime");

			json.addProperty("code", 0);
			json.addProperty("interval", interval);
			json.addProperty("maxSend", maxSend);
			json.addProperty("allowTime", allowTime);
		} else {
			json.addProperty("code", -1);
			json.addProperty("msg", "管理员密码错误");
		}
		writeJson(json, response);
	}

	/**
	 * 设置参数
	 * 
	 * @param map
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void setParams(HashMap mapData, final HttpServletResponse response)
			throws ServletException, IOException {
		if (!checkParameter("interval,maxSend,allowTime", mapData, response)) {
			return;
		}
		int interval = (int) Float.parseFloat(mapData.get("interval")
				.toString());
		int maxSend = (int) Float.parseFloat(mapData.get("maxSend").toString());
		String allowTime = mapData.get("allowTime").toString();

		Result rs = mgt.updateParams(interval, maxSend, allowTime);
		JsonObject json = new JsonObject();
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			SystemSettingBean bean = BaseEnv.systemSet.get("smsInterval");
			bean.setSetting(interval + "");

			bean = BaseEnv.systemSet.get("smsMaxSend");
			bean.setSetting(maxSend + "");

			bean = BaseEnv.systemSet.get("smsAllowTime");
			bean.setSetting(allowTime + "");

			json.addProperty("code", 0);
			json.addProperty("msg", "修改成功");
		} else {
			json.addProperty("code", -1);
			json.addProperty("msg", "修改失败");
		}

		writeJson(json, response);

	}

	/**
	 * 请求短信
	 * 
	 * @param map
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void reqSms(HashMap mapData, final HttpServletResponse response)
			throws ServletException, IOException {
		String sendMobile = mapData.get("deviceId").toString();
		Result rs = mgt.querySms(sendMobile);
		JsonObject json = new JsonObject();
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			json = (JsonObject) rs.getRetVal();

			if (null != json) {
				json.addProperty("code", 0);
			} else {
				json = new JsonObject();
				json.addProperty("code", -1);
				json.addProperty("msg", "无新数据");
			}
		} else {
			json.addProperty("code", -1);
			json.addProperty("msg", "查询失败");
		}

		writeJson(json, response);
	}

	/**
	 * 短信结果
	 * 
	 * @param map
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void handleSmsResult(HashMap mapData,
			final HttpServletResponse response) throws ServletException,
			IOException {
		if (!checkParameter("id,status", mapData, response)) {
			return;
		}

		String id = mapData.get("id").toString();
		int status = (int) Float.parseFloat(mapData.get("status").toString());

		Result rs = mgt.updateSms(id, status);
		JsonObject json = new JsonObject();
		if (rs.getRetCode() == ErrorCanst.DEFAULT_SUCCESS) {
			json.addProperty("code", 0);
			json.addProperty("id", id);
		} else {
			json.addProperty("code", -1);
			json.addProperty("msg", "更新失败");
		}
		writeJson(json, response);
	}
}
