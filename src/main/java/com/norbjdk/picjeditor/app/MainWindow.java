package com.norbjdk.picjeditor.app;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class MainWindow {
    private static final int WINDOW_HEIGHT = 820;
    private static final int WINDOW_WIDTH = 1250;

    private final BorderPane root;
    private final Scene scene;

    public MainWindow() {
        root = new BorderPane();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public BorderPane getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }
}
