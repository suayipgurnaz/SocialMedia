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
    // kuyruk oluşturmak için 3 şey lazım: kuyruk ismi, exchange(tüm kuyruklarda kullanılabilir), bindingKey
    // bu degerleri .yml'dan çekiyoruz:
    @Value("${rabbitmq.exchange-auth}")
    private String exchange;  // bu exchange uzerinden...
    @Value("${rabbitmq.registerkey}")
    private String registerBindingKey; // bu key'i kullanarak
    @Value("${rabbitmq.queueRegister}")
    private String queueNameRegister;  // (kuyruk ismi) bu kuyruga baglayacagiz

    // mail için kuyruk parametreleri oluşturuyoruz:
    @Value("${rabbitmq.registermailkey}")
    private String registerMailBingingKey;
    @Value("${rabbitmq.registermailqueue}")
    private String registerMailQueue;  // (kuyruk ismi)


    /**  Exchange oluşturalım:  */
    @Bean // singleton yapıda, altta donen DirectExchange'i yonetmek için bean ekliyoruz
    DirectExchange exchangeAuth(){
        return new DirectExchange(exchange);
    }

    /**  Queue oluşturalım:  */
    @Bean
    Queue registerQueue(){
        return new Queue(queueNameRegister);
    }

    // mail için queue oluşturalım:
    @Bean
    Queue registerMailQueue(){
        return new Queue(registerMailQueue);
    }


    /**  registerQueue ile exchangeAuth'u, registerBindingKey kullanarak bağlayalım:  */
    @Bean
    public Binding bindingRegister(final Queue registerQueue, final DirectExchange exchangeAuth){
        return BindingBuilder.bind(registerQueue).to(exchangeAuth).with(registerBindingKey);
    }

    // mail kuyrugu icin binding olusturalım:
    @Bean
    public Binding bindingRegisterMail(final Queue registerMailQueue, final DirectExchange exchangeAuth){
        return BindingBuilder.bind(registerMailQueue).to(exchangeAuth).with(registerMailBingingKey);
    }
}






