package com.norbjdk.picjeditor.app;

import com.norbjdk.picjeditor.ui.component.MenuBar;
import com.norbjdk.picjeditor.ui.component.NavigationBar;
import com.norbjdk.picjeditor.ui.component.StatusBar;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class MainWindow {
    private static final int WINDOW_HEIGHT = 820;
    private static final int WINDOW_WIDTH = 1250;

    private final BorderPane root;
    private final Scene scene;

    private NavigationBar navigationBar;
    private MenuBar menuBar;
    private StatusBar statusBar;

    public MainWindow() {
        root = new BorderPane();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        initComponents();
        setupStyles();
        layoutComponents();
    }

    private void initComponents() {
        navigationBar = new NavigationBar();
        menuBar = new MenuBar();
        statusBar = new StatusBar();
    }

    private void setupStyles() {
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/picjeditor/styles/views.css")).toExternalForm());
        root.getStyleClass().add("main-window");
    }

    private void layoutComponents() {
        root.setTop(new VBox(0, menuBar, navigationBar));
        root.setBottom(statusBar);
    }

    public BorderPane getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }
}
