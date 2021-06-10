package com.example.cs183;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CheckData extends AppCompatActivity {

    private Button SaveAndBack ;
    private Button Nosave;

    private TextView textView1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_data);

        Intent intent = getIntent();
        String text = intent.getStringExtra("DATA") ;

        SetButton();
        SetTextView(text);


    }

    public void  SetButton(){
        SaveAndBack = (Button) findViewById(R.id.CD_BT_SAVE);
        Nosave = (Button) findViewById(R.id.CD_BT_BACK);

        SaveAndBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckData.this , "保存并返回",Toast.LENGTH_SHORT).show();
                //--------------------
                //数据库保存数据的方法
                //--------------------
                Intent intent = null ;
                intent = new Intent(CheckData.this , TestPoint.class);
                startActivity(intent);
            }
        });

        Nosave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckData.this , "不保存数据",Toast.LENGTH_SHORT).show();

                Intent intent = null ;
                intent = new Intent(CheckData.this , TestPoint.class);
                startActivity(intent);
            }
        });
    }

    public void  SetTextView(String text){
        textView1 = (TextView) findViewById(R.id.CD_TV_DATA );
        textView1.setText(text);
    }
}
