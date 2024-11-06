package com.example.proyectodein.model;


import java.sql.Blob;
import java.util.Objects;

public class Deportista {
    private int idDeportista;
    private String nombre;
    private char sexo;
    private int peso;
    private int altura;
    private Blob foto;

    public Deportista(int idDeportista, String nombre, char sexo, int peso, int altura, Blob foto) {
        this.idDeportista = idDeportista;
        this.nombre = nombre;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
        this.foto = foto;
    }

    public Deportista(){}

    public int getIdDeportista() {
        return idDeportista;
    }

    public void setIdDeportista(int idDeportista) {
        this.idDeportista = idDeportista;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public Blob getFoto() {
        return foto;
    }

    public void setFoto(Blob foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deportista that = (Deportista) o;
        return idDeportista == that.idDeportista && peso == that.peso && altura == that.altura && Objects.equals(nombre, that.nombre) && Objects.equals(sexo, that.sexo) && Objects.equals(foto, that.foto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDeportista, nombre, sexo, peso, altura, foto);
    }
}
