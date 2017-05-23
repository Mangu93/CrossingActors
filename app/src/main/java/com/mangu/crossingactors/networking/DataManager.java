package com.mangu.crossingactors.networking;

import com.mangu.crossingactors.models.ActorListResponse;
import com.mangu.crossingactors.models.Credits;
import com.mangu.crossingactors.networking.services.ActorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class DataManager {

    private final ActorService mActorService;

    @SuppressWarnings("SameParameterValue")
    @Inject
    public DataManager(ActorService actorService) {
        this.mActorService = actorService;
    }

    public Observable<ActorListResponse> getActorList(String name) {
        return mActorService.getActorList(name);
    }

    public Observable<Credits> getActor(String id) {
        return mActorService.getActor(id);
    }

    public Observable<com.mangu.crossingactors.models.tv.Credits> getActorTv(String id) {
        return mActorService.getActorTv(id);
    }

}
