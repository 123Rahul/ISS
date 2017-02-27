package com.ambeyindustry.iss;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.gms.vision.text.Line;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

public class PredictionsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private double lat, lng;

    public PredictionsFragment() {
    }

    public static PredictionsFragment newInstance(int sectionNumber) {
        PredictionsFragment fragment = new PredictionsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_predictions, container, false);
        ImageView submit = (ImageView) rootView.findViewById(R.id.search);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = ((EditText) getActivity().findViewById(R.id.location)).getText().toString();
                if (location != null && location != "") {
                    Geocoder geocoder = new Geocoder(getActivity());
                    Address address;
                    try {
                        address = geocoder.getFromLocationName(location, 1).get(0);
                        lat = address.getLatitude();
                        lng = address.getLongitude();
                    } catch (Exception e) {
                        lat = 22.0;
                        lng = 77.0;
                        e.printStackTrace();
                    }
                } else {
                    lat = 22.0;
                    lng = 77.0;
                }
                new MyTask().execute();
            }
        });
        return rootView;
    }

    private class MyTask extends AsyncTask<Void, Integer, List<Predictions>> {
        @Override
        protected List<Predictions> doInBackground(Void... voids) {
            List<Predictions> predictions = HttpUtil.getPredictionsFromServer(lat, lng);
            return predictions;
        }

        @Override
        protected void onPostExecute(List<Predictions> predictions) {
            super.onPostExecute(predictions);
            TextView textViews[] = new TextView[predictions.size()];
            TextView textViews1[] = new TextView[predictions.size()];
            LinearLayout linearLayouts[] = new LinearLayout[predictions.size()];
            LinearLayout predictionLayout = (LinearLayout) getActivity().findViewById(R.id.predictionLayout);
            predictionLayout.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            for (int i = 0; i < predictions.size(); i++) {
                Predictions prediction = predictions.get(i);
                String date = prediction.getDate().split("-")[0];
                String time = prediction.getDate().split("-")[1];
                textViews[i] = new TextView(getActivity());
                textViews[i].setText(date);
                textViews[i].setTextSize(16);
                textViews1[i] = new TextView(getActivity());
                textViews1[i].setText(time);
                textViews1[i].setTextSize(16);
                linearLayouts[i] = new LinearLayout(getActivity());
                linearLayouts[i].setLayoutParams(params);
                linearLayouts[i].setOrientation(LinearLayout.VERTICAL);
                linearLayouts[i].addView(textViews[i]);
                linearLayouts[i].addView(textViews1[i]);
                predictionLayout.addView(linearLayouts[i]);
            }
        }
    }
}
