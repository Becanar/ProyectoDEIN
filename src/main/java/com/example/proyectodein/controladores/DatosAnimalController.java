package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.animalDao;
import com.example.proyectodein.model.Animal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DatosAnimalController implements Initializable {
    private Object animal = null;
    private Animal ap;
    @FXML
    private Button btCancelar;

    @FXML
    private Button btGuardar;

    @FXML
    private Button btImg;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ImageView imgView;

    @FXML
    private Label lblDatos;

    @FXML
    private Label lblEdad;

    @FXML
    private Label lblEspecie;

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblObservaciones;

    @FXML
    private Label lblPeso;

    @FXML
    private Label lblRaza;

    @FXML
    private Label lblSexo;

    @FXML
    private FlowPane panelBotones;

    @FXML
    private GridPane rootPane;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtEspecie;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtObservaciones;

    @FXML
    private TextField txtPeso;

    @FXML
    private TextField txtRaza;

    @FXML
    private TextField txtSexo;

    private Blob imagenBlob;

    /**
     * Constructor que inicializa el controlador con un objeto animal.
     *
     * @param animal el objeto animal a inicializar.
     */
    public DatosAnimalController(Object animal) {
        this.animal = animal;
    }

    /**
     * Constructor vacío.
     */
    public DatosAnimalController(){}

    /**
     * Establece el objeto animal en el controlador.
     *
     * @param animal el objeto animal a establecer.
     */
    public void setAnimal(Object animal) {
        this.animal = animal;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btGuardar.setDefaultButton(true);
        btCancelar.setCancelButton(true);

        if (animal == null) {
            System.out.println("Null");
        } else {
            Animal anm = (Animal) animal;
            this.ap = anm;

            txtNombre.setText(anm.getNombre());
            txtEspecie.setText(anm.getEspecie());
            txtRaza.setText(anm.getRaza());
            txtSexo.setText(anm.getSexo());
            txtEdad.setText(String.valueOf(anm.getEdad()));
            txtPeso.setText(String.valueOf(anm.getPeso()));
            txtObservaciones.setText(anm.getObservaciones());

            if (anm.getFoto() != null) {
                System.out.println("Has image");
                this.imagenBlob = anm.getFoto();
                InputStream imagen = null;
                try {
                    imagen = anm.getFoto().getBinaryStream();
                    imgView.setImage(new Image(imagen));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Maneja el evento de cancelación.
     *
     * @param event el evento de acción.
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Maneja el evento de guardar datos del animal.
     *
     * @param event el evento de acción.
     */
    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> lst = new ArrayList<>();
        String error = "";

        // Validación del campo nombre
        if (txtNombre.getText().isEmpty()) {
            lst.add("Campo nombre no puede estar vacío");
        }

        // Validación del campo especie
        if (txtEspecie.getText().isEmpty()) {
            lst.add("Campo especie no puede estar vacío");
        }

        // Validación del campo raza
        if (txtRaza.getText().isEmpty()) {
            lst.add("Campo raza no puede estar vacío");
        }

        // Validación del campo sexo
        if (txtSexo.getText().isEmpty()) {
            lst.add("Campo sexo no puede estar vacío");
        }

        // Validación del campo edad
        if (txtEdad.getText().isEmpty()) {
            lst.add("Campo edad no puede estar vacío");
        } else {
            try {
                int edad = Integer.parseInt(txtEdad.getText());
            } catch (NumberFormatException e) {
                lst.add("Campo edad tiene que ser numérico");
            }
        }

        // Validación del campo peso
        if (txtPeso.getText().isEmpty()) {
            lst.add("Campo peso no puede estar vacío");
        } else {
            try {
                double peso = Double.parseDouble(txtPeso.getText());
            } catch (NumberFormatException e) {
                lst.add("Campo peso tiene que ser numérico");
            }
        }

        // Validación del campo observaciones
        if (txtObservaciones.getText().isEmpty()) {
            lst.add("Campo observaciones no puede estar vacío");
        }

        // Si hay errores, mostrar alerta
        if (!lst.isEmpty()) {
            alerta(lst);
        } else {
            // Guardar o modificar el animal
            boolean resultado;
            if (this.ap == null) {
                resultado = crearAnimal(); // Implementa esta lógica según tu modelo
            } else {
                resultado = modificarAnimal(); // Implementa esta lógica según tu modelo
            }

            // Cerrar la ventana si la operación fue exitosa
            if (resultado) {
                Stage stage = (Stage) txtNombre.getScene().getWindow();
                stage.close();
            }
        }
    }

    /**
     * Modifica los datos del animal existente.
     *
     * @return true si la modificación fue exitosa, false de lo contrario.
     */
    public boolean modificarAnimal() {
        try {
            Animal animal = new Animal();
            animal.setId(ap.getId());
            animal.setNombre(txtNombre.getText());
            animal.setEspecie(txtEspecie.getText());
            animal.setRaza(txtRaza.getText());
            animal.setSexo(txtSexo.getText());

            if (txtEdad.getText().isEmpty()) {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("Campo edad no puede estar vacío");
                alerta(lst);
                return false;
            }
            animal.setEdad(Integer.parseInt(txtEdad.getText()));

            if (txtPeso.getText().isEmpty()) {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("Campo peso no puede estar vacío");
                alerta(lst);
                return false;
            }
            animal.setPeso(Double.parseDouble(txtPeso.getText()));

            // Validación y establecimiento de las observaciones
            if (txtObservaciones.getText().isEmpty()) {
                ArrayList<String> lst = new ArrayList<>();
                lst.add("Campo observaciones no puede estar vacío");
                alerta(lst);
                return false;
            }
            LocalDate localDate = datePicker.getValue();
            if (localDate != null) {
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                animal.setFechaPrimeraConsulta(date);
            } else {
                System.out.println("No date selected.");
            }
            animal.setObservaciones(txtObservaciones.getText());

            // Establecimiento de la imagen
            animal.setFoto(imagenBlob);

            // Lógica para modificar el animal en la base de datos
            if (!animalDao.modificar(ap, animal)) { // `ap` es la instancia original
                ArrayList<String> lst = new ArrayList<>();
                lst.add("No se han podido cargar los datos.");
                alerta(lst);
                return false;
            }

            // Confirmación de la actualización
            ArrayList<String> lst = new ArrayList<>();
            lst.add("Animal actualizado correctamente");
            confirmacion(lst);
            return true;

        } catch (NumberFormatException e) {
            alerta(new ArrayList<>(List.of("Errores de formato numérico. Asegúrese de que los campos edad y peso sean numéricos.")));
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            alerta(new ArrayList<>(List.of("Se produjo un error al modificar el animal.")));
            return false;
        }
    }

    /**
     * Crea un nuevo animal y lo guarda en la base de datos.
     *
     * @return true si la creación fue exitosa, false de lo contrario.
     */
    public boolean crearAnimal() {
        try {
            Animal animal = new Animal();
            animal.setNombre(txtNombre.getText());
            animal.setEspecie(txtEspecie.getText());
            animal.setRaza(txtRaza.getText());
            animal.setSexo(txtSexo.getText());

            // Validación y establecimiento de la edad
            if (txtEdad.getText().isEmpty()) {
                alerta(new ArrayList<>(List.of("Campo edad no puede estar vacío")));
                return false;
            }
            animal.setEdad(Integer.parseInt(txtEdad.getText()));

            // Validación y establecimiento del peso
            if (txtPeso.getText().isEmpty()) {
                alerta(new ArrayList<>(List.of("Campo peso no puede estar vacío")));
                return false;
            }
            animal.setPeso(Double.parseDouble(txtPeso.getText()));

            // Validación y establecimiento de las observaciones
            if (txtObservaciones.getText().isEmpty()) {
                alerta(new ArrayList<>(List.of("Campo observaciones no puede estar vacío")));
                return false;
            }
            animal.setObservaciones(txtObservaciones.getText());
            LocalDate localDate = datePicker.getValue();
            if (localDate != null) {
                // Convert LocalDate to Date
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                animal.setFechaPrimeraConsulta(date);
            } else {
                // Handle case where no date is selected
                alerta(new ArrayList<>(List.of("Por favor, seleccione una fecha para la primera consulta.")));
            }
            // Establecimiento de la imagen
            animal.setFoto(imagenBlob);

            // Lógica para insertar el animal en la base de datos
            int id_animal = animalDao.insertar(animal);
            if (id_animal == -1) {
                alerta(new ArrayList<>(List.of("No se han podido cargar los datos.")));
                return false;
            } else {
                animal.setId(id_animal);
                confirmacion(new ArrayList<>(List.of("Animal creado correctamente")));
                return true;
            }
        } catch (NumberFormatException e) {
            alerta(new ArrayList<>(List.of("Errores de formato numérico. Asegúrese de que los campos edad y peso sean numéricos.")));
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            alerta(new ArrayList<>(List.of("Se produjo un error al crear el animal.")));
            return false;
        }
    }

    /**
     * Muestra una alerta de error con los mensajes especificados.
     *
     * @param textos Lista de mensajes de error.
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
     * Muestra una confirmación de éxito con los mensajes especificados.
     *
     * @param textos Lista de mensajes de confirmación.
     */
    public void confirmacion(ArrayList<String> textos) {
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /**
     * Abre un cuadro de diálogo de selección de archivos para que el usuario pueda elegir
     * una imagen y convierte la imagen seleccionada en un objeto Blob para almacenarla en
     * la base de datos.
     *
     * <p>Este método utiliza {@link FileChooser} para abrir un explorador de archivos que
     * permite seleccionar archivos de imagen en formato PNG, JPG o GIF. Si el usuario selecciona
     * un archivo válido, este se convierte en un Blob y se almacena en el atributo `imagenBlob`.
     * Además, muestra la imagen seleccionada en el componente {@code imgView}.</p>
     *
     * @param actionEvent el evento que dispara la selección de imagen.
     * @throws RuntimeException si ocurre un error de SQL al convertir la imagen en un Blob.
     */
    @FXML
    public void elegirImg(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Animal");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(null); // Cambia null por la ventana principal si es necesario
        if (file != null) {
            try {
                InputStream imagen = new FileInputStream(file);
                Blob blob = animalDao.convertFileToBlob(file);
                this.imagenBlob = blob;
                imgView.setImage(new Image(imagen));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
