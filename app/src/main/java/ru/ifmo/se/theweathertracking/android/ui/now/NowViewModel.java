package ru.ifmo.se.theweathertracking.android.ui.now;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("The weather now");
    }

    public LiveData<String> getText() {
        return mText;
    }
}