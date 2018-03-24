package tzhive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.chain.contexts.ServletActionContext;

import com.dbfactory.Result;
import com.google.gson.Gson;
import com.menyi.aio.web.mobile.Message;
import com.menyi.web.util.BaseEnv;
import com.menyi.web.util.ErrorCanst;

class TzResult {
	
	public String state;
	public String reason;
	public String count;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) { 
		this.reason = reason;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
	
}

public class HiveServlet extends HttpServlet {

	private static Gson gson;
	private static Message message;
	HttpServletRequest req ;
	HttpServletResponse rsp;
	
	/**
	 * 
	 * @param url			蜂巢接口
	 * @param UserName		易站通用户名
	 * @param StartDateTime	订单的开始时间
	 * @param EndDateTime	订单的结束时间
	 * @param wsCount		购买蜂巢数
	 * @param Token			蜂巢接口token
	 * @param tableParamMap 返回到define中的消息放到这个map里
	 */
    public  void optHive(String url,String UserName,String StartDateTime,String EndDateTime,String wsCount,String Token,HashMap tableParamMap) {
    	String param = "UserName="+UserName+"&"+ "StartDateTime="+StartDateTime+"&"+ "EndDateTime="+EndDateTime+"&"+ "wsCount="+wsCount +"&"+"Token="+Token;
    	
			System.out.println("----HiveServlet---发送参数:"+param);
			if(UserName != null && UserName!= ""){
		    	String TZmsg = HiveServlet.sendGet(url,param);
		    	try {
		    		TZmsg = new String(TZmsg.getBytes("gbk"), "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					BaseEnv.log.error("HiveServlet 字符集转码错误", e);
				}
		    	if(TZmsg != null && TZmsg.indexOf("{") > 0){
		    		TZmsg = TZmsg.substring(TZmsg.indexOf("{")+1,TZmsg.indexOf("}"));
		    		//蜂巢接口返回结果输出到日志中
		    		BaseEnv.log.debug("HiveServlet:---->"+TZmsg);
		    		if(TZmsg.indexOf(",") > 0 && url.indexOf("GetWebSiteCount") > 0){
		    		 String[] split = TZmsg.split(",");
		    		 if(split[2] != null && split[2].indexOf(":") > 0){
			    		 String[] split2 = split[2].split(":");
			    		 String Swcount = split2[1].substring(split2[1].indexOf("\"")+1,split2[1].lastIndexOf("\""));
			    		 int Hivecount = Integer.parseInt(Swcount);
			    		 tableParamMap.put("Hivecount",Hivecount);
		    		 }
		    		 //Gson gson = new Gson();
		    		 //TzResult rs = gson.fromJson(TZmsg, TzResult.class);
		    		 //tableParamMap.put("Hivestate", rs.getState());
		    		 //tableParamMap.put("Hivemsg", rs.getReason());
		    		}else{
		    			tableParamMap.put("Hivecount",-1009);
		    		}
		    	}else {
		    		tableParamMap.put("TZmsg",TZmsg);
		    	}

			}
    }
    
	
    
    
    /***
     *远程调用易站通接口:http://fc.tz1288.com/fc.asmx
     *
     */
    
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
	
}