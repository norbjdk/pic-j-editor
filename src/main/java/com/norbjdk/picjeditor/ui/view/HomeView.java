package com.norbjdk.picjeditor.ui.view;

import com.norbjdk.picjeditor.ui.model.Presentable;
import com.norbjdk.picjeditor.ui.model.Viewable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class HomeView extends ScrollPane implements Presentable, Viewable {

    private VBox contentContainer;
    private Label header;

    public HomeView() {
        present();
    }

    @Override
    public void initComponents() {
        contentContainer = new VBox();
        header = new Label();
    }

    @Override
    public void setupComponents() {
        header.setText("Welcome to Pic-J Editor");
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/picjeditor/styles/views.css")).toExternalForm());
        this.getStyleClass().add("home-view");
        this.setFitToWidth(true);

        contentContainer.getStyleClass().add("home-container");

        header.getStyleClass().add("home-header");
        addReflection(header);
    }

    @Override
    public void setupLayout() {
        this.setContent(contentContainer);

        contentContainer.getChildren().addAll(
                createComponentContainer(new Insets(50), "", header),
                createComponentContainer(new Insets(40), "",
                        createSpacer(),
                        createAboutColumn(),
                        createSpacer(),
                        createInstructionColumn(),
                        createSpacer())
        );
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }

    private HBox createComponentContainer(Insets padding, String style, Node ... components) {
        final HBox container = new HBox();

        container.setPadding(padding);
        container.getStyleClass().add(style);
        container.getChildren().addAll(components);

        return container;
    }

    private VBox createAboutColumn() {
        final VBox container = new VBox();
        container.getStyleClass().add("readme-container");

        final Label titleLabel = new Label("Image Analyzer Project");
        titleLabel.getStyleClass().add("h1");

        final Label subtitleLabel = new Label("Justified selection of techniques for the intended analysis");
        subtitleLabel.getStyleClass().add("subtitle");

        final Label firstSectionLabel = new Label("Short description");
        firstSectionLabel.getStyleClass().add("h2");

        final Label descriptionLabel = new Label("""
            The aim of the project is to make a justified selection of techniques 
            for the intended analysis, rather than to use as many tools as possible.
            
            Various input formats, various dimensions and resolutions,
            various characteristics: a few photos, a few graphics/drawings,
            flowcharts/scientific diagrams/sketches.
            """);
        descriptionLabel.getStyleClass().add("body-text");
        descriptionLabel.setWrapText(true);

        final Label secondSectionLabel = new Label("Author");
        secondSectionLabel.getStyleClass().add("h2");

        final Label authorLabel = new Label("Norbert Dominkiewicz");
        authorLabel.getStyleClass().add("author-name");

        final Label githubLabel = new Label("[https://github.com/norbjdk] @norbjdk");
        githubLabel.getStyleClass().add("github-link");

        container.getChildren().addAll(
                titleLabel, subtitleLabel,
                firstSectionLabel, descriptionLabel,
                secondSectionLabel, authorLabel, githubLabel
        );

        return container;
    }

    private VBox createInstructionColumn() {
        final VBox container = new VBox();
        container.getStyleClass().add("instruction-container");

        final Label titleLabel = new Label("How to start");
        titleLabel.getStyleClass().add("h2");

        final VBox stepsBox = new VBox(15);
        stepsBox.getStyleClass().add("steps-box");

        final Label step1 = new Label("1. Click the 'Open Image' button below.");
        final Label step2 = new Label("2. Select your desired photo from the file browser.");
        final Label step3 = new Label("3. Use the editor view to explore available operations.");

        step1.getStyleClass().add("step-text");
        step2.getStyleClass().add("step-text");
        step3.getStyleClass().add("step-text");
        step3.setWrapText(true);

        final Button openButton = new Button("Open Image");
        openButton.getStyleClass().add("primary-button");

        stepsBox.getChildren().addAll(step1, step2, step3, openButton);
        container.getChildren().addAll(titleLabel, stepsBox);

        return container;
    }


    private void addReflection(Node target) {
        final Reflection reflection = new Reflection();

        reflection.setFraction(0.7);
        reflection.setTopOpacity(0.5);
        reflection.setBottomOpacity(0.0);
        reflection.setTopOffset(0.0);

        target.setEffect(reflection);
    }

    private Region createSpacer() {
        final Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        return region;
    }
}
