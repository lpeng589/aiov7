package com.koron.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public abstract class AbstractSearchSite implements SearchSite {
	public String getContent(String keyWord, int page) {
		try {
			URL u = new URL(searchURL()+this.doSearch(keyWord)+"&"+this.doPage(page));
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
	
	public InformationFinder getFinder() {
		return new DefaultInformationFinder();
	}

	/**
	 * 实现搜索用的字符串
	 * @return http请求用的字符串
	 */
	public abstract String doSearch(String keyWord);
	/**
	 * 实现翻页需要请求的字符串
	 * @param page 页数
	 * @return http请求用的字符串
	 */
	public abstract String doPage(int page);
	/**
	 * 执行搜索的URL
	 * @return
	 */
	public abstract String searchURL();
	
	public abstract void setHeader(HttpURLConnection conn);
	
	public abstract int getSelPages();

	
	public static void main(String[] args) {
		SearchSite ss = new HuiCongSite();
		String content = ss.getContent("五金公司", 2);
//		System.out.println(content);
		ArrayList<String[]> al = ss.getLink(content);
		for (String[] string : al) {
			//System.out.println(string[1]+"\t"+string[0]);
		}
		ArrayList<InfoItem> ai = new ArrayList<InfoItem>();
		InformationFinder fi = ss.getFinder();
		for (String[] string : al) {
			try {
				ai.add(fi.findInforItem(new URL(string[0]),string[1]));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		for (InfoItem infoItem : ai) {
			//System.out.println(infoItem);
		}
		
	}
}
