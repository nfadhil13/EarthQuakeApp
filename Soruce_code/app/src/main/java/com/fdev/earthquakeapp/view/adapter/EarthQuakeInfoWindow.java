package com.fdev.earthquakeapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fdev.earthquakeapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class EarthQuakeInfoWindow implements GoogleMap.InfoWindowAdapter {
    private View view;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public EarthQuakeInfoWindow(Context context) {
        mContext = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.info_window,null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView mTextViewTitle = view.findViewById(R.id.tv_title);
        mTextViewTitle.setText(marker.getTitle());

        TextView mTextViewMagnitude = view.findViewById(R.id.tv_magnitude);
        mTextViewMagnitude.setText(marker.getSnippet());
        return view;
    }
}
