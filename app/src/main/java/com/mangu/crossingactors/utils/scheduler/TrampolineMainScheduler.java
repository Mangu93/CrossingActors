package com.mangu.crossingactors.utils.scheduler;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@SuppressWarnings("ALL")
public class TrampolineMainScheduler<T> extends BaseScheduler<T> {

    protected TrampolineMainScheduler() {
        super(Schedulers.trampoline(), AndroidSchedulers.mainThread());
    }
}
