package com.mensajeriaservice.microserviceMensajeria.DTO;

public class SalaDTO {
    private String idSala;
    private String nombreSala;

    // Constructor, getters, setters
    public SalaDTO(String idSala, String nombreSala) {
        this.idSala = idSala;
        this.nombreSala = nombreSala;
    }
    public SalaDTO() {

    }

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }
}
