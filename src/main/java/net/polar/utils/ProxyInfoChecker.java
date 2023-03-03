package net.polar.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.polar.PolarBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;

public final class ProxyInfoChecker {

    private ProxyInfoChecker() {}

    public static void init() {
        PolarBot.getScheduler().buildTask(checkProxyInfo).repeat(Duration.ofMinutes(5)).schedule();
    }

    private static final Runnable checkProxyInfo = () -> {
        EmbedBuilder embed = DiscordEmbed.newThemedBuilder().setTitle("The PolarMC Network");
        String proxy = "thepolarmc.net:25565";
        String pingConnection = "https://api.mcsrvstat.us/2/" + proxy;
        try {
            URL url = new URL(pingConnection);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            JsonObject players = json.get("players").getAsJsonObject();
            int online = players.get("online").getAsInt();
            int max = players.get("max").getAsInt();

            embed.addField("Server IP", "thepolarmc.net", false);
            embed.addField("Online Players", online + "/" + max, false);
            embed.addField("Version", "1.12.2-1.19.3", false);
            embed.addField("Website", "https://thepolarmc.net", false);
            embed.addField("Internal Latency", "0ms", false);

            sendToChannel(embed.build());
        }
        catch (IOException e) {
            sendToChannel(embed.setDescription("Error while checking proxy info! Server is down!").build());
        }
    };

    private static void sendToChannel(MessageEmbed embed) {
        TextChannel channel = PolarBot.getClient().getTextChannelById(IDConstants.INFO_CHANNEL);
        if (channel == null) throw new NullPointerException("Channel is null");

        channel.getIterableHistory().takeAsync(1000).thenAccept(messages -> {
            channel.purgeMessages(messages);
            channel.sendMessageEmbeds(embed).queue();
        });
    }
}
