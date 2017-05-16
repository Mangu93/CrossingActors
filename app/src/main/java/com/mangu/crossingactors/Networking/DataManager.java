package com.mangu.crossingactors.Networking;

import com.mangu.crossingactors.Model.ActorListResponse;
import com.mangu.crossingactors.Model.Credits;
import com.mangu.crossingactors.Networking.Services.ActorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class DataManager {

    private final ActorService actorService;

    @Inject
    public DataManager(ActorService actorService) {
        this.actorService = actorService;
    }

    public Observable<ActorListResponse> getActorList(String name) {
        return actorService.getActorList(name);
    }

    public Observable<Credits> getActor(String id) {
        return actorService.getActor(id);
    }

}
