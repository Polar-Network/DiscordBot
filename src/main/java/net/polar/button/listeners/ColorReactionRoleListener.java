package net.polar.button.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.polar.PolarBot;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;
import net.polar.utils.StringUtils;

import java.util.Arrays;

public final class ColorReactionRoleListener {

    private ColorReactionRoleListener() {}

    public static void handle(ButtonInteractionEvent event) {
        String id = event.getButton().getId();

        IDConstants.ReactionRoles role = IDConstants.ReactionRoles.getByButton(id);
        if (role != null) {
            handleReactionRole(event, role);
            return;
        }

        IDConstants.ColorRoles color = IDConstants.ColorRoles.getByButton(id);
        if (color != null) {
            handleColorRole(event, color);
        }
    }


    private static void handleReactionRole(ButtonInteractionEvent event, IDConstants.ReactionRoles role) {
        event.deferEdit().queue();
        Member member = event.getMember();
        if (member == null) return;

        DiscordEmbed.log("Reaction role: " + role.name());
        clearRoles(member, Arrays.stream(IDConstants.ReactionRoles.VALUES).mapToLong(IDConstants.ReactionRoles::getId).toArray());
        DiscordEmbed.log("Cleared roles");
        PolarBot.getGuild().addRoleToMember(event.getMember(), PolarBot.getGuild().getRoleById(role.getId())).queue();
        receiveMessage(event, StringUtils.capitalize(role.name()));
        DiscordEmbed.log("Received message");
    }

    private static void handleColorRole(ButtonInteractionEvent event, IDConstants.ColorRoles color) {
        event.deferEdit().queue();
        Member member = event.getMember();
        if (member == null) return;

        DiscordEmbed.log("Color role: " + color.name());
        clearRoles(member, Arrays.stream(IDConstants.ColorRoles.VALUES).mapToLong(IDConstants.ColorRoles::getId).toArray());
        DiscordEmbed.log("Cleared roles");
        PolarBot.getGuild().addRoleToMember(event.getMember(), PolarBot.getGuild().getRoleById(color.getId())).queue();
        receiveMessage(event, StringUtils.capitalize(color.name()));
        DiscordEmbed.log("Received message");
    }

    private static void clearRoles(Member member, long[] roles) {
        for (long l : roles) {
            Role role = PolarBot.getGuild().getRoleById(l);
            if (role == null) continue;
            if (member.getRoles().contains(role)) {
                PolarBot.getGuild().removeRoleFromMember(member, role).queue();
            }
        }
    }

    private static void receiveMessage(ButtonInteractionEvent event, String roleName) {
        event.getHook().sendMessageEmbeds(
                DiscordEmbed.newBuilder()
                        .setTitle("You have received the role " + roleName)
                        .build()
        ).setEphemeral(true).queue();
    }
}
