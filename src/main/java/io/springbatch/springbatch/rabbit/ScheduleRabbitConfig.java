package io.springbatch.springbatch.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ScheduleRabbitConfig {

    public static final String EXCHANGE_NAME = "ws.exchange-topic.schedule.v0";
    public static final String QUEUE_NAME = "ws.queue.schedule.v0";
    public static final String ROUTING_KEY = "ws.schedule.cmd.v0";


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding binding (Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter(null));
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public MessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper){
//        return new Jackson2JsonMessageConverter(objectMapper);
//    }
}
