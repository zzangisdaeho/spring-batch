package io.springbatch.springbatch.fail;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class FailJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job failJob(){
        return jobBuilderFactory.get("fail-Job")
                .start(compStep1())
                .next(failStep2())
                .build();
    }

    /**
     * fail job은 재실행 가능. (comp job은 재실행 불가능)
     * step 단위로 재실행된다. 이미 성공한 step은 재실행되지 않는다. step execution table status 참고
     * 중간 step이 실패하면 다음 step은 실행해보지도 않는다
     */
//    @Bean
    public Step failStep2() {
        return stepBuilderFactory.get("fail-Step-2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                        JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();

                        String name = jobParameters.getString("name");
                        Long seq = jobParameters.getLong("seq");
                        Date date = jobParameters.getDate("date");
                        Double age = jobParameters.getDouble("age");

                        Map<String, Object> jobParameters1 = chunkContext.getStepContext().getJobParameters();

                        throw new IllegalStateException("Step 2 실패!!");

//                        System.out.println("==============================================");
//                        System.out.println(" >> Hello Spring Batch!! - Step2 is executing");
//                        System.out.println("==============================================");
//
//                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

//    @Bean
    public Step compStep1() {
        return stepBuilderFactory.get("comp-Step-1")
                .tasklet((contribution, chunkContext) -> {

                    System.out.println("==============================================");
                    System.out.println(" >> Hello Spring Batch!! - Step1 is executing");
                    System.out.println("==============================================");

                    return RepeatStatus.FINISHED;
                })
                .build();

    }
}
