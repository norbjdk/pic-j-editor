package com.norbjdk.picjeditor.ui.util;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

public final class ButtonFactory {
    private ButtonFactory() {}

    /**
     * ButtonFactory - Button level 1
     *
     * @param text Text displayed on the button
     * @param id ID of the button
     * @return JavaFX Button adjusted with given arguments
     */

    public static Button createButton(String text, String id) {
        Button button = new Button(text);
        button.setId(id);
        return button;
    }

    /**
     * ButtonFactory - Button level 2
     *
     * @param text Text displayed on the button
     * @param id ID of the button
     * @param tooltip Tooltip displayed on the button
     * @return JavaFX Button adjusted with given arguments
     */

    public static Button createButton(String text, String id, String tooltip) {
        Button button = createButton(text, id);
        button.setTooltip(new Tooltip(tooltip));
        return button;
    }

    /**
     * ButtonFactory - Button level 3
     *
     * @param text Text displayed on the button
     * @param id ID of the button
     * @param tooltip Tooltip displayed on the button
     * @param style Styling pinned to the button
     * @return JavaFX Button adjusted with given arguments
     */

    public static Button createButton(String text, String id, String tooltip, String style) {
        final Button button = createButton(text, id, tooltip);
        button.getStyleClass().add(style);
        return button;
    }

    /**
     * ButtonFactory - Button level 4
     *
     * @param text Text displayed on the button
     * @param id ID of the button
     * @param tooltip Tooltip displayed on the button
     * @param style Styling pinned to the button
     * @param font JavaFX font type object mounted to the button
     * @return JavaFX Button adjusted with given arguments
     */

    public static Button createButton(String text, String id, String tooltip, String style, Font font) {
        final Button button = createButton(text, id, tooltip, style);
        button.setFont(font);
        return button;
    }

}
