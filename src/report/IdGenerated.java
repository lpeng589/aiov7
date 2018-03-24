package report;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**<pre>
 * id产生器. 
 * 根据主机IP地址和当前时间和5位随机数，生成MD5摘要。 
 * 保证不同机器，不同时间启动，产生不同的头
 * 加上时间到毫秒。 加上10位序列数。
 * 
 * @author 周新宇
 * @version 1.0
 * </pre>
 */
public class IdGenerated {
	/**
	 * 前缀
	 */
	private static String head = getHead();
	/**
	 * 格式化时间用
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
	/**
	 * 格式化数字用
	 */
	private static java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance();
	/**
	 * 系列号
	 */
	private static int curNo = 0;
	
	static {
		nf.setMinimumIntegerDigits(4);
        nf.setGroupingUsed(false);
	}

	/**
	 * id产生器. 根据主机IP地址和当前时间和5位随机数，生成MD5摘要。保证不同机器，不是时间启动，产生不同的头 加上时间到毫秒。
	 * 加上10位序列数。
	 * 
	 * @return String
	 */
	public synchronized static String getId() {
		int no = ++curNo;
		if (curNo >= 9999) {
			curNo = 0;
		}
		return head + "_" + sdf.format(new Date()) + nf.format(no);
	}

	private static String getHead() {
		String ip;
		try {
			ip = "localhost";
			try {
				java.net.InetAddress ipa = java.net.InetAddress.getLocalHost();
				ip = ipa.getHostAddress();
			} catch (Exception ex) {
			}
			Random rd = new Random();
			String seek = ip + System.currentTimeMillis() + "" + rd.nextInt();
			byte[] now = seek.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(String.valueOf(System.currentTimeMillis()).getBytes());
			md.update(now);
			String head = toHex(md.digest());
			return head;
		} catch (NoSuchAlgorithmException ex1) {
			return System.currentTimeMillis() + "";
		}
	}

	private static String toHex(byte[] buffer) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buffer.length; i += 2) {
			sb.append(Character.forDigit((buffer[i] + buffer[i + 1]) & 0x0f, 16));
		}
		return sb.toString();
	}
}
