package com.mensajeriaservice.microserviceMensajeria.Controller;
import org.springframework.web.bind.annotation.*; // Para las anotaciones REST
import reactor.core.publisher.Flux;            // Para trabajar con datos reactivos (Flujos)
import reactor.core.publisher.Mono;            // Para trabajar con datos reactivos (Objetos)
import org.springframework.beans.factory.annotation.Autowired; // Para inyección de dependencias (opcional si usas @Autowired)
import org.springframework.http.ResponseEntity; // Opcional si manejas respuestas HTTP más avanzadas
import org.springframework.kafka.core.KafkaTemplate;

import com.mensajeriaservice.microserviceMensajeria.Entidades.Message;
import com.mensajeriaservice.microserviceMensajeria.Service.MessageService;
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/messages")
public class MessageController {
       private MessageService messageService;

    
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<String> createMessage(@RequestBody Message message) {
   

        // Aquí puedes agregar la lógica para asociar el mensaje con la sala por el roomId
        System.out.println("🎉 Mensaje recibido para la sala: " + message.getRoomId());
        
        // Lógica para guardar el mensaje y enviarlo a Kafka si es necesario
        messageService.createMessage(message);
    
        return ResponseEntity.ok("Mensaje enviado correctamente al roomId: " + message.getRoomId());
    }
    
    
    /* 
    @GetMapping("/{recipientId}")
    public Flux<Message> getMessagesByRecipientId(@PathVariable String recipientId) {
        return messageService.getMessagesByRecipientId(recipientId);
    }

    @GetMapping("/recent/{recipientId}")
    public Flux<Message> getRecentMessages(@PathVariable String recipientId) {
        return messageService.getRecentMessages(recipientId);
    }*/
}