package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.DaoDeporte;
import com.example.proyectodein.model.Deporte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeportesController implements Initializable {

    private Deporte deporte; // La variable para almacenar el deporte a editar o crear.

    // Elementos del FXML
    @FXML
    private Button btnEliminar;
    @FXML
    private TextField txtNombre; // El TextField donde se ingresa el nombre del deporte
    @FXML
    private Label lblDelete; // La etiqueta que indica si el deporte no es eliminable

    private ResourceBundle resources;

    // Constructor vacío para crear un nuevo deporte
    public DeportesController() {
        this.deporte = null; // No hay un deporte a editar, por lo que se creará uno nuevo.
    }

    // Constructor para editar un deporte existente
    public DeportesController(Deporte deporte) {
        this.deporte = deporte; // Se asigna el deporte que se va a editar.
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;

        if (deporte == null) {
            // Si no hay un deporte a editar, configuramos los campos para crear uno nuevo.
            txtNombre.setText("");
            btnEliminar.setDisable(true);
            lblDelete.setVisible(false);
        } else {
            // Si hay un deporte a editar, configuramos los campos con los datos del deporte.
            txtNombre.setText(deporte.getNombre());
            if (DaoDeporte.esEliminable(deporte)) {
                btnEliminar.setDisable(false);
            } else {
                lblDelete.setVisible(true);
            }
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        // Cerrar la ventana sin guardar cambios
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    @FXML
    void eliminar(ActionEvent event) {
        // Mostrar una confirmación antes de eliminar el deporte
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(txtNombre.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle(resources.getString("window.confirm"));
        alert.setContentText(resources.getString("delete.sports.prompt"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (DaoDeporte.eliminar(deporte)) {
                confirmacion(resources.getString("delete.sports.success"));
                Stage stage = (Stage) txtNombre.getScene().getWindow();
                stage.close();
            } else {
                alerta(resources.getString("delete.sports.fail"));
            }
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        // Validar el nombre del deporte antes de guardar
        if (txtNombre.getText().isEmpty()) {
            alerta(resources.getString("validate.sports.name"));
        } else {
            // Crear un nuevo objeto deporte con los datos del formulario
            Deporte nuevo = new Deporte();
            nuevo.setNombre(txtNombre.getText());
            if(DaoDeporte.getDeporte(nuevo.getNombre())==null){
            if (this.deporte == null) {
                // Si no estamos editando un deporte, lo creamos
                int id = DaoDeporte.insertar(nuevo);
                if (id == -1) {
                    alerta(resources.getString("save.fail"));
                } else {
                    confirmacion(resources.getString("save.sports"));
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
                }
            } else {
                // Si estamos editando un deporte, lo actualizamos
                if (DaoDeporte.modificar(this.deporte, nuevo)) {
                    confirmacion(resources.getString("update.sports"));
                    Stage stage = (Stage) txtNombre.getScene().getWindow();
                    stage.close();
                } else {
                    alerta(resources.getString("save.fail"));
                }
            }}else{
                alerta(resources.getString("save.fail"));
            }
        }
    }

    public void alerta(String texto) {
        // Mostrar un mensaje de error
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("Error");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

    public void confirmacion(String texto) {
        // Mostrar un mensaje de confirmación
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}
