package io.springbatch.springbatch.batch.service;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class TestService {

    @Transactional(transactionManager = "apiTransactionManager")
    public void update(CompanyEntity companyEntity){
        if(companyEntity.getCompanySeq() % 5 == 0){
            throw new IllegalStateException("5번째 컴페니는 통과할 수 없다!");
        }

        companyEntity.setUpdateTime(ZonedDateTime.now());
        companyEntity.getMembers().forEach(member -> member.setUpdateTime(ZonedDateTime.now()));
    }
}
