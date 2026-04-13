package com.norbjdk.picjeditor.ui.view;

import com.norbjdk.picjeditor.core.event.EventBus;
import com.norbjdk.picjeditor.core.event.dto.ImageSelectedEvent;
import com.norbjdk.picjeditor.core.picture.ColorStats;
import com.norbjdk.picjeditor.core.picture.ImageData;
import com.norbjdk.picjeditor.core.picture.PictureService;
import com.norbjdk.picjeditor.core.project.ProjectManager;
import com.norbjdk.picjeditor.ui.model.Presentable;
import com.norbjdk.picjeditor.ui.model.Viewable;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.nio.file.Path;
import java.util.Objects;

public class EditorView extends BorderPane implements Presentable, Viewable {
    private VBox sidePanel;
    private Label sidePanelTitle;
    private ListView<ImageData> imageList;

    private ScrollPane imageScrollPane;
    private VBox imageArea;
    private Label imageNameLabel;
    private HBox imageCompareRow;
    private VBox originalBox;
    private VBox standardizedBox;
    private VBox processedBox;
    private ImageView originalView;
    private ImageView standardizedView;
    private ImageView processedView;
    private Label originalLabel;
    private Label standardizedLabel;
    private Label processedLabel;
    private Label processedSizeLabel;

    private VBox statsPanel;
    private Label statsTitle;
    private Label statRgb;
    private Label statHsv;
    private Label statEntropy;
    private Label statUnique;
    private Label statDominant;
    private Label statBrightness;
    private Label statDark;
    private Label statBright;
    private Rectangle dominantColorPreview;

    private VBox actionsPanel;
    private Label actionsTitle;
    private Button binarizeBtn;
    private Button rotateBtn;
    private Button contrastBtn;

    private ImageData selectedImage;

    public EditorView() {
        present();
    }

    @Override
    public void initComponents() {
        sidePanel = new VBox();
        sidePanelTitle = new Label("Images");
        imageList = new ListView<>();

        imageArea = new VBox();
        imageScrollPane = new ScrollPane();
        imageNameLabel = new Label("Select an image from the list");
        imageCompareRow = new HBox();

        originalView = new ImageView();
        standardizedView = new ImageView();
        processedView = new ImageView();
        originalLabel = new Label("Original");
        standardizedLabel = new Label("Standardized");
        processedLabel = new Label("Processed");
        processedSizeLabel = new Label();
        originalBox = new VBox();
        standardizedBox = new VBox();
        processedBox = new VBox();

        statsPanel = new VBox();
        statsTitle = new Label("Color Statistics");
        statRgb = new Label();
        statHsv = new Label();
        statEntropy = new Label();
        statUnique = new Label();
        statDominant = new Label();
        statBrightness = new Label();
        statDark = new Label();
        statBright = new Label();
        dominantColorPreview = new Rectangle(18, 18);

        actionsPanel = new VBox();
        actionsTitle = new Label("Processing");
        binarizeBtn = new Button("Binarize (threshold 128)");
        rotateBtn = new Button("Rotate 45°");
        contrastBtn = new Button("Contrast Stretch");
    }

    @Override
    public void setupComponents() {
        double previewSize = 220;
        for (ImageView iv : new ImageView[]{ originalView, standardizedView, processedView }) {
            iv.setFitWidth(previewSize);
            iv.setFitHeight(previewSize);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
        }

        imageList.setPlaceholder(new Label("No images loaded"));
        imageList.setCellFactory(lv -> new ImageListCell());

        binarizeBtn.setDisable(true);
        rotateBtn.setDisable(true);
        contrastBtn.setDisable(true);

        dominantColorPreview.setStroke(Color.GRAY);
        dominantColorPreview.setStrokeWidth(1);
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/com/norbjdk/picjeditor/styles/views.css")).toExternalForm()
        );
        this.getStyleClass().add("editor-view");

        sidePanel.getStyleClass().add("editor-sidebar");
        sidePanelTitle.getStyleClass().add("sidebar-title");
        imageList.getStyleClass().add("image-list");

        imageArea.getStyleClass().add("editor-image-area");
        imageNameLabel.getStyleClass().add("editor-image-name");
        originalLabel.getStyleClass().addAll("image-caption");
        standardizedLabel.getStyleClass().addAll("image-caption");
        processedLabel.getStyleClass().addAll("image-caption");
        processedSizeLabel.getStyleClass().add("image-size-label");

        statsPanel.getStyleClass().add("editor-stats-panel");
        statsTitle.getStyleClass().add("h2");
        for (Label l : new Label[]{ statRgb, statHsv, statEntropy, statUnique,
                statDominant, statBrightness, statDark, statBright }) {
            l.getStyleClass().add("stat-label");
            l.setWrapText(true);
        }

