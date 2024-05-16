package com.example.lego.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModelDos extends ViewModel {

    private final MutableLiveData<String> mText;

    public GalleryViewModelDos() {
        mText = new MutableLiveData<>();
        mText.setValue("En contruccion");
    }

    public LiveData<String> getText() {
        return mText;
    }
}