package com.norbjdk.picjeditor.core.project;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class ProjectManager {
    private static final ProjectManager instance = new ProjectManager();

    public static ProjectManager getInstance() {
        return instance;
    }

    private final ObservableList<Project> openProjects = FXCollections.observableArrayList();

    private final ObjectProperty<Project> currentProject = new SimpleObjectProperty<>(null);

    private ProjectManager() {}

    public void addDocument(Project doc) {
        if (doc.getFilePath().get() != null) {
            Optional<Project> existing = openProjects
                    .stream()
                    .filter(project -> doc.getFilePath().get().equals(project.getFilePath().get()))
                    .findFirst();

            if (existing.isPresent()) {
                currentProject.set(existing.get());
                return;
            }
        }

        openProjects.add(doc);
        currentProject.set(doc);
    }

    public boolean closeDocument(Project doc) {
        if (!openProjects.remove(doc)) {
            return false;
        }

        if (doc.equals(currentProject.get())) {
            if (openProjects.isEmpty()) {
                currentProject.set(null);
            } else {
                currentProject.set(openProjects.getLast());
            }
        }

        return true;
    }

    public boolean hasUnsavedChanges() {
        return openProjects.stream().anyMatch(d -> d.getIsSaved().get());
    }

    public ObservableList<Project> getOpenProjects() {
        return openProjects;
    }

    public ObjectProperty<Project> getCurrentProject() {
        return currentProject;
    }

    public void setActiveDocument(Project doc) {
        currentProject.set(doc);
    }
}
