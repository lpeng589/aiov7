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

public class AlibabaSite implements SearchSite {
	
	public int getSelPages(){
		return 100;
	}
	public String getContent(String keyWord, int page) {
		try {
			URL u = new URL("http://search.china.alibaba.com/company/k-"+java.net.URLEncoder.encode(keyWord, "gbk")+"_p-"+page+"_n-y.html");
			//System.out.println("URL is "+u);
			HttpURLConnection conn = (HttpURLConnection)u.openConnection();
			
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

	public InformationFinder getFinder() {
		return new DefaultInformationFinder();
	}

	public ArrayList<String[]> getLink(String content) {
		Pattern p = Pattern.compile("(http://[^/.]*.cn.alibaba.com/)[\"'\\s]+[^<]*>(.*?)<");
		Matcher m = p.matcher(content);
		ArrayList<String[]> al = new ArrayList<String[]>();
		while (m.find()) {
			String addr = m.group(2);
			String href = m.group(1);
			href = href + "athena/contact/"+href.substring(7,href.indexOf('.'))+".html";
			al.add(new String[]{href,addr});
		}
		return al;
	}

}
