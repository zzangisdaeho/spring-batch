package io.springbatch.springbatch.batch.job.process;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.batch.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemProcessorTest implements ItemProcessor<CompanyEntity, CompanyEntity> {

    private final TestService testService;

    @Override
    public CompanyEntity process(CompanyEntity item) throws Exception {

        log.info("company Seq : {}", item.getCompanySeq());

        testService.update(item);

        return item;
    }
}
