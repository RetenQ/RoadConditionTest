package com.example.cs183;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestPoint extends AppCompatActivity {

    //Button
    private Button startTest ;
    private Button endTest ;
    private Button back ;

    //TextView
    private TextView textView1 ;
    private TextView textView2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_point);

        SetButton() ;
        SetTextView();
    }

    public void  SetButton(){
        startTest = (Button) findViewById(R.id.TP_BT_startTest);
        endTest = (Button) findViewById(R.id.TP_BT_endTest);
        back = (Button) findViewById(R.id.TP_BT_back);

        //Set Listener
        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestPoint.this,"开始测试！",Toast.LENGTH_SHORT).show();

            }
        });

        endTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestPoint.this,"测试结束",Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestPoint.this,"返回",Toast.LENGTH_SHORT).show();

                Intent intent = null ;
                intent = new Intent(TestPoint.this ,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public  void  SetTextView(){
        textView1 = (TextView) findViewById(R.id.TP_TV_01);
        textView2 = (TextView) findViewById(R.id.TP_TV_02);
    }
}
