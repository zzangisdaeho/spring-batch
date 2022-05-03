package io.springbatch.springbatch.fail;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class FailJobRunner implements ApplicationRunner {

    // job 실행기
    private final JobLauncher jobLauncher;

    private final Job failJob;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user1")
                .addLong("seq", 1L)
//                .addDate("date", new Date())
                .addDouble("age", 33.3)
                .toJobParameters();
        jobLauncher.run(failJob, jobParameters);
    }
}
