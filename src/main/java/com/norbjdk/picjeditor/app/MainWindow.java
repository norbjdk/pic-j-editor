package com.norbjdk.picjeditor.app;

import com.norbjdk.picjeditor.core.event.EventBus;
import com.norbjdk.picjeditor.core.event.dto.ChangeViewRequestedEvent;
import com.norbjdk.picjeditor.core.event.dto.ViewChangedEvent;
import com.norbjdk.picjeditor.ui.component.MenuBar;
import com.norbjdk.picjeditor.ui.component.NavigationBar;
import com.norbjdk.picjeditor.ui.component.StatusBar;
import com.norbjdk.picjeditor.ui.model.ViewName;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
        setupEventListeners();
        EventBus.getInstance().publish(new ChangeViewRequestedEvent(ViewName.HOME));
    }

    private void initComponents() {
        navigationBar = new NavigationBar();
        menuBar = new MenuBar();
        statusBar = new StatusBar();
    }

    private void setupStyles() {
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/picjeditor/styles/views.css")).toExternalForm());
        root.getStyleClass().add("main-window");

        VBox.setMargin(navigationBar, new Insets(14));
    }

    private void layoutComponents() {
        final VBox topContainer = new VBox(0, menuBar, navigationBar);

        root.setTop(topContainer);
        root.setBottom(statusBar);
    }

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(ViewChangedEvent.class, this::handleViewChanged);
    }

    private void handleViewChanged(ViewChangedEvent event) {
        final var newView = (Node) event.getView();

        if (newView != null && root.getCenter() != newView) {
            root.setCenter(newView);
            BorderPane.setMargin(newView, new Insets(20));
        }
    }


    public BorderPane getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }
}
