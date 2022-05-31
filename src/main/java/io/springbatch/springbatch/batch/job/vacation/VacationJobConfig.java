package io.springbatch.springbatch.batch.job.vacation;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.batch.job.listener.CompanyJobListener;
import io.springbatch.springbatch.batch.job.listener.CompanySkipListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class VacationJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final PlatformTransactionManager apiTransactionManager;

    private final ItemReader<CompanyEntity> companyItemReader;
    private final ItemWriter<CompanyEntity> companyItemWriter;

    private final VacationProcessor itemProcessorTest;

    private final CompanySkipListener skipListener;

    @Bean
    public Job vacationJob() {
        return jobBuilderFactory.get("vacationJob")
                .start(vacationStep())
                .listener(new CompanyJobListener())
                .build();
    }
    @Bean
    public Step vacationStep() {

        return stepBuilderFactory.get("vacationStep")
                .<CompanyEntity, CompanyEntity>chunk(1)
                .reader(companyItemReader)
                .processor(itemProcessorTest)
                .writer(companyItemWriter)
                .faultTolerant()
                .retry(TransientDataAccessException.class)
                .retryLimit(3)
//                .backOffPolicy(getFixedBackOffPolicy())
                .skip(RuntimeException.class)
                .skipLimit(Integer.MAX_VALUE)
                .listener(skipListener)
                .transactionManager(apiTransactionManager)
                .build();
    }


    private FixedBackOffPolicy getFixedBackOffPolicy() {
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000); //지정한 시간만큼 대기후 재시도 한다.
        return backOffPolicy;
    }
}
