package com.menyi.aio.web.upgrade;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//import com.menyi.web.util.BaseEnv;

public class HttpTransfer {
	public String postHttp(String path,String parameterData){
		
		return postHttp(path,parameterData,"application/x-www-form-urlencoded");
	}
	
	public String postHttp(String path,String parameterData,String contentType){
		String response = "";		
		HttpURLConnection uRLConnection= null;		
		try{
			URL url = new URL(path);
			uRLConnection = (HttpURLConnection) url.openConnection();			
			uRLConnection.setDoInput(true);
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setRequestProperty("Accept-Charset", "UTF-8");
			uRLConnection.setConnectTimeout(3*1000);
			//uRLConnection.setReadTimeout(60*1000);			
			uRLConnection.setUseCaches(false);
			uRLConnection.setInstanceFollowRedirects(false);
			uRLConnection.setRequestProperty("Content-Type",contentType);
			//uRLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			//uRLConnection.setRequestProperty("Content-Type","text/html");
			uRLConnection.connect();			
			
			DataOutputStream out = new DataOutputStream(uRLConnection.getOutputStream());			
			out.writeBytes(parameterData);
			out.writeBytes("\r\n");
			out.flush();
			out.close();
			InputStream is = uRLConnection.getInputStream();			
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String readLine = null;
			
			while ((readLine = br.readLine()) != null && readLine.length()!=0) {								
				response += readLine;				
			}
			
			is.close();
			br.close();
			uRLConnection.disconnect();			
		}catch (Exception e) {			
			//BaseEnv.log.error("postHttp error:",e);
			System.out.println(e.getMessage());
		}finally{
			if(uRLConnection!=null){
				uRLConnection.disconnect();
			}
			return response;			
		}
	}
	
	private static class TrustAnyTrustManager implements X509TrustManager {


		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}


		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}


		public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[] {};
		}
	}
	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
		return true;
		}
	}
	
	public boolean postFile(String path,String f){
		
		DataOutputStream dos = null;
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		byte[] bt = new byte[1024];
		long fileSize = 0;
		long TotalSize = 0;
		int len = 0;
		HttpURLConnection urlc;		
		RandomAccessFile raFile = null;
		try{
			URL url = new URL(path);
			urlc = (HttpURLConnection) url.openConnection();		
			urlc.setDoInput(true);
			urlc.setDoOutput(true);
			urlc.setRequestMethod("POST");
			urlc.setConnectTimeout(2*1000);
			urlc.setReadTimeout(10*1000);
			//*****原始下载******//			
			DataOutputStream out = new DataOutputStream(urlc.getOutputStream());
			out.writeBytes("");
			out.writeBytes("\r\n");
			out.flush();
			out.close();
			
			fos = new FileOutputStream(f);
			
			dos = new DataOutputStream(fos);				
		    
			bis = new BufferedInputStream(urlc.getInputStream());				
		
			while((len=bis.read(bt))>0){
				dos.write(bt,0,len);					
			}
		
			if(bis!=null)
				bis.close();
			if(fos!=null)
				fos.close();
			if(dos!=null){
				dos.close();
			}
			if(raFile!=null){
				raFile.close();
			}
			
			return true;
		}catch (FileNotFoundException e) {				
			System.out.println("升级包下载失败。");
			return false;
		}catch (IOException e) { 
			System.out.println("升级包下载失败:"+e.getMessage());
			return false;
		}finally{			
		}
	}
	

	public String postHttps(String path){
		return postHttps(path,"");
	}
	
	public String postHttps(String path,String parameterData){
			return 	postHttps(path,parameterData,"application/x-www-form-urlencoded");
	}
	
	public String postHttps(String path,String parameterData,String contentType){		
		String response = "";		
		HttpsURLConnection  uRLConnection= null;
		try{
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },new java.security.SecureRandom());
			URL url = new URL(path);
			uRLConnection = (HttpsURLConnection) url.openConnection();
			uRLConnection.setSSLSocketFactory(sc.getSocketFactory());
			uRLConnection.setHostnameVerifier(new TrustAnyHostnameVerifier());
			uRLConnection.setDoInput(true);
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setConnectTimeout(3*1000);
			uRLConnection.setReadTimeout(30*1000);			
			uRLConnection.setUseCaches(false);
			uRLConnection.setInstanceFollowRedirects(false);
			uRLConnection.setRequestProperty("Content-Type",contentType);
			uRLConnection.connect();			
			
			DataOutputStream out = new DataOutputStream(uRLConnection.getOutputStream());
			out.writeBytes(parameterData);
			out.writeBytes("\r\n");
			out.flush();
			out.close();
			InputStream is = uRLConnection.getInputStream();			
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String readLine = null;			
			while ((readLine = br.readLine()) != null && readLine.length()!=0) {				
				response = response + readLine;
			}			
			is.close();
			br.close();
			uRLConnection.disconnect();			
		}catch(Exception e){	
			//BaseEnv.log.error("path="+path,e);			
		}finally{
			if(uRLConnection!=null){
				try{										
					uRLConnection.disconnect();					
				}catch(Exception e){
					//BaseEnv.log.error("connection 关闭异常：",e);					
				}				
			}
			//BaseEnv.log.info("response="+response);
			return response;			
		}
	}
}
