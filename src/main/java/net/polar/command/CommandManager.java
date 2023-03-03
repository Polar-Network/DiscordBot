package net.polar.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.polar.command.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class CommandManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);

    private final Set<DiscordCommand> commands = Set.of(
            new HelpCommand(),
            new PurgeCommand(),
            new IPCommand(),
            new LevelCommand(),
            new AnnounceCommand(),
            new TimeOutCommand(),
            new BanCommand(),
            new PushWelcomeCommand(),
            new PushVerifyCommand(),
            new PushRulesCommand(),
            new PushReactionRolesCommand()
    );

    public void init(@NotNull JDA client) {
        List<SlashCommandData> data = new ArrayList<>(commands.size());

        for (DiscordCommand command : commands) {
            final SlashCommandData cmd = Commands.slash(command.getName(), command.getDescription());
            cmd.addOptions(command.getOptions());
            cmd.setGuildOnly(true);
            data.add(cmd);
        }

        client.updateCommands().addCommands(data).queue();
        LOGGER.info("Registered {} commands", commands.size());
    }

    public Set<DiscordCommand> getCommands() {
        return Set.copyOf(commands);
    }

    public @Nullable DiscordCommand getCommand(String name) {
        return commands.stream().filter(cmd -> cmd.getName().equals(name)).findFirst().orElse(null);
    }


}
