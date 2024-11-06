package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.DaoOlimpiada;
import com.example.proyectodein.model.Olimpiada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class OlimpiadasVController implements Initializable {

    private Olimpiada olimpiada; // La olimpiada que estamos editando o creando

    // Elementos del FXML
    @FXML
    private Button btnEliminar;
    @FXML
    private RadioButton rbInvierno;
    @FXML
    private RadioButton rbVerano;
    @FXML
    private ToggleGroup tgTemporada;
    @FXML
    private TextField txtAnio;
    @FXML
    private TextField txtCiudad;
    @FXML
    private TextField txtNombre;
    @FXML
    private Label lblDelete;

    private ResourceBundle resources;

    // Constructor vacío para la creación de una nueva olimpiada
    public OlimpiadasVController() {
        this.olimpiada = null;
    }

    // Constructor para la edición de una olimpiada existente
    public OlimpiadasVController(Olimpiada olimpiada) {
        this.olimpiada = olimpiada;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;

        if (olimpiada == null) {
            // Crear una nueva olimpiada (Campos vacíos)
            txtNombre.setText("");
            txtAnio.setText("");
            txtCiudad.setText("");
            rbInvierno.setSelected(true);
            rbVerano.setSelected(false);
            btnEliminar.setDisable(true);
            lblDelete.setVisible(false);
        } else {
            // Editar una olimpiada existente (Cargar datos)
            txtNombre.setText(olimpiada.getNombre());
            txtAnio.setText(String.valueOf(olimpiada.getAnio()));
            if ("Winter".equals(olimpiada.getTemporada())) {
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

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
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
            if(DaoOlimpiada.getOlimpiada(nuevo.getNombre())==null){
            nuevo.setAnio(Integer.parseInt(txtAnio.getText()));
            if (rbInvierno.isSelected()) {
                nuevo.setTemporada("Winter");
            } else {
                nuevo.setTemporada("Summer");
            }
            nuevo.setCiudad(txtCiudad.getText());

            if (this.olimpiada == null) {
                // Crear una nueva olimpiada
                int id = DaoOlimpiada.insertar(nuevo);
                if (id == -1) {
                    alerta(resources.getString("save.fail"));
                } else {
                    confirmacion(resources.getString("save.olympics"));
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
                }
            } else {
                // Editar una olimpiada existente
                if (DaoOlimpiada.modificar(this.olimpiada, nuevo)) {
                    confirmacion(resources.getString("update.olympics"));
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
                } else {
                    alerta(resources.getString("save.fail"));
                }
            }}
            else {
                alerta(resources.getString("save.fail"));
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
