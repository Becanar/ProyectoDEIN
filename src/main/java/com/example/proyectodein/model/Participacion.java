package com.example.proyectodein.model;

/**
 * La clase Participacion representa la participación de un deportista en un evento específico,
 * asociada a un equipo, edad del deportista en el momento y el tipo de medalla obtenida.
 */
public class Participacion {
    private int idDeportista;
    private int idEvento;
    private int idEquipo;
    private Integer edad;
    private String medalla;

    /**
     * Constructor que inicializa una participación con los datos específicos.
     *
     * @param idDeportista el ID del deportista que participa
     * @param idEvento el ID del evento en el cual participa
     * @param idEquipo el ID del equipo con el que participa
     * @param edad la edad del deportista en el momento de la participación
     * @param medalla el tipo de medalla obtenida (por ejemplo, "oro", "plata", "bronce")
     */
    public Participacion(int idDeportista, int idEvento, int idEquipo, Integer edad, String medalla) {
        this.idDeportista = idDeportista;
        this.idEvento = idEvento;
        this.idEquipo = idEquipo;
        this.edad = edad;
        this.medalla = medalla;
    }

    /**
     * Constructor vacío para crear una instancia de Participacion sin inicializar sus atributos.
     */
    public Participacion(){}

    /**
     * Obtiene el ID del deportista.
     *
     * @return el ID del deportista
     */
    public int getIdDeportista() {
        return idDeportista;
    }

    /**
     * Establece el ID del deportista.
     *
     * @param idDeportista el nuevo ID del deportista
     */
    public void setIdDeportista(int idDeportista) {
        this.idDeportista = idDeportista;
    }

    /**
     * Obtiene el ID del evento.
     *
     * @return el ID del evento
     */
    public int getIdEvento() {
        return idEvento;
    }

    /**
     * Establece el ID del evento.
     *
     * @param idEvento el nuevo ID del evento
     */
    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    /**
     * Obtiene el ID del equipo.
     *
     * @return el ID del equipo
     */
    public int getIdEquipo() {
        return idEquipo;
    }

    /**
     * Establece el ID del equipo.
     *
     * @param idEquipo el nuevo ID del equipo
     */
    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    /**
     * Obtiene la edad del deportista en el momento de la participación.
     *
     * @return la edad del deportista, o null si no está establecida
     */
    public Integer getEdad() {
        return edad;
    }

    /**
     * Establece la edad del deportista en el momento de la participación.
     *
     * @param edad la edad del deportista
     */
    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    /**
     * Obtiene el tipo de medalla obtenida en el evento.
     *
     * @return el tipo de medalla (por ejemplo, "oro", "plata", "bronce") o null si no se obtuvo una medalla
     */
    public String getMedalla() {
        return medalla;
    }

    /**
     * Establece el tipo de medalla obtenida en el evento.
     *
     * @param medalla el tipo de medalla obtenida (por ejemplo, "oro", "plata", "bronce")
     */
    public void setMedalla(String medalla) {
        this.medalla = medalla;
    }
}
