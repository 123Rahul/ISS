package com.ambeyindustry.iss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openMapActivity(View view) {
        Intent info = new Intent(this, InfoActivity.class);
        info.putExtra("id","1");
        startActivity(info);
    }

    public void openPredictionsActivity(View view) {
        Intent info = new Intent(this, InfoActivity.class);
        info.putExtra("id","2");
        startActivity(info);
    }

    public void openPeopleActivity(View view) {
        Intent info = new Intent(this, InfoActivity.class);
        info.putExtra("id","3");
        startActivity(info);
    }
}
