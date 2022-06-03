package io.springbatch.springbatch.batch.job.vacation;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.batch.job.listener.CompanyJobListener;
import io.springbatch.springbatch.batch.job.listener.CompanySkipListener;
import io.springbatch.springbatch.batch.service.VacationBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
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

    private final CompanySkipListener skipListener;
    private final EntityManagerFactory emf;

    private final VacationBatchService vacationBatchService;

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
                .reader(vacationReader())
                .processor(vacationProcessor())
                .writer(vacationWriter())
                .faultTolerant()
                .retry(TransientDataAccessException.class)
                .retryLimit(3)
//                .backOffPolicy(getFixedBackOffPolicy())
                .skip(RuntimeException.class)
                .skipLimit(Integer.MAX_VALUE)
                .listener(skipListener)
//                .transactionManager(apiTransactionManager)
                .build();
    }


    private FixedBackOffPolicy getFixedBackOffPolicy() {
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000); //지정한 시간만큼 대기후 재시도 한다.
        return backOffPolicy;
    }

    @Bean
    public ItemReader<CompanyEntity> vacationReader() {
        return new JpaPagingItemReaderBuilder<CompanyEntity>()
                .name("companyReader")
                .entityManagerFactory(emf)
                .pageSize(1)
                .queryString("select c from CompanyEntity c order by c.companySeq")
                .build();
    }

    @Bean
    public ItemProcessor<CompanyEntity, CompanyEntity> vacationProcessor(){
        return item -> {
//            Thread.sleep(1000);
            log.info("company Seq : {}", item.getCompanySeq());
            vacationBatchService.update(item);
            return item;
        };
    }

    @Bean
    public ItemWriter<CompanyEntity> vacationWriter() {

        return items -> {
            log.info("=============================Writer Result========================");
            items.forEach(company -> log.info("{} : {} : {}",company.getCompanySeq(), company.getCompanyName(), company.getUpdateTime()));
            log.info("=============================Writer Result========================");
        };
    }
}
