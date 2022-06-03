package io.springbatch.springbatch.batch.job.payment;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.batch.job.listener.CompanyJobListener;
import io.springbatch.springbatch.batch.job.listener.CompanySkipListener;
import io.springbatch.springbatch.batch.service.PaymentBatchService;
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
public class PaymentJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PlatformTransactionManager apiTransactionManager;

    private final EntityManagerFactory emf;

    private final CompanySkipListener skipListener;

    private final PaymentBatchService paymentBatchService;

    @Bean
    public Job paymentJob() {
        return jobBuilderFactory.get("paymentJob")
                .start(paymentStep())
                .listener(new CompanyJobListener())
                .build();
    }
    @Bean
    public Step paymentStep() {

        return stepBuilderFactory.get("paymentStep")
                .<CompanyEntity, CompanyEntity>chunk(1)
                .reader(paymentReader())
                .processor(paymentProcessor())
                .writer(paymentWriter())
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

//    @Bean
    private ItemReader<CompanyEntity> paymentReader() {
        return new JpaPagingItemReaderBuilder<CompanyEntity>()
                .name("companyReader")
                .entityManagerFactory(emf)
                .pageSize(1)
                .queryString("select c from CompanyEntity c order by c.companySeq")
                .build();
    }

//    @Bean
    private ItemProcessor<CompanyEntity, CompanyEntity> paymentProcessor(){
        return item -> {
            log.info("company Seq : {}", item.getCompanySeq());

            Thread.sleep(1000);
            paymentBatchService.update(item);

            return item;
        };
    }

//    @Bean
    private ItemWriter<CompanyEntity> paymentWriter() {

        return items -> {
            log.info("=============================Writer Result========================");
            items.forEach(company -> log.info("{} : {} : {}",company.getCompanySeq(), company.getCompanyName(), company.getUpdateTime()));
            log.info("=============================Writer Result========================");
        };
    }
}
