package com.menyi.aio.web.mobile;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.dbfactory.Result;
import com.dbfactory.hibernate.DBUtil;
import com.dbfactory.hibernate.IfDB;
import com.google.gson.Gson;
import com.koron.wechat.common.oauth2.Oauth2;
import com.koron.wechat.common.oauth2.Oauth2ResultBean;
import com.koron.wechat.common.util.WXSetting;
import com.menyi.aio.web.login.LoginBean;
import com.menyi.aio.web.usermanage.UserMgt;

public class MobileVueFilter extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	UserMgt userMgt = new UserMgt();

	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURI();
        if (check(url, response)) // ����Ƿ���΢�Ź���ƽ̨�ص���֤
        	return ;
		String userAgent = request.getHeader("user-agent").toLowerCase();
		LoginBean loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
		if ((userAgent.indexOf("micromessenger") >=0 || userAgent.indexOf("wxwork") >= 0) && loginBean == null) { // ΢�������
			String code = request.getParameter("code");
			String state = request.getParameter("state");
			// Ĭ�ϵ�keyNameΪ��ҵ΢��
			Oauth2 oauth2 = new Oauth2(WXSetting.AGENTKEYNAME_WXWORK);
			// ���Ը���state��ȡkeyName(keyName����Ψһ��ʶ΢�ŷ���ţ�΢����ҵ�ţ���ҵ΢��)
			if (state != null && WXSetting.getInstance().getSettingBase(state) != null)
				oauth2 = new Oauth2(state);
			if (code == null) {
				// �ض���ȡcode
				String queryString = request.getQueryString();
				if(queryString != null)
					 url += "?"+ queryString;
				String redirectURL = oauth2.getCodeUrl(url);
				response.sendRedirect(redirectURL);
				return ;
			} else {
				// ��code��ȡopenid 
				Oauth2ResultBean oauth2User = oauth2.getUserIdByCode(code);
				String userId = oauth2User.getUserId();
				if (userId == null && oauth2User.getOpenid() != null) { // ��Դ��΢�ţ�ȡopenid
					userId = oauth2User.getOpenid();
					request.getSession().setAttribute("wxopenid", oauth2User.getOpenid());
				}
				if (oauth2User != null && userId != null) {
					Result rs = userMgt.login(userId);
					Message msg = new AIOApi().loginAuth(request, response,rs, userId);
					if( msg.getCode().equals("OK") ) {
						request.getSession().setAttribute("wxstate", oauth2.getKeyName()); // ֱ��ʹ��keyName ��Ϊstate,��������Դ
						if(oauth2.getKeyName().equals(WXSetting.AGENTKEYNAME_WXWORK))// �������ҵ΢�ţ����߿ͻ�����Ҫ��ˢһ��ҳ�棬����Ϊ�˽����ǰ�汾jssdk��bug.
							request.setAttribute("reloadFlag", new Boolean(true)); 
					}
				}
			}	
		}
		if(request.getSession().getAttribute("LoginBean") != null) {
		    loginBean = (LoginBean) request.getSession().getAttribute("LoginBean");
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("name", loginBean.getName());
			userMap.put("password", "");
			userMap.put("id", "signed");
			request.setAttribute("user", new Gson().toJson(userMap));
		}
		request.getRequestDispatcher(request.getContextPath() + "/mobile/index.html").forward(request, response);
  }
	
  /**
   * �����ݿ���ȡ΢��id��secret����	
   */
  @Override	
  public void init() {
	  DBUtil.execute(new IfDB() {
			public int exec(Session session) {
				session.doWork(new Work() {
					public void execute(Connection conn) throws SQLException {
						try {
							PreparedStatement ps = conn.prepareStatement("select * from tblWxSet");
							ResultSet rset = ps.executeQuery();
							while (rset.next())
								WXSetting.getInstance().register(rset.getString("KeyName"), rset.getString("CorpID"), rset.getString("Secret"), Integer.parseInt(rset.getString("AgentId_check")), rset.getString("RemoteUrl"), Boolean.parseBoolean(rset.getString("Flag")));
						} catch (Exception ex) {
							System.out.println("��ʼ��΢�ų���");
							return;
						}
					}
				});
				return 1;
			}
		});
  }
  
  /**
   * ��Ӧ΢�ŵĻص���ַ��֤
   * @param url
   * @param response
   * @return
   */
  public boolean check(String url, HttpServletResponse response) {
	  String regEx = "/mobilevue/MP_verify_(.+)\\.txt$";  
      Pattern p = Pattern.compile(regEx);  
      Matcher m = p.matcher(url);  
      if (m.find()) {  
    	  try
    	  {
	         response.setContentType("text/plain;charset=UTF-8");
	         response.setHeader("Content-Disposition", "attachment; filename=" + "MP_verify_" + m.group(1) + ".txt"); 
	         OutputStream outputStream = response.getOutputStream();
	         String context = m.group(1);
	         byte[] buffer = context.getBytes();  
	         outputStream.write(buffer);
	         outputStream.flush();
	         outputStream.close();
    	  }
    	  catch(Exception e)
    	  {
    	   System.out.println("΢�Ź���ƽ̨�ص���֤�ش�verify�ļ�����");
    	  } 
    	  return true;
      }
      else 
    	  return false;
  }
  
}