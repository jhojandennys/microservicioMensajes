package com.mensajeriaservice.microserviceMensajeria.KafkaListener;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mensajeriaservice.microserviceMensajeria.DTO.SalaDTO;
import com.mensajeriaservice.microserviceMensajeria.Entidades.Message;
import com.mensajeriaservice.microserviceMensajeria.MessageThread.MessageProcessorThread;
import com.mensajeriaservice.microserviceMensajeria.Service.MessageService;

@Component
public class KafkaListener {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
MessageService messageService;
  
    @org.springframework.kafka.annotation.KafkaListener(
        topicPattern = "chat-topic-",
        groupId = "chat-group"
    )
    public void listener(@Payload String message) {
        try {
            Message parsedMessage = objectMapper.readValue(message, Message.class);
            System.out.println("Received message: " + parsedMessage.getContent());

              MessageProcessorThread thread = new MessageProcessorThread(message);
        thread.start();
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + message);
            e.printStackTrace();
        }
    }

       private final ConcurrentHashMap<String, String> salasRegistradas = new ConcurrentHashMap<>();

    @org.springframework.kafka.annotation.KafkaListener(topics = "salas-topic", groupId = "salas-group")
    public void consumirSala(String message) {
        try {
            // Convertir el mensaje JSON en un objeto SalaDTO
            ObjectMapper objectMapper = new ObjectMapper();
            SalaDTO salaDTO = objectMapper.readValue(message, SalaDTO.class);
    
            System.out.println("🎉 Sala recibida en Kafka: " + salaDTO.getIdSala());
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
