package org.iflab.icampus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mapView = (MapView) findViewById(R.id.baidu_mapview);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//设置地图类型
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_position);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions optionJianXiangQiao = new MarkerOptions().position(JIANXIANGQIAO).icon(bitmap);
        OverlayOptions optionXiaoYing = new MarkerOptions().position(XIAOYING).icon(bitmap);
        OverlayOptions optionQingHe = new MarkerOptions().position(QINGHE).icon(bitmap);
        OverlayOptions optionJinTai = new MarkerOptions().position(JINGTAI).icon(bitmap);
        OverlayOptions optionJiuXianQiao = new MarkerOptions().position(JIUXIOUQIAO).icon(bitmap);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(optionJianXiangQiao);
        baiduMap.addOverlay(optionXiaoYing);
        baiduMap.addOverlay(optionQingHe);
        baiduMap.addOverlay(optionJinTai);
        baiduMap.addOverlay(optionJiuXianQiao);


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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
