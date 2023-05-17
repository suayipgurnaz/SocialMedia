package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import com.bilgeadam.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterMailConsumer {
    private final MailSenderService mailSenderService;

    // application.yml'da adı verilen kuyrugu dinleyecek:
    @RabbitListener(queues = "${rabbitmq.registermailqueue}")
    public void sendActivationCode(RegisterMailModel model){
        log.info("Model {}",model.toString());
        mailSenderService.sendMail(model);
    }
}






