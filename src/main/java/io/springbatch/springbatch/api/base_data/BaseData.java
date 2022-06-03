package io.springbatch.springbatch.api.base_data;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.api.entity.MemberEntity;
import io.springbatch.springbatch.api.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseData {

    private final CompanyRepository companyRepository;


    @PostConstruct
    @Transactional
    public void addData() throws InterruptedException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        for (int i = 0; i < 100; i++) {
            CompanyEntity company = CompanyEntity.builder().companyName("company" + i).build();
            for (int j = 0; j < 10; j++) {
                MemberEntity member = MemberEntity.builder().memberName("name" + i*10+j).build();
                company.addMember(member);
            }
            companyRepository.save(company);
        }

    }

}
