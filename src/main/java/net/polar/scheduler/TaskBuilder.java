package net.polar.scheduler;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskBuilder {

    private final Runnable runnable;
    private final Scheduler owner;

    private ExecutionType executionType = ExecutionType.SYNC;
    private TaskSchedule delay = TaskSchedule.immediate();
    private TaskSchedule repeat = TaskSchedule.stop();


    TaskBuilder(@NotNull Runnable runnable, @NotNull Scheduler owner) {
        this.runnable = runnable;
        this.owner = owner;
    }

    public @NotNull TaskBuilder executionType(@NotNull ExecutionType executionType) {
        this.executionType = executionType;
        return this;
    }

    public @NotNull TaskBuilder delay(@NotNull TaskSchedule delay) {
        this.delay = delay;
        return this;
    }

    public @NotNull TaskBuilder repeat(@NotNull TaskSchedule repeat) {
        this.repeat = repeat;
        return this;
    }

    public @NotNull TaskBuilder delay(@NotNull Duration duration) {
        this.delay = TaskSchedule.duration(duration);
        return this;
    }

    public @NotNull TaskBuilder delay(long duration, @NotNull TemporalUnit unit) {
        this.delay = TaskSchedule.duration(duration, unit);
        return this;
    }

    public @NotNull TaskBuilder repeat(@NotNull Duration duration) {
        this.repeat = TaskSchedule.duration(duration);
        return this;
    }

    public @NotNull TaskBuilder repeat(long duration, @NotNull TemporalUnit unit) {
        this.repeat = TaskSchedule.duration(duration, unit);
        return this;
    }

    public @NotNull Task schedule() {
        Runnable runnable = this.runnable;
        ExecutionType executionType = this.executionType;
        TaskSchedule delay = this.delay;
        TaskSchedule repeat = this.repeat;
        AtomicBoolean first = new AtomicBoolean(true);
        return owner.submitTask(() -> {
            if (first.get()) {
                first.set(false);
                return delay;
            }
            runnable.run();
            return repeat;
        }, executionType);
    }

}
