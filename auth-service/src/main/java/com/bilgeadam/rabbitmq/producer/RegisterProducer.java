package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterProducer {
    // daha once .yml'a ekledigimiz degerleri cekiyoruz:
    @Value("${rabbitmq.exchange-auth}")
    private String directExchange;
    @Value("${rabbitmq.registerkey}")
    private String registerBindingKey;

    /**   Dışarıdan aldıgımız veriyi template uzerinden kuyruga verecegiz: */
    private final RabbitTemplate rabbitTemplate;

    /**   Kuyruga ekleyecegim veriyi gonderiyoruz:  */
    public void sendNewUser(RegisterModel model){
        rabbitTemplate.convertAndSend(directExchange,registerBindingKey,model);
    }
}
