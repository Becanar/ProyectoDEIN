package com.example.proyectodein.model;

import java.sql.Blob;
import java.util.Date;
import java.util.Objects;

/**
 * Representa un animal en el sistema.
 */
public class Animal {
    private int id;
    private String nombre;
    private String especie;
    private String raza;
    private String sexo;
    private int edad;
    private double peso;
    private String observaciones;
    private Date fechaPrimeraConsulta;
    private Blob foto;

    /**
     * Constructor de la clase Animal.
     *
     * @param id                  El identificador único del animal.
     * @param nombre              El nombre del animal.
     * @param especie             La especie del animal.
     * @param raza                La raza del animal.
     * @param sexo                El sexo del animal.
     * @param edad                La edad del animal.
     * @param peso                El peso del animal.
     * @param observaciones       Observaciones sobre el animal.
     * @param fechaPrimeraConsulta Fecha de la primera consulta del animal.
     * @param foto                La foto del animal como un objeto Blob.
     */
    public Animal(int id, String nombre, String especie, String raza, String sexo, int edad, double peso, String observaciones, Date fechaPrimeraConsulta, Blob foto) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.edad = edad;
        this.peso = peso;
        this.observaciones = observaciones;
        this.fechaPrimeraConsulta = fechaPrimeraConsulta;
        this.foto = foto;
    }

    /**
     * Constructor vacío para la clase Animal.
     */
    public Animal() {
    }

    /**
     * Obtiene el identificador del animal.
     *
     * @return El identificador del animal.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador del animal.
     *
     * @param id El nuevo identificador del animal.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del animal.
     *
     * @return El nombre del animal.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del animal.
     *
     * @param nombre El nuevo nombre del animal.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la especie del animal.
     *
     * @return La especie del animal.
     */
    public String getEspecie() {
        return especie;
    }

    /**
     * Establece la especie del animal.
     *
     * @param especie La nueva especie del animal.
     */
    public void setEspecie(String especie) {
        this.especie = especie;
    }

    /**
     * Obtiene la raza del animal.
     *
     * @return La raza del animal.
     */
    public String getRaza() {
        return raza;
    }

    /**
     * Establece la raza del animal.
     *
     * @param raza La nueva raza del animal.
     */
    public void setRaza(String raza) {
        this.raza = raza;
    }

    /**
     * Obtiene el sexo del animal.
     *
     * @return El sexo del animal.
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * Establece el sexo del animal.
     *
     * @param sexo El nuevo sexo del animal.
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * Obtiene la edad del animal.
     *
     * @return La edad del animal.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece la edad del animal.
     *
     * @param edad La nueva edad del animal.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Obtiene el peso del animal.
     *
     * @return El peso del animal.
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Establece el peso del animal.
     *
     * @param peso El nuevo peso del animal.
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Obtiene las observaciones sobre el animal.
     *
     * @return Las observaciones sobre el animal.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Establece las observaciones sobre el animal.
     *
     * @param observaciones Las nuevas observaciones sobre el animal.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Obtiene la fecha de la primera consulta del animal.
     *
     * @return La fecha de la primera consulta del animal.
     */
    public Date getFechaPrimeraConsulta() {
        return fechaPrimeraConsulta;
    }

    /**
     * Establece la fecha de la primera consulta del animal.
     *
     * @param fechaPrimeraConsulta La nueva fecha de la primera consulta del animal.
     */
    public void setFechaPrimeraConsulta(Date fechaPrimeraConsulta) {
        this.fechaPrimeraConsulta = fechaPrimeraConsulta;
    }

    /**
     * Obtiene la foto del animal.
     *
     * @return La foto del animal como un objeto Blob.
     */
    public Blob getFoto() {
        return foto;
    }

    /**
     * Establece la foto del animal.
     *
     * @param foto La nueva foto del animal como un objeto Blob.
     */
    public void setFoto(Blob foto) {
        this.foto = foto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id && edad == animal.edad && Double.compare(peso, animal.peso) == 0 && Objects.equals(nombre, animal.nombre) && Objects.equals(especie, animal.especie) && Objects.equals(raza, animal.raza) && Objects.equals(sexo, animal.sexo) && Objects.equals(observaciones, animal.observaciones) && Objects.equals(fechaPrimeraConsulta, animal.fechaPrimeraConsulta) && Objects.equals(foto, animal.foto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, especie, raza, sexo, edad, peso, observaciones, fechaPrimeraConsulta, foto);
    }

    @Override
    public String toString() {
        return nombre +
                ", " + especie +
                ", " + raza +
                ", " + sexo +
                ", " + edad +
                " años, " + peso +
                "kg. Observaciones: " + observaciones +
                fechaPrimeraConsulta;
    }
}
