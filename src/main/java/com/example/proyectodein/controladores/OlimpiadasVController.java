package com.example.proyectodein.controladores;


import com.example.proyectodein.dao.DaoOlimpiada;
import com.example.proyectodein.model.Olimpiada;
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

public class OlimpiadasVController implements Initializable {
    private Olimpiada olimpiada;
    private Olimpiada crear;

    @FXML // fx:id="btnEliminar"
    private Button btnEliminar; // Value injected by FXMLLoader

    @FXML // fx:id="cbOlimpiada"
    private ComboBox<Olimpiada> cbOlimpiada; // Value injected by FXMLLoader

    @FXML // fx:id="rbInvierno"
    private RadioButton rbInvierno; // Value injected by FXMLLoader

    @FXML // fx:id="rbVerano"
    private RadioButton rbVerano; // Value injected by FXMLLoader

    @FXML // fx:id="tgTemporada"
    private ToggleGroup tgTemporada; // Value injected by FXMLLoader

    @FXML // fx:id="txtAnio"
    private TextField txtAnio; // Value injected by FXMLLoader

    @FXML // fx:id="txtCiudad"
    private TextField txtCiudad; // Value injected by FXMLLoader

    @FXML // fx:id="txtNombre"
    private TextField txtNombre; // Value injected by FXMLLoader

    @FXML // fx:id="lblDelete"
    private Label lblDelete; // Value injected by FXMLLoader

    @FXML
    private ResourceBundle resources; // ResourceBundle injected automatically by FXML loader


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        this.olimpiada = null;
        crear = new Olimpiada();
        crear.setIdOlimpiada(0);
        crear.setNombre(resources.getString("cb.new"));
        cargarOlimpiadas();
        // Listener ComboBox
        cbOlimpiada.getSelectionModel().selectedItemProperty().addListener(this::cambioOlimpiada);
    }


    public void cargarOlimpiadas() {
        cbOlimpiada.getItems().clear();
        cbOlimpiada.getItems().add(crear);
        ObservableList<Olimpiada> olimpiadas = DaoOlimpiada.cargarListado();
        cbOlimpiada.getItems().addAll(olimpiadas);
        cbOlimpiada.getSelectionModel().select(0);
    }

    public void cambioOlimpiada(ObservableValue<? extends Olimpiada> observable, Olimpiada oldValue, Olimpiada newValue) {
        if (newValue != null) {
            btnEliminar.setDisable(true);
            lblDelete.setVisible(false);
            if (newValue.equals(crear)) {
                olimpiada = null;
                txtNombre.setText(null);
                txtAnio.setText(null);
                rbInvierno.setSelected(true);
                rbVerano.setSelected(false);
                txtCiudad.setText(null);
            } else {
                olimpiada = newValue;
                txtNombre.setText(olimpiada.getNombre());
                txtAnio.setText(olimpiada.getAnio() + "");
                if (olimpiada.getTemporada().equals("Winter")) {
                    rbInvierno.setSelected(true);
                    rbVerano.setSelected(false);
                } else {
                    rbVerano.setSelected(true);
                    rbInvierno.setSelected(false);
                }
                txtCiudad.setText(olimpiada.getCiudad());
                if (DaoOlimpiada.esEliminable(olimpiada)) {
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
        alert.setContentText(resources.getString("delete.olympics.prompt"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (DaoOlimpiada.eliminar(olimpiada)) {
                confirmacion(resources.getString("delete.olympics.success"));
                cargarOlimpiadas();
            } else {
                alerta(resources.getString("delete.olympics.fail"));
            }
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        String error = validar();
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Olimpiada nuevo = new Olimpiada();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setAnio(Integer.parseInt(txtAnio.getText()));
            if (rbInvierno.isSelected()) {
                nuevo.setTemporada("Winter");
            } else {
                nuevo.setTemporada("Summer");
            }
            nuevo.setCiudad(txtCiudad.getText());
            if (this.olimpiada == null) {
                int id = DaoOlimpiada.insertar(nuevo);
                if (id == -1) {
                    alerta(resources.getString("save.fail"));
                } else {
                    confirmacion(resources.getString("save.olympics"));
                    cargarOlimpiadas();
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
                }
            } else {
                if (DaoOlimpiada.modificar(this.olimpiada, nuevo)) {
                    confirmacion(resources.getString("update.olympics"));
                    cargarOlimpiadas();
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
                } else {
                    alerta(resources.getString("save.fail"));
                }
            }
        }
    }

    public String validar() {
        String error = "";
        if (txtNombre.getText().isEmpty()) {
            error = resources.getString("validate.olympics.name") + "\n";
        }
        if (txtAnio.getText().isEmpty()) {
            error += resources.getString("validate.olympics.year") + "\n";
        } else {
            try {
                Integer.parseInt(txtAnio.getText());
            } catch (NumberFormatException e) {
                error += resources.getString("validate.olympics.year.num") + "\n";
            }
        }
        if (txtCiudad.getText().isEmpty()) {
            error += resources.getString("validate.olympics.city") + "\n";
        }
        return error;
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
