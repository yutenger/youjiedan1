package com.stardai.manage.listener;
import com.stardai.manage.quartz.manager.QuartzManager;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class QuartzListener implements ServletContextListener {
	private QuartzManager quartzManager;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		quartzManager.shutdownJobs();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		quartzManager= (QuartzManager) WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext()).getBean("quartzManager");
		 Class cla=null;
		 try {
				cla=Class.forName("com.stardai.manage.quartz.task.ClearTimingTask");
				//每年1月1日零点触发(0 0 0 1 1 ? *)
			 quartzManager.addJob("ClearTimingTask",cla, "0 0 0 1 1 ? *","定时积分清零以及备份");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}

	

}
