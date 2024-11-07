package com.example.proyectodein.controladores;

import com.example.proyectodein.app.App;
import com.example.proyectodein.dao.*;
import com.example.proyectodein.model.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Supplier;
/**
 * Controlador para gestionar la vista de las Olimpiadas en la interfaz de usuario.
 * Esta clase maneja la interacción con las entidades relacionadas con las olimpiadas, como
 * deportistas, equipos, eventos y deportes. Permite realizar acciones como añadir, eliminar
 * y filtrar las distintas entidades en la tabla.
 */
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
    /**
     * Método de inicialización que se ejecuta al cargar la vista.
     * Se encargará de cargar los datos en los controles (ComboBox, TableView),
     * configurar las acciones de los botones y permitir la búsqueda a través del campo de texto.
     */
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
    /**
     * Filtra las entidades mostradas en la tabla según el texto ingresado en el campo de búsqueda.
     * El filtro es sensible al texto y buscará coincidencias en los nombres de las entidades.
     */
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

    /**
     * Elimina un elemento de la tabla según la selección del usuario.
     * Muestra un mensaje de confirmación antes de realizar la eliminación de la entidad.
     * La eliminación está condicionada a si la entidad es eliminable o no.
     *
     * @param o El objeto a eliminar, dependiendo de la selección de la tabla.
     */
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

    /**
     * Muestra una alerta de confirmación para la eliminación de una entidad seleccionada.
     * Si el usuario confirma, se realiza la eliminación y se actualiza la vista correspondiente.
     *
     * @param tipoElemento El nombre de la entidad que se va a eliminar (ej. "Olimpiada", "Deportista").
     * @param mensajeConfirmacion El mensaje de confirmación a mostrar.
     * @param eliminacion La acción a ejecutar para eliminar la entidad.
     * @param recargar La acción para recargar la vista después de la eliminación.
     */
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
    /**
     * Permite editar el elemento seleccionado en la tabla. Abre una ventana modal para editar
     * el elemento (Olimpiada, Deportista, Equipo, Evento, Deporte o Participación).
     *
     * @param o Objeto seleccionado (no se utiliza directamente en este método).
     */

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

    /**
     * Carga los datos en el ComboBox de selección de tipo de elementos, con las opciones disponibles
     * como "Olimpiadas", "Deportistas", "Equipos", "Eventos", "Deportes" y "Participaciones".
     */

    private void cargarDatosComboBox() {
        ObservableList<String> opciones = FXCollections.observableArrayList(
                resources.getString("olympics"), resources.getString("athletes"), resources.getString("teams"), resources.getString("events"),resources.getString("sports"),resources.getString("participations")
        );
        comboBoxDatos.setItems(opciones);
    }
    /**
     * Actualiza la tabla según el valor seleccionado en el ComboBox.
     * Dependiendo de la opción seleccionada (Olimpiadas, Deportistas, Equipos, Eventos, Deportes o Participaciones),
     * carga los datos correspondientes en la tabla.
     *
     * @param event El evento que se genera cuando se activa la acción (por ejemplo, un clic de botón).
     */
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
    /**
     * Carga los datos en la tabla para mostrar las participaciones, que incluyen el nombre del deportista,
     * el nombre del evento, el nombre del equipo y la medalla obtenida (si tiene).
     */
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

    /**
     * Carga los datos en la tabla para mostrar las olimpiadas, que incluyen el nombre, el año y la ciudad
     * en la que se celebró cada olimpiada.
     */
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

    /**
     * Carga los datos en la tabla para mostrar los deportistas, que incluyen el nombre y el sexo de cada deportista.
     */
    private void cargarDeportistas() {
        if (tablaVista.getColumns().isEmpty()) {
            // Columna de nombre
            TableColumn<Deportista, String> colNombre = new TableColumn<>(resources.getString("name"));
            colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

            // Columna de sexo
            TableColumn<Deportista, String> colSexo = new TableColumn<>(resources.getString("sex"));
            colSexo.setCellValueFactory(cellData -> new SimpleStringProperty("" + cellData.getValue().getSexo()));

            // Columna de imagen
            TableColumn<Deportista, Image> colImagen = new TableColumn<>(resources.getString("image"));
            colImagen.setCellValueFactory(cellData -> {
                try {
                    // Obtener el Blob de la imagen del deportista
                    Blob imagenBlob = cellData.getValue().getFoto();  // Suponiendo que getImagen() devuelve un Blob
                    if (imagenBlob != null) {
                        // Convertir el Blob a un array de bytes
                        byte[] imageBytes = imagenBlob.getBytes(1, (int) imagenBlob.length());
                        return new SimpleObjectProperty<>(new Image(new ByteArrayInputStream(imageBytes)));
                    } else {
                        // Si no hay imagen, cargar la imagen por defecto
                        return new SimpleObjectProperty<>(new Image(getClass().getResourceAsStream("/com/example/proyectodein/images/null.jpg")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace(); // Si ocurre un error con el Blob
                    return new SimpleObjectProperty<>(new Image(getClass().getResourceAsStream("/com/example/proyectodein/images/null.jpg")));
                }
            });

            // Configuración para mostrar la imagen en la celda
            colImagen.setCellFactory(col -> {
                TableCell<Deportista, Image> cell = new TableCell<Deportista, Image>() {
                    @Override
                    protected void updateItem(Image item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            ImageView imageView = new ImageView(item);
                            imageView.setFitHeight(50); // Establecer el tamaño de la imagen
                            imageView.setFitWidth(50);
                            setGraphic(imageView);
                        }
                    }
                };
                return cell;
            });

            // Añadir las columnas a la tabla
            tablaVista.getColumns().addAll(colNombre, colSexo, colImagen);
        }

        // Cargar los deportistas
        lstEntera.setAll(DaoDeportista.cargarListado());
        tablaVista.setItems(lstEntera);
    }


    /**
     * Carga los datos en la tabla para mostrar los equipos, que incluyen el nombre y las iniciales del país
     * de cada equipo.
     */
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

    /**
     * Carga los datos en la tabla para mostrar los eventos, que incluyen el nombre del evento, el nombre de la olimpiada
     * asociada y el deporte relacionado con el evento.
     */
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

    /**
     * Carga los datos en la tabla para mostrar los deportes, que incluyen el nombre de cada deporte.
     */
    private void cargarDeportes() {
        if (tablaVista.getColumns().isEmpty()) {
        TableColumn<Deporte, String> colNombre = new TableColumn<>(resources.getString("name"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tablaVista.getColumns().addAll(colNombre);}
        lstEntera.setAll(DaoDeporte.cargarListado());
        tablaVista.setItems(lstEntera);
    }

    /**
     * Cambia el idioma de la aplicación a inglés.
     * Este método se ejecuta cuando se selecciona la opción de cambiar a inglés.
     *
     * @param actionEvent El evento que se genera cuando se activa la acción (por ejemplo, un clic de botón).
     */
    public void cambiarIngles(ActionEvent actionEvent) {
        cambiarIdioma("en");
    }
    /**
     * Cambia el idioma de la aplicación a español.
     * Este método se ejecuta cuando se selecciona la opción de cambiar a español.
     *
     * @param actionEvent El evento que se genera cuando se activa la acción (por ejemplo, un clic de botón).
     */
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


    /**
     * Maneja la acción de añadir una nueva entidad dependiendo de la selección del ComboBox.
     * Abre una ventana modal para añadir información relacionada con Olimpiadas, Deportistas, Equipos, Eventos, Deportes o Participaciones.
     *
     * @param event El evento generado por la acción de añadir (por ejemplo, un clic en un botón).
     */
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
    /**
     * Muestra una alerta con los mensajes proporcionados.
     *
     * @param mensajes Una lista de mensajes que se mostrarán en la alerta.
     */
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
    /**
     * Muestra una alerta de confirmación con el mensaje proporcionado.
     *
     * @param mensajes Mensaje o lista de mensajes que se mostrarán en la alerta.
     */
    public void confirmacion(String mensajes) {
        String contenido = String.join("\n", mensajes);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("info"));
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }


}
