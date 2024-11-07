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

    @FXML // fx:id="lstDeportista"
    private ListView<Deportista> lstDeportista; // Value injected by FXMLLoader

    @FXML // fx:id="lstEquipo"
    private ListView<Equipo> lstEquipo; // Value injected by FXMLLoader

    @FXML // fx:id="lstEvento"
    private ListView<Evento> lstEvento; // Value injected by FXMLLoader

    @FXML // fx:id="txtEdad"
    private TextField txtEdad; // Value injected by FXMLLoader

    @FXML // fx:id="txtMedalla"
    private TextField txtMedalla; // Value injected by FXMLLoader

    @FXML
    private ResourceBundle resources; // ResourceBundle injected automatically by FXML loader

    public ParticipacionController(Participacion participacion) {
        this.participacion = participacion;
    }

    public ParticipacionController() {
        this.participacion = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        cargarListas();
        if (this.participacion != null) {
            lstDeportista.getSelectionModel().select(participacion.getIdDeportista());// Deshabilitar si no es necesario editar
            lstEvento.getSelectionModel().select(participacion.getIdEvento());// Deshabilitar si no es necesario editar
            lstEquipo.getSelectionModel().select(participacion.getIdEquipo());
            txtEdad.setText(String.valueOf(participacion.getEdad()));
            txtMedalla.setText(participacion.getMedalla());
        } else {
            // Habilitar los campos si estamos creando una nueva participación
            lstDeportista.setDisable(false);
            lstEvento.setDisable(false);
            lstEquipo.setDisable(false);
        }
    }

    public void cargarListas() {
        ObservableList<Deportista> deportistas = DaoDeportista.cargarListado();
        lstDeportista.getItems().addAll(deportistas);
        ObservableList<Evento> eventos = DaoEvento.cargarListado();
        lstEvento.getItems().addAll(eventos);
        ObservableList<Equipo> equipos = DaoEquipo.cargarListado();
        lstEquipo.getItems().addAll(equipos);
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtEdad.getScene().getWindow();
        stage.close();
    }

    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> errores = new ArrayList<>();  // Usamos ArrayList para múltiples errores

        String error = validar(); // Validar los campos
        if (!error.isEmpty()) {
            errores.add(error);  // Si hay error, lo agregamos al ArrayList
        }

        if (!errores.isEmpty()) {
            alerta(errores);  // Pasamos el ArrayList de errores
        } else {
            Participacion nuevo = new Participacion();
            nuevo.setIdDeportista(lstDeportista.getSelectionModel().getSelectedItem().getIdDeportista());
            nuevo.setIdEvento(lstEvento.getSelectionModel().getSelectedItem().getIdEvento());

            if(DaoParticipacion.getParticipacion(nuevo.getIdDeportista(),nuevo.getIdEvento()) == null){
                nuevo.setIdEquipo(lstEquipo.getSelectionModel().getSelectedItem().getIdEquipo());
                nuevo.setEdad(Integer.parseInt(txtEdad.getText()));
                nuevo.setMedalla(txtMedalla.getText());

                if (this.participacion == null) {
                    // Crear nueva participación
                    if (DaoParticipacion.insertar(nuevo)) {
                        confirmacion(resources.getString("save.participation"));
                        Stage stage = (Stage) txtEdad.getScene().getWindow();
                        stage.close();
                    } else {
                        errores.add(resources.getString("save.fail"));
                    }
                } else {
                    // Editar participación existente
                    if (DaoParticipacion.modificar(participacion, nuevo)) {
                        confirmacion(resources.getString("update.participation"));
                        Stage stage = (Stage) txtEdad.getScene().getWindow();
                        stage.close();
                    } else {
                        errores.add(resources.getString("save.fail"));
                    }
                }
            } else {
                errores.add(resources.getString("duplicate.participacion"));
            }

            // Si hay errores de validación o al guardar, mostramos la alerta
            if (!errores.isEmpty()) {
                alerta(errores);
            }
        }
    }


    public String validar() {
        String error = "";
        if (lstDeportista.getSelectionModel().getSelectedItem() == null) {
            error = resources.getString("validate.participation.athlete") + "\n";
        }
        if (lstEvento.getSelectionModel().getSelectedItem() == null) {
            error += resources.getString("validate.participation.evento") + "\n";
        }
        if (lstEquipo.getSelectionModel().getSelectedItem() == null) {
            error += resources.getString("validate.participation.team") + "\n";
        }
        if (txtEdad.getText().isEmpty()) {
            error += resources.getString("validate.participation.age") + "\n";
        } else {
            try {
                Integer.parseInt(txtEdad.getText());
            } catch (NumberFormatException e) {
                error += resources.getString("validate.participation.age.num") + "\n";
            }
        }
        if (txtMedalla.getText().isEmpty()) {
            error += resources.getString("validate.participation.medal") + "\n";
        } else {
            if (txtMedalla.getText().length() > 6) {
                error += resources.getString("validate.participation.medal.num") + "\n";
            }
        }
        return error;
    }

    public void alerta(ArrayList<String> mensajes) {
        StringBuilder texto = new StringBuilder();
        // Recorrer el ArrayList y concatenar cada mensaje
        for (String mensaje : mensajes) {
            texto.append(mensaje).append("\n");
        }

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(texto.toString()); // Mostrar todos los mensajes concatenados
        alerta.showAndWait();
    }


    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}
