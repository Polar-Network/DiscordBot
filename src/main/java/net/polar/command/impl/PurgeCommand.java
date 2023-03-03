package net.polar.command.impl;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.polar.PolarBot;
import net.polar.command.DiscordCommand;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;

public class PurgeCommand extends DiscordCommand {

    public PurgeCommand() {
        super(
                "purge",
                "Deletes specified messages from the channel",
                IDConstants.STAFF_ROLE,
                List.of(
                        new OptionData(OptionType.INTEGER, "amount", "The amount of messages to delete", true),
                        new OptionData(OptionType.USER, "user", "The user to delete messages from", false),
                        new OptionData(OptionType.STRING, "contains", "The string to search for in messages", false)
                )
        );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        if (event.getChannel().asTextChannel().getIdLong() == IDConstants.LOGGER_ID) {
            event.replyEmbeds(DiscordEmbed.newErrorBuilder().setDescription("Why would you want to purge logs?").build()).queue();
            DiscordEmbed.log("The user " + event.getUser().getAsTag() + " tried to purge the logs channel", event.getGuild());
            return;
        }

        int amount = event.getOption("amount").getAsInt();
        if (amount < 0 || amount > 100) {
            event.replyEmbeds(DiscordEmbed.newErrorBuilder().setDescription("Amount must be between 1 and 100").build()).queue();
            return;
        }
        Member member = event.getOption("user") != null ? event.getOption("user").getAsMember() : null;
        String contains = event.getOption("contains") != null ? event.getOption("contains").getAsString() : null;

        if (member != null && contains != null) {
            MessageHistory history = event.getChannel().getHistory();
            List<Message> messages = history.retrievePast(amount).complete();
            List<Message> toDelete = messages.stream()
                    .filter(message -> message.getAuthor().equals(member.getUser()))
                    .filter(message -> message.getContentRaw().contains(contains))
                    .toList();
            event.getChannel().purgeMessages(toDelete);
            event.replyEmbeds(DiscordEmbed.newBuilder().setDescription(
                    "Deleted " + toDelete.size() + " messages that contained"
                            + " `" + contains + "` from " + member.getAsMention()
            ).build()).queue();
            PolarBot.getScheduler().buildTask(() -> {
                event.getHook().deleteOriginal().queue();
            }).delay(Duration.ofSeconds(3)).schedule();
            return;
        }
        if (member != null && contains == null) {
            MessageHistory history = event.getChannel().getHistory();
            List<Message> messages = history.retrievePast(amount).complete();
            List<Message> toDelete = messages.stream()
                    .filter(message -> message.getAuthor().equals(member.getUser()))
                    .toList();
            event.getChannel().purgeMessages(toDelete);
            event.replyEmbeds(DiscordEmbed.newBuilder().setDescription(
                    "Deleted " + toDelete.size() + " messages"
                            + " from " + member.getAsMention()
            ).build()).queue();
            PolarBot.getScheduler().buildTask(() -> {
                event.getHook().deleteOriginal().queue();
            }).delay(Duration.ofSeconds(3)).schedule();
            return;
        }
        if (member == null && contains != null) {
            MessageHistory history = event.getChannel().getHistory();
            List<Message> messages = history.retrievePast(amount).complete();
            List<Message> toDelete = messages.stream()
                    .filter(message -> message.getContentRaw().contains(contains))
                    .toList();
            event.getChannel().purgeMessages(toDelete);
            event.replyEmbeds(DiscordEmbed.newBuilder().setDescription(
                    "Deleted " + toDelete.size() + " messages that contained"
                            + " `" + contains + "`"
            ).build()).queue();
            PolarBot.getScheduler().buildTask(() -> {
                event.getHook().deleteOriginal().queue();
            }).delay(Duration.ofSeconds(3)).schedule();
            return;
        }
        event.getChannel().getHistory().retrievePast(amount).queue(messages -> {
            event.getChannel().purgeMessages(messages);
            event.replyEmbeds(DiscordEmbed.newBuilder().setDescription(
                    "Deleted " + messages.size() + " messages"
            ).build()).queue();
            PolarBot.getScheduler().buildTask(() -> {
                event.getHook().deleteOriginal().queue();
            }).delay(Duration.ofSeconds(3)).schedule();
        });
    }
}
