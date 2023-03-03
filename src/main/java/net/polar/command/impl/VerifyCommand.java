package net.polar.command.impl;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.polar.command.DiscordCommand;
import org.jetbrains.annotations.NotNull;

public class VerifyCommand extends DiscordCommand {

    public VerifyCommand() {
        super("verify", "Verify your account");
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {

    }
}
