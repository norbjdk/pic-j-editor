package com.norbjdk.picjeditor.core.project;

import com.norbjdk.picjeditor.core.picture.ImageData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.time.Instant;

public class Project {
    private final String id;

    private final ObjectProperty<Path> filePath = new SimpleObjectProperty<>(null);
    private final BooleanProperty isSaved = new SimpleBooleanProperty(false);
    private final ObjectProperty<Instant> lastModified = new SimpleObjectProperty<>(Instant.now());
    private final ObservableList<ImageData> images = FXCollections.observableArrayList();

    public static Project createNew() {
        return new Project(generateId());
    }

    private Project(String id) {
        this.id = id;
    }

    public void markUnsaved() {
        isSaved.set(false);
        lastModified.set(Instant.now());
    }

    public void markSaved() {
        isSaved.set(true);
    }

    public void addImage(ImageData imageData) {
        images.add(imageData);
        markUnsaved();
    }

    public String getId() { return id; }

    public BooleanProperty getIsSaved() { return isSaved; }

    public ObjectProperty<Path> getFilePath() { return filePath; }

    public ObservableList<ImageData> getImages() { return images; }

    private static String generateId() {
        return "pic-j-" + System.nanoTime();
    }
}