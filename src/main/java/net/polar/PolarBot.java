package net.polar;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.polar.button.ButtonManager;
import net.polar.command.CommandManager;
import net.polar.database.Database;
import net.polar.event.EventManager;
import net.polar.event.listeners.*;
import net.polar.runtime.RunConfigurations;
import net.polar.scheduler.Scheduler;
import net.polar.utils.IDConstants;
import net.polar.utils.ProxyInfoChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PolarBot {

    private static JDA client;
    private static final Logger LOGGER = LoggerFactory.getLogger(PolarBot.class);
    private static final CommandManager commandManager = new CommandManager();
    private static final EventManager eventManager = new EventManager();

    private static final ButtonManager buttonManager = new ButtonManager();
    private static final Scheduler scheduler = new Scheduler();
    private static Database database;


    private PolarBot(RunConfigurations configurations) throws Exception {
        database = new Database(configurations.mongoUri());
        client = JDABuilder.create(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .setToken(configurations.botToken())
                .addEventListeners(eventManager.getJdaListener())
                .setActivity(Activity.of(Activity.ActivityType.PLAYING, "with the polar bears"))
                .build();
        client.awaitReady();
        Runtime.getRuntime().addShutdownHook(new Thread(this::onDisable));
        commandManager.init(client);
        eventManager.addListeners(
                new ReadyListener(),
                new MessageEditListener(),
                new MessageCreateListener(),
                new MemberJoinListener(),
                new MemberLeaveListener()
        );
        ProxyInfoChecker.init();
    }


    private void onDisable() {

    }

    public static void main(String[] args) {
        try {
            new PolarBot(RunConfigurations.build());
            eventManager.callEvent(new ReadyEvent(client));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JDA getClient() {
        return client;
    }
    public static Guild getGuild() {
        return client.getGuildById(IDConstants.GUILD_ID);
    }
    public static Database getDatabase() {
        return database;
    }
    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static Scheduler getScheduler() {
        return scheduler;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static ButtonManager getButtonManager() {
        return buttonManager;
    }

    private static String getToken(String[] args) {
        String token;
        for (String arg : args) {
            if (arg.startsWith("--token=")) {
                token = arg.replaceFirst("--token=", "");
                return token;
            }
        }
        throw new IllegalArgumentException("No token provided, please provide a token with --token=TOKEN. Shutting down...");
    }
}
