package com.example.learningplatform.fragment;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.learningplatform.R;

public class HomeFragment extends Fragment {

    private EditText searchEditText;


    public HomeFragment() {}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置搜索图片大小
        searchEditText = view.findViewById(R.id.search);
        Drawable drawableSearch = getResources().getDrawable(R.drawable.icon_search);
        drawableSearch.setBounds(0, 0, 50, 50);
        searchEditText.setCompoundDrawables(null, null, drawableSearch, null);

    }
}


