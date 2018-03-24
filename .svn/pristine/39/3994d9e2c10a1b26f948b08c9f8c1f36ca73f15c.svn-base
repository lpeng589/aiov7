package com.koron.robot;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Net114Site extends AbstractSearchSite {
	
	public int getSelPages(){
		return 50;
	}

	public String doPage(int page) {
		return "-p-" + (page+1)+".html";
	}

	public String doSearch(String keyWord) {
		try {
			return java.net.URLEncoder.encode(keyWord, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String searchURL() {
		//http://corp.net114.com/sou/%E6%B7%B1%E5%9C%B3%E7%AE%A1%E5%AE%B6%E5%A9%86.html
		http://corp.net114.com/sou/%E6%B7%B1%E5%9C%B3%E7%AE%A1%E5%AE%B6%E5%A9%86-p-2.html
		return "http://corp.net114.com/sou/";
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
		//<a href="http://www.net114.com/company/230397731.html" title=" 深圳 交点技术有限公司" target="_blank" onclick="SE.onClickViews(this,'','corp');">
		//<font style='color:red'>深圳</font>交点技术有限公司</a></b></span>
		//<a href="http://www.net114.com/2503107/" title="* 深圳 市南田科技有限公司*" target="_blank" onclick="SE.onClickViews(this,'','corp');">
        //*<font style='color:red'>深圳</font>市南田科技有限公司*</a>
		//<a[.]*?href=\"(.*?)\"[^>]*?>(.*?)</a>
		Pattern p = Pattern
				.compile("<a href=\"(http://www.net114.com/[^\"]*?)\" title=[^>]*?>(.*?)</a>");
		Matcher m = p.matcher(content);
		ArrayList<String[]> al = new ArrayList<String[]>();
		while (m.find()) {
			System.out.println(m.group());
			String addr = m.group().replaceAll("<.*?>", "");
			String tmpUrl = m.group(1);			
			
			al.add(new String[] { tmpUrl, addr });
		}
		return al;
	}
	
	public static void main(String[] args){
		Net114Site site = new Net114Site();
		String content = site.getContent("深圳管家婆", 0);
		//String content = "<a href=\"http://www.net114.com/2503107/\" title=\"* 深圳 市南田科技有限公司*\" target=\"_blank\" onclick=\"SE.onClickViews(this,'','corp');\">*<font style='color:red'>深圳</font>市南田科技有限公司*</a>";
		//ArrayList list = site.getLink(content);
		try{
		InfoItem item=site.getFinder().findInforItem(new URL("http://www.net114.com/764268870/"),
				"深圳市先锋电子工具有限公司电子工具部");
		System.out.println(item.getPhone());
		}catch(Exception e){}
		
	}

}
