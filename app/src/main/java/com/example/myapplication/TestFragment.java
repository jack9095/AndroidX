package com.example.myapplication;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 测试 Fragment 的 setRetainInstance 方法：
 * https://blog.csdn.net/airk000/article/details/38557605
 */
public class TestFragment extends Fragment {
    private final static String TAG = TestFragment.class.getSimpleName();
    /**
     * setRetainInstance(boolean) 是 Fragment 中的一个方法。将这个方法设置为 true 就可以使当前 Fragment 在 Activity 重建时存活下来,
     * 如果不设置或者设置为 false, 当前 Fragment 会在 Activity 重建时同样发生重建, 以至于被新建的对象所替代。 
     * 在 setRetainInstance(boolean) 为 true 的 Fragment （就是HolderFragment）中放一个专门用于存储 ViewModel 的 Map,
     * 这样 Map 中所有的 ViewModel 都会幸免于 Activity 的配置改变导致的重建，让需要创建 ViewModel 的 Activity,
     * Fragment 都绑定一个这样的 Fragment（就是 HolderFragment）, 将 ViewModel 存放到这个 Fragment 的 Map 中, ViewModel 组件就这样实现了。
     */
    public TestFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG,"*********  onAttach  ********");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"*********  onCreate  ********");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG,"*********  onCreateView  ********");
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG,"*********  onViewCreated  ********");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG,"*********  onActivityCreated  ********");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG,"*********  onStart  ********");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG,"*********  onSaveInstanceState  ********");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"*********  onResume  ********");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG,"*********  onPause  ********");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG,"*********  onStop  ********");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG,"*********  onDestroyView  ********");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"*********  onDestroy  ********");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG,"*********  onDetach  ********");
    }
}
