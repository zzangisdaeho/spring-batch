package io.springbatch.springbatch.quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class QuartzBeanConfig {

    private final DataSource dataSource;

//    @Bean
//    @QuartzDataSource
//    public DataSource quartzDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean
//    public SpringBeanJobFactory springBeanJobFactory() {
//        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
//        jobFactory.setApplicationContext(applicationContext);
//        return jobFactory;
//    }
}
//
//    private final DataSource dataSource;
//    private final SchedulerFactoryBean schedulerFactory;
//
//    private Trigger buildJobTrigger(String scheduleExp) {
//        return TriggerBuilder.newTrigger()
//                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
//    }
//
//    private JobDetail buildJobDetail(Class job, String name, String group, Map params) {
//        JobDataMap jobDataMap = new JobDataMap();
//        jobDataMap.putAll(params);
//
//        return JobBuilder.newJob(job)
//                .withIdentity(name, group)
//                .withDescription("Test Quartz running")
//                .storeDurably()
//                .usingJobData(jobDataMap)
//                .build();
//    }
//
//    @PostConstruct
//    public void scheduler() {
//        JobDetail jobDetail = buildJobDetail(TestScheduleJob.class, "fileJob", "batch", new HashMap());
//        Trigger trigger = buildJobTrigger("0/50 * * * * ?");
//        try {
//            schedulerFactory.getObject().scheduleJob(jobDetail, trigger);
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
//    }
//}
