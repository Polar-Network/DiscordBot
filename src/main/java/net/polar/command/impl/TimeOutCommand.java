package net.polar.command.impl;

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

public final class TimeOutCommand extends DiscordCommand {

    public TimeOutCommand() {
        super(
                "timeout",
                "Time out a member",
                IDConstants.STAFF_ROLE,
                List.of(
                        new OptionData(OptionType.USER, "member", "The member to time out", true),
                        new OptionData(OptionType.STRING, "duration", "The duration of the time out (10s, 10m, 10h, 10d)", true),
                        new OptionData(OptionType.STRING, "reason", "The reason for the time out", true)
                )
        );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        String durationString = event.getOption("duration").getAsString();
        Duration duration = StringUtils.fromString(durationString);
        if (duration.toDays() > 28) {
            event.reply("The duration can't be longer than 28 days").setEphemeral(true).queue();
            return;
        }
        Member member = event.getOption("member").getAsMember();

        String reason = event.getOption("reason").getAsString();

        assert member != null;
        PolarBot.getGuild().timeoutFor(member, duration);
        ChatUtils.attemptDM(
                member.getIdLong(),
                DiscordEmbed.newThemedBuilder()
                        .setTitle("You have been timed out")
                        .setDescription("You have been timed out for " + durationString + " for the following reason: " + reason)
                        .build()
        );
        event.replyEmbeds(
                DiscordEmbed.newBuilder()
                        .setTitle("Member timed out")
                        .setDescription(member.getAsMention() + " has been timed out for " + durationString + " for the following reason: " + reason)
                        .build()
        ).queue();
        DiscordEmbed.log(
                "Member timed out",
                member.getAsMention() + " has been timed out for " + durationString + " for the following reason: " + reason,
                "Timed out by" + event.getUser().getAsTag());
    }
}
