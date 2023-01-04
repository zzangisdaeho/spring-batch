package io.springbatch.springbatch.rabbit.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.springbatch.springbatch.dto.SchedulerRegisterRequestDto;
import io.springbatch.springbatch.quartz.QuartzService;
import io.springbatch.springbatch.quartz.jobs.TestScheduleJob;
import io.springbatch.springbatch.rabbit.ScheduleRabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduleRabbitConsumer {

    private final ObjectMapper objectMapper;
    private final QuartzService quartzService;

    @RabbitListener(queues = ScheduleRabbitConfig.QUEUE_NAME)
    public void receiveDispatch(Message message){
        String payload = new String(message.getBody());
        try {
            SchedulerRegisterRequestDto schedulerRegisterRequestDto = objectMapper.readValue(payload, SchedulerRegisterRequestDto.class);

            quartzService.addSimpleJob(
                    schedulerRegisterRequestDto.getJob().getClazz(),
                    schedulerRegisterRequestDto.getName(),
                    schedulerRegisterRequestDto.getGroup(),
                    schedulerRegisterRequestDto.getDesc(),
                    schedulerRegisterRequestDto.getParams(),
                    schedulerRegisterRequestDto.getTime()
            );


        } catch (JsonProcessingException e) {
            log.error("wrong message form. received Message : \n{} \n error : {}", payload, e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            log.error("message handling fail " + e);
        }
    }
}
