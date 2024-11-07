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

    public DeportistaController(Deportista deportista) {
        this.deportista = deportista;
    }

    public DeportistaController() {
        this.deportista = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        this.imagen = null;
        if (deportista != null) {
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
            if (deportista.getFoto() != null) {
                this.imagen = deportista.getFoto();
                try {
                    InputStream imagen = deportista.getFoto().getBinaryStream();
                    foto.setImage(new Image(imagen));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                btnFotoBorrar.setDisable(false);
            }
        }
    }

    @FXML
    void borrarFoto(ActionEvent event) {
        imagen = null;
        foto.setImage(new Image(getClass().getResourceAsStream("/images/deportista.png")));
        btnFotoBorrar.setDisable(true);
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    @FXML
    void guardar(ActionEvent event) {
        ArrayList<String> errores = validar();
        if (!errores.isEmpty()) {
            alerta(errores);
        } else {
            Deportista nuevo = new Deportista();
            nuevo.setNombre(txtNombre.getText());
            if (rbFemale.isSelected()) {
                nuevo.setSexo('F');
            } else {
                nuevo.setSexo('M');
            }
            if (DaoDeportista.getDeportista(nuevo.getNombre(), nuevo.getSexo()) == null) {
                nuevo.setPeso(Integer.parseInt(txtPeso.getText()));
                nuevo.setAltura(Integer.parseInt(txtAltura.getText()));
                nuevo.setFoto(this.imagen);
                if (this.deportista == null) {
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

    public void alerta(ArrayList<String> textos) {
        String contenido = String.join("\n", textos);
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText(null);
        alerta.setTitle(resources.getString("error.title"));
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    public void confirmacion(ArrayList<String> mensajes) {
        String contenido = String.join("\n", mensajes);
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
