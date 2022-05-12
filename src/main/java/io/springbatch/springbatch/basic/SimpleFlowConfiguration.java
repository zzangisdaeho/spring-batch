package io.springbatch.springbatch.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SimpleFlowConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleFlowJob() {
        return jobBuilderFactory.get("simpleFlowJob")
                .start(flow1())
                    .on("COMPLETED")
                    .to(flow2())
                .from(flow1())
                    .on("FAILED")
                    .to(flow3())
                .end()
                .build();
    }
    @Bean
    public Flow flow1() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow1");

        flowBuilder.start(simpleFlowStep1())
                .next(simpleFlowStep2())
                .end();

        return flowBuilder.build();
    }

    @Bean
    public Flow flow2() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow2");

        flowBuilder.start(flow3())
                .next(simpleFlowStep5())
                .next(simpleFlowStep6())
                .end();

        return flowBuilder.build();
    }

    @Bean
    public Flow flow3() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow3");

        flowBuilder.start(simpleFlowStep3())
                .next(simpleFlowStep4())
                .end();

        return flowBuilder.build();
    }
    @Bean
    public Step simpleFlowStep1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step1 has executed");
//                    throw new RuntimeException("step1 was failed");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step simpleFlowStep2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step2 has executed");
                    throw new IllegalStateException("step2 error occur");
//                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step simpleFlowStep3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step3 has executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step simpleFlowStep4() {
        return stepBuilderFactory.get("step4")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step4 has executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step simpleFlowStep5() {
        return stepBuilderFactory.get("step5")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step5 has executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step simpleFlowStep6() {
        return stepBuilderFactory.get("step6")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step6 has executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
