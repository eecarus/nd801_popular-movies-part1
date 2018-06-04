package com.example.android.popularmovies.moviedb;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.android.popularmovies.moviedb.ApiUtils.initializeGson;

public class MovieDbService {

    private static final String TAG = MovieDbService.class.getSimpleName();
    private static ApiClient API_CLIENT;
    private static String POSTER_SIZE;
    private static final String SORTEDBY_POPULAR = "popular";
    private static final String SORTEDBY_TOPRATED = "top_rated";

    /**
     * Initializes the Service class.   This consists of retrieving configuration
     * information the Context's Resources object.   In order to avoid memory leaks,
     * this method avoids storing the Context object, since it is likely to be an
     * Activity, and storing it would cause zombie Activity objects to be retained
     * in memory.   If initialization has already occurred, then it is not
     * repeated.
     *
     * @param context most likely an Activity.
     */
    public static void initializeIfNeeded(Context context) {
        if (API_CLIENT != null)
            return;     // previously initialized, don't do it again

        String apiBaseUrl = context.getString(R.string.api_moviedb_base_url);
        String imageBaseUrl = context.getString(R.string.api_image_base_url);
        String apiKey = context.getString((R.string.api_moviedb_api_key));
        API_CLIENT = new ApiClient(apiBaseUrl, imageBaseUrl, apiKey);

        // retrieve selected image size
        try {
            POSTER_SIZE = context.getString(R.string.api_moviedb_poster_image_size);
        } catch (Resources.NotFoundException e) {
            POSTER_SIZE = "w185";
        }
    }

    /**
     * Takes a partial image path and converts it to a full URL.
     *
     * @param posterPath the partial path that comes from the MovieSummary object.
     * @return a Uri consisting of the full URL.
     */
    public static Uri getFullImagePath(String posterPath) {
        assertState();
        return API_CLIENT.getImageUri(POSTER_SIZE, posterPath);
    }

    /**
     * Returns data from an API call to the MovieDB endpoint.
     *
     * @param context
     * @return a MovieSummaryResults loaded from the API call.
     * @throws IOException           if an error occurs while loading the data
     * @throws IllegalStateException if the API client has not been properly initialized.
     */
    public static MovieSummaryResults getMovieSummaryResults(Context context, String type) throws IllegalStateException, IOException {
        assertState();
        return API_CLIENT.getMovies(type);
    }

    /**
     * Returns fake data for initial testing
     *
     * @param context
     * @return a MovieSummaryResults loaded from a raw fake_movie_data JSON file.
     * @throws IllegalStateException
     */
    public static MovieSummaryResults getFakeData(Context context) throws IllegalStateException {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.fake_movie_data);
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                line = reader.readLine();
            }

            String jsonString = buffer.toString();
            Log.w(TAG, jsonString);
            return initializeGson().fromJson(jsonString, MovieSummaryResults.class);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read fake data", e);
        }
    }

    /**
     * Makes sure that the API client has been properly initialized.  Throws
     * an <code>IllegalStateException</code> when it has not been initialized.
     */
    private static void assertState() {
        if (API_CLIENT == null)
            throw new IllegalStateException("MovieDbService must be initialized before use!");
    }
}
