package com.mensajeriaservice.microserviceMensajeria.RedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.mensajeriaservice.microserviceMensajeria.RedisExpirationListener;
import com.mensajeriaservice.microserviceMensajeria.Entidades.Message;

@Configuration
public class RedisConfig {
    @Bean
    public ReactiveRedisTemplate<String, Message> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        // Serializador JSON para los valores (mensajes)
        Jackson2JsonRedisSerializer<Message> serializer = new Jackson2JsonRedisSerializer<>(Message.class);
        
        // Serializador para las claves (Strings)
        StringRedisSerializer keySerializer = new StringRedisSerializer();

        // Configurar el contexto de serialización
        RedisSerializationContext.RedisSerializationContextBuilder<String, Message> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        
        RedisSerializationContext<String, Message> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        RedisExpirationListener listener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listener, new PatternTopic("__keyevent@0__:expired"));
        return container;
    }
}
