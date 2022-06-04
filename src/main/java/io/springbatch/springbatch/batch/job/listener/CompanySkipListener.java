package io.springbatch.springbatch.batch.job.listener;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import io.springbatch.springbatch.batch.job.dto.CompanyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@StepScope
@Slf4j
public class CompanySkipListener implements SkipListener<CompanyDto, CompanyEntity> {

    @Value("#{jobExecutionContext['failCompanySet']}")
    private Set<Long> failCompanySet;

    @Override
    public void onSkipInRead(Throwable t) {
        t.printStackTrace();
        System.out.println(">> onSkipInRead : "+ t.getMessage());
    }


    @Override
    public void onSkipInWrite(CompanyEntity item, Throwable t) {
        System.out.println(">> onSkipInWrite : "+ item);
//		System.out.println(">> onSkipInWrite : "+ t.getMessage());
    }

    @Override
    public void onSkipInProcess(CompanyDto item, Throwable t) {
        System.out.println(">> onSkipInProcess : "+ item.getCompanySeq());
        failCompanySet.add(item.getCompanySeq());
        System.out.println(">> onSkipInProcess : "+ t.getMessage());
    }
}