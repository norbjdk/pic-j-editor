module com.norbjdk.picjeditor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.norbjdk.picjeditor.app to javafx.fxml;
    exports com.norbjdk.picjeditor.app;
}