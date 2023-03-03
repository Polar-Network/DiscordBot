package net.polar.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.polar.PolarBot;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;

public final class DiscordEmbed {

    private DiscordEmbed() {}

    public static @NotNull EmbedBuilder newBuilder() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(new Color(ThreadLocalRandom.current().nextInt()));
        builder.setFooter("For support, please create a ticket");
        builder.setTimestamp(ZonedDateTime.now());
        return builder;
    }

    public static @NotNull EmbedBuilder colored() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(new Color(ThreadLocalRandom.current().nextInt()));
        return builder;
    }

    public static @NotNull EmbedBuilder newErrorBuilder() {
        return newBuilder().setColor(Color.RED).setTitle("ERROR :x:");
    }

    public static @NotNull EmbedBuilder colored(Color color) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(color);
        return builder;
    }

    public static @NotNull EmbedBuilder newThemedBuilder() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setThumbnail("https://cdn.discordapp.com/attachments/1076957692419113014/1079840098419490826/Logo.png");
        builder.setColor(Color.decode("#00ffe1"));
        builder.setFooter("For support, please create a ticket");
        builder.setTimestamp(ZonedDateTime.now());
        return builder;
    }

    public static void log(@NotNull EmbedBuilder b) {
        TextChannel logChannel = PolarBot.getClient().getTextChannelById(IDConstants.LOGGER_ID);
        if (logChannel != null) {
            logChannel.sendMessageEmbeds(b.build()).queue();
        }
    }

    public static void log(@NotNull Object... messages) {
        TextChannel logChannel = PolarBot.getClient().getTextChannelById(IDConstants.LOGGER_ID);
        if (logChannel != null) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(new Color(ThreadLocalRandom.current().nextInt()));
            builder.setTitle("PolarBot Log");
            for (Object message : messages) {
                builder.appendDescription(message.toString() + " \n");
            }
            logChannel.sendMessageEmbeds(builder.build()).queue();
        }
    }

}
