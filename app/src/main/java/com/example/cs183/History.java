package com.example.cs183;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    //数据
    private SQLiteDatabase db;
    String[] all = new String[100];
    double[] readLati=new double[100];
    double[] readLongti=new double[100];






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
                Toast.makeText(History.this , "尝试更新数据",Toast.LENGTH_SHORT).show();
                //--------------------
                //数据库更新数据的方法，把数据展示在这一块
                readDatabase() ;
                readLatitude() ;
                readLongitude() ;
                Toast.makeText(History.this , "Finish",Toast.LENGTH_SHORT).show();

                textView.setText("测试" + all[0] +"|"+ readLati[0]+"|" +readLongti[0]);
                //--------------------
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
                //--------------------
                //数据库清除数据的方法
                //--------------------
            }
        });
    }

    public  void  SetTextView(){
        textView  = (TextView) findViewById(R.id.HS_TV_1);
    }

    public void FillArray(){
        for(int i = 0 ; i < 100 ; i ++){
            all[i] = "空" ;
            readLati[i] = 1.1 ;
            readLongti[i] = 2.2 ;
        }
    }

    public String[] readDatabase() {

        Cursor c = db.rawQuery("SELECT * FROM " + "HoleDatabase1", null);

        //创一个容量为100的数组以保存所有数据

        //一行一行读取数据
        for (int i = 0; i < all.length; i++) {
            if (c.moveToFirst()) {
                String str1, str2, str3, str4, str5;//分别代表每个属性
                String str;//
                str1 = c.getString(0);
                str2 = c.getString(1);
                str3 = c.getString(2);
                str4 = c.getString(3);
                str5 = c.getString(4);
                str = str1 + str2 + str3 + str4 + str5;

                all[i] = str;
            }
        }

        return all;
    }

    //单独分别读取纬度 latitude
    public double[] readLatitude(){
        Cursor c = db.rawQuery("SELECT * FROM " + "HoleDatabase1", null);
        //一行一行读取数据
        for (int i = 0; i < readLati.length; i++) {
            if (c.moveToFirst()) {
                readLati[i] = c.getDouble(3);
            }
        }
        return readLati;

    }

    //单独分别读取纬度 Longitude
    public double[] readLongitude(){
        Cursor c = db.rawQuery("SELECT * FROM " + "HoleDatabase1", null);


        //一行一行读取数据
        for (int i = 0; i < readLongti.length; i++) {
            if (c.moveToFirst()) {
                readLongti[i] = c.getDouble(4);
            }
        }

        return readLongti;

    }
}
