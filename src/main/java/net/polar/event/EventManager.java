package net.polar.event;

import net.dv8tion.jda.api.events.GenericEvent;

import java.util.List;
import java.util.Map;import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {

    private static final Map<Class<? extends GenericEvent>, List<EventListener<? extends GenericEvent>>> eventListeners = new ConcurrentHashMap<>();
    private final JDADiscordListener jdaListener = new JDADiscordListener(this);


    public <T extends GenericEvent> void addListener(EventListener<T> listener) {
        eventListeners.computeIfAbsent(
                listener.getEventClass(),
                k -> new CopyOnWriteArrayList<>()
        ).add(listener);
    }

    public void addListeners(EventListener<?>... listeners) {
        for (EventListener<?> listener : listeners) {
            eventListeners.computeIfAbsent(
                    listener.getEventClass(),
                    k -> new CopyOnWriteArrayList<>()
            ).add(listener);
        }
    }

    public <T extends GenericEvent> void removeListener(EventListener<T> listener) {
        eventListeners.computeIfAbsent(
                listener.getEventClass(),
                k -> new CopyOnWriteArrayList<>()
        ).remove(listener);
    }

    public <T extends GenericEvent> void callEvent(T event) {
        List<EventListener<?>> listeners = getListeners(event.getClass());
        if (listeners == null || listeners.isEmpty()) return;
        listeners.forEach(listener -> ((net.polar.event.EventListener<GenericEvent>) listener).onEvent(event));
    }


    List<EventListener<?>> getListeners(Class<? extends GenericEvent> eventClass) {
        return eventListeners.get(eventClass);
    }

    public Object getJdaListener() {
        return jdaListener;
    }

}
