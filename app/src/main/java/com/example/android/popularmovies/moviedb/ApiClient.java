package com.example.android.popularmovies.moviedb;

import android.net.Uri;

import com.google.gson.Gson;

import java.io.IOException;

import static com.example.android.popularmovies.moviedb.ApiUtils.getResponseFromHttpUrl;
import static com.example.android.popularmovies.moviedb.ApiUtils.initializeGson;
import static com.example.android.popularmovies.moviedb.ApiUtils.toURL;

class ApiClient {

    private static final String PATH_CONFIGURATION = "configuration";
    private static final String QUERY_API_KEY = "api_key";
    private static final String QUERY_PAGE = "page";

    private final String apiBaseUrl;
    private final String imageBaseUrl;
    private final String apiKey;
    private final Gson gson;
    private Configuration configuration;

    ApiClient(String apiBaseUrl, String imageBaseUrl, String apiKey) {
        validateInputs(apiBaseUrl, imageBaseUrl, apiKey);
        this.apiBaseUrl = apiBaseUrl;
        this.imageBaseUrl = imageBaseUrl;
        this.apiKey = apiKey;
        this.gson = initializeGson();
    }

    public Configuration getConfiguration() throws IOException {
        if (configuration == null)
            this.configuration = initializeConfiguration();
        return configuration;
    }

    public Uri getImageUri(String size, String path) throws IllegalStateException {
        if (path.startsWith("/"))
            path = path.substring(1); // remove the leading /

        return Uri.parse(imageBaseUrl).buildUpon()
                .appendPath(size)
                .appendPath(path)
                .build();
    }

    private Gson getGson() {
        return gson;
    }

    private Configuration initializeConfiguration() throws IOException {
        Uri uri = getUriBuilder(PATH_CONFIGURATION).build();
        String jsonResponse = getResponseFromHttpUrl(toURL(uri));
        return getGson().fromJson(jsonResponse, Configuration.class);
    }

    private void validateInputs(String apiBaseUrl, String imageBaseUrl, String apiKey) {
        if (apiBaseUrl == null || imageBaseUrl == null || apiKey == null)
            throw new NullPointerException("apiBaseUrl, imageBaseUrl, and apiKey are required.");
        if (apiBaseUrl.isEmpty() || imageBaseUrl.isEmpty() || apiKey.isEmpty())
            throw new IllegalArgumentException("apiBaseUrl, imageBaseUrl and apiKey should not be empty.");
    }

    private Uri.Builder getUriBuilder(String subPath) {
        Uri.Builder bldr = Uri.parse(apiBaseUrl).buildUpon();
        bldr.appendPath(subPath);
        bldr.appendQueryParameter(QUERY_API_KEY, apiKey);
        return bldr;
    }

    MovieSummaryResults getMovies(String type) throws IOException {
        Uri uri = getUriBuilder("movie")
                .appendPath(type)
                .appendQueryParameter(QUERY_PAGE, String.valueOf(1))
                .build();
        String jsonResponse = getResponseFromHttpUrl(toURL(uri));
        return getGson().fromJson(jsonResponse, MovieSummaryResults.class);
    }

}