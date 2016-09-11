package com.pruebaandroid.misterhouse.ui.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pruebaandroid.misterhouse.R;
import com.pruebaandroid.misterhouse.Utils.Utils;
import com.pruebaandroid.misterhouse.ui.Activitys.MainActivity;


public class NoDataNoConectionFragment extends Fragment implements View.OnClickListener{

    Button mBtnTryAgain;
    public NoDataNoConectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_no_data_no_conection, container, false);
        mBtnTryAgain = (Button)rootView.findViewById(R.id.tryagain);
        mBtnTryAgain.setOnClickListener(this);
        return rootView ;
    }

    @Override
    public void onClick(View v) {
        if(Utils.isConnected(getContext()))
        {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }
}
