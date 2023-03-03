package net.polar.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.polar.PolarBot;
import net.polar.command.DiscordCommand;
import net.polar.utils.DiscordEmbed;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends DiscordCommand {

    public HelpCommand() {
        super("help", "Shows this help message");
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember() == null ? event.getGuild().getSelfMember() : event.getMember(); // If the member is null, it's a webhook
        var commands = PolarBot.getCommandManager().getCommands().stream().filter(command -> member.getRoles().contains(command.getRoleRequired()));
        EmbedBuilder builder = DiscordEmbed.newBuilder();
        builder.setTitle("Help");
        builder.setDescription("Here are all the commands you can use");
        commands.forEach(command -> {
            StringBuilder descriptionBuilder = new StringBuilder();
            descriptionBuilder.append(command.getDescription());
            if (!command.getOptions().isEmpty()) {
                descriptionBuilder.append(" (Arguments: ");

                command.getOptions().forEach(option -> {
                    descriptionBuilder.append(option.getName());
                    descriptionBuilder.append(" | ");
                });
                descriptionBuilder.replace(descriptionBuilder.length() - 3, descriptionBuilder.length(), ""); // remove the last " | "
                descriptionBuilder.append(")");
            }
            String description =  descriptionBuilder.toString();
            builder.addField(
                    command.getName(),
                    description,
                    false
            );
        });
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }
}
