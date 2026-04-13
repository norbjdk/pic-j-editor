package com.norbjdk.picjeditor.core.event.dto;

import com.norbjdk.picjeditor.core.event.model.ApplicationEvent;
import com.norbjdk.picjeditor.ui.model.Viewable;

public final class ViewChangedEvent implements ApplicationEvent {
    private final Viewable view;

    public ViewChangedEvent(Viewable view) {
        this.view = view;
    }

    public Viewable getView() {
        return view;
    }
}
