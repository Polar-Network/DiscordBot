package net.polar.command.impl;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.polar.command.DiscordCommand;
import net.polar.utils.DiscordEmbed;
import org.jetbrains.annotations.NotNull;

public class IPCommand extends DiscordCommand {

    public IPCommand() {
        super("ip", "Shows the server IP");
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        event.replyEmbeds(
                DiscordEmbed.newBuilder()
                        .setTitle("You can connect to the server from: thepolarmc.net")
                        .build()
        ).queue();
    }

}
