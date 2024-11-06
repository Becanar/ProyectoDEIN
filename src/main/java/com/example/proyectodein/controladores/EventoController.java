package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.DaoDeporte;
import com.example.proyectodein.dao.DaoEvento;
import com.example.proyectodein.dao.DaoOlimpiada;
import com.example.proyectodein.model.Deporte;
import com.example.proyectodein.model.Evento;
import com.example.proyectodein.model.Olimpiada;
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

public class EventoController implements Initializable {
    private Evento evento;

    @FXML // fx:id="lstDeporte"
    private ListView<Deporte> lstDeporte; // Value injected by FXMLLoader

    @FXML // fx:id="lstOlimpiada"
    private ListView<Olimpiada> lstOlimpiada; // Value injected by FXMLLoader

    @FXML // fx:id="txtNombre"
    private TextField txtNombre; // Value injected by FXMLLoader

    @FXML
    private ResourceBundle resources; // ResourceBundle injected automatically by FXML loader

    public EventoController(Evento evento) {
        this.evento = evento;
    }

    public EventoController() {
        this.evento = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        cargarListas();
        if (this.evento != null) {
            txtNombre.setText(evento.getNombre());
            lstOlimpiada.getSelectionModel().select(evento.getIdOlimpiada());
            lstDeporte.getSelectionModel().select(evento.getIdDeporte());
        }
    }
    public void cargarListas() {
        ObservableList<Olimpiada> olimpiadas = DaoOlimpiada.cargarListado();
        lstOlimpiada.getItems().addAll(olimpiadas);
        ObservableList<Deporte> deportes = DaoDeporte.cargarListado();
        lstDeporte.getItems().addAll(deportes);
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage)txtNombre.getScene().getWindow();
        stage.close();
    }

    @FXML
    void guardar(ActionEvent event) {
        String error = "";
        if (txtNombre.getText().isEmpty()) {
            error = resources.getString("validate.event.name") + "\n";
        }
        if (lstOlimpiada.getSelectionModel().getSelectedItem() == null) {
            error += resources.getString("validate.event.olympic") + "\n";
        }
        if (lstDeporte.getSelectionModel().getSelectedItem() == null) {
            error += resources.getString("validate.event.sport") + "\n";
        }
        if (!error.isEmpty()) {
            alerta(error);
        } else {
            Evento nuevo = new Evento();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setIdOlimpiada(lstOlimpiada.getSelectionModel().getSelectedItem().getIdOlimpiada());
            nuevo.setIdDeporte(lstDeporte.getSelectionModel().getSelectedItem().getIdDeporte());
            if(DaoEvento.getEvento(nuevo.getNombre(),nuevo.getIdOlimpiada(),nuevo.getIdDeporte())==null){
            if (this.evento == null) {
                int id = DaoEvento.insertar(nuevo);
                if (id == -1) {
                    alerta(resources.getString("save.fail"));
                } else {
                    confirmacion(resources.getString("save.events"));
                    Stage stage = (Stage)txtNombre.getScene().getWindow();
                    stage.close();
                }
            } else {
                if (DaoEvento.modificar(evento, nuevo)) {
                    confirmacion(resources.getString("update.events"));
                    Stage stage = (Stage)txtNombre.getScene().getWindow();
                    stage.close();
                } else {
                    alerta(resources.getString("save.fail"));
                }
            }}else {
                alerta(resources.getString("save.fail"));
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
