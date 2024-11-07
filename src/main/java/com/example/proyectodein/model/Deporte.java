package com.example.proyectodein.model;

import java.util.Objects;

/**
 * Representa un deporte con un ID único y un nombre.
 * <p>
 * Esta clase proporciona métodos para obtener y establecer el ID y el nombre del deporte,
 * junto con los métodos sobreescritos {@code equals}, {@code hashCode} y {@code toString}.
 * </p>
 */
public class Deporte {
    private int idDeporte;
    private String nombre;
    /**
     * Construye un nuevo {@code Deporte} con el ID y el nombre especificados.
     *
     * @param idDeporte el identificador único del deporte
     * @param nombre    el nombre del deporte
     */
    public Deporte(int idDeporte, String nombre) {
        this.idDeporte = idDeporte;
        this.nombre = nombre;
    }
    /**
     * Constructor por defecto que crea una instancia de {@code Deporte} sin valores iniciales.
     */
    public Deporte(){}

    /**
     * Obtiene el ID del deporte.
     *
     * @return el identificador único del deporte
     */

    public int getIdDeporte() {
        return idDeporte;
    }
    /**
     * Establece el ID del deporte.
     *
     * @param idDeporte el nuevo ID para el deporte
     */
    public void setIdDeporte(int idDeporte) {
        this.idDeporte = idDeporte;
    }

    /**
     * Obtiene el nombre del deporte.
     *
     * @return el nombre del deporte
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Establece el nombre del deporte.
     *
     * @param nombre el nuevo nombre para el deporte
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * Devuelve una representación en cadena del {@code Deporte}.
     * <p>
     * La representación en cadena es el nombre del deporte.
     * </p>
     *
     * @return el nombre del deporte
     */
    @Override
    public String toString() {
        return nombre;
    }
    /**
     * Compara este {@code Deporte} con el objeto especificado.
     * <p>
     * Dos objetos {@code Deporte} se consideran iguales si tienen el mismo
     * ID y nombre.
     * </p>
     *
     * @param o el objeto con el que comparar
     * @return {@code true} si este objeto es igual al objeto especificado; {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deporte deporte = (Deporte) o;
        return idDeporte == deporte.idDeporte && Objects.equals(nombre, deporte.nombre);
    }
    /**
     * Devuelve un valor de código hash para el {@code Deporte}.
     * <p>
     * Este código hash se calcula en función del ID y el nombre del deporte.
     * </p>
     *
     * @return el valor de código hash para este {@code Deporte}
     */
    @Override
    public int hashCode() {
        return Objects.hash(idDeporte, nombre);
    }
}
