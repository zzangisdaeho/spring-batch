package io.springbatch.springbatch.rabbit.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleRabbitProducer {

    private final RabbitTemplate rabbitTemplate;


    public void sendMessageToMail(Object payload){


        rabbitTemplate.convertAndSend("ns.exchange-topic.mail.v0", "ns.mail.cmd.v0", payload);

//        try {
//            rabbitTemplate.convertAndSend("ns.exchange-topic.mail.v0", "ns.mail.cmd.v0", objectMapper.writeValueAsString(payload));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
    }

    public void sendMessageToWebNotification(Object payload){
        rabbitTemplate.convertAndSend("ns.exchange-fanout.wn.v0", "", payload);
//        try {
//            rabbitTemplate.convertAndSend("ns.exchange-fanout.wn.v0", "", objectMapper.writeValueAsString(payload));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
    }
}
