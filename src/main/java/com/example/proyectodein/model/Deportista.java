package com.example.proyectodein.model;


import java.sql.Blob;
import java.util.Objects;
/**
 * Representa un deportista con un ID único, nombre, sexo, peso, altura y una foto.
 * <p>
 * Esta clase proporciona métodos para obtener y establecer el ID, nombre, sexo,
 * peso, altura y foto del deportista, junto con métodos sobreescritos
 * {@code equals}, {@code hashCode} y {@code toString}.
 * </p>
 */
public class Deportista {
    private int idDeportista;
    private String nombre;
    private char sexo;
    private int peso;
    private int altura;
    private Blob foto;

    /**
     * Construye un nuevo {@code Deportista} con el ID, nombre, sexo, peso, altura y foto especificados.
     *
     * @param idDeportista el identificador único del deportista
     * @param nombre       el nombre del deportista
     * @param sexo         el sexo del deportista, representado como un carácter ('M' o 'F')
     * @param peso         el peso del deportista en kilogramos
     * @param altura       la altura del deportista en centímetros
     * @param foto         una imagen en formato Blob que representa la foto del deportista
     */
    public Deportista(int idDeportista, String nombre, char sexo, int peso, int altura, Blob foto) {
        this.idDeportista = idDeportista;
        this.nombre = nombre;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
        this.foto = foto;
    }
    /**
     * Constructor por defecto que crea una instancia de {@code Deportista} sin valores iniciales.
     */
    public Deportista(){}

    /**
     * Obtiene el ID del deportista.
     *
     * @return el identificador único del deportista
     */
    public int getIdDeportista() {
        return idDeportista;
    }
    /**
     * Establece el ID del deportista.
     *
     * @param idDeportista el nuevo ID para el deportista
     */
    public void setIdDeportista(int idDeportista) {
        this.idDeportista = idDeportista;
    }
    /**
     * Obtiene el nombre del deportista.
     *
     * @return el nombre del deportista
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Establece el nombre del deportista.
     *
     * @param nombre el nuevo nombre para el deportista
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * Obtiene el sexo del deportista.
     *
     * @return el sexo del deportista
     */
    public char getSexo() {
        return sexo;
    }
    /**
     * Establece el sexo del deportista.
     *
     * @param sexo el nuevo sexo para el deportista
     */
    public void setSexo(char sexo) {
        this.sexo = sexo;
    }
    /**
     * Obtiene el peso del deportista.
     *
     * @return el peso del deportista en kilogramos
     */
    public int getPeso() {
        return peso;
    }
    /**
     * Establece el peso del deportista.
     *
     * @param peso el nuevo peso para el deportista en kilogramos
     */
    public void setPeso(int peso) {
        this.peso = peso;
    }
    /**
     * Obtiene la altura del deportista.
     *
     * @return la altura del deportista en centímetros
     */
    public int getAltura() {
        return altura;
    }
    /**
     * Establece la altura del deportista.
     *
     * @param altura la nueva altura para el deportista en centímetros
     */
    public void setAltura(int altura) {
        this.altura = altura;
    }
    /**
     * Obtiene la foto del deportista.
     *
     * @return una imagen en formato Blob que representa la foto del deportista
     */
    public Blob getFoto() {
        return foto;
    }
    /**
     * Establece la foto del deportista.
     *
     * @param foto la nueva foto en formato Blob del deportista
     */
    public void setFoto(Blob foto) {
        this.foto = foto;
    }
    /**
     * Devuelve una representación en cadena del {@code Deportista}.
     * <p>
     * La representación en cadena es el nombre del deportista.
     * </p>
     *
     * @return el nombre del deportista
     */
    @Override
    public String toString() {
        return nombre;
    }
    /**
     * Compara este {@code Deportista} con el objeto especificado.
     * <p>
     * Dos objetos {@code Deportista} se consideran iguales si tienen el mismo ID,
     * nombre, sexo, peso, altura y foto.
     * </p>
     *
     * @param o el objeto con el cual comparar
     * @return {@code true} si este objeto es igual al objeto especificado; {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deportista that = (Deportista) o;
        return idDeportista == that.idDeportista && peso == that.peso && altura == that.altura && Objects.equals(nombre, that.nombre) && Objects.equals(sexo, that.sexo) && Objects.equals(foto, that.foto);
    }
    /**
     * Devuelve un valor de código hash para el {@code Deportista}.
     * <p>
     * Este código hash se calcula en función del ID, nombre, sexo, peso, altura y foto del deportista.
     * </p>
     *
     * @return el valor de código hash para este {@code Deportista}
     */
    @Override
    public int hashCode() {
        return Objects.hash(idDeportista, nombre, sexo, peso, altura, foto);
    }
}
