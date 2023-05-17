package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterMailProducer {
    // daha once .yml'a ekledigimiz degerleri cekiyoruz:
    @Value("${rabbitmq.exchange-auth}")
    private String directExchange;
    @Value("${rabbitmq.registermailkey}")
    private String registerMailBindingKey;

    /**   Dışarıdan aldıgımız veriyi template uzerinden kuyruga verecegiz: */
    private final RabbitTemplate rabbitTemplate;

    /**   Kuyruga ekleyecegim veriyi gonderiyoruz:  */
    public void sendActivationCode(RegisterMailModel model){
        rabbitTemplate.convertAndSend(directExchange,registerMailBindingKey,model);
    }
}
