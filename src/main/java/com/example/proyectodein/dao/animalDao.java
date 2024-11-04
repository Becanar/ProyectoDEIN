package com.example.proyectodein.dao;

import com.example.proyectodein.db.ConectorDB;
import com.example.proyectodein.model.Animal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

/**
 * La clase {@code AnimalDao} proporciona métodos para realizar operaciones
 * de acceso a datos relacionadas con la entidad {@link Animal}.
 * Esto incluye la recuperación, modificación, inserción y eliminación
 * de información sobre animales en la base de datos.
 */
public class animalDao {

    /**
     * Obtiene un animal de la base de datos a partir de su ID.
     *
     * @param id el ID del animal a recuperar
     * @return un objeto {@link Animal} que contiene la información del animal,
     *         o {@code null} si no se encuentra ningún animal con el ID proporcionado
     */
    public static Animal getAnimal(int id) {
        ConectorDB connection;
        Animal animal = null;
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id, nombre, especie, raza, sexo, edad, peso, observaciones, fecha_primera_consulta, foto FROM animales WHERE id = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int animalId = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String especie = rs.getString("especie");
                String raza = rs.getString("raza");
                String sexo = rs.getString("sexo");
                int edad = rs.getInt("edad");
                double peso = rs.getDouble("peso");
                String observaciones = rs.getString("observaciones");
                Date fecha_primera_consulta = rs.getDate("fecha_primera_consulta");
                Blob foto = rs.getBlob("foto");
                animal = new Animal(animalId, nombre, especie, raza, sexo, edad, peso, observaciones, fecha_primera_consulta, foto);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return animal;
    }

    /**
     * Carga una lista de todos los animales en la base de datos.
     *
     * @return una lista observable de objetos {@link Animal}
     */
    public static ObservableList<Animal> cargarListado() {
        ConectorDB connection;
        ObservableList<Animal> animalList = FXCollections.observableArrayList();
        try {
            connection = new ConectorDB();
            String consulta = "SELECT id, nombre, especie, raza, sexo, edad, peso, observaciones, fecha_primera_consulta, foto FROM animales";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String especie = rs.getString("especie");
                String raza = rs.getString("raza");
                String sexo = rs.getString("sexo");
                int edad = rs.getInt("edad");
                double peso = rs.getDouble("peso");
                String observaciones = rs.getString("observaciones");
                Date fecha_primera_consulta = rs.getDate("fecha_primera_consulta");
                Blob foto = rs.getBlob("foto");
                Animal animal = new Animal(id, nombre, especie, raza, sexo, edad, peso, observaciones, fecha_primera_consulta, foto);
                animalList.add(animal);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return animalList;
    }

    /**
     * Modifica la información de un animal en la base de datos.
     *
     * @param animal el animal existente que se desea modificar
     * @param animalNuevo el nuevo objeto {@link Animal} que contiene la información actualizada
     * @return {@code true} si la modificación fue exitosa; {@code false} en caso contrario
     */
    public static boolean modificar(Animal animal, Animal animalNuevo) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "UPDATE animales SET nombre = ?, especie = ?, raza = ?, sexo = ?, edad = ?, peso = ?, observaciones = ?, fecha_primera_consulta = ?, foto = ? WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, animalNuevo.getNombre());
            pstmt.setString(2, animalNuevo.getEspecie());
            pstmt.setString(3, animalNuevo.getRaza());
            pstmt.setString(4, animalNuevo.getSexo());
            pstmt.setInt(5, animalNuevo.getEdad());
            pstmt.setDouble(6, animalNuevo.getPeso());
            pstmt.setString(7, animalNuevo.getObservaciones());
            pstmt.setDate(8, new java.sql.Date(animalNuevo.getFechaPrimeraConsulta().getTime()));
            pstmt.setBlob(9, animalNuevo.getFoto());
            pstmt.setInt(10, animal.getId());
            int filasAfectadas = pstmt.executeUpdate();
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
     * Inserta un nuevo animal en la base de datos.
     *
     * @param animal el animal a insertar
     * @return el ID del nuevo animal si la inserción fue exitosa; -1 en caso contrario
     */
    public static int insertar(Animal animal) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "INSERT INTO animales (nombre, especie, raza, sexo, edad, peso, observaciones, fecha_primera_consulta, foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, animal.getNombre());
            pstmt.setString(2, animal.getEspecie());
            pstmt.setString(3, animal.getRaza());
            pstmt.setString(4, animal.getSexo());
            pstmt.setInt(5, animal.getEdad());
            pstmt.setDouble(6, animal.getPeso());
            pstmt.setString(7, animal.getObservaciones());
            pstmt.setDate(8, new java.sql.Date(animal.getFechaPrimeraConsulta().getTime()));
            pstmt.setBlob(9, animal.getFoto());
            int filasAfectadas = pstmt.executeUpdate();
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
     * Elimina un animal de la base de datos.
     *
     * @param animal el animal que se desea eliminar
     * @return {@code true} si la eliminación fue exitosa; {@code false} en caso contrario
     */
    public static boolean eliminar(Animal animal) {
        ConectorDB connection;
        PreparedStatement pstmt;
        try {
            connection = new ConectorDB();
            String consulta = "DELETE FROM animales WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, animal.getId());
            int filasAfectadas = pstmt.executeUpdate();
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
     * Convierte un archivo en un objeto Blob para almacenarlo en una base de datos.
     *
     * <p>Este método lee los datos de un archivo y los convierte en un Blob mediante
     * un flujo de bytes. El Blob resultante puede utilizarse para almacenar datos binarios
     * en la base de datos. La conexión a la base de datos se gestiona a través de
     * la clase ConectorDB.</p>
     *
     * @param file el archivo que se desea convertir en un Blob.
     * @return un objeto Blob que contiene los datos binarios del archivo proporcionado.
     * @throws SQLException si ocurre un error al crear el Blob o con la conexión a la base de datos.
     * @throws FileNotFoundException si el archivo especificado no se encuentra o no se puede abrir.
     * @throws RuntimeException si ocurre un error de entrada/salida al leer el archivo.
     */
    public static Blob convertFileToBlob(File file) throws SQLException, FileNotFoundException {
        ConectorDB connection = new ConectorDB();
        try (Connection conn = connection.getConnection();
             FileInputStream inputStream = new FileInputStream(file)) {

            Blob blob = conn.createBlob();
            byte[] buffer = new byte[1024];
            int bytesRead;

            try (var outputStream = blob.setBinaryStream(1)) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return blob;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
