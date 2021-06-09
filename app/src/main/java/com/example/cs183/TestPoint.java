package com.example.cs183;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.Date;

public class TestPoint extends AppCompatActivity {

    private SensorManager sensorManager ; //创建传感器管理器

    //传感器对应数值
    private   int holeNum  ;
    private  int testMode ;
    private  boolean firsttime = true ;

    float zValue = 0 ;

    private  float holeStart ;
    private  float holeEnd ;
    private  float theArrange;

    private  long theTimerStart = 0 ;
    private  long theTimerEnd = 1500 ;

    //Map
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;

    private boolean isFirstLocate = true;
    private MyLocationConfiguration.LocationMode locationMode;

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

        SetSensor();
        SetButton() ;
        SetTextView();
        SetMap();
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //得到传感器的数值
             zValue = ((event.values[2]));
            //

            //时间计数
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//            Date date = new Date(System.currentTimeMillis());
//            long TimeinLong = date.getTime();
//            theTimecounter.setText(""+TimeinLong);

            //计算坑洞
            FindHole();


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }


    };

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            // 如果是第一次定位
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            if (isFirstLocate) {
                isFirstLocate = false;
                //给地图设置状态
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll));
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.map1);

            // 定位模式 地图SDK支持三种定位模式：NORMAL（普通态）, FOLLOWING（跟随态）, COMPASS（罗盘态）
            locationMode = MyLocationConfiguration.LocationMode.NORMAL;
            // 定位模式、是否开启方向、设置自定义定位图标、精度圈填充颜色以及精度圈边框颜色5个属性（此处只设置了前三个）。
            MyLocationConfiguration mLocationConfiguration = new MyLocationConfiguration(locationMode,true,mCurrentMarker);
            // 使自定义的配置生效
            //   mBaiduMap.setMyLocationConfiguration(mLocationConfiguration);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n经度：" + location.getLatitude());
            stringBuilder.append("\n纬度：" + location.getLongitude());
            stringBuilder.append("\n国家：" + location.getCountry());
            stringBuilder.append("\n城市：" + location.getCity());
            stringBuilder.append("\n区：" + location.getDistrict());
            stringBuilder.append("\n街道：" + location.getStreet());
            stringBuilder.append("\n地址：" + location.getAddrStr());

            //经纬度是Double!

            textView1.setText("经度： " + location.getLatitude());
            textView2.setText("纬度： " + location.getLongitude());

        }
    }

    public  void  SetSensor(){
        //获取传感器管理器
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void FindHole(){
        //得到坑洞计数

        if(testMode == 0 && zValue > 1){
            //如果得到一个下的加速度（代表向下移动了），那么进入模式1
            //If you get a lower acceleration (representing downward movement), then enter mode 1
            testMode = 1 ;
            Date dateStart = new Date(System.currentTimeMillis());
            theTimerStart = dateStart.getTime();
            holeStart = zValue ;
        }

        if(testMode ==1 && zValue < -1){
            //在模式1下，如果得到一个上的加速度（代表向上移动了），那么进入模式2
            //In mode 1, if you get an upward acceleration (representing upward movement), then enter mode 2
            Date dateEnd = new Date(System.currentTimeMillis());
            theTimerEnd = dateEnd.getTime();
            holeEnd = zValue ;
            testMode = 2 ;
        }

        //进行对应的判定，无论怎样都会进入待机模式，模式0，直到模式1被触发
        //Make corresponding judgments, no matter what the result is, it will enter standby mode, mode 0, until trigger mode 1
        if( (theTimerEnd - theTimerStart)>=500 && (theTimerEnd - theTimerStart)<=2000 && testMode ==2){
            holeNum++;
            testMode = 0 ;
        }else if((holeEnd-holeStart)!=0 && testMode ==2){
            testMode = 0 ;
        }
    }

    public void SetMap(){
        //获取地图控件引用
        mMapView = findViewById(R.id.bmapView);
        //获取文本显示控件
        //textView1 = findViewById(R.id.AC1_TV01);
        // 得到地图
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位初始化
        mLocationClient = new LocationClient(this);
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        // 可选，设置地址信息
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        option.setIsNeedLocationDescribe(true);
        //设置locationClientOption
        mLocationClient.setLocOption(option);
        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();

    }

    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();

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
