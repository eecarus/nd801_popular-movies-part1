package com.example.android.popularmovies.moviedb;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.R;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.LocalDate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ApiUtils {

    private static final String TAG = ApiUtils.class.getSimpleName();

    // Attribution: copied from Sunshine NetworkUtils example.  Could be changed to use okhttp
    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : null;
        } finally {
            urlConnection.disconnect();
        }
    }

    static URL toURL(Uri uri) {
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Unable to convert to URL", e);
        }
    }

    static Gson initializeGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        builder.setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        builder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        return builder.create();
    }

}
