package com.mangu.crossingactors.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

public class UrlFactory {
    private static final String GOOGLE_QUERY_BASE = "https://www.google.com/search?q=";

    private static String formQuery(@NonNull String query) {
        return GOOGLE_QUERY_BASE + query;
    }

    public static Intent generateBrowserIntent(@NonNull String destination) {
        String destinationUrl = formQuery(destination);
        return new Intent(Intent.ACTION_VIEW).setData(Uri.parse(destinationUrl));
    }
}
