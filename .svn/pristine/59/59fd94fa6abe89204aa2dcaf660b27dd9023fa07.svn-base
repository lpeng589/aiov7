package com.koron.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultInformationFinder implements InformationFinder {

	private static final String EMAIL_PATTERN = "([\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+)";
	
	private static final String PHONE_PATTERN = "电话[\\D]*(0[0-9]{2,3})[-\\s\\)]*([0-9]{7,8})[\\D]";
	private static final String PHONE_PATTERN2 = "[\\D](0[0-9]{2,3})[-\\s\\)]*([0-9]{7,8})[\\D]";

	private static final String MOBILE_PATTERN = "[\\D](1[0-9]{10})[\\D]";

	private static final String FAX_PATTERN = "传真[\\D]*(0[0-9]{2,3})[-\\s\\)]*([0-9]{7,8})[\\D]"; 
	private static final String FAX_PATTERN2 = "(0[0-9]{2,3})[-\\s\\)]*([0-9]{7,8})[\\D]*传真"; 

	private static final String NAME_PATTERN = "[\\(\\)\\u4e00-\\u9fa5]{4,50}(公司|厂|集团|店)"; //公司名称要在2-50个字符以内

	public InfoItem findInforItem(URL url, String name) {

		InfoItem item = new InfoItem(name, url.toString());
		
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String charset = conn.getHeaderField("Content-Type");
			
			if (charset!=null && charset.toLowerCase().indexOf("charset=") != -1)
				charset = charset.substring(charset.toLowerCase().indexOf("charset=") + 8);
			else
				charset = "gbk";			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn
					.getInputStream(), charset));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null)
				sb.append(line).append('\n');

			String content = sb.toString();

			content = content.replaceAll("<.*?>", " ");
			content = content.replaceAll("&nbsp;", "");
			content = content.replaceAll("[\\(]{0,1}86[- )]{1}", ""); //去掉所有的86 中国的区号，自动拨号时也不能有国家号
			
			Pattern p = Pattern.compile(NAME_PATTERN);
			Matcher m = null;
			m = p.matcher(content);
			if (m.find()) {
				item = new InfoItem(m.group(), url.toString());
			}

			p = Pattern.compile(EMAIL_PATTERN);
			m = p.matcher(content);
			while (m.find()) {
				item.getEmail().add(m.group(1));
				item.setValid(true);
				//System.out.println(item.hashCode() +item.getName()+":mail:"+m.group(1)+":"+item.getEmail());
			}

			p = Pattern.compile(MOBILE_PATTERN);
			m = p.matcher(content);
			while (m.find()) {
				item.getMobile().add(m.group(1));
				item.setValid(true);
				//System.out.println(item.hashCode() +item.getName()+":mobile:"+m.group(1)+":"+item.getMobile());
			}

			p = Pattern.compile(PHONE_PATTERN);
			m = p.matcher(content);
			boolean found =false;
			while (m.find()) {
				item.getPhone().add(m.group(1)+m.group(2));				
				found = true;
				item.setValid(true);
				//System.out.println(item.hashCode() +item.getName()+":phone:"+m.group(1)+m.group(2)+":"+item.getPhone());
			}
			if(!found){
				p = Pattern.compile(PHONE_PATTERN2);
				m = p.matcher(content);
				while (m.find()) {
					item.getPhone().add(m.group(1)+m.group(2));
					item.setValid(true);
					//System.out.println(item.hashCode() +item.getName()+":phone2:"+m.group(1)+m.group(2)+":"+item.getPhone());
				}				
			}
			

			p = Pattern.compile(FAX_PATTERN);
			m = p.matcher(content);
			found =false;
			while (m.find()) {
				item.getFax().add(m.group(1)+m.group(2));
				found = true;
				item.setValid(true);
				//System.out.println(item.hashCode() +item.getName()+":fax:"+m.group(1)+m.group(2)+":"+item.getFax());
			}
			if(!found){
				p = Pattern.compile(FAX_PATTERN);
				m = p.matcher(content);
				while (m.find()) {
					item.getFax().add(m.group(1)+m.group(2));
					item.setValid(true);
					//System.out.println(item.hashCode() +item.getName()+":fax2:"+m.group(1)+m.group(2)+":"+item.getFax());
				}
			}
		} catch (IOException e) {
//			e.printStackTrace();
		}
		//System.out.println(item.hashCode() +item.getName()+":total:"+item.getEmail()+":"+item.getMobile()+":"+item.getPhone()+":"+item.getFax());
		return item ;
	}

	public static void main(String[] args) {
//		String content = " <li >电话： 86-020 85641597(086)</li>";
//		content = content.replaceAll("[\\(]{0,1}86[- )]{1}", "");
//		System.out.println(content);
//		
//		
//		Pattern p = Pattern.compile(PHONE_PATTERN);
//		Matcher m = p.matcher(content);
//		while (m.find()) {
//			System.out.println(m.groupCount());			
//			System.out.println("1:"+m.group(1));
//			System.out.println("2:"+m.group(2));
//			//System.out.println("3:"+m.group(3));
//			System.out.println("0:"+m.group(0));
//			System.out.println("所有:"+m.group());
//		}
		//http://www.gdhongyuan.com/
		try {
			InfoItem item = new DefaultInformationFinder().findInforItem(new URL(
					"http://cache.baidu.com/c?m=9f65cb4a8c8507ed4fece763105392230e54f730678783492ac3933fc239045c1131a5e87c7c0d07d4c77b6c02a54f57fdf04074340822b098c98a49c9fecf68798730447b0bf03005a368b8bd4632c050872bedb868e0&p=8e49c64ad1934eaf12b4c22246&user=baidu&fm=sc&query=%B9%DC%BC%D2%C6%C5&qid=a4a555f60caa16ad&p1=55"), null);
			System.out.println(item);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
