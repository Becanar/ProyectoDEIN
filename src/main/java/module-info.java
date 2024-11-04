module org.example.proyectodein {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.proyectodein to javafx.fxml;
    exports org.example.proyectodein;
}