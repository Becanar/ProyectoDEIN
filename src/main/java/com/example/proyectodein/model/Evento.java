package com.example.proyectodein.model;

import java.util.Objects;

/**
 * Representa un evento con un ID único, nombre, ID de olimpiada y ID de deporte.
 * <p>
 * Esta clase proporciona métodos para obtener y establecer el ID, nombre, ID de olimpiada
 * y ID de deporte del evento, junto con métodos sobreescritos {@code equals}, {@code hashCode}
 * y {@code toString}.
 * </p>
 */
public class Evento {
    private int idEvento;
    private String nombre;
    private int idOlimpiada;
    private int idDeporte;

    /**
     * Construye un nuevo {@code Evento} con el ID, nombre, ID de olimpiada e ID de deporte especificados.
     *
     * @param idEvento   el identificador único del evento
     * @param nombre     el nombre del evento
     * @param idOlimpiada el identificador de la olimpiada asociada al evento
     * @param idDeporte  el identificador del deporte asociado al evento
     */
    public Evento(int idEvento, String nombre, int idOlimpiada, int idDeporte) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.idOlimpiada = idOlimpiada;
        this.idDeporte = idDeporte;
    }

    /**
     * Constructor por defecto que crea una instancia de {@code Evento} sin valores iniciales.
     */
    public Evento(){}

    /**
     * Obtiene el ID del evento.
     *
     * @return el identificador único del evento
     */
    public int getIdEvento() {
        return idEvento;
    }

    /**
     * Establece el ID del evento.
     *
     * @param idEvento el nuevo ID para el evento
     */
    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    /**
     * Obtiene el nombre del evento.
     *
     * @return el nombre del evento
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del evento.
     *
     * @param nombre el nuevo nombre para el evento
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el ID de la olimpiada asociada al evento.
     *
     * @return el ID de la olimpiada
     */
    public int getIdOlimpiada() {
        return idOlimpiada;
    }

    /**
     * Establece el ID de la olimpiada asociada al evento.
     *
     * @param idOlimpiada el nuevo ID de la olimpiada para el evento
     */
    public void setIdOlimpiada(int idOlimpiada) {
        this.idOlimpiada = idOlimpiada;
    }

    /**
     * Obtiene el ID del deporte asociado al evento.
     *
     * @return el ID del deporte
     */
    public int getIdDeporte() {
        return idDeporte;
    }

    /**
     * Establece el ID del deporte asociado al evento.
     *
     * @param idDeporte el nuevo ID del deporte para el evento
     */
    public void setIdDeporte(int idDeporte) {
        this.idDeporte = idDeporte;
    }

    /**
     * Devuelve una representación en cadena del {@code Evento}.
     * <p>
     * La representación en cadena es el nombre del evento.
     * </p>
     *
     * @return el nombre del evento
     */
    @Override
    public String toString() {
        return nombre;
    }

    /**
     * Compara este {@code Evento} con el objeto especificado.
     * <p>
     * Dos objetos {@code Evento} se consideran iguales si tienen el mismo ID, nombre,
     * ID de olimpiada y ID de deporte.
     * </p>
     *
     * @param o el objeto con el cual comparar
     * @return {@code true} si este objeto es igual al objeto especificado; {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return idEvento == evento.idEvento &&
                idOlimpiada == evento.idOlimpiada &&
                idDeporte == evento.idDeporte &&
                Objects.equals(nombre, evento.nombre);
    }

    /**
     * Devuelve un valor de código hash para el {@code Evento}.
     * <p>
     * Este código hash se calcula en función del ID, nombre, ID de olimpiada e ID de deporte del evento.
     * </p>
     *
     * @return el valor de código hash para este {@code Evento}
     */
    @Override
    public int hashCode() {
        return Objects.hash(idEvento, nombre, idOlimpiada, idDeporte);
    }
}
