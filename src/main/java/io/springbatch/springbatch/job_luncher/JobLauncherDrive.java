package io.springbatch.springbatch.job_luncher;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JobLauncherDrive {

    private final JobLauncher jobLauncher;
    private final Job helloJob;
    private final BasicBatchConfigurer basicBatchConfigurer;

    public JobExecution launchSync(String param){
        JobParameters jobParameters = jobParameterGenerate(param);

        try {
            SimpleJobLauncher jobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
            //async한 executor를 넣어주면 이후 모든 joblauncher가 async하게 동작하기 때문에
            //sync로 돌리고싶으면 다시 syncTaskExecutor를 넣어준다.
            jobLauncher.setTaskExecutor(new SyncTaskExecutor());
            JobExecution jobExecution = jobLauncher.run(helloJob, jobParameters);
            return jobExecution;
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new RuntimeException(helloJob.getName() + "fail", e);
        }
    }

    public JobExecution launchAsync(String param){
        JobParameters jobParameters = jobParameterGenerate(param);

        try {
            //JobLauncher로 가져오는 SimpleJobLauncher는 jdk dynamic으로 생성된 proxy이다.
            //고로 자식타입으로 downcasting 시도시 castexception이 일어난다.
            //configuer클래스에서 실제 launcher클래스의 구현체인 SimpleJobLauncher를 가지고 있기 때문에
            //configuer를 통해 실제 구현체로 접근하여 setting해준다.
            SimpleJobLauncher jobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
            //근데 이렇게 직접 설정에서 setting을 바꿔주면, 이후 jobLauncher를 사용하여 실행시 모두 다 async하게 동작한다.
            jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
            JobExecution jobExecution = jobLauncher.run(helloJob, jobParameters);
            return jobExecution;
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new RuntimeException(helloJob.getName() + "fail", e);
        }
    }

    private JobParameters jobParameterGenerate(String param) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("input", param)
                .addDate("date", new Date())
                .toJobParameters();

        return jobParameters;
    }
}
