package com.koron.robot;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public interface SearchSite {

	/**
	 * 实现搜索用的字符串
	 * @return http请求用的字符串
	 */
//	public String doSearch(String keyWord);
	/**
	 * 实现翻页需要请求的字符串
	 * @param page 页数
	 * @return http请求用的字符串
	 */
//	public String doPage(int page);
	/**
	 * 执行搜索的URL
	 * @return
	 */
//	public String searchURL();
	
//	public void setHeader(HttpURLConnection conn);
	
	public String getContent(String keyWord,int page);
	
	public ArrayList<String[]> getLink(String content);
	
	public InformationFinder getFinder();
	
	public int getSelPages();
	
}
