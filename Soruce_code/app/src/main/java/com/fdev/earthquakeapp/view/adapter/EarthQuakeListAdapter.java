package com.fdev.earthquakeapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.fdev.earthquakeapp.R;
import com.fdev.earthquakeapp.service.model.EarthQuake;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EarthQuakeListAdapter extends RecyclerView.Adapter<EarthQuakeListAdapter.EarthQuakeViewHolder> {
    private List<EarthQuake> earthQuakeList = new ArrayList<>();
    private MutableLiveData<Integer> clicked = new MutableLiveData<>();

    @NonNull
    @Override
    public EarthQuakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.info_window, parent, false);
        return new EarthQuakeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthQuakeViewHolder holder, int position) {
        EarthQuake earthQuake = earthQuakeList.get(position);
        holder.mTextViewTitle.setText(earthQuake.getPlace());

        String dateFormat = new SimpleDateFormat("MMM dd,YYYY (hh:MM)").format(new Date(earthQuake.getTime()));

        String snippet = "Magnitude: " + earthQuake.getMagnitude() +"\n"
                + "Date : " + dateFormat;

        holder.mTextViewMagnitude.setText(snippet);

    }

    @Override
    public int getItemCount() {
        return earthQuakeList.size();
    }

    public void setEarthQuakeList(List<EarthQuake> earthQuakeList){
        this.earthQuakeList = earthQuakeList;
        notifyDataSetChanged();
    }

    public class EarthQuakeViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewMagnitude;
        private TextView mTextViewTitle;

        public EarthQuakeViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.tv_title);
            mTextViewMagnitude = itemView.findViewById(R.id.tv_magnitude);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked.setValue(getAdapterPosition());
                }
            });

        }
    }

    public MutableLiveData<Integer> getClicked(){
        return clicked;
    }
}
