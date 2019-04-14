package com.example.learningplatform.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.learningplatform.R;

public class AddFragment extends Fragment {

    private TextView mTextView;


    public AddFragment() {}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment,container,false);
        mTextView = (TextView)view.findViewById(R.id.txt_content);
        mTextView.setText(getArguments().getString("name"));// 将获取到的返回值放在setText内部
        return view;
    }

}


