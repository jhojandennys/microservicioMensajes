package com.mensajeriaservice.microserviceMensajeria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient  // Habilita la integración con Eureka

@SpringBootApplication
public class MicroserviceMensajeriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceMensajeriaApplication.class, args);
	}




}
