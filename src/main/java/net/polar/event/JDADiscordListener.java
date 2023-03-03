package net.polar.event;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.polar.PolarBot;
import net.polar.button.ButtonListener;
import net.polar.button.listeners.ColorReactionRoleListener;
import net.polar.command.DiscordCommand;
import net.polar.utils.DiscordEmbed;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class JDADiscordListener implements EventListener {

    private final EventManager manager;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    JDADiscordListener(EventManager manager) {
        this.manager = manager;
    }

    @Override @SuppressWarnings("unchecked")
    public void onEvent(@NotNull GenericEvent event) {
        executor.submit(() -> {

            if (event instanceof SlashCommandInteractionEvent cmdEvent) {
                final DiscordCommand command = PolarBot.getCommandManager().getCommand(cmdEvent.getName());
                if (command == null) return;
                Member member = cmdEvent.getMember();
                if (member == null) return;

                Role highestRole = member.getRoles().get(0); // Highest role
                if (highestRole == null) return;

                if (highestRole.getPosition() < command.getRoleRequired().getPosition())  {
                    cmdEvent.replyEmbeds(
                            DiscordEmbed.newErrorBuilder()
                                    .setDescription("You need to be " + command.getRoleRequired().getAsMention() + " to use this command")
                                    .build()
                    ).setEphemeral(true).queue();
                    return;
                }
                command.execute(cmdEvent);
                return; // Don't call event listeners for slash commands
            }

            if (event instanceof ButtonInteractionEvent e) {
                ColorReactionRoleListener.handle(e);
                ButtonListener[] listeners = PolarBot.getButtonManager().listenersFor(e.getComponentId());
                if (listeners == null || listeners.length == 0) return;
                Arrays.stream(listeners).forEach(listener -> listener.onClick(e));
                return; // Don't call event listeners for buttons
            }

            List<net.polar.event.EventListener<?>> listeners = manager.getListeners(event.getClass());
            if (listeners == null || listeners.isEmpty()) return;
            for (var listener : listeners) {
                ((net.polar.event.EventListener<GenericEvent>) listener).onEvent(event);
            }
        });
    }
}
