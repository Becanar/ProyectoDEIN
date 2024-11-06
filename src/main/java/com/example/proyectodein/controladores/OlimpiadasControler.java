package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.DaoDeporte;
import com.example.proyectodein.dao.DaoDeportista;
import com.example.proyectodein.dao.DaoEquipo;
import com.example.proyectodein.dao.DaoEvento;
import com.example.proyectodein.dao.DaoOlimpiada;
import com.example.proyectodein.model.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

public class OlimpiadasControler {
    @FXML
    private VBox rootPane;
    @FXML
    private TableView tablaVista;

    @FXML
    private ComboBox<String> comboBoxDatos;

    @FXML
    private TextField txtNombre;

    private ObservableList<Object> lstEntera = FXCollections.observableArrayList();
    private ObservableList<Object> lstFiltrada = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        cargarDatosComboBox();
        comboBoxDatos.setValue("Olimpiadas");
        actualizarTabla(null);
        comboBoxDatos.setOnAction(this::actualizarTabla);
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Editar");
        editItem.setOnAction(event -> editar(null));


        MenuItem deleteItem = new MenuItem("Borrar");
        deleteItem.setOnAction(event -> borrar(null));

        contextMenu.getItems().addAll(editItem, deleteItem);

        tablaVista.setContextMenu(contextMenu);
        tablaVista.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                infoR(null);
            }
        });
        rootPane.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.F) {
                txtNombre.requestFocus();
                event.consume();
            }
        });
        txtNombre.setOnKeyTyped(keyEvent -> filtrar());
    }

    private void filtrar() {
        String valor = txtNombre.getText();
        if (valor == null || valor.isEmpty()) {
            tablaVista.setItems(lstEntera);
        } else {
            valor = valor.toLowerCase();
            lstFiltrada.clear();
            for (Object item : lstEntera) {
                String nombre;
                if (item instanceof Olimpiada) {
                    nombre = ((Olimpiada) item).getNombre();
                } else if (item instanceof Deportista) {
                    nombre = ((Deportista) item).getNombre();
                } else if (item instanceof Equipo) {
                    nombre = ((Equipo) item).getNombre();
                } else if (item instanceof Evento) {
                    nombre = ((Evento) item).getNombre();
                } else if (item instanceof Deporte) {
                    nombre = ((Deporte) item).getNombre();
                } else {
                    continue;
                }
                if (nombre.toLowerCase().contains(valor)) {
                    lstFiltrada.add(item);
                }
            }
            tablaVista.setItems(lstFiltrada);
        }
    }


    private void infoR(Object o) {
    }

    private void borrar(Object o) {
    }

    private void editar(Object o) {
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
        lstEntera.setAll(DaoOlimpiada.cargarListado());
        tablaVista.setItems(lstEntera);
    }


    private void cargarDeportistas() {
        TableColumn<Deportista, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Deportista, String> colSexo = new TableColumn<>("Sexo");
        colSexo.setCellValueFactory(cellData -> new SimpleStringProperty(""+cellData.getValue().getSexo()));

        tablaVista.getColumns().addAll(colNombre, colSexo);
        lstEntera.setAll(DaoDeportista.cargarListado());
        tablaVista.setItems(lstEntera);
    }

    private void cargarEquipos() {
        TableColumn<Equipo, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Equipo, String> colPais = new TableColumn<>("Siglas");
        colPais.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIniciales()));

        tablaVista.getColumns().addAll(colNombre, colPais);
        lstEntera.setAll(DaoEquipo.cargarListado());
        tablaVista.setItems(lstEntera);
    }


    private void cargarEventos() {
        TableColumn<Evento, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Evento, String> colOlimpiada = new TableColumn<>("Olimpiada");
        colOlimpiada.setCellValueFactory(cellData -> new SimpleStringProperty(DaoOlimpiada.getOlimpiada(cellData.getValue().getIdOlimpiada()).getNombre()));

        TableColumn<Evento, String> colDeporte = new TableColumn<>("Deporte");
        colDeporte.setCellValueFactory(cellData -> new SimpleStringProperty(DaoDeporte.getDeporte(cellData.getValue().getIdDeporte()).getNombre()));

        tablaVista.getColumns().addAll(colNombre, colOlimpiada, colDeporte);
        lstEntera.setAll(DaoEvento.cargarListado());
        tablaVista.setItems(lstEntera);
    }


    private void cargarDeportes() {
        TableColumn<Deporte, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tablaVista.getColumns().addAll(colNombre);
        lstEntera.setAll(DaoDeporte.cargarListado());
        tablaVista.setItems(lstEntera);
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

    public void aniadir(ActionEvent event) {
    }
}
