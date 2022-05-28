package io.springbatch.springbatch.quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class QuartzRegister {

    private final QuartzService quartzService;

    @PostConstruct
    public void scheduler() {
        quartzService.addCronJob(TestScheduleJob.class, "testJob2", "test", "Quartz 잡 테스트",null , "0/10 * * * * ?");
    }
}
