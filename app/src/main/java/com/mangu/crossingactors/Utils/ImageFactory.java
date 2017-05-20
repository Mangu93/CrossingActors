package com.mangu.crossingactors.Utils;

import android.support.annotation.NonNull;

public class ImageFactory {
    private static final String BASE_URL_PICS = "https://image.tmdb.org/t/p/";

    private static final String SIZE_PROFILE_PICS = "w185";
    private static final String SIZE_POSTER_PICS = "w500";

    private static final String URL_PROFILE_PICS = BASE_URL_PICS + SIZE_PROFILE_PICS;
    private static final String URL_POSTER_PICS = BASE_URL_PICS + SIZE_POSTER_PICS;

    public static String formUrlPic(@NonNull String img_url) {
        return URL_PROFILE_PICS + img_url;
    }

    public static String formUrlPoster(@NonNull String img_url) {
        return URL_POSTER_PICS + img_url;
    }

}
