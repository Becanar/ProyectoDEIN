package com.example.proyectodein.model;

import java.util.Objects;

/**
 * Representa una olimpiada con un ID único, nombre, año, temporada y ciudad.
 * <p>
 * Esta clase proporciona métodos para obtener y establecer el ID, nombre, año, temporada y ciudad de la olimpiada,
 * junto con métodos sobreescritos {@code equals}, {@code hashCode} y {@code toString}.
 * </p>
 */
public class Olimpiada {
    private int idOlimpiada;
    private String nombre;
    private int anio;
    private String temporada;
    private String ciudad;

    /**
     * Construye una nueva {@code Olimpiada} con el ID, nombre, año, temporada y ciudad especificados.
     *
     * @param idOlimpiada el identificador único de la olimpiada
     * @param nombre      el nombre de la olimpiada
     * @param anio        el año en que se celebró la olimpiada
     * @param temporada   la temporada de la olimpiada (por ejemplo, "verano" o "invierno")
     * @param ciudad      la ciudad donde se celebró la olimpiada
     */
    public Olimpiada(int idOlimpiada, String nombre, int anio, String temporada, String ciudad) {
        this.idOlimpiada = idOlimpiada;
        this.nombre = nombre;
        this.anio = anio;
        this.temporada = temporada;
        this.ciudad = ciudad;
    }

    /**
     * Constructor por defecto que crea una instancia de {@code Olimpiada} sin valores iniciales.
     */
    public Olimpiada() {}

    /**
     * Obtiene el ID de la olimpiada.
     *
     * @return el identificador único de la olimpiada
     */
    public int getIdOlimpiada() {
        return idOlimpiada;
    }

    /**
     * Establece el ID de la olimpiada.
     *
     * @param idOlimpiada el nuevo ID para la olimpiada
     */
    public void setIdOlimpiada(int idOlimpiada) {
        this.idOlimpiada = idOlimpiada;
    }

    /**
     * Obtiene el nombre de la olimpiada.
     *
     * @return el nombre de la olimpiada
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la olimpiada.
     *
     * @param nombre el nuevo nombre para la olimpiada
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el año en que se celebró la olimpiada.
     *
     * @return el año de la olimpiada
     */
    public int getAnio() {
        return anio;
    }

    /**
     * Establece el año en que se celebró la olimpiada.
     *
     * @param anio el nuevo año para la olimpiada
     */
    public void setAnio(int anio) {
        this.anio = anio;
    }

    /**
     * Obtiene la temporada de la olimpiada (por ejemplo, "verano" o "invierno").
     *
     * @return la temporada de la olimpiada
     */
    public String getTemporada() {
        return temporada;
    }

    /**
     * Establece la temporada de la olimpiada.
     *
     * @param temporada la nueva temporada para la olimpiada
     */
    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    /**
     * Obtiene la ciudad donde se celebró la olimpiada.
     *
     * @return la ciudad de la olimpiada
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad donde se celebró la olimpiada.
     *
     * @param ciudad la nueva ciudad para la olimpiada
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Devuelve una representación en cadena de la {@code Olimpiada}.
     * <p>
     * La representación en cadena consiste en el nombre de la olimpiada seguido de la ciudad.
     * </p>
     *
     * @return una cadena que representa la olimpiada en formato "nombre, ciudad"
     */
    @Override
    public String toString() {
        return nombre + ", " + ciudad;
    }

    /**
     * Compara esta {@code Olimpiada} con el objeto especificado.
     * <p>
     * Dos objetos {@code Olimpiada} se consideran iguales si tienen el mismo ID, nombre, año,
     * temporada y ciudad.
     * </p>
     *
     * @param o el objeto con el cual comparar
     * @return {@code true} si este objeto es igual al objeto especificado; {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Olimpiada olimpiada = (Olimpiada) o;
        return idOlimpiada == olimpiada.idOlimpiada &&
                anio == olimpiada.anio &&
                Objects.equals(nombre, olimpiada.nombre) &&
                Objects.equals(temporada, olimpiada.temporada) &&
                Objects.equals(ciudad, olimpiada.ciudad);
    }

    /**
     * Devuelve un valor de código hash para la {@code Olimpiada}.
     * <p>
     * Este código hash se calcula en función del ID, nombre, año, temporada y ciudad de la olimpiada.
     * </p>
     *
     * @return el valor de código hash para esta {@code Olimpiada}
     */
    @Override
    public int hashCode() {
        return Objects.hash(idOlimpiada, nombre, anio, temporada, ciudad);
    }
}
