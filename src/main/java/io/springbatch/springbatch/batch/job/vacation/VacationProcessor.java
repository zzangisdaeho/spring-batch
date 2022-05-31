package io.springbatch.springbatch.batch.job.vacation;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.batch.service.VacationBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VacationProcessor implements ItemProcessor<CompanyEntity, CompanyEntity> {

    private final VacationBatchService vacationBatchService;

    @Override
    public CompanyEntity process(CompanyEntity item) throws Exception {

        log.info("company Seq : {}", item.getCompanySeq());

        vacationBatchService.update(item);

        return item;
    }
}
