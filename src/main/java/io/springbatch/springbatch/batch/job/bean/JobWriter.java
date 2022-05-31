package io.springbatch.springbatch.batch.job.bean;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobWriter {

    @Bean
    public ItemWriter<CompanyEntity> companyItemWriter() {

        return (ItemWriter<CompanyEntity>) items -> {
            log.info("=============================Writer Result========================");
            items.forEach(company -> log.info("{} : {} : {}",company.getCompanySeq(), company.getCompanyName(), company.getUpdateTime()));
            log.info("=============================Writer Result========================");
        };

    }
}
