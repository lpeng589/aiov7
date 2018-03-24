package yqzl;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * 银企直连监听
 * @author pwy
 *
 */
public class YqzlListener implements  ServletContextListener{

	private Timer t = new Timer(true);
	public void contextDestroyed(ServletContextEvent event) {
		t.cancel();
	}
	public void contextInitialized(ServletContextEvent event) {
	    //设置任务计划，启动和间隔时间 
		t.schedule(new YqzlTask(event.getServletContext()), 0, 1000 * 60*1); 
	}

}
