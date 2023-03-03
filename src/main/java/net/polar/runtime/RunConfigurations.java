package net.polar.runtime;

public record RunConfigurations(String botToken, String mongoUri) {



    public static RunConfigurations build() {
        return new RunConfigurations(
                System.getenv("BOT_TOKEN"),
                System.getenv("MONGO_URI")
        );
    }

}
