package net.polar.scheduler;

import it.unimi.dsi.fastutil.HashCommon;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.function.Supplier;

public class Task {

    private static final VarHandle PARKED;

    static {
        try {
            PARKED = MethodHandles.lookup().findVarHandle(Task.class, "parked", boolean.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final int id;
    private final Supplier<TaskSchedule> schedule;
    private final ExecutionType executionType;
    private final Scheduler owner;

    private volatile boolean alive = true;
    private volatile boolean parked = true;

    Task(
            int id,
            @NotNull Supplier<TaskSchedule> schedule,
            @NotNull ExecutionType executionType,
            @NotNull Scheduler scheduler
    ) {
        this.id = id;
        this.schedule = schedule;
        this.executionType = executionType;
        this.owner = scheduler;
    }

    public void unpark() {
        this.owner.unparkTask(this);
    }

    public void tryUnpark() {
        PARKED.compareAndSet(this, true, false);
    }

    public boolean isParked() {
        return this.parked;
    }

    public void cancel() {
        this.alive = false;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public int getId() {
        return this.id;
    }

    public @NotNull Supplier<TaskSchedule> getSchedule() {
        return this.schedule;
    }

    public @NotNull ExecutionType getExecutionType() {
        return this.executionType;
    }

    public @NotNull Scheduler getOwner() {
        return this.owner;
    }

    public void setParked(boolean parked) {
        this.parked = parked;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return this.id == ((Task) obj).id;
    }

    @Override
    public int hashCode() {
        return HashCommon.murmurHash3(this.id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + this.id +
                ", schedule=" + this.schedule +
                ", executionType=" + this.executionType +
                ", owner=" + this.owner +
                ", alive=" + this.alive +
                ", parked=" + this.parked +
                '}';
    }
}
