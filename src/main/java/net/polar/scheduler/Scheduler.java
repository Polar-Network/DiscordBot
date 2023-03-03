package net.polar.scheduler;

import org.jctools.queues.MpscUnboundedArrayQueue;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class Scheduler {

    private final MpscUnboundedArrayQueue<Task> taskQueue = new MpscUnboundedArrayQueue<>(64);
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private final AtomicInteger taskCounter = new AtomicInteger(0);
    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("Scheduler Thread");
        thread.setDaemon(true);
        return thread;
    });


    public Scheduler() {
        new Thread(() -> {
            while (true) {
                process();
            }
        }).start();
    }


    public void unparkTask(@NotNull Task task) {
        task.tryUnpark();
        taskQueue.relaxedOffer(task);
    }

    public @NotNull Task submitTask(Supplier<TaskSchedule> supplier, ExecutionType executionType) {
        Task task = new Task(taskCounter.getAndIncrement(), supplier, executionType, this);
        handleTask(task);
        return task;
    }

    public @NotNull TaskBuilder buildTask(@NotNull Runnable runnable) {
        return new TaskBuilder(runnable, this);
    }

    public @NotNull Task scheduleTask(
            @NotNull Runnable runnable,
            @NotNull TaskSchedule delay,
            @NotNull TaskSchedule repeat,
            @NotNull ExecutionType executionType
    ) {
        return buildTask(runnable).delay(delay).repeat(repeat).executionType(executionType).schedule();
    }

    private void process() {
        if (!taskQueue.isEmpty()) {
            taskQueue.drain((it) -> {
                if (!it.isAlive()) return;
                switch (it.getExecutionType()) {
                    case SYNC -> handleTask(it);
                    case ASYNC -> forkJoinPool.submit(() -> handleTask(it));
                }
            });
        }
    }

    private void handleTask(@NotNull Task task) {
        TaskSchedule schedule = task.getSchedule().get();
        if (schedule instanceof TaskSchedule.DurationSchedule d) {
            scheduledExecutor.schedule(() -> {
                safeExecute(task);
            }, d.duration().toMillis(), TimeUnit.MILLISECONDS);
        }
        else if (schedule instanceof TaskSchedule.FutureSchedule f) {
            f.getFuture().thenRun(() -> {
                safeExecute(task);
            });
        }
        else if (schedule instanceof TaskSchedule.ParkSchedule) {
            task.setParked(true);
        }
        else if (schedule instanceof TaskSchedule.StopSchedule) {
            task.cancel();
        }
        else if (schedule instanceof TaskSchedule.ImmediateSchedule) {
            taskQueue.relaxedOffer(task);
        }
    }

    private void safeExecute(@NotNull Task task) {
        switch (task.getExecutionType()) {
            case SYNC -> taskQueue.offer(task);
            case ASYNC -> {
                forkJoinPool.submit(() -> {
                    if (task.isAlive()) {
                        handleTask(task);
                    }
                });
            }
        }
    }

}
