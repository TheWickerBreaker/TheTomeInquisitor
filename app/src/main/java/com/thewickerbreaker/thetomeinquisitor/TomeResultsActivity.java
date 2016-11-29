package com.thewickerbreaker.thetomeinquisitor;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TomeResultsActivity extends AppCompatActivity implements LoaderCallbacks<List<TomeInfo>> {

    /**
     * Constant value for the tome loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int TOME_LOADER_ID = 1;
    private static String queryURL;

    private static String BOOK_REQUEST_URL;
    private String query = "";
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    /**
     * Adapter for the list of TomeInfo
     */
    private TomeInfoAdapter mAdapter;
    private EditText searchField;
    private Button find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tome_activity);
        //Hides keyboard until it is called for.
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        // Find a reference to the {@link ListView} in the layout
        ListView tomeListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        assert tomeListView != null;
        tomeListView.setEmptyView(mEmptyStateTextView);
        searchField = (EditText) findViewById(R.id.searchAgain);
        find = (Button) findViewById(R.id.findAgain);
        //Activates search when find button is pressed.
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets the query that is entered in to the search field.
                query = searchField.getEditableText().toString();
                //Activates search if the search field is not left empty
                if (!query.isEmpty()) {
                    queryURL = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&filter=free-ebooks&maxResults=40";
                    BOOK_REQUEST_URL = queryURL;
                    Intent resultsIntent = new Intent(TomeResultsActivity.this, TomeResultsActivity.class);
                    resultsIntent.putExtra("query", query);
                    startActivity(resultsIntent);
                } else {
                    //Toasts users a warning when the leave the search field empty and hits find.
                    customToast(getApplicationContext(),
                            getString(R.string.empty_search_toast), Toast.LENGTH_SHORT);
                }
            }
        });
        //Receives intent bundle with mQuery from main page search
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            query = extras.getString("query");
        }
        //Search activated
        queryURL = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&filter=free-ebooks&maxResults=40";
        BOOK_REQUEST_URL = queryURL;
        // Create a new adapter that takes an empty list of tomes as input
        mAdapter = new TomeInfoAdapter(this, new ArrayList<TomeInfo>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        tomeListView.setAdapter(mAdapter);
        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected tome.
        tomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current tome that was clicked on
                TomeInfo currentTome = mAdapter.getItem(position);
                Intent resultsIntent = new Intent(TomeResultsActivity.this, SelectedTomeActivity.class);
                //Passes data to the SelectedTomeActivity for the current selected book.
                assert currentTome != null;
                resultsIntent.putExtra("coverImage", currentTome.getmCoverImage());
                resultsIntent.putExtra("title", currentTome.getmTitle());
                resultsIntent.putExtra("subtitle", currentTome.getmSubtitle());
                resultsIntent.putExtra("author", currentTome.getmAuthor());
                resultsIntent.putExtra("pubDate", currentTome.getmPublisherDate());
                resultsIntent.putExtra("description", currentTome.getmDescription());
                resultsIntent.putExtra("genre", currentTome.getmGenre());
                resultsIntent.putExtra("pageCount", currentTome.getmPageCount());
                resultsIntent.putExtra("buyLink", currentTome.getmDownload());
                resultsIntent.putExtra("random", currentTome.getmRandom());
                startActivity(resultsIntent);
            }
        });
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(TOME_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            assert loadingIndicator != null;
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<TomeInfo>> onCreateLoader(int i, Bundle bundle) {
        View notFound = findViewById(R.id.empty_view);
        assert notFound != null;
        notFound.setVisibility(View.GONE);
        searchField.setVisibility(View.GONE);
        find.setVisibility(View.GONE);
        // Create a new loader for the given URL
        return new TomeResultsLoader(this, BOOK_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<TomeInfo>> loader, List<TomeInfo> tomes) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        assert loadingIndicator != null;
        loadingIndicator.setVisibility(View.GONE);
        searchField.setVisibility(View.VISIBLE);
        find.setVisibility(View.VISIBLE);
        //Formats String to be displayed if no item is found.
        String weSearched = "We've searched high…<br/>We've searched low...<br/>";
        String badSearch = "<font color='#EE0000'>" + query + "</font>";
        String notFound = "<br/>was nowhere to be found...";
        // Set empty state text to display "No tomes found."
        mEmptyStateTextView.setText(Html.fromHtml(weSearched + badSearch + notFound));
        // Clear the adapter of previous tome data
        mAdapter.clear();
        // If there is a valid list of {@link Tome}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (tomes != null && !tomes.isEmpty()) {
            mAdapter.addAll(tomes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<TomeInfo>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    //Customizes the Toast layout that warns users if the search field is left empty.
    @TargetApi(Build.VERSION_CODES.M)
    private void customToast(Context context, String msg, int duration) {
        Toast toast = Toast.makeText(context, msg, duration);
        toast.setMargin(0, .35f);
        View view = toast.getView();
        view.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        view.setAlpha(.9f);
        TextView toastMessage = (TextView) view.findViewById(android.R.id.message);
        toastMessage.setTextColor(getColor(R.color.mainTextGold));
        toastMessage.setPadding(24, 24, 24, 24);
        toast.show();
    }
}