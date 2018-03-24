package com.koron.robot;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Google2Site extends AbstractSearchSite {

	public int getSelPages(){
		return 10;
	}
	public String doPage(int page) {
		return "start=" + page * 10;
	}

	public String doSearch(final String keyWord) {
		try {
			return "q=" + java.net.URLEncoder.encode(keyWord, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String searchURL() {
		return "http://www.google.com.hk/search?hl=zh-CN&";
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
		conn.setRequestProperty("Accept-Encoding", "deflate");
		conn.setRequestProperty("Host", "www.google.com.hk");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Cookie","PREF=ID=40fe22b6b1060b54:U=c4b5c4b2c43ffffd:FF=1:LD=zh-CN:NW=1:TM=1275926532:LM=1310053407:S=UwsKwSSR_k0ZF3z_; NID=48=J7cW_sm8l_lLLp4ipKtnNjsvOddyNvzQo829wlzOpzl-4UTFs8FniNKpG8FhnVlPToak2ewIFOnlPgMDkdYxHEZfhs8sHNffDxo6zN01SQzOXpsAP0CoXaMZerzIzJiG");
	}

	public ArrayList<String[]> getLink(String content) {
		if(content==null||content.length()==0){
			return new ArrayList<String[]>();
		}
		Pattern p = Pattern
				.compile("<h3[\\d\\D]*?href=\"(.*?)\" [\\d\\D]*?</h3>");
		Matcher m = p.matcher(content);
		ArrayList<String[]> al = new ArrayList<String[]>();
		int i = 0;
		while (m.find() && i < 10) {
			String addr = m.group().replaceAll("<.*?>", "");
			String tmpUrl = m.group(1);
			if(tmpUrl.startsWith("/"));
			try {
				tmpUrl = new URL(new URL("http://www.google.com.hk"),tmpUrl).toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			al.add(new String[] { tmpUrl, addr });
			i++;
		}
		return al;
	}

}
