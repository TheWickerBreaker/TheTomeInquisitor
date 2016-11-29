package com.thewickerbreaker.thetomeinquisitor;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class SelectedTomeActivity extends AppCompatActivity {

    private String coverImageURL;
    private String title;
    private String subtitle;
    private String author;
    private String pubDate;
    private String description;
    private String genre;
    private String pageCount;
    private String download;
    private int randomColorPalletID;
    private String mQuery;

    @SuppressLint("Assert")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_tome);
        //Finds all the views to be used in this activity
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.selectedTome);
        LinearLayout pageColor = (LinearLayout) findViewById(R.id.pageColor);
        ImageView coverImage = (ImageView) findViewById(R.id.coverImage);
        TextView selectedTitle = (TextView) findViewById(R.id.selectedTitle);
        TextView selectedSubtitle = (TextView) findViewById(R.id.selectedSubtitle);
        TextView selectedByLine = (TextView) findViewById(R.id.selectedByLine);
        TextView selectedAuthor = (TextView) findViewById(R.id.selectedAuthor);
        TextView selectedPubDate = (TextView) findViewById(R.id.selectedPubDate);
        View topDivideLine = findViewById(R.id.topDivideLine);
        TextView descriptionHeader = (TextView) findViewById(R.id.descriptionHeader);
        TextView selectedDescription = (TextView) findViewById(R.id.selectedDescription);
        TextView genreHeader = (TextView) findViewById(R.id.genreHeader);
        TextView selectedGenre = (TextView) findViewById(R.id.selectedGenre);
        TextView pageCountHeader = (TextView) findViewById(R.id.pageCountHeader);
        TextView selectedPageCount = (TextView) findViewById(R.id.selectedPageCount);
        View bottomDivideLine = findViewById(R.id.bottomDivideLine);
        Button downloadBtn = (Button) findViewById(R.id.downloadBtn);
        final Button find = (Button) findViewById(R.id.findYetAgain);
        final EditText searchField = (EditText) findViewById(R.id.searchYetAgain);
        //Hides keyboard until it is called
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        //Receives Intent Bundle for Data to populate the selected books details.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            coverImageURL = extras.getString("coverImage");
            title = extras.getString("title");
            subtitle = extras.getString("subtitle");
            author = extras.getString("author");
            pubDate = extras.getString("pubDate");
            description = extras.getString("description");
            genre = extras.getString("genre");
            pageCount = extras.getString("pageCount");
            download = extras.getString("buyLink");
            randomColorPalletID = extras.getInt("random");
        }
        //Checks Intent Bundle for null entries and provide alt data if so.
        if (coverImageURL.isEmpty()) {
            assert coverImage != null;
            coverImage.setImageResource(R.drawable.nocover);
        }
        if (author.isEmpty()) {
            author = getString(R.string.no_author);
        }
        if (description.isEmpty()) {
            description = getString(R.string.no_description);
        }
        if (genre.isEmpty()) {
            genre = getString(R.string.na);
        }
        if (pageCount.isEmpty()) {
            pageCount = getString(R.string.na);
        }
        //uses Picasso to get the Thumbnail image from the URL and sets said image to the ImageView
        Picasso
                .with(this)
                .load(coverImageURL)
                .into(coverImage);
        //Removes brackets and reformats information from incoming arraylist.
        author = author.replace("[", "").replace("\"", "").replace(",", ",  ").replace("]", "");
        genre = genre.replace("[", "").replace("\"", "").replace(",", ",  ").replace("]", "");
        //Sets the selected summary page based on the information received from the intent bundle.
        assert selectedTitle != null;
        selectedTitle.setText(title);
        assert selectedSubtitle != null;
        selectedSubtitle.setText(subtitle);
        assert selectedAuthor != null;
        selectedAuthor.setText(author);
        assert selectedPubDate != null;
        selectedPubDate.setText(pubDate);
        assert selectedDescription != null;
        selectedDescription.setText(description);
        assert selectedGenre != null;
        selectedGenre.setText(genre);
        assert selectedPageCount != null;
        selectedPageCount.setText(pageCount);
        //Color Ids for noted segments of the selected tome summary page.
        int titleHeadersColor;
        int authorInfoColor;
        int buttonDividersColor;
        int paperColor;
        int coverEdgeColor;
        //Switches color pallet to randomize the display of the selected tome summary page.

        switch (randomColorPalletID) {
            case 0:
            case 1:
                titleHeadersColor = R.color.pallet1MD;
                authorInfoColor = R.color.pallet1M;
                buttonDividersColor = R.color.pallet1L;
                paperColor = R.color.pallet1ML;
                coverEdgeColor = R.color.pallet1D;
                break;
            case 2:
                titleHeadersColor = R.color.pallet2MD;
                authorInfoColor = R.color.pallet2M;
                buttonDividersColor = R.color.pallet2ML;
                paperColor = R.color.pallet2L;
                coverEdgeColor = R.color.pallet2D;
                break;
            case 3:
                titleHeadersColor = R.color.pallet3L;
                authorInfoColor = R.color.pallet3MD;
                buttonDividersColor = R.color.pallet3M;
                paperColor = R.color.pallet3ML;
                coverEdgeColor = R.color.pallet3D;
                break;
            case 4:
                titleHeadersColor = R.color.pallet4ML;
                authorInfoColor = R.color.pallet4MD;
                buttonDividersColor = R.color.pallet4M;
                paperColor = R.color.pallet4L;
                coverEdgeColor = R.color.pallet4D;
                break;
            case 5:
                titleHeadersColor = R.color.pallet5L;
                authorInfoColor = R.color.pallet5M;
                buttonDividersColor = R.color.pallet5ML;
                paperColor = R.color.pallet5D;
                coverEdgeColor = R.color.pallet5MD;
                break;
            case 6:
                titleHeadersColor = R.color.pallet6ML;
                authorInfoColor = R.color.pallet6M;
                buttonDividersColor = R.color.pallet6L;
                paperColor = R.color.pallet6MD;
                coverEdgeColor = R.color.pallet6D;
                break;
            case 7:
                titleHeadersColor = R.color.pallet7L;
                authorInfoColor = R.color.pallet7MD;
                buttonDividersColor = R.color.pallet7M;
                paperColor = R.color.pallet7ML;
                coverEdgeColor = R.color.pallet7D;
                break;
            case 8:
                titleHeadersColor = R.color.pallet8MD;
                authorInfoColor = R.color.pallet8M;
                buttonDividersColor = R.color.pallet8ML;
                paperColor = R.color.pallet8L;
                coverEdgeColor = R.color.pallet8D;
                break;
            case 9:
                titleHeadersColor = R.color.pallet9ML;
                authorInfoColor = R.color.pallet9MD;
                buttonDividersColor = R.color.pallet9M;
                paperColor = R.color.pallet9L;
                coverEdgeColor = R.color.pallet9D;
                break;
            default:
                titleHeadersColor = R.color.pallet10MD;
                authorInfoColor = R.color.pallet10ML;
                buttonDividersColor = R.color.pallet10L;
                paperColor = R.color.pallet10D;
                coverEdgeColor = R.color.pallet10M;
                break;
        }
        //Assigns colors to backgrounds and textColors based on the Switch pallet group.
        assert mainLayout != null;
        mainLayout.setBackgroundResource(coverEdgeColor);
        assert pageColor != null;
        pageColor.setBackgroundResource(paperColor);
        assert coverImage != null;
        coverImage.setBackgroundResource(coverEdgeColor);
        selectedTitle.setTextColor(getResources().getColor(titleHeadersColor));
        selectedSubtitle.setTextColor(getResources().getColor(titleHeadersColor));
        assert selectedByLine != null;
        selectedByLine.setTextColor(getResources().getColor(authorInfoColor));
        selectedAuthor.setTextColor(getResources().getColor(authorInfoColor));
        selectedPubDate.setTextColor(getResources().getColor(titleHeadersColor));
        assert topDivideLine != null;
        topDivideLine.setBackgroundResource(buttonDividersColor);
        assert descriptionHeader != null;
        descriptionHeader.setTextColor(getResources().getColor(titleHeadersColor));
        selectedDescription.setTextColor(getResources().getColor(authorInfoColor));
        assert genreHeader != null;
        genreHeader.setTextColor(getResources().getColor(titleHeadersColor));
        selectedGenre.setTextColor(getResources().getColor(authorInfoColor));
        assert pageCountHeader != null;
        pageCountHeader.setTextColor(getResources().getColor(titleHeadersColor));
        selectedPageCount.setTextColor(getResources().getColor(authorInfoColor));
        assert bottomDivideLine != null;
        bottomDivideLine.setBackgroundResource(buttonDividersColor);
        assert downloadBtn != null;
        downloadBtn.setBackgroundResource(buttonDividersColor);
        downloadBtn.setTextColor(getResources().getColor(coverEdgeColor));
        //Sends user to GooglePlay page to download their FreeTome if pressed.
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(download); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        //Starts a new search if button is pressed.
        assert find != null;
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets the query that is entered in to the search field.
                assert searchField != null;
                mQuery = searchField.getEditableText().toString();
                //Checks to see if the search field is not empty before starting the search.
                if (!mQuery.isEmpty()) {
                    //Passes search query to the results activity.
                    Intent resultsIntent = new Intent(SelectedTomeActivity.this,
                            TomeResultsActivity.class);
                    resultsIntent.putExtra("query", mQuery);
                    startActivity(resultsIntent);
                } else {
                    //Toasts users a warning when the leave the search field empty and hits find.
                    customToast(getApplicationContext(),
                            getString(R.string.empty_search_toast), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    //Customizes the Toast layout that warns users if the search field is left empty.
    @TargetApi(Build.VERSION_CODES.M)
    private void customToast(Context context, String msg, int duration) {
        Toast toast = Toast.makeText(context, msg, duration);
        View view = toast.getView();
        toast.setMargin(0, .35f);
        view.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        view.setAlpha(.9f);
        TextView toastMessage = (TextView) view.findViewById(android.R.id.message);
        toastMessage.setTextColor(getColor(R.color.mainTextGold));
        toastMessage.setPadding(24, 24, 24, 24);
        toast.show();
    }
}