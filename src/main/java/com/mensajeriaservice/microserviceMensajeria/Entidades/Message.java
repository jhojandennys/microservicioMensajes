package com.mensajeriaservice.microserviceMensajeria.Entidades;

import lombok.Data;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Message implements Serializable{
    @Id
    private String id;
    private String chatSessionId;

    private String senderId;

    private String recipientId;

    private String content;

    private String roomId;  // 📌 Identificador de la sala
 
}
