package com.bilgeadam.config.redis;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration // once configuration class'ları ayaga kalkar
@EnableCaching
@EnableRedisRepositories
public class RedisConfiguration {
    @Bean // bu methodda yazdıgım şey uygulama ayaga kalkarken oluşturulacak
    public LettuceConnectionFactory redisConnectionFactory(){ // nereye bagşanacagını belirtiyoruz:
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost",6379));
    }
}
