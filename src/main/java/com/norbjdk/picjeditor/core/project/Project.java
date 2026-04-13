package com.norbjdk.picjeditor.core.project;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.nio.file.Path;
import java.time.Instant;

public class Project {
    private final String id;

    private final ObjectProperty<Path> filePath = new SimpleObjectProperty<>(null);
    private final BooleanProperty isSaved = new SimpleBooleanProperty(false);
    private final ObjectProperty<Instant> lastModified = new SimpleObjectProperty<>(Instant.now());

    public static Project createNew() {
        final Project project = new Project(generateId());

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

    public String getId() {
        return id;
    }

    public BooleanProperty getIsSaved() {
        return isSaved;
    }

    public ObjectProperty<Path> getFilePath() {
        return filePath;
    }

    private static String generateId() {
        return "pic-j-" + System.nanoTime();
    }
}
