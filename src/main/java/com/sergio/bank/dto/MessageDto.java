package com.sergio.bank.dto;

public class MessageDto {
    private String idEntidad;
    private String fecha;
    private String mensaje;
    private String recurso;
    private Boolean estado;

    public MessageDto() {
    }

    public MessageDto(String idEntidad, String fecha, String mensaje, String recurso, Boolean estado) {
        this.idEntidad = idEntidad;
        this.fecha = fecha;
        this.mensaje = mensaje;
        this.recurso = recurso;
        this.estado = estado;
    }

    public String getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(String idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
