module com.norbjdk.picjeditor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.norbjdk.picjeditor to javafx.fxml;
    exports com.norbjdk.picjeditor;
}