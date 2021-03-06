package com.mangu.crossingactors.utils.scheduler;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@SuppressWarnings("ALL")
public class NewThreadMainScheduler<T> extends BaseScheduler<T> {

    protected NewThreadMainScheduler() {
        super(Schedulers.newThread(), AndroidSchedulers.mainThread());
    }
}
