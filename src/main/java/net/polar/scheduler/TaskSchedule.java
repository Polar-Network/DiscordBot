package net.polar.scheduler;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.CompletableFuture;

public sealed interface TaskSchedule permits
        TaskSchedule.DurationSchedule,
        TaskSchedule.FutureSchedule,
        TaskSchedule.ParkSchedule,
        TaskSchedule.StopSchedule,
        TaskSchedule.ImmediateSchedule
{

    static @NotNull TaskSchedule duration(@NotNull Duration duration) {
        return new DurationSchedule(duration);
    }

    static @NotNull TaskSchedule duration(long duration, @NotNull TemporalUnit unit) {
        return new DurationSchedule(Duration.of(duration, unit));
    }

    static @NotNull TaskSchedule hours(long hours) {
        return new DurationSchedule(Duration.ofHours(hours));
    }

    static @NotNull TaskSchedule minutes(long minutes) {
        return new DurationSchedule(Duration.ofMinutes(minutes));
    }

    static @NotNull TaskSchedule seconds(long seconds) {
        return new DurationSchedule(Duration.ofSeconds(seconds));
    }

    static @NotNull TaskSchedule millis(long millis) {
        return new DurationSchedule(Duration.ofMillis(millis));
    }

    static @NotNull TaskSchedule future(@NotNull CompletableFuture<?> future) {
        return new FutureSchedule(future);
    }

    static @NotNull TaskSchedule park() {
        return ParkSchedule.INSTANCE;
    }

    static @NotNull TaskSchedule stop() {
        return StopSchedule.INSTANCE;
    }

    static @NotNull TaskSchedule immediate() {
        return ImmediateSchedule.INSTANCE;
    }


    final class DurationSchedule implements TaskSchedule {
        private final Duration duration;
        DurationSchedule(@NotNull Duration duration) {this.duration = duration;}
        @NotNull public Duration duration() {return duration;}
    }

    final class FutureSchedule implements TaskSchedule {
        private final CompletableFuture<?> future;
        FutureSchedule(@NotNull CompletableFuture<?> future) {this.future = future;}
        @NotNull public CompletableFuture<?> getFuture() {return future;}
    }

    final class ParkSchedule implements TaskSchedule {
        public static final ParkSchedule INSTANCE = new ParkSchedule();
    }
    final class StopSchedule implements TaskSchedule {
        public static final StopSchedule INSTANCE = new StopSchedule();
    }
    final class ImmediateSchedule implements TaskSchedule {
        public static final ImmediateSchedule INSTANCE = new ImmediateSchedule();
    }

}
