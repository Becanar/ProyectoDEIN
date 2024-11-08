package com.example.proyectodein.dao;

import com.example.proyectodein.db.ConectorDB;
import com.example.proyectodein.model.Deportista;
import com.example.proyectodein.model.Equipo;
import com.example.proyectodein.model.Evento;
import com.example.proyectodein.model.Participacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoParticipacion {

    /**
     * Obtiene una participación de la base de datos dado el ID de un deportista y el ID de un evento.
     *
     * @param idDeportista El ID del deportista.
     * @param idEvento El ID del evento.
     * @return Un objeto Participacion correspondiente a la búsqueda, o null si no se encuentra.
     */
    public static Participacion getParticipacion(int idDeportista, int idEvento) {
        ConectorDB connection;
        PreparedStatement pstmt;
        ResultSet rs;
        Participacion participacion = null;
        try {
            // Establecer la conexión con la base de datos
            connection = new ConectorDB();
            String consulta = "SELECT id_deportista, id_evento, id_equipo, edad, medalla FROM Participacion WHERE id_deportista = ? AND id_evento = ?";

            pstmt = connection.getConnection().prepareStatement(consulta);

            // Establecer los parámetros
            pstmt.setInt(1, idDeportista);  // ID del deportista
            pstmt.setInt(2, idEvento);  // ID del evento

            // Ejecutar la consulta
            rs = pstmt.executeQuery();

            // Verificar si se encontró una participación
            if (rs.next()) {
                // Obtener los datos de la base de datos
                int idEquipo = rs.getInt("id_equipo");
                int edad = rs.getInt("edad");
                String medalla = rs.getString("medalla");

                // Crear un objeto Participacion con los datos obtenidos
                participacion = new Participacion(idDeportista, idEvento, idEquipo, edad, medalla);
            }

            // Cerrar los recursos
            rs.close();
            pstmt.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println("Error al obtener participación: " + e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return participacion;  // Retorna la participación encontrada o null si no existe
    }

    /**
     * Carga todas las participaciones de la base de datos.
     *
     * @return Una lista observable de todas las participaciones.
     */
    public static ObservableList<Participacion> cargarListado() {
        ConectorDB connection;
        ObservableList<Participacion> participacions = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id_deportista,id_evento,id_equipo,edad,medalla FROM Participacion";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_deportista = rs.getInt("id_deportista");
                Deportista deportista = DaoDeportista.getDeportista(id_deportista);
                int id_evento = rs.getInt("id_evento");
                Evento evento = DaoEvento.getEvento(id_evento);
                int id_equipo = rs.getInt("id_equipo");
                Equipo equipo = DaoEquipo.getEquipo(id_equipo);
                int edad = rs.getInt("edad");
                String medalla = rs.getString("medalla");
                Participacion participacion = new Participacion(deportista.getIdDeportista(),evento.getIdEvento(),equipo.getIdEquipo(),edad,medalla);
                participacions.add(participacion);
            }
            rs.close();
            connection.closeConexion();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return participacions;
    }

    /**
     * Modifica los datos de una participación en la base de datos.
     *
     * @param participacion La participación original a modificar.
     * @param participacionNuevo La nueva participación con los valores actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public static boolean modificar(Participacion participacion, Participacion participacionNuevo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE Participacion SET id_deportista = ?,id_evento = ?,id_equipo = ?,edad = ?,medalla = ? WHERE id_deportista = ? AND id_evento = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, participacionNuevo.getIdDeportista());
            pstmt.setInt(2, participacionNuevo.getIdEvento());
            pstmt.setInt(3, participacionNuevo.getIdEquipo());
            pstmt.setInt(4, participacionNuevo.getEdad());
            pstmt.setString(5, participacionNuevo.getMedalla());
            pstmt.setInt(6, participacion.getIdDeportista());
            pstmt.setInt(7, participacion.getIdEvento());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Actualizado participacion");
            pstmt.close();
            connection.closeConexion();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserta una nueva participación en la base de datos.
     *
     * @param participacion La participación a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public static boolean insertar(Participacion participacion) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO Participacion (id_deportista,id_evento,id_equipo,edad,medalla) VALUES (?,?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, participacion.getIdDeportista());
            pstmt.setInt(2, participacion.getIdEvento());
            pstmt.setInt(3, participacion.getIdEquipo());
            pstmt.setInt(4, participacion.getEdad());
            pstmt.setString(5, participacion.getMedalla());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Nueva entrada en participacion");
            return (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Elimina una participación de la base de datos.
     *
     * @param participacion La participación a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public static boolean eliminar(Participacion participacion) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM Participacion WHERE id_deportista = ? AND id_evento = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, participacion.getIdDeportista());
            pstmt.setInt(2, participacion.getIdEvento());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConexion();
            System.out.println("Eliminado con éxito");
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
