package com.example.proyectodein.dao;

import com.example.proyectodein.db.ConectorDB;
import com.example.proyectodein.model.Olimpiada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoOlimpiada {

    /**
     * Obtiene una olimpiada de la base de datos dado su ID.
     *
     * @param id El ID de la olimpiada a buscar.
     * @return La olimpiada correspondiente al ID, o null si no se encuentra.
     */
    public static Olimpiada getOlimpiada(int id) {
        ConectorDB connection;
        Olimpiada olimpiada = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_olimpiada,nombre,anio,temporada,ciudad FROM Olimpiada WHERE id_olimpiada = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_olimpiada = rs.getInt("id_olimpiada");
                String nombre = rs.getString("nombre");
                int anio = rs.getInt("anio");
                String temporada = rs.getString("temporada");
                String ciudad = rs.getString("ciudad");
                olimpiada = new Olimpiada(id_olimpiada,nombre,anio,temporada,ciudad);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return olimpiada;
    }

    /**
     * Obtiene una olimpiada de la base de datos dado su nombre.
     *
     * @param str El nombre de la olimpiada.
     * @return La olimpiada correspondiente al nombre, o null si no se encuentra.
     */
    public static Olimpiada getOlimpiada(String str) {
        ConectorDB connection;
        Olimpiada olimpiada = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_olimpiada,nombre,anio,temporada,ciudad FROM Olimpiada WHERE nombre = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, str);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_olimpiada = rs.getInt("id_olimpiada");
                String nombre = rs.getString("nombre");
                int anio = rs.getInt("anio");
                String temporada = rs.getString("temporada");
                String ciudad = rs.getString("ciudad");
                olimpiada = new Olimpiada(id_olimpiada,nombre,anio,temporada,ciudad);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return olimpiada;
    }

    /**
     * Carga todas las olimpiadas de la base de datos.
     *
     * @return Una lista observable de todas las olimpiadas en la base de datos.
     */
    public static ObservableList<Olimpiada> cargarListado() {
        ConectorDB connection;
        ObservableList<Olimpiada> olimpiadas = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id_olimpiada,nombre,anio,temporada,ciudad FROM Olimpiada";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_olimpiada = rs.getInt("id_olimpiada");
                String nombre = rs.getString("nombre");
                int anio = rs.getInt("anio");
                String temporada = rs.getString("temporada");
                String ciudad = rs.getString("ciudad");
                Olimpiada olimpiada = new Olimpiada(id_olimpiada,nombre,anio,temporada,ciudad);
                olimpiadas.add(olimpiada);
            }
            rs.close();
            connection.closeConexion();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return olimpiadas;
    }

    /**
     * Verifica si una olimpiada es eliminable de la base de datos.
     * Una olimpiada es eliminable si no tiene eventos asociados.
     *
     * @param olimpiada La olimpiada a verificar.
     * @return true si la olimpiada es eliminable, false en caso contrario.
     */
    public static boolean esEliminable(Olimpiada olimpiada) {
        ConectorDB connection;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT count(*) as cont FROM Evento WHERE id_olimpiada = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, olimpiada.getIdOlimpiada());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int cont = rs.getInt("cont");
                rs.close();
                connection.closeConexion();
                return (cont==0);
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
     * Modifica los detalles de una olimpiada en la base de datos.
     *
     * @param olimpiada La olimpiada a modificar.
     * @param olimpiadaNuevo La nueva olimpiada con los valores actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public static boolean modificar(Olimpiada olimpiada, Olimpiada olimpiadaNuevo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE Olimpiada SET nombre = ?,anio = ?,temporada = ?,ciudad = ? WHERE id_olimpiada = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, olimpiadaNuevo.getNombre());
            pstmt.setInt(2, olimpiadaNuevo.getAnio());
            pstmt.setString(3, olimpiadaNuevo.getTemporada().toString());
            pstmt.setString(4, olimpiadaNuevo.getCiudad());
            pstmt.setInt(5, olimpiada.getIdOlimpiada());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Actualizado olimpiada");
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
     * Inserta una nueva olimpiada en la base de datos.
     *
     * @param olimpiada La olimpiada a insertar.
     * @return El ID generado para la nueva olimpiada, o -1 si la inserción falla.
     */
    public  static int insertar(Olimpiada olimpiada) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO Olimpiada (nombre,anio,temporada,ciudad) VALUES (?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, olimpiada.getNombre());
            pstmt.setInt(2, olimpiada.getAnio());
            pstmt.setString(3, olimpiada.getTemporada().toString());
            pstmt.setString(4, olimpiada.getCiudad());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Nueva entrada en olimpiada");
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
     * Elimina una olimpiada de la base de datos.
     *
     * @param olimpiada La olimpiada a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public static boolean eliminar(Olimpiada olimpiada) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM Olimpiada WHERE id_olimpiada = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, olimpiada.getIdOlimpiada());
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
