package net.polar.button;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

public interface ButtonListener {

    @NotNull String getButtonId();

    void onClick(@NotNull ButtonInteractionEvent event);

}
