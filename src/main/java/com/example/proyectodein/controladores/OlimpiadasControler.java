package com.example.proyectodein.controladores;

import com.example.proyectodein.app.App;
import com.example.proyectodein.dao.*;
import com.example.proyectodein.model.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

public class OlimpiadasControler {
    @FXML
    private Button btAniadir;
    @FXML
    private VBox rootPane;
    @FXML
    private TableView tablaVista;

    @FXML
    private ComboBox<String> comboBoxDatos;

    @FXML
    private TextField txtNombre;


    private ObservableList<Object> lstEntera = FXCollections.observableArrayList();
    private ObservableList<Object> lstFiltrada = FXCollections.observableArrayList();



    @FXML
    private ResourceBundle resources;

    @FXML
    private void initialize() {

        cargarDatosComboBox();
        comboBoxDatos.setValue(resources.getString("olympics"));
        actualizarTabla(null);
        comboBoxDatos.setOnAction(this::actualizarTabla);
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem(resources.getString("edit"));
        editItem.setOnAction(event -> editar(null));


        MenuItem deleteItem = new MenuItem(resources.getString("delete"));
        deleteItem.setOnAction(event -> borrar(null));

        contextMenu.getItems().addAll(editItem, deleteItem);

        tablaVista.setContextMenu(contextMenu);
        rootPane.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.F) {
                txtNombre.requestFocus();
                event.consume();
            }
        });
        txtNombre.setOnKeyTyped(keyEvent -> filtrar());
    }

    private void filtrar() {
        String valor = txtNombre.getText();
        if (valor == null || valor.isEmpty()) {
            tablaVista.setItems(lstEntera);
        } else {
            valor = valor.toLowerCase();
            lstFiltrada.clear();
            for (Object item : lstEntera) {
                String nombre;
                if (item instanceof Olimpiada) {
                    nombre = ((Olimpiada) item).getNombre();
                } else if (item instanceof Deportista) {
                    nombre = ((Deportista) item).getNombre();
                } else if (item instanceof Equipo) {
                    nombre = ((Equipo) item).getNombre();
                } else if (item instanceof Evento) {
                    nombre = ((Evento) item).getNombre();
                } else if (item instanceof Deporte) {
                    nombre = ((Deporte) item).getNombre();
                } else {
                    continue;
                }
                if (nombre.toLowerCase().contains(valor)) {
                    lstFiltrada.add(item);
                }
            }
            tablaVista.setItems(lstFiltrada);
        }
    }


    private void borrar(Object o) {
            Object seleccion = tablaVista.getSelectionModel().getSelectedItem();
            if (seleccion != null) {
                String item = comboBoxDatos.getSelectionModel().getSelectedItem();

                if (item.equals(resources.getString("olympics"))) {
                    // Olimpiada
                    Olimpiada olimpiada = (Olimpiada) seleccion;
                    if (DaoOlimpiada.esEliminable(olimpiada)) {
                        mostrarConfirmacionYEliminar(resources.getString("olympic"), resources.getString("confirm.delete.olympics"),
                                () -> DaoOlimpiada.eliminar(olimpiada), this::cargarOlimpiadas);
                    } else {
                        alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.olympic"))));
                    }

                } else if (item.equals(resources.getString("athletes"))) {
                    // Deportista
                    Deportista deportista = (Deportista) seleccion;
                    if (DaoDeportista.esEliminable(deportista)) {
                        mostrarConfirmacionYEliminar(resources.getString("athlete"), resources.getString("confirm.delete.athlete"),
                                () -> DaoDeportista.eliminar(deportista), this::cargarDeportistas);
                    } else {

                        alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.athlete"))));
                    }

                } else if (item.equals(resources.getString("teams"))) {
                    // Equipo
                    Equipo equipo = (Equipo) seleccion;
                    if (DaoEquipo.esEliminable(equipo)) {
                        mostrarConfirmacionYEliminar(resources.getString("team"), resources.getString("confirm.delete.team"),
                                () -> DaoEquipo.eliminar(equipo), this::cargarEquipos);
                    } else {
                        alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.team"))));
                    }

                } else if (item.equals(resources.getString("events"))) {
                    // Evento
                    Evento evento = (Evento) seleccion;
                    if (DaoEvento.esEliminable(evento)) {
                        mostrarConfirmacionYEliminar(resources.getString("event"),  resources.getString("confirm.delete.event"),
                                () -> DaoEvento.eliminar(evento), this::cargarEventos);
                    } else {
                        alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.event"))));
                    }

                } else if (item.equals(resources.getString("sports"))) {
                    // Deporte
                    Deporte deporte = (Deporte) seleccion;
                    if (DaoDeporte.esEliminable(deporte)) {
                        mostrarConfirmacionYEliminar(resources.getString("sport"),  resources.getString("confirm.delete.sport"),
                                () -> DaoDeporte.eliminar(deporte), this::cargarDeportes);
                    } else {
                        alerta(new ArrayList<>(Arrays.asList( resources.getString("no.delete.sport"))));

                    }

                } else if (item.equals(resources.getString("participations"))) {
                    // Participación
                    Participacion participacion = (Participacion) seleccion;
                    mostrarConfirmacionYEliminar(resources.getString("participation"), resources.getString("confirm.delete.participation"),
                            () -> DaoParticipacion.eliminar(participacion), this::cargarParticipaciones);
                }
            } else {
                alerta(new ArrayList<>(Arrays.asList(resources.getString("select"))));
            }


    }


    private void mostrarConfirmacionYEliminar(String tipoElemento, String mensajeConfirmacion, Supplier<Boolean> eliminacion, Runnable recargar) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(tablaVista.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle(resources.getString("confirmation"));
        alert.setContentText(mensajeConfirmacion);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (eliminacion.get()) {  // Llamar a .get() en lugar de .run() para el Supplier
                recargar.run();
                confirmacion(""+tipoElemento + " eliminado exitosamente.");
            } else {
                alerta(new ArrayList<>(Arrays.asList("No se pudo eliminar el " + tipoElemento.toLowerCase() + ".")));
            }
        }
    }


    private void editar(Object o) {
        // Obtener el objeto seleccionado desde la tabla
        Object seleccion = tablaVista.getSelectionModel().getSelectedItem();

        if (seleccion != null) {
            String item = comboBoxDatos.getSelectionModel().getSelectedItem();

            if (item.equals(resources.getString("olympics"))) {
                // Olimpiada
                Olimpiada olimpiada = (Olimpiada) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/olimpiadasV.fxml"), bundle);
                    OlimpiadasVController controlador = new OlimpiadasVController(olimpiada);  // Pasamos la Olimpiada seleccionada
                    fxmlLoader.setController(controlador);
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    try {
                        Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                        stage.getIcons().add(img);
                    } catch (Exception e) {
                        System.out.println("error.img " + e.getMessage());
                    }
                    scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                    stage.setTitle(resources.getString("olympics"));
                    stage.initOwner(ventana);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    cargarOlimpiadas();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
                }

            } else if (item.equals(resources.getString("athletes"))) {
                // Deportista
                Deportista deportista = (Deportista) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/deportista.fxml"), bundle);
                    DeportistaController controlador = new DeportistaController(deportista);  // Pasamos el Deportista seleccionado
                    fxmlLoader.setController(controlador);
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    try {
                        Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                        stage.getIcons().add(img);
                    } catch (Exception e) {
                        System.out.println("error.img " + e.getMessage());
                    }
                    scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                    stage.setTitle(resources.getString("athletes"));
                    stage.initOwner(ventana);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    cargarDeportistas();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
                }

            } else if (item.equals(resources.getString("teams"))) {
                // Equipo
                Equipo equipo = (Equipo) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/equipo.fxml"), bundle);
                    EquiposController controlador = new EquiposController(equipo);  // Pasamos el Equipo seleccionado
                    fxmlLoader.setController(controlador);
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    try {
                        Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                        stage.getIcons().add(img);
                    } catch (Exception e) {
                        System.out.println("error.img " + e.getMessage());
                    }
                    scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                    stage.setTitle(resources.getString("teams"));
                    stage.initOwner(ventana);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    cargarEquipos();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
                }

            } else if (item.equals(resources.getString("events"))) {
                // Evento
                Evento evento = (Evento) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/evento.fxml"), bundle);
                    EventoController controlador = new EventoController(evento);  // Pasamos el Evento seleccionado
                    fxmlLoader.setController(controlador);
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    try {
                        Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                        stage.getIcons().add(img);
                    } catch (Exception e) {
                        System.out.println("error.img " + e.getMessage());
                    }
                    scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                    stage.setTitle(resources.getString("events"));
                    stage.initOwner(ventana);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    cargarEventos();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
                }

            } else if (item.equals(resources.getString("sports"))) {
                // Deporte
                Deporte deporte = (Deporte) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/deportes.fxml"), bundle);
                    DeportesController controlador = new DeportesController(deporte);  // Pasamos el Deporte seleccionado
                    fxmlLoader.setController(controlador);
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    try {
                        Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                        stage.getIcons().add(img);
                    } catch (Exception e) {
                        System.out.println("error.img " + e.getMessage());
                    }
                    scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                    stage.setTitle(resources.getString("sports"));
                    stage.initOwner(ventana);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    cargarDeportes();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
                }

            } else if (item.equals(resources.getString("participations"))) {
                // Participación
                Participacion participacion = (Participacion) seleccion;
                try {
                    Window ventana = tablaVista.getScene().getWindow();
                    String idioma = Propiedades.getValor("language");
                    ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/participacion.fxml"), bundle);
                    ParticipacionController controlador = new ParticipacionController(participacion);  // Pasamos la Participación seleccionada
                    fxmlLoader.setController(controlador);
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    try {
                        Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                        stage.getIcons().add(img);
                    } catch (Exception e) {
                        System.out.println("error.img " + e.getMessage());
                    }
                    scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                    stage.setTitle(resources.getString("participations"));
                    stage.initOwner(ventana);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    cargarParticipaciones();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
                }

            }
        } else {
            alerta(new ArrayList<>(Arrays.asList(resources.getString("select.ed"))));
        }
    }



    private void cargarDatosComboBox() {
        ObservableList<String> opciones = FXCollections.observableArrayList(
                resources.getString("olympics"), resources.getString("athletes"), resources.getString("teams"), resources.getString("events"),resources.getString("sports"),resources.getString("participations")
        );
        comboBoxDatos.setItems(opciones);
    }

    public void actualizarTabla(ActionEvent event) {
        // Cargar el ResourceBundle para acceder a los mensajes
        String idioma = Propiedades.getValor("language");
        ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));

        // Obtener el valor de la clave "participations" desde el archivo de recursos
        String olimpiadas = resources.getString("olympics");
        String deportistas = resources.getString("athletes");
        String equipos = resources.getString("teams");
        String eventos = resources.getString("events");
        String deportes = resources.getString("sports");
        String participaciones = resources.getString("participations");


        // Obtener el valor seleccionado del ComboBox
        String seleccion = comboBoxDatos.getValue();

        // Limpiar la tabla antes de actualizarla
        tablaVista.getItems().clear();
        tablaVista.getColumns().clear(); // Limpiar columnas antes de configurar nuevas

        // Usar if-else para comparar las opciones seleccionadas
        if (olimpiadas.equals(seleccion)) {
            cargarOlimpiadas();
        } else if (deportistas.equals(seleccion)) {
            cargarDeportistas();
        } else if (equipos.equals(seleccion)) {
            cargarEquipos();
        } else if (eventos.equals(seleccion)) {
            cargarEventos();
        } else if (deportes.equals(seleccion)) {
            cargarDeportes();
        } else if (participaciones.equals(seleccion)) { // Comparar con la cadena obtenida del ResourceBundle
            cargarParticipaciones();
        }
    }

    private void cargarParticipaciones() {
        if (tablaVista.getColumns().isEmpty()) {
            // Columna para el nombre del deportista
            TableColumn<Participacion, String> colDeportista = new TableColumn<>(resources.getString("athlete"));
            colDeportista.setCellValueFactory(cellData -> new SimpleStringProperty(DaoDeportista.getDeportista(cellData.getValue().getIdDeportista()).getNombre()));

            // Columna para el nombre del evento
            TableColumn<Participacion, String> colEvento = new TableColumn<>(resources.getString("event"));
            colEvento.setCellValueFactory(cellData -> new SimpleStringProperty(DaoEvento.getEvento(cellData.getValue().getIdEvento()).getNombre()));

            // Columna para la posición
            TableColumn<Participacion, String> colPosicion = new TableColumn<>(resources.getString("team"));
            colPosicion.setCellValueFactory(cellData -> new SimpleStringProperty(DaoEquipo.getEquipo(cellData.getValue().getIdEquipo()).getNombre()));

            // Columna para la medalla (si tiene)
            TableColumn<Participacion, String> colMedalla = new TableColumn<>(resources.getString("medal"));
            colMedalla.setCellValueFactory(cellData -> {
                String medalla = cellData.getValue().getMedalla();
                return new SimpleStringProperty(medalla != null ? medalla : "NA");
            });


            // Agregar todas las columnas a la tabla
            tablaVista.getColumns().addAll(colDeportista, colEvento, colPosicion, colMedalla);
        }
        // Cargar los datos de las participaciones
        lstEntera.setAll(DaoParticipacion.cargarListado());
        tablaVista.setItems(lstEntera);
    }


    private void cargarOlimpiadas() {
        if (tablaVista.getColumns().isEmpty()) {
            TableColumn<Olimpiada, String> colNombre = new TableColumn<>(resources.getString("name"));
            colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

            TableColumn<Olimpiada, Integer> colAño = new TableColumn<>(resources.getString("year"));
            colAño.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAnio()).asObject());

            TableColumn<Olimpiada, String> colCiudad = new TableColumn<>(resources.getString("city"));
            colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCiudad()));
            tablaVista.getColumns().addAll(colNombre, colAño, colCiudad);
        }
        lstEntera.setAll(DaoOlimpiada.cargarListado());
        tablaVista.setItems(lstEntera);
    }


    private void cargarDeportistas() {
        if (tablaVista.getColumns().isEmpty()) {
        TableColumn<Deportista, String> colNombre = new TableColumn<>(resources.getString("name"));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Deportista, String> colSexo = new TableColumn<>(resources.getString("sex"));
        colSexo.setCellValueFactory(cellData -> new SimpleStringProperty(""+cellData.getValue().getSexo()));

        tablaVista.getColumns().addAll(colNombre, colSexo);}
        lstEntera.setAll(DaoDeportista.cargarListado());
        tablaVista.setItems(lstEntera);
    }

    private void cargarEquipos() {
        if (tablaVista.getColumns().isEmpty()) {
        TableColumn<Equipo, String> colNombre = new TableColumn<>(resources.getString("name"));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Equipo, String> colPais = new TableColumn<>(resources.getString("siglas"));
        colPais.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIniciales()));

        tablaVista.getColumns().addAll(colNombre, colPais);}
        lstEntera.setAll(DaoEquipo.cargarListado());
        tablaVista.setItems(lstEntera);
    }


    private void cargarEventos() {
        if (tablaVista.getColumns().isEmpty()) {
        TableColumn<Evento, String> colNombre = new TableColumn<>(resources.getString("name"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Evento, String> colOlimpiada = new TableColumn<>(resources.getString("olympics"));
        colOlimpiada.setCellValueFactory(cellData -> new SimpleStringProperty(DaoOlimpiada.getOlimpiada(cellData.getValue().getIdOlimpiada()).getNombre()));

        TableColumn<Evento, String> colDeporte = new TableColumn<>(resources.getString("sport"));
        colDeporte.setCellValueFactory(cellData -> new SimpleStringProperty(DaoDeporte.getDeporte(cellData.getValue().getIdDeporte()).getNombre()));

        tablaVista.getColumns().addAll(colNombre, colOlimpiada, colDeporte);}
        lstEntera.setAll(DaoEvento.cargarListado());
        tablaVista.setItems(lstEntera);
    }


    private void cargarDeportes() {
        if (tablaVista.getColumns().isEmpty()) {
        TableColumn<Deporte, String> colNombre = new TableColumn<>(resources.getString("name"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tablaVista.getColumns().addAll(colNombre);}
        lstEntera.setAll(DaoDeporte.cargarListado());
        tablaVista.setItems(lstEntera);
    }


    public void cambiarIngles(ActionEvent actionEvent) {
        cambiarIdioma("en");
    }

    public void cambiarEsp(ActionEvent actionEvent) {
        cambiarIdioma("es");
    }


    /**
     * Cambia el idioma de la interfaz a uno específico.
     *
     * @param idioma El código del idioma a cambiar (eu, es, en).
     */

    public void cambiarIdioma(String idioma) {
        // Actualizar el archivo db.properties con el nuevo idioma
        Propiedades.setIdioma("language", idioma);

        // Cargar el nuevo ResourceBundle con el idioma seleccionado
        ResourceBundle bundle = ResourceBundle.getBundle("com.example.proyectodein.languages.lan", new Locale(idioma));

        // Obtener la ventana actual (en este caso el Stage principal)
        Stage stage = (Stage) btAniadir.getScene().getWindow();

        // Actualizar la ventana principal
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/proyectodein/fxml/olimpiadas.fxml"), bundle);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(bundle.getString("app.name"));
            stage.setResizable(false);

            try {
                Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                System.out.println(bundle.getString("error.img") + e.getMessage());
            }

            scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
            stage.setTitle(bundle.getString("app.name"));
            stage.setScene(scene);
            stage.show();

            // Cierra todas las ventanas auxiliares y vuelve a abrirlas con el idioma actualizado (si es necesario)
            // Ejemplo:
            if (stage.getOwner() != null) {
                Stage owner = (Stage)stage.getOwner();
                // Aquí puedes usar un código para reiniciar o actualizar otras ventanas hijas
                // owner.close();  // O también puedes recargar otras ventanas
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void aniadir(ActionEvent event) {
        String seleccion = comboBoxDatos.getSelectionModel().getSelectedItem();
        System.out.println(seleccion);
        if (seleccion.equals(resources.getString("olympics"))) {

            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/olimpiadasV.fxml"), bundle);
                OlimpiadasVController controlador = new OlimpiadasVController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle(resources.getString("olympics"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarOlimpiadas();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        } else if (seleccion.equals(resources.getString("athletes"))) {
            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/deportista.fxml"), bundle);
                DeportistaController controlador = new DeportistaController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle(resources.getString("athletes"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarDeportistas();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        } else if (seleccion.equals(resources.getString("teams"))) {
            // Agregar nuevo Equipo
            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/equipo.fxml"), bundle);
                EquiposController controlador = new EquiposController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle((resources.getString("teams")));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarEquipos();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        } else if (seleccion.equals(resources.getString("events"))) {
            // Agregar nuevo Evento
            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/evento.fxml"), bundle);
                EventoController controlador = new EventoController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle(resources.getString("events"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarEventos();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        } else if (seleccion.equals(resources.getString("sports"))) {
            // Agregar nuevo Deporte
            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/deportes.fxml"), bundle);
                DeportesController controlador = new DeportesController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle(resources.getString("sports"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarDeportes();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        }else if (seleccion.equals(resources.getString("participations"))) {
            // Agregar nuevo Deporte
            try {
                Window ventana = tablaVista.getScene().getWindow();
                String idioma = Propiedades.getValor("language");
                ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lan", new Locale(idioma));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/participacion.fxml"), bundle);
                ParticipacionController controlador = new ParticipacionController();
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("error.img " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                stage.setTitle(resources.getString("participations"));
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                cargarParticipaciones();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                alerta(new ArrayList<>(Arrays.asList(resources.getString("message.window_open"))));
            }
        }
    }
    private void alerta(ArrayList<String> mensajes) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(resources.getString("advert"));
        alert.setHeaderText(null);

        StringBuilder contenido = new StringBuilder();
        for (String mensaje : mensajes) {
            contenido.append(mensaje).append("\n");
        }

        alert.setContentText(contenido.toString().trim());
        alert.showAndWait();
    }

    public void confirmacion(String mensajes) {
        String contenido = String.join("\n", mensajes);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }


}
