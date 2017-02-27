package com.ambeyindustry.iss;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

public class PeopleFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public PeopleFragment() {
    }

    public static PeopleFragment newInstance(int sectionNumber) {
        PeopleFragment fragment = new PeopleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people, container, false);

        new MyTask().execute();
        return rootView;
    }

    private class MyTask extends AsyncTask<Void, Integer, List<People>> {
        @Override
        protected List<People> doInBackground(Void... voids) {
            List<People> peoples = HttpUtil.getPeopleFromServer();
            return peoples;
        }

        @Override
        protected void onPostExecute(List<People> peoples) {
            super.onPostExecute(peoples);
            TextView textViews[] = new TextView[peoples.size()];
            TextView textViews1[] = new TextView[peoples.size()];
            LinearLayout linearLayouts[] = new LinearLayout[peoples.size()];
            LinearLayout peopleLayout = (LinearLayout) getActivity().findViewById(R.id.peopleLayout);
            peopleLayout.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            for (int i = 0; i < peoples.size(); i++) {
                People people = peoples.get(i);
                textViews[i] = new TextView(getActivity());
                textViews[i].setText(people.getName());
                textViews[i].setTextSize(16);
                textViews1[i] = new TextView(getActivity());
                textViews1[i].setText(people.getCraft());
                textViews1[i].setTextSize(16);
                linearLayouts[i] = new LinearLayout(getActivity());
                linearLayouts[i].setLayoutParams(params);
                linearLayouts[i].setOrientation(LinearLayout.VERTICAL);
                linearLayouts[i].addView(textViews[i]);
                linearLayouts[i].addView(textViews1[i]);
                peopleLayout.addView(linearLayouts[i]);
            }
        }
    }
}
