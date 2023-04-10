module com.example.ics108_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ics108_project to javafx.fxml;
    exports com.example.ics108_project;
}