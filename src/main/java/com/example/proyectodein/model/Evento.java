package com.example.proyectodein.model;

import java.util.Objects;

public class Evento {
    private int idEvento;
    private String nombre;
    private int idOlimpiada;
    private int idDeporte;

    public Evento(int idEvento, String nombre, int idOlimpiada, int idDeporte) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.idOlimpiada = idOlimpiada;
        this.idDeporte = idDeporte;
    }

    public Evento(){}

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdOlimpiada() {
        return idOlimpiada;
    }

    public void setIdOlimpiada(int idOlimpiada) {
        this.idOlimpiada = idOlimpiada;
    }

    public int getIdDeporte() {
        return idDeporte;
    }

    public void setIdDeporte(int idDeporte) {
        this.idDeporte = idDeporte;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return idEvento == evento.idEvento && idOlimpiada == evento.idOlimpiada && idDeporte == evento.idDeporte && Objects.equals(nombre, evento.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEvento, nombre, idOlimpiada, idDeporte);
    }
}
