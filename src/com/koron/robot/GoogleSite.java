package com.koron.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleSite extends AbstractSearchSite {

	public int getSelPages(){
		return 10;
	}
	public String doPage(int page) {
		return "start=" + page * 10;
	}

	public String doSearch(String keyWord) {
		try {
			return "q=" + java.net.URLEncoder.encode(keyWord, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getContent(String keyWord, int page) {
		try {
			URL u = new URL("http://www.google.com.hk/#"+doSearch(keyWord)+"&hl=zh-CN&newwindow=1&safe=strict&prmd=ivnsl&ei=tClmTtigIK-hiAfe0sWvCg&"+doPage(page)+"&sa=N&fp=b6d0fee1798fa8bc&biw=1366&bih=594");
			//System.out.println("URL is "+u);
			HttpURLConnection conn = (HttpURLConnection)u.openConnection();
			
			setHeader(conn);
			
			String charset = conn.getHeaderField("Content-Type");
			if (charset!=null && charset.toLowerCase().indexOf("charset=") != -1)
				charset = charset.substring(charset.toLowerCase().indexOf("charset=") + 8);
			else
				charset = "gbk";
			
			
			 BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),charset));
			 StringBuilder sb = new StringBuilder();
			 String line = null;
			 while((line = br.readLine())!=null)
				 sb.append(line).append('\n');
			return sb.toString();
			 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println();
			e.printStackTrace();
		}
		return null;
	}

	public String searchURL() {
//		return "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&rsz=large&";
		 return
		 "http://www.google.com.hk/#q=%E8%B4%A2%E5%8A%A1%E8%BD%AF%E4%BB%B6&hl=zh-CN&newwindow=1&safe=strict&prmd=ivnsl&ei=tClmTtigIK-hiAfe0sWvCg&start=0&sa=N&fp=b6d0fee1798fa8bc&biw=1366&bih=594";

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
		//conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.setRequestProperty("Host", "www.google.com.hk");
		conn.setRequestProperty("Connection", "Keep-Alive");
		 conn
		 .setRequestProperty(
		 "Cookie",
		 "PREF=ID=9a37ce4a35e3cb22:FF=2:LD=zh-CN:NW=1:TM=1315355933:LM=1315355933:S=9qf-NVIP2KCh2C9X");

	}

	public ArrayList<String[]> getLink(String content) {
		ArrayList<String[]> al = new ArrayList<String[]>();
		if(content == null || content.length() ==0){
			return al;
		}
		Pattern p = Pattern.compile("\"unescapedUrl\":\"(.*?)\"");
		Matcher m = p.matcher(content);
		
		while (m.find()) {
			al.add(new String[]{m.group(1),null});
		}
//		titleNoFormatting
		p = Pattern.compile("\"titleNoFormatting\":\"(.*?)\"");
		m = p.matcher(content);
		int i = 0;
		while (m.find()) {
			al.get(i)[1]=m.group(1);
			i++;
		}
		return al;
	}

}
