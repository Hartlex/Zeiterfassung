package com.danex.zeitapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


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

        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);

        Button Monday = findViewById(R.id.weekOverviewMondayButton);
        Button Tuesday = findViewById(R.id.weekOverviewTuesdayButton);
        Button Wednesday = findViewById(R.id.weekOverviewWednesdayButton);
        Button Thursday = findViewById(R.id.weekOverviewThursdayButton);
        Button Friday = findViewById(R.id.weekOverviewFridayButton);

        montagDateText = findViewById(R.id.weekOverviewMondayDate);
        montagDateText.setText(pref.getString(getString(R.string.monday)+"Date","01/01/1111"));
        dienstagDateText = findViewById(R.id.weekOverviewTuesdayDate);
        dienstagDateText.setText(pref.getString(getString(R.string.tuesday)+"Date","01/01/1111"));
        mittwochDateText = findViewById(R.id.weekOverviewWednesdayDate);
        mittwochDateText.setText(pref.getString(getString(R.string.wednesday)+"Date","01/01/1111"));
        donnerstagDateText = findViewById(R.id.weekOverviewThursdayDate);
        donnerstagDateText.setText(pref.getString(getString(R.string.thursday)+"Date","01/01/1111"));
        freitagDateText = findViewById(R.id.weekOverviewFridayDate);
        freitagDateText.setText(pref.getString(getString(R.string.friday)+"Date","01/01/1111"));


        OpenTimePickerActivity(Monday,1,getString(R.string.monday));
        OpenTimePickerActivity(Tuesday,2,getString(R.string.tuesday));
        OpenTimePickerActivity(Wednesday,3,getString(R.string.wednesday));
        OpenTimePickerActivity(Thursday,4,getString(R.string.thursday));
        OpenTimePickerActivity(Friday,5,getString(R.string.friday));

        days= new TextView[5];
        progressBarText = findViewById(R.id.arbeitsStundenBarText);
        progressBar = findViewById(R.id.progressBar);

        montagText = findViewById(R.id.weekOverviewMondayText);
        montagText.setText(pref.getString(getString(R.string.monday),"0:00"));
        days[0]= montagText;
        dienstagText = findViewById(R.id.weekOverviewTuesdayText);
        dienstagText.setText(pref.getString(getString(R.string.tuesday),"0:00"));
        days[1]= dienstagText;
        mittwochText = findViewById(R.id.weekOverviewWednesdayText);
        mittwochText.setText(pref.getString(getString(R.string.wednesday),"0:00"));
        days[2]=mittwochText;
        donnerstagText = findViewById(R.id.weekOverviewThursdayText);
        donnerstagText.setText(pref.getString(getString(R.string.thursday),"0:00"));
        days[3]=donnerstagText;
        freitagText = findViewById(R.id.weekOverviewFridayText);
        freitagText.setText(pref.getString(getString(R.string.friday),"0:00"));
        days[4]=freitagText;
        progressBar.setMax(40);
        updateProgressBar(progressBar,days);
    }
    private void saveTimeInSharedPreferences(TextView[] days){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.monday), days[0].getText().toString());
        editor.putString(getString(R.string.monday)+"Date", montagDateText.getText().toString());
        editor.putString(getString(R.string.tuesday), days[1].getText().toString());
        editor.putString(getString(R.string.tuesday)+"Date", dienstagDateText.getText().toString());
        editor.putString(getString(R.string.wednesday), days[2].getText().toString());
        editor.putString(getString(R.string.wednesday)+"Date", mittwochDateText.getText().toString());
        editor.putString(getString(R.string.thursday), days[3].getText().toString());
        editor.putString(getString(R.string.thursday)+"Date", donnerstagDateText.getText().toString());
        editor.putString(getString(R.string.friday), days[4].getText().toString());
        editor.putString(getString(R.string.friday)+"Date", freitagDateText.getText().toString());
        editor.commit();
    }
    private void updateProgressBar(ProgressBar progressBar,TextView[] days){
        saveTimeInSharedPreferences(days);
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
        switch (requestCode){
            case 1:
                montagText.setText(data.getStringExtra("timeWorked"));
                montagDateText.setText(data.getStringExtra("date"));
                break;
            case 2:
                dienstagText.setText(data.getStringExtra("timeWorked"));
                dienstagDateText.setText(data.getStringExtra("date"));
                break;
            case 3:
                mittwochText.setText(data.getStringExtra("timeWorked"));
                mittwochDateText.setText(data.getStringExtra("date"));
                break;
            case 4:
                donnerstagText.setText(data.getStringExtra("timeWorked"));
                donnerstagDateText.setText(data.getStringExtra("date"));
                break;
            case 5:
                freitagText.setText(data.getStringExtra("timeWorked"));
                freitagDateText.setText(data.getStringExtra("date"));
                break;
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
        ZeitMemo monday=  dataSource.createZeitMemo(montagDateText.getText().toString(),montagText.getText().toString());
        ZeitMemo tuesday= dataSource.createZeitMemo(dienstagDateText.getText().toString(),dienstagText.getText().toString());
        ZeitMemo wednesday= dataSource.createZeitMemo(mittwochDateText.getText().toString(),mittwochText.getText().toString());
        ZeitMemo thursday = dataSource.createZeitMemo(donnerstagDateText.getText().toString(),donnerstagText.getText().toString());
        ZeitMemo friday = dataSource.createZeitMemo(freitagDateText.getText().toString(),freitagText.getText().toString());
        Toast t = Toast.makeText(this,
                       monday.getDatum()+" Zeit: "+monday.getTimeWorked()+" wurde gespeichert"+"\n"+
                            tuesday.getDatum()+" Zeit: "+tuesday.getTimeWorked()+" wurde gespeichert"+"\n"+
                            wednesday.getDatum()+" Zeit: "+wednesday.getTimeWorked()+" wurde gespeichert"+"\n"+
                            thursday.getDatum()+" Zeit: "+thursday.getTimeWorked()+" wurde gespeichert"+"\n"+
                            friday.getDatum()+" Zeit: "+friday.getTimeWorked()+" wurde gespeichert"+"\n"
                ,Toast.LENGTH_LONG);
        t.show();
        dataSource.close();

    }

}
