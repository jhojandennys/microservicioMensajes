package com.mensajeriaservice.microserviceMensajeria.MessageThread;

public class MessageProcessorThread extends Thread {
    private final String message;

    public MessageProcessorThread(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println("Procesando en hilo: " + Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
            System.out.println("Mensaje procesado: " + message);
        } catch (InterruptedException e) {
            System.err.println("Error en el hilo de procesamiento: " + message);
        }
    }
}

