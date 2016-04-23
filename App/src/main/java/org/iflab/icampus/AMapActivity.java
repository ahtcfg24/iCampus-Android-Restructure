package org.iflab.icampus;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.MyTrafficStyle;


/**
 * AMapV2地图中简单介绍矢量地图和卫星地图模式切换
 */
public class AMapActivity extends AppCompatActivity implements AMapLocationListener, LocationSource {
    private AMap aMap;
    private MapView mapView;
    private int mapType;
    private boolean isShowTraffic = false;


    private static final LatLng JIANXIANGQIAO = new LatLng(39.989088, 116.38193);// 健翔桥经纬度
    private static final LatLng XIAOYING = new LatLng(40.037217, 116.347535);//小营
    private static final LatLng QINGHE = new LatLng(40.044232, 116.342463);//清河
    private static final LatLng JINGTAI = new LatLng(39.919, 116.47);//金台路
    private static final LatLng JIUXIANQIAO = new LatLng(39.963, 116.49);//酒仙桥

    private AMapLocationClient locationClient;//定位器
    private OnLocationChangedListener locationChangedListener;//定位监听器
    private LatLng currentPosition;//当前位置坐标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        setMapType();
        addMarkersToMap();
        setLocation();
    }

    /**
     * 设置地图类型
     */
    private void setMapType() {
        mapType = AMap.MAP_TYPE_NORMAL;
        aMap.setMapType(mapType);
        MyTrafficStyle myTrafficStyle = new MyTrafficStyle();
        myTrafficStyle.setSeriousCongestedColor(0xff92000a);
        myTrafficStyle.setCongestedColor(0xffea0312);
        myTrafficStyle.setSlowColor(0xffff7508);
        myTrafficStyle.setSmoothColor(0xff00a209);
        aMap.setMyTrafficStyle(myTrafficStyle);
    }


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {

        aMap.addMarker(new MarkerOptions().position(JIANXIANGQIAO).title("健翔桥校区").draggable(false));
        aMap.addMarker(new MarkerOptions().position(XIAOYING).title("小营校区").draggable(true));
        aMap.addMarker(new MarkerOptions().position(QINGHE).title("清河校区").draggable(false));
        aMap.addMarker(new MarkerOptions().position(JINGTAI).title("金台路校区").draggable(false));
        aMap.addMarker(new MarkerOptions().position(JIUXIANQIAO).title("酒仙桥校区").draggable(false));
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng clickLatLng = marker.getPosition();
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(clickLatLng, 18));
                marker.showInfoWindow();
                return true;
            }
        });

    }

    /**
     * 配置定位功能
     */
    private void setLocation() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//创建定位中心样式
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);// 自定义定位中心样式
        aMap.setLocationSource(this);//设置定位资源。如果不设置此定位资源则定位按钮不可点击。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);//设置定位类型
    }


    /**
     * 创建工具栏菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_amap, menu);
        return true;
    }

    /**
     * 监听工具栏选项
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.satellite_View://切换到卫星地图
                if (mapType == AMap.MAP_TYPE_NORMAL) {
                    mapType = AMap.MAP_TYPE_SATELLITE;
                    aMap.setMapType(mapType);
                } else {
                    mapType = AMap.MAP_TYPE_NORMAL;
                    aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.traffic_View://切换到交通路况图
                isShowTraffic = !isShowTraffic;
                aMap.setTrafficEnabled(isShowTraffic);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (locationChangedListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                locationChangedListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                if (currentPosition == null) {
                    currentPosition = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    LatLngBounds bounds = new LatLngBounds.Builder().include(JIANXIANGQIAO)
                                                                    .include(XIAOYING)
                                                                    .include(QINGHE)
                                                                    .include(JINGTAI)
                                                                    .include(JIUXIANQIAO)
                                                                    .build();
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Toast.makeText(AMapActivity.this, errText, Toast.LENGTH_SHORT).show();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        locationChangedListener = listener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(this);
            locationClient.setLocationListener(this);//设置定位监听
            AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
            //设置为高精度定位模式
            locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否强制刷新WIFI，默认为强制刷新
            locationClientOption.setWifiActiveScan(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            locationClientOption.setMockEnable(true);
            //设置定位间隔,单位毫秒,默认为2000ms
            locationClientOption.setInterval(3000);
            //设置定位参数
            locationClient.setLocationOption(locationClientOption);
            locationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        locationChangedListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
        }
    }


}
