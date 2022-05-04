package io.springbatch.springbatch.fail;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("mysql")
class FailJobConfigurationTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job failJob;

    @Test
    public void run() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user1")
                .addLong("seq", 1L)
                .addDouble("age", 33.3)
                .toJobParameters();
        jobLauncher.run(failJob, jobParameters);
    }
}