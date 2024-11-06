package com.example.proyectodein.model;

import java.util.Objects;

public class Olimpiada {
    private int idOlimpiada;
    private String nombre;
    private int anio;
    private String temporada;
    private String ciudad;

    public Olimpiada(int idOlimpiada, String nombre, int anio, String temporada, String ciudad) {
        this.idOlimpiada = idOlimpiada;
        this.nombre = nombre;
        this.anio = anio;
        this.temporada = temporada;
        this.ciudad = ciudad;
    }

    public Olimpiada(){
    }

    public int getIdOlimpiada() {
        return idOlimpiada;
    }

    public void setIdOlimpiada(int idOlimpiada) {
        this.idOlimpiada = idOlimpiada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return "Olimpiada{" +
                "nombre='" + nombre + '\'' +
                ", temporada='" + temporada + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Olimpiada olimpiada = (Olimpiada) o;
        return idOlimpiada == olimpiada.idOlimpiada && anio == olimpiada.anio && Objects.equals(nombre, olimpiada.nombre) && Objects.equals(temporada, olimpiada.temporada) && Objects.equals(ciudad, olimpiada.ciudad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOlimpiada, nombre, anio, temporada, ciudad);
    }
}
