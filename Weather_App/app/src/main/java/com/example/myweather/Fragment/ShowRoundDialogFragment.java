package com.example.myweather.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myweather.Activities.RoundedBottomSheet;
import com.example.myweather.R;

public class ShowRoundDialogFragment extends RoundedBottomSheet {
    public TextView refreshTextView, graphTextView, locationTextView, settingsTextView, shareTextView, aboutTextView;
    private CheckRefreshClickListener mCheckGraphListener;
    private CheckRefreshClickListener mCheckAutoDetectListener;
    private CheckRefreshClickListener mCheckSettingsListener;
    private CheckRefreshClickListener mCheckAboutListener;
    private CheckRefreshClickListener mCheckShareListener;
    private CheckRefreshClickListener mCheckRefresh;

    public static ShowRoundDialogFragment newInstance() {
        return new ShowRoundDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_show_setting_dialog, container,
                false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCheckGraphListener = (CheckRefreshClickListener) context;
        mCheckSettingsListener = (CheckRefreshClickListener) context;
        mCheckAboutListener = (CheckRefreshClickListener) context;
        mCheckShareListener = (CheckRefreshClickListener) context;
        mCheckAutoDetectListener = (CheckRefreshClickListener) context;
        mCheckRefresh = (CheckRefreshClickListener) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        graphTextView = getView().findViewById(R.id.graph);
        locationTextView = getView().findViewById(R.id.locationup);
        settingsTextView = getView().findViewById(R.id.settings);
        aboutTextView = getView().findViewById(R.id.about);
        shareTextView = getView().findViewById(R.id.share);
        refreshTextView = getView().findViewById(R.id.refresh);
        graphTextView.setOnClickListener(v -> mCheckGraphListener.onGraphClick());
        locationTextView.setOnClickListener(v -> mCheckAutoDetectListener.onUpdateClick());
        settingsTextView.setOnClickListener(v -> mCheckSettingsListener.onSettingsClick());
        aboutTextView.setOnClickListener(v -> mCheckAboutListener.onAboutClick());
        shareTextView.setOnClickListener(v -> mCheckShareListener.onShareClick());
        refreshTextView.setOnClickListener(v->mCheckRefresh.onRefresh());
        super.onViewCreated(view, savedInstanceState);
    }
}

