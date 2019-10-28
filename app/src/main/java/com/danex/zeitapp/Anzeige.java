package com.danex.zeitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Anzeige extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anzeige);
        Intent intent=getIntent();

    }

    @Override
    public void onClick(View view) {

        Intent intent=new Intent();
        setResult(RESULT_OK,intent);

        finish();
    }
}
