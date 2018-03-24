package com.koron.robot;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YahooSite extends AbstractSearchSite {

	public int getSelPages(){
		return 10;
	}
	public String doPage(int page) {
		return "page=" + (page +1 );
	}

	public String doSearch(String keyWord) {
		try {
			return "q=" + java.net.URLEncoder.encode(keyWord, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String searchURL() {
		return "http://www.yahoo.cn/s?vendor=100101&src=8003&";
		// return
		// "http://www.google.com.hk/#hl=zh-CN&newwindow=1&safe=strict&prmd=ivnsm&ei=2zcPTvvDKMbEmAWS3IGeDg&sa=N&fp=a4493305a5b77967&biw=1440&bih=813&";

	}

	public void setHeader(HttpURLConnection conn) {
		conn
				.setRequestProperty(
						"Accept",
						"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-shockwave-flash, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, application/QVOD, application/QVOD, */*");
		conn.setRequestProperty("Accept-Language", "zh-cn");
		conn
				.setRequestProperty(
						"User-Agent",
						"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; InfoPath.2; CIBA; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
		conn.setRequestProperty("Accept-Encoding", " deflate");
		conn.setRequestProperty("Connection", "Keep-Alive");
	}

	public ArrayList<String[]> getLink(String content) {
		Pattern p = Pattern.compile("<h3[\\d\\D]*?href=\"(.*?)\" [\\d\\D]*?</h3>");
		Matcher m = p.matcher(content);
		ArrayList<String[]> al = new ArrayList<String[]>();
		int i = 0;
		while (m.find() && i < 10) {
			String addr = m.group().replaceAll("<.*?>", "");
			String tmpUrl = m.group(1);
			if(tmpUrl.startsWith("/"));
			try {
				tmpUrl = new URL(new URL("http://www.baidu.com"),tmpUrl).toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			al.add(new String[] { tmpUrl, addr });
			i ++;
		}
		return al;
	}

}
