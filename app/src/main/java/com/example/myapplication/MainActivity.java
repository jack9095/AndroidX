package com.example.myapplication;

import android.os.Bundle;

import com.example.myapplication.app.LogUtil;
import com.example.myapplication.viewmodel.MainViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication());

        // Activity 中创建 ViewModel
//        MainViewModel activityViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // fragment 中创建 ViewModel
//        MainViewModel fragmentViewModel = ViewModelProviders.of(fragment).get(MainViewModel.class);

        /**
         * 在任何地方创建使用 ViewModel：
         * 将 context 强转成 FragmentActivity 就行
         */
//        MainViewModel mMainViewModel = ViewModelProviders.of((FragmentActivity) context).get(MainViewModel.class);

        // 第一步:根据 Activity 或者 Fragment 获得 ViewModelProvider
        ViewModelProvider viewModelProvider = ViewModelProviders.of(MainActivity.this);

        // 第二步:使用 ViewModelProvider 反射创建需要的 ViewModel
        MainViewModel mainViewModel = viewModelProvider.get(MainViewModel.class);
        mainViewModel.setData();
        mainViewModel.getLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String data) {
                // update ui.
                LogUtil.e("data = " + data);
            }
        });
    }
}
