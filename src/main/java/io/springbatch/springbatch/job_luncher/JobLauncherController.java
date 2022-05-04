package io.springbatch.springbatch.job_luncher;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobLauncherController {

    private final JobLauncherDrive launcherDrive;

    @PostMapping("/launch/sync/{param}")
    public String launchSync(@PathVariable String param){

        JobExecution jobExecution = launcherDrive.launchSync(param);
        return "sync " + jobExecution.getExitStatus();
    }

    @PostMapping("/launch/async/{param}")
    public String launchAsync(@PathVariable String param){

        JobExecution jobExecution = launcherDrive.launchAsync(param);
        return "async " + jobExecution.getExitStatus();
    }
}
