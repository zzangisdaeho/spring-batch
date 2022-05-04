package io.springbatch.springbatch.job_repository;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JobRepositoryListener implements JobExecutionListener {

    private final JobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String format = sdf.format(date);

        String jobName = jobExecution.getJobInstance().getJobName();
        JobParameters jobParameters = new JobParametersBuilder().addString("requestDate", format).toJobParameters();
        JobExecution lastExecution = jobRepository.getLastJobExecution(jobName, jobParameters);
        if(lastExecution != null) {
            for (StepExecution execution : lastExecution.getStepExecutions()) {
                BatchStatus status = execution.getStatus();
                System.out.println("BatchStatus = " + status.isRunning());
                System.out.println("BatchStatus = " + status.name());
            }
        }
    }
}
