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

public class GPSNode extends AbstractNodeMain {

    private Node node;
    private Publisher<std_msgs.Float64MultiArray> publisher;
    private LocationProvider mLocationProvider;
    private Context context;
    private double currentLatitude = 0;
    private double currentLongitude = 0;

    public GPSNode(Context context) {
        this.context = context;
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
        publisher = connectedNode.newPublisher("gps_publisher", std_msgs.Float64MultiArray._TYPE);
             // This CancellableLoop will be canceled automatically when the node shuts
             // down.
        mLocationProvider = new LocationProvider(context, new PublishingGPSCallback());
        connectedNode.executeCancellableLoop(new CancellableLoop() {
            int count;

            @Override
            protected void setup() {
                Log.d("FUCK","node setup");
                count = 0;
                mLocationProvider.connect();
            }

            @Override
            protected void loop() throws InterruptedException {
                Log.d("FUCK",count + "");
                count ++;
                std_msgs.Float64MultiArray msg = publisher.newMessage();
                double[] data = {currentLatitude, currentLongitude};
                msg.setData(data);
                publisher.publish(msg);
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

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            std_msgs.Float64MultiArray msg = publisher.newMessage();
            double[] data = {currentLatitude, currentLongitude};
            msg.setData(data);
            publisher.publish(msg);

        }
    }
}
