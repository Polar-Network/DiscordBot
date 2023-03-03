package net.polar.command;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.polar.PolarBot;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class DiscordCommand {

    private final String name;
    private final String description;
    private final long roleRequired;
    private final List<OptionData> options;

    public DiscordCommand(
            @NotNull String name,
            @NotNull String description,
            long roleRequired,
            @NotNull List<OptionData> options
    ) {
        this.name = name;
        this.description = description;
        this.roleRequired = roleRequired;
        this.options = options;
    }

    public DiscordCommand(
            @NotNull String name,
            @NotNull String description,
            long roleRequired
    ) {
        this(name, description, roleRequired, List.of());
    }

    public DiscordCommand(
            @NotNull String name,
            @NotNull String description
    ) {
        this(name, description, IDConstants.MEMBER_ROLE);
    }

    public DiscordCommand(
            @NotNull String name,
            @NotNull String description,
            @NotNull List<OptionData> options
    ) {
        this(name, description, IDConstants.MEMBER_ROLE, options);
    }

    public void execute(@NotNull SlashCommandInteractionEvent event) {
        event.replyEmbeds(DiscordEmbed.newErrorBuilder().setDescription("This command has not been implemented yet").build()).setEphemeral(true).queue();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Role getRoleRequired() {
        return PolarBot.getClient().getRoleById(roleRequired);
    }

    public List<OptionData> getOptions() {
        return options;
    }
}

