package com.mensajeriaservice.microserviceMensajeria.Interfaces;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import com.mensajeriaservice.microserviceMensajeria.Entidades.Message;

public interface MessagesRepository extends ReactiveMongoRepository<Message, String> {
    Flux<Message> findByRecipientId(String recipientId);
}
