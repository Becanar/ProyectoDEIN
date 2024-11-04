module com.example.ejercicios {
    requires javafx.graphics;  // Asegúrate de que esta línea esté presente
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql.rowset; // Verifica que esto sea necesario

    exports com.example.proyectodein.app;
    opens com.example.proyectodein.app to javafx.fxml;
    exports com.example.proyectodein.controladores;
    opens com.example.proyectodein.controladores to javafx.fxml;
}
