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
import java.util.ArrayList;
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
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> errores = new ArrayList<>();

        if (txtNombre.getText().isEmpty()) {
            errores.add(resources.getString("validate.event.name"));
        }
        if (lstOlimpiada.getSelectionModel().getSelectedItem() == null) {
            errores.add(resources.getString("validate.event.olympic"));
        }
        if (lstDeporte.getSelectionModel().getSelectedItem() == null) {
            errores.add(resources.getString("validate.event.sport"));
        }

        if (!errores.isEmpty()) {
            alerta(errores);  // Pasamos el ArrayList de errores
        } else {
            Evento nuevo = new Evento();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setIdOlimpiada(lstOlimpiada.getSelectionModel().getSelectedItem().getIdOlimpiada());
            nuevo.setIdDeporte(lstDeporte.getSelectionModel().getSelectedItem().getIdDeporte());

            // Verificar si el evento ya existe en la base de datos
            if (DaoEvento.getEvento(nuevo.getNombre(), nuevo.getIdOlimpiada(), nuevo.getIdDeporte()) == null) {
                // Si no existe, insertar o modificar según sea necesario
                if (this.evento == null) {
                    int id = DaoEvento.insertar(nuevo);
                    if (id == -1) {
                        // Mostrar mensaje de error al guardar
                        errores.add(resources.getString("save.fail"));
                        alerta(errores);
                    } else {
                        confirmacion(resources.getString("save.events"));
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    if (DaoEvento.modificar(evento, nuevo)) {
                        confirmacion(resources.getString("update.events"));
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    } else {
                        // Si no se pudo modificar, mostrar mensaje de error
                        errores.add(resources.getString("save.fail"));
                        alerta(errores);
                    }
                }
            } else {
                // Si el evento ya existe, mostrar mensaje de duplicado
                errores.add(resources.getString("duplicate.event"));
                alerta(errores);
            }
        }
    }

    // Método para mostrar alerta con múltiples errores (ArrayList)
    public void alerta(ArrayList<String> mensajes) {
        // Unir los mensajes del ArrayList en un solo String para mostrarlos
        StringBuilder texto = new StringBuilder();
        for (String mensaje : mensajes) {
            texto.append(mensaje).append("\n");
        }

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("Error");
        alerta.setContentText(texto.toString());
        alerta.showAndWait();
    }

    // Método para mostrar mensaje de confirmación
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}
