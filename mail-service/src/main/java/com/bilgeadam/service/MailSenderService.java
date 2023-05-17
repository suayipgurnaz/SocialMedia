package com.bilgeadam.service;

import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender javaMailSender;
    public void sendMail(RegisterMailModel model){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom("${java6mailusername}"); // e-mail gonderen adres
        mailMessage.setTo(model.getEmail()); // mail'i hangi adrese gonderecegim
        mailMessage.setSubject("AKTİVASYON KODUNUZ...."); // mail konusu
        // mail içerigi:
        mailMessage.setText(model.getUsername()+" adıyla başarılı bir şekilde kayıt oldunuz\n"
                +"Aktivasyon kodunuz: "+model.getActivationCode());
        javaMailSender.send(mailMessage);
    }
}
