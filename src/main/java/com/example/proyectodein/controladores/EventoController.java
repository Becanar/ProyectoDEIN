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
    private ListView<Deporte> lstDeporte; // ListView para mostrar los deportes

    @FXML // fx:id="lstOlimpiada"
    private ListView<Olimpiada> lstOlimpiada; // ListView para mostrar las olimpiadas

    @FXML // fx:id="txtNombre"
    private TextField txtNombre; // Campo de texto para ingresar el nombre del evento

    @FXML
    private ResourceBundle resources; // Recurso de cadenas para internacionalización

    /**
     * Constructor para crear o editar un evento.
     *
     * @param evento El evento que se está editando. Si es null, se crea un nuevo evento.
     */
    public EventoController(Evento evento) {
        this.evento = evento;
    }

    /**
     * Constructor vacío para crear un nuevo evento.
     */
    public EventoController() {
        this.evento = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        cargarListas(); // Cargar las listas de olimpiadas y deportes
        if (this.evento != null) {
            // Si se está editando un evento, se cargan sus datos
            txtNombre.setText(evento.getNombre());
            lstOlimpiada.getSelectionModel().select(evento.getIdOlimpiada());
            lstDeporte.getSelectionModel().select(evento.getIdDeporte());
        }
    }

    /**
     * Método para cargar las listas de olimpiadas y deportes desde la base de datos.
     */
    public void cargarListas() {
        ObservableList<Olimpiada> olimpiadas = DaoOlimpiada.cargarListado();
        lstOlimpiada.getItems().addAll(olimpiadas); // Añadir las olimpiadas al ListView
        ObservableList<Deporte> deportes = DaoDeporte.cargarListado();
        lstDeporte.getItems().addAll(deportes); // Añadir los deportes al ListView
    }

    /**
     * Método que se ejecuta al hacer clic en el botón "Cancelar".
     * Cierra la ventana sin realizar cambios.
     *
     * @param event El evento de acción asociado al botón de cancelar.
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Método que se ejecuta al hacer clic en el botón "Guardar".
     * Valida los datos y guarda el evento en la base de datos.
     * Si el evento ya existe, muestra una alerta de duplicado.
     *
     * @param event El evento de acción asociado al botón de guardar.
     */
    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> errores = new ArrayList<>();

        // Validar los campos
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
            // Si hay errores, se muestra una alerta
            alerta(errores);
        } else {
            // Crear un nuevo objeto Evento con los datos proporcionados
            Evento nuevo = new Evento();
            nuevo.setNombre(txtNombre.getText());
            nuevo.setIdOlimpiada(lstOlimpiada.getSelectionModel().getSelectedItem().getIdOlimpiada());
            nuevo.setIdDeporte(lstDeporte.getSelectionModel().getSelectedItem().getIdDeporte());

            // Verificar si el evento ya existe en la base de datos
            if (DaoEvento.getEvento(nuevo.getNombre(), nuevo.getIdOlimpiada(), nuevo.getIdDeporte()) == null) {
                // Si no existe, insertar o modificar según sea necesario
                if (this.evento == null) {
                    // Si no estamos editando, creamos un nuevo evento
                    int id = DaoEvento.insertar(nuevo);
                    if (id == -1) {
                        // Si no se puede guardar, mostrar mensaje de error
                        errores.add(resources.getString("save.fail"));
                        alerta(errores);
                    } else {
                        confirmacion(resources.getString("save.events"));
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    // Si estamos editando, modificamos el evento
                    if (DaoEvento.modificar(evento, nuevo)) {
                        confirmacion(resources.getString("update.events"));
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    } else {
                        // Si no se puede modificar, mostrar mensaje de error
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

    /**
     * Muestra una alerta con los errores proporcionados en un ArrayList.
     *
     * @param mensajes Los mensajes de error a mostrar en la alerta.
     */
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

    /**
     * Muestra un mensaje de confirmación con el texto proporcionado.
     *
     * @param texto El mensaje de confirmación a mostrar.
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}
