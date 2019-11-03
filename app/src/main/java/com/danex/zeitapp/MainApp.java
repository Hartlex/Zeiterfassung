package com.danex.zeitapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class MainApp extends AppCompatActivity {
    public static final String LOG_TAG = MainApp.class.getSimpleName();
    private ZeitMemoDataSource dataSource;
    protected TextView progressBarText;
    protected ProgressBar progressBar;
    protected TextView montagText;
    protected TextView dienstagText;
    protected TextView mittwochText;
    protected TextView donnerstagText;
    protected TextView freitagText;
    protected TextView[] days;
    protected TextView montagDateText;
    protected TextView dienstagDateText;
    protected TextView mittwochDateText;
    protected TextView donnerstagDateText;
    protected TextView freitagDateText;
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
        Button SaveButton = findViewById(R.id.weekOverviewSaveButton);
        SaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveCurrentWeek();
            }
        });

        Button Monday = findViewById(R.id.weekOverviewMondayButton);
        Button Tuesday = findViewById(R.id.weekOverviewTuesdayButton);
        Button Wednesday = findViewById(R.id.weekOverviewWednesdayButton);
        Button Thursday = findViewById(R.id.weekOverviewThursdayButton);
        Button Friday = findViewById(R.id.weekOverviewFridayButton);
        montagDateText = findViewById(R.id.weekOverviewMondayDate);
        dienstagDateText = findViewById(R.id.weekOverviewTuesdayDate);
        mittwochDateText = findViewById(R.id.weekOverviewWednesdayDate);
        donnerstagDateText = findViewById(R.id.weekOverviewThursdayDate);
        freitagDateText = findViewById(R.id.weekOverviewFridayDate);
        OpenTimePickerActivity(Monday,1,getString(R.string.monday));
        OpenTimePickerActivity(Tuesday,2,getString(R.string.tuesday));
        OpenTimePickerActivity(Wednesday,3,getString(R.string.wednesday));
        OpenTimePickerActivity(Thursday,4,getString(R.string.thursday));
        OpenTimePickerActivity(Friday,5,getString(R.string.friday));
        days= new TextView[5];
        progressBarText = findViewById(R.id.arbeitsStundenBarText);
        progressBar = findViewById(R.id.progressBar);
        montagText = findViewById(R.id.weekOverviewMondayText);
        days[0]= montagText;
        dienstagText = findViewById(R.id.weekOverviewTuesdayText);
        days[1]= dienstagText;
        mittwochText = findViewById(R.id.weekOverviewWednesdayText);
        days[2]=mittwochText;
        donnerstagText = findViewById(R.id.weekOverviewThursdayText);
        days[3]=donnerstagText;
        freitagText = findViewById(R.id.weekOverviewFridayText);
        days[4]=freitagText;
        progressBar.setMax(40);
        updateProgressBar(progressBar,days);
    }
    private void updateProgressBar(ProgressBar progressBar,TextView[] days){
        int hours=0;
        for(TextView day:days){
            hours += Integer.parseInt(day.getText().toString().split(":")[0]);
        }
        progressBar.setProgress(hours);
        progressBarText.setText(hours+"/"+progressBar.getMax());
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
        //SwitchCase hat nicht richtig funktioniert
        if(requestCode==1){
            montagText.setText(data.getStringExtra("timeWorked"));
            montagDateText.setText(data.getStringExtra("date"));
        }
        else if(requestCode==2) {
            dienstagText.setText(data.getStringExtra("timeWorked"));
            dienstagDateText.setText(data.getStringExtra("date"));
        }
        else if(requestCode==3) {
            mittwochText.setText(data.getStringExtra("timeWorked"));
            mittwochDateText.setText(data.getStringExtra("date"));
        }
        else if(requestCode==4) {
            donnerstagText.setText(data.getStringExtra("timeWorked"));
            donnerstagDateText.setText(data.getStringExtra("date"));
        }
        else if(requestCode==5) {
            freitagText.setText(data.getStringExtra("timeWorked"));
            freitagDateText.setText(data.getStringExtra("date"));
        }
        updateProgressBar(progressBar,days);
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
    public void saveCurrentWeek(){
        dataSource = new ZeitMemoDataSource(this);
        dataSource.open();
        ZeitMemo monday=  dataSource.createShoppingMemo(montagDateText.getText().toString(),montagText.getText().toString());
        ZeitMemo tuesday= dataSource.createShoppingMemo(dienstagDateText.getText().toString(),dienstagText.getText().toString());
        ZeitMemo wednesday= dataSource.createShoppingMemo(mittwochDateText.getText().toString(),mittwochText.getText().toString());
        ZeitMemo thursday = dataSource.createShoppingMemo(donnerstagDateText.getText().toString(),donnerstagText.getText().toString());
        ZeitMemo friday = dataSource.createShoppingMemo(freitagDateText.getText().toString(),freitagText.getText().toString());
        dataSource.close();

    }

}
