package com.b_noble.android.locationdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locationlayout);

        tv = (TextView) findViewById(R.id.locationTextView);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
             Toast.makeText(this, "no Location provider to use", Toast.LENGTH_SHORT).show();
            return;
        }


        Location location = null;

        PackageManager pm = getPackageManager();
        /****/

        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", "com.b_noble.android.locationdemo"));
        if (permission) {
            //showToast("有这个权限");
            Log.d("MainAcitivity","has permission");
        }else {
            //showToast("没有这个权限");
        }


        location = locationManager.getLastKnownLocation(provider);

        while (location == null){
            Log.d("MainActivity.while","OLOL");
            locationManager.requestLocationUpdates(provider,5000,1,listener);
        }

        if(location !=null){
            showLocation(location);
        }

    }

    private void showLocation(Location location){
        String currentPosition = "latitude is:" + location.getLatitude() + ",longitude is:" + location.getLongitude();
        tv.setText(currentPosition);
    }

    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PackageManager pm = getPackageManager();
        /****/

        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", "com.b_noble.android.locationdemo"));
        if (permission) {
            //showToast("有这个权限");
            Log.d("MainAcitivity.Destory","has permission");
        }else {
            //showToast("没有这个权限");
        }
        if(locationManager != null){
            locationManager.removeUpdates(listener);
        }
    }
}
