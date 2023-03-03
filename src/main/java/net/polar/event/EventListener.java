package net.polar.event;

import net.dv8tion.jda.api.events.GenericEvent;

public interface EventListener<T extends GenericEvent> {

    Class<T> getEventClass();

    void onEvent(T event);


}
