package io.springbatch.springbatch.batch.job;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.batch.job.listener.CompanyJobListener;
import io.springbatch.springbatch.batch.job.listener.CustomSkipListener;
import io.springbatch.springbatch.batch.job.process.ItemProcessorTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final PlatformTransactionManager apiTransactionManager;

    private final ItemProcessorTest itemProcessorTest;

    private final CustomSkipListener skipListener;

    @Bean
    public Job companyJob() {
        return jobBuilderFactory.get("companyJob")
                .incrementer(new RunIdIncrementer())
                .start(companyStep())
                .listener(new CompanyJobListener())
                .build();
    }
    @Bean
    public Step companyStep() {

        return stepBuilderFactory.get("companyStep")
                .<CompanyEntity, CompanyEntity>chunk(1)
                .reader(CompanyItemReader())
                .processor(itemProcessorTest)
                .writer(CompanyItemWriter())
                .faultTolerant()
                .retry(RuntimeException.class)
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

    @Bean
    public ItemReader<? extends CompanyEntity> CompanyItemReader() {
        return new JpaPagingItemReaderBuilder<CompanyEntity>()
                .name("companyReader")
                .entityManagerFactory(emf)
                .pageSize(1)
                .queryString("select c from CompanyEntity c")
                .build();
    }

    @Bean
    public ItemWriter<? super CompanyEntity> CompanyItemWriter() {
        return new JpaItemWriterBuilder<CompanyEntity>()
                .entityManagerFactory(emf)
                .build();
    }
}
