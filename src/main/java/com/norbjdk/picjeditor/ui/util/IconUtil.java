package com.norbjdk.picjeditor.ui.util;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class IconUtil {
    private IconUtil() {
        throw new UnsupportedOperationException("Utility class! No instance allowed.");
    }

    /**
     * IconUtil - Icon level 1
     *
     * @param button Reference to JavaFX Button
     * @param solid Selected ikonli fontawesome enum type icon
     * @param size Value of icon size
     * @param color Chosen color for the icon
     */

    public static void addIcon(Button button, FontAwesomeSolid solid, int size, Color color) {
        addIcon(button, solid, size, color, ContentDisplay.LEFT);
    }

    /**
     * IconUtil - Icon level 2
     *
     * @param button Reference to JavaFX Button
     * @param solid Selected ikonli fontawesome enum type icon
     * @param size Value of icon size
     * @param color Chosen color for the icon
     * @param display Chosen enum value for ContentDisplay
     */

    public static void addIcon(Button button, FontAwesomeSolid solid, int size, Color color, ContentDisplay display) {
        addIcon(button, solid, size, color, display, 10);
    }

    /**
     * IconUtil - Icon level 3
     *
     * @param button Reference to JavaFX Button
     * @param solid Selected ikonli fontawesome enum type icon
     * @param size Value of icon size
     * @param color Chosen color for the icon
     * @param display Chosen enum value for ContentDisplay
     * @param textGap Value of gap between text and icon
     */

    public static void addIcon(Button button, FontAwesomeSolid solid, int size, Color color, ContentDisplay display, int textGap) {
        final FontIcon icon = new FontIcon(solid);
        icon.setIconSize(size);
        icon.setIconColor(color);

        button.setGraphic(icon);
        button.setContentDisplay(display);
        button.setGraphicTextGap(textGap);
    }
}
