package net.polar.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.polar.PolarBot;
import net.polar.command.DiscordCommand;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class AnnounceCommand extends DiscordCommand {

    public AnnounceCommand() {
        super(
                "announce",
                "Announce something to the server",
                IDConstants.ADMIN_ROLE,
                List.of(
                        new OptionData(OptionType.STRING, "title", "The title of the announcement", true),
                        new OptionData(OptionType.STRING, "description", "The description of the announcement", true),
                        new OptionData(OptionType.BOOLEAN, "themed", "Whether or not the announcement should be themed", false),
                        new OptionData(OptionType.BOOLEAN, "mention", "Whether or not the announcement should mention @everyone", false)
                )
        );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        boolean themed = event.getOption("themed") == null || event.getOption("themed").getAsBoolean();
        String title = event.getOption("title").getAsString();
        String description = event.getOption("description").getAsString();
        boolean mention = event.getOption("mention") != null && event.getOption("mention").getAsBoolean();
        EmbedBuilder builder = themed ? DiscordEmbed.newThemedBuilder() : DiscordEmbed.newBuilder();
        builder.setTitle(title);
        builder.setDescription(description);
        PolarBot.getGuild().getTextChannelById(IDConstants.ANNOUNCEMENTS_CHANNEL).sendMessageEmbeds(builder.build()).queue((message) -> {
            if (mention) {
                message.reply("@everyone").queue();
            }
        });
        event.reply("Announcement sent!").setEphemeral(true).queue();
    }
}
