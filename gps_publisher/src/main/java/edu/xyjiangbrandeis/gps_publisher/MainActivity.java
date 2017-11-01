package edu.xyjiangbrandeis.gps_publisher;

import android.app.FragmentTransaction;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ros.address.InetAddressFactory;
import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

public class MainActivity extends RosActivity implements GPSNode.NodeLocationCallback, SensorEventListener, OnMapReadyCallback {

    private GPSNode node;
    private TextView latitudeView, longitudeView, countView, accuracyView;
    private int count = 0;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;
    private GoogleMap mMap;
    TextView tvHeading;
//    private MapFragment mMapFragment;

    public MainActivity() {
        super("CameraTutorial", "CameraTutorial");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        node = new GPSNode(this,this);

        latitudeView = (TextView) findViewById(R.id.textView1);
        longitudeView = (TextView) findViewById(R.id.textView2);
        countView = (TextView) findViewById(R.id.textView3);
        accuracyView = (TextView) findViewById(R.id.textView4);

        //Sensor and Textview to display
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //mapfragment code
        //((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        //mapFragment.getMapAsync(this);

        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        NodeConfiguration nodeConfiguration =
                NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(node, nodeConfiguration);
//        handy.post(sizeCheckRunnable);
        Log.d("FUCK","finish init");
    }

    @Override
    public void handleNewLocation(Location location) {
        count ++;
        countView.setText("count: " + count);
        latitudeView.setText("latitude: " + location.getLatitude());
        longitudeView.setText("longitude: " + location.getLongitude());
        accuracyView.setText("accuracy: " + location.getAccuracy());
    }

//    private final class MapReadyCallBack implements OnMapReadyCallback {
//
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//
//        }
//    }
    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
        //Log.d("DEGREES",Float.toString(degree));
        if (tvHeading != null) {
            tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}


