package io.springbatch.springbatch.batch.service;

import io.springbatch.springbatch.api.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FailOverService {

    private final CompanyRepository companyRepository;
    private final TestService testService;

    public void failOver(long companySeq) throws Exception {
        testService.update(companyRepository.findById(companySeq).get());
    }

}
