package net.polar.command.impl;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.polar.PolarBot;
import net.polar.command.DiscordCommand;
import net.polar.utils.DiscordEmbed;
import net.polar.utils.IDConstants;
import org.jetbrains.annotations.NotNull;

public final class PushRulesCommand extends DiscordCommand {

    public PushRulesCommand() {
        super("pushrules", "Push rules into the channel", IDConstants.OWNER_ROLE);
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        TextChannel channel = PolarBot.getGuild().getTextChannelById(IDConstants.RULES_ID);
        channel.sendMessageEmbeds(
                DiscordEmbed.newThemedBuilder()
                        .setTitle("**RULES**")
                        .addField("1. Be respectful", "This means no mean, rude, or harassing comments. Treat others the way you want to be treated.", false)
                        .addField("2. No pornographic/adult/other NSFW material", "We are a Minecraft server, not a porno server", false)
                        .addField("3. No advertising", "No invasive advertising, whether it be for other communities or streams. Media related roles are permitted to bypass this rule and provide actual content value for the community.", false)
                        .addField("4. Don’t share your personal information", "Do not share your personal information or the personal information of other users without their consent. This includes phone numbers, addresses, and any other sensitive information.", false)
                        .addField("5. No threats", "Threatening to DOX/DDOS or to engage in other illegal activities is strictly prohibited, you will be reported to authortities and banned", false)
                        .addField("6. Use common sense", "Before doing something, think about it :D", false)
                        .addField("7. Follow Discord TOS (https://discord.com/terms)", "You're on Discord, please follow its guidelines! Not following will result in a permanent ban", false)
                        .setDescription("*Your presence in this server implies accepting these rules, including all further changes. These changes might be done at any time without notice, it is your responsibility to check for them. PolarMC moderation reserves the right to punish you without providing any reason, all punishments are managed by administration to avoid mistreatment*")
                        .build()
        ).queue();
        event.reply("Pushed rules!").setEphemeral(true).queue();
    }
}
