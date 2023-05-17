package com.bilgeadam.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    // auth-microservisde olusturdugumuz kuyruğu user microservisde de
    // confıguration ayarları ile beraber olusturyoruz:
    @Value("${rabbitmq.queueRegister}")
    private String queueNameRegister;

    @Bean
    Queue registerQueue(){
        return new Queue(queueNameRegister);
    }

//    @Value("${rabbitmq.queueregisterelastic}")
//    private String elasticRegisterQueue;
//    @Value("${rabbitmq.elasticregisterkey}")
//    private String elasticRegisterBindingKey;
//    @Value("${rabbitmq.exchange-user}")
//    private String exchange;

//    @Bean
//    Queue registerQueueElastic(){
//        return new Queue(elasticRegisterQueue);
//    }
//    @Bean
//    DirectExchange excahangeUser(){
//
//        return new DirectExchange(exchange);
//    }
//
//    @Bean
//    public Binding bindingRegisterElastic(final Queue registerQueueElastic, final DirectExchange excahangeUser){
//
//        return BindingBuilder.bind(registerQueueElastic).to(excahangeUser).with(elasticRegisterBindingKey);
//    }
}
