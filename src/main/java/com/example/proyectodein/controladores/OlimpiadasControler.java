package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.DaoDeporte;
import com.example.proyectodein.dao.DaoDeportista;
import com.example.proyectodein.dao.DaoEquipo;
import com.example.proyectodein.dao.DaoEvento;
import com.example.proyectodein.dao.DaoOlimpiada;
import com.example.proyectodein.model.Deportista;
import com.example.proyectodein.model.Equipo;
import com.example.proyectodein.model.Evento;
import com.example.proyectodein.model.Olimpiada;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class OlimpiadasControler {

    @FXML
    private TableView tablaVista; // Utilizando Object como tipo base

    @FXML
    private ComboBox<String> comboBoxDatos;

    @FXML
    private TextField txtNombre;

    @FXML
    private void initialize() {
        cargarDatosComboBox();
        comboBoxDatos.setValue("Olimpiadas");
        actualizarTabla(null);
        comboBoxDatos.setOnAction(this::actualizarTabla);
    }

    private void cargarDatosComboBox() {
        ObservableList<String> opciones = FXCollections.observableArrayList(
                "Olimpiadas", "Deportistas", "Equipos", "Eventos", "Deportes"
        );
        comboBoxDatos.setItems(opciones);
    }

    public void actualizarTabla(ActionEvent event) {
        String seleccion = comboBoxDatos.getValue();
        tablaVista.getItems().clear();
        tablaVista.getColumns().clear(); // Limpiar columnas antes de configurar nuevas

        switch (seleccion) {
            case "Olimpiadas":
                cargarOlimpiadas();
                break;
            case "Deportistas":
                cargarDeportistas();
                break;
            case "Equipos":
                cargarEquipos();
                break;
            case "Eventos":
                cargarEventos();
                break;
            case "Deportes":
                cargarDeportes();
                break;
            default:
                break;
        }
    }

    private void cargarOlimpiadas() {
        TableColumn<Olimpiada, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Olimpiada, Integer> colAño = new TableColumn<>("Año");
        colAño.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAnio()).asObject());

        TableColumn<Olimpiada, String> colCiudad = new TableColumn<>("Ciudad");
        colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCiudad()));

        tablaVista.getColumns().addAll(colNombre, colAño, colCiudad);
        ObservableList<Olimpiada> olimpiadas = DaoOlimpiada.cargarListado();
        tablaVista.setItems(olimpiadas);
    }

    private void cargarDeportistas() {
        TableColumn<Deportista, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Deportista, String> colDeporte = new TableColumn<>("Sexo");
        colDeporte.setCellValueFactory(cellData -> new SimpleStringProperty(""+cellData.getValue().getSexo()));

        tablaVista.getColumns().addAll(colNombre, colDeporte);
        ObservableList<Deportista> deportistas = DaoDeportista.cargarListado();
        tablaVista.setItems(deportistas);
    }

    private void cargarEquipos() {
        TableColumn<Equipo, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Equipo, String> colPais = new TableColumn<>("Siglas");
        colPais.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIniciales()));

        tablaVista.getColumns().addAll(colNombre, colPais);
        ObservableList<Equipo> equipos = DaoEquipo.cargarListado();
        tablaVista.setItems(equipos);
    }

    private void cargarEventos() {
        TableColumn<Evento, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id_evento"));

        TableColumn<Evento, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Evento, String> colOlimpiada = new TableColumn<>("Olimpiada");
        colOlimpiada.setCellValueFactory(cellData -> new SimpleStringProperty(DaoOlimpiada.getOlimpiada(cellData.getValue().getIdOlimpiada()).getNombre()));

        TableColumn<Evento, String> colDeporte = new TableColumn<>("Deporte");
        colDeporte.setCellValueFactory(cellData -> new SimpleStringProperty((DaoDeporte.getDeporte(cellData.getValue().getIdDeporte()).getNombre())));

        tablaVista.getColumns().addAll(colId, colNombre, colOlimpiada, colDeporte);
        ObservableList<Evento> eventos = DaoEvento.cargarListado();
        tablaVista.setItems(eventos);
    }

    private void cargarDeportes() {
        // Implementa la carga de deportes aquí
    }

    public void cambiarIngles(ActionEvent actionEvent) {
        // Lógica para cambiar idioma a inglés
    }

    public void cambiarEsp(ActionEvent actionEvent) {
        // Lógica para cambiar idioma a español
    }

    public void cambiarEus(ActionEvent actionEvent) {
        // Lógica para cambiar idioma a euskera
    }
}
