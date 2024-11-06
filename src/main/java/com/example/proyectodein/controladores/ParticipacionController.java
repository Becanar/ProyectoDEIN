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
            lstDeportista.getSelectionModel().select(participacion.getIdDeportista());
            lstDeportista.setDisable(true);
            lstEvento.getSelectionModel().select(participacion.getIdEvento());
            lstEvento.setDisable(true);
            lstEquipo.getSelectionModel().select(participacion.getIdEquipo());
            txtEdad.setText(participacion.getEdad() + "");
            txtMedalla.setText(participacion.getMedalla());
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
        Stage stage = (Stage)txtEdad.getScene().getWindow();
        stage.close();
    }

    @FXML
    void guardar(ActionEvent event) {
        String error = validar();
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Participacion nuevo = new Participacion();
            nuevo.setIdDeportista(lstDeportista.getSelectionModel().getSelectedItem().getIdDeportista());
            nuevo.setIdEvento(lstEvento.getSelectionModel().getSelectedItem().getIdEvento());
            if(DaoParticipacion.getParticipacion(nuevo.getIdDeportista(),nuevo.getIdEvento())==null){
            nuevo.setIdEquipo(lstEquipo.getSelectionModel().getSelectedItem().getIdEquipo());
            nuevo.setEdad(Integer.parseInt(txtEdad.getText()));
            nuevo.setMedalla(txtMedalla.getText());
            if (this.participacion == null) {
                if (DaoParticipacion.insertar(nuevo)) {
                    confirmacion(resources.getString("save.participation"));
                    Stage stage = (Stage)txtEdad.getScene().getWindow();
                    stage.close();
                } else {
                    alerta(resources.getString("save.fail"));
                }
            } else {
                if (DaoParticipacion.modificar(participacion, nuevo)) {
                    confirmacion(resources.getString("update.participation"));
                    Stage stage = (Stage)txtEdad.getScene().getWindow();
                    stage.close();
                } else {
                    alerta(resources.getString("save.fail"));
                }
            }}else {
                alerta(resources.getString("save.fail"));
            }
        }
    }

    public String validar() {
        String error = "";
        if (lstDeportista.getSelectionModel().getSelectedItem() == null) {
            error = resources.getString("validate.participation.athlete") + "\n";
        }
        if (lstEvento.getSelectionModel().getSelectedItem() == null) {
            error += resources.getString("validate.participation.event") + "\n";
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
