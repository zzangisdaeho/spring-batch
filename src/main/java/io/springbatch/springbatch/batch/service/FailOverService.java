package io.springbatch.springbatch.batch.service;

import io.springbatch.springbatch.api.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FailOverService {

    private final CompanyRepository companyRepository;
    private final VacationBatchService testService;

    public void failOver(long companySeq) {
        testService.update(companyRepository.findById(companySeq).get());
    }

}
