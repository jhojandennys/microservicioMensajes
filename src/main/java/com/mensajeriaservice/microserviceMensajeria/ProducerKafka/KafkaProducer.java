package com.mensajeriaservice.microserviceMensajeria.ProducerKafka;
import java.util.Map;
import java.util.HashMap;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.mensajeriaservice.microserviceMensajeria.DTO.SalaDTO;
import com.mensajeriaservice.microserviceMensajeria.Entidades.Message;

@Configuration
public class KafkaProducer {

    @Value("${spring.kafka.bootstrap-servers}")
  private String boostrapservers;
  private KafkaTemplate<String, String> kafkaTemplate;

  public Map<String, Object> producerConfig() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapservers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    
    // Set JsonSerializer class for Message
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    
    // You can also specify trusted packages and type mapper as needed
    return props;
}

@Bean
    public ProducerFactory<String, String> producerFactory2() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate12() {
        return new KafkaTemplate<>(producerFactory2());
    }
  @Bean
public ProducerFactory<String, Message> producerFactory() {
return new DefaultKafkaProducerFactory<>(producerConfig());
}

@Bean
public KafkaTemplate<String,Message> kafkaTemplate( ProducerFactory<String,Message> producerFactory){
  return new KafkaTemplate<>(producerFactory);
}


  }