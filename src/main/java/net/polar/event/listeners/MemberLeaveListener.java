package net.polar.event.listeners;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.polar.event.EventListener;
import net.polar.utils.DiscordEmbed;

public final class MemberLeaveListener implements EventListener<GuildMemberRemoveEvent> {
    @Override
    public Class<GuildMemberRemoveEvent> getEventClass() {
        return GuildMemberRemoveEvent.class;
    }

    @Override
    public void onEvent(GuildMemberRemoveEvent event) {
        DiscordEmbed.log("**Member Leave**", "",  event.getMember().getAsMention() + " has left the server!");
    }
}
