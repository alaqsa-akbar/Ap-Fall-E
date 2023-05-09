
module com.example.ics108_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;


    opens com.example.ics108_project to javafx.fxml;
    exports com.example.ics108_project;
}