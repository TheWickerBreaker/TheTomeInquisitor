package com.thewickerbreaker.thetomeinquisitor;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
class TomeResultsLoader extends AsyncTaskLoader<List<TomeInfo>> {
    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link TomeResultsLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    TomeResultsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<TomeInfo> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        return QueryUtils.fetchTomeData(mUrl);
    }
}