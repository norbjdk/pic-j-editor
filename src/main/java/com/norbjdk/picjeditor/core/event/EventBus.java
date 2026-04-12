package com.norbjdk.picjeditor.core.event;

import com.norbjdk.picjeditor.core.event.model.ApplicationEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {
    private static final EventBus instance = new EventBus();
    private Map<Class<?>, List<Consumer>> listeners = new HashMap();

    public static EventBus getInstance() {
        return instance;
    }

    private EventBus() {}

    public <T extends ApplicationEvent> void subscribe(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public <T extends ApplicationEvent> void publish(T event) {
        Class<?> eventClass = event.getClass();
        List<Consumer> eventListeners = listeners.get(eventClass);
        if (eventListeners != null) {
            eventListeners.forEach(listener -> listener.accept(event));
        }
    }
}
