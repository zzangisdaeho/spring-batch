package io.springbatch.springbatch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "io.springbatch.springbatch.api",
        entityManagerFactoryRef = "apiEntityManagerFactory",
        transactionManagerRef = "apiTransactionManager"
)
@RequiredArgsConstructor
public class ApiJpaConfiguration {

    private final Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean apiEntityManagerFactory(
            @Qualifier("apiDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
//        properties.put("hibernate.physical_naming_strategy", CustomJpaPhysicalNamingStrategy.class.getName());
        return builder
                .dataSource(dataSource)
                //Entity scan package
                .packages("io.springbatch.springbatch.api")
                .properties(properties)
                .build();
    }

    @Bean
    public PlatformTransactionManager apiTransactionManager(
            @Qualifier("apiEntityManagerFactory") LocalContainerEntityManagerFactoryBean apiEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(apiEntityManagerFactory.getObject()));
    }

}
