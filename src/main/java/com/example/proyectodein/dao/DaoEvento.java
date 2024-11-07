package com.example.proyectodein.dao;

import com.example.proyectodein.db.ConectorDB;
import com.example.proyectodein.model.Deporte;
import com.example.proyectodein.model.Evento;
import com.example.proyectodein.model.Olimpiada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoEvento {

    /**
     * Obtiene un evento de la base de datos dado su ID.
     *
     * @param id El ID del evento a buscar.
     * @return El evento correspondiente al ID, o null si no se encuentra.
     */
    public static Evento getEvento(int id) {
        ConectorDB connection;
        Evento evento = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_evento,nombre,id_olimpiada,id_deporte FROM Evento WHERE id_evento = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_evento = rs.getInt("id_evento");
                String nombre = rs.getString("nombre");
                int id_olimpiada = rs.getInt("id_olimpiada");
                Olimpiada olimpiada = DaoOlimpiada.getOlimpiada(id_olimpiada);
                int id_deporte = rs.getInt("id_deporte");
                Deporte deporte = DaoDeporte.getDeporte(id_deporte);
                evento = new Evento(id_evento,nombre,olimpiada.getIdOlimpiada(),deporte.getIdDeporte());
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return evento;
    }

    /**
     * Obtiene un evento de la base de datos buscando por nombre, deporte y olimpiada.
     *
     * @param str El nombre del evento.
     * @param dep El ID del deporte.
     * @param ol El ID de la olimpiada.
     * @return El evento correspondiente, o null si no se encuentra.
     */
    public static Evento getEvento(String str,int dep,int ol ) {
        ConectorDB connection;
        Evento evento = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_evento,nombre,id_olimpiada,id_deporte FROM Evento WHERE nombre = ? AND id_olimpiada=? AND id_deporte=?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, str);
            pstmt.setInt(2, ol);
            pstmt.setInt(3, dep);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_evento = rs.getInt("id_evento");
                String nombre = rs.getString("nombre");
                int id_olimpiada = rs.getInt("id_olimpiada");
                Olimpiada olimpiada = DaoOlimpiada.getOlimpiada(id_olimpiada);
                int id_deporte = rs.getInt("id_deporte");
                Deporte deporte = DaoDeporte.getDeporte(id_deporte);
                evento = new Evento(id_evento,nombre,olimpiada.getIdOlimpiada(),deporte.getIdDeporte());
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return evento;
    }

    /**
     * Verifica si un evento es eliminable de la base de datos.
     * Un evento es eliminable si no tiene participaciones asociadas.
     *
     * @param evento El evento a verificar.
     * @return true si el evento es eliminable, false en caso contrario.
     */
    public static boolean esEliminable(Evento evento) {
        ConectorDB connection;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT count(*) as cont FROM Participacion WHERE id_evento = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, evento.getIdEvento());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int cont = rs.getInt("cont");
                rs.close();
                connection.getConnection();
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
     * Carga todos los eventos de la base de datos.
     *
     * @return Una lista observable de todos los eventos en la base de datos.
     */
    public static ObservableList<Evento> cargarListado() {
        ConectorDB connection;
        ObservableList<Evento> eventos = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id_evento,nombre,id_olimpiada,id_deporte FROM Evento";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_evento = rs.getInt("id_evento");
                String nombre = rs.getString("nombre");
                int id_olimpiada = rs.getInt("id_olimpiada");
                Olimpiada olimpiada = DaoOlimpiada.getOlimpiada(id_olimpiada);
                int id_deporte = rs.getInt("id_deporte");
                Deporte deporte = DaoDeporte.getDeporte(id_deporte);
                Evento evento = new Evento(id_evento,nombre,olimpiada.getIdOlimpiada(),deporte.getIdDeporte());
                eventos.add(evento);
            }
            rs.close();
            connection.closeConexion();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return eventos;
    }

    /**
     * Modifica los detalles de un evento en la base de datos.
     *
     * @param evento El evento a modificar.
     * @param eventoNuevo El nuevo evento con los valores actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public static boolean modificar(Evento evento, Evento eventoNuevo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE Evento SET nombre = ?,id_olimpiada = ?,id_deporte = ? WHERE id_evento = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, eventoNuevo.getNombre());
            pstmt.setInt(2, eventoNuevo.getIdOlimpiada());
            pstmt.setInt(3, eventoNuevo.getIdDeporte());
            pstmt.setInt(4, evento.getIdEvento());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Actualizado evento");
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
     * Inserta un nuevo evento en la base de datos.
     *
     * @param evento El evento a insertar.
     * @return El ID generado para el nuevo evento, o -1 si la inserción falla.
     */
    public  static int insertar(Evento evento) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO Evento (nombre,id_olimpiada,id_deporte) VALUES (?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, evento.getNombre());
            pstmt.setInt(2, evento.getIdOlimpiada());
            pstmt.setInt(3, evento.getIdDeporte());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Nueva entrada en evento");
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
     * Elimina un evento de la base de datos.
     *
     * @param evento El evento a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public static boolean eliminar(Evento evento) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM Evento WHERE id_evento = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, evento.getIdEvento());
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
