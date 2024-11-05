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
