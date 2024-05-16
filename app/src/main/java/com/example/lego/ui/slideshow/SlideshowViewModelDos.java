package com.example.lego.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModelDos extends ViewModel {

    private final MutableLiveData<String> mText;

    public SlideshowViewModelDos() {
        mText = new MutableLiveData<>();
        mText.setValue("En contruccion");
    }

    public LiveData<String> getText() {
        return mText;
    }
}