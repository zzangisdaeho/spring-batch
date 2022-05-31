package io.springbatch.springbatch.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuartzService {
    private final Scheduler scheduler;

    public void addSimpleJob(Class job, String name, String group, String desc, Map params, Integer seconds) {
        JobDetail jobDetail = buildJobDetail(job, name, group, desc, params);

        try {
            if (scheduler.checkExists(jobDetail.getKey())) {
                scheduler.deleteJob(jobDetail.getKey());
            }

            scheduler.scheduleJob(
                    jobDetail,
                    buildSimpleJobTrigger(seconds)
            );
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void addCronJob(Class job, String name, String group, String desc, Map params, String expression) {
        JobDetail jobDetail = buildJobDetail(job, name, group, desc, params);

        try {
            if (scheduler.checkExists(jobDetail.getKey())) {
                scheduler.deleteJob(jobDetail.getKey());
            }

            scheduler.scheduleJob(
                    jobDetail,
                    buildCronJobTrigger(expression)
            );

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private JobDetail buildJobDetail(Class job, String name, String group, String desc, Map params) {
        JobDataMap jobDataMap = new JobDataMap();
        if (params != null) jobDataMap.putAll(params);
        return JobBuilder
                .newJob(job)
                .withIdentity(name, group)
                .withDescription(desc)
                .usingJobData(jobDataMap)
                .build();
    }

    /**
     * Cron Job Trigger
     */
    // *  *   *   *   *   *     *
    // 초  분  시   일   월  요일  년도(생략가능)
    private Trigger buildCronJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp).inTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC)))
                .build();
    }

    /**
     * Simple Job Trigger
     */
    private Trigger buildSimpleJobTrigger(Integer seconds) {
        return TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .repeatForever()
                        .withIntervalInSeconds(seconds))
                .build();
    }

    public static String buildCronExpression(LocalDateTime time) {
        LocalDateTime fireTime = time.plusSeconds(10);
        // 0 0 0 15 FEB ? 2021
        return String.format("%d %d %d %d %s ? %d", fireTime.getSecond(), fireTime.getMinute(), fireTime.getHour(), fireTime.getDayOfMonth(), fireTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase(), fireTime.getYear());
    }
}
