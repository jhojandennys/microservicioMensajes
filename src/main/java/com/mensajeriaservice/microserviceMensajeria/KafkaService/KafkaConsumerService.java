package com.mensajeriaservice.microserviceMensajeria.KafkaService;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.mensajeriaservice.microserviceMensajeria.Service.MessageService;
import com.mensajeriaservice.microserviceMensajeria.DTO.SalaDTO;
import com.mensajeriaservice.microserviceMensajeria.Entidades.Message;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerService {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Configuración base para los consumidores
    private Map<String, Object> consumerConfig(String groupId, Class<?> valueDeserializer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueDeserializer);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); // O el paquete específico
        return props;
    }

    // ConsumerFactory para Message
    @Bean
    public ConsumerFactory<String, Message> consumerFactoryMessage() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig("chat-group", Message.class));
    }

    // ConsumerFactory para SalaDTO
    @Bean
    public ConsumerFactory<String, SalaDTO> consumerFactorySalaDTO() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig("salas-group", SalaDTO.class));
    }

    // KafkaListenerContainerFactory para Message
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Message>> messageFactory(
            ConsumerFactory<String, Message> consumerFactoryMessage) {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryMessage);
        factory.setConcurrency(3);  // Número de hilos para procesamiento paralelo
        return factory;
    }

    // KafkaListenerContainerFactory para SalaDTO
}
    
/* 
    private final MessageService messageService;

    public KafkaConsumerService(MessageService messageService) {
        this.messageService = messageService;
    }

    // Consume mensajes de Kafka
    @KafkaListener(topics = "messages", groupId = "message-service")
    public void listen(String messageContent) {
        processMessageAsync(messageContent);
    }

    // Procesa el mensaje asincrónicamente
    @Async
    public void processMessageAsync(String messageContent) {
        // Simular procesamiento del mensaje
        Message message = parseMessage(messageContent);
        messageService.saveMessageAsync(message).subscribe();
        System.out.println("Mensaje procesado en hilo: " + Thread.currentThread().getName());
    }

    // Convierte el contenido del mensaje en un objeto Message
    private Message parseMessage(String messageContent) {
        Message message = new Message();
        message.setContent(messageContent);
        message.setSenderId("system");
        message.setRecipientId("user123");
        message.setTimestamp(System.currentTimeMillis());
        return message;
    }*/



