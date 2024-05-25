package com.example.catcheservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class ConfigRedis {

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private String port;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(Integer.parseInt(port));
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;

    }
}
