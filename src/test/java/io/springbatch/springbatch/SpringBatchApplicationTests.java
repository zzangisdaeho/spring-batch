package io.springbatch.springbatch;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.api.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest(properties = "spring.config.location=classpath:/application.yml, classpath:/dataSources.yml")
@ActiveProfiles("mysql")
@Commit
@Transactional(transactionManager = "apiTransactionManager")
class SpringBatchApplicationTests {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private List<PlatformTransactionManager> managerList;

    @Autowired
    EntityManager entityManager;

    @Test
    void save() {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setCompanyName("insert during batch");
        companyEntity.setUpdateTime(ZonedDateTime.now());
        companyRepository.save(companyEntity);
    }

    @Test
    void update() {
        CompanyEntity companyEntity = companyRepository.findById(23L).get();
        companyEntity.setCompanyName("update during batch" + ZonedDateTime.now());
    }

    @Test
    void read(){
        List<CompanyEntity> all = companyRepository.findAll();
        System.out.println("all.size() = " + all.size());

        all.forEach(each -> each.setUpdateTime(ZonedDateTime.now()));
    }

}
