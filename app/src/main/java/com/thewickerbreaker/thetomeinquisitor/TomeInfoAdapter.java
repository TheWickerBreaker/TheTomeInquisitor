package com.thewickerbreaker.thetomeinquisitor;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.thewickerbreaker.thetomeinquisitor.R.id.tomeTitle;

class TomeInfoAdapter extends ArrayAdapter<TomeInfo> {

    TomeInfoAdapter(Activity context, ArrayList<TomeInfo> tomeInfo) {
        super(context, 0, tomeInfo);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        //Gets current data set for TomeInfo
        TomeInfo currentInfo = getItem(position);
        //Finds all the views needed for this adapter.
        LinearLayout mainContainter = (LinearLayout) listItemView.findViewById(R.id.mainContainer);
        LinearLayout publisherBG = (LinearLayout) listItemView.findViewById(R.id.publisherContainer);
        View topLine = listItemView.findViewById(R.id.topSpineLine);
        TextView titleTextView = (TextView) listItemView.findViewById(tomeTitle);
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.tomeAuthor);
        TextView publishInfo = (TextView) listItemView.findViewById(R.id.publisher);
        TextView byLine = (TextView) listItemView.findViewById(R.id.byLine);
        //Sets title text in TomeResultActivity
        assert currentInfo != null;
        titleTextView.setText(currentInfo.getmTitle());
        //Gets the string from the JSON results which is also an array.
        String authorList = currentInfo.getmAuthor();
        //Removes brackets and reformats issues caused from the author string coming from an array
        authorList = authorList.replace("[", "").replace("\"", "").replace(",", ",  ").replace("]", "");
        //Sets author/s text in TomeResultActivity
        if (!authorList.isEmpty()) {
            authorTextView.setText(authorList);
        } else {
            authorTextView.setText(R.string.no_author);
        }
        //Formats and sets data for the published text in TomeResultActivity
        publishInfo.setText(Html.fromHtml("Published:<br/>" + currentInfo.getmPublisher() + "<br/>" + currentInfo.getmPublisherDate()));
        //Gets random number from TomeInfo to use in the Switch
        int randomColorPalletID = currentInfo.getmRandom();
        //Color Ids for noted segments of the selected tome results page.
        int mainSpineColor;
        int titleTextColor;
        int byAuthorTextColor;
        int publisherTextColor;
        int publisherBGColor;
        //Switches color pallet to randomize the display of the selected tome results page.
        switch (randomColorPalletID) {
            case 0:
            case 1:
                mainSpineColor = R.color.pallet1L;
                titleTextColor = R.color.pallet1MD;
                byAuthorTextColor = R.color.pallet1M;
                publisherTextColor = R.color.pallet1ML;
                publisherBGColor = R.color.pallet1D;
                break;
            case 2:
                mainSpineColor = R.color.pallet2D;
                titleTextColor = R.color.pallet2ML;
                byAuthorTextColor = R.color.pallet2MD;
                publisherTextColor = R.color.pallet2M;
                publisherBGColor = R.color.pallet2L;
                break;
            case 3:
                mainSpineColor = R.color.pallet3ML;
                titleTextColor = R.color.pallet3MD;
                byAuthorTextColor = R.color.pallet3D;
                publisherTextColor = R.color.pallet3L;
                publisherBGColor = R.color.pallet3M;
                break;
            case 4:
                mainSpineColor = R.color.pallet4M;
                titleTextColor = R.color.pallet4MD;
                byAuthorTextColor = R.color.pallet4ML;
                publisherTextColor = R.color.pallet4D;
                publisherBGColor = R.color.pallet4L;
                break;
            case 5:
                mainSpineColor = R.color.pallet5ML;
                titleTextColor = R.color.pallet5M;
                byAuthorTextColor = R.color.pallet5MD;
                publisherTextColor = R.color.pallet5L;
                publisherBGColor = R.color.pallet5D;
                break;
            case 6:
                mainSpineColor = R.color.pallet6L;
                titleTextColor = R.color.pallet6M;
                byAuthorTextColor = R.color.pallet6MD;
                publisherTextColor = R.color.pallet6ML;
                publisherBGColor = R.color.pallet6D;
                break;
            case 7:
                mainSpineColor = R.color.pallet7ML;
                titleTextColor = R.color.pallet7M;
                byAuthorTextColor = R.color.pallet7MD;
                publisherTextColor = R.color.pallet7L;
                publisherBGColor = R.color.pallet7D;
                break;
            case 8:
                mainSpineColor = R.color.pallet8D;
                titleTextColor = R.color.pallet8MD;
                byAuthorTextColor = R.color.pallet8ML;
                publisherTextColor = R.color.pallet8L;
                publisherBGColor = R.color.pallet8M;
                break;
            case 9:
                mainSpineColor = R.color.pallet9L;
                titleTextColor = R.color.pallet9MD;
                byAuthorTextColor = R.color.pallet9M;
                publisherTextColor = R.color.pallet9ML;
                publisherBGColor = R.color.pallet9D;
                break;
            default:
                mainSpineColor = R.color.pallet10D;
                titleTextColor = R.color.pallet10M;
                byAuthorTextColor = R.color.pallet10ML;
                publisherTextColor = R.color.pallet10L;
                publisherBGColor = R.color.pallet10MD;
                break;
        }
        //Implements color pallet that is selected by the switch.
        publishInfo.setTextColor(ContextCompat.getColor(getContext(),
                publisherTextColor));
        titleTextView.setTextColor(ContextCompat.getColor(getContext(),
                titleTextColor));
        byLine.setTextColor(ContextCompat.getColor(getContext(),
                byAuthorTextColor));
        authorTextView.setTextColor(ContextCompat.getColor(getContext(), byAuthorTextColor));
        publisherBG.setBackgroundColor(ContextCompat.getColor(getContext(), publisherBGColor));
        topLine.setBackgroundColor(ContextCompat.getColor(getContext(), publisherBGColor));
        mainContainter.setBackgroundColor(ContextCompat.getColor(getContext(), mainSpineColor));
        return listItemView;
    }
}
