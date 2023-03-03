package net.polar.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.polar.PolarBot;
import net.polar.scheduler.ExecutionType;
import net.polar.scheduler.TaskSchedule;

public final class ChatUtils {
    private ChatUtils() {}


    public static void ghostPing(TextChannel channel, User user) {
        channel.sendMessage(user.getAsMention()).queue();
        long id = channel.getLatestMessageIdLong();
        PolarBot.getScheduler().scheduleTask(() -> channel.deleteMessageById(id).queue(), TaskSchedule.millis(100), TaskSchedule.stop(), ExecutionType.ASYNC);
    }

    public static void attemptDM(long userId, MessageEmbed embed) {
        ErrorHandler errorHandler = new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER, (response) -> {
            DiscordEmbed.log("**DM Failed**", "", "<@" + userId + "> has DMs disabled.");
        });
        Guild guild = PolarBot.getGuild();
        User user = guild.getMemberById(userId).getUser();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessageEmbeds(embed).queue(null, errorHandler);
        });
    }



}
