package io.springbatch.springbatch.job_repository;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("mysql")
@Import(CustomBatchConfigurer.class)
class JobRepositoryConfigurationTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job repositoryJob;

    @Test
    public void run() throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(date);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", format)
                .toJobParameters();

        jobLauncher.run(repositoryJob, jobParameters);
    }
}