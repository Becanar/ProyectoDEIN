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

public class DaoDeportista {

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

     public  static int insertar(Deportista deportista) {
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
