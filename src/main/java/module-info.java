module com.norbjdk.picjeditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.desktop;
    requires javafx.swing;


    opens com.norbjdk.picjeditor.app to javafx.fxml;
    exports com.norbjdk.picjeditor.app;
}