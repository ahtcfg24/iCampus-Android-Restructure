package org.iflab.icampus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import org.iflab.icampus.application.MyApplication;

import static com.baidu.mapapi.map.BaiduMap.MAP_TYPE_NORMAL;
import static com.baidu.mapapi.map.BaiduMap.MAP_TYPE_SATELLITE;
import static com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;


/**
 * 地图模块
 */
public class MapActivity extends ActionBarActivity {
    private static final LatLng JIANXIANGQIAO = new LatLng(39.994365, 116.387924);// 健翔桥经纬度
    private static final LatLng XIAOYING = new LatLng(40.045694, 116.352887);//清河小营
    private static final LatLng QINGHE = new LatLng(40.049554, 116.347389);//清河
    private static final LatLng JINGTAI = new LatLng(39.927279, 116.478002);//金台路
    private static final LatLng JIUXIOUQIAO = new LatLng(39.970887, 116.49572);//酒仙桥
    private MapView mapView = null;//百度地图控件
    private BaiduMap baiduMap;    // 百度地图对象
    private int mapType;//地图类型
    private boolean isShowTraffic = false;//是不是显示了交通图

    private LocationClient locationClient;//定位所需的Client
    private BDLocationListener locationListener;//位置监听器

    private double longitude;// 精度
    private double latitude;// 维度
    private float radius;// 定位精度半径，单位是米


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        init();
        initMarker();
        initLocation();//初始化定位
    }

    /**
     * 初始化
     */
    private void init() {
        mapView = (MapView) findViewById(R.id.baidu_mapview);
        baiduMap = mapView.getMap();
        mapType = MAP_TYPE_NORMAL;
        baiduMap.setMapType(mapType);//设置地图类型
    }

    /**
     * 初始化标注
     */
    private void initMarker() {
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_position);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions optionJianXiangQiao = new MarkerOptions().position(JIANXIANGQIAO).icon(bitmap).title("健翔桥校区（南门）")
                .draggable(true).visible(true);
        OverlayOptions optionXiaoYing = new MarkerOptions().position(XIAOYING).icon(bitmap).title("清河小营校区（北门）").perspective(true)
                .draggable(true).visible(true);
        OverlayOptions optionQingHe = new MarkerOptions().position(QINGHE).icon(bitmap).title("清河校区").perspective(true)
                .draggable(true).visible(true);
        OverlayOptions optionJinTai = new MarkerOptions().position(JINGTAI).icon(bitmap).title("金台路校区").perspective(true)
                .draggable(true).visible(true);
        OverlayOptions optionJiuXianQiao = new MarkerOptions().position(JIUXIOUQIAO).icon(bitmap).title("酒仙桥校区").perspective(true)
                .draggable(true).visible(true);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(optionJianXiangQiao);
        baiduMap.addOverlay(optionXiaoYing);
        baiduMap.addOverlay(optionQingHe);
        baiduMap.addOverlay(optionJinTai);
        baiduMap.addOverlay(optionJiuXianQiao);

        baiduMap.setOnMarkerClickListener(new MarkerClickListener());//监听marker
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        baiduMap.setMyLocationEnabled(true);//开启定位图层
        locationClient = new LocationClient(MyApplication.getContext());//初始化LocationClient类
        locationListener = new MyLocationListener();//声明LocationListener类
        locationClient.registerLocationListener(locationListener);//注册位置监听函数
        //设置定位参数
        LocationClientOption locOption = new LocationClientOption();
        locOption.setLocationMode(LocationMode.Hight_Accuracy);//设置高精度定位模式
        locOption.setCoorType("bd09ll");// 设置定位结果类型
        locOption.setScanSpan(2000);// 设置发起定位请求的间隔时间,ms
        locationClient.setLocOption(locOption);
        locationClient.start();//启动定位
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        mapView.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.satellite_View://切换到卫星地图
                if (mapType == MAP_TYPE_NORMAL) {
                    mapType = MAP_TYPE_SATELLITE;
                    baiduMap.setMapType(mapType);
                } else {
                    mapType = MAP_TYPE_NORMAL;
                    baiduMap.setMapType(MAP_TYPE_NORMAL);
                }
                break;
            case R.id.traffic_View://切换到交通路况图
                isShowTraffic = !isShowTraffic;
                baiduMap.setTrafficEnabled(isShowTraffic);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示infoWindow
     *
     * @param marker0  当前点击的marker
     * @param textView 要在infoWindow中显示的View
     */
    private void showMarkerInfo(final Marker marker0, TextView textView) {
        textView.setText(marker0.getTitle());
        OnInfoWindowClickListener listener = new OnInfoWindowClickListener() {
            public void onInfoWindowClick() {
                LatLng position = marker0.getPosition();
                //偏移View位置
                LatLng newPosition = new LatLng(position.latitude + 0.005, position.longitude + 0.005);
                marker0.setPosition(newPosition);
                baiduMap.hideInfoWindow();
            }
        };
        LatLng currentPosition = marker0.getPosition();
        InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(textView), currentPosition, -47, listener);
        baiduMap.showInfoWindow(mInfoWindow);

    }

    /**
     * marker的监听器
     */
    private class MarkerClickListener implements OnMarkerClickListener {
        /**
         * marker点击事件
         *
         * @param marker 当前点击的marker对象
         * @return
         */
        @Override
        public boolean onMarkerClick(Marker marker) {
            //创建InfoWindow展示的view
            TextView textView = new TextView(getApplicationContext());
            textView.setBackgroundResource(R.drawable.map_popup);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setGravity(Gravity.CENTER);
            MapStatus mMapStatus = new MapStatus.Builder().target(marker.getPosition())
                    .zoom(12).build();//构造地图即将改变成的状态
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            baiduMap.animateMapStatus(mMapStatusUpdate);//以动画方式更新地图状态
            showMarkerInfo(marker, textView);//显示marker标题
            return false;
        }
    }

    /**
     * 获取到位置的监听器
     */
    private class MyLocationListener implements BDLocationListener {
        /**
         * @param location 当前定位到的位置
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            longitude = location.getLongitude();//获取经度
            latitude = location.getLatitude();//获取纬度
            if (location.hasRadius()) {
                radius = location.getRadius();//获取定位精度半径（如果有）
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(radius)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();// 构造定位数据
            baiduMap.setMyLocationData(locData);// 设置定位数据
            LatLng myLocation = new LatLng(latitude, longitude);//当前位置坐标
            MapStatusUpdate currentStatus = MapStatusUpdateFactory.newLatLng(myLocation);//地图中心点移到当前坐标
            baiduMap.animateMapStatus(currentStatus);//执行更新

        }
    }


}


