package com.example.proyectodein.model;

public class Participacion {
    private int idDeportista;
    private int idEvento;
    private int idEquipo;
    private Integer edad;
    private String medalla;

    public Participacion(int idDeportista, int idEvento, int idEquipo, Integer edad, String medalla) {
        this.idDeportista = idDeportista;
        this.idEvento = idEvento;
        this.idEquipo = idEquipo;
        this.edad = edad;
        this.medalla = medalla;
    }

    public int getIdDeportista() {
        return idDeportista;
    }

    public void setIdDeportista(int idDeportista) {
        this.idDeportista = idDeportista;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getMedalla() {
        return medalla;
    }

    public void setMedalla(String medalla) {
        this.medalla = medalla;
    }


}
