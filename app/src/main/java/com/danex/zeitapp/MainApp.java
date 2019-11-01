package com.danex.zeitapp;

import androidx.annotation.Nullable;
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
    protected TextView montagText;
    protected TextView dienstagText;
    protected TextView mittwochText;
    protected TextView donnerstagText;
    protected TextView freitagText;
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
        Button Monday = findViewById(R.id.weekOverviewMondayButton);
        Button Tuesday = findViewById(R.id.weekOverviewTuesdayButton);
        Button Wednesday = findViewById(R.id.weekOverviewWednesdayButton);
        Button Thursday = findViewById(R.id.weekOverviewThursdayButton);
        Button Friday = findViewById(R.id.weekOverviewFridayButton);
        OpenTimePickerActivity(Monday,1,getString(R.string.monday));
        OpenTimePickerActivity(Tuesday,2,getString(R.string.tuesday));
        OpenTimePickerActivity(Wednesday,3,getString(R.string.wednesday));
        OpenTimePickerActivity(Thursday,4,getString(R.string.thursday));
        OpenTimePickerActivity(Friday,5,getString(R.string.friday));
        progressBarText = findViewById(R.id.arbeitsStundenBarText);
        progressBar = findViewById(R.id.progressBar);
        montagText = findViewById(R.id.weekOverviewMondayText);
        dienstagText = findViewById(R.id.weekOverviewTuesdayText);
        mittwochText = findViewById(R.id.weekOverviewWednesdayText);
        donnerstagText = findViewById(R.id.weekOverviewThursdayText);
        freitagText = findViewById(R.id.weekOverviewFridayText);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**switch (requestCode){
            case 1:
                montagText.setText(data.getStringExtra("timeWorked"));
            case 2:
                dienstagText.setText(data.getStringExtra("timeWorked"));
            default:
                progressBarText.setText("Hallo");
         }
         **/
        if(requestCode==1)montagText.setText(data.getStringExtra("timeWorked"));
        else if(requestCode==2) dienstagText.setText(data.getStringExtra("timeWorked"));
        else if(requestCode==3) mittwochText.setText(data.getStringExtra("timeWorked"));
        else if(requestCode==4) donnerstagText.setText(data.getStringExtra("timeWorked"));
        else if(requestCode==5) freitagText.setText(data.getStringExtra("timeWorked"));



    }

    private void OpenTimePickerActivity(Button btn, final int requestCode, final String message){
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainApp.this, Start.class);
                String msg = message;
                intent.putExtra("tag", msg);
                startActivityForResult(intent, requestCode);
            }
        });
    }

}
