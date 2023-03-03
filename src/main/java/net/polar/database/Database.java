package net.polar.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.polar.PolarBot;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;
import org.bson.Document;

import java.time.Duration;

public class Database {

    private final MongoClient client;
    private final MongoDatabase database;


    public Database(String mongoUri) {
        this.client = MongoClients.create(mongoUri);
        this.database = client.getDatabase("discord");
    }

    public int getExperience(long user) {
        Document document = getLevelCollection().find(new Document("discordId", user)).first();
        if (document == null) return 0;

        return document.getInteger("experience");
    }

    public int getLevel(long user) {
        Document document = getLevelCollection().find(new Document("discordId", user)).first();
        if (document == null) return 0;

        return document.getInteger("level");
    }

    public int addExperience(long user, int amount) {
        int experience = getExperience(user);
        int futureExperience = experience + amount;
        Document document = new Document("discordId", user)
                .append("level", getLevel(user))
                .append("experience", futureExperience);
        getLevelCollection().replaceOne(new Document("discordId", user), document, new ReplaceOptions().upsert(true));
        calculateLevel(user);
        return futureExperience;
    }

    public MessageEmbed getProfileEmbed(long user) {
        User member = PolarBot.getClient().getUserById(user);
        if (member == null) return null; // Impossible, but just in case

        return DiscordEmbed.newBuilder()
                .setTitle(member.getName() + "'s profile")
                .setThumbnail(member.getEffectiveAvatarUrl())
                .addField("Level", String.valueOf(getLevel(user)), true)
                .addField("Experience", String.valueOf(getExperience(user)), true)
                .addField("Experience to next level", String.valueOf(calculateExperienceNeeded(getLevel(user), getExperience(user))), true)
                .build();
    }

    private void calculateLevel(long user) {
        int experience = getExperience(user);
        int level = getLevel(user);
        if (level == 0) level = 1;

        int xpNeeded = getExperienceNeeded(level);
        if (experience <= xpNeeded) return;

        level++;
        experience -= xpNeeded;
        Document document = new Document("discordId", user)
                .append("level", level)
                .append("experience", experience);
        getLevelCollection().replaceOne(new Document("discordId", user), document, new ReplaceOptions().upsert(true));
        congratulateUser(user, level, experience);
    }

    private void congratulateUser(long user, int level, int experience) {
        User member = PolarBot.getClient().getUserById(user);
        if (member == null) return; // Impossible, but just in case

        TextChannel channel = PolarBot.getClient().getTextChannelById(IDConstants.LEVEL_UP_CHANNEL);
        if (channel == null) return; // Impossible, but just in case1

        EmbedBuilder builder = DiscordEmbed.newBuilder()
                .setTitle("Level Up!")
                .setThumbnail(member.getEffectiveAvatarUrl())
                .setDescription("Congratulations " + member.getAsMention() + " for reaching level " + level + "! " +
                        "Next level in " + calculateExperienceNeeded(
                        level + 1,
                        experience
                ) + " experience points!");

        IDConstants.LevelRoles role = IDConstants.LevelRoles.getByLevel(level);
        if (role != null) {
            PolarBot.getGuild().addRoleToMember(member, PolarBot.getGuild().getRoleById(role.getId())).queue();
            builder.addField("Special Level! You have been awarded the role " + PolarBot.getGuild().getRoleById(role.getId()).getAsMention(), "", false);
        }

        channel.sendMessageEmbeds(builder.build()).queue();
    }

    private MongoCollection<Document> getLevelCollection() {
        return database.getCollection("levels");
    }

    public int getExperienceNeeded(int level) {
        return (5 * (level * level)) + (50 * level) + 100;
    }

    public int calculateExperienceNeeded(int level, int experience) {
        return getExperienceNeeded(level) - experience == 0 ? 1 : getExperienceNeeded(level) - experience;
    }

    public MongoCollection<Document> getBansCollection() {
        return database.getCollection("bans");
    }

    public void appendBan(long userId, Duration banDuration) {
        Document document = new Document("discordId", userId)
                .append("banDuration", System.currentTimeMillis() + banDuration.toMillis());
        getBansCollection().replaceOne(new Document("discordId", userId), document, new ReplaceOptions().upsert(true));
    }

}
