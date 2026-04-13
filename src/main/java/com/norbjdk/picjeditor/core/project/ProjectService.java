package com.norbjdk.picjeditor.core.project;

import com.norbjdk.picjeditor.core.event.EventBus;
import com.norbjdk.picjeditor.core.event.dto.OpenPictureRequestedEvent;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.nio.file.Path;

public class ProjectService {
    private static final ProjectService instance = new ProjectService();

    public static ProjectService getInstance() {
        return instance;
    }

    private Stage primaryStage;

    private ProjectService() {
        setupEventListeners();
    }

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(OpenPictureRequestedEvent.class, event -> handleOpenProjectRequested());
    }

    private void handleOpenProjectRequested() {
        Platform.runLater(() -> {
            File file = showFileChooser();
            if (file == null) return;

            loadFile(file.toPath());
        });
    }

    private File showFileChooser() {
        final FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );

        return fileChooser.showOpenDialog(primaryStage);
    }

    private void loadFile(Path path) {

    }
}
