package com.example.proyectodein.controladores;


import com.example.proyectodein.dao.DaoDeporte;
import com.example.proyectodein.model.Deporte;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeportesController implements Initializable {
    private Deporte deporte;
    private Deporte crear;

    @FXML // fx:id="btnEliminar"
    private Button btnEliminar; // Value injected by FXMLLoader

    @FXML // fx:id="cbDeporte"
    private ComboBox<Deporte> cbDeporte; // Value injected by FXMLLoader

    @FXML // fx:id="txtNombre"
    private TextField txtNombre; // Value injected by FXMLLoader

    @FXML // fx:id="lblDelete"
    private Label lblDelete; // Value injected by FXMLLoader

    @FXML
    private ResourceBundle resources; // ResourceBundle injected automatically by FXML loader

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        this.deporte = null;
        crear = new Deporte();
        crear.setIdDeporte(0);
        crear.setNombre(resources.getString("cb.new"));
        cargarDeportes();
        // Listener ComboBox
        cbDeporte.getSelectionModel().selectedItemProperty().addListener(this::cambioDeporte);
    }

    public void cargarDeportes() {
        cbDeporte.getItems().clear();
        cbDeporte.getItems().add(crear);
        ObservableList<Deporte> deportes = DaoDeporte.cargarListado();
        cbDeporte.getItems().addAll(deportes);
        cbDeporte.getSelectionModel().select(0);
    }

    public void cambioDeporte(ObservableValue<? extends Deporte> observable, Deporte oldValue, Deporte newValue) {
        if (newValue != null) {
            btnEliminar.setDisable(true);
            lblDelete.setVisible(false);
            if (newValue.equals(crear)) {
                deporte = null;
                txtNombre.setText(null);
            } else {
                deporte = newValue;
                txtNombre.setText(deporte.getNombre());
                if (DaoDeporte.esEliminable(deporte)) {
                    btnEliminar.setDisable(false);
                } else {
                    lblDelete.setVisible(true);
                }
            }
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage)txtNombre.getScene().getWindow();
        stage.close();
    }

    @FXML
    void eliminar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(txtNombre.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle(resources.getString("window.confirm"));
        alert.setContentText(resources.getString("delete.sports.prompt"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (DaoDeporte.eliminar(deporte)) {
                confirmacion(resources.getString("delete.sports.success"));
                cargarDeportes();
            } else {
                alerta(resources.getString("delete.sports.fail"));
            }
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        if (txtNombre.getText().isEmpty()) {
            alerta(resources.getString("validate.sports.name"));
        } else {
            Deporte nuevo = new Deporte();
            nuevo.setNombre(txtNombre.getText());
            if (this.deporte == null) {
                int id = DaoDeporte.insertar(nuevo);
                if (id == -1) {
                    alerta(resources.getString("save.fail"));
                } else {
                    confirmacion(resources.getString("save.sports"));
                    cargarDeportes();
                }
            } else {
                if (DaoDeporte.modificar(this.deporte, nuevo)) {
                    confirmacion(resources.getString("update.sports"));
                    cargarDeportes();
                } else {
                    alerta(resources.getString("save.fail"));
                }
            }
        }
    }

    public void alerta(String texto) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("Error");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

}
