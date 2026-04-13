package com.norbjdk.picjeditor.ui.manager;

import com.norbjdk.picjeditor.core.event.EventBus;
import com.norbjdk.picjeditor.core.event.dto.ChangeViewRequestedEvent;
import com.norbjdk.picjeditor.core.event.dto.ViewChangedEvent;
import com.norbjdk.picjeditor.ui.model.ViewName;
import com.norbjdk.picjeditor.ui.model.Viewable;
import com.norbjdk.picjeditor.ui.view.EditorView;
import com.norbjdk.picjeditor.ui.view.HomeView;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ViewManager {
    private static final ViewManager instance = new ViewManager();

    public static ViewManager getInstance() {
        return instance;
    }

    private final Map<ViewName, Viewable> views;
    private Viewable currentView;

    private ViewManager() {
        views = new ConcurrentHashMap<>();

        initViews();
        setupEventListener();
    }

    private void initViews() {
        addView(ViewName.HOME, new HomeView());
        addView(ViewName.EDITOR, new EditorView());
    }

    private void setupEventListener() {
        EventBus.getInstance().subscribe(ChangeViewRequestedEvent.class, changeViewRequestedEvent -> {
            if (changeViewRequestedEvent.getViewName() != null) {
                changeView(changeViewRequestedEvent.getViewName());
            }
        });
    }

    private void addView(ViewName name, Viewable view) {
        if (view != null && !views.containsKey(name)) {
            views.put(name, view);
        }
    }

    private void changeView(ViewName viewName) {
        final Viewable newView = views.get(viewName);

        if (newView != null) {
            currentView = newView;
            EventBus.getInstance().publish(new ViewChangedEvent(currentView));
        }
    }

    public Map<ViewName, Viewable> getViews() {
        return views;
    }

    public Viewable getCurrentView() {
        return currentView;
    }
}
