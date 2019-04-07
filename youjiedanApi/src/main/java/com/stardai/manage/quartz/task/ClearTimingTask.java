package com.stardai.manage.quartz.task;

import com.stardai.manage.service.CreditService;
import com.stardai.manage.utils.SpringContextUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务类
 * @author  yax
 */
public class ClearTimingTask implements Job {
    private static final Logger log = LoggerFactory.getLogger(ClearTimingTask.class);
    private CreditService creditService;
    @Override
    public void execute(JobExecutionContext context)  {
        creditService= SpringContextUtil.getBean(CreditService.class);
        creditService.clearTimingAndBackUp();
    }
}
