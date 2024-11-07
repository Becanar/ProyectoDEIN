package com.example.proyectodein.dao;

import com.example.proyectodein.db.ConectorDB;
import com.example.proyectodein.model.Deporte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase de acceso a datos (DAO) para realizar operaciones CRUD sobre la entidad Deporte.
 */
public class DaoDeporte {

    /**
     * Obtiene un objeto Deporte a partir de su ID.
     *
     * @param id el ID del deporte que se desea obtener
     * @return un objeto Deporte si se encuentra, o null si no existe
     */
    public static Deporte getDeporte(int id) {
        ConectorDB connection;
        Deporte deporte = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_deporte,nombre FROM Deporte WHERE id_deporte = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_deporte = rs.getInt("id_deporte");
                String nombre = rs.getString("nombre");
                deporte = new Deporte(id_deporte, nombre);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException | FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return deporte;
    }

    /**
     * Obtiene un objeto Deporte a partir de su nombre.
     *
     * @param str el nombre del deporte que se desea obtener
     * @return un objeto Deporte si se encuentra, o null si no existe
     */
    public static Deporte getDeporte(String str) {
        ConectorDB connection;
        Deporte deporte = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_deporte,nombre FROM Deporte WHERE nombre = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, str);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_deporte = rs.getInt("id_deporte");
                String nombre = rs.getString("nombre");
                deporte = new Deporte(id_deporte, nombre);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException | FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return deporte;
    }

    /**
     * Carga una lista observable con todos los deportes almacenados en la base de datos.
     *
     * @return una lista observable de objetos Deporte
     */
    public static ObservableList<Deporte> cargarListado() {
        ConectorDB connection;
        ObservableList<Deporte> deportes = FXCollections.observableArrayList();
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_deporte,nombre FROM Deporte";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_deporte = rs.getInt("id_deporte");
                String nombre = rs.getString("nombre");
                Deporte deporte = new Deporte(id_deporte, nombre);
                deportes.add(deporte);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return deportes;
    }

    /**
     * Verifica si un deporte puede ser eliminado de la base de datos, comprobando si está
     * asociado a algún evento.
     *
     * @param deporte el objeto Deporte que se desea verificar
     * @return true si el deporte no está asociado a ningún evento, false en caso contrario
     */
    public static boolean esEliminable(Deporte deporte) {
        ConectorDB connection;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT count(*) as cont FROM Evento WHERE id_deporte = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, deporte.getIdDeporte());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int cont = rs.getInt("cont");
                rs.close();
                connection.closeConexion();
                return (cont == 0);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Modifica un deporte existente en la base de datos.
     *
     * @param deporte      el objeto Deporte existente que se desea modificar
     * @param deporteNuevo el objeto Deporte que contiene los nuevos datos
     * @return true si la modificación se realizó con éxito, false en caso contrario
     */
    public static boolean modificar(Deporte deporte, Deporte deporteNuevo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE Deporte SET nombre = ? WHERE id_deporte = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, deporteNuevo.getNombre());
            pstmt.setInt(2, deporte.getIdDeporte());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Actualizado deporte");
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
     * Inserta un nuevo deporte en la base de datos.
     *
     * @param deporte el objeto Deporte que se desea insertar
     * @return el ID generado del nuevo deporte insertado, o -1 si hubo un error
     */
    public static int insertar(Deporte deporte) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO Deporte (nombre) VALUES (?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, deporte.getNombre());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Nueva entrada en deporte");
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    pstmt.close();
                    connection.closeConexion();
                    return id;
                }
            }
            pstmt.close();
            connection.closeConexion();
            return -1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Elimina un deporte de la base de datos.
     *
     * @param deporte el objeto Deporte que se desea eliminar
     * @return true si la eliminación se realizó con éxito, false en caso contrario
     */
    public static boolean eliminar(Deporte deporte) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM Deporte WHERE id_deporte = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, deporte.getIdDeporte());
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
