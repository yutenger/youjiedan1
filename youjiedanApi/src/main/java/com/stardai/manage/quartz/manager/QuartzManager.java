package com.stardai.manage.quartz.manager;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务管理器
 */
@Component
public class QuartzManager {
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";  
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";

    /**
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名 
     *  
     * @param jobName 
     *            任务名 
     * @param cls 
     *            任务 
     * @param time 
     *            时间设置，参考quartz说明文档 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2017
     *  
     *  
     */  
    @SuppressWarnings("unchecked")  
    public void addJob(String jobName, Class cls, String time,String description) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(cls)
                    .withDescription(description) //job的描述
                    .withIdentity(jobName,JOB_GROUP_NAME) //job 的name和group
                    .build();// 任务名，任务组，任务执行类  
            // 触发器 
           CronTrigger trigger = TriggerBuilder.newTrigger().withDescription(description).withIdentity(jobName, TRIGGER_GROUP_NAME).withSchedule(CronScheduleBuilder.cronSchedule(time)).build();// 触发器名,触发器组
            sched.scheduleJob(jobDetail, trigger);
            // 启动  
            if (!sched.isShutdown()) {
                sched.start();
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description: 添加一个定时任务 
     *  
     * @param jobName 
     *            任务名 
     * @param jobGroupName 
     *            任务组名 
     * @param triggerName 
     *            触发器名 
     * @param triggerGroupName 
     *            触发器组名 
     * @param jobClass 
     *            任务 
     * @param time 
     *            时间设置，参考quartz说明文档 
     * @param lazystart 
     *            几秒后启动任务
     * @param description 
     *            任务描述
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2017 
     *  
     * 
     */  
    @SuppressWarnings("unchecked")  
    public void addJob(String jobName, String jobGroupName,
            String triggerName, String triggerGroupName, Class jobClass,  
            String time,String lazystart,String description) { 
    	if(description==null){
    		description="";
    	}
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withDescription(description) //job的描述
                    .withIdentity(jobName,jobGroupName) //job 的name和group
                    .build();// 任务名，任务组，任务执行类  
            // 触发器  
            CronTrigger trigger = TriggerBuilder.newTrigger().withDescription("").withIdentity(triggerName, triggerGroupName).startNow().withSchedule(CronScheduleBuilder.cronSchedule(time)).build();// 触发器名,触发器组
            if(lazystart!=null){
            	  long stime=  System.currentTimeMillis() + Integer.valueOf(lazystart)*1000L; //3秒后启动任务
                  Date statTime = new Date(stime);
            	trigger = TriggerBuilder.newTrigger().withDescription("").withIdentity(triggerName, triggerGroupName).startAt(statTime).withSchedule(CronScheduleBuilder.cronSchedule(time)).build();// 触发器名,触发器组
            }
            sched.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名) 
     *  
     * @param jobName 
     * @param time 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2017
     *  
     * 
     */  
    @SuppressWarnings("unchecked")  
    public void modifyJobTime(String jobName, String time) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName,TRIGGER_GROUP_NAME);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {  
                return;  
            }  
            String oldTime = trigger.getCronExpression();  
            if (!oldTime.equalsIgnoreCase(time)) {  
                JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName,JOB_GROUP_NAME));
                Class objJobClass = jobDetail.getJobClass();  
                removeJob(jobName);  
                addJob(jobName, objJobClass, time,jobDetail.getDescription());  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description: 修改一个任务的触发时间 
     *  
     * @param triggerName 
     * @param triggerGroupName 
     * @param time 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2017
     *  
     * 
     */  
    public void modifyJobTime(String triggerName,
            String triggerGroupName, String time) {  
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {  
                return;  
            }  
            String oldTime = trigger.getCronExpression();  
            if (!oldTime.equalsIgnoreCase(time)) {  
            	 TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                 // 触发器名,触发器组  
                 triggerBuilder.withIdentity(triggerName, triggerGroupName);
                 triggerBuilder.startNow();
                 // 触发器时间设定  
                 triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(time));
                 // 创建Trigger对象
                 trigger = (CronTrigger) triggerBuilder.build();
                 sched.rescheduleJob(triggerKey, trigger);
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名) 
     *  
     * @param jobName 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2017
     *  
     *   
     *  
     * 
     */  
    public void removeJob(String jobName) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName, JOB_GROUP_NAME));// 删除任务
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description: 移除一个任务 
     *  
     * @param jobName 
     * @param jobGroupName 
     * @param triggerName 
     * @param triggerGroupName 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2017
     *  
     * 
     */  
    public  void removeJob(String jobName, String jobGroupName,
            String triggerName, String triggerGroupName) {  
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description:启动所有定时任务 
     *  
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2017
     *  
     *  
     */  
    public void startJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description:关闭所有定时任务 
     *  
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2017
     *  
     * 
     */  
    public  void shutdownJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
}  

