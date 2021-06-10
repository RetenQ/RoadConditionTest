package com.example.cs183;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

//


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestPoint extends AppCompatActivity {

    private SensorManager sensorManager ; //创建传感器管理器

    //传感器对应数值
    private  boolean istest = false ; //是否在测试
    private  boolean needSave = false ; //是否需要存储
    private   int holeNum  ;
    private  int testMode ;
    private  boolean firsttime = true ;

        //利用数组存储
    private  Hole[] holes = new Hole[150] ;
    private  int currentIndex = 0 ;

    float zValue = 0 ;

    private  float holeStart ;
    private  float holeEnd ;
    private  float theArrange;

    private  long theTimerStart = 0 ;
    private  long theTimerEnd = 1500 ;

    private  double userLatitude ;
    private  double userLongitude ;

    private  double holeLatitude ; //某个坑洞的经度
    private  double holeLongitude ; //某个坑洞的纬度

    //Map
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;

    private boolean isFirstLocate = true;
    private MyLocationConfiguration.LocationMode locationMode;

    List<OverlayOptions> options = new ArrayList<OverlayOptions>();

    private  String showText = " " ;

    //数据库
    private MyDatabaseHelper dbHelper;

    //构建Marker图标
    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.drawable.point);

    LatLng point = new LatLng(0.0,0.0);


    //Button
    private Button startTest ;
    private Button endTest ;
    private Button back ;

    //TextView
    private TextView textView1 ;
    private TextView textView2 ;
    private TextView textView3 ;


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
            if(istest){
                //如果处于测试状态
                FindHole();
            }
            ShowDetail();
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
            locationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
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
            userLatitude = location.getLatitude() ;
            userLongitude = location.getLongitude() ;

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
            //添加坑洞
            holeNum++;
            holeLatitude = userLatitude ;
            holeLongitude = userLongitude ;

            //存储模块--------------------------------------------------------------------------------------------------------------------
            holes[currentIndex] = new Hole(theTimerEnd - theTimerStart , Math.abs(holeStart - holeEnd) , holeLatitude ,holeLongitude);
            holes[currentIndex].RankHole();
            Toast.makeText(TestPoint.this,"存储一个坑洞，编号为" + currentIndex + "  等级为 " +holes[currentIndex].getRank() ,Toast.LENGTH_SHORT).show();
            drawTip(holeLatitude,holeLongitude,bd,""+"测试显示： \n" +holes[currentIndex].getLongitude() + "\n" + holes[currentIndex].getLatitude()+"\n");
            Toast.makeText(TestPoint.this,"成功标记",Toast.LENGTH_SHORT).show();

            currentIndex ++ ;
            if(currentIndex >= 145){
                Toast.makeText(TestPoint.this,"存储即将越界",Toast.LENGTH_SHORT).show();
                if(currentIndex >= 146){
                    currentIndex = 0 ;
                    Toast.makeText(TestPoint.this,"存储位置归零",Toast.LENGTH_SHORT).show();
                }
            }

            //存储模块--------------------------------------------------------------------------------------------------------------------

            //
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
                istest = true ;
                Toast.makeText(TestPoint.this,"开始测试！",Toast.LENGTH_SHORT).show();
                drawTip(26.06374,119.19198,bd,"test1");
                drawTip(userLatitude,userLongitude,bd,"test2");


            }
        });

        endTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                istest = false ;
                if(holes.length >=1 && holes[0] != null){
                    Toast.makeText(TestPoint.this,"测试结束，成功标记了： " + holes[0].getHoleNum() + " 个点",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(TestPoint.this,"没检测到",Toast.LENGTH_SHORT).show();
                }

                //--------------------
                //数据库保存数据的方法，以防万一在这里也做一次数据保存
                Toast.makeText(TestPoint.this,"尝试将数据备份至数据库",Toast.LENGTH_SHORT).show();
                GetHoles();
                Toast.makeText(TestPoint.this,"数据库备份完成！",Toast.LENGTH_SHORT).show();
                //--------------------


                //下面是利用Intent在活动间传递数据的方法，这些数据重启之后就会丢失，请注意------------------------------------------
                Intent intent = new Intent(TestPoint.this,CheckData.class);
                //打包数组数据
                intent.putExtra("DATA",SaveData01());
                startActivity(intent);
                //-----------------------------------------------------------所以这个只是测试一下保存模块----------------------------

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestPoint.this,"返回",Toast.LENGTH_SHORT).show();

                Intent intent = null ;
                intent = new Intent(TestPoint.this ,MainActivity.class);
                intent.putExtra("TEST",true) ;
                startActivity(intent);
            }
        });

    }

    public  void  SetTextView(){
        textView1 = (TextView) findViewById(R.id.TP_TV_01);
        textView2 = (TextView) findViewById(R.id.TP_TV_02);
        textView3 = (TextView) findViewById(R.id.TP_TV_03);
    }

    public  void  ShowDetail(){
//        StringBuilder stringBuilder = new StringBuilder() ;
        //
        if(currentIndex >=1){
//            stringBuilder.append(istest+"||") ;
//            stringBuilder.append(currentIndex+"||");
//            stringBuilder.append(holes[currentIndex].getTime()+"||");
//            stringBuilder.append(holes[currentIndex].getValue()+"||");
            textView3.setText("得到了数据");
        }else{
            textView3.setText("目前暂无数据");
        }

    }

    //标点方法  包含点击该点触发信息的功能
    public void drawTip(double a,double b,BitmapDescriptor bd,String str) {
        point = new LatLng(a,b);
        showText = str ;
        OverlayOptions option1 =  new MarkerOptions()
                .position(point) //根据每个点的经纬度信息，进行定位
                .icon(bd);
        options.add(option1);
        mBaiduMap.addOverlays( options);
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {  //设置地图加载监听
            @Override
            public void onMapLoaded() {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder = builder.include(point);
                LatLngBounds latlngBounds = builder.build();
                //改变地图某些状态需要MapStatusUpdate
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds,mMapView.getWidth(),mMapView.getHeight());
                mBaiduMap.animateMapStatus(u);
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 构建一个需要显示的view，我这里只是一个textview，也可以是其他的布局
                TextView tv = new TextView(TestPoint.this);
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setBackgroundResource(R.drawable.map1);
                tv.setText(showText);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(tv);
                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        //隐藏infowindow
                        mBaiduMap.hideInfoWindow();
                    }
                };
                // －130表示的是y轴的偏移量
                InfoWindow infoWindow =new InfoWindow(bitmapDescriptor,marker.getPosition(),-110,listener);
                //通过百度地图来显示view
                mBaiduMap.showInfoWindow(infoWindow);

                return false;
            }
        });

    }

    public  String  SaveData01(){
        String theSum = " " ;
            for(int k = 0 ; k < holes.length ; k ++){
                if(holes[k]!= null){
                    theSum += "第"+ k +"个洞 "+":  "+holes[k].getRank()+" "+holes[k].getTime()+" "+holes[k].getValue()+"\n"+holes[k].getLatitude()+"  "+holes[k].getLongitude() + "\n\n" ;
                }else{
                    Toast.makeText(TestPoint.this,"完毕",Toast.LENGTH_SHORT).show();
                }
            }
        return  theSum ;
    }

    //数据库存储方法
    public void GetHoles(){
        for(int j = 0 ; j <= 149 ; j++){

            if(holes[0] == null){
                Toast.makeText(TestPoint.this,"没东西存个锤子？",Toast.LENGTH_SHORT).show();
                break;
            }

            //
            if(holes[j] == null  || j == 148){
                Toast.makeText(TestPoint.this,"到达最后的数据点",Toast.LENGTH_SHORT).show();
                break;
            }

            //创建数据库
            dbHelper = new MyDatabaseHelper(this,"HoleDatabase.db",null,1);
            //写入数据预备
            dbHelper.getWritableDatabase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            //将数据放入表的各个属性中
            values.put("rank", holes[j].getRank()); //左字符串为表的属性，右字符串为对应的值
            values.put("time", holes[j] .getTime());
            values.put("value",holes[j] .getValue());
            values.put("Latitude", holes[j] .getLatitude());
            values.put("Longitude", holes[j].getLongitude());

            //插入表
            db.insert("HoleDatabase1", null, values); //holedatabase为数据库表名

        }
    }
}

