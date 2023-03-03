package net.polar.command.impl;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.polar.PolarBot;
import net.polar.command.DiscordCommand;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class PushReactionRolesCommand extends DiscordCommand {

    public PushReactionRolesCommand() {
        super("pushreactionroles", "Pushes the reaction roles message to the channel", IDConstants.OWNER_ROLE);
    }


    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {

        List<Button> colors = Arrays.stream(IDConstants.ColorRoles.VALUES).map(color -> Button.primary(color.name(), color.name())).toList();
        List<Button> reactionRoles = Arrays.stream(IDConstants.ReactionRoles.VALUES).map(role -> Button.primary(role.name(), role.name())).toList();

        TextChannel channel = PolarBot.getGuild().getTextChannelById(IDConstants.REACTION_ROLES_ID);
        if (channel == null) return;

        channel.sendMessageEmbeds(
                DiscordEmbed.newBuilder()
                        .setTitle("Reaction Roles")
                        .setDescription("Choose the specific gender/pronouns you identify with by clicking the corresponding button below.")
                        .build()
        ).setActionRow(reactionRoles).queue();


        channel.sendMessageEmbeds(
                DiscordEmbed.newBuilder()
                        .setTitle("Reaction Roles")
                        .setDescription("Choose the color you want to be by clicking the corresponding button below.")
                        .build()
        ).addActionRow(colors.subList(0, 4)).addActionRow(colors.subList(4, colors.size() - 1)).queue();
    }
}
