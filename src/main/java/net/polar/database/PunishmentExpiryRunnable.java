package net.polar.database;

import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.polar.PolarBot;
import net.polar.utils.DiscordEmbed;
import org.bson.Document;

final class PunishmentExpiryRunnable implements Runnable{

    private final Database database;
    PunishmentExpiryRunnable(Database database) {
        this.database = database;
    }

    @Override
    public void run() {
        MongoCollection<Document> bans = database.getBansCollection();
        bans.find().forEach((Document document) -> {
            if (document.getLong("expiry") < System.currentTimeMillis()) {
                long id = document.getLong("discordId");
                PolarBot.getGuild().unban(UserSnowflake.fromId(id));
                DiscordEmbed.log("Unbanned " + id);
                bans.deleteOne(document);
            }
        });
    }
}
