package com.example.proyectodein.model;

import java.util.Objects;
/**
 * Representa un equipo con un ID único, nombre e iniciales.
 * <p>
 * Esta clase proporciona métodos para obtener y establecer el ID, nombre e iniciales del equipo,
 * junto con métodos sobreescritos {@code equals}, {@code hashCode} y {@code toString}.
 * </p>
 */
public class Equipo {
    private int idEquipo;
    private String nombre;
    private String iniciales;
    /**
     * Construye un nuevo {@code Equipo} con el ID, nombre e iniciales especificados.
     *
     * @param idEquipo  el identificador único del equipo
     * @param nombre    el nombre completo del equipo
     * @param iniciales las iniciales que representan al equipo
     */
    public Equipo(int idEquipo, String nombre, String iniciales) {
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.iniciales = iniciales;
    }
    /**
     * Constructor por defecto que crea una instancia de {@code Equipo} sin valores iniciales.
     */
    public Equipo(){};
    /**
     * Obtiene el ID del equipo.
     *
     * @return el identificador único del equipo
     */
    public int getIdEquipo() {
        return idEquipo;
    }
    /**
     * Establece el ID del equipo.
     *
     * @param idEquipo el nuevo ID para el equipo
     */
    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }
    /**
     * Obtiene el nombre del equipo.
     *
     * @return el nombre completo del equipo
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Establece el nombre del equipo.
     *
     * @param nombre el nuevo nombre para el equipo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * Obtiene las iniciales del equipo.
     *
     * @return las iniciales del equipo
     */
    public String getIniciales() {
        return iniciales;
    }
    /**
     * Establece las iniciales del equipo.
     *
     * @param iniciales las nuevas iniciales para el equipo
     */
    public void setIniciales(String iniciales) {
        this.iniciales = iniciales;
    }
    /**
     * Devuelve una representación en cadena del {@code Equipo}.
     * <p>
     * La representación en cadena consiste en el nombre y las iniciales del equipo.
     * </p>
     *
     * @return una cadena que representa el equipo en formato "nombre,iniciales"
     */
    @Override
    public String toString() {
        return nombre+","+iniciales;
    }
    /**
     * Compara este {@code Equipo} con el objeto especificado.
     * <p>
     * Dos objetos {@code Equipo} se consideran iguales si tienen el mismo ID,
     * nombre e iniciales.
     * </p>
     *
     * @param o el objeto con el cual comparar
     * @return {@code true} si este objeto es igual al objeto especificado; {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        return idEquipo == equipo.idEquipo && Objects.equals(nombre, equipo.nombre) && Objects.equals(iniciales, equipo.iniciales);
    }
    /**
     * Devuelve un valor de código hash para el {@code Equipo}.
     * <p>
     * Este código hash se calcula en función del ID, nombre e iniciales del equipo.
     * </p>
     *
     * @return el valor de código hash para este {@code Equipo}
     */
    @Override
    public int hashCode() {
        return Objects.hash(idEquipo, nombre, iniciales);
    }
}

