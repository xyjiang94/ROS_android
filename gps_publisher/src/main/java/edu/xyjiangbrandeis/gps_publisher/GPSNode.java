package edu.xyjiangbrandeis.gps_publisher;

/**
 * Created by xinyijiang on 10/15/17.
 */

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;

import sensor_msgs.NavSatStatus;
import std_msgs.Float64;

public class GPSNode extends AbstractNodeMain {

    private boolean isReady = false;
    private Publisher<std_msgs.Float64> publisher;
    private Publisher<sensor_msgs.NavSatFix> publisher2;
    private Publisher<sensor_msgs.NavSatStatus> publisher3;
    private LocationProvider mLocationProvider;
    private Context context;

    private double mHeading;

    private NodeLocationCallback callback;

    public GPSNode(Context context, NodeLocationCallback callback) {

        this.callback = callback;
        this.context = context;
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
        publisher = connectedNode.newPublisher("heading", Float64._TYPE);
        publisher2 = connectedNode.newPublisher("gps/fix", sensor_msgs.NavSatFix._TYPE);
        publisher3 = connectedNode.newPublisher("status", sensor_msgs.NavSatStatus._TYPE);
             // This CancellableLoop will be canceled automatically when the node shuts
             // down.
        mLocationProvider = new LocationProvider(context, new PublishingGPSCallback());
        connectedNode.executeCancellableLoop(new CancellableLoop() {
            int count;

            @Override
            protected void setup() {
                isReady = true;
                count = 0;
                mLocationProvider.connect();
            }

            @Override
            protected void loop() throws InterruptedException {
                Log.d("FUCK",count + "");
                count ++;
//                sensor_msgs.NavSatFix msgs = publisher2.newMessage();
//                msgs.setAltitude(0);
//                std_msgs.Float64MultiArray msg = publisher.newMessage();
//                double[] data = {currentLatitude, currentLongitude};
//                msg.setData(data);
//
//                std_msgs.Float64 msg = publisher.newMessage();
//                msg.setData(mHeading);
//                publisher.publish(msg);

                Thread.sleep(1000);

            }
        });
    }

    @Override
    public void onShutdown(Node node){
        mLocationProvider.disconnect();
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("android/gps");
    }



    private final class PublishingGPSCallback implements LocationProvider.LocationCallback {

        public void handleNewLocation(Location location) {
            Log.d("Location", location.toString());

            sensor_msgs.NavSatFix msgs = publisher2.newMessage();
            msgs.setAltitude(location.getAltitude());
            msgs.setLatitude(location.getLatitude());
            msgs.setLongitude(location.getLongitude());

            sensor_msgs.NavSatStatus mstatus = publisher3.newMessage();
            mstatus.setStatus(NavSatStatus.STATUS_FIX);
            mstatus.setService(NavSatStatus.SERVICE_GPS);
            msgs.setStatus(mstatus);

            msgs.setPositionCovarianceType((byte)1);
            double co_value = (float) Math.pow(location.getAccuracy(), 2);
            double[] covariance = {co_value,0,0,0,co_value,0,0,0,co_value};

            msgs.setPositionCovariance(covariance);

            publisher2.publish(msgs);

            callback.handleNewLocation(location);
        }
    }


    public void headingChangedCallback(double degree) {
        mHeading = degree;
        if (isReady) {
            std_msgs.Float64 msg = publisher.newMessage();
            msg.setData(mHeading);
            publisher.publish(msg);
        }
    }


    public abstract interface NodeLocationCallback {
        public void handleNewLocation(Location location);
    }
}
