package com.norbjdk.picjeditor.core.event.dto;

import com.norbjdk.picjeditor.core.event.model.ApplicationEvent;
import com.norbjdk.picjeditor.ui.model.ViewName;

public final class ChangeViewRequestedEvent implements ApplicationEvent {
    private final ViewName viewName;

    public ChangeViewRequestedEvent(ViewName viewName) {
        this.viewName = viewName;
    }

    public ViewName getViewName() {
        return viewName;
    }
}
