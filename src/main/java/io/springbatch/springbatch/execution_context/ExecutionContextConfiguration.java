package io.springbatch.springbatch.execution_context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class ExecutionContextConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * context는 데이터 저장공간이라고 생각하면 편하다
     * Job context는 step끼리 공유 가능, job끼리는 불가능
     * Step context는 step끼리 공유 불가능, job끼리도 불가능
     */

    @Bean
    public Job contextJob() {
        return this.jobBuilderFactory.get("context-Job")
                .start(step1())
                .next(step2())
                .next(step3())
                .next(step4())
                .build();
    }

//    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        //StepContribution 을 통한 context를 얻어오기
                        ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
                        ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();

                        //ChunkContext 를 통한 context를 얻어오기
                        ExecutionContext jobExecutionContext2 = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                        Map<String, Object> jobExecutionContext3 = chunkContext.getStepContext().getJobExecutionContext();
                        log.info("jobExecutionContext memory {}, {}, {}", jobExecutionContext, jobExecutionContext2, jobExecutionContext3);

                        ExecutionContext stepExecutionContext2 = chunkContext.getStepContext().getStepExecution().getExecutionContext();
                        Map<String, Object> stepExecutionContext3 = chunkContext.getStepContext().getStepExecutionContext();
                        log.info("stepExecutionContext memory {}, {}, {}", stepExecutionContext, stepExecutionContext2, stepExecutionContext3);

                        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
                        String stepName = chunkContext.getStepContext().getStepExecution().getStepName();

                        if(jobExecutionContext.get("jobName") == null){
                            jobExecutionContext.put("jobName", jobName);
                        }

                        if(stepExecutionContext.get("stepName") == null){
                            stepExecutionContext.put("stepName", stepName);
                        }

                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
//    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 has executed");
                    Object jobName = contribution.getStepExecution().getJobExecution().getExecutionContext().get("jobName");
                    Object stepName = contribution.getStepExecution().getExecutionContext().get("stepName");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
//    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step3 has executed");
                    Object name = contribution.getStepExecution().getJobExecution().getExecutionContext().get("name");
                    if(name == null){
                        contribution.getStepExecution().getJobExecution().getExecutionContext().put("name", "newJobName");
                        throw new IllegalStateException("job name not exist");
                    }

                    return RepeatStatus.FINISHED;
                })
                .build();
    }
//    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step4 has executed");
                    Object name = contribution.getStepExecution().getJobExecution().getExecutionContext().get("name");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
