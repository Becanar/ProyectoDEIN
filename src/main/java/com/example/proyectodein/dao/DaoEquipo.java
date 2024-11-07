package com.example.proyectodein.dao;

import com.example.proyectodein.db.ConectorDB;
import com.example.proyectodein.model.Equipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * La clase DaoEquipo proporciona métodos para interactuar con la base de datos
 * y realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los objetos Equipo.
 */
public class DaoEquipo {

    /**
     * Obtiene un equipo de la base de datos mediante su ID.
     *
     * @param id el ID del equipo a buscar
     * @return un objeto Equipo si se encuentra en la base de datos, o null si no se encuentra
     */
    public static Equipo getEquipo(int id) {
        ConectorDB connection;
        Equipo equipo = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_equipo,nombre,iniciales FROM Equipo WHERE id_equipo = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_equipo = rs.getInt("id_equipo");
                String nombre = rs.getString("nombre");
                String iniciales = rs.getString("iniciales");
                equipo = new Equipo(id_equipo,nombre,iniciales);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return equipo;
    }

    /**
     * Obtiene un equipo de la base de datos mediante su nombre y sus iniciales.
     *
     * @param str el nombre del equipo
     * @param str2 las iniciales del equipo
     * @return un objeto Equipo si se encuentra en la base de datos, o null si no se encuentra
     */
    public static Equipo getEquipo(String str, String str2) {
        ConectorDB connection;
        Equipo equipo = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_equipo,nombre,iniciales FROM Equipo WHERE nombre = ? AND iniciales = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, str);
            pstmt.setString(2, str2);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_equipo = rs.getInt("id_equipo");
                String nombre = rs.getString("nombre");
                String iniciales = rs.getString("iniciales");
                equipo = new Equipo(id_equipo,nombre,iniciales);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return equipo;
    }

    /**
     * Carga una lista de todos los equipos almacenados en la base de datos.
     *
     * @return una lista observable con los equipos
     */
    public static ObservableList<Equipo> cargarListado() {
        ConectorDB connection;
        ObservableList<Equipo> equipos = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id_equipo,nombre,iniciales FROM Equipo";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_equipo = rs.getInt("id_equipo");
                String nombre = rs.getString("nombre");
                String iniciales = rs.getString("iniciales");
                Equipo equipo = new Equipo(id_equipo,nombre,iniciales);
                equipos.add(equipo);
            }
            rs.close();
            connection.closeConexion();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return equipos;
    }

    /**
     * Verifica si un equipo puede ser eliminado de la base de datos.
     * Un equipo no puede ser eliminado si está participando en algún evento.
     *
     * @param equipo el equipo a verificar
     * @return true si el equipo puede ser eliminado (no tiene participaciones), false en caso contrario
     */
    public static boolean esEliminable(Equipo equipo) {
        ConectorDB connection;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT count(*) as cont FROM Participacion WHERE id_equipo = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, equipo.getIdEquipo());
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
     * Actualiza la información de un equipo en la base de datos.
     *
     * @param equipo el equipo a actualizar
     * @param equipoNuevo los nuevos datos del equipo
     * @return true si la actualización fue exitosa, false si ocurrió un error
     */
    public static boolean modificar(Equipo equipo, Equipo equipoNuevo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE Equipo SET nombre = ?,iniciales = ? WHERE id_equipo = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, equipoNuevo.getNombre());
            pstmt.setString(2, equipoNuevo.getIniciales());
            pstmt.setInt(3, equipo.getIdEquipo());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Actualizado equipo");
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
     * Inserta un nuevo equipo en la base de datos.
     *
     * @param equipo el equipo a insertar
     * @return el ID generado para el nuevo equipo, o -1 si la inserción falló
     */
    public static int insertar(Equipo equipo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO Equipo (nombre,iniciales) VALUES (?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, equipo.getNombre());
            pstmt.setString(2, equipo.getIniciales());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Nueva entrada en equipo");
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
     * Elimina un equipo de la base de datos.
     *
     * @param equipo el equipo a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public static boolean eliminar(Equipo equipo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM Equipo WHERE id_equipo = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, equipo.getIdEquipo());
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
