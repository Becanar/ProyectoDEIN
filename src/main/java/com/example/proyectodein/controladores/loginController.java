package com.example.proyectodein.controladores;

import com.example.proyectodein.dao.userDao;
import com.example.proyectodein.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Clase que se encarga de controlar el login
 */
public class loginController implements Initializable {

    @FXML
    private Button btLogin;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblUsuario;

    @FXML
    private GridPane rootPane;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsuario;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btLogin.setDefaultButton(true);
    }

    /**
     * Maneja el evento de inicio de sesión cuando el usuario presiona el botón de login.
     * Valida las credenciales del usuario e inicia la ventana de la aplicación si son correctas.
     *
     * @param event El evento de acción que se dispara al presionar el botón de login.
     */
    @FXML
    void login(ActionEvent event) {
        ArrayList<String> lst = new ArrayList<>();
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (usuario.equals("")) {
            lst.add("El usuario no puede estar vacío.");
        }
        if (password.equals("")) {
            lst.add("La contraseña no puede estar vacía.");
        }
        if (!lst.isEmpty()) {
            error(lst);
        } else {
            User user = userDao.getUsuario(usuario);
            if (user == null) {
                lst.add("Usuario no válido.");
                txtUsuario.setText("");
                txtPassword.setText("");
                error(lst);
            } else {
                if (password.equals(user.getPassword())) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(loginController.class.getResource("/com/example/proyectodein/fxml/animales.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        try {
                            Image img = new Image(getClass().getResource("/com/example/proyectodein/images/veet.jpg").toString());
                            stage.getIcons().add(img);
                        } catch (Exception e) {
                            System.out.println("Error al cargar la imagen: " + e.getMessage());
                        }
                        scene.getStylesheets().add(getClass().getResource("/com/example/proyectodein/estilo/style.css").toExternalForm());
                        stage.setTitle("VETERINARIA - ANIMALES");
                        stage.show();
                        Stage esta = (Stage) txtUsuario.getScene().getWindow();
                        esta.close();

                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        lst.add("No se ha podido abrir la ventana.");
                        error(lst);
                    }
                } else {
                    lst.add("Contraseña incorrecta");
                    txtPassword.setText("");
                    error(lst);
                }
            }
        }
    }

    /**
     * Muestra una alerta con un mensaje de error específico.
     *
     * @param lst Lista de mensajes de error a mostrar en la alerta.
     */
    private void error(ArrayList<String> lst) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(btLogin.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle("Error");
        String error = String.join("\n", lst);
        alert.setContentText(error);
        alert.showAndWait();
    }

}
