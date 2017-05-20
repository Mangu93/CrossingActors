package com.mangu.crossingactors.Networking.Services;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mangu.crossingactors.BuildConfig.THE_MOVIE_DB_API_TOKEN;

public class ActorServiceFactory {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static ActorService makeActorService() {
        return makeActorService(makeGson());
    }

    private static ActorService makeActorService(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(makeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(ActorService.class);
    }

    private static OkHttpClient makeOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(loggingInterceptor);
        httpClientBuilder.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();
            HttpUrl url = originalHttpUrl.newBuilder().addQueryParameter("api_key", THE_MOVIE_DB_API_TOKEN).build();
            Request.Builder requestBuilder = original.newBuilder().url(url);
            return chain.proceed(requestBuilder.build());
        });
        return httpClientBuilder.build();
    }

    private static Gson makeGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
}
