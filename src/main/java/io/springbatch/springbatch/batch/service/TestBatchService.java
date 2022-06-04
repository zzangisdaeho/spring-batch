package io.springbatch.springbatch.batch.service;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.api.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class TestBatchService {

    private final CompanyRepository companyRepository;

    @Transactional(transactionManager = "apiTransactionManager")
    public CompanyEntity update(Long companySeq) {

        CompanyEntity companyEntity = companyRepository.findById(companySeq).get();

        if (companyEntity.getCompanySeq() % 3 == 0) {
            throw new IllegalStateException("3번째 컴페니는 통과할 수 없다!");
        }

        companyEntity.setUpdateTime(ZonedDateTime.now());
        companyEntity.getMembers().forEach(member -> {
            member.setMemberName(member.getMemberName() + "_test");
            member.setUpdateTime(ZonedDateTime.now());
        });

        return companyEntity;
    }
}
