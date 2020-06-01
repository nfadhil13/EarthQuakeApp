package com.fdev.earthquakeapp.view.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fdev.earthquakeapp.R;
import com.fdev.earthquakeapp.service.model.EarthQuake;
import com.fdev.earthquakeapp.util.EarthQuakeConstants;
import com.fdev.earthquakeapp.view.adapter.EarthQuakeInfoWindow;
import com.fdev.earthquakeapp.view.adapter.EarthQuakeListAdapter;
import com.fdev.earthquakeapp.viewmodel.EarthQuakeViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,
        GoogleMap.OnInfoWindowClickListener , GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private EarthQuakeViewModel mEarthQuakeViewModel;
    private List<EarthQuake> mEarthQuakeList;
    private AlertDialog mDiaglog;
    private AlertDialog.Builder mDialogBuilder;

    private ImageButton mPopUpButtonDismissTop;
    private Button mPopUpButtonDismissBot;
    private TextView mPopUpDetailTextView;
    private WebView mPopUpWebview;
    private ProgressBar mPopUpProgressbar;

    private ProgressBar mMapProgressBar;
    private Button mButtonShowList;
    private TextView mTextViewLoading;

    private RecyclerView mListRecylerView;
    private EarthQuakeListAdapter mEQAdapter;

    private BitmapDescriptor[] iconColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        iconColors = new BitmapDescriptor[] {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
                // BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)

        };

        mMapProgressBar = findViewById(R.id.proggress_bar_map);
        mMapProgressBar.setIndeterminate(true);

        mTextViewLoading = findViewById(R.id.tv_loading);


        mEQAdapter = new EarthQuakeListAdapter();


        mButtonShowList = findViewById(R.id.btn_showList);
        mButtonShowList.setVisibility(View.INVISIBLE);
        mButtonShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListDialog();
            }
        });


        mEarthQuakeViewModel = new ViewModelProvider(this).get(EarthQuakeViewModel.class);
        mMapProgressBar.setVisibility(View.VISIBLE);
        mTextViewLoading.setVisibility(View.VISIBLE);
        mEarthQuakeViewModel.getCityDetail().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mPopUpDetailTextView.setVisibility(View.VISIBLE);
                mPopUpProgressbar.setVisibility(View.INVISIBLE);
                mTextViewLoading.setVisibility(View.INVISIBLE);
                mButtonShowList.setVisibility(View.VISIBLE);
                if(!s.equals("")){
                    mPopUpDetailTextView.setText(s);
                }else{
                    mPopUpDetailTextView.setText("There is no city list yet \n comeback later");
                }

            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new EarthQuakeInfoWindow(this));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mEarthQuakeViewModel.getAllEarthQuakes().observe(this, new Observer<List<EarthQuake>>() {
            @Override
            public void onChanged(List<EarthQuake> earthQuakes) {
                mEarthQuakeList = earthQuakes;
                if(mMap!= null){
                    mMap.clear();
                    initMarker();
                }
                mEQAdapter.setEarthQuakeList(earthQuakes);
            }
        });
    }

    private void initMarker(){
        for(EarthQuake earthQuake :mEarthQuakeList){
            MarkerOptions markerOptions = new MarkerOptions();
            int index = EarthQuakeConstants.randomInt(iconColors.length , 0);
            markerOptions.icon(iconColors[index]);
            markerOptions.position(earthQuake.getLatLng());
            markerOptions.title(earthQuake.getPlace());

            String dateFormat = new SimpleDateFormat("MMM dd,YYYY (hh:MM)").format(new Date(earthQuake.getTime()));

            String snippet = "Magnitude: " + earthQuake.getMagnitude() +"\n"
                            + "Date : " + dateFormat;
            markerOptions.snippet(snippet);

            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(earthQuake.getDetailLink());


        }
        if(!mEarthQuakeList.isEmpty()){
            int lastIndex = mEarthQuakeList.size()-1;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mEarthQuakeList.get(lastIndex).getLatLng(),10));
            mMapProgressBar.setVisibility(View.INVISIBLE);
            mTextViewLoading.setVisibility(View.INVISIBLE);
            mButtonShowList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
       mEarthQuakeViewModel.setCityDetail(marker.getTag().toString());
       showDetailDialog();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private void showDetailDialog(){
        mDialogBuilder = new AlertDialog.Builder(MapsActivity.this);
        View view = getLayoutInflater().inflate(R.layout.popup_earthquake_detail,null);

        mPopUpButtonDismissTop = view.findViewById(R.id.btn_close_top);
        mPopUpButtonDismissBot = view.findViewById(R.id.btn_close_bottom);
        mPopUpDetailTextView = view.findViewById(R.id.tv_city);
        mPopUpWebview = view.findViewById(R.id.web_view_content);
        mPopUpProgressbar = view.findViewById(R.id.proggress_bar_popup);

        mPopUpDetailTextView.setVisibility(View.INVISIBLE);
        mPopUpProgressbar.setVisibility(View.VISIBLE);

        mPopUpButtonDismissTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiaglog.dismiss();
            }
        });

        mPopUpButtonDismissBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiaglog.dismiss();
            }
        });

        mDialogBuilder.setView(view);
        mDiaglog = mDialogBuilder.create();
        mDiaglog.show();


    }

    private void showListDialog(){
        mDialogBuilder = new AlertDialog.Builder(MapsActivity.this);
        View view = getLayoutInflater().inflate(R.layout.list_popu,null);

        mListRecylerView = view.findViewById(R.id.recyler_view);
        mListRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mListRecylerView.setHasFixedSize(true);
        mListRecylerView.setAdapter(mEQAdapter);

        mEQAdapter.getClicked().observe(MapsActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mDiaglog.dismiss();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mEarthQuakeList.get(integer).getLatLng(),12));
            }
        });

        mDialogBuilder.setView(view);
        mDiaglog = mDialogBuilder.create();
        mDiaglog.show();
    }
}
