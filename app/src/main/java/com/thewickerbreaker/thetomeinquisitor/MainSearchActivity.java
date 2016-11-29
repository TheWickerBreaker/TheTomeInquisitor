package com.thewickerbreaker.thetomeinquisitor;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainSearchActivity extends AppCompatActivity {

    public String mQuery;
    private EditText searchField;
    private TextView searchText;
    private Button find;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        //Hides keyboard until it is called.
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        //Finds all the views that are required for this activity.
        searchField = (EditText) findViewById(R.id.searchField);
        searchText = (TextView) findViewById(R.id.searchText);
        find = (Button) findViewById(R.id.find);
        progress = (ProgressBar) findViewById(R.id.progress);
        //Activates search when find button is pressed.
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets the query that is entered in to the search field.
                mQuery = searchField.getEditableText().toString();
                //Checks to see if the search field is not empty before starting the search.
                if (!mQuery.isEmpty()) {
                    //Hides and displays views to make the main search activity look like a book
                    //cover for purely aesthetic reasons.
                    progress.setVisibility(View.VISIBLE);
                    searchField.setVisibility(View.GONE);
                    searchText.setText(mQuery);
                    searchText.setVisibility(View.VISIBLE);
                    find.setVisibility(View.GONE);
                    //Holds on the main search activity for a second and a half to allow the user to
                    //enjoy the above mentioned aesthetic feature
                    new CountDownTimer(1500, 1000) {
                        public void onTick(long millisecondsUntilDone) {
                        }
                        public void onFinish() {
                            //Passes search query to the results activity.
                            Intent resultsIntent = new Intent(MainSearchActivity.this,
                                    TomeResultsActivity.class);
                            resultsIntent.putExtra("query",mQuery);
                            startActivity(resultsIntent);
                        }
                    }.start();
                } else {
                    //Toasts users a warning when the leave the search field empty and hits find.
                    customToast(getApplicationContext(),
                            getString(R.string.empty_search_toast), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        //Resets main search activity to default setting when user returns to page.
        searchField.setVisibility(View.VISIBLE);
        searchField.setText("");
        find.setVisibility(View.VISIBLE);
        searchText.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        super.onResume();
    }

    //Customizes the Toast layout that warns users if the search field is left empty.
    @TargetApi(Build.VERSION_CODES.M)
    private void customToast(Context context, String msg, int duration) {
        Toast toast = Toast.makeText(context, msg, duration);
        View view = toast.getView();
        view.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        view.setAlpha(.9f);
        TextView toastMessage = (TextView) view.findViewById(android.R.id.message);
        toastMessage.setTextColor(getColor(R.color.mainTextGold));
        toastMessage.setPadding(24, 24, 24, 24);
        toast.show();
    }
}
