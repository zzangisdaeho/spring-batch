package io.springbatch.springbatch.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.springbatch.springbatch.quartz.jobs.JobList;
import io.springbatch.springbatch.quartz.jobs.TestScheduleJob;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerRegisterRequestDtoTest {

    @SneakyThrows
    @Test
    public void jsonPrint(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


        SchedulerRegisterRequestDto build = SchedulerRegisterRequestDto.builder()
                .job(JobList.TEST)
                .desc("desc")
                .type(SchedulerType.TIME)
                .group("group")
                .name("name")
                .time(ZonedDateTime.now())
                .params(Map.of("param1", "value1", "param2", "value2"))
                .build();

        String jsonString = objectMapper.writeValueAsString(build);

        System.out.println("jsonString = " + jsonString);
    }

    @Test
    public void getClassTest() throws ClassNotFoundException {
        Class<TestScheduleJob> testScheduleJobClass = TestScheduleJob.class;
        String prefix = "io.springbatch.springbatch.quartz.jobs.";
        Class<?> aClass = Class.forName(prefix + "TestScheduleJob");

    }

}