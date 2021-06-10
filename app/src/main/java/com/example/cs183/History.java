package com.example.cs183;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class History extends AppCompatActivity {

    private Button update ;
    private Button test1 ;
    private Button test2 ;

    private TextView textView ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SetButton();
        SetTextView();
    }

    public void SetButton(){
        update = (Button) findViewById(R.id.HS_BT_Updata) ;
        test1 = (Button) findViewById(R.id.HS_BT_TEST1) ;
        test2 = (Button) findViewById(R.id.HS_BT_TEST2) ;

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(History.this , "更新数据，未完成，等待数据库模块",Toast.LENGTH_SHORT).show();
            }
        });

        test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(History.this , "返回上级",Toast.LENGTH_SHORT).show();
                Intent intent = null ;
                intent = new Intent(History.this,MainActivity.class);
                startActivity(intent);
            }
        });

        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(History.this , "清除所有历史记录,未开发",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  void  SetTextView(){
        textView  = (TextView) findViewById(R.id.HS_TV_1);
    }
}
