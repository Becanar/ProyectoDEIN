package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.DaoDeportista;
import com.example.proyectodein.model.Deportista;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DeportistaController implements Initializable {

    private Deportista deportista;
    private Blob imagen;

    @FXML // fx:id="foto"
    private ImageView foto;

    @FXML // fx:id="rbFemale"
    private RadioButton rbFemale;

    @FXML // fx:id="rbMale"
    private RadioButton rbMale;

    @FXML // fx:id="tgSexo"
    private ToggleGroup tgSexo;

    @FXML // fx:id="txtAltura"
    private TextField txtAltura;

    @FXML // fx:id="txtNombre"
    private TextField txtNombre;

    @FXML // fx:id="txtPeso"
    private TextField txtPeso;

    @FXML // fx:id="btnFotoBorrar"
    private Button btnFotoBorrar;

    @FXML
    private ResourceBundle resources;

    /**
     * Constructor para crear un nuevo deportista o editar uno existente.
     *
     * @param deportista El deportista a editar (si es nulo, se crea un nuevo deportista).
     */
    public DeportistaController(Deportista deportista) {
        this.deportista = deportista;
    }

    /**
     * Constructor vacío para crear un nuevo deportista.
     */
    public DeportistaController() {
        this.deportista = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        this.imagen = null;

        if (deportista != null) {
            // Rellenar los campos con los datos del deportista existente
            txtNombre.setText(deportista.getNombre());
            if (deportista.getSexo() == 'F') {
                rbFemale.setSelected(true);
                rbMale.setSelected(false);
            } else {
                rbMale.setSelected(true);
                rbFemale.setSelected(false);
            }
            txtPeso.setText(deportista.getPeso() + "");
            txtAltura.setText(deportista.getAltura() + "");

            // Mostrar la foto si existe
            if (deportista.getFoto() != null) {
                this.imagen = deportista.getFoto();
                try {
                    InputStream imagenStream = deportista.getFoto().getBinaryStream();
                    foto.setImage(new Image(imagenStream));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                btnFotoBorrar.setDisable(false);
            } else {
                // Si no tiene foto, cargar la imagen predeterminada (null.jpg)
                foto.setImage(new Image(getClass().getResourceAsStream("/com/example/proyectodein/images/null.jpg")));
                btnFotoBorrar.setDisable(true);
            }
        }
    }


    /**
     * Método para borrar la foto del deportista.
     *
     * @param event El evento de acción asociado al botón de borrar foto.
     */
    @FXML
    void borrarFoto(ActionEvent event) {
        imagen = null;
        foto.setImage(new Image(getClass().getResourceAsStream("/images/deportista.png")));
        btnFotoBorrar.setDisable(true);
    }

    /**
     * Método para cancelar la operación y cerrar la ventana.
     *
     * @param event El evento de acción asociado al botón de cancelar.
     */
    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Método para guardar los datos del deportista.
     * Si se está editando, actualiza los datos, si no, crea un nuevo deportista.
     *
     * @param event El evento de acción asociado al botón de guardar.
     */
    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> errores = validar();

        if (!errores.isEmpty()) {
            alerta(errores);
        } else {
            Deportista nuevo = new Deportista();
            nuevo.setNombre(txtNombre.getText());

            // Configuración del sexo
            if (rbFemale.isSelected()) {
                nuevo.setSexo('F');
            } else {
                nuevo.setSexo('M');
            }

            // Verificación de deportista duplicado en la base de datos
            Deportista existente = DaoDeportista.getDeportista(nuevo.getNombre(), nuevo.getSexo());
            if (existente == null || (this.deportista != null && existente.getIdDeportista() == this.deportista.getIdDeportista())) {

                // Configuración de altura y peso
                nuevo.setPeso(Integer.parseInt(txtPeso.getText()));
                nuevo.setAltura(Integer.parseInt(txtAltura.getText()));

                // Configuración de la imagen (Blob)
                if (this.imagen == null) {
                    try {
                        // Usa una imagen predeterminada en caso de que la imagen esté ausente
                        InputStream imagenDefault = getClass().getResourceAsStream("/com/example/proyectodein/images/null.jpg");
                        Blob defaultBlob = DaoDeportista.convertInputStreamToBlob(imagenDefault);
                        nuevo.setFoto(defaultBlob);
                    } catch (Exception e) {
                        errores.add("Error al cargar la imagen predeterminada.");
                        alerta(errores);
                        return;
                    }
                } else {
                    // Si ya tenemos una imagen seleccionada
                    nuevo.setFoto(this.imagen);
                }

                // Inserta o actualiza el deportista en la base de datos
                if (this.deportista == null) {
                    // Insertar nuevo deportista
                    int id = DaoDeportista.insertar(nuevo);
                    if (id == -1) {
                        ArrayList<String> failMessages = new ArrayList<>();
                        failMessages.add(resources.getString("save.fail"));
                        alerta(failMessages);
                    } else {
                        ArrayList<String> successMessages = new ArrayList<>();
                        successMessages.add(resources.getString("save.athlete"));
                        confirmacion(successMessages);
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    // Actualizar deportista existente
                    if (DaoDeportista.modificar(this.deportista, nuevo)) {
                        ArrayList<String> successMessages = new ArrayList<>();
                        successMessages.add(resources.getString("update.athlete"));
                        confirmacion(successMessages);
                        Stage stage = (Stage) txtNombre.getScene().getWindow();
                        stage.close();
                    } else {
                        ArrayList<String> failMessages = new ArrayList<>();
                        failMessages.add(resources.getString("save.fail"));
                        alerta(failMessages);
                    }
                }
            } else {
                ArrayList<String> failMessages = new ArrayList<>();
                failMessages.add(resources.getString("duplicate.athlete"));
                alerta(failMessages);
            }
        }
    }


    /**
     * Valida los datos ingresados del deportista.
     *
     * @return Una lista de errores de validación (si los hay).
     */
    private ArrayList<String> validar() {
        ArrayList<String> errores = new ArrayList<>();
        if (txtNombre.getText().isEmpty()) {
            errores.add(resources.getString("validate.athlete.name"));
        }
        if (txtPeso.getText().isEmpty()) {
            errores.add(resources.getString("validate.athlete.weight"));
        } else {
            try {
                Integer.parseInt(txtPeso.getText());
            } catch (NumberFormatException e) {
                errores.add(resources.getString("validate.athlete.weight.num"));
            }
        }
        if (txtAltura.getText().isEmpty()) {
            errores.add(resources.getString("validate.athlete.height"));
        } else {
            try {
                Integer.parseInt(txtAltura.getText());
            } catch (NumberFormatException e) {
                errores.add(resources.getString("validate.athlete.height.num"));
            }
        }
        return errores;
    }

    /**
     * Método para seleccionar y cargar una foto para el deportista.
     *
     * @param event El evento de acción asociado al botón de seleccionar foto.
     */
    @FXML
    void seleccionImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resources.getString("athlete.photo.chooser"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(null);
        try {
            double kbs = (double) file.length() / 1024;
            if (kbs > 64) {
                ArrayList<String> failMessages = new ArrayList<>();
                failMessages.add(resources.getString("athlete.photo.chooser.size"));
                alerta(failMessages);
            } else {
                InputStream imagen = new FileInputStream(file);
                Blob blob = DaoDeportista.convertFileToBlob(file);
                this.imagen = blob;
                foto.setImage(new Image(imagen));
                btnFotoBorrar.setDisable(false);
            }
        } catch (IOException | NullPointerException e) {
            System.out.println("Imagen no seleccionada");
        } catch (SQLException e) {
            ArrayList<String> failMessages = new ArrayList<>();
            failMessages.add(resources.getString("athlete.photo.chooser.fail"));
            alerta(failMessages);
        }
    }

    /**
     * Muestra una alerta con los mensajes de error proporcionados.
     *
     * @param textos Los textos de error a mostrar en la alerta.
     */
    public void alerta(ArrayList<String> textos) {
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("error.title"));
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de confirmación con los mensajes proporcionados.
     *
     * @param mensajes Los mensajes de confirmación a mostrar.
     */
    public void confirmacion(ArrayList<String> mensajes) {
        String contenido = String.join("\n", mensajes);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
