package com.mensajeriaservice.microserviceMensajeria;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RedisExpirationListener implements MessageListener {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String NOTIFICATION_TOPIC = "notification-topic";

    public RedisExpirationListener(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody(), StandardCharsets.UTF_8);

        if (expiredKey.startsWith("room:")) {  // Solo procesar si es una sala
            String roomId = expiredKey.replace("room:", "");
            System.out.println("🚨 Sala expirada: " + roomId);

            // Enviar evento de notificación a Kafka
            kafkaTemplate.send(NOTIFICATION_TOPIC, "La sala " + roomId + " ha expirado.");
            System.out.println("📢 Notificación enviada a Kafka para la sala: " + roomId);
        }
    }
}
