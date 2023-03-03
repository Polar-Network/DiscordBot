package net.polar.event.listeners;

import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.polar.event.EventListener;
import net.polar.utils.DiscordEmbed;

public class MessageEditListener implements EventListener<MessageUpdateEvent> {
    @Override
    public Class<MessageUpdateEvent> getEventClass() {
        return MessageUpdateEvent.class;
    }

    @Override
    public void onEvent(MessageUpdateEvent event) {
        if (event.getAuthor().isBot()) return;

        DiscordEmbed.log(
                DiscordEmbed.colored()
                        .setTitle("Message Edited")
                        .addField("Channel", event.getChannel().getAsMention(), true)
                        .addField("Author", event.getAuthor().getAsTag(), true)
                        .addField("Message", event.getMessage().getContentRaw(), false)
        );
    }
}
