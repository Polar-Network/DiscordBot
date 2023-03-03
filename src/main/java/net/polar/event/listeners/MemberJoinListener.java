package net.polar.event.listeners;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.polar.event.EventListener;
import net.polar.utils.ChatUtils;
import net.polar.utils.DiscordEmbed;

public final class MemberJoinListener implements EventListener<GuildMemberJoinEvent> {

    @Override
    public Class<GuildMemberJoinEvent> getEventClass() {
        return GuildMemberJoinEvent.class;
    }

    @Override
    public void onEvent(GuildMemberJoinEvent event) {
        DiscordEmbed.log("**Member Join**", "",  event.getMember().getAsMention() + " has joined the server!");

        ChatUtils.attemptDM(
                event.getUser().getIdLong(),
                DiscordEmbed.newThemedBuilder()
                        .setTitle("Welcome to the PolarMC Network!")
                        .setDescription("We are a Minecraft network with a variety of gamemodes. We hope you enjoy your stay!")
                        .build()
        );

    }
}
