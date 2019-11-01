package com.danex.zeitapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Start extends Activity implements View.OnClickListener {
    protected Button zuruck=null;
    protected Button weiter=null;
    protected TextView tag=null;
    protected EditText anfang=null;
    protected EditText ende=null;
    protected TextView datum=null;
    private static final String TAG="Start";
    private TextView DisplayDate=null;
    private DatePickerDialog.OnDateSetListener DateSetListener=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent intent = getIntent();
        String message = intent.getStringExtra("tag");
        tag = findViewById(R.id.tag);
        tag.setText(message);
        DisplayDate=findViewById(R.id.date);
        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal= Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog= new DatePickerDialog(Start.this,
                        android.R.style.Theme_Black,
                DateSetListener,
                year,month,day );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        DateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month +1;
                Log.d(TAG, "onDateSet: dd/mm/yyy" + day +"/" + month +"/" + year);
                String date= day +"/"+month+"/"+year;
                DisplayDate.setText(date);
            }
        };

        Spinner spinner=(Spinner) findViewById(R.id.stundenplan);

        ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(Start.this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.name));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        weiter=findViewById(R.id.weiter);
        anfang=findViewById(R.id.anfang);
        ende=findViewById(R.id.ende);
        datum=findViewById(R.id.date);
        weiter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent=new Intent();
        intent.putExtra("date",datum.getText().toString());
        intent.putExtra("anfang",anfang.getText().toString());
        intent.putExtra("ende",ende.getText().toString());
        intent.putExtra("timeWorked",getTimeWorked(anfang.getText().toString(),ende.getText().toString()));
        setResult(RESULT_OK,intent);

        finish();

    }
    private String getTimeWorked(String anfang,String ende){
        String[] strs = new String[2];
        strs[0]=anfang;
        strs[1]=ende;
        if(!checkIfStringsAreCorrect(strs))
            return getString(R.string.ErrorWrongTimeFormat);
        Date date1=convertStringToDate(anfang);
        Date date2=convertStringToDate(ende);
        long timeDiff = date2.getTime()-date1.getTime();
        Date date = new Date();
        date.setTime(timeDiff);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);

    }

    private boolean checkIfStringsAreCorrect(String[] strings) {
        for(String str:strings){
            if(str.matches("\\d{2}:\\d{2}"));
            else return false;
        }
        return true;
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
