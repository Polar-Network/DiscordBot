package net.polar.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.polar.PolarBot;
import net.polar.command.DiscordCommand;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;
import org.jetbrains.annotations.NotNull;

public class PushVerifyCommand extends DiscordCommand {

    public PushVerifyCommand() {
        super("pushverify", "Pushes the verify message to the channel", IDConstants.OWNER_ROLE);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        TextChannel channel = PolarBot.getGuild().getTextChannelById(IDConstants.VERIFICATION_ID);
        assert channel != null;

        EmbedBuilder builder = DiscordEmbed.newThemedBuilder();
        builder.setTitle("Welcome to the PolarMC Network!")
                .setDescription("Before we let you in, we need to verify that you are a human. Please click the Checkmark below.");

        Button checkmark = Button.primary("verify", Emoji.fromUnicode("✅"));
        Button wrong = Button.primary("wrong", Emoji.fromUnicode("❌"));

        channel.sendMessageEmbeds(builder.build()).setActionRow(checkmark, wrong).queue();
        event.reply("Pushed verify message!").setEphemeral(true).queue();
    }
}
