package com.mensajeriaservice.microserviceMensajeria.RedisService;
import org.springframework.scheduling.annotation.Async;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisService {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public RedisService(ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Guarda un mensaje en Redis
    @Async
    public Mono<Boolean> saveMessageToCache(String key, String message) {
        return redisTemplate.opsForValue().set(key, message)
                .doOnNext(saved -> System.out.println("Saved to Redis: " + key + " - Thread: " + Thread.currentThread().getName()))
                .onErrorResume(e -> {
                    System.err.println("Error guardando en Redis: " + e.getMessage());
                    return Mono.just(false); // Devolver un valor predeterminado si ocurre un error
                });
    }
    
    // Recupera un mensaje de Redis
    @Async
    public Mono<String> getMessageFromCache(String key) {
        return redisTemplate.opsForValue().get(key)
                .doOnNext(msg -> System.out.println("Fetched from Redis: " + msg + " - Thread: " + Thread.currentThread().getName()));
    }
}
