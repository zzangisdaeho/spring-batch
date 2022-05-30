package io.springbatch.springbatch.batch.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.util.HashSet;
import java.util.Objects;

@Slf4j
public class CompanyJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("***************** memberJob start *****************");
        jobExecution.getExecutionContext().put("failCompanySet", new HashSet<Long>());
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        long startTime = Objects.requireNonNull(jobExecution.getStartTime()).getTime();
        long endTime = Objects.requireNonNull(jobExecution.getEndTime()).getTime();
        log.info("***************** memberJob end *****************");
        log.info("fail company List = {}", jobExecution.getExecutionContext().get("failCompanySet"));
        log.info("수행시간 -> {}", (endTime - startTime));
    }
}
