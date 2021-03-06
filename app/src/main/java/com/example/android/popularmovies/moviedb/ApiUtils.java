package com.example.android.popularmovies.moviedb;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.LocalDate;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * This method checks if the network state is considered connected.   If not, it returns
     * false.  If it is connected, the additional boolean <code>checkForInternet</code> will
     * indicate if an additional check is needed before returning true.  This implementation
     * was adapted from the StackOverflow page suggested by the implementation guide.
     * <p>
     * Note that <code>checkForInternet</code> should never be called while on the main UI
     * thread!
     * <p>
     * Attribution: https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     *
     * @param context          the Activity or Application Context
     * @param checkForInternet true if a ping to Google is desired
     * @return true if the connection is ready
     */
    public static boolean isNetworkReadyForUse(Context context, boolean checkForInternet) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                if (!checkForInternet)
                    return true;    // no need to check further, this is good enough

                // attempt to actually connect to Google
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://www.google.com");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(1000);
                    conn.connect();
                    return true;        // we can connect if there is no exception
                } catch (MalformedURLException e) {
                    // this should never happen!
                    Log.e(TAG, "Unable to create Google.com URL", e);
                } catch (IOException e) {
                    // quietly fail and indicate no connectivity
                    // Log.v(TAG, "Unable to connect to Google", e);
                } finally {
                    closeConnectionQuietly(conn);
                }
            }
        }
        return false;
    }

    private static void closeConnectionQuietly(HttpURLConnection connection) {
        if (connection != null) {
            try {
                Closeable inputStream = connection.getInputStream();
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                // close quietly
            }
            try {
                Closeable outputStream = connection.getOutputStream();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                // close quietly
            }
            try {
                Closeable errorStream = connection.getErrorStream();
                if (errorStream != null)
                    errorStream.close();
            } catch (IOException e) {
                // close quietly
            }
            connection.disconnect();
        }
    }
}
