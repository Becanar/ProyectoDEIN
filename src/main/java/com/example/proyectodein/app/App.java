package com.example.proyectodein.app;

import com.example.proyectodein.model.Propiedades;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {

    /**
     * Método start, que inicializa y muestra la ventana principal de la aplicación.
     *
     * @param stage El escenario principal en el que se mostrará la interfaz.
     * @throws IOException Si hay un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        String idioma = Propiedades.getValor("language");
        ResourceBundle bundle = ResourceBundle.getBundle("/com/example/proyectodein/languages/lang", new Locale(idioma));
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/proyectodein/fxml/olimpiadas.fxml"),bundle);
        Scene scene = new Scene(fxmlLoader.load());
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
    }
    /**
     * Método main que lanza la aplicación.a
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch();
    }
}