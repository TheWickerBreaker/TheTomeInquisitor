package com.thewickerbreaker.thetomeinquisitor;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving tome data from USGS.
 */
final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the tome JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                //noinspection ThrowFromFinallyBlock
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link TomeInfo} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<TomeInfo> extractFeatureFromJson(String tomeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(tomeJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding tomes to
        List<TomeInfo> tomes = new ArrayList<>();
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(tomeJSON);
            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or tomes).
            JSONArray tomeArray = baseJsonResponse.getJSONArray("items");
            // For each tome in the tomeArray, create an {@link Tome} object
            for (int i = 0; i < tomeArray.length(); i++) {
                // Get a single tome at position i within the list of tomes
                JSONObject currentTome = tomeArray.getJSONObject(i);
                // For a given tome, extract the JSONObject associated with the
                // key called "volume", which represents a list of all properties
                // for that tome.
                JSONObject volumeInfo = currentTome.getJSONObject("volumeInfo");
                // Extract the value for the key called "title"
                String title = volumeInfo.optString("title");
                // Extract the value for the key called "authors"
                String author = volumeInfo.optString("authors");
                // Extract the value for the key called "infoLink"
                String publisher = volumeInfo.optString("publisher");
                // Extract the value for the key called "publishedDate"
                String publisherDate = volumeInfo.optString("publishedDate");
                // Extract the JSONObject for the key called "imageLinks"
                JSONObject imageOption = volumeInfo.optJSONObject("imageLinks");
                // Extract the value for the key called "thumbnail"
                String coverThumbnail = imageOption.optString("thumbnail");
                // Extract the value for the key called "subtitle"
                String subTitle = volumeInfo.optString("subtitle");
                // Extract the value for the key called "description"
                String description = volumeInfo.optString("description");
                // Extract the value for the key called "categories"
                String genre = volumeInfo.optString("categories");
                // Extract the value for the key called "pageCount"
                String pageCount = volumeInfo.optString("pageCount");
                // Extract the JASONObect for the key called "saleInfo"
                JSONObject salesInfo = currentTome.optJSONObject("saleInfo");
                // Extract the value for the key called "buyLink"
                String downloadLink = salesInfo.optString("buyLink");
                // Create a new {@link Tome} object with the magnitude, location, time,
                // and url from the JSON response.
                TomeInfo tome = new TomeInfo(title, author, publisher, publisherDate,
                        coverThumbnail, subTitle, description, genre, pageCount, downloadLink);
                // Add the new {@link Tome} to the list of tomes.
                tomes.add(tome);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the tome JSON results", e);
        }
        // Return the list of tomes
        return tomes;
    }

    /**
     * Query the USGS dataset and return a list of {@link TomeInfo} objects.
     */
    static List<TomeInfo> fetchTomeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link Tome}s and
        // returns the list of {@link Tome}s
        return extractFeatureFromJson(jsonResponse);
    }
}