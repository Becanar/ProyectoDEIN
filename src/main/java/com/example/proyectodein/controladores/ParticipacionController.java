package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.DaoDeportista;
import com.example.proyectodein.dao.DaoEquipo;
import com.example.proyectodein.dao.DaoEvento;
import com.example.proyectodein.dao.DaoParticipacion;
import com.example.proyectodein.model.Deportista;
import com.example.proyectodein.model.Equipo;
import com.example.proyectodein.model.Evento;
import com.example.proyectodein.model.Participacion;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ParticipacionController implements Initializable {

    private Participacion participacion;

    // FXML elements
    @FXML private ListView<Deportista> lstDeportista;
    @FXML private ListView<Equipo> lstEquipo;
    @FXML private ListView<Evento> lstEvento;
    @FXML private TextField txtEdad;
    @FXML private TextField txtMedalla;

    private ResourceBundle resources;

    // Constructor for new participacion (no existing data)
    public ParticipacionController() {
        this.participacion = null;
    }

    // Constructor for editing existing participacion
    public ParticipacionController(Participacion participacion) {
        this.participacion = participacion;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        cargarListas(); // Populate list views with data

        if (this.participacion != null) {
            // Populate fields with existing data if editing
            lstDeportista.getSelectionModel().select(participacion.getIdDeportista());
            lstEvento.getSelectionModel().select(participacion.getIdEvento());
            lstEquipo.getSelectionModel().select(participacion.getIdEquipo());
            txtEdad.setText(String.valueOf(participacion.getEdad()));
            txtMedalla.setText(participacion.getMedalla());
        } else {
            // Enable fields if creating a new participation
            lstDeportista.setDisable(false);
            lstEvento.setDisable(false);
            lstEquipo.setDisable(false);
        }
    }

    // Load data into ListView controls
    public void cargarListas() {
        ObservableList<Deportista> deportistas = DaoDeportista.cargarListado();
        lstDeportista.getItems().addAll(deportistas);

        ObservableList<Evento> eventos = DaoEvento.cargarListado();
        lstEvento.getItems().addAll(eventos);

        ObservableList<Equipo> equipos = DaoEquipo.cargarListado();
        lstEquipo.getItems().addAll(equipos);
    }

    // Handle cancellation (close the window)
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtEdad.getScene().getWindow();
        stage.close();
    }

    // Handle saving a participation (create or update)
    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> errores = new ArrayList<>();

        // Validate input fields
        String error = validar();
        if (!error.isEmpty()) {
            errores.add(error);
        }

        // If errors found, show alert
        if (!errores.isEmpty()) {
            alerta(errores);
        } else {
            // Proceed with saving or updating participation
            Participacion nuevo = new Participacion();
            nuevo.setIdDeportista(lstDeportista.getSelectionModel().getSelectedItem().getIdDeportista());
            nuevo.setIdEvento(lstEvento.getSelectionModel().getSelectedItem().getIdEvento());

            if (DaoParticipacion.getParticipacion(nuevo.getIdDeportista(), nuevo.getIdEvento()) == null) {
                nuevo.setIdEquipo(lstEquipo.getSelectionModel().getSelectedItem().getIdEquipo());
                nuevo.setEdad(Integer.parseInt(txtEdad.getText()));
                nuevo.setMedalla(txtMedalla.getText());

                if (this.participacion == null) {
                    // Create new participation
                    if (DaoParticipacion.insertar(nuevo)) {
                        confirmacion(resources.getString("save.participation"));
                        closeWindow();
                    } else {
                        errores.add(resources.getString("save.fail"));
                    }
                } else {
                    // Edit existing participation
                    if (DaoParticipacion.modificar(participacion, nuevo)) {
                        confirmacion(resources.getString("update.participation"));
                        closeWindow();
                    } else {
                        errores.add(resources.getString("save.fail"));
                    }
                }
            } else {
                // If participation already exists
                errores.add(resources.getString("duplicate.participacion"));
            }

            // Show error alert if needed
            if (!errores.isEmpty()) {
                alerta(errores);
            }
        }
    }

    // Validate input fields before saving
    public String validar() {
        StringBuilder error = new StringBuilder();

        if (lstDeportista.getSelectionModel().getSelectedItem() == null) {
            error.append(resources.getString("validate.participation.athlete")).append("\n");
        }
        if (lstEvento.getSelectionModel().getSelectedItem() == null) {
            error.append(resources.getString("validate.participation.evento")).append("\n");
        }
        if (lstEquipo.getSelectionModel().getSelectedItem() == null) {
            error.append(resources.getString("validate.participation.team")).append("\n");
        }
        if (txtEdad.getText().isEmpty()) {
            error.append(resources.getString("validate.participation.age")).append("\n");
        } else {
            try {
                Integer.parseInt(txtEdad.getText());
            } catch (NumberFormatException e) {
                error.append(resources.getString("validate.participation.age.num")).append("\n");
            }
        }
        if (txtMedalla.getText().isEmpty()) {
            error.append(resources.getString("validate.participation.medal")).append("\n");
        } else {
            if (txtMedalla.getText().length() > 6) {
                error.append(resources.getString("validate.participation.medal.num")).append("\n");
            }
        }
        return error.toString();
    }

    // Show alert with multiple error messages
    public void alerta(ArrayList<String> mensajes) {
        StringBuilder texto = new StringBuilder();
        for (String mensaje : mensajes) {
            texto.append(mensaje).append("\n");
        }

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(texto.toString());
        alerta.showAndWait();
    }

    // Show confirmation message
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

    // Close the window
    private void closeWindow() {
        Stage stage = (Stage) txtEdad.getScene().getWindow();
        stage.close();
    }
}
