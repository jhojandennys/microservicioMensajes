package com.mensajeriaservice.microserviceMensajeria.Service;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.mensajeriaservice.microserviceMensajeria.Interfaces.MessagesRepository;
import com.mensajeriaservice.microserviceMensajeria.DTO.SalaDTO;
import com.mensajeriaservice.microserviceMensajeria.Entidades.Message;
import com.mensajeriaservice.microserviceMensajeria.RedisService.RedisConfig;
import com.mensajeriaservice.microserviceMensajeria.Topic.KafkaTopicConfig;
@Service
public class MessageService {
    private KafkaTemplate<String,Message> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;
    private static final String ROOM_KEY = "room:";

    private static final Duration ROOM_TTL = Duration.ofSeconds(10); // 10 segundos

    private final MessagesRepository messageRepository;
    private final ReactiveRedisTemplate<String, Message> redisTemplate;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);  // 2 hilos, uno para Redis y otro para Kafka
    private static final String MESSAGE_KEY = "message:";

    public MessageService(MessagesRepository messageRepository, ReactiveRedisTemplate<String, Message> redisTemplate,KafkaTemplate<String,Message> kafkaTemplate,KafkaTopicConfig kafkaTopicConfig) {
        this.messageRepository = messageRepository;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicConfig = kafkaTopicConfig;
    }

    // Método asincrónico que guarda el mensaje en MongoDB

    
    private String generateMessageId() {
        // Lógica para generar un ID único para el mensaje
        return UUID.randomUUID().toString();
    }

    public void createMessage(Message message) {
        if (message.getId() == null) {
            message.setId(UUID.randomUUID().toString());  // Generar un ID único si no existe
        }

        String redisKey = MESSAGE_KEY + message.getId();
        String roomKey = ROOM_KEY + message.getRoomId();  // Clave de la sala en Redis
        String topic = "chat-topic-" + message.getRoomId();  // Tópico basado en el roomId de la sala

        System.out.println("📌 Clave Redis: " + redisKey);
        System.out.println("📌 Tópico Kafka: " + topic);

        // Guardar el mensaje en Redis de manera reactiva
        redisTemplate.opsForValue()
            .set(redisKey, message, Duration.ofSeconds(10))
            .doOnSuccess(success -> System.out.println("✅ Mensaje guardado en Redis: " + redisKey))
            .doOnError(error -> System.err.println("❌ Error al guardar en Redis: " + error.getMessage()))
            .subscribe();

        // Establecer el TTL para la sala (si no existe ya)
        redisTemplate.expire(roomKey, ROOM_TTL)
            .doOnSuccess(success -> System.out.println("⏳ TTL configurado para la sala: " + roomKey))
            .doOnError(error -> System.err.println("❌ Error al configurar TTL para la sala: " + error.getMessage()))
            .subscribe();

        // Enviar el mensaje a Kafka de manera asíncrona
        CompletableFuture.runAsync(() -> {
            try {
                kafkaTemplate.send(topic, message);
                System.out.println("📨 Mensaje enviado a Kafka en el tópico: " + topic);
            } catch (Exception e) {
                System.err.println("❌ Error al enviar mensaje a Kafka: " + e.getMessage());
            }
        });
    }
    

    public void createMessageForSala(SalaDTO salaDTO) {
        // Crear un nuevo mensaje asociado a la sala
        Message message = new Message();
        message.setRoomId(salaDTO.getIdSala());  // Asignar el roomId de la sala recibida
    
        // Ahora, crea el mensaje, y si es necesario, lo guardas en Redis o lo envías a Kafka
        createMessage(message);
    }

    public Flux<Message> getMessagesByRecipientId(String recipientId) {
        return messageRepository.findByRecipientId(recipientId);
    }

    public Flux<Message> getRecentMessages(String recipientId) {
        return redisTemplate.opsForList().range("recent:" + recipientId, 0, 10);
    }
}


