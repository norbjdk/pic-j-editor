package com.norbjdk.picjeditor.ui.component;

import com.norbjdk.picjeditor.ui.model.Presentable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.Objects;

public class StatusBar extends HBox implements Presentable {

    private Label changesNumberLabel;
    private Label filePathLabel;

    public StatusBar() {
        present();
    }

    @Override
    public void initComponents() {
        changesNumberLabel = new Label();
        filePathLabel = new Label();
    }

    @Override
    public void setupComponents() {
        changesNumberLabel.setText("0 changes made");
        filePathLabel.setText("File Path: NaN");
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/picjeditor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("statusbar");
    }

    @Override
    public void setupLayout() {
        this.getChildren().addAll(
                changesNumberLabel,
                createSpacer(),
                filePathLabel
        );
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }

    private Region createSpacer() {
        final Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        return region;
    }
}
