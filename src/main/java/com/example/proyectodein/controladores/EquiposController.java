package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.DaoEquipo;
import com.example.proyectodein.model.Equipo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EquiposController implements Initializable {

    private Equipo equipo; // La variable para almacenar el equipo a editar o crear.

    // Elementos del FXML
    @FXML
    private Button btnEliminar;
    @FXML
    private TextField txtIniciales; // El TextField para las iniciales del equipo
    @FXML
    private TextField txtNombre; // El TextField para el nombre del equipo
    @FXML
    private Label lblDelete; // La etiqueta que indica si el equipo no es eliminable

    private ResourceBundle resources;

    // Constructor vacío para crear un nuevo equipo
    public EquiposController() {
        this.equipo = null; // No hay un equipo a editar, por lo que se creará uno nuevo.
    }

    // Constructor para editar un equipo existente
    public EquiposController(Equipo equipo) {
        this.equipo = equipo; // Se asigna el equipo que se va a editar.
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;

        if (equipo == null) {
            // Si no hay un equipo a editar, configuramos los campos para crear uno nuevo.
            txtNombre.setText("");
            txtIniciales.setText("");
            btnEliminar.setDisable(true);
            lblDelete.setVisible(false);
        } else {
            // Si hay un equipo a editar, configuramos los campos con los datos del equipo.
            txtNombre.setText(equipo.getNombre());
            txtIniciales.setText(equipo.getIniciales());
            if (DaoEquipo.esEliminable(equipo)) {
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
        // Mostrar una confirmación antes de eliminar el equipo
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(txtNombre.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle(resources.getString("window.confirm"));
        alert.setContentText(resources.getString("delete.teams.prompt"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (DaoEquipo.eliminar(equipo)) {
                confirmacion(resources.getString("delete.teams.success"));
                Stage stage = (Stage) txtNombre.getScene().getWindow();
                stage.close();
            } else {
                ArrayList<String> errores = new ArrayList<>();
                errores.add(resources.getString("delete.teams.fail"));
                alerta(errores);  // Ahora pasa un ArrayList<String>
            }
        }
    }

    @FXML
    void guardar(ActionEvent event) {
        // Validar los campos antes de guardar
        ArrayList<String> errores = new ArrayList<>();

        if (txtNombre.getText().isEmpty()) {
            errores.add(resources.getString("validate.teams.name"));
        }
        if (txtIniciales.getText().isEmpty()) {
            errores.add(resources.getString("validate.teams.noc"));
        } else {
            if (txtIniciales.getText().length() > 3) {
                errores.add(resources.getString("validate.teams.noc.num"));
            }
        }

        if (!errores.isEmpty()) {
            alerta(errores);  // Ahora pasa un ArrayList<String>
        } else {
            // Crear un nuevo objeto equipo con los datos del formulario
            Equipo nuevo = new Equipo();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setIniciales(txtIniciales.getText());
            if (DaoEquipo.getEquipo(nuevo.getNombre(), nuevo.getIniciales()) == null) {
                if (this.equipo == null) {
                    // Si no estamos editando un equipo, lo creamos
                    int id = DaoEquipo.insertar(nuevo);
                    if (id == -1) {
                        errores.add(resources.getString("save.fail"));
                        alerta(errores);  // Pasamos el ArrayList con el mensaje de error
                    } else {
                        confirmacion(resources.getString("save.teams"));
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    // Si estamos editando un equipo, lo actualizamos
                    if (DaoEquipo.modificar(equipo, nuevo)) {
                        confirmacion(resources.getString("update.teams"));
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    } else {
                        errores.add(resources.getString("save.fail"));
                        alerta(errores);  // Pasamos el ArrayList con el mensaje de error
                    }
                }
            } else {
                errores.add(resources.getString("save.fail"));
                alerta(errores);  // Pasamos el ArrayList con el mensaje de error
            }
        }
    }

    public void alerta(ArrayList<String> textos) {
        // Mostrar un mensaje de error basado en una lista de textos
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("error.title"));
        alerta.setContentText(contenido);
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
