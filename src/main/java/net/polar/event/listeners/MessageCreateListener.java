package net.polar.event.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.polar.PolarBot;
import net.polar.event.EventListener;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MessageCreateListener implements EventListener<MessageReceivedEvent> {

    private final Map<Long, Long> xpCooldowns = new HashMap<>();



    @Override
    public Class<MessageReceivedEvent> getEventClass() {
        return MessageReceivedEvent.class;
    }

    @Override
    public void onEvent(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Member member = event.getMember();
        if (member == null) return; // This should never happen, but just in case

        long id = member.getIdLong();
        if (xpCooldowns.containsKey(id)) {
            long lastMessage = xpCooldowns.get(id);
            if (System.currentTimeMillis() - lastMessage < Duration.ofSeconds(5).toMillis()) {
                return;
            }
        }
        PolarBot.getDatabase().addExperience(id, randomBetween(5, 25));
        xpCooldowns.put(id, System.currentTimeMillis());
    }

    private int randomBetween(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

}
