package com.danex.zeitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class MainApp extends AppCompatActivity {
    protected TextView progressBarText;
    protected ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        Button BackButton = findViewById(R.id.BackToMenu);
        BackButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainApp.this, MainActivity.class));
            }
        });
        Button Start = findViewById(R.id.weekOverviewMondayButton);
        Start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainApp.this, Start.class));
            }
        });
        progressBarText = findViewById(R.id.arbeitsStundenBarText);
        progressBar = findViewById(R.id.progressBar);
        TextView montag = findViewById(R.id.weekOverviewMondayText);
        TextView dienstag = findViewById(R.id.weekOverviewTuesdayText);
        TextView mitwoch = findViewById(R.id.weekOverviewWednesdayText);
        TextView thursday = findViewById(R.id.weekOverviewThursdayText);
        TextView friday = findViewById(R.id.weekOverviewFridayText);
        Date x = convertStringToDate(montag.getText().toString());
        progressBarText.setText(x.toString());
    }

    private Date convertStringToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            return format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
