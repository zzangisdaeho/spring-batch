package io.springbatch.springbatch.quartz;

import io.springbatch.springbatch.quartz.schedule.PaymentJobSchedule;
import io.springbatch.springbatch.quartz.schedule.VacationJobSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class QuartzRegister {

    private final QuartzService quartzService;

    @PostConstruct
    public void scheduler() {
        quartzService.addCronJob(VacationJobSchedule.class, "vacationBatch", "docswave_batch", "docswave 휴가 부여 테스트", null, "0 0/1 * 1/1 * ? *");
        quartzService.addCronJob(PaymentJobSchedule.class, "paymentBatch", "docswave_batch", "docswave 결제 테스트", null, "0 0/1 * 1/1 * ? *");
    }
}
