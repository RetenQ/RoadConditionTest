package com.example.cs183;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CheckData extends AppCompatActivity {
    private TextView textView1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_data);

        Intent intent = getIntent();
        String text = intent.getStringExtra("DATA") ;

        textView1 = (TextView) findViewById(R.id.CD_TV1) ;

        textView1.setText(text);
    }
}
