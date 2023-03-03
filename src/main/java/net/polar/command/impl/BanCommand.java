package net.polar.command.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.polar.PolarBot;
import net.polar.command.DiscordCommand;
import net.polar.utils.ChatUtils;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;
import net.polar.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class BanCommand extends DiscordCommand {

    public BanCommand() {
        super(
                "ban",
                "Ban a user from the server",
                IDConstants.STAFF_ROLE,
                List.of(
                        new OptionData(OptionType.USER, "member", "The member to ban", true),
                        new OptionData(OptionType.STRING, "reason", "The reason for the ban", true),
                        new OptionData(OptionType.STRING, "duration", "The duration of the ban (10s, 10m, 10h, 10d)", true),
                        new OptionData(OptionType.INTEGER, "delete_days", "The number of days of messages to delete", true)
                )
        );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getOption("member").getAsMember();
        String reason = event.getOption("reason").getAsString();
        Duration duration = StringUtils.fromString(event.getOption("duration").getAsString());
        Guild guild = event.getGuild();
        int deleteDays = event.getOption("delete_days").getAsInt();
        assert member != null;
        ChatUtils.attemptDM(
                member.getIdLong(),
                DiscordEmbed.newThemedBuilder()
                        .setTitle("You have been banned")
                        .setDescription("You have been banned for " + event.getOption("duration").getAsString() + " days for the following reason: " + reason)
                        .build());
        guild.ban(member, deleteDays, TimeUnit.DAYS).reason(reason).queue();
        event.replyEmbeds(
                DiscordEmbed.newBuilder()
                        .setTitle("Member banned")
                        .setDescription(member.getAsMention() + " has been banned for " + duration.toDays() + " days for the following reason: " + reason)
                        .build()
        ).queue();
        PolarBot.getDatabase().appendBan(member.getIdLong(), duration);
        DiscordEmbed.log(
                DiscordEmbed.newBuilder()
                        .setTitle("Member banned")
                        .setDescription(member.getAsMention() + " has been banned for " + duration.toDays() + " days for the following reason: " + reason)
                        .build()
        );
    }
}
