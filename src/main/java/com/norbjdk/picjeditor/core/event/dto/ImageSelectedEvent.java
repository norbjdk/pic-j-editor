package com.norbjdk.picjeditor.core.event.dto;

import com.norbjdk.picjeditor.core.event.model.ApplicationEvent;
import com.norbjdk.picjeditor.core.picture.ImageData;

public class ImageSelectedEvent implements ApplicationEvent {

    private final ImageData imageData;

    public ImageSelectedEvent(ImageData imageData) {
        this.imageData = imageData;
    }

    public ImageData getImageData() {
        return imageData;
    }
}
