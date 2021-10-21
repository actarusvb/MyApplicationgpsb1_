package net.iskranet.app.g1.myapplicationgpsb1;

import static android.hardware.Sensor.TYPE_LIGHT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import java.util.Calendar;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.TextView;

import android.util.Log;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener,SensorEventListener {
    protected LocationManager locationManager;
    TextView textView0, textView1, textView2, textView3, textView4, textView5;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lighEventListener;
    private float maxValue;
    protected boolean haveSensor;
    private int pcount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pcount=0;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(TYPE_LIGHT);

        textView0 = (TextView) findViewById(R.id.textView0);
        textView0.setText(getString(R.string.App_GPSFail));

        if(lightSensor == null){
            haveSensor=false;
            textView4 = (TextView) findViewById(R.id.textView4);
            textView4.setText(getString(R.string.App_NoLightSensor));
        }else{
            haveSensor = true;
            maxValue = lightSensor.getMaximumRange();

            textView4 = (TextView) findViewById(R.id.textView4);
            textView4.setText(getString(R.string.App_WaitLight)+((int) maxValue));

            lighEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvens) {
                    float value = sensorEvens.values[0];
                    int newValue = (int) (255f * value /maxValue);
                    textView4 = (TextView) findViewById(R.id.textView4);
                    textView4.setText(String.format("%f",value)+getString(R.string.App_lux));

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            textView0 = (TextView) findViewById(R.id.textView0);
            textView0.setText(getString(R.string.App_GPSFail));
            return;
        }
        Log.w("Orientetor","I'm starting!");

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // promptEnableGps();
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Orientetor","status");
        Log.w("Orientetor","status");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        textView0 = (TextView) findViewById(R.id.textView0);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        Date currentTime = Calendar.getInstance().getTime();
        textView0.setText(currentTime.toString());
        textView1.setText(getString(R.string.App_latitude) + location.getLatitude() + " ,\n" + getString(R.string.App_longitude) + location.getLongitude());
        textView2.setText(getString(R.string.App_altitude) + String.format("%.2f", location.getAltitude()) + "\n" + getString(R.string.App_AltAccuracy) + location.getVerticalAccuracyMeters());
        // textView2.setText(getString(R.string.App_altitude) + location.getAltitude() + " ,\n" +getString(R.string.App_AltAccuracy) + location.getVerticalAccuracyMeters());
        textView3.setText(getString(R.string.App_speed) + location.getSpeed() + "\n" + getString(R.string.App_Kmh) + location.getSpeed() * 3.6);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Orientetor","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Orientetor","enable");
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(lighEventListener, lightSensor, sensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(lighEventListener);
    }
    public void takeCare(View vview){
        Log.w("Button pushed","Pushed");
        textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setText(getString(R.string.App_pushed) + pcount++);

    }

}
