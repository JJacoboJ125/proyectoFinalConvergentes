package com.example.lego.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideshowViewModelC extends ViewModel {

    private final MutableLiveData<String> mText;

    public SlideshowViewModelC() {
        mText = new MutableLiveData<>();
        mText.setValue("En contruccion");
    }

    public LiveData<String> getText() {
        return mText;
    }
}