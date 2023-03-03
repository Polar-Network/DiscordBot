package net.polar.event.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.polar.PolarBot;
import net.polar.event.EventListener;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;

public final class ReadyListener implements EventListener<ReadyEvent> {

    @Override
    public Class<ReadyEvent> getEventClass() {
        return ReadyEvent.class;
    }

    @Override
    public void onEvent(ReadyEvent event) {
        DiscordEmbed.log("Bot startup complete!");
    }
}
