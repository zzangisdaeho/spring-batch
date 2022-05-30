package io.springbatch.springbatch.batch.job.listener;

import io.springbatch.springbatch.api.entity.CompanyEntity;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@StepScope
public class CustomSkipListener implements SkipListener<CompanyEntity, CompanyEntity> {

    @Value("#{jobExecutionContext['failCompanySet']}")
    private Set failCompanySet;

    @Override
    public void onSkipInRead(Throwable t) {
        System.out.println(">> onSkipInRead : "+ t.getMessage());
    }

    @Override
    public void onSkipInWrite(CompanyEntity item, Throwable t) {
        System.out.println(">> onSkipInWrite : "+ item);
//		System.out.println(">> onSkipInWrite : "+ t.getMessage());
    }

    @Override
    public void onSkipInProcess(CompanyEntity item, Throwable t) {
        System.out.println(">> onSkipInProcess : "+ item.getCompanySeq());
        failCompanySet.add(item.getCompanySeq());
        System.out.println(">> onSkipInProcess : "+ t.getMessage());
    }
}