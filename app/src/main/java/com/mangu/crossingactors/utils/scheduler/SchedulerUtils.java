package com.mangu.crossingactors.utils.scheduler;


public class SchedulerUtils {

    public static <T> IoMainScheduler<T> ioToMain() {
        return new IoMainScheduler<>();
    }
}
