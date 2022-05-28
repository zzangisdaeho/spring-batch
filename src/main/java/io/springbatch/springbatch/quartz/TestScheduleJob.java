package io.springbatch.springbatch.quartz;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class TestScheduleJob extends QuartzJobBean {

//    private final Job apiJob;

    private final JobLauncher jobLauncher;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {


        Integer testNum = (Integer)context.getJobDetail().getJobDataMap().get("testNum");

        log.info("TestScheduleJob.executeInternal" + testNum);

//        JobParameters jobParameters = new JobParametersBuilder()
//                .addDate("date", new Date())
//                .toJobParameters();
//        jobLauncher.run(apiJob, jobParameters);
    }
}
