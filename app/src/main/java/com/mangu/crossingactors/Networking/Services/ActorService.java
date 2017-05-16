package com.mangu.crossingactors.Networking.Services;

import com.mangu.crossingactors.Model.ActorListResponse;
import com.mangu.crossingactors.Model.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.mangu.crossingactors.BuildConfig.THE_MOVIE_DB_API_TOKEN;

public interface ActorService {
    @Headers({"api_key: " +THE_MOVIE_DB_API_TOKEN})
    @GET("search/person")
    Observable<ActorListResponse> getActorList(@Query("query") String name);

    @Headers({"api_key: " +THE_MOVIE_DB_API_TOKEN})
    @GET("person/{person_id}/movie_credits?api_key={api_key}")
    Observable<Result> getActor(@Path("person_id") String id);
}
