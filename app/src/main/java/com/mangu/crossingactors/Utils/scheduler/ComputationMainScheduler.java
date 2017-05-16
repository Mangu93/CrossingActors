package com.mangu.crossingactors.Utils.scheduler;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@SuppressWarnings("ALL")
public class ComputationMainScheduler<T> extends BaseScheduler<T> {

    protected ComputationMainScheduler() {
        super(Schedulers.computation(), AndroidSchedulers.mainThread());
    }
}
