package io.springbatch.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchApplication {

    public static void main(String[] args) {

        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(SpringBatchApplication.class);
        springApplicationBuilder.properties("spring.config.location=" + "classpath:/application.yml" + ", classpath:/dataSources.yml" );
        SpringApplication springApplication = springApplicationBuilder.build();

        springApplication.run(args);
    }

}
