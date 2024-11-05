package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.animalDao;
import com.example.proyectodein.model.Animal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class olimpiadasControler implements Initializable {

    @FXML
    private MenuItem aniadirAnimal;

    @FXML
    private MenuBar barraMenu;

    @FXML
    private MenuItem borrarAnimal;

    @FXML
    private MenuItem editarAnimal;

    @FXML
    private MenuItem infoAnimal;

    @FXML
    private Label lblListado;

    @FXML
    private Label lblNombre;

    @FXML
    private Menu menuAnimales;

    @FXML
    private Menu menuAyuda;

    @FXML
    private FlowPane panelListado;

    @FXML
    private VBox rootPane;

    @FXML
    private TableView<Animal> tablaVista;

    @FXML
    private TextField txtNombre;
    private ObservableList lstEntera = FXCollections.observableArrayList();
    private ObservableList lstFiltrada = FXCollections.observableArrayList();

    /**
     * Maneja el evento de añadir un nuevo animal.
     *
     * <p>Este método abre una nueva ventana para añadir un animal. Al cerrar la ventana,
     * se cargan nuevamente los animales en la tabla.</p>
     *
     * @param event el evento de acción.
     */
    @FXML
    void aniadirAnimal(ActionEvent event) {
        try {
            Window ventana = txtNombre.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/datosAnimal.fxml"));
            DatosAnimalController controlador = new DatosAnimalController();
            fxmlLoader.setController(controlador);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            try {
                Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                stage.getIcons().add(img);
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
            }
            scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
            stage.setTitle("VETERINARIA - AÑADIR ANIMAL");
            stage.initOwner(ventana);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarAnimales();

        } catch (IOException e) {
            ArrayList<String> lst=new ArrayList<>();
            lst.add("No se ha podido abrir la ventana.");
            alerta(lst);
        }
    }

    /**
     * Carga la lista de animales en la tabla.
     *
     * <p>Este método limpia la tabla y carga todos los animales desde la base de datos.</p>
     */
    public void cargarAnimales() {
        try {
            tablaVista.getSelectionModel().clearSelection();
            txtNombre.setText(null);
            lstEntera.clear();
            lstFiltrada.clear();
            tablaVista.getItems().clear();
            tablaVista.getColumns().clear();

            TableColumn<Animal, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getId()));

            TableColumn<Animal, String> colNombre = new TableColumn<>("Nombre");
            colNombre.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getNombre()));

            TableColumn<Animal, String> colEspecie = new TableColumn<>("Especie");
            colEspecie.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getEspecie()));

            TableColumn<Animal, String> colRaza = new TableColumn<>("Raza");
            colRaza.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getRaza()));

            TableColumn<Animal, String> colSexo = new TableColumn<>("Sexo");
            colSexo.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getSexo()));

            TableColumn<Animal, Integer> colEdad = new TableColumn<>("Edad");
            colEdad.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getEdad()));

            TableColumn<Animal, Double> colPeso = new TableColumn<>("Peso");
            colPeso.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getPeso()));

            TableColumn<Animal, String> colObservaciones = new TableColumn<>("Observaciones");
            colObservaciones.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createObjectBinding(() -> cellData.getValue().getObservaciones()));

            tablaVista.getColumns().addAll(colId, colNombre, colEspecie, colRaza, colSexo, colEdad, colPeso, colObservaciones);

            ObservableList<Animal> animales = animalDao.cargarListado();

            if (animales != null && !animales.isEmpty()) {
                lstEntera.setAll(animales);
                tablaVista.setItems(animales);
            } else {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("No se encontraron Animales.");
                alerta(lst);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de errores
        }
    }

    /**
     * Maneja el evento de borrar un animal seleccionado.
     *
     * <p>Este método muestra un cuadro de confirmación antes de eliminar el animal
     * de la lista y de la base de datos.</p>
     *
     * @param event el evento de acción.
     */
    @FXML
    void borrarAnimal(ActionEvent event) {
        Animal animal = tablaVista.getSelectionModel().getSelectedItem();

        if (animal == null) {
            ArrayList<String> lst = new ArrayList<>();
            lst.add("No has seleccionado ningún animal.");
            alerta(lst);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(tablaVista.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText("¿Estás seguro que quieres eliminar este animal? Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (animalDao.eliminar(animal)) {
                cargarAnimales();
                confirmacion("Animal eliminado correctamente");
            } else {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("No se ha podido eliminar el animal.");
                alerta(lst);
            }
        }
    }

    /**
     * Maneja el evento de editar un animal seleccionado.
     *
     * <p>Este método abre una ventana para editar los datos del animal seleccionado.
     * Al cerrar la ventana, se recargan los animales en la tabla.</p>
     *
     * @param event el evento de acción.
     */
    @FXML
    void editarAnimal(ActionEvent event) {
        Animal animal = (Animal) tablaVista.getSelectionModel().getSelectedItem();

        if (animal == null) {
            ArrayList<String> lst = new ArrayList<>();
            lst.add("No has seleccionado ningún animal.");
            alerta(lst);
        } else {
            try {

                Window ventana = tablaVista.getScene().getWindow();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectodein/fxml/DatosAnimal.fxml"));
                DatosAnimalController controlador = new DatosAnimalController(animal);
                fxmlLoader.setController(controlador);
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                try {
                    Image img = new Image(getClass().getResource("/com/example/proyectodein/images/ol.png").toString());
                    stage.getIcons().add(img);
                } catch (Exception e) {
                    System.out.println("Error al cargar la imagen: " + e.getMessage());
                }
                scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());

                stage.setTitle("EDITAR ANIMAL");
                stage.initOwner(ventana);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                cargarAnimales();

            } catch (IOException e) {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("No se ha podido abrir la ventana.");
                alerta(lst);
            }
        }
    }

    /**
     * Muestra la información del animal seleccionado.
     *
     * <p>Si no se ha seleccionado ningún animal, se muestra una alerta. De lo contrario,
     * se muestra un cuadro de diálogo con la información del animal.</p>
     *
     * @param event el evento de acción.
     */
    @FXML
    void infoAnimal(ActionEvent event) {
        Object selectedAnimal = tablaVista.getSelectionModel().getSelectedItem();
        if (selectedAnimal == null) {
            ArrayList<String> errores = new ArrayList<>();
            errores.add("Selecciona un animal antes de ver su información");
            alerta(errores);
        } else {
            ArrayList<String> info = new ArrayList<>();

            if (selectedAnimal instanceof Animal) {
                Animal animal = (Animal) selectedAnimal;
                info.add("Nombre: " + animal.getNombre());
                info.add("Especie: " + animal.getEspecie());
                info.add("Raza: " + animal.getRaza());
                info.add("Sexo: " + animal.getSexo());
                info.add("Edad: " + animal.getEdad());
                info.add("Peso: " + animal.getPeso());
                info.add("Observaciones: " + animal.getObservaciones());
                info.add("Fecha de primera consulta: " + (animal.getFechaPrimeraConsulta() != null ? animal.getFechaPrimeraConsulta().toString() : "No disponible"));
                String contenido = String.join("\n", info);
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setHeaderText(null);
                alerta.setTitle("Información");
                alerta.setContentText(contenido);
                alerta.showAndWait();
            }
        }
    }

    /**
     * Inicializa el controlador.
     *
     * <p>Este método se llama al cargar la vista. Configura los listeners y carga los
     * animales en la tabla.</p>
     *
     * @param url la URL utilizada para localizar recursos.
     * @param resourceBundle el recurso utilizado para localizar los objetos.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablaVista.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observableValue, Object oldValue, Object newValue) {
                if (newValue != null) {
                    deshabilitarMenus(false);
                } else {
                    deshabilitarMenus(true);
                }
            }
        });
        cargarAnimales();
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Editar Animal");
        editItem.setOnAction(event -> editarAnimal(null));


        MenuItem deleteItem = new MenuItem("Borrar Animal");
        deleteItem.setOnAction(event -> borrarAnimal(null));

        contextMenu.getItems().addAll(editItem, deleteItem);

        tablaVista.setContextMenu(contextMenu);
        tablaVista.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                infoAnimal(null);
            }
        });
        rootPane.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.F) {
                txtNombre.requestFocus();
                event.consume();
            }
        });
        txtNombre.setOnKeyTyped(keyEvent -> filtrar());
    }

    /**
     * Muestra una alerta de error con los mensajes proporcionados.
     *
     * @param textos Lista de mensajes a mostrar en la alerta de error.
     */
    public void alerta(ArrayList<String> textos) {
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle("ERROR");
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de confirmación con el mensaje proporcionado.
     *
     * @param texto Mensaje a mostrar en la alerta de confirmación.
     */
    public void confirmacion(String texto) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }

    /**
     * Habilita o deshabilita los menús de editar, borrar e información.
     *
     * @param deshabilitado Indica si los menús deben ser deshabilitados.
     */
    public void deshabilitarMenus(boolean deshabilitado) {
        editarAnimal.setDisable(deshabilitado);
        borrarAnimal.setDisable(deshabilitado);
        infoAnimal.setDisable(deshabilitado);
    }

    /**
     * Filtra los animales en la tabla según el texto ingresado en el campo de texto.
     *
     * <p>Este método actualiza la lista de animales mostrada en la tabla basándose
     * en el nombre ingresado por el usuario.</p>
     */
    public void filtrar() {
        String valor = txtNombre.getText();
        if (valor==null) {
            tablaVista.setItems(lstEntera);
        } else {
            valor = valor.toLowerCase();
            lstFiltrada.clear();
            for (Object animal : lstEntera) {
                Animal animall = (Animal) animal;
                String nombre = animall.getNombre();
                nombre = nombre.toLowerCase();
                if (nombre.contains(valor)) {
                    lstFiltrada.add(animall);}
            }

            tablaVista.setItems(lstFiltrada);
        }
    }

}
