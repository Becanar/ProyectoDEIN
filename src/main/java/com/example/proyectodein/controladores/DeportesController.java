package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.DaoDeporte;
import com.example.proyectodein.model.Deporte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
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

    /**
     * Constructor vacío para crear un nuevo deporte.
     */
    public DeportesController() {
        this.deporte = null; // No hay un deporte a editar, por lo que se creará uno nuevo.
    }

    /**
     * Constructor para editar un deporte existente.
     *
     * @param deporte El deporte a editar.
     */
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

    /**
     * Método para cerrar la ventana sin guardar cambios.
     *
     * @param event El evento de acción (click) asociado al botón de cancelar.
     */
    @FXML
    void cancelar(ActionEvent event) {
        // Cerrar la ventana sin guardar cambios
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Método para eliminar un deporte de la base de datos con confirmación.
     *
     * @param event El evento de acción (click) asociado al botón de eliminar.
     */
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
                ArrayList<String> confirmMessages = new ArrayList<>();
                confirmMessages.add(resources.getString("delete.sports.success"));
                confirmacion(confirmMessages);
                Stage stage = (Stage) txtNombre.getScene().getWindow();
                stage.close();
            } else {
                ArrayList<String> failMessages = new ArrayList<>();
                failMessages.add(resources.getString("delete.sports.fail"));
                alerta(failMessages);
            }
        }
    }

    /**
     * Método para guardar los cambios realizados en el deporte.
     *
     * @param event El evento de acción (click) asociado al botón de guardar.
     */
    @FXML
    void guardar(ActionEvent event) {
        // Validar el nombre del deporte antes de guardar
        if (txtNombre.getText().isEmpty()) {
            ArrayList<String> failMessages = new ArrayList<>();
            failMessages.add(resources.getString("validate.sports.name"));
            alerta(failMessages);
        } else {
            // Crear un nuevo objeto deporte con los datos del formulario
            Deporte nuevo = new Deporte();
            nuevo.setNombre(txtNombre.getText());
            if (DaoDeporte.getDeporte(nuevo.getNombre()) == null) {
                if (this.deporte == null) {
                    // Si no estamos editando un deporte, lo creamos
                    int id = DaoDeporte.insertar(nuevo);
                    if (id == -1) {
                        ArrayList<String> failMessages = new ArrayList<>();
                        failMessages.add(resources.getString("save.fail"));
                        alerta(failMessages);
                    } else {
                        ArrayList<String> successMessages = new ArrayList<>();
                        successMessages.add(resources.getString("save.sports"));
                        confirmacion(successMessages);
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    // Si estamos editando un deporte, lo actualizamos
                    if (DaoDeporte.modificar(this.deporte, nuevo)) {
                        ArrayList<String> successMessages = new ArrayList<>();
                        successMessages.add(resources.getString("update.sports"));
                        confirmacion(successMessages);
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    } else {
                        ArrayList<String> failMessages = new ArrayList<>();
                        failMessages.add(resources.getString("save.fail"));
                        alerta(failMessages);
                    }
                }
            } else {
                ArrayList<String> failMessages = new ArrayList<>();
                failMessages.add(resources.getString("duplicate.sport"));
                alerta(failMessages);
            }
        }
    }

    /**
     * Muestra una alerta de tipo error con los mensajes proporcionados.
     *
     * @param textos Lista de textos a mostrar en la alerta.
     */
    public void alerta(ArrayList<String> textos) {
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("error.title"));
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de confirmación con los mensajes proporcionados.
     *
     * @param textos Lista de textos a mostrar en la alerta.
     */
    public void confirmacion(ArrayList<String> textos) {
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info.title"));
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
