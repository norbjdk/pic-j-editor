package com.norbjdk.picjeditor.ui.component;

import com.norbjdk.picjeditor.core.event.EventBus;
import com.norbjdk.picjeditor.core.event.dto.ChangeViewRequestedEvent;
import com.norbjdk.picjeditor.ui.model.Presentable;
import com.norbjdk.picjeditor.ui.model.ViewName;
import com.norbjdk.picjeditor.ui.util.ButtonFactory;
import com.norbjdk.picjeditor.ui.util.IconUtil;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Objects;

public class NavigationBar extends HBox implements Presentable {
    private final EventBus eventBus = EventBus.getInstance();

    private Button homeBtn;
    private Button editorBtn;
    private Button collectionBtn;
    private Button settingsBtn;

    public NavigationBar() {
        present();
    }

    @Override
    public void initComponents() {
        homeBtn = ButtonFactory.createButton("Home", "home-btn", "Switch to Home", "navbar-btn");
        editorBtn = ButtonFactory.createButton("Editor", "editor-btn", "Switch to Editor", "navbar-btn");
        collectionBtn = ButtonFactory.createButton("Collection", "collection-btn", "Switch to Collection", "navbar-btn");
        settingsBtn = ButtonFactory.createButton("Settings", "settings-btn", "Switch to Settings", "navbar-btn");
    }

    @Override
    public void setupComponents() {
        editorBtn.setDisable(true);

        IconUtil.addIcon(homeBtn, FontAwesomeSolid.HOME, 15, Color.rgb(5, 5, 5));
        IconUtil.addIcon(editorBtn, FontAwesomeSolid.PEN_FANCY, 15, Color.rgb(5, 5, 5));
        IconUtil.addIcon(collectionBtn, FontAwesomeSolid.LAYER_GROUP, 15, Color.rgb(5, 5, 5));
        IconUtil.addIcon(settingsBtn, FontAwesomeSolid.COG, 15, Color.rgb(5, 5, 5));
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/picjeditor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("navbar");
    }

    @Override
    public void setupLayout() {
        this.getChildren().addAll(
                homeBtn,
                collectionBtn,
                createSpacer(),
                editorBtn,
                createSpacer(),
                settingsBtn
        );
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {
        homeBtn.setOnAction(actionEvent -> handleHomeButtonClicked());
        editorBtn.setOnAction(actionEvent -> handleEditorButtonClicked());
        collectionBtn.setOnAction(actionEvent -> handleCollectionButtonClicked());
        settingsBtn.setOnAction(actionEvent -> handleSettingsButtonClicked());
    }

    private void handleHomeButtonClicked() {
        eventBus.publish(new ChangeViewRequestedEvent(ViewName.HOME));
    }

    private void handleEditorButtonClicked() {
        eventBus.publish(new ChangeViewRequestedEvent(ViewName.EDITOR));
    }

    private void handleCollectionButtonClicked() {
        eventBus.publish(new ChangeViewRequestedEvent(ViewName.COLLECTION));
    }

    private void handleSettingsButtonClicked() {
        eventBus.publish(new ChangeViewRequestedEvent(ViewName.SETTINGS));
    }

    private Region createSpacer() {
        final Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        return region;
    }
}
