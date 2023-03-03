package net.polar.command.impl;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.polar.PolarBot;
import net.polar.command.DiscordCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class LevelCommand extends DiscordCommand {

    public LevelCommand() {
        super(
                "level",
                "Shows your level and experience",
                List.of(new OptionData(OptionType.USER, "user", "The user to show the level of").setRequired(false))
        );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        User user = event.getOption("user") == null ? event.getUser() : event.getOption("user").getAsUser();
        long id = user.getIdLong();
        event.replyEmbeds(PolarBot.getDatabase().getProfileEmbed(id)).queue();
    }
}
