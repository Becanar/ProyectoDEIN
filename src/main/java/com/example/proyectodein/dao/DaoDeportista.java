package com.example.proyectodein.dao;

import com.example.proyectodein.db.ConectorDB;
import com.example.proyectodein.model.Deportista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

/**
 * La clase DaoDeportista proporciona métodos para interactuar con la base de datos
 * y realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los objetos Deportista.
 */
public class DaoDeportista {

    /**
     * Obtiene un deportista de la base de datos mediante su ID.
     *
     * @param id el ID del deportista a buscar
     * @return un objeto Deportista si se encuentra en la base de datos, o null si no se encuentra
     */
    public static Deportista getDeportista(int id) {
        ConectorDB connection;
        Deportista deportista = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_deportista,nombre,sexo,peso,altura,foto FROM Deportista WHERE id_deportista = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_deportista = rs.getInt("id_deportista");
                String nombre = rs.getString("nombre");
                char sexo = rs.getString("sexo").charAt(0);
                int peso = rs.getInt("peso");
                int altura = rs.getInt("altura");
                Blob foto = rs.getBlob("foto");
                deportista = new Deportista(id_deportista,nombre,sexo,peso,altura,foto);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return deportista;
    }

    /**
     * Obtiene un deportista de la base de datos mediante su nombre y sexo.
     *
     * @param str el nombre del deportista
     * @param sx  el sexo del deportista
     * @return un objeto Deportista si se encuentra en la base de datos, o null si no se encuentra
     */
    public static Deportista getDeportista(String str, char sx) {
        ConectorDB connection;
        Deportista deportista = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id_deportista,nombre,sexo,peso,altura,foto FROM Deportista WHERE nombre = ? AND sexo=?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, str);
            pstmt.setString(2, ""+sx);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id_deportista = rs.getInt("id_deportista");
                String nombre = rs.getString("nombre");
                char sexo = rs.getString("sexo").charAt(0);
                int peso = rs.getInt("peso");
                int altura = rs.getInt("altura");
                Blob foto = rs.getBlob("foto");
                deportista = new Deportista(id_deportista,nombre,sexo,peso,altura,foto);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return deportista;
    }

    /**
     * Convierte un archivo de tipo File a un objeto Blob para ser almacenado en la base de datos.
     *
     * @param file el archivo a convertir a Blob
     * @return el Blob generado a partir del archivo
     * @throws SQLException en caso de errores al trabajar con la base de datos
     * @throws IOException  en caso de errores al leer el archivo
     */
    public static Blob convertFileToBlob(File file) throws SQLException, IOException {
        ConectorDB connection = new ConectorDB();
        // Open a connection to the database
        try (Connection conn = connection.getConnection();
             FileInputStream inputStream = new FileInputStream(file)) {

            // Create Blob
            Blob blob = conn.createBlob();
            // Write the file's bytes to the Blob
            byte[] buffer = new byte[1024];
            int bytesRead;

            try (var outputStream = blob.setBinaryStream(1)) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return blob;
        }
    }

    /**
     * Carga una lista de todos los deportistas almacenados en la base de datos.
     *
     * @return una lista observable con los deportistas
     */
    public static ObservableList<Deportista> cargarListado() {
        ConectorDB connection;
        ObservableList<Deportista> deportistas = FXCollections.observableArrayList();
        try{
            connection = new ConectorDB();
            String consulta = "SELECT id_deportista,nombre,sexo,peso,altura,foto FROM Deportista";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_deportista = rs.getInt("id_deportista");
                String nombre = rs.getString("nombre");
                char sexo = rs.getString("sexo").charAt(0);
                int peso = rs.getInt("peso");
                int altura = rs.getInt("altura");
                Blob foto = rs.getBlob("foto");
                Deportista deportista = new Deportista(id_deportista,nombre,sexo,peso,altura,foto);
                deportistas.add(deportista);
            }
            rs.close();
            connection.closeConexion();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return deportistas;
    }

    /**
     * Verifica si un deportista puede ser eliminado de la base de datos.
     * Un deportista no puede ser eliminado si está participando en algún evento.
     *
     * @param deportista el deportista a verificar
     * @return true si el deportista puede ser eliminado (no tiene participaciones), false en caso contrario
     */
    public static boolean esEliminable(Deportista deportista) {
        ConectorDB connection;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT count(*) as cont FROM Participacion WHERE id_deportista = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, deportista.getIdDeportista());
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
     * Actualiza la información de un deportista en la base de datos.
     *
     * @param deportista     el deportista a actualizar
     * @param deportistaNuevo los nuevos datos del deportista
     * @return true si la actualización fue exitosa, false si ocurrió un error
     */
    public static boolean modificar(Deportista deportista, Deportista deportistaNuevo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE Deportista SET nombre = ?,sexo = ?,peso = ?,altura = ?,foto = ? WHERE id_deportista = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, deportistaNuevo.getNombre());
            pstmt.setString(2, deportistaNuevo.getSexo() + "");
            pstmt.setInt(3, deportistaNuevo.getPeso());
            pstmt.setInt(4, deportistaNuevo.getAltura());
            pstmt.setBlob(5, deportistaNuevo.getFoto());
            pstmt.setInt(6, deportista.getIdDeportista());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Actualizado deportista");
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
     * Inserta un nuevo deportista en la base de datos.
     *
     * @param deportista el deportista a insertar
     * @return el ID generado para el nuevo deportista, o -1 si la inserción falló
     */
    public static int insertar(Deportista deportista) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO Deportista (nombre,sexo,peso,altura,foto) VALUES (?,?,?,?,?) ";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, deportista.getNombre());
            pstmt.setString(2, deportista.getSexo() + "");
            pstmt.setInt(3, deportista.getPeso());
            pstmt.setInt(4, deportista.getAltura());
            pstmt.setBlob(5, deportista.getFoto());
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println("Nueva entrada en deportista");
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
     * Elimina un deportista de la base de datos.
     *
     * @param deportista el deportista a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public static boolean eliminar(Deportista deportista) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM Deportista WHERE id_deportista = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, deportista.getIdDeportista());
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
