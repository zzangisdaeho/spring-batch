package io.springbatch.springbatch.batch.job.payment;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.batch.service.PaymentBatchService;
import io.springbatch.springbatch.batch.service.VacationBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessor implements ItemProcessor<CompanyEntity, CompanyEntity> {

    private final PaymentBatchService paymentBatchService;

    @Override
    public CompanyEntity process(CompanyEntity item) throws Exception {

        log.info("company Seq : {}", item.getCompanySeq());

        paymentBatchService.update(item);

        return item;
    }
}
