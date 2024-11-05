package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class olimpiadasControler {

    @FXML
    private MenuBar barraMenu;

    @FXML
    private ImageView imgOlD;

    @FXML
    private ImageView imgOlI;

    @FXML
    private ImageView imgOlI1;

    @FXML
    private Label lblCombo;

    @FXML
    private Label lblGestion;

    @FXML
    private Menu menuAyuda;

    @FXML
    private MenuItem menuEn;

    @FXML
    private MenuItem menuEs;

    @FXML
    private MenuItem menuEus;

    @FXML
    private Menu menuIdioma;

    @FXML
    private HBox panelHueco;

    @FXML
    private FlowPane panelListado;

    @FXML
    private VBox rootPane;

    @FXML
    private TableView<Object> tablaVista; // Cambia Object por tu tipo de datos real

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<String> comboBoxDatos;

    // Inicializador para cargar datos
    @FXML
    public void initialize() {
        // Cargar datos en el ComboBox
        cargarDatosComboBox();

        // Seleccionar "Olimpiadas" por defecto
        comboBoxDatos.setValue("Olimpiadas");

        // Actualizar la tabla con los datos de olimpiadas
        actualizarTabla(null); // Llama a actualizarTabla sin un evento

        // Configurar el listener del ComboBox
        comboBoxDatos.setOnAction(this::actualizarTabla);
    }

    private void cargarDatosComboBox() {
        ObservableList<String> opciones = FXCollections.observableArrayList();

        // Agregar opciones al ComboBox
        opciones.add("Deportes");
        opciones.add("Deportistas");
        opciones.add("Equipos");
        opciones.add("Eventos");
        opciones.add("Olimpiadas"); // Asegúrate de que esta opción esté incluida

        comboBoxDatos.setItems(opciones);
    }

    public void actualizarTabla(ActionEvent event) {
        String seleccion = comboBoxDatos.getValue();

        if (seleccion == null) {
            System.out.println("No se ha seleccionado ninguna opción.");
            return;
        }

        tablaVista.getItems().clear();

        switch (seleccion) {
            case "Deportes":
                System.out.println("Deportes");
                tablaVista.setItems(FXCollections.observableArrayList(DaoDeporte.cargarListado()));
                break;
            case "Deportistas":
                System.out.println("Deportistas");
                tablaVista.setItems(FXCollections.observableArrayList(DaoDeportista.cargarListado()));
                break;
            case "Equipos":
                System.out.println("Equipos");
                tablaVista.setItems(FXCollections.observableArrayList(DaoEquipo.cargarListado()));
                break;
            case "Eventos":
                System.out.println("Eventos");
                tablaVista.setItems(FXCollections.observableArrayList(DaoEvento.cargarListado()));
                break;
            case "Olimpiadas":
                System.out.println("Olimpiadas");
                tablaVista.setItems(FXCollections.observableArrayList(DaoOlimpiada.cargarListado()));
                break;
            default:
                break;
        }
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
