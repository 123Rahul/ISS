package com.ambeyindustry.iss;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LocationFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    MapView mMapView;
    private GoogleMap googleMap;
    private MarkerOptions markerOps;
    private Marker marker;

    public LocationFragment() {
    }

    public static LocationFragment newInstance(int sectionNumber) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {
                googleMap = gMap;

                LatLng sydney = new LatLng(-34, 151);
                if (marker != null) {
                    marker.remove();
                }
                markerOps = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.satellite));
                marker = googleMap.addMarker(markerOps.position(sydney).title("Marker Title").snippet("Marker Description"));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(4).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                callAsynchronousTask();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private class MyTask extends AsyncTask<Void, Integer, IssLocation> {
        @Override
        protected IssLocation doInBackground(Void... voids) {
            IssLocation location = HttpUtil.getIssLocation();
            return location;
        }

        @Override
        protected void onPostExecute(IssLocation location) {
            LatLng sydney = new LatLng(location.getLat(), location.getLng());
            if (marker != null) {
                marker.remove();
            }
            markerOps = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.satellite));
            marker = googleMap.addMarker(markerOps.position(sydney).title("Marker Title").snippet("Marker Description"));

            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(4).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            MyTask performBackgroundTask = new MyTask();
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000);
    }
}
