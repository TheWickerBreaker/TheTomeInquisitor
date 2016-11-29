package com.thewickerbreaker.thetomeinquisitor;

import java.util.Random;

class TomeInfo {
    private String mTitle;
    private String mAuthor;
    private String mPublisher;
    private String mPublisherDate;
    private String mCoverImage;
    private String mSubtitle;
    private String mDescription;
    private String mGenre;
    private String mPageCount;
    private String mDownload;
    private int mRandom;

    //Collects data received from the JSON file that's sorted out via QueryUtilis
    TomeInfo(String title, String author, String publisher,
             String publisherDate, String coverImage, String subtitle, String description,
             String genre, String pageCount, String download) {
        mTitle = title;
        mAuthor = author;
        mPublisher = publisher;
        mPublisherDate = publisherDate;
        mCoverImage = coverImage;
        mSubtitle = subtitle;
        mDescription = description;
        mGenre = genre;
        mPageCount = pageCount;
        mDownload = download;
    }

    //Getters to make the above information available in other activities
    String getmTitle() {
        return mTitle;
    }

    String getmAuthor() {
        return mAuthor;
    }

    String getmPublisher() {
        return mPublisher;
    }

    String getmPublisherDate() {
        return mPublisherDate;
    }

    String getmCoverImage() {
        return mCoverImage;
    }

    String getmSubtitle() {
        return mSubtitle;
    }

    String getmDescription() {
        return mDescription;
    }

    String getmGenre() {
        return mGenre;
    }

    String getmPageCount() {
        return mPageCount;
    }

    String getmDownload() {
        return mDownload;
    }

    int getmRandom() {
        //create a random number between 0 and 9 to be used in Switches in other activities.
        Random random = new Random();
        int randomNumber = random.nextInt(10);
        while (randomNumber == mRandom) {
            randomNumber = random.nextInt(10);
        }
        mRandom = randomNumber;
        return mRandom;
    }
}
