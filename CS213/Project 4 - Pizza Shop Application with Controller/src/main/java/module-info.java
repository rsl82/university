module com.example.pr4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;
    requires junit;


    opens com.example.pr4 to javafx.fxml;
    exports com.example.pr4;
}