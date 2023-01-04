package io.springbatch.springbatch.quartz.jobs;

import lombok.Getter;

@Getter
public enum JobList {

    TEST(TestScheduleJob.class);

    private final Class<?> clazz;

    JobList(Class<?> clazz) {
        this.clazz = clazz;
    }
}
