package net.polar.command.impl;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.polar.PolarBot;
import net.polar.command.DiscordCommand;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;
import org.jetbrains.annotations.NotNull;

public class PushWelcomeCommand extends DiscordCommand {

    public PushWelcomeCommand() {
        super("pushwelcome", "Pushes the welcome message to the channel", IDConstants.OWNER_ROLE);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        TextChannel general = PolarBot.getGuild().getTextChannelById(IDConstants.GENERAL_ID);
        TextChannel reactionRoles = PolarBot.getGuild().getTextChannelById(IDConstants.REACTION_ROLES_ID);
        TextChannel rules = PolarBot.getGuild().getTextChannelById(IDConstants.RULES_ID);
        PolarBot.getGuild().getTextChannelById(IDConstants.WELCOME_ID)
                .sendMessageEmbeds(
                        DiscordEmbed.newThemedBuilder()
                                .setTitle("Welcome to **PolarMC** <3")
                                .setDescription(
                                        "We hope you enjoy your stay! Some channels you should check out! \n" +
                                        "• " + rules.getAsMention() +" - Read the rules! \n" +
                                        "• " + reactionRoles.getAsMention() +" - Get some roles! \n" +
                                        "• " + general.getAsMention() +" - Talk about anything! \n"
                                )
                                .build()
                ).queue();
        event.reply("Pushed welcome message!").setEphemeral(true).queue();
    }
}
