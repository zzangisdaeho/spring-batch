package io.springbatch.springbatch.dto;

import io.springbatch.springbatch.quartz.jobs.JobList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerRegisterRequestDto {

    private JobList job;
    private SchedulerType type;
    private String name;
    private String group;
    private Map<String, String> params;
    private String desc;
    private ZonedDateTime time;
}
