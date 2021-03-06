package com.mangu.crossingactors.utils.scheduler;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@SuppressWarnings("ALL")
public class SingleMainScheduler<T> extends BaseScheduler<T> {

    protected SingleMainScheduler() {
        super(Schedulers.single(), AndroidSchedulers.mainThread());
    }
}
