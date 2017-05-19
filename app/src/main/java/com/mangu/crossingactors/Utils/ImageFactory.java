package com.mangu.crossingactors.Utils;

import android.support.annotation.NonNull;

public class ImageFactory {
    private static final String BASE_URL_PICS = "https://image.tmdb.org/t/p/";
    private static final String SIZE_PICS = "w185";

    private static final String URL_PICS = BASE_URL_PICS + SIZE_PICS;

    public static String formUrlPic(@NonNull String img_url) {
        return URL_PICS + img_url;
    }
}
