module com.example.project3cs213 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.project3cs213 to javafx.fxml;
    exports com.example.project3cs213;
}