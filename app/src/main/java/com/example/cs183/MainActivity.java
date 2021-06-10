package com.example.cs183;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.baidu.mapapi.SDKInitializer;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //地图系列
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;

    private boolean isFirstLocate = true;
    private MyLocationConfiguration.LocationMode locationMode;

    private boolean hasEnterTest = false ;

    //标点系列
    private String showText = " " ;
    LatLng point = new LatLng(0,0) ;
    List<OverlayOptions> options = new ArrayList<OverlayOptions>();
    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.drawable.point);

    //经纬度系列
    //private LocationManager locationManager;

    //Button
    private Button enter ;
    private Button history ;
    private Button update ;

    //TextView
    private TextView textView1;
    private TextView textView2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //设置经纬度
        //SetJWD();

        //设置地图
        SetMap();

        //设置按钮
        SetButton();

        //设置TV
        SetTexView();

    }
    // 继承抽象类BDAbstractListener并重写其onReceieveLocation方法来获取定位数据，并将其传给MapView
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

            textView1.setText("经度： " + location.getLatitude());
            textView2.setText("纬度： " + location.getLongitude());
        }
    }

    @Override
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

    public void  SetButton(){
        enter = (Button) findViewById(R.id.AC1_BT_enterTestPoint) ;
        history = (Button) findViewById(R.id.AC1_BT_enterHistory) ;
        update = (Button) findViewById(R.id.AC1_BT_updateData) ;

        //Set Listener
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Enter",Toast.LENGTH_SHORT).show();
                hasEnterTest = true ;
                //跳转
                Intent intent = null ;
                intent = new Intent(MainActivity.this , TestPoint.class);
                startActivity(intent);

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"进入历史记录管理",Toast.LENGTH_SHORT).show();
                Intent intent = null ;
                intent = new Intent(MainActivity.this , History.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //--------------------
                //数据库保存数据的方法，并且还有根据这个数据来画点的办法
                //--------------------
                UpdateData(); //这个只是测试我们的猜想是否可行
            }
        });

    }

    public void  SetTexView(){
        textView1 = (TextView) findViewById(R.id.AC1_TV_01);
        textView2 = (TextView) findViewById(R.id.AC1_TV_02);
    }

    public void  UpdateData(){
        Toast.makeText(MainActivity.this,"正在更新数据",Toast.LENGTH_SHORT).show();
        drawTip(21.11,110.11,bd,"test1");
        drawTip(26,120,bd,"test2") ;
        for(int i = 0 ; i <=30 ; i++){
            drawTip(21+i,110 - i,bd,"loopTest: "+i);
        }

        for(int j = 0 ; j <=30 ; j++){
            drawTip(20-j,100 - j,bd,"loopTest: "+ j);
        }

        Toast.makeText(MainActivity.this,"完成",Toast.LENGTH_SHORT).show();
    }

    public void drawTip(double a,double b,BitmapDescriptor bd,String str) {
        point = new LatLng(a,b);
        showText = str ;
        OverlayOptions option1 =  new MarkerOptions()
                .position(point) //根据每个点的经纬度信息，进行定位
                .icon(bd);
        options.add(option1);
        mBaiduMap.addOverlays(options);
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
                TextView tv = new TextView(MainActivity.this);
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setBackgroundResource(R.drawable.white);
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

}
