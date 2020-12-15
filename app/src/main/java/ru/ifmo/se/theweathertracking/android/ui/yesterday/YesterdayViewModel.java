package ru.ifmo.se.theweathertracking.android.ui.yesterday;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class YesterdayViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public YesterdayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("The weather yesterday");
    }

    public LiveData<String> getText() {
        return mText;
    }
}