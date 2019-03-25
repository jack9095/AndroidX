package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by fei.wang on 2019/3/24.
 */
public class MainViewModel extends ViewModel {

    private final MutableLiveData<String> mainLiveData = new MutableLiveData<>();

    public void setData(){
        mainLiveData.setValue("asdefr");
    }

    public LiveData<String> getLiveData() {
        return mainLiveData;
    }
}
