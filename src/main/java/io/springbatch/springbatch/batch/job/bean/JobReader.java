package io.springbatch.springbatch.batch.job.bean;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class JobReader {

    private final EntityManagerFactory emf;

    @Bean
    public ItemReader<CompanyEntity> companyItemReader() {
        return new JpaPagingItemReaderBuilder<CompanyEntity>()
                .name("companyReader")
                .entityManagerFactory(emf)
                .pageSize(1)
                .queryString("select c from CompanyEntity c order by c.companySeq")
                .build();
    }
}
