package com.koron.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlibabaInformationFinder implements InformationFinder {
	private static final String EMAIL_PATTERN = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

	private static final String QQ_PATTERN = "(QQ|qq)[\\D]*[1-9][0-9]+";

	private static final String PHONE_PATTERN = "<li >电&nbsp;&nbsp;&nbsp;&nbsp;话： (.*?)</li>";

	private static final String PHONE_PATTERN2 = "[\\D]((0[0-9]+-)?[1-9][0-9]{6,7})[\\D]";

	private static final String MOBILE_PATTERN = "<li >移动电话： (.*?)</li>";

	private static final String FAX_PATTERN = "<li >传&nbsp;&nbsp;&nbsp;&nbsp;真： (.*?)</li>";

	private static final String FAX_PATTERN2 = "[\\D]((0[0-9]+-)?[1-9][0-9]{6,7})[\\D]";

	private static final String NAME_PATTERN = "<div class=\"company-name\">(.*?)</div>";

	public InfoItem findInforItem(URL url, String name) {

		InfoItem item = new InfoItem(name, url.toString());
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String charset = conn.getHeaderField("Content-Type");
			if (charset != null && charset.indexOf("charset=") != -1)
				charset = charset.substring(charset.indexOf("charset=") + 8);
			else
				charset = "gbk";

			BufferedReader br = new BufferedReader(new InputStreamReader(conn
					.getInputStream(), charset));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null)
				sb.append(line).append('\n');

			String content = sb.toString();

			Pattern p = Pattern.compile(NAME_PATTERN);
			Matcher m = null;
			m = p.matcher(content);
			if (m.find()) {
				item = new InfoItem(m.group(1), url.toString());
			}

			p = Pattern.compile(EMAIL_PATTERN);
			m = p.matcher(content);
			while (m.find()) {
				item.getEmail().add(m.group());
			}

			p = Pattern.compile(MOBILE_PATTERN);
			m = p.matcher(content);
			while (m.find()) {
				item.getMobile().add(m.group(1));
			}

			p = Pattern.compile(PHONE_PATTERN);
			m = p.matcher(content);
			int i = 0;
			while (m.find()) {
				item.getPhone().add(m.group(1));
				i++;
			}
			if (i == 0) {
				p = Pattern.compile(PHONE_PATTERN2);
				m = p.matcher(content);
				while (m.find()) {
					item.getPhone().add(m.group(1));
				}
			}

			p = Pattern.compile(FAX_PATTERN);
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				item.getFax().add(m.group(1));
				i++;
			}
			if (i == 0) {
				p = Pattern.compile(FAX_PATTERN2);
				m = p.matcher(content);
				while (m.find()) {
					item.getFax().add(m.group(1));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return item;
	}

}
