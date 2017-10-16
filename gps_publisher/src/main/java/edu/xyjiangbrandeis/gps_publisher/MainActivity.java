package edu.xyjiangbrandeis.gps_publisher;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.ros.address.InetAddressFactory;
import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

public class MainActivity extends RosActivity {

//    private int cameraId = 0;
//    private RosCameraPreviewView rosCameraPreviewView;
//    private Handler handy = new Handler();
    private GPSNode node;

    public MainActivity() {
        super("CameraTutorial", "CameraTutorial");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
//        rosCameraPreviewView = (RosCameraPreviewView) findViewById(R.id.ros_camera_preview_view);
        node = new GPSNode(this);
        Log.d("FUCK","finish onCreate");
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

}


