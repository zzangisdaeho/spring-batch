package io.springbatch.springbatch.quartz;

import io.springbatch.springbatch.quartz.jobs.TestScheduleJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class QuartzRegister {

    private final QuartzService quartzService;

//    @PostConstruct
    public void scheduler() {
        Map<String, Integer> param1 = new HashMap<>();
        param1.put("testNum", 1);

        Map<String, Integer> param2 = new HashMap<>();
        param2.put("testNum", 2);

        Map<String, Integer> param3 = new HashMap<>();
        param3.put("testNum", 3);
        quartzService.addCronJob(TestScheduleJob.class, "testJob1", "test", "Quartz 잡 테스트1", param1, "0/3 * * * * ?");
        quartzService.addCronJob(TestScheduleJob.class, "testJob2", "test", "Quartz 잡 테스트2", param2, "0/5 * * * * ?");
        quartzService.addCronJob(TestScheduleJob.class, "testJob3", "test", "Quartz 잡 테스트3", param3, "0/7 * * * * ?");
        quartzService.addCronJob(TestScheduleJob.class, "testJob3", "test", "Quartz 잡 테스트3", param3, "0/7 * * * * ?");
    }
}
