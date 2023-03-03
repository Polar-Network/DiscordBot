package net.polar.button;

import net.polar.button.listeners.VerifyButtonListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ButtonManager {

    private final Set<ButtonListener> buttons = ConcurrentHashMap.newKeySet();

    public ButtonManager() {
        buttons.addAll(
                List.of(
                        new VerifyButtonListener.Correct(),
                        new VerifyButtonListener.Wrong()
                )
        );
    }

    public @Nullable ButtonListener[] listenersFor(@NotNull String id) {
        return buttons.stream()
                .filter(button -> button.getButtonId().equalsIgnoreCase(id))
                .toArray(ButtonListener[]::new);
    }
}