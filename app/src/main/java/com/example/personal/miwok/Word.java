package com.example.personal.miwok;

class Word {

    private String mMiwokTranslation;
    private String mDefaultTranslation;
    private int mImageResourceID;
    private int audioResourceID;

    private int IMAGE_SOURCE_AVAILABLE = -1;

    Word(String mMiwokTranslation, String mDefaultTranslation, int mImageResourceID, int audioResourceID) {
        this.mMiwokTranslation = mMiwokTranslation;
        this.mDefaultTranslation = mDefaultTranslation;
        this.mImageResourceID = mImageResourceID;
        IMAGE_SOURCE_AVAILABLE = 1;
        this.audioResourceID = audioResourceID;
    }

    Word(String mMiwokTranslation, String mDefaultTranslation, int audioResourceID) {
        this.mDefaultTranslation = mDefaultTranslation;
        this.mMiwokTranslation = mMiwokTranslation;
        this.audioResourceID = audioResourceID;
    }

    String getmMiwokTranslation() {
        return mMiwokTranslation;
    }

    String getmDefaultTranslation() {
        return mDefaultTranslation;
    }

    int getmImageResourceID() {
        return mImageResourceID;
    }

    boolean isImageAvailable() {
        return IMAGE_SOURCE_AVAILABLE != -1;
    }

    int getAudioResourceID() { return audioResourceID; }
}
