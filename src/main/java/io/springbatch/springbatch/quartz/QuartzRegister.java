package io.springbatch.springbatch.quartz;

import io.springbatch.springbatch.quartz.schedule.BatchJobScheduler;
import io.springbatch.springbatch.quartz.schedule.TestScheduleJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class QuartzRegister {

    private final QuartzService quartzService;

    @PostConstruct
    public void scheduler() {
        Map<String, Integer> param1 = new HashMap<>();
        param1.put("testNum", 1);

        Map<String, Integer> param2 = new HashMap<>();
        param2.put("testNum", 2);
//        quartzService.addCronJob(TestScheduleJob.class, "testJob1", "test", "Quartz 잡 테스트1", param1, "0/3 * * * * ?");
//        quartzService.addCronJob(TestScheduleJob.class, "testJob2", "test", "Quartz 잡 테스트2", param2, "0/5 * * * * ?");
        quartzService.addCronJob(BatchJobScheduler.class, "testBatch", "test", "Batch Quartz 연동 테스트", null, "0 0/1 * 1/1 * ? *");
//        quartzService.addSimpleJob(BatchJobScheduler.class, "testBatch", "test", "Batch Quartz 연동 테스트", null, 120);
    }
}
