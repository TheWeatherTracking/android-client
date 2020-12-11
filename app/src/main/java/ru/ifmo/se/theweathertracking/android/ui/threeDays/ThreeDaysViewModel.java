package ru.ifmo.se.theweathertracking.android.ui.threeDays;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ThreeDaysViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ThreeDaysViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("The weather last three days");
    }

    public LiveData<String> getText() {
        return mText;
    }
}