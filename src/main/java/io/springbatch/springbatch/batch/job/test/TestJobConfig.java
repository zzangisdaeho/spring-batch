package io.springbatch.springbatch.batch.job.test;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.batch.job.dto.CompanyDto;
import io.springbatch.springbatch.batch.job.listener.CompanyJobListener;
import io.springbatch.springbatch.batch.job.listener.CompanySkipListener;
import io.springbatch.springbatch.batch.service.TestBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.retry.backoff.FixedBackOffPolicy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TestJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final CompanySkipListener skipListener;

    private final TestBatchService testBatchService;

    @Qualifier("apiDataSource")
    @Autowired
    private final DataSource apiDataSource;

    @Bean
    public Job testJob() throws Exception {
        return jobBuilderFactory.get("testJob")
                .start(testStep())
                .listener(new CompanyJobListener())
                .build();
    }
    @Bean
    public Step testStep() throws Exception {

        return stepBuilderFactory.get("testStep")
                .<CompanyDto, CompanyEntity>chunk(1)
                .reader(testReader())
                .processor(testProcessor())
                .writer(testWriter())
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
    public JdbcPagingItemReader<CompanyDto> testReader() throws Exception {

        return new JdbcPagingItemReaderBuilder<CompanyDto>()
                .name("testItemReader")
                .pageSize(1)
                .dataSource(apiDataSource)
                .rowMapper(new BeanPropertyRowMapper<>(CompanyDto.class))
                .queryProvider(testQueryProvider())
                .build();
    }

    @Bean
    public PagingQueryProvider testQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(apiDataSource);
//        queryProvider.setSelectClause("company_seq, company_name");
//        queryProvider.setFromClause("from company_entity");
        queryProvider.setSelectClause("companySeq, companyName");
        queryProvider.setFromClause("from CompanyEntity");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("companySeq", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    @Bean
    public ItemProcessor<CompanyDto, CompanyEntity> testProcessor(){
        return item -> {
            log.info("company Seq : {}", item.getCompanySeq());

            return testBatchService.update(item);
        };
    }

    @Bean
    public ItemWriter<CompanyEntity> testWriter() {

        return items -> {
            log.info("=============================Writer Result========================");
            items.forEach(company -> log.info("{} : {} : {}",company.getCompanySeq(), company.getCompanyName(), company.getUpdateTime()));
        };
    }
}