package edu.xyjiangbrandeis.gps_publisher;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.ros.address.InetAddressFactory;
import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

public class MainActivity extends RosActivity implements GPSNode.NodeLocationCallback {

    private GPSNode node;
    private TextView latitudeView, longitudeView, countView, accuracyView;
    private int count = 0;
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

//        mMapFragment = MapFragment.newInstance();
//        FragmentTransaction fragmentTransaction =
//                getFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.scrollView, mMapFragment);
//        fragmentTransaction.commit();
//
//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(new MapReadyCallBack());

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
}


