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

public class HuiCongSite implements SearchSite {
	
	public int getSelPages(){
		return 50;
	}
	public String getContent(String keyWord, int page) {
		try {
			URL u = null;
			if(page==1)
				u = new URL("http://www.search.hc360.com/seinterface.cgi?word="+java.net.URLEncoder.encode(keyWord, "gbk")+"&ind=&price=&class=%C6%F3%D2%B5%BF%E2"); 
			else
			u = new URL("http://s.hc360.com/company/"+java.net.URLEncoder.encode(keyWord, "gbk")+".html?L=7&e="+((page-1)*24+1)+"&v=4");
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
		Pattern p = Pattern.compile("(http://[^/.]*.b2b.hc360.com/)[\"'\\s]+[^<]*>(.*?)<");
		Matcher m = p.matcher(content);
		ArrayList<String[]> al = new ArrayList<String[]>();
		while (m.find()) {
			String href = m.group(1)+"shop/company.html";
			al.add(new String[]{href,m.group(2).replaceAll("<.*?>", "")});
		}
		return al;
	}

}
