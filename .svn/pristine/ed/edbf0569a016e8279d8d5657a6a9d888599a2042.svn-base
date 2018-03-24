package yqzl;

import java.util.TimerTask;

import javax.servlet.ServletContext;

public class YqzlTask extends TimerTask{
	private ServletContext context;
	private String host;
	private String lgnnam;
	private String accnbr;
	public YqzlTask(ServletContext context){
		this.context=context;
		this.host=context.getInitParameter("host");
		this.lgnnam=context.getInitParameter("lgnnam");
		this.accnbr=context.getInitParameter("accnbr");
	}
	
	@Override
	public void run() {
		try {
			new Yqzl(context, host, lgnnam).deal(accnbr);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
