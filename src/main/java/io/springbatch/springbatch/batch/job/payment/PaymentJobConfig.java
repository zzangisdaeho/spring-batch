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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.retry.backoff.FixedBackOffPolicy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class PaymentJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final CompanySkipListener skipListener;

    private final PaymentBatchService paymentBatchService;

    @Qualifier("apiDataSource")
    @Autowired
    private final DataSource apiDataSource;

    @Bean
    public Job paymentJob() throws Exception {
        return jobBuilderFactory.get("paymentJob")
                .start(paymentStep())
                .listener(new CompanyJobListener())
                .build();
    }
    @Bean
    public Step paymentStep() throws Exception {

        return stepBuilderFactory.get("paymentStep")
                .<Long, CompanyEntity>chunk(1)
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
//                .transactionManager(apiTransactionManager)
                .build();
    }


    private FixedBackOffPolicy getFixedBackOffPolicy() {
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000); //지정한 시간만큼 대기후 재시도 한다.
        return backOffPolicy;
    }

//    @Bean
//    public ItemReader<CompanyEntity> paymentReader() {
//        return new JpaPagingItemReaderBuilder<CompanyEntity>()
//                .name("companyReader")
//                .entityManagerFactory(emf)
//                .pageSize(1)
//                .queryString("select c from CompanyEntity c order by c.companySeq")
//                .build();
//    }
@Bean
public JdbcPagingItemReader<Long> paymentReader() throws Exception {

    return new JdbcPagingItemReaderBuilder<Long>()
            .name("testItemReader")
            .pageSize(1)
            .dataSource(apiDataSource)
            .rowMapper(new SingleColumnRowMapper<>())
            .queryProvider(paymentQueryProvider())
            .build();
}

    @Bean
    public PagingQueryProvider paymentQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(apiDataSource);
//        queryProvider.setSelectClause("company_seq, company_name");
//        queryProvider.setFromClause("from company_entity");
        queryProvider.setSelectClause("companySeq");
        queryProvider.setFromClause("from CompanyEntity");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("companySeq", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }


    @Bean
    public ItemProcessor<Long, CompanyEntity> paymentProcessor(){
        return item -> {
            log.info("company Seq : {}", item);


//            Thread.sleep(1000);
            return paymentBatchService.update(item);
        };
    }

    @Bean
    public ItemWriter<CompanyEntity> paymentWriter() {

        return items -> {
            log.info("=============================Writer Result========================");
            items.forEach(company -> log.info("{} : {} : {}",company.getCompanySeq(), company.getCompanyName(), company.getUpdateTime()));
        };
    }
}
