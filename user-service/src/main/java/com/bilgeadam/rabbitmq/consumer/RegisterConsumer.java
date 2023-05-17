package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j// console a log info çıktısı vermek için kullanılan kutuphane
public class RegisterConsumer {
    private final UserProfileService userProfileService;

    /**  auth-service'de oluşturdugum queue'yu burada dinliyorum, model yakalıyorum:   */
    @RabbitListener(queues = ("${rabbitmq.queueRegister}")) // .yml'dan cektik
    public void newUserCreate(RegisterModel model){
        log.info("User {}",model.toString());
        userProfileService.createUserWithRabbitMq(model);
       // userProfileService.createUser(IUserMapper.INSTANCE.toNewCreateUserRequestDto(model)); 2.tercih
    }
}






