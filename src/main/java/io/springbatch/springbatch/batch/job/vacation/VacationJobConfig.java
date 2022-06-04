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
public class VacationJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final CompanySkipListener skipListener;

    private final VacationBatchService vacationBatchService;

    @Qualifier("apiDataSource")
    @Autowired
    private final DataSource apiDataSource;

    @Bean
    public Job vacationJob() throws Exception {
        return jobBuilderFactory.get("vacationJob")
                .start(vacationStep())
                .listener(new CompanyJobListener())
                .build();
    }
    @Bean
    public Step vacationStep() throws Exception {

        return stepBuilderFactory.get("vacationStep")
                .<Long, CompanyEntity>chunk(1)
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

//    @Bean
//    public ItemReader<CompanyEntity> vacationReader() {
//        return new JpaPagingItemReaderBuilder<CompanyEntity>()
//                .name("companyReader")
//                .entityManagerFactory(emf)
//                .pageSize(1)
//                .queryString("select c from CompanyEntity c order by c.companySeq")
//                .build();
//    }

    @Bean
    public JdbcPagingItemReader<Long> vacationReader() throws Exception {

        return new JdbcPagingItemReaderBuilder<Long>()
                .name("vacationItemReader")
                .pageSize(1)
                .dataSource(apiDataSource)
                .rowMapper(new SingleColumnRowMapper<>())
                .queryProvider(vacationQueryProvider())
                .build();
    }

    @Bean
    public PagingQueryProvider vacationQueryProvider() throws Exception {
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
    public ItemProcessor<Long, CompanyEntity> vacationProcessor(){
        return item -> {
//            Thread.sleep(1000);
            log.info("company Seq : {}", item);
            return vacationBatchService.update(item);
        };
    }

    @Bean
    public ItemWriter<CompanyEntity> vacationWriter() {

        return items -> {
            log.info("=============================Writer Result========================");
            items.forEach(company -> log.info("{} : {} : {}",company.getCompanySeq(), company.getCompanyName(), company.getUpdateTime()));
        };
    }
}
