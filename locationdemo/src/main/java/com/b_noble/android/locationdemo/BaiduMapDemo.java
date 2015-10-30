package com.b_noble.android.locationdemo;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by mqy on 15/10/30.
 */
public class BaiduMapDemo extends Activity {

    private MapView mMapView = null;
    private BaiduMap bdMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.bdmaplayout);

        mMapView = (MapView) findViewById(R.id.bdmapView);

        bdMap = mMapView.getMap();
        //标注
        bdMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        LatLng point = new LatLng(39.963175, 116.400244);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.point);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        bdMap.addOverlay(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