        actionsPanel.getStyleClass().add("editor-actions-panel");
        actionsTitle.getStyleClass().add("h2");
        for (Button b : new Button[]{ binarizeBtn, rotateBtn, contrastBtn }) {
            b.getStyleClass().add("primary-button");
            b.setMaxWidth(Double.MAX_VALUE);
        }
    }

    @Override
    public void setupLayout() {
        // Sidebar
        sidePanel.setSpacing(8);
        sidePanel.setPadding(new Insets(16));
        sidePanel.setPrefWidth(200);
        VBox.setVgrow(imageList, Priority.ALWAYS);
        sidePanel.getChildren().addAll(sidePanelTitle, imageList);

        for (VBox[] pair : new VBox[][]{{ originalBox, null }, { standardizedBox, null }, { processedBox, null }}) {

        }
        originalBox.setAlignment(Pos.TOP_CENTER);
        originalBox.setSpacing(6);
        originalBox.getChildren().addAll(originalLabel, originalView);

        standardizedBox.setAlignment(Pos.TOP_CENTER);
        standardizedBox.setSpacing(6);
        standardizedBox.getChildren().addAll(standardizedLabel, standardizedView);

        processedBox.setAlignment(Pos.TOP_CENTER);
        processedBox.setSpacing(6);
        processedBox.getChildren().addAll(processedLabel, processedView, processedSizeLabel);

        imageCompareRow.setSpacing(24);
        imageCompareRow.setAlignment(Pos.TOP_CENTER);
        imageCompareRow.setPadding(new Insets(16));
        imageCompareRow.getChildren().addAll(originalBox, standardizedBox, processedBox);

        imageArea.setSpacing(12);
        imageArea.setPadding(new Insets(16));
        imageArea.getChildren().addAll(imageNameLabel, imageCompareRow);

        imageScrollPane.setContent(imageArea);
        imageScrollPane.setFitToWidth(true);

        HBox dominantRow = new HBox(8);
        dominantRow.setAlignment(Pos.CENTER_LEFT);
        dominantRow.getChildren().addAll(dominantColorPreview, statDominant);

        statsPanel.setSpacing(8);
        statsPanel.setPadding(new Insets(16));
        statsPanel.setPrefWidth(240);
        statsPanel.getChildren().addAll(
                statsTitle,
                statRgb, statHsv,
                new Separator(),
                statEntropy, statUnique,
                new Separator(),
                statBrightness, statDark, statBright,
                new Separator(),
                dominantRow
        );

        actionsPanel.setSpacing(10);
        actionsPanel.setPadding(new Insets(16));
        actionsPanel.setPrefWidth(200);
        actionsPanel.getChildren().addAll(actionsTitle, binarizeBtn, rotateBtn, contrastBtn);

        VBox rightPanel = new VBox(16);
        rightPanel.setPadding(new Insets(0, 8, 0, 0));
        VBox.setVgrow(statsPanel, Priority.ALWAYS);
        rightPanel.getChildren().addAll(statsPanel, actionsPanel);

        this.setLeft(sidePanel);
        this.setCenter(imageScrollPane);
        this.setRight(rightPanel);
    }

    @Override
    public void setupEventListeners() {
        ProjectManager.getInstance().getCurrentProject().addListener((obs, oldProject, newProject) -> {
            if (newProject != null) {
                bindToProject(newProject);
            } else {
                Platform.runLater(() -> imageList.getItems().clear());
            }
        });

        var currentProject = ProjectManager.getInstance().getCurrentProject().get();
        if (currentProject != null) {
            bindToProject(currentProject);
        }

        EventBus.getInstance().subscribe(ImageSelectedEvent.class, event ->
                Platform.runLater(() -> selectImage(event.getImageData()))
        );
    }

    @Override
    public void setupEventHandlers() {
        imageList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) selectImage(newVal);
        });

        binarizeBtn.setOnAction(e -> handleBinarize());
        rotateBtn.setOnAction(e -> handleRotate());
        contrastBtn.setOnAction(e -> handleContrastStretch());
    }

    private void bindToProject(com.norbjdk.picjeditor.core.project.Project project) {
        Platform.runLater(() -> {
            imageList.setItems(project.getImages());
            project.getImages().addListener((ListChangeListener<ImageData>) change -> {
                while (change.next()) {
                    if (change.wasAdded() && !change.getAddedSubList().isEmpty()) {
                        Platform.runLater(() ->
                                imageList.getSelectionModel().select(change.getAddedSubList().get(0))
                        );
                    }
                }
            });
        });
    }

    private void selectImage(ImageData data) {
        selectedImage = data;

        imageNameLabel.setText(data.getOriginalName()
                               + "  |  " + data.getOriginalDimensions()
                               + " → " + data.getStdDimensions()
                               + "  |  " + data.getOriginalSizeKB() + " KB → " + data.getStandardizedSizeKB() + " KB");

        originalView.setImage(data.getOriginalImage());
        standardizedView.setImage(data.getStandardizedImage());

        if (data.getProcessedImage() != null) {
            processedView.setImage(data.getProcessedImage());
            processedLabel.setText(data.getProcessedDescription() != null
                    ? data.getProcessedDescription() : "Processed");
            processedSizeLabel.setText(data.getProcessedSizeBytes() / 1024 + " KB");
        } else {
            processedView.setImage(null);
            processedLabel.setText("Processed");
            processedSizeLabel.setText("—");
        }

        updateStats(data.getColorStats());

        binarizeBtn.setDisable(false);
        rotateBtn.setDisable(false);
        contrastBtn.setDisable(false);
    }

    private void updateStats(ColorStats s) {
        if (s == null) {
            for (Label l : new Label[]{ statRgb, statHsv, statEntropy, statUnique,
                    statDominant, statBrightness, statDark, statBright }) {
                l.setText("—");
            }
            return;
        }

        statRgb.setText(String.format("RGB  R: %.1f  G: %.1f  B: %.1f",
                s.getMeanR(), s.getMeanG(), s.getMeanB()));

        statHsv.setText(String.format("HSV  H: %.3f  S: %.3f  V: %.3f",
                s.getMeanH(), s.getMeanS(), s.getMeanV()));

        statEntropy.setText(String.format("Entropy: %.2f bits", s.getEntropy()));

        statUnique.setText("Unique colors: " + s.getUniqueColors());

        statDominant.setText(String.format("Dominant: rgb(%d, %d, %d)",
                s.getDominantR(), s.getDominantG(), s.getDominantB()));

        dominantColorPreview.setFill(Color.rgb(
                s.getDominantR(), s.getDominantG(), s.getDominantB()));

        statBrightness.setText(String.format("Brightness variance: %.4f", s.getBrightnessVariance()));
        statDark.setText(String.format("Dark pixels (V<0.3): %.1f%%", s.getDarkPixelRatio() * 100));
        statBright.setText(String.format("Bright pixels (V>0.7): %.1f%%", s.getBrightPixelRatio() * 100));
    }

    private void handleBinarize() {
        if (selectedImage == null) return;
        Path outputDir = resolveOutputDir();
        ImageData result = PictureService.getInstance().applyBinarization(selectedImage, 128, outputDir);
        mergeProcessedResult(result);
    }

    private void handleRotate() {
        if (selectedImage == null) return;
        Path outputDir = resolveOutputDir();
        ImageData result = PictureService.getInstance().applyRotation(selectedImage, 45, outputDir);
        mergeProcessedResult(result);
    }

    private void handleContrastStretch() {
        if (selectedImage == null) return;
        Path outputDir = resolveOutputDir();
        ImageData result = PictureService.getInstance().applyContrastStretch(selectedImage, outputDir);
        mergeProcessedResult(result);
    }

    private void mergeProcessedResult(ImageData result) {
        if (result == null) return;
        selectedImage.setProcessedImage(result.getProcessedImage());
        selectedImage.setProcessedDescription(result.getProcessedDescription());
        selectedImage.setProcessedSizeBytes(result.getProcessedSizeBytes());
        selectImage(selectedImage); // odświeżenie widoku
    }

    private Path resolveOutputDir() {
        if (selectedImage.getOriginalPath() != null
            && selectedImage.getOriginalPath().getParent() != null) {
            return selectedImage.getOriginalPath().getParent();
        }
        return Path.of(System.getProperty("user.dir"));
    }

    private static class ImageListCell extends ListCell<ImageData> {
        private final ImageView thumb = new ImageView();
        private final Label nameLabel = new Label();
        private final Label dimLabel = new Label();
        private final VBox content = new VBox(2);
        private final HBox row = new HBox(8);

        ImageListCell() {
            thumb.setFitWidth(40);
            thumb.setFitHeight(40);
            thumb.setPreserveRatio(true);
            thumb.setSmooth(true);

            nameLabel.getStyleClass().add("cell-name");
            dimLabel.getStyleClass().add("cell-dim");

            content.getChildren().addAll(nameLabel, dimLabel);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getChildren().addAll(thumb, content);
        }

        @Override
        protected void updateItem(ImageData item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                thumb.setImage(item.getStandardizedImage());
                nameLabel.setText(item.getOriginalName());
                dimLabel.setText(item.getOriginalDimensions() + " → " + item.getStdDimensions());
                setGraphic(row);
            }
        }
    }
}