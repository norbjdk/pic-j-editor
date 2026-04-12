package com.norbjdk.picjeditor.ui.component;

import com.norbjdk.picjeditor.ui.model.Presentable;
import javafx.scene.control.Menu;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class MenuBar extends HBox implements Presentable {
    private javafx.scene.control.MenuBar menuBar;

    public MenuBar() {
        present();
    }

    @Override
    public void initComponents() {
        menuBar = new javafx.scene.control.MenuBar();
    }

    @Override
    public void setupComponents() {
        final Menu fileMenu = new Menu("File");
        final Menu editMenu = new Menu("Edit");
        final Menu viewMenu = new Menu("View");
        final Menu addMenu = new Menu("Add");
        final Menu formatMenu = new Menu("Format");
        final Menu toolsMenu = new Menu("Tools");
        final Menu helpMenu = new Menu("Help");

        menuBar.getMenus().addAll(
                fileMenu,
                editMenu,
                viewMenu,
                addMenu,
                formatMenu,
                toolsMenu,
                helpMenu
        );
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/picjeditor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("menu-bar");
    }

    @Override
    public void setupLayout() {
        this.getChildren().add(menuBar);
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
