package net.polar.button.listeners;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.polar.PolarBot;
import net.polar.button.ButtonListener;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Objects;

public final class VerifyButtonListener {
    private VerifyButtonListener() {}

    public static class Correct implements ButtonListener {
        @NotNull
        @Override
        public String getButtonId() {
            return "verify";
        }

        @Override
        public void onClick(@NotNull ButtonInteractionEvent event) {
            User user = event.getUser();
            PolarBot.getGuild().addRoleToMember(user, Objects.requireNonNull(PolarBot.getGuild().getRoleById(IDConstants.MEMBER_ROLE))).queue();
            event.replyEmbeds(
                    DiscordEmbed.newThemedBuilder()
                            .setTitle("Verification Successful")
                            .build()
            ).setEphemeral(true).queue();
            DiscordEmbed.log(user.getAsMention() + "( " + user.getId() + ") has been verified!");
        }
    }

    public static class Wrong implements ButtonListener {
        @NotNull
        @Override
        public String getButtonId() {
            return "wrong";
        }

        @Override
        public void onClick(@NotNull ButtonInteractionEvent event) {
            User user = event.getUser();
            event.replyEmbeds(
                    DiscordEmbed.newThemedBuilder()
                            .setTitle("Verification Failed")
                            .setDescription("You have been kicked from the server.")
                            .build()
            ).setEphemeral(true).queue((message) -> {
                PolarBot.getGuild().kick(user).delay(Duration.ofSeconds(1)).queue();
            });

        }
    }
}
