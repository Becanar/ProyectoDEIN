package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.DaoEquipo;
import com.example.proyectodein.model.Equipo;
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

public class EquiposController implements Initializable {
    private Equipo equipo;
    private Equipo crear;

    @FXML // fx:id="btnEliminar"
    private Button btnEliminar; // Value injected by FXMLLoader

    @FXML // fx:id="cbEquipo"
    private ComboBox<Equipo> cbEquipo; // Value injected by FXMLLoader

    @FXML // fx:id="txtIniciales"
    private TextField txtIniciales; // Value injected by FXMLLoader

    @FXML // fx:id="txtNombre"
    private TextField txtNombre; // Value injected by FXMLLoader

    @FXML // fx:id="lblDelete"
    private Label lblDelete; // Value injected by FXMLLoader

    @FXML
    private ResourceBundle resources; // ResourceBundle injected automatically by FXML loader

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        this.equipo = null;
        crear = new Equipo();
        crear.setIdEquipo(0);
        crear.setNombre(resources.getString("cb.new"));
        cargarEquipos();
        // Listener ComboBox
        cbEquipo.getSelectionModel().selectedItemProperty().addListener(this::cambioEquipo);
    }

    public void cargarEquipos() {
        cbEquipo.getItems().clear();
        cbEquipo.getItems().add(crear);
        ObservableList<Equipo> equipos = DaoEquipo.cargarListado();
        cbEquipo.getItems().addAll(equipos);
        cbEquipo.getSelectionModel().select(0);
    }

    public void cambioEquipo(ObservableValue<? extends Equipo> observable, Equipo oldValue, Equipo newValue) {
        if (newValue != null) {
            btnEliminar.setDisable(true);
            lblDelete.setVisible(false);
            if (newValue.equals(crear)) {
                equipo = null;
                txtNombre.setText(null);
                txtIniciales.setText(null);
            } else {
                equipo = newValue;
                txtNombre.setText(equipo.getNombre());
                txtIniciales.setText(equipo.getIniciales());
                if (DaoEquipo.esEliminable(equipo)) {
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
        alert.setContentText(resources.getString("delete.teams.prompt"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (DaoEquipo.eliminar(equipo)) {
                confirmacion(resources.getString("delete.teams.success"));
                cargarEquipos();
                Stage stage = (Stage) txtNombre.getScene().getWindow();
                stage.close();
            } else {
                alerta(resources.getString("delete.teams.fail"));
            }
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        String error = "";
        if (txtNombre.getText().isEmpty()) {
            error = resources.getString("validate.teams.name") + "\n";
        }
        if (txtIniciales.getText().isEmpty()) {
            error +=  resources.getString("validate.teams.noc") + "\n";
        } else {
            if (txtIniciales.getText().length() > 3) {
                error +=  resources.getString("validate.teams.noc.num") +  "\n";
            }
        }
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Equipo nuevo = new Equipo();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setIniciales(txtIniciales.getText());
            if (this.equipo == null) {
                int id = DaoEquipo.insertar(nuevo);
                if (id == -1) {
                    alerta(resources.getString("save.fail"));
                } else {
                    confirmacion(resources.getString("save.teams"));
                    cargarEquipos();
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
                }
            } else {
                if (DaoEquipo.modificar(equipo, nuevo)) {
                    confirmacion(resources.getString("update.teams"));
                    cargarEquipos();
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
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
