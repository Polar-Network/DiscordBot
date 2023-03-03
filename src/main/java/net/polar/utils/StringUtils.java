package net.polar.utils;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class StringUtils {

    private StringUtils() {}

    public static @NotNull String capitalize(@NotNull String other) {
        if (other.isEmpty()) {
            return other;
        }
        return other.substring(0, 1).toUpperCase() + other.substring(1);
    }

    /**
     * Parses a string to a duration, for example 10s, 10m, 10h, 10d, 10m
     * @param str The string to parse
     * @return The duration
     */
    public static @NotNull Duration fromString(@NotNull String str) {
        if (str.isEmpty()) {
            return Duration.ZERO;
        }
        char lastChar = str.charAt(str.length() - 1);
        long duration = Long.parseLong(str.substring(0, str.length() - 1));
        return switch (lastChar) {
            case 's' -> Duration.ofSeconds(duration);
            case 'm' -> Duration.ofMinutes(duration);
            case 'h' -> Duration.ofHours(duration);
            case 'd' -> Duration.ofDays(duration);
            default -> Duration.ZERO;
        };
    }

}
