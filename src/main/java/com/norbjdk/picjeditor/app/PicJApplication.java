package com.norbjdk.picjeditor.app;

import com.norbjdk.picjeditor.core.project.ProjectService;
import com.norbjdk.picjeditor.ui.manager.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class PicJApplication extends Application {
    private static final int MIN_WIDTH = 1200;
    private static final int MIN_HEIGHT = 650;

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        final MainWindow mainWindow = new MainWindow();

        primaryStage.setTitle("Pic-J Editor");
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setScene(mainWindow.getScene());

        primaryStage.show();
    }

    @Override
    public void init() {
        final ViewManager viewManager = ViewManager.getInstance();
        System.out.println("Initialized View Manager");

        final ProjectService projectService = ProjectService.getInstance();
        projectService.init(primaryStage);
        System.out.println("Initialized Project Service");
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
