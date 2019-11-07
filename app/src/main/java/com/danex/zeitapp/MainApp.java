package com.danex.zeitapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


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
    private LineChart chart;

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
        chart = findViewById(R.id.lineChart);
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.setBackgroundColor(Color.rgb(104, 241, 175));

        // no description text
        chart.getDescription().setEnabled(false);
        // enable touch gestures
        chart.setTouchEnabled(true);
        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(true);
        XAxis x = chart.getXAxis();
        x.setAxisMaximum(30);
        x.setAxisMinimum(0);
        x.setEnabled(true);
        x.setLabelCount(6,false);
        x.setTextColor(Color.BLACK);
        x.setDrawGridLines(true);
        x.setAxisLineColor(Color.BLACK);
        YAxis y = chart.getAxisLeft();
        y.setAxisMaximum(15);
        y.setAxisMinimum(0);
        y.setLabelCount(6, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(true);
        y.setAxisLineColor(Color.BLACK);

        chart.getAxisRight().setEnabled(false);
        chart.animateXY(2000, 2000);

        // don't forget to refresh the drawing
        chart.invalidate();
        setData();
    }
    private void setData(){
        ZeitMemoDataSource source= new ZeitMemoDataSource(this);
        source.open();
        List<ZeitMemo> list= source.getAllZeitMemos();
        ArrayList<Entry> values = new ArrayList<>();
        float xValue=1f;

        for(ZeitMemo memo: list){
            float time = Float.parseFloat(memo.getTimeWorked().split(":")[0]);
            time += Float.parseFloat(memo.getTimeWorked().split(":")[1])/60;
            values.add(new Entry(xValue,time));
            xValue++;
        }
        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Worked Time");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(0xFFC61958);
            set1.setFillColor(0xFFC61958);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // create a data object with the data sets
            LineData data = new LineData(set1);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
        }

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
            //hours += Integer.parseInt(day.getText().toString().split(":")[0]);
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
        setData();
        dataSource.close();

    }

}
